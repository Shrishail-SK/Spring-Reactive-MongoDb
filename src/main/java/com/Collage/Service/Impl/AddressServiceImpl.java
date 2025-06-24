package com.Collage.Service.Impl;

import com.Collage.DTO.AddressDto;
import com.Collage.DTO.ResponseDTO;
import com.Collage.Entity.Address;
import com.Collage.Repository.AddressRepo;
import com.Collage.Service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepo addressRepo;

    @Override
    public Mono<ResponseEntity<ResponseDTO>> create(AddressDto addressDto) {
        return addressRepo.save(Address.builder()
                        .street(addressDto.getStreet())
                        .city(addressDto.getCity())
                        .state(addressDto.getState())
                        .zipCode(addressDto.getZipCode())
                        .country(addressDto.getCountry())
                        .build())
                .map(saved -> {
                    ResponseDTO response = new ResponseDTO(HttpStatus.OK.value(), true, "Address is Saved");
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    ResponseDTO errorResponse = new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to save address");
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                });
    }
}
