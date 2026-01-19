package com.ECOM.BUYIT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BuyitApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuyitApplication.class, args);
	}

}
