package me.mason.management.domain.exception;

public class LocationDoesNotExistException extends RuntimeException {

    public LocationDoesNotExistException() {
        super("존재하지 않는 보관장소입니다");
    }
}
