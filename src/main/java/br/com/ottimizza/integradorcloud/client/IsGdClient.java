package br.com.ottimizza.integradorcloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "IsGdApi", url = "https://is.gd/create.php")
public interface IsGdClient {

    @GetMapping()
    public String shortURL(@RequestParam("format") String format,
                           @RequestParam("url") String  url);
    
}
