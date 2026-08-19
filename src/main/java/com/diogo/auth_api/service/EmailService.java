package com.diogo.auth_api.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailRecuperacao(String destinatario, String token) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setTo(destinatario);
            mensagem.setSubject("Recuperação de Senha - Diogo API");
            mensagem.setText("Seu token de recuperação é: " + token);
            mailSender.send(mensagem);
        } catch (Exception e) {
            // Se falhar ao enviar (porque não tem internet/servidor real), ele apenas avisa no console mas não quebra a API
            System.out.println("E-mail simulado. Token para " + destinatario + ": " + token);
        }
    }
}