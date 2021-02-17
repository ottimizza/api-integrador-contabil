package br.com.ottimizza.integradorcloud.utils;

import java.text.MessageFormat;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.client.RestTemplate;

public class ServiceUtils {
	public static String getAuthorizationHeader(OAuth2Authentication authentication) {
        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        String accessToken = details.getTokenValue();
        return MessageFormat.format("Bearer {0}", accessToken);
    }
	
	public static String defaultPatch(String url, String body, String authentication) {
    	RestTemplate template = new RestTemplate();
    	
    	HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    	requestFactory.setConnectTimeout(15000);
    	requestFactory.setReadTimeout(15000);
    	
    	template.setRequestFactory(requestFactory);
    	
    	HttpHeaders headers =  new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.set("Authorization", authentication);
    	
    	return template.patchForObject(url, new HttpEntity<String>(body, headers), String.class);
    }
}
