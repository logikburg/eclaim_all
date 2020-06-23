<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.1.0/chosen.min.css" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.1.0/chosen.jquery.min.js"></script>

<script>
	$(document).ready(function(e) {
	
		$('.chosen-select').chosen();
		$('.chosen-container.chosen-container-multi').width('100%');
		$("#wrapper").toggleClass("active");
	
		$("#frmInvitation .input-group.date").datepicker({
			format : "dd/mm/yyyy",
			autoclose : true,
			daysOfWeekHighlighted : [ 0 ],
			todayHighlight : true,
			todayBtn : "linked"
		}).on(
			'changeDate',
			function(e) {
				if ($(this).next().hasClass('help-block')) {
					$("#frmDialog").bootstrapValidator('revalidateField',
							$($(this).parent().find("input")[0]).attr("name"));
			}
		});
				
		$("[id*='includedInvitation']").each(function(){
			$(this).find('#departmentName').autocomplete({
				source: function(request, response) {
					$.getJSON("<c:url value='/payment/getProjectDepartmentList'/>",request, function(result){
						response($.map(result, function(item, key){
							console.log(item);
							return {label: item, value: key}
						}));
					});
				},
				focus: function(event, ui) {
					$(this).val(ui.item.label);
					$("#departmentId").val(ui.item.value);
					return false;
				},
				select: function(event, ui) {
					$(this).val(ui.item.label);
					$("#departmentId").val(ui.item.value);
					return false;
				}
			});
		});

		$("[id*='includedInvitation']").each(function(){
			$(this).find('#hospitalName').autocomplete({
				source: function(request, response) {
					$.getJSON("<c:url value='/payment/getProjectHospitalList'/>",request, function(result){
						response($.map(result, function(item, key){
							console.log(item);
							return {label: item, value: key}
						}));
					});
				},
				focus: function(event, ui) {
					$(this).val(ui.item.label);
					$("#hospitalId").val(ui.item.value);
					return false;
				},
				select: function(event, ui) {
					$(this).val(ui.item.label);
					$("#hospitalId").val(ui.item.value);
					return false;
				}
			});
		});
		
	});
	
	function publish(e){
		//alert("Published");
		var formInv = $("#frmInvitation");
		var includedInvitations = $("#frmInvitation *[id*=includedInvitation]");
		
		var req = {}; 
		var inclArr = [];
		var inclObj = {};
		includedInvitations.each(function(e){
			var tis = $(this);
			var inclInputs = tis.find('input');
			inclInputs.each(function(_e){
				var ts = $(this);
				if(ts.attr('id') != undefined)
					inclObj[ts.attr('id')] = ts.val();
			});
			inclArr.push(inclObj);
			inclObj = {};
		});
		
		req['publishDate'] = formInv.find('#publishDate').val();
		req['projectName'] = formInv.find('#projectName').text();
		req['startDate'] = formInv.find('#startDate').text();
		req['endDate'] = formInv.find('#endDate').text();
		req['othInfo'] = formInv.find('#othInfo').val();
		req['targetApp'] = formInv.find('#targetApp').val();
		req['includedInvitations'] = inclArr;
		
		var data = JSON.stringify(req);
		console.log(data);
				
	    $.ajax({
	        url: './invitation/publish',
	        type: 'POST',
	        data: data,
	        contentType: 'application/json',
			success: function(result) {
				location.href = result;
				alert("Published successfully");
	        },
	        error: function(request, status, error) {
				//Ajax failure
				alert("Some problem occur during call the ajax: " + request.responseText);
            }
	    });
	}
</script>


