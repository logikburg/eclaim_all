<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	function performUpdate() {
		$("#divSuccessMessage").hide();
		$("#divErrorMsg").hide();
		
		if (formValidate()) {
			$("#frmDetail").submit();
		}
	}
	
	function formValidate() {
		if ($("#documentDesc").val() == "") {
			$("#divError").html("<b>Document Description</b> cannot be empty.");
			$("#divErrorMsg").show();
			return false;
		}
		
		return true;
	}

	function returnMain() {
		document.location.href = "<%= request.getContextPath() %>/maintenance/documentMaintenance";
	}
	
	function changeType() {
		if ($($("input[name='documentType']")[0]).is(":checked")) {
			$("#divURL").show();
			$("#divAttachment").hide();
		}
		
		if ($($("input[name='documentType']")[1]).is(":checked")) {
			$("#divURL").hide();
			$("#divAttachment").show();
		}
	}
	
	$(function(){
		$(document).on('click', "[name='documentType']", function() {
			changeType();
		});
	
		changeType();
		<c:choose>
			<c:when test="${formBean.documentType == 'A'}">
				$("#divUploadSection").hide();
			</c:when>
		</c:choose>
	});
	
	function changeAttachment() {
		$("#divAttachmentLink").hide();
		$("#divUploadSection").show();
	}
</script>
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > Maintenance > <a href="<c:url value="/maintenance/documentMaintenance"/>">Document Maintenance</a> > Detail
		</div>
		<div id="divSuccess" style="display:none" class="alert-box-success">
			<div class="alert-box-icon-success"><i class="fa fa-check"></i></div>
			<div id="divSuccessMsg" class="alert-box-content-success"></div>
		</div>
		<div id="divError" style="display:none" class="alert-box-danger">
			<div class="alert-box-icon-danger"><i class="fa fa-warning"></i></div>
			<div id="divErrorMsg" class="alert-box-content-danger"></div>
		</div>
		<div class="section-title">
			<div class="title pull-left"><i class="fa fa-file-text-o"></i>Document Details</div>
		</div>
		<div class="panel panel-custom-success">
			<div class="panel-heading">
				<div class="panel-heading-title">Document Details</div>
			</div>
			<div class="panel-body">
				<form id="frmDetail" method="POST" enctype="multipart/form-data">
				
					<div class="row">
						<div class="col-sm-2">
							<label class="field_request_label">Document Description<font class="star">*</font></label>
						</div>
						<div class="col-sm-8">
							<form:textarea path="formBean.documentDesc" style="width:100%" class="form-control" id="documentDesc" name="documentDesc"/>		
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<label class="field_request_label">Document Type<font class="star">*</font></label>
						</div>
						<div class="col-sm-8">
							<form:radiobutton path="formBean.documentType" value="U"/><label class="radio-inline" >URL</label>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<form:radiobutton path="formBean.documentType" value="A"/><label class="radio-inline" >Attachment</label>
						</div>
					</div>
					<div class="row" id="divURL">
						<div class="col-sm-2">
							<label class="field_request_label">URL</label>
						</div>
						<div class="col-sm-8">
							<form:input path="formBean.documentUrl" class="form-control" style="width:100%" />
						</div>
					</div>
					<div class="row" id="divAttachment">
						<div class="col-sm-2">
							<label class="field_request_label">Attachment</label>
						</div>
						<div class="col-sm-2">
							<div id="divUploadSection">			
								<input type="file" name="documentFile" style="width:100%"/>	
							</div>
						</div>
					</div>
				
					<form:hidden path="formBean.documentUid"/>
					<form:hidden path="formBean.lastUpdatedDate"/>
				
					<div class="row">
						<div class="col-sm-12" style="text-align:right">
							<button type="button" class="btn btn-primary" style="width:110px" onclick="performUpdate()"><i class="fa fa-floppy-o"></i> Save</button>
							<button type="button" class="btn btn-default" style="width:110px" onclick="returnMain()"><i class="fa fa-undo"></i> Cancel</button>
						</div>
					</div>
				</form>
			</div>
			
		</div>
		
	</div>
	<!-- ./#container -->
</div>
<!-- ./#page-content-wrapper -->

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>