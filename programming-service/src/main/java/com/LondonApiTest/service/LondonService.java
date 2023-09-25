package com.LondonApiTest.service;


import com.LondonApiTest.dto.PersonResponse;
import com.LondonApiTest.dto.PersonRequest;
import com.LondonApiTest.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LondonService {

    private static final int radius = 50;

    private static final double[] centerCoordsRads = {Math.toRadians(51.5080),Math.toRadians(0.1281)};//[0]lat, [1]long

    private static final double earthRaduisMiles = 3958.8;

    public Person mapToPerson(PersonRequest personRequest){
        return Person.builder()
                .id(personRequest.getId())
                .first_name(personRequest.getFirst_name())
                .last_name(personRequest.getLast_name())
                .email(personRequest.getEmail())
                .ip_address(personRequest.getIp_address())
                .latitude(personRequest.getLatitude())
                .longitude(personRequest.getLongitude())
                .build();
    }

    private PersonResponse mapToPersonResponse(Person person) {
        return PersonResponse.builder()
                .id(person.getId())
                .first_name(person.getFirst_name())
                .last_name(person.getLast_name())
                .email(person.getEmail())
                .ip_address(person.getIp_address())
                .latitude(person.getLatitude())
                .longitude(person.getLongitude())
                .build();
    }
    public Flux<Person> mapToPersonMono(Mono<List<PersonRequest>> people){
        return people.flatMapMany(dataList-> Flux.fromIterable(dataList).map(this::mapToPerson));
    }

    public Mono<List<PersonResponse>> findLondonders(Mono<List<PersonRequest>> listedInCity, Mono<List<PersonRequest>> allPeople){
        Flux<Person> inCityFlux= mapToPersonMono(listedInCity);

        Flux<Person> allPeopleFlux = mapToPersonMono(allPeople);

        Flux<Person> filteredPeopleFlux = allPeopleFlux.filter(person -> withinRadius(person.getLatitude(), person.getLongitude()));

        Flux<PersonResponse> londoners = filteredPeopleFlux.concatWith(inCityFlux).map(this::mapToPersonResponse);

        return londoners.collectList();
    }

    public boolean withinRadius(double userLat, double userLong){
        //This calculation uses the Haversine formula
        userLat = Math.toRadians(userLat);
        userLong = Math.toRadians(userLong);

        double disLat = centerCoordsRads[0] - userLat;
        double disLong = centerCoordsRads[1] - userLong;

        double a = Math.pow(Math.sin(disLat/2),2) + Math.cos(userLat) * Math.cos(centerCoordsRads[0]) * Math.pow(Math.sin(disLong/2),2);
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));

        double distance = earthRaduisMiles * c;

        return distance <= radius;
    }


}
