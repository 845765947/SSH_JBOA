package com.oyxy.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.oyxy.entity.ClaimVoucher;
import com.oyxy.entity.ClaimVoucherDetail;

public interface ClaimVoucherDetailDao extends BaseDao<ClaimVoucher> {
	// ɾ����������ϸ
	public int DeleteByClaimVoucherDeteail(ClaimVoucher claimVoucher);
}
