package com.Collage.Service.Impl;
import com.Collage.DTO.ResponseDTO;
import com.Collage.DTO.StudentDTO;
import com.Collage.DTO.StudentWithAddressDTO;
import com.Collage.Entity.Student;
import com.Collage.Repository.StudentRepo;
import com.Collage.Service.StudentService;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public  class   StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepo studentRepo;


    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    /**
     * @param studentDTO the DTO containing api data to save in student data
     * @return student data and send msg through ResponseEntity
     */

    @Override

     // Use ManualMapping
    public Mono<ResponseEntity<ResponseDTO>> createStudent(StudentDTO studentDTO) {
        List<ObjectId> addressobjectIds = studentDTO.getAddressId().stream()
                .map(ObjectId::new)
                .collect(Collectors.toList());
        List<ObjectId> hobbiesobjectIds = studentDTO.getHobbiesId().stream()
                .map(ObjectId::new)
                .collect(Collectors.toList());
        Student student = Student.builder()
                        .name(studentDTO.getName())
                        .age(studentDTO.getAge())
                        .department(studentDTO.getDepartment())
                        .phone(studentDTO.getPhone())
                        .email(studentDTO.getEmail())
                        .password(studentDTO.getPassword())
                        .feesPaid(studentDTO.getFeesPaid())
                        .addressId(addressobjectIds)
                        .hobbiesId(hobbiesobjectIds)
                        .build();
        return studentRepo.save(student)
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
            case "age" -> Integer.compare(a.getAge(),b.getAge());
            case "name" -> a.getName().compareToIgnoreCase(b.getName());
            case "department" -> a.getDepartment().compareToIgnoreCase(b.getDepartment());
            case "email" -> a.getEmail().compareToIgnoreCase(b.getEmail());
            case "phone" -> a.getPhone().compareTo(b.getPhone());
            case "feesPaid" -> Double.compare(a.getFeesPaid(),b.getFeesPaid());
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
                                       .age(student.getAge())
                                       .department(student.getDepartment())
                                       .phone(student.getPhone())
                                       .email(student.getEmail())
                                       .feesPaid(student.getFeesPaid())
                                       .addressId( student.getAddressId().stream()
                                               .map(ObjectId::toHexString)
                                               .collect(Collectors.toList()))
                                       .hobbiesId(Optional.ofNullable(student.getHobbiesId())
                                               .orElse(Collections.emptyList())
                                               .stream()
                                               .map(ObjectId::toHexString)
                                               .collect(Collectors.toList()))
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
                    List<ObjectId> addressobjectIds = studentDTO.getAddressId().stream()
                            .map(ObjectId::new)
                            .collect(Collectors.toList());
                    List<ObjectId> hobbiesobjectIds = studentDTO.getHobbiesId().stream()
                            .map(ObjectId::new)
                            .collect(Collectors.toList());
                    existing.setName(studentDTO.getName());
                    existing.setAge(studentDTO.getAge());
                    existing.setDepartment(studentDTO.getDepartment());
                    existing.setPhone(studentDTO.getPhone());
                    existing.setEmail(studentDTO.getEmail());
                    existing.setPassword(studentDTO.getPassword());
                    existing.setFeesPaid(studentDTO.getFeesPaid());
                    existing.setAddressId(addressobjectIds);
                    existing.setHobbiesId(hobbiesobjectIds);
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

    /**
     *
     * @return Aggregation query's
     */
    @Override
    public Mono<List<StudentWithAddressDTO>> getStudentsWithFullAddress() {
        Aggregation aggregation = Aggregation.newAggregation(
                //Aggregation.match(Criteria.where("department").is("Information Science")),
               // Aggregation.project("name", "email", "addressId"),
               // Aggregation.unwind("addresses", true),
               // Aggregation.group("department").count().as("studentCount"),
                Aggregation.group("department").count().as("studentCount"),
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "studentCount")),

                Aggregation.lookup("address", "addressId", "_id", "addresses"), // if addressId is a List of ObjectIds
                Aggregation.lookup("Hobbies","hobbiesId","id","hobbiess")
        );

        return reactiveMongoTemplate.aggregate(aggregation, "student", StudentWithAddressDTO.class)
                .collectList(); // // Mono<List<>>
    }

    /**
     *
     * @param pageable
     * @return Aggregation Query With Pagination
     */
    @Override
    public Mono<ResponseEntity<ResponseDTO>> getStudentsWithFullAddresss(Pageable pageable) {
        int pageNo = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Sort sort = pageable.getSort();

        List<AggregationOperation> operations = new ArrayList<>();

        // Lookup address and hobbies
        operations.add(Aggregation.lookup("hobbies", "hobbiesId", "_id", "hobbiess"));
        operations.add(Aggregation.lookup("address", "addressId", "_id", "addresses"));


        // Apply sorting if provided
        if (sort != null && sort.isSorted()) {
            for (Sort.Order order : sort) {
                operations.add(Aggregation.sort(Sort.by(order.getDirection(), order.getProperty())));
            }
        }

        // Apply pagination
        operations.add(Aggregation.skip((long) pageNo * pageSize));
        operations.add(Aggregation.limit(pageSize));

        Aggregation aggregation = Aggregation.newAggregation(operations);

        Mono<List<StudentWithAddressDTO>> dataMono = reactiveMongoTemplate
                .aggregate(aggregation, "student", StudentWithAddressDTO.class)
                .collectList();

        Mono<Long> totalMono = reactiveMongoTemplate.count(new Query(), Student.class);

        return Mono.zip(dataMono, totalMono)
                .map(tuple -> {
                    List<StudentWithAddressDTO> students = tuple.getT1();
                    long total = tuple.getT2();

                    Page<StudentWithAddressDTO> page = new PageImpl<>(students, pageable, total);
                    return ResponseEntity.ok(new ResponseDTO(200, true, "Students with addresses fetched", page));
                })
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ResponseDTO(500, false, "Aggregation failed: " + e.getMessage(), null))
                ));
    }


    @Override
    public Mono<ResponseEntity<ResponseDTO>> getStudentsByRegex(String pattern) {
        return studentRepo.findByNamePattern(pattern)
                .collectList()
                .map(result -> ResponseEntity.ok(
                        new ResponseDTO(200, true, "Students with full details fetched", result)))
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ResponseDTO(500, false, "Error: " + e.getMessage(), null))
                ));
    }




}