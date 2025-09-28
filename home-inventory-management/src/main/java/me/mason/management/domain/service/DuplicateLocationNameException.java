package me.mason.management.domain.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateLocationNameException extends RuntimeException {
    public DuplicateLocationNameException(String name) {
        super("이미 존재하는 위치 이름입니다. : " + name);
    }
}
