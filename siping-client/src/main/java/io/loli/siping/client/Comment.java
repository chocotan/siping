package io.loli.siping.client;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private Long id;
    private Date createDate;
    private String headerImgUrl;
    private String email;
    private String username;
    private String content;
    private Long parentId;
}
