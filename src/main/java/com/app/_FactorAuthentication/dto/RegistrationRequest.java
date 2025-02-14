package com.app._FactorAuthentication.dto;

public record RegistrationRequest(
        String email,
        String name,
        String password
) {
}
