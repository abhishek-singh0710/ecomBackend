package com.ecommerceProject.springboot_ecommerceProject.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name="categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    // Every Entity Class Requires An ID Since It Will Be A Table In The Database So It Needs A Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryId;

    @NotBlank
    @Size(min=5, message="Category Name Must Contain Atleast 5 Characters")
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;

}
