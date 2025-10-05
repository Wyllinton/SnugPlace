package com.snugplace.demo.DTO;

public record ResponseListDTO<T>(
        boolean error,
        String message,
        T data
) {
}
