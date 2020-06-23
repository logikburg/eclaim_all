<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	function switchRole(roleId) {
		$("#divSuccess").hide();
		$("#divSuccessMsg").html('');
		$("#divError").hide();
		$("#divErrorMsg").html('');
		showLoading();
		// Perform checking of role
		$("#selectedRoleId").val(roleId);
		$("#frmDetail").submit();
	}
	
	function setDefaultRoleswitchRole(roleId) {
		$("#divSuccess").hide();
		$("#divSuccessMsg").html('');
		$("#divError").hide();
		$("#divErrorMsg").html('');
		showLoading();
		// Perform checking of role
		$("#formAction").val("setDefault");
// 		$("#selectedRoleId").val($("#defaultRole").val());
		$("#frmDetail").submit();
	}
	
	$(function(){
		$("#wrapper").toggleClass("active");
		
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
	});
</script>

<!-- Page Content -->
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > Profile > User Setup
		</div>
		<div id="divSuccess" style="display:none" class="alert-box-success">
			<div class="alert-box-icon-success"><i class="fa fa-check"></i></div>
			<div id="divSuccessMsg" class="alert-box-content-success"></div>
		</div>
		<div id="divError" style="display:none" class="alert-box-danger">
			<div class="alert-box-icon-danger"><i class="fa fa-warning"></i></div>
			<div id="divErrorMsg" class="alert-box-content-danger"></div>
		</div>
		
		<!-- Function Name -->
		<div class="section-title">
			<div class="title pull-left"><i class="fa fa-exchange"></i>User Setup</div>
		</div>
		<form id="frmDetail" method="POST">
			<div class="panel panel-custom-primary">
				<div class="panel-body">
					<form:hidden path="formBean.formAction" id="formAction" />
					<div class="row">
						<div class="col-sm-2">
							<label for="" class="defaultRole">Default User Role:</label>
						</div>
						<div class="col-sm-2">
							<form:select path="formBean.selectedRoleId" name="defaultRole"
								class="form-control">
								<form:options items="${formBean.roleList}" itemValue="roleId"
									itemLabel="roleId" />
							</form:select>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<label for="" class="defaultRole">Hamburger menu:</label>
						</div>
						<div class="col-sm-2">
							<form:select path="formBean.showHamburger"
								name="showHamburgerMenu" class="form-control">
								<form:option value="Y">Show</form:option>
								<form:option value="N">Hide</form:option>
							</form:select>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<label for="" class="defaultRole">Default Department:</label>
						</div>
						<div class="col-sm-6">
							<form:select path="formBean.defaultDeptId"
								name="defaultDeptId" class="form-control">
								<form:options items="${deptList}" /> 
							</form:select>
						</div>
					</div>
					<div class="row">
					<div class="col-sm-1">
					<button type="button" id="btnAddPost" class="btn btn-primary"
												style="width: 100px" onclick="setDefaultRoleswitchRole(4)">Save</button>
					</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>