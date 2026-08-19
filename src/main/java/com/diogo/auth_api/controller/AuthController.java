package com.diogo.auth_api.controller;

import com.diogo.auth_api.dto.LoginDTO;
import com.diogo.auth_api.dto.RegisterDTO;
import com.diogo.auth_api.dto.ResetSenhaDTO;
import com.diogo.auth_api.entity.User;
import com.diogo.auth_api.repository.UserRepository;
import com.diogo.auth_api.service.EmailService;
import com.diogo.auth_api.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> registrar(@RequestBody RegisterDTO request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Erro: E-mail já está cadastrado!");
        }

        User novoUsuario = new User();
        novoUsuario.setEmail(request.email());
        novoUsuario.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(novoUsuario);
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO request) {
        User usuario = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (passwordEncoder.matches(request.password(), usuario.getPassword())) {
            String token = jwtService.generateToken(usuario.getEmail());
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> esqueciSenha(@RequestParam String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado"));

        String resetToken = UUID.randomUUID().toString();
        usuario.setResetToken(resetToken);
        userRepository.save(usuario);

        emailService.enviarEmailRecuperacao(usuario.getEmail(), resetToken);

        return ResponseEntity.ok("Instruções de recuperação foram enviadas para o seu e-mail.");
    }

    @PostMapping("/reset-senha")
    public ResponseEntity<String> resetSenha(@RequestParam String token, @RequestBody ResetSenhaDTO request) {
        User usuario = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido ou já utilizado"));

        usuario.setPassword(passwordEncoder.encode(request.novaSenha()));
        usuario.setResetToken(null);
        userRepository.save(usuario);

        return ResponseEntity.ok("Senha atualizada com sucesso!");
    }
}