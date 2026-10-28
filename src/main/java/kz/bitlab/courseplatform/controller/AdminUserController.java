
package kz.bitlab.courseplatform.controller;

import kz.bitlab.courseplatform.dto.UserCreateRequest;
import kz.bitlab.courseplatform.service.KeycloakAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final KeycloakAdminService keycloakAdminService;

    public AdminUserController(KeycloakAdminService keycloakAdminService) {
        this.keycloakAdminService = keycloakAdminService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> createUser(
            @RequestBody UserCreateRequest request) {

        keycloakAdminService.createUser(
                request.username(),
                request.email(),
                request.password()
        );
        keycloakAdminService.assignRealmRole(
                request.username(),
                request.role()
        );


        return Map.of("message", "User created successfully");
    }
}
