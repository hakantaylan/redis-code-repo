package com.example.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RedisDataRepository extends CrudRepository<RedisData, String> {

    Optional<RedisData> findById(String id);

    List<RedisData> findByType(String type);
}
