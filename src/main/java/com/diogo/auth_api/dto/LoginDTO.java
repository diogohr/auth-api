package com.diogo.auth_api.dto;

public record LoginDTO(
        String email,
        String password
) {
}