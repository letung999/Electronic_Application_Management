package com.ecommerce.electronicapplicationmanagement.repository;

import com.ecommerce.electronicapplicationmanagement.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

    @Query("SELECT b FROM Basket AS b WHERE b.logicalDeleteFlag = 0 " +
            "AND b.customerId = :customerId")
    Optional<Basket> findByCustomerId(@Param("customerId") Long customerId);
}
