package com.hellobatch.hellobatch.configuration;

import com.hellobatch.hellobatch.domain.ETF;
import com.hellobatch.hellobatch.reader.DynamicMultiResourceItemReader;
import com.hellobatch.hellobatch.reader.OpenCsvItemReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class MergeFileJobConfiguration {

    @Bean
    public Job job1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, DynamicMultiResourceItemReader<ETF> reader) {
        return new JobBuilder("job", jobRepository)
                .start(
                        new StepBuilder("testStep", jobRepository)
                                .tasklet(
                                        (contribution, chunkContext) -> {
                                            ExecutionContext executionContext = chunkContext
                                                    .getStepContext()
                                                    .getStepExecution()
                                                    .getJobExecution()
                                                    .getExecutionContext();

                                            executionContext.put("path", "file:test-data/Data/ETFs/*.txt");
                                            return RepeatStatus.FINISHED;
                                        },
                                        platformTransactionManager
                                )
                                .listener(promotionListener())
                                .build()
                )
                .next(
                        new StepBuilder("testStep2", jobRepository)
                                .<ETF,ETF>chunk(10, platformTransactionManager)
                                .reader(reader)
                                .writer(chunk -> log.info(chunk.toString()))
                                .build()
                )
                .build();
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[] {"path"});
        return listener;
    }

    @Bean
    public DynamicMultiResourceItemReader<ETF> dynamicMultiResourceItemReader(OpenCsvItemReader<ETF> csvItemReader){
        DynamicMultiResourceItemReader<ETF> reader = new DynamicMultiResourceItemReader<>();
        reader.setDelegate(csvItemReader);
        return reader;
    }

    @Bean
    public OpenCsvItemReader<ETF> csvReader(){
        return new OpenCsvItemReader<>(ETF.class);
    }

}
