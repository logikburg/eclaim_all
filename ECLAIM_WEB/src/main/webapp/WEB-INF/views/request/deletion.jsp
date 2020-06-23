<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	function payrollIsRunning() {
		if ($("button[name='btnConfirm']").length > 0) {
			$("#warningTitle").html("Warning");
	        $("#warningContent").html("HCM Payroll is running, please wait a moment and re-try.");
	        $("#warningModal").modal("show");
	        
	        $("button[name='btnConfirm']").attr("disabled", true);
        }
	}

	function createNewRequest() {
		document.location.href = "<%= request.getContextPath() %>/request/deletion";
	}

	function validateForm() {
		var errMsg = "";
	
		if ($("#tblPositionDelete tbody tr td").html() == "No record found." || $("#tblPositionDelete tbody tr").size() == 0) {
			errMsg += "Please select at least one post.<br/>";
			
			return displayError(errMsg);
		}
		
		return true;
	}
	
	function displayError(errMsg) {
		if (errMsg == "") {
			return true;
		}
		else {
			$("#divErrorMsg").html(errMsg);
			$($("div[class='alert-box-icon-danger']")[0]).css("height", $("#divError").height())
			$("#divError").show();
			
			$('html,body').animate({ scrollTop: 0 }, 'slow');
			
			return false;
		}
	}

	function startSearching() {
		$("#divSuccess").hide();
		$("#divError").hide();
	
		if ($("#effectiveDate").val() == "") {
			displayError("Please select effective date.");
			return;
		}
	
		// Hide the result section
		$("#tblSearchResult").hide();
		var oTable = $('#tblSearchResult').dataTable();
		oTable.fnDestroy();
				
		$("#searchResultModel").modal("show");
	}
	
	function selectPost(clusterCode, instCode, postUid, postId, rankDesc, annualPlanDesc, postDurationDesc, postFte, deptCode, staffGroupCode) {
		// Check is Post ID exist
		for (var i=0; i<$("#tblPositionDelete tbody tr").length; i++) {
			if ($($($("#tblPositionDelete tbody tr")[i]).find("td")[0]).text().trim() == postId) {
			
				$("#warningTitle").html("Error");
            	$("#warningContent").html("Post ID already exist.");
            	$("#warningModal").modal("show");
			
				return;
			}
		}
	
		$.ajax({
            url: "<%=request.getContextPath() %>/api/searchExistingRequest",
            type: "POST",
            data: {searchPostNo: postUid},
            dataType: 'json',
            cache: false,
            success: function(data) {
            	var haveInProgress = false;
            	
            	if (data.jsonResultResponse != null && data.jsonResultResponse.length > 0) {
            		var responseStr = "<table class='table-bordered mprs_table'><thead><th>Request Date</th><th>Request Type</th><th>Request ID</th><th>Status</th></thead><tbody>";
            		for (i=0; i<data.jsonResultResponse.length; i++) {
            			var d = new Date(data.jsonResultResponse[i].requestDate);
            			
	            		responseStr += "<tr>";
	            		responseStr += "<td>" + d.getDate() + "/" + (d.getMonth()+1) + '/' + d.getFullYear() + "</td>";
						responseStr += "<td>" + data.jsonResultResponse[i].requestType + "</td>";
						responseStr += "<td>" + data.jsonResultResponse[i].requestId + "</td>";
						responseStr += "<td>" + data.jsonResultResponse[i].statusDesc + "</td>";
						responseStr += "</tr>";
						
						if (data.jsonResultResponse[i].statusDesc != "Confirmed") {
							haveInProgress = true;
						}
            		}
            		
					$("#warningTitle").html("This post cannot be selected because it is being processed in other outstanding request.");
            		$("#warningContent").html(responseStr);
            		$("#warningModal").modal("show");
            	}
            	
            	if (!haveInProgress) {
	            	// Remove the selected row
					if ($("#tblPositionDelete tbody tr td").html() == "No record found.") {
			        	$("#tblPositionDelete tbody").remove();
			        }
				
					var row = "<tbody><tr>";
					row += "<td><input type='hidden' name='requestPostNo' value='" + postUid + "'/>" + postId + "</td>";
					row += "<td>" + rankDesc + "</td>";
					row += "<td>" + annualPlanDesc + "</td>";
					row += "<td>" + postDurationDesc + "</td>";
					row += "<td>" + postFte + "</td>";
					row += "<td style='text-align:center'><button type='button' class='btn btn-primary' onclick='showPostDetails(\"" + postUid + "\")'>View</button><button type='button' name='btnRemove' class='btn btn-primary' onclick='$(this).parent().parent().remove();enableEffectiveDate();'><i class='fa fa-times'></i> Remove</button></td>";
					row += "</tr></tbody>";
					
					if ($("#tblPositionDelete tbody").length == 0) {
						$("#tblPositionDelete thead").after(row);
					}
					else {
						$("#tblPositionDelete tbody:last").after(row);
					}
					
					// Close the search Dialog
					$("#searchResultModel").modal("hide");
					
					enableEffectiveDate();
					
					// Enable the submit button
					$("#btnSave").attr("disabled", false);
				}
			}
		});
	}

	$(function(){
		$("#requestStatus").prop("disabled", true);
		
		var status = $("#requestStatus").val();
		
		if ($("#tblPositionDelete tbody tr").length == 0) {
			$("#tblPositionDelete thead").after("<tbody><tr><td colspan='6'>No record found.</td></tr></tbody>");
		}
		
		$("#frmDetail").bootstrapValidator({
			message: ' This value is not valid',
			live: "submitted",
			fields: {
			},
		})
		.on('error.field.bv', function(e, data) {
			hideLoading();
		})
		.on('success.form.bv', function(e){
			var currentTarget = e.target;
			e.preventDefault();
			
			if (!validateForm()) {
				hideLoading();
			}
			else {
				showLoading();
				var postUid = [];
				for (var i=0; i<$("input[name='requestPostNo']").length; i++) {
					postUid.push($($("input[name='requestPostNo']")[i]).val());
				}
			
				$.ajax({
		            url: "<%=request.getContextPath() %>/request/validateDeletion",
		            type: "POST",
		            data: {postUid: postUid, effectiveDate: $("#effectiveDate").val() },
		            success: function(data) {
		            	if (data != "") {
		            		hideLoading();
		            		displayError(data);
		            	}
		            	else {
		            		$("#frmDetail input[type='text']").attr("disabled", false);
							$("#frmDetail textarea").attr("disabled", false);
							$("#frmDetail select").attr("disabled", false);
							$("#frmDetail input[type='hidden']").attr("disabled", false);
							currentTarget.submit();
		            	}
		            }
		        });
			}
		});
		
		<c:choose>
			<c:when test="${formBean.updateSuccess == 'Y'}">
				$("#divSuccessMsg").html('<c:out value="${formBean.displayMessage}"/>');
				$("#divSuccess").show();
			</c:when>
			<c:when test="${formBean.updateSuccess == 'N'}">
				$("#divErrorMsg").html('<c:out value="${formBean.displayMessage}"/>');
				$($("div[class='alert-box-icon-danger']")[0]).css("height", $("#divError").height())
				$("#divError").show();
			</c:when>
		</c:choose>
		
		<c:choose>
			<c:when test="${formBean.userHaveSaveRight != 'Y'}">
				$("select").prop("disabled", true);
				$("#model_nextWFStep select").prop("disabled", false);
				$("input[type='text']").prop("disabled", true);
				$("#model_editEmail input[type='text']").prop("disabled", false);
				$("#btnAddPost").prop("disabled", true);
				$("button[name='btnRemove']").prop("disabled", true);
				$("input[type='file']").prop("disabled", true);
				$("button[name='btnUploadFile']").attr("disabled", true);
				$("button[name='btnRemoveFile']").attr("disabled", true);
			</c:when>
		</c:choose>
		
		// Set Request ID to readonly instead of disable
		$("#requestId").prop("disabled", false);
		$("#requestId").attr("readonly", true); 
		
		enableEffectiveDate();
	});
	
	function enableEffectiveDate() {
		if ($("#tblPositionDelete tbody tr").size() > 0 && $("#tblPositionDelete tbody tr td").html() != "No record found.") {
			$("#effectiveDate").attr("disabled", true);
		}
		else {
			$("#effectiveDate").attr("disabled", false);
		}
	}
	
