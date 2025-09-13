package auth.controllers;

import auth.dtos.JwtAuthenticationResponse;
import auth.dtos.SignInRequest;
import auth.dtos.SignUpRequest;
import auth.services.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthServiceImpl authService;

    @Autowired
    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authService.signIn(request);
    }
}
