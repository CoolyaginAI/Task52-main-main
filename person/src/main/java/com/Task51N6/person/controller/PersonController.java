package com.Task51N6.person.controller;


import com.Task51N6.person.model.Person;
import com.Task51N6.person.model.Weather;
import com.Task51N6.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    public RestTemplate restTemplate;

    @GetMapping
    public Iterable<Person> findAll() {
        return personRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Person> findById(@PathVariable int id) {
        return  personRepository.findById(id);
    }

    @PostMapping
    public ResponseEntity<Person> save(@RequestBody Person person) {
        return  personRepository.findById(person.getId()).isPresent()
                ? new ResponseEntity(personRepository.findById(person.getId()), HttpStatus.BAD_REQUEST)
                : new ResponseEntity(personRepository.save(person), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/weather")
    public ResponseEntity<Weather> getWeather(@PathVariable int id) {
        if (personRepository.existsById(id)) {
            String location = personRepository.findById(id).get().getLocation();
            Weather weather = restTemplate.getForObject(
                    "http://localhost:8083/location/weather?name=" + location, Weather.class);
            return new ResponseEntity(weather, HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }


}
