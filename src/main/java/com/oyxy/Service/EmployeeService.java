package com.oyxy.Service;

import java.util.List;

import com.oyxy.entity.Employee;

public interface EmployeeService {
	// ��¼
	public Employee findEmployeeByProperty(String name, String pwd);

	// ����Ա������
	public Employee findByManager(int deptid);

}
