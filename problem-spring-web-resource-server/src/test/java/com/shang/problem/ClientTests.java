package com.shang.problem;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by shangwei2009@hotmail.com on 2020/12/2 10:46
 */
public class ClientTests {

    private static final Logger log = LoggerFactory.getLogger(ClientTests.class);

    private static final String authServer = "localhost:9904";
    private static final String resourceServer = "localhost:9900";
    private static final String clientId = "client";
    private static final String clientSecret = "secret";
    private static final String grantType = "password";
    private static final String username = "admin";
    private static final String password = "123456";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void loginSuccess() {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(clientId, clientSecret);
        final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", grantType);
        parameters.add("username", username);
        parameters.add("password", password);
        final HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(parameters, httpHeaders);

        try {
            final String result = restTemplate.postForObject(String.format("http://%s/oauth/token", authServer), httpEntity, String.class);
            final Map<String, Object> map = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
            });
            final Object access_token = map.get("access_token");
            if (access_token != null) {
                final String echo = restTemplate.getForObject(String.format("http://%s/echo?access_token=%s", resourceServer, access_token), String.class);
                log.info("echo: {}", echo);
                final String deny = restTemplate.getForObject(String.format("http://%s/deny?access_token=%s", resourceServer, access_token), String.class);
                log.info("deny: {}", deny);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * when login failed, the response can't be modified.
     * It seems that the problem-spring-web project can't resolve {@link OAuth2Exception}.
     */
    @Test
    public void loginFail() {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(clientId, clientSecret);
        final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", grantType);
        parameters.add("username", username);
        // give a wrong password
        parameters.add("password", password + "error");
        final HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(parameters, httpHeaders);

        try {
            final String result = restTemplate.postForObject(String.format("http://%s/oauth/token", authServer), httpEntity, String.class);
            final Map<String, Object> map = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
            });
            final Object access_token = map.get("access_token");
            if (access_token != null) {
                final String echo = restTemplate.getForObject(String.format("http://%s/echo?access_token=%s", resourceServer, access_token), String.class);
                log.info("echo: {}", echo);
                final String deny = restTemplate.getForObject(String.format("http://%s/deny?access_token=%s", resourceServer, access_token), String.class);
                log.info("deny: {}", deny);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
