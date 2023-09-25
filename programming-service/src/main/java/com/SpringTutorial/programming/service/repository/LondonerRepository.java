package com.SpringTutorial.programming.service.repository;

import com.SpringTutorial.programming.service.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LondonerRepository extends MongoRepository<Person, String> {

}
