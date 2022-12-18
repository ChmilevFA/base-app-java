package net.chmilevfa.templates.base.repository;

import net.chmilevfa.templates.base.db.schema.tables.records.ArticlesRecord;
import net.chmilevfa.templates.base.model.Article;
import net.chmilevfa.templates.repository.SimpleJooqRepository;

import javax.sql.DataSource;
import java.util.UUID;

import static net.chmilevfa.templates.base.db.schema.tables.Articles.ARTICLES;
import static net.chmilevfa.templates.base.model.Article.Builder.article;

public class ArticleRepository extends SimpleJooqRepository<Article, UUID, ArticlesRecord> {

    public ArticleRepository(DataSource dataSource) {
        super(dataSource, ARTICLES, ARTICLES.ID);
    }

    @Override
    protected Article fromRecord(ArticlesRecord record) {
        return article()
            .id(record.getId())
            .title(record.getTitle())
            .body(record.getBody())
            .createdDate(record.getCreatedDate())
            .updatedDate(record.getUpdatedDate())
            .build();
    }

    @Override
    protected ArticlesRecord toRecord(Article entity) {
        return new ArticlesRecord(
            entity.id,
            entity.title,
            entity.body,
            entity.createdDate,
            entity.updatedDate);
    }
}
