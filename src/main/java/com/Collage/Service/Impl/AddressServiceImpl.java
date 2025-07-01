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
public  class AddressServiceImpl implements AddressService {

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

    @Override
    public Mono<ResponseEntity<ResponseDTO>> deleteAddress(String id) {
        return addressRepo.findById(id)
                .flatMap(existing -> addressRepo.deleteById(id))
                .then(Mono.just(ResponseEntity.ok(new ResponseDTO(200,true,"Address deleted Successfully"))))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(404, false, "Address not found")));
    }

    @Override
    public Mono<ResponseEntity<ResponseDTO>> getStudentById(String id) {
        return addressRepo.findById(id)
                .map(student -> ResponseEntity.ok(new ResponseDTO(200, true, "Student Data Found Successfully",student)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(404, false, "Student not found")));
    }
}
