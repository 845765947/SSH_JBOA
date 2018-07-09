package com.oyxy.Service;

import java.util.List;

import com.oyxy.entity.Employee;

public interface EmployeeService {
	// 登录
	public Employee findEmployeeByProperty(String name, String pwd);

	// 查找员工经理
	public Employee findByManager(int deptid);

}
