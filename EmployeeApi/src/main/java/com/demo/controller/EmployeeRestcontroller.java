package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dao.EmployeeDao;
import com.demo.entity.Employee;

@RestController
@RequestMapping("/api/employee")
public class EmployeeRestcontroller {

	private static final String EMPLOYEE_ALREADY_EXISTS_WITH_NAME = "Employee already exists with name ";
	private static final String FEMALE = "female";
	private static final String MALE = "male";

	@Autowired
	EmployeeDao employeeDao;

	/**
	 * this method is used to get all Employees
	 * 
	 * @return Employees
	 */
	@GetMapping(produces = { "application/xml", "application/json" })
	public ResponseEntity<?> getEmployees() {
		ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		List<Employee> employees = employeeDao.getEmployees();
		if (!employees.isEmpty()) {
			return ResponseEntity.ok(employees);
		}
		return responseEntity;
	}

	/**
	 * this method is used to get Employee by employee nuber
	 * 
	 * @param employeeNo
	 * @return
	 */
	@GetMapping(value = "/{employeeNo}", produces = { "application/xml", "application/json" })
	public ResponseEntity<?> getEmployeeById(@PathVariable("employeeNo") int employeeNo) {
		ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Employee employee = employeeDao.getEmployeeById(employeeNo);
		if (employee != null) {
			return ResponseEntity.ok(employee);
		}
		return responseEntity;

	}

	/**
	 * this method is used to create new employee
	 * 
	 * @param emp
	 * @return
	 */
	@PostMapping(produces = { "application/xml", "application/json" }, consumes = { "application/xml",
			"application/json" })
	public ResponseEntity<?> createEmployee(@RequestBody Employee emp) {
		ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.CONFLICT);
		try {
			if (emp.getGender().equalsIgnoreCase(MALE) || emp.getGender().equalsIgnoreCase(FEMALE)) {
				Employee createdEmployee = employeeDao.createEmployee(emp);
				if (null != createdEmployee && createdEmployee.getEmployeeNo() != 0) {
					responseEntity = new ResponseEntity<Employee>(createdEmployee, HttpStatus.CREATED);
				} else
					System.out
							.println(EMPLOYEE_ALREADY_EXISTS_WITH_NAME + emp.getFirstName() + " " + emp.getLastName());
			}
		} catch (Exception e) {
			System.out.println("error occured " + e.getMessage());
		}
		return responseEntity;

	}

	/**
	 * this method is used to update employee for the given employee number
	 * 
	 * @param employeeNo
	 * @param emp
	 * @return
	 */
	@PutMapping(value = "/{employeeNo}", produces = { "application/xml", "application/json" }, consumes = {
			"application/xml", "application/json" })
	public ResponseEntity<?> updateEmployee(@PathVariable("employeeNo") int employeeNo, @RequestBody Employee emp) {
		ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.CONFLICT);
		try {
			if (emp.getGender().equalsIgnoreCase(MALE) || emp.getGender().equalsIgnoreCase(FEMALE)) {
				int rowCount = employeeDao.updateEmployee(employeeNo, emp);
				if (rowCount == 1) {
					Employee updatedEmployee = employeeDao.getEmployeeById(employeeNo);
					if (updatedEmployee != null) {
						return ResponseEntity.ok(updatedEmployee);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("error occured " + e.getMessage());
		}
		return responseEntity;
	}

	/**
	 * this method is used to delete employee for the given employee number
	 * 
	 * @param employeeNo
	 * @return
	 */
	@DeleteMapping("/{employeeNo}")
	public ResponseEntity<?> deleteEmployee(@PathVariable("employeeNo") int employeeNo) {
		ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.CONFLICT);
		int rowCount = employeeDao.deleteEmployee(employeeNo);
		if (rowCount == 1) {
			responseEntity = new ResponseEntity<>(HttpStatus.OK);
		}
		return responseEntity;
	}
}
