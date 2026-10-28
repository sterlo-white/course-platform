
package kz.bitlab.courseplatform.service;

import kz.bitlab.courseplatform.dto.LoginRequest;
import kz.bitlab.courseplatform.dto.TokenResponse;
import kz.bitlab.courseplatform.mapper.AuthMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class AuthService {

    private final RestClient restClient;
    private final AuthMapper authMapper;

    @Value("${keycloak.server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    public AuthService(AuthMapper authMapper) {
        this.restClient = RestClient.create();
        this.authMapper = authMapper;
    }

    public TokenResponse login(LoginRequest request) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("grant_type", "password");
        body.add("username", request.username());
        body.add("password", request.password());

        Map<String, Object> response = restClient.post()
                .uri(keycloakUrl + "/realms/" + realm
                        + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(Map.class);

        return authMapper.toTokenResponse(response);
    }

    public TokenResponse refresh(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);

        Map<String, Object> response = restClient.post()
                .uri(keycloakUrl + "/realms/" + realm
                        + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(Map.class);

        return authMapper.toTokenResponse(response);
    }
}
