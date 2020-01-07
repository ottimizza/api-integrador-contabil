package br.com.ottimizza.integradorcloud.controllers.v1;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Value("${oauth2-config.server-url}")
    private String OAUTH2_SERVER_URL;

    @Value("${oauth2-config.client-id}")
    private String OAUTH2_CLIENT_ID;

    @Value("${oauth2-config.client-secret}")
    private String OAUTH2_CLIENT_SECRET;

    @PostMapping("/auth/callback")
    public ResponseEntity<?> oauthCallback(@RequestParam("code") String code,
            @RequestParam("redirect_uri") String redirectUri) throws IOException {
        String credentials = OAUTH2_CLIENT_ID + ":" + OAUTH2_CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            String uri = MessageFormat.format("{0}/oauth/token?grant_type={1}&code={2}&redirect_uri={3}",
                    OAUTH2_SERVER_URL, "authorization_code", code, redirectUri);

            System.out.println("URI ............: " + uri);
            System.out.println("Credentials ....: " + credentials);

            HttpPost httpPost = new HttpPost(uri);
            httpPost.setHeader("Authorization", "Basic " + encodedCredentials);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity = httpResponse.getEntity();

            Integer status = httpResponse.getStatusLine().getStatusCode();
            String response = EntityUtils.toString(responseEntity, "UTF-8");
            System.out.println("Status .........: " + status);
            System.out.println("Response .......: " + response);

            return ResponseEntity.status(status).body(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(401).body("{}");
        }
    }

    @PostMapping(value = "/auth/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> oauthRefresh(@RequestParam("refresh_token") String refreshToken,
            @RequestParam("client_id") String clientId) throws IOException {
        String credentials = OAUTH2_CLIENT_ID + ":" + OAUTH2_CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            URIBuilder uriBuilder = new URIBuilder(OAUTH2_SERVER_URL + "/oauth/token");
            uriBuilder.addParameter("refresh_token", refreshToken);
            uriBuilder.addParameter("grant_type", "refresh_token");
            HttpPost httpPost = new HttpPost(uriBuilder.build());
            httpPost.setHeader("Authorization", "Basic " + encodedCredentials);
            HttpResponse httpResponse = httpClient.execute(httpPost);

            Integer status = httpResponse.getStatusLine().getStatusCode();
            String response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

            return ResponseEntity.status(status).body(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(401).body("{}");
        }
    }

}