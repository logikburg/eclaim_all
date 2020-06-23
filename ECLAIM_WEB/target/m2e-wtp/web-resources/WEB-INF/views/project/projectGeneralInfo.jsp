<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>
<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script> -->
<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/jquery/momentJS/moment.js"%>"></script>

<link rel="stylesheet" href="<c:url value="/plugins/bootstrap/select/css/bootstrap-select.css" />" type="text/css" media="all" />
<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/bootstrap/select/bootstrap-select.min.js"%>"></script>

<link rel="stylesheet" href="<c:url value="/plugins/jquery/timeentry/css/jquery.timeentry.css" />" type="text/css" media="all" />
<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/jquery/timeentry/jquery.plugin.js"%>"></script>
<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/jquery/timeentry/jquery.timeentry.js"%>"></script>


<!-- <style> -->
/* div#generalInfo .row { */
/* 	margin-bottom: 0px; */
/* } */
/* div#generalInfo label{ */
/* 	min-width: 159px; */
/* } */
/* div#generalInfo .panel-body{ */
/*     padding: 15px 40px; */
/* } */
<!-- </style> -->

<script>

	function performUpdate() {
		$("#divErrorMsg").html('');
		$("#divError").hide();
		$("#divSuccessMsg").html('');
		$("#divSuccess").hide();
	}
	
	$(function(){
// 		$("#wrapper").toggleClass("active");
		$('.selectpicker').selectpicker();
		
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
		// For Seesion StartTime & End Time Start
		$('.form_datetime').timeEntry({show24Hours: true, spinnerImage: '../plugins/jquery/timeentry/spinnerDefault.png'});

		$('.session').on("change", function(event) {
			if($(this).prop('type') === undefined){
				return;
			}
			console.log($(this).val());
			var isDisable = ($(this).val() === '' || $(this).val() === null);
			console.log('isDisable:'+isDisable);
			$(this).parents('tr').find('.is-timeEntry').timeEntry(isDisable ? 'disable' : 'enable');
		});
		// For Seesion StartTime & End Time End
		
		var dateFormat = 'DD/MM/YYYY';
		
		$.fn.bootstrapValidator.validators.validStartDate = {
		        validate: function(validator, $field, options) {
		            var value = $field.val();
		          	var recType = $('#recType').val();
		          	
		          	var startDate = moment(value, dateFormat);
		          	var endDate = moment($('#endDate').val(), dateFormat);
		          	
					if(!startDate.isValid()){
						return false;
					}
					
		            if(recType == 'PROJ_EXTEND'){
		            	var orgiEndDate = moment($('#orgiEndDate').val(), dateFormat);
		            	if(orgiEndDate.diff(startDate,'days') >= 0){
		            		return {
		                        valid: false, 
		                        message: 'The project start date must be later than previous project end date'
		                    }
		            	}
		            }
		            
		            if(endDate.isValid()){
						if(startDate.diff(endDate,'days') >= 0){
							return {
		                        valid: false, 
		                        message: 'The project start date must be earlier than project end date'
		                    }
						}
					}
		            
					var isAfterScheduleDate = false;
					
		            var scheduleDateList = $('#scheduleDateTable').find('tr.scheduleRow').each(function(){
						console.log($(this).find('[name$="scheduleDate"]').val());
						var scheduleDate = moment($(this).find('[name$="scheduleDate"]').val(), dateFormat);
						console.log('DIFF' +scheduleDate.diff(startDate,'days'));
						if(scheduleDate.isValid() && scheduleDate.diff(startDate,'days') < 0){
							isAfterScheduleDate = true;
						}
					});
		            if(isAfterScheduleDate){
		            	console.log('Return schedule')
		            	return {
		                    valid: false, 
                        	message: 'The Project start date could not be later than any scheduled duty'
                    	}
		            }

		            return true;
		        }
		};
		
		$.fn.bootstrapValidator.validators.validEndDate = {
		        validate: function(validator, $field, options) {
		            var value = $field.val();
		          	var recType = $('#recType').val();
		          	
		          	var endDate = moment(value, dateFormat);
		          	var startDate = moment($('#startDate').val(), dateFormat);
		          	
					if(!endDate.isValid()){
						return false;
					}
					
		            if(recType == 'PROJ_UPDATE'){
		            	var orgiEndDate = moment($('#orgiEndDate').val(), dateFormat);
		            	if(endDate.diff(orgiEndDate,'days') > 0){
		            		return {
		                        valid: false, 
		                        message: 'The project end date could not be later than original project end date'
		                    }
		            	}
		            }
		            
		            if(startDate.isValid()){
		            	if(startDate.diff(endDate,'days') >= 0){
							return {
		                        valid: false, 
		                        message: 'The project end date must be later than project start date'
		                    }
						}
					}
					var isAfterScheduleDate = false;
					
		            var scheduleDateList = $('#scheduleDateTable').find('tr.scheduleRow').each(function(){
						console.log($(this).find('[name$="scheduleDate"]').val());
						var scheduleDate = moment($(this).find('[name$="scheduleDate"]').val(), dateFormat);
						if(scheduleDate.diff(endDate,'days') > 0){
							isAfterScheduleDate = true;
						}
					});
		            if(isAfterScheduleDate){
		            	return {
		                    valid: false, 
                        	message: 'The Project end date could not be earlier than any scheduled duty'
                    	}
		            }
		            
		            return true;
		        }
		};
		
		$("#frmDetail").bootstrapValidator({
			excluded: [':disabled'],
			message: ' This value is not valid',
// 			live: "submitted",
			fields: {
				departmentName: {
	                message: 'The department is not valid',
	                validators: {
	                    notEmpty: {
	                        message: 'The department is required and cannot be empty'
	                    }
	                }
	            },
				projectName: {
	                message: 'The project name is not valid',
	                validators: {
	                    notEmpty: {
	                        message: 'The project name is required and cannot be empty'
	                    },
	                    stringLength: {
	                    	max: 150,
	                        message: 'The project name must less than 150 characters long'
	                    }
	                }
	            },
	            projectPurpose: {
	                message: 'The project purpose is not valid',
	                validators: {
	                    notEmpty: {
	                        message: 'The project purpose is required and cannot be empty'
	                    },
	                    stringLength: {
	                    	max: 4000,
	                        message: 'The project purpose must less than 4000 characters long'
	                    }
	                }
	            },
	            owner: {
	            	message: 'The project owner is not valid',
	            	validators: {
	                    notEmpty: {
	                        message: 'The project owner is required and cannot be empty'
	                    },
	                    stringLength: {
	                    	max: 150,
	                        message: 'The project owner must less than 150 characters long'
	                    }
	                }
	            },
	            startDate:{
	            	validators: {
	                    validStartDate: {
	                        message: 'The start date is not valid'
	                    }
	                }
	            },
	            endDate:{
	            	validators: {
	            		validEndDate: {
	            			message: 'The end date is not valid'
	            		}
	            	}
	            }
			},
		}).on('success.form.bv', function(e){
		}).on('submit', function(e){
			var count = 0;
			var scheduleList = $('#scheduleTable').find('tr.scheduleRow').each(function(){
// 				console.log($(this).find('[name$="patternCode"]').val());
				if($(this).find('[name$="patternCode"]').val() != null 
						&& $(this).find('[name$="patternCode"]').val() != '' ){
					count++;
				}
			});
			
			var scheduleDateList = $('#scheduleDateTable').find('tr.scheduleRow').each(function(){
// 				console.log($(this).find('[name$="scheduleDate"]').val());
				if($(this).find('[name$="scheduleDate"]').val() != ''){
					count++;
				}
			});
			console.log('Count' + count);
			if(count == 0){
				alert('Please input at At laset one schedule record.');
				e.preventDefault();
			}
		}).on('click', '.addButton', function() {
			var $table    = $(this).parents('Table');
			var preName = $table.attr("id") == 'scheduleDateTable' ? 'scheduleDateList' : 'scheduleList';
			var lastIndex = $table.find('tr.scheduleRow').size();
            var $template = $table.find('#templateRow'),
		                $clone    = $template
		                                .clone()
		                                .removeClass('hide')
		                                .addClass('scheduleRow')
		                                .removeAttr('id')
		                                .insertBefore($template);
		            
          				//setup Schedule Day
 						$clone.find('[name="patternCode"]').selectpicker();
 
 						//setup Schedule Date
 						$clone.find('[name="scheduleDate"]').datepicker({
					  		format: "dd/mm/yyyy",
					  		autoclose: true,
					  		daysOfWeekHighlighted: [0],
					  		todayHighlight: true,
					  		todayBtn: "linked"
					  	});
 						
						$clone.find(".form-control").each(function() {
						    $(this).attr({
						      'name': function(_, name) { return preName+'['+lastIndex+'].'+ name},
						    });
						  });
						
						$clone.find(".form_datetime_clone").each(function() {
							$(this).timeEntry({show24Hours: true, spinnerImage: '../plugins/jquery/timeentry/spinnerDefault.png'});
						});

		        })
		     	// Remove button click handler
		        .on('click', '.removeButton', function() {
		            var $row    = $(this).parents('.scheduleRow'),
		            	$status   = $row.find('[name$="status"]');
		            
		            if($row.parent().find(".scheduleRow:visible").length == 1){
		            	alert("At laset one Schedule row.");
		            }else{
			            // Remove element containing the option
			            $row.hide();
	
			            // Set Row Status as Delete
			            $status.val("DELETE");
		            }
		     	});
		
		
		
		$("#departmentName").autocomplete({
			source: function(request, response) {
				$.getJSON("getProjectDepartmentList",request, function(result){
					response($.map(result, function(item, key){
// 						console.log(item);
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
			},
		});
		
		$("#owner").autocomplete({
			source: function(request, response) {
				$.getJSON("getUserNameList", request, function(result){
					response($.map(result, function(item, key){
						console.log(key);
						console.log(item);
						return {label: item, value: key}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#ownerId").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#ownerId").val(ui.item.value);
				return false;
			},
		});
		
		$("#departmentName").on("keydown focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}	
			if ($("#departmentId").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#departmentId").val("");
				return;
			}
		});
		
		$("#departmentName").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#departmentId").val("");
			}
		});
		
		$("#owner").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#ownerId").val("");
			}
		});
		
		$("#owner").on("keydown focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}	
			if ($("#ownerId").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#ownerId").val("");
				return;
			}
		});
	});
</script>

<style type="text/css">
     .date{ 
     	width:130px;  
     }
	.table {
		width: 550px;
	}
	.chosen-select{
    	width:"100px";
    }
	.timedate-picker table.table-condensed {
		margin-top: -28px;
	}
	.form_datetime, .form_datetime_clone{
		width: 90px;
		display: inline-block;
	}
	.timedate-picker .datetimepicker-hours>table thead, .timedate-picker .datetimepicker-minutes>table thead {
		visibility: hidden;
		font-size: 0px;
		display: inline-table;
	}
	
	.timedate-picker .datetimepicker-hours>table th, .timedate-picker .datetimepicker-minutes>table th {
		padding: 0px;
		visibility: visible;
		height: 0px;
		overflow: hidden;
		display: inline-block;
		width: 60px;
		height: 0px;
		overflow: hidden;
	}
</style>

<!-- Page Content -->
<div id="page-content-wrapper">
	<div id="currStep" style="display: none">1</div>
	<div class="container-fluid">
		<div id="divSuccess" style="display:none" class="alert-box-success">
			<div class="alert-box-icon-success"><i class="fa fa-check"></i></div>
			<div id="divSuccessMsg" class="alert-box-content-success"></div>
		</div>
		<div id="divError" style="display:none" class="alert-box-danger">
			<div class="alert-box-icon-danger"><i class="fa fa-warning"></i></div>
			<div id="divErrorMsg" class="alert-box-content-danger"></div>
		</div>
		<c:forEach var="msg" items="${formBean.displayMessages}">
			<div id="divError" class="alert-box-danger">
				<div class="alert-box-icon-danger"><i class="fa fa-warning"></i></div>
				<div id="divErrorMsgs" class="alert-box-content-danger">${msg}</div>
			</div>
		</c:forEach>
		
		<!-- Function Name -->
		<%@ include file="/WEB-INF/views/project/projectHeading.jsp"%>

		<form id="frmDetail" method="POST" autocomplete="off">
			<div class="panel panel-custom-primary" id="generalInfo">
				<div class="panel-body">
					<div class="row">
						<div class="form-group">
							<div class="col-md-2">
								<label for="department" id="departmentLab"
									class="field_request_label v-middle">Department<font
									class="star">*</font></label>
							</div>
							<div class="col-md-10">
								<form:input path="formBean.departmentName" class="form-control department"></form:input>
								<form:hidden path="formBean.departmentId" />
							</div>
						</div>
					</div>
					<!-- 					<div class="row"> -->
					<!-- 						<div class="col-md-2"> -->
					<!-- 							<label for="hospitalSRS" >Hospital (SRS)</label> -->
					<!-- 						</div> -->
					<!-- 						<div class="col-md-3"> -->
					<%-- 							<form:select path="formBean.srsHospital" name="hospitalSRS" class="form-control"> --%>
					<%-- 								<form:options items="${hospitalSrsList}" /> --%>
					<%-- 							</form:select> --%>

					<!-- 						</div> -->
					<!-- 						<div class="col-md-2"> -->
					<!-- 							<label for="departmentSRS" >Department (SRS)</label> -->
					<!-- 						</div> -->
					<!-- 						<div class="col-md-3"> -->
					<%-- 							<form:select path="formBean.srsDepartment" name="departmentSRS"	class="form-control"> --%>
					<%-- 								<form:options items="${deptSrsList}" /> --%>
					<%--  							</form:select> --%>
					<!-- 						</div> -->
					<!-- 					</div> -->
					<div class="row">
						<div class="form-group">
							<div class="col-md-2">
								<label for="projectName" class="field_request_label">Project
									Name<font class="star">*</font>
								</label>
							</div>
							<div class="col-md-10">
								<form:input path="formBean.projectName" name="projectName"
									class="form-control" required="required"></form:input>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="form-group">
							<div class="col-md-2">
								<label for="projectNameChinese">&#38917;&#30446;&#21517;&#31281;
							</div>
							<div class="col-md-10">
								<form:input path="formBean.projectNameC"
									name="projectNameChinese" class="form-control"></form:input>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="form-group">
							<div class="col-md-2">
								<label for="projectPurpose" class="field_request_label">Project
									Purpose<font class="star">*</font>
								</label>
							</div>
							<div class="col-md-10">
								<form:textarea path="formBean.purpose" rows="4" type="text"
									name="projectPurpose" required="required" class="form-control"></form:textarea>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="form-group">
							<div class="col-md-2">
								<label for="projectOwner" id="projectOwnerLab"
									class="field_request_label">Project Owner<font
									class="star">*</font></label>
							</div>
							<div class="col-md-4">
								<form:input path="formBean.owner" class="form-control"></form:input>
								<form:hidden path="formBean.ownerId" />
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-2">
								<label for="projectPreparer" class="field_request_label">Project
									Preparer<font class="star">*</font>
								</label>
							</div>
							<div class="col-md-4">
								<div>
									<c:out value="${formBean.preparer}" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="form-group">
							<div class="col-md-2">
								<label for="fundSource">Funding Source<font class="star">*</font></label>
							</div>
							<div class="col-md-4">
								<form:input path="formBean.fundingSource" name="fundSource" required="required"
									class="form-control" placeholder="e.g. HCE Fund, HO Fund"></form:input>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-2">
								<label for="programType" class="field_request_label">Program Type<font class="star">*</font>
								</label>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<form:select path="formBean.programType" class="form-control" required="required">
										<form:options items="${programTypeList}" />
									</form:select>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="form-group">
							<div class="col-md-2">
								<label for="startDate">Project Start Date<font
									class="star">*</font> </label>
							</div>
							<div class="col-md-2">
								<div class="input-group date">
									<form:input path="formBean.startDate"
										id="startDate" name="startDate" class="form-control"
										required="required"/>
									<span class="input-group-addon"> 
										<span class="glyphicon glyphicon-calendar"></span>
									</span>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-2">
								<label for="endDate">Project End Date<font
									class="star">*</font> </label>
							</div>
							<div class="col-md-2">
								<div class='input-group date'>
									<form:input path="formBean.endDate"
										id="endDate" name="endDate" class="form-control" 
										required="required" />
									<span class="input-group-addon"> <span
										class="glyphicon glyphicon-calendar"></span>
									</span>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="form-group">
							<div class="col-md-4">
								<label for="scheduleTable">Proposed Daily Schedule</lable>
							</div>
							<div class="col-md-6"></div>
						</div>
					</div>
					<div class="row">
					<div class="col-md-12">
						<table id="scheduleTable"
							class="table table-striped table-bordered">
							<thead>
								<tr>
									<th style="width: 20%;"></th>
									<th style="width: 30%;">Session Days</th>
									<th style="width: 25%;">Start Time</th>
									<th style="width: 25%;">End Time</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${formBean.scheduleList}" var="vo"
									varStatus="status">
									<tr class="scheduleRow">
										<td>
											<div class="btn-group">
												<button name="newRow" type="button"
													class="btn btn-default addButton">
													<i class="fa fa-plus-circle fa-2x"></i>
												</button>
												<button name="deleteRow" type="button"
													class="btn btn-default removeButton">
													<i class="fa fa-times-circle fa-2x"></i>
												</button>
											</div>
										</td>
										<td class="form-group">
											<form:select path="formBean.scheduleList[${status.index}].patternCode" required="${vo.patternCode != null ? 'required' : ''}"
												class="form-control selectpicker session" multiple = "true" data-width="fit">
												<form:options items="${patternList}" itemLabel="patternDesc"
													itemValue="patternCode" />
											</form:select>
<%-- 												<form:checkboxes items="${patternList}" itemLabel="patternDesc" itemValue="patternCode" path = "formBean.scheduleList[${status.index}].patternCode" /> --%>
										</td>
										<td class="form-group"><input
											name="scheduleList[${status.index}].startTime"
											data-date-format="hh:ii" class='form-control form_datetime'
											type="text" value="${vo.startTime}" data-bv-notempty <c:if test="${vo.startTime == null}">disabled</c:if> /></td>
										<td class="form-group"><input
											name="scheduleList[${status.index}].endTime"
											data-date-format="hh:ii" class='form-control form_datetime'
											type="text" value="${vo.endTime}" data-bv-notempty <c:if test="${vo.endTime == null}">disabled</c:if> /></td>
										<td class="hide"><input
											name="scheduleList[${status.index}].status"
											value="${vo.status}" class='form-control' type="text" /></td>
										<td class="hide"><input
											name="scheduleList[${status.index}].projectId"
											value="${vo.projectId}" class='form-control ' type="text" /></td>
										<td class="hide"><input
											name="scheduleList[${status.index}].projectVerId"
											value="${vo.projectVerId}" class='form-control' type="text" /></td>
										<td class="hide"><input
											name="scheduleList[${status.index}].scheduleId"
											value="${vo.scheduleId}" class='form-control' type="text" /></td>
									</tr>
								</c:forEach>

								<tr class="hide" id="templateRow">
									<td>
										<div class="btn-group">
											<button name="newRow" type="button"
												class="btn btn-default addButton">
												<i class="fa fa-plus-circle fa-2x"></i>
											</button>
											<button name="deleteRow" type="button"
												class="btn btn-default removeButton">
												<i class="fa fa-times-circle fa-2x"></i>
											</button>
										</div>
									</td>

									<td class="form-group">
										<select name="patternCode" class="form-control session" multiple/> 
											<c:forEach items="${patternList}" var="pattern">
												<option value="${pattern.patternCode}">${pattern.patternDesc}</option>
											</c:forEach> 
										</select>
									</td>

									<td class="form-group"><input name="startTime"
										class="form-control form_datetime_clone" autocomplete="off" data-bv-notempty disabled
										type="text"></td>

									<td class="form-group"><input name="endTime"
										class="form-control form_datetime_clone" autocomplete="off" data-bv-notempty disabled
										type="text"></td>

									<td class="hide"><input name="status" value="new"
										class='form-control' type="text" /></td>
								</tr>

							</tbody>
						</table>
						</div>
					</div>
					<div class="row">
						<div class="form-group">
							<div class="col-md-4">
								<label for="scheduleTable">Proposed Schedule of Dates</lable>
							</div>
							<div class="col-md-6"></div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
						<table id="scheduleDateTable"
							class="table table-striped table-bordered">
							<thead>
								<tr>
									<th style="width: 20%;"></th>
									<th style="width: 30%;">Session Date</th>
									<th style="width: 25%;">Start Time</th>
									<th style="width: 25%;">End Time</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${formBean.scheduleDateList}" var="vo"
									varStatus="status">
									<tr class="scheduleRow">
										<td>
											<div class="btn-group">
												<button name="newRow" type="button"
													class="btn btn-default addButton">
													<i class="fa fa-plus-circle fa-2x"></i>
												</button>
												<button name="deleteRow" type="button"
													class="btn btn-default removeButton">
													<i class="fa fa-times-circle fa-2x"></i>
												</button>
											</div>
										</td>
										<td class="form-group">
										<div class="input-group date">
											<form:input path="formBean.scheduleDateList[${status.index}].scheduleDate" class="form-control session" required="${vo.scheduleDate != null ? 'required' : ''}"/>
											<span class="input-group-addon"> 
												<span class="glyphicon glyphicon-calendar"></span>
											</span>
										</div>
										</td>
										<td class="form-group"><input
											name="scheduleDateList[${status.index}].startTime"
											data-date-format="hh:ii" class='form-control form_datetime'
											type="text" value="${vo.startTime}"  data-bv-notempty <c:if test="${vo.startTime == null}">disabled</c:if> /></td>
										<td class="form-group"><input
											name="scheduleDateList[${status.index}].endTime"
											data-date-format="hh:ii" class='form-control form_datetime'
											type="text" value="${vo.endTime}"  data-bv-notempty <c:if test="${vo.endTime == null}">disabled</c:if> /></td>
										<td class="hide"><input
											name="scheduleDateList[${status.index}].status"
											value="${vo.status}" class='form-control' type="text" /></td>
										<td class="hide"><input
											name="scheduleDateList[${status.index}].projectId"
											value="${vo.projectId}" class='form-control ' type="text" /></td>
										<td class="hide"><input
											name="scheduleDateList[${status.index}].projectVerId"
											value="${vo.projectVerId}" class='form-control' type="text" /></td>
										<td class="hide"><input
											name="scheduleDateList[${status.index}].scheduleId"
											value="${vo.scheduleId}" class='form-control' type="text" /></td>
									</tr>
								</c:forEach>

								<tr class="hide" id="templateRow" preName="scheduleDateList">
									<td>
										<div class="btn-group">
											<button name="newRow" type="button"
												class="btn btn-default addButton">
												<i class="fa fa-plus-circle fa-2x"></i>
											</button>
											<button name="deleteRow" type="button"
												class="btn btn-default removeButton">
												<i class="fa fa-times-circle fa-2x"></i>
											</button>
										</div>
									</td>

									<td class="form-group">
										<div class="input-group date">
											<input name="scheduleDate" class="form-control" type="text"/>
											<span class="input-group-addon"> 
												<span class="glyphicon glyphicon-calendar"></span>
											</span>
										</div>
									</td>

									<td class="form-group"><input name="startTime"
										class="form-control form_datetime_clone" autocomplete="off" data-bv-notempty disabled
										type="text" d></td>

									<td class="form-group"><input name="endTime"
										class="form-control form_datetime_clone" autocomplete="off" data-bv-notempty disabled
										type="text"></td>

									<td class="hide"><input name="status" value="new"
										class='form-control' type="text" /></td>
								</tr>

							</tbody>
						</table>
						</div>
					</div>
					<form:hidden path="formBean.preparer" />
					<form:hidden path="formBean.preparerId" />
					<form:hidden path="formBean.projectVerId" />
					<form:hidden path="formBean.projectType" />
					<form:hidden id="commencementDate" path="formBean.commencementDate" />
					<form:hidden id="orgiEndDate" path="formBean.orgiEndDate" />
					<form:hidden id="projectStep" path="formBean.projectStep" />
					<form:hidden id="projectStatus" path="formBean.projectStatus" />
					<form:hidden id="recType" path="formBean.recType" />
					
					<%@ include file="/WEB-INF/views/project/projectFooter.jsp"%>
				</div>
			</div>
		</form>
	</div>
</div>

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>