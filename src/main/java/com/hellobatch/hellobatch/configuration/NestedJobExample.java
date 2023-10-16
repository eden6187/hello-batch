package com.hellobatch.hellobatch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class NestedJobExample {
    @Bean
    public Job parentJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return null;
    }
}
