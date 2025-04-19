package com.example.soundarchive.dao;

import com.example.soundarchive.model.entity.CartDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends MongoRepository<CartDocument, String> {

    List<CartDocument> findByUserId(Integer userId);

    //Cart findById(String id);
}
