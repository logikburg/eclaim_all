<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.1.0/chosen.min.css" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.1.0/chosen.jquery.min.js"></script>

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
	margin-top: 2px;
	margin-bottom: 2px;
	margin-left: 5px;
	background-color: #dfdfdf;
}

.chosen-container-multi .chosen-choices {
	border: 1px solid #dfdfdf;
	background-image: none;
	-webkit-appearance: textfield;
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
</style>

<script>
	$(document).ready(function(e) {
			$('.preparer-body').hide();	//bcz show only requester's panel on first.
			$('#myRequestTable').dataTable();
			$('.chosen-select').chosen();
			$('.chosen-container.chosen-container-multi').width('70%');
	<%		
			String requestURI = (String) request.getAttribute("javax.servlet.forward.request_uri");
			String[] el = requestURI.split("/");
			String currPage = el.length > 0 ? el[el.length - 1] : "";
			currPage = currPage.replace(".jsp", "").replaceAll("(?i)claim", "");%>
			var filteredDivs = $(".breadcrumb-item").filter(function() {
			var reg = new RegExp("<%=currPage%>", "i");
					return reg.test($(this).text());
			});
			filteredDivs.removeClass('active');
	});

	function preparerReq(event) {
		$('.request-body').hide();
		$('.preparer-body').show();
	}
</script>

<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12" id="divMyRequest" style="padding-left: 5px; padding-right: 5px;">

				<div class="panel panel-custom-primary">

					<div class="panel-heading">
						<h3 class="panel-title">
							<i class="glyphicon glyphicon-list-alt"></i> Claim
						</h3>
					</div>

					<div class="panel-body">
						<div class="row">
							<div class="col-md-12">
								<ol class="breadcrumb">
									<li class="breadcrumb-item active" style="width: 115px;"><a href="#"> <i class="arrow"></i> <i
											class="glyphicon glyphicon-file"> </i> Prepare Claim </a></li>
									<li class="breadcrumb-item active" style="width: 155px;"><a href="#"><i class="arrow"></i> <i
											class="glyphicon glyphicon-new-window"> </i> Submit to Fin</a></li>
									<li class="breadcrumb-item active" style="width: 155px;"><a href="#"><i class="arrow"></i> <i
											class="glyphicon glyphicon-list-alt"> </i> Validate Claim </a></li>
									<li class="breadcrumb-item active" style="width: 170px;"><a href="#"><i class="arrow"></i> <i
											class="glyphicon glyphicon-new-window"> </i> Transfer to HCM </a></li>
								</ol>
							</div>
						</div>
						<div class="preparer-body">
							<form id="frmCheckQuota" method="POST">
								<div class="claim panel-body">
									<div class="row">
										<div class="col-sm-2 col-darkgrey">Type</div>
										<div class="col-sm-10 col-darkgrey col-text">
											<input type="text" style="width: 70%">
										</div>
									</div>
									<div class="row">
										<div class="col-sm-2 col-darkgrey">Hospital / Department:</div>
										<div class="col-sm-10 col-darkgrey col-text">
											<input type="text" style="width: 70%">
										</div>
									</div>
									<div class="row">
										<div class="col-sm-2">Project No.:</div>
										<div class="col-sm-4  col-text">
											<input type="text" style="width: 70%">
										</div>
										<div class="col-sm-2 ">Work Location:</div>
										<div class="col-sm-4  col-select" style="padding-top: 0.3rem;">
											<select style="width: 70%;background: #ffffff;border: 1px solid #dfdfdf; height: 26px;">
												<option value=""></option>
											</select>
										</div>
									</div>
									<div class="row">
										<div class="col-sm-2 col-darkgrey">Job(s):</div>
										<div class="col-sm-4 col-darkgrey col-text">
											<select style="width: 70%" name="jobList" class="form-control chosen-select" multiple>
												<option value='RN'>Resident</option>
												<option value='EN'>EN</option>
												<option value='AC'>AC</option>
											</select>
										</div>
										<div class="col-sm-2 col-darkgrey">Project Duration:</div>
										<div class="col-sm-4 col-darkgrey">Propagated from Project Initiation</div>
									</div>
									<div class="row">
										<div class="col-sm-2 ">Pay month:</div>
										<div class="col-sm-4  col-text">
											<input type="text" style="width: 70%">
										</div>
										<div class="col-sm-2 ">Available Work Hours:</div>
										<div class="col-sm-4 ">1234</div>
									</div>
									<div class="row">
										<div class="col-sm-12 buttons-grp">
											<button type="button" class="btn btn-primary">Create</button>
											<button type="button" class="btn btn-primary">Clear</button>
											<!-- <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#workUtilizationModel">Upload</button>
											<button type="button" class="btn btn-primary">View</button>
											<button type="button" class="btn btn-primary">Reset</button> -->
										</div>
									</div>
								</div>
							</form>
						</div>
						<div class="request-body">
							<div class="row">
								<div class="col-md-12">
									<button type="button" class="btn btn-primary btn-preparer" onclick="preparerReq()">Prepare Claim
										Request</button>
								</div>
							</div>
							<div class="panel">
								<div class="panel-heading">
									<h3 class="panel-title">Request(s) to follow up</h3>
								</div>

								<div class="panel-body">
									<div class="table-responsive" style="padding: 3px">
										<table id="myRequestTable" class="table table-striped table-bordered">
											<thead>
												<tr>
													<th style="width: 10%">Claim ID</th>
													<th style="width: 15%">SHS Project / No.</th>
													<th style="width: 15%">Hospital / Dept</th>
													<th style="width: 10%">Job(s)</th>
													<th style="width: 10%">O/S Claim as of month</th>
													<th style="width: 10%">Last update date</th>
													<th style="width: 10%">Status</th>
													<th style="width: 5%">Total</th>
													<th style="width: 5%">Approved</th>
													<th style="width: 5%">On-hold</th>
													<th style="width: 5%">Action</th>
												</tr>
											</thead>
											<tbody>
												<%-- <c:forEach var="listValue" items="${formBean.myRequest}"> </c:forEach> --%>
												<tr>
													<td><c:out value="CM123456" /></td>
													<td><c:out value="SHS12345 - To enhance medical manpower support" /></td>
													<td><c:out value="PYN - Medicine & Geriatrics" /></td>
													<td><c:out value="Resident, AC" /></td>
													<td><c:out value="September 2018" /></td>
													<td><c:out value="20/09/2018" /></td>
													<td><c:out value="Pending Approval" /></td>
													<td><c:out value="4" /></td>
													<td><c:out value="0" /></td>
													<td><c:out value="1" /></td>
													<td><span style="font-size: 12px;"> <a href='<c:url value="/claim/prepareClaim?claimId=CM123456"/>'><i
																class="glyphicon glyphicon-new-window"></i></a></span></td>
													<%-- <c:choose> <c:when test='${listValue.requestType.rqTypeCode == "DELETION"}'></c:when> </c:choose>--%>
												</tr>
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