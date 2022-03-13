package net.chmilevfa.templates.base.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;
import java.util.UUID;

@JsonDeserialize(builder = Article.Builder.class)
public class Article extends Entity<Article, UUID, Article.Builder> {

    public final String title;
    public final String body;

    public Article(Builder builder) {
        super(builder);
        this.title = builder.title;
        this.body = builder.body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(title, article.title) && Objects.equals(body, article.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, body);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder extends Entity.Builder<Article, UUID, Builder> {

        private String title;
        private String body;

        public static Builder article() {
            return new Builder();
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
