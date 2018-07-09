package com.oyxy.Service;

import com.oyxy.entity.CheckResult;
import com.oyxy.entity.ClaimVoucher;

public interface CheckResultService {

	public void saveCheckResult(CheckResult checkResult, Long id, String sn);
}
