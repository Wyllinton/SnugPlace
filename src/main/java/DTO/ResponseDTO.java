package DTO;

public record ResponseDTO<T>(
        boolean error,
        T content
) {
}
