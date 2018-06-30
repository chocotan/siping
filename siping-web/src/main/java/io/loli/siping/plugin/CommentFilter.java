package io.loli.siping.plugin;


import io.loli.siping.entity.Comment;

public interface CommentFilter {
    public boolean doFilter(Comment comment);
    public String getName();
}
