package br.com.ottimizza.integradorcloud.domain.models.roteiro;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Converter(autoApply = true)
public class MapeamentoConverter implements AttributeConverter<Mapeamento, String>{

	@Override
	public String convertToDatabaseColumn(Mapeamento mapeamento) {
		ObjectMapper mapper = new ObjectMapper();
		String additionalInformation = "{}";
	    if (mapeamento == null)
	    	return null;
	    try {
	        additionalInformation = mapper.writeValueAsString(mapeamento);
	    } catch (Exception e) {
	        additionalInformation = "{}";
	    }
	    return additionalInformation;
	}

	@Override
	public Mapeamento convertToEntityAttribute(String dbData) {
        ObjectMapper mapper = new ObjectMapper();
        Mapeamento additionalInformation = null;
        if (dbData == null)
            return null;
        try {
            additionalInformation = mapper.readValue(dbData, Mapeamento.class);
        } catch (Exception e) {
        }
        return additionalInformation;
	}

}
