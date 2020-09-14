package br.com.ottimizza.integradorcloud.domain.models.checklist;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Converter(autoApply = true)
public class PerguntasOpcoesRespostaConverter implements AttributeConverter<PerguntasOpcoesResposta, String>{

	@Override
	public String convertToDatabaseColumn(PerguntasOpcoesResposta attribute) {
		ObjectMapper mapper = new ObjectMapper();
		String additionalInformation = "{}";
	    if (attribute == null)
	    	return null;
	    try {
	        additionalInformation = mapper.writeValueAsString(attribute);
	    } catch (Exception e) {
	        additionalInformation = "{}";
	    }
	    return additionalInformation;
	}

	@Override
	public PerguntasOpcoesResposta convertToEntityAttribute(String dbData) {
		ObjectMapper mapper = new ObjectMapper();
		PerguntasOpcoesResposta additionalInformation = null;
        if (dbData == null)
            return null;
        try {
            additionalInformation = mapper.readValue(dbData, PerguntasOpcoesResposta.class);
        } catch (Exception e) {
        }
        return additionalInformation;
	}

}
