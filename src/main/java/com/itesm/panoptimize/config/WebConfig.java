package com.itesm.panoptimize.config;

import com.itesm.panoptimize.util.interceptor.InstanceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final InstanceInterceptor instanceInterceptor;

    public WebConfig(InstanceInterceptor instanceInterceptor) {
        this.instanceInterceptor = instanceInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(instanceInterceptor)
                .addPathPatterns("/dashboard/**");
    }
}
