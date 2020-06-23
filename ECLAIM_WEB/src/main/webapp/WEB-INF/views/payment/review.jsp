<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.1.0/chosen.min.css" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.1.0/chosen.jquery.min.js"></script>
<script src="https://oss.maxcdn.com/momentjs/2.8.2/moment.min.js"></script>
<link rel="stylesheet" href="<c:url value="/plugins/bootstrap/datetimepicker/css/bootstrap-datetimepicker.css" />" type="text/css" media="all" />
<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/bootstrap/datetimepicker/bootstrap-datetimepicker.js"%>"></script>

<%@ page import="hk.org.ha.eclaim.model.payment.PaymentEnquirySearchVo"%>
<%@ page import="java.nio.charset.StandardCharsets"%>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>

<%
 PaymentEnquirySearchVo searchVo =  (PaymentEnquirySearchVo)session.getAttribute("searchVo");
 String deptName = "";
 if(searchVo != null){
 	deptName = URLDecoder.decode(searchVo.getDepartmentName(), StandardCharsets.UTF_8.toString());
 }
%>

<style>
.request-body {
	
}

.request-body .panel {
	box-shadow: none;
	border-top: none;
	padding-top: 10px;
}

.request-body .panel-heading {
	border: 2px solid #dddddd;
	background: #f9f9f9;
	border-radius: 0px;
	border-top-left-radius: 10px;
	border-top-right-radius: 10px;
}

.request-body .panel-body {
	border: 2px solid #dddddd;
	margin-top: 10px;
}

.preparer-body .claim .buttons-grp {
	padding-top: 4rem;
}

.request-body .btn-preparer {
	font-size: 16px;
}

.claim {
	
}

.claim [class*="row"] {
	margin-bottom: 0px;
	margin-top: 0px;
}
.claim .form-control {
	width: 70%;
}
.claim [class*="col-"] {
	padding-top: 0.8rem;
	padding-bottom: 0.8rem;
}

.claim [class*="col-text"] {
	padding-top: 0.5rem;
	padding-bottom: 0.5rem;
}

.claim [class*="col-select"] {
	padding-top: 0.7rem;
	padding-bottom: 0.7rem;
}

.chosen-container {
	font-size: 12px;
}

.chosen-container-multi .chosen-choices li.search-choice {
	padding: 1px 20px 1px 3px;
	border: 1px solid #dfdfdf;
	margin-top: 3px;
	margin-bottom: 2px;
	margin-left: 5px;
	background-color: #dfdfdf;
}

.chosen-container-multi .chosen-choices {
	border: 1px solid #dfdfdf;
	background-image: none;
	appearance: textfield;
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
	height: 17px;
}

.chosen-container.chosen-container-multi {
	width: 70%;
}

.chosen-container .chosen-drop {
	border: 1px solid #dfdfdf;
}

.chosen-container-multi .chosen-choices li.search-choice .search-choice-close{
	top: 2px;
}

.chosen-container-active .chosen-choices {
	box-shadow: none;
}

.breadcrumb {
	padding: 15px 15px;
	margin-bottom: 15px
}

.breadcrumb .breadcrumb-item {
	width: 92px;
	color: #2f79b9;
}

.breadcrumb>li i {
	font-size: 48px;
	float: left;
	margin-right: 8px;
	margin-top: -5px;
	vertical-align: middle;
	line-height: 45px;
	display: table-cell;
}

.breadcrumb>li a:focus, a:hover {
	text-decoration: none
}

.breadcrumb>li.active a, .breadcrumb>li.active a:focus {
	text-decoration: none;
	color: #777;
}

.breadcrumb>li.active a:hover {
	text-decoration: none;
	color: #337ab7;
}

.breadcrumb>li+li .arrow {
	font-size: 20px;
	font-style: normal;
	font-weight: bold;
}

.breadcrumb>li+li .arrow:before {
	content: ">>\00a0";
	font-size: 20px;
}

.breadcrumb>li+li:before {
	content: none;
}

.help-block{
	color: #ff0010;
}
</style>

