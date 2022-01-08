package net.chmilevfa.templates.base.http;

import io.javalin.apibuilder.EndpointGroup;

public interface Resource {

    EndpointGroup routes();
}
