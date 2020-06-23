<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	function performGenerate() {
		showLoading();
	
		var token = new Date().getTime();
		$("#downloadToken").val(token);
		fileDownloadCheckTimer = window.setInterval(function() { 
			var cookieValue = $.cookie("fileDownloadToken");
			if (cookieValue == token) {
				$("#downloadToken").val("");
				$.removeCookie("fileDownloadToken");
				hideLoading();
				document.getElementById("btnSearch").disabled = false; 
			}
		}, 1000);		
	}
	
	function performReset() {
		document.location.href = "<%= request.getContextPath() %>/reporting/reportReviewTimeLtdPost";
	}
	
	function downloadReport(uid, outputFormat) {
		window.open("<%= request.getContextPath() %>/reporting/downloadHistory?uid=" + uid +"&outputFormat=" + outputFormat , '_blank');
	}
	
	function showHistory() {
		$.ajax({
            url: "getReportHistory",
            type: "POST",
            data: {type: "RPT_04"},
            success: function(data) {
            	$("#tblHistory tbody").remove();
            	
            	var content = "<tbody>";
            
            	jQuery.each(data.reportList, function(index, item) {
            		content += "<tr>";
			      	content += "<td>";
            		content += "<button type='button' class='btn btn-primary' width='100px' onclick='downloadReport(\"" + data.reportList[index].reportUid + "\", \"" + data.reportList[index].outputFormat + "\")'><i class=\"fa fa-cloud-download\"></i> Download</button>";
            		content += "</td>";
		            
		            content += "<td>";
			    	content += data.reportList[index].outputFormat;
            		content += "</td>";
		            
		            content += "<td>";
			    	content += data.reportList[index].createdDateStr;
            		content += "</td>";
            		
            		content += "<td>";
			    	content += data.reportList[index].createdBy;
            		content += "</td>";
            		
			      	content += "<td>";
			      	if (data.reportList[index].criteria != null) {
			    		content += data.reportList[index].criteria;
			    	}
			    	else {
			    		content += "";
			    	}
            		content += "</td>";
			      	content += "<td>";
            		if (data.reportList[index].remark != null) {
			    		content += data.reportList[index].remark;
			    	}
			    	else {
			    		content += "";
			    	}

            		content += "</td>";
            	
            		content += "</tr>";
		        });
		        
		        content += "</tbody>";
            	
            	
            	// Append row to search result
				$("#tblHistory thead").after(content);
	            	
            
		    	$("#modalHistory").modal("show");
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	}
	
	$(function(){
		$("#frmDetail").bootstrapValidator({
			message: ' This value is not valid',
			live: "submitted",
			fields: {
			},
		})
		.on('error.field.bv', function(e, data) {
			hideLoading();
		})
		.on('success.form.bv', function(e){
			
		});
	});
</script>
<!-- Page Content -->
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > <a href="<c:url value="/reporting/reportHome"/>">Reporting</a> > Review of Time-Limited Posts that are close to effective date
		</div>
		<div class="section-title">
			<div class="title pull-left"><i class="fa fa-file-pdf-o"></i>Review of Time-Limited Posts that are close to effective date</div>
		</div>
				  
		<form id="frmDetail" method="POST">
			<form:hidden path="formBean.downloadToken"/>
			<div class="panel panel-custom-primary">
				<div class="panel-heading">
					<div class="panel-heading-title">Report Parameter Input</div>
				</div>
			
				<div class="panel-body">
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="dateAsAt" class="control-label">As at<font class="star">*</font></label>
							</div>
							<div class="col-sm-2">
								<div class='input-group date' id='datetimepicker1'>
									<input type='text' class="form-control" id="dateAsAt" name="dateAsAt" required="required"/>
									<span class="input-group-addon">
										<span class="glyphicon glyphicon-calendar"></span>
									</span>	
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<label for="" class="field_request_label">Cluster</label>
						</div>
						<div class="col-sm-4">
							<form:select path="formBean.clusterCode" name="clusterCode" class="form-control">
								<option value="">- Select -</option>
								<form:options items="${clusterList}" />
							</form:select>
						</div>
					</div>
					<div class="form-group">
						<div class="row">
							<div class="col-sm-2">
								<label for="" class="control-label">Staff Group<font class="star">*</font></label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.staffGroupCode" name="staffGroupCode" class="form-control" required="required">
									<option value="">- Select -</option>
									<form:options items="${staffGroupList}" />
								</form:select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<label for="" class="field_request_label">Remark</label>
						</div>
						<div class="col-sm-8">
							<form:input path="formBean.remark" class="form-control" maxlength="200"/>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<label for="" class="field_request_label">Output Format</label>
						</div>
						<div class="col-sm-8">
							<form:radiobutton path="formBean.outputFormat"
										      id="rbOutputFormat2" name="rbOutputFormat" value="Excel"></form:radiobutton> Excel
							<form:radiobutton path="formBean.outputFormat"
										      id="rbOutputFormat1" name="rbOutputFormat" value="PDF"></form:radiobutton> PDF&nbsp;
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12" style="text-align:right">
							<button type="submit" class="btn btn-primary" id="btnSearch" name="btnSearch" style="width:110px"
								onclick="performGenerate()"><i class="fa fa-download"></i> Generate</button>
							<button type="button" class="btn btn-default" name="btnReset" style="width:110px"
								onclick="performReset()"><i class="fa fa-eraser"></i> Clear</button>
							<button type="button" class="btn btn-default" name="btnHistory" style="width:110px"
								onclick="showHistory()"><i class="fa fa-archive"></i> History</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
	<!-- ./#container -->
</div>
<!-- ./#page-content-wrapper -->

<!-- Model for history -->
<div id="modalHistory" class="modal fade" role="dialog">
		<div class="modal-dialog modal-dialog-custom" style="width:800px">
		<!-- Modal content-->
	    <div class="modal-content">
	    	<div class="modal-header">
	    		<h4><b>Generation History</b>
		    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
		    	</h4>
	      	</div>
	      	<div class="modal-body" style="height:400px;overflow:auto">
	        	<table id="tblHistory" class="table table-hover">
	    			<thead>
	      				<tr>
					        <th>&nbsp;</th>
					        <th>Output Format</th>
					        <th>Generation Time</th>
					        <th>Generation By</th>
					        <th>Criteria</th>
					        <th>Remark</th>
					      </tr>
	   				</thead>
	   				<tbody></tbody>
	  			</table>
      		</div>
		    <div class="modal-footer">
		    	<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		    </div>
    	</div>
  	</div>
</div>

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>