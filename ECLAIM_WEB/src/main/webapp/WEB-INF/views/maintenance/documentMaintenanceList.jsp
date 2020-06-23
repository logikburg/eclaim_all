<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	var selectedDocumentUid;

	function createDocument() {
		document.location.href = "<%=request.getContextPath() %>/maintenance/documentMaintenanceDetail";
	}

	function editDocument(documentUid) {
		document.location.href = "<%=request.getContextPath() %>/maintenance/documentMaintenanceDetail?id=" + documentUid;
	}
	
	function deleteDocument(documentUid) {
		selectedDocumentUid = documentUid
		$("#confirmationModel").modal("show");
	}
	
	function confirmDeleteDocument() {
		$("#deleteDocumentUid").val(selectedDocumentUid);
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
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > Maintenance > Document Maintenance
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
			<div class="title pull-left"><i class="fa fa-file-text-o"></i>Document Maintenance</div>
		</div>

<div class="panel panel-custom-warning">
	<div class="panel-heading">
		<div class="panel-heading-title">List of Document</div>
	</div>

	<div class="panel-body">
		<table id="tblResult" class="table table-striped table-bordered">
			<thead>
				<tr>
					<th>Document </th>
					<th>Type</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="listValue" items="${formBean.allDocument}">
				<tr>
					<td><c:out value="${listValue.documentDesc}"/></td>
					<td><c:out value="${listValue.documentType}"/></td>
					<td>
						<a class="btn btn-primary" href="#" onclick="editDocument(<c:out value="${listValue.documentUid}"/>)"><i class="fa fa-pencil fa-sm"></i> Edit</a>
						<a class="btn btn-danger" href="#" onclick="deleteDocument(<c:out value="${listValue.documentUid}"/>)"><i class="fa fa-trash-o fa-sm"></i> Delete</a>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>

		<button type="button" class="btn btn-primary" onclick="createDocument()"><i class="fa fa-plus"></i> Add Document</button>
		<form id="frmDetail" action="documentMaintenance" method="POST">
			<input type="hidden" id="deleteDocumentUid" name="deleteDocumentUid" />
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
        		<button type="button" class="btn btn-primary" style="width:110px" onclick='confirmDeleteDocument()'><i class="fa fa-check"></i> Yes</button>
        		<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal"><i class="fa fa-times"></i> No</button>
      		</div>
		</div>
	</div>
</div>


<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>