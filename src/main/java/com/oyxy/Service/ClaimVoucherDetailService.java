package com.oyxy.Service;

import com.oyxy.entity.ClaimVoucher;
import com.oyxy.entity.ClaimVoucherDetail;

public interface ClaimVoucherDetailService {
	// ɾ����������ϸ
	public int DeleteByClaimVoucherDeteail(ClaimVoucher claimVoucher);

	// ���±�������ϸ
	public void update(ClaimVoucher claimVoucher);
}
