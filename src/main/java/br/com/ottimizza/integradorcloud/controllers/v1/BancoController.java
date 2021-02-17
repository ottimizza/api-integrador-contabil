package br.com.ottimizza.integradorcloud.controllers.v1;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.BancoDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.BancoService;

@RestController
@RequestMapping("/api/v1/bancos")
public class BancoController {
	
	@Inject
	BancoService bancoService;
//c
	@PostMapping
	public ResponseEntity<?> salvarBanco(BancoDTO bancoDto, OAuth2Authentication authentication) throws Exception {
		return ResponseEntity.ok(new GenericResponse<BancoDTO> (
				bancoService.salvar(bancoDto, authentication)
			));
	}
//r
	@GetMapping
    public ResponseEntity<?> buscarBancos(@Valid BancoDTO filter, 
                                          @Valid PageCriteria pageCriteria, 
                                           OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericPageableResponse<BancoDTO>(
            bancoService.buscarBancos(filter, pageCriteria, authentication)
        ));
    }
//u
	
//d
}
