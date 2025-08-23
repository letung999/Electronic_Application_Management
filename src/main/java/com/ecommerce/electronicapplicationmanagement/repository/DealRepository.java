package com.ecommerce.electronicapplicationmanagement.repository;

import com.ecommerce.electronicapplicationmanagement.entity.Deal;
import com.ecommerce.electronicapplicationmanagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
    @Query("SELECT d FROM Deal d " +
            "WHERE d.product = :product " +
            "AND d.expiration > :now " +
            "AND d.logicalDeleteFlag = 0")
    List<Deal> findByProductAndExpirationAfter(@Param("product") Product product,
                                               @Param("now") LocalDateTime now);

    @Query("SELECT d FROM Deal d " +
            "WHERE d.product.id = :productId " +
            "AND d.logicalDeleteFlag = 0")
    List<Deal> findByProductId(@Param("productId") Long productId);

    @Query("SELECT COUNT(d) > 0 FROM Deal d " +
            "WHERE d.product.id = :productId " +
            "AND d.logicalDeleteFlag = 0")
    boolean existsByProductId(@Param("productId") Long productId);

    @Modifying
    @Query("UPDATE Deal d SET d.logicalDeleteFlag = 1" +
            " WHERE d.product.id = :productId " +
            "AND d.logicalDeleteFlag = 0")
    int deleteByProductId(@Param("productId") Long productId);
}
