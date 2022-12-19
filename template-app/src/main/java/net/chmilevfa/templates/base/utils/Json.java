package net.chmilevfa.templates.base.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

public class Json {

    public final static ObjectMapper OBJECT_MAPPER = new ObjectMapper()
        .findAndRegisterModules();

    public static <T> T parse(String jsonText, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(jsonText, type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> List<T> parseList(String jsonText, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(jsonText, OBJECT_MAPPER.getTypeFactory()
                .constructCollectionType(ArrayList.class, type));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String toJsonString(Object element) {
        try {
            return OBJECT_MAPPER.writeValueAsString(element);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
