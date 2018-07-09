package com.oyxy.Service;

import com.oyxy.entity.ClaimVoucher;
import com.oyxy.entity.ClaimVoucherDetail;

public interface ClaimVoucherDetailService {
	// 删除报销单明细
	public int DeleteByClaimVoucherDeteail(ClaimVoucher claimVoucher);

	// 更新报销单明细
	public void update(ClaimVoucher claimVoucher);
}
