package net.chmilevfa.templates.base.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;
import java.util.UUID;

@JsonDeserialize(builder = Article.Builder.class)
public class Article {

    public final UUID id;
    public final String title;
    public final String body;

    public Article(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.body = builder.body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id) && Objects.equals(title, article.title) && Objects.equals(body, article.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, body);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {

        UUID id;
        String title;
        String body;

        public static Builder article() {
            return new Builder();
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Article build() {
            return new Article(this);
        }

        private Builder() {
        }
    }
}
