package io.loli.siping.controller;

import io.loli.siping.entity.Comment;
import io.loli.siping.service.CommentService;
import io.loli.siping.service.SiteService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/comment")
public class CommentApiController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private SiteService siteService;

    private static final Logger logger = LoggerFactory.getLogger(CommentApiController.class);

    @RequestMapping("add")
    @ResponseBody
    public ResponseDto<Object> addComment(String url, String secret, String username, String email, String content,
                                          Long parentId, Long siteId) {
        if (!siteService.validateSecret(siteId, secret)) {
            logger.info("Comment add error: invalid secret, url={}, username={}, content={}", url
                    , username, content);
            return ResponseDto.error("invalid secret");
        }
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUsername(username);
        comment.setEmail(email);
        comment.setParentId(parentId);
        try {
            commentService.addComment(siteId, url, comment);
            logger.info("Comment added, url={}, username={}, content={}", url
                    , username, content);
        } catch (Exception e) {
            logger.info("Comment add error, url={}, username={}, content={}, Exception={}", url
                    , username, content, ExceptionUtils.getStackTrace(e));
            return ResponseDto.error(e.getClass() + ">" + e.getMessage());
        }
        return ResponseDto.ok();
    }

    @RequestMapping("query")
    @ResponseBody
    public ResponseDto<List<Comment>> query(String url, Long siteId, String secret) {
        if (!siteService.validateSecret(siteId, secret)) {
            return ResponseDto.error("invalid secret");
        }
        return ResponseDto.ok(commentService.selectByUrl(url));
    }
}
