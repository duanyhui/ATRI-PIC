package duan.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    int seconds() default 10; // 限流时间窗口，单位秒
    int maxRequests() default 50; // 限流请求数
}
