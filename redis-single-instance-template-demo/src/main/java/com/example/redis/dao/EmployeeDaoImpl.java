package com.example.redis.dao;

import com.example.redis.model.Employee;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class EmployeeDaoImpl implements IEmployeeDao {

    private final String hashReference= "Employee";

    @Resource(name="redisTemplate")          // 'redisTemplate' is defined as a Bean in RedisConfig.java
    private HashOperations<String, Integer, Employee> hashOperations;

    @Override
    public void saveEmployee(Employee emp) {
        //creates one record in Redis DB if record with that Id is not present
        hashOperations.putIfAbsent(hashReference, emp.getEmpId(), emp);
    }

    @Override
    public void saveAllEmployees(Map<Integer, Employee> map) {
        hashOperations.putAll(hashReference, map);
    }

    @Override
    public Employee getOneEmployee(Integer id) {
        return hashOperations.get(hashReference, id);
    }

    @Override
    public void updateEmployee(Employee emp) {
        hashOperations.put(hashReference, emp.getEmpId(), emp);
    }

    @Override
    public Map<Integer, Employee> getAllEmployees() {
        return hashOperations.entries(hashReference);
    }

    @Override
    public void deleteEmployee(Integer id) {
        hashOperations.delete(hashReference, id);
    }
}