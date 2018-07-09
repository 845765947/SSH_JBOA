package com.oyxy.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.oyxy.dao.ClaimVoucherDao;
import com.oyxy.entity.ClaimVoucher;
import com.oyxy.util.PaginationSupport;

@Repository("claimDao")
public class ClaimVoucherDaoImpl extends BaseHibernateDaoSupport<ClaimVoucher> implements ClaimVoucherDao {
	@Autowired
	public ClaimVoucherDaoImpl(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	@Override
	public void deleteByClaimVoucher(ClaimVoucher claimVoucher) {

	}

}
