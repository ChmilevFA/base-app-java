package net.chmilevfa.templates.base.http;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class TemplateResource implements Resource {

    @Override
    public EndpointGroup routes() {
        return () -> path("/", () -> {
            get(ctx -> ctx.result("Hello World!"));
            post(ctx -> ctx.result("Hello World from post!"));
        });
    }
}
