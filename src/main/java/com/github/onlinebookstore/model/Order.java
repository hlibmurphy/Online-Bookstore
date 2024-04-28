package com.github.onlinebookstore.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @NotNull
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private BigDecimal total;

    @NotNull
    private LocalDateTime orderDate;

    @NotNull
    private String shippingAddress;

    @OneToMany(mappedBy = "order", orphanRemoval = true, fetch = FetchType.LAZY, cascade =
            CascadeType.ALL)
    private Set<OrderItem> orderItems = new HashSet<>();

    public enum Status {
        DELIVERED,
        COMPLETED,
        PENDING
    }
}
