package com.cbhb.invocation.invocationdetect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.cbhb.invocation")
public class InvocationDetectApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvocationDetectApplication.class, args);
	}

}
