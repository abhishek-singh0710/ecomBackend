package com.ecommerceProject.springboot_ecommerceProject.project.controller;

import com.ecommerceProject.springboot_ecommerceProject.project.model.Address;
import com.ecommerceProject.springboot_ecommerceProject.project.model.User;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.AddressDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.service.AddressService;
import com.ecommerceProject.springboot_ecommerceProject.project.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class AddressController {

    @Autowired
    AddressService addressService;

    @Autowired
    AuthUtil authUtil;


    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        User currUser = authUtil.loggedInUser();
        System.out.println("currUser="+currUser);
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, currUser);

        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        List<AddressDTO> addressesDTOs = addressService.getAddresses();

        return new ResponseEntity<List<AddressDTO>>(addressesDTOs, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressById(addressId);

        return new ResponseEntity<AddressDTO>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses() {
        User currUser = authUtil.loggedInUser();
        List<AddressDTO> addressesDTOs = addressService.getUserAddresses(currUser);

        return new ResponseEntity<List<AddressDTO>>(addressesDTOs, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId, @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddressDTO = addressService.updateAddress(addressId, addressDTO);

        return new ResponseEntity<AddressDTO>(updatedAddressDTO, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        String status = addressService.deleteAddress(addressId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
