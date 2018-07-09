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
	// ����Ĭ������
	private ClaimVoucherService claService;

	// ��ʼ���Ͷ�Ӧ����ְλ����������Service
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
	// ������ʵ��
	private ClaimVoucher claimVoucher;

	private Integer pageNo;

	private String status;

	private Date startDate;

	private Date endDate;

	private Integer pageSize;

	private static Map<String, String> statusMap;

	private List<ClaimVoucherDetail> detailList = new ArrayList<ClaimVoucherDetail>();

	public void staticLogin() {
		// ��session��ȡ����¼����
		Employee emp = (Employee) ActionContext.getContext().getSession().get(Constants.AUTH_EMPLOYEE);
		// �ж��Ƿ�Ϊ��ֵ������ǿյĻ��͸����û����͸���Ĭ��ֵ
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

	// ���汨����
	public String saveClaimVoucher() {
		staticLogin();
		// ��ȡ����session�е�Employee
		claimVoucher.setCreator(this.getLoginEmployee());
		// ������Ϊ�Լ�,����Ļ����ǲ��ž���
		if (claimVoucher.getStatus().equals(Constants.CLAIMVOUCHER_CREATED)) {
			claimVoucher.setNextDeal(this.getLoginEmployee());
		} else {
			Employee manager = (Employee) (ActionContext.getContext().getSession()
					.get(Constants.AUTH_EMPLOYEE_MANAGER));
			claimVoucher.setNextDeal(manager);
		}
		// ��ӵ���������ϸ
		claimVoucher.setDetailList(detailList);
		// ��ÿһ����������ϸӳ��,����˫��
		for (ClaimVoucherDetail d : detailList) {
			d.setBizClaimVoucher(claimVoucher);
		}
		// ���ñ���
		claService.saveClaimVoucher(claimVoucher);
		return "cla_save";
	}

	// ��ҳ������
	public String searchClaimVoucher() {

		staticLogin();
		pageSupport = claService.getClaimVoucherPage(this.getLoginEmployee(), status, startDate, endDate, pageNo,
				pageSize);
		return "cla_list";

	}

	// �鿴������
	public String getClaimVoucherById() {
		staticLogin();
		claimVoucher = claService.findByClaimVoucher(claimVoucher.getId());
		return "cla_voucher_view";
	}

	// ��ת�����±�����ҳ��
	public String toUpdate() {
		staticLogin();
		claimVoucher = claService.findByClaimVoucher(claimVoucher.getId());
		return "cla_update_view";
	}

	// ���������޸ķ���
	public String updateClaimVoucher() {
		staticLogin();
		// ִ�и���֮ǰ�Ȱ�ԭ�����󶨵�ȫ��ɾ��
		// ������Ϊ�Լ�,����Ļ����ǲ��ž���
		if (claimVoucher.getStatus().equals(Constants.CLAIMVOUCHER_CREATED)) {
			claimVoucher.setNextDeal(this.getLoginEmployee());
		} else {
			claimVoucher.setNextDeal((Employee) this.getSession().get(Constants.AUTH_EMPLOYEE_MANAGER));
		}
		// ��ִ��ɾ��
		calDetailService.DeleteByClaimVoucherDeteail(claimVoucher);
		// ִ�а�ִ������
		claimVoucher.setDetailList(detailList);
		// ����
		claimVoucher.setCreator(this.getLoginEmployee());
		// ��ÿһ����������ϸӳ��,����˫��
		for (ClaimVoucherDetail d : detailList) {
			d.setBizClaimVoucher(claimVoucher);
		}
		claimVoucher.setModifyTime(new Date());
		// ���ñ���
		calDetailService.update(claimVoucher);
		return "cla_update";
	}

	// ɾ��������
	public String deleteClaimVoucherById() {
		staticLogin();
		// �����ҵ�������
		claimVoucher = claService.findByClaimVoucher(claimVoucher.getId());
		// Ȼ��ֱ��ɾ��
		claService.deleteByClaimVoucher(claimVoucher);
		return "cla_delete";
	}

	// ��������������ҳ��
	public String toCheck() {
		staticLogin();
		claimVoucher = claService.findByClaimVoucher(claimVoucher.getId());
		return "cla_check";
	}

	// ���ݶ��󴴽���ͬ��Service���Լ���������Ĭ��ֵ
	protected void ClaimVoucherStuff() {
		claService = claServiceStuff;
		statusMap = new LinkedHashMap<>();
		statusMap.put(Constants.CLAIMVOUCHER_CREATED, "�´���");
		statusMap.put(Constants.CLAIMVOUCHER_SUBMITTED, "���ύ");
		statusMap.put(Constants.CLAIMVOUCHER_APPROVING, "������");
		statusMap.put(Constants.CLAIMVOUCHER_BACK, "�Ѵ��");
		statusMap.put(Constants.CLAIMVOUCHER_APPROVED, "������");
		statusMap.put(Constants.CLAIMVOUCHER_PAID, "�Ѹ���");
		statusMap.put(Constants.CLAIMVOUCHER_TERMINATED, "����ֹ");
	}

	// ���ž���
	protected void ClaimVoucherFM() {
		claService = claServiceFM;
		if (this.status == null || this.status.equals("")) {
			this.status = Constants.CLAIMVOUCHER_SUBMITTED;
		}
		statusMap = new LinkedHashMap<>();
		statusMap.put(Constants.CLAIMVOUCHER_SUBMITTED, "���ύ");
		statusMap.put(Constants.CLAIMVOUCHER_APPROVING, "������");
		statusMap.put(Constants.CLAIMVOUCHER_BACK, "�Ѵ��");
		statusMap.put(Constants.CLAIMVOUCHER_APPROVED, "������");
		statusMap.put(Constants.CLAIMVOUCHER_PAID, "�Ѹ���");
		statusMap.put(Constants.CLAIMVOUCHER_TERMINATED, "����ֹ");
	}

	// �ܾ���
	protected void ClaimVoucherGM() {
		claService = claServiceGM;
		if (this.status == null || this.status.equals("")) {
			this.status = Constants.CLAIMVOUCHER_APPROVING;
		}
		statusMap = new LinkedHashMap<>();
		statusMap.put(Constants.CLAIMVOUCHER_APPROVING, "������");
		statusMap.put(Constants.CLAIMVOUCHER_BACK, "�Ѵ��");
		statusMap.put(Constants.CLAIMVOUCHER_APPROVED, "������");
		statusMap.put(Constants.CLAIMVOUCHER_PAID, "�Ѹ���");
		statusMap.put(Constants.CLAIMVOUCHER_TERMINATED, "����ֹ");
	}

	// ����
	protected void ClaimVoucherCashier() {
		claService = claServiceCahier;
		if (this.status == null || this.status.equals("")) {
			this.status = Constants.CLAIMVOUCHER_APPROVED;
		}
		statusMap = new LinkedHashMap<>();
		statusMap.put(Constants.CLAIMVOUCHER_APPROVED, "������");
		statusMap.put(Constants.CLAIMVOUCHER_PAID, "�Ѹ���");
		statusMap.put(Constants.CLAIMVOUCHER_TERMINATED, "����ֹ");
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
