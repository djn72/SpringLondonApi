package com.SpringTutorial.programming.service.controller;


import com.SpringTutorial.programming.service.dto.PersonRequest;
import com.SpringTutorial.programming.service.dto.PersonResponse;
import com.SpringTutorial.programming.service.service.LondonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class ApiController {
    private final LondonService londonService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody PersonRequest personRequest){
        londonService.addPersonDatabase(personRequest);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponse> getAllProducts(){
        return londonService.getAllPeople();
    }
}
