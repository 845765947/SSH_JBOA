package com.oyxy.entity;

public class Employee implements java.io.Serializable {
	private static final long serialVersionUID = 1035721299787840813L;
	private String sn; // Ô±¹¤±àºÅ
	private String password;
	private String name;
	private String status;

	private Department sysDepartment;
	private Position sysPosition;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Department getSysDepartment() {
		return sysDepartment;
	}

	public void setSysDepartment(Department sysDepartment) {
		this.sysDepartment = sysDepartment;
	}

	public Position getSysPosition() {
		return sysPosition;
	}

	public void setSysPosition(Position sysPosition) {
		this.sysPosition = sysPosition;
	}

}
