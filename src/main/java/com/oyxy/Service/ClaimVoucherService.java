package com.oyxy.Service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.oyxy.entity.ClaimVoucher;
import com.oyxy.entity.Employee;
import com.oyxy.util.PaginationSupport;

public interface ClaimVoucherService {
	public void saveClaimVoucher(ClaimVoucher claimVoucher);

	/**
	 * ��ҳ
	 */
	public PaginationSupport<ClaimVoucher> getClaimVoucherPage(Employee emp, String status, Date startDate,
			Date endDate, Integer pageNo, Integer pageSize);

	/**
	 * ���ұ�����
	 */
	public ClaimVoucher findByClaimVoucher(long id);

	/**
	 * ɾ���������Լ���ϸ
	 */
	public void deleteByClaimVoucher(ClaimVoucher claimVoucher);
}
