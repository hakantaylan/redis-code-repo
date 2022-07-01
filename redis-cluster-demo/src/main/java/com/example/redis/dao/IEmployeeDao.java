package com.example.redis.dao;

import com.example.redis.model.Employee;

import java.util.Map;

public interface IEmployeeDao {

    void saveEmployee(Employee employee);
    Employee getOneEmployee(Integer id);
    void updateEmployee(Employee employee);
    Map<Integer, Employee> getAllEmployees();
    void deleteEmployee(Integer id);
    void saveAllEmployees(Map<Integer, Employee> map);
    void deleteAll();
}