package com.oyxy.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.mapping.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oyxy.dao.EmployeeDao;
import com.oyxy.entity.Employee;

@Repository("empDao")
public class EmployeeDaoImpl extends BaseHibernateDaoSupport<Employee> implements EmployeeDao {

	@Autowired
	public EmployeeDaoImpl(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	public Employee findEmployeeByProperty(String propertyName, Object value) {
		String hql = "from Employee as e where e." + propertyName + "=?";
		if (propertyName.equals("POSITION_ID")) {
			hql = "from Employee as e where e." + propertyName + "=? and e.sysPosition.id=2";
		}
		List<Employee> list = this.getHibernateTemplate().find(hql, value);
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

	@Override
	public Employee findByPosition(String position) {
		String hql = "from Employee e where e.sysPosition.nameCn=? and  e.status='ÔÚÖ°'";
		List<Employee> list = this.getHibernateTemplate().find(hql, position);
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

}
