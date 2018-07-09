package com.oyxy.impl;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.oyxy.Service.ClaimVoucherService;
import com.oyxy.action.BaseAction;
import com.oyxy.dao.ClaimVoucherDetailDao;
import com.oyxy.entity.ClaimVoucher;
import com.oyxy.entity.ClaimVoucherDetail;
import com.oyxy.util.PaginationSupport;

@Repository("detaildao")
public class ClaimVoucherDetailDaoImpl extends BaseHibernateDaoSupport<ClaimVoucher> implements ClaimVoucherDetailDao {
	@Autowired
	public ClaimVoucherDetailDaoImpl(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	@Override
	public int DeleteByClaimVoucherDeteail(ClaimVoucher claimVoucher) {
		String hql = "delete from ClaimVoucherDetail where bizClaimVoucher.id=?";
		return this.getHibernateTemplate().bulkUpdate(hql, claimVoucher.getId());
	}

}
