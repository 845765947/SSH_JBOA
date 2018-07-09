package com.oyxy.Service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.oyxy.Service.ClaimVoucherDetailService;
import com.oyxy.dao.ClaimVoucherDetailDao;
import com.oyxy.entity.ClaimVoucher;
import com.oyxy.entity.ClaimVoucherDetail;

@Service("claiDetailService")
public class ClaimVoucherDetailServiceImpl implements ClaimVoucherDetailService {

	@Resource
	private ClaimVoucherDetailDao detaildao;

	@Override
	public int DeleteByClaimVoucherDeteail(ClaimVoucher claimVoucher) {
		return detaildao.DeleteByClaimVoucherDeteail(claimVoucher);
	}

	public ClaimVoucherDetailDao getDetaildao() {
		return detaildao;
	}

	public void setDetaildao(ClaimVoucherDetailDao detaildao) {
		this.detaildao = detaildao;
	}

	@Override
	public void update(ClaimVoucher claimVoucher) {
		detaildao.update(claimVoucher);
	}

}
