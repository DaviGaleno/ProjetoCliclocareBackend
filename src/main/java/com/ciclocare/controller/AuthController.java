package com.ciclocare.controller;

import com.ciclocare.dto.request.LoginRequest;
import com.ciclocare.dto.request.RegisterRequest;
import com.ciclocare.dto.response.ApiResponse;
import com.ciclocare.dto.response.LoginResponse;
import com.ciclocare.entity.Usuario;
import com.ciclocare.service.JwtService;
import com.ciclocare.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registrar(@Valid @RequestBody RegisterRequest request) {
        try {
            var usuarioResponse = usuarioService.registrar(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.sucesso("Usuário registrado com sucesso", usuarioResponse));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()
                    )
            );

            Usuario usuario = usuarioService.buscarPorEmail(request.getEmail());
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

            String token = jwtService.gerarToken(userDetails);
            String refreshToken = jwtService.gerarRefreshToken(userDetails);

            LoginResponse loginResponse = LoginResponse.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .usuario(usuarioService.mapToResponse(usuario))
                    .build();

            return ResponseEntity
                    .ok(ApiResponse.sucesso("Login realizado com sucesso", loginResponse));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.erro("Email ou senha inválidos"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String email = jwtService.extrairEmail(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String novoToken = jwtService.gerarToken(userDetails);

            LoginResponse response = LoginResponse.builder()
                    .token(novoToken)
                    .build();

            return ResponseEntity.ok(ApiResponse.sucesso("Token renovado", response));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.erro("Token inválido ou expirado"));
        }
    }
}
