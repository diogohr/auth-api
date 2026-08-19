package com.diogo.auth_api.dto;

public record RegisterDTO(
        String email,
        String password
) {
}