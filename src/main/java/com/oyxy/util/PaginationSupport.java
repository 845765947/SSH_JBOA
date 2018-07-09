package com.oyxy.util;

import java.util.LinkedList;
import java.util.List;

public class PaginationSupport<T> {

	// 页面大小,即每页显示记录数
	private int pageSize = 5;
	// 当前页号
	private int currPageNo = 1;
	// 总页数
	private int totalPageCount;
	// 记录总数
	private int totalCount;
	// 返回结果集
	private List<T> items = new LinkedList<T>();

	public PaginationSupport() {

	}

	public PaginationSupport(int currPageNo, int pageSize) {
		setPageSize(pageSize);
		setCurrPageNo(currPageNo);
	}

	// public PaginationSupport(List<T> items ,int currPageNo, int totalCount){
	// setTotalCount(totalCount);
	// refreshPageNo(currPageNo);
	// setItems(items);
	//
	// }
	//
	// private int refreshPageNo(int currPageNo){
	// int tempCurrPage = currPageNo;
	// if(currPageNo < 1){
	// tempCurrPage = 1;
	// }
	// if(currPageNo > this.totalPageCount){
	// tempCurrPage = this.totalPageCount;
	// }
	// return tempCurrPage;
	// }
	/**
	 * 得到开始记录数
	 * 
	 * @return
	 */
	public int getStartRow() {
		return (currPageNo - 1) * pageSize;
	}

	/**
	 * 得到结束记录数
	 * 
	 * @return
	 */
	// public int getEndRow() {
	// return currPageNo * pageSize;
	// }

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrPageNo() {
		return currPageNo;
	}

	public void setCurrPageNo(int currPageNo) {
		this.currPageNo = currPageNo;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		if (totalCount >= 0) {
			int count = totalCount / pageSize;
			if (totalCount % pageSize > 0) {
				count++;
			}
			this.totalPageCount = count;

		}
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

}
