package com.ecommerceProject.springboot_ecommerceProject.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
//@ToString
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street Name Must Be Atleast 5 Characters Long")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building Name Must Be Atleast 5 Characters Long")
    private String buildingName;

    @NotBlank
    @Size(min = 4, message = "City Name Must Be Atleast 4 Characters Long")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State Name Must Be Atleast 2 Characters Long")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country Name Must Be Atleast 4 Characters Long")
    private String country;

    @NotBlank
    @Size(min = 6, message = "Pincode Name Must Be Atleast 6 Characters Long")
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String street, String buildingName, String city, String state, String country, String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
}
