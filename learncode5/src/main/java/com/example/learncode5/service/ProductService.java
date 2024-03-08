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
        List<Product> productList = productRepository.findAll();
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product : productList) {
            productDTOList.add(mapper.productToProductDTO(product));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "", productDTOList));
    }

    public ResponseEntity<ResponseObject> getProductByProductName(String product_name) {
        Optional<Product> product = productRepository.findByProductName(product_name);
        return product.map(value -> ResponseEntity.ok(new ResponseObject("success", "get user successfully", mapper.productToProductDTO(value)))).orElseGet(() -> ResponseEntity.badRequest().body(new ResponseObject("failed", "product name not exist", product_name)));
    }

    public ResponseEntity<ResponseObject> deleteProductById(long id) {
        Optional<Product> product = productRepository.findProductById(id);
        if (product.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseObject("failed", "id not exist", id));
        }
        productRepository.deleteProductByIdCustom(id);
        return ResponseEntity.ok(new ResponseObject("success", "deleted successfully", mapper.productToProductDTO(product.get())));
    }

    public ResponseEntity<ResponseObject> updateProduct(Long id, String productName, String productCode, String manufacturer, int quantity, double price, Long categoryId) {
        Optional<Product> product = productRepository.findProductById(id);
        if (product.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ResponseObject("failed", "id not exists", null)
            );
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product1 = product.get();
        product1.setProductCode(productCode);
        product1.setProductName(productName);
        product1.setManufacturer(manufacturer);
        product1.setQuantity(quantity);
        product1.setPrice(price);
        product1.setCategory(category);
        productRepository.save(product1);
        return ResponseEntity.ok(new ResponseObject("success", "", mapper.productToProductDTO(product1)));
    }

    public ResponseEntity<ResponseObject> addProduct(Long id, String productName, String productCode, String manufacturer, int quantity, double price, Long categoryId) {
        if (productRepository.existsByProductCode(productCode)) {
            return ResponseEntity.badRequest().body(
                    new ResponseObject("failed", "Product with the given product code already exists", null)
            );
        }
        if (productRepository.existsByProductName(productName)) {
            return ResponseEntity.badRequest().body(
                    new ResponseObject("failed", "Product with the given product name already exists", null)
            );
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();

        product.setProductCode(productCode);
        product.setProductName(productName);
        product.setManufacturer(manufacturer);
        product.setQuantity(quantity);
        product.setPrice(price);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(new ResponseObject("success","", mapper.productToProductDTO(savedProduct)));
    }
}
