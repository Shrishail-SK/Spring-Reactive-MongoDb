package com.Collage.Repository;

import com.Collage.Entity.Student;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends ReactiveMongoRepository<Student,String> {

}
