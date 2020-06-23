<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/jquery/typeahead/bootstrap3-typeahead.js"%>"></script>
<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/jquery/typeahead/bloodhound.js"%>"></script>


<link rel="stylesheet" href="<c:url value="/plugins/bootstrap/tagsinput/css/bootstrap-tagsinput.css" />" type="text/css" media="all" />
<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/bootstrap/tagsinput/bootstrap-tagsinput.js"%>"></script>

<style>
<!--
.panel-heading .accordion-toggle:before {
	font-family: 'Glyphicons Halflings';
	content: "\e114";
	float: left;
	padding-right: 12px;
	color: #FFFFFF;
}

.panel-heading .accordion-toggle.collapsed:before {
	content: "\e080";
}

.panel-default>.panel-heading+.panel-collapse>.panel-body {
	padding-bottom: 6px;
	padding-top: 8px;
}

.sidebar-nav li.active>a, a[aria-expanded="true"] {
	color: #FFFFFF;
	background: none;
}

.panel {
	border-top: 3px solid #777777;
}

.panel-default {
	border-color: #777;
}

.panel-default>.panel-heading {
	color: #333;
	background-color: #2f79b9;
	border-color: #777;
	border-top-left-radius: unset;
	border-top-right-radius: unset;
}

.panel-title>.small, .panel-title>.small>a, .panel-title>a, .panel-title>small,
	.panel-title>small>a {
	color: #FFFFFF;
}

div.job-summary {
	padding-bottom: 10px;
}

div#circumstances .group-item {
	padding-top: 10px;
	padding-bottom: 10px;
}

.text-underline {
	text-decoration: underline;
}

.descButton{
   	width:"100px";
}

.finance-summary {
	padding-bottom: 10px;
	text-align: center;
}

.finance-summary table thead th {
	text-align: center;
}

.finance-summary input.col-text, .finance-summary select.col-select { 
	width: 100%; 
	background: #ffffff; 
	border: 1px solid #dfdfdf; 
	height: 26px;
}

/* skinned Radio button */
label.btn>span.fa-stack {
	width: 1em;
	line-height: 1.9em;
}

label.btn span {
	font-size: 1.5em;
}

label.btn span:last-child {
	font-size: 1.0em;
}

label:hover input[type="radio"] ~ i.fa {
	color: #7AA3CC;
}

div[data-toggle="buttons"] label.active {
	color: #7AA3CC;
}

div[data-toggle="buttons"] label {
	display: inline-block;
	padding: 6px 12px;
	margin-bottom: 0;
	font-size: 14px;
	font-weight: normal;
	line-height: 2em;
	text-align: left;
	white-space: nowrap;
	vertical-align: top;
	cursor: pointer;
	background-color: none;
	border: 0px solid #c8c8c8;
	border-radius: 3px;
	color: #c8c8c8;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	-o-user-select: none;
	user-select: none;
	pointer-events:none;
}

div[data-toggle="buttons"] label.active span.uncheck {
	display: none;
}

div[data-toggle="buttons"] label:not(.active) span.check {
	display: none;
}

div[data-toggle="buttons"] label:hover {
	color: #7AA3CC;
}

div[data-toggle="buttons"] label:active, div[data-toggle="buttons"] label.active {
	-webkit-box-shadow: none;
	box-shadow: none;
}

.icon-green {
	color: #0fc14a;
}

.icon-black {
	color: #000000;
}

.bootstrap-tagsinput {
  width: 100% !important;
}
.bootstrap-tagsinput input {
  width: 100% !important;
}

-->
</style>

