
package kz.bitlab.courseplatform.controller;

import kz.bitlab.courseplatform.dto.PasswordUpdateRequest;
import kz.bitlab.courseplatform.dto.UserUpdateRequest;
import kz.bitlab.courseplatform.service.KeycloakAdminService;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users/me")
public class UserProfileController {

    private final KeycloakAdminService keycloakAdminService;

    public UserProfileController(
            KeycloakAdminService keycloakAdminService
    ) {
        this.keycloakAdminService = keycloakAdminService;
    }

    @GetMapping
    public Map<String, Object> getProfile(
            JwtAuthenticationToken authentication
    ) {
        String userId = authentication.getToken().getSubject();

        Map<String, Object> user =
                keycloakAdminService.getUserById(userId);

        Map<String, Object> profile = new HashMap<>();
        profile.put("id", user.get("id"));
        profile.put("username", user.get("username"));
        profile.put("email", user.get("email"));
        profile.put("firstName", user.get("firstName"));
        profile.put("lastName", user.get("lastName"));

        return profile;
    }

    @PutMapping
    public Map<String, String> updateProfile(
            JwtAuthenticationToken authentication,
            @RequestBody UserUpdateRequest request
    ) {
        String userId = authentication.getToken().getSubject();

        keycloakAdminService.updateUser(
                userId,
                request.username(),
                request.email(),
                request.firstName(),
                request.lastName()
        );

        return Map.of("message", "Profile updated successfully");
    }

    @PutMapping("/password")
    public Map<String, String> updatePassword(
            JwtAuthenticationToken authentication,
            @RequestBody PasswordUpdateRequest request
    ) {
        String userId = authentication.getToken().getSubject();

        if (request.newPassword() == null
                || request.newPassword().length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters"
            );
        }

        keycloakAdminService.updatePassword(
                userId,
                request.newPassword()
        );

        return Map.of("message", "Password updated successfully");
    }
}
