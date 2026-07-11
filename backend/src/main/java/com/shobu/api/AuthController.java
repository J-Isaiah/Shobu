package com.shobu.api;

import com.shobu.api.dto.request.LogInRequest;
import com.shobu.api.dto.request.SignUpRequest;
import com.shobu.api.dto.response.AuthResponse;
import com.shobu.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LogInRequest request) {
        return authService.loginUser(request);
    }

    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody SignUpRequest request) {
        System.out.println(request);
        return authService.signupUser(request);
    }

}
