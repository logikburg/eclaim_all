<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script>
	function returnToHome() {
		document.location.href = "<%=request.getContextPath()%>/home/home";
	}

	function submitSave() {
		$("#divSuccess").hide();
		$("#divError").hide();
	
		$("#hiddenFormAction").val("SAVE");
		
		showLoading();
	}

	function performWithdraw() {
		$("#divSuccess").hide();
		$("#divError").hide();
	
		$("#model_confirmWithdraw").modal("show");
	}
	
	function submitWithdraw() {
		$("#model_confirmWithdraw").modal("hide");
	
		showLoading();
		
		$("#hiddenFormAction").val("WITHDRAW");
		$("#frmDetail").submit();
	}
	
	function validateApproval() {
		if ($("#txtApprovalDate").val() == "") {
			$("#errorMsgDivContent").html("<b>Approval Date</b> cannot be empty.");
			$("#errorMsgDiv").show();
			
			$('html,body').animate({ scrollTop: 0 }, 'slow');
			return false;
		}
		
		return true;
	}
	
	function performApproval() {
		if (typeof checkFTE == 'function') {
			if (!checkFTE()) {
				return;
			}
		}
	
		$("#divSuccess").hide();
		$("#divError").hide();
	
		$("#nextWFGroup").val("");
		$("#nextWFUser").val("");
        $("#returnCase").val("");
	
		$("#hiddenFormAction").val("APPROVE");
		$("#model_generateEmail").modal("show");
	}
	
	function editEmail() {
		var postIdList = "";
		var postIdList2 = "";
		var param6 = "";
		
		if ($("#hiddenFormAction").val() == "SUBMIT") {
			if ($("#requestType").val() == "NEW") {
				emailTemplateId = "RQ_NEW_POST_SUBMIT";
			}
			else if ($("#requestType").val() == "DELETION") {
				emailTemplateId = "RQ_DEL_SUBMIT";
			}
			else if ($("#requestType").val() == "FROZEN") {
				emailTemplateId = "RQ_FROZ_SUBMIT";
			}
			else if ($("#requestType").val() == "EXTENSION") {
				emailTemplateId = "RQ_EXT_SUBMIT";
			}
			else if ($("#requestType").val() == "UPGRADE") {
				emailTemplateId = "RQ_UPG_SUBMIT";
			}
			else if ($("#requestType").val() == "CHG_STAFF") {
				emailTemplateId = "RQ_CHG_STF_SUBMIT";
			}
			else if ($("#requestType").val() == "SUPP_PROM") {
				emailTemplateId = "RQ_SUP_PROM_SUBMIT";
			}
			else if ($("#requestType").val() == "CHG_FUND") {
				emailTemplateId = "RQ_CHG_FUND_SUBMIT";
			}
			else if ($("#requestType").val() == "CHG_HCM") {
				emailTemplateId = "RQ_CHG_HCM_SUBMIT";
			}
		}
		else if ($("#hiddenFormAction").val() == "APPROVE") {
			if ($("#requestType").val() == "NEW") {
				emailTemplateId = "RQ_NEW_POST_CONFIRM";
			}
			else if ($("#requestType").val() == "DELETION") {
				emailTemplateId = "RQ_DEL_CONFIRM";
			}
			else if ($("#requestType").val() == "FROZEN") {
				emailTemplateId = "RQ_FROZ_CONFIRM";
			}
			else if ($("#requestType").val() == "EXTENSION") {
				emailTemplateId = "RQ_EXT_CONFIRM";
			}
			else if ($("#requestType").val() == "UPGRADE") {
				emailTemplateId = "RQ_UPG_CONFIRM";
			}
			else if ($("#requestType").val() == "CHG_STAFF") {
				emailTemplateId = "RQ_CHG_STF_CONFIRM";
			}
			else if ($("#requestType").val() == "SUPP_PROM") {
				emailTemplateId = "RQ_SUP_PROM_CONFIRM";
			}
			else if ($("#requestType").val() == "CHG_FUND") {
				emailTemplateId = "RQ_CHG_FUND_CONFIRM";
			}
			else if ($("#requestType").val() == "CHG_HCM") {
				emailTemplateId = "RQ_CHG_HCM_CONFIRM";
			}
		}
		
		if ($("#requestType").val() == "NEW") {
			postIdList = $("#txt_proposed_post_id").val();
		}
		
		if ($("#requestType").val() == "DELETION") {
			for (var i=1; i< $("#tblPositionDelete tr").length; i++) {
				if (i != 1) {
					postIdList += ", ";
				}
			
				postIdList += $($($("#tblPositionDelete tr")[i]).find("td:eq(0)")).text();
			}
		}
		
		if ($("#requestType").val() == "FROZEN") {
			for (var i=1; i< $("#tblPositionFrozen tr").length; i++) {
				if (i != 1) {
					postIdList += ", ";
				}
			
				postIdList += $($($("#tblPositionFrozen tr")[i]).find("td:eq(0)")).text();
			}
		}
		
		if ($("#requestType").val() == "SUPP_PROM") {
			for (var i=1; i< $("#tblSuppPromotion tr").length; i++) {
				if (i != 1) {
					postIdList += ", ";
				}
			
				postIdList += $($($("#tblSuppPromotion tr")[i]).find("td:eq(0)")).text();
			}
		}
		
		if ($("#requestType").val() == "EXTENSION") {
			for (var i=1; i< $("#tblExtension tr").length; i++) {
				if (i != 1) 
					postIdList += ", ";
			
				postIdList += $($($("#tblExtension tr")[i]).find("td:eq(0)")).text();
			}
			
			
			for (var i=1; i< $("#tblExtension tr").length; i++) {
				if (i != 1) {
					postIdList2 += ", ";
				}
			
				postIdList2 += $($($("#tblExtension tr")[i]).find("td:eq(4)")).text();
			}
			
			if ($("#requestDuartion").prop("checked") == true) {
				param6 = $("#requestEndDate").val();
			}
			
			if ($("#requestDuartion2").prop("checked") == true) {
				param6 = $("#requestDurationValue").val();
				if ($("#requestDurationUnit").val() == "M") {
					param6 = param6 + " Month"; 
				}
			
				if ($("#requestDurationUnit").val() == "Y") {
					param6 = param6 + " Year"; 
				}
			} 
		}
		
		if ($("#requestType").val() == "CHG_FUND") {
			for (var i=1; i< $("#tblPositionChangeFunding tr").length; i++) {
				if (i != 1) {
					postIdList += ", ";
				}
			
				postIdList += $($($("#tblPositionChangeFunding tr")[i]).find("td:eq(0)")).text();
			}
		}
		
		
		if ($("#requestType").val() == "CHG_STAFF" || $("#requestType").val() == "UPGRADE") {
			for (var i=1; i< $("#tblPositionFrom tr").length; i++) {
				if (i != 1) {
					postIdList += ", ";
				}
			
				postIdList += $($($("#tblPositionFrom tr")[i]).find("td:eq(0)")).text();
			}
			
			for (var i=1; i< $("#tblPositionTo tr").length; i++) {
				if (i != 1) {
					postIdList2 += ", ";
				}
			
				if ($($($("#tblPositionTo tr")[i]).find("td:eq(0)")).text() == "") {
					postIdList2 += "To be confirmed";
				}
				else {
					postIdList2 += $($($("#tblPositionTo tr")[i]).find("td:eq(0)")).text();
				}
			}
		}
				
		// Ajax call to perform search
		var searchData = {
			requestId: $("#hiddenRequestNo").val(),
			templateId: emailTemplateId, 
			param: $("#requestId").val(),
    	   	param2: postIdList,
			param3: '<c:out value="${sessionScope.userProfile.userName}"/>',
			param4: $.datepicker.formatDate('dd/mm/yy', new Date()),
			param5: postIdList2,
			param6: param6,
			param7: "",
			param8: "",
			param9: "",
			wfGroup: $("#nextWFGroup").val(),
			wfUser: $("#nextWFUser").val(),
			isReturn: $("#returnCase").val()
		}
		$.ajax({
            url: "<%=request.getContextPath()%>/api/searchEmailTemplate",
            type: "POST",
            contentType: 'application/json; charset=utf-8',
      		dataType: 'json',
      		data: JSON.stringify(searchData),
            success: function(data) {
	            if (data.emailTemplate != null) {
	            	 $("#txtEmailTitle").val(data.emailTemplate.templateTitle);
	            	 $("#txtEmailContent").val(data.emailTemplate.templateContent);
	            	 $("#divEmailContent").html(data.emailTemplate.templateContent);
            	 }
            	 $("#emailTo").val(data.toEmailList);
            	 $("#emailCC").val(data.ccEmailList);
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	
		$("#txtEmailTitle").attr("readonly", true);
		$("#model_editEmail").modal("show");
	}
	
	function disableAllFields() {
		if ($("#returnCase").val() == "Y") {
			$("#frmDetail input[type='text']").attr("disabled", true);
			$("#frmDetail textarea").attr("disabled", true);
			$("#frmDetail select").attr("disabled", true);
		}
	}
	
	function submitWorkflowWithDefaultEmail() {
		$("#model_generateEmail").modal("hide");
	
		showLoading();
		
		// Disable All fields for by pass mandatory checking
		disableAllFields();
	
		$("#submitWithModifiedEmail").val("N");
		$("#submitWithEmail").val("Y");
		$("#submitWithoutEmail").val("N");
	}
	
	// Added for ST08741 - Check email address
	function checkEmail(inEmail) {
		if (inEmail == "" || inEmail == null) {
			return true;
		}
		else {
			var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
  			return regex.test(inEmail.trim());
		}
	}
	
	function submitSendEmail(event) {
		var msg = "";
	
		if ($("#emailTo").val() == "") {
			msg += "<b>To</b> cannot be empty. <br>";
		}
		else {
			// Added for ST08741 - Check email address
			var emailTo = $("#emailTo").val().replace(";",",");
			var tmp = emailTo.split(",");
			for (var x=0; x<tmp.length; x++) {
				if (!checkEmail(tmp[x])) {
					msg += "Invalid email address is found in <b>To</b> field. <br>";
					break;
				}
			}
		}
		
		// Added for ST08741 - Check email address
		if ($("#emailCC").val() != "") {
			var emailCC = $("#emailCC").val().replace(";",",");
			var tmp = emailCC.split(",");
			for (var x=0; x<tmp.length; x++) {
				if (!checkEmail(tmp[x])) {
					msg += "Invalid email address is found in <b>CC</b> field. <br>";
					break;
				}
			}
		}
		
		if ($("#txtEmailTitle").val() == "") {
			msg += "<b>Title</b> cannot be empty. <br>";
		}	
		
		if (msg != "") {
			$("#warningTitle").html("Error");
            $("#warningContent").html(msg);
            $("#warningModal").modal("show");
			
			event.preventDefault();
			return;
		}
	
		$("#model_editEmail").modal("hide");
	
		showLoading();
		
		// Disable All fields for by pass mandatory checking
		disableAllFields();
	
		$("#submitWithModifiedEmail").val("Y");
		$("#submitWithEmail").val("N");
		$("#submitWithoutEmail").val("N");
	}
	
	function cancelSendEmail() {
		$("#model_confirmCancelSend input[type='button']").attr("disabled", false);
		$("#model_confirmCancelSend").modal("show");
	}
	
	function confirmCancelSendEmail() {
		$("#model_editEmail").modal("hide");
	}
	
	
	function preSubmit() {
		var currentRole = $("#hidRole").val();
		if (currentRole == "FIN_OFFICER" || currentRole == "FIN_MANAGER") {
			$("#model_review_funding_submit").modal("show");			
		}
		else {
			performSubmit();
		}
	}
	
	function preApproval() {
		var currentRole = $("#hidRole").val();
		if (currentRole == "FIN_OFFICER" || currentRole == "FIN_MANAGER") {
			$("#model_review_funding_approve").modal("show");
		}
		else {
			performApproval();
		}
	}
	
	function performSubmit() {
		if (typeof checkFTE == 'function') {
			if (!checkFTE()) {
				return;
			}
		}
	
		$("#divSuccess").hide();
		$("#divError").hide();
	
		$("#divWFTargetUser").show();
	
		$("#returnCase").val("N");
	
		// Get the default group
		$.ajax ({
            url: "<%=request.getContextPath()%>/request/getNextStep",
            type: "POST",
            data: {
            	requestNo: $("#hiddenRequestNo").val(),
				requestType: $("#requestType").val(),
				currentStatus: $("#requestStatus").val(),
				mgTeamInd: $("#mgTeamInd").val()
			},
            success: function(data) {
				$("#nextWFGroup").empty();
            	 
				var option = "<option value=''> - Select - </option>";
				$("#nextWFGroup").append(option);

				for (var i=0; i<data.userId.length; i++) {
					if (data.defaultRole == data.userId[i]) {
	            	 	option = "<option value='" + data.userId[i] + "' selected>" + data.userName[i] + "</option>";
					}
					else {
						option = "<option value='" + data.userId[i] + "'>" + data.userName[i] + "</option>";
					}
					$("#nextWFGroup").append(option);
				}
            	 
				changeGroup();
            	 
            	 // If target group is MG OFFICER, hide the user dropdown
				if ($("#nextWFGroup").val() == "HO_MG_OFFICER") {
					$("#divWFTargetUser").hide();
				}
				else {
					$("#divWFTargetUser").show();
				}
            	 
				// Update the submit button
				if (data.nextActionName == "" || data.nextActionName == null) {
					$("#btnWFNext").val("Return");
				}
				else {
					$("#btnWFNext").val(data.nextActionName);
				}
				$("#defaultRole").val(data.defaultRole);
				$("#nextAction").val(data.nextActionName);
	
				$("#model_nextWFStep").modal("show");
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	}
	
	function performReturn() {
		$("#divSuccess").hide();
		$("#divError").hide();
	
		$("#divWFTargetUser").show();
	
		$("#returnCase").val("Y");
		
		// Get the default group
		$.ajax ({
            url: "<%=request.getContextPath()%>/request/getPreviousStep",
            type: "POST",
            data: {requestNo: $("#hiddenRequestNo").val(), requestType: $("#requestType").val(), currentStatus: $("#requestStatus").val(), mgTeamInd: $("#mgTeamInd").val()},
            success: function(data) {
				$("#nextWFGroup").empty();
            	 
				var option = "<option value=''> - Select - </option>";
				$("#nextWFGroup").append(option);
            	 
				for (var i=0; i<data.userId.length; i++) {
					option = "<option value='" + data.userId[i] + "'>" + data.userName[i] + "</option>";
					$("#nextWFGroup").append(option);
				}
            	 
				changeGroup();
            	 
				$("#model_generateEmail input[type='button']").attr("disabled", false);
				$("#model_generateEmail button").attr("disabled", false);
            	 
				$("#btnWFNext").val("Return");
				$("#model_nextWFStep").modal("show");
			},
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
		});
	}
	
	function continueSubmitWorkflow() {
		if ($("#nextWFGroup").val()  == "" && $("#nextWFUser").val() == "") {
			$("#warningTitle").html("Warning");
            $("#warningContent").html("Please select either Group or User.");
            $("#warningModal").modal("show");
            
			return;
		}
	
		$("#hiddenFormAction").val("SUBMIT");
		$("#model_nextWFStep").modal("hide");
		$("#model_generateEmail").modal("show");
	}
	
	function changeGroup() {
		$.ajax ({
            url: "<%=request.getContextPath()%>/common/getWFUserList",
            type: "POST",
            data: {requestNo: $("#hiddenRequestNo").val(), groupId: $("#nextWFGroup").val()},
            success: function(data) {
				if ($("#defaultRole").val() == $("#nextWFGroup").val()) {
					$("#btnWFNext").val($("#nextAction").val());
				}
				else {
					$("#btnWFNext").val("Return");
				}
	         
				$("#nextWFUser").empty();
            	 
				var option = "<option value=''> - Select - </option>";
				$("#nextWFUser").append(option);
            	 
				for (var i=0; i<data.userId.length; i++) {
					option = "<option value='" + data.userId[i] + "'>" + data.userName[i] + "</option>";
					$("#nextWFUser").append(option);
				}
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	}
	
	function changeAttachment(obj, attachmentId) {
		// $("#frmDetail").append("<input type='hidden'>");
		// $("#removeAttachment").val(attachmentId);
		$(obj).parent().hide();
		// $("#divAttachmentLink")
		// $("#divUploadSection").show();
		$("#mgTeamInd").append("<input type='hidden' name='removeAttachmentUid' value='" + attachmentId + "'/>");
	}
	
	var files;
	$(function(){
		$("#approvalFile").on('change',prepareLoad);
	
	  	function prepareLoad(event) {
	      	files=event.target.files;
	  	}
  	});
  	
	function processFileUpload(row) {
		if ($(row).find("#approvalFile").val() == "") {
			alert("Please select the file before press the upload button.");
			return;
		}
		
		var oMyForm = new FormData();
		oMyForm.append("approvalFile", $($(row).find("#approvalFile"))[0].files[0]);
     	$.ajax({
     		dataType: 'json',
           	url: "<%=request.getContextPath()%>/common/uploadFile",
           	data: oMyForm,
           	type: "POST",
           	enctype: 'multipart/form-data',
           	processData: false,
           	contentType: false,
           	success: function(result) {
           		$(row).find("#uploadFileId").val(result);
           		$(row).find("#divUploadBefore").hide();
           		$(row).find("#divUploadAfter").find("a").html($(row).find("#divUploadAfter").parent().find("[name='approvalFile']").val());
           		$(row).find("#divUploadAfter").show();
           		
           		var tmpRow = $(row).clone();
				$(tmpRow).find("#approvalFile").val("");
				$(tmpRow).find("#uploadFileId").val("");
				$(tmpRow).find("#divUploadBefore").show();
				$(tmpRow).find("#divUploadAfter").hide();
				$("#divUploadSection").append(tmpRow);
           	},
           	error : function(result){
               	alert("Fail to upload.");
           	}
		});
  	}
  
  	var deleteNewUpload = false;
  	var currentRow = null;
  
  	function reuploadFile(row) {
		deleteNewUpload = true;
		currentRow = row;
		
		$("#model_confirmDeleteAttachment").modal('show');
  	}
  	
  	function confirmReupload() {
  		// Check is this only one attachment, if yes, show the attachment upload section again.
  		if ($("input[name='uploadFileId']").length == 1) {
	  		$(currentRow).find("#approvalFile").val("");
	  		$(currentRow).find("#uploadFileId").val("");
	        $(currentRow).find("#divUploadBefore").show();
	        $(currentRow).find("#divUploadAfter").hide();
	    }
	    // Otherwise, remove the row 
	    else {
	    	$(currentRow).remove();
	    }
  	}
  
  	function downloadTempFile() {
  		window.open('<%=request.getContextPath()%>/common/getTempFile?uid=' + $("#uploadFileId").val(), '_blank');
	}
	
	function cloneRow(row) {
		var tmpRow = $(row).clone();
		$(tmpRow).find("#approvalFile").val("");
		$(tmpRow).find("#divUploadBefore").show();
		$(tmpRow).find("#divUploadAfter").hide();
		$("#divUploadSection").append(tmpRow);
	}
	
	var deleteAttachmentUid = "";
	var selectAttachmentRow = null;
	function deleteAttachment(obj, attachmentUid) {
		deleteNewUpload = false;
	
		deleteAttachmentUid = attachmentUid;
		$("#model_confirmDeleteAttachment").modal('show');
		selectAttachmentRow = obj;
	}
	
	function confirmDeleteAttachment() {
		if (deleteNewUpload) {
			confirmReupload();
		}
		else {
			confirmDeleteExistingAttachment();
		}
	}
	
	function confirmDeleteExistingAttachment() {
		showLoading();
		$.ajax({
            url: "<%=request.getContextPath() %>/request/deleteAttachment",
            type: "POST",
            data: {attachmentUid: deleteAttachmentUid},
            success: function(data) {
            	hideLoading();
            	
            	// Remove the row
            	$(selectAttachmentRow).parent().hide();
			}
		});
	}
</script>
<div class="panel panel-custom-primary" id="divApproval">
	<div class="panel-heading">
		<div class="panel-heading-title">Approval Information</div>
	</div>
	<div class="panel-body">
		<div style="width: 100%;">
			<div class="row">
				<div class="form-group">
					<div class="col-sm-2">
						<label for="txt_reason" class="control-label"><strong>Approval Date</strong><font class="star">*</font></label>
					</div>

					<div class="col-sm-2">
						<div class="input-group date" id="approvalDateGroup">
							<form:input path="formBean.approvalDate" class="form-control"
								id="txtApprovalDate" required="required" />
							<span class="input-group-addon"> <span
								class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
				<div class="col-sm-2">
					<label for="txt_reason" class="control-label"><strong>Reference
					</strong></label>
				</div>
				<div class="col-sm-6">
					<form:input path="formBean.approvalReference" class="form-control"
						id="txtApprovalReference" maxlength="100"/>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-2">
					<label for="txt_reason" class="field_request_label"><strong>Remarks</strong></label>
				</div>
				<div class="col-sm-10">
					<form:input path="formBean.approvalRemark" class="form-control"
						id="txtApprovalRemark" style="width:100%" maxlength="2000"/>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-2">
					<label class="field_request_label"><strong>Attachment</strong></label>
				</div>
				<div class="col-sm-10">
					<c:choose>
						<c:when test="${formBean.approvalAttachmentUid != null}">
							<c:forEach var="listValue"
								items="${formBean.approvalAttachmentUid}" varStatus="loop">
								<div id="divAttachmentLink">
									<a href="<%= request.getContextPath() %>/request/downloadAttachment?aid=<c:out value="${listValue}"/>"><c:out value="${formBean.approvalAttachmentFilename[loop.index]}"/></a>
									<button name="btnRemoveFile" type="button" class="btn btn-danger" style="width:100px"
										onclick="deleteAttachment(this, '<c:out value="${listValue}"/>')" ><i class="fa fa-trash-o"></i> Delete</button>
								</div>
							</c:forEach>
						</c:when>
					</c:choose>
					<div id="divUploadSection">
					<div class="row">
						<div id="divUploadBefore">
							<div class="col-sm-8">
								<input type="file" id="approvalFile" name="approvalFile" style="width:100%" />
							</div>
							<div class="col-sm-2" style="text-align: right">
								<button type="button" name="btnUploadFile"
									onclick="processFileUpload($(this).parent().parent().parent())"
									class="btn btn-primary" style="width:110px"><i class="fa fa-upload"></i> Upload</button>
								<input type="hidden" id="uploadFileId" name="uploadFileId" />
							</div>
						</div>

						<div id="divUploadAfter" style="display: none">
							<div class="col-sm-8">
								<a href="#" onclick="downloadTempFile()">Download</a> <button
									type="button" name="btnRemoveFile" style="width:100px"
									onclick="reuploadFile($(this).parent().parent().parent());"
									class="btn btn-danger"><i class="fa fa-trash-o"></i> Delete</button>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
		
		<form:hidden path="formBean.currentWFUser" />
		<form:hidden path="formBean.submitWithModifiedEmail" />
		<form:hidden path="formBean.submitWithEmail" />
		<form:hidden path="formBean.submitWithoutEmail" />
		<form:hidden path="formBean.requestType" />
		<form:hidden path="formBean.mgTeamInd" />
	</div>
</div>
</div>

<div class="row" style="padding: 5px">
	<div class="col-sm-8" style="text-align:right; width:100%">
		<input type="hidden" id="hidRole" value="${sessionScope.currentRole}"/>
		<c:choose>
			<c:when
				test="${formBean.updateSuccess == 'Y' && formBean.userHaveCreationRight == 'Y'}">
				<button type="button" class="btn btn-default" onclick="createNewRequest()">
					<i class="fa fa-location-arrow"></i> Create another request</button>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${formBean.haveMassSaveRight == 'Y'}">
				<button type="button" class="btn btn-default" name="btnMassSave" onclick="performMassSave()">
					<i class="fa fa-files-o"></i> Multiple Creation</button>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${formBean.userHaveSaveRight == 'Y'}">
				<button type="submit" class="btn btn-primary" id="btnSave"
					name="btnSave" style="width:110px" onclick="submitSave()">
					<i class="fa fa-save"></i> Save</button>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when
				test="${formBean.userHaveSubmitRight == 'Y' && formBean.userHaveApprovalRight != 'Y'}">
				<button type="button" class="btn btn-success" name="btnSubmit"
					style="width:110px" onclick="preSubmit()">
					<i class="fa fa-send"></i> <c:out value="${formBean.submitButtonLabel}"/>
				</button>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${formBean.userHaveApprovalRight == 'Y'}">
				<button type="button" class="btn btn-success" name="btnConfirm"
					style="width:110px" onclick="preApproval()">
					<i class="fa fa-check-square-o"></i> <c:out value="${formBean.submitButtonLabel}"/>
				</button>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${formBean.userHaveSubmitRight == 'Y' && formBean.requestStatus != 'NEW'}">
				<button type="button" class="btn btn-warning" name="btnReturn"
					style="width:110px" onclick="performReturn()">
					<i class="fa fa-reply"></i> Return</button>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${formBean.userHaveWithdrawRight == 'Y' && formBean.requestStatus != 'WITHDRAW' && formBean.requestStatus != 'CONFIRMED'}">
				<button type="button" class="btn btn-danger" name="btnWithdraw"
					style="width:110px" onclick="performWithdraw()">
					<i class="glyphicon glyphicon-remove"></i> Withdraw</button>
			</c:when>
		</c:choose>
	</div>
</div>


<!-- Model to confirm Withdraw - Start -->
<div id="model_confirmWithdraw" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom modal-dialog-postId">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-body">
				<div class="row" style="padding: 20px;">
					<div class="col-sm-12">
						<label for="" class="field_request_label">Are you sure to withdraw this request?</label>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" style="width:110px" data-dismiss="modal" 
					onclick="submitWithdraw();"><i class="fa fa-check"></i> Yes</button>
				<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal">
					<i class="fa fa-times"></i> Cancel</button>
			</div>
		</div>
	</div>
</div>
<!-- Model to confirm Withdraw - End -->

<!-- Model for Email need to edit - Start -->
<div id="model_generateEmail" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom modal-dialog-postId">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Email Notification</b>
			    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
			    </h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-sm-12">
						<label for="" class="field_request_label">An notification
							email will be sent to next party, do you want to modify the content?</label>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" style="width:110px" data-dismiss="modal"
					onclick="editEmail();">Edit Email</button>
				<button type="submit" class="btn btn-success" style="width:110px" data-dismiss="modal"
					onclick="submitWorkflowWithDefaultEmail();"><i class="fa fa-send-o"></i> Send</button>
				<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal">
					<i class="fa fa-times"></i> Cancel</button>
			</div>
		</div>
	</div>
</div>
<!-- Model for Email need to edit - End -->

<!-- Model for edit email - Start -->
<div id="model_editEmail" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom modal-dialog-postId">
		<form:hidden path="formBean.returnCase" />
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Modify Email Content</b>
		    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
		    	</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-sm-2">
						<label for="" class="field_request_label">To<font class="star">*</font></label>
					</div>
					<div class="col-sm-9">
						<form:input path="formBean.emailTo" class="form-control" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<label for="" class="field_request_label">CC</label>
					</div>
					<div class="col-sm-9">
						<form:input path="formBean.emailCC" class="form-control" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<label for="" class="field_request_label">Title<font class="star">*</font></label>
					</div>
					<div class="col-sm-9">
						<form:input path="formBean.emailTitle" class="form-control"
							id="txtEmailTitle" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<label for="" class="field_request_label">Content</label>
					</div>
					<div class="col-sm-9" style="height:200px">
						<div id="divEmailContent"
							style="border:1px solid #DDDDDD; overflow:auto;height:200px"></div>
						<form:hidden path="formBean.emailContent" class="form-control"
							style="width:100%;height:200px" id="txtEmailContent" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<label for="" class="field_request_label">Supplementary Info</label>
					</div>
					<div class="col-sm-9" style="height:200px">
						<form:textarea path="formBean.emailSuppInfo" class="form-control" id="emailSuppInfo" name="emailSuppInfo"
							style="width:100%;height:200px" />
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="submit" class="btn btn-success" style="width:110px;" onclick="submitSendEmail(event)">
					<i class="fa fa-send-o"></i> Send</button>
				<button type="button" class="btn btn-default" style="width:110px" onclick="cancelSendEmail()">
					<i class="fa fa-times"></i> Cancel</button>
			</div>
		</div>

	</div>
</div>
<!-- Model for edit email - Start -->


<!-- Model for select next WF group - Start -->
<div id="model_nextWFStep" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom modal-dialog-postId">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Select Group / User</b>
			    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
			    </h4>
			</div>
			<div class="modal-body" style="width:100%">
				<div class="row">
					<div class="col-sm-2">
						<label for="" class="field_request_label">Group<font class="star">*</font></label>
					</div>
					<div class="col-sm-8">
						<form:select path="formBean.nextWFGroup" name="nextWFGroup"
							class="form-control" onchange="changeGroup()">
							<option value="">- Select -</option>
						</form:select>
					</div>
				</div>
				<div class="row" id="divWFTargetUser">
					<div class="col-sm-2">
						<label for="" class="field_request_label">User</label>
					</div>
					<div class="col-sm-8">
						<form:select path="formBean.nextWFUser" name="nextWFUser"
							class="form-control">
							<option value="">- Select -</option>
						</form:select>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" style="width:110px" onclick="continueSubmitWorkflow();">
					<i class="fa fa-chevron-right"></i> Next</button>
				<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal">
					<i class="fa fa-times"></i> Cancel</button>
			</div>
		</div>
	</div>
</div>
<!-- ./#model_nextWFStep -->

<!-- Model to confirm cancel send email - Start -->
<div id="model_confirmCancelSend" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom modal-dialog-postId">
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Modify Email Content</b></h4>
			</div>
			<div class="modal-body">
				<div class="row" style="padding:20px">
					<div class="col-sm-12">
						<label for="" class="field_request_label">Are you sure to cancel?</label>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal" style="width:110px"
					onclick="confirmCancelSendEmail();"><i class="fa fa-check"></i> Yes</button>
				<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal">
					<i class="fa fa-times"></i> Cancel</button>
			</div>
		</div>
	</div>
</div>
<!-- ./#model_confirmCancelSend -->

<!-- Model to confirm delete attachment - Start -->
<div id="model_confirmDeleteAttachment" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:750px">
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Approval Information - Attachment</b></h4>
			</div>
			<div class="modal-body">
				<div class="row" style="padding:20px">
					<div class="col-sm-12">
						<label for="" class="field_request_label">Are you sure to delete the attachment?</label>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal" style="width:110px"
					onclick="confirmDeleteAttachment();"><i class="fa fa-check"></i> Yes</button>
				<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal">
					<i class="fa fa-times"></i> Cancel</button>
			</div>
		</div>
	</div>
</div>
<!-- ./#model_confirmCancelSend -->

<!-- Model to model_review_funding_submit - Start -->
<div id="model_review_funding_submit" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:750px">
		<div class="modal-content">
			<!--<div class="modal-header" style="background-color:#f39c12">
				<h4><b>Warning</b></h4>
			</div>-->
			<div class="modal-body">
				<div class="row" style="padding:20px">
					<div class="col-sm-12">
						<label for="" class="field_request_label">Have you reviewed the Funding Source Start / End Dates?</label>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal" style="width:110px"
					onclick="performSubmit();"><i class="fa fa-check"></i> Yes</button>
				<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal">
					<i class="fa fa-times"></i> No</button>
			</div>
		</div>
	</div>
</div>
<!-- ./#model_review_funding_submit -->

<!-- Model to model_review_funding_approve - Start -->
<div id="model_review_funding_approve" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:750px">
		<div class="modal-content">
			<!--<div class="modal-header" style="background-color:#f39c12">
				<h4><b>Warning</b></h4>
			</div>-->
			<div class="modal-body">
				<div class="row" style="padding:20px">
					<div class="col-sm-12">
						<label for="" class="field_request_label">Have you reviewed the Funding Source Start / End Dates?</label>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal" style="width:110px"
					onclick="performApproval();"><i class="fa fa-check"></i> Yes</button>
				<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal">
					<i class="fa fa-times"></i> No</button>
			</div>
		</div>
	</div>
</div>
<!-- ./#model_review_funding_approve -->