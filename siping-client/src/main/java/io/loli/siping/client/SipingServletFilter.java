package io.loli.siping.client;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SipingServletFilter implements Filter {
    private SipingClient sipingClient;
    private SipingProperties properties;
    private static final Logger logger = LoggerFactory.getLogger(SipingServletFilter.class);

    public SipingServletFilter(SipingClient client, SipingProperties properties) {
        this.sipingClient = client;
        this.properties = properties;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getMethod().equalsIgnoreCase("post")) {
            String username = request.getParameter("siping_username");
            String email = request.getParameter("siping_email");
            String content = request.getParameter("siping_content");
            String url = properties.getSiteUrl() + request.getRequestURI();
            try {
                ResponseDto<Object> resp = sipingClient.addComment(url, username, email, content);
                if (!resp.getStatus()) {
                    logger.error("Comment add error :{}", resp.getMsg());
                }
            } catch (Exception e) {
                logger.error("Comment add error :{}", ExceptionUtils.getStackTrace(e));
            }
            // save username and email into cookie
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            Cookie usernameCookie = new Cookie("siping_username", username);
            usernameCookie.setMaxAge(2592000);
            response.addCookie(usernameCookie);
            Cookie emailCookie = new Cookie("siping_email", email);
            emailCookie.setMaxAge(2592000);
            response.addCookie(emailCookie);
            response.sendRedirect(request.getRequestURI());
            return;
        } else {
            if (request.getMethod().equalsIgnoreCase("get")) {
                String url = properties.getSiteUrl() + request.getRequestURI();
                try {
                    Map<String, String> context = new HashMap<>();
                    if (request.getCookies() != null) {
                        for (Cookie cookie : request.getCookies()) {
                            if (cookie.getName().equals("siping_username")) {
                                context.put("username", cookie.getValue());

                            }
                            if (cookie.getName().equals("siping_email")) {
                                context.put("email", cookie.getValue());
                            }
                        }
                    }
                    String commentsHtml = sipingClient.getCommentsHtml(url, context);
                    request.setAttribute("sipingComments", commentsHtml);
                } catch (Exception e) {
                    logger.error("Comment fetch error :{}", ExceptionUtils.getStackTrace(e));
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {
    }
}
