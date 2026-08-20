package com.diogo.auth_api.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void enviarEmailRecuperacao(String destinatario, String token) {
        // Por enquanto, apenas simulamos o envio imprimindo no console do Docker
        System.out.println("----------------------------------------");
        System.out.println("SIMULACAO DE ENVIO DE E-MAIL");
        System.out.println("Para: " + destinatario);
        System.out.println("Token de Recuperacao: " + token);
        System.out.println("Link simulado: http://localhost:8081/auth/reset-senha?token=" + token);
        System.out.println("----------------------------------------");
    }
}