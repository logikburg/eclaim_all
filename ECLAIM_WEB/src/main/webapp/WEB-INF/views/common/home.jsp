<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>

	String.prototype.format = function() {
	  var str = this;
	  for (var i = 0; i < arguments.length; i++) {       
	    var reg = new RegExp("\\{" + i + "\\}", "gm");             
	    str = str.replace(reg, arguments[i]);
	  }
	  return str;
	}
	
	$(function(){
		$("#projectTable").dataTable();
		$("#myTeamRequestTable").dataTable();
		$("#recentRequestTable").dataTable();
		$("#wrapper").toggleClass("active");
		
		$("#txtProjectOwner").autocomplete({
			source: function(request, response) {
				$.getJSON("<c:url value='/project/getUserNameList'/>", request, function(result){
					response($.map(result, function(item, key){
						return {label: item, value: key}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#txtProjectOwnerId").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#txtProjectOwnerId").val(ui.item.value);
				return false;
			},
		});
		
		$("#txtProjectPreparer").autocomplete({
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
				$("#txtProjectPreparerId").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#txtProjectPreparerId").val(ui.item.value);
				return false;
			},
		});
		
		$("#txtProjectName").autocomplete({
			source: function(request, response) {
				$.getJSON("<c:url value='/project/getProjectNameList'/>", { name: $("#txtProjectName").val(), deptId: null,status:null }, function(result){
					response($.map(result, function(item, key){
						console.log(item);
						return {label: item, value: key}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#txtProjectId").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#txtProjectId").val(ui.item.value).trigger('change');
				return false;
			}
		});
	});
	
	$(document).on('click', '.panel-heading span.clickable', function(e){
	    var $this = $(this);
		if(!$this.hasClass('panel-collapsed')) {
			$this.parents('.panel').find('.panel-body').slideUp();
			$this.addClass('panel-collapsed');
			$this.find('i').removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
		} else {
			$this.parents('.panel').find('.panel-body').slideDown();
			$this.removeClass('panel-collapsed');
			$this.find('i').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
		}
	});
	
	$(document).ready(function(e) {
		$('.table-responsive').find('.row').first().children().first().attr("class", "col-md-3 col-sm-4");
		$('.table-responsive').find('.row').first().children().last().attr("class", "col-md-3 col-sm-4");
		var $actRow = $('.table-responsive').find('.row').first();
		$actRow = $actRow.prepend('<div class="col-md-4 col-sm-4"><label">Actions: </label></div>');
		var $actsF = $actRow.find('div').first().children().first().append('<select id="actionSelection" class="form-control"><option value="" selected></option><option value="COPY">Copy</option><option value="PRINT">Print</option><option value="UPDATE">Update</option><option value="EXTEND">Extend</option><option value="INVITATION">New Invitation</option></select>');
		$actsF.append('<button type="button" name="actionBtn" onclick="actionBtnOnClick()" class="btn btn-primary" style="width: 50px">Go</button>');
	});
	
	function actionBtnOnClick(){
		var action = $("#actionSelection").val();
		var selectedProject = $("#projectTable").find("input[type='radio']:checked");
		if(action == null || action == ""){
			alert("Please select actions.");
			return;
		}
		if(selectedProject.length == 0){
			alert("Please select project record first.");
			return;
		}
		var row = $(selectedProject[0]).parents('.projectRow');
		ConfirmDialog(row);
	}
	
	function precessAction(row){
		if($("#actionSelection").val() == 'INVITATION'){
			var _projectId = $(row).find('.projectId').text();
			var inputPrjId = $("<input>")
	               .attr("type", "hidden")
	               .attr("id", "projectId")
	               .attr("name", "projectId").val(_projectId);
	
			$('<form>', {
	   			"action": '../invitation',
	   			"method": 'POST',
	   			"id": 'invitation'
			}).append(inputPrjId).appendTo(document.body).submit();

		} else {
			$.getJSON("<c:url value='/project/doProjectAction'/>", {projectId: $(row).find('.projectId').text(), verId: $(row).find('.verId').text(), action: $("#actionSelection").val() }, function(result){
				if(result == 0){
					alert("Success!");
				}else if(result == 2){
					alert("Error! Only HR Officer can process the aciton.");
				}else{
					alert("Error! can't process the aciton.");
				}
			});
		}
	};
	
	function ConfirmDialog(row) {
		$('#model_confirmAction label').text("Are you sure to {0} the {1} (Ref:{2})?".format($("#actionSelection option:selected").text(), $(row).find('td').eq(2).text(), $(row).find('td:nth-child(2) a').text()));
		$('#model_confirmAction .yesBtn').off('click');
		$("#model_confirmAction .yesBtn").click(function(){
			precessAction(row);
	    }); 
		$("#model_confirmAction").modal({
			show: true,
		});
	};
</script>

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
		margin-top: 3px;
		margin-bottom: 2px;
		margin-left: 5px;
		background-color: #dfdfdf;
	}
	
	.chosen-container-multi .chosen-choices {
		border: 1px solid #cccccc;
		background-image: none;
		appearance: textfield;
		border-radius: 4px;
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
							<i class="glyphicon glyphicon-list-alt"></i> Project
						</h3>
					</div>

					<div class="panel-body">
						<!-- Search Project -->
						<form id="frmDetail" method="POST" action='<c:url value="/home/searchProject"></c:url>'>
							<div class="panel-heading">
								<div class="page-title">Search Project</div>
							</div>
							<div class="panel-body">
								<div class="row">
									<div class="form-group">
										<div class="col-sm-2">
											<label for="" class="control-label">Status</label>
										</div>
										<div class="col-sm-3">
											<form:select path="formBean.statusCode" class="form-control">
												<form:options items="${statusList}" itemLabel="statusDesc"
													itemValue="statusCode" />
											</form:select>
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-3">
											<label for="" class="control-label">Project Name</label>
										</div>
										<div class="col-sm-3">
											<input type='text' id="txtProjectName" name="projectName" class="form-control" />
											<input type='hidden' id="txtProjectId" name="txtProjectId" class="form-control" />
										</div>
									</div>
								</div>
								<div class="row">
									<div class="form-group">
										<div class="col-sm-2">
											<label for="" class="control-label">Project Owner</label>
										</div>
										<div class="col-sm-3">
											<input type='text' id="txtProjectOwner" name="txtProjectOwner"	class="form-control" />
											<input type="hidden" id="txtProjectOwnerId" name="ownerId" />	
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-3">
											<label for="" class="control-label">Project Preparer</label>
										</div>
										<div class="col-sm-3">
											<input type='text' id="txtProjectPreparer" name="txtProjectPreparer"
												class="form-control" />
											<input type="hidden" id="txtProjectPreparerId" name="preparerId" />	
										</div>
									</div>
								</div>
								<div class="row">
									<div class="form-group">
										<div class="col-sm-2">
											<label for="" class="control-label">Project No.</label>
										</div>
										<div class="col-sm-3">
											<input type='text' name="projectId" class="form-control" />
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-3">
											<label for="" class="control-label">Include Pass SHS Project</label>
										</div>
										<div class="col-sm-2">
											<select class="form-control">
												<option value="Y">Yes</option>
												<option value="N" selected>No</option>
											</select>
										</div>
									</div>
								</div>
								<br />
								<div class="row">
									<div class="col-sm-12" style="text-align: left; width: 100%">
										<button type="submit" name="Search" class="btn btn-primary"
											style="width: 110px">Search</button>
										<button type="button" class="btn btn-primary"
											style="width: 110px" onclick="performReset()">Reset</button>
										<a class="btn btn-primary" style="width: 110px" href="<c:url value="/project/newProject"></c:url>">
												New Project</a>
									</div>
								</div>
							</div>
						</form>
						<div class="table-responsive" style="padding: 3px">
							<table id="projectTable"
								class="table table-striped table-bordered">
								<thead>
									<tr>
										<th style="width: 10%"></th>
										<th style="width: 15%">Project No.</th>
										<th style="width: 15%">Project Name</th>
										<th style="width: 20%">Project Owner</th>
										<th style="width: 30%">Department</th>
										<th style="width: 20%">Status</th>
										<th style="width: 20%">Extension</th>
<!-- 										<th style="width: 20%">No. of Invitation</th> -->
										<th style="width: 20%">Application Status(O/S)</th>
<!-- 										<th style="width: 20%">Total Application</th> -->
<!-- 										<th style="width: 20%">O/S Application</th> -->
										<th style="width: 20%">Approval Hours</th>
										<th style="width: 20%">Hours</th>
										<th style="width: 20%">Available Hours</th>
										<th style="width: 20%">Project Start Date</th>
										<th style="width: 20%">Project End Date</th>
										<th class="hide">Project Id</th>
										<th class="hide">Project Ver Id</th>
									</tr>
								</thead>
								<tbody>
								<tr class="projectRow">
									<td><input type='radio' name="selectedRb" class="form-control" /></td>
									<td><a href='/project/enterProject?projectId=1&verId=1'>SHS10076</a></td>
									<td>Special honorarium in PWH</td>
									<td>Wong Ling</td>
									<td>PWH/Clin Svc/Neurosurgery</td>
									<td>Approved</td>
									<td>0</td>
									<td>0</td>
									<td>n/a</td>
									<td>n/a</td>
									<td>2,100</td>
									<td><fmt:formatDate value="${now}" pattern="dd/MM/yyyy" /></td>
									<td><fmt:formatDate value="${now}" pattern="dd/MM/yyyy" /></td>
									<td class="hide projectId"><c:out value="1" /></td>
									<td class="hide verId"><c:out value="1" /></td>
								</tr>
									<c:forEach var="listValue" items="${formBean.myProjectList}">
										<tr class="projectRow">
<%-- 											<td><span style="display: none"><fmt:formatDate --%>
<%-- 														value="${listValue.updatedDate}" pattern="yyyyMMddHHmmss" /></span><span><fmt:formatDate --%>
<%-- 														value="${listValue.updatedDate}" pattern="dd/MM/yyyy" /></span></td> --%>
<!-- 											<td></td>			 -->
											<td><input type='radio' name="selectedRb" class="form-control" /></td>
											<td><a href='<c:url value="/project/enterProject"> 
												<c:param name="projectId" value="${listValue.projectId}"/> 
												<c:param name="verId" value="${listValue.projectVerId}"/>
												</c:url>'><c:out value="${listValue.projectId}" /></a>
											</td>
											<td><c:out value="${listValue.projectName}" /></td>
											<td><c:out value="${listValue.projectOwner}" /></td>
											<td><c:out value="${listValue.departmentName}" /></td>
											<td><c:out value="${listValue.projectStatus}" /></td>
											<td><c:out value="${listValue.extension}" /></td>
<%-- 											<td><c:out value="${listValue.invitation}" /></td> --%>
<%-- 											<td><c:out value="${listValue.totAppl}" /></td> --%>
											<td><c:out value="${listValue.osAppl}" /></td>
											<td><c:out value="${listValue.apprWorkHour}" /></td>
											<td><c:out value="${listValue.usedWorkHour}" /></td>
											<td><c:out value="${listValue.avalWorkHour}" /></td>
											<td><fmt:formatDate value="${listValue.startDate}" pattern="dd/MM/yyyy" /></td>
											<td><fmt:formatDate value="${listValue.endDate}" pattern="dd/MM/yyyy" /></td>
											<td class="hide projectId"><c:out value="${listValue.projectId}" /></td>
											<td class="hide verId"><c:out value="${listValue.projectVerId}" /></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							<!-- ./projectTable -->
						</div>
					</div>
					<!-- ./panel-body -->
				</div>
				<!-- ./panel -->
			</div>
			<!-- ./col-md-4 -->
		</div>
		<!-- ./row -->
	</div>
	<!-- ./container-fluid -->
</div>
<!-- ./Page Content -->

<!-- model_confirmAction -->
<div id="model_confirmAction" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:750px">
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Confirm</b></h4>
			</div>
			<div class="modal-body">
				<div class="row" style="padding:20px">
					<div class="col-sm-12">
						<label for="" class="field_request_label">Are you sure to ?</label>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary yesBtn" data-dismiss="modal" style="width:110px">
					<i class="fa fa-check"></i> Yes</button>
				<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal">
					<i class="fa fa-times"></i> Cancel</button>
			</div>
		</div>
	</div>
</div>
<!-- ./#model_confirmAction -->
<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>