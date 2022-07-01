package com.example.redis.dao;

import com.example.redis.model.Employee;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisCluster;

import java.util.Map;
import java.util.stream.Collectors;

@Qualifier("jedis")
@Repository
public class EmployeeDaoImpl implements IEmployeeDao {

    private final String hashReference = "Employee";

    private final JedisCluster jedisCluster;

    public EmployeeDaoImpl(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public void saveEmployee(Employee employee) {
        try {
            jedisCluster.hsetnx(hashReference, employee.getEmpId() + "", convertToString(employee));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Employee getOneEmployee(Integer id) {
        try {
            String hget = jedisCluster.hget(hashReference, id + "");
            return convertFromString(hget);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
        try {
            jedisCluster.hset(hashReference, employee.getEmpId() + "", convertToString(employee));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Integer, Employee> getAllEmployees() {
        Map<String, String> strMap = jedisCluster.hgetAll(hashReference);
        return convertFromMap(strMap);
    }

    @Override
    public void deleteEmployee(Integer id) {
        jedisCluster.hdel(hashReference, id + "");
    }

    @Override
    public void saveAllEmployees(Map<Integer, Employee> map) {
        map.forEach((key, value) -> {
            saveEmployee(value);
        });
    }

    @Override
    public void deleteAll() {
//        Map<String, String> stringStringMap = jedisCluster.hgetAll(hashReference);
//        jedisCluster.hdel(hashReference, stringStringMap.keySet().toArray(new String[] {}));
    }

    private String convertToString(Employee employee) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writeValueAsString(employee);
    }

    private Map<Integer, Employee> convertFromMap(Map<String, String> map) {
        return map.entrySet().stream().collect(Collectors.toMap(
                entry -> Integer.valueOf(entry.getKey()),
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