package com.github.onlinebookstore.dto.book;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookDtoWithoutCategoryIds {
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
