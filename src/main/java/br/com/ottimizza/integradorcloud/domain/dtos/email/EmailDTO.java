package br.com.ottimizza.integradorcloud.domain.dtos.email;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailDTO {

	private String to;
	
	private String subject;
	
	private String body;
	
	private String from;
	
	private String cc;
	
	private String cco;
	
	private String replyTo;
	
	private String name;
	
}