<script>
	$(document).ready(function(e) {
			$('.preparer-body').hide();	//bcz show only requester's panel on first.
			$('.preparerSub-body').hide();
			$('#myRequestTable').dataTable();
			$('.chosen-select').chosen();
			$('.chosen-container.chosen-container-multi').width('70%');
	<%
			String currPage = "";
			currPage = (String)request.getSession().getAttribute("currentView");%>
			var filteredDivs = $(".breadcrumb-item").filter(function() {
			var reg = new RegExp("<%=currPage%>", "i");
				return reg.test($(this).text());
			});
			filteredDivs.removeClass('active');
			
			$('.form_datetime').datetimepicker({
				format : "mm/yyyy",
				startView : 3,
				minView : 3,
				maxView : 3,
				autoclose : true,
				pickDate : false,
				forceParse: false
			})
			.on('changeDate', function(e) {
            	// Revalidate the date field
            	$('#frmNewBatch').bootstrapValidator('updateStatus', 'payMonth', 'NOT_VALIDATED').bootstrapValidator('validateField', 'payMonth');
        	});
			/* .on('changeDate show', function(e) {
        			$('#frmNewBatch').bootstrapValidator('revalidateField', 'payMonth');
        	}); */
 			
 			$('#frmNewBatch').bootstrapValidator({
 				excluded: [':disabled'],
 				fields: {
			            payMonth: {
			                validators: {
			                	notEmpty: {
			                        message: 'Please use valid format i.e. 12/2019.'
			                    },
			                    callback: {
			                        message: 'Please use valid format i.e. 12/2019.',
			                        callback: function (value, validator) {
			                            var m = new moment(value, 'MM/YYYY', true);
			                            if (!m.isValid()) {
			                                return false;
			                            }
			                            return true;
			                        }
			                    }
			                }
			            },
			            departmentName: {
			                validators: {
			                    notEmpty: {
			                        message: 'The department name field is required.'
			                    }
			                }
			            },
			            projectName: {
			                validators: {
			                    notEmpty: {
			                        message: 'The project name field is required.'
			                    }
			                }
			            },
			            jobGroupId: {
			                validators: {
			                    notEmpty: {
			                        message: 'The job field is required.'
			                    }
			                }
			            }
			        }
 			})
 			.on('error.validator.bv', function(e, data) {
            // $(e.target)    --> The field element
            // data.bv        --> The BootstrapValidator instance
            // data.field     --> The field name
            // data.element   --> The field element
            // data.validator --> The current validator name

            data.element
                .data('bv.messages')
                // Hide all the messages
                .find('.help-block[data-bv-for="' + data.field + '"]').hide()
                // Show only message associated with current validator
                .filter('[data-bv-validator="' + data.validator + '"]').show();
        	});
			
			$("#departmentName").add("#searchCriteria #departmentName").autocomplete({
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
			
			$("#projectName").add("#searchCriteria #projectName").autocomplete({
				source: function(request, response) {
					console.log($("#projectName").val());
					console.log($("#departmentId").val()); 
					$.getJSON("<c:url value='/payment/getProjectNameList'/>", { name: $("#projectName").val(), deptId: $("#departmentId").val(),status:$.parseJSON('["DRAFT","APPROVED"]') }, function(result){
						response($.map(result, function(item, key){
							console.log(item);
							return {label: item, value: key}
						}));
					});
				},
				focus: function(event, ui) {
					$(this).val(ui.item.label);
					$("#projectId").val(ui.item.value);
					return false;
				},
				select: function(event, ui) {
					$(this).val(ui.item.label);
					$("#projectId").val(ui.item.value).trigger('change');
					return false;
				}
			});
			
			$("#projectId").change(function(){
				var jobRank = $("#jobGroupId");
				console.log("11");
				$.getJSON("<c:url value='/payment/getJobListByProject' />", {projectId: $("#projectId").val()}, function(result){
					jobRank.empty();
					$.each(result.ranks, function(i, item){
						jobRank.append("<option value='"+i+"'>"+item+"</option>")
					});
					jobRank.trigger("chosen:updated");
					console.log(jobRank.val());
					$("#prjDuration").text(result.prjDuration);
					$("#prgType").text(result.projectType);
					$("#departmentId").val(result.deptId);
					$("#departmentName").val(result.deptName);
				});
			});
			
			$("#searchCriteria #departmentName").val("<%=deptName%>");
	});

	function preparerReq(event) {
		$('.request-body').hide();
		$('.preparer-body').show();
	}
	
	function preparerSubReq(_in) {
		_populateRowData(_in);
		$('.request-body').hide();
		$('.preparerSub-body').show();
	}
	
	function backToEnq(event) {
		$('.request-body').show();
		$('.preparerSub-body').hide();
		$('.preparerSub-body').hide();
	}
	
	function _populateRowData(dIndx){
		console.log('dataIndx' + dIndx);
		var subBatData = {}; 
		var trdata = $("tr[data-index=" + dIndx + "] td");
		trdata.each(function(ee){
			var tisdata = $(this).data();
			for(var key in tisdata){
				//console.log(key + " " + tisdata[key]);
				subBatData[key] = tisdata[key];
			}
		})
		var _row = $("#frmSubBatch .row .col-sm-4");
		_row.eq(0).text(subBatData['projectnmno']);
		//_row.eq(1).text(subBatData['paymonth']);
		_row.eq(2).text(subBatData['hospdept']);
		_row.eq(3).text(subBatData['projectnmno']);
		_row.eq(4).text(subBatData['jobs']);
		$("#frmSubBatch input[name='batchId'").val(subBatData['batchid']);
	}
	
	function performReset(event){
		// clear all the fields of the form page.
    	var searchCriterias = $('#searchCriteria :input');
    	searchCriterias.each(function() {
    		$(this).prop('type') == 'checkbox' ? $(this).prop('checked', true) : "";
    		$(this).prop('type') == 'hidden' ? $(this).prop('value', '') : "";
    		$(this).prop('type') == 'text' ? $(this).prop('value', '') : "";
    	});
	}
	
	function performClear(event){
		// clear all the fields of the form page.
    	var frmNewBatch = $('#frmNewBatch :input');
    	frmNewBatch.each(function() {
    		$(this).prop('type') == 'text' ? $(this).prop('value', '') : "";
    		$(this).prop('type') == 'select-one' ? $(this).prop('value', '') : "";
    		$(this).prop('type') == 'hidden' ? $(this).prop('value', '') : "";
    	});
    	$('#frmNewBatch #prjDuration').text('');
    	$('#frmNewBatch #prgType').text('');
	}
	
	function performCreate(event){

	}
	
	function performUpdate(event){		
		var reqObj = {};
		// get all the inputs into an array.
    	var $inputs = $('#searchCriteria input');
    	$inputs.each(function() {
    		if($(this).prop('type') == 'checkbox'){
    			reqObj[$(this).attr('id')] = $(this).is(':checked'); 
    		}
    		else if($(this).prop('type') == 'text' || $(this).prop('type') == 'hidden')
    		{
				reqObj[$(this).attr('id')] = encodeURI($(this).val());
    		}
    	});
		var inputStr = $.param( reqObj );
        var action = "./search?" + inputStr;
        location.href = action; 
	}
	
	function performCreateSubBatch(ev){   
		var batchId = $('batchId');
		$('<form>', {
   			"action": './createSubBatch',
   			"method": 'POST',
   			"id": 'createSubBatch'
		}).append(batchId).appendTo(document.body).submit();
	}
	
</script>

<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12" id="divMyRequest" style="padding-left: 5px; padding-right: 5px;">

				<div class="panel panel-custom-primary">

					<div class="panel-heading">
						<h3 class="panel-title">
							<i class="glyphicon glyphicon-list-alt"></i> Payment
						</h3>
					</div>

					<div class="panel-body">
						<div class="row">
							<div class="col-md-12">
								<ol class="breadcrumb">
									<li class="breadcrumb-item active" style="width: 125px;"><a href="#"><i class="arrow"></i> <i
											class="glyphicon glyphicon-list-alt"> </i> Validate Payment </a></li>
									<li class="breadcrumb-item active" style="width: 162px;"><a href="#"><i class="arrow"></i> <i
											class="glyphicon glyphicon-new-window"> </i> Transfer to HCM </a></li>
								</ol>
							</div>
						</div>
						<div class="preparer-body">
							<form id="frmNewBatch" action="<c:url value="/payment/createBatch"/>" method="POST">
								<div class="claim panel-body">
									<div class="row">
										<div class="col-sm-2 col-darkgrey">Type</div>
										<div class="col-sm-4 col-darkgrey col-text">
											<select name="paymentType" id ="paymentType" class="form-control">
												<option value='SHS'>SHS</option>
												<option value='LOCUM'>LOCUM</option>
												<option value='TIMESHEET'>TIMESHEET</option>
											</select>
										</div>
										<div class="col-sm-2 col-darkgrey">Pay Month</div>
										<div class="col-sm-4 col-darkgrey col-text">
											<input name="payMonth" class="form-control form_datetime" autocomplete="off" type="text">
										</div>
									</div>
									<div class="row">
										<div class="col-sm-2 col-darkgrey">Hospital / Department:</div>
										<div class="col-sm-4 col-darkgrey col-text">
											<input type="text" id="departmentName" name = "departmentName" class="form-control"
												data-validation="required" data-validation-error-msg="Department is missing.">
											<input type="hidden" id="departmentId" name = "departmentId" class="form-control">
										</div>
										<div class="col-sm-2 col-darkgrey">Project Name/No.:</div>
										<div class="col-sm-4 col-darkgrey col-text">
											<input type="text" id="projectName" name = "projectName" class="form-control" 
											data-validation="required" data-validation-error-msg="Project is missing.">
											<input type="hidden" id="projectId" name = "projectId" class="form-control">
										</div>
									</div>
									<div class="row">
										<div class="col-sm-2 col-darkgrey">Job(s):</div>
										<div class="col-sm-4 col-darkgrey col-text">
											<select style="width: 70%" name="jobGroupId" id ="jobGroupId"  class="form-control chosen-select" multiple>
												<!-- <option value='RN'>Resident</option>
												<option value='EN'>EN</option>
												<option value='AC'>AC</option> -->
											</select>
										</div>
										<div class="col-sm-2 ">Work Location:</div>
										<div class="col-sm-4 col-darkgrey col-text">
											<input type="text" style="width: 70%">
										</div>
									</div>
									<div class="row">
										<div class="col-sm-2 col-darkgrey">Project Duration:</div>
										<div class="col-sm-4 col-darkgrey" id="prjDuration"></div>
										<div class="col-sm-2 ">Program Type:</div>
										<div class="col-sm-4 " id="prgType"></div>
									</div>
									<div class="row">
										<div class="col-sm-12 buttons-grp">
											<button type="submit" class="btn btn-primary">Create</button>
											<button type="button" class="btn btn-primary" onclick="performClear()">Clear</button>
										</div>
									</div>
								</div>
							</form>
						</div>
						<div class="preparerSub-body">
							<form id="frmSubBatch" method="POST" action="<c:url value="/payment/createSubBatch"/>" method="POST">
								<div class="claim panel-body">
									<input id="batchId" name="batchId" autocomplete="off" type="hidden">
									<div class="row">
										<div class="col-sm-2 col-darkgrey">Type</div>
										<div class="col-sm-4 "></div>
										<div class="col-sm-2 col-darkgrey">Pay Month</div>
										<div class="col-sm-4 col-darkgrey col-text">
											<input name="payMonth" class="form-control form_datetime" autocomplete="off" type="text">
										</div>
									</div>
									<div class="row">
										<div class="col-sm-2 col-darkgrey">Hospital / Department:</div>
										<div class="col-sm-4 "></div>
										<div class="col-sm-2 col-darkgrey">Project Name/No.:</div>
										<div class="col-sm-4 "></div>
									</div>
									<div class="row">
										<div class="col-sm-2 col-darkgrey">Job(s):</div>
										<div class="col-sm-4 "></div>
										<div class="col-sm-2 ">Work Location:</div>
										<div class="col-sm-4 "></div>
									</div>
									<div class="row">
										<div class="col-sm-2 col-darkgrey">Project Duration:</div>
										<div class="col-sm-4 col-darkgrey"></div>
										<div class="col-sm-2 ">Project Type:</div>
										<div class="col-sm-4 "></div>
									</div>
									<div class="row">
										<div class="col-sm-12 buttons-grp">
											<button type="submit" class="btn btn-primary">Create</button>
											<button type="button" class="btn btn-primary" onclick="backToEnq()">Back</button>
										</div>
									</div>
								</div>
							</form>
						</div>
						<div class="request-body">
							<!-- <div class="row">
								<div class="col-md-12">
									<button type="button" class="btn btn-primary btn-preparer" onclick="preparerReq()">Prepare Claim
										Request</button>
								</div>
							</div> -->
							<div class="panel">
								<div class="panel-heading">
									<h3 class="panel-title">Request(s) to follow up</h3>
								</div>

								<div class="panel-body">
									<div class="card">
										<div class="card-header" role="tab" id="headingTwo2">
									    	<a class="collapsed" data-toggle="collapse" data-parent="#accordionEx" href="#searchCriteria" aria-expanded="false" aria-controls="searchCriteria">
									         <i class="glyphicon glyphicon-zoom-in"> </i> <b>Search Criteria </b>
									      	</a>
									    </div>
									    <div id="searchCriteria" class="collapse" role="tabpanel" aria-labelledby="headingTwo2" data-parent="#accordionEx">
     										<div class="card-body">
     											<div class="row">
													<div class="form-group">
														<div class="col-sm-3">
															<label for="" class="control-label">Department</label>
														</div>
														<div class="col-sm-3">
															<!-- <input type="text" id="departmentName" name = "departmentName" class="form-control">
															<input type="hidden" id="departmentId" name = "departmentId" class="form-control"> -->
															<input type="text" id="departmentName" name = "departmentName" class="form-control">
															<input type='hidden' name="departmentId" id="departmentId" class="form-control" value=<%= searchVo != null ? (searchVo.getDepartmentId() == null ? "" : searchVo.getDepartmentId()) : "" %> >
														</div>
													</div>	
													<div class="form-group">
														<div class="col-sm-3">
															<label for="" class="control-label">Project Name</label>
														</div>
														<div class="col-sm-3">
															<!-- <input type="text" id="projectName" name = "projectName" class="form-control"> -->
															<input type='text' name="projectName" id="projectName" class="form-control" value=<%= searchVo != null ? (searchVo.getProjectName() == null ? "" : searchVo.getProjectName()) : "" %> >
														</div>
													</div>
												</div>
												<div class="row">
													<div class="form-group">
														<div class="col-sm-3">
															<label for="" class="control-label">Project ID</label>
														</div>
														<div class="col-sm-3">
															<input type='text' name="projectId" id="projectId" class="form-control" value=<%= searchVo != null ? (searchVo.getProjectId() == null ? "" : searchVo.getProjectId()) : "" %> >
														</div>
													</div>	
													<div class="form-group">
														<div class="col-sm-3">
															<label for="" class="control-label">Emp No.</label>
														</div>
														<div class="col-sm-3">
															<%-- <form:input path="formBean.empNo" name="empNo" class="form-control"/> --%>
															<input type='text' name="empNo" id="empNo" class="form-control" value=<%= searchVo != null ? (searchVo.getEmpNo() == null ? "" : searchVo.getEmpNo()) : "" %> >
														</div>
													</div>	
												</div>
												<div class="row">
													<div class="form-group">
														<div class="col-sm-3">
															<label for="" class="control-label">Earned Month</label>
														</div>
														<div class="col-sm-3">
															<%-- <form:input path="formBean.earnedMonth" name="earnedMonth" class="form-control form_datetime"/> --%>
															<input type='text' name="earnedMonth" id="earnedMonth" class="form-control form_datetime" value=<%= searchVo != null ? (searchVo.getEarnedMonth() == null ? "" : searchVo.getEarnedMonth()) : "" %> >
														</div>
													</div>	
													<div class="form-group">
														<div class="col-sm-3">
															<label for="" class="control-label">Pay Month</label>
														</div>
														<div class="col-sm-3">
															<%-- <form:input path="formBean.payMonth" name="payMonth" class="form-control form_datetime"/> --%>
															<input type='text' name="payMonth" id="payMonth" class="form-control form_datetime" value=<%= searchVo != null ? (searchVo.getPayMonth() == null ? "" : searchVo.getPayMonth()) : "" %> >
														</div>
													</div>
												</div>
												<div class="row">
													<div class="form-group">
														<div class="col-sm-3">
															<label for="" class="control-label">Status</label>
														</div>
													</div>
												</div>
												<div class="row">
													<div class="custom-control custom-checkbox">
														<div class="col-sm-3">
													 		<input type="checkbox" class="custom-control-input" id="unProcess" name="unProcess" <%= searchVo != null ? (searchVo.getUnProcess() == false ? "" : "checked") : "checked" %>>
														  	<label class="custom-control-label" for="defaultChecked2">Un-Process</label>
														</div>
													 	<div class="col-sm-3"> 
													  		<input type="checkbox" class="custom-control-input" id="validated" name="validated" <%= searchVo != null ? (searchVo.getValidated() == false ? "" : "checked") : "checked" %>>
													  		<label class="custom-control-label" for="defaultChecked2">Validated</label>
													 	</div>
													 	<div class="col-sm-3">
													 	 	<input type="checkbox" class="custom-control-input" name="partiallyValidated" id="partiallyValidated" <%= searchVo != null ? (searchVo.getPartiallyValidated() == false ? "" : "checked") : "checked" %>>
													  		<label class="custom-control-label" for="defaultChecked2">Partially Validated</label>
													 	</div>
													</div>
												</div>
												<div class="row">
													<div class="custom-control custom-checkbox">
														<div class="col-sm-3">
													 		<input type="checkbox" class="custom-control-input" id="pendingTransfer" name="pendingTransfer" <%= searchVo != null ? (searchVo.getPendingTransfer() == false ? "" : "checked") : "checked" %>>
														  	<label class="custom-control-label" for="defaultChecked2">Pending Transfer</label>
														</div>
													 	<div class="col-sm-3"> 
													  		<input type="checkbox" class="custom-control-input" id="transferred" name="transferred" <%= searchVo != null ? (searchVo.getTransferred() == false ? "" : "checked") : "checked" %>>
													  		<label class="custom-control-label" for="defaultChecked2">Transferred</label>
													 	</div>
													 	<div class="col-sm-3">
													 	 	<input type="checkbox" class="custom-control-input" id="partiallyTransferred" name="partiallyTransferred" <%= searchVo != null ? (searchVo.getPartiallyTransferred() == false ? "" : "checked") : "checked" %>>
													  		<label class="custom-control-label" for="defaultChecked2">Partially Transferred</label>
													 	</div>
													</div>
												</div>
     										</div>
										</div>
									</div>
									
									<div class="row">
										<div class="col-sm-12" style="text-align: left; width: 100%">
											<button type="button" name="Search" class="btn btn-primary"
												style="width: 110px" onclick="performUpdate()">Search</button>
											<button type="button" class="btn btn-primary"
												style="width: 110px" onclick="performReset()">Reset</button>
											<button type="button" class="btn btn-primary" onclick="preparerReq()" style="width: 110px">New Batch</button>	
										</div>
									</div>
								 	<div class="row">
									<%-- 	<div class="col-sm-12 buttons-grp">		
											<button type="button" class="btn btn-primary" onclick="preparerReq()">New Batch</button>
											<button type="button" class="btn btn-primary" onclick="preparerSubReq()">New Sub Batch</button>
											<a class="btn btn-primary" href="<c:url value="/payment/review?claimId=CM123456"/>" role="button">Edit</a>
										</div> --%>
									</div> 
									
									<div class="table-responsive" style="padding: 3px">
										<table id="myRequestTable" class="table table-striped table-bordered">
											<thead>
												<tr>
													<th style="width: 8%">Batch ID</th>
													<th style="width: 10%">Sub Batch ID</th>
													<th style="width: 15%">Project Name/ No.</th>
													<th style="width: 15%">Hospital / Dept</th>
													<th style="width: 10%">Job(s)</th>
													<th style="width: 10%">Pay month</th>
													<th style="width: 15%">Last update date</th>
													<th style="width: 10%">Status</th>
													<th style="width: 5%">Total</th>
													<th style="width: 5%">Transfered</th>
													<th style="width: 5%">Outstanding</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach var="listValue" items="${formBean.paymentBatchList}">
													<tr data-index="${listValue.batchId}">
														<c:if test="${empty listValue.parentBatchId}">
															<td data-batchId="${listValue.batchId}"><a href='<c:url value="/payment/review"> <c:param name="claimId" value="${listValue.batchId}"/> </c:url>'><c:out value="${listValue.batchId}" /></a></td>
														</c:if>
														<c:if test="${not empty listValue.parentBatchId}">
															<td data-batchId="${listValue.batchId}"><a href='<c:url value="/payment/review"> <c:param name="claimId" value="${listValue.parentBatchId}"/> </c:url>'><c:out value="${listValue.batchId}" /></a></td>
														</c:if>
														
														<%-- <td><c:out value="${listValue.batchId}" /></td> --%>
														<td data-parentBatchId="${listValue.parentBatchId}"><c:out value="${listValue.parentBatchId}" /></td>
														<td data-projectNmNo="${listValue.projectNmNo}"><c:out value="${listValue.projectNmNo}" /></td>
														<td data-hospDept="${listValue.hospDept}"><c:out value="${listValue.hospDept}" /></td>
														<td data-jobs="${listValue.jobs}"><c:out value="${listValue.jobs}" /></td>
														<td data-payMonth="${listValue.payMonth}"><c:out value="${listValue.payMonth}" /></td>
														<td data-lastUpdateDate="${listValue.lastUpdateDate}"><c:out value="${listValue.lastUpdateDate}" /></td>
														<td data-status="${listValue.status}"><c:out value="${listValue.status}" /></td>
														<td data-totalSum="${listValue.totalSum}"><c:out value="${listValue.totalSum}" /></td>
														<td data-transferredSum="${listValue.transferredSum}"><c:out value="${listValue.transferredSum}" /></td>
														<td data-subBatch="${listValue.subBatchButton}">
														<c:choose>
														    <c:when test="${listValue.onHoldSum gt 0}">
														        <a href="#" data-toggle="modal" data-batch-id="${listValue.batchId}" data-target="#outstandingModal" style="text-decoration: underline;"><c:out value="${listValue.onHoldSum}" /></a>
														    </c:when>    
														    <c:otherwise>
														        <c:out value="${listValue.onHoldSum}" />
														    </c:otherwise>
														</c:choose>
															<c:if test="${listValue.subBatchButton}"><button type="button" class="btn btn-primary" style="width:110px;" onclick="preparerSubReq(${listValue.batchId})">New Sub Batch</button></c:if>
														</td>
														<!-- <td> --><%-- <c:out value="${listValue.remark}" /> --%><!-- <a href="#" data-toggle="modal" data-target="#errorModel">Remark</a></td> -->
													</tr>
												</c:forEach>
											</tbody>
										</table>
										<!-- ./myRequestTable -->
									</div>
									<!-- ./panel-body -->
								</div>
							</div>

							<!-- ./request-body -->
						</div>
					</div>
				</div>
				<!-- ./panel-body -->
			</div>
			<!-- ./panel -->
		</div>
	</div>
	<!-- ./col-md-4 -->
</div>
<!-- ./row -->
<%@ include file="/WEB-INF/views/core/commonPopups.jsp"%>
<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>