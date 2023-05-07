package duan.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HeaderInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<String> SATOKEN_THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String satoken = request.getHeader("satoken");
        SATOKEN_THREAD_LOCAL.set(satoken);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SATOKEN_THREAD_LOCAL.remove();
    }

    public static String getSatoken() {
        return SATOKEN_THREAD_LOCAL.get();
    }
}
