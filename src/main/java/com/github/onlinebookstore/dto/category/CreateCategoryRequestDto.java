package com.github.onlinebookstore.dto.category;

import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    private String name;
    private String description;
}
