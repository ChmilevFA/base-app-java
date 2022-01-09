package net.chmilevfa.templates.base.http;

import net.chmilevfa.templates.base.FunctionalSpec;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static org.assertj.core.api.Assertions.assertThat;

public class TemplateResourceSpec extends FunctionalSpec {

    @Test
    void should_return_successful_GET_response() {
        // given
        final var request = httpRequestBuilder("/")
            .GET()
            .build();

        // when
        final var response = sendHttp(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HTTP_OK);
        assertThat(response.body()).isEqualTo("Hello World!");
    }

    @Test
    void should_return_successful_POST_response() {
        // given
        final var requestBody = "some string";
        final var request = httpRequestBuilder("/")
            .POST(ofString(requestBody))
            .build();

        // when
        final var response = sendHttp(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HTTP_OK);
        assertThat(response.body()).isEqualTo("Hello World from post! Body: " + requestBody);
    }

}
