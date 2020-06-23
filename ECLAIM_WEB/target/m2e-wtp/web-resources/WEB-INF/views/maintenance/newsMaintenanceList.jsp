<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	var selectedNewsUid;

	function createNews() {
		document.location.href = "<%=request.getContextPath() %>/maintenance/newsMaintenanceDetail";
	}

	function editNews(newsUid) {
		document.location.href = "<%=request.getContextPath() %>/maintenance/newsMaintenanceDetail?newsUid=" + newsUid;
	}
	
	function deleteNews(newsUid) {
		selectedNewsUid = newsUid
		$("#confirmationModel").modal("show");
	}
	
	function confirmDeleteNews() {
		$("#deleteNewsUid").val(selectedNewsUid);
		$("#confirmationModel").modal("hide");
		$("#frmDetail").submit();
	}
	
	$(function(){
		$("#tblResult").dataTable();
	});

</script>
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > Maintenance > News Maintenance
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
			<div class="title pull-left"><i class="fa fa-bullhorn"></i>News and Announcement Maintenance</div>
		</div>
		<div class="panel panel-custom-warning">
			<div class="panel-heading">
				<div class="panel-heading-title">List of News and Announcement</div>
			</div>
			<div class="panel-body">
				<table id="tblResult" class="table table-striped table-bordered">
					<thead>
						<tr>
							<th>News / Announcement</th>
							<th>Effective From</th>
							<th>Effective To</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach var="listValue" items="${formBean.allNews}">
						<tr>
							<td width="40%"><c:out value="${listValue.newsDesc}"/></td>
							<td width="20%" data-order='<fmt:formatDate value="${listValue.effectiveFrom}" pattern="yyyyMMdd"/>'><fmt:formatDate value="${listValue.effectiveFrom}" pattern="dd/MM/yyyy"/></td>
							<td width="20%" data-order='<fmt:formatDate value="${listValue.effectiveTo}" pattern="yyyyMMdd"/>'><fmt:formatDate value="${listValue.effectiveTo}" pattern="dd/MM/yyyy"/></td>
							<td width="20%">
								<a class="btn btn-primary" href="#" onclick="editNews(<c:out value="${listValue.newsUid}"/>)"><i class="fa fa-pencil"></i> Edit</a>
								<a class="btn btn-danger" href="#" onclick="deleteNews(<c:out value="${listValue.newsUid}"/>)"><i class="fa fa-trash-o"></i> Delete</a>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
		
				<button type="button" class="btn btn-primary" onclick="createNews()"><i class="fa fa-plus"></i> Add News/Announcement</button>
				<form id="frmDetail" action="newsMaintenance" method="POST">
					<input type="hidden" id="deleteNewsUid" name="deleteNewsUid" />
				</form>
			</div>
		</div>

	</div>
</div>

<!--  Confirmation Model -->
<div id="confirmationModel" class="modal fade" role="dialog">
  	<div class="modal-dialog modal-dialog-custom" style="width:750px">
	    <div class="modal-content">
	    	<div class="modal-header">
		    	<h4><b>Confirmation</b>
		    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
		    	</h4>
		    </div>
			<div class="modal-body">
		      	Are you sure to delete?
			</div>
	      	<div class="modal-footer">
	        	<button type="button" class="btn btn-primary" style="width:110px" onclick="confirmDeleteNews()"><i class="fa fa-check"></i> Yes</button>
	        	<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal"><i class="fa fa-times"></i> No</button>
	      	</div>
	    </div>
  	</div>
</div>


<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>