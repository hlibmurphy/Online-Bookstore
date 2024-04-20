package com.github.onlinebookstore.services.impl;

import com.github.onlinebookstore.dto.category.CategoryResponseDto;
import com.github.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.github.onlinebookstore.mapper.CategoryMapper;
import com.github.onlinebookstore.model.Category;
import com.github.onlinebookstore.repositories.CategoryRepository;
import com.github.onlinebookstore.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto).toList();
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        Category category =
                categoryRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Cannot find category with id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto save(CreateCategoryRequestDto createCategoryRequestDtos) {
        System.out.println(createCategoryRequestDtos.getName());
        Category category = categoryMapper.toModel(createCategoryRequestDtos);
        System.out.println(category.getName());
        Category savedCategory = categoryRepository.save(category);
        System.out.println(savedCategory.getName());
        System.out.println(categoryMapper.toDto(savedCategory).getName());
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryResponseDto update(Long id, CreateCategoryRequestDto requestDtos) {
        categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Cannot find category with id: " + id));
        Category newCategory = categoryMapper.toModel(requestDtos);
        newCategory.setId(id);
        Category savedCategory = categoryRepository.save(newCategory);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryResponseDto deleteById(Long id) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Failed to delete category. Cannot find category with id: " + id));
        categoryRepository.deleteById(id);
        return categoryMapper.toDto(category);
    }
}
