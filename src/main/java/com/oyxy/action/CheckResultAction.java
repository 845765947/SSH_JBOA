package com.oyxy.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.oyxy.Service.CheckResultService;
import com.oyxy.Service.ClaimVoucherService;
import com.oyxy.entity.CheckResult;
import com.oyxy.entity.ClaimVoucher;

public class CheckResultAction extends BaseAction<CheckResult> {
	// ���������ʵ��
	private CheckResult checkResult;

	private String sn;
	@Autowired
	@Qualifier("checkResultService")
	private CheckResultService checkResultService;

	// �����������
	public String checkClaimVoucher() {
		// ����ʵ������������Ҹ��±�����
		checkResultService.saveCheckResult(checkResult, checkResult.getClaimId(), sn);
		return "check_checkClaimVoucher";
	}

	public CheckResult getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(CheckResult checkResult) {
		this.checkResult = checkResult;
	}

	public CheckResultService getCheckResultService() {
		return checkResultService;
	}

	public void setCheckResultService(CheckResultService checkResultService) {
		this.checkResultService = checkResultService;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

}
