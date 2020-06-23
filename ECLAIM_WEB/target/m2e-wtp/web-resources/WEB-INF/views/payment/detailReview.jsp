<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.1.0/chosen.min.css" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.1.0/chosen.jquery.min.js"></script>
<style>
.claim {
	padding-bottom: 15px;
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

.claim table>tbody>tr>td.bg-green {
	background-color: #4eea1c;
}

.claim .work-hours {
	
}

.claim .total-hours-worked {
	
}

.claim table {
	border: 2px solid #dddddd;
}

.claim table>thead>tr>th, .claim table>tbody>tr>td {
	border: 1px solid #ddd;
	padding: 3px;
	text-align: center;
	vertical-align: middle;
	font-size: 11px;
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

.chosen-container-multi .chosen-choices li.search-choice .search-choice-close {
	top: 2px;
	right: 3px;
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

.chosen-container-multi .chosen-choices li.search-field input[type=text] {
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

table.legend {
	border: 1px solid #ddd;
	margin-top: 20px;
}

table.legend>thead>tr>th, table.legend>tbody>tr>td {
	border: 1px solid #ddd;
	padding: 3px;
	text-align: center;
	vertical-align: middle;
	font-size: 11px;
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

table#claim-table {
	width: 100%;
}

span.prog-status {
	display: none;
	font-style: italic;
	font-size: 11px;
}
</style>
<script>
	var claimTable = '#claim-table';
	$(function() {
		$('.chosen-select').chosen();
		$('.chosen-container.chosen-container-multi').width('70%');

		TableEvents = {
			'check.table' : 'onCheck',
			'uncheck.table' : 'onUncheck',
			'check-all.table' : 'onCheckAll',
			'uncheck-all.table' : 'onUncheckAll'
		}

		that = this;
		this.$elTable = $(claimTable);
		this.$selectAll = this.$elTable.find('[name="chkSelectAll"]');
		this.$selectItem = this.$elTable.find('[name="chkSelectItem"]');
		this.$selectAll.off('click').on('click', function() {
			var checked = $(this).prop('checked');
			that.$selectItem.each(function() {
				$(this).closest('tr').find('input').prop('checked', checked);
			});
			that.$elTable.trigger(checked ? TableEvents['check-all.table'] : TableEvents['uncheck-all.table']);
		});

		this.$selectItem.off('click').on('click', function() {
			var checked = $(this).prop('checked');
			that.$elTable.trigger(checked ? TableEvents['check.table'] : TableEvents['uncheck.table']);
			updateSelected();
		});

		updateSelected = function() {
			var checkAll = that.$selectItem.filter(':enabled').length
					&& that.$selectItem.filter(':enabled').length === that.$selectItem.filter(':enabled').filter(
							':checked').length;
			that.$selectAll.prop('checked', checkAll);
		};
	});

	$(document).ready(function(e) {
		that = this;

		this.$selectItem.closest('tr').each(function() {
			var totalWH_ = 0;
			$(this).find('td.work-hours').each(function() {
				totalWH_ += Number(this.innerText);
			});
			$(this).find('td.total-hours-worked').text(totalWH_);
		});
		
		$("[class$='hours']").hide();
		$("[class$='coa']").hide();

		updateLegend = (function() {
			var total = that.$selectItem.filter(':enabled').length;
			var selected = that.$selectItem.filter(':enabled').filter(':checked').length;
			var onhold = total - selected;
			$('.legend .total').text(total);
			$('.legend .selected').text(selected);
			$('.legend .onhold').text(onhold);
		});

		updateLegend();

		$(claimTable).on('onCheck onCheckAll', function(e) {
			updateLegend();
			console.log('onCheck');
		});

		$(claimTable).on('onUncheck onUncheckAll', function(e) {
			updateLegend();
			console.log('onUncheck');
		});
				
		<%String currPage = "";
			currPage = (String) request.getSession().getAttribute("currentView");%>
		var filteredDivs = $(".breadcrumb-item").filter(function() {
		var reg = new RegExp("<%=currPage%>", "i");
				return reg.test($(this).text());
			});
			filteredDivs.removeClass('active');
		
		$("[name='fileUp']").on('click', function(e) {
			$('[name="fn"]').click();
			console.log('selectedFile');
		});
		
		$('[name="fn"]').change(function (e){
    		var _file = this.files[0];
			console.log(this.length)
	        if (_file == null || !_file.name.match(/(.xlsx)$/)) {
	            $('#prog-status').show().text('invalid file');
	            return;
	        }
				var oData = new FormData();
				oData.append("fn", _file);
     				$.ajax({
		        		// Your server script to process the upload
				        url: './upload-payment-detail',
				        type: 'POST',
				        
						// file data
				        data: oData,
				        // Tell jQuery not to process data or worry about content-type
				        // You *must* include these options for async op.
				        cache: false,
				        processData: false,
						contentType: false,
		
				        xhr: function() {
				            var myXhr = $.ajaxSettings.xhr();
				            hrsDisp = $("[class$='hours']").css('display');
				            coaDisp = $("[class$='coa']").css('display');
							$("[name='btn14']").attr("disabled", "disabled");
							$("[name='btn15']").attr("disabled", "disabled");
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
				                    $('#prog-status').show().text('processing..');
				                } , false);
				            }
				            return myXhr;
				        },
		 				success: function (data) {
							$('[name="fn"]').val(null);
							$('progress').show();
							$("[name='btn14']").removeAttr("disabled");
							$("[name='btn15']").removeAttr("disabled");
							$(claimTable).find('tbody tr').remove();
							var $payTBody = $(claimTable).find('tbody');
							var jsonBatchRecords = JSON.parse(data);
							if(jsonBatchRecords.hasOwnProperty('mapPaymentBatch')){
								var batchRecords = jsonBatchRecords.mapPaymentBatch
								for(var recKy in batchRecords){
									console.log(batchRecords[recKy]);
									var $trl = $('<tr>').appendTo($payTBody);
									$('<td>').appendTo($trl).text(batchRecords[recKy]['status'] != null ? batchRecords[recKy]['status'] : "");
									$('<td>').appendTo($trl).text(batchRecords[recKy]['cluster'] != null ? batchRecords[recKy]['cluster'] : "");
									$('<td>').appendTo($trl).text(batchRecords[recKy]['hosp'] != null ? batchRecords[recKy]['hosp'] : "");
									$('<td>').appendTo($trl).text(batchRecords[recKy]['empNo'] != null ? batchRecords[recKy]['empNo'] : "");
									$('<td>').appendTo($trl).text(batchRecords[recKy]['asgNo'] != null ? batchRecords[recKy]['asgNo'] : "");
									$('<td>').appendTo($trl).text(batchRecords[recKy]['empName'] != null ? batchRecords[recKy]['empName'] : "");
									$('<td>').appendTo($trl).text(batchRecords[recKy]['staffRank'] != null ? batchRecords[recKy]['staffRank'] : "");
									$('<td>').appendTo($trl).text(batchRecords[recKy]['job'] != null ? batchRecords[recKy]['job'] : "");
									$('<td>').appendTo($trl).text(batchRecords[recKy]['shsGrade'] != null ? batchRecords[recKy]['shsGrade'] : "");
									$('<td>').appendTo($trl).text(batchRecords[recKy]['workLocation'] != null ? batchRecords[recKy]['workLocation'] : "");
									$('<td>').appendTo($trl).text(batchRecords[recKy]['haCs'] != null ? batchRecords[recKy]['haCs'] : "");
									$('<td>').appendTo($trl).text(batchRecords[recKy]['hourType'] != null ? batchRecords[recKy]['hourType'] : "");
									$('<td>').appendTo($trl).text(batchRecords[recKy]['reason'] != null ? batchRecords[recKy]['reason'] : "");
									$('<td>').appendTo($trl).text(batchRecords[recKy]['earnedMonth'] != null ? batchRecords[recKy]['earnedMonth'] : "");
									
									// coa columns
									$('<td>').appendTo($trl).text(batchRecords[recKy]['coaInst'] != null ? batchRecords[recKy]['coaInst'] : "").addClass("th-coa").css('display', coaDisp);
									$('<td>').appendTo($trl).text(batchRecords[recKy]['coaFund'] != null ? batchRecords[recKy]['coaFund'] : "").addClass("th-coa").css('display', coaDisp);
									$('<td>').appendTo($trl).text(batchRecords[recKy]['cosSection'] != null ? batchRecords[recKy]['cosSection'] : "").addClass("th-coa").css('display', coaDisp);
									$('<td>').appendTo($trl).text(batchRecords[recKy]['coaAnalystic'] != null ? batchRecords[recKy]['coaAnalystic'] : "").addClass("th-coa").css('display', coaDisp);
									$('<td>').appendTo($trl).text(batchRecords[recKy]['coaType'] != null ? batchRecords[recKy]['coaType'] : "").addClass("th-coa").css('display', coaDisp);
									if(batchRecords[recKy]['dutyDate'] != null){
										var dutyDate = batchRecords[recKy]['dutyDate'];
										for(var ii = 1; ii <= 31; ii++){
											$('<td>').appendTo($trl).text(batchRecords[recKy]['dutyDate']["" + ii] != null ? batchRecords[recKy]['dutyDate']["" + ii] : "").addClass("work-hours").css('display', hrsDisp);
											console.log('date ' + ii);
										}
									}
									
									$('<td>').appendTo($trl).text(batchRecords[recKy]['totalWorkedHours'] != null ? batchRecords[recKy]['totalWorkedHours'] : "");
								}
							}
							// tell user about uploaded excel has errors and need to fix and upload that excel again.
							if(jsonBatchRecords.hasOwnProperty('errorMsg') && jsonBatchRecords.errorMsg.length > 0) {
								var errList = jsonBatchRecords.errorMsg;
								var ulError = $('#uploadErrorModel ul');
								ulError.find('li').remove();
								for(var i in errList){
									$('<li>').appendTo(ulError).text(errList[i]).css('color', '#b70000');
								}
								$('#uploadErrorModel').modal('show');
							}
							$('#prog-status').show().text('done');
						},
		 				error: function (data) {
							$('[name="fn"]').val(null);
							$('progress').hide();
							$('#prog-status').show().text('error');
							$("[name='btn14']").removeAttr("disabled");
							$("[name='btn15']").removeAttr("disabled");
			     			console.error(data);
			 			}
  					});
			});
	});

	function performCheckQuota() {

	}

	var showingHrsTable = false;
	function showDetailHours(event) {
		showingHrsTable == true ? (showingHrsTable = false, $("[class$='hours']").hide()) : (showingHrsTable = true, $("[class$='hours']").show()); 	
	}
	
	var showingCoaTable = false;
	function showDetailCoa(event) {
		showingCoaTable == true ? (showingCoaTable = false, $("[class$='coa']").hide()) : (showingCoaTable = true, $("[class$='coa']").show());
		$("#model_editEmail").modal('show');
	}
	
	function dwnldTempl(ev){
	    $('<form>', {
    		"action": './generate-payment-template'
		}).appendTo(document.body).submit();
	}
	
	function tryToValidate(ev){
	    
	}
	
	function deleteBatch(ev){
        $("#deleteConfirmModal").on("click",".btn-default", function(){

	    });
	
	    $("#deleteConfirmModal").on("click",".btn-primary", function(){
	       $("#deleteConfirmModal").modal('hide');
	       //Todo for post seleted (batchId) for delete to serverside. 
	       var input = $("<input>")
               .attr("type", "hidden")
               .attr("id", "batchId")
               .attr("name", "batchId").val("1231");
	       $('<form>', {
    			"action": './delete',
    			"method": 'POST',
    			"id": 'deleteBatch'
			}).append(input).appendTo(document.body).submit();
	    });
	    
	    $("#deleteConfirmModal").modal('show');
	}
	
	/*
		Modal Start 
	*/
	$('#emailTo, #emailCC').tagsinput({
		 itemValue: 'email',
		 itemText: 'name',
		 typeahead: {
			displayKey: 'name',
		source: function (query, process) {
		     return $.get('getEmailList', { term: query });
		 },
		 afterSelect: function() {
			 console.log(this.$element[0].value);
		           this.$element[0].value = '';
		       }
		 }
	});
	
	function validateEmail(email) {
	    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	    return re.test(String(email).toLowerCase());
	}
	
	$('#emailTo, #emailCC').on('keyup keypress', function(e) {
	  var keyCode = e.keyCode || e.which;
	  if (keyCode === 13){
		if(e.target.type == 'text'){
			var $element = $(e.target);
			console.log('element:' +$element);
			var elTVal = $.trim($element.val());
			
			if (!$element.data('tagsinput') && elTVal.length > 0 && validateEmail(elTVal) ) {
				var elt = $element.parents('.col-sm-9').children('.form-control');
				elt.tagsinput('add', {
					"name" : elTVal,
					"email" : elTVal
				});
				$element.val('');
			}
		}
	    e.preventDefault();
	    return false;
	  }
	});
	
	// File Upload start
	function processFileUpload(row) {
		
		var bytesOf10MB = 10485760;
		
		if ($(row).find("#approvalFile").val() == "") {
			alert("Please select the file before press the upload button.");
			return;
		}
		var file = $(row).find("#approvalFile")[0].files[0];
		
		if(file.size > bytesOf10MB){
			alert("upload file size can't more than 10MB.");
			return;
		}
		
		var oMyForm = new FormData();
		oMyForm.append("approvalFile", $($(row).find("#approvalFile"))[0].files[0]);
		oMyForm.append("desc", $(row).find('#fileDesc').val());
     	$.ajax({
     		dataType: 'json',
           	url: "<%=request.getContextPath()%>/common/uploadFile",
           	data: oMyForm,
           	type: "POST",
           	enctype: 'multipart/form-data',
           	processData: false,
           	contentType: false,
           	success: function(result) {
           		$(row).find("#uploadFileId").val(result);
           		$(row).find("#divUploadBefore").hide();
           		$(row).find("#divUploadAfter").find("a").html($(row).find("#divUploadAfter").parent().find("[name='approvalFile']").val());
           		$(row).find("#divUploadAfter").show();
           		
           		var tmpRow = $(row).clone();
				$(tmpRow).find("#approvalFile").val("");
				$(tmpRow).find("#uploadFileId").val("");
				$(tmpRow).find("#divUploadBefore").show();
				$(tmpRow).find("#divUploadAfter").hide();
				$("#divUploadSection").append(tmpRow);
           	},
           	error : function(result){
               	alert("Fail to upload.");
           	}
		});
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
									<li class="breadcrumb-item active" style="width: 125px;"><a href="#"><i class="arrow"></i> <i class="glyphicon glyphicon-list-alt"> </i>
											Validate Payment </a></li>
									<li class="breadcrumb-item active" style="width: 162px;"><a href="#"><i class="arrow"></i> <i class="glyphicon glyphicon-new-window"> </i>
											Transfer to HCM </a></li>
								</ol>
							</div>
						</div>
						<!-- Check Quota Project -->
						<form id="frmCheckQuota" method="POST">
							<div class="claim panel-body">
								<div class="row">
									<div class="col-sm-2 col-darkgrey">Type:</div>
									<div class="col-sm-4">${formBean.paymentType}</div>
									<div class="col-sm-2 ">Pay month:</div>
									<div class="col-sm-4">${formBean.payMonth}</div>
								</div>
								<div class="row">
									<div class="col-sm-2 col-darkgrey">Department:</div>
									<div class="col-sm-4">${formBean.departmentName}</div>
									<div class="col-sm-2">Project Name:</div>
									<div class="col-sm-4">${formBean.projectName}</div>
								</div>
								<div class="row">
									<div class="col-sm-2 col-darkgrey">Job(s):</div>
									<div class="col-sm-4">${formBean.jobs}</div>
									<div class="col-sm-2 ">Work Location:</div>
									<div class="col-sm-4"></div>
								</div>
								<div class="row">
									<div class="col-sm-2 col-darkgrey">Project Duration:</div>
									<div class="col-sm-4 col-darkgrey">${formBean.projectDuration}</div>
									<div class="col-sm-2 ">Program Type:</div>
									<div class="col-sm-4 ">${formBean.projectType}</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6 text-left">
									<c:if test="${formBean.status == 'Un-Process' || formBean.status == 'Validated' || formBean.status == 'Partially Validated'}">
										<input type="file" id="fn" name="fn" style="display: none;">
										<progress value=0 max=0 style="display: none; width: 66px;"></progress>
										<span id="prog-status" class="prog-status">no file selected</span>
										<button type="button" class="btn btn-primary" id="fileUp" name="fileUp">Upload</button>
										<button type="button" class="btn btn-primary" onclick="dwnldTempl()">Download</button>
									</c:if>
								</div>
								<div class="col-sm-6 text-right">
									<button type="button" class="btn btn-primary" name="btn14" onclick="showDetailCoa()">COA</button>
									<button type="button" class="btn btn-primary" name="btn15" onclick="showDetailHours()">Date</button>
								</div>
							</div>
						</form>
						<div class="claim table-responsive">
							<div class="fixed-table-container">
								<div class="fixed-table-body">
									<table id="claim-table">
										<thead>
											<tr>
												<c:if test="${formBean.status == 'Transferred' || formBean.status == 'Partially Transferred'}">
													<th style="text-align: center; vertical-align: middle; width: 36px;" rowspan="2"><input name="chkSelectAll" type="checkbox"></th>
												</c:if>
												<th rowspan="2">Status</th>
												<th rowspan="2">Cluster</th>
												<th rowspan="2">Hosp.</th>
												<th rowspan="2">Emp. no.</th>
												<th rowspan="2">Assign No.</th>
												<th rowspan="2">Name</th>
												<th rowspan="2">Staff Rank</th>
												<th rowspan="2">Performed Job</th>
												<th rowspan="2">SHS Grade</th>
												<th rowspan="2">Work Location</th>
												<th rowspan="2">HA/CS</th>
												<th rowspan="2">Hour Type</th>
												<th rowspan="2">Reason</th>
												<th rowspan="2">Earned Month</th>
												<th colspan="5" class="th-header-coa">COA</th>
												<th colspan="31" class="th-header-hours">Date / No. of hours worked</th>
												<th style="width: 72px;" rowspan="2">Total no. of hours worked</th>
											</tr>
											<tr>
												<th class="th-coa">Inst</th>
												<th class="th-coa">Fund</th>
												<th class="th-coa">Section</th>
												<th class="th-coa">Analytic</th>
												<th class="th-coa">Type</th>
												<c:forEach var="i" begin="1" end="31">
											            <th class="th-hours">${i}</th>
									          	</c:forEach>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="batch" items="${formBean.listDetailBatch}">
												<tr data-index="0">
													<c:if test="${formBean.status == 'Transferred' || formBean.status == 'Partially Transferred'}">
														<td class="td-checkbox"><input data-index="${list.index}" name="batchGroupId" id="batchGroupId" type="checkbox" value="${batch.detailGroupId}"></td>
													</c:if>
													<td><c:out value="${batch.status}" /></td>
													<td><!-- c:out value="${batch.cluster}" /> --></td>
													<td><c:out value="${batch.hosp}" /></td>
													<td><c:out value="${batch.empNo}" /></td>
													<td><c:out value="${batch.asgNo}" /></td>
													<td><c:out value="${batch.empName}" /></td>
													<td><c:out value="${batch.staffRank}" /></td>
													<td><c:out value="${batch.job}" /></td>
													<td></td>
													<td></td>
													<td><c:out value="${batch.haCs}" /></td>
													<td></td>
													<td><c:out value="${batch.reason}" /></td>
													<td><c:out value="${batch.earnedMonth}" /></td>
													
													<td class="th-coa"><c:out value="${batch.coaInst}" /></td>
													<td class="th-coa"><c:out value="${batch.coaFund}" /></td>
													<td class="th-coa"><c:out value="${batch.coaSection}" /></td>
													<td class="th-coa"><c:out value="${batch.coaAnalytic}" /></td>
													<td class="th-coa"><c:out value="${batch.coaType}" /></td>
													<c:forEach var="i" begin="1" end="31">
											            <td class="work-hours">
												            <c:choose><c:when test="${empty batch.dutyDate[i]}"></c:when>
															<c:otherwise>${batch.dutyDate[i]}</c:otherwise>
															</c:choose>
														</td>
										          	</c:forEach>
													<td class="total-hours-worked"><c:out value="${batch.totalWorkedHours}" /></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-3 col-sm-4 pull-right">
								<table class="legend pull-right" style="width: 100%;">
									<tbody>
										<tr>
											<td colspan="2">Status</td>
											<td colspan="2">${formBean.status}</td>
											<!-- <td style="text-decoration: underline;"><a href="#" data-toggle="modal" data-target="#errorModel">10</a></td> -->
										</tr>
										<tr>
											<td>Total</td>
											<td>Validated</td>
											<td>Transferred</td>
											<td>Outstanding</td>
										</tr>
										<tr>
											<td><div class="total">0</div></td>
											<td><div class="unprocess">0</div></td>
											<td><div class="transferred">0</div></td>
											<td style="text-decoration: underline;"><a href="#" data-toggle="modal" data-target="#outstandingModel">0</a></td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class="col-md-4 col-sm-5 pull-right">
								<table class="legend  pull-right">
									<thead>
										<tr>
											<th style="font-weight: normal; width: 70px">Rank</th>
											<th style="font-weight: normal; width: 90px">Approved Work Hours</th>
											<th style="font-weight: normal; width: 80px">Used Work Hours</th>
											<th style="font-weight: normal; width: 80px">Available Work Hours</th>
											<th style="font-weight: normal; width: 80px">Work Hours for This Batch</th>
										</tr>
									</thead>
									<tbody>
									<c:forEach var="jobHours" items="${formBean.paymentJobHours}">
										<tr data-index="${jobHours.prjJobGroupId}">
											<td>${jobHours.ranks}</td>
											<td><strong>${jobHours.apprHour}</strong></td>
											<td><strong>${jobHours.usedHour}</strong></td>
											<td><strong>${jobHours.availHour}</strong></td>
											<td><strong>${jobHours.batchHour}</strong></td>
										</tr>
									</c:forEach>
										<tr>
											<td>Total</td>
											<td><strong>0</strong></td>
											<td><strong>0</strong></td>
											<td><strong>0</strong></td>
											<td><strong>0</strong></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-3">
								<div class="pull-left" style="padding-top: 15px;">
									<c:if test="${formBean.status == 'Un-Process' || formBean.status == 'Validated' || formBean.status == 'Partially Validated'}">
										<button type="button" class="btn btn-primary" onclick="deleteBatch()">Delete</button>
									</c:if>
								</div>
							</div>
							<div class="col-sm-9">
								<div class="pull-right" style="padding-top: 15px;">
									<c:if test="${formBean.status == 'Transferred' || formBean.status == 'Partially Transferred'}">
										<a class="btn btn-primary" href="<c:url value="/payment/submitBatch"/>" role="button">Rollback</a>
									</c:if>
									<c:if test="${formBean.status == 'Un-Process' || formBean.status == 'Validated' || formBean.status == 'Partially Validated'}">
										<button type="button" class="btn btn-primary" onclick="tryToValidate()">Validate</button>
									</c:if>	
									<c:if test="${formBean.status == 'Validated' || formBean.status == 'Partially Validated'}">
										<a class="btn btn-primary" href="<c:url value="/payment/submitBatch"/>" role="button">Submit</a>
									</c:if>
								</div>
							</div>
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

<!-- Model for edit email - Start -->
<div id="model_editEmail" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom modal-dialog-postId">
		<!-- form:hidden path="formBean.returnCase" /-->
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Modify Email Content</b>
		    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
		    	</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-sm-2">
						<label for="" class="field_request_label">To<font class="star">*</font></label>
					</div>
					<div class="col-sm-9">
<%-- 						<form:input path="formBean.emailTo" class="form-control"/> --%>
							<input type="text" id="emailTo" name="emailTo" class="form-control"/>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<label for="" class="field_request_label">CC</label>
					</div>
					<div class="col-sm-9">
						<input type="text" id="emailCC" name="emailCC" class="form-control"/>
						<!-- form:input path="formBean.emailCC" class="form-control" / -->
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<label for="" class="field_request_label">Title<font class="star">*</font></label>
					</div>
					<div class="col-sm-9">
					<input type="text" id="txtEmailTitle" name=txtEmailTitle class="form-control"/>
						<!--  form:input path="formBean.emailTitle" class="form-control"
							id="txtEmailTitle" /> -->
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<label for="" class="field_request_label">Content</label>
					</div>
					<div class="col-sm-9" style="height:200px">
						<div id="divEmailContent"
							style="border:1px solid #DDDDDD; overflow:auto;height:200px"></div>
						<input type="hidden" id="txtEmailContent" name=txtEmailContent class="form-control"/>
						<!-- form:hidden path="formBean.emailContent" class="form-control"
							style="width:100%;height:200px" id="txtEmailContent" /> -->
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<label for="" class="field_request_label">Supplementary Info</label>
					</div>
					<div class="col-sm-9" style="height:200px">
						<textarea class="form-control" id="emailSuppInfo" name="emailSuppInfo"
							style="width:100%;height:200px"></textarea>
						<!-- form:textarea path="formBean.emailSuppInfo" class="form-control" id="emailSuppInfo" name="emailSuppInfo"
							style="width:100%;height:200px" /> -->
					</div>
				</div>
				<div class="row">
						<div class="col-sm-2">
							<label for="" class="field_request_label">Attachment</label>
						</div>
						<div id="divUploadSection" class="col-md-8">
							<div class="row">
								<div id="divUploadBefore" >
									<div class="col-sm-6">
										<input type="file" id="approvalFile" name="approvalFile" style="width:100%" />
									</div>
<!-- 									<div class="col-sm-4" style="text-align: left"> -->
<!-- 										<input type="text" id="fileDesc" style="width:100%" placeholder="Description"/> -->
<!-- 									</div> -->
									<div class="col-sm-2" style="text-align: right">
										<button type="button" name="btnUploadFile"
										onclick="processFileUpload($(this).parent().parent().parent())"
										class="btn btn-primary" style="width:110px"><i class="fa fa-upload"></i> Upload</button>
										<input type="hidden" id="uploadFileId" name="uploadFileId" />
									</div>
								</div>
				
								<div id="divUploadAfter" style="display: none">
									<div class="col-sm-8">
										<a href="#" onclick="downloadTempFile()">Download</a> <button
											type="button" name="btnRemoveFile" style="width:100px"
											onclick="reuploadFile($(this).parent().parent().parent());"
											class="btn btn-danger"><i class="fa fa-trash-o"></i> Delete</button>
									</div>
								</div>
							</div>
						</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="submit" class="btn btn-primary" style="width:110px;" onclick="submitSendEmail()">
					<i class="fa fa-send-o"></i> Send</button>
				<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal" >
					<i class="fa fa-times"></i> Cancel</button>
			</div>
		</div>

	</div>
</div>
<!-- Model for edit email - End -->

<%@ include file="/WEB-INF/views/core/commonPopups.jsp"%>
<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>