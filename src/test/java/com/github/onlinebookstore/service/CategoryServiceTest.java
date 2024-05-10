package com.github.onlinebookstore.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.onlinebookstore.dto.category.CategoryResponseDto;
import com.github.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.github.onlinebookstore.mapper.CategoryMapper;
import com.github.onlinebookstore.model.Category;
import com.github.onlinebookstore.repository.CategoryRepository;
import com.github.onlinebookstore.service.impl.CategoryServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void findAllCategories_withPageable_shouldReturnAllCategories() {
        Category category = createTestCategory();
        Page<Category> page = new PageImpl<>(List.of(category));

        when(categoryRepository.findAll(any(Pageable.class)))
                .thenReturn(page);
        CategoryResponseDto categoryResponseDto = mapCategoryToDto(category);
        List<CategoryResponseDto> expected = List.of(categoryResponseDto);
        when(categoryMapper.toDtos(page))
                .thenReturn(expected);
        List<CategoryResponseDto> actual =
                categoryService.findAll(Pageable.unpaged());

        Assertions.assertEquals(actual, expected,
                "The category DTOs retrieved should match the expected category DTOs");
    }

    @Test
    public void getById_withValidId_shouldReturnCategory() {
        Category category = createTestCategory();
        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.of(category));
        CategoryResponseDto expected = mapCategoryToDto(category);
        when(categoryMapper.toDto(category))
                .thenReturn(expected);

        CategoryResponseDto actual = categoryService.getById(1L);
        Assertions.assertEquals(actual, expected,
                "The category DTO retrieved should match the expected category DTO");
    }

    @Test
    public void getById_withInvalidId_shouldThrowException() {
        Long categoryId = 3L;
        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getById(categoryId);
        });
    }

    @Test
    public void save_withValidCategory_shouldSaveCategory() {
        CreateCategoryRequestDto createCategoryRequestDto =
                createTestCategoryRequestDto("Test Category");
        Category testCategory = mapToModel(1L, createCategoryRequestDto);
        when(categoryMapper.toModel(any(CreateCategoryRequestDto.class))).thenReturn(testCategory);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);
        CategoryResponseDto expected = mapCategoryToDto(testCategory);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(expected);

        CategoryResponseDto actual = categoryService.save(createCategoryRequestDto);

        Assertions.assertEquals(expected, actual,
                "The category DTO retrieved should match the expected category DTO");
    }

    @Test
    public void update_withValidId_shouldUpdateCategory() {
        Category category = createTestCategory();
        CreateCategoryRequestDto newCreateCategoryRequestDto =
                createTestCategoryRequestDto("New Category");
        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.of(category));
        when(categoryMapper.toModel(any(CreateCategoryRequestDto.class)))
                .thenReturn(category);
        category.setName(newCreateCategoryRequestDto.getName());
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);
        CategoryResponseDto expected = mapCategoryToDto(category);
        when(categoryMapper.toDto(any(Category.class)))
                .thenReturn(expected);

        CategoryResponseDto actual =
                categoryService.updateCategory(category.getId(), newCreateCategoryRequestDto);

        Assertions.assertEquals(expected, actual,
                "The category DTO retrieved should match the expected category DTO");
    }

    @Test
    public void update_withInvalidId_shouldThrowException() {
        Long categoryId = 6L;

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            CreateCategoryRequestDto createCategoryRequestDto = createTestCategoryRequestDto(
                    "Test Category");
            categoryService.updateCategory(categoryId, createCategoryRequestDto);
        });
    }

    @Test
    public void delete_withValidId_shouldDeleteCategory() {
        Category category = createTestCategory();
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        categoryService.deleteById(category.getId());
        verify(categoryRepository).deleteById(category.getId());
    }

    @Test
    public void delete_withInvalidId_shouldThrowException() {
        Long categoryId = 1L;
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            categoryService.deleteById(categoryId);
        });
    }

    private Category mapToModel(Long id, CreateCategoryRequestDto createCategoryRequestDto) {
        Category category = new Category();
        category.setId(id);
        category.setName(createCategoryRequestDto.getName());
        category.setDescription(createCategoryRequestDto.getDescription());

        return category;
    }

    private CreateCategoryRequestDto createTestCategoryRequestDto(String title) {
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto();
        createCategoryRequestDto.setName(title);
        createCategoryRequestDto.setDescription("Test Category");
        return createCategoryRequestDto;
    }

    private CategoryResponseDto mapCategoryToDto(Category category) {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(category.getId());
        categoryResponseDto.setName(category.getName());
        categoryResponseDto.setDescription(category.getDescription());
        return categoryResponseDto;
    }

    private Category createTestCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setDescription("Test description");

        return category;
    }
}
