package com.Collage.Controller;

import com.Collage.DTO.AddressDto;
import com.Collage.DTO.ResponseDTO;
import com.Collage.Service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing Student entities.
 * Provides endpoints for creating students.
 */
@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    AddressService addressService;


    /**
     * Creates a new student.
     *
     * @param addressDto The address data to be created.
     * @return A Mono containing a ResponseEntity with ResponseDTO indicating the operation result.
     */
    @PostMapping("/add")
    public Mono<ResponseEntity<ResponseDTO>> createAddress(@Valid @RequestBody AddressDto addressDto){
        return addressService.create(addressDto);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<ResponseDTO>> deleteAddress(@PathVariable String id) {
        return addressService.deleteAddress(id);
    }
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO>> getStudentById(@PathVariable String id) {
        return addressService.getStudentById(id);
    }


}
