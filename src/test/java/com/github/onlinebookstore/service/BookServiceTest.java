package com.github.onlinebookstore.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.onlinebookstore.dto.book.BookDto;
import com.github.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.github.onlinebookstore.dto.book.BookSearchParameters;
import com.github.onlinebookstore.dto.book.CreateBookRequestDto;
import com.github.onlinebookstore.mapper.BookMapper;
import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.model.Category;
import com.github.onlinebookstore.repository.BookRepository;
import com.github.onlinebookstore.repository.impl.BookSpecificationBuilder;
import com.github.onlinebookstore.service.impl.BookServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final long STANDARD_CATEGORY_ID = 1;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void save_withRequestDto_shouldSaveBook() {
        CreateBookRequestDto createBookRequestDto = createTestBookRequestDto(
                "Book DTO",
                BigDecimal.valueOf(25)
        );

        Book book = mapToTestModel(createBookRequestDto);
        when(bookMapper.toModel(any(CreateBookRequestDto.class))).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto expected = mapToTestDto(book);
        when(bookMapper.toDto(any(Book.class))).thenReturn(expected);

        BookDto actual = bookService.save(createBookRequestDto);

        Assertions.assertEquals(expected, actual,
                "The book DTO retrieved should match the expected book DTO");
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    public void findAllBooks_withPageable_shouldReturnAllBooks() {
        Book firstBook = createTestBook(1L, "First Book");
        Book secondBook = createTestBook(2L, "Second Book");
        List<Book> books = List.of(firstBook, secondBook);
        Page<Book> bookPage = new PageImpl<>(books);

        BookDto firstBookDto = mapToTestDto(firstBook);
        BookDto secondBookDto = mapToTestDto(secondBook);
        List<BookDto> expected = List.of(firstBookDto, secondBookDto);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
        when(bookMapper.toDtos(bookPage)).thenReturn(expected);

        List<BookDto> actual = bookService.findAll(Pageable.ofSize(10));

        Assertions.assertEquals(expected, actual,
                "The book DTOs retrieved should match the expected book DTOs");
    }

    @Test
    public void findBookById_withValidBookId_shouldReturnBook() {
        Book book = createTestBook(1L, "Book");
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        BookDto expected = mapToTestDto(book);
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.findBookById(book.getId());

        Assertions.assertEquals(expected, actual,
                "The book DTO retrieved should match the expected book DTO");
        verify(bookRepository).findById(book.getId());
    }

    @Test
    public void findBookById_withInvalidBookId_shouldThrowException() {
        Long invalidBookId = 3L;
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bookService.findBookById(invalidBookId);
        });

        verify(bookRepository).findById(invalidBookId);
    }

    @Test
    public void findBooksByCategoryId_withValidCategoryId_shouldReturnBooks() {
        Book firstBook = createTestBook(1L, "First Book");
        Book secondBook = createTestBook(2L, "Second Book");
        Long categoryId = STANDARD_CATEGORY_ID;

        when(bookRepository.findBooksByCategoriesId(anyLong()))
                .thenReturn(List.of(firstBook, secondBook));

        BookDtoWithoutCategoryIds firstBookDto = createTestBookDtoWithoutCategories(firstBook);
        BookDtoWithoutCategoryIds secondBookDto = createTestBookDtoWithoutCategories(secondBook);
        List<BookDtoWithoutCategoryIds> expected = List.of(firstBookDto, secondBookDto);
        when(bookMapper.toDtosWithoutCategories(List.of(firstBook, secondBook)))
                .thenReturn(expected);

        List<BookDtoWithoutCategoryIds> actual =
                bookService.findBooksByCategoryId(categoryId);

        Assertions.assertEquals(expected, actual,
                "The book DTOs retrieved should match the expected book DTOs");
        verify(bookRepository).findBooksByCategoriesId(categoryId);
    }

    @Test
    public void deleteBookById_withValidBookId_shouldDeleteBook() {
        Book book = createTestBook(1L, "Test Book");
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        BookDto expected = mapToTestDto(book);
        when(bookMapper.toDto(any(Book.class))).thenReturn(expected);
        BookDto actual = bookService.deleteById(book.getId());

        Assertions.assertEquals(expected, actual,
                "The book DTO retrieved should match te expected book DTO");
        verify(bookRepository).deleteById(book.getId());
    }

    @Test
    public void deleteBookById_withInvalidBookId_shouldThrowException() {
        Long bookId = 1L;
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bookService.deleteById(bookId);
        });
    }

    @Test
    public void updateBookById_withValidBookId_shouldUpdateBook() {
        Book book = createTestBook(1L, "Book");
        CreateBookRequestDto requestDto = createTestBookRequestDto(
                "Updated Book",
                BigDecimal.valueOf(25));
        Book updatedBook = new Book();
        updatedBook.setId(book.getId());
        updatedBook.setTitle(requestDto.getTitle());
        updatedBook.setPrice(requestDto.getPrice());
        BookDto expectedDto = mapToTestDto(updatedBook);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookMapper.toModel(any(CreateBookRequestDto.class))).thenReturn(updatedBook);
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
        when(bookMapper.toDto(any(Book.class))).thenReturn(expectedDto);

        BookDto actualDto = bookService.updateBook(book.getId(), requestDto);

        Assertions.assertEquals(expectedDto, actualDto,
                "The book DTO retrieved should match the expected book DTO");
        verify(bookRepository).findById(book.getId());
        verify(bookRepository).save(updatedBook);
        verify(bookMapper).toModel(requestDto);
        verify(bookMapper).toDto(updatedBook);
    }

    @Test
    public void updateBookById_withInvalidBookId_shouldUpdateBook() {
        Long bookId = 5L;
        CreateBookRequestDto testBookRequestDto = createTestBookRequestDto(
                "Book DTO",
                BigDecimal.valueOf(25));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bookService.updateBook(bookId, testBookRequestDto);
        });

        verify(bookRepository).findById(bookId);
    }

    @Test
    public void search_WithValidParameters_ReturnsBookDtos() {
        Book book = createTestBook(1L, "Book");
        BookSearchParameters parameters = new BookSearchParameters();
        parameters.setTitles(new String[]{book.getTitle()});
        Pageable pageable = Pageable.unpaged();
        Specification<Book> specification = mock(Specification.class);

        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookSpecificationBuilder.build(parameters)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(bookPage);

        List<BookDto> expected = List.of(mapToTestDto(book));
        when(bookMapper.toDtos(bookPage)).thenReturn(expected);

        List<BookDto> actualDtos = bookService.search(parameters, pageable);

        Assertions.assertEquals(expected, actualDtos,
                "The returned DTOs should match the expected values");
        verify(bookSpecificationBuilder).build(parameters);
        verify(bookRepository).findAll(specification, pageable);
        verify(bookMapper).toDtos(bookPage);
    }

    private BookDtoWithoutCategoryIds createTestBookDtoWithoutCategories(Book firstBook) {
        BookDtoWithoutCategoryIds dto = new BookDtoWithoutCategoryIds();
        dto.setTitle(firstBook.getTitle());
        dto.setPrice(BigDecimal.valueOf(10));
        return dto;
    }

    private Book createTestBook(Long id, String title) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setPrice(BigDecimal.valueOf(10));
        Category category = new Category();
        category.setId(STANDARD_CATEGORY_ID);
        book.setCategories(Set.of(category));
        return book;
    }

    private BookDto mapToTestDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setPrice(book.getPrice());
        dto.setCategoriesIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
        return dto;
    }

    private CreateBookRequestDto createTestBookRequestDto(String title, BigDecimal price) {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle(title);
        dto.setPrice(price);
        Category category = new Category();
        category.setId(STANDARD_CATEGORY_ID);
        dto.setCategoriesIds(Set.of(category.getId()));
        return dto;
    }

    private Book mapToTestModel(CreateBookRequestDto dto) {
        Book book = new Book();
        book.setId(1L);
        book.setPrice(dto.getPrice());
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setAuthor(dto.getAuthor());
        book.setDescription(dto.getDescription());
        book.setCoverImage(dto.getCoverImage());
        book.setCategories(dto.getCategoriesIds().stream()
                .map(Category::new)
                .collect(Collectors.toSet())
        );

        return book;
    }
}
