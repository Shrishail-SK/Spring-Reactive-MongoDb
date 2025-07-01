package com.Collage.Repository;

import com.Collage.Entity.Hobbies;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HobbiesRepo extends ReactiveMongoRepository<Hobbies,String> {
}
