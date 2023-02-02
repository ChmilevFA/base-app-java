package net.chmilevfa.templates.base.http;

import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import net.chmilevfa.templates.action.Actions;
import net.chmilevfa.templates.base.repository.UserRepository;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static java.time.Clock.systemUTC;
import static java.time.LocalDateTime.now;

public class UserResource implements Resource {

    private final UserRepository userRepository;
    private final Actions actions;

    public UserResource(UserRepository userRepository,
                        Actions actions) {
        this.userRepository = userRepository;
        this.actions = actions;
    }

    @Override
    public EndpointGroup routes() {
        return () -> path("/users", () -> {
            post(this::createUser);
        });
    }


    private void createUser(Context context) {
        final var authTokenCreateRequest = Json.parse(context.body(), AuthTokenCreateRequest.class);
        final var user = userRepository.findByPhone(authTokenCreateRequest.phone())
            .orElseGet(() -> createUser(authTokenCreateRequest.phone()));

        final var createdSince = now(systemUTC()).minusMinutes(1);
        final var authToken = authTokenRepository.findInactive(user.id, createdSince)
            .orElseGet(() -> createAuthToken(user.id));

        if (!authToken.activationCodeSent) {
            actions.create(AuthTokenSendActivationCodeAction.class)
                .run(authToken.id);
        }

        context.json(new AuthTokenResponse(authToken.id));
    }
}
