package com.ecommerce.electronicapplicationmanagement.repository;

import com.ecommerce.electronicapplicationmanagement.entity.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {
    @Query("SELECT bi FROM BasketItem bi WHERE bi.product.id = :productId " +
            "AND bi.logicalDeleteFlag = 0")
    List<BasketItem> findByProductIdAndDeleteFlagFalse(@Param("productId") Long productId);

    @Query("SELECT COUNT(bi) > 0 FROM BasketItem bi " +
            "WHERE bi.product.id = :productId " +
            "AND bi.logicalDeleteFlag = 0")
    boolean existsByProductId(@Param("productId") Long productId);

    @Modifying
    @Query("UPDATE BasketItem bi SET bi.logicalDeleteFlag = 1 " +
            "WHERE bi.product.id = :productId" +
            " AND bi.logicalDeleteFlag = 0")
    int deleteByProductId(@Param("productId") Long productId);
}
