package com.hellobatch.hellobatch.domain;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvNumber;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public  class ETF{
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
