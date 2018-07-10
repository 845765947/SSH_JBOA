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

		// 判断是否为第一次进入
		if (pageNo == null) {
			pageNo = 1;
		}

		if (pageSize == null) {
			pageSize = 5;
		}

		// 对于用户输入的页码进行设置
		if (pageNo > 0) {
			request.setCurrPageNo(pageNo);
		}

		if (pageSize > 0) {
			request.setPageSize(pageSize);
		}
		// 根据数据请求写入hql
		StringBuffer hql = new StringBuffer();
		List<Object> values = new ArrayList<Object>();

		if (emp.getSysPosition().getNameCn().equals(Constants.POSITION_FM)) {
			hql.append("from Leave l where l.creator.sysDepartment.name=? ");
			values.add(emp.getSysDepartment().getName());
			// 判断是否有选择类型
			if (leaveType != null && !leaveType.equals("")) {
				hql.append(" and l.status=? ");
				values.add(leaveType);
			} else {
				hql.append("and l.status='待审批'");
			}
		} else {
			hql.append("from Leave l where l.creator.sn=? ");
			values.add(emp.getSn());
			// 判断是否有选择类型
			if (leaveType != null && !leaveType.equals("")) {
				hql.append(" and l.leaveType=? ");
				values.add(leaveType);
			}
		}

		// 判断是否有开始时间和结束时间
		if (startDate != null) {
			hql.append(" and c.createTime >=? ");
			values.add(startDate);
		}
		if (endDate != null) {
			hql.append(" and c.createTime <=? "); // 设置日期延后
			Calendar oneDayLet = Calendar.getInstance();
			oneDayLet.setTime(endDate);
			oneDayLet.add(Calendar.DAY_OF_MONTH, 1);
			values.add(oneDayLet.getTime());
			values.add(endDate);
		}
		// 把弄好的sql以及参数发送给dao层次进行处理,获取总记录数
		int count = claimDao.getTotalCount(hql.toString(), values.toArray()).intValue();
		// 设置返回的总记录数
		request.setTotalCount(count);
		// 如何用户输入页数超出最大页数
		if (request.getCurrPageNo() > request.getTotalPageCount()) {
			request.setCurrPageNo(request.getTotalPageCount());
		}
		hql.append(" order by createTime desc");
		List<Leave> items = claimDao.fingForPage(hql.toString(), request.getCurrPageNo(), request.getPageSize(),
				values.toArray());
		// 把找到的items进行绑定
		request.setItems(items);
		return request;
	}

	// 保存方法
	public void saveLeave(Leave leave, Employee manager) {
		leave.setNextDeal(manager);
		leaveDao.save(leave);
	}

	@Override
	// 审批报销单方法
	public void updateLeave(Leave leave) {
		Leave tocheck = leaveDao.get(leave.getId());
		// 设置状态
		tocheck.setStatus(leave.getStatus());
		tocheck.setApproveOpinion(leave.getApproveOpinion());
		tocheck.setModifyTime(new Date());
		if (tocheck.getStatus().equals(Constants.LEAVESTATUS_APPROVED)) {
			tocheck.setStatus("已审批");
		} else {
			tocheck.setNextDeal(tocheck.getCreator());
			tocheck.setStatus("已打回");
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
