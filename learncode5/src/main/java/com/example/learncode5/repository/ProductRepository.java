package com.example.learncode5.repository;

import com.example.learncode5.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String product_name);
    Optional<Product> findProductById(long id);
    boolean existsByProductCode(String productCode);
    boolean existsByProductName(String productName);

    @Modifying
    @Query(value = "Delete From Product where id = ?1", nativeQuery = true)
    void deleteProductByIdCustom(Long id);
}
