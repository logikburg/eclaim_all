<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	var tblUpgradeHist, tblExtensionHist, tblDeletionHist, tblChangeFundingHist, tblSuppHist, tblFrozenHist, tblChangeStaffMixHist, tblAssignmentHist, tblFTEAdjustmentHist;
	var tblAttachment;

	function showDelete(requestNo) {
		document.location.href = "<%=request.getContextPath() %>/request/deletion?rq=" + requestNo;
	}
	
	function showFrozen(requestNo) {
		document.location.href = "<%=request.getContextPath() %>/request/frozen?rq=" + requestNo;
	}
	
	function showNew(requestNo) {
		document.location.href = "<%=request.getContextPath() %>/request/newPost?rq=" + requestNo;
	}
	
	function showChangeFunding(requestNo) {
		document.location.href = "<%=request.getContextPath() %>/request/changeOfFunding?rq=" + requestNo;
	}
	
	function showExtension(requestNo) {
		document.location.href = "<%=request.getContextPath() %>/request/extension?rq=" + requestNo;
	}
	
	function showSuppProm(requestNo) {
		document.location.href = "<%=request.getContextPath() %>/request/suppPromotion?rq=" + requestNo;
	}
	
	function showChangeStaffMix(requestNo) {
		document.location.href = "<%=request.getContextPath() %>/request/changeStaffMix?rq=" + requestNo;
	}
	
	function showFTEAdjustment(requestNo) {
		document.location.href = "<%=request.getContextPath() %>/request/fteAdjustment?rq=" + requestNo;
	}
	
	function showUpgrade(requestNo) {
		document.location.href = "<%=request.getContextPath() %>/request/upgrade?rq=" + requestNo;
	}
	
	function downloadAttachment(attUid) {
		document.location.href = "<%= request.getContextPath() %>/request/downloadAttachment?aid=" + attUid;
	}
	
	function removeAttachment(attUid) {
		$("#attUid").val(attUid);
		$("#model_confirmRemove").modal("show");
	}
	
	function confirmRemoveAttachment() {
		showLoading();		
		
		$.ajax({dataType: 'json',
            url: "<%=request.getContextPath()%>/common/removePostFile",
            data: {attUid: $("#attUid").val()},
            type: "POST",
            success: function(result) {
           		// hideLoading();
				$("#postDetailModal").modal("hide");
				
				setTimeout(function() {    		
           			showDetail($("#currentUid").val(), $("#currentSnapUid").val(), 'Y');
           		}, 1000);
           	},
           	error: function(result){
               	alert('error'+JSON.stringify(result));
           	}
		});
	}
	
	function processFileUpload(row) {
		if ($(row).find("#approvalFile").val() == "") {
			alert("Please select the file before press the upload button.");
			return;
		}

		showLoading();		
		var oMyForm = new FormData();
		oMyForm.append("approvalFile", $($(row).find("#approvalFile"))[0].files[0]);
		oMyForm.append("postUId", $("#currentUid").val());
     	$.ajax({dataType: 'json',
            	url: "<%=request.getContextPath()%>/common/uploadPostFile",
            	data: oMyForm,
            	type: "POST",
            	enctype: 'multipart/form-data',
            	processData: false, 
            	contentType: false,
            	success: function(result) {
					$("#postDetailModal").modal("hide");    		
					setTimeout(function() {    		
           				showDetail($("#currentUid").val(), $("#currentSnapUid").val(), 'Y');
           			}, 1000);
            	},
            	error: function(result){
            		hideLoading();
               		alert("Fail to upload.");
            	}
		});
  	}

	$(document).ready(function(){
		// Disable all field in "postDetailModal"
		$("#postDetailModal input").attr("disabled", true);
		$("#postDetailModal select").attr("disabled", true);
		$("#postDetailModal textarea").attr("disabled", true);
		
		tblUpgradeHist = $("#tblUpgradeHist").DataTable();
		tblExtensionHist = $("#tblExtensionHist").DataTable();		
		tblDeletionHist = $("#tblDeletionHist").DataTable();
		tblChangeFundingHist = $("#tblChangeFundingHist").DataTable();
		tblSuppHist = $("#tblSuppHist").DataTable();
		tblFrozenHist = $("#tblFrozenHist").DataTable();
		tblChangeStaffMixHist = $("#tblChangeStaffMixHist").DataTable();
		tblAssignmentHist = $("#tblAssignmentHist").DataTable();
		tblFTEAdjustmentHist = $("#tblFTEAdjustmentHist").DataTable();
		tblAttachment = $("#tblAttachment").DataTable();
	});
		
	function performSearch() {
		showLoading();
		$("#frmDetail").submit();
	}
	
	function performReset() {
		document.location.href = "<%= request.getContextPath() %>/request/enquiry";
	}
	
	function showDetail(postUid, postSnapUid, effectiveDate, fromUpload) {
		if (fromUpload != "Y") {
			showLoading();
		}
		$.ajax({
            url: "<%=request.getContextPath() %>/api/request/getPostDetailsWithSnap",
            cache: false,
            type: "POST",
            data: {searchPostNo: postUid, searchPostSnapUid: postSnapUid, searchEffectiveDate: effectiveDate},
            success: function(data) {
            	$("#currentUid").val(postUid);
            	$("#currentSnapUid").val(postSnapUid);
            
            	$("#lblPostId").text(data.postId);
                       
            	//Position info
            	$('#txt_unit').val(data.unit);	
            	$('#ddl_post_title').val(data.postTitle);
				$('#ddl_post_duration').val(data.postDuration);
				$('#txt_postStartDate').val(data.postStartDate);
				if (data.limitDurationType!=null && data.limitDurationType=="FIXED_END_DATE") {
					$("#rd_Duartion2").prop('checked', 'checked');	
				}
				else { 
					$("#rd_Duartion").prop('checked', 'checked');
				}	
				$('#txt_limit_duration_no').val(data.limitDurationNo);
				$('#ddl_limit_duration_unit').val(data.limitDurationUnit);
				$('#txt_post_actual_start_date').val(data.postActualStartDate);
				
				if ($("#rd_Duartion2").prop('checked') == true) {
					$('#txt_post_actual_end_date').val(data.limitDurationEndDate);
				}
				else {
					$('#txt_post_actual_end_date').val("");
				}
				$('#ddl_post_remark').val(data.postRemark);
				$('#ddl_postFTE').val(data.postFTE);
				$('#txt_post_FTE_value').val(data.postFTEValue);
				$('#ddl_position_status').val(data.positionStatus);
				$('#dp_position_start_date').val(data.positionStartDate);
				$('#dp_position_end_date').val(data.positionEndDate);
				$('#txt_cluster_ref_no').val(data.clusterRefNo);
				if(data.clusterRemark != null){
					$('#txt_clusterRemark').html(data.clusterRemark.replace(/(?:\r\n|\r|\n)/g, "<br/>"));
				} else {
					$('#txt_clusterRemark').html("");
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
				
            	$('#ddl_res_sup_fr_ext').val(data.res_sup_fr_ext);
				$('#txt_res_sup_remark').val(data.res_sup_remark);
				
				$('#txt_proposed_post_id').val(data.proposed_post_id);
				$('#txt_post_id_just').val(data.post_id_just);
				
				$("#ddl_rank").val(data.rankCode);
				$("#subSpecialty").val(data.subSpecialty);
				
				tblUpgradeHist.clear();
				tblExtensionHist.clear();
				tblDeletionHist.clear();
				tblChangeFundingHist.clear();
				tblSuppHist.clear();
				tblFrozenHist.clear();
				tblChangeStaffMixHist.clear();
				tblAssignmentHist.clear();
				tblAttachment.clear();
				tblFTEAdjustmentHist.clear();
				
				// Enable and clear the attachment input file box
				$("#approvalFile").val("");
				$("#approvalFile").attr("disabled", false);
				
            	//upgarde hist
				if (data.upgradeListSize > 0) {
            		for (var i=0; i<data.upgradeListSize; i++) {
						tblUpgradeHist.row.add([
							 "<td name='upgradeRequestID'>"+ data.upgradeHistRequestList_RequestID[i] +"</td>",
							"<td name='upgradeRequestEffDate'><span style='display:none'>" + formatDateForSort(data.upgradeHistRequestList_RequestEffDate[i]) + "</span>"+ data.upgradeHistRequestList_RequestEffDate[i] +"</td>",
							"<td name='upgradeRequestPostID'>"+ data.upgradeHistRequestList_RequestPostID[i] +"</td>",
							"<td name='upgradeRequestReason'>"+ data.upgradeHistRequestList_Reason[i] +"</td>",
							"<td style='text-align:center'><button type='button' class='btn btn-primary' onclick='showUpgrade(\"" + data.upgradeHistRequestList_RequestUid[i] + "\")'>View Detail</button></td>"
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
						 	"<td name='extensionTransDate'><span style='display:none'>" + formatDateForSort(data.extensionHistRequestList_RequestTransDate[i]) + "</span>"+ data.extensionHistRequestList_RequestTransDate[i] +"</td>",
						 	"<td name='extensionOrgStartDate'><span style='display:none'>" + formatDateForSort(data.extensionHistRequestList_RequestOrgStartDate[i]) + "</span>"+ data.extensionHistRequestList_RequestOrgStartDate[i] +"</td>",
							"<td name='extensionRevisedStartDate'><span style='display:none'>" + formatDateForSort(data.extensionHistRequestList_RequestRevisedStartDate[i]) + "</span>"+ data.extensionHistRequestList_RequestRevisedStartDate[i] +"</td>",
							"<td name='extensionOrgEndDate'><span style='display:none'>" + formatDateForSort(data.extensionHistRequestList_RequestOrgEndDate[i]) + "</span>"+ data.extensionHistRequestList_RequestOrgEndDate[i] +"</td>",
							"<td name='extensionRevisedEndDate'><span style='display:none'>" + formatDateForSort(data.extensionHistRequestList_RequestRevisedEndDate[i]) + "</span>"+ data.extensionHistRequestList_RequestRevisedEndDate[i] +"</td>",
							"<td style='text-align:center'><button type='button' class='btn btn-primary' onclick='showExtension(\"" + data.extensionHistRequestList_RequestUid[i] + "\")'>View Detail</button></td>"
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
							"<td name='deletionRequestTransDate'><span style='display:none'>" + formatDateForSort(data.deletionHistRequestList_RequestTransDate[i]) + "</span>"+ data.deletionHistRequestList_RequestTransDate[i] +"</td>",
							"<td name='deletionRequestEffDate'><span style='display:none'>" + formatDateForSort(data.deletionHistRequestList_RequestEffDate[i]) + "</span>"+ data.deletionHistRequestList_RequestEffDate[i] +"</td>",
							"<td name='deletionRequestReason'>"+ data.deletionHistRequestList_RequestReason[i] +"</td>",
							"<td style='text-align:center'><button type='button' class='btn btn-primary' onclick='showDelete(\"" + data.deletionHistRequestList_RequestUid[i] + "\")'>View Detail</button></td>"
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
							"<td name='changeFundingRequestTransDate'><span style='display:none'>" + formatDateForSort(data.changeFundHistRequestList_RequestTransDate[i]) + "</span>"+ data.changeFundHistRequestList_RequestTransDate[i] +"</td>",
							"<td name='changeFundingRequestReason'>"+ data.changeFundHistRequestList_RequestReason[i] +"</td>",
							"<td style='text-align:center'><button type='button' class='btn btn-primary' onclick='showChangeFunding(\"" + data.changeFundHistRequestList_RequestUid[i] + "\")'>View Detail</button></td>"
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
							"<td name='supplementaryRequestTransDate'><span style='display:none'>" + formatDateForSort(data.suppPromoHistRequestList_RequestTransDate[i]) + "</span>"+ data.suppPromoHistRequestList_RequestTransDate[i] +"</td>",
							"<td name='supplementaryRequestEffDate'><span style='display:none'>" + formatDateForSort(data.suppPromoHistRequestList_RequestEffDate[i]) + "</span>"+ data.suppPromoHistRequestList_RequestEffDate[i] +"</td>",
							"<td name='supplementaryRequestRemark'>"+ data.suppPromoHistRequestList_RequestRemark[i] +"</td>",
							"<td style='text-align:center'><button type='button' class='btn btn-primary'  onclick='showSuppProm(\"" + data.suppPromoHistRequestList_RequestUid[i] + "\")'>View Detail</button></td>"
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
							"<td name='frozenRequestTransDate'><span style='display:none'>" + formatDateForSort(data.frozenHistRequestList_RequestTransDate[i]) + "</span>"+ data.frozenHistRequestList_RequestTransDate[i] +"</td>",
							"<td name='frozenRequestEffStartDate'><span style='display:none'>" + formatDateForSort(data.frozenHistRequestList_RequestEffDate[i]) + "</span>"+ data.frozenHistRequestList_RequestEffDate[i] +"</td>",
							"<td name='frozenRequestEffEndDate'><span style='display:none'>" + formatDateForSort(data.frozenHistRequestList_RequestFrozenEndDate[i]) + "</span>"+ data.frozenHistRequestList_RequestFrozenEndDate[i] +"</td>",
							"<td name='frozenRequestReason'>"+ data.frozenHistRequestList_RequestReason[i] +"</td>",
							"<td style='text-align:center'><button type='button' class='btn btn-primary'  onclick='showFrozen(\"" + data.frozenHistRequestList_RequestUid[i] + "\")'>View Detail</button></td>"
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
							"<td name='changeStaffMixRequestEffDate'><span style='display:none'>" + formatDateForSort(data.staffMixHistRequestList_RequestEffDate[i]) + "</span>"+ data.staffMixHistRequestList_RequestEffDate[i] +"</td>",
							"<td name='changeStaffMixRequestPostID'>"+ data.staffMixHistRequestList_RequestPostID[i] +"</td>",
							"<td name='changeStaffMixRequestReason'>"+ data.staffMixHistRequestList_Reason[i] +"</td>",
							"<td style='text-align:center'><button type='button' class='btn btn-primary' onclick='showChangeStaffMix(\"" + data.staffMixHistRequestList_RequestUid[i] + "\")'>View Detail</button></td>"
						]);
	            	}
	            	
	            	$("#secChangeStaffMixHist").show();
            	}
            	else {
            		$("#secChangeStaffMixHist").hide();
            	}
            	
            	if (data.fteAdjustmentListSize > 0) {
            		for (var i=0; i<data.fteAdjustmentListSize; i++) {
						tblFTEAdjustmentHist.row.add([
							"<td name='fteAdjustmentRequestID'>"+ data.fteAdjustmentHistRequestList_RequestID[i] +"</td>",
							"<td name='fteAdjustmentRequestTransDate'><span style='display:none'>" + formatDateForSort(data.fteAdjustmentHistRequestList_RequestTransDate[i]) + "</span>"+ data.fteAdjustmentHistRequestList_RequestTransDate[i] +"</td>",
							"<td name='fteAdjustmentRequestReason'>"+ data.fteAdjustmentHistRequestList_RequestReason[i] +"</td>",
							"<td style='text-align:center'><button type='button' class='btn btn-primary' onclick='showFTEAdjustment(\"" + data.fteAdjustmentHistRequestList_RequestUid[i] + "\")'>View Detail</button></td>"
						]);	
	            	}
	            	
	            	$("#secFTEAdjustmentHist").show();
            	}
            	else {
            		$("#secFTEAdjustmentHist").hide();
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
							"<td name='assignmentStartDate'><span style='display:none'>" + formatDateForSort(data.assignmentHistList_StartDate[i]) + "</span>"+ data.assignmentHistList_StartDate[i] +"</td>",
							"<td name='assignmentEndDate'><span style='display:none'>" + formatDateForSort(data.assignmentHistList_EndDate[i]) + "</span>"+ data.assignmentHistList_EndDate[i] +"</td>",
							"<td name='assignmentLeaveReason'>"+ data.assignmentHistList_LeaveReason[i] +"</td>",
							"<td style='text-align:center'><input name='btnAssignmentView' type='button' value='View Detail' class='btn btn-primary' onclick='viewStrengthDetail(\""
									+ data.assignmentHistList_AssignmentNumber[i] + "\", \"" + data.assignmentHistList_StartDate[i]
									+ "\")' style='margin-right:5px,'/></td>"
						]);
	            	}
            	}
            	
            	// Attachment 
            	//assignment hist
				if (data.attachmentListSize > 0) {
            		for (var i=0; i<data.attachmentListSize; i++) {
						tblAttachment.row.add([
							"<td>"+ data.attachmentNameList[i] +"</td>",
							"<td style='text-align:center'><button name='btnDownload' type='button' class='btn btn-primary' onclick='downloadAttachment(\"" + data.attachmentUidList[i] + "\")'>Download</button>" +
							"<button name='btnRemove' type='button' class='btn btn-danger' onclick='removeAttachment(\"" + data.attachmentUidList[i] + "\")'>Delete</button>" + "</td>"
						]);
	            	}
            	}
            	
            	if (data.upgradeListSize == 0 && data.extensionListSize == 0 && data.deletionListSize == 0 
            		&& data.changeFundingListSize == 0 && data.suppPromotionListSize == 0 && data.frozenListSize == 0 
            		&& data.changeStaffMixListSize == 0 && data.fteAdjustmentListSize == 0) {	
            		$("#secNoChangeHist").show();
            	}
            	else {
            		$("#secNoChangeHist").hide();
            	}
				
				tblUpgradeHist.draw();
				tblExtensionHist.draw();
				tblDeletionHist.draw();
				tblChangeFundingHist.draw();
				tblSuppHist.draw();
				tblFrozenHist.draw();
				tblChangeStaffMixHist.draw();
				tblAssignmentHist.draw();
				tblAttachment.draw();
				tblFTEAdjustmentHist.draw();

				changeRankDropdown();
				changeAnnualPlan();
				postDurationChange();
				
				$("#relatedHcmEffectiveStartDate").html(data.relatedHcmEffectiveStartDate);
				$("#relatedHcmFTE").html(data.relatedHcmFTE);
				$("#relatedHcmHeadCount").html(data.relatedHcmHeadCount);
				$("#relatedHcmPositionName").html(data.relatedHcmPositionName);
				$("#relatedHcmHiringStatus").html(data.relatedHcmHiringStatus);
				$("#relatedHcmType").html(data.relatedHcmType);
				
				setTimeout(function() {
					hideLoading();
	
					// Open the post dialog
					$("#postDetailModal").modal("show");
				}, 1000);
			}
		});
	}
	
	function changeRankDropdown() {
		$("#lblSubSpecialty").show();
		$("#subSpecialty").show();
	}
	
	function changeAnnualPlan() {
		if ($("#ddl_annual_plan_ind").val() != "Y") {
			$("#starProgramCode").hide();
			$("#starYear").hide();
			$("#starName").hide();
		}
		else {
			$("#starProgramCode").show();
			$("#starYear").show();
			$("#starName").show();
		}
	}
	
	function postDurationChange() {
		// If Recurrent - disable the duration and fixed end date
		if ($("#ddl_post_duration").val() == "R") {
			$("#rd_Duartion").attr("disabled", true);
			$("#rd_Duartion2").attr("disabled", true);
			$("#ddl_limit_duration_unit").attr("disabled", true);
			$("#txt_limit_duration_no").attr("disabled", true);
			$("#txt_post_actual_end_date").attr("disabled", true);
			
			$("#rd_Duartion").hide();
			$("#rd_Duartion2").hide();
			$("#ddl_limit_duration_unit").hide();
			$("#txt_limit_duration_no").hide();
			$("#grpLimitDurationEndDate").hide();
			$("#lblDurationType").hide();
			$("#lblDurationType2").hide();
		}
		else {
			$("#rd_Duartion").show();
			$("#rd_Duartion2").show();
			$("#ddl_limit_duration_unit").show();
			$("#txt_limit_duration_no").show();
			$("#grpLimitDurationEndDate").show();
			$("#lblDurationType").show();
			$("#lblDurationType2").show();
		}
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
	
	function viewStrengthDetail(assignmentNumber, effectiveStartDate) {
		showLoading();
		$.ajax ({
            url: "<%=request.getContextPath() %>/hcm/getAssignmentDetail",
            type: "POST",
            data: {assignmentNumber: assignmentNumber, effectiveStartDate: effectiveStartDate},
            success: function(data) {
	            $("#strengthEmployeeName").text(data.fullName);
	            $("#strengthEmpType").text(data.employmentType == null ? "" : data.employmentType);
	            $("#strengthRank").text(data.rank);
	            $("#strengthPositionTitle").text(data.positionTitle);
	            $("#strengthFTE").text(data.fte);
	            $("#strengthHeadCount").text(data.head);
	            $("#strengthStartDate").text(data.effectiveStartDateStr);
	            $("#strengthEndDate").text(data.effectiveEndDateStr);
	            $("#strengthLeaveReason").text(data.reasonOfLeaving == null ? "" : data.reasonOfLeaving);
	            $("#strengthActualTermDate").text(data.actualTerminationDateStr);
	            $("#strengthPositionName").text(data.positionName);
	            $("#strengthChangeReason").text(data.changeReasonMeaning == null ? "" : data.changeReasonMeaning);
	            $("#strengthPrimaryFlag").text(data.primaryFlag);
	            $("#strengthEmployeeCategory").text(data.employeeCategory == null ? "" : data.employeeCategory);
	            $("#payzone").text(data.payzone == null ? "" : data.payzone);
            
            	$("#strengthDetailModal").modal("show");
            	hideLoading();
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
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
		
		<c:if test="${formBean.haveResult == 'Y'}">
			$('#searchCriteria').collapse('toggle');
		</c:if>
	});

	function showApprovalInformation() {
		$.ajax({
            url: "<%=request.getContextPath() %>/api/request/getApprovalInfo",
            cache: false,
            type: "POST",
            data: {searchPostSnapUid: $("#currentSnapUid").val()},
            success: function(data) {
            	$("#approvalInfoModel #requestApprovalReference").val(data.requestApprovalReference);
            	$("#approvalInfoModel #requestApprovalDate").val(data.requestApprovalDate);
            	$("#approvalInfoModel #requestApprovalRemark").val(data.requestApprovalRemark);
            	
				$("#approvalInfoModel").modal("show");
				
				hideLoading();
			}
		});
	}
	
	function editClusterRemark() {
		showLoading();
		$.ajax({
            url: "<%=request.getContextPath() %>/api/request/getClusterRemark",
            cache: false,
            type: "POST",
            data: {searchPostSnapUid: $("#currentSnapUid").val()},
            success: function(data) {
            	$("#editClusterModel #clusterReferenceNo").val(data.clusterRefNo);
            	$("#editClusterModel #additionRemark").val(data.clusterRemark)
				$("#editClusterModel").modal("show");
				
				hideLoading();
			}
		});
	}
	
	function updateClusterRemark() {
		showLoading();
		$.ajax({
            url: "<%=request.getContextPath() %>/api/request/saveClusterRemark",
            cache: false,
            type: "POST",
            data: {searchPostSnapUid: $("#currentSnapUid").val(), 
                   clusterRef: $("#editClusterModel #clusterReferenceNo").val(), 
                   clusterRemark: $("#editClusterModel #additionRemark").val()
                   },
            success: function(data) {
            	$("#editClusterModel").modal("hide");
				
				$("#postDetailModal #txt_cluster_ref_no").val($("#editClusterModel #clusterReferenceNo").val());
				$('#postDetailModal #txt_clusterRemark').html($("#editClusterModel #additionRemark").val().replace(/(?:\r\n|\r|\n)/g, "<br/>"));
				
				hideLoading();
			}
		});
	}

	function addNewFundingView () {
		// Clone the last row
		var x = $("#tblFundingView tr:last").clone();
		
		// Initial the value
		$(x).find("select").val("");
		$(x).find("input").val("");
		
		// Remove the option for program type
		$(x).find("select[name$='programTypeCode']").empty();
        var option = "<option value=''> - Select - </option>";
        $(x).find("select[name$='programTypeCode']").append(option);
	
		// UT29997 Set default value for post funding start date  
		$(x).find("input[name$='fundSrcStartDate']").val($("#txt_postStartDate").val());
			
		$("#tblFundingView tr:last").after(x);
	
		// Refresh the name of each component
		for (var i=0; i<$("#tblFundingView tr").length; i++) {
			// Annual Plan
			$($("#tblFundingView select[name$='viewAnnualPlanInd']")[i]).attr("name", "requestFundingList[" + i + "].viewAnnualPlanInd");
			$($("#tblFundingView select[name$='viewAnnualPlanInd']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].viewAnnualPlanInd");		
			
			// Program Year
			$($("#tblFundingView select[name$='viewProgramYear']")[i]).attr("name", "requestFundingList[" + i + "].viewProgramYear");
			$($("#tblFundingView select[name$='viewProgramYear']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].viewProgramYear");		
			
			// Program Code
			$($("#tblFundingView input[name$='viewProgramCode']")[i]).attr("name", "requestFundingList[" + i + "].viewProgramCode");
			
			// Program Name
			$($("#tblFundingView input[name$='viewProgramName']")[i]).attr("name", "requestFundingList[" + i + "].viewProgramName");
			$($("#tblFundingView input[name$='viewProgramName']")[i]).attr("id", "requestFundingList" + i + "viewProgramName");
			
			// Program Type
			$($("#tblFundingView select[name$='viewProgramTypeCode']")[i]).attr("name", "requestFundingList[" + i + "].viewProgramTypeCode");
			
			// Funding Source
			$($("#tblFundingView select[name$='viewFundSrcId']")[i]).attr("name", "requestFundingList[" + i + "].viewFundSrcId");
			$($("#tblFundingView select[name$='viewFundSrcId']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].viewFundSrcId");		
			
			// Start Date
			$($("#tblFundingView input[name$='viewFundSrcStartDate']")[i]).attr("name", "requestFundingList[" + i + "].viewFundSrcStartDate");
			$($("#tblFundingView input[name$='viewFundSrcStartDate']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].viewFundSrcStartDate");		
			
			// End Date
			$($("#tblFundingView input[name$='viewFundSrcEndDate']")[i]).attr("name", "requestFundingList[" + i + "].viewFundSrcEndDate");

			// FTE			
			$($("#tblFundingView input[name$='viewFundSrcFte']")[i]).attr("name", "requestFundingList[" + i + "].viewFundSrcFte");
			$($("#tblFundingView input[name$='viewFundSrcFte']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].viewFundSrcFte");
	
			// Remark
			$($("#tblFundingView textarea[name$='viewFundSrcRemark']")[i]).attr("name", "requestFundingList[" + i + "].viewFundSrcRemark");
			
			// Inst
			$($("#tblFundingView input[name$='viewInst']")[i]).attr("name", "requestFundingList[" + i + "].viewInst");
			
			// Section
			$($("#tblFundingView input[name$='viewSection']")[i]).attr("name", "requestFundingList[" + i + "].viewSection");
			
			// Analytical
			$($("#tblFundingView input[name$='viewAnalytical']")[i]).attr("name", "requestFundingList[" + i + "].viewAnalytical");
			
		}
		
		// Refresh the label for funding source
		refreshFundingSourceLabelView();
		
	}
	
	function refreshFundingSourceLabelView() {
		for (var m=0; m<$("label[name='lblFundingSourceView']").length; m++) { 
			$($("label[name='lblFundingSourceView']")[m]).text("Funding Source " + (m+1));
		}
	}
</script>

<!-- Page Content -->
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href='<c:url value="/home/home"/>'><i class="fa fa-home"></i>Home</a> > Enquiry
		</div>
		<div class="section-title">
			<div class="title pull-left"><i class="fa fa-question-circle"></i>Post Enquiry</div>
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
								<label for="" class="field_request_label">Cluster</label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.clusterCode" name="clusterCode"
									class="form-control" onchange="changeCluster()">
									<option value="">- Select -</option>
									<form:options items="${clusterList}" />
								</form:select>
							</div>
							<div class="col-sm-2">
								<label for="" class="field_request_label">Institution</label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.instCode" name="instCode"
									class="form-control">
									<option value="">- Select -</option>
									<form:options items="${instList}" />
								</form:select>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-2">
								<label for="" class="field_request_label">Department</label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.deptCode" name="deptCode"
									class="form-control">
									<option value="">- Select -</option>
									<form:options items="${deptList}" />
								</form:select>
							</div>
							<div class="col-sm-2">
								<label for="" class="field_request_label">Staff Group</label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.staffGroupCode" name="staffGroupCode"
									class="form-control">
									<option value="">- Select -</option>
									<form:options items="${staffGroupList}" />
								</form:select>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-2">
								<label for="position_org" class="field_request_label">Rank</label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.rankCode" name="rankCode"
									class="form-control">
									<option value="">- Select -</option>
									<form:options items="${rankList}" />
								</form:select>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-2">
								<label for="position_org" class="field_request_label">Post ID</label>
							</div>
							<div class="col-sm-4">
								<form:input path="formBean.postId" class="form-control" />
							</div>
							<div class="col-sm-2">
								<label for="position_org" class="field_request_label">Effective Date</label>
							</div>
							<div class="col-sm-4">
								<div class='input-group date' id='datetimepicker1'>
									<form:input path="formBean.effectiveDate" class="form-control" />
									<span
										class="input-group-addon"> <span
										class="glyphicon glyphicon-calendar"></span>
									</span>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-2">
								<label for="position_org" class="field_request_label">Employee ID</label>
							</div>
							<div class="col-sm-4">
								<form:input path="formBean.employeeId" class="form-control" />
							</div>
							<div class="col-sm-2">
								<label for="position_org" class="field_request_label">Reference from Approval Information</label>
							</div>
							<div class="col-sm-4">
								<form:input path="formBean.approvalReference" class="form-control" />
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
								<th style="10px">&nbsp;</th>
								<th>Post ID</th>
								<th>Unit</th>
								<th>Annual Plan</th>
								<th>Duration</th>
								<th>FTE</th>
								<th>Duration Start Date</th>
								<th>Duration End Date</th>
								<th>Status</th>
								<th>Occupied Employee ID</th>
								<th class="no-sort" style="width: 100px">Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="searchResult" items="${formBean.searchResultList}">
								<tr>
									<td><c:out value="${searchResult.extraInfo}"/></td>
									<td><c:out value="${searchResult.postId}"/></td>
									<td><c:out value="${searchResult.unit}"/></td>
									<td><c:out value="${searchResult.annualPlan}"/></td>
									<td><c:out value="${searchResult.postDuration}"/></td>
									<td><c:out value="${searchResult.postFTE}"/></td>
									<td data-order='<fmt:formatDate value="${searchResult.postStartDate}" pattern="yyyyMMdd" />'>
										<fmt:formatDate value="${searchResult.postStartDate}" pattern="dd/MM/yyyy" /></td>
									
										<c:if test="${searchResult.limitDurationUnit == null}" >
										<td data-order='<fmt:formatDate value="${searchResult.limitDurationEndDate}" pattern="yyyyMMdd"/>'>
											<fmt:formatDate value="${searchResult.limitDurationEndDate}" pattern="dd/MM/yyyy"/>
										</td> 
										</c:if>
										
										<c:if test="${searchResult.limitDurationUnit != null}" >
										<td>
											<c:out value="${searchResult.limitDurationNo}"/> 
											<c:out value="${searchResult.limitDurationUnit}"/>
										</td>
										</c:if>
									
									<td><c:out value="${searchResult.postStatusDesc}"/></td>
									<td><c:out value="${searchResult.employeeID}"/></td>
									<td><button type="button" onclick="showDetail('<c:out value="${searchResult.postUid}"/>', '<c:out value="${searchResult.postSnapUid}"/>', '<c:out value="${formBean.effectiveDate}"/>', 'N');"
										class="btn btn-primary">Detail</button></td>
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


<!--  Post Detail Model - Start -->
<div id="postDetailModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:980px">
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Post Detail - <label id="lblPostId"></label></b>
			    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
			    </h4>
			</div>
			<div class="modal-body">
				<!-- Tab start-->
				<div id="tab_details" class="">
					<ul class="nav nav-pills">
						<li class="active"><a href="#tab1_position" data-toggle="tab">Post Details</a></li>
						<li><a href="#tab2_fund" data-toggle="tab">Funding Related Information</a></li>
						<li><a href="#tab3_resources" data-toggle="tab">Resources Support from External</a></li>
						<li><a href="#tab5" data-toggle="tab">Change History</a></li>
						<li><a href="#tab6" data-toggle="tab">Strength</a></li>
						<li><a href="#tabAttachment" data-toggle="tab">Attachment</a></li>
					</ul>

					<div class="tab-content clearfix" style="max-height:500px;overflow:auto">
						<div class="tab-pane active" id="tab1_position">
							<div class="row">
							<table id="tblHCMResult" class="table table-bordered mprs_table" style="border: solid 1px #DDD">
								<thead>
									<tr>
										<th style="width:100px">Effective Date</th>
										<th style="width:300px">Position name</th>
										<th style="width:80px">FTE</th>
										<th style="width:80px">Headcount</th>
										<th style="width:100px">Hiring Status</th>
										<th style="width:100px">Type</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td><label id="relatedHcmEffectiveStartDate"></label></td>
										<td><label id="relatedHcmPositionName"></label></td>
										<td><label id="relatedHcmFTE"></label></td>
										<td><label id="relatedHcmHeadCount"></label></td>
										<td><label id="relatedHcmHiringStatus"></label></td>
										<td><label id="relatedHcmType"></label></td>
									</tr>
								</tbody>
							</table>
							</div>
							<div class="clearfix"></div>
							<div class="row">
								<div class="col-sm-2">
									<label for="txt_unit" class="control-label">Unit</label>
								</div>
								<div class="col-sm-4">
									<form:input path="formBean.unit" type="text"
										class="form-control " id="txt_unit" name="txt_unit"
										style="width:300px;"></form:input>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-2">
									<label for="ddl_post_title" class="control-label">Post Title<font class="star">*</font></label>
								</div>
								<div class="col-sm-4">
									<form:input path="formBean.postTitle" class="form-control" id="ddl_post_title" style="width:100%;" />
								</div>
								<div class="col-sm-2">
									<label id="lblSubSpecialty" for="subSpecialty" class="control-label">Sub-specialty</label>
								</div>
								<div class="col-sm-4">
									<form:hidden path="formBean.rankCode" id="ddl_rank"/>
									<form:select path="formBean.subSpecialty" class="form-control" style="width:100%;">
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
										<form:select path="formBean.postDuration"  required="required"
											id="ddl_post_duration" name="ddl_post_duration"
											class="form-control" style="width:100%;" onchange="postDurationChange()">
											<form:option value="" label="- Select -" />
											<form:options items="${PostDurationList}" />
										</form:select>
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-2 nopadding"><label class="control-label">Start Date<font class="star">*</font></label></div>
									<div class="col-sm-2">
										<div class="input-group date">
											<form:input path="formBean.postStartDate" id="txt_postStartDate"
												class="form-control" required="required"></form:input>
											<span class="input-group-addon"> <span
												class="glyphicon glyphicon-calendar"></span>
											</span>
										</div>
									</div>
								</div>
								<div class="col-sm-2" id="lblDurationType"><label class="control-label">Duration</label></div>
								<div class="col-sm-2 nopadding">
									<div class="form-inline form-group">
										<form:radiobutton path="formBean.limitDurationType"
											id="rd_Duartion" name="rd_Duartion" value="DURATION_PERIOD"></form:radiobutton>
										<span class="form-space"></span>
										<form:input path="formBean.limitDurationNo" type="text"
											class="form-control" id="txt_limit_duration_no"
											name="txt_limit_duration_no" style="width:40px;"></form:input>
										<span class="form-space"></span>
										<form:select path="formBean.limitDurationUnit"
											id="ddl_limit_duration_unit" name="ddl_limit_duration_unit"
											class="form-control" style="width:70px;">
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
									<form:input path="formBean.postActualStartDate" type="text"
										class="form-control " id="txt_post_actual_start_date"
										name="txt_post_actual_start_date" readonly="readonly"></form:input>
								</div>
								<div class="col-sm-2" id="lblDurationType2"><label class="control-label">Fixed End Date</label></div>
								<div class="col-sm-2 nopadding">
									<div class="form-inline">
										<form:radiobutton path="formBean.limitDurationType"
											id="rd_Duartion2" name="rd_Duartion2" value="FIXED_END_DATE"></form:radiobutton>
										<span class="form-space"></span>
										<div class="form-group">
											<div class="input-group date" id="grpLimitDurationEndDate">
												<form:input path="formBean.limitDurationEndDate" type="text"
													class="form-control" id="txt_post_actual_end_date"
													name="txt_post_actual_end_date" style="width:90px;"></form:input>
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
									<label for="ddl_post_remark" class="control-label">Remarks</label>
								</div>
								<div class="col-sm-6">
									<form:input path="formBean.postRemark" type="text"
										class="form-control " id="ddl_post_remark"
										name="ddl_post_remark" style="width:80%"></form:input>
								</div>
							</div>
							<div class="row">
								<div class="form-inline form-group">
									<div class="col-sm-2">
										<label for="ddl_postFTE" class="control-label">FTE<font
											class="star">*</font></label>
									</div>
									<div class="col-sm-6">
										<form:select path="formBean.postFTE" class="form-control" id="ddl_postFTE"
											onchange="changeFTE()"
											required="required">
											<form:option value="" label="- Select -" />
											<form:option value="FULL_TIME" label="Full Time" />
											<form:option value="PART_TIME" label="Part Time" />
										</form:select>
										<span class="form-space"></span>
										<form:input path="formBean.postFTEValue" type="text" id="txt_post_FTE_value"
											required="required" class="form-control" style="width:50px;"></form:input>
										<span class="form-space"></span>
										<label for="txt_post_FTE_value">(no of net working hours per weeks / 39)</label>
									</div>
									
									<div class="col-sm-2">
									</div>
										
									<!--  HO Buy Service -->
									<div class="col-sm-2">
										<form:checkbox path="formBean.hoBuyServiceInd" value="Y"/> <label>HO Buy Service</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-2">
									<label for="ddl_position_status" class="control-label">Post Status</label>
								</div>
								<div class="col-sm-2">
									<form:select path="formBean.positionStatus" id="ddl_position_status" name="ddl_position_status"
 											class="form-control" style="width:100%;">
										<form:options items="${postStatusList}" />
									</form:select>
								</div>
								<div style="display: none">
									<div class="col-sm-2">Start Date</div>
									<div class="col-sm-2">
										<div class="input-group date" id='position_start_date'>
											<form:input path="formBean.positionStartDate" type="text"
												class="form-control" id="dp_position_start_date"
												name="dp_position_start_date"></form:input>
											<span class="input-group-addon"> <span
												class="glyphicon glyphicon-calendar"></span>
											</span>
										</div>
									</div>
									<div class="col-sm-2">End Date</div>
									<div class="col-sm-2">
										<div class='input-group date' id='position_end_date'>
											<form:input path="formBean.positionEndDate" type="text"
												class="form-control" id="dp_position_end_date"
												name="dp_position_end_date"></form:input>
											<span class="input-group-addon"> <span
												class="glyphicon glyphicon-calendar"></span>
											</span>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-3">
									<label for="txt_cluster_ref_no" class="control-label_opt"><b>Cluster Reference No.</b></label>
								</div>
								<div class="col-sm-4">
									<form:input path="formBean.clusterRefNo" type="text"
										class="form-control" id="txt_cluster_ref_no" style="width:100%"></form:input>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-3">
									<label for="txt_clusterRemark" class="control-label_opt">Additional Remarks from Cluster </label>
								</div>
								<div class="col-sm-9">
									<div id="txt_clusterRemark"
   						                 name="txt_clusterRemark" 
										 style="width:100%" ></div>	           
								</div>
							</div>

						</div>
						<div class="tab-pane" id="tab2_fund">
							<!-- To be updated -->						
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
														type="text" class="form-control" style="width:100%"></form:input>
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
															type="text" class="form-control" style="width:100%"
															data-bv-excluded="true"></form:textarea>
											</div>
											<!--  Remark - End -->
										</div>
									</td>
								</tr>
							</table>
						
						
						
						
							
						</div>
						<!--tab2 end-->
						<div class="tab-pane" id="tab3_resources">
							<div class="row">
								<div class="col-sm-3">
									<label for="ddl_res_sup_fr_ext" class="field_request_label">Resources support from external</label>
								</div>
								<div class="col-sm-6">
									<form:select path="formBean.res_sup_fr_ext"
										id="ddl_res_sup_fr_ext" name="ddl_res_sup_fr_ext"
										class="form-control" style="width:200px;">
										<form:option value="" label="- Select -" />
										<form:options items="${ExternalSupportList}" />
									</form:select>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-3">
									<label for="txt_res_sup_remark" class="field_request_label">Remark</label>
								</div>
								<div class="col-sm-6">
									<form:input path="formBean.res_sup_remark" type="text"
										class="form-control " id="txt_res_sup_remark"
										name="txt_res_sup_remark" style="width:100%"></form:input>
								</div>
							</div>
						</div>
						<!-- Change History tab -->
						<div class="tab-pane" id="tab5">
							<!-- 01 Upgrade History -->
							<div id="secUpgradeHist">
								<H4>Upgrade</H4>
								<table id="tblUpgradeHist" class="display cell-border mprs_table">
									<thead>
										<tr>
											<th>Request ID</th>
											<th>Effective Date</th>
											<th>From Post ID</th>
											<th>Reason</th>
											<th>Action</th>
										</tr>
									</thead>
								</table>
							</div>
							<div id="secExtensionHist">
							<!-- Extension History -->
								<H4>Extension</H4>
								<table id="tblExtensionHist"
									class="display cell-border mprs_table">
									<thead>
										<tr>
											<th>Request ID</th>
											<th>Transaction Date</th>
											<th>Original Start Date</th>
											<th>Revised Start Date</th>
											<th>Original End Date</th>
											<th>Revised End Date</th>
											<th>Action</th>
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
											<th>Reason</th>
											<th>Action</th>
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
											<th>Reason</th>
											<th>Action</th>
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
											<th>Action</th>
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
											<th>Reason</th>
											<th>Action</th>
										</tr>
									</thead>
								</table>
							</div>
							<div id="secChangeStaffMixHist">
							<!-- Change of staff mix History -->
								<H4>Change of Staff Mix</H4>
								<table id="tblChangeStaffMixHist" class="display cell-border mprs_table">
									<thead>
										<tr>
											<th>Request ID</th>
											<th>Effective Date</th>
											<th>From Post ID</th>
											<th>Reason</th>
											<th>Action</th>
										</tr>
									</thead>
								</table>
							</div>
							
							<div id="secFTEAdjustmentHist">
							<!-- Change FTE History -->
								<H4>FTE Adjustment</H4>
								<table id="tblFTEAdjustmentHist"
									class="display cell-border mprs_table">
									<thead>
										<tr>
											<th>Request ID</th>
											<th>Effective Date</th>
											<th>Reason</th>
											<th>Action</th>
										</tr>
									</thead>
								</table>
							</div>
							<div id="secNoChangeHist" style="text-align: center;">No transaction found.</div>
						</div>
						<div class="tab-pane" id="tab6">
							<!-- Strength/Assignment History -->
							<H5>Assignment History</H5>
							<table id="tblAssignmentHist" class="display cell-border mprs_table">
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
										<th>Action</th>
									</tr>
								</thead>
							</table>
						</div>
						<div class="tab-pane" id="tabAttachment">
							<H5>Attachment</H5>
							<table id="tblAttachment" class="display cell-border mprs_table">
								<thead>
									<tr>
										<th style="width:400px">File Name</th>
										<th style="width:200px">Action</th>
									</tr>
								</thead>
								<tbody>
								
								</tbody>
							</table>
							
							<fieldset class="scheduler-border">
							<div style="width:100%">
								<h5><b>Upload File</b></h5>
								<div class="col-sm-2">
			      					File
			      				</div>
			      				<div class="col-sm-6">
			      					<input type="file" id="approvalFile" name="approvalFile"/>
			      				</div>
			      				<div class="col-sm-2">
			      					<button type="button" name="btnUploadFile"
										 onclick="processFileUpload($(this).parent().parent().parent())"
										 class="btn btn-default" style="width:130px"><i class="fa fa-upload"></i> Upload</button>
			      				</div>
							</div>
							</fieldset>
						</div>
					</div>
				</div>
				<!-- Tab end-->


			</div>
			<div class="modal-footer">
				<table style="width:100%">
					<tr>
						<td style="text-align:left">
							<button type="button" class="btn btn-default" onclick='showApprovalInformation()'>Approval Information</button>
							<c:choose>
								<c:when test="${ formBean.isHR == 'Y'}">
									<button type="button" class="btn btn-default" onclick='editClusterRemark()'>Edit Cluster's remarks</button>
								</c:when>
							</c:choose>
						</td>
						<td style="text-align:right">
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<!--  Post Detail Model - End -->

<div id="strengthDetailModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:980px">
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Strength Detail</b>
			    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
			    </h4>
			</div>
      		<div class="modal-body">
      			<div class="row">
      				<div class="col-sm-4"><b>Employee Name</b></div>
      				<div class="col-sm-8" id="strengthEmployeeName"></div>
      			</div>
      		
      			<div class="row">
      				<div class="col-sm-4"><b>Emp Type</b></div>
      				<div class="col-sm-8" id="strengthEmpType"></div>
      			</div>
      			
      			<div class="row">
      				<div class="col-sm-4"><b>Ranks</b></div>
      				<div class="col-sm-8" id="strengthRank"></div>
      			</div>
      			
      			<div class="row">
      				<div class="col-sm-4"><b>Position Title</b></div>
      				<div class="col-sm-8" id="strengthPositionTitle"></div>
      			</div>
      			
      			<div class="row">
      				<div class="col-sm-4"><b>FTE</b></div>
      				<div class="col-sm-8" id="strengthFTE"></div>
      			</div>
      			
      			<div class="row">
      				<div class="col-sm-4"><b>Head Count</b></div>
      				<div class="col-sm-8" id="strengthHeadCount"></div>
      			</div>
      			
      			<div class="row">
      				<div class="col-sm-4"><b>Start Date</b></div>
      				<div class="col-sm-8" id="strengthStartDate"></div>
      			</div>
      			
      			<div class="row">
      				<div class="col-sm-4"><b>End Date</b></div>
      				<div class="col-sm-8" id="strengthEndDate"></div>
      			</div>
      			
      			<div class="row">
      				<div class="col-sm-4"><b>Reason of Leaving</b></div>
      				<div class="col-sm-8" id="strengthLeaveReason"></div>
      			</div>
      			
      			<div class="row">
      				<div class="col-sm-4"><b>Actual Termination Date</b></div>
      				<div class="col-sm-8" id="strengthActualTermDate"></div>
      			</div>
      			
      			<div class="row">
      				<div class="col-sm-4"><b>Position Name</b></div>
      				<div class="col-sm-8" id="strengthPositionName"></div>
      			</div>
      			
      			<div class="row">
      				<div class="col-sm-4"><b>Change Reason</b></div>
      				<div class="col-sm-8" id="strengthChangeReason"></div>
      			</div>
      			
      			<div class="row">
      				<div class="col-sm-4"><b>Primary</b></div>
      				<div class="col-sm-8" id="strengthPrimaryFlag"></div>
      			</div>
      			
      			<div class="row">
      				<div class="col-sm-4"><b>Employee Category</b></div>
      				<div class="col-sm-8" id="strengthEmployeeCategory"></div>
      			</div>
      			
      			<div class="row">
      				<div class="col-sm-4"><b>Payzone</b></div>
      				<div class="col-sm-8" id="payzone"></div>
      			</div>
	      	</div>
      		<div class="modal-footer">
        		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
     		</div>
    	</div>
  	</div>
</div>

<!-- Model to confirm cancel send email - Start -->
<div id="model_confirmRemove" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom modal-dialog-postId" style="width:500px">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-body">
				<div class="row" style="padding: 20px;">
					<div class="col-sm-12">
						<label for="" class="field_request_label">Are you sure to delete the attachment?</label>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal" style="width:130px;"
					onclick="confirmRemoveAttachment();">Yes</button>
				<button type="button" class="btn btn-default" style="width:130px;"
					data-dismiss="modal"><i class="fa fa-chevron-left"></i> Cancel
				</button>
			</div>
		</div>
	</div>
</div>
<!-- Model to confirm cancel send email - End -->

<!-- Model for Approval Information -->
<div id="approvalInfoModel" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:800px">
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Approval Information</b></h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="form-group">
						<div class="col-sm-2">
							<label for="txt_reason" class="control-label"><strong>Approval Date</strong></label>
						</div>
	
						<div class="col-sm-2">
							<div class="input-group date" id="approvalDateGroup">
								<form:input path="formBean.requestApprovalDate" class="form-control" readonly="true"/>
								
							</div>
						</div>
					</div>
					<div class="col-sm-2">
						<label for="txt_reason" class="control-label"><strong>Reference
						</strong></label>
					</div>
					<div class="col-sm-6">
						<form:input path="formBean.requestApprovalReference" class="form-control" readonly="true"/>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<label for="txt_reason" class="field_request_label"><strong>Remarks</strong></label>
					</div>
					<div class="col-sm-10">
						<form:input path="formBean.requestApprovalRemark" class="form-control"
							        style="width:100%" readonly="true"/>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>	
					
<!-- Model for Edit Cluster's remark -->
<div id="editClusterModel" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:800px">
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Edit Cluster's Remark</b></h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-sm-4">
						Cluster Reference No.
					</div>
					<div class="col-sm-8">
						<form:input path="formBean.clusterReferenceNo" type="text"
										class="form-control " 
										style="width:100%" maxlength="100"></form:input>
					</div>
				</div>
				<div class="row">
				    <div class="col-sm-4">
						Addition Remarks from Cluster
					</div>
					<div class="col-sm-8">
						<form:textarea path="formBean.additionRemark" type="text" 
						               class="form-control"
									   style="width:100%;height:60px" maxlength="2000"></form:textarea>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" name="btnSaveClusterRemark" style="width:110px" onclick="updateClusterRemark()"><i class="fa fa-floppy-o"></i> Save</button>
				<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal"><i class="fa fa-times"></i> Cancel</button>
			</div>
		</div>
	</div>
</div>					

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>