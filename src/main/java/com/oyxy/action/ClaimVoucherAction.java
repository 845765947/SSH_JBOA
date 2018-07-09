package com.oyxy.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.oyxy.Service.ClaimVoucherDetailService;
import com.oyxy.Service.ClaimVoucherService;
import com.oyxy.common.Constants;
import com.oyxy.entity.CheckResult;
import com.oyxy.entity.ClaimVoucher;
import com.oyxy.entity.ClaimVoucherDetail;
import com.oyxy.entity.Employee;

public class ClaimVoucherAction extends BaseAction {
	// 设置默认容器
	private ClaimVoucherService claService;

	// 初始化就对应各个职位创建各个的Service
	@Autowired
	@Qualifier("claServiceStuff")
	private ClaimVoucherService claServiceStuff;

	@Autowired
	@Qualifier("claServiceFM")
	private ClaimVoucherService claServiceFM;

	@Autowired
	@Qualifier("claServiceGM")
	private ClaimVoucherService claServiceGM;

	@Autowired
	@Qualifier("claServiceCahier")
	private ClaimVoucherService claServiceCahier;

	@Autowired
	@Qualifier("claiDetailService")
	private ClaimVoucherDetailService calDetailService;
	// 报销单实体
	private ClaimVoucher claimVoucher;

	private Integer pageNo;

	private String status;

	private Date startDate;

	private Date endDate;

	private Integer pageSize;

	private static Map<String, String> statusMap;

	private List<ClaimVoucherDetail> detailList = new ArrayList<ClaimVoucherDetail>();

	public void staticLogin() {
		// 从session中取出登录对象
		Employee emp = (Employee) ActionContext.getContext().getSession().get(Constants.AUTH_EMPLOYEE);
		// 判断是否为空值，如果是空的话就根据用户类型给予默认值
		if (status == null || status.equals("")) {
			switch (emp.getSysPosition().getNameCn()) {
			case Constants.POSITION_FM:
				this.ClaimVoucherFM();
				break;
			case Constants.POSITION_GM:
				this.ClaimVoucherGM();
				break;
			case Constants.POSITION_CASHIER:
				this.ClaimVoucherCashier();
				break;
			default:
				this.ClaimVoucherStuff();
				break;
			}
		}
	}

	// 保存报销单
	public String saveClaimVoucher() {
		staticLogin();
		// 获取存在session中的Employee
		claimVoucher.setCreator(this.getLoginEmployee());
		// 保存人为自己,否则的话就是部门经理
		if (claimVoucher.getStatus().equals(Constants.CLAIMVOUCHER_CREATED)) {
			claimVoucher.setNextDeal(this.getLoginEmployee());
		} else {
			Employee manager = (Employee) (ActionContext.getContext().getSession()
					.get(Constants.AUTH_EMPLOYEE_MANAGER));
			claimVoucher.setNextDeal(manager);
		}
		// 添加到报销单明细
		claimVoucher.setDetailList(detailList);
		// 把每一个报销单明细映射,配置双向
		for (ClaimVoucherDetail d : detailList) {
			d.setBizClaimVoucher(claimVoucher);
		}
		// 调用保存
		claService.saveClaimVoucher(claimVoucher);
		return "cla_save";
	}

	// 分页报销单
	public String searchClaimVoucher() {

		staticLogin();
		pageSupport = claService.getClaimVoucherPage(this.getLoginEmployee(), status, startDate, endDate, pageNo,
				pageSize);
		return "cla_list";

	}

	// 查看报销单
	public String getClaimVoucherById() {
		staticLogin();
		claimVoucher = claService.findByClaimVoucher(claimVoucher.getId());
		return "cla_voucher_view";
	}

	// 跳转到更新报销单页面
	public String toUpdate() {
		staticLogin();
		claimVoucher = claService.findByClaimVoucher(claimVoucher.getId());
		return "cla_update_view";
	}

	// 报销单的修改方法
	public String updateClaimVoucher() {
		staticLogin();
		// 执行更新之前先把原来所绑定的全部删除
		// 保存人为自己,否则的话就是部门经理
		if (claimVoucher.getStatus().equals(Constants.CLAIMVOUCHER_CREATED)) {
			claimVoucher.setNextDeal(this.getLoginEmployee());
		} else {
			claimVoucher.setNextDeal((Employee) this.getSession().get(Constants.AUTH_EMPLOYEE_MANAGER));
		}
		// 先执行删除
		calDetailService.DeleteByClaimVoucherDeteail(claimVoucher);
		// 执行绑定执行新增
		claimVoucher.setDetailList(detailList);
		// 设置
		claimVoucher.setCreator(this.getLoginEmployee());
		// 把每一个报销单明细映射,配置双向
		for (ClaimVoucherDetail d : detailList) {
			d.setBizClaimVoucher(claimVoucher);
		}
		claimVoucher.setModifyTime(new Date());
		// 调用保存
		calDetailService.update(claimVoucher);
		return "cla_update";
	}

