package io.loli.siping.dao;

import io.loli.siping.entity.Article;
import io.loli.siping.entity.Site;
import org.springframework.data.repository.CrudRepository;

public interface SiteRepository extends CrudRepository<Site, Long> {
}
