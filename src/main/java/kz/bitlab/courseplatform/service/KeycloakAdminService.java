
package kz.bitlab.courseplatform.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import java.util.List;

import java.util.Map;

@Service
public class KeycloakAdminService {

    private final RestClient restClient = RestClient.create();

    @Value("${keycloak.server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-realm:master}")
    private String adminRealm;

    @Value("${keycloak.admin-client-id:admin-cli}")
    private String adminClientId;

    @Value("${keycloak.admin-username}")
    private String adminUsername;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    private String getAdminToken() {
        MultiValueMap<String, String> body =
                new LinkedMultiValueMap<>();

        body.add("client_id", adminClientId);
        body.add("grant_type", "password");
        body.add("username", adminUsername);
        body.add("password", adminPassword);

        Map response = restClient.post()
                .uri(keycloakUrl + "/realms/" + adminRealm
                        + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(Map.class);

        return (String) response.get("access_token");
    }

    public void createUser(String username, String email, String password) {
        String token = getAdminToken();

        Map<String, Object> credential = Map.of(
                "type", "password",
                "value", password,
                "temporary", false
        );

        Map<String, Object> user = Map.of(
                "username", username,
                "email", email,
                "enabled", true,
                "credentials", java.util.List.of(credential)
        );

        restClient.post()
                .uri(keycloakUrl + "/admin/realms/" + realm + "/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .retrieve()
                .toBodilessEntity();
    }

    public void assignRealmRole(String username, String roleName) {
        String token = getAdminToken();

        List<Map<String, Object>> users = restClient.get()
                .uri(keycloakUrl + "/admin/realms/" + realm
                        + "/users?username=" + username + "&exact=true")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(List.class);

        if (users == null || users.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        String userId = (String) users.get(0).get("id");

        Map<String, Object> role = restClient.get()
                .uri(keycloakUrl + "/admin/realms/" + realm
                        + "/roles/" + roleName)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(Map.class);

        restClient.post()
                .uri(keycloakUrl + "/admin/realms/" + realm
                        + "/users/" + userId + "/role-mappings/realm")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(List.of(role))
                .retrieve()
                .toBodilessEntity();
    }

    public Map<String, Object> getUserById(String userId) {
        String token = getAdminToken();

        return restClient.get()
                .uri(keycloakUrl + "/admin/realms/" + realm
                        + "/users/" + userId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(Map.class);
    }

    public void updateUser(
            String userId,
            String username,
            String email,
            String firstName,
            String lastName
    ) {
        String token = getAdminToken();

        Map<String, Object> user = new java.util.HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);

        restClient.put()
                .uri(keycloakUrl + "/admin/realms/" + realm
                        + "/users/" + userId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .retrieve()
                .toBodilessEntity();
    }

    public void updatePassword(String userId, String newPassword) {
        String token = getAdminToken();

        Map<String, Object> credential = Map.of(
                "type", "password",
                "value", newPassword,
                "temporary", false
        );

        restClient.put()
                .uri(keycloakUrl + "/admin/realms/" + realm
                        + "/users/" + userId + "/reset-password")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(credential)
                .retrieve()
                .toBodilessEntity();
    }



}
