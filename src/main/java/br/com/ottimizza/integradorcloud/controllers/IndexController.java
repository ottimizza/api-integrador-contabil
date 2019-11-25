package br.com.ottimizza.integradorcloud.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class IndexController {

    @GetMapping(value = { "", "/" })
    public ResponseEntity<?> getMethodName(@RequestParam String param) {
        return ResponseEntity.ok("Welcome Dude!");
    }

}