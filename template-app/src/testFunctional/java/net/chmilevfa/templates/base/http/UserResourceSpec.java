package net.chmilevfa.templates.base.http;

import net.chmilevfa.templates.base.FunctionalSpec;
import net.chmilevfa.templates.base.http.request.UserCreateRequest;
import net.chmilevfa.templates.base.http.response.UserCreateResponse;
import net.chmilevfa.templates.base.utils.Json;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.net.HttpURLConnection.HTTP_OK;
import static net.chmilevfa.templates.base.utils.Json.toJsonString;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;

public class UserResourceSpec extends FunctionalSpec {

    @Test
    void should_return_create_action() throws IOException {
        // given
        final var username = randomAlphanumeric(5);
        final var password = randomAlphanumeric(10);
        final var email = randomAlphanumeric(15);

        final var createActionRequest = new UserCreateRequest(username, password, email);

        final var request = new HttpPost(serverUri.resolve("/users"));
        request.setEntity(new StringEntity(toJsonString(createActionRequest)));

        // when
        final var response = httpClient().execute(request);

        // then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_OK);
        final var createActionResponse = Json.parse(EntityUtils.toString(response.getEntity()), UserCreateResponse.class);
        final var savedUser = userRepository.get(createActionResponse.userId());

        assertThat(savedUser.username).isEqualTo(username);
        assertThat(savedUser.password).isEqualTo(password);
        assertThat(savedUser.email).isEqualTo(email);
    }

}
