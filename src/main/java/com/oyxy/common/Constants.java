package com.oyxy.common;

public class Constants {
	// 登录员工
	public final static String AUTH_EMPLOYEE = "employee";
	public final static String EMPLOYEE_POSITION = "employee_position";
	// 登录员工的经理
	public final static String AUTH_EMPLOYEE_MANAGER = "manager";
	// 员工状态
	public static final String EMPLOYEE_STAY = "在职";
	public static final String EMPLOYEE_LEAVE = "离职";
	public static final String EMPLOYEE_VACATION = "休假";
	// 报销单状态
	public static final String CLAIMVOUCHER_CREATED = "1"; // 新创建
	public static final String CLAIMVOUCHER_SUBMITTED = "2"; // 已提交
	public static final String CLAIMVOUCHER_APPROVING = "3"; // 待审批
	public static final String CLAIMVOUCHER_BACK = "4"; // 已打回
	public static final String CLAIMVOUCHER_APPROVED = "5"; // 已审批
	public static final String CLAIMVOUCHER_PAID = "6"; // 已付款
	public static final String CLAIMVOUCHER_TERMINATED = "7"; // 已终止
	// 审核状态
	public static final String CHECKRESULT_BACK = "打回";
	public static final String CHECKRESULT_REJECT = "拒绝";
	public static final String CHECKRESULT_PASS = "通过";
	public static final String CHECKRESULT_PAID = "付款";
	// 处理类型
	public static final String CHECKR_FM = "部门经理审核";
	public static final String CHECKR_GM = "总经理审核";
	public static final String CHECKR_CASHIER = "财务审核";
	// 职务
	public static final String POSITION_STAFF = "员工";
	public static final String POSITION_FM = "部门经理";
	public static final String POSITION_GM = "总经理";
	public static final String POSITION_CASHIER = "财务";
	// 职务英文短名称
	public static final String POSITION_STAFF_EN = "Stuff";
	public static final String POSITION_FM_EN = "FM";
	public static final String POSITION_GM_EN = "GM";
	public static final String POSITION_CASHIER_EN = "Cashier";
	// 请假类型
	public static final String LEAVE_SICK = "病假";
	public static final String LEAVE_ANNUAL = "年假";
	public static final String LEAVE_CASUAL = "事假";
	public static final String LEAVE_MARRIAGE = "婚假";
	public static final String LEAVE_MATERNITY = "产假";
	// 请假状态
	public static final String LEAVESTATUS_APPROVING = "1"; // 待审批
	public static final String LEAVESTATUS_APPROVED = "2"; // 已审批
	public static final String LEAVESTATUS_BACK = "3"; // 已打回

}
