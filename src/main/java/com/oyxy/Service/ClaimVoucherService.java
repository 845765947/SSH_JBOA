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
	 * 分页
	 */
	public PaginationSupport<ClaimVoucher> getClaimVoucherPage(Employee emp, String status, Date startDate,
			Date endDate, Integer pageNo, Integer pageSize);

	/**
	 * 查找报销单
	 */
	public ClaimVoucher findByClaimVoucher(long id);

	/**
	 * 删除报销单以及明细
	 */
	public void deleteByClaimVoucher(ClaimVoucher claimVoucher);
}
