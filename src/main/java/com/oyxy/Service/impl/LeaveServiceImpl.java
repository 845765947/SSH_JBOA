package com.oyxy.Service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.oyxy.Service.LeaveService;
import com.oyxy.common.Constants;
import com.oyxy.dao.ClaimVoucherDao;
import com.oyxy.dao.EmployeeDao;
import com.oyxy.dao.LeaveDao;
import com.oyxy.entity.ClaimVoucher;
import com.oyxy.entity.Employee;
import com.oyxy.entity.Leave;
import com.oyxy.util.PaginationSupport;

@Service("leaveService")
public class LeaveServiceImpl implements LeaveService {
	@Autowired
	@Qualifier("leaveDao")
	private LeaveDao leaveDao;
	@Autowired
	@Qualifier("claimDao")
	private ClaimVoucherDao claimDao;

	@Override
	public PaginationSupport<Leave> getClaimVoucherPage(Employee emp, String leaveType, Date startDate, Date endDate,
			Integer pageNo, Integer pageSize) {

		PaginationSupport<Leave> request = new PaginationSupport<Leave>();

		// �ж��Ƿ�Ϊ��һ�ν���
		if (pageNo == null) {
			pageNo = 1;
		}

		if (pageSize == null) {
			pageSize = 5;
		}

		// �����û������ҳ���������
		if (pageNo > 0) {
			request.setCurrPageNo(pageNo);
		}

		if (pageSize > 0) {
			request.setPageSize(pageSize);
		}
		// ������������д��hql
		StringBuffer hql = new StringBuffer();
		List<Object> values = new ArrayList<Object>();

		if (emp.getSysPosition().getNameCn().equals(Constants.POSITION_FM)) {
			hql.append("from Leave l where l.creator.sysDepartment.name=? ");
			values.add(emp.getSysDepartment().getName());
			// �ж��Ƿ���ѡ������
			if (leaveType != null && !leaveType.equals("")) {
				hql.append(" and l.status=? ");
				values.add(leaveType);
			} else {
				hql.append("and l.status='������'");
			}
		} else {
			hql.append("from Leave l where l.creator.sn=? ");
			values.add(emp.getSn());
			// �ж��Ƿ���ѡ������
			if (leaveType != null && !leaveType.equals("")) {
				hql.append(" and l.leaveType=? ");
				values.add(leaveType);
			}
		}

		// �ж��Ƿ��п�ʼʱ��ͽ���ʱ��
		if (startDate != null) {
			hql.append(" and c.createTime >=? ");
			values.add(startDate);
		}
		if (endDate != null) {
			hql.append(" and c.createTime <=? "); // ���������Ӻ�
			Calendar oneDayLet = Calendar.getInstance();
			oneDayLet.setTime(endDate);
			oneDayLet.add(Calendar.DAY_OF_MONTH, 1);
			values.add(oneDayLet.getTime());
			values.add(endDate);
		}
		// ��Ū�õ�sql�Լ��������͸�dao��ν��д���,��ȡ�ܼ�¼��
		int count = claimDao.getTotalCount(hql.toString(), values.toArray()).intValue();
		// ���÷��ص��ܼ�¼��
		request.setTotalCount(count);
		// ����û�����ҳ���������ҳ��
		if (request.getCurrPageNo() > request.getTotalPageCount()) {
			request.setCurrPageNo(request.getTotalPageCount());
		}
		hql.append(" order by createTime desc");
		List<Leave> items = claimDao.fingForPage(hql.toString(), request.getCurrPageNo(), request.getPageSize(),
				values.toArray());
		// ���ҵ���items���а�
		request.setItems(items);
		return request;
	}

	// ���淽��
	public void saveLeave(Leave leave, Employee manager) {
		leave.setNextDeal(manager);
		leaveDao.save(leave);
	}

	@Override
	// ��������������
	public void updateLeave(Leave leave) {
		Leave tocheck = leaveDao.get(leave.getId());
		// ����״̬
		tocheck.setStatus(leave.getStatus());
		tocheck.setApproveOpinion(leave.getApproveOpinion());
		tocheck.setModifyTime(new Date());
		if (tocheck.getStatus().equals(Constants.LEAVESTATUS_APPROVED)) {
			tocheck.setStatus("������");
		} else {
			tocheck.setNextDeal(tocheck.getCreator());
			tocheck.setStatus("�Ѵ��");
		}
	}

	@Override
	public Leave fingByLeave(Long id) {
		return leaveDao.get(id);
	}

	public ClaimVoucherDao getClaimDao() {
		return claimDao;
	}

	public void setClaimDao(ClaimVoucherDao claimDao) {
		this.claimDao = claimDao;
	}

	public LeaveDao getLeaveDao() {
		return leaveDao;
	}

	public void setLeaveDao(LeaveDao leaveDao) {
		this.leaveDao = leaveDao;
	}

}
