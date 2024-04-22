package com.github.onlinebookstore.mapper;

import com.github.onlinebookstore.config.MapperConfig;
import com.github.onlinebookstore.dto.category.CategoryResponseDto;
import com.github.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.github.onlinebookstore.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto createCategoryRequestDto);
}
