package com.github.onlinebookstore.dto.order;

import java.util.List;
import lombok.Data;

@Data
public class OrderHistoryDto {
    private List<OrderDto> orders;
}
