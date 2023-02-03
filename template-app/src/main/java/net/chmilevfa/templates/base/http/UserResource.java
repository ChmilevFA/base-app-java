package net.chmilevfa.templates.base.http;

import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import net.chmilevfa.templates.action.Actions;
import net.chmilevfa.templates.base.action.UserCreateAction;
import net.chmilevfa.templates.base.http.request.UserCreateRequest;
import net.chmilevfa.templates.base.http.response.UserCreateResponse;
import net.chmilevfa.templates.base.utils.Json;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class UserResource implements Resource {

    private final Actions actions;

    public UserResource(Actions actions) {
        this.actions = actions;
    }

    @Override
    public EndpointGroup routes() {
        return () -> path("/users", () -> {
            post(this::createUser);
        });
    }


    private void createUser(Context context) {
        final var createUserRequest = Json.parse(context.body(), UserCreateRequest.class);
        final var user = actions.create(UserCreateAction.class)
            .run(new UserCreateAction.Params(createUserRequest.username(), createUserRequest.password(), createUserRequest.email()));

        context.json(new UserCreateResponse(user.id));
    }
}
