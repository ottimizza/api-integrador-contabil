package br.com.ottimizza.integradorcloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.ottimizza.integradorcloud.domain.dtos.email.EmailDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;

@FeignClient(name = "${emailsender.service.name}", url = "${emailsender.service.url}")
public interface EmailSenderClient {

	@PostMapping("/api/v1/emails")
	HttpEntity<GenericResponse<?>> sendMail(@RequestBody EmailDTO mailDto);	
	
}
