<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>
<script>

	function performUpdate() {
		$("#divErrorMsg").html('');
		$("#divError").hide();
		$("#divSuccessMsg").html('');
		$("#divSuccess").hide();
	}
	
	function isManpower(e){
		$('#frmDetail').bootstrapValidator('enableFieldValidators','manPowerShortage', e);
		
		if(e){
			$('.manpower').show();
			$('.manpower').removeClass("hide");
		}else{
			$('.manpower').val('');
			$('.manpower').hide();
		}
	}
	
	function isOTA(isOTA){
		$('#frmDetail').bootstrapValidator('enableFieldValidators','otaJustifications', isOTA);
		
		if(isOTA){
			$('.otaj').show();
			$('.otaj').removeClass("hide");
		}else{
			$('.otaj').hide();
		}
	}
	
	var files;
	function processFileUpload(row) {
		
		var bytesOf10MB = 10485760;
		
		if ($(row).find("#approvalFile").val() == "") {
			alert("Please select the file before press the upload button.");
			return;
		}
		var file = $(row).find("#approvalFile")[0].files[0];
		
		if(file.size > bytesOf10MB){
			alert("upload file size can't more than 10MB.");
			return;
		}
		
		var oMyForm = new FormData();
		oMyForm.append("approvalFile", $($(row).find("#approvalFile"))[0].files[0]);
		oMyForm.append("desc", $(row).find('#fileDesc').val());
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
	
  	function prepareLoad(event) {
      	files=event.target.files;
  	}
  	
	$(function() {
// 		$("#wrapper").toggleClass("active");
		<c:choose>
			<c:when test="${formBean.manPowerShortage != null}">
				$(".manpower").show();
				$('.manpower').removeClass("hide");
			</c:when>
		</c:choose>
		
		isOTA($("#usingOTA1").prop("checked"));
		
		$("#approvalFile").on('change',prepareLoad);
	  	
		$('[data-toggle="popover"]').popover({
				html : true,
			    content: function() {
			        return $('#popover_content_wrapper').html();
			      }
		});
		$("#popover_content_wrapper").load("../html/justificationNote.html");

		$('[data-toggle="popover1"]').popover({
			html : true,
		    content: function() {
		        return $('#qDeliver_popover_content_wrapper').html();
		      }
		});
		$("#qDeliver_popover_content_wrapper").load("../html/quantifiableNote.html");
		
		$("#frmDetail")
				.bootstrapValidator(
						{
							excluded : [ ':disabled', ':hidden' ],
							message : ' This value is not valid',
							live : "submitted",
							fields : {
								justification : {
									message : 'The justifications is not valid',
									validators : {
										notEmpty : {
											message : 'The justification is required and cannot be empty'
										},
										stringLength : {
											max : 4000,
											message : 'The justification must less than 4000 characters long'
										}
									}
								},
								manPowerShortage : {
									enabled : false,
									validators : {
										notEmpty : {
											message : 'The manpower situation is required and cannot be empty'
										},
										stringLength : {
											max : 4000,
											message : 'The manpower situation must less than 4000 characters long'
										}
									}
								},
								otaJustifications : {
									enabled : false,
									message : 'The justifications is not valid',
									validators : {
										notEmpty : {
											message : 'The justification is required and cannot be empty'
										},
										stringLength : {
											max : 4000,
											message : 'The justification must less than 4000 characters long'
										}
									}
								},
								circumIds : {
									validators : {
										choice : {
											min : 1,
											message : 'Please select at last one Circumstance'
										}
									}
								},
							},
						}).on('success.form.bv', function(e) {

				});

	});
</script>

<style type="text/css">
    .popover{
        max-width:100%;
    }
    .popoverBtn{
     	color: #337AB7;
    	border: none;
    }
</style>

<!-- Page Content -->
<div id="page-content-wrapper">
	<div id="currStep" style="display: none">3</div>
	<div class="container-fluid">
		<div id="divSuccess" style="display: none" class="alert-box-success">
			<div class="alert-box-icon-success">
				<i class="fa fa-check"></i>
			</div>
			<div id="divSuccessMsg" class="alert-box-content-success"></div>
		</div>
		<div id="divError" style="display: none" class="alert-box-danger">
			<div class="alert-box-icon-danger">
				<i class="fa fa-warning"></i>
			</div>
			<div id="divErrorMsg" class="alert-box-content-danger"></div>
		</div>

		<!-- Function Name -->
<!-- 		<div class="section-title"> -->
			<%@ include file="/WEB-INF/views/project/projectHeading.jsp"%>
<!-- 		</div> -->

		<form id="frmDetail" method="POST" enctype="multipart/form-data">
			<div class="panel panel-custom-primary">

				<div class="panel-body">
					<div class="form-group">
					<div class="col-sm">
						<label for="ch">Circumstances under which SHS is applied (examples, list not exhaustive) (Please "<i class="fa fa-check" ></i>" the appropriate circumstances) <font class="star">*</font></label>
							<div class="form-group">
								<ul class="list-group">
								<c:forEach var="listValue" items="${circumList}">
									<li class="list-group-item">
										<input type="checkbox" id="circumId_${listValue.circumstanceId}" name="circumIds" <c:if test="${fn:contains(formBean.circumIdinString, listValue.circumstanceId)}"> checked</c:if>
										<c:if test="${listValue.manpowerShortage == 'Y'}"> onchange="isManpower(this.checked)" </c:if> class="custom-control-input" value="<c:out value="${listValue.circumstanceId}" />" > 
										<c:out value="${listValue.displaySeq}"/>. <c:out value="${listValue.descriptionQ}" />
									</li>
								</c:forEach>
								</ul>
							</div>
					</div>
					<input id="circumId" type="hidden" value="${formBean.circumIdinString}"/>
					</div>
					
					<div class="col-sm manpower hide">		
						<div class="form-group">
								<label for="manpower">Manpower situation (strength and vacancies by rank) of the department unit <font class="star">**</font> (for manpower situation only)</label>
								<br><i>Download supplementary document templates</i> <a href="<%=request.getContextPath()%>/common/getSysFile?code=MANPOWER"><u>For New Project</u></a> <a href="<%=request.getContextPath()%>/common/getSysFile?code=MANPOWER_EXT"><u>For Project Extension</u></a>
								<form:textarea path="formBean.manPowerShortage" rows="2" type="text" 
									id="manpower" required="required" class="form-control"></form:textarea>
						</div>
					</div>
					
					<br>
					<div class="col-sm manpower hide">
							<label for="document">Upload supporting document, if any</label>
					</div>
					<div class="col-sm manpower hide">
<%-- 						<c:choose> --%>
<%-- 							<c:when test="${formBean.approvalAttachmentUid != null}"> --%>
<%-- 								<c:forEach var="listValue" --%>
<%-- 									items="${formBean.approvalAttachmentUid}" varStatus="loop"> --%>
<!-- 									<div id="divAttachmentLink"> -->
<%-- 										<a href="<%= request.getContextPath() %>/request/downloadAttachment?aid=<c:out value="${listValue}"/>"><c:out value="${formBean.approvalAttachmentFilename[loop.index]}"/></a> --%>
<!-- 										<button name="btnRemoveFile" type="button" class="btn btn-danger" style="width:100px" -->
<%-- 											onclick="deleteAttachment(this, '<c:out value="${listValue}"/>')" ><i class="fa fa-trash-o"></i> Delete</button> --%>
<!-- 									</div> -->
<%-- 								</c:forEach> --%>
<%-- 							</c:when> --%>
<%-- 						</c:choose> --%>
						<div id="divUploadSection">
							<div class="row">
								<div id="divUploadBefore">
									<div class="col-sm-4">
										<input type="file" id="approvalFile" name="approvalFile" style="width:100%" />
									</div>
									<div class="col-sm-4" style="text-align: left">
										<input type="text" id="fileDesc" style="width:100%" placeholder="Description"/>
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
					<div class="col-sm">
						<div class="form-group">
								<label for="justification">Justifications <font class="star">*</font> 
								<button type="button" id="justificationPop" class="popoverBtn" data-toggle="popover" data-html="true" data-trigger="focus">
									<i class="fa fa-info-circle fa-2x"></i> 
								</button>
								</label>
								<form:textarea path="formBean.justifications" rows="4" type="text"
									id="justification" required="required" class="form-control"></form:textarea>
						</div>
					</div>
					
					<br>
					<div class="col-sm">
						<div class="form-group">
							<label for="other">Is the department/unit currently using Overtime Allowance(OTA) / intending to use OTA in parallel to compensate staff in the rank requested in the SHS project?</label>
							<br>
				            <label class="radio-inline"><form:radiobutton class="custom-control-input" path = "formBean.usingOTA"  value = "Y" onclick="isOTA(true)"/>Yes</label>
				            <label class="radio-inline"><form:radiobutton class="custom-control-input" path = "formBean.usingOTA"  value = "N" onclick="isOTA(false)"/>No</label>
						</div>
					</div>
					
					<br>
					<div class="col-sm otaj hide">
						<div class="form-group">
							<label>Please provide justifications for using OTA and SHS in parallel for the same rank under the same project.</label>
							<br>
							<label for="justification">Justifications <font class="star">*</font> 
							<a href="#" id="justificationPop" data-toggle="popover" data-html="true" data-trigger="focus">
<!-- 							<i class="fa fa-info-circle fa-2x"></i> -->
							</a>
							</label>
							<form:textarea path="formBean.otaJustifications" rows="4" type="text"
								id="justification" required="required" class="form-control"></form:textarea>
						</div>
					</div>
					
					<br>
					<div class="col-sm">
						<div class="form-group">
							<label for="other">Others e.g. trigger point(s), if any, for trigger or deactivation of use of SHS</label>
							<form:textarea path="formBean.triggerPoint" rows="4" type="text" id="other"
								class="form-control"></form:textarea>
						</div>
					</div>
					<br>
					<div class="col-sm">
						<div class="form-group">
							<label for="qDeliver">Quantifiable Deliverables <font class="star">*</font>
							<button type="button" id="qDeliverPop" class="popoverBtn" data-toggle="popover1" data-html="true" data-trigger="focus">
								<i class="fa fa-info-circle fa-2x"></i> 
							</button>
							
							</label>
							<form:textarea path="formBean.qDeliver" rows="4" required="required" type="text" id="qDeliver"
								class="form-control"></form:textarea>
						</div>
					</div>
					
					<form:hidden path="formBean.projectVerId" />
					<form:hidden id="projectStatus" path="formBean.projectStatus" />
					<form:hidden id="recType" path="formBean.recType" />
					<form:hidden id="projectStep" path="formBean.projectStep" />
					<br>
					<%@ include file="/WEB-INF/views/project/projectFooter.jsp"%>
				</div>
			</div>
		</form>
	</div>
</div>

<div id="popover_content_wrapper" style="display: none;"></div>
<div id="qDeliver_popover_content_wrapper" style="display: none;"></div>

<!-- Model to confirm delete attachment - Start -->
<div id="model_confirmDeleteAttachment" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:750px">
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Attachment</b></h4>
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

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>