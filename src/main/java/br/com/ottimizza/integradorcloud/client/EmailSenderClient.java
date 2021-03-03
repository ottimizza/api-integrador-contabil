package br.com.ottimizza.integradorcloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.ottimizza.integradorcloud.domain.dtos.EmailDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;

@FeignClient(name = "${email-sender.service.name}", url = "${email-sender.service.url}")
public interface EmailSenderClient {

	@PostMapping("/api/v1/emails")
	HttpEntity<GenericResponse<?>> sendMail(@RequestBody EmailDTO mailDto);	
	
}
