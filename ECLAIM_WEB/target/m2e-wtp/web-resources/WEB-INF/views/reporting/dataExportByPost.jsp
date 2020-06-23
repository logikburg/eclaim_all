<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	function performReset() {
		document.location.href = "<%= request.getContextPath() %>/reporting/dataExportByPost";
	}
	
	function changeCluster() {
		$.ajax ({
            url: "<%=request.getContextPath() %>/common/getInstitution",
            type: "POST",
            data: {clusterCode: $("#clusterCode").val()},
            success: function(data) {
            	 $("#instCode").empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $("#instCode").append(option);
            	 
            	 for (var i=0; i<data.instList.length; i++) {
	            	 option = "<option value='" + data.instList[i].instCode + "'>" + data.instList[i].instCode + "</option>";
	            	 $("#instCode").append(option);
            	 }
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	}
	
	$(function(){
		$("#frmDetail").bootstrapValidator({
			message: 'This value cannot empty',
			live: "submitted",
			fields: {
				password: {
					validators : {
						notEmpty: "This value cannot empty.",
						stringLength: {
							min: 8,
							message: "Please enter minimum 8 characters."
						}
					}
				}
			},
		})
		.on('success.form.bv', function(e){
			if (!$("#modalPassword").hasClass("in")) {
				e.preventDefault();
			}
		});
	});
	
	function submitGenerate() {
		var validator = $("#frmDetail").data('bootstrapValidator');
	
		validator.validate();
		if (!validator.isValid()) {
			return;
		}
		
		performGenerate();
		$("#hiddenSubmitBtn").click();
		$("#zipPassword").val("");
	}
	
	var token = "";
	
	function performGenerate() {
		showLoading();
	
		fileDownloadCheckTimer = window.setInterval(function() { 
			var cookieValue = $.cookie("fileDownloadToken");
			if (cookieValue == token) {
				$("#downloadToken").val("");
				$.removeCookie("fileDownloadToken");
				hideLoading();
				$("#modalPassword").modal('hide');
				$("#frmDetail").bootstrapValidator('updateStatus', 'password', 'NOT_VALIDATED');
			}
		}, 1000);
	}
	
	function showPassword() {
		token = new Date().getTime();
		$("#downloadToken").val(token);
	
		var validator = $("#frmDetail").data('bootstrapValidator');
	
		validator.validate();
		if (!validator.isValid()) {
			return;
		}
		
		$("#frmDetail").bootstrapValidator('updateStatus', 'password', 'NOT_VALIDATED');
	
		$('#modalPassword').modal('show');
	}
</script>
<!-- Page Content -->
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > <a href="<c:url value="/reporting/reportHome"/>">Reporting</a> > Data Export By Post
		</div>
		<div class="section-title">
			<div class="title pull-left"><i class="fa fa-file-excel-o"></i>Data Export By Post</div>
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
							<label for="" class="control-label">Post ID<font class="star">*</font></label>
						</div>
						<div class="col-sm-4">
							<form:input path="formBean.postId" name="postId" class="form-control" required="required"/>
						</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12" style="text-align:right">
							<button type="button" class="btn btn-primary" name="btnSearch" style="width:110px"
							       onclick="showPassword()"><i class="fa fa-download"></i> Generate</button>
							<button type="button" class="btn btn-default" name="btnReset" style="width:110px"
							       onclick="performReset()"><i class="fa fa-eraser"></i> Clear</button>
						</div>
					</div>
				</div>
				
			</div>

			<!-- Model for history -->
			<div id="modalPassword" class="modal fade" role="dialog">
				<div class="modal-dialog modal-dialog-custom" style="width:600px">
			
					<!-- Modal content-->
				    <div class="modal-content">
				    	<div class="modal-header">
				    		<h4><b>Please input PASSWORD to protect your excel file</b></h4>
				    	</div>
				      	<div class="modal-body" style="height:120px">
				      		<div class="row" style="text-align:center">
				      			<div class="col-sm-12" style="text-align:center">
				      				This download file requires password protection.<br/>
				      				<b>Please provide a password of minimum 8 characters.</b>
				      			</div>
				      		</div>
				      		<div class="row" style="text-align:center">
				      			<div class="form-group">
					      		<div class="col-sm-12" style="text-align:center">
					      			<form:password path="formBean.password" name="password" id="zipPassword" class="form-control"/>
				      			</div>	
				      			</div>
							</div>
			      		</div>
					    <div class="modal-footer">
					    	<button type="button" id="btnSubmitGenerate" class="btn btn-primary" style="width:110px" onclick="submitGenerate()"><i class="fa fa-check"></i> Submit</button>
					    	<button type="submit" id="hiddenSubmitBtn" style="display:none"></button>
					    	<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal"><i class="fa fa-times"></i> Cancel</button>
					    	<button type="button" class="btn btn-default" style="width:110px" onclick="$('#modalHint').modal('show')"><i class="fa fa-sticky-note"></i> Hints</button>
					    </div>
			    	</div>
			  	</div>
			</div>

			<div id="modalHint" class="modal fade" role="dialog">
				<div class="modal-dialog modal-dialog-custom" style="width:800px">
					<!-- Modal content-->
				    <div class="modal-content">
				    	<div class="modal-header">
					    	<h4><b>Hints</b>
					    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
					    	</h4>
				    	</div>
				      	<div class="modal-body" style="height:150px">
				      		Hints for choosing your password:<br/>
				      		- Must be a minimum of 8 characters in length<br/>
				      		- Select a word or acronym that CANNOT be easily guessed by someone else<br/>
				      		- Do NOT use any of your logon passwords<br/>
				      		- Try to include numerals, upper and lower case letters<br/>
				      		- Memorize your password, do not write it down<br/>
				      		- If you forget the password, you will have to download the file again
			      		</div>
					    <div class="modal-footer">
					    	<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal">OK</button>
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