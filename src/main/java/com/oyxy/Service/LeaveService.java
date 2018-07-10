package com.oyxy.Service;

import java.util.Date;

import com.oyxy.entity.ClaimVoucher;
import com.oyxy.entity.Employee;
import com.oyxy.entity.Leave;
import com.oyxy.util.PaginationSupport;

public interface LeaveService {

	/**
	 * 分页
	 */
	public PaginationSupport<Leave> getClaimVoucherPage(Employee emp, String leaveType, Date startDate, Date endDate,
			Integer pageNo, Integer pageSize);

	/**
	 * 保存请假信息
	 */
	public void saveLeave(Leave leave, Employee manager);

	/**
	 * 查看请假详情
	 */
	public Leave fingByLeave(Long id);

	/**
	 * 审批请假
	 */
	public void updateLeave(Leave leave);
}
