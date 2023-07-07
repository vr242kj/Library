package com.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.core.DefaultHttpLogFormatter;

@Configuration
public class CustomLogbookConfiguration {

    @Bean
    public HttpLogFormatter httpFormatter() {
        return new DefaultHttpLogFormatter();
    }

}
