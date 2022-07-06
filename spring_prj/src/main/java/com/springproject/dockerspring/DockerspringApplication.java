package com.springproject.dockerspring;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class DockerspringApplication {

	public static void main(String[] args) {
		SpringApplication.run(DockerspringApplication.class, args);
	}


	//これは、フォームクラスの並列処理で使用する設定である。
	@Bean("Form")
	public Executor formThread(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setQueueCapacity(2);
		executor.setMaxPoolSize(100);
		executor.setThreadNamePrefix("Form--");
		executor.initialize();
		return executor;
	}
}
