<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.js"></script>
<script>
	
	function performUpdate() {
		$("#divErrorMsg").html('');
		$("#divError").hide();
		$("#divSuccessMsg").html('');
		$("#divSuccess").hide();
	}
	
	$(document).ready(function(){
		$('.form_datetime').each(function(){
			var value = $(this).val();
// 			$(this).val("2018-1-1 14:20");
// 			$(this).datetimepicker("update");
// 			$(this).datetimepicker("update", "2018-01-01 14:25");
		});
		console.log($('#check7x24').prop("checked"));
		$("tr.scheduleRow button, tr.scheduleRow input, tr.scheduleRow select").prop("disabled", $('#check7x24').prop("checked"));
	});
		
	$(function() {
		$('.form_datetime').datetimepicker.Constructor.prototype.getDate = function() {
			var d = this.getUTCDate();
			if (d === undefined) {
				return null;
			}
			this.date;
		};

		$('.form_datetime').datetimepicker({
			format : "hh:ii",
			startView : 1,
			minView : 0,
			maxView : 1,
			autoclose : true,
			minuteStep : 15,
			pickDate : false
		});

		$('.datetimepicker').wrap("<div class='timedate-picker'>");
		
		$('#check7x24').change(function(e){
			$("tr.scheduleRow button, tr.scheduleRow input, tr.scheduleRow select").prop("disabled", this.checked);
// 			$("tr.scheduleRow input, tr.scheduleRow select").prop("readonly", this.checked);
		});
		
		$("#frmDetail").on("change","input[type='radio']",function(e){
			$('#frmDetail').bootstrapValidator('enableFieldValidators','other', $(this).val() == "OTH" ? true : false);
			$('#frmDetail').bootstrapValidator('validateField', 'other');
		});
		
// 		$("#wrapper").toggleClass("active");
		
		$("#frmDetail")
				.bootstrapValidator(
						{
							excluded : [ ':disabled', ':hidden' ],
							message : 'This value is not valid',
							live : "submitted",
							fields : {
								startDate: {
					                validators: {
					                	notEmpty : {
											message : 'The Start Date is required and cannot be empty'
										},
					                    date: {
					                        format: 'DD/MM/YYYY',
					                        message: 'The start date is not a valid date'
					                    },
					                    callback: {
					                        message: 'The start date cant after than end date',
					                        callback: function(value, validator) {
					                            var endDate = $('#endDate');
					                            if(endDate.val() === ''){
					                            	return true;
					                            }
					                            
					                            var m = new moment(value, 'DD/MM/YYYY', true);
					                            var e = new moment(endDate.val(), 'DD/MM/YYYY', true);
					                            return m.isBefore(e);
					                        }
					                    }
					                }
					            },
								other : {
									enabled : false,
									message : 'The other is not valid',
									validators : {
										notEmpty : {
											message : 'The other is required and cannot be empty'
										},
										stringLength : {
											max : 4000,
											message : 'The other must less than 4000 characters long'
										}
									}
								}
							}
						})
				.on('success.form.bv', function(e) {
// 					 e.preventDefault();
				})
				// Add button click handler
				.on('click', '.addButton', function() {
					var i = $('tr.scheduleRow').size();
		            var $template = $('#templateRow'),
		                $clone    = $template
		                                .clone()
		                                .removeClass('hide')
		                                .addClass('scheduleRow')
		                                .removeAttr('id')
		                                .insertBefore($template);
						
						$clone.find(".form-control").each(function() {
						    $(this).attr({
						      'name': function(_, name) { return 'scheduleList['+i+'].'+ name},
						    });
						  });
						
						$clone.find(".form_datetime").each(function() {
						    $(this).datetimepicker({
								format: "hh:ii",
								startView: 1,  
								minView: 0,
								maxView: 1,
								autoclose: true,
								minuteStep: 15
							});
						});
						
		            	$('#frmDetail').bootstrapValidator('addField', 'scheduleList['+ i +'].patternCode', {
		    	            validators: {
		    	                notEmpty: {
		    	                    message: 'this value cant be empty'
		    	                }
		    	            }
		    	        });
		            	$('#frmDetail').bootstrapValidator('addField', 'scheduleList['+ i +'].startTime', {
		    	            validators: {
		    	                notEmpty: {
		    	                    message: 'this value cant be empty'
		    	                }
		    	            }
		    	        });
		            	$('#frmDetail').bootstrapValidator('addField', 'scheduleList['+ i +'].endTime', {
		    	            validators: {
		    	                notEmpty: {
		    	                    message: 'this value cant be empty'
		    	                }
		    	            }
		    	        });
		        })
		     	// Remove button click handler
		        .on('click', '.removeButton', function() {
		            var $row    = $(this).parents('.scheduleRow'),
		            	$status   = $row.find('[name$="status"]');
		            	
		            // Remove element containing the option
		            $row.hide();

		            // Set Row Status as Delete
		            $status.val("DELETE");
		        });
	});
