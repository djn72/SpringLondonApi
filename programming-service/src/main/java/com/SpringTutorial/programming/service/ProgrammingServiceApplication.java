package com.SpringTutorial.programming.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProgrammingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgrammingServiceApplication.class, args);
	}

}

//Plan to redo the london API test using what i have learned about Springboot
//Inside the product controller have methods that use a rest template to make
//requests to the London API to get users from city and all users

//These controllers will then pass the data needed to the service layer which will determine
//which users are "London" and then store them in the mongo database

//Also have methods, similar to the product ones that will be able to store and get the "London" users
//to and from a local mongo database

//
