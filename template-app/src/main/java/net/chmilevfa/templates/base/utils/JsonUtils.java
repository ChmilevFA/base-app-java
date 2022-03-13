package net.chmilevfa.templates.base.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    public final static ObjectMapper OBJECT_MAPPER = new ObjectMapper()
        .findAndRegisterModules();
}
