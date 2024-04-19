package com.example.learncode5.service;

import com.example.learncode5.DTO.ProductDTO;
import com.example.learncode5.entities.Category;
import com.example.learncode5.entities.Product;
import com.example.learncode5.mapper.Mapper;
import com.example.learncode5.payload.Response.ResponseObject;
import com.example.learncode5.repository.CategoryRepository;
import com.example.learncode5.repository.ProductRepository;
import com.example.learncode5.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Autowired
    Mapper mapper;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CategoryRepository categoryRepository;

    public ResponseEntity<ResponseObject> getProductList() {
        try {
            List<Product> productList = productRepository.findAll();
            List<ProductDTO> productDTOList = new ArrayList<>();
            for (Product product : productList) {
                productDTOList.add(mapper.productToProductDTO(product));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "", productDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("failed", "An error occurred while fetching product list", null));
        }
    }

    public ResponseEntity<ResponseObject> getProductByProductName(String product_name) {
        try {
            Optional<Product> product = productRepository.findByProductName(product_name);
            return product.map(value -> ResponseEntity.ok(new ResponseObject("success", "get user successfully", mapper.productToProductDTO(value)))).orElseGet(() -> ResponseEntity.badRequest().body(new ResponseObject("failed", "product name not exist", product_name)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("failed", "An error occurred while fetching product by name", product_name));
        }
    }

    public ResponseEntity<ResponseObject> deleteProductById(long id) {
        try {
            Optional<Product> product = productRepository.findProductById(id);
            if (product.isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseObject("failed", "id not exist", id));
            }
            productRepository.deleteProductByIdCustom(id);
            return ResponseEntity.ok(new ResponseObject("success", "deleted successfully", mapper.productToProductDTO(product.get())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("failed", "An error occurred while deleting product", id));
        }
    }

    public ResponseEntity<ResponseObject> updateProduct(ProductDTO product , Long categoryId) {
        try {
            Optional<Product> existingProduct = productRepository.findProductById(product.getId());
            if (existingProduct.isPresent() == false) {
                return ResponseEntity.badRequest().body(
                        new ResponseObject("failed", "id not exists", null)
                );
            }

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            Product product1 = existingProduct.get();
            product1.setProductCode(product.getProductCode());
            product1.setProductName(product.getProductName());
            product1.setManufacturer(product.getManufacturer());
            product1.setQuantity(product.getQuantity());
            product1.setPrice(product.getPrice());
            product1.setCategory(category);
            productRepository.save(product1);
            return ResponseEntity.ok(new ResponseObject("success", "", mapper.productToProductDTO(product1)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("failed", "An error occurred while updating product", null));
        }
    }

    public ResponseEntity<ResponseObject> addProduct(ProductDTO product, Long categoryId) {
        try {
            if (productRepository.existsByProductCode(product.getProductCode())) {
                return ResponseEntity.badRequest().body(
                        new ResponseObject("failed", "Product with the given product code already exists", null)
                );
            }
            if (productRepository.existsByProductName(product.getProductName())) {
                return ResponseEntity.badRequest().body(
                        new ResponseObject("failed", "Product with the given product name already exists", null)
                );
            }
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            String addedBy = SecurityContextHolder.getContext().getAuthentication().getName();

            Product product1 = new Product();

            product1.setProductCode(product.getProductCode());
            product1.setProductName(product.getProductName());
            product1.setManufacturer(product.getManufacturer());
            product1.setQuantity(product.getQuantity());
            product1.setPrice(product.getPrice());
            product1.setCategory(category);
            product1.setAddedBy(addedBy);

            Product savedProduct = productRepository.save(product1);
            return ResponseEntity.ok(new ResponseObject("success", "", mapper.productToProductDTO(savedProduct)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("failed", "An error occurred while add product", null));
        }
    }
}
