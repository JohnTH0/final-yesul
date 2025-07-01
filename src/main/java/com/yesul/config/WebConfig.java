package com.yesul.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 정적 파일(이미지, CSS 등) 경로 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 클래스패스에서 정적 파일 위치 지정
        registry.addResourceHandler("/user/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCachePeriod(3600)
                .resourceChain(true);

        registry.addResourceHandler("/community/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCachePeriod(3600)
                .resourceChain(true);

        // 로컬 이미지 업로드 폴더 매핑 (e.g. /images/** → file:upload-dir/)
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:upload-dir/")
                .setCachePeriod(3600)
                .resourceChain(true);

        registry.addResourceHandler("/admin/asserts/**")
                .addResourceLocations("classpath:/static/asserts/")
                .setCachePeriod(3600)
                .resourceChain(true);
    }
}