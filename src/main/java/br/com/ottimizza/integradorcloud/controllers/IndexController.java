package br.com.ottimizza.integradorcloud.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping
	@RequestMapping("/")
    public String getMethodName() {
        return "{\"api\":\"Api-Integrador is on!\", \"by\":\"Ottimizza\"}";
    }

}
