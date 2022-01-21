package net.chmilevfa.templates.base.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chmilevfa.templates.base.FunctionalSpec;
import net.chmilevfa.templates.base.model.Article;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.util.UUID.randomUUID;
import static net.chmilevfa.templates.base.model.Article.Builder.article;
import static org.assertj.core.api.Assertions.assertThat;

public class ArticleResourceSpec extends FunctionalSpec {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void should_create_article() {
        // given
        var requestBody = "{ \"title\": \"title text\", \"body\": \"body text\"}";
        var request = httpRequestBuilder("/articles")
            .POST(ofString(requestBody))
            .build();

        // when
        var response = sendHttp(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HTTP_CREATED);
        assertThat(response.body()).isNotEmpty();
    }

    @Test
    void should_return_404_if_id_not_exists_while_get_article() {
        // given
        var id = randomUUID();
        var request = httpRequestBuilder("/articles/" + id)
            .GET()
            .build();

        // when
        var response = sendHttp(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HTTP_NOT_FOUND);
    }

    @Test
    void should_return_article() throws JsonProcessingException {
        // given
        var requestBody = "{ \"title\": \"title text\", \"body\": \"body text\"}";
        var createRequest = httpRequestBuilder("/articles")
            .POST(ofString(requestBody))
            .build();
        var id = UUID.fromString(sendHttp(createRequest).body());
        var expectedArticle = article()
            .id(id)
            .title("title text")
            .body("body text")
            .build();

        var request = httpRequestBuilder("/articles/" + id)
            .GET()
            .build();

        // when
        var response = sendHttp(request);

        // then
        var actualArticle = OBJECT_MAPPER.readValue(response.body(), Article.class);
        assertThat(actualArticle).isEqualTo(expectedArticle);
        assertThat(response.statusCode()).isEqualTo(HTTP_OK);
    }

    @Test
    void should_return_all_articles() throws JsonProcessingException {
        // given
        var requestBody1 = "{ \"title\": \"title text1\", \"body\": \"body text1\"}";
        var createRequest1 = httpRequestBuilder("/articles")
            .POST(ofString(requestBody1))
            .build();
        var id1 = UUID.fromString(sendHttp(createRequest1).body());
        var expectedArticle1 = article()
            .id(id1)
            .title("title text1")
            .body("body text1")
            .build();

        var requestBody2 = "{ \"title\": \"title text2\", \"body\": \"body text2\"}";
        var createRequest2 = httpRequestBuilder("/articles")
            .POST(ofString(requestBody2))
            .build();
        var id2 = UUID.fromString(sendHttp(createRequest2).body());
        var expectedArticle2 = article()
            .id(id2)
            .title("title text2")
            .body("body text2")
            .build();
        var expectedArticles = List.of(expectedArticle1, expectedArticle2);

        var request = httpRequestBuilder("/articles")
            .GET()
            .build();

        // when
        var response = sendHttp(request);

        // then
        var actualValues = Arrays.asList(OBJECT_MAPPER.readValue(response.body(), Article[].class));
        assertThat(actualValues).containsAll(expectedArticles);
        assertThat(response.statusCode()).isEqualTo(HTTP_OK);
    }

    @Test
    void should_return_404_if_article_not_exists_while_updating_article() throws JsonProcessingException {
        // given
        var id = randomUUID();
        var article = article()
            .id(id)
            .title("some title")
            .body("some body")
            .build();
        var request = httpRequestBuilder("/articles/" + id)
            .method("PATCH", ofString(OBJECT_MAPPER.writeValueAsString(article)))
            .build();

        // when
        var response = sendHttp(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HTTP_NOT_FOUND);
    }

    @Test
    void should_return_400_if_path_id_and_article_id_dont_match_while_updating_article() throws JsonProcessingException {
        // given
        var createRequest1 = httpRequestBuilder("/articles")
            .POST(ofString("{ \"title\": \"title text1\", \"body\": \"body text1\"}"))
            .build();
        var id = UUID.fromString(sendHttp(createRequest1).body());

        var articleToUpdate = article()
            .id(randomUUID())
            .title("some title")
            .body("some body")
            .build();

        var request = httpRequestBuilder("/articles/" + id)
            .method("PATCH", ofString(OBJECT_MAPPER.writeValueAsString(articleToUpdate)))
            .build();

        // when
        var response = sendHttp(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HTTP_BAD_REQUEST);
    }

    @Test
    void should_update_article() throws JsonProcessingException {
        // given
        var createRequest = httpRequestBuilder("/articles")
            .POST(ofString("{ \"title\": \"title text1\", \"body\": \"body text1\"}"))
            .build();
        var id = UUID.fromString(sendHttp(createRequest).body());

        var articleToUpdate = article()
            .id(id)
            .title("some title")
            .body("some body")
            .build();

        var patchRequest = httpRequestBuilder("/articles/" + id)
            .method("PATCH", ofString(OBJECT_MAPPER.writeValueAsString(articleToUpdate)))
            .build();

        // when
        var patchResponse = sendHttp(patchRequest);

        // then
        assertThat(patchResponse.statusCode()).isEqualTo(HTTP_OK);
        assertThat(patchResponse.body()).isEqualTo(id.toString());

        var getRequest = httpRequestBuilder("/articles/" + id)
            .GET()
            .build();
        var getResponse = sendHttp(getRequest);
        assertThat(getResponse.statusCode()).isEqualTo(HTTP_OK);
        var actualArticle = OBJECT_MAPPER.readValue(getResponse.body(), Article.class);
        assertThat(actualArticle).isEqualTo(articleToUpdate);
    }

    @Test
    void should_return_404_if_article_does_not_exist_while_deleting() {
        // given
        var request = httpRequestBuilder("/articles/" + randomUUID())
            .DELETE()
            .build();

        // when
        var response = sendHttp(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HTTP_NOT_FOUND);
    }

    @Test
    void should_delete_article() {
        // given
        var createRequest = httpRequestBuilder("/articles")
            .POST(ofString("{ \"title\": \"title text1\", \"body\": \"body text1\"}"))
            .build();
        var id = UUID.fromString(sendHttp(createRequest).body());

        var request = httpRequestBuilder("/articles/" + id)
            .DELETE()
            .build();

        // when
        var response = sendHttp(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HTTP_OK);
        var getRequest = httpRequestBuilder("/articles/" + id)
            .GET()
            .build();
        assertThat(sendHttp(getRequest).statusCode()).isEqualTo(HTTP_NOT_FOUND);
    }
}
