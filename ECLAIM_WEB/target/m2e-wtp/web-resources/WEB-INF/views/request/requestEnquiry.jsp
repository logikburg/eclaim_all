<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	var tblRequestHistory;

	function showHistory(requestUid, requestId) {
		$.ajax({
            url: "<%=request.getContextPath() %>/request/getRequestHistory",
            cache: false,
            type: "POST",
            data: {requestUid: requestUid},
            success: function(data) {
            	tblRequestHistory.clear();
				
            	//upgarde hist
				if (data.historyList.length > 0) {
            		for (var i=0; i<data.historyList.length; i++) {
						tblRequestHistory.row.add([
							"<td><span style=\"display:none\">" + data.historyList[i].actionDate + "</span><span>" + data.historyList[i].actionDateStr + "</td>",
							"<td>" + data.historyList[i].actionTaken + "</td>",
							"<td>" + data.historyList[i].actionRoleId + "</td>",
							"<td>" + data.historyList[i].actionBy + "</td>",
						]);
	            	}
            	}	
            	
            	tblRequestHistory.draw();
            	
            	$("#lblRequestId").text(requestId);
            
            	$("#requestHistoryModal").modal("show");
            }
        });
	}

	$(document).ready(function(){
		tblRequestHistory = $("#tblRequestHistory").DataTable();
		
		<c:if test="${formBean.haveResult == 'Y'}">
			$('#searchCriteria').collapse('toggle');
		</c:if>
	});
		
	function performSearch() {
		showLoading();
		$("#frmDetail").submit();
	}
	
	function performReset() {
		document.location.href = "<%= request.getContextPath() %>/request/requestEnquiry";
	}
	
	$(function() {
		$("#tblSearchResult").dataTable({
	        "columnDefs": [ {
				"targets": 'no-sort',
				"orderable": false,
	    	}], 
	    	"language": {
				"emptyTable":"No record found."
			}
		});
		
		<c:if test="${formBean.showRecordTrimmedMsg == 'Y'}">  
			$("#warningTitle").html("Warning");
            $("#warningContent").html("Result more than 150 records and result list is trimmed, please refine your searching critieria.");
            $("#warningModal").modal("show");
		</c:if>
	});
	
	function openLink(type, id) {
		if (type == "DELETION") {
			document.location.href = "<%= request.getContextPath() %>/request/deletion?rq=" + id;
		}
		else if (type == "FROZEN") {
			document.location.href = "<%= request.getContextPath() %>/request/frozen?rq=" + id;
		}
		else if (type == "NEW") {
			document.location.href = "<%= request.getContextPath() %>/request/newPost?rq=" + id;
		}
		else if (type == "CHG_FUND") {
			document.location.href = "<%= request.getContextPath() %>/request/changeOfFunding?rq=" + id;
		}
		else if (type == "EXTENSION") {
			document.location.href = "<%= request.getContextPath() %>/request/extension?rq=" + id;
		}
		else if (type == "SUPP_PROM") {
			document.location.href = "<%= request.getContextPath() %>/request/suppPromotion?rq=" + id;
		}
		else if (type == "CHG_STAFF") {
			document.location.href = "<%= request.getContextPath() %>/request/changeStaffMix?rq=" + id;
		}
		else if (type == "UPGRADE") {
			document.location.href = "<%= request.getContextPath() %>/request/upgrade?rq=" + id;
		}
		else if (type == "CHG_HCM") {
			document.location.href = "<%= request.getContextPath() %>/request/changeHCMPost?rq=" + id;
		}
		else if (type == "FTE_ADJ") {
			document.location.href = "<%= request.getContextPath() %>/request/fteAdjustment?rq=" + id;
		}
	}

</script>

