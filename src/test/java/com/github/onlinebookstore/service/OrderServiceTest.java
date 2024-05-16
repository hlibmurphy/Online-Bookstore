package com.github.onlinebookstore.service;

import com.github.onlinebookstore.mapper.OrderItemMapper;
import com.github.onlinebookstore.mapper.OrderMapper;
import com.github.onlinebookstore.repository.OrderRepository;
import com.github.onlinebookstore.repository.ShoppingCartRepository;
import com.github.onlinebookstore.repository.UserRepository;
import com.github.onlinebookstore.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void add_withValidRequest_shouldCreateOrder() {

    }

    @Test
    public void getAllOrders_withValidUserId_shouldReturnOrders() {

    }

    @Test
    public void updateOrder_withValidOrderId_shouldUpdateOrder() {

    }

    @Test
    public void updateOrder_withInvalidOrderId_shouldThrowException() {

    }
}
