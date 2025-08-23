package com.ecommerce.electronicapplicationmanagement.repository;

import com.ecommerce.electronicapplicationmanagement.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    @Query("SELECT p FROM Product p WHERE (:category IS NULL OR :category ='' OR p.category = :category) " +
            "AND (:name IS NULL OR p.name LIKE %:name%) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:available IS NULL OR p.available = :available)" +
            " AND p.logicalDeleteFlag = 0")
    Page<Product> findFiltered(@Param("category") String category,
                               @Param("name") String nameProduct,
                               @Param("minPrice") BigDecimal minPrice,
                               @Param("maxPrice") BigDecimal maxPrice,
                               @Param("available") Boolean available,
                               Pageable pageable);

    @Query("SELECT p FROM Product AS p WHERE p.version =:version " +
            "AND p.id =:id " +
            "AND p.logicalDeleteFlag = 0")
    Optional<Product> findProductByIdAndVersion(@Param("id") Long id,
                                                @Param("version") Long version);


    @Query(value = "SELECT p FROM Product AS p WHERE p.id IN (:productIds) AND p.logicalDeleteFlag = 0")
    List<Product> getInformationProduct(@Param("productIds") List<Long> ids);
}
