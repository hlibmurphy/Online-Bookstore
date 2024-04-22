package com.github.onlinebookstore.controller;

import com.github.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.github.onlinebookstore.dto.category.CategoryResponseDto;
import com.github.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.github.onlinebookstore.service.BookService;
import com.github.onlinebookstore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private static final int STANDARD_PAGE_SIZE = 10;
    private final BookService bookService;
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(summary = "Get all categories", description = "Get all categories starting from a "
            + "first page with a size of " + STANDARD_PAGE_SIZE)
    public List<CategoryResponseDto> getAll(
            @PageableDefault(page = 0, size = STANDARD_PAGE_SIZE) Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get category by id", description = "Get a category by its id")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}/books")
    @Operation(summary = "Get books by category id", description = "Get books by category id")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id) {
        return bookService.findBooksByCategoryId(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "Create a category", description = "Add a new category")
    public CategoryResponseDto createCategory(@RequestBody CreateCategoryRequestDto requestDtos) {
        return categoryService.save(requestDtos);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category", description = "Fully delete category by its id")
    public CategoryResponseDto updateCategory(@PathVariable Long id,
                                              @Validated @RequestBody
                                              CreateCategoryRequestDto requestDtos) {
        return categoryService.update(id, requestDtos);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a category", description = "Update an existing category")
    public CategoryResponseDto deleteCategory(@PathVariable Long id) {
        return categoryService.deleteById(id);
    }
}
