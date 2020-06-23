<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script>
	var tblUpgradeHist, tblExtensionHist, tblDeletionHist, tblChangeFundingHist, tblSuppHist, tblFrozenHist, tblChangeStaffMixHist, tblAssignmentHist;
	
	function showPostDetails(postUid) {
		showLoading();
		$.ajax({
            url: "<%=request.getContextPath() %>/api/request/getPostDetails",
            cache: false,
            type: "POST",
            data: {searchPostNo: postUid},
            success: function(data) {
				tblUpgradeHist = $("#tblUpgradeHist").DataTable();
				tblExtensionHist = $("#tblExtensionHist").DataTable();		
				tblDeletionHist = $("#tblDeletionHist").DataTable();
				tblChangeFundingHist = $("#tblChangeFundingHist").DataTable();
				tblSuppHist = $("#tblSuppHist").DataTable();
				tblFrozenHist = $("#tblFrozenHist").DataTable();
				tblChangeStaffMixHist = $("#tblChangeStaffMixHist").DataTable();
				tblAssignmentHist = $("#tblAssignmentHist").DataTable();

				$("#postDetailModal #lblPostIdN").text(data.postId);
				$("#postDetailModal #ddl_rankN").val(data.rankCode);

            	//Position info
            	$('#postDetailModal #txt_unitN').val(data.unit);	
            	$('#postDetailModal #ddl_post_titleN').val(data.postTitle);
				$('#postDetailModal #ddl_post_durationN').val(data.postDuration);
				$('#postDetailModal #txt_postStartDateN').val(data.postStartDate);
				if (data.limitDurationType!=null && data.limitDurationType=="FIXED_END_DATE") {
					$("#postDetailModal #rd_DuartionN2N").prop('checked', 'checked');	
				}
				else { 
					$("#postDetailModal #rd_DuartionN").prop('checked', 'checked');
				}	
					
				$('#postDetailModal #txt_limit_duration_noN').val(data.limitDurationNo);
				$('#postDetailModal #ddl_limit_duration_unitN').val(data.limitDurationUnit);
				$('#postDetailModal #txt_post_actual_start_dateN').val(data.postActualStartDate);

				if ($("#rd_DuartionN2N").prop('checked') == true) {
					$('#txt_post_actual_end_dateN').val(data.limitDurationEndDate);
				}
				else {
					$('#txt_post_actual_end_dateN').val("");
				}	

				$('#postDetailModal #ddl_post_remarkN').val(data.postRemark);
				$('#postDetailModal #ddl_postFTEN').val(data.postFTE);
				$('#postDetailModal #txt_post_FTE_valueN').val(data.postFTEValue);
				$('#postDetailModal #ddl_position_statusN').val(data.positionStatus);
				$('#postDetailModal #dp_position_start_dateN').val(data.positionStartDate);
				$('#postDetailModal #dp_position_end_dateN').val(data.positionEndDate);
				$('#postDetailModal #txt_cluster_ref_noN').val(data.clusterRefNo);
				if(data.clusterRemark != null){
					$('#postDetailModal #txt_clusterRemarkN').html(data.clusterRemark.replace(/(?:\r\n|\r|\n)/g, "<br/>"));
				} else {
					$('#postDetailModal #txt_clusterRemarkN').html("");
				}
		
				/**** For funding information - Start ****/
				// Remove all row(s)
				for (var i=1; i<$("#tblFundingView tr").length; i++) {
					$($("#tblFundingView tr")[0]).remove();
				}
				
				// Get Number of Funding Source
				
				var hiddenTable = data.fundingList;
				var numberOfFundingSource = hiddenTable.length;
				
				// Add row
				for (var i=1; i<data.fundingList.length; i++) {
					addNewFundingView();
				}
				
				// Update content of each row
				for (var i=0; i<numberOfFundingSource; i++) {
					$($("#tblFundingView select[name$='viewAnnualPlanInd']")[i]).val(data.fundingList[i].annualPlanInd);
					$($("#tblFundingView select[name$='viewProgramYear']")[i]).val(data.fundingList[i].programYear);
					$($("#tblFundingView input[name$='viewProgramCode']")[i]).val(data.fundingList[i].programCode);
					$($("#tblFundingView input[name$='viewProgramName']")[i]).val(data.fundingList[i].programName);
					$($("#tblFundingView select[name$='viewProgramTypeCode']")[i]).val(data.fundingList[i].programTypeCode);
					$($("#tblFundingView select[name$='viewFundSrcId']")[i]).val(data.fundingList[i].fundSrcId);
					$($("#tblFundingView input[name$='viewFundSrcStartDate']")[i]).val(data.fundingList[i].fundSrcStartDate);
					$($("#tblFundingView input[name$='viewFundSrcEndDate']")[i]).val(data.fundingList[i].fundSrcEndDate);
					$($("#tblFundingView input[name$='viewFundSrcFte']")[i]).val(data.fundingList[i].fundSrcFte);
					$($("#tblFundingView textarea[name$='viewFundSrcRemark']")[i]).val(data.fundingList[i].fundSrcRemark);
					$($("#tblFundingView input[name$='viewInst']")[i]).val(data.fundingList[i].inst);
					$($("#tblFundingView input[name$='viewSection']")[i]).val(data.fundingList[i].section);
					$($("#tblFundingView input[name$='viewAnalytical']")[i]).val(data.fundingList[i].analytical);
				}
				/**** For funding information - End ****/
		
            	$('#postDetailModal #ddl_res_sup_fr_extN').val(data.res_sup_fr_ext);
				$('#postDetailModal #txt_res_sup_remarkN').val(data.res_sup_remark);
				
				$('#postDetailModal #txt_proposed_post_idN').val(data.proposed_post_id);
				$('#postDetailModal #txt_post_id_justN').val(data.post_id_just);
				
				$("#postDetailModal #dtlProgramTypeN").val(data.programType);
				$("#postDetailModal #dtlSubSpecialtyN").val(data.subSpecialty);
				
				tblUpgradeHist.clear();
				tblExtensionHist.clear();
				tblDeletionHist.clear();
				tblChangeFundingHist.clear();
				tblSuppHist.clear();
				tblFrozenHist.clear();
				tblChangeStaffMixHist.clear();
				tblAssignmentHist.clear();
				
            	//upgarde hist
				if (data.upgradeListSize > 0) {
            		for (var i=0; i<data.upgradeListSize; i++) {
						tblUpgradeHist.row.add([
							"<td name='upgradeRequestID'>"+ data.upgradeHistRequestList_RequestID[i] +"</td>",
							"<td name='upgradeRequestEffDate'>"+ data.upgradeHistRequestList_RequestEffDate[i] +"</td>",
							"<td name='upgradeRequestFromToInd'>"+ data.upgradeHistRequestList_RequestFromToInd[i] +"</td>",
							"<td name='upgradeRequestPostID'>"+ data.upgradeHistRequestList_RequestPostID[i] +"</td>",
							"<td name='upgradeRequestDept'>"+ data.upgradeHistRequestList_RequestDept[i] +"</td>",
							"<td name='upgradeRequestRank'>"+ data.upgradeHistRequestList_RequestRank[i] +"</td>",
							"<td name='upgradeRequestPositiontDurationType'>"+ data.upgradeHistRequestList_RequestPositiontDurationType[i] +"</td>",
							"<td name='upgradeRequestPositionType'>"+ data.upgradeHistRequestList_PositionType[i] +"</td>"//,
							//"<td style='text-align:center'><input name='btnUpgradeView' type='button' value='View Detail' class='btn' onclick='viewUpgradeRequest(\""
							//		+ data.upgradeHistRequestList_RequestID[i]
							//		+ "\")' style='margin-right:5px,'/></td>"
						]);
	            	}
	            	
	            	$("#secUpgradeHist").show();
            	}
            	else {
            		$("#secUpgradeHist").hide();
            	}	
            	
            	
            	//extension hist
				if (data.extensionListSize > 0) {
            		for (var i=0; i<data.extensionListSize; i++) {
						tblExtensionHist.row.add([
							"<td name='extensionRequestID'>"+ data.extensionHistRequestList_RequestID[i] +"</td>",
							"<td name='extensionTransDate'>"+ data.extensionHistRequestList_RequestTransDate[i] +"</td>",
							"<td name='extensionOrgEndDate'>"+ data.extensionHistRequestList_RequestOrgEndDate[i] +"</td>",
							"<td name='extensionRevisedEndDate'>"+ data.extensionHistRequestList_RequestRevisedEndDate[i] +"</td>"//,
							//"<td style='text-align:center'><input name='btnExtensionView' type='button' value='View Detail' class='btn' onclick='viewExtensionRequest(\""
							//	+ data.extensionHistRequestList_RequestID[i]
							//	+ "\")' style='margin-right:5px;'/></td>"
						]);
						 		
	            	}
	            	
	            	$("#secExtensionHist").show();
            	}
            	else {
            		$("#secExtensionHist").hide();
            	}
            
            	//deletion hist
				if (data.deletionListSize > 0) {
            		for (var i=0; i<data.deletionListSize; i++) {
            				
						tblDeletionHist.row.add([
							"<td name='deletionRequestID'>"+ data.deletionHistRequestList_RequestID[i] +"</td>",
							"<td name='deletionRequestTransDate'>"+ data.deletionHistRequestList_RequestTransDate[i] +"</td>",
							"<td name='deletionRequestEffDate'>"+ data.deletionHistRequestList_RequestEffDate[i] +"</td>",
							"<td name='deletionRequestReason'>"+ data.deletionHistRequestList_RequestReason[i] +"</td>"//,
							//"<td style='text-align:center'><input name='btnDeletionView' type='button' value='View Detail' class='btn' onclick='viewDetail(\""
							//		+ data.deletionHistRequestList_RequestID[i]
							//		+ "\")' style='margin-right:5px,'/></td>"
						]);
	            	}
	            	
	            	$("#secDeletionHist").show();
            	}
            	else {
            		$("#secDeletionHist").hide();
            	}
				
				//change funding hist
				if (data.changeFundingListSize > 0) {
            		for (var i=0; i<data.changeFundingListSize; i++) {
						tblChangeFundingHist.row.add([
							"<td name='changeFundingRequestID'>"+ data.changeFundHistRequestList_RequestID[i] +"</td>",
							"<td name='changeFundingRequestTransDate'>"+ data.changeFundHistRequestList_RequestTransDate[i] +"</td>",
							"<td name='changeFundingRequestReason'>"+ data.changeFundHistRequestList_RequestReason[i] +"</td>"//,
							//"<td style='text-align:center'><input name='btnChangeFundingView' type='button' value='View Detail' class='btn' onclick='viewDetail(\""
							//		+ data.changeFundHistRequestList_RequestID[i]
							//		+ "\")' style='margin-right:5px,'/></td>"
						]);	
	            	}
	            	
	            	$("#secChangeFundingHist").show();
            	}
            	else {
            		$("#secChangeFundingHist").hide();
            	}
				
				//supplementary hist
				if (data.suppPromotionListSize > 0) {
            		for (var i=0; i<data.suppPromotionListSize; i++) {
						tblSuppHist.row.add([
							"<td name='supplementaryRequestID'>"+ data.suppPromoHistRequestList_RequestID[i] +"</td>",
							"<td name='supplementaryRequestTransDate'>"+ data.suppPromoHistRequestList_RequestTransDate[i] +"</td>",
							"<td name='supplementaryRequestEffDate'>"+ data.suppPromoHistRequestList_RequestEffDate[i] +"</td>",
							"<td name='supplementaryRequestRemark'>"+ data.suppPromoHistRequestList_RequestRemark[i] +"</td>"//,
							//"<td style='text-align:center'><input name='btnSuppView' type='button' value='View Detail' class='btn' onclick='viewDetail(\""
							//		+ data.suppPromoHistRequestList_RequestID[i]
							//		+ "\")' style='margin-right:5px,'/></td>"
						]);
	            	}
	            	
	            	$("#secSuppHist").show();
            	}
            	else {
            		$("#secSuppHist").hide();
            	}
				
				//frozen hist
				if (data.frozenListSize > 0) {
            		for (var i=0; i<data.frozenListSize; i++) {
						tblFrozenHist.row.add([
							"<td name='frozenRequestID'>"+ data.frozenHistRequestList_RequestID[i] +"</td>",
							"<td name='frozenRequestTransDate'>"+ data.frozenHistRequestList_RequestTransDate[i] +"</td>",
							"<td name='frozenRequestEffStartDate'>"+ data.frozenHistRequestList_RequestEffDate[i] +"</td>",
							"<td name='frozenRequestEffEndDate'>"+ data.frozenHistRequestList_RequestFrozenEndDate[i] +"</td>",
							"<td name='frozenRequestReason'>"+ data.frozenHistRequestList_RequestReason[i] +"</td>"//,
							//"<td style='text-align:center'><input name='btnFrozenView' type='button' value='View Detail' class='btn' onclick='viewDetail(\""
							//		+ data.frozenHistRequestList_RequestID[i]
							//		+ "\")' style='margin-right:5px,'/></td>"
						]);	
	            	}
	            	
	            	$("#secFrozenHist").show();
            	}
            	else {
            		$("#secFrozenHist").hide();
            	}
				
				//change of staff mix hist
				if (data.changeStaffMixListSize > 0) {
            		for (var i=0; i<data.changeStaffMixListSize; i++) {
						tblChangeStaffMixHist.row.add([
							"<td name='changeStaffMixRequestID'>"+ data.staffMixHistRequestList_RequestID[i] +"</td>",
							"<td name='changeStaffMixRequestEffDate'>"+ data.staffMixHistRequestList_RequestEffDate[i] +"</td>",
							"<td name='changeStaffMixRequestFromToInd'>"+ data.staffMixHistRequestList_RequestFromToInd[i] +"</td>",
							"<td name='changeStaffMixRequestPostID'>"+ data.staffMixHistRequestList_RequestPostID[i] +"</td>",
							"<td name='changeStaffMixRequestDept'>"+ data.staffMixHistRequestList_RequestDept[i] +"</td>",
							"<td name='changeStaffMixRequestRank'>"+ data.staffMixHistRequestList_RequestRank[i] +"</td>",
							"<td name='changeStaffMixRequestPositiontDurationType'>"+ data.staffMixHistRequestList_RequestPositiontDurationType[i] +"</td>",
							"<td name='changeStaffMixRequestPositionType'>"+ data.staffMixHistRequestList_PositionType[i] +"</td>"//,
							//"<td style='text-align:center'><input name='btnchangeStaffMixView' type='button' value='View Detail' class='btn' onclick='viewchangeStaffMixRequest(\""
							//		+ data.staffMixHistRequestList_RequestID[i]
							//		+ "\")' style='margin-right:5px,'/></td>"
						]);
	            	}
	            	
	            	$("#secChangeStaffMixHist").show();
            	}
            	else {
            		$("#secChangeStaffMixHist").hide();
            	}
            	
            	//assignment hist
				if (data.assignmentListSize > 0) {
            		for (var i=0; i<data.assignmentListSize; i++) {
						tblAssignmentHist.row.add([
							"<td name='assignmentEmpName'>"+ data.assignmentHistList_EmpName[i] +"</td>",
							"<td name='assignmentRank'>"+ data.assignmentHistList_Rank[i] +"</td>",
							"<td name='assignmentEmpType'>"+ data.assignmentHistList_EmpType[i] +"</td>",
							"<td name='assignmentPositionTitle'>"+ data.assignmentHistList_EmpPositionTitle[i] +"</td>",
							"<td name='assignmentFTE'>"+ data.assignmentHistList_FTE[i] + "</td>",
							"<td name='assignmentStartDate'>"+ data.assignmentHistList_StartDate[i] +"</td>",
							"<td name='assignmentEndDate'>"+ data.assignmentHistList_EndDate[i] +"</td>",
							"<td name='assignmentLeaveReason'>"+ data.assignmentHistList_LeaveReason[i] +"</td>"// ,
							// "<td style='text-align:center'><input name='btnAssignmentView' type='button' value='View Detail' class='btn' onclick='viewDetail(\""
							//		+ data.assignmentHistList_PositionID[i]
							//		+ "\")' style='margin-right:5px,'/></td>"
						]);
	            	}
            	}
            	
            	if (data.upgradeListSize == 0 && data.extensionListSize == 0 && data.deletionListSize == 0 && 
            		data.changeFundingListSize == 0 && data.suppPromotionListSize == 0 && data.frozenListSize == 0 && 
            		data.changeStaffMixListSize == 0) {
            		$("#postDetailModal #secNoChangeHist").show();
            	}
            	else {
            		$("#postDetailModal #secNoChangeHist").hide();
            	}
            	
				tblUpgradeHist.draw();
				tblExtensionHist.draw();
				tblDeletionHist.draw();
				tblChangeFundingHist.draw();
				tblSuppHist.draw();
				tblFrozenHist.draw();
				tblChangeStaffMixHist.draw();
				tblAssignmentHist.draw();

				hideLoading();
				
				$("#postDetailModal input").attr("disabled", true);
				$("#postDetailModal select").attr("disabled", true);
				$("#postDetailModal textarea").attr("disabled", true);

				changeRankDropdownNew();
				postDurationChangeNew();

				// Open the post dialog
				$("#postDetailModal").modal("show");
			}
		});
	}
	
	function changeRankDropdownNew() {
		$("#postDetailModal #lblSubSpecialtyN").show();
		$("#postDetailModal #dtlSubSpecialtyN").show();
	}
	
	function postDurationChangeNew() {
		// If Recurrent - disable the duration and fixed end date
		if ($("#postDetailModal #ddl_post_durationN").val() == "R") {
			$("#postDetailModal #rd_DuartionN").hide();
			$("#postDetailModal #rd_DuartionN2N").hide();
			$("#postDetailModal #ddl_limit_duration_unitN").hide();
			$("#postDetailModal #txt_limit_duration_noN").hide();
			$("#grpLimitDurationEndDateN").hide();
			$("#postDetailModal #lblDurationTypeN").hide();
			$("#postDetailModal #lblDurationTypeN2").hide();
		}
		else {
			$("#postDetailModal #rd_DuartionN").show();
			$("#postDetailModal #rd_DuartionN2N").show();
			$("#postDetailModal #ddl_limit_duration_unitN").show();
			$("#postDetailModal #txt_limit_duration_noN").show();
			$("#postDetailModal #grpLimitDurationEndDateN").show();
			$("#postDetailModal #lblDurationTypeN").show();
			$("#postDetailModal #lblDurationTypeN2").show();
		}
		
		clickDurationNew();
	}
	
	function clickDurationNew() {
		if ($("#postDetailModal #rd_DuartionN").prop("checked") == false) {
			$("#postDetailModal #ddl_limit_duration_unitN").val("");
			$("#postDetailModal #txt_limit_duration_noN").val("");
		}
	}
