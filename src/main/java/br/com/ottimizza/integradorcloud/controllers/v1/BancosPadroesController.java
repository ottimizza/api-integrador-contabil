package br.com.ottimizza.integradorcloud.controllers.v1;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.dtos.banco.BancosPadroesDTO;
import br.com.ottimizza.integradorcloud.services.BancosPadroesService;

@RestController
@RequestMapping("/api/v1/bancos_padroes")
public class BancosPadroesController {

    @Inject
    BancosPadroesService service;
    
}
