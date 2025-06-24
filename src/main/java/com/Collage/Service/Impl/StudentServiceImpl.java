package com.Collage.Service.Impl;
import com.Collage.DTO.ResponseDTO;
import com.Collage.DTO.StudentDTO;
import com.Collage.Entity.Student;
import com.Collage.Repository.StudentRepo;
import com.Collage.Service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public  class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepo studentRepo;

    private final ModelMapper modelMapper = new ModelMapper();


    /**
     * @param studentDTO the DTO containing api data to save in student data
     * @return student data and send msg through ResponseEntity
     */

    @Override

     // Use ManualMapping
    public Mono<ResponseEntity<ResponseDTO>> createStudent(StudentDTO studentDTO) {
        return studentRepo.save(Student.builder()
                        .name(studentDTO.getName())
                        .department(studentDTO.getDepartment())
                        .phone(studentDTO.getPhone())
                        .email(studentDTO.getEmail())
                        .password(studentDTO.getPassword())
                        .addressId(studentDTO.getAddressId())
                        .build())
                .map(saved -> ResponseEntity.ok(new ResponseDTO(200, true, "Student saved successfully")))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDTO(500, false, "Student Saving failed"))));
    }



    /**
     * @return All Student data
     */


    private int compareByProperty(Student a, Student b, String property) {
        return switch (property) {
            case "id" -> a.getId().compareToIgnoreCase(b.getId());
            case "name" -> a.getName().compareToIgnoreCase(b.getName());
            case "department" -> a.getDepartment().compareToIgnoreCase(b.getDepartment());
            case "email" -> a.getEmail().compareToIgnoreCase(b.getEmail());
            case "phone" -> a.getPhone().compareTo(b.getPhone());
            default -> 0;
        };
    }
       @Override


       public Mono<Page<StudentDTO>> getAllStudentsWithPagination(Pageable pageable) {
           int pageNo = pageable.getPageNumber();
           int pageSize = pageable.getPageSize();
           Sort sort = pageable.getSort();

           return studentRepo.findAll()
                   .collectList()
                   .map(list -> {
                       // Sort manually
                       list.sort((a, b) -> {
                           for (Sort.Order order : sort) {
                               int cmp = compareByProperty(a, b, order.getProperty());
                               if (cmp != 0) return order.isAscending() ? cmp : -cmp;
                           }
                           return 0;
                       });

                       // Pagination
                       int total = list.size();
                       int fromIndex = Math.min(pageNo * pageSize, total);
                       int toIndex = Math.min(fromIndex + pageSize, total);
                       List<Student> paginated = list.subList(fromIndex, toIndex);

                       // Map to DTOs
                       List<StudentDTO> dtoList = paginated.stream()
                               .map(student -> StudentDTO.builder()
                                       .id(student.getId())
                                       .name(student.getName())
                                       .department(student.getDepartment())
                                       .phone(student.getPhone())
                                       .email(student.getEmail())
                                       .password(student.getPassword())
                                       .addressId(student.getAddressId())
                                       .build())
                               .collect(Collectors.toList());

                       return new PageImpl<>(dtoList, pageable, total);
                   });
       }




    /**
     * @param id the id is for getting student data
     * @return Student data
     */
    @Override
    public Mono<ResponseEntity<ResponseDTO>> getStudentById(String id) {
        return studentRepo.findById(id)
                .map(student -> ResponseEntity.ok(new ResponseDTO(200, true, "Student Data Found Successfully",student)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(404, false, "Student not found")));
    }




    /**
     * @param id          the ID of the student to update
     * @param studentDTO  the DTO containing updated student data
     * @return a {@link Mono} containing a {@link ResponseEntity} with a {@link ResponseDTO}
     *         indicating the outcome of the operation
     */

    @Override


    //Using ManualMapping
    public Mono<ResponseEntity<ResponseDTO>> updateStudent(String id, StudentDTO studentDTO) {
        return studentRepo.findById(id)
                .flatMap(existing-> {
                    existing.setName(studentDTO.getName());
                    existing.setDepartment(studentDTO.getDepartment());
                    existing.setPhone(studentDTO.getPhone());
                    existing.setEmail(studentDTO.getEmail());
                    existing.setPassword(studentDTO.getPassword());
                    existing.setAddressId(studentDTO.getAddressId());
                        return studentRepo.save(existing)
                    .map(saved -> ResponseEntity.ok(new ResponseDTO(200, true, "Student updated Successfully",existing)));
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(404, false, "Student not found")));
    }



    /**
     * @param id is for getting student data
     * @return deleting student data
     */
    @Override
    public Mono<ResponseEntity<ResponseDTO>> deleteStudent(String id) {
        return studentRepo.findById(id)
                .flatMap(existing -> studentRepo.deleteById(id)
                        .then(Mono.just(ResponseEntity.ok(new ResponseDTO(200, true, "Student deleted Successfully")))))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(404, false, "Student not found")));
    }

}