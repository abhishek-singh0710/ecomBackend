package com.ecommerceProject.springboot_ecommerceProject.project.service;

import com.ecommerceProject.springboot_ecommerceProject.project.exceptions.ResourceNotFoundException;
import com.ecommerceProject.springboot_ecommerceProject.project.model.Address;
import com.ecommerceProject.springboot_ecommerceProject.project.model.User;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.AddressDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.repositories.AddressRepository;
import com.ecommerceProject.springboot_ecommerceProject.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;


//    @Override
//    public AddressDTO createAddress(AddressDTO addressDTO, User currUser) {
//
//        Address address = modelMapper.map(addressDTO, Address.class);
//
//        // Updated The Address In The User's Address List
//        List<Address> addressList = currUser.getAddresses();
//        addressList.add(address);
//        currUser.setAddresses(addressList);
//
//        address.setUser(currUser);
//        Address savedAddress = addressRepository.save(address);
//
//        return modelMapper.map(savedAddress, AddressDTO.class);
//    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User currUser) {

        // Check if an address with the same details already exists for the user
        Optional<Address> existingAddress = addressRepository.findByUserAndStreetAndBuildingNameAndCityAndStateAndCountryAndPincode(
                currUser,
                addressDTO.getStreet(),
                addressDTO.getBuildingName(),
                addressDTO.getCity(),
                addressDTO.getState(),
                addressDTO.getCountry(),
                addressDTO.getPincode()
        );

        if (existingAddress.isPresent()) {
            // Return the existing address if found
            return modelMapper.map(existingAddress.get(), AddressDTO.class);
        }

        // If address does not exist, create and save a new one
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(currUser);

        // Add to user's address list
        currUser.getAddresses().add(address);

        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addressList= addressRepository.findAll();

//        List<AddressDTO> addressDTOs = addressList.stream().map(
//                addr -> modelMapper.map(addr, AddressDTO.class))
//                .collect(Collectors.toList());

        List<AddressDTO> addressDTOs = addressList.stream().map(
                        addr -> modelMapper.map(addr, AddressDTO.class))
                .toList();

        return addressDTOs;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow( () -> new ResourceNotFoundException("Address", "AddressId", addressId));

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User currUser) {
        List<Address> addressList= currUser.getAddresses();

//        List<AddressDTO> addressDTOs = addressList.stream().map(
//                addr -> modelMapper.map(addr, AddressDTO.class))
//                .collect(Collectors.toList());

        List<AddressDTO> addressDTOs = addressList.stream().map(
                        addr -> modelMapper.map(addr, AddressDTO.class))
                .toList();

        return addressDTOs;
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow( () -> new ResourceNotFoundException("Address", "AddressId", addressId));

        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPincode(addressDTO.getPincode());
        address.setStreet(addressDTO.getStreet());
        address.setCountry(addressDTO.getCountry());
        address.setBuildingName(addressDTO.getBuildingName());

        Address updatedAddress = addressRepository.save(address);

        User user = address.getUser();
        user.getAddresses().removeIf(addr -> addr.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDTO.class);

    }

    @Override
    public String deleteAddress(Long addressId) {
        Address deletedAddress = addressRepository.findById(addressId)
                        .orElseThrow( () -> new ResourceNotFoundException("Address", "AddressId", addressId));

        User user = deletedAddress.getUser();
        user.getAddresses().removeIf(addr -> addr.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.deleteById(addressId);

        return "Address With AddressId "+ addressId+" Deleted Successfully";
    }
}
