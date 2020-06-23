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
            data: {userId: '<c:out value="${formBean.userId}"/>'},
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
	
	function confirmSave(responsibilityId) {
		showLoading();
		$("#repsonsibilityId").val(responsibilityId);
		currentTarget.submit();
	}

	function returnToHome() {
		document.location.href = "<%= request.getContextPath() %>/home/home";
	}

	// Form javascript - Start
	function performValidation() {
		var errMsg = "";
		
		// Perform checking on valid grade date field
		for (var i=0; i<$("input[name='gradeDateFrom']").length; i++) {
			if ($($("input[name='ddGrade']")[i]).val() != "") {
				if ($($("input[name='gradeDateFrom']")[i]).val() == "") {
					errMsg = "From date for Valid Grade cannot be empty." 
					break;
				}
			
				if (daysBetween($($("input[name='gradeDateFrom']")[i]).val(), $("#effectiveFromDate").val()) < 0) {
					errMsg = "From/To date for Valid Grade should on or after position start date." 
					break;
				}	
				
				if ($($("input[name='gradeDateTo']")[i]).val() != "") {
					if (daysBetween($($("input[name='gradeDateTo']")[i]).val(), $("#effectiveFromDate").val()) < 0) {
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
	  			if ($(this).next().css("display") != "none") {
	  				$("#frmDetail").bootstrapValidator('revalidateField', $($(this).parent().find("input")[0]).attr("name"));
	  			}
	  		}
	  	});
	}
	
	function updatePostName() {
		var str = "";
		str += $("#ddHcmPostTitle").val();
		if ($("#ddHcmPostOrganization").val() != "") {
			str += "." + $("#ddHcmPostOrganization").val() + ".";
			if ($("#hcmUnitTeam").val() != "") {
				str += $("#hcmUnitTeam").val();
			}
		}
	
		$("#lblPostName").html(str);
		$("#hiddenPostName").val(str);
	}
	
	function changeDefaultLocation() {
		if ($("#hcmPostOrganization").val() != "") {
			showLoading();
			
			$.ajax({
	            url: "<%=request.getContextPath() %>/hcm/getDefaultLocation",
	            type: "POST",
	            data: {organizationId: $("#hcmPostOrganization").val()},
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
	
	function redirectUpdate() {
		var postId = $("#positionId").val();
		var effectiveDate = $("#effectiveFromDate").val();
		document.location.href="<%= request.getContextPath() %>/hcm/updateHCMPost?pId=" + postId + "&effectiveDate=" + effectiveDate;
	}
	
	function createAnotherHCM() {
		document.location.href="<%= request.getContextPath() %>/hcm/createHCMPost";
	}
	
	function createNewPost() {
		document.location.href="<%= request.getContextPath() %>/request/newPost";
	}
	
	$(function(){
		$.ajaxSetup({cache: false});
	
		// For autocomplete
		// $('#hcmPostTitle').selectToAutocomplete();
		// $('#hcmPostOrganization').selectToAutocomplete();
		// $('#hcmOrganization').selectToAutocomplete();
		// $('#hcmJob').selectToAutocomplete();
		
		$("#txtStartDate").prop("readonly", true);
		$("#txtProposedEndDate").prop("readonly", true);
		$("#txtFTE").prop("readonly", true);

		$("#frmDetail").bootstrapValidator({
			message: 'This field cannot be empty.',
			live: "submitted",
			fields: {
			},
		})
		.on('error.field.bv', function(e, data) {
			hideLoading();
		})
		.on('success.form.bv', function(e){
			currentTarget = e.target;
			if (!performValidation()) {
				e.preventDefault();
			}
			showLoading();
		});
		
		$("#ddHcmPostTitle").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMPostTitleList", request, function(result){
					response($.map(result, function(item){
						return {label: item.titleId, value: item.titleId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#hcmPostTitle").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#hcmPostTitle").val(ui.item.value);
				return false;
			},
		});
		var loadFlag = false;
		$("#ddHcmPostOrganization").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMPostOrganizationList", request, function(result){
					response($.map(result, function(item){
						return {label: item.organizatonName, value: item.organizatonId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#hcmPostOrganization").val(ui.item.value);
				
				$("#ddHcmOrganization").val(ui.item.label);
				$("#hcmOrganization").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#hcmPostOrganization").val(ui.item.value);
				
				$("#ddHcmOrganization").val(ui.item.label);
				$("#hcmOrganization").val(ui.item.value);
				
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
		
		$("#ddHcmOrganization").attr("readonly", true);
		
		$("#ddHcmJob").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMJobList", request, function(result){
					response($.map(result, function(item){
						return {label: item.jobName, value: item.jobId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#hcmJob").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#hcmJob").val(ui.item.value);
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
		
		$("#ddGrade").autocomplete({
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
		
		$("#ddGrade").on("keydown focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($(this).parent().find("input[name='grade']").val() == "") {
				$(this).val("");
				return;
			}
		});
		
		$("#ddHcmPostTitle").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#hcmPostTitle").val("");
			}
		});
		
		$("#ddHcmPostTitle").on("focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#hcmPostTitle").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#hcmPostTitle").val("");
				return;
			}
		});
		
		$("#ddHcmJob").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#hcmJob").val("");
			}
		});
		
		$("#ddHcmJob").on("focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#hcmJob").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#hcmJob").val("");
				return;
			}
		});
		
		$("#ddHcmPostOrganization").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9 && event.which != 16) {
				$("#hcmPostOrganization").val("");
				
				$("#ddHcmOrganization").val("");
				$("#hcmOrganization").val("");
				
				$("#ddLocation").val("");
				$("#location").val("");
			}
		});
		
		$("#ddHcmPostOrganization").on("focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#hcmPostOrganization").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#hcmPostOrganization").val("");
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
				
				$("#buttonPanelForSave").hide();
				$("#buttonPanelAfterUpdateSuccess").show();
				
				$("input[type='text']").attr("readonly", true);
				$("input[type='radio']").attr("disabled", true);
				$("select").attr("disabled", true);
				$("#btnAddGradeRow").attr("disabled", true);
				$("#btnAddGradeRow").hide();
				$("input[name='btnRemoveValidGrade']").attr("disabled", true);
			</c:when>
			<c:when test="${formBean.updateSuccess == 'N'}">
				$("#divErrorMsg").html('<c:out value="${formBean.displayMessage}"/>');
				$($("div[class='alert-box-icon-danger']")[0]).css("height", $("#divError").height())
				$("#divError").show();
			</c:when>
		</c:choose>
		
		<c:choose>
			<c:when test="${formBean.updateSuccess != 'Y'}">
				$("#buttonPanelForSave").show();
				$("#buttonPanelAfterUpdateSuccess").hide();
			</c:when>
		</c:choose>
	});
	
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
	
	// Form javascript - End
</script>		
			
<!-- Page Content -->
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > HCM Position > Create HCM Position 
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
			<div class="title pull-left"><i class="fa fa-envelope-open-o"></i>Create HCM Position</div>
		</div>
		<div class="panel panel-custom-primary">
			<div class="panel-body">
				<form id="frmDetail" method="POST" data-toggle="validator" role="form">
					<form:hidden path="formBean.positionId"/>
					<form:hidden path="formBean.repsonsibilityId"/>
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
							  	<label for="effectiveFromDate" class="control-label">Effective Date<font class="star">*</font></label>
							</div>
							<div class="col-sm-2">
								<div class='input-group date' id='datetimepicker1'>
									<form:input path="formBean.effectiveFromDate" 
									            class="form-control"
									            required="required"/>
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
								<label for="txt_post_name" id="lblPostName" class="txt_post_name"><c:out value="${formBean.postName}"/></label>
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
									<form:input path="formBean.ddHcmPostTitle" class="form-control" style="width:100%;" onchange="updatePostName()" onblur="updatePostName()"
												required="required"/>
									<form:hidden path="formBean.hcmPostTitle" />
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
									<form:input path="formBean.ddHcmPostOrganization"  class="form-control" style="width:100%;" onchange="updatePostName()"  onblur="updatePostName()" required="required"/>
									<form:hidden path="formBean.hcmPostOrganization" />
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
									<form:input path="formBean.hcmUnitTeam"  class="form-control" style="width:100%;" onchange="updatePostName()" required/>
								</div>
							</div>
						</div>
					
					</fieldset>
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
								<form:input path="formBean.ddHcmOrganization" class="form-control" style="width:100%;"/>
								<form:hidden path="formBean.hcmOrganization" />
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="hcmJob" class="control-label">Job<font class="star">*</font></label>
							</div>
							<div class="col-sm-8">
								<form:input path="formBean.ddHcmJob" class="form-control" style="width:100%;" required="required"/>
								<form:hidden path="formBean.hcmJob" />
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="hiringStatus" class="control-label">Hiring Status<font class="star">*</font></label>
							</div>
							<div class="col-sm-2">
								<form:select path="formBean.hiringStatus" id="ddl_HiringStatus" name="ddl_HiringStatus" class="form-control" style="width:100%;" required="required">
									<option value="">- Select -</option>
									<form:options items="${hiringStatusList}" />
								</form:select>  
							</div>
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
							<form:input path="formBean.ddLocation" class="form-control" />
							<form:hidden path="formBean.location"/>
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
								<label for="probationDurationUnit" class="control-label">Duration Unit<font class="star">*</font></label>
							</div>
							<div class="col-sm-2">
								<form:select path="formBean.probationDurationUnit" id="txtProbationDurationUnit" name="txtProbationDurationUnit" class="form-control" style="width:100%;" required="required">
									<option value="">- Select -</option>
									<form:options items="${durationUnitList}" />
								</form:select>  
							</div>
						</div>
						
						<div class="col-sm-2" style="text-align:right">
							<button type="button" class="btn btn-default" style="width:130px;" onclick="$('#updateAddition').modal('show')"><i class="fa fa-plus-circle fa-sm"></i> Additional Detail</button>					
						</div>
					</div>
					<!-- ./row -->
						
					<fieldset class="scheduler-border">
						<table style="margin:0px;padding:0px">
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
															<form:input path="formBean.ddGrade" name="grade" class="form-control" style="width:100%;" multiple="false"/>
															<form:hidden path="formBean.grade"/>
														</td>
														<td>
															<div class='input-group date'>
																<input type="text" name="gradeDateFrom" 
																		class="form-control"/>
																<span class="input-group-addon">
																	<span class="glyphicon glyphicon-calendar"></span>
																</span>
															</div>
														</td>
														<td>
															<div class='input-group date'>
																<input type="text" name="gradeDateTo" 
																		class="form-control"/>
																<span class="input-group-addon">
																	<span class="glyphicon glyphicon-calendar"></span>
																</span>
															</div>
														</td>
														<td style="text-align:center"><button type="button" name="btnRemoveValidGrade" class="btn btn-primary" onclick="removeGrade($(this))"><i class="fa fa-times"></i> Remove</button></td>
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
															            name="gradeDateFrom" class="form-control"/>														            
															<span class="input-group-addon">
																<span class="glyphicon glyphicon-calendar"></span>
															</span>
														</div>
													</td>
													<td>
														<div class='input-group date'>
															<form:input path="formBean.gradeList[${pStatus.index}].dateTo" 
															            name="gradeDateTo" class="form-control"/>
															<span class="input-group-addon">
																<span class="glyphicon glyphicon-calendar"></span>
															</span>
														</div>
													</td>
													<td></td>
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
					<br/>
					<div id="buttonPanelForSave" class="col-sm-12" style="text-align:right">
						<button type="button" class="btn btn-primary" name="btnSave" style="width:110px" onclick="performSave()"><i class="fa fa-floppy-o"></i> Save</button>
						<button type="submit" class="btn btn-primary" id="btnSubmitSave" style="width:110px; display:none"></button>
					</div>
					<div id="buttonPanelAfterUpdateSuccess" class="col-sm-12" style="text-align:right">
						<button type="button" class="btn btn-primary" name="btnCreateAnother" style="width:180px;" onclick="redirectUpdate()">Perform Update</button>
						<button type="button" class="btn btn-primary" name="btnCreateAnother" style="width:180px;" onclick="createAnotherHCM()">Create Another HCM Position</button>
						<button type="button" class="btn btn-primary" name="btnCreateNewPost" style="width:180px;" onclick="createNewPost()">Create New Post</button>
					</div>
							
					<!-- Model for input additional details -->
					<div id="updateAddition" class="modal fade" role="dialog">
						<div class="modal-dialog modal-dialog-custom" style="width:750px">
					    	<div class="modal-content">
					    		<div class="modal-header">
									<h4><b>Additional Details</b></h4>
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
											<label for="srcFunding" class="control-label">Source Funding</label></div>
										<div class="col-sm-7">
											<form:select path="formBean.srcFunding" class="form-control" style="width:100%" >
												<option value="">- Select -</option>
												<form:options items="${srcFundingList}" />
											</form:select>
										</div>
					      			</div>
						      	</div>
					      		<div class="modal-footer">
					        		<button type="button" class="btn btn-default" style="width:100px" data-dismiss="modal">OK</button>
					      		</div>
					    	</div>
					  	</div>
				  	</div><!-- ./updateAddition -->
				</form>
			</div>
		</div> <!-- ./panel -->
	</div> <!-- ./container -->


	<!-- Model for select Update / Correction -->
	<div id="updateModeModel" class="modal fade" role="dialog">
		<div class="modal-dialog modal-dialog-custom" style="width:300px">
	    	<div class="modal-content">
	    		<div class="modal-header">
					<h4><b>Additional Details</b>
			    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
			    	</h4>
				</div>
	      		<div class="modal-body">
			        <button type="button" style="width:130px" class="btn btn-primary" onclick="performCorrection()">Correction</button>
			        <br/><br/>
			        <button type="button" style="width:130px" class="btn btn-success">Update</button>
		      		<br/>
		      	</div>
	      		<div class="modal-footer">
	        		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      		</div>
	    	</div>
	  	</div>
	</div>
	
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
	</div>
</div> <!-- ./page-content-wrapper -->
<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>