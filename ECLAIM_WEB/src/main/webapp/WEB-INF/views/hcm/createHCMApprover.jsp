<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	var currentTarget = null;

	function parseDate(str) {
		var mdy = str.split("/");
		return new Date(mdy[2], mdy[1]-1, mdy[0]);
	}
	
	function daysBetween(date1, date2) {
		return Math.round((parseDate(date1)-parseDate(date2))/ (1000*60*60*24));
	}
	
	function performSave() {
		// Hide the success/error message
		$("#divError").hide();
		$("#divSuccess").hide();
	
		var validator = $("#frmDetail").data('bootstrapValidator');
		validator.validate();
		if (!validator.isValid() || !performValidation()) {
			return;
		}
	
		showLoading();
	
		// Check the responsibility
		$.ajax({
            url: "<%=request.getContextPath() %>/hcm/searchResponsibility",
            type: "POST",
            data: {userId: '<c:out value="${formBean.userId}"/>'  },
            success: function(data) {
            	hideLoading();
            	if (data.length == 0) {
            		$("#divErrorMsg").html("You do not have responsibility to create HCM record.");
					$("#divError").show();
			
					$('html,body').animate({ scrollTop: 0 }, 'slow');
            	} 
            	else if (data.length == 1) {
            		$("#repsonsibilityId").val(data[0].responsibilityName);
            		currentTarget.submit();
            		showLoading();
            	}
            	else {
            		$("#tblResponsibility tbody").empty()
            		var html = "";
            		for (var i=0; i<data.length; i++) {
            			html += "<tr>";
            			html += "<td><div class='row'><button type='button' style='width:200px' class='btn btn-primary' onclick='confirmSave(\"" + data[i].responsibilityName + "\")'>" + data[i].responsibilityName + "</button></div>";
            			html += "</td>";
            			html += "</tr>";
            		}
            		$("#tblResponsibility tbody").append(html);
            		$("#responsibilityModal").modal("show");
            	}
			}
		});
	}

	function returnToHome() {
		document.location.href = "<%= request.getContextPath() %>/home/home";
	}

	function performValidation() {
		var errMsg = "";
		
		// Perform checking on valid grade date field
		for (var i=0; i<$("input[name='gradeDateFrom']").length; i++) {
			if ($($("input[name='ddGrade']")[i]).val() != "") {
				if ($($("input[name='gradeDateFrom']")[i]).val() == "") {
					errMsg = "From date for Valid Grade cannot be empty." 
					break;
				}
			
				if (daysBetween($($("input[name='gradeDateFrom']")[i]).val(), $("#txtEffectiveFromDate").val()) < 0) {
					errMsg = "From/To date for Valid Grade should on or after position start date." 
					break;
				}	
				
				if ($($("input[name='gradeDateTo']")[i]).val() != "") {
					if (daysBetween($($("input[name='gradeDateTo']")[i]).val(), $("#txtEffectiveFromDate").val()) < 0) {
						errMsg = "From/To date for Valid Grade should on or after position start date." 
						break;
					}
				}
			}
		}
		
		if (errMsg == "") {
			return true;
		}
		else {
			hideLoading();
		
			$("#divErrorMsg").html(errMsg);
			$($("div[class='alert-box-icon-danger']")[0]).css("height", $("#divError").height())
			$("#divError").show();
			
			$('html,body').animate({ scrollTop: 0 }, 'slow');
			
			return false;
		}
	}
	
	function hiddenMessage() {
		$(".alert").hide();
		$("#divHCMDetail").hide();
	}
	
	
	function performSearch() {
		hiddenMessage();
		
		showLoading();
	
		// Ajax call to perform search
		$.ajax({
            url: "<%=request.getContextPath() %>/api/post/searchPost",
            cache: false,
            type: "POST",
            data: $("#frmSearch").serialize(),
            success: function(data) {
            	hideLoading();
            	// Clear the result table
            	$("#tblSearchResult tbody").remove();
            	var html = "";
            	html = "<tbody>";
            	if (data.postResponse != null) {
            		var len = data.postResponse.length;
            		if (len > 0) {
	            		for (var i = 0; i < len; i++) {
							html += "<tr>";
							html += "<td>" + data.postResponse[i].postId + "</td>";
							html += "<td>" + data.postResponse[i].rankDesc + "</td>";
							html += "<td>" + data.postResponse[i].annualPlanDesc + "</td>";
							html += "<td>" + data.postResponse[i].postDurationDesc + "</td>";
							html += "<td>" + data.postResponse[i].postFte + "</td>";
							html += "<td><button type='button' class='btn btn-primary' ";
							html += "onclick=\"selectPostDetails('" + data.postResponse[i].hcmPositionId + "', '" + data.postResponse[i].postFte + "', '" + data.postResponse[i].postUid + "', '" + data.postResponse[i].postId + "')\">Select</button></td>";
							html += "</tr>";
	            		} 
	            	}
            	}
            	
            	html += "</tbody>";
            	// Append row to search result
				$("#tblSearchResult thead").after(html);
				
				var oTable = $('#tblSearchResult').dataTable();
				oTable.fnDestroy();
				$('#tblSearchResult').dataTable({
					"language": {
						"emptyTable":"No record found."
					}
				});
				
				$("#tblSearchResult").show();
				
				$("#searchResultModel").modal("show");
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	}
	
	function confirmSave(responsibilityId) {
		showLoading();
		$("#repsonsibilityId").val(responsibilityId);
		showLoading();
		currentTarget.submit();
	}
	
	function showUpdateFTE() {
		$('#updateFTEModel').modal('show');
	}
	
	function performUpdateFTE() {
		$("#txtFTE").val($("#updatedFTE").val());
		$('#updateFTEModel').modal('hide');
	}
	
	function selectPostDetails(postId, fte, mprsPostId, displayPostId) {
		// Hide the search result model and collapse the search panel
		$('#searchCriteria').collapse('hide');
		$('#searchResultModel').modal('hide');
		showLoading();
		
		// Ajax call to perform search
		$.ajax({
            url: "<%=request.getContextPath() %>/hcm/getHCMPostDetail",
            type: "POST",
            data: {postId:postId, mprsPostId:displayPostId},
            success: function(data) {
            	$("#hiddenVersionNo").val(data.hcmRecord.versionNumber);
            	$("#hiddenPositionId").val(data.hcmRecord.positionId);
            	
            	$("#txtEffectiveFromDate").val(data.hcmRecord.effectiveStartDateStr);
            	$("#txtStartDate").val(data.hcmRecord.positionStartDateStr);
            	$("#txtProposedEndDate").val(data.hcmRecord.hiringStatusPropEndDateStr);
            	
            	$("#lblPostName").html(data.hcmRecord.name);
            	$("#hiddenPostName").val(data.hcmRecord.name);
            	$("#postTitle").val(data.hcmRecord.positionTitle);
            	$("#postOrganization").val(data.hcmRecord.postOrganizationId);
            	
            	// Default set unit/team to ""
            	$("#unitTeam").val(""); 
            	
            	$("#organization").val(data.hcmRecord.organizationId);
            	$("#job").val(data.hcmRecord.jobId);
            	
            	$("#ddPostTitle").val(data.hcmRecord.positionTitle);
            	$("#ddPostOrganization").val(data.hcmRecord.positionOrganization);
            	$("#ddOrganization").val(data.hcmRecord.organizationName);
            	$("#ddJob").val(data.hcmRecord.jobName);
            	$("#hiringStatus").val(data.hcmRecord.availabilityStatus);

				// Default Selected "Single Incumbent"            	
           		$("#type1").prop("checked", false)
           		$("#type2").prop("checked", true)
           		
            	$("#txtEndDate").val(data.hcmRecord.effectiveEndDateStr);
            	
            	$("#txtFTE").val(0);
            	$("#txtHeadCount").val(1);
            	$("#txtHeadCount").hide();
            	$("#txtProbationDuration").val(data.hcmRecord.porbationPeriod);
            	$("#txtProbationDurationUnit").val(data.hcmRecord.probationPeriodUnitCd);
            	
            	// Clear the valid grade table first
            	var validGradeCnt = $("#tblValidGrade tbody tr").length;
            	for (var m=0; m<validGradeCnt; m++) {
            		$($("#tblValidGrade tbody tr")[validGradeCnt - m]).remove();
            	}
            	
            	if (data.hcmGradeListSize > 0) {
	            	for (var i=0; i<data.hcmGradeListSize; i++) {
	            		if (i!=0) {
	            			var x = $("#tblValidGrade tbody tr:last").clone();
							$("#tblValidGrade tbody tr:last").after(x);
	            		} 	
	            		$($("input[name='grade']")[i]).val(data.hcmGradeList[i].gradeId);
	            		$($("input[name='ddGrade']")[i]).val(data.hcmGradeList[i].gradeDesc);
	            		$($("input[name='gradeDateFrom']")[i]).val(data.hcmGradeList[i].dateFromStr);
	            		$($("input[name='gradeDateTo']")[i]).val(data.hcmGradeList[i].dateToStr);
	            	}
	            }
	            
	            $("#location").val(data.hcmRecord.locationId);
	            $("#positionGroup").val(data.hcmRecord.positionGroup);
	            $("#srcFunding").val(data.hcmRecord.srcFunding);
	            $("#ddLocation").val(data.hcmRecord.locationDesc);
	            
	            $("#selectedPostId").val(postId);
	            $("#selectedMPRSPostId").val(mprsPostId);
	            $("#selectedFTE").val(fte);
	            
	            $("#effectiveFromDisplay").val(data.hcmRecord.effectiveStartDateStr);
	            $("#effectiveToDisplay").val(data.hcmRecord.effectiveEndDateStr);
	            $("#txtHiringStatusStartDate").val(data.hcmRecord.hiringStatusStartDateStr);
            
           		$("#effectiveFromDisplay").prop("readonly", true);
				$("#effectiveToDisplay").prop("readonly", true);
				$("#txtHiringStatusStartDate").prop("readonly", true);
            
				// Refresh the post name
				updatePostName();
				
				var employeeId = data.employeeId;
				var employeeName = data.employeeName;
				
				// Update Employee Info
				$("#employeeInfo tbody tr").remove();
				$("#employeeInfo tbody").append('<tr><td>' + displayPostId + '</td><td>' + employeeId + '</td><td>' + employeeName + '</td></tr>');
				
            	hideLoading();
            
		        $("#divHCMDetail").show();
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	}
	
	function performUpdate() {
		$("#hiddenFormAction").val("UPDATE");
	}
	
	function performCorrection() {
		$("#hiddenFormAction").val("CORRECTION");
	}
	
	function addGradeRow() {
		var x = $("#tblValidGrade tr:last").clone();
		$("#tblValidGrade tr:last").after(x);
		
		var newRowIdx = $("#tblValidGrade tr").length-2;
		$($("input[name='ddGrade']")[newRowIdx]).val("");
		$($("input[name='grade']")[newRowIdx]).val("");
		$($("input[name='gradeDateFrom']")[newRowIdx]).val("");
		$($("input[name='gradeDateTo']")[newRowIdx]).val("");
		
		$($("input[name='ddGrade']")[newRowIdx]).autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMGradeList", request, function(result){
					response($.map(result, function(item){
						return {label: item.gradeName, value: item.gradeId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$(this).parent().find("input[name='grade']").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$(this).parent().find("input[name='grade']").val(ui.item.value);
				return false;
			}
		});
		
		$(".input-group.date").datepicker({
	  		format: "dd/mm/yyyy",
	  		autoclose: true,
	  		daysOfWeekHighlighted: [0],
	  		todayHighlight: true,
	  		todayBtn: "linked"
	  	})
	  	.on('changeDate', function(e) {
	  		if ($($(this).parent().find("input")[0]).attr("required") === undefined) {
	  		}
	  		else {
	  			if ($(this).next().css("display") != "none")
	  				$("#frmDetail").bootstrapValidator('revalidateField', $($(this).parent().find("input")[0]).attr("name"));
	  		}
	  	});
	}

	function updatePostName() {
		var str = "";
		str += $("#postTitle").val();
		if ($("#ddPostOrganization").val() != "")	{
			str += "." + $("#ddPostOrganization").val() + ".";
			if ($("#unitTeam").val() != "") {
				str += $("#unitTeam").val();
			}
		}
	
		$("#lblPostName").html(str);
		$("#hiddenPostName").val(str);
	}	
	
	function changeDefaultLocation() {
		if ($("#postOrganization").val() != "") {
			showLoading();
			
			$.ajax({
	            url: "<%=request.getContextPath() %>/hcm/getDefaultLocation",
	            type: "POST",
	            data: {organizationId: $("#postOrganization").val()},
	            success: function(data) {
	            	hideLoading();
	            	
	            	if (data != null) {
	            		$("#ddLocation").val(data.locationCode + " [" + data.description + "]");
	            		$("#location").val(data.locationId);
	            	}
				}
			});
		}
	}
	
	$(function(){
		$.ajaxSetup({cache: false});
	
		$("#txtStartDate").prop("readonly", true);
		$("#txtProposedEndDate").prop("readonly", true);
		$("#txtFTE").prop("readonly", true);
		
		$('#tblSearchResult').DataTable();
		 
		$("#frmDetail").bootstrapValidator({
			message: 'This field cannot be empty.',
			live: "submitted",
			fields: {
			},
		})
		.on('error.form.bv', function(e){
			hideLoading();
		})
		.on('success.form.bv', function(e){
			currentTarget = e.target;
			if (!performValidation()) {
				e.preventDefault();
			}
		});
		
		$("#ddPostTitle").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMPostTitleList", request, function(result){
					response($.map(result, function(item){
						return {label: item.titleId, value: item.titleId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#postTitle").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#postTitle").val(ui.item.value);
				return false;
			}
		});
		
		var loadFlag = false;
		$("#ddPostOrganization").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMPostOrganizationList", request, function(result){
					response($.map(result, function(item){
						return {label: item.organizatonName, value: item.organizatonId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#postOrganization").val(ui.item.value);
				
				$("#ddOrganization").val(ui.item.label);
				$("#organization").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#postOrganization").val(ui.item.value);
				
				$("#ddOrganization").val(ui.item.label);
				$("#organization").val(ui.item.value);
				
				changeDefaultLocation();
				loadFlag = true;
				return false;
			},
			change: function(event, ui) {
				if (!loadFlag) {
					changeDefaultLocation();
				}
				else {
					loadFlag = false;
				}
				
				return false;
			}
		});
		
		$("#ddJob").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMJobList", request, function(result){
					response($.map(result, function(item){
						return {label: item.jobName, value: item.jobId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#job").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#job").val(ui.item.value);
				return false;
			}
		});
		
		$("#ddLocation").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMLocationList", request, function(result){
					response($.map(result, function(item){
						return {label: item.locationCode + " [" + item.description + "]", value: item.locationId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#location").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#location").val(ui.item.value);
				return false;
			}
		});
		
		$("input[name='ddGrade']").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMGradeList", request, function(result){
					response($.map(result, function(item){
						return {label: item.gradeName, value: item.gradeId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$(this).parent().find("input[name='grade']").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$(this).parent().find("input[name='grade']").val(ui.item.value);
				return false;
			}
		});

		if ($("#hiddenPositionId").val() != "") {
			$('#searchCriteria').collapse('hide');
			$("#divHCMDetail").show();
		}
		
		$("#ddPostTitle").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#postTitle").val("");
			}
		});
		
		$("#ddPostTitle").on("focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#postTitle").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#postTitle").val("");
				return;
			}
		});
		
		$("#ddJob").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#job").val("");
			}
		});
		
		$("#ddJob").on("focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#job").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#job").val("");
				return;
			}
		});
		
		$("#ddPostOrganization").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#postOrganization").val("");
				
				$("#ddOrganization").val("");
				$("#organization").val("");
				
				$("#ddLocation").val("");
				$("#location").val("");
			}
		});
		
		$("#ddPostOrganization").on("focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#postOrganization").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#postOrganization").val("");
				return;
			}
		});
		
		$("#ddLocation").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#location").val("");
			}
		});
		
		$("#ddLocation").on("focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#location").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#location").val("");
				return;
			}
		});
		
		// Update the name field for valid grade
		for (var i=1; i<$("#tblValidGrade tr").length; i++) {
			$("#tblValidGrade tr:eq(" + i + ") input:eq(0)").attr("name", "ddGrade");
			$("#tblValidGrade tr:eq(" + i + ") input:eq(1)").attr("name", "grade");
			$("#tblValidGrade tr:eq(" + i + ") input:eq(2)").attr("name", "gradeDateFrom");
			$("#tblValidGrade tr:eq(" + i + ") input:eq(3)").attr("name", "gradeDateTo");
		}
		
		<c:choose>
			<c:when test="${formBean.updateSuccess == 'Y'}">
				$("#divSuccessMsg").html('<c:out value="${formBean.displayMessage}"/>');
				$("#divSuccess").show();
				$("#divHCMDetail").show();
			</c:when>
			<c:when test="${formBean.updateSuccess == 'N'}">
				$("#divErrorMsg").html('<c:out value="${formBean.displayMessage}"/>');
				$($("div[class='alert-box-icon-danger']")[0]).css("height", $("#divError").height())
				$("#divError").show();
			</c:when>
		</c:choose>

		// Disable the ddOrganization
		$("#ddOrganization").attr("readonly", true);
	});
	
	function performDeleteNext() {
		$("#hiddenFormAction").val("DEL_NEXT");
	}
	
	function performDeletePurge() {
		$("#hiddenFormAction").val("DEL_PURGE");
	}
	
	function performDeleteAll() {
		$("#hiddenFormAction").val("DEL_ALL");
	}
	
	function removeGrade(obj) {
		if ($("#tblValidGrade tbody tr").length == 1) {
			var newRowIdx = $("#tblValidGrade tbody tr").length-1;
			$($("input[name='ddGrade']")[newRowIdx]).val("");
			$($("input[name='grade']")[newRowIdx]).val("");
			$($("input[name='gradeDateFrom']")[newRowIdx]).val("");
			$($("input[name='gradeDateTo']")[newRowIdx]).val("");
		}
		else {
			$(obj).parent().parent().remove();
		}
	}
	
