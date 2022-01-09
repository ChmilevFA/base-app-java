package net.chmilevfa.templates.base.http;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.http.HttpCode.OK;

public class HealthcheckResource implements Resource {

    @Override
    public EndpointGroup routes() {
        return () -> path("/healthcheck",
            () -> get(ctx -> {
                ctx.status(OK);
                ctx.result("OK");
            }));
    }
}
