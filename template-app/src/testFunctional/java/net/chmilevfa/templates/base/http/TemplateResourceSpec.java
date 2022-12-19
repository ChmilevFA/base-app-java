package net.chmilevfa.templates.base.http;

import net.chmilevfa.templates.base.FunctionalSpec;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.assertj.core.api.Assertions.assertThat;

public class TemplateResourceSpec extends FunctionalSpec {

    @Test
    void should_return_successful_GET_response() throws IOException {
        final var request = new HttpGet(serverUri.resolve("/"));

        // when
        final var response = httpClient.execute(request);

        // then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_OK);
        assertThat(EntityUtils.toString(response.getEntity())).isEqualTo("Hello World!");
    }

    @Test
    void should_return_successful_POST_response() throws IOException {
        // given
        final var requestBody = "some string";
        final var request = new HttpPost(serverUri.resolve("/"));
        request.setEntity(new StringEntity(requestBody));

        // when
        final var response = httpClient.execute(request);

        // then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_OK);
        assertThat(EntityUtils.toString(response.getEntity())).isEqualTo("Hello World from post! Body: " + requestBody);
    }

}
