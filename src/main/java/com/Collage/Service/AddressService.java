package com.Collage.Service;

import com.Collage.DTO.AddressDto;
import com.Collage.DTO.ResponseDTO;
import com.Collage.Entity.Address;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface AddressService {
    Mono<ResponseEntity<ResponseDTO>> create (AddressDto addressDto);
}
