package com.oyxy.Service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.oyxy.entity.Employee;

@Service("claServiceFM")
public class ClaimVoucherServiceImplFM extends ClaimVoucherServiceImpl {

	@Override
	protected void ClaimVoucherHqlBuffer(Employee emp, String status, Date startDate, Date endDate, StringBuffer hql,
			List<Object> values) {
		// ����û���id values.add(emp.getSn());
		hql.append("from ClaimVoucher c where c.nextDeal.sn=? and c.creator.sysDepartment.id=?");
		// ����û����
		values.add(emp.getSn());
		values.add(emp.getSysDepartment().getId());
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

		hql.append(" and c.status>=? ");
		values.add(status);
	}

	@Override
	protected void OrederByHql(StringBuffer hql, String status) {
		if (status == null) {
			hql.append(" order by c.createTime desc,status");
		} else {
			hql.append(" order by c.createTime desc ");
		}
	}
}
