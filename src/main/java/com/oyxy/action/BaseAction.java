package com.oyxy.action;

import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.oyxy.common.Constants;
import com.oyxy.entity.Employee;
import com.oyxy.util.PaginationSupport;

public class BaseAction<T> extends ActionSupport {

	protected Integer pageNo = 1;
	protected Integer pageSize = 5;
	protected PaginationSupport<T> pageSupport;

	protected Map<String, Object> getSession() {
		ActionContext actionContext = ActionContext.getContext();
		return actionContext.getSession();
	}

	protected Employee getLoginEmployee() {
		return (Employee) this.getSession().get(Constants.AUTH_EMPLOYEE);
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public PaginationSupport<T> getPageSupport() {
		return pageSupport;
	}

	public void setPageSupport(PaginationSupport<T> pageSupport) {
		this.pageSupport = pageSupport;
	}

}
