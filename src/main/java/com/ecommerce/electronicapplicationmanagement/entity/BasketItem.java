package com.ecommerce.electronicapplicationmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        name = "basket_item",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"basket_id", "product_id"})
        }
)
public class BasketItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "basket_id", nullable = false)
    private Basket basket;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}

