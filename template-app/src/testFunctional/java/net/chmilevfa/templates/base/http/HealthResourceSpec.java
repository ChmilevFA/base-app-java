package net.chmilevfa.templates.base.http;

import net.chmilevfa.templates.base.FunctionalSpec;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.assertj.core.api.Assertions.assertThat;

public class HealthResourceSpec extends FunctionalSpec {

    @Test
    void should_return_successful_healthcheck() {
        // given
        final var request = httpRequestBuilder("/healthcheck")
            .GET()
            .build();

        // when
        final var response = sendHttp(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HTTP_OK);
    }

}
