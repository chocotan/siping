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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SipingServletFilter implements Filter {
    private SipingClient sipingClient;
    private SipingProperties properties;
    private static final Logger logger = LoggerFactory.getLogger(SipingServletFilter.class);
    private static List<String> spiders = Arrays.asList("bot", "spider", "Spider", "Bot", "curl");

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
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            if (!validateAnswer(request)) {
                logger.error("Invalid answer");
                response.sendRedirect(request.getRequestURI());
                return;
            }
            String url = properties.getSiteUrl() + request.getRequestURI();
            try {
                ResponseDto<Object> resp = sipingClient.addComment(url, username, email, content);
                if (!resp.getStatus()) {
                    logger.error("Comment add error :{}", resp.getMsg());
                    response.sendRedirect(request.getRequestURI());
                    return;
                }
            } catch (Exception e) {
                logger.error("Comment add error :{}", ExceptionUtils.getStackTrace(e));
                response.sendRedirect(request.getRequestURI());
                return;
            }
            // save username and email into cookie
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
                    String[] questions = generateQuestion(request);
                    String question = questions[1];
                    context.put("question", question);
                    String commentsHtml = sipingClient.getCommentsHtml(url, context);
                    request.setAttribute("sipingComments", commentsHtml);
                } catch (Exception e) {
                    logger.error("Comment fetch error :{}", ExceptionUtils.getStackTrace(e));
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private static boolean validateAnswer(HttpServletRequest request) {
        String answer = request.getParameter("siping_answer");
        String answerInSession = (String) request.getSession().getAttribute("answer");
        if (answerInSession != null && answerInSession.equals(answer)) {
            request.getSession().removeAttribute("answer");
            return true;
        }
        return false;
    }

    private static String[] generateQuestion(HttpServletRequest request) {
        String[] op = new String[]{"+", "-", "*"};
        Integer aa = new Random().nextInt(10);
        Integer bb = new Random().nextInt(10);
        String oo = op[new Random().nextInt(3)];
        String question = "";
        String answer = "";
        switch (oo) {
            case "+":
                question = aa + "+" + bb;
                answer = "" + (aa + bb);
                break;
            case "-":
                question = aa + "-" + bb;
                answer = "" + (aa - bb);
                break;
            case "*":
                question = aa + "*" + bb;
                answer = "" + aa * bb;
                break;
        }
        question += "=";
        String ua = request.getHeader("User-Agent");
        if (ua!=null && spiders.stream().noneMatch(ua::contains)) {
            request.getSession().setAttribute("answer", answer);
        }

        String token = UUID.randomUUID().toString();
        return new String[]{token, question, answer};

    }

    @Override
    public void destroy() {
    }
}
