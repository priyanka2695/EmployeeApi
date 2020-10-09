package com.demo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.demo.entity.Employee;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

	private static final String EMP_COUNT = "empCount";

	private static final String HIRE_DATE = "hire_date";

	private static final String GENDER = "gender";

	private static final String LAST_NAME = "last_name";

	private static final String FIRST_NAME = "first_name";

	private static final String BIRTH_DATE = "birth_date";

	private static final String EMPLOYEE_NO = "employee_no";

	private static final String GET_EMPLOYEES = "select * from Employee";
	private static final String GET_EMPLOYEE_BY_ID = "select * from Employee where employee_no=:employee_no";
	private static final String UPDATE_EMPLOYEES = "update Employee set birth_date=:birth_date,first_name=:first_name,last_name=:last_name,gender=:gender,hire_date=:hire_date where employee_no=:employee_no";
	private static final String DELETE_EMPLOYEES = "delete from Employee where employee_no=:employee_no";
	private static final String INSERT_EMPLOYEES = "insert into Employee(birth_date,first_name,last_name,gender,hire_date) values(:birth_date,:first_name,:last_name,:gender,:hire_date)";
	private static final String GET_EMPLOYEE_COUNT = "select count(*) as empCount from Employee where first_name=:first_name and last_name=:last_name";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	/**
	 * this method is used to get all the employees from database
	 *
	 */
	@Override
	public List<Employee> getEmployees() {
		List<Employee> employeeList = new ArrayList<Employee>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(GET_EMPLOYEES, new MapSqlParameterSource());
		for (Map<String, Object> row : rows) {
			Employee employee = getEmployeeObjFromRow(row);
			employeeList.add(employee);
		}
		return employeeList;
	}

	/**
	 * this method is used to get the employee using given employee number from
	 * database
	 *
	 */
	@Override
	public Employee getEmployeeById(int employeeNo) {
		Employee employee = null;
		try {
			SqlParameterSource parameters = new MapSqlParameterSource().addValue(EMPLOYEE_NO, employeeNo);
			Map<String, Object> row = jdbcTemplate.queryForMap(GET_EMPLOYEE_BY_ID, parameters);
			employee = getEmployeeObjFromRow(row);
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			System.out.println("no employee found for employeeNo" + employeeNo);
		}
		return employee;
	}

	/**
	 * this method is used to check if employee already exists
	 *
	 */
	public boolean isNewEmployee(Employee employee) {
		SqlParameterSource parameters = new MapSqlParameterSource().addValue(FIRST_NAME, employee.getFirstName())
				.addValue(LAST_NAME, employee.getLastName());
		Map<String, Object> row = jdbcTemplate.queryForMap(GET_EMPLOYEE_COUNT, parameters);
		long count = (long) row.get(EMP_COUNT);
		if (count == 0) {
			return true;
		}
		return false;
	}

	/**
	 * this method is used to create new employee to the database
	 *
	 */
	@Override
	public Employee createEmployee(Employee employee) {
		// check if employee is new employee
		if (isNewEmployee(employee)) {
			KeyHolder holder = new GeneratedKeyHolder();
			SqlParameterSource parameters = new MapSqlParameterSource().addValue(BIRTH_DATE, employee.getBirthDate())
					.addValue(FIRST_NAME, employee.getFirstName()).addValue(LAST_NAME, employee.getLastName())
					.addValue(GENDER, employee.getGender()).addValue(HIRE_DATE, employee.getHireDate());
			jdbcTemplate.update(INSERT_EMPLOYEES, parameters, holder);
			employee.setEmployeeNo(holder.getKey().intValue());
			return employee;
		}
		return null;
	}

	/**
	 * this method is used to update an employee with the given employee number in
	 * the database
	 *
	 */
	@Override
	public int updateEmployee(int employeeNo, Employee employee) {
		SqlParameterSource parameters = new MapSqlParameterSource().addValue(BIRTH_DATE, employee.getBirthDate())
				.addValue(FIRST_NAME, employee.getFirstName()).addValue(LAST_NAME, employee.getLastName())
				.addValue(GENDER, employee.getGender()).addValue(HIRE_DATE, employee.getHireDate())
				.addValue(EMPLOYEE_NO, employeeNo);
		return jdbcTemplate.update(UPDATE_EMPLOYEES, parameters);
	}

	/**
	 * this method is used to delete the employee from the database
	 *
	 */
	@Override
	public int deleteEmployee(int employeeNo) {
		SqlParameterSource parameters = new MapSqlParameterSource().addValue(EMPLOYEE_NO, employeeNo);
		return jdbcTemplate.update(DELETE_EMPLOYEES, parameters);
	}

	/**
	 * @param row
	 * @return
	 */
	private Employee getEmployeeObjFromRow(Map<String, Object> row) {
		Employee employee = new Employee();
		employee.setEmployeeNo((Integer) row.get(EMPLOYEE_NO));
		employee.setBirthDate((Date) row.get(BIRTH_DATE));
		employee.setFirstName((String) row.get(FIRST_NAME));
		employee.setLastName((String) row.get(LAST_NAME));
		employee.setGender((String) row.get(GENDER));
		employee.setHireDate((Date) row.get(HIRE_DATE));
		return employee;
	}

}
