package io.loli.siping.service;

import io.loli.siping.dao.ArticleRepository;
import io.loli.siping.dao.CommentRepository;
import io.loli.siping.dao.SiteRepository;
import io.loli.siping.entity.Article;
import io.loli.siping.entity.Comment;
import io.loli.siping.entity.Site;
import io.loli.siping.exception.CommentRejectException;
import io.loli.siping.plugin.FilterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Autowired
    private FilterManager filterManager;


    @Transactional
    public void addComment(Long siteId, String url, Comment comment) {
        comment.setCreateDate(new Date());
        Optional<Site> siteOpt = siteRepository.findById(siteId);
        siteOpt.ifPresent(site -> {
            String plugins = site.getPlugins();
            if (plugins != null) {
                String[] pluginArray = plugins.split(",");
                if (pluginArray.length > 0) {
                    if (Arrays.stream(pluginArray)
                            .map(filterManager::getCommentFilter)
                            .filter(Objects::nonNull).anyMatch(f -> !f.doFilter(comment))) {
                        throw new CommentRejectException("Comment reject");
                    }
                }
            }
        });

        if (!articleRepository.existsByUrl(url)) {
            siteOpt
                    .ifPresent(site -> {
                        Article article = new Article();
                        article.setSite(site);
                        article.setUrl(url);
                        article.setCreateDate(new Date());
                        articleRepository.save(article);
                    });
        }
        articleRepository.findByUrl(url).ifPresent(article -> {
            comment.setArticle(article);

            commentRepository.save(comment);
        });
    }

    public List<Comment> selectByUrl(String url) {
        return commentRepository.findByUrl(url);
    }
}
