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
		$("#selectedRoleId").val(roleId);
		$("#frmDetail").submit();
	}
	
	$(function(){
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
	});
</script>

<!-- Page Content -->
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > Profile > Switch Role
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
			<div class="title pull-left"><i class="fa fa-exchange"></i>Switch Role</div>
		</div>
		<form id="frmDetail" method="POST">
			<div class="panel panel-custom-primary">
				<div class="panel-heading">
					<div class="panel-heading-title">Please select the role:</div>
				</div>
			
				<div class="panel-body">
					<div class="row">
						<div class="form-group">
							<div class="col-sm-12">
								<table>
									<tr>
										<td><b>Current Role - <c:out value="${sessionScope.currentRoleName}"/></b></td>
										<td style="width:150px;text-align:center">
											<div style="width:100%;text-align:center" class="btn btn-success">Set Default Role</div>
										</td>
									</tr>
									<c:forEach var="listItem" items="${formBean.roleList}">
										<tr>
											<td>
												<button type="button" id="btnAddPost" class="btn btn-primary" style="width:200px" onclick='switchRole("<c:out value="${listItem.roleId}"/>")' ><c:out value="${listItem.roleName}"/></button><br/><br/>
											</td>
											<td style="width:150px;text-align:center;vertical-align:top">
												<c:choose>
													<c:when test="${listItem.defaultRole != 'Y'}">
														<input type="radio" name="cbDefaultRole" onclick='setDefaultRoleswitchRole("<c:out value="${listItem.roleId}"/>")' />
													</c:when>
												</c:choose>
												<c:choose>
													<c:when test="${listItem.defaultRole == 'Y'}">
														<input type="radio" name="cbDefaultRole" checked='checked' onclick='setDefaultRoleswitchRole("<c:out value="${listItem.roleId}"/>")' />
													</c:when>
												</c:choose>
											</td>
										</tr>
									</c:forEach>
								</table>
								
								<form:hidden path="formBean.selectedRoleId" id="selectedRoleId"/>
								<form:hidden path="formBean.formAction" id="formAction"/>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>