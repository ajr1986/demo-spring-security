package com.springboot.billing.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.springboot.billing.app.model.service.UploadFileService;

@SpringBootApplication
public class SpringBootBillingAppApplication implements CommandLineRunner {

	@Autowired
	private UploadFileService uploadFileService;

//	@Autowired
//	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBillingAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		uploadFileService.deleteAll();
		uploadFileService.init();

//		The following code is to generate the encrypted passwords
//		
//		String password = "1234";
//		
//		String encode1 = passwordEncoder.encode(password);
//		String encode2 = passwordEncoder.encode(password);
//		System.out.println(encode1);
//		System.out.println(encode2);
//		
//		System.out.println(passwordEncoder.matches("1234", encode1));
//		System.out.println(passwordEncoder.matches("1234", encode1));
	}
}
