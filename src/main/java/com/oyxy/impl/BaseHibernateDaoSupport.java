package com.oyxy.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.oyxy.dao.BaseDao;

@Repository("baseDao")
public abstract class BaseHibernateDaoSupport<T> extends HibernateDaoSupport implements BaseDao<T> {

	private Class<T> cls;

	public BaseHibernateDaoSupport() {
		cls = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void save(T instance) {
		this.getHibernateTemplate().save(instance);
	}

	public void update(T instance) {
		this.getHibernateTemplate().update(instance);
	}

	public void delete(T instance) {
		this.getHibernateTemplate().delete(instance);
	}

	public List<T> findByall() {
		return this.getHibernateTemplate().find("from " + cls.getSimpleName()); // from xxx
	}

	public <E> List<E> fingForPage(final String hql, final int pageNo, final int pageSize, final Object... values) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<E>>() {
			public List<E> doInHibernate(Session session) {
				Query query = session.createQuery(hql);
				if (values != null && values.length != 0) {
					for (int i = 0; i < values.length; ++i) {
						query.setParameter(i, values[i]);

						System.out.println("=========" + values[i]);
					}
				}
				query.setFirstResult((pageNo - 1) * pageSize);
				query.setMaxResults(pageSize);
				System.out.println(query);
				return query.list();
			}
		});
	}

	public Number getTotalCount(String hql, Object... values) {
		if (hql.startsWith("select ")) {
			hql = "select count(*) " + hql.substring(hql.indexOf(" from ") + 1);
		} else {
			hql = "select count(*) " + hql;
		}
		return (Number) this.getHibernateTemplate().find(hql, values).get(0);
	}

	public <E> List<E> find(String hql, Object... values) {
		return this.getHibernateTemplate().find(hql, values);
	}

	public T get(Serializable id) {
		return this.getHibernateTemplate().get(cls, id);
	}

}
