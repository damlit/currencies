package com.pocket.currencies.registration;

import com.pocket.currencies.registration.entity.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken token);
    Optional<ConfirmationToken> getToken(String token);
    int setConfirmedAt(String token);
}
