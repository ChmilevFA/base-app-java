package net.chmilevfa.templates.base.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import net.chmilevfa.templates.base.model.Article;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.http.HttpCode.BAD_REQUEST;
import static io.javalin.http.HttpCode.CREATED;
import static io.javalin.http.HttpCode.NOT_FOUND;
import static java.util.UUID.randomUUID;
import static net.chmilevfa.templates.base.model.Article.Builder.article;

public class ArticleResource implements Resource {

    // TODO: move to Provider/Service/Controller
    private final Map<UUID, Article> articles = new HashMap<>();

    // TODO: move to app static
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public EndpointGroup routes() {
        return () -> path("articles", () -> {
            get(this::getAllArticles);
            post(this::createArticle);
            path("{id}", () -> {
                get(this::getArticle);
                patch(this::updateArticle);
                delete(this::deleteArticle);
            });
        });
    }

    private void getAllArticles(Context context) throws JsonProcessingException {
        context.result(OBJECT_MAPPER.writeValueAsString(articles.values()));
    }

    private void createArticle(Context context) throws JsonProcessingException {
        final var jsonNode = OBJECT_MAPPER.readTree(context.body());
        final var title = jsonNode.get("title").asText();
        final var body = jsonNode.get("body").asText();
        final var article = article()
            .id(randomUUID())
            .title(title)
            .body(body)
            .build();
        articles.put(article.id, article);
        context.result(article.id.toString());
        context.status(CREATED);
    }

    private void getArticle(Context context) throws JsonProcessingException {
        final var id = context.pathParamAsClass("id", UUID.class).get();
        if (!articles.containsKey(id)) {
            context.status(NOT_FOUND);
            return;
        }
        context.result(OBJECT_MAPPER.writeValueAsString(articles.get(id)));
    }

    private void updateArticle(Context context) throws JsonProcessingException {
        final var id = context.pathParamAsClass("id", UUID.class).get();
        if (!articles.containsKey(id)) {
            context.status(NOT_FOUND);
            return;
        }
        final var articleToUpdate = OBJECT_MAPPER.readValue(context.body(), Article.class);
        if (!id.equals(articleToUpdate.id)) {
            context.status(BAD_REQUEST);
            return;
        }
        articles.put(articleToUpdate.id, articleToUpdate);
        context.result(articleToUpdate.id.toString());
    }

    private void deleteArticle(Context context) {
        final var id = context.pathParamAsClass("id", UUID.class).get();
        if (!articles.containsKey(id)) {
            context.status(NOT_FOUND);
            return;
        }
        articles.remove(id);
    }
}
