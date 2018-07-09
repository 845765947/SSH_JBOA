package com.oyxy.dao;

import java.util.Date;

import com.oyxy.entity.ClaimVoucher;
import com.oyxy.util.PaginationSupport;

public interface ClaimVoucherDao extends BaseDao<ClaimVoucher> {
	// É¾³ý±¨Ïúµ¥
	public void deleteByClaimVoucher(ClaimVoucher claimVoucher);
}
