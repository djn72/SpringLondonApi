package com.SpringTutorial.programming.service.controller;

import com.SpringTutorial.programming.service.dto.PersonRequest;
import com.SpringTutorial.programming.service.dto.PersonResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.SpringTutorial.programming.service.service.LondonService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//Change to web client, the thing of the future, read up about retries?
//Streams? Lambdas?
@RestController
@RequestMapping("api/London")
@RequiredArgsConstructor
public class LondonController {
    private final String baseApi = "https://london-api.onrender.com/";
    private final WebClient.Builder webClientBuilder;
    ObjectMapper objectMapper = new ObjectMapper();
    private final LondonService londonService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponse> getAllLondonPeople(){



        Mono<List<PersonRequest>> cityList = monoToPersonRequest(getCity("London"));

        Mono<List<PersonRequest>> allPeopleList = monoToPersonRequest(getAllUsers());

        Mono<List<PersonResponse>> londonerList = londonService.findLondonders(cityList,allPeopleList);

        return londonerList.block();
    }

    public Mono<List<PersonRequest>> monoToPersonRequest(Mono<String> mono){
        Flux<PersonRequest> personFlux = mono.flatMapMany( json -> {
            try {
                return Flux.fromArray(objectMapper.readValue(json,PersonRequest[].class));
            } catch (Exception e) {
                return Flux.error(e);
            }
        });
        return personFlux.collectList();

    }

    public Mono<String> getAllUsers(){
        return webClientBuilder.build()
                .get()
                .uri(baseApi + "users")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getCity(String city){
        return webClientBuilder.build()
                .get()
                .uri(baseApi + "city/" + city + "/users" )
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getIndividual(String id){
        return webClientBuilder.build()
                .get()
                .uri(baseApi + "user/" + id)
                .retrieve()
                .bodyToMono(String.class);

    }

}
