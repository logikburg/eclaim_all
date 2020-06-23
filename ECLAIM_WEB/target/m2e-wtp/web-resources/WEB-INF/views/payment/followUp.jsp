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

<c:set var="currentView" value='${sessionScope.currentView}' />
<script>
	$(document).ready(function(e) {
			$('#myRequestTable').dataTable();
			$('.chosen-select').chosen();
			$('.chosen-container.chosen-container-multi').width('70%');
			
			var currPage = "${currentView}";
			var filteredDivs = $(".breadcrumb-item").filter(function() {
			var reg = new RegExp(currPage, "i");
					return reg.test($(this).text());
			});
			filteredDivs.removeClass('active');
			currPage === "prepare" ? $(".btn-preparer").show() : $(".btn-preparer").hide();
			

			$('[name="fileUp"]').on('click', function() {
				$('[name="fn"]').click();
				console.log('selectedFile');
			});
			
			$('[name="fn"]').change(function (){
       				var _file = this.files[0];
			        if (_file == null || !_file.name.match(/(.xlsx)$/)) {
			            $('#prog-status').show().text('invalid file');
			            return;
			        }
					var oData = new FormData();
					oData.append("fn", _file);
       				$.ajax({
				        // Your server script to process the upload
				        url: 'http://localhost:7001/eclaim/try-to-upload',
				        type: 'POST',
				        
						// file data
				        data: oData,
				        // Tell jQuery not to process data or worry about content-type
				        // You *must* include these options!
				        cache: false,
				        processData: false,
						contentType: false,

				        xhr: function() {
				            var myXhr = $.ajaxSettings.xhr();
				            if (myXhr.upload) {
								$('progress').show();
				                // For handling the progress of the upload
				                myXhr.upload.addEventListener('progress', function(e) {
				                    if (e.lengthComputable) {
				                        $('progress').attr({
				                            value: e.loaded,
				                            max: e.total,
				                        });
				                    }
				                } , false);
				            }
				            return myXhr;
				        },
    					success: function (data) {
							$('progress').show();
							$('#prog-status').show().text('done');
        					console.log(data);
    					},
    					error: function (data) {
							$('progress').hide();
							$('#prog-status').show().text('error');
        					console.error(data);
    					}
    				});
   				});
	});

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
						<div class="request-body">
							<div class="panel">
								<div class="panel-heading">
									<h3 class="panel-title">Request(s) to follow up</h3>
								</div>

								<div class="panel-body">
									<div class="card">
										<div class="card-header" role="tab" id="headingTwo2">
									    	<a class="collapsed" data-toggle="collapse" data-parent="#accordionEx" href="#collapseTwo2" aria-expanded="false" aria-controls="collapseTwo2">
									          <i class="glyphicon glyphicon-zoom-in"> </i><b>Search Criteria </b>
									      	</a>
									    </div>
									    <div id="collapseTwo2" class="collapse" role="tabpanel" aria-labelledby="headingTwo2" data-parent="#accordionEx">
     										<div class="card-body">
     											<div class="row">
													<div class="form-group">
														<div class="col-sm-3">
															<label for="" class="control-label">Department</label>
														</div>
														<div class="col-sm-3">
															<input type='text' name="txtDepartment" class="form-control" />
														</div>
													</div>	
													<div class="form-group">
														<div class="col-sm-3">
															<label for="" class="control-label">Project Name</label>
														</div>
														<div class="col-sm-3">
															<input type='text' name="txtDepartment" class="form-control" />
														</div>
													</div>
												</div>
												<div class="row">
													<div class="form-group">
														<div class="col-sm-3">
															<label for="" class="control-label">Project ID</label>
														</div>
														<div class="col-sm-3">
															<input type='text' name="txtDepartment" class="form-control" />
														</div>
													</div>	
													<div class="form-group">
														<div class="col-sm-3">
															<label for="" class="control-label">Emp No.</label>
														</div>
														<div class="col-sm-3">
															<input type='text' name="txtDepartment" class="form-control" />
														</div>
													</div>
												</div>
												<div class="row">
													<div class="form-group">
														<div class="col-sm-3">
															<label for="" class="control-label">Earned Month</label>
														</div>
														<div class="col-sm-3">
															<input type='text' name="txtDepartment" class="form-control" />
														</div>
													</div>	
													<div class="form-group">
														<div class="col-sm-3">
															<label for="" class="control-label">Pay Month</label>
														</div>
														<div class="col-sm-3">
															<input type='text' name="txtDepartment" class="form-control" />
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
													 		<input type="checkbox" class="custom-control-input" id="defaultChecked1" checked>
														  	<label class="custom-control-label" for="defaultChecked2">Pending Transfer</label>
														</div>
													 	<div class="col-sm-3"> 
													  		<input type="checkbox" class="custom-control-input" id="defaultChecked2" checked>
													  		<label class="custom-control-label" for="defaultChecked2">Transferred</label>
													 	</div>
													 	<div class="col-sm-3">
													 	 	<input type="checkbox" class="custom-control-input" id="defaultChecked3" checked>
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
										</div>
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
													<tr>
														<td><a href='<c:url value="/payment/transfer"> <c:param name="paymentId" value="${listValue.batchId}"/> </c:url>'><c:out value="${listValue.batchId}" /></a></td>
														<td><c:out value="${listValue.parentBatchId}" /></td>
														<td><c:out value="${listValue.projectNmNo}" /></td>
														<td><c:out value="${listValue.hospDept}" /></td>
														<td><c:out value="${listValue.jobs}" /></td>
														<td><c:out value="${listValue.payMonth}" /></td>
														<td><c:out value="${listValue.lastUpdateDate}" /></td>
														<td><c:out value="${listValue.status}" /></td>
														<td><c:out value="${listValue.totalSum}" /></td>
														<td><c:out value="${listValue.transferredSum}" /></td>
														<td><c:out value="${listValue.onHoldSum}" /></td>
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