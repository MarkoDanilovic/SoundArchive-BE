package com.example.soundarchive.dao;

import com.example.soundarchive.model.entity.CartDocument;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<CartDocument, String> {

    List<CartDocument> findByUserId(Integer userId);

    Optional<CartDocument> findFirstByUserIdAndStatusIn(Integer userId, List<String> statuses, Sort sort);

    List<CartDocument> findByStatus(String status);

    //Cart findById(String id);
}
