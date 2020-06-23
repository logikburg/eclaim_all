<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.1.0/chosen.min.css" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.1.0/chosen.jquery.min.js"></script>
<style>
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
	-webkit-appearance: textfield;
	background-image: none;
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
</style>

<script>
	$(document).ready(function(e) {
		$('#myRequestTable').dataTable();

		$('.chosen-select').chosen();
		$('.chosen-container.chosen-container-multi').width('70%');
	});
</script>

<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12" id="divMyRequest"
				style="padding-left: 5px; padding-right: 5px;">

				<div class="panel panel-custom-primary">

					<div class="panel-heading">
						<h3 class="panel-title">
							<i class="glyphicon glyphicon-list-alt"></i> Claim
						</h3>
					</div>

					<div class="panel-body">
						<!-- Check Quota Project -->
						<form id="frmCheckQuota" method="POST">
							<div class="claim panel-body">
								<div class="row">
									<div class="col-sm-2">Type:</div>
									<div class="col-sm-10 col-text">
										<input type="text" style="width: 70%">
									</div>
								</div>
								<div class="row">
									<div class="col-sm-2">Hospital / Department:</div>
									<div class="col-sm-10 col-text">
										<input type="text" style="width: 70%">
									</div>
								</div>
								<div class="row">
									<div class="col-sm-2">Project No.:</div>
									<div class="col-sm-4 col-text">
										<input type="text" style="width: 70%">
									</div>
									<div class="col-sm-2">Work Location:</div>
									<div class="col-sm-4  col-select" style="padding-top: 0.3rem;">
										<select style="width: 70%;background: #ffffff;border: 1px solid #dfdfdf; height: 26px;">
											<option value=""></option>
										</select>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-2">Job(s):</div>
									<div class="col-sm-4 col-text">
										<select style="width: 70%" name="jobList"
											class="form-control chosen-select" multiple>
											<option value='RN'>Resident</option>
											<option value='EN'>EN</option>
											<option value='AC'>AC</option>
										</select>
									</div>
									<div class="col-sm-2">Project Duration:</div>
									<div class="col-sm-4">Propagated from
										Project Initiation</div>
								</div>
								<div class="row">
									<div class="col-sm-2">Pay month:</div>
									<div class="col-sm-4 col-text">
										<input type="text" style="width: 70%">
									</div>
									<div class="col-sm-2">Available Work Hours:</div>
									<div class="col-sm-4">1234</div>
								</div>
								<div class="row">
									<div class="col-sm-12 text-right">
										<button type="button" class="btn btn-primary"
											data-toggle="modal" data-target="#notifyModel">Send
											Notification</button>
										<button type="button" class="btn btn-success"
											onclick="performCheckQuota()">New Check Quota</button>
									</div>
								</div>
							</div>
						</form>

						<div class="table-responsive" style="padding: 3px">
							<table id="myRequestTable"
								class="table table-striped table-bordered">
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
										<td><c:out
												value="SHS12345 - To enhance medical manpower support" /></td>
										<td><c:out value="PYN - Medicine & Geriatrics" /></td>
										<td><c:out value="Resident, AC" /></td>
										<td><c:out value="September 2018" /></td>
										<td><c:out value="20/09/2018" /></td>
										<td><c:out value="Pending Approval" /></td>
										<td><c:out value="4" /></td>
										<td><c:out value="0" /></td>
										<td><c:out value="1" /></td>
										<td><span style="font-size: 12px;"> <a
												href='<c:url value="/payment/approveClaim"/>'><i
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