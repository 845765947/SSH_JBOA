package com.oyxy.Service;

import java.util.Date;

import com.oyxy.entity.ClaimVoucher;
import com.oyxy.entity.Employee;
import com.oyxy.entity.Leave;
import com.oyxy.util.PaginationSupport;

public interface LeaveService {

	/**
	 * ��ҳ
	 */
	public PaginationSupport<Leave> getClaimVoucherPage(Employee emp, String leaveType, Date startDate, Date endDate,
			Integer pageNo, Integer pageSize);

	/**
	 * ���������Ϣ
	 */
	public void saveLeave(Leave leave, Employee manager);

	/**
	 * �鿴�������
	 */
	public Leave fingByLeave(Long id);

	/**
	 * �������
	 */
	public void updateLeave(Leave leave);
}
