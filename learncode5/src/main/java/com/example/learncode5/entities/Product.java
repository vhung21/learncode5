package com.example.learncode5.entities;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "productName",length = 300)
    private String productName;
    @Column(name = "productCode",length = 300)
    private String productCode;
    @Column(name = "manufacturer", length = 300)
    private String manufacturer;
    @Column(name = "quantity", length = 300)
    private int quantity;
    @Column(name = "price", length = 300)
    private double price;
    @Column(name = "added_by")
    private String addedBy;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", addedBy='" + addedBy + '\'' +
                ", category=" + category +
                '}';
    }
}


