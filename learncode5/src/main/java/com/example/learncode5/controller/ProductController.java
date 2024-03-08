package com.example.learncode5.controller;

import com.example.learncode5.DTO.ProductDTO;
import com.example.learncode5.payload.Response.ResponseObject;
import com.example.learncode5.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
    public ResponseEntity<ResponseObject> getProductList()
    {
        return productService.getProductList();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> delete(@PathVariable long id){
        return productService.deleteProductById(id);
    }

    @GetMapping("/{product_name}")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
    public ResponseEntity<ResponseObject> get(@PathVariable String product_name)
    {
        return productService.getProductByProductName(product_name);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> update(@PathVariable long id, @RequestBody ProductDTO productDTO)
    {
        Long categoryId = productDTO.getCategory().getId();
        return productService.updateProduct(
                id,
                productDTO.getProductName(),
                productDTO.getProductCode(),
                productDTO.getManufacturer(),
                productDTO.getQuantity(),
                productDTO.getPrice(),
                categoryId
        );
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> addProduct(@RequestBody ProductDTO productDTO)
    {
        Long categoryId = productDTO.getCategory().getId();
        return productService.addProduct(
                productDTO.getId(),
                productDTO.getProductName(),
                productDTO.getProductCode(),
                productDTO.getManufacturer(),
                productDTO.getQuantity(),
                productDTO.getPrice(),
                categoryId
        );
    }
}