	// 删除报销单
	public String deleteClaimVoucherById() {
		staticLogin();
		// 首先找到报销单
		claimVoucher = claService.findByClaimVoucher(claimVoucher.getId());
		// 然后直接删除
		claService.deleteByClaimVoucher(claimVoucher);
		return "cla_delete";
	}

	// 进入审批报销单页面
	public String toCheck() {
		staticLogin();
		claimVoucher = claService.findByClaimVoucher(claimVoucher.getId());
		return "cla_check";
	}

	// 根据对象创建不同的Service，以及设置他的默认值
	protected void ClaimVoucherStuff() {
		claService = claServiceStuff;
		statusMap = new LinkedHashMap<>();
		statusMap.put(Constants.CLAIMVOUCHER_CREATED, "新创建");
		statusMap.put(Constants.CLAIMVOUCHER_SUBMITTED, "已提交");
		statusMap.put(Constants.CLAIMVOUCHER_APPROVING, "待审批");
		statusMap.put(Constants.CLAIMVOUCHER_BACK, "已打回");
		statusMap.put(Constants.CLAIMVOUCHER_APPROVED, "已审批");
		statusMap.put(Constants.CLAIMVOUCHER_PAID, "已付款");
		statusMap.put(Constants.CLAIMVOUCHER_TERMINATED, "已终止");
	}

	// 部门经理
	protected void ClaimVoucherFM() {
		claService = claServiceFM;
		if (this.status == null || this.status.equals("")) {
			this.status = Constants.CLAIMVOUCHER_SUBMITTED;
		}
		statusMap = new LinkedHashMap<>();
		statusMap.put(Constants.CLAIMVOUCHER_SUBMITTED, "已提交");
		statusMap.put(Constants.CLAIMVOUCHER_APPROVING, "待审批");
		statusMap.put(Constants.CLAIMVOUCHER_BACK, "已打回");
		statusMap.put(Constants.CLAIMVOUCHER_APPROVED, "已审批");
		statusMap.put(Constants.CLAIMVOUCHER_PAID, "已付款");
		statusMap.put(Constants.CLAIMVOUCHER_TERMINATED, "已终止");
	}

	// 总经理
	protected void ClaimVoucherGM() {
		claService = claServiceGM;
		if (this.status == null || this.status.equals("")) {
			this.status = Constants.CLAIMVOUCHER_APPROVING;
		}
		statusMap = new LinkedHashMap<>();
		statusMap.put(Constants.CLAIMVOUCHER_APPROVING, "待审批");
		statusMap.put(Constants.CLAIMVOUCHER_BACK, "已打回");
		statusMap.put(Constants.CLAIMVOUCHER_APPROVED, "已审批");
		statusMap.put(Constants.CLAIMVOUCHER_PAID, "已付款");
		statusMap.put(Constants.CLAIMVOUCHER_TERMINATED, "已终止");
	}

	// 财务
	protected void ClaimVoucherCashier() {
		claService = claServiceCahier;
		if (this.status == null || this.status.equals("")) {
			this.status = Constants.CLAIMVOUCHER_APPROVED;
		}
		statusMap = new LinkedHashMap<>();
		statusMap.put(Constants.CLAIMVOUCHER_APPROVED, "已审批");
		statusMap.put(Constants.CLAIMVOUCHER_PAID, "已付款");
		statusMap.put(Constants.CLAIMVOUCHER_TERMINATED, "已终止");
	}

	public ClaimVoucherDetailService getCalDetailService() {
		return calDetailService;
	}

	public void setCalDetailService(ClaimVoucherDetailService calDetailService) {
		this.calDetailService = calDetailService;
	}

	public ClaimVoucher getClaimVoucher() {
		return claimVoucher;
	}

	public void setClaimVoucher(ClaimVoucher claimVoucher) {
		this.claimVoucher = claimVoucher;
	}

	public List<ClaimVoucherDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<ClaimVoucherDetail> detailList) {
		this.detailList = detailList;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static void setStatusMap(Map<String, String> statusMap) {
		ClaimVoucherAction.statusMap = statusMap;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Map<String, String> getStatusMap() {
		return statusMap;
	}

	public ClaimVoucherService getClaService() {
		return claService;
	}

	public void setClaService(ClaimVoucherService claService) {
		this.claService = claService;
	}

	public ClaimVoucherService getClaServiceStuff() {
		return claServiceStuff;
	}

	public void setClaServiceStuff(ClaimVoucherService claServiceStuff) {
		this.claServiceStuff = claServiceStuff;
	}

	public ClaimVoucherService getClaServiceFM() {
		return claServiceFM;
	}

	public void setClaServiceFM(ClaimVoucherService claServiceFM) {
		this.claServiceFM = claServiceFM;
	}

	public ClaimVoucherService getClaServiceGM() {
		return claServiceGM;
	}

	public void setClaServiceGM(ClaimVoucherService claServiceGM) {
		this.claServiceGM = claServiceGM;
	}

	public ClaimVoucherService getClaServiceCahier() {
		return claServiceCahier;
	}

	public void setClaServiceCahier(ClaimVoucherService claServiceCahier) {
		this.claServiceCahier = claServiceCahier;
	}

}