</script>	
			
<!-- Page Content -->
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > HCM Position > Create HCM Approver
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
			<div class="title pull-left"><i class="fa fa-envelope-open-o"></i>Create HCM Approver</div>
		</div>
		<c:choose>
			<c:when test="${formBean.fromCreateHCMPage != 'Y'}">

				<div class="panel panel-custom-primary">
					<div class="panel-heading">
						<div class="panel-heading-title">
							<a role="button" data-target="#searchCriteria" class="panel-title" data-toggle="collapse">Searching Existing Post Record</a>
						</div>
					</div>
					<div id="searchCriteria" class="panel-collapse collapse in">
						<div class="panel-body">
							<form id="frmSearch" method="POST">
								<div class="row">
									<div class="col-sm-2">
										<label class="field_request_label">Cluster</label>
									</div>
									<div class="col-sm-4">
									  	<form:select path="formBean.searchClusterId" name="searchClusterId" class="form-control">
											<option value="">- Select -</option>
											<form:options items="${clusterList}" />
										</form:select>
									</div>
									<div class="col-sm-2">
										<label class="field_request_label">Institution</label>
									</div>
									<div class="col-sm-4">
									  	<form:select path="formBean.searchInstId" name="searchInstId" class="form-control">
											<option value="">- Select -</option>
											<form:options items="${instList}" />
										</form:select>
									</div>
								</div>
								
								<div class="row">
									<div class="col-sm-2">
										<label class="field_request_label">Department</label>
									</div>
									<div class="col-sm-4">
										<form:select path="formBean.searchDeptId" name="searchDeptId" class="form-control">
											<option value="">- Select -</option>
											<form:options items="${deptList}" />
										</form:select>
									</div>
									<div class="col-sm-2">
										<label class="field_request_label">Staff Group</label>
									</div>
									<div class="col-sm-4">
										<form:select path="formBean.searchStaffGroupId" name="searchStaffGroupId" class="form-control">
											<option value="">- Select -</option>
											<form:options items="${staffGroupList}" />
										</form:select>
									</div>
								</div>
								
								<div class="row">
									<div class="col-sm-2">
										<label class="field_request_label">Rank</label>
									</div>
									<div class="col-sm-4">
										<form:select path="formBean.searchRankId" name="searchRankId" class="form-control">
											<option value="">- Select -</option>
											<form:options items="${rankList}" />
										</form:select>
									</div>
									<div class="col-sm-2">
										<label class="field_request_label">Post ID</label>
									</div>
									<div class="col-sm-4">
										<form:input path="formBean.searchPostId" name="searchPostId" class="form-control"/>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-2">
										<label for="position_org" class="field_request_label">Employee ID</label>
									</div>
									<div class="col-sm-4">
										<form:input path="formBean.searchEmployeeId" class="form-control" />
									</div>
								</div>
								
								<div class="row" style="text-align:right">
									<div class="col-sm-12">
										<button type="button" class="btn btn-primary" style="width:130px;" onclick="performSearch()"><i class="fa fa-search"></i> Search</button>
									</div>
								</div>
							</form>
						</div>	
					</div>
					<!-- ./searchCriteria -->
				</div>
			</c:when>
		</c:choose>
		<div id="divHCMDetail" style="display:none" class="panel panel-custom-primary">
			<div class="panel-heading">
				<div class="panel-heading-title">HCM Position Detail</div>
			</div>
			<div class="panel-body">
				<form id="frmDetail" method="POST" >
					<form:hidden path="formBean.formAction" id="hiddenFormAction"/>
					<form:hidden path="formBean.positionId" id="hiddenPositionId"/>
					<form:hidden path="formBean.repsonsibilityId"/>
					<form:hidden path="formBean.selectedPostId"/>
					<form:hidden path="formBean.selectedMPRSPostId"/>
					<form:hidden path="formBean.selectedFTE"/>
					
					<div class="form-group">
						<div class="row">
							<div class="col-sm-2">
							  <label for="txt_unit" class="field_request_label">Effective Date<font class="star">*</font></label>
							</div>
							<div class="col-sm-2">
								<div class='input-group date' id='datetimepicker1'>
									<form:input path="formBean.effectiveFromDate" 
									            id="txtEffectiveFromDate" 
									            name="txtEffectiveFromDate" 
									            class="form-control"
									            required="required" />
									<span class="input-group-addon">
										<span class="glyphicon glyphicon-calendar"></span>
									</span>	 
								</div>
							</div>
						</div>
					</div>
					<div class="delimiter"></div>
				
					<div class="row">
						<div class="col-sm-2">
							<label for="txt_post_title" class="field_request_label">Start Date</label>
						</div>
						<div class="col-sm-2">
							<form:input path="formBean.startDate" id="txtStartDate" name="txtStartDate" class="form-control"/>
						</div>
					</div>
					<fieldset class="scheduler-border" style="margin:0px">
						<div class="row">
							<div class="col-sm-2">
								<label for="txt_post_name" class="field_request_label">Position Name</label>
							</div>
							<div class="col-sm-7">
								<label for="txt_post_name" id="lblPostName" class="txt_post_name"></label>
								<form:hidden path="formBean.hiddenPostName"/>
							</div>
						</div>
							 
						<div class="form-group">
							<div class="row">
								<div class="col-sm-1"></div>
								<div class="col-sm-3">
									<label for="hcmPostTitle" class="control-label">Position Title/Generic Rank<font class="star">*</font></label>
								</div>
								<div class="col-sm-6">
									<form:input path="formBean.ddPostTitle"  class="form-control" style="width:100%;" onchange="updatePostName()" onblur="updatePostName()" required="required"/>
									<form:hidden path="formBean.postTitle" />
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-1"></div>
								<div class="col-sm-3">
									<label for="hcmPostOrganization" class="control-label">Position Organization<font class="star">*</font></label>
								</div>
								<div class="col-sm-6">
									<form:input path="formBean.ddPostOrganization"  class="form-control" style="width:100%;" onchange="updatePostName()" onblur="updatePostName()" required="required"/>
									<form:hidden path="formBean.postOrganization" />
								</div>
							</div>
						</div>
					
						<div class="form-group">
							<div class="row">
								<div class="col-sm-1"></div>
								<div class="col-sm-3">
									<label for="hcmUnitTeam" class="control-label">Unit/Team</label>
								</div>
								<div class="col-sm-6">
									<form:input path="formBean.unitTeam"  class="form-control" style="width:100%;" onchange="updatePostName()" required/>
								</div>
							</div>
						</div>
					
					</fieldset>
					
					<div class="row" style="margin:2px">
						<table id="employeeInfo" class="table table-bordered mprs_table" style="border:solid 1px #DDD; width:700px; margin-top:5px">
							<thead>
								<tr>
									<th style="width:250px">Post ID</th>
									<th style="width:150px">Employee ID</th>
									<th style="width:300px">Employee Name</th>
								</tr>
							</thead>
							<tbody>
								
							</tbody>
						</table>
					</div>
					
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="txt_post_name" class="control-label">Effective From</label>
							</div>
							
							<div class="col-sm-2">
								<form:input path="formBean.effectiveFromDisplay"  class="form-control" style="width:100%;"/>
							</div>
							<div class="col-sm-1">
							 	to
   						    </div>
							<div class="col-sm-2">
								<form:input path="formBean.effectiveToDisplay" class="form-control" style="width:100%;"/>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="txt_post_name" class="control-label">Type</label>
							</div>
							
							<div class="col-sm-4">
								<form:radiobutton path="formBean.type" value="SHARED"/><label class="radio-inline">Shared</label>
								&nbsp;&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="formBean.type" value="SINGLE"/><label class="radio-inline">Single Incumbent</label>
							</div>
						</div>
					</div>
									
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="hcmOrganization" class="control-label">Organization<font class="star">*</font></label>
							</div>
							<div class="col-sm-8">
								<form:input path="formBean.ddOrganization" class="form-control" style="width:100%;" />
								<form:hidden path="formBean.organization" />
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="hcmJob" class="control-label">Job<font class="star">*</font></label>
							</div>
							<div class="col-sm-8">
								<form:input path="formBean.ddJob" class="form-control" style="width:100%;" required="required"/>
								<form:hidden path="formBean.job" />
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="hiringStatus" class="control-label">Hiring Status<font class="star">*</font></label>
							</div>
							<div class="col-sm-2">
								<form:select path="formBean.hiringStatus" class="form-control" style="width:100%;" required="required">
									<option value="">- Select -</option>
									<form:options items="${hiringStatusList}" />
								</form:select>  
							</div>
						</div>
						
						<div class="col-sm-2">
							<label for="proposedEndDate" class="control-label">Start Date</label>
						</div>
						<div class="col-sm-2">
							<form:input path="formBean.hiringStatusStartDate" id="txtHiringStatusStartDate" name="txtHiringStatusStartDate" class="form-control"/>
						</div>
						
						<div class="form-group">
							<div class="col-sm-2">
								<label for="proposedEndDate" class="control-label">Proposed End Date</label>
							</div>
							<div class="col-sm-2">
								<form:input path="formBean.proposedEndDate" id="txtProposedEndDate" name="txtProposedEndDate" class="form-control"/>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-2">
							<label for="location" class="field_request_label">Location</label>
						</div>
						<div class="col-sm-8">
							<form:input path="formBean.ddLocation" class="form-control" style="width:100%;"/>
							<form:hidden path="formBean.location" />
						</div>
					</div>
					
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="fte" class="control-label">FTE</label>
							</div>
							<div class="col-sm-2">
								<form:input path="formBean.fte" id="txtFTE" name="txtFTE" class="form-control" />
							</div>
						</div>
						<div class="form-group" style="display:none">
							<div class="col-sm-2">
								<label for="headCount" class="control-label">Headcount<font class="star">*</font></label>
							</div>
							<div class="col-sm-2">
								<form:input path="formBean.headCount" id="txtHeadCount" name="txtHeadCount" class="form-control" required="required"/>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="probationDuration" class="control-label">Probation Duration<font class="star">*</font></label>
							</div>
							<div class="col-sm-2">
								<form:input path="formBean.probationDuration" id="txtProbationDuration" name="txtProbationDuration" class="form-control" required="required"/>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-2">
								<label for="probationDurationUnit" class="control-label">Duration Unit<font class="star">*</font></label></div>
							<div class="col-sm-2">
								<form:select path="formBean.probationDurationUnit" id="txtProbationDurationUnit" name="txtProbationDurationUnit" class="form-control" style="width:100%;" required="required">
									<option value="">- Select -</option>
									<form:options items="${durationUnitList}" />
								</form:select>  
							</div>
						</div>
						
						<div class="col-sm-2" style="text-align:right">
							<button type="button" class="btn btn-default" style="width:130px;" onclick="$('#updateAddition').modal('show')"><i class="fa fa-plus-circle"></i> Additional Detail</button>
						</div>
					</div>
					
					<fieldset class="scheduler-border">
						<table>
							<tr>
								<td>
									<table id="tblValidGrade" class="table-bordered mprs_table" style="width:700px;margin-top:3px;margin-bottom:0px">
										<thead>
											<tr>
												<th style="width:400px">Valid Grades</th>
												<th style="width:150px">Date From</th>
												<th style="width:150px">Date To</th>
												<th style="width:100px">&nbsp;</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${formBean.gradeListSize == 0}">
													<tr>
														<td>
															<input type="text" name="ddGrade" class="form-control" value=""/>
															<input type="hidden" name="grade" value=""/>
														</td>
														<td>
															<div class='input-group date'>
																<input type="text" name="gradeDateFrom" class="form-control"/>
																<span class="input-group-addon">
																	<span class="glyphicon glyphicon-calendar"></span>
																</span>
															</div>
														</td>
														<td>
															<div class='input-group date'>
																<input type="text" name="gradeDateTo" class="form-control"/>
																<span class="input-group-addon">
																	<span class="glyphicon glyphicon-calendar"></span>
																</span>
															</div>
														</td>
														<td style="text-align:center"><button type="button" name="btnRemoveValidGrade" class="btn btn-danger" onclick="removeGrade($(this))"><i class="fa fa-trash"></i> Delete</button></td>
													</tr>
												</c:when>
											</c:choose>
											
											<c:forEach var="listValue" items="${formBean.gradeList}" varStatus="pStatus">
												<tr>
													<td>
														<form:input path="formBean.gradeList[${pStatus.index}].gradeDesc" name="ddGrade" class="form-control" style="width:100%;" multiple="false"/>
														<form:hidden path="formBean.gradeList[${pStatus.index}].gradeId" name="grade" />													        
													</td>
													<td>
														<div class='input-group date'>
															<form:input path="formBean.gradeList[${pStatus.index}].dateFrom" 
																		name="gradeDateFrom" 
																		class="form-control"/>
															<span class="input-group-addon">
																<span class="glyphicon glyphicon-calendar"></span>
															</span>
														</div>
													</td>
													<td>
														<div class='input-group date'>
															<form:input path="formBean.gradeList[${pStatus.index}].dateTo" 
																	    name="gradeDateTo" 
																	    class="form-control"/>
															<span class="input-group-addon">
																<span class="glyphicon glyphicon-calendar"></span>
															</span>
														</div>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
							
								</td>
								<td style="vertical-align:bottom">&nbsp;&nbsp;
									<button type="button" id="btnAddGradeRow" class="btn btn-primary" style="width:130px" onclick="addGradeRow()"><i class="fa fa-plus"></i> Add Valid Grade</button>
								</td>
							</tr>
						</table>
					</fieldset> 
	
					<div class="row" style="text-align:right">
						<div class="col-sm-12">
							<button type="button" class="btn btn-primary" name="btnSave" style="width:110px" onclick="performSave()" ><i class="fa fa-floppy-o"></i> Save</button>
							<button type="submit" id="btnSubmitSave" style="display:none"></button>
						</div>
					</div>
					
					<!-- Model for input additional details -->
					<div id="updateAddition" class="modal fade" role="dialog">
						<div class="modal-dialog modal-dialog-custom" style="width:750px">
					    	<div class="modal-content">
					    		<div class="modal-header">
									<h4><b>Additional Details</b>
							    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
							    	</h4>
								</div>
					      		<div class="modal-body">
						      		<div class="row">
										<div class="col-sm-3">
											<label for="positionGroup" class="control-label">Position Group</label>
										</div>
										<div class="col-sm-7">
											<form:input path="formBean.positionGroup" class="form-control"/>
										</div>
									</div>
									<div class="row">
										<div class="col-sm-3">
											<label for="srcFunding" class="control-label">Source Funding</label>
										</div>
										<div class="col-sm-7">
											<form:select path="formBean.srcFunding" class="form-control" style="width:100%" >
												<option value="">- Select -</option>
												<form:options items="${srcFundingList}" />
											</form:select>  
										</div>
					      			</div>
						      	</div>
					      		<div class="modal-footer">
					        		<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal">OK</button>
					      		</div>
					    	</div>
					  	</div>
					</div>
					
					<!-- Model for select Update / Correction -->
					<div id="updateModeModel" class="modal fade" role="dialog">
						<div class="modal-dialog modal-dialog-custom">
					    	<div class="modal-content">
					      		<div class="modal-header">
						        	<h4><b>Choose an option:</b>
							    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
							    	</h4>
					      		</div>
						    	<div class="modal-body">
					      			<div class="row">
					      				<div class="col-sm-12">
							        		<button type="submit" name="btnCorrection" style="width:130px" class="btn btn-primary" onclick="performCorrection()">Correction</button>
											Keep history of existing information
										</div>
									</div>
							        <div class="row">
							        	<div class="col-sm-12">
							        		<button type="submit" style="width:130px" class="btn btn-primary" onclick="performUpdate()">Update</button>
						      				Correct existing information
						      			</div>
						      		</div>
						      	</div>
					      		<div class="modal-footer">
					        		<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal"><i class="fa fa-reply"></i> Cancel</button>
					      		</div>
					    	</div>
					  	</div>
					</div>
					
					<!-- Model for select Update / Correction -->
					<div id="deleteModeModel" class="modal fade" role="dialog">
						<div class="modal-dialog modal-dialog-custom">
					    	<div class="modal-content">
					      		<div class="modal-header">
					        		<h4><b>Choose an option:</b>
							    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
							    	</h4>
					      		</div>
						    	<div class="modal-body">
					      			<div class="row">
					      				<div class="col-sm-12">
							        		<button type="submit" name="btnDeleteNext" style="width:130px" class="btn btn-primary" onclick="performDeleteNext()"><i class="fa fa-chevron-right"></i> Next</button>
											Remove next change
										</div>
									</div>
							        <div class="row">
							        	<div class="col-sm-12">
							        		<button type="submit" style="width:130px" class="btn btn-primary" onclick="performDeleteAll()">All</button>
						      				Remove all scheduled changes
						      			</div>
						      		</div>
						      		<div class="row">
							        	<div class="col-sm-12">
							        		<button type="submit" style="width:130px" class="btn btn-primary" onclick="performDeletePurge()">Purge</button>
						      				Complete remove from the database
						      			</div>
						      		</div>
						      	</div>
					      		<div class="modal-footer">
					        		<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal"><i class="fa fa-reply"></i> Cancel</button>
					      		</div>
					    	</div>
					  	</div>
					</div>
					
					<!-- Model for select Update / Correction -->
					<div id="updateFTEModel" class="modal fade" role="dialog">
						<div class="modal-dialog modal-dialog-custom">
					    	<div class="modal-content">
					    		<div class="modal-header">
					        		<h4><b>Update FTE</b>
							    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
							    	</h4>
					      		</div>
						    	<div class="modal-body">
					      			<div class="row">
					      				<div class="col-sm-4">
							        		New FTE:
										</div>
										<div class="col-sm-8">
											<form:input path="formBean.updatedFTE" class="form-control" />
										</div>
									</div>
							        <div class="row">
					      				<div class="col-sm-4">
							        		Update reason:
										</div>
										<div class="col-sm-8">
											<form:input path="formBean.updatedFTEReason" class="form-control" />
										</div>
									</div>
						      	</div>
					      		<div class="modal-footer">
					      			<button type="button" class="btn btn-primary" name="btnSaveFTE" style="width:110px" onclick="performUpdateFTE()"><i class="fa fa-floppy-o"></i> Save</button>
					        		<button type="button" class="btn btn-default" style="width:110px"  data-dismiss="modal"><i class="fa fa-reply"></i> Cancel</button>
					      		</div>
					    	</div>
					  	</div>
					</div>
				</form>
			</div>
		</div>

		<!-- Model to confirm Delete - Start -->
		<div id="model_confirmDelete" class="modal fade" role="dialog">
			<div class="modal-dialog modal-dialog-custom modal-dialog-postId">
			    <!-- Modal content-->
			    <div class="modal-content">
			    	<div class="modal-body">
			        	<div class="row" style="padding:20px;">
							<div class="col-sm-12">
							  	<label for="" class="field_request_label">Are you sure to delete this post?</label>
							</div>
						</div>		
			      	</div>
			      	<div class="modal-footer">
						<button type="button" class="btn btn-primary" style="width:110px" data-dismiss="modal" onclick="submitDelete();"><i class="fa fa-check"></i> Yes</button>
			        	<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal"><i class="fa fa-reply"></i> Cancel</button>
				    </div>
			    </div>
		  	</div>
		</div>
		<!-- Model to confirm Withdraw - End -->

	</div> <!-- ./contain -->
</div> <!-- ./page-content-wrapper -->


<!--  Search Result Model - Start -->
<div id="searchResultModel" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:900px">
		<div class="modal-content">
			<div class="modal-header">
		    	<h4><b>Search Post</b>
		    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
		    	</h4>
		    </div>
		    <div class="modal-body">
		        <table id="tblSearchResult" class="table table-striped table-bordered"> 
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
		        </table>
		    </div>
		    <div class="modal-footer">
		    	<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div><!-- ./#searchResultModel -->

<div id="responsibilityModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:300px">
    	<div class="modal-content">
    		<div class="modal-header">
				<h4><b>Select Responsibility</b>
	    			<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
	    		</h4>
			</div>
      		<div class="modal-body">
		        <table id="tblResponsibility" style="width:260px;text-align:center">
		        	<tbody>
		        	
		        	</tbody>
		        </table>
	      	</div>
      		<div class="modal-footer">
        		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      		</div>
	    </div>
	  </div>
</div><!-- ./#responsibilityModal -->		

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>

