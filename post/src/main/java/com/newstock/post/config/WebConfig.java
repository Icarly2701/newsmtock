package com.newstock.post.config;

import com.newstock.post.JWTUtil;
import com.newstock.post.web.LoginArgumentResolver;
import com.newstock.post.web.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig  implements WebMvcConfigurer {

    @Value("${aws.bucket-name}")
    private String bucketName;

    private final JWTUtil jwtUtil;

    public WebConfig(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(new LoginCheckInterceptor(jwtUtil))
//                .order(1)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/", "/css/**", "/*.ico", "/error", "/login" , "/logout", "/signup",
//                        "/search/**", "/news/**", "/post/stock", "/image/**", "/post/freeBoard", "/post/*"
//                );
//    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginArgumentResolver(jwtUtil));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/post_image/**")
                .addResourceLocations("http://localhost:4566/" + bucketName + "/post_image/");
    }
}
