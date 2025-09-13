package auth.services;

import auth.domains.enums.Role;
import auth.domains.models.Client;
import auth.dtos.JwtAuthenticationResponse;
import auth.dtos.SignInRequest;
import auth.dtos.SignUpRequest;
import auth.jwts.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {
    private final ClientServiceImpl clientService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(ClientServiceImpl clientService, JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.clientService = clientService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var client = Client.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        clientService.create(client);

        var jwt = jwtUtil.generateToken(client);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = clientService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtUtil.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
