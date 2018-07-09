<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>北大青鸟办公自动化管理系统</title>
		<link href="<%=request.getContextPath() %>/css/style.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.0.min.js"></script>
		<script type="text/javascript">
		var $claimForm;
		var rowNumber=parseInt(<s:property value="claimVoucher.detailList.size"/>);
        var totalAccount = <s:property value="claimVoucher.totalAccount"/>;
		$(function(){
			//表单提交校验
			//$("#myTable tr").eq(0).hide();	
			$claimForm = $("form[name='claimForm']").submit(function(){
				var hasError = false;
				//判断是否加入了明细 
				if(rowNumber<1){
					$("#notice").text("* 至少添加一条报销单明细 ！");
					hasError = true;
				}
				if($("#event").val()==""){
					$("#eventNotice").text("必须填写事由 ！");
					hasError = true;
				}
				if (hasError) return false;
				$("#notice").text("");
				$("#eventNotice").text("");
				$(".buttons").hide();
				return true;
			});
		    
			$("#AddRow").click(function(){
				var item = $("#item").val();
				var account = $("#account").val();
				var desc = $("#desc").val();

                var hasError = false;
                var $accountNotice = $("#account").next(".notice").text("*");
                var $descNotice = $("#desc").next(".notice").text("*");
                if(account==""){
                    $accountNotice.text("* 金额不能为空  ！");
                    hasError = true;
                }       
                if(desc==""){
                    $descNotice.text("* 说明不能为空  ！");
                    hasError = true;
                }       
                if (hasError) return false;
                
				var tr=$("#myTable tr").eq(0).clone();
				var j = rowNumber++;
				totalAccount += parseFloat(account);
				tr.find("td").get(0).innerHTML="<input  name=detailList["+j+"].item id=item"+j+" type=hidden value="+item+" />"+item;
				tr.find("td").get(1).innerHTML="<input  name=detailList["+j+"].account id=account"+j+"  type=hidden value="+account+" />"+account;
				tr.find("td").get(2).innerHTML="<input  name=detailList["+j+"].desc  id=desc"+j+" type=hidden value="+desc+" />"+desc;		
				tr.find("td").get(3).innerHTML="<img src=${images}/delete.gif width=16 height=16 id=DelRow"+j+" onclick=delRow("+j+") />";
				tr.appendTo("#myTable");
				tr.show();
				//传递一共添加多少明细 
				$("#account").attr("value","");
				$("#desc").attr("value","");
				$("#totalAccount").attr("value",totalAccount);
		
			});	

		});

		function delRow(id){  
            var account = $("#account"+id).val();
            $("#myTable tr").eq(id+1).remove();
            //var rowNumber=$("#rowNumber").val();
            for(var s=id+1;s<rowNumber;s++){
                $("#item"+s).attr("name","detailList["+(s-1)+"].item");
                $("#item"+s).attr("id","item"+(s-1));
                $("#account"+s).attr("name","detailList["+(s-1)+"].account");
                $("#account"+s).attr("id","account"+(s-1));
                $("#desc"+s).attr("name","detailList["+(s-1)+"].desc");
                $("#desc"+s).attr("id","desc"+(s-1));       
                var js="delRow("+(s-1)+");";
                var newclick=eval("(false||function (){"+js+"});");
                $("#DelRow"+s).unbind('click').removeAttr('onclick').click(newclick);
                $("#DelRow"+s).attr("id","DelRow"+(s-1));                   
            }
            //$("#rowNumber").attr("value",rowNumber-1);
            --rowNumber;
            totalAccount -= parseFloat(account);
            $("#totalAccount").val(totalAccount);
        }

        function submitClaimVoucher(action){
            if(!confirm("确定"+action+"报销单吗")) return;
            if (action == '保存'){// 保持原状态（新创建 或 已打回）
                //$claimForm.find("#status").val("1");//新创建
            }else{
                $claimForm.find("#status").val("2");//已提交
            }
            $claimForm.submit();

        }
		</script>
		
		</head>
	<body>
	<div class="action  divaction">
		<div class="t">报销单更新</div>
		<div class="pages">
			<s:form action="claimVoucher_updateClaimVoucher.action" name="claimForm" method="post">
			
			<%-- <input type="hidden" id="rowNumber" name="rowNumber" value="<s:property value="rowNumber"/>"/> --%>
			<input type="hidden" id="claimId" name="claimVoucher.id" value="<s:property value="claimVoucher.id"/>"/>
				<table width="90%" border="0" cellspacing="0" cellpadding="0" class="addform-base">
                  <caption>基本信息</caption>
                  <tr>
                  	<td >编&nbsp;&nbsp;号：<s:property value="claimVoucher.id"/></td>
                    <td>填&nbsp;写&nbsp;人：<s:property value="claimVoucher.creator.name"/></td>
                    <td>部&nbsp;&nbsp;门：<s:property value="claimVoucher.creator.sysDepartment.name"/></td>
                    <td>职&nbsp;&nbsp;&nbsp;&nbsp;位：<s:property value="claimVoucher.creator.sysPosition.nameCn"/></td>
                  </tr>
                  <tr>
                    <td>总金额：
                    <s:textfield name="claimVoucher.totalAccount" id="totalAccount" readonly="true"/></td>
                    <td>填报时间：<s:date name="claimVoucher.createTime" format="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>状态：<s:property value="statusMap[claimVoucher.status]"/></td>
                    <td>待处理人：<s:property value="claimVoucher.nextDeal.name"/></td>
                    <s:hidden id="status" name="claimVoucher.status" value="%{claimVoucher.status}"/>
                  </tr>
                  <tr>
                  	<td colspan="4"><p>-------------------------------------------------------------------------------</p>
                  	<span class="notice" id="notice"></span></td>
                  </tr>
                </table>
				<table id="myTable" width="90%" border="0" cellspacing="0" cellpadding="0" class="addform-base">
					<tr>
						<td width="30%">项目类别</td>
						<td width="30%">项目金额</td>
						<td width="30%">费用说明</td>
						<td width="10%">操作</td>
					</tr>
					<s:iterator value="claimVoucher.detailList" id="claimDetail" begin="0" status="s">
					<tr>
						<td>
							<%--<s:hidden id="id%{#s.index}" name="detailList[%{#s.index}].id" value="%{#claimDetail.id}"/>	--%>
							<s:hidden id="item%{#s.index}" name="detailList[%{#s.index}].item" value="%{#claimDetail.item}"/>
							<s:property value="#claimDetail.item"/></td>
						<td>
							<s:hidden id="account%{#s.index}" name="detailList[%{#s.index}].account" value="%{#claimDetail.account}"/>
							<s:property value="#claimDetail.account"/></td>
						<td>
							<s:hidden id="desc%{#s.index}" name="detailList[%{#s.index}].desc" value="%{#claimDetail.desc}"/>
							<s:property value="#claimDetail.desc"/>
						</td>
						<td>
							<img src=${images}/delete.gif width=16 height=16 id=DelRow${s.index}  
							onclick="delRow(${s.index})" />
						</td>
					</tr>
				</s:iterator>
				</table>
				<table id="detailTable" width="90%" border="0" cellspacing="0" cellpadding="0" class="addform-base">
					<tr>
						<td width="30%">
							<select name="claimVoucherDetail.item" id="item">
								<option value="城际交通费">城际交通费</option>
								<option value="市内交通费">市内交通费</option>
								<option value="通讯费">通讯费</option>
								<option value="礼品费">礼品费</option>
								<option value="办公费">办公费</option>
								<option value="交际餐费">交际餐费</option>
								<option value="餐补">餐补</option>
								<option value="住宿费">住宿费</option>
							</select>
						</td>
						<td width="30%"><input type="text" name="claimVoucherDetail.account" id="account" /><span class=notice>*</span></td>
						<td width="30%"><input type="text" name="claimVoucherDetail.desc" id="desc" /><span class=notice>*</span></td>
						<td width="10%"><img src="${images}/add.gif" width="16" height="16" id="AddRow"/></td>
					</tr>
				</table>
				<table>
					<tr>
						<td>*事由：<br/><span class="notice" id="eventNotice"></span></td>
						<td colspan="3">
						<s:textarea name="claimVoucher.event" id="event" rows="5" cols="66"></s:textarea>
						</td>
					</tr>
					<tr align="center">
						<td>
							&nbsp; 
						</td>
						<td >
						<input type="button" id="1" name="1" value="保存" onclick="submitClaimVoucher('保存')" class="submit_01"/>
							<input type="button" id="2" name="2" value="提交" class="submit_01"
								onclick="submitClaimVoucher('提交')"/>
							<input type="button" value="返回" onclick="window.history.go(-1)" class="submit_01"/>
						</td>
					</tr>
				</table>
				</s:form>
			</div>
		</div>
	</body>
</html>
