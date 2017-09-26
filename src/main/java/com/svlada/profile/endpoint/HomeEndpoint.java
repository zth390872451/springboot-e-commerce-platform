package com.svlada.profile.endpoint;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeEndpoint {


    @GetMapping("/home")
    public ResponseEntity<Object> home(){

        return new ResponseEntity<Object>("home", HttpStatus.OK);
    }



}