</script>

<!--  Post Detail Model - Start -->
<div id="postDetailModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:980px">
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Post Detail - <span id="lblPostIdN"></span></b>
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
				</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" id="ddl_rankN"/>
				
				<!-- Tab start-->
				<div id="tab_details" class="">
					<ul class="nav nav-pills">
						<li class="active"><a href="#tab1_positionN" data-toggle="tab">Post Details</a></li>
						<li><a href="#tab2_fundN" data-toggle="tab">Funding Related Information</a></li>
						<li><a href="#tab3_resourcesN" data-toggle="tab">Resources Support from External</a></li>
						<li><a href="#tab5N" data-toggle="tab">Change History</a></li>
						<li><a href="#tab6N" data-toggle="tab">Strength</a></li>
					</ul>

					<div class="tab-content clearfix"
						style="margin-bottom: 5px; padding-bottom:0px;max-height:500px;overflow:auto">
						<div class="tab-pane active" id="tab1_positionN">
							<div class="row">
								<div class="col-sm-2">
									<label for="txt_unitN" class="control-label">Unit</label>
								</div>
								<div class="col-sm-4">
									<form:input path="formBean.dtlUnit" type="text"
										class="form-control" id="txt_unitN" name="txt_unitN"
										style="width:300px;"></form:input>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-2">
									<label for="ddl_post_titleN" class="control-label">Post Title<font class="star">*</font></label>
								</div>
								<div class="col-sm-4">
									<form:input path="formBean.dtlPostTitle" class="form-control" id="ddl_post_titleN" style="width:100%;" />
								</div>
								
								<div class="col-sm-2">
									<label id="lblSubSpecialtyN" for="subSpecialty" class="control-label">Sub-specialty</label>
								</div>
								<div class="col-sm-4">
									<form:select path="formBean.dtlSubSpecialtyN" class="form-control" style="width:100%;">
										<form:option value="" label="- Select -" />
										<form:options items="${subSpecialtyList}" />
									</form:select>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-4">
									<label for="lb_post_duration_title" class="control-label">Post Duration</label>
								</div>
							</div>
							<div class="clearfix"></div>
							<div class="row">
								<div class="form-group">
									<div class="col-sm-2">
										<label for="ddl_duration" class="control-label">Duration<font class="star">*</font></label>
									</div>
									<div class="col-sm-2">
										<form:select path="formBean.dtlPostDuration" 
											id="ddl_post_durationN" name="ddl_post_durationN"
											class="form-control" style="width:100%;" onchange="postDurationChangeNew(); ">
											<form:option value="" label="- Select -" />
											<form:options items="${PostDurationList}" />
										</form:select>
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-2 nopadding"><label class="control-label">Start Date</label></div>
									<div class="col-sm-2">
										<div class="input-group date">
											<form:input path="formBean.dtlPostStartDate" id="txt_postStartDateN"
												class="form-control"></form:input>
											<span class="input-group-addon"> <span
												class="glyphicon glyphicon-calendar"></span>
											</span>
										</div>
									</div>
								</div>
								<div class="col-sm-2" id="lblDurationTypeN"><label class="control-label">Duration</label></div>
								<div class="col-sm-2 nopadding">
									<div class="form-inline form-group">
										<form:radiobutton path="formBean.dtlLimitDurationType"
												id="rd_DuartionN" name="rd_DuartionN" value="DURATION_PERIOD" onclick="clickDurationNew()" ></form:radiobutton>
										<span class="form-space"></span>
										<form:input path="formBean.dtlLimitDurationNo" type="text"
											class="form-control" id="txt_limit_duration_noN"
											name="txt_limit_duration_noN" style="width:50px;"></form:input>
										<span class="form-space"></span>
										<form:select path="formBean.dtlLimitDurationUnit"
											id="ddl_limit_duration_unitN" name="ddl_limit_duration_unitN"
											class="form-control" style="width:60%;">
											<form:option value="" label="- Select -" />
											<form:option value="Y" label="Year" />
											<form:option value="M" label="Month" />
										</form:select>
									</div>
								</div>
							</div>
							
							<div class="row clearfix">
								<div class="col-sm-4"></div>
								<div class="col-sm-2 nopadding"><label class="control-label">Actual Start Date</label></div>
								<div class="col-sm-2">
									<form:input path="formBean.dtlPostActualStartDate" type="text"
										class="form-control " id="txt_post_actual_start_dateN"
										name="txt_post_actual_start_dateN" readonly="readonly"></form:input>
								</div>
								<div class="col-sm-2" id="lblDurationTypeN2"><label class="control-label">Fixed End Date</label></div>
								<div class="col-sm-2 nopadding">
									<div class="form-inline">
										<form:radiobutton path="formBean.dtlLimitDurationType" id="rd_DuartionN2N" name="rd_DuartionN"
											value="FIXED_END_DATE" onclick="clickDurationNew()" required="required"></form:radiobutton>
										<span class="form-space"></span>
										<div class="form-group">
											<div class="input-group date" id="grpLimitDurationEndDateN">
												<form:input path="formBean.dtlLimitDurationEndDate" type="text"
													class="form-control " id="txt_post_actual_end_dateN"
													name="txt_post_actual_end_dateN" required="required"></form:input>
												<span class="input-group-addon"> <span
													class="glyphicon glyphicon-calendar"></span>
												</span>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-2">
									<label for="ddl_post_remarkN" class="control-label">Remarks</label>
								</div>
								<div class="col-sm-6">
									<form:input path="formBean.dtlPostRemark" type="text"
										class="form-control " id="ddl_post_remarkN"
										name="ddl_post_remarkN" style="width:100%"></form:input>
								</div>
							</div>
							<div class="row">
								<div class="form-group">
									<div class="col-sm-2">
										<label for="ddl_postFTEN" class="control-label">FTE<font
											class="star">*</font></label>
									</div>
									<div class="col-sm-2">
										<form:select path="formBean.dtlPostFTE" class="form-control" id="ddl_postFTEN"
											style="width:100%;" onchange="changeFTE()"
											required="required">
											<form:option value="" label="- Select -" />
											<form:option value="FULL_TIME" label="Full Time" />
											<form:option value="PART_TIME" label="Part Time" />
										</form:select>
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-1">
										<form:input path="formBean.dtlPostFTEValue" type="text" id="txt_post_FTE_valueN"
											required="required" class="form-control" style="width:50px;"></form:input>
									</div>
									<div class="col-sm-4">
										<label for="txt_post_FTE_valueN">(no of net working hours per weeks / 39)</label>
									</div>
								</div>
								
								<div class="col-sm-1">
								</div>
										
								<!--  HO Buy Service -->
								<div class="col-sm-2">
									<form:checkbox path="formBean.hoBuyServiceInd" value="Y"/> <label>HO Buy Service</label>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-2">
									<label for="ddl_position_statusN" class="control-label">Post Status</label>
								</div>
								<div class="col-sm-2">
									<form:select path="formBean.dtlPositionStatus" id="ddl_position_statusN" name="ddl_position_statusN"
 												     class="form-control" style="width:100%;">
									<!-- <form:option value="" label="- Select -" /> -->
									<form:options items="${postStatusList}" />
									</form:select>
								</div>
								<div style="display: none">
									<div class="col-sm-2">Start Date</div>
									<div class="col-sm-2">
										<div class='input-group date' id='position_start_date'>
											<form:input path="formBean.dtlPositionStartDate" type="text"
												class="form-control " id="dp_position_start_dateN"
												name="dp_position_start_dateN"></form:input>
											<span class="input-group-addon"> <span
												class="glyphicon glyphicon-calendar"></span>
											</span>
										</div>
									</div>
									<div class="col-sm-2">End Date</div>
									<div class="col-sm-2">
										<div class='input-group date' id='position_end_date'>
											<form:input path="formBean.dtlPositionEndDate" type="text"
												class="form-control " id="dp_position_end_dateN"
												name="dp_position_end_dateN"></form:input>
											<span class="input-group-addon"> <span
												class="glyphicon glyphicon-calendar"></span>
											</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="delimiter"></div>
							<div class="row">
								<div class="col-sm-2">
									<label for="txt_cluster_ref_noN"
										class="control-label_opt"><strong>Cluster Reference No.</strong></label>
								</div>
								<div class="col-sm-5">
									<form:input path="formBean.dtlClusterRefNo" type="text"
										class="form-control " id="txt_cluster_ref_noN" style="width:100%"></form:input>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-2">
									<label for="txt_clusterRemarkN"
										class="control-label_opt">Additional Remarks from Cluster</label>
								</div>
								<div class="col-sm-10">
								  	<div id="txt_clusterRemarkN"
   						                 name="txt_clusterRemarkN" 
										 style="width:100%" ></div>	
								</div>
							</div>
						</div>
						<!-- ./#tab1_position -->
						<div class="tab-pane" id="tab2_fundN">
							<table id="tblFundingView" style="width: 100%">
								<tr>
									<td style="padding-bottom:20px">
										<div class="row">
											<div class="col-sm-12">
												<div class="row">
													<div class="col-sm-4">
														<div style="border-bottom: 1px solid black; width: 100%">
															<label name="lblFundingSourceView" style="font-weight: bold">Funding Source</label>
														</div>
													</div>
												</div>
											</div>
										</div>
										
										<div class="row">
											<!-- Annual Plan - Start -->
											<div class="form-group">
												<div class="col-sm-2">
													<label for="ddl_annual_plan_ind" class="control-label">Annual Plan</label>
												</div>
		
												<div class="col-sm-2">
													<form:select path="formBean.viewAnnualPlanInd"
																 class="form-control"
																 style="width:100%;">
														<form:option value="" label="- Select -" />
														<form:option value="Y" label="Yes" />
														<form:option value="N" label="No" />
													</form:select>
												</div>
											</div>
											<!-- Annual Plan - End -->
		
											<!-- Year - Start -->
											<div class="form-group">
												<div class="col-sm-2">
													<label class="control-label">Year</label>
												</div>
												<div class="col-sm-2">
													<form:select path="formBean.viewProgramYear"
																 class="form-control" style="width:100%;">
														<form:option value="" label="- Select -" />
														<form:options items="${financialYearList}" />
													</form:select>
												</div>
											</div>
											<!-- Year - End -->
		
											<!-- Program Code - Start -->
											<div class="form-group">
												<div class="col-sm-2">
													<label class="control-label">Program Code / Ref No.</label>
												</div>
												<div class="col-sm-2">
													<form:input path="formBean.viewProgramCode"
																type="text" class="form-control" name="txt_program_code"
																style="width:100%"
																data-bv-excluded="true"></form:input>
												</div>
											</div>
											<!-- Program Code - End -->
										</div>
		
										<div class="row">
											<!-- Program Name - Start -->
											<div class="form-group">
												<div class="col-sm-2">
													<label for="txt_program_name" class="control-label">Name</label>
												</div>
												<div class="col-sm-6">
													<form:input path="formBean.viewProgramName"
																type="text" class="form-control" style="width:100%"
																data-bv-excluded="true"></form:input>
												</div>
											</div>
											<!-- Program Name - End -->
		
											<!-- Program Type - Start -->
											<div class="form-group">
												<div class="col-sm-2">
													<label id="lblProgramType" for="txt_program_name" class="control-label">Program Type</label>
												</div>
												<div class="col-sm-2">
													<form:select path="formBean.viewProgramTypeCode"
																 class="form-control" style="width:100%;">
														<form:option value="" label="- Select -" />
														<form:options items="${programTypeList}" />
													</form:select>
												</div>
											</div>
											<!-- Program Type - End -->
										</div>
		
										<div class="row">
											<!--  Funding Source - Start -->
											<div class="form-group">
												<div class="col-sm-2">
													<label for="" class="control-label"><strong>Funding Source</strong></label>
												</div>
												<div class="col-sm-2">
													<form:select path="formBean.viewFundSrcId"
																 class="form-control" style="width:100%;" >
														<form:option value="" label="- Select -" />
														<form:options items="${FundingSourceList}" />
													</form:select>
												</div>
											</div>
											<!--  Funding Source - End -->
															
											<!--  Sub-category of Funding Source - Start -->
											<div class="form-group">
												<div class="col-sm-2">
													<label for="" class="control-label"><strong>Sub-category of <br/>Funding
																			Source</strong></label>
												</div>
												<div class="col-sm-4">
													<form:select path="formBean.viewFundSrcSubCatId"
																 class="form-control" style="width:100%;">
														<form:option value="" label="- Select -" />
														<form:options items="${FundingSourceSubCatList}" />
													</form:select>
												</div>
											</div>
											<!--  Sub-category of Funding Source - End -->
										</div>
		
										<div class="row">
											<!--  Start Date - Start -->
											<div class="form-group">
												<div class="col-sm-2">
													<label for="" class="control-label">Start Date</label>
												</div>
												<div class="col-sm-2">
													<div id="dp_fund_src_1st_start_date" class="input-group date">
														<form:input path="formBean.viewFundSrcStartDate"
																	type="text" class="form-control " style="width:100%"></form:input>
														<span class="input-group-addon"> 
															<span class="glyphicon glyphicon-calendar"></span>
														</span>
													</div>
												</div>
											</div>
											<!--  Start Date - End -->
		
		
											<!--  End Date - Start -->
											<div class="form-group">
												<div class="col-sm-2">
													<label for="" class="control-label">End Date</label>
												</div>
												<div class="col-sm-2">
													<div id="dp_fund_src_1st_end_date" class="input-group date">
														<form:input path="formBean.viewFundSrcEndDate"
																	type="text" class="form-control " style="width:100%"></form:input>
														<span class="input-group-addon"> 
															<span class="glyphicon glyphicon-calendar"></span>
														</span>
													</div>
												</div>
											</div>
											<!--  End Date - End -->
															
											<!--  FTE - Start -->
											<div class="form-group">
												<div class="col-sm-2">
													<label class="control-label">FTE</label>
																</div>
																<div class="col-sm-2">
																	<form:input
																		path="formBean.viewFundSrcFte"
																		type="text" class="form-control" style="width:100%"
																		required="required"></form:input>
																</div>
															</div>
											<!--  FTE - End -->
										</div>
		
										<div class="row">
											<!--  Cost Code - Start -->
											<div class="col-sm-2">
												<label class="control-label">Cost Code</label>
											</div>
											<div class="col-sm-1">
												<label class="control-label">Inst</label>
											</div>
											<div class="col-sm-2">
												<form:input path="formBean.viewInst"
															type="text" class="form-control" style="width:100%"
															data-bv-excluded="true"></form:input>
											</div>
		
											<div class="col-sm-1">
												<label class="control-label">Section</label>
											</div>
											<div class="col-sm-2">
												<form:input	path="formBean.viewSection"
															type="text" class="form-control" style="width:100%"
														    data-bv-excluded="true"></form:input>
											</div>
		
											<div class="col-sm-1">
												<label class="control-label">Analytical</label>
											</div>
											<div class="col-sm-2">
												<form:input path="formBean.viewAnalytical"
															type="text" class="form-control" style="width:100%"
															data-bv-excluded="true"></form:input>
											</div>
											<!--  Cost Code - End -->
										</div>
		
										<div class="row">
											<!--  Remark - Start -->
											<div class="col-sm-2">
												<label for="txt_program_remark" class="control-label">Remark</label>
											</div>
											<div class="col-sm-10">
												<form:textarea path="formBean.viewFundSrcRemark"
															type="text" class="form-control"
															style="width:100%;height:60px"
															data-bv-excluded="true"></form:textarea>
											</div>
											<!--  Remark - End -->
										</div>
									</td>
								</tr>
							</table>
						</div>
						<!-- ./#tab2_fund -->
						<div class="tab-pane" id="tab3_resourcesN">
							<div class="row">
								<div class="col-sm-3">
									<label for="ddl_res_sup_fr_extN" class="field_request_label">Resources support from external</label>
								</div>
								<div class="col-sm-6">
									<form:select path="formBean.dtlRes_sup_fr_ext"
										id="ddl_res_sup_fr_extN" name="ddl_res_sup_fr_extN"
										class="form-control" style="width:200px;">
										<form:option value="" label="- Select -" />
										<form:options items="${ExternalSupportList}" />
									</form:select>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-3">
									<label for="txt_res_sup_remarkN" class="field_request_label">Remark</label>
								</div>
								<div class="col-sm-6">
									<form:input path="formBean.dtlRes_sup_remark" type="text"
										class="form-control " id="txt_res_sup_remarkN"
										name="txt_res_sup_remarkN" style="width:100%"></form:input>
								</div>
							</div>
						</div><!-- ./#tab3_resources -->
						<!-- Change History tab -->
						<div class="tab-pane" id="tab5N">
							<!-- 01 Upgrade History -->
							<div id="secUpgradeHist">
								<H4>Upgrade</H4>
								<table id="tblUpgradeHist" class="display cell-border mprs_table">
									<thead>
										<tr>
											<th>Request ID</th>
											<th>Effective Date</th>
											<th>From / To</th>
											<th>Post ID</th>
											<th>Department</th>
											<th>Rank</th>
											<th>Position Duration</th>
											<th>Position Type</th>
										</tr>
									</thead>
								</table>
							</div>
							<div id="secExtensionHist">
								<!-- Extension History -->
								<H4>Extension</H4>
								<table id="tblExtensionHist" class="display cell-border mprs_table">
									<thead>
										<tr>
											<th>Request ID</th>
											<th>Transaction Date</th>
											<th>Original End Date</th>
											<th>Revised End Date</th>
										</tr>
									</thead>
								</table>
							</div>
							<div id="secDeletionHist">
							<!-- Deletion History -->
								<H4>Deletion</H4>
								<table id="tblDeletionHist" class="display cell-border mprs_table">
									<thead>
										<tr>
											<th>Request ID</th>
											<th>Transaction Date</th>
											<th>Effective Date</th>
											<th>Reason of Deletion</th>
										</tr>
									</thead>
								</table>
							</div>
							<div id="secChangeFundingHist">
							<!-- Change of Funding History -->
								<H4>Change of Funding</H4>
								<table id="tblChangeFundingHist" class="display cell-border mprs_table">
									<thead>
										<tr>
											<th>Request ID</th>
											<th>Transaction Date</th>
											<th>Reason of Change</th>
										</tr>
									</thead>
								</table>
							</div>
							<div id="secSuppHist">
							<!-- Supplementary History -->
								<H4>Supplementary Promotion</H4>
								<table id="tblSuppHist" class="display cell-border mprs_table">
									<thead>
										<tr>
											<th>Request ID</th>
											<th>Transaction Date</th>
											<th>Effective Date</th>
											<th>Remark</th>
										</tr>
									</thead>
								</table>
							</div>
							<div id="secFrozenHist">
							<!-- Frozen Post History -->
								<H4>Frozen Post</H4>
								<table id="tblFrozenHist" class="display cell-border mprs_table">
									<thead>
										<tr>
											<th>Request ID</th>
											<th>Transaction Date</th>
											<th>Effective Date</th>
											<th>Frozen End Date</th>
											<th>Reason of Frozen</th>
										</tr>
									</thead>
								</table>
							</div>
							<div id="secChangeStaffMixHist">
							<!-- Change of staff mix History -->
								<H4>Change of Staff Mix</H4>
								<table id="tblChangeStaffMixHist"
									class="display cell-border mprs_table">
									<thead>
										<tr>
											<th>Request ID</th>
											<th>Effective Date</th>
											<th>From / To</th>
											<th>Post ID</th>
											<th>Department</th>
											<th>Rank</th>
											<th>Position Duration</th>
											<th>Position Type</th>
										</tr>
									</thead>
								</table>
							</div>
							<div id="secNoChangeHist" style="text-align: center;">No transaction found.</div>
						</div>
						<div class="tab-pane" id="tab6N">
							<!-- Strength/Assignment History -->
							<!-- <H4>Strength</H4>  -->
							<H5>Assignment History</H5>
							<table id="tblAssignmentHist" class="table table-striped table-bordered">
								<thead>
									<tr>
										<th>Employee Name</th>
										<th>Ranks</th>
										<th>Emp Type</th>
										<th>Position Title</th>
										<th>FTE</th>
										<th>Start Date</th>
										<th>End Date</th>
										<th>Reason of Leaving</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
				<!-- ./#tab_details -->
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<!--  Post Detail Model - End -->