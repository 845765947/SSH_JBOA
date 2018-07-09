package com.oyxy.dao;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T> {
	public void save(T instance);

	public void update(T instance);

	public void delete(T instance);

	public List<T> findByall(); // from xxx

	public <E> List<E> fingForPage(String hql, int pageNo, int pageSize, Object... values);

	public Number getTotalCount(String hql, Object... values);

	public <E> List<E> find(String hql, Object... values);

	public T get(Serializable id);
}
