package com.snugplace.demo.DTO;

public record ResponseDTO<T>(
        boolean error,
        T content
)
{
}
