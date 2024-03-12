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
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') ")
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
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
    public ResponseEntity<ResponseObject> update(@PathVariable long id, @RequestBody ProductDTO productDTO)
    {
        ResponseEntity<ResponseObject> response = productService.updateProduct(id, productDTO, productDTO.getCategory().getId());
        return response;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
    public ResponseEntity<ResponseObject> addProduct(@RequestBody ProductDTO productDTO)
    {
        ResponseEntity<ResponseObject> response = productService.addProduct(productDTO, productDTO.getCategory().getId());
        return response;
    }
}
