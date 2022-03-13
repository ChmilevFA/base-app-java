package net.chmilevfa.templates.base.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import net.chmilevfa.templates.base.model.Article;
import net.chmilevfa.templates.base.repository.ArticleRepository;

import java.util.UUID;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.patch;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.http.HttpCode.BAD_REQUEST;
import static io.javalin.http.HttpCode.CREATED;
import static io.javalin.http.HttpCode.NOT_FOUND;
import static java.util.UUID.randomUUID;
import static net.chmilevfa.templates.base.model.Article.Builder.article;
import static net.chmilevfa.templates.base.utils.JsonUtils.OBJECT_MAPPER;

public class ArticleResource implements Resource {

    private final ArticleRepository articleRepository;

    public ArticleResource(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

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
        context.result(OBJECT_MAPPER.writeValueAsString(articleRepository.findAll()));
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
        articleRepository.add(article);
        context.result(article.id.toString());
        context.status(CREATED);
    }

    private void getArticle(Context context) throws JsonProcessingException {
        final var id = context.pathParamAsClass("id", UUID.class).get();
        if (!articleRepository.exists(id)) {
            context.status(NOT_FOUND);
            return;
        }
        context.result(OBJECT_MAPPER.writeValueAsString(articleRepository.get(id)));
    }

    private void updateArticle(Context context) throws JsonProcessingException {
        final var id = context.pathParamAsClass("id", UUID.class).get();
        if (!articleRepository.exists(id)) {
            context.status(NOT_FOUND);
            return;
        }
        final var articleToUpdate = OBJECT_MAPPER.readValue(context.body(), Article.class);
        if (!id.equals(articleToUpdate.id)) {
            context.status(BAD_REQUEST);
            return;
        }
        articleRepository.update(articleToUpdate);
        context.result(articleToUpdate.id.toString());
    }

    private void deleteArticle(Context context) {
        final var id = context.pathParamAsClass("id", UUID.class).get();
        if (!articleRepository.exists(id)) {
            context.status(NOT_FOUND);
            return;
        }
        articleRepository.delete(id);
    }
}
