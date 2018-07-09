package com.oyxy.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.oyxy.dao.CheckResultDao;
import com.oyxy.entity.CheckResult;
import com.oyxy.entity.Employee;

@Repository("checkDao")
public class CheckResultDaoImpl extends BaseHibernateDaoSupport<CheckResult> implements CheckResultDao {
	@Autowired
	public CheckResultDaoImpl(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	
}
