package br.com.ottimizza.integradorcloud.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class WhatsAppNotificationDTO {

    private String number;

    //Token contem a url do aplicativo que enviara a mensagem, um token do proprio aplicativo e o numero que enviara a mensagem
    private String token;
    
    private String message;

}
