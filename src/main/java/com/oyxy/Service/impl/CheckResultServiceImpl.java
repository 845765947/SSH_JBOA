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
		// ��ȡ������
		ClaimVoucher claimVoucher = claimDao.get(id);
		// ����ʱ��
		checkResult.setCheckTime(new Date());
		// ���ø���ʱ��
		claimVoucher.setModifyTime(checkResult.getCheckTime());
		// ���±�����
		this.UpdateClaimVoucherStatus(checkResult.getCheckEmployee().getSysPosition().getNameCn(),
				checkResult.getResult(), claimVoucher);
		// �����������
		checkDao.save(checkResult);

	}

	protected void UpdateClaimVoucherStatus(String position, String checkResult, ClaimVoucher claimVoucher) {
		// ͨ��
		if (checkResult.equals(Constants.CHECKRESULT_PASS)) {
			// ���ž���
			if (position.equals(Constants.POSITION_FM)) {
				// ����Ƿ����5000
				if (claimVoucher.getTotalAccount() >= 5000) {
					// ������һ��������
					claimVoucher.setStatus(Constants.CLAIMVOUCHER_APPROVED);
					claimVoucher.setNextDeal(empDao.findByPosition(Constants.POSITION_GM));
				} else {
					claimVoucher.setStatus(Constants.CLAIMVOUCHER_APPROVED);
					claimVoucher.setNextDeal(empDao.findByPosition(Constants.POSITION_CASHIER));
				}
				// �ܾ���
			} else if (position.equals(Constants.POSITION_GM)) {
				claimVoucher.setStatus(Constants.CLAIMVOUCHER_APPROVED);
				claimVoucher.setNextDeal(empDao.findByPosition(Constants.POSITION_CASHIER));
				// ���
			} else {
				claimVoucher.setStatus(Constants.CLAIMVOUCHER_PAID);
				claimVoucher.setNextDeal(null);
			}
			// ���
		} else if (checkResult.equals(Constants.CHECKRESULT_BACK)) {
			claimVoucher.setStatus(Constants.CLAIMVOUCHER_BACK);
			claimVoucher.setNextDeal(claimVoucher.getCreator());
			// �ܾ�
		} else {
			claimVoucher.setStatus(Constants.CLAIMVOUCHER_TERMINATED);
			claimVoucher.setNextDeal(null);
		}
		// ���±�����
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
