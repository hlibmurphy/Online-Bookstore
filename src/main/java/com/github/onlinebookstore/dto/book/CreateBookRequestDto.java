package com.github.onlinebookstore.dto.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateBookRequestDto {
    @NotEmpty
    private String title;
    private String author;
    @Length(min = 13, max = 13)
    private String isbn;
    @NotEmpty
    private BigDecimal price;
    private String description;
    @Pattern(regexp = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
    private String coverImage;
}
