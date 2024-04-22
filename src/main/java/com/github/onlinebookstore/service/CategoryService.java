package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.category.CategoryResponseDto;
import com.github.onlinebookstore.dto.category.CreateCategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CreateCategoryRequestDto createCategoryRequestDtos);

    CategoryResponseDto update(Long id, CreateCategoryRequestDto requestDtos);

    CategoryResponseDto deleteById(Long id);
}
