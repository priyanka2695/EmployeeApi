package com.demo.dao;

import java.util.List;

import com.demo.entity.Employee;

public interface EmployeeDao {

	List<Employee> getEmployees();

	Employee getEmployeeById(int employeeNo);

	Employee createEmployee(Employee emp);

	int updateEmployee(int employeeNo, Employee emp);

	int deleteEmployee(int employeeNo);

}
