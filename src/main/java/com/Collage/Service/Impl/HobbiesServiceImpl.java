package com.Collage.Service.Impl;

import com.Collage.DTO.HobbiesDTO;
import com.Collage.DTO.ResponseDTO;
import com.Collage.Entity.Hobbies;
import com.Collage.Repository.HobbiesRepo;
import com.Collage.Service.HobbiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class HobbiesServiceImpl implements HobbiesService {

    @Autowired
    HobbiesRepo hobbiesRepo;

    @Override
    public Mono<ResponseEntity<ResponseDTO>> create(HobbiesDTO hobbiesDTO) {
        return hobbiesRepo.save(Hobbies.builder()
                .hobbyName(hobbiesDTO.getHobbyName())
                        .build())
                .map(saved -> ResponseEntity.ok(new ResponseDTO(200,true,"Hobbies Saved Successfully")))
                .onErrorResume(e->{
                    ResponseDTO errorResponse = new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to save Hobbies");
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                });
    }

    @Override
    public Mono<ResponseEntity<ResponseDTO>> deleteHobbies(String id) {
        return hobbiesRepo.findById(id)
                .flatMap(existing -> hobbiesRepo.deleteById(id))
                    .then(Mono.just(ResponseEntity.ok(new ResponseDTO(200,true,"Hobbies deleted Successfully"))))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(404, false, "Hobbies not found")));
    }

    @Override
    public Mono<ResponseEntity<ResponseDTO>> getStudentById(String id) {
        return hobbiesRepo.findById(id)
                .map(student -> ResponseEntity.ok(new ResponseDTO(200, true, "Student Data Found Successfully",student)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(404, false, "Student not found")));
    }
}
