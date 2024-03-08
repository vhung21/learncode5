package com.example.learncode5.DTO;

import com.example.learncode5.entities.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class ProductDTO {
    private Long id;
    private String productName;
    private String productCode;
    private String manufacturer;
    private int quantity;
    private double price;
    private CategoryDTO category;

    public ProductDTO(Product product) {
        this.id= product.getId();
        this.productName= product.getProductName();
        this.productCode= product.getProductCode();
        this.manufacturer= product.getManufacturer();
        this.quantity= product.getQuantity();
        this.price= product.getPrice();
    }

}
