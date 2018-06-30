package io.loli.siping.dao;

import io.loli.siping.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface  ArticleRepository  extends CrudRepository<Article,Long> {
    public boolean existsByUrl(String url);

    Optional<Article> findByUrl(String url);
}
