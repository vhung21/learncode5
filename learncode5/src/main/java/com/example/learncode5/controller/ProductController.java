package com.example.learncode5.controller;

import com.example.learncode5.DTO.ProductDTO;
import com.example.learncode5.payload.Response.ResponseObject;
import com.example.learncode5.service.ProductService;
import com.example.learncode5.service.ProductService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
//  @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> getProductList()
    {
        return productService.getProductList();
    }

    @DeleteMapping("/{id}")
//  @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> delete(@PathVariable long id){
        return productService.deleteProductById(id);
    }

    @GetMapping("/{product_name}")
//  @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> get(@PathVariable String product_name)
    {
        return productService.getProductByProductName(product_name);
    }

    @PostMapping("/{id}")
//  @PreAuthorize("hasAuthority('ADMIN')")
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
