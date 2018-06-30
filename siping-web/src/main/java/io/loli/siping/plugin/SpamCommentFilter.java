package io.loli.siping.plugin;

import io.loli.siping.entity.Comment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpamCommentFilter implements CommentFilter {
    private List<String> keyword = new ArrayList<>();

    @Scheduled(fixedRate = 1000000)
    @PostConstruct
    public void reload() {
        try {
            keyword = FileUtils.readLines(new File("/tmp/spam.txt"), Charset.forName("UTF-8"));
            keyword = keyword.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        } catch (IOException ignored) {
            // ignored
        }
    }

    @Override
    public boolean doFilter(Comment comment) {
        if (keyword.size() == 0) {
            return true;
        }
        return keyword.stream().noneMatch(str -> comment.getContent().contains(str));
    }

    @Override
    public String getName() {
        return "simpleSpam";
    }
}