<script>
	$(document).ready(function() {
		var status = $("#projectStatus").val();
		console.log(status);
		switch(status){
			case "PENDING_APPR":
			case "APPROVED":
				$("#icConfirmd").addClass("active");
			case "PENDING_HOSP_IC":
				$("#finVetted").addClass("active");
			case "PENDING_FIN_VET":
				$("#hrVetted").addClass("active");
				break;				
		}
		
		if(status == 'DRAFT'){
			$("#apprHistTab").hide();
			$("#statusTab").hide();
			$('#finImplTab').hide();
		}else if(status == 'OPEN'){
			$("#statusTab").hide();
			$('#finImplTab').hide();
		}else if(status == 'PENDING_FIN_VET' || status == 'PENDING_HOSP_IC'){
			$("#apprHistTab").removeClass("hide");
			$("#statusTab").removeClass("hide");
			$('#finImplTab').removeClass("hide");
		}else{
			$("#apprHistTab").removeClass("hide");
			$("#statusTab").removeClass("hide");
			$('#finImplTab').hide();
		}
	});

	function performSubmit() {
		$("#divSuccess").hide();
		$("#divError").hide();
		$("#hiddenFormAction").val("SUBMIT");
		$("#requestType").val("NEW");
		$("#returnCase").val("N");
		
		$('#frmDetail').bootstrapValidator('validateField', 'ownerEndorse');
		$('#frmDetail').bootstrapValidator('validateField', 'finIcName');
		if($('#frmDetail').data('bootstrapValidator').isValid()){
			editEmail();
		}
	}
	
	function performReturn() {
		$("#divSuccess").hide();
		$("#divError").hide();
		$("#hiddenFormAction").val("RETURN");
		$("#returnCase").val("Y");
		editEmail();
	}
	
	function pad(str, max) {
		  str = str.toString();
		  return str.length < max ? pad("0" + str, max) : str;
		}
	
	function editEmail() {
		var emailTemplateId = "";
		var toRole = "";
		var ccRole = "";
		
		if ($("#hiddenFormAction").val() == "SUBMIT") {
			emailTemplateId = $("#toTemplateId").val();
			toRole = $("#toRole").val();
		}
		else if ($("#hiddenFormAction").val() == "RETURN") {
			emailTemplateId = $("#returnTemplateId").val();
			toRole = $("#returnRole").val();
		}
	
		var postIdList = "";
		var postIdList2 = "";

		// Ajax call to perform search
		var searchData = {
			requestId: $("#hiddenRequestNo").val(),
			verId: $('#projectVerId').val(),
			templateId: emailTemplateId, 
			param: "SHS"+pad($("#hiddenRequestNo").val(),5),
    	   	param2: '<c:out value="${formBean.projectName}"/>',
			param3: '<c:out value="${sessionScope.userProfile.userName}"/>',
			param4: $.datepicker.formatDate('dd/mm/yy', new Date()),
			param5: postIdList2,
			param6: $('#txtFinIcId').val(),
			param7: '<c:out value="${formBean.projectPreparer}"/>',
			param8: '<c:out value="${formBean.projectOwner}"/>',
			param9: "",
			wfGroup: "",
			wfUser: "",
			isReturn: $("#returnCase").val(),
			toRole: toRole,
			ccRole: ccRole
		}
		$.ajax({
            url: "<%=request.getContextPath()%>/api/searchEmailTemplate",
            type: "POST",
            contentType: 'application/json; charset=utf-8',
      		dataType: 'json',
      		data: JSON.stringify(searchData),
            success: function(data) {
	             $("#txtEmailTitle").val(data.templateTitle);
	             $("#txtEmailContent").val(data.templateContent);
	             $("#divEmailContent").html(data.templateContent);
//             	 $("#emailTo").val(data.toEmailList);
				console.log(data.emailToList);
	            $.each(data.emailToList, function (index, value) {
	            	$("#emailTo").tagsinput('add', value);
	            });
//             	 $("#emailCC").val(data.ccEmailList);
            	 $.each(data.emailCCList, function (index, value) {
 	            	$("#emailCC").tagsinput('add', value);
 	            });
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	
		$("#txtEmailTitle").attr("readonly", true);
		$("#model_editEmail").modal("show");
	}
	
	function performUpdate() {
		$("#divErrorMsg").html('');
		$("#divError").hide();
		$("#divSuccessMsg").html('');
		$("#divSuccess").hide();
	}

	// File Upload start
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
	// File Upload end
	
	function validateEmail(email) {
	    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	    return re.test(String(email).toLowerCase());
	}
	
	$(function() {
// 		$("#wrapper").toggleClass("active");

		<c:choose>
		<c:when test="${formBean.updateSuccess == 'Y'}">
			$("#divSuccessMsg").html('<c:out value="${formBean.displayMessage}"/>');
			$("#divSuccess").show();
		</c:when>
		<c:when test="${formBean.updateSuccess == 'N'}">
			$("#divErrorMsg").html('<c:out value="${formBean.displayMessage}"/>');
			$("#divError").show();
		</c:when>
		</c:choose>
		
// 		var coaType = new Bloodhound({
// 			  datumTokenizer: Bloodhound.tokenizers.whitespace,
// 			  queryTokenizer: Bloodhound.tokenizers.whitespace,
// 			  prefetch : "<c:url value='/common/getCoaType'/>"
// 			});
// 		coaType.initialize();
		
		var coaFund = new Bloodhound({
			  datumTokenizer: Bloodhound.tokenizers.whitespace,
			  queryTokenizer: Bloodhound.tokenizers.whitespace,
			  prefetch : "<c:url value='/common/getCoaFund'/>"
			});
		coaFund.initialize();
		
		var coaInst = new Bloodhound({
			  datumTokenizer: Bloodhound.tokenizers.whitespace,
			  queryTokenizer: Bloodhound.tokenizers.whitespace,
			  prefetch : "<c:url value='/common/getCoaInst'/>"
			});
		coaInst.initialize();
		
		var coaSection = new Bloodhound({
			  datumTokenizer: Bloodhound.tokenizers.whitespace,
			  queryTokenizer: Bloodhound.tokenizers.whitespace,
			  prefetch : "<c:url value='/common/getCoaSection'/>"
			});
		coaSection.initialize();
		
		var coaAnaly = new Bloodhound({
			  datumTokenizer: Bloodhound.tokenizers.whitespace,
			  queryTokenizer: Bloodhound.tokenizers.whitespace,
			  prefetch : "<c:url value='/common/getCoaAnaly'/>"
			});
		coaAnaly.initialize();
		
		$('.instCOA').typeahead({ source:coaInst.ttAdapter() }).blur(function(){
			$(this).val(coaInst.get($(this).val()));
        });
		
		$('.fundCOA').typeahead({source:coaFund.ttAdapter() }).blur(function(){
// 			console.log('source:'+$(this).val());
// 			console.log('coaFund:'+coaFund.get($(this).val()));
            $(this).val(coaFund.get($(this).val()));
        });
		
		$('.sectCOA').typeahead({ source:coaSection.ttAdapter() }).blur(function(){
			$(this).val(coaSection.get($(this).val()));
        });
		
		$('.analyCOA').typeahead({ source:coaAnaly.ttAdapter() }).blur(function(){
			$(this).val(coaAnaly.get($(this).val()));
        });

		
				
		$('#emailTo, #emailCC').tagsinput({
		  itemValue: 'email',
		  itemText: 'name',
		  typeahead: {
		    displayKey: 'name',
		    changeInputOnMove: false,
			source: function (query, process) {
			     return $.get("<c:url value='/common/getEmailList'/>", { name: query });
			 },
			 afterSelect: function() {
// 				 console.log(this.$element[0].value);
	             this.$element[0].value = '';
	         }
		  }
		});
		
		$("#frmDetail").bootstrapValidator({
			excluded : [ ':disabled', ':hidden' ],
			message : 'This value is not valid',
			live : "submitted",
			fields : {
// 				ownerEndorse : {
// 					enabled : false,
// 					validators : {
// 						notEmpty : {
// 							message : 'You must endorse the application.'
// 						}
// 					}
// 				}
			}
		});
		
		$('#frmDetail').on('keyup keypress', function(e) {
			  var keyCode = e.keyCode || e.which;
			  if (keyCode === 13){
				if(e.target.type == 'text'){
					var $element = $(e.target);
					console.log('element:' +$element);
					var elTVal = $.trim($element.val());
					
					if (!$element.data('tagsinput') && elTVal.length > 0 && validateEmail(elTVal) ) {
						var elt = $element.parents('.col-sm-9').children('.form-control');
						elt.tagsinput('add', {
							"name" : elTVal,
							"email" : elTVal
						});
						$element.val('');
					}
				}
			    e.preventDefault();
			    return false;
			  }
			});
		
		$("#txtFinIcName").autocomplete({
			source: function(request, response) {
				$.getJSON("<c:url value='/project/getUserNameList'/>", request, function(result){
					response($.map(result, function(item, key){
						console.log(key);
						console.log(item);
						return {label: item, value: key}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#txtFinIcId").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#txtFinIcId").val(ui.item.value);
				return false;
			},
		});
		
		$('#btnCollapseAll').on('click', function(event) {
			console.log('btnCollapseAll > clicked');
			$('.panel-collapse').collapse('hide');
		});

		$('#btnExpandAll').on('click', function(event) {
			console.log('btnExpandAll > clicked');
			$('.panel-collapse').collapse('show');
		});
		
		$(".estImpactBtn").click(function(e){
			if($('.estImpact').is(':visible')){
				$('.estImpact').addClass('hide');
				$(this).html("Show Esitmated Financial Impact");
			}else{
				$('.estImpact').removeClass('hide');
				$(this).html("Hide Esitmated Financial Impact");
			}
			
		});
		
		$('#jobModal').on('show.bs.modal', function (event) {
			  var button = $(event.relatedTarget) // Button that triggered the modal
			  var $row =  button.parents('tr');
			  var jobRank = $row.find("select[name$='jobRank']");
			  var desc = $row.find("input[name$='description']");
			  var info = $row.find("input[name$='otherInfo']");
			  var app = $row.find("input[name$='targerApp']");
			 
			  var modal = $(this)
			  if(jobRank.val() != null){
				  var rankList = Array.prototype.slice.call(jobRank.val());			  
				  modal.find('#jobRank').text(' for '+ rankList.join(',') + ')');
			  }else{
				  modal.find('#jobRank').text(')');
			  }
			  modal.find('#rowIndex').text($row.prop('rowIndex'));
			  modal.find('#jobDesc').val(desc.val());
			  modal.find('#otherInfo').val(info.val());
			  modal.find('#targetApp').val(app.val());
			  
		});
		
		
	});
	
	function submitSendEmail() {
	}
</script>

<!-- Page Content -->
<div id="page-content-wrapper">
	<div id="currStep" style="display: none">4</div>
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

		<%@ include file="/WEB-INF/views/project/projectHeading.jsp"%>

		<form id="frmDetail" method="POST">
			<div class="panel panel-custom-primary">

				<div class="panel-body">
					<div class="form-group">

					<div class="col-md">
						<div class="row">
							<div class="col-md-12">
								<button type="button" class="btn btn-default" id="btnExpandAll"
									name="btnExpandAll">
									<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
									Expand All
								</button>
								<button type="button" class="btn btn-default"
									id="btnCollapseAll" name="btnCollapseAll">
									<span class="glyphicon glyphicon-minus" aria-hidden="true"></span>
									Collapse All
								</button>
							</div>
						</div>
						<div class="panel-group" id="accordionReviewPage"
							aria-multiselectable="true">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title">
										<a class="accordion-toggle " data-toggle="collapse"
											href="#generalInfo"> General Information</a>
									</h4>
								</div>
								<div id="generalInfo" class="panel-collapse collapse in">
									<div class="panel-body">
										<div class="row">
											<div class="col-md-2 col-darkgrey">Project No. :</div>
											<div class="col-md-4"><span><c:out value="${formBean.projectId}"/></span></div>
										</div>
										<div class="row">
											<div class="col-md-2">HCM Department:</div>
											<div class="col-md-4"><c:out value="${formBean.departmentName}"></c:out></div>
										</div>
										<div class="row hide">
											<div class="col-md-2">SRS Hospital</div>
											<div class="col-md-4">PWH</div>
											<div class="col-md-2">SRS Department</div>
											<div class="col-md-4">Neurosurgery</div>
										</div>
										<div class="row">
											<div class="col-md-2">Project Name:</div>
											<div class="col-md-4"><span><c:out value="${formBean.projectName}"/></span></div>
										</div>
										<div class="row">
											<div class="col-md-2">&#38917;&#30446;&#21517;&#31281;:</div>
											<div class="col-md-4"><c:out value="${formBean.projectNameC}"/></div>
										</div>
										<div class="row">
											<div class="col-md-2">Project Purpose:</div>
											<div class="col-md-4"><span><c:out value="${formBean.projectPurpose}"/></span></div>
										</div>
										<div class="row">
											<div class="col-md-2">Project Owner:</div>
											<div class="col-md-4"><span><c:out value="${formBean.projectOwner}"/></span></div>
											<div class="col-md-2">Project Preparer:</div>
											<div class="col-md-4"><span><c:out value="${formBean.projectPreparer}"/></span></div>
										</div>
										<div class="row">
											<div class="col-md-2">Funding Source:</div>
											<div class="col-md-4"><span><c:out value="${formBean.fundingSource}"/></span></div>
											<div class="col-md-2">Program Type:</div>
											<div class="col-md-4"><span><c:out value="${formBean.programType}"></c:out></span></div>
										</div>
										<div class="row">
											<div class="col-md-2">Project Duration:</div>
											<div class="col-md-4"><span>From <c:out value="${formBean.startDate}"/> To <c:out value="${formBean.endDate}"/></span></div>
										</div>
										
										<div class="row">
												<div class="col-md-12">
												<table class="table table-striped table-bordered" style="width:550px;">
														<thead>
															<tr>
																<th>Session Days/Date</th>
																<th>Start Time</th>
																<th>End Time</th>
															</tr>
														</thead>
														<tbody>
														<c:forEach items="${formBean.scheduleList}" var="schedule" varStatus="status">
															<tr>
															<c:choose>
																<c:when test="${empty schedule.patternDesc}">
																	<td><c:out value="${schedule.scheduleDate}"/></td>
																</c:when>
																<c:otherwise>
																	<td><c:out value="${schedule.patternDesc}"/></td>
																</c:otherwise>
															</c:choose>
																<td><c:out value="${schedule.startTime}"/></td>
																<td><c:out value="${schedule.endTime}"/></td>
															</tr>
														</c:forEach>
														<c:if test="${empty formBean.scheduleList}">
															<tr><td colspan="5" ><center>No schedule record</center></td></tr>
														</c:if>
														</tbody>
													</table>
												</div>
										</div>
										
										<div class="row">
											<div class="col-md-12">
												<a class="btn-lg btn-primary pull-right" href="<c:url value="/project/newProject"> <c:param name="projectId" value="${formBean.projectId}"/> </c:url>">
												Edit</a>
											</div>
										</div>
									</div>
								</div>
							</div>
							
														<div class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title">
										<a class="accordion-toggle collapsed" data-toggle="collapse"
											href="#postDetail">Post Detail Information</a>
									</h4>
								</div>
								<div id="postDetail" class="panel-collapse collapse">
									<div class="panel-body">
										<h5>Job Summary</h5>
										<div class="row job-summary">
											<div class="col-md-12">
												<table class="table table-bordered">
													<thead>
														<tr>
															<th style="width: 10%">Staff Group</th>
															<th style="width: 10%">Job Required</th>
															<th style="width: 10%">Project <br>Co-ordinator</th>
															<th style="width: 45%">Job Descriptions <br>/ Competency Requirements</th>
															<th style="width: 4%">Quota</th>
															<th style="width: 5%">No. of <br>Days</th>
															<th style="width: 5%">Session<br>/Day</th>
															<th style="width: 5%">Hours<br>/Session</th>
															<th style="width: 5%">Total<br> Hours</th>
															<th class="hide estImpact" style="width: 5%;">Estimated<br> Financial<br> Impact</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach items="${formBean.jobList}" var="job" varStatus="status">
														<tr>
															<td><c:out value="${job.staffGrp}"/></td>
															<td><c:out value="${job.jobRanks}"/></td>
															<td><c:out value="${job.coOrdinator}"/></td>
															<td><div><c:out value="${job.description}"/></div><br>
															<button name="descDetails" type="button" class="btn btn-primary descButton" data-toggle="modal" data-target="#jobModal">More</button>
															</td>
															<td><c:out value="${job.quota}"/></td>
															<td><c:out value="${job.duration}"/></td>
															<td><c:out value="${job.sessionDay}"/></td>
															<td><c:out value="${job.sessionHour}"/></td>
															<td><c:out value="${job.totalHour}"/></td>
															<td class="hide estImpact"><c:out value="${job.estImpact}"/></td>
															<td class="hide"><input name="description" value= "${job.description}" class='hide form-control' type="text"/></td>
															<td class="hide"><input name="otherInfo" value= "${job.otherInfo}" class='hide form-control' type="text"/></td>
															<td class="hide"><input name="targerApp" value= "${job.targerApp}" class='hide form-control' type="text"/></td>
														</tr>
														</c:forEach>
														<tr id="footerRow">
															<td colspan="8"><div class="pull-right">Total:</div></td>
															<td><span id="totHour" style="font-size:16px;"><c:out value="${formBean.totHour }"/></span></td>
															<td class="hide estImpact"><span id="totEstImp" style="font-size:16px;"><c:out value="${formBean.totEstImp}"/></span></td>
														</tr>
													</tbody>
												</table>
											</div>
											<div class="col-sm-12">
												<div class="pull-right">
													<button name="estImpactBtn" type="button" class="btn btn-primary estImpactBtn">Show Estimated Financial Impact</button>
												</div>
											</div>
										</div>
										<div class="row">
											<div class="col-md-12">
												<a class="btn-lg btn-primary pull-right" href="<c:url value="/project/jobDetails"> <c:param name="projectId" value="${formBean.projectId}"/> </c:url>">
												Edit</a>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<div class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title">
										<a class="accordion-toggle collapsed" data-toggle="collapse"
											href="#circumstances">Circumstances &amp; Justification</a>
									</h4>
								</div>
								<div id="circumstances" class="panel-collapse collapse">
									<div class="panel-body">
										<div class="row group-item">
											<div class="col-md-12">
												<p class="text-underline">Circumstance under which SHS
													is apply</p>
												<ul>
													<c:forEach items="${formBean.circumList}" var="circum" varStatus="status">
														<li><c:out value="${circum}"></c:out></li>
													</c:forEach>
												</ul>
											</div>
										</div>
										<div class="row group-item">
											<div class="col-md-12">
												<p class="text-underline">Justifications</p>
												<p><c:out value="${formBean.justifications}"></c:out></p>
											</div>
										</div>
										<div class="row group-item">
											<div class="col-md-12">
												<p class="text-underline">In the department/unit currently using Overtime
													Allowance (OTA) / intending to use OTA in parallel to
													compensate staff in the rank requested in this SHS project?</p>
												<c:choose>
												<c:when test="${formBean.usingOTA == 'Y'}">
												<p>
													<strong>Yes, the following reason(s):</strong>
												</p>
												<p>
													<c:out value="${formBean.otaJustifications}"/>
												</p>
												</c:when>
												 <c:otherwise>
													<p>
														<strong>No</strong>
													</p>
												</c:otherwise>
												</c:choose>
											</div>
										</div>
										<div class="row group-item">
											<div class="col-md-12">
												<p class="text-underline">Others e.g. trigger point(s),
													if any, for trigger or deactivation of use of SHS</p>
												<p><c:out value="${formBean.triggerPoint}"></c:out></p>	
											</div>
										</div>
										<c:if test="${formBean.manPowerShortage != null}">
											<div class="row group-item">
												<div class="col-md-12">
													<p class="text-underline">Manpower situation</p>
													<p><c:out value="${formBean.manPowerShortage}"></c:out></p>
												</div>
											</div>
										</c:if>
										<div class="row group-item">
											<div class="col-md-12">
												<p class="text-underline">Quantifiable Deliverables</p>
													<p><c:out value="${formBean.qDeliver}"/></p>
											</div>
										</div>
										<div class="row">
											<div class="col-md-12">
												<a class="btn-lg btn-primary pull-right" href="<c:url value="/project/projectCircum"> <c:param name="projectId" value="${formBean.projectId}"/> </c:url>">
												Edit</a>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<div id="finImplTab"class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title">
										<a class="accordion-toggle collapsed" data-toggle="collapse"
											href="#financial">Financial Implication</a>
									</h4>
								</div>
								<div id="financial" class="panel-collapse collapse">
									<div class="panel-body finance-summary">
										<div class="row">
											<div class="col-md-12">
												<table class="table table-bordered">
													<thead>
														<tr>
															<th style="width: 10%" rowspan="2">Hospital Department</th>
															<th style="width: 6%" rowspan="2">Job</th>
															<th style="width: 5%" rowspan="2">Level</th>
															<th style="width: 9%" rowspan="2">Work Hours Required</th>
															<th style="width: 10%" rowspan="2">Method</th>
															<th style="width: 35%" rowspan="1" colspan="4">COA</th>
															<th style="width: 15%" rowspan="2">Financial Implication</th>
														</tr>
														<tr id="coa-row">
															<th>Inst</th>
															<th>Fund</th>
															<th>Section</th>
															<th>Analytic</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach items="${formBean.finImplList}" var="fin" varStatus="status">
														<tr>
															<td><c:out value="${fin.departName}"/></td>
															<td>
																<div>
																	<select class="form-control" value="${fin.job}" name="finImplList[${status.index}].job">
																		<c:forEach items="${fin.jobRankList}" var="jobRank">
																			<option value="${jobRank}">${jobRank}</option>
																		</c:forEach>
																	</select>
																</div>
															</td>
															<td><c:out value="${fin.level}"/></td>
															<td><c:out value="${fin.workHour}"/></td>
															<td>	
																<select class="form-control" value="${fin.job}" name="finImplList[${status.index}].method">
																	<option value="min">Minimum</option>
																	<option value="max">Maximum</option>
																</select>
															</td>
															<td><input type="text" name="finImplList[${status.index}].instCOA" value="${fin.instCOA}" class="form-control instCOA"></td>
															<td><input type="text" name="finImplList[${status.index}].fundCOA" value="${fin.fundCOA}"class="form-control fundCOA"></td>
															<td><input type="text" name="finImplList[${status.index}].sectionCOA" value="${fin.sectionCOA}"class="form-control sectCOA"></td>
															<td><input type="text" name="finImplList[${status.index}].analyticCOA" value="${fin.analyticCOA}"class="form-control analyCOA"></td>
															<td>HKD: <c:out value="${fin.finImplication}"/></td>
														</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>
										</div>
										<div class="row">
											<div class="col-sm-12">
												<div class="pull-right">
													<button name="recalculateBtn" type="button" class="btn btn-primary recalculateBtn">Recalculate</button>
													<span><strong>Total: 1,688,652.64</strong></span>
												</div>
											</div>
										</div>
										<div class="row">
											<div style="text-align: left;padding-left: 15px;float: left;">
												Choose FIN Hospital I/C
											</div>
											<div class="col-sm-4">
												<c:choose>
												<c:when test="${formBean.projectStatus == 'PENDING_FIN_VET'}">
												<div class="form-group">
													<input type='text' id="txtFinIcName" name="finIcName" class="form-control" data-bv-notempty="true" data-bv-notempty-message="You Must choose FIN Hospital I/C"/>
													<input type="hidden" id="txtFinIcId" name="finIcId" />	
												</div>
												</c:when>
												<c:otherwise>
													<input type='text' id="txtFinIcUser" name="txtFinIcUser" class="form-control" value="${formBean.finIcName}" disabled/>
												</c:otherwise>
												</c:choose>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<div id="docTab" class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title">
										<a class="accordion-toggle collapsed" data-toggle="collapse"
											href="#document">Uploaded Document</a>
									</h4>
								</div>
								<div id="document" class="panel-collapse collapse">
									<div class="panel-body">
										<div class="row">
											<div class="col-md-12">
												<table class="table table-striped table-bordered">
													<thead>
														<tr>
															<th style="width:30px"></th>
															<th>Filename</th>
															<th>Description</th>
															<th>Upload By</th>
															<th style="width:150px">Upload Date</th>
														</tr>
													</thead>
													<tbody>
													<c:forEach items="${formBean.docList}" var="doc">
														<tr>
															<td>
																<c:if test="${doc.uploadUserId == sessionScope.userProfile.userName}">
																<a href="<c:url value="/common/deleteAttachment"> <c:param name="attachmentUid" value="${doc.documentId}"/></c:url>"><span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span></a>
																</c:if>
															</td>
															<td><a href="<c:url value="/common/downloadAttachment"> <c:param name="aid" value="${doc.documentId}"/> </c:url>"><c:out value="${doc.fileName}"/></a></td>
															<td><c:out value="${doc.description}"/></td>
															<td><c:out value="${doc.uploadUser}"/></td>
															<td><fmt:formatDate value="${doc.uploadDate}" pattern="HH:mm:ss dd/MM/yyyy" /></td>
														</tr>
													</c:forEach>
													<c:if test="${empty formBean.docList}">
														<tr><td colspan="5" ><center>No uploaded document</center></td></tr>
													</c:if>
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div>
							</div>

							<div id="statusTab" class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title">
										<a class="accordion-toggle collapsed" data-toggle="collapse"
											href="#status">Status</a>
									</h4>
								</div>
								<div id="status" class="panel-collapse collapse">
									<div class="panel-body">
										<div class="row">
											<div class="col-md-12">
												<div class="btn-group btn-group-horizontal" data-toggle="buttons">
													<label class="btn disabled" id="hrVetted"> <input type="checkbox" name='hro'> <span class="fa-stack fa-1x uncheck"> <i
															class="fa fa-circle-o fa-stack-1x icon-black"></i>
													</span> <span class="fa-stack fa-1x check"> <i class="fa fa-circle fa-stack-1x icon-green"></i> <i
															class="fa fa-circle-o fa-stack-1x icon-black"></i>
													</span> <span> HR Officer Vetted</span>
													</label> <label id= "finVetted" class="btn"> <input type="checkbox" name='fin'> <span class="fa-stack fa-1x uncheck"> <i
															class="fa fa-circle-o fa-stack-1x icon-black"></i>
													</span> <span class="fa-stack fa-1x check"> <i class="fa fa-circle fa-stack-1x icon-green"></i> <i
															class="fa fa-circle-o fa-stack-1x icon-black"></i>
													</span> <span> FIN Officer Vetted</span>
													</label> <label id="icConfirmd" class="btn" disabled> <input type="checkbox" name='fin'> <span class="fa-stack fa-1x uncheck"> <i
															class="fa fa-circle-o fa-stack-1x icon-black"></i>
													</span> <span class="fa-stack fa-1x check"> <i class="fa fa-circle fa-stack-1x icon-green"></i> <i
															class="fa fa-circle-o fa-stack-1x icon-black"></i>
													</span> <span> FIN Hospital I/C Fund Confirmed</span>
													</label>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>	
																				
							<div id="apprHistTab" class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title">
										<a class="accordion-toggle collapsed" data-toggle="collapse"
											href="#approvalHistory">Approval History</a>
									</h4>
								</div>
								<div id="approvalHistory" class="panel-collapse collapse">
									<div class="panel-body">
										<table class="table table-striped table-bordered">
											<thead>
												<tr>
													<th>User Name</th>
													<th>Email Address</th>
													<th>Role</th>
													<th>Action</th>
													<th>Date Time</th>
													<th>Remark</th>
												</tr>
											</thead>
											<tbody>
											<c:forEach items="${formBean.apprHistList}" var="apprHist">
												<tr>
													<td><c:out value="${apprHist.name}"></c:out></td>
													<td><a href="#"><c:out value="${apprHist.email}"/></a></td>
													<td><c:out value="${apprHist.roleName}"/></td>
													<td><c:out value="${apprHist.actionName}"/></td>
													<td><c:out value="${apprHist.actionDate}"/></td>
													<td><c:out value="${apprHist.remark}"/></td>
												</tr>
											</c:forEach>
											<c:if test="${empty formBean.apprHistList}">
												<tr><td colspan="6" ><center>No approval History record</center></td></tr>
											</c:if>
											</tbody>
										</table>
									</div>
								</div>
							</div>
							
						</div>
						
						<c:if test="${ formBean.actionButton != ''}">
						<div class="row">
							<div class="col-md-12">
								<c:if test="${formBean.projectStatus == 'OPEN' && formBean.projectOwner == sessionScope.userProfile.userName}">
									<div class="checkbox form-group">
										<label for="ownerEndorse"> <input class="custom-control-input" type="checkbox" name="ownerEndorse" data-bv-notempty="true" data-bv-notempty-message="You must endorse the application."> I hereby endorse the application of the above SHS Project and will comply with the policy
										requirements summarized under the points to note in this Project application form.
										</label>
									</div>
								</c:if>
							</div>
						</div>
						<div class="form-group" style="margin-bottom: 15px;">
									<p>Remark</p>
									<textarea class="form-control" name="remark" style="height: 100px;"></textarea>
						</div>
						<div class="form-group" style="margin-bottom: 15px;">
									<p>Upload supporting document, if any</p>
						</div>
						<div id="divUploadSection" class="col-md-8">
							<div class="row">
								<div id="divUploadBefore" >
									<div class="col-sm-6">
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
						</c:if>
						
					</div>
					<!-- end container -->
					<form:hidden id="projectVerId" path="formBean.projectVerId" />
					<form:hidden id="projectStep" path="formBean.projectStep" />
					<form:hidden id="projectStatus" path="formBean.projectStatus"/>
					<form:hidden id="hiddenFormAction" path="formBean.formAction" />
					<form:hidden id="toTemplateId" path="formBean.toTemplateId" />
					<form:hidden id="returnTemplateId" path="formBean.returnTemplateId" />
					<form:hidden id="toRole" path="formBean.toRole" />
					<form:hidden id="returnRole" path="formBean.returnRole" />
					
					<input type="text" id="requestType" class="hide"/>
					<input type="text" id="hiddenRequestNo" value="${formBean.projectId}"class="hide"/>
					
						
					<div class="row">
						<div class="col-md-6"></div>
						<div class="col-md-6" style="text-align:right">
							<c:if test="${ formBean.actionButton != ''}">
								<button name="submitBtn" type="button" class="btn-lg btn-primary" onclick="performSubmit();">${ formBean.actionButton }</button>
							</c:if>
							<c:if test="${ formBean.returnButton != ''}">
								<button name="rejectBtn" type="button" class="btn-lg btn-primary" onclick="performReturn();">Return for Correction</button>
							</c:if>
							<c:if test="${ formBean.projectStatus == 'SAVE' && formBean.actionButton != ''}">
								<button name="saveBtn" type="submit" class="btn-lg btn-primary" onclick="performUpdate()">Save</button>
							</c:if>
						</div>
					</div>
				</div>
			</div>
			
<!-- Model for More Job Descriptions - Start -->			
<div class="modal fade" id="jobModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLabel">More Job Descriptions (To be included in SHS job adv.
					<span id="jobRank"></span>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				          <span aria-hidden="true">&times;</span>
				      </button>
				</h5>
				 
			</div>
			<div class="modal-body">
				<div class="hide" id="rowIndex"></div>
					<div class="form-group">
						<label for="jobDesc">Job Descriptions / Competency Requirements<font class="star">*</font></label> 
						<textarea id="jobDesc" class='form-control' row="8" style="height: 100px;"></textarea>
					</div>
				
					<div class="form-group">
						<label for="otherInfo"> Other Information(e.g. requirements, preferred attributes, brief job descriptions, if any)</label>
						<textarea id="otherInfo" class='form-control' rows="4"></textarea>
					</div>
				
					<div class="form-group">
						<label for="targetApp"> Target Applicant:</label>
						<textarea id="targetApp" class='form-control' rows="4"></textarea>
					</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>

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
<%-- 						<form:input path="formBean.emailTo" class="form-control"/> --%>
							<input type="text" id="emailTo" name="emailTo" class="form-control" value="${formBean.emailTo}"/>
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
				<div class="row">
						<div class="col-sm-2">
							<label for="" class="field_request_label">Attachment</label>
						</div>
						<div id="divUploadSection" class="col-md-8">
							<div class="row">
								<div id="divUploadBefore" >
									<div class="col-sm-6">
										<input type="file" id="approvalFile" name="approvalFile" style="width:100%" />
									</div>
<!-- 									<div class="col-sm-4" style="text-align: left"> -->
<!-- 										<input type="text" id="fileDesc" style="width:100%" placeholder="Description"/> -->
<!-- 									</div> -->
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
			<div class="modal-footer">
				<button type="submit" class="btn btn-primary" style="width:110px;" onclick="submitSendEmail()">
					<i class="fa fa-send-o"></i> Send</button>
				<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal" >
					<i class="fa fa-times"></i> Cancel</button>
			</div>
		</div>

	</div>
</div>
<!-- Model for edit email - End -->
		</form>
	</div>
</div>



<div id="popover_content_wrapper" style="display: none"></div>
<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>