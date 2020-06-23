<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	function performUpdate() {
		$("#divSuccessMessage").hide();
		$("#divErrorMessage").hide();
		
		if (formValidate()) {
			$("#frmDetail").submit();
		}
	}
	
	function formValidate() {
		if ($("#newsDesc").val() == "") {
			$("#errMsg").html("<b>News Description</b> cannot be empty.");
			$("#divErrorMessage").show();
			return false;
		}
		
		return true;
	}

	function returnMain() {
		document.location.href = '<c:url value="/maintenance/newsMaintenance"/>';
	}
</script>
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > Maintenance > <a href="<c:url value="/maintenance/newsMaintenance"/>">News Maintenance</a> > Detail
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
			<div class="title pull-left"><i class="fa fa-bullhorn"></i>News Maintenance Detail</div>
		</div>
		<div class="panel panel-custom-warning">
			<div class="panel-heading">
				<div class="panel-heading-title">News and Announcement Details</div>
			</div>
			<div class="panel-body">
				<form id="frmDetail" method="POST">
					<div class="row">
						<div class="col-sm-2">
							<label class="field_request_label">News Description<font class="star">*</font>:</label>
						</div>
						<div class="col-sm-8">
							<form:textarea path="formBean.newsDesc" style="width:100%" class="form-control" id="newsDesc" name="newsDesc"/>		
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<label class="field_request_label">Effective From:</label>
						</div>
						<div class="col-sm-2">
							<div class="input-group date">
								<form:input path="formBean.effectiveFrom" class="form-control"/>
								<span class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span>
								</span>	
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<label class="field_request_label">Effective To:</label>
						</div>
						<div class="col-sm-2">
							<div class="input-group date">
								<form:input path="formBean.effectiveTo" class="form-control"/>
								<span class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span>
								</span>	
							</div>
						</div>
					</div>
				
					<form:hidden path="formBean.hiddenNewsUid"/>
					<form:hidden path="formBean.lastUpdatedDate"/>
					<br/>
					<div class="row">
						<div class="col-sm-6">
							<button type="button" class="btn btn-primary" style="width:110px" onclick="performUpdate()"><i class="fa fa-floppy-o"></i> Save</button>
							<button type="button" class="btn btn-default" style="width:110px" onclick="returnMain()"><i class="fa fa-undo"></i> Cancel</button>
						</div>
					</div>
				</form>
			</div>
			
		</div>
		
	</div>
	<!-- ./#container -->
</div>
<!-- ./#page-content-wrapper -->

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>