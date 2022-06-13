package com.app.fileprocess;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class FileProcessApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(FileProcessApplication.class, args);
	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FileProcessApplication.class);
    }

	@Bean
	public Executor taskExecutor() {
	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	    executor.setCorePoolSize(3);
	    executor.setMaxPoolSize(3);
	    executor.setQueueCapacity(500);
	    executor.setThreadNamePrefix("FileProcessorThread-");
	    executor.initialize();
	    return executor;
	}
}
