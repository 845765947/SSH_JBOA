package com.oyxy.action;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.oyxy.Service.LeaveService;
import com.oyxy.common.Constants;
import com.oyxy.entity.Employee;
import com.oyxy.entity.Leave;

public class LeaveAction extends BaseAction<Leave> {
	@Autowired
	@Qualifier("leaveService")
	private LeaveService leaveService;

	private String leaveType;

	private Leave leave;

	private String empId;

	private Integer pageNo;

	private Date startDate;

	private Date endDate;

	private Integer pageSize;

	private static Map<String, String> leaveTypeMap;

	private static Map<String, String> statusMap;

	static {
		statusMap = new HashMap<String, String>();
		statusMap.put(Constants.LEAVESTATUS_APPROVING, "待审批");
		statusMap.put(Constants.LEAVESTATUS_APPROVED, "已审批");
		statusMap.put(Constants.LEAVESTATUS_BACK, "已打回");

		leaveTypeMap = new HashMap<String, String>();
		leaveTypeMap.put(Constants.LEAVE_SICK, "病假");
		leaveTypeMap.put(Constants.LEAVE_ANNUAL, "年假");
		leaveTypeMap.put(Constants.LEAVE_CASUAL, "事假");
		leaveTypeMap.put(Constants.LEAVE_MARRIAGE, "婚假");
		leaveTypeMap.put(Constants.LEAVE_MATERNITY, "产假");

	}

	public String searchLeave() {
		pageSupport = leaveService.getClaimVoucherPage(this.getLoginEmployee(), leaveType, startDate, endDate, pageNo,
				pageSize);
		return "leave_searchLeave";
	}

	// 跳转添加视图
	public String toEdit() {
		return "leave_edit_view";
	}

	// 保存请假信息
	public String saveLeave() {
		leave.setCreator(this.getLoginEmployee());
		leave.setCreateTime(new Date());
		leave.setModifyTime(new Date());
		leave.setStatus(Constants.LEAVESTATUS_APPROVING);
		leaveService.saveLeave(leave, (Employee) this.getSession().get(Constants.AUTH_EMPLOYEE_MANAGER));
		return "leave_edit_save";
	}

	// 查看请假详情
	public String getLeaveById() {
		leave = leaveService.fingByLeave(leave.getId());
		return "leave_view";
	}

	// 进入审批请假单视图
	public String toCheck() {
		leave = leaveService.fingByLeave(leave.getId());
		return "leave_update_view";
	}

	// 进行请假单更新操作
	public String checkLeave() {
		leaveService.updateLeave(leave);
		return "leave_update";
	}

	public LeaveService getLeaveService() {
		return leaveService;
	}

	public void setLeaveService(LeaveService leaveService) {
		this.leaveService = leaveService;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public Leave getLeave() {
		return leave;
	}

	public void setLeave(Leave leave) {
		this.leave = leave;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Map<String, String> getLeaveTypeMap() {
		return leaveTypeMap;
	}

	public void setLeaveTypeMap(Map<String, String> leaveTypeMap) {
		LeaveAction.leaveTypeMap = leaveTypeMap;
	}

	public Map<String, String> getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map<String, String> statusMap) {
		LeaveAction.statusMap = statusMap;
	}

}
