package com.pawlak.subscription.exception.domain;

import com.pawlak.subscription.exception.base.BusinessException;
import org.springframework.http.HttpStatus;

public class RecordNotFoundException extends BusinessException {
    public RecordNotFoundException() {
        super("Record not found", HttpStatus.NOT_FOUND);
    }
}
