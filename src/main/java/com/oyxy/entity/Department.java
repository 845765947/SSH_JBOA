package com.oyxy.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 部门实体类
 */
public class Department implements java.io.Serializable {

	private static final long serialVersionUID = -4386159382927256061L;
	private Integer id;
	private String name;
	private Set<Employee> sysEmployees = new HashSet<Employee>(0);

	/** default constructor */
	public Department() {
	}

	/** minimal constructor */
	public Department(String name) {
		this.name = name;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Employee> getSysEmployees() {
		return this.sysEmployees;
	}

	public void setSysEmployees(Set<Employee> sysEmployees) {
		this.sysEmployees = sysEmployees;
	}

}