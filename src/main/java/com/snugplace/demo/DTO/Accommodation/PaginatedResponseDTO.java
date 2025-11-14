package com.snugplace.demo.DTO.Accommodation;


import com.fasterxml.jackson.annotation.JsonProperty;

public record PaginatedResponseDTO<T>(
        @JsonProperty("error") boolean error,
        @JsonProperty("message") String message,
        @JsonProperty("data") T data,
        @JsonProperty("totalElements") long totalElements,
        @JsonProperty("totalPages") int totalPages,
        @JsonProperty("currentPage") int currentPage,
        @JsonProperty("size") int size
) {
    // Constructor para respuestas exitosas
    public static <T> PaginatedResponseDTO<T> success(String message, T data,
                                                      long totalElements, int totalPages,
                                                      int currentPage, int size) {
        return new PaginatedResponseDTO<>(false, message, data, totalElements, totalPages, currentPage, size);
    }

    // Constructor para respuestas de error
    public static <T> PaginatedResponseDTO<T> error(String message) {
        return new PaginatedResponseDTO<>(true, message, null, 0, 0, 0, 0);
    }
}

