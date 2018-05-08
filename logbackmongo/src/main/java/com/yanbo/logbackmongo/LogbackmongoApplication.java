package com.yanbo.logbackmongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogbackmongoApplication implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static void main(String[] args) {
        SpringApplication.run(LogbackmongoApplication.class, args);
    }
    @Override
    public void run(String... args)  {
        logger.debug("This is the DEBUG log");
        logger.info("This is the INFO log");
        logger.warn("This is the WARN log");
    }
}
