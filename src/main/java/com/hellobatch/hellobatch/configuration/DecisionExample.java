package com.hellobatch.hellobatch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
@Slf4j
public class DecisionExample {

    @Bean
    public Job decisionExampleJob(JobRepository jobRepository, Step evenStep, Step oddStep, Step startStep, JobExecutionDecider jobExecutionDecider){
        Job job = new JobBuilder("job", jobRepository)
                .start(startStep)
                .next(jobExecutionDecider)

                .from(jobExecutionDecider).on("ODD").to(oddStep)
                .from(jobExecutionDecider).on("EVEN").to(evenStep)
                .from(jobExecutionDecider).on("END").end()

                .from(evenStep).on("*").to(jobExecutionDecider)
                .from(oddStep).on("*").to(jobExecutionDecider)

                .end()
                .build();

        return job;
    }

    @Bean
    public Step startStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("startStep", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.info("This is START step");
                return RepeatStatus.FINISHED;
            }
        }, platformTransactionManager).build();
    }


    @Bean
    public Step evenStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("evenStep", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.info("This is EVEN step");
                return RepeatStatus.FINISHED;
            }
        }, platformTransactionManager).build();
    }

    @Bean
    public Step oddStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("oddStep", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.info("This is ODD step");
                return RepeatStatus.FINISHED;
            }
        }, platformTransactionManager).build();
    }

    @Component
    public static class OddDecider implements JobExecutionDecider{
        int count = 0;
        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            int result = count % 2;
            count++;

            if (count == 100){
                return new FlowExecutionStatus("END");
            }

            log.info("Decision Made");
            if (result == 0){
                return new FlowExecutionStatus("EVEN");
            }else{
                return new FlowExecutionStatus("ODD");
            }
        }
    }

}
