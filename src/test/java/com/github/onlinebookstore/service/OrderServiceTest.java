package com.github.onlinebookstore.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.github.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.github.onlinebookstore.dto.order.OrderDto;
import com.github.onlinebookstore.dto.order.UpdateOrderRequestDto;
import com.github.onlinebookstore.dto.orderitem.OrderItemDtoWithoutOrder;
import com.github.onlinebookstore.mapper.OrderItemMapper;
import com.github.onlinebookstore.mapper.OrderMapper;
import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.model.CartItem;
import com.github.onlinebookstore.model.Order;
import com.github.onlinebookstore.model.OrderItem;
import com.github.onlinebookstore.model.ShoppingCart;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.repository.OrderRepository;
import com.github.onlinebookstore.repository.ShoppingCartRepository;
import com.github.onlinebookstore.repository.UserRepository;
import com.github.onlinebookstore.service.impl.OrderServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.HashSet;
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
        CreateOrderRequestDto orderDto = createTestCreateOrderDto();
        Long userId = 1L;
        int quantity = 2;
        int price = 50;
        ShoppingCart shoppingCart = createTestShoppingCart(userId, quantity,
                BigDecimal.valueOf(price));

        when(shoppingCartRepository.findByUserId(anyLong())).thenReturn(Optional.of(shoppingCart));
        Set<OrderItem> orderItems = mapCartItemsToOrderItems(shoppingCart.getCartItems());
        when(orderItemMapper.toOrderItems(shoppingCart.getCartItems()))
                .thenReturn(orderItems);

        Order order = orderDtoToModel(orderDto);
        when(orderMapper.toModel(any(CreateOrderRequestDto.class)))
                .thenReturn(order);

        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        order.setOrderItems(orderItems);
        when(userRepository.findById(userId))
                .thenReturn(Optional.ofNullable(shoppingCart.getUser()));
        order.setUser(shoppingCart.getUser());
        order.setTotal(BigDecimal.valueOf((quantity * price)));
        order.setId(1L);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        OrderDto expected = mapToDto(order);
        when(orderMapper.toDto(order)).thenReturn(expected);

        OrderDto actual = orderService.add(orderDto, userId);
        Assertions.assertEquals(expected, actual,
                "The retrieved DTO should match the expected DTO");
    }

    @Test
    public void getAllOrders_withValidUserId_shouldReturnOrders() {
        Page<Order> ordersPage = new PageImpl<>(
                List.of(createTestOrder(1L),
                        createTestOrder(2L))
        );
        List<Order> orders = ordersPage.getContent();
        when(orderRepository.findAllByUserId(anyLong(), any(Pageable.class)))
                .thenReturn(ordersPage);
        List<OrderDto> expected = orders.stream()
                .map(this::mapToDto)
                .toList();
        when(orderMapper.toDtos(orders)).thenReturn(expected);

        List<OrderDto> actual = orderService.getAllOrders(1L, Pageable.unpaged());
        Assertions.assertEquals(expected, actual,
                "The retrieved DTO should match the expected DTO"
        );
    }

    @Test
    public void updateOrder_withValidOrderId_shouldUpdateOrder() {
        Order.Status newStatus = Order.Status.COMPLETED;
        Order order = createTestOrder(1L);
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        order.setStatus(newStatus);
        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        OrderDto expected = mapToDto(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(expected);

        UpdateOrderRequestDto orderRequestDto = createTestUpdateOrderDto(newStatus);
        OrderDto actual = orderService.updateOrder(orderRequestDto, 1L);
        Assertions.assertEquals(expected, actual,
                "The retrieved DTO should match the expected DTO");
    }

    @Test
    public void updateOrder_withInvalidOrderId_shouldThrowException() {
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            Order.Status newStatus = Order.Status.COMPLETED;
            UpdateOrderRequestDto orderRequestDto = createTestUpdateOrderDto(newStatus);
            orderService.updateOrder(orderRequestDto, 1L);
        });
    }

    private Order createTestOrder(Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setShippingAddress("shipping address");

        Book book = new Book();
        book.setId(1L);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setBook(book);
        orderItem.setOrder(order);
        Set<OrderItem> orderItems = order.getOrderItems();
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);

        User user = new User();
        user.setId(1L);
        order.setUser(user);
        return order;
    }

    private Order orderDtoToModel(CreateOrderRequestDto orderDto) {
        Order order = new Order();
        order.setShippingAddress(orderDto.shippingAddress());
        return order;
    }

    private OrderDto mapToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderItems(order.getOrderItems().stream().map(orderItem ->
                new OrderItemDtoWithoutOrder(
                        orderItem.getId(),
                        orderItem.getBook().getId(),
                        orderItem.getQuantity()))
                .collect(Collectors.toSet()));
        orderDto.setTotal(order.getTotal());
        orderDto.setUserId(order.getUser().getId());
        orderDto.setStatus(order.getStatus());
        orderDto.setOrderDate(order.getOrderDate());
        return orderDto;
    }

    private CreateOrderRequestDto createTestCreateOrderDto() {
        return new CreateOrderRequestDto("shipping address");
    }

    private Set<OrderItem> mapCartItemsToOrderItems(Set<CartItem> cartItems) {
        return cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            return orderItem;
        }).collect(Collectors.toSet());
    }

    private ShoppingCart createTestShoppingCart(Long userId, int quantity, BigDecimal price) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        User user = new User();
        user.setId(userId);
        user.setShoppingCart(shoppingCart);

        shoppingCart.setUser(user);

        Book book = new Book();
        book.setTitle("Book");
        book.setId(1L);
        book.setPrice(price);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setQuantity(quantity);
        cartItem.setBook(book);
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);

        cartItem.setBook(book);

        shoppingCart.setCartItems(cartItems);
        return shoppingCart;
    }

    private UpdateOrderRequestDto createTestUpdateOrderDto(Order.Status status) {
        UpdateOrderRequestDto updateOrderRequestDto = new UpdateOrderRequestDto();
        updateOrderRequestDto.setStatus(status);
        return updateOrderRequestDto;
    }
}
