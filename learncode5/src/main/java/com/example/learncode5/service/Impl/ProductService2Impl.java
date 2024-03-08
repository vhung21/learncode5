package com.example.learncode5.service.Impl;

import com.example.learncode5.entities.Product;
import com.example.learncode5.repository.ProductRepository;
import com.example.learncode5.service.ProductService2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductService2Impl implements ProductService2 {
    private final ProductRepository productRepository;

    public ProductService2Impl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void delete(Long id) {
        Optional<Product> product = productRepository.findProductById(id);
        if(product.isPresent()) {
            productRepository.deleteProductByIdCustom(id);
        }
    }
}
