package com.systech.springsecurity.controller.dto;

public record LoginResponse(String accessToekn, Long expiresIn) {
}
