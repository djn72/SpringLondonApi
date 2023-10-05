package com.LondonApiTest.controller;

import com.LondonApiTest.SpringConfig;
import com.LondonApiTest.dto.PersonRequest;
import com.LondonApiTest.dto.PersonResponse;
import com.LondonApiTest.service.LondonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


//Change to web client, the thing of the future, read up about retries?
//Streams? Lambdas?
@RestController
@RequestMapping("api/London")
@RequiredArgsConstructor
public class LondonController {

    private final SpringConfig springConfig;
    private final WebClient.Builder webClientBuilder;
    ObjectMapper objectMapper = new ObjectMapper();
    private final LondonService londonService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponse> getAllLondonPeople(){

        Mono<List<PersonRequest>> cityList = mapJsonToPersonRequest(getCity("London"));

        Mono<List<PersonRequest>> allPeopleList = mapJsonToPersonRequest(getAllUsers());

        Mono<List<PersonResponse>> londonerList = londonService.findLondonders(cityList,allPeopleList);

        return londonerList.block();
    }

    //Maps all the JSON that was sent by the API into PersonRequest objects
    //Using the PersonRequest objects to avoid exposing service to api directly
    public Mono<List<PersonRequest>> mapJsonToPersonRequest(Mono<String> mono){
        Flux<PersonRequest> personFlux = mono.flatMapMany( json -> {
            try {
                return Flux.fromArray(objectMapper.readValue(json,PersonRequest[].class));
            } catch (Exception e) {
                return Flux.error(e);
            }
        });
        return personFlux.collectList();

    }


    //These functions are what call the London Api itself, they return the JSON data stored in a Mono<String>
    public Mono<String> getAllUsers(){
        return webClientBuilder.build()
                .get()
                .uri(springConfig.getBaseApi() + "/users")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getCity(String city){
        return webClientBuilder.build()
                .get()
                .uri(springConfig.getBaseApi() + "/city/" + city + "/users" )
                .retrieve()
                .bodyToMono(String.class);
    }

}
