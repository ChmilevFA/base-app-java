package net.chmilevfa.templates.base.http;

import net.chmilevfa.templates.base.FunctionalSpec;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.assertj.core.api.Assertions.assertThat;

public class HealthResourceSpec extends FunctionalSpec {

    @Test
    void should_return_successful_healthcheck() throws IOException {
        // given
        final var request = new HttpGet(serverUri.resolve("/healthcheck"));

        // when
        final var response = httpClient().execute(request);

        // then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_OK);
    }

}