<!-- Page Content -->
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href='<c:url value="/home/home"/>'><i class="fa fa-home"></i>Home</a> > Request Enquiry
		</div>
		<div class="section-title">
			<div class="title pull-left"><i class="fa fa-question-circle"></i>Request Enquiry</div>
		</div>
		<form id="frmDetail" method="POST">
			<input type="hidden" id="currentUid"/>
			<input type="hidden" id="currentSnapUid"/>
			<input type="hidden" id="attUid"/>
		
			<div class="panel panel-custom-primary">
				<div class="panel-heading">
					<div class="panel-heading-title">
						<a role="button" data-target="#searchCriteria" class="panel-title" data-toggle="collapse">Searching Criteria</a>
					</div>
				</div>
			
				<div id="searchCriteria" class="panel-collapse collapse in">
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-2">
								<label for="position_org" class="field_request_label">Request ID</label>
							</div>
							<div class="col-sm-4">
								<form:input path="formBean.requestId" class="form-control" />
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-2">
								<label for="position_org" class="field_request_label">Creation Date</label>
							</div>
							<div class="col-sm-2">
								<div class="input-group date">
									<form:input path="formBean.createdDateFrom" class="form-control" />
									<span class="input-group-addon"> 
										<span class="glyphicon glyphicon-calendar"></span>
									</span>
								</div>
							</div>
							<div class="col-sm-1">
								<label>to</label>
							</div>
							<div class="col-sm-2">
								<div class="input-group date">
									<form:input path="formBean.createdDateTo" class="form-control" />
									<span class="input-group-addon"> 
										<span class="glyphicon glyphicon-calendar"></span>
									</span>
								</div>
							</div>
						</div>
						
						
						<div class="row">
							<div class="col-sm-2">
								<label for="position_org" class="field_request_label">Last Updated Date</label>
							</div>
							<div class="col-sm-2">
								<div class="input-group date">
									<form:input path="formBean.updatedDateFrom" class="form-control" />
									<span class="input-group-addon"> 
										<span class="glyphicon glyphicon-calendar"></span>
									</span>
								</div>
							</div>
							<div class="col-sm-1">
								<label>to</label>
							</div>
							<div class="col-sm-2">
								<div class="input-group date">
									<form:input path="formBean.updatedDateTo" class="form-control" />
									<span class="input-group-addon"> 
										<span class="glyphicon glyphicon-calendar"></span>
									</span>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-12" style="text-align: right">
								<button type="button" class="btn btn-primary" name="btnSearch"
								style="width:110px" onclick="performSearch()"><i class="fa fa-search"></i> Search</button>
							<button type="button" class="btn btn-default" name="btnReset"
								style="width:110px" onclick="performReset()"><i class="fa fa-eraser"></i> Clear</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>

		<c:if test='${formBean.haveResult == "Y"}'>
		<div class="panel panel-custom-primary">
			<div class="panel-heading">
				<div class="panel-heading-title">Search Result</div>
			</div>
			<div class="panel-body">
				<div id="divSearchResult" style="background-color:#ffffff">
					<table id="tblSearchResult" class="table table-striped table-bordered">
						<thead>
							<tr>
								<th>Creation Date</th>
								<th>Last Updated Date</th>
								<th>Request Type</th>
								<th>Request ID</th>
								<th>Status</th>
								<th class="no-sort" style="width: 100px">Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="searchResult" items="${formBean.searchResultList}">
								<tr>
									<td><span style="display:none"><fmt:formatDate value="${searchResult.createdDate}" pattern="yyyyMMddHHmmss"/></span><span><fmt:formatDate value="${searchResult.createdDate}" pattern="dd/MM/yyyy HH:mm:ss"/></span></td>
									<td><span style="display:none"><fmt:formatDate value="${searchResult.updatedDate}" pattern="yyyyMMddHHmmss"/></span><span><fmt:formatDate value="${searchResult.updatedDate}" pattern="dd/MM/yyyy HH:mm:ss"/></span></td>
									<td><c:out value="${searchResult.requestType.rqTypeName}"/></td>
									<td><a href='#' onclick="openLink('${searchResult.requestType.rqTypeCode}', '${searchResult.requestNo}')"><c:out value="${searchResult.requestId}"/></a></td>
									<td><c:out value="${searchResult.requestStatus.statusDesc}"/></td>
									<td><button type="button" 
										        onclick="showHistory('<c:out value="${searchResult.requestNo}"/>', '<c:out value="${searchResult.requestId}"/>');"
										        class="btn btn-primary">History</button></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		</c:if>
	</div>
	<!-- ./#container -->
</div>
<!-- ./#page-content-wrapper -->


<div id="requestHistoryModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:680px">
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Request History - <label id="lblRequestId"></label></b>
			    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
			    </h4>
			</div>
			<div class="modal-body">
				<table id="tblRequestHistory" class="table table-bordered mprs_table" style="border: solid 1px #DDD">
					<thead>
						<tr>
							<th style="width:150px">Date</th>
							<th style="width:100px">Action</th>
							<th style="width:100px">Role</th>
							<th style="width:300px">Action By</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>



<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>