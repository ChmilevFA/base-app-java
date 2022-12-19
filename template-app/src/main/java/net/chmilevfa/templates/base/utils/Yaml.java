package net.chmilevfa.templates.base.utils;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;

import static net.chmilevfa.templates.base.utils.Json.OBJECT_MAPPER;

public final class Yaml {

    public static <T> T parse(URL configLocation, Class<T> type) {
        try {
            final var parser = new YAMLFactory().createParser(configLocation);
            return OBJECT_MAPPER.readValue(parser, type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
