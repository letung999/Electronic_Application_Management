package com.ecommerce.electronicapplicationmanagement.repository;

import com.ecommerce.electronicapplicationmanagement.mapper.ReceiptInfoDtoMapper;
import com.ecommerce.electronicapplicationmanagement.entity.BasketItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {
    @Query("SELECT bi FROM BasketItem bi WHERE bi.basket.id = :basketId " +
            "AND bi.product.id = :productId " +
            "AND bi.logicalDeleteFlag = 0")
    Optional<BasketItem> findByBasketIdAndProductId(@Param("basketId") Long basketId,
                                                    @Param("productId") Long productId);

    @Query("SELECT COUNT(bi) > 0 FROM BasketItem bi " +
            "WHERE bi.product.id = :productId " +
            "AND bi.logicalDeleteFlag = 0")
    boolean existsByProductId(@Param("productId") Long productId);


    @Query("SELECT b.customerId AS customerId, " +
            "p.price AS price, " +
            "bi.quantity AS quantity, " +
            "SUM (bi.quantity * p.price) AS totalPrice " +
            "FROM BasketItem AS bi " +
            " INNER JOIN Basket AS b ON bi.basket.id = b.id " +
            " INNER JOIN Product p ON bi.product.id = p.id" +
            " WHERE bi.basket.id = b.id " +
            "GROUP BY  b.customerId, p.price, bi.quantity")
    Page<ReceiptInfoDtoMapper> getInfoReceipt(Pageable pageable);

    @Modifying
    @Query("UPDATE BasketItem bi SET bi.logicalDeleteFlag = 1 " +
            "WHERE bi.product.id = :productId" +
            " AND bi.logicalDeleteFlag = 0")
    int deleteByProductId(@Param("productId") Long productId);
}
