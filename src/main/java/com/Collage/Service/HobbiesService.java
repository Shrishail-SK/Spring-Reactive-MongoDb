package com.Collage.Service;

import com.Collage.DTO.HobbiesDTO;
import com.Collage.DTO.ResponseDTO;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface HobbiesService {
    Mono<ResponseEntity<ResponseDTO>> create (HobbiesDTO hobbiesDTO);
    Mono<ResponseEntity<ResponseDTO>> deleteHobbies(String id);
    Mono<ResponseEntity<ResponseDTO>> getStudentById(String id);
}
