package com.hellobatch.hellobatch.reader;

import com.hellobatch.hellobatch.domain.ETF;
import com.hellobatch.hellobatch.how_to_use.HowToUseOpenCSV;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

@Slf4j
@SpringBootTest(classes = OpenCsvItemReaderTest.Config.class)
public class OpenCsvItemReaderTest {
    @Autowired
    OpenCsvItemReader<ETF> csvItemReader;

    @Configuration
    public static class Config{
        @Bean
        public OpenCsvItemReader<ETF> csvItemReader(){
            OpenCsvItemReader<ETF> reader = new OpenCsvItemReader<>(ETF.class);
            return reader;
        }
    }

    @Test
    public void read() throws Exception {
        Resource resource = new FileSystemResource("/Users/gim-yeonghyeon/dev/IdeaProjects/hello-batch/test-data/Data/ETFs/aadr.us.txt");
        csvItemReader.setResource(resource);
        csvItemReader.open(new ExecutionContext());
        ETF read = csvItemReader.read();
        log.info(read.toString());
    }

    @Test
    public void readMultipleResource() throws IOException {
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        String currentPath = new java.io.File(".").getCanonicalPath();
        log.info(currentPath);
        Resource[] resources = pathMatchingResourcePatternResolver.getResources("file:./test-data/Data/ETFs/*.txt");

        MultiResourceItemReader<ETF> multiResourceItemReader = new MultiResourceItemReader<>();
        multiResourceItemReader.setDelegate(csvItemReader);
        multiResourceItemReader.setResources(resources);
        multiResourceItemReader.open(new ExecutionContext());
        int count = 0;
        try {
            ETF etf;
            do {
                etf = multiResourceItemReader.read();
                count++;
            }while (etf != null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(count);
    }
}
