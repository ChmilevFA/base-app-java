package net.chmilevfa.templates.base.http;

import net.chmilevfa.templates.base.FunctionalSpec;
import net.chmilevfa.templates.base.model.Article;
import net.chmilevfa.templates.base.utils.Json;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.UUID.randomUUID;
import static net.chmilevfa.templates.base.model.Article.Builder.article;
import static org.assertj.core.api.Assertions.assertThat;

public class ArticleResourceSpec extends FunctionalSpec {

    @Test
    void should_create_article() throws IOException {
        // given
        final var requestBody = "{ \"title\": \"title text\", \"body\": \"body text\"}";
        final var request = new HttpPost(serverUri.resolve("/articles"));
        request.setEntity(new StringEntity(requestBody));

        // when
        final var response = httpClient.execute(request);

        // then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_CREATED);
        assertThat(EntityUtils.toString(response.getEntity())).isNotEmpty();
    }

    @Test
    void should_return_404_if_id_not_exists_while_get_article() throws IOException {
        // given
        final var request = new HttpGet(serverUri.resolve("/articles/" + randomUUID()));

        // when
        final var response = httpClient.execute(request);

        // then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_NOT_FOUND);
    }

    @Test
    void should_return_article() throws IOException {
        // given
        final var createRequestBody = "{ \"title\": \"title text\", \"body\": \"body text\"}";
        final var createRequest = new HttpPost(serverUri.resolve("/articles"));
        createRequest.setEntity(new StringEntity(createRequestBody));

        final var createResponse = httpClient.execute(createRequest);
        final var id = UUID.fromString(EntityUtils.toString(createResponse.getEntity()));
        final var expectedArticle = article()
            .id(id)
            .title("title text")
            .body("body text")
            .build();

        final var request = new HttpGet(serverUri.resolve("/articles/" + id));

        // when
        final var response = httpClient.execute(request);

        // then
        final var actualArticle = Json.parse(EntityUtils.toString(response.getEntity()), Article.class);
        assertThat(actualArticle).isEqualTo(expectedArticle);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_OK);
    }

    @Test
    void should_return_all_articles() throws IOException {
        // given
        final var createRequestBody1 = "{ \"title\": \"title text1\", \"body\": \"body text1\"}";
        final var createRequest1 = new HttpPost(serverUri.resolve("/articles"));
        createRequest1.setEntity(new StringEntity(createRequestBody1));

        final var createResponse1 = httpClient.execute(createRequest1);
        final var id1 = UUID.fromString(EntityUtils.toString(createResponse1.getEntity()));
        final var expectedArticle1 = article()
            .id(id1)
            .title("title text1")
            .body("body text1")
            .build();

        final var createRequestBody2 = "{ \"title\": \"title text2\", \"body\": \"body text2\"}";
        final var createRequest2 = new HttpPost(serverUri.resolve("/articles"));
        createRequest2.setEntity(new StringEntity(createRequestBody2));

        final var createResponse2 = httpClient.execute(createRequest2);
        final var id2 = UUID.fromString(EntityUtils.toString(createResponse2.getEntity()));
        final var expectedArticle2 = article()
            .id(id2)
            .title("title text2")
            .body("body text2")
            .build();

        final var expectedArticles = List.of(expectedArticle1, expectedArticle2);

        final var request = new HttpGet(serverUri.resolve("/articles"));

        // when
        final var response = httpClient.execute(request);

        // then
        final var actualValues = Json.parseList(EntityUtils.toString(response.getEntity()), Article.class);
        assertThat(actualValues).containsAll(expectedArticles);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_OK);
    }

    @Test
    void should_return_404_if_article_not_exists_while_updating_article() throws IOException {
        // given
        final var id = randomUUID();
        final var article = article()
            .id(id)
            .title("some title")
            .body("some body")
            .build();

        final var request = new HttpPatch(serverUri.resolve("/articles/" + id));
        request.setEntity(new StringEntity(Json.toJsonString(article)));

        // when
        final var response = httpClient.execute(request);

        // then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_NOT_FOUND);
    }

    @Test
    void should_return_400_if_path_id_and_article_id_dont_match_while_updating_article() throws IOException {
        // given
        final var createRequest = new HttpPost(serverUri.resolve("/articles"));
        createRequest.setEntity(new StringEntity("{ \"title\": \"title text1\", \"body\": \"body text1\"}"));

        final var createResponse = httpClient.execute(createRequest);
        final var id = UUID.fromString(EntityUtils.toString(createResponse.getEntity()));

        final var articleToUpdate = article()
            .id(randomUUID())
            .title("some title")
            .body("some body")
            .build();

        final var request = new HttpPatch(serverUri.resolve("/articles/" + id));
        request.setEntity(new StringEntity(Json.toJsonString(articleToUpdate)));

        // when
        final var response = httpClient.execute(request);

        // then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_BAD_REQUEST);
    }

    @Test
    void should_update_article() throws IOException {
        // given
        var createRequest = new HttpPost(serverUri.resolve("/articles"));
        createRequest.setEntity(new StringEntity("{ \"title\": \"title text1\", \"body\": \"body text1\"}"));

        final var createResponse = httpClient.execute(createRequest);
        final var id = UUID.fromString(EntityUtils.toString(createResponse.getEntity()));

        final var articleToUpdate = article()
            .id(id)
            .title("some title")
            .body("some body")
            .build();

        final var request = new HttpPatch(serverUri.resolve("/articles/" + id));
        request.setEntity(new StringEntity(Json.toJsonString(articleToUpdate)));

        // when
        final var response = httpClient.execute(request);

        // then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_OK);
        assertThat(EntityUtils.toString(response.getEntity())).isEqualTo(id.toString());

        final var getRequest = new HttpGet(serverUri.resolve("/articles/" + id));
        final var getResponse = httpClient.execute(getRequest);
        final var actualArticle = Json.parse(EntityUtils.toString(getResponse.getEntity()), Article.class);
        assertThat(actualArticle).isEqualTo(articleToUpdate);
    }

    @Test
    void should_return_404_if_article_does_not_exist_while_deleting() throws IOException {
        // given
        final var request = new HttpDelete(serverUri.resolve("/articles" + randomUUID()));

        // when
        final var response = httpClient.execute(request);

        // then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_NOT_FOUND);
    }

    @Test
    void should_delete_article() throws IOException {
        // given
        var createRequest = new HttpPost(serverUri.resolve("/articles"));
        createRequest.setEntity(new StringEntity("{ \"title\": \"title text1\", \"body\": \"body text1\"}"));

        final var createResponse = httpClient.execute(createRequest);
        final var id = UUID.fromString(EntityUtils.toString(createResponse.getEntity()));

        final var request = new HttpDelete(serverUri.resolve("/articles/" + id));

        // when
        final var response = httpClient.execute(request);

        // then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_OK);
        final var getResponse = httpClient.execute(new HttpGet(serverUri.resolve("/articles/" + id)));
        assertThat(getResponse.getStatusLine().getStatusCode()).isEqualTo(HTTP_NOT_FOUND);
    }
}
