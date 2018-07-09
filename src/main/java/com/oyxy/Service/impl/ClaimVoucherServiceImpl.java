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
		this.ClaimVoucherHqlBuffer(emp, status, startDate, endDate, hql, values);
		// 把弄好的sql以及参数发送给dao层次进行处理,获取总记录数
		int count = claimDao.getTotalCount(hql.toString(), values.toArray()).intValue();
		// 设置返回的总记录数
		request.setTotalCount(count);
		// 如何用户输入页数超出最大页数
		if (request.getCurrPageNo() > request.getTotalPageCount()) {
			request.setCurrPageNo(request.getTotalPageCount());
		}
		// 调用根据子类进行hql的拼接排序
		this.OrederByHql(hql, status);
		List<ClaimVoucher> items = claimDao.fingForPage(hql.toString(), request.getCurrPageNo(), request.getPageSize(),
				values.toArray());

		request.setItems(items);

		return request;
	}

	// 根据子类传递过来的hql
	protected abstract void ClaimVoucherHqlBuffer(Employee emp, String status, Date startDate, Date endDate,
			StringBuffer hql, List<Object> values);

	// 根据子类进行hql的拼接排序
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
