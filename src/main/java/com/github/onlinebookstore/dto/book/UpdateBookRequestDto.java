package com.github.onlinebookstore.dto.book;

import jakarta.validation.constraints.NotNull;

public record UpdateBookRequestDto(@NotNull int quantity) {
}
