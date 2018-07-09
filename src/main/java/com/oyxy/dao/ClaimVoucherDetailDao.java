package com.oyxy.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.oyxy.entity.ClaimVoucher;
import com.oyxy.entity.ClaimVoucherDetail;

public interface ClaimVoucherDetailDao extends BaseDao<ClaimVoucher> {
	// 删除报销单明细
	public int DeleteByClaimVoucherDeteail(ClaimVoucher claimVoucher);
}
