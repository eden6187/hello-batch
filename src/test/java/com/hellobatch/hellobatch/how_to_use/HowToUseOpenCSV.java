package com.hellobatch.hellobatch.how_to_use;

import com.opencsv.bean.*;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.time.LocalDate;

@Slf4j
@SpringBootTest
public class HowToUseOpenCSV {

    @ToString
    public static class ETF{
        @CsvBindByName(column = "Date", required = true)
        @CsvDate("yyyy-MM-dd")
        private LocalDate date;

        @CsvBindByName(column = "Open", required = true)
        @CsvNumber("#")
        private Double open;

        @CsvBindByName(column = "High", required = true)
        @CsvNumber("#")
        private Double high;

        @CsvBindByName(column = "Low", required = true)
        @CsvNumber("#")
        private Double low;

        @CsvBindByName(column = "Close", required = true)
        @CsvNumber("#")
        private Double close;

        @CsvBindByName(column = "Volume", required = true)
        @CsvNumber("#")
        private Double volume;

        @CsvBindByName(column = "OpenInt", required = true)
        @CsvNumber("#")
        private Integer openInt;
    }

    @Test
    public void test(){
        try (Reader inputReader = new InputStreamReader(new FileInputStream(
                new File("src/test/resources/test-data/aadr.us.txt")), "UTF-8")) {

            CsvToBean<ETF> build = new CsvToBeanBuilder<ETF>(inputReader)
                    .withType(ETF.class)
                    .withSeparator(',')
                    .build();

            ETF next = build.iterator().next();

            log.info(next.toString());

        } catch (IOException e) {
            // handle exception
        }
    }
}