</script>

<style type="text/css">
    .popover{
        max-width:100%;
    }
     .date{ 
     	width:130px;  
     }
     .timedate-picker {
	
	}
	
	.timedate-picker table.table-condensed {
		margin-top: -28px;
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

		<form id="frmDetail" method="POST" autocomplete="off">
			<div class="panel panel-custom-primary">

				<div class="panel-body">
				
					<div class="row">
						<div class="col-sm-12">
							<label>Proposed Project Duration<font class="star">*</font></label>
						</div>
				    </div>
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<label for="txtStartDate" class="col-sm-1 control-label">From </label>
								<div class='input-group date' id='datetimepicker1'>
									<fmt:parseDate pattern="yyyy-dd-MM" value="${formBean.startDate}" var="parsedStDate" />
									<fmt:formatDate var="startDate" value="${parsedStDate}" pattern="dd/MM/yyyy" />
									<form:input path="formBean.startDate" value="${startDate}"
									            id="startDate" name="startDate" 
									            class="form-control" required="required" /> 
									<span class="input-group-addon">
										<span class="glyphicon glyphicon-calendar"></span> 
 									</span>	 
								</div>  
							</div>
						</div>
						<div class="col-sm">
							<div class="form-group">
									<label for="txtStartDate" class="col-sm-1 control-label">To </label> 
									<div class='input-group date' id='datetimepicker2'>
										<fmt:parseDate pattern="yyyy-dd-MM" value="${formBean.endDate}" var="parsedEdDate" />
										<fmt:formatDate var="endDate" value="${parsedEdDate}" pattern="dd/MM/yyyy" />
										<form:input path="formBean.endDate" value="${endDate}"
										            id="endDate" name="endDate" 
										            class="form-control" required="required" />
										<span class="input-group-addon">
											<span class="glyphicon glyphicon-calendar"></span>
										</span>	 
									</div> 
							</div>
						</div>
					</div>	
					<br>
					<label>Proposed Working Schedule<font class="star">*</font></label>
					<br>
					<div class="panel panel-default" style="width:600px;">
  						<div class="panel-body">
							<div class="col-sm">
								<div class="form-group">
									<div class="checkbox">
									 	<label><form:checkbox path="formBean.is7x24" id="check7x24"/>
										Please tick this box if SHS could be scheduled 7x24 </lable>
									</div>
								</div>
									<table id="myRequestTable" class="table table-striped table-bordered" style="width:550px;">
										<thead>
											<tr>
												<th style="width: 20%;"></th>
												<th style="width: 30%;">Session Pattern</th>
												<th style="width: 25%;">Start Time</th>
												<th style="width: 25%;">End Time</th>
											</tr>
										</thead>
										<tbody>
										<c:forEach items="${formBean.scheduleList}" var="vo" varStatus="status">
											<tr class="scheduleRow">
												<td>
													<div class="btn-group">
														<button name="newRow" type="button" class="btn btn-default addButton"><i class="fa fa-plus-circle fa-2x"></i></button>
														<button name="deleteRow" type="button" class="btn btn-default removeButton" ><i class="fa fa-times-circle fa-2x"></i></button>
													</div>
												</td>
												<td class="form-group">
													<form:select path="formBean.scheduleList[${status.index}].patternCode" class="form-control">
<%-- 													<select name='scheduleList[${status.index}].patternCode' value="${vo.patternCode}" class='form-control'> --%>
															<form:options items="${patternList}" itemLabel="patternDesc" itemValue="patternCode" /> 
<!-- 														<option value=''> - Select - </option> -->
<%-- 														<option value='wkday' <c:if test="${vo.patternCode == 'wkday'}"> selected</c:if> > Weekday </option> --%>
<%-- 														<option value='wkend' <c:if test="${vo.patternCode == 'wkend'}"> selected</c:if> > Weekend </option> --%>
													</form:select> 
												</td>
												<td class="form-group"><input name="scheduleList[${status.index}].startTime" data-date-format="hh:ii" class='form-control form_datetime' type="text" value="${vo.startTime}"/></td>
												<td class="form-group"><input name="scheduleList[${status.index}].endTime" data-date-format="hh:ii" class='form-control form_datetime' type="text" value="${vo.startTime}"/></td>
												<td class="hide"><input name="scheduleList[${status.index}].status" value="${vo.status}" class='form-control' type="text"/></td>
												<td class="hide"><input name="scheduleList[${status.index}].projectId" value="${vo.projectId}" class='form-control ' type="text"/></td>
												<td class="hide"><input name="scheduleList[${status.index}].projectVerId" value="${vo.projectVerId}" class='form-control' type="text"/></td>
												<td class="hide"><input name="scheduleList[${status.index}].scheduleId" value="${vo.scheduleId}" class='form-control' type="text"/></td>
											</tr>
										</c:forEach>
											
											<tr class="hide" id="templateRow">
												<td>
													<div class="btn-group">
														<button name="newRow" type="button" class="btn btn-default addButton"><i class="fa fa-plus-circle fa-2x"></i></button>
														<button name="deleteRow" type="button" class="btn btn-default removeButton" ><i class="fa fa-times-circle fa-2x"></i></button>
													</div>
												</td>
												
												<td class="form-group">
														<select name='patternCode' class='form-control'/>
														<c:forEach items="${patternList}" var="pattern"> --%>
															<option value='${pattern.patternCode}'> ${pattern.patternDesc} </option>
														</c:forEach>
														</select>
												</td>
												
												<td class="form-group">
													<input name="startTime" class="form-control form_datetime" autocomplete="off" type="text">
												</td>
												
												<td class="form-group">													
													<input name="startTime" class="form-control form_datetime" autocomplete="off" type="text">
												</td>
												
												<td class="hide"><input name="status" value="new" class='form-control' type="text"/></td>
											</tr>
										
										</tbody>
									</table>
							</div>
							<div class="col-sm">
								<div>Note</div>
							</div>
							<div class="col-sm">
								<div>Special approval is required for hour/session less than 4</div>
							</div>
							<div class="col-sm">
								<div>Actual SHS schedule could be different from but must be bound by above sessions</div>
							</div>
						</div>
					</div>
					<br>
					<div class="col-sm">
						<label for="other">The attendance of SHS sessions will be checked/ monitored by: <i>(please indicate from the below)</i></label>
						<div class="form-group">
				            <div class="radio"><label><form:radiobutton path="formBean.attendance" value = "MAN" id="manualRb"/> Manual records (e.g. attendance log book)</label></div>
				            <div class="radio"><label><form:radiobutton path="formBean.attendance" value = "SYS" id="systemRb"/> System records (e.g. system output)</label></div>
				           	<div class="radio"><label><form:radiobutton path="formBean.attendance" value = "OTH" id="otherRb"/> Others: <i>(please specify)</i></label></div>
						</div>
					</div>
					
					<div class="col-sm">
						<div class="form-group">
							<form:textarea path="formBean.other" rows="4" type="text"
								id="other" required="required" class="form-control"></form:textarea>
						</div>
					</div>
					<br>
					<form:hidden id="projectStep" path="formBean.projectStep" />
					
					<div class="col-sm-12">
						<div class="pull-right">
							<button name="saveBtn" type="submit" class="btn-lg btn-primary"
								onclick="performUpdate()">Next</button>
							<button name="cancelBtn" type="button" class="btn-lg btn-primary">Cancel</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

<div id="popover_content_wrapper" style="display: none"></div>
<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>