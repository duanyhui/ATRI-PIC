package duan.aspect;

import cn.dev33.satoken.stp.StpUtil;
import duan.annotation.RateLimit;
import duan.utils.RedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RateLimitAspect {

    // 这里可以使用Redis来实现限流逻辑
    // 例如，使用Redis的INCR命令来计数，并设置过期时间
    // 如果超过最大请求数，则拒绝请求
    @Autowired
    private RedisUtil redisUtil;

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String satoken  = request.getHeader("satoken");
        String uuid = (String) StpUtil.getExtra(satoken, "uuid");
        String key = "rate_limit:" + uuid;
        // 获取当前请求的次数
        Long count = redisUtil.incr(key, 1);
        // 设置过期时间
        if (count == 1) {
            redisUtil.expire(key, rateLimit.seconds());
        }
        // 如果超过最大请求数，则拒绝请求
        if (count > rateLimit.maxRequests()) {
            throw new RuntimeException("请求过于频繁，请稍后再试");
        }

        return joinPoint.proceed();
    }

}
