package br.com.ottimizza.integradorcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IntegradorCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegradorCloudApplication.class, args);
	}

}
