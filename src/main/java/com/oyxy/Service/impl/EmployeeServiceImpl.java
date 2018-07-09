package com.oyxy.Service.impl;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.oyxy.Service.EmployeeService;
import com.oyxy.dao.EmployeeDao;
import com.oyxy.entity.Employee;
import com.oyxy.util.MD5;

import jdk.nashorn.internal.ir.annotations.Reference;

@Service("empService")
public class EmployeeServiceImpl implements EmployeeService {

	@Resource
	private EmployeeDao empDao;

	public Employee findEmployeeByProperty(String sn, String pwd) {
		Employee emp = null;
		try {
			Employee employee = empDao.findEmployeeByProperty("sn", sn);
			if (employee != null) {
				// MD5º”√‹
				pwd = MD5.MD5Encode(pwd);
				if (pwd.equals(employee.getPassword())) {
					emp = employee;
					return emp;
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public Employee findByManager(int deptid) {
		Employee manager = null;
		try {
			manager = empDao.findEmployeeByProperty("sysDepartment.id", deptid);
			if (manager != null) {
				return manager;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

}
