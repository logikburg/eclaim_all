<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	// Form javascript - Start
	function performReset() {
		document.location.href = "editProfile?id=" + $("#hiddenUserId").val();  
	}

	function performUpdate() {
		$("#divErrorMsg").html('');
		$("#divError").hide();
		$("#divSuccessMsg").html('');
		$("#divSuccess").hide();
	}
	
	$(function(){
		$("#frmDetail").bootstrapValidator({
			excluded: [':disabled'],
			message: ' This value is not valid',
			live: "submitted",
			fields: {
				email : {
					validators : {
						emailAddress: {
							message: 'The value is not a valid email address'
						},
					}
				},
				phone : {
					validators : {
						numeric: {
							message: 'Inputted value is invalid'
						}
					}
				}
			},
		}).on('success.form.bv', function(e){

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
	});
	// Form javascript - End
</script>

<!-- Page Content -->
<div id="page-content-wrapper">

	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > Profile > Edit Profile
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
			<div class="title pull-left"><i class="glyphicon glyphicon-user"></i>Edit Profile</div>
		</div>
		
		<form id="frmDetail" method="POST">
			<div class="panel panel-custom-primary">
				<div class="panel-heading">
					<div class="panel-heading-title">Basic Information</div>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="" class="control-label">User Name<font class="star">*</font></label>
							</div>
							<div class="col-sm-4">
								<form:input path="formBean.userName" id="txtUserName" class="form-control" style="width:100%;" maxlength="100" required="required"/>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
							  	<label for="" class="control-label">Phone No.</label>
							</div>
							<div class="col-sm-4">
								<form:input path="formBean.phone" class="form-control" style="width:100%;" maxlength="20"/>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
							  	<label for="" class="control-label">Email Address<font class="star">*</font></label>
							</div>
							<div class="col-sm-4">
							   	<form:input path="formBean.email" id="txtEmail" class="form-control" style="width:100%;" maxlength="100" required="required"/>
							</div>
						</div>
					</div>
					<form:hidden path="formBean.userId" id="hiddenUserId" />
					<br/>
					<div class="row">
						<div class="col-sm-12" style="text-align:right; width:100%">
							<button type="submit" name="btnSave" class="btn btn-primary" style="width:110px" onclick="performUpdate()"><i class="fa fa-floppy-o"></i> Save</button>
							<button type="button" class="btn btn-default" style="width:110px" onclick="performReset()"><i class="fa fa-undo"></i> Reset</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
	<!-- ./#container -->
</div>
<!-- ./#page-content-wrapper -->

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>