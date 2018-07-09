package com.oyxy.Service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jfree.ui.UIUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.oyxy.Service.ClaimVoucherService;
import com.oyxy.dao.ClaimVoucherDao;
import com.oyxy.entity.ClaimVoucher;
import com.oyxy.entity.Employee;
import com.oyxy.util.PaginationSupport;

@Service("claService")
public abstract class ClaimVoucherServiceImpl implements ClaimVoucherService {
	@Autowired
	@Qualifier("claimDao")
	private ClaimVoucherDao claimDao;

	@Override
	public void saveClaimVoucher(ClaimVoucher claimVoucher) {
		claimVoucher.setCreateTime(new Date());
		claimVoucher.setModifyTime(claimVoucher.getCreateTime());
		claimDao.save(claimVoucher);
	}

	@Override
	public PaginationSupport<ClaimVoucher> getClaimVoucherPage(Employee emp, String status, Date startDate,
			Date endDate, Integer pageNo, Integer pageSize) {
		PaginationSupport<ClaimVoucher> request = new PaginationSupport<ClaimVoucher>();

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
		this.ClaimVoucherHqlBuffer(emp, status, startDate, endDate, hql, values);
		// ��Ū�õ�sql�Լ��������͸�dao��ν��д���,��ȡ�ܼ�¼��
		int count = claimDao.getTotalCount(hql.toString(), values.toArray()).intValue();
		// ���÷��ص��ܼ�¼��
		request.setTotalCount(count);
		// ����û�����ҳ���������ҳ��
		if (request.getCurrPageNo() > request.getTotalPageCount()) {
			request.setCurrPageNo(request.getTotalPageCount());
		}
		// ���ø����������hql��ƴ������
		this.OrederByHql(hql, status);
		List<ClaimVoucher> items = claimDao.fingForPage(hql.toString(), request.getCurrPageNo(), request.getPageSize(),
				values.toArray());

		request.setItems(items);

		return request;
	}

	// �������ഫ�ݹ�����hql
	protected abstract void ClaimVoucherHqlBuffer(Employee emp, String status, Date startDate, Date endDate,
			StringBuffer hql, List<Object> values);

	// �����������hql��ƴ������
	protected abstract void OrederByHql(StringBuffer hql, String status);

	@Override
	public ClaimVoucher findByClaimVoucher(long id) {
		String hql = "from ClaimVoucher  c where c.id=?";
		List<ClaimVoucher> list = claimDao.find(hql, id);
		return list.get(0);
	}

	@Override
	public void deleteByClaimVoucher(ClaimVoucher claimVoucher) {
		claimDao.delete(claimVoucher);
	}

}
