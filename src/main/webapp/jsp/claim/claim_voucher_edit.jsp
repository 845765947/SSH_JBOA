<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>北大青鸟办公自动化管理系统</title>
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript">
	var $claimForm;
	var rowNumber = 0;
	var totalAccount = 0;
	$(function() {
		//表单提交校验
		//$("#myTable tr").eq(0).hide();
		$claimForm = $("form[name='claimForm']").submit(function() {
			var hasError = false;
			//判断是否加入了明细 
			if (rowNumber < 1) {
				$("#notice").text("* 至少添加一条报销单明细 ！");
				hasError = true;
			}
			if ($("#event").val() == "") {
				$("#eventNotice").text("必须填写事由 ！");
				hasError = true;
			}
			if (hasError)
				return false;
			$("#notice").text("");
			$("#eventNotice").text("");
			$(".buttons").hide();
			return true;
		});

		$("#AddRow")
				.click(
						function() {
							var item = $("#item").val();
							var account = $("#account").val();
							var desc = $("#desc").val();

							var hasError = false;
							var $accountNotice = $("#account").next(".notice")
									.text("*");
							var $descNotice = $("#desc").next(".notice").text(
									"*");
							if (account == "") {
								$accountNotice.text("* 金额不能为空  ！");
								hasError = true;
							}
							if (desc == "") {
								$descNotice.text("* 说明不能为空  ！");
								hasError = true;
							}
							if (hasError)
								return false;

							var tr = $("#myTable tr").eq(0).clone();
							var j = rowNumber++;
							totalAccount += parseFloat(account);
							tr.find("td").get(0).innerHTML = "<input  name=detailList["+j+"].item id=item"+j+" type=hidden value="+item+" />"
									+ item;
							tr.find("td").get(1).innerHTML = "<input  name=detailList["+j+"].account id=account"+j+"  type=hidden value="+account+" />"
									+ account;
							tr.find("td").get(2).innerHTML = "<input  name=detailList["+j+"].desc  id=desc"+j+" type=hidden value="+desc+" />"
									+ desc;
							tr.find("td").get(3).innerHTML = "<img src=${images}/delete.gif width=16 height=16 id=DelRow"
									+ j + " onclick=delRow(" + j + ") />";
							tr.appendTo("#myTable");
							tr.show();
							//传递一共添加多少明细 
							$("#account").attr("value", "");
							$("#desc").attr("value", "");
							$("#totalAccount").attr("value", totalAccount);

						});

	});

	function delRow(id) {
		var account = $("#account" + id).val();
		$("#myTable tr").eq(id + 1).remove();
		//var rowNumber=$("#rowNumber").val();
		for (var s = id + 1; s < rowNumber; s++) {
			$("#item" + s).attr("name", "detailList[" + (s - 1) + "].item");
			$("#item" + s).attr("id", "item" + (s - 1));
			$("#account" + s).attr("name",
					"detailList[" + (s - 1) + "].account");
			$("#account" + s).attr("id", "account" + (s - 1));
			$("#desc" + s).attr("name", "detailList[" + (s - 1) + "].desc");
			$("#desc" + s).attr("id", "desc" + (s - 1));
			var js = "delRow(" + (s - 1) + ");";
			var newclick = eval("(false||function (){" + js + "});");
			$("#DelRow" + s).unbind('click').removeAttr('onclick').click(
					newclick);
			$("#DelRow" + s).attr("id", "DelRow" + (s - 1));
		}
		//$("#rowNumber").attr("value",rowNumber-1);
		--rowNumber;
		totalAccount -= parseFloat(account);
		$("#totalAccount").val(totalAccount);
	}

	function submitClaimVoucher(action) {
		if (!confirm("确定" + action + "报销单吗"))
			return;
		if (action == '保存') {
			$claimForm.find("#status").val("1");// 新创建
		} else {
			$claimForm.find("#status").val("2");// 已提交
		}
		$claimForm.submit();

	}
</script>

</head>
<body>
	<div class="action  divaction">
		<div class="t">报销单添加</div>
		<div class="pages">
			<form action="claimVoucher_saveClaimVoucher.action" name="claimForm"
				method="post">

				<%-- <input type="hidden" id="rowNumber" name="rowNumber" value="<s:property value="rowNumber"/>"/> --%>
				<table width="90%" border="0" cellspacing="0" cellpadding="0"
					class="addform-base">
					<tr>
						<td>*填报人：</td>
						<td><s:property value="#session.employee.name" /></td>
						<td>*填报时间：</td>
						<td><s:date name="new java.util.Date()"
								format="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr>
						<td>*总金额：￥</td>
						<td><input type="text" id="totalAccount"
							name="claimVoucher.totalAccount" value="0.0" readonly="readonly" /></td>
						<td>*状态：</td>
						<td>新创建<input type="hidden" id="status"
							name="claimVoucher.status" value="" /></td>
					</tr>
					<tr>
						<td colspan="4"><span class="notice" id="notice"></span></td>
					</tr>
				</table>
				<table id="myTable" width="90%" border="0" cellspacing="0"
					cellpadding="0" class="addform-base">
					<tr>
						<td width="30%">项目类别</td>
						<td width="30%">项目金额</td>
						<td width="30%">费用说明</td>
						<td width="10%">操作</td>
					</tr>
				</table>
				<table id="detailTable" width="90%" border="0" cellspacing="0"
					cellpadding="0" class="addform-base">
					<tr>
						<td width="30%"><select name="claimVoucherDetail.item"
							id="item">
								<option value="城际交通费">城际交通费</option>
								<option value="市内交通费">市内交通费</option>
								<option value="通讯费">通讯费</option>
								<option value="礼品费">礼品费</option>
								<option value="办公费">办公费</option>
								<option value="交际餐费">交际餐费</option>
								<option value="餐补">餐补</option>
								<option value="住宿费">住宿费</option>
						</select></td>
						<td width="30%"><input type="text"
							name="claimVoucherDetail.account" id="account" /><span
							class=notice>*</span></td>
						<td width="30%"><input type="text"
							name="claimVoucherDetail.desc" id="desc" /><span class=notice>*</span></td>
						<td width="10%"><img src="${images}/add.gif" width="16"
							height="16" id="AddRow" /></td>
					</tr>
				</table>
				<table>
					<tr>
						<td>*事由：<br /> <span class="notice" id="eventNotice"></span></td>
						<td colspan="3"><textarea rows="5" cols="66"
								name="claimVoucher.event" id="event"></textarea></td>
					</tr>
					<tr align="center">
						<td>&nbsp;</td>
						<td><input type="button" id="1" name="1" value="保存"
							onclick="submitClaimVoucher('保存')" class="submit_01" /> <input
							type="button" id="2" name="2" value="提交" class="submit_01"
							onclick="submitClaimVoucher('提交')" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>
