package br.com.ottimizza.integradorcloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.ottimizza.integradorcloud.domain.dtos.WhatsAppNotificationDTO;

@FeignClient(name = "${whatsapp-notification.service.name}", url = "${whatsapp-notification.service.url}")
public interface WhatsAppClient {
    

    @PostMapping("/api/v1/{parceiro}")
    public ResponseEntity<?> enviaMensagem(@PathVariable("parceiro") String parceiro,
                                           @RequestBody WhatsAppNotificationDTO whatsAppNotification);
                                           
}
