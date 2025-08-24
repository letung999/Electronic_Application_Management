package com.ecommerce.electronicapplicationmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"deal_type", "product_id"})
        }
)
public class Deal extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "deal_type")
    private String dealType;

    @Column(name = "expiration")
    private LocalDateTime expiration;

    @Column(name = "discount_value")
    private BigDecimal discountValue;
}
