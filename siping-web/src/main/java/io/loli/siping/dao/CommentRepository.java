package io.loli.siping.dao;

import io.loli.siping.entity.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    @Query("from Comment where article.url=?1")
    List<Comment> findByUrl(String url);
}
