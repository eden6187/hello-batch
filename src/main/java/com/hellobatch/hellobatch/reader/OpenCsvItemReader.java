package com.hellobatch.hellobatch.reader;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.BufferedReaderFactory;
import org.springframework.batch.item.file.DefaultBufferedReaderFactory;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Slf4j
public class OpenCsvItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> implements ResourceAwareItemReaderItemStream<T>, InitializingBean {
    private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();
    private Resource resource;

    private CsvToBean<T> csvToBean;
    @Setter
    private BufferedReaderFactory bufferedReaderFactory = new DefaultBufferedReaderFactory();
    private BufferedReader reader;

    private CsvToBeanConfig csvToBeanConfig;
    private Iterator<T> iterableItem;

    @Setter
    private String encoding;
    private final Class<T> type;

    public OpenCsvItemReader(Class<T> type) {
        this.type = type;
        setName(ClassUtils.getShortName(OpenCsvItemReader.class));
    }

    @Override
    public void setResource(Resource resource) {
        this.encoding = DEFAULT_CHARSET;
        this.resource = resource;
        log.info("Resource set {}", resource.getDescription());
    }

    @Override
    protected T doRead(){
        if(this.iterableItem.hasNext()){
            T result = this.iterableItem.next();
            return result;
        }else{
            return null;
        }
    }

    @Override
    protected void doOpen() throws Exception {
        // assert precondition
        Assert.notNull(this.resource, "Input resource must be set");

        if (!this.resource.exists()) {
            throw new IllegalStateException("Input resource must exist :" + this.resource);
        } else if (!this.resource.isReadable()) {
            throw new IllegalStateException("Input resource must be readable : " + this.resource);
        } else {
            this.reader = this.bufferedReaderFactory.create(this.resource, this.encoding);
            this.csvToBean = buildWithConfig(this.csvToBeanConfig);
            this.iterableItem = this.csvToBean.iterator();
        }

    }

    private CsvToBean<T> buildWithConfig(CsvToBeanConfig csvToBeanConfig) {
        CsvToBeanBuilder<T> defaultBuilder = new CsvToBeanBuilder<T>(this.reader)
                .withSkipLines(0)
                .withType(this.type);
        return defaultBuilder.build();
    }

    @Override
    protected void doClose() throws Exception {
        this.reader.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setSaveState(boolean saveState) {
        super.setSaveState(saveState);
    }
}
