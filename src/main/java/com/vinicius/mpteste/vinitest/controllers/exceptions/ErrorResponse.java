package com.vinicius.mpteste.vinitest.controllers.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
//o Required args constructor é um construtor apenas para as variaveis final
@RequiredArgsConstructor
//So inclui oque não esta nulo
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final int status;
    private final String message;
    private String stacktrace;
    private List<ValidationError> errors;

    @Getter
    @Setter
    // o Required args constructor é um construtor apenas para as variaveis final
    @RequiredArgsConstructor
    private static class ValidationError {
        private final String field;
        private final String message;
    }

    public void addValidationError(String field, String message){
        if (Objects.isNull(errors)){
            this.errors = new ArrayList<>();
        }
        this.errors.add(new ValidationError(field, message));
    }

    public String toJson(){
        return "{\"status\": " + getStatus() + ", " + "\"message\": \"" + getMessage() + "\"}";
    }
}
