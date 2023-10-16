package com.hellobatch.hellobatch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
@Slf4j
public class SplitExample {

    @Bean
    public Job job(JobRepository jobRepository, Flow splitFlow, Step step4) {
        return new JobBuilder("job", jobRepository)
                .start(splitFlow)
                .next(step4)
                .build()        //builds FlowJobBuilder instance
                .build();       //builds Job instance
    }

    @Bean
    public Flow splitFlow(Flow flow1, Flow flow2, Flow flow3) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                .add(flow1, flow2, flow3)
                .build();
    }

    @Bean
    public Flow flow1(Step step1, Step step2) {
        return new FlowBuilder<SimpleFlow>("flow1")
                .start(step1)
                .next(step2)
                .build();
    }

    @Bean
    public Flow flow2(Step step3) {
        return new FlowBuilder<SimpleFlow>("flow2")
                .start(step3)
                .build();
    }

    @Bean Flow flow3(Step step5){
        return new FlowBuilder<SimpleFlow>("flow3")
                .start(step5)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }


    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step1", jobRepository)
                .tasklet((a,b) -> {
                    for(int i = 0 ; i< 10; i++) {
                        Thread.sleep(1000L);
                        log.info("step1");
                    }
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step2", jobRepository)
                .tasklet((a,b) -> {
                    for(int i = 0 ; i< 10; i++) {
                        Thread.sleep(1000L);
                        log.info("step2");
                    }
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Step step3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step3", jobRepository)
                .tasklet((a,b) -> {
                    for(int i = 0 ; i< 20; i++) {
                        Thread.sleep(1000L);
                        log.info("step3");
                    }
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Step step4(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step4", jobRepository)
                .tasklet((a,b) -> {
                    for(int i = 0 ; i< 3; i++) {
                        Thread.sleep(1000L);
                        log.info("step4");
                    }
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Step step5(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step5", jobRepository)
                .tasklet((a,b) -> {
                    for(int i = 0 ; i< 20; i++) {
                        Thread.sleep(1000L);
                        log.info("step5");
                    }
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

}