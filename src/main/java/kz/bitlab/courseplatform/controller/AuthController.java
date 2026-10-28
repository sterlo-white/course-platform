
package kz.bitlab.courseplatform.controller;

import kz.bitlab.courseplatform.dto.LoginRequest;
import kz.bitlab.courseplatform.dto.TokenResponse;
import kz.bitlab.courseplatform.service.AuthService;
import org.springframework.web.bind.annotation.*;
import kz.bitlab.courseplatform.dto.RefreshRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        System.out.println("AUTH LOGIN CONTROLLER CALLED");
        return authService.login(request);
    }
    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshRequest request) {
        return authService.refresh(request.refreshToken());
    }
}
