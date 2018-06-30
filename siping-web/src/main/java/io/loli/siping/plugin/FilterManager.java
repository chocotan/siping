package io.loli.siping.plugin;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FilterManager {
    public Map<String, CommentFilter> filterMap;


    public FilterManager(List<CommentFilter> filters) {
        filterMap = filters.stream().collect(Collectors.toMap(CommentFilter::getName, f -> f));
    }

    public CommentFilter getCommentFilter(String name) {
        return filterMap.get(name);
    }
}
