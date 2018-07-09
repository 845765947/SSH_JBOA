package com.oyxy.dao;

import java.util.List;

import com.oyxy.entity.Employee;

public interface EmployeeDao extends BaseDao<Employee> {
	// �����Բ����û�
	public Employee findEmployeeByProperty(String propertyName, Object value);

	// ���ղ������Ʋ���
	public Employee findByPosition(String position);
}
