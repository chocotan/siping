package io.loli.siping.client;

import java.util.List;
import java.util.Map;

public interface ClientRender {

    public String render(ResponseDto<List<Comment>> comments, Map<String, String> additionalContext);
    public String getFileName();
}
