package com.example.learncode5.mapper;

import com.example.learncode5.DTO.CategoryDTO;
import com.example.learncode5.DTO.ProductDTO;
import com.example.learncode5.DTO.RoleDTO;
import com.example.learncode5.DTO.UserDTO;
import com.example.learncode5.entities.Product;
import com.example.learncode5.entities.Role;
import com.example.learncode5.entities.User;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    public UserDTO userToUserDTO(User user) {
        return new UserDTO(user);
    }

    public ProductDTO productToProductDTO(Product product) {
        ProductDTO dto = new ProductDTO(product);
        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setProductCode(product.getProductCode());
        dto.setManufacturer(product.getManufacturer());
        dto.setQuantity(product.getQuantity());
        dto.setPrice(product.getPrice());

        // Mapping Category
        if (product.getCategory() != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId((long) Math.toIntExact(product.getCategory().getId()));
            categoryDTO.setCategoryName(product.getCategory().getCategoryName());
            dto.setCategory(categoryDTO);
        }
        return dto;
    }
}
