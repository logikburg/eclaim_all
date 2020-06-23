<style>
.claimPopup {
	
}

.claimPopup h4 {
	float: left;
	color: #FFFFFF;;
}

.claimPopup .modal-header {
	background: #133c61;
}

.claimPopup button.close {
	color: #FFFFFF;;
}

.claimPopup label {
	top: 4px;
	padding-right: 0px;
	font-weight: normal;
}

.claimPopup .modal-footer {
	padding-top: 10px;
	border-top: none;
}

.claimPopup .form-control {
	padding: 6px 12px;
	background-color: #fff;
	background-image: none;
	border: 1px solid #ccc;
	border-radius: 0px;
	box-shadow: none;
	transition: none;
}
</style>

<script>
	$(document).ready(function(e) {
		//triggered when outstanding Modal is about to be shown
		$('#outstandingModal').on('show.bs.modal', function(e) {
	
		    //get batch-id attribute of the clicked element
		    var batchId = $(e.relatedTarget).data('batch-id');
		    console.log("Outstand batchId " + batchId);
		    
		    $.getJSON("<c:url value='/payment/getErrorList' />", {batchId: batchId}, function(result){
				
			});
		});
	});
</script>

<!-- Warning Model -->
<div id="warningModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width: 600px">
		<div class="modal-content">
			<div class="modal-header" style="background-color: #f39c12">
				<h4>
					<b><span id="warningTitle"></span></b>
				</h4>
			</div>
			<div class="modal-body">
				<div id="warningContent"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" style="width: 100px" data-dismiss="modal">OK</button>
			</div>
		</div>
	</div>
</div>
<!-- ./#warningModal -->

<!-- Delete Model -->
<div class="modal fade claimPopup" id="deleteConfirmModal" role="dialog">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
				<h4 class="modal-title" id="deleteConfirmModalTitle">Delete Confirmation ?</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
        <div class="modal-body">
          <p>Are you sure to delete current batch ?</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
          <button type="button" class="btn btn-primary">Yes</button>
        </div>
      </div>
    </div>
 </div>
<!-- ./#deleteModal -->

<!-- uploadErrorModel -->
<div id="uploadErrorModel" class="modal fade claimPopup" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="uploadErrorTitle">Errors</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="row">
					<ul>
				      <li>Error 1</li>
				      <li>Error 2</li>
				      <li>Error 3</li>
				      <li>Error 4</li>
				    </ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- ./#uploadErrorModel -->

<!-- outstandingModal -->
<div id="outstandingModal" class="modal fade claimPopup" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="outstandingModalTitle">Outstanding List</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="row">
					<ul>
				      <li>Outstanding 1</li>
				      <li>Outstanding 2</li>
				      <li>Outstanding 3</li>
				      <li>Outstanding 4</li>
				    </ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- ./#outstandingModal -->

<!-- SendNotificationModel -->
<div class="modal fade claimPopup" id="notifyModel" tabindex="-1" role="dialog" aria-labelledby="notifyModelTitle"
	aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="notifyModelTitle">Send Notification</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<form>
					<div class="form-row">
						<div class="form-group row">
							<label for="inputTo" class="col-sm-2 col-form-label text-right">To:</label>
							<div class="col-sm-10">
								<input type="email" class="form-control" id="inputTo" placeholder="...">
							</div>
						</div>
						<div class="form-group row">
							<label for="inputCc" class="col-sm-2 col-form-label text-right">CC:</label>
							<div class="col-sm-10">
								<input type="email" class="form-control" id="inputCc" placeholder="...">
							</div>
						</div>
						<div class="form-group row">
							<label for="inputSubject" class="col-sm-2 col-form-label text-right">Subject:</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="inputSubject" placeholder="...">
							</div>
						</div>
						<div class="form-group row">
							<label for="inputContent" class="col-sm-2 col-form-label text-right">Content:</label>
							<div class="col-sm-10">
								<textarea class="form-control" id="inputContent" placeholder="..." style="height: 186px; width: 468.33px;"></textarea>
							</div>
						</div>
						<div class="form-group row">
							<label for="inputSupplement" class="col-sm-2 col-form-label text-right">Supplement:</label>
							<div class="col-sm-10">
								<textarea class="form-control" id="inputSupplement" placeholder="..." style="height: 93px; width: 468.33px;"></textarea>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-primary">Send</button>
			</div>
		</div>
	</div>
</div>
<!-- /SendNotificationModel -->

<!-- WorkUtilization -->
<div class="modal fade claimPopup" id="workUtilizationModel" tabindex="-1" role="dialog"
	aria-labelledby="workUtilizationModelTitle" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="workUtilizationModelTitle">Work Hour Utilization</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<table class="table table-striped table-bordered">
					<thead>
						<tr>
							<th>Project No</th>
							<th>Hosp.</th>
							<th>Dept.</th>
							<th>Job(s)</th>
							<th>Approved Work Hours</th>
							<th>Used Work Hours</th>
							<th>Available Work Hours</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>SHS1234</td>
							<td>PYN</td>
							<td>Medicine &amp; Geriatrics</td>
							<td>Resident, AC</td>
							<td>1401</td>
							<td>78</td>
							<td>1322</td>
						</tr>
						<%
							double usedHours = 200 * .8;
							String redstyle = "";
							if (usedHours >= 160) {
								redstyle = "style='color:red;'";
							}
						%>
						<tr <%=redstyle%>>
							<td>SHS1234</td>
							<td>PYN</td>
							<td>Medicine &amp; Geriatrics</td>
							<td>Resident, AC</td>
							<td>200</td>
							<td>160</td>
							<td>40</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<!-- /WorkUtilization -->