<style>
<!--	
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
	
	/*.panel-default {
		border-color: #777;
	}*/
	
	.panel-default>.panel-heading {
		color: #333;
		background-color: #2f79b9;
		border-color: #777;
		border-top-left-radius: unset;
		border-top-right-radius: unset;
	}
	
	.panel-title>.small, .panel-title>.small>a, .panel-title>a, .panel-title>small, .panel-title>small>a {
		color: #FFFFFF;
	}

	.checkbox {
	  padding-left: 20px;
	}
	.checkbox label {
	  display: inline-block;
	  vertical-align: middle;
	  position: relative;
	  padding-left: 5px;
	}
	.checkbox label::before {
	  content: "";
	  display: inline-block;
	  position: absolute;
	  width: 17px;
	  height: 17px;
	  left: 0;
	  margin-left: -20px;
	  border: 1px solid #cccccc;
	  border-radius: 3px;
	  background-color: #fff;
	  -webkit-transition: border 0.15s ease-in-out, color 0.15s ease-in-out;
	  -o-transition: border 0.15s ease-in-out, color 0.15s ease-in-out;
	  transition: border 0.15s ease-in-out, color 0.15s ease-in-out;
	}
	.checkbox label::after {
	  display: inline-block;
	  position: absolute;
	  width: 16px;
	  height: 16px;
	  left: 0;
	  top: 0;
	  margin-left: -20px;
	  padding-left: 3px;
	  padding-top: 1px;
	  font-size: 11px;
	  color: #555555;
	}
	
	.checkbox input[type="checkbox"]{
		display: none;
	}
	
	.checkbox input[type="checkbox"]:checked + label::after,
	.checkbox input[type="radio"]:checked + label::after {
	  font-family: "FontAwesome";
	  content: "\f00c";
	}
	
	.checkbox.checkbox-md label::before {
	  width: 34px;
	  height: 34px;
	  top: -7px;
	}

	.checkbox.checkbox-md label::after {
	  width: 34px;
	  height: 34px;
	  padding-left: 4px;
	  font-size: 24px;
	  left: 1px;
	  top: -8px;
	}

	.checkbox.checkbox-md label {
	  padding-left: 22px;
	  top: -2px;
	}
	
	.chosen-container-multi .chosen-choices li.search-choice {
		padding: 1px 20px 1px 3px;
		border: 1px solid #dfdfdf;
		margin-top: 6px;
		margin-bottom: 2px;
		margin-left: 5px;
		background-color: #dfdfdf;
	}
	
	.chosen-container-multi .chosen-choices {
		border: 1px solid #cccccc;
		background-image: none;
		appearance: textfield;
		border-radius: 4px;
		box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	}
	
	.search-choice>span {
		font-size: 11px;
	}
	
	.chosen-container .search-choice .search-choice-close {
		top: 3px;
	}
	
	.chosen-container-multi .chosen-choices li.search-field input[type=text]
	{
		margin: 3px 0;
		height: 24px;
	}
	
	.chosen-container.chosen-container-multi {
		width: 70%;
	}
	
	.chosen-container .chosen-drop {
		border: 1px solid #dfdfdf;
	}
	
	.chosen-container-multi .chosen-choices li.search-choice .search-choice-close {
		top: 2px;
	}
	
	.chosen-container-active .chosen-choices {
		box-shadow: none;
	}
-->
</style>

<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12" id="divMyRequest" style="padding-left: 5px;padding-right: 5px;">
				<div class="panel panel-custom-primary">
					<div class="panel-heading">
						<h3 class="panel-title">
							<i class="glyphicon glyphicon-list-information"></i> Project Invitation 
						</h3>
					</div>

					<div class="panel-body">
						<form id="frmInvitation" method="POST" action='<c:url value="/invitation/publish"></c:url>'>
							<input type="hidden" id="projectId" name="projectId" value="${includedInvitations.projectId}">
							<div class="row">
								<div class="col-md-2">Project Name</div>
									<div class="col-md-4"><label for="projectName" id="projectName">Special honorarium in PWH</label></div>
								<div class="col-md-2">Project Duration</div>
									<div class="col-md-4"><label for="startDate" id="startDate">01/06/2018</label> - <label for="endDate" id="endDate">30/11/2018</label></div>
							</div>
							<div class="row">
								<div class="col-md-2">Publish Date <font class="star">*</font></div>
								<div class="col-md-4">
									<div class="input-group date" id="datetimepicker1" style="max-width: 130px;">
										<input id="publishDate" name="publishDate" value="10/11/2019" class="form-control" required="required" type="text" data-bv-field="publishDate">
										<span class="input-group-addon">
											<span class="glyphicon glyphicon-calendar"></span>
										</span>
									</div>
								</div>
							</div>
							
							<c:forEach var="includedInvitations" items="${formBean.includedInvitations}" varStatus="invar">
								<div id="includedInvitation_${invar.index}" class="panel panel-default">
									<div class="panel-heading">
										<div class="checkbox checkbox-inline checkbox-md">
		                					<input type="checkbox" class="styled" id="included" value="0">
		                					<label for="included${invar.index}" style="color: white;"> Included in this invitation</label>
		            					</div>
									</div>
		
									<div id="generalInfo" class="panel-collapse collapse in" aria-expanded="true">
										<div class="panel-body">
											<div class="row">
												<div class="col-sm-2 col-darkgrey">Job(s):</div>
												<div class="col-sm-4 col-text">
													<select name="jobGroup" id ="jobGroup"  class="form-control chosen-select" multiple>
														<c:forEach var="jobs" items="${includedInvitations.jobs.split(',')}">
															<option selected value='${jobs}'>${jobs}</option>
														</c:forEach>
													</select>
													<input type="hidden" id="jobs" name="jobs" value="${includedInvitations.jobs}">
													<input type="hidden" id="jobGroupId" name="jobGroupId" value="${includedInvitations.jobGroupId}">
												</div>
											</div>
											<div class="row">
												<div class="col-md-2">Working Schedule</div>
												<div class="col-sm-4 col-text">
													<input type="text" id="workingSchedule" name="workingSchedule" class="form-control ui-autocomplete-input" autocomplete="off">
												</div>
											</div>
											<div class="row">
												<div class="col-md-2">Application Deadline <font class="star">*</font></div>
												<div class="col-md-4">
													<div class="input-group date" id="datetimepicker1" style="max-width: 130px;">
														<input id="appDeadline" name="appDeadline" value="10/11/2019" class="form-control" required="required" type="text" data-bv-field="publishDate">
														<span class="input-group-addon">
															<span class="glyphicon glyphicon-calendar"></span>
														</span>
													</div>
												</div>
												<div class="col-md-2">Last Application Deadline</div>
												<div class="col-md-4"><b>N/A</b></div>
											</div>
											<div class="row">
												<div class="col-md-2">Hospital</div>
												<div class="col-md-4">
													<input type="text" id="hospitalName" name = "hospitalName" class="form-control"
														data-validation="required" data-validation-error-msg="Department is missing.">
													<input type="hidden" id="hospitalCode" name = "hospitalId" class="form-control">
												</div>
												<div class="col-md-2">Department</div>
												<div class="col-md-4">
													<input type="text" id="departmentName" name="departmentName" class="form-control"
														data-validation="required" data-validation-error-msg="Department is missing.">
													<input type="hidden" id="departmentId" name = "departmentId" class="form-control">
												</div>
											</div>
											<div class="row">
												<div class="col-md-2">Job Description / Competency Requirement <font class="star">*</font></div>
												<div class="col-md-10">
													<textarea id="jobDesc" name="jobDesc" class="form-control ui-autocomplete-input" rows="3"></textarea>
												</div>
											</div>
										</div>
									</div>
								</div>
							</c:forEach>

							<div class="row">
								<div class="col-md-2">Other Information</div>
								<div class="col-md-4">
									<textarea id="othInfo" name="othInfo" class="form-control ui-autocomplete-input" rows="5"></textarea>
								</div>
								<div class="col-md-2">Target Application</div>
								<div class="col-md-4">
									<textarea id="targetApp" name="targetApp" class="form-control ui-autocomplete-input" rows="5"></textarea>
								</div>
							</div>
							<div class="row">
								<div class="col-md-2 buttons-grp pull-right">
									<button type="button" class="btn btn-primary" onclick="back()" style="width: 61px;">Back</button>
									<button type="button" class="btn btn-primary" onclick="publish()" style="width: 61px;">Publish</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- ./container-fluid -->
</div>
<!-- ./Page Content -->

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>