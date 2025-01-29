package com.app._FactorAuthentication.dto;

import com.app._FactorAuthentication.entity.User;

public record AuthJwtResponse(
        String accessToken,
        String refreshToken,
        User user
) {
}
