package com.diogo.auth_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Injeta a chave secreta diretamente via Reflection para o teste não depender do application.properties
        ReflectionTestUtils.setField(jwtService, "secretKey", "minha_chave_secreta_super_segura_123_para_testes_com_tamanho_adequado");
    }

    @Test
    void deveGerarEValidarTokenComSucesso() {
        String email = "diogo@teste.com";

        // Gera o token
        String token = jwtService.generateToken(email);

        // Verifica se o token foi gerado
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Extrai o email do token
        String emailExtraido = jwtService.extractUsername(token);

        // Valida se o email extraído é o mesmo
        assertEquals(email, emailExtraido);
    }
}