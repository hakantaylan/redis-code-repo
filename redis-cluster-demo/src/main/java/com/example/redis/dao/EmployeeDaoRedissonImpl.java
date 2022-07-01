package com.example.redis.dao;

import com.example.redis.model.Employee;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisCluster;

import java.util.Map;
import java.util.stream.Collectors;

@Qualifier("redisson")
@Repository
public class EmployeeDaoRedissonImpl implements IEmployeeDao {

    private final String hashReference = "Employee";

    private final RedissonClient redissonClient;

    public EmployeeDaoRedissonImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void saveEmployee(Employee employee) {
        try {
            redissonClient.getMap(hashReference).putIfAbsent(employee.getEmpId(), convertToString(employee));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Employee getOneEmployee(Integer id) {
        try {
            String json = (String) redissonClient.getMap(hashReference).get(id);
            return convertFromString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
        try {
            redissonClient.getMap(hashReference).put(employee.getEmpId(), convertToString(employee));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Integer, Employee> getAllEmployees() {
        RMap<Integer, String> map = redissonClient.getMap(hashReference);
        Map<Integer, String> strMap = map.readAllMap();
        return convertFromMap(strMap);
    }

    @Override
    public void deleteEmployee(Integer id) {
        redissonClient.getMap(hashReference).remove(id);
    }

    @Override
    public void saveAllEmployees(Map<Integer, Employee> map) {
        map.forEach((key, value) -> {
            saveEmployee(value);
        });
    }

    @Override
    public void deleteAll() {
        redissonClient.getMap(hashReference).delete();
    }

    private String convertToString(Employee employee) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writeValueAsString(employee);
    }

    private Map<Integer, Employee> convertFromMap(Map<Integer, String> map) {
        return map.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey(),
                entry -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        return mapper.readValue(entry.getValue(), Employee.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
        ));
    }

    private Employee convertFromString(String str) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper.readValue(str, Employee.class);
    }
}