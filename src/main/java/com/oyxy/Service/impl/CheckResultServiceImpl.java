package com.oyxy.Service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.oyxy.Service.CheckResultService;
import com.oyxy.common.Constants;
import com.oyxy.dao.CheckResultDao;
import com.oyxy.dao.ClaimVoucherDao;
import com.oyxy.dao.EmployeeDao;
import com.oyxy.entity.CheckResult;
import com.oyxy.entity.ClaimVoucher;
import com.oyxy.entity.Employee;

@Service("checkResultService")
public class CheckResultServiceImpl implements CheckResultService {
	@Autowired
	@Qualifier("checkDao")
	private CheckResultDao checkDao;

	@Autowired
	@Qualifier("claimDao")
	private ClaimVoucherDao claimDao;

	@Autowired
	@Qualifier("empDao")
	private EmployeeDao empDao;

	@Override
	public void saveCheckResult(CheckResult checkResult, Long id, String sn) {
		Employee emp=empDao.get(sn);
		checkResult.setCheckEmployee(emp);
		// 获取报销单
		ClaimVoucher claimVoucher = claimDao.get(id);
		// 设置时间
		checkResult.setCheckTime(new Date());
		// 设置更新时间
		claimVoucher.setModifyTime(checkResult.getCheckTime());
		// 更新报销单
		this.UpdateClaimVoucherStatus(checkResult.getCheckEmployee().getSysPosition().getNameCn(),
				checkResult.getResult(), claimVoucher);
		// 保存审批结果
		checkDao.save(checkResult);

	}

	protected void UpdateClaimVoucherStatus(String position, String checkResult, ClaimVoucher claimVoucher) {
		// 通过
		if (checkResult.equals(Constants.CHECKRESULT_PASS)) {
			// 部门经理
			if (position.equals(Constants.POSITION_FM)) {
				// 金额是否大于5000
				if (claimVoucher.getTotalAccount() >= 5000) {
					// 设置下一个处理人
					claimVoucher.setStatus(Constants.CLAIMVOUCHER_APPROVED);
					claimVoucher.setNextDeal(empDao.findByPosition(Constants.POSITION_GM));
				} else {
					claimVoucher.setStatus(Constants.CLAIMVOUCHER_APPROVED);
					claimVoucher.setNextDeal(empDao.findByPosition(Constants.POSITION_CASHIER));
				}
				// 总经理
			} else if (position.equals(Constants.POSITION_GM)) {
				claimVoucher.setStatus(Constants.CLAIMVOUCHER_APPROVED);
				claimVoucher.setNextDeal(empDao.findByPosition(Constants.POSITION_CASHIER));
				// 会计
			} else {
				claimVoucher.setStatus(Constants.CLAIMVOUCHER_PAID);
				claimVoucher.setNextDeal(null);
			}
			// 打回
		} else if (checkResult.equals(Constants.CHECKRESULT_BACK)) {
			claimVoucher.setStatus(Constants.CLAIMVOUCHER_BACK);
			claimVoucher.setNextDeal(claimVoucher.getCreator());
			// 拒绝
		} else {
			claimVoucher.setStatus(Constants.CLAIMVOUCHER_TERMINATED);
			claimVoucher.setNextDeal(null);
		}
		// 更新报销单
		claimDao.update(claimVoucher);
	}

	public EmployeeDao getEmpDao() {
		return empDao;
	}

	public void setEmpDao(EmployeeDao empDao) {
		this.empDao = empDao;
	}

	public CheckResultDao getCheckDao() {
		return checkDao;
	}

	public void setCheckDao(CheckResultDao checkDao) {
		this.checkDao = checkDao;
	}

	public ClaimVoucherDao getClaimDao() {
		return claimDao;
	}

	public void setClaimDao(ClaimVoucherDao claimDao) {
		this.claimDao = claimDao;
	}

}
