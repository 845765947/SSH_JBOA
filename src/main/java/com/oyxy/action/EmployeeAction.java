package com.oyxy.action;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.oyxy.Service.EmployeeService;
import com.oyxy.common.Constants;
import com.oyxy.entity.Employee;

public class EmployeeAction extends BaseAction {
	private Employee employee;
	private EmployeeService empService;
	private String msg;
	private String random;

	// 登录
	public String login() {
		ActionContext ac = ActionContext.getContext();
		if (random == null || random.equals("")) {
			this.setMsg("验证码错误，请重试");
			return "login_error";
		}
		if (!ac.getSession().get("random").equals(random)) {
			this.setMsg("验证码错误，请重试");
			return "login_error";
		}
		if (employee.getSn() == null || employee.getSn().equals("")) {
			this.setMsg("用户名不能为空");
			return "login_error";
		}
		if (employee.getPassword() == null || employee.getPassword().equals("")) {
			this.setMsg("密码不能为空");
			return "login_error";
		}
		Employee emp = empService.findEmployeeByProperty(employee.getSn(), employee.getPassword());
		if (emp != null) {
			// 经理
			Employee manager = empService.findByManager(emp.getSysDepartment().getId());
			// 把经理添加到session中
			ac.getSession().put(Constants.AUTH_EMPLOYEE_MANAGER, manager);
			ac.getSession().put(Constants.AUTH_EMPLOYEE, emp);
			// 根据页面素材把职位添加到session中
			ac.getSession().put(Constants.EMPLOYEE_POSITION, emp.getSysPosition().getNameCn());
			return "login_success";
		}
		this.setMsg("用户名或密码错误");
		return "login_error";

	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public EmployeeService getEmpService() {
		return empService;
	}

	public void setEmpService(EmployeeService empService) {
		this.empService = empService;
	}
}
