package com.oyxy.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.oyxy.Service.CheckResultService;
import com.oyxy.Service.ClaimVoucherService;
import com.oyxy.entity.CheckResult;
import com.oyxy.entity.ClaimVoucher;

public class CheckResultAction extends BaseAction<CheckResult> {
	// 审批结果表实体
	private CheckResult checkResult;

	private String sn;
	@Autowired
	@Qualifier("checkResultService")
	private CheckResultService checkResultService;

	// 处理审批结果
	public String checkClaimVoucher() {
		// 保存实体审批结果并且更新报销单
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
