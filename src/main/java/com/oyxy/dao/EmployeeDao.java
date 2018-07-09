package com.oyxy.dao;

import java.util.List;

import com.oyxy.entity.Employee;

public interface EmployeeDao extends BaseDao<Employee> {
	// 按属性查找用户
	public Employee findEmployeeByProperty(String propertyName, Object value);

	// 按照部门名称查找
	public Employee findByPosition(String position);
}
