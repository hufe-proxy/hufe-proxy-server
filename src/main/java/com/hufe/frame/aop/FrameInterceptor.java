package com.hufe.frame.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hufe.frame.dataobject.bo.AccessLogBO;
import com.hufe.frame.dataobject.vo.common.FrameResponse;
import com.hufe.frame.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

public class FrameInterceptor implements HandlerInterceptor {

    @Autowired
    JedisPool jedisPool;

    @Autowired
    CommonServiceImpl commonService;

    @Value("${validate.ignore}")
    List<String> validateIgnoreAPI;

    private void setResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        out = response.getWriter();
        FrameResponse res = FrameResponse.builder()
                .errorDetail(message)
                .success(false)
                .build();
        out.write(new ObjectMapper().writeValueAsString(res));
        out.flush();
        out.close();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String originalUrl = request.getRequestURI();
        if (validateIgnoreAPI.isEmpty() || !validateIgnoreAPI.contains(originalUrl)) {
            String token = request.getHeader("x-token");
            if (token == null) {
                setResponse(response, "缺失token信息");
                return false;
            }
            try (Jedis jedis = jedisPool.getResource()) {
                String userId = jedis.get(token);
                if (userId != null) {
                    request.setAttribute("userId", userId);
                    return true;
                } else {
                    setResponse(response, "非法访问");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String api = request.getRequestURI();
        String ip = Optional.ofNullable(request.getHeader("x-real-ip")).orElse(request.getRemoteAddr());
        String userId = (String) request.getAttribute("userId");
        AccessLogBO accessLog = AccessLogBO.builder()
                .api(api)
                .ip(ip)
                .userId(userId)
                .build();
        commonService.createAccessLog(accessLog);
    }

}
