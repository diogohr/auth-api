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

    // Construtor para injeção de dependências do Spring
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    // 1. Rota de Cadastro
    @PostMapping("/cadastro")
    public ResponseEntity<String> registrar(@RequestBody RegisterDTO request) {
        // Verifica se o e-mail já existe no banco
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Erro: E-mail já está cadastrado!");
        }

        // Cria o novo usuário e criptografa a senha antes de salvar
        User novoUsuario = new User();
        novoUsuario.setEmail(request.email());
        novoUsuario.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(novoUsuario);
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }

    // 2. Rota de Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO request) {
        // Busca o usuário pelo e-mail
        User usuario = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Compara a senha enviada na requisição com o Hash salvo no banco
        if (passwordEncoder.matches(request.password(), usuario.getPassword())) {
            // Se a senha bater, gera e devolve o Token JWT
            String token = jwtService.generateToken(usuario.getEmail());
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    // 3. Rota de Esqueci a Senha
    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> esqueciSenha(@RequestParam String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado"));

        // Gera um token aleatório (UUID) e salva no usuário
        String resetToken = UUID.randomUUID().toString();
        usuario.setResetToken(resetToken);
        userRepository.save(usuario);

        // Chama o serviço de e-mail para enviar a mensagem
        emailService.enviarEmailRecuperacao(usuario.getEmail(), resetToken);

        return ResponseEntity.ok("Instruções de recuperação foram enviadas para o seu e-mail.");
    }

    // 4. Rota para Redefinir a Senha
    @PostMapping("/reset-senha")
    public ResponseEntity<String> resetSenha(@RequestParam String token, @RequestBody ResetSenhaDTO request) {
        // Busca o usuário que tem esse token de recuperação
        User usuario = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido ou já utilizado"));

        // Criptografa a nova senha, salva e "queima" o token de recuperação para não ser usado de novo
        usuario.setPassword(passwordEncoder.encode(request.novaSenha()));
        usuario.setResetToken(null);
        userRepository.save(usuario);

        return ResponseEntity.ok("Senha atualizada com sucesso!");
    }
}