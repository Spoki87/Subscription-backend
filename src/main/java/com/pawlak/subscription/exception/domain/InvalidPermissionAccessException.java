package com.pawlak.subscription.exception.domain;

import com.pawlak.subscription.exception.base.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidPermissionAccessException extends BusinessException {
    public InvalidPermissionAccessException() {
        super("Invalid permission access", HttpStatus.UNAUTHORIZED);
    }
}
