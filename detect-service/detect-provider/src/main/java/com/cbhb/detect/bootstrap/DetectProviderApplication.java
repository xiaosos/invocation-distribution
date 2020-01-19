package com.cbhb.detect.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.cbhb.detect")
@MapperScan("com.cbhb.detect.dal.persistence")
public class DetectProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DetectProviderApplication.class, args);
	}

}