</script>

<!-- Page Content -->
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > Request > Deletion 
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
			<div class="title pull-left"><i class="fa fa-file-text-o"></i>Deletion of Post</div>
		</div>
		<form id="frmDetail" method="POST" enctype="multipart/form-data">
			<form:hidden path="formBean.formAction" id="hiddenFormAction"/>
			<form:hidden path="formBean.requestNo" id="hiddenRequestNo"/>
			<form:hidden path="formBean.lastUpdateDate" id="hiddenLastUpdateDate" />
			<div class="panel panel-custom-primary">
				<div class="panel-heading">
					<div class="panel-heading-title">General Information</div>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="request_id" class="field_request_label">Request ID</label>
							</div>
							<div class="col-sm-4">
							  	<input type="text" id="requestId" name="requestId" value="${formBean.requestId}" class="form-control" readonly/>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-3">
							  	<label for="request_status" class="control-label">Request Status</label>
							</div>
							<div class="col-sm-3">
								<form:select path="formBean.requestStatus" name="requestStatus" class="form-control">
									<option value=""></option>
									<form:options items="${requestStatusList}" />
								</form:select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
							  	<label for="request_id" class="control-label">Requester<font class="star">*</font></label>
							</div>
							<div class="col-sm-4">
							  	<form:select path="formBean.requester" name="requester" class="form-control" required="required">
									<option value="">- Select -</option>
									<form:options items="${userList}" />
								</form:select>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-3">
							  	<label for="effectiveDate" class="control-label">Effective Date of Change<font class="star">*</font></label>
							</div>
							<div class="col-sm-3">
							  	<div class="input-group date">
							  		<form:input path="formBean.effectiveDate" class="form-control" required="required"/>
									<span class="input-group-addon">
										<span class="glyphicon glyphicon-calendar"></span>
									</span>	
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="panel panel-custom-primary">
				<div class="panel-heading">
					<div class="panel-heading-title">Post to be deleted</div>
				</div>
				<div class="panel-body" style="padding-top:5px">
					<table id="tblPositionDelete" class="table-bordered mprs_table">
						<thead>
							<tr>
								<th>Post ID</th>
								<th>Rank</th>
								<th>Annual Plan</th>
								<th>Duration</th>
								<th>FTE</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="listValue" items="${formBean.requestPositionList}">
								<tr>
									<td><input type="hidden" name="requestPostNo" value="${listValue.postNo}">${listValue.postId}</td>
									<td>${listValue.rank.rankName}</td>
									<td>${listValue.annualPlanInd}</td>
									<td>${listValue.postDuration == "R"?"Recurrent":listValue.postDuration == "TLC"?"Time Limited - Contract":listValue.postDuration == "TLT"?"Time Limited - Temporary":""}</td>
									<td>${listValue.postFTE}</td>
									<td style="text-align:center">
										<button type='button' name='btnViewPost' class='btn btn-primary' onclick='showPostDetails("${listValue.postNo}")'>View</button>										
										<button type="button" name="btnRemove" class="btn btn-primary" onclick="$(this).parent().parent().remove();enableEffectiveDate();"><i class="fa fa-times"></i> Remove</button>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div style="text-align:right">
						<button type="button" id="btnAddPost" class="btn btn-primary" style="width:110px" onclick="startSearching()"><i class="fa fa-plus"></i> Add Post</button>
					</div>
				
					<br>
					<div class="row">
						<div class="col-sm-2">
							<label for="txt_reason" class="field_request_label"><strong>Reason of Deletion</strong></label>
						</div>
						<div class="col-sm-10">
							<form:input path="formBean.requestReason" class="form-control" maxlength="200"/>
						</div>
					</div>
				</div>
			</div>
			<br>
		
			<%@ include file="/WEB-INF/views/request/common_approval.jsp"%>
			
		</form>
		
		<%@ include file="/WEB-INF/views/request/common_VacantMPRPost_search.jsp"%>
		<%@ include file="/WEB-INF/views/request/common_MPRPost_detail.jsp"%>
	</div>
	<!-- ./#container -->
</div>
<!-- ./#page-content-wrapper -->

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>
