package br.com.ottimizza.integradorcloud.controllers.v1;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.client.EmailSenderClient;
import br.com.ottimizza.integradorcloud.domain.dtos.EmailDTO;

@RestController
@RequestMapping("/api/v1/email")
public class EmailSendController {
	
	@Inject
	EmailSenderClient emailSenderClient;
	
	@PostMapping
	public ResponseEntity<?> testeEmail(@RequestBody EmailDTO email) throws Exception {
		
		return ResponseEntity.ok(
				emailSenderClient.sendMail(email)
		);
		
	}
	

}
