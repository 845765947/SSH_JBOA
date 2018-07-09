package com.oyxy.Service.impl;

import java.util.Calendar;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.oyxy.entity.Employee;

@Service("claServiceStuff")
public class ClaimVoucherServiceImplStuff extends ClaimVoucherServiceImpl {

	@Override
	protected void ClaimVoucherHqlBuffer(Employee emp, String status, Date startDate, Date endDate, StringBuffer hql,
			List<Object> values) {
		// ����û���id values.add(emp.getSn());
		hql.append("from ClaimVoucher c where c.creator.sn=?");

		// ����û����
		values.add(emp.getSn());

		if (startDate != null) {
			hql.append(" and c.createTime >=? ");
		}
		if (endDate != null) {
			hql.append(" and c.createTime <=? "); // ���������Ӻ�
			Calendar oneDayLet = Calendar.getInstance();
			oneDayLet.setTime(endDate);
			oneDayLet.add(Calendar.DAY_OF_MONTH, 1);
			values.add(oneDayLet.getTime());
			values.add(endDate);
		}

		if (status != null && !status.equals("")) {
			hql.append(" and c.status=? ");
			values.add(status);
		}

	}

	@Override
	protected void OrederByHql(StringBuffer hql, String status) {
		hql.append(" order by c.createTime desc ");
	}
}
