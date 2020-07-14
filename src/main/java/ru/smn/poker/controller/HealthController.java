package ru.smn.poker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/health")
public class HealthController {


    @GetMapping("/check")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.of(Optional.of("OK"));
    }

}
