package com.oyxy.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.oyxy.dao.EmployeeDao;
import com.oyxy.dao.LeaveDao;
import com.oyxy.entity.Employee;
import com.oyxy.entity.Leave;

@Repository("leaveDao")
public class LeaveDaoImpl extends BaseHibernateDaoSupport<Leave> implements LeaveDao {

	@Autowired
	public LeaveDaoImpl(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}
}
