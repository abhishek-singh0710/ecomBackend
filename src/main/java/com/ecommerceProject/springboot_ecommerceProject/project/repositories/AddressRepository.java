package com.ecommerceProject.springboot_ecommerceProject.project.repositories;

import com.ecommerceProject.springboot_ecommerceProject.project.model.Address;
import com.ecommerceProject.springboot_ecommerceProject.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByUserAndStreetAndBuildingNameAndCityAndStateAndCountryAndPincode(
            User user,
            String street,
            String buildingName,
            String city,
            String state,
            String country,
            String pincode
    );
}
