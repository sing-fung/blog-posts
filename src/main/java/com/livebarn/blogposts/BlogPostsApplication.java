package com.livebarn.blogposts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAsync
public class BlogPostsApplication
{
    @Autowired
    private RestTemplateBuilder builder;

    @Bean
    public RestTemplate restTemplate()
    { return builder.build(); }

    public static void main(String[] args) {
        SpringApplication.run(BlogPostsApplication.class, args);
    }
}
