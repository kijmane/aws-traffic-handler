package org.example.expert.domain.manager.config;

import com.amazonaws.services.s3.AmazonS3;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestS3Config {
    @Bean
    public AmazonS3 amazonS3() {
        return Mockito.mock(AmazonS3.class); // 가짜 객체로 대체
    }
}