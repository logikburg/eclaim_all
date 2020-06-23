<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	var requestType = 'UPGRADE';
	
	var postSeq = 1;
	var tblHCMResult;
	var endDateMandatory = false;
	
	var haveDefaultDepartment = "";
	var haveDefaultStaffGroup = "";
	var haveDefaultRank = "";
	
	var fromPost = "";
	var fromRankName = "";
	
	function payrollIsRunning() {
		if ($("button[name='btnConfirm']").length > 0) {
			$("#warningTitle").html("Warning");
	        $("#warningContent").html("HCM Payroll is running, please wait a moment and re-try.");
	        $("#warningModal").modal("show");
	        
	        $("button[name='btnConfirm']").attr("disabled", true);
        }
	}
	
	function displayError(errMsg) {
		if (errMsg == "") {
			return true;
		}
		else {
			$("#divErrorMsg").text(errMsg);
			$($("div[class='alert-box-icon-danger']")[0]).css("height", $("#divError").height())
			$("#divError").show();
			
			$('html,body').animate({ scrollTop: 0 }, 'slow');
			
			return false;
		}
	}
	
	function selectDuration() {
		$("#editPostModel #rd_Duartion").prop('checked', 'checked');
		clickDuration2();
	}
									
	function selectFixEndDate() {
		$("#editPostModel #rd_Duartion2").prop('checked', 'checked');
		clickDuration2();
	}
	
	$(document).ready(function(){
		$("#tblHCMSearchResult").DataTable();
	});
	
	function startSearching() {
		fromPost = "Y";
	
		$("#divSuccess").hide();
		$("#divError").hide();
	
		if ($("#effectiveDate").val() == "") {
			displayError("Please select effective date.");
			return;
		}
	
		// Hide the result section
		$("#tblSearchResult").hide();
		var oTable = $('#tblSearchResult').dataTable();
		oTable.fnDestroy();
		
		// Refresh the searching list
		$("#searchStaffGroupId").empty();
		
		var option = "<option value=''> - Select - </option>";
	    $("#searchStaffGroupId").append(option);
		for (var k=1; k<$("#tmpStaffGroup option").length; k++) {
			option = "<option value='" + $($("#tmpStaffGroup option")[k]).val() + "'>" + $($("#tmpStaffGroup option")[k]).text() + "</option>";
	    	$("#searchStaffGroupId").append(option);
		}
	
		$("#searchResultModel").modal("show");
	}
	
	function validateForm() {
		var errMsg = "";
	
		if ($("#tblPositionFrom tbody tr td").html() == "No record found." || $("#tblPositionFrom tbody tr").size() == 0) {
			errMsg += "Please select at least one from post.<br/>";
		}
		
		if ($("#tblPositionTo tbody tr td").html() == "No record found." || $("#tblPositionTo tbody tr").size() == 0) {
			errMsg += "Please select at least one to post.<br/>";
		}
		
		if ($("#canEditDetailInfo").val() == "Y") {
			for (var i=0; i< $("#tblPositionTo tbody tr").size(); i++) {
				if ($($("input[name='RequestPostDuration']")[i]).val() == "") {
					errMsg += "Please input the post detail information.<br/>";	
				}
			}
		}
		
		if ($("#canEditFinancialInfo").val()== "Y") {
			for (var i=0; i< $("#tblPositionTo tbody tr").size(); i++) {
				// ST08733: Add the checking if not existing record 
				var isExistingRec = $($("input[name='requestPostExist']")[i]).val();
			
				if (isExistingRec != "Y") {
					// Check the first record is empty or not
					var fundSrcId = "";
					var annualPlanInd = "";
				
					fundSrcId = $($($("div[name='hiddenTable']")[0]).find("input[name$='fundSrcId']")[0]).val();
					annualPlanInd = $($($("div[name='hiddenTable']")[0]).find("input[name$='annualPlanInd']")[0]).val();
				
					if (fundSrcId == "" ||
						annualPlanInd == "") {
						errMsg += "Please input the detail funding information.<br/>";	
					}
				}
			}
		}
		
		if (errMsg == "") {
			return true;
		}
		else {
			$("#divErrorMsg").html(errMsg);
			$($("div[class='alert-box-icon-danger']")[0]).css("height", $("#divError").height())
			$("#divError").show();
			
			$('html,body').animate({ scrollTop: 0 }, 'slow');
			
			return false;
		}
	}
	
	function performDialogChecking() {
		var errMsg = "";
	
		var numberOfFundingSource = $("#tblFunding tr").length;

		if (numberOfFundingSource == 0) {
			return true;
		}
		else {
			if ($($("#tblFunding select[name$='annualPlanInd']")[0]).prop("disabled")) {
				return true;
			}		
		}
		var sum = 0;
		for (var i=0; i< numberOfFundingSource; i++) {
			var annualPlan = $($("#tblFunding select[name$='annualPlanInd']")[i]).val();
			var programCode = $($("#tblFunding input[name$='programCode']")[i]).val();
			var programName = $($("#tblFunding input[name$='programName']")[i]).val();
			var programType = $($("#tblFunding select[name$='programTypeCode']")[i]).val();
			var fundSrcStartDateStr = $($("#tblFunding input[name$='fundSrcStartDate']")[i]).val();
			var fundSrcEndDateStr = $($("#tblFunding input[name$='fundSrcEndDate']")[i]).val();
			var fundSrcFte = $($("#tblFunding input[name$='fundSrcFte']")[i]).val();
			var year = $($("#tblFunding select[name$='programYear']")[i]).val();
				
			if (annualPlan == "") {
				errMsg += "Funding Source " + (i+1) + ": Annual Plan cannot be empty.<br/>";
			}
			
			if (programType == "") {
				errMsg += "Funding Source " + (i+1) + ": Program Type cannot be empty.<br/>";
			}
	
			if (fundSrcStartDateStr != "" && fundSrcEndDateStr != "") {
				var diff = getDaysBetween(fundSrcStartDateStr, fundSrcEndDateStr);
								
				if (diff > 0) {
					errMsg += "Funding Source " + (i+1) + ": End date should be on or after Start Date.<br/>";
				}
			}	
			
			// Check FTE
			if (fundSrcFte == "") {
				errMsg += "Funding Source " + (i+1) + ": FTE cannot be empty.<br/>";
			}
			else {
				sum = parseFloat(fundSrcFte) + sum;
			}
			
			if (parseFloat(fundSrcFte) < 0.01 || parseFloat(fundSrcFte) > 1) {
				errMsg += "Funding Source " + (i+1) + ": Value should between 0.01 and 1.<br/>";
			}
			
			// Check Program Name and Program Code
			if (annualPlan == "Y") {
				if (programCode == "") {
					errMsg += "Funding Source " + (i+1) + ": Program Code / Ref No. cannot be empty.<br/>";
				}
				
				if (programName == "") {
					errMsg += "Funding Source " + (i+1) + ": Program Name cannot be empty.<br/>";
				}
				
				if (year == "") {
					errMsg += "Funding Source " + (i+1) + ": Year cannot be empty.<br/>";
				}
			}
			
			
			// UT29797: Validation 1: Cross check again year and start date
			// Get the program year
			if (fundSrcStartDateStr != "") {
				var startDate = $($("#tblFunding input[name$='fundSrcStartDate']")[i]).val();
					
				var fromYear = year.split("/")[0];
				var toYear = "20" + year.split("/")[1];
				var startDateSplit = startDate.split("/");
				
				if (fromYear != "") {
					if (!(new Date(fromYear, 3, 1).getTime() <= new Date(startDateSplit[2], Number(startDateSplit[1])-1, Number(startDateSplit[0])).getTime() 
						&& new Date(toYear, 2, 31).getTime() >= new Date(startDateSplit[2], Number(startDateSplit[1])-1, Number(startDateSplit[0])).getTime())) {
						errMsg += "Funding Source " + (i+1) + ": Start Date should fall within the selected Program Year.<br/>";
					}
				}
			}
			else {
				errMsg += "Funding Source " + (i+1) + ": Start Date cannot be empty.<br/>";
			}
						
			if ($('#editPostModel #ddl_post_duration').val() != "R") {
				if (fundSrcEndDateStr == "") {
					errMsg += "Funding Source " + (i+1) + ": End date cannot be empty.<br/>";
				}
			}
		}		
		
		// Check sum of FTE should >= total FTE
		sum = sum.toFixed(4);
		
		// Get the FTE for current row 
		var totalFTE = $($($("#tblPositionTo tbody")[currentIdx]).find("td")[3]).text();
		
		if (sum < parseFloat(totalFTE).toFixed(4)) {
			errMsg += "Total FTE should be equal or greater than Post FTE.<br/>";
		}
		
		if (errMsg != "") {
			$("#warningTitle").html("Error");
            $("#warningContent").html(errMsg);
           	$("#warningModal").modal("show");
           		
           	return false;
		}
		
		return true;	
	}
	
	function resetValidation() {
		/*$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_1st_start_date', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_1st_end_date', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_2nd_start_date', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_2nd_end_date', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_3rd_start_date', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_3rd_end_date', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_1st', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'annual_plan_ind', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_1st_fte', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_2nd_fte', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_3rd_fte', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'programType', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'program_year', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'program_code', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'program_name', 'NOT_VALIDATED');*/
	}
	
	var currentIdx = 0;
	function editNewPosition(postID, obj, idx){
		currentIdx = idx;
	
		resetValidation();
	
		initNewPostModal();
		var tmpRank = "";
		
		if ($("#tblPositionFrom tbody tr td").length > 1) {
			tmpRank = $("#tblPositionFrom tbody tr:eq(0) td:eq(1)").html().trim();
		}
		
		// Default the staff group default
		var staffGroupCode =  $("#tmpStaffGroup").val();
		var staffGroupDesc =  $("#tmpStaffGroup option:selected").text();
		
		//retrieve row details
		// var cluster = $("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestCluster']").val();
		$("#editPostModel #ddl_cluster").val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestCluster']").val());
		$('#editPostModel #ddl_institution').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestInst']").val());
		$('#editPostModel #ddl_department').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestDept']").val());
		$('#editPostModel #ddl_staff_group').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestStaffGroup']").val());
		$('#editPostModel #ddl_rank').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestRank']").val());
		$('#editPostModel #txt_unit').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestUnit']").val());
		$('#editPostModel #postTitle').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostTitle']").val());
		$('#editPostModel #ddl_post_duration').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostDuration']").val());
		$('#editPostModel #txt_postStartDate').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostStartDate']").val());
		
		// Refresh staff group drop down
		$("#editPostModel #ddl_staff_group").empty();
		var option = "<option value='" + staffGroupCode + "'>" + staffGroupDesc + "</option>";
	    $("#editPostModel #ddl_staff_group").append(option);
		$("#editPostModel #ddl_staff_group").val(staffGroupCode);
		
		// Refresh rank / department
		changeStaffGroup();
		
		if ($('#editPostModel #ddl_post_duration').val() != 'R') {		
			if ("FIXED_END_DATE" == $("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestRdDuration']").val()) {
				$('#editPostModel #rd_Duartion2').prop('checked', 'checked');
				$('#editPostModel #txt_post_actual_end_date').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostActualEndDate']").val());
			}
			else {
				$('#editPostModel #rd_Duartion').prop('checked', 'checked');	
			}
			
			$("font[name='lblEndDateStar']").show(); 
		}
		else {
			$('#editPostModel #rd_Duartion').prop('checked', false);
			$('#editPostModel #rd_Duartion2').prop('checked', false);
			$("font[name='lblEndDateStar']").hide();
		}
		
		$('#editPostModel #txt_limit_duration_no').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestDurationValue']").val());
		$('#editPostModel #ddl_limit_duration_unit').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestDurationUnit']").val());
		$('#editPostModel #txt_post_actual_start_date').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostActualStartDate']").val());
		$('#editPostModel #rd_Duartion2').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestRdDuration2']").val());
		// $('#editPostModel #txt_post_actual_end_date').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostActualEndDate']").val());
		$('#editPostModel #ddl_post_remark').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostRemark']").val());
		$('#editPostModel #postFTE').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostFTE']").val());
		$('#editPostModel #postFTEValue').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostFTEValue']").val());
		$('#editPostModel #ddl_position_status').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPositionStatus']").val());
		$('#editPostModel #dp_position_start_date').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPositionStartDate']").val());
		$('#editPostModel #dp_position_end_date').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPositionEndDate']").val());
		$('#editPostModel #txt_cluster_ref_no').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestClusterRefNo']").val());
		$('#editPostModel #txt_clusterRemark').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestClusterRemark']").val());
		
		$('#editPostModel #ddl_res_sup_fr_ext').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestResSupFrExt']").val());
		$('#editPostModel #txt_res_sup_remark').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestResSupRemark']").val());
		
		$('#editPostModel #txt_proposed_post_id').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestProposedPostId']").val());
		$('#editPostModel #txt_post_id_just').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostIdJust']").val());
		
		if ($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestHoBuyServiceInd']").val() == "Y") {
			$('#editPostModel #cbHoBuyServiceInd').attr("checked", true);
		}
		else {
			$('#editPostModel #cbHoBuyServiceInd').attr("checked", false);
		}
		
		// Remove all row(s)
		var tableLength = $("#tblFunding tr").length;
		for (var i=1; i<tableLength; i++) {
			$($("#tblFunding tr")[0]).remove();
		}
		
		// Get Number of Funding Source
		var hiddenTable = $(obj).parent();
		// alert(hiddenTable.find("input[name$='annualPlanInd']").length);
		var numberOfFundingSource = hiddenTable.find("input[name$='annualPlanInd']").length;
		
		// Add row
		for (var i=1; i<numberOfFundingSource; i++) {
			addNewFunding();
		}
		
		// Update content of each row
		for (var i=0; i<numberOfFundingSource; i++) {
			$($("#tblFunding select[name$='annualPlanInd']")[i]).val($(hiddenTable.find("input[name$='annualPlanInd']")[i]).val());
			$($("#tblFunding select[name$='programYear']")[i]).val($(hiddenTable.find("input[name$='programYear']")[i]).val());
			$($("#tblFunding input[name$='programCode']")[i]).val($(hiddenTable.find("input[name$='programCode']")[i]).val());
			$($("#tblFunding input[name$='programName']")[i]).val($(hiddenTable.find("input[name$='programName']")[i]).val());
			$($("#tblFunding select[name$='programTypeCode']")[i]).val($(hiddenTable.find("input[name$='programTypeCode']")[i]).val());
			$($("#tblFunding select[name$='fundSrcId']")[i]).val($(hiddenTable.find("input[name$='fundSrcId']")[i]).val());
			$($("#tblFunding select[name$='fundSrcSubCatId']")[i]).val($(hiddenTable.find("input[name$='fundSrcSubCatId']")[i]).val());
			$($("#tblFunding input[name$='fundSrcStartDate']")[i]).val($(hiddenTable.find("input[name$='startDateStr']")[i]).val());
			$($("#tblFunding input[name$='fundSrcEndDate']")[i]).val($(hiddenTable.find("input[name$='endDateStr']")[i]).val());
			$($("#tblFunding input[name$='fundSrcFte']")[i]).val($(hiddenTable.find("input[name$='fundSrcFte']")[i]).val());
			$($("#tblFunding textarea[name$='fundSrcRemark']")[i]).val($(hiddenTable.find("input[name$='fundSrcRemark']")[i]).val());
			$($("#tblFunding input[name$='inst']")[i]).val($(hiddenTable.find("input[name$='inst']")[i]).val());
			$($("#tblFunding input[name$='section']")[i]).val($(hiddenTable.find("input[name$='section']")[i]).val());
			$($("#tblFunding input[name$='analytical']")[i]).val($(hiddenTable.find("input[name$='analytical']")[i]).val());
			$($("#tblFunding input[name='currentPostDuration']")[i]).val($(hiddenTable.find("input[name$='hidPostDuration']")[i]).val());
			$($("#tblFunding input[name='currentPostFTE']")[i]).val($(hiddenTable.find("input[name$='hidPostFTE']")[i]).val());
			
			changeAnnualPlanByRow($($("#tblFunding select[name$='annualPlanInd']")[i]));
		}
		
		// Refresh the label for funding source
		refreshFundingSourceLabel();
		
		$('#editPostModel #tmpHCMPositionId').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='hcmPositionId']").val());
		
		$('#editPostModel #programType').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='requestProgramType']").val());
		$('#editPostModel #subSpecialty').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='requestSubSpecialty']").val());
		
		postDurationChange2();
		// changeAnnualPlan2($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='requestProgramType']").val());
		clickDuration2();	
		changeRankDropdown2();
		
		if ($("#editPostModel #postFTE").val() == "FULL_TIME") {
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('postFTEValue', false);
			$("#editPostModel #postFTEValue").attr("disabled", true);
			
		}
		else {
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('postFTEValue', true);
			$("#editPostModel #postFTEValue").attr("disabled", false);
		}
		
		if ($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='hcmPositionId']").val() != "") {
			$.ajax({
				url: "<%= request.getContextPath() %>/request/getRelatedHCMInfo",
				type: "POST",
				data: {
					hcmPositionId: $("#tblPositionTo tbody tr[id='"+postID+"'] input[name='hcmPositionId']").val() 
				},
				success: function(data) {
					$("#editPostModel #relatedHcmEffectiveStartDate").text(data.relatedHcmEffectiveStartDate);
					$("#editPostModel #relatedHcmFTE").text(data.relatedHcmFTE);
					$("#editPostModel #relatedHcmHeadCount").text(data.relatedHcmHeadCount);
					$("#editPostModel #relatedHcmPositionName").text(data.relatedHcmPositionName);
					$("#editPostModel #relatedHcmHiringStatus").text(data.relatedHcmHiringStatus);
					$("#editPostModel #relatedHcmType").text(data.relatedHcmType);
					
					if (data.deptCode != "" && data.deptCode != null) {
						haveDefaultDepartment = data.deptCode;
					}
					else {
						haveDefaultDepartment = "";
					}
					
					if (data.staffGroupCode != "" && data.staffGroupCode != null) {
						haveDefaultStaffGroup = data.staffGroupCode;
					}
					else {
						haveDefaultStaffGroup = "";
					}
					
					if (data.rankCode != "" && data.rankCode != null) {
						haveDefaultRank = data.rankCode;
					}
					else {
						haveDefaultRank = "";
					}
					
					$("#searchPanel").hide();
					checkEnableField($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='requestPostExist']").val());
					$("#btnSaveNewPosition").attr("onclick","performUpdate(\""+ postID +"\", this)"); 					
					$("#editPostModel").modal("show");
				}
			});
		}
		else {
			checkEnableField();
			$("#btnSaveNewPosition").attr("onclick","performUpdate(\""+ postID +"\", this)"); 					
			$("#editPostModel").modal("show");
		}
	}
	
	function checkEnableField(isExisting) {
		var disableField = false;
		if (typeof isExisting != "undefined") {
			if (isExisting == "Y") {
				disableField = true;
			}
		}
		
		if (disableField) {
			$("#tab0_basic_1 input").attr("disabled", true);
			$("#tab0_basic_1 select").attr("disabled", true);
			$("#tab0_basic_1 input").attr("disabled", true);
			$("#tab0_basic_1 button").attr("disabled", true);
				
			$("#tab1_position_1 input").attr("disabled", true);
			$("#tab1_position_1 select").attr("disabled", true);
			$("#tab1_position_1 textarea").attr("disabled", true);
			
			$("#tab2_fund_1 input").attr("disabled", true);
			$("#tab2_fund_1 select").attr("disabled", true);
			$("#tab2_fund_1 textarea").attr("disabled", true);
			$("#tab2_fund_1 button[name='btnRemoveFunding']").attr("disabled", true);
			$("#tab2_fund_1 button[name='btnAddFunding']").attr("disabled", true);
					
			$("#tab3_resources_1 input").attr("disabled", true);
			$("#tab3_resources_1 select").attr("disabled", true);
		}
		else {
			if ("Y" != $("#canEditDetailInfo").val()) {
				$("#tab0_basic_1 input").attr("disabled", true);
				$("#tab0_basic_1 select").attr("disabled", true);
				$("#tab0_basic_1 input").attr("disabled", true);
				$("#tab0_basic_1 button").attr("disabled", true);
				
				$("#tab1_position_1 input").attr("disabled", true);
				$("#tab1_position_1 select").attr("disabled", true);
				$("#tab1_position_1 textarea").attr("disabled", true);
					
				$("#tab3_resources_1 input").attr("disabled", true);
				$("#tab3_resources_1 select").attr("disabled", true);
			}
			else {
				$("#tab0_basic_1 input").attr("disabled", false);
				$("#tab0_basic_1 select").attr("disabled", false);
				$("#tab0_basic_1 input").attr("disabled", false);
				$("#tab0_basic_1 button").attr("disabled", false);
				
				$("#tab1_position_1 input").attr("disabled", false);
				$("#tab1_position_1 select").attr("disabled", false);
				$("#tab1_position_1 textarea").attr("disabled", false);
					
				$("#tab3_resources_1 input").attr("disabled", false);
				$("#tab3_resources_1 select").attr("disabled", false);
			}
		
			if ("Y" != $("#canEditFinancialInfo").val()) {
				$("#tab2_fund_1 input").attr("disabled", true);
				$("#tab2_fund_1 select").attr("disabled", true);
				$("#tab2_fund_1 textarea").attr("disabled", true);
				$("#tab2_fund_1 button[name='btnRemoveFunding']").attr("disabled", true);
				$("#tab2_fund_1 button[name='btnAddFunding']").attr("disabled", true);
			}
			else {
				$("#tab2_fund_1 input").attr("disabled", false);
				$("#tab2_fund_1 select").attr("disabled", false);
				$("#tab2_fund_1 textarea").attr("disabled", false);
				$("#tab2_fund_1 button[name='btnRemoveFunding']").attr("disabled", false);
				$("#tab2_fund_1 button[name='btnAddFunding']").attr("disabled", false);
			}
		}
	}
	
	function saveNewPosition(){
		if (!performDialogChecking()) {
			return;
		}
	
		// Reset all date field checking (to perform revalidate)
		resetValidation();
	
		if ($("#editPostModel #tmpHCMPositionId").val() == "") {
			$("#warningTitle").html("Error");
            $("#warningContent").html("Please select the HCM Position.");
            $("#warningModal").modal("show");
			return;
		}
		
		if (!$("#editPostModel #txt_postStartDate").prop("disabled") 
		       && !$("#editPostModel #txt_post_actual_end_date").prop("disabled")) {
			var postStartDate = $("#editPostModel #txt_postStartDate").val();
			var postEndDate = $("#editPostModel #txt_post_actual_end_date").val();
			var dayBetween = getDaysBetween(postEndDate, postStartDate);
			if (dayBetween < 0) {
				$("#warningTitle").html("Error");
		        $("#warningContent").html("Fixed End Date should be after Post Start Date.");
		        $("#warningModal").modal("show");
				return;
			} 
		}
		
		var validator = $("#frmDialog").data('bootstrapValidator');
	
		/*validator.validate();
		if (!validator.isValid()) {
			return;
		}*/
		
		$("#frmDetail").bootstrapValidator('revalidateField', 'approvalDate');
	
		var rank_desc ="";
		// Remove the selected row
		if ($("#tblPositionTo tbody tr td").html() == "No record found.") {
        	$("#tblPositionTo tbody").remove();
        }
		
		if ( $("#editPostModel #ddl_rank").val() != "") {
			rank_desc = $("#editPostModel #ddl_rank option:selected").text();
		}
			
		var durationDesc = "";
		durationDesc = $("#editPostModel #ddl_post_duration option:selected").text();
		
		var rowNum = $("#tblPositionTo tbody tr").length-1;
		if (rowNum < 0) {
			rowNum = 0;
		}
		
		var row = "<tbody><tr id='" + postSeq + "'>";
		row += "<td><input type='hidden' name='requestPostNoTo' value=''/></td>";
		row += "<td>" + rank_desc + "</td>";
		row += "<td>" + durationDesc + "</td>";
		row += "<td>" + $("#editPostModel #postFTEValue").val() + "</td>";
		row += "<td style='text-align:center'><button type='button' name='btnEdit' class='btn btn-primary' onclick='editNewPosition(\""
				+ postSeq
				+ "\", this, " + rowNum + ")' style='margin-right:5px;'>Edit Funding</button><button type='button' class='btn btn-primary' onclick='removePost($(this));$(\"#btnAddExistingPost\").prop(\"disabled\", false);$(\"#btnAddToPost\").prop(\"disabled\", false);enableEffectiveDate()'><i class='fa fa-times'></i> Remove</button>";
		//Position Details
		row += "<input type='hidden' name='RequestCluster' value='" + $("#editPostModel #ddl_cluster").val() + "'/>";
		row += "<input type='hidden' name='RequestInst' value='" + $("#editPostModel #ddl_institution").val() + "'/>";
		row += "<input type='hidden' name='RequestDept' value='" + $("#editPostModel #ddl_department").val() + "'/>";
		row += "<input type='hidden' name='RequestStaffGroup' value='" + $("#editPostModel #ddl_staff_group").val() + "'/>";
		row += "<input type='hidden' name='RequestRank' value='" + $("#editPostModel #ddl_rank").val() + "'/>";
		row += "<input type='hidden' name='RequestUnit' value='" + $("#editPostModel #txt_unit").val() + "'/>";
		row += "<input type='hidden' name='RequestPostTitle' value='" + $("#editPostModel #postTitle").val() + "'/>";
		row += "<input type='hidden' name='RequestPostDuration' value='" + $("#editPostModel #ddl_post_duration").val() + "'/>";
		row += "<input type='hidden' name='RequestPostStartDate' value='" + $("#editPostModel #txt_postStartDate").val() + "'/>";
		// row += "<input type='hidden' name='RequestRdDuration' value='" + $("#editPostModel #rd_Duartion").val() + "'/>";
		row += "<input type='hidden' name='RequestDurationValue' value='" + $("#editPostModel #txt_limit_duration_no").val() + "'/>";
		row += "<input type='hidden' name='RequestDurationUnit' value='" + $("#editPostModel #ddl_limit_duration_unit").val() + "'/>";
		row += "<input type='hidden' name='RequestPostActualStartDate' value='" + $("#editPostModel #txt_post_actual_start_date").val() + "'/>";
		// row += "<input type='hidden' name='RequestRdDuration2' value='" + $("#editPostModel #rd_Duartion2").val() + "'/>";
		row += "<input type='hidden' name='RequestPostActualEndDate' value='" + $("#editPostModel #txt_post_actual_end_date").val() + "'/>";
		row += "<input type='hidden' name='RequestPostRemark' value='" + $("#editPostModel #ddl_post_remark").val() + "'/>";
		row += "<input type='hidden' name='RequestPostFTE' value='" + $("#editPostModel #postFTE").val() + "'/>";
		row += "<input type='hidden' name='RequestPostFTEValue' value='" + $("#editPostModel #postFTEValue").val() + "'/>";
		row += "<input type='hidden' name='RequestPositionStatus' value='" + $("#editPostModel #ddl_position_status").val() + "'/>";
		row += "<input type='hidden' name='RequestPositionStartDate' value='" + $("#editPostModel #dp_position_start_date").val() + "'/>";
		row += "<input type='hidden' name='RequestPositionEndDate' value='" + $("#editPostModel #dp_position_end_date").val() + "'/>";
		row += "<input type='hidden' name='RequestClusterRefNo' value='" + $("#editPostModel #txt_cluster_ref_no").val() + "'/>";
		row += "<input type='hidden' name='RequestClusterRemark' value='" + $("#editPostModel #txt_clusterRemark").val() + "'/>";
		
		if ($("#editPostModel #ddl_post_duration").val() != 'R') { 
			if ($("#editPostModel #rd_Duartion").prop("checked")) {
				row += "<input type='hidden' name='RequestRdDuration' value='DURATION_PERIOD'/>";
			}
			else {
				row += "<input type='hidden' name='RequestRdDuration' value='FIXED_END_DATE'/>";
			}
		}
		else {
			row += "<input type='hidden' name='RequestRdDuration' value=''/>";
		}
		
		//Funding
		row += "<input type='hidden' name='RequestResSupFrExt' value='" + $("#editPostModel #ddl_res_sup_fr_ext").val() + "'/>";
		row += "<input type='hidden' name='RequestResSupRemark' value='" + $("#editPostModel #txt_res_sup_remark").val() + "'/>";
		
		row += "<input type='hidden' name='RequestProposedPostId' value='" + $("#editPostModel #txt_proposed_post_id").val() + "'/>";
		row += "<input type='hidden' name='RequestPostIdJust' value='" + $("#editPostModel #txt_post_id_just").val() + "'/>";
		
		if ($('#editPostModel #cbHoBuyServiceInd').prop("checked")) {
			row += "<input type='hidden' name='RequestHoBuyServiceInd' value='Y'/>";
		}
		else {
			row += "<input type='hidden' name='RequestHoBuyServiceInd' value='N'/>";
		}
		
		row += "<input type='hidden' name='hcmPositionId' value='" + $("#editPostModel #tmpHCMPositionId").val() + "'/>";
		
		row += "<input type='hidden' name='requestProgramType' value='" + $("#editPostModel #programType").val() + "'/>";
		row += "<input type='hidden' name='requestSubSpecialty' value='" + $("#editPostModel #subSpecialty").val() + "'/>";
		row += "<input type='hidden' name='requestPostExist' value='N'/>";
		row += "<div name='hiddenTable'>";
		
		var numberOfFundingSource = $("#tblFunding tr").length;				
		
		for (var i=0; i<numberOfFundingSource; i++) {
							
			row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].annualPlanInd' value='" + $($("#tblFunding select[name$='annualPlanInd']")[i]).val()  + "'/>";
			row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].programYear' value='" + $($("#tblFunding select[name$='programYear']")[i]).val()  + "'/>";
			row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].programCode' value='" + $($("#tblFunding input[name$='programCode']")[i]).val()  + "'/>";
			row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].programName' value='" + $($("#tblFunding input[name$='programName']")[i]).val()  + "'/>";
			row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].programTypeCode' value='" + $($("#tblFunding select[name$='programTypeCode']")[i]).val()  + "'/>";
			row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].fundSrcId' value='" + $($("#tblFunding select[name$='fundSrcId']")[i]).val()  + "'/>";
			row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].fundSrcSubCatId' value='" + $($("#tblFunding select[name$='fundSrcSubCatId']")[i]).val()  + "'/>";
			row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].startDateStr' value='" + $($("#tblFunding input[name$='fundSrcStartDate']")[i]).val()  + "'/>";
			row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].endDateStr' value='" + $($("#tblFunding input[name$='fundSrcEndDate']")[i]).val()  + "'/>";
			row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].fundSrcFte' value='" + $($("#tblFunding input[name$='fundSrcFte']")[i]).val() + "'/>";
			row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].fundSrcRemark' value='" + $($("#tblFunding textarea[name$='fundSrcRemark']")[i]).val()  + "'/>";
			row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].inst' value='" + $($("#tblFunding input[name$='inst']")[i]).val() + "'/>";
			row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].section' value='" + $($("#tblFunding input[name$='section']")[i]).val() + "'/>";
			row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].analytical' value='" + $($("#tblFunding input[name$='analytical']")[i]).val() + "'/>";
		}
							
		row += "</div>";
		row += "</td>";
		
		row += "</tr></tbody>";
		if ($("#tblPositionTo tbody").length == 0) {
			$("#tblPositionTo thead").after(row);
		}
		else {
			$("#tblPositionTo tbody:last").after(row);
		}
					
		$("#editPostModel").modal("hide");
		
		enableEffectiveDate();
		
		postSeq += 1;
		
		$("#btnAddToPost").prop("disabled", true);
		$("#btnAddExistingPost").prop("disabled", true);
	}
	
	function selectPost(clusterCode, instCode, postUid, postId, rankDesc, annualPlanDesc, postDurationDesc, postFte, deptCode, staffGroupCode) {
		// Add to From Post
		if (fromPost == "Y") {
			$.ajax({
	            url: "<%=request.getContextPath() %>/api/searchExistingRequest",
	            type: "POST",
	            data: {searchPostNo: postUid},
	            dataType: 'json',
	            cache: false,
	            success: function(data) {
	            	var haveInProgress = false;
	            	
	            	if (data.jsonResultResponse != null && data.jsonResultResponse.length > 0) {
	            		var responseStr = "<table class='table-bordered mprs_table'><thead><th>Request Date</th><th>Request Type</th><th>Request ID</th><th>Status</th></thead><tbody>";
	            		for (i=0; i<data.jsonResultResponse.length; i++) {
	            			var d = new Date(data.jsonResultResponse[i].requestDate);
	            			
		            		responseStr += "<tr>";
		            		responseStr += "<td>" + d.getDate() + "/" + (d.getMonth()+1) + '/' + d.getFullYear() + "</td>";
							responseStr += "<td>" + data.jsonResultResponse[i].requestType + "</td>";
							responseStr += "<td>" + data.jsonResultResponse[i].requestId + "</td>";
							responseStr += "<td>" + data.jsonResultResponse[i].statusDesc + "</td>";
							responseStr += "</tr>";
							
							if (data.jsonResultResponse[i].statusDesc != "Confirmed") {
								haveInProgress = true;
							}
	            		}
	            		
						$("#warningTitle").html("This post cannot be selected because it is being processed in other outstanding request.");
	            		$("#warningContent").html(responseStr);
	            		$("#warningModal").modal("show");
	            	}
	            	
	            	if (!haveInProgress) {
						// Remove the selected row
						if ($("#tblPositionFrom tbody tr td").html() == "No record found.") {
							$("#tblPositionFrom tbody").remove();
						}
				
						var row = "<tbody><tr>";
						row += "<td><input type='hidden' name='requestPostNo' value='" + postUid + "'/>"
								+ postId + "</td>";
						row += "<td>" + rankDesc + "</td>";
						row += "<td>" + postDurationDesc + "</td>";
						row += "<td>" + postFte + "</td>";
						row += "<td style='text-align:center'><button type='button' class='btn btn-primary' onclick='showPostDetails(\"" + postUid + "\")'>View</button><button type='button' class='btn btn-primary' onclick='removePost($(this));$(\"#btnAddFromPost\").prop(\"disabled\", false);enableEffectiveDate()'><i class='fa fa-times'></i> Remove</button></td>";
						row += "</tr></tbody>";
						if ($("#tblPositionFrom tbody").length == 0) {
							$("#tblPositionFrom thead").after(row);
						}
						else {
							$("#tblPositionFrom tbody:last").after(row);
						}
						
						enableEffectiveDate(); 
						
						// Update the hidden staff group
						$("#tmpStaffGroup").val(staffGroupCode);
						
						$("#btnAddFromPost").prop("disabled", true);
			
						// Close the search Dialog
						$("#searchResultModel").modal("hide");
					}	
				}
			});
		}
		// Add to "To" Post
		else {
			// ST09240 - Check whether the case is exist in the from list
			for (var m=0; m<$("#tblPositionFrom tbody").length; m++) {
				var tmpPostId = $($($("#tblPositionFrom tbody")[m]).find("td")[0]).text().trim();
				if (tmpPostId == postId) {
					$("#divErrorMsg").html('Selected post cannot exist in Upgraded From Post list.');
					$("#divError").show();
					$("#searchResultModel").modal("hide");
	            	hideLoading();
	            		
	            	return;
				}
			}			
		
			showLoading();
			$.ajax({
	            url: "<%=request.getContextPath() %>/api/request/getPostDetails",
	            type: "POST",
	            data: {searchPostNo: postUid},
	            dataType: 'json',
	            cache: false,
	            success: function(data) {
	            	// Check is the Post Start Date == Effective Start Date
	            	if ($("#effectiveDate").val() != data.postStartDate) {
	            		$("#divErrorMsg").html('Post Start Date does not equal to the selected Effective date.');
						$("#divError").show();
						$("#searchResultModel").modal("hide");
	            		hideLoading();
	            		
	            		return;
	            	}
	            
	            	// Remove the selected row
					if ($("#tblPositionTo tbody tr td").html() == "No record found.") {
						$("#tblPositionTo tbody").remove();
					}
	            
					var durationDesc = postDurationDesc;
				
					var rowNum = $("#tblPositionTo tbody tr").length-1;
					if (rowNum < 0) {
						rowNum = 0;
					}
				
					var row = "<tbody><tr id='" + postSeq + "'>";
					row += "<td><input type='hidden' name='requestPostNoTo' value=''/>" + postId + " [Existing]</td>";
					row += "<td>" + rankDesc + "</td>";
					row += "<td>" + durationDesc + "</td>";
					row += "<td>" + data.postFTEValue + "</td>";
					row += "<td style='text-align:center'><button type='button' name='btnEdit' class='btn btn-primary' onclick='editNewPosition(\""
							+ postSeq
							+ "\", this, " + rowNum + ")' style='margin-right:5px;'>View</button><button type='button' class='btn btn-primary' onclick='removePost($(this));$(\"#btnAddExistingPost\").prop(\"disabled\", false);$(\"#btnAddToPost\").prop(\"disabled\", false);enableEffectiveDate()'><i class='fa fa-times'></i> Remove</button>";
					//Position Details
					row += "<input type='hidden' name='RequestCluster' value='" + replaceNull(data.clusterCode) + "'/>";
					row += "<input type='hidden' name='RequestInst' value='" + replaceNull(data.instCode) + "'/>";
					row += "<input type='hidden' name='RequestDept' value='" + replaceNull(data.deptCode) + "'/>";
					row += "<input type='hidden' name='RequestStaffGroup' value='" + replaceNull(data.staffGroupCode) + "'/>";
					row += "<input type='hidden' name='RequestRank' value='" + replaceNull(data.rankCode) + "'/>";
					row += "<input type='hidden' name='RequestUnit' value='" + replaceNull(data.unit) + "'/>";
					row += "<input type='hidden' name='RequestPostTitle' value='" + replaceNull(data.postTitle) + "'/>";
					row += "<input type='hidden' name='RequestPostDuration' value='" + replaceNull(data.postDuration) + "'/>";
					row += "<input type='hidden' name='RequestPostStartDate' value='" + replaceNull(data.postStartDate) + "'/>";
					row += "<input type='hidden' name='RequestDurationValue' value='" + replaceNull(data.limitDurationNo) + "'/>";
					row += "<input type='hidden' name='RequestDurationUnit' value='" + replaceNull(data.limitDurationUnit) + "'/>";
					row += "<input type='hidden' name='RequestPostActualStartDate' value='" + replaceNull(data.postActualStartDate) + "'/>";
					
					row += "<input type='hidden' name='RequestPostActualEndDate' value='" + replaceNull(data.limitDurationEndDate) + "'/>";
					row += "<input type='hidden' name='RequestPostRemark' value='" + replaceNull(data.postRemark) + "'/>";
					row += "<input type='hidden' name='RequestPostFTE' value='" + replaceNull(data.postFTE) + "'/>";
					row += "<input type='hidden' name='RequestPostFTEValue' value='" + replaceNull(data.postFTEValue) + "'/>";
					row += "<input type='hidden' name='RequestPositionStatus' value='" + replaceNull(data.positionStatus) + "'/>";
					row += "<input type='hidden' name='RequestPositionStartDate' value='" + replaceNull(data.positionStartDate) + "'/>";
					row += "<input type='hidden' name='RequestPositionEndDate' value='" + replaceNull(data.positionEndDate) + "'/>";
					row += "<input type='hidden' name='RequestClusterRefNo' value='" + replaceNull(data.clusterRefNo) + "'/>";
					row += "<input type='hidden' name='RequestClusterRemark' value='" + replaceNull(data.clusterRemark) + "'/>";
					row += "<input type='hidden' name='RequestRdDuration' value='" + replaceNull(data.limitDurationType) + "'/>";
					
					row += "<input type='hidden' name='RequestResSupFrExt' value='" + replaceNull(data.res_sup_fr_ext) + "'/>";
					row += "<input type='hidden' name='RequestResSupRemark' value='" + replaceNull(data.res_sup_remark) + "'/>";
					row += "<input type='hidden' name='RequestProposedPostId' value='" + replaceNull(data.postId) + "'/>";
					row += "<input type='hidden' name='RequestPostIdJust' value='" + replaceNull(data.post_id_just) + "'/>";
					row += "<input type='hidden' name='RequestHoBuyServiceInd' value='" + replaceNull(data.hoBuyServiceInd) + "'/>";
					row += "<input type='hidden' name='hcmPositionId' value='" + replaceNull(data.hcmPositionId) + "'/>";
					
					row += "<input type='hidden' name='requestProgramType' value='" + replaceNull(data.programType) + "'/>";
					row += "<input type='hidden' name='requestSubSpecialty' value='" + replaceNull(data.subSpecialty) + "'/>";
					row += "<input type='hidden' name='requestPostExist' value='Y'/>";
					
					row += "<div name='hiddenTable'>";
					
					//Funding
					var numberOfFundingSource = $("#tblFunding tr").length;
					for (var i=0; i<numberOfFundingSource; i++) {
										
						row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].annualPlanInd' value='" + data.fundingList[i].annualPlanInd  + "'/>";
						row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].programYear' value='" + data.fundingList[i].programYear  + "'/>";
						row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].programCode' value='" + data.fundingList[i].programCode  + "'/>";
						row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].programName' value='" + data.fundingList[i].programName  + "'/>";
						row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].programTypeCode' value='" + data.fundingList[i].programTypeCode  + "'/>";
						row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].fundSrcId' value='" + data.fundingList[i].fundSrcId  + "'/>";
						row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].fundSrcSubCatId' value='" + data.fundingList[i].fundSrcSubCatId  + "'/>";
						row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].startDateStr' value='" + data.fundingList[i].fundSrcStartDateStr  + "'/>";
						row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].endDateStr' value='" + data.fundingList[i].fundSrcEndDateStr  + "'/>";
						row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].fundSrcFte' value='" + data.fundingList[i].fundSrcFte  + "'/>";
						row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].fundSrcRemark' value='" + data.fundingList[i].fundSrcRemark  + "'/>";
						row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].inst' value='" + data.fundingList[i].inst  + "'/>";
						row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].section' value='" + data.fundingList[i].section  + "'/>";
						row += "<input type='hidden' name='requestPositionToList[" + rowNum +"].requestFundingList[" + i + "].analytical' value='" + data.fundingList[i].analytical  + "'/>";
					}
										
					row += "</div>";
					row += "</tr></tbody>";
					
					if ($("#tblPositionTo tbody").length == 0) {
						$("#tblPositionTo thead").after(row);
					}
					else {
						$("#tblPositionTo tbody:last").after(row);
					}
								
					postSeq += 1;
					
					enableEffectiveDate();
		
					$("#btnAddToPost").prop("disabled", true);
					$("#btnAddExistingPost").prop("disabled", true);
					
					// Close the search Dialog
					$("#searchResultModel").modal("hide");
					
					hideLoading();
				}
			});
		}
	}
	
	function replaceNull(inStr) {
		if (inStr == null) {
			return "";
		}
		
		return inStr;	
	}
	
	function changeRankDropdown2() {
		$("#lblSubSpecialty").show();
		$("#subSpecialty").show();
	
		$("#editPostModel #txt_proposed_post_id").val('');
		
		changerRank();
	}

	$(function() {
		$.ajaxSetup({cache: false});
	
		$("#requestStatus").prop("disabled", true);

		var status = $("#requestStatus").val();

		// If Withdraw / Approved, disable All field
		if (status == "A" || status == "W") {
			$("select").prop("disabled", true);
			$("input[type='text']").prop("disabled", true);
			$("#btnAddToPost").prop("disabled", true);
			$("#btnAddExistingPost").prop("disabled", true);
		}

		if ($("#tblPositionFrom tbody tr").length == 0) {
			$("#tblPositionFrom thead").after("<tbody><tr><td colspan='6'>No record found.</td></tr></tbody>");
							
			$("#btnAddFromPost").prop("disabled", false);
		}
		else {
			$("#btnAddFromPost").prop("disabled", true);
		}
		
		
		if ($("#tblPositionTo tbody tr").length == 0) {
			$("#tblPositionTo thead").after("<tbody><tr><td colspan='6'>No record found.</td></tr></tbody>");
							
			$("#btnAddToPost").prop("disabled", false);
			$("#btnAddExistingPost").prop("disabled", false);
		}
		else {
			$("#btnAddToPost").prop("disabled", true);
			$("#btnAddExistingPost").prop("disabled", true);
		}
		
		
		$("#frmDialog").bootstrapValidator({
			message: 'This value is not valid',
		    excluded: [':disabled'],
			live: "submitted",
			fields: {
				postFTEValue : {
					validators : {
						numeric: {
							message: 'Inputted value is invalid'
						},
						between: {
							min: 0.01,
							max: 0.99,
							message: 'Value should between 0.01 and 0.99'
						}
					}
				}, 
				/*fund_src_1st_fte : {
					validators : {
						numeric: {
							message: 'Inputted value is invalid'
						},
						callback: {
							message: 'Inputted value is invalid',
							callback: function(value, validator, $field) {
								if (value == "") {
									return true;
								}
							
								if (value < 0.01 || value > 1) {
									return {
										valid: false,
										message: 'Value should between 0.01 and 1'
									}
								}
								else {
									return true; 
								}
							}
						}
					}
				},
				fund_src_2nd_fte : {
					validators : {
						numeric: {
							message: 'Inputted value is invalid'
						},
						callback: {
							message: 'Inputted value is invalid',
							callback: function(value, validator, $field) {
								if (value == "") {
									return true;
								}
							
								if (value < 0.01 || value > 1) {
									return {
										valid: false,
										message: 'Value should between 0.01 and 1'
									}
								}
								else {
									return true; 
								}
							}
						}
					}
				},
				fund_src_3rd_fte : {
					validators : {
						numeric: {
							message: 'Inputted value is invalid'
						},
						callback: {
							message: 'Inputted value is invalid',
							callback: function(value, validator, $field) {
								if (value == "") {
									return true;
								}
							
								if (value < 0.01 || value > 1) {
									return {
										valid: false,
										message: 'Value should between 0.01 and 1'
									}
								}
								else {
									return true; 
								}
							}
						}
					}
				},
				fund_src_1st_start_date: {
					validators : {
						callback: {
							message: 'End date should be on or after Start Date',
							callback: function(value, validator, $field) {
								if ($("#frmDialog #txt_fund_src_1st_start_date").val() == "") {
									return {
										valid: false,
										message: 'Please enter a value'
									}
								}
								
								if ($("#frmDialog #txt_fund_src_1st_start_date").val() == "" || $("#frmDialog #txt_fund_src_1st_end_date").val() == "") {
									return true;
								}
								
								var diff = getDaysBetween($("#frmDialog #txt_fund_src_1st_start_date").val(), $("#frmDialog #txt_fund_src_1st_end_date").val());
								
								if (diff <= 0) {
									return true;
								}
								else {
									return {
										valid: false,
										message: 'End date should be on or after Start Date'
									}
								}	
							}
						}
					}
				},
				fund_src_1st_end_date: {
					validators : {
						callback: {
							message: 'End date should be on or after Start Date',
							callback: function(value, validator, $field) {
								if ($("#frmDialog #txt_fund_src_1st_end_date").val() == "") {
									if (endDateMandatory) {
										return {
											valid: false,
											message: 'Please enter a value'
										}
									}
								}
								
								if ($("#frmDialog #txt_fund_src_1st_start_date").val() == "" || $("#frmDialog #txt_fund_src_1st_end_date").val() == "") {
									return true;
								}			
								
								var diff = getDaysBetween($("#frmDialog #txt_fund_src_1st_start_date").val(), $("#frmDialog #txt_fund_src_1st_end_date").val());
								
								if (diff <= 0) {
									return true;
								}
								else {
									return {
										valid: false,
										message: 'End date should be on or after Start Date'
									}
								}	
							}
						}
					}
				},
				fund_src_2nd_start_date: {
					validators : {
						callback: {
							message: 'End date should be on or after Start Date',
							callback: function(value, validator, $field) {
								if ($("#frmDialog #txt_fund_src_2nd_start_date").val() == "" || $("#frmDialog #txt_fund_src_2nd_end_date").val() == "") {
									return true;
								}
							
								var diff = getDaysBetween($("#frmDialog #txt_fund_src_2nd_start_date").val(), $("#frmDialog #txt_fund_src_2nd_end_date").val());
								
								if (diff <= 0) {
									return true;
								}
								else {
									return {
										valid: false,
										message: 'End date should be on or after Start Date'
									}
								}	
							}
						}
					}
				},
				fund_src_2nd_end_date: {
					validators : {
						callback: {
							message: 'End date should be on or after Start Date',
							callback: function(value, validator, $field) {
								if ($("#frmDialog #txt_fund_src_2nd_start_date").val() == "" || $("#frmDialog #txt_fund_src_2nd_end_date").val() == "") {
									return true;
								}
							
								var diff = getDaysBetween($("#frmDialog #txt_fund_src_2nd_start_date").val(), $("#frmDialog #txt_fund_src_2nd_end_date").val());
								
								if (diff <= 0) {
									return true;
								}
								else {
									return {
										valid: false,
										message: 'End date should be on or after Start Date'
									}
								}	
							}
						}
					}
				},
				fund_src_3rd_start_date: {
					validators : {
						callback: {
							message: 'End date should be on or after Start Date',
							callback: function(value, validator, $field) {
								if ($("#frmDialog #txt_fund_src_3rd_start_date").val() == "" || $("#frmDialog #txt_fund_src_3rd_end_date").val() == "") {
									return true;
								}
							
								var diff = getDaysBetween($("#frmDialog #txt_fund_src_3rd_start_date").val(), $("#frmDialog #txt_fund_src_3rd_end_date").val());
								
								if (diff <= 0) {
									return true;
								}
								else {
									return {
										valid: false,
										message: 'End date should be on or after Start Date'
									}
								}	
							}
						}
					}
				},
				fund_src_3rd_end_date: {
					validators : {
						callback: {
							message: 'End date should be on or after Start Date',
							callback: function(value, validator, $field) {
								if ($("#frmDialog #txt_fund_src_3rd_start_date").val() == "" || $("#frmDialog #txt_fund_src_3rd_end_date").val() == "") {
									return true;
								}
							
								var diff = getDaysBetween($("#frmDialog #txt_fund_src_3rd_start_date").val(), $("#frmDialog #txt_fund_src_3rd_end_date").val());
								
								if (diff <= 0) {
									return true;
								}
								else {
									return {
										valid: false,
										message: 'End date should be on or after Start Date'
									}
								}	
							}
						}
					}
				},*/
			},
		}).on('success.form.bv', function(e){
		});
		
		$("#frmDialog").bootstrapValidator({
			message: ' This value is not valid',
			live: "submitted",
			fields: {
			},
		})
		.on('error.field.bv', function(e, data) {
		
			hideLoading();
			
			var $invalidfields = data.bv.getInvalidFields().eq(0);
			
			var $tabPane = $invalidfields.parents('.tab-pane'),
			invalidTabId = $tabPane.attr('id');
			
			if(!$tabPane.hasClass('active')){
			
			//Activate 
			$tabPane.parents('.tab-content')
			.find('.tab-pane')
			.each(function(index,tab){
					var tabId = $(tab).attr('id'),
					$li = $('a[href="#'+ tabId + '"][data-toggle="tab"]').parent();
					
					if (tabId === invalidTabId){
						$(tab).addClass('active');
						$li.addClass('active');
						 $('a[href="#'+ tabId + '"][data-toggle="tab"]').attr("aria-expanded", true);
					} else {
						$(tab).removeClass('active');
						$li.removeClass('active');
						$('a[href="#'+ tabId + '"][data-toggle="tab"]').attr("aria-expanded", false);
					}
				});
			};
		})
		.on('success.form.bv', function(e){
			var currentTarget = e.target;
			e.preventDefault();
			
		});
		
		
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
			var currentTarget = e.target;
			e.preventDefault();
			
			if (!validateForm()) {
				hideLoading();
			}
			else {
				showLoading();
				
				// Check whether the rank is upgraded
				var fromRank = $("#tblPositionFrom tr:eq(1) td:eq(1)").text().trim();
				var toRank = $("#tblPositionTo tr:eq(1) td:eq(1)").text().trim();
				
				$.ajax ({
		            url: "<%=request.getContextPath() %>/common/getRank",
		            type: "POST",
		            data: {rankCode: fromRank, staffGroupCode: $("#tmpStaffGroup").val()},
		            success: function(data) {
  		            	 var haveMatch = false;
		            	 for (var i=0; i<data.rankList.length; i++) {
		            	 	if (toRank == data.rankList[i].rankName) {
							    haveMatch = true;	
								break;
		            	 	}
		            	 }
		            	 
		            	 var errMsg = "";
		            	 if (!haveMatch) {
		            	 	errMsg += "The Ranks of Upgrade are invalid.";
		            	 	displayError(errMsg);
							hideLoading();
		            	 }
		            	 else {
							var postUid = [];
							for (var i=0; i<$("input[name='requestPostNo']").length; i++) {
								postUid.push($($("input[name='requestPostNo']")[i]).val());
							}
							
							var isRecurrent = false;
							var url = "";
							if ($("#tblPositionTo tr:eq(1) td:eq(2)").text().trim() == "Recurrent") {
								isRecurrent = true;
							}
							
							// If To Post is recurrent
							if (isRecurrent) {
								url = "<%=request.getContextPath() %>/request/validateDeletion";
							}
							else {
								url = "<%=request.getContextPath() %>/request/validateFrozen";
							}
						
							$.ajax({
					            url: url,
					            type: "POST",
					            data: {postUid: postUid, effectiveDate: $("#effectiveDate").val()},
					            success: function(data) {
					            	if (data != "") {
					            		hideLoading();
					            		displayError(data);
					            	}
					            	else {
					            		$("#frmDetail input[type='text']").attr("disabled", false);
										$("#frmDetail textarea").attr("disabled", false);
										$("#frmDetail select").attr("disabled", false);
										$("#frmDetail input[type='hidden']").attr("disabled", false);
										
										currentTarget.submit();
										showLoading();
					            	}
					            }
					        });		            	 
		            	 }
		            },
		            error: function(request, status, error) {
		                //Ajax failure
		                alert("Some problem occur during call the ajax: " + request.responseText);
		            }
		        });
			}
		});
		
		// Enable and disable button
		if ("Y" != $("#canEditDetailInfo").val()) {
			$("input[name='btnAddPost']").prop("disabled", true); 
			
			// Disable the approval section
			$("#divApproval input").attr("disabled", true);
			$("#divApproval input[type='file']").attr("disabled", true);
			$("#divApproval input[name='btnUploadFile']").attr("disabled", true);
			$("#divApproval input[name='btnAddUploadFile']").attr("disabled", true);
			
			$("button[name='btnAddPost']").prop("disabled", true);
			$("button[name='btnRemovePost']").prop("disabled", true);
			
			$("#requester").attr("disabled", true);
			$("#effectiveDate").attr("disabled", true);
			$("#requestReason").attr("disabled", true);
			
			$("input[type='file']").prop("disabled", true);
			$("button[name='btnUploadFile']").attr("disabled", true);
			$("button[name='btnRemoveFile']").attr("disabled", true);
		}
		
		// For autocomplete - Start
		$("#ddHcmPostTitle").autocomplete({
			source: function(request, response) {
				$.getJSON("<%= request.getContextPath() %>/hcm/getHCMPostTitleListByStaffGroup?staffGroup=" + $("#tmpStaffGroup option:selected").text(), request, function(result){
					response($.map(result, function(item){
						return {label: item.titleId, value: item.titleId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#hcmPostTitle").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#hcmPostTitle").val(ui.item.value);
				return false;
			}
		});
		
		$("#ddHcmPostOrganization").autocomplete({
			source: function(request, response) {
				$.getJSON("<%= request.getContextPath() %>/hcm/getHCMPostOrganizationList", request, function(result){
					response($.map(result, function(item){
						return {label: item.organizatonName, value: item.organizatonId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#hcmPostOrganization").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#hcmPostOrganization").val(ui.item.value);
				return false;
			}
		});
		
		$("#ddHcmOrganization").autocomplete({
			source: function(request, response) {
				$.getJSON("<%= request.getContextPath() %>/hcm/getHCMOrganizationList", request, function(result){
					response($.map(result, function(item){
						return {label: item.organizatonName, value: item.organizatonId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#hcmOrganization").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#hcmOrganization").val(ui.item.value);
				return false;
			}
		});
		
		$("#ddHcmJob").autocomplete({
			source: function(request, response) {
				$.getJSON("<%= request.getContextPath() %>/hcm/getHCMJobList", request, function(result){
					response($.map(result, function(item){
						return {label: item.jobName, value: item.jobId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#hcmJob").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#hcmJob").val(ui.item.value);
				return false;
			}
		});
		
		$("#ddHcmPostTitle").on("keydown focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#hcmPostTitle").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#hcmPostTitle").val("");
				return;
			}
		});
		
		$("#ddHcmJob").on("keydown focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#hcmJob").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#hcmJob").val("");
				return;
			}
		});
		
		$("#ddHcmPostOrganization").on("keydown focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#hcmPostOrganization").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#hcmPostOrganization").val("");
				return;
			}
		});
		
		$("#ddHcmOrganization").on("keydown focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#hcmOrganization").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#hcmOrganization").val("");
				return;
			}
		});
		
		$("#ddLocation").on("keydown focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#location").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#location").val("");
				return;
			}
		});
		// For autocomplete - End
		
		$("#editPostModel #txt_post_actual_start_date").attr("disabled", true);
		
		<c:choose>
			<c:when test="${formBean.updateSuccess == 'Y'}">
				$("#divSuccessMsg").html('<c:out value="${formBean.displayMessage}"/>');
				$("#divSuccess").show();
			</c:when>
			<c:when test="${formBean.updateSuccess == 'N'}">
				$("#divErrorMsg").html('<c:out value="${formBean.displayMessage}"/>');
				$($("div[class='alert-box-icon-danger']")[0]).css("height", $("#divError").height())
				$("#divError").show();
			</c:when>
		</c:choose>
		
		<c:choose>
			<c:when test="${ formBean.userHaveSaveRight != 'Y' }">
				$("button[name='btnEdit']").html("View");
				$("#btnSaveNewPosition").hide();
			</c:when>
		</c:choose>
		
		// Set Request ID to readonly instead of disable
		$("#requestId").prop("disabled", false);
		$("#requestId").attr("readonly", true); 
		
		enableEffectiveDate();
	});

	function addNewPosition() {
		if ($("#tblPositionFrom tbody tr td").html() == "No record found." || $("#tblPositionFrom tbody tr").size() == 0) {
			var errMsg = "Please select at least one from post.<br/>";
			$("#divErrorMsg").html(errMsg);
			$("#divError").show();
			
			$('html,body').animate({ scrollTop: 0 }, 'slow');
			
			return;
		}
	
		$("#divSuccess").hide();
		$("#divError").hide();
	
		if ($("#effectiveDate").val() == "") {
			displayError("Please select effective date.");
			return;
		}
	
		if ($("#tblPositionFrom tbody tr td").length > 1) {
			var rank = $("#tblPositionFrom tbody tr:eq(0) td:eq(1)").html().trim();
			changeRankDropdownList(rank);
		}
		else {
			changeRankDropdownList("");
		}
	
		initNewPostModal();
		checkEnableField();
		
		// Default status to "Active"
		$('#editPostModel #ddl_position_status').val("ACTIVE");
		
		$('.nav-pills a[href="#tab0_basic_1"]').tab('show');
		
		// Default the staff group default
		var staffGroupCode =  $("#tmpStaffGroup").val();
		var staffGroupDesc =  $("#tmpStaffGroup option:selected").text();
		
		// Refresh staff group drop down
		$("#editPostModel #ddl_staff_group").empty();
		var option = "<option value='" + staffGroupCode + "'>" + staffGroupDesc + "</option>";
	    $("#editPostModel #ddl_staff_group").append(option);
		$("#editPostModel #ddl_staff_group").val(staffGroupCode);
		
		// Refresh rank / department
		changeStaffGroup();
		
		$("#searchPanel").show();
		$("#tblHCMResult").hide();
		$("#editPostModel").modal("show");
	}
	
	function initNewPostModal() {
		$("#hcmJob").val("");  
		$("#hcmPostTitle").val("");
		$("#hcmOrganization").val("");
		$("#hcmPostOrganization").val("");
		$("#hcmUnitTeam").val("");
	
		$("#ddHcmJob").val("");  
		$("#ddHcmPostTitle").val("");
		$("#ddHcmOrganization").val("");
		$("#ddHcmPostOrganization").val("");
	
		setEmpty('ddl_cluster');
		setEmpty('ddl_institution');
		setEmpty('ddl_department');
		setEmpty('ddl_staff_group');
		setEmpty('ddl_rank');
		setEmpty('txt_unit');
		setEmpty('postTitle');
		setEmpty('ddl_post_duration');
		setEmpty('txt_postStartDate');
		setEmpty('rd_Duartion');
		setEmpty('txt_limit_duration_no');
		setEmpty('ddl_limit_duration_unit');
		setEmpty('txt_post_actual_start_date');
		setEmpty('rd_Duartion2');
		setEmpty('txt_post_actual_end_date');
		setEmpty('ddl_post_remark');
		setEmpty('postFTE');
		setEmpty('postFTEValue');
		setEmpty('ddl_position_status');
		setEmpty('dp_position_start_date');
		setEmpty('dp_position_end_date');
		setEmpty('txt_cluster_ref_no');
		setEmpty('txt_clusterRemark');
		
		//Funding
		setEmpty('hiddenMPRSPostNo');
		setEmpty('ddl_annual_plan_ind');
		setEmpty('ddl_program_year');
		setEmpty('txt_program_code');
		setEmpty('txt_program_name');
		setEmpty('txt_program_remark');
		setEmpty('ddl_1st_funding_source');
		setEmpty('ddl_2nd_funding_source');
		setEmpty('ddl_3rd_funding_source');
		setEmpty('txt_fund_src_1st_start_date');
		setEmpty('txt_fund_src_2nd_start_date');
		setEmpty('txt_fund_src_3rd_start_date');
		setEmpty('txt_fund_src_1st_end_date');
		setEmpty('txt_fund_src_2nd_end_date');
		setEmpty('txt_fund_src_3rd_end_date');
		setEmpty('fund_remark');
		setEmpty('txt_fund_cost_code');
		
		setEmpty('fund_src_2nd_remark');
		setEmpty('fund_src_3rd_remark');
		setEmpty('fund_src_1st_fte');
		setEmpty('fund_src_2nd_fte');
		setEmpty('fund_src_3rd_fte');
		
		setEmpty('ddl_res_sup_fr_ext');
		setEmpty('txt_res_sup_remark');
		
		setEmpty('txt_proposed_post_id');
		setEmpty('txt_post_id_just');
		
		
		setEmpty('programType');
		setEmpty('subSpecialty');
		
		$("#ddl_cluster").val("");
		$("#ddl_institution").val("");
		$("#ddl_department").val("");
		
		$('#editPostModel #rd_Duartion').prop('checked', false);
		$('#editPostModel #rd_Duartion2').prop('checked', false);
		
		/*$("#frmDialog").data('bootstrapValidator').enableFieldValidators('staffGroup', false);
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('postTitle', false);
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('rank', false);
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('department', false);
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('postDuration', false);
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('postFTE', false);
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('postFTEValue', false);
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('postStartDate', false);
		
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('staffGroup', true);
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('postTitle', true);
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('rank', true);
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('department', true);
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('postDuration', true);
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('postFTE', true);
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('postFTEValue', true);
		$("#frmDialog").data('bootstrapValidator').enableFieldValidators('postStartDate', true);*/
		
		$("#btnSaveNewPosition").attr("onclick","saveNewPosition()"); 	
		
		// Disable the Radio button for duration / fixed end date
		$("#editPostModel #rd_Duartion").attr("disabled", true);
		$("#editPostModel #rd_Duartion2").attr("disabled", true);
		$("#editPostModel #txt_post_actual_end_date").attr("disabled", true);
		$("#editPostModel #txt_limit_duration_no").attr("disabled", true);
		$("#editPostModel #ddl_limit_duration_unit").attr("disabled", true);
		
		// Bring the effective date to the effective start date
		$("#editPostModel #txt_postStartDate").val($("#effectiveDate").val());
		$("#editPostModel #txt_postStartDate").attr("disabled", true);
	}
	function setEmpty(a) {
		$('#editPostModel #' + a + '').val('');
	}
	
	function clickDuration2() {
		if ($("#editPostModel #rd_Duartion").prop("checked") == false) {
			$("#editPostModel #ddl_limit_duration_unit").val("");
			$("#editPostModel #txt_limit_duration_no").val("");
			
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('limitDurationUnit', false);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('limitDurationNo', false);
			$("#frmDialog").bootstrapValidator('revalidateField', 'limitDurationUnit');
			$("#frmDialog").bootstrapValidator('revalidateField', 'limitDurationNo');
			
			$("#editPostModel #ddl_limit_duration_unit").attr("disabled", true);
			$("#editPostModel #txt_limit_duration_no").attr("disabled", true);
		}
		else {
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('limitDurationUnit', true);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('limitDurationNo', true);

			$("#editPostModel #ddl_limit_duration_unit").attr("disabled", false);
			$("#editPostModel #txt_limit_duration_no").attr("disabled", false);
		}
		
		if ($("#editPostModel #rd_Duartion2").prop("checked") == false) {
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('limitDurationEndDate', false);
			$("#frmDialog").bootstrapValidator('revalidateField', 'limitDurationEndDate');

			$("#editPostModel #txt_post_actual_end_date").val("");
			$("#editPostModel #txt_post_actual_end_date").attr("disabled", true);
		}
		else {
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('limitDurationEndDate', true);
			$("#editPostModel #txt_post_actual_end_date").attr("disabled", false);
		}
		
		if ($("#editPostModel #rd_Duartion").prop("checked") == true || $("#editPostModel #rd_Duartion2").prop("checked") == true) {
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('limitDurationType', false);
		}
	}
	
	// Added for MPR0002
	function newRequestFTEChecker() {
		if ($("#tblFunding input[name$='fundSrcFte']").length == 0) {
			return true;
		}
	
		if ($($("#tblFunding input[name$='fundSrcFte']")[0]).is(":disabled")) {
			return true;
		}
		
		var showWarning = false;
		var sum = 0;
		
		for (var a=0; a<$("#tblFunding input[name$='fundSrcFte']").length; a++) {
			if ($($("#tblFunding input[name$='fundSrcFte']")[a]).val() == "") {
				showWarning = true;
			}
			else {
				sum += parseFloat($($("#tblFunding input[name$='fundSrcFte']")[a]).val());
			}
		}
		
		var totalFTE = parseFloat($("#editPostModel #postFTEValue").val()).toFixed(4);
		
		// Fixed for ST08706
		sum = sum.toFixed(4);
		
		if (sum != totalFTE || showWarning) {
			$("#warningTitle").html("Warning");
	        $("#warningContent").html("Post FTE value is not equals to the sum of FTE under the Funding related information.");
	        $("#warningModal").modal("show");
	        
	        return false;
		}
		
		return true;
	}
	
	function performUpdate(postID, obj){
		if (!performDialogChecking()) {
			return;
		}
	
		resetValidation();
		
		if (!$("#editPostModel #txt_postStartDate").prop("disabled") 
		       && !$("#editPostModel #txt_post_actual_end_date").prop("disabled")) {
			var postStartDate = $("#editPostModel #txt_postStartDate").val();
			var postEndDate = $("#editPostModel #txt_post_actual_end_date").val();
			var dayBetween = getDaysBetween(postEndDate, postStartDate);
			if (dayBetween < 0) {
				$("#warningTitle").html("Error");
		        $("#warningContent").html("Fixed End Date should be after Post Start Date.");
		        $("#warningModal").modal("show");
				return;
			} 
		}
		var validator = $("#frmDialog").data('bootstrapValidator');
	
		/*validator.validate();
		if (!validator.isValid()) {
			return;
		}*/
		
		// Added for MPR0002
		newRequestFTEChecker();
		var originalStatus = $("input[name='approvalDate']").attr("disabled");
		$("input[name='approvalDate']").attr("disabled", false);
		$("#frmDetail").bootstrapValidator('revalidateField', 'approvalDate');
		$("input[name='approvalDate']").attr("disabled", originalStatus);
		
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestCluster']").val($('#editPostModel #ddl_cluster').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestInst']").val($('#editPostModel #ddl_institution').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestDept']").val($('#editPostModel #ddl_department').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestStaffGroup']").val($('#editPostModel #ddl_staff_group').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestRank']").val($('#editPostModel #ddl_rank').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestUnit']").val($('#editPostModel #txt_unit').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostTitle']").val($('#editPostModel #postTitle').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostDuration']").val($('#editPostModel #ddl_post_duration').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostStartDate']").val($('#editPostModel #txt_postStartDate').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestRdDuration']").val($('#editPostModel #rd_Duartion').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestDurationValue']").val($('#editPostModel #txt_limit_duration_no').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestDurationUnit']").val($('#editPostModel #ddl_limit_duration_unit').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostActualStartDate']").val($('#editPostModel #txt_post_actual_start_date').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestRdDuration2']").val($('#editPostModel #rd_Duartion2').val());
		
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostActualEndDate']").val($('#editPostModel #txt_post_actual_end_date').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostRemark']").val($('#editPostModel #ddl_post_remark').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostFTE']").val($('#editPostModel #postFTE').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostFTEValue']").val($('#editPostModel #postFTEValue').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPositionStatus']").val($('#editPostModel #ddl_position_status').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPositionStartDate']").val($('#editPostModel #dp_position_start_date').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPositionEndDate']").val($('#editPostModel #dp_position_end_date').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestClusterRefNo']").val($('#editPostModel #txt_cluster_ref_no').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestClusterRemark']").val($('#editPostModel #txt_clusterRemark').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestResSupFrExt']").val($('#editPostModel #ddl_res_sup_fr_ext').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestResSupRemark']").val($('#editPostModel #txt_res_sup_remark').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestProposedPostId']").val($('#editPostModel #txt_proposed_post_id').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostIdJust']").val($('#editPostModel #txt_post_id_just').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestHoBuyServiceInd']").val($('#editPostModel #cbHoBuyServiceInd').val());
		
		if ($('#editPostModel #ddl_post_duration').val() != 'R') {
			if ($("#editPostModel #rd_Duartion").prop("checked")) {
				$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestRdDuration']").val("DURATION_PERIOD");			
			}
			else {
				$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestRdDuration']").val("FIXED_END_DATE");
			}
		}
		else {
			$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestRdDuration']").val("");
		}
		
		var rowId = currentIdx;
		
		var numberOfFundingSource = $("#tblFunding tr").length;
		
		// Remove All child
		var currentRow = $("#tblPositionTo tbody tr[id='"+postID+"'] div[name='hiddenTable']")[rowId];
		$(currentRow).empty();
		
		// Append the hidden field
		for (var i=0; i< numberOfFundingSource; i++) {
			var annualPlanInd = $($("#tblFunding select[name$='annualPlanInd']")[i]).val();
			var programYear = $($("#tblFunding select[name$='programYear']")[i]).val();
			var programCode = $($("#tblFunding input[name$='programCode']")[i]).val();
			var programName = $($("#tblFunding input[name$='programName']")[i]).val();
			var programTypeCode = $($("#tblFunding select[name$='programTypeCode']")[i]).val();
			var fundSrcId = $($("#tblFunding select[name$='fundSrcId']")[i]).val();
			var fundSrcSubCatId = $($("#tblFunding select[name$='fundSrcSubCatId']")[i]).val();
			var fundSrcStartDateStr = $($("#tblFunding input[name$='fundSrcStartDate']")[i]).val();
			var fundSrcEndDateStr = $($("#tblFunding input[name$='fundSrcEndDate']")[i]).val();
			var fundSrcFte = $($("#tblFunding input[name$='fundSrcFte']")[i]).val();
			var fundSrcRemark = $($("#tblFunding textarea[name$='fundSrcRemark']")[i]).val();
			var inst = $($("#tblFunding input[name$='inst']")[i]).val();
			var section = $($("#tblFunding input[name$='section']")[i]).val();
			var analytical = $($("#tblFunding input[name$='analytical']")[i]).val();
			
			var annualPlanIndHidden = $('<input>').attr('type', 'hidden');
			$(annualPlanIndHidden).attr('name', 'requestPositionToList[' + rowId +'].requestFundingList[' + i + '].annualPlanInd');
			$(annualPlanIndHidden).val(annualPlanInd);
			$(annualPlanIndHidden).appendTo($(currentRow));
			
			var programYearHidden = $('<input>').attr('type', 'hidden');
			$(programYearHidden).attr('name', 'requestPositionToList[' + rowId +'].requestFundingList[' + i + '].programYear');
			$(programYearHidden).val(programYear);
			$(programYearHidden).appendTo($(currentRow));
			
			var programCodeHidden = $('<input>').attr('type', 'hidden');
			$(programCodeHidden).attr('name', 'requestPositionToList[' + rowId +'].requestFundingList[' + i + '].programCode');
			$(programCodeHidden).val(programCode);
			$(programCodeHidden).appendTo($(currentRow));
			
			var programNameHidden = $('<input>').attr('type', 'hidden');
			$(programNameHidden).attr('name', 'requestPositionToList[' + rowId +'].requestFundingList[' + i + '].programName');
			$(programNameHidden).val(programName);
			$(programNameHidden).appendTo($(currentRow));
			
			var programTypeCodeHidden = $('<input>').attr('type', 'hidden');
			$(programTypeCodeHidden).attr('name', 'requestPositionToList[' + rowId +'].requestFundingList[' + i + '].programTypeCode');
			$(programTypeCodeHidden).val(programTypeCode);
			$(programTypeCodeHidden).appendTo($(currentRow));
			
			var fundSrcIdHidden = $('<input>').attr('type', 'hidden');
			$(fundSrcIdHidden).attr('name', 'requestPositionToList[' + rowId +'].requestFundingList[' + i + '].fundSrcId');
			$(fundSrcIdHidden).val(fundSrcId);
			$(fundSrcIdHidden).appendTo($(currentRow));
			
			var fundSrcSubCatIdHidden = $('<input>').attr('type', 'hidden');
			$(fundSrcSubCatIdHidden).attr('name', 'requestPositionToList[' + rowId +'].requestFundingList[' + i + '].fundSrcSubCatId');
			$(fundSrcSubCatIdHidden).val(fundSrcSubCatId);
			$(fundSrcSubCatIdHidden).appendTo($(currentRow));
			
			var fundSrcStartDateStrHidden = $('<input>').attr('type', 'hidden');
			$(fundSrcStartDateStrHidden).attr('name', 'requestPositionToList[' + rowId +'].requestFundingList[' + i + '].startDateStr');
			$(fundSrcStartDateStrHidden).val(fundSrcStartDateStr);
			$(fundSrcStartDateStrHidden).appendTo($(currentRow));
			
			var fundSrcEndDateStrHidden = $('<input>').attr('type', 'hidden');
			$(fundSrcEndDateStrHidden).attr('name', 'requestPositionToList[' + rowId +'].requestFundingList[' + i + '].endDateStr');
			$(fundSrcEndDateStrHidden).val(fundSrcEndDateStr);
			$(fundSrcEndDateStrHidden).appendTo($(currentRow));
			
			var fundSrcFteHidden = $('<input>').attr('type', 'hidden');
			$(fundSrcFteHidden).attr('name', 'requestPositionToList[' + rowId +'].requestFundingList[' + i + '].fundSrcFte');
			$(fundSrcFteHidden).val(fundSrcFte);
			$(fundSrcFteHidden).appendTo($(currentRow));
			
			var fundSrcRemarkHidden = $('<input>').attr('type', 'hidden');
			$(fundSrcRemarkHidden).attr('name', 'requestPositionToList[' + rowId +'].requestFundingList[' + i + '].fundSrcRemark');
			$(fundSrcRemarkHidden).val(fundSrcRemark);
			$(fundSrcRemarkHidden).appendTo($(currentRow));
			
			var instHidden = $('<input>').attr('type', 'hidden');
			$(instHidden).attr('name', 'requestPositionToList[' + rowId +'].requestFundingList[' + i + '].inst');
			$(instHidden).val(inst);
			$(instHidden).appendTo($(currentRow));
			
			var sectionHidden = $('<input>').attr('type', 'hidden');
			$(sectionHidden).attr('name', 'requestPositionToList[' + rowId +'].requestFundingList[' + i + '].section');
			$(sectionHidden).val(section);
			$(sectionHidden).appendTo($(currentRow));
			
			var analyticalHidden = $('<input>').attr('type', 'hidden');
			$(analyticalHidden).attr('name', 'requestPositionToList[' + rowId +'].requestFundingList[' + i + '].analytical');
			$(analyticalHidden).val(analytical);
			$(analyticalHidden).appendTo($(currentRow));

			var postDuration = $($("#tblFunding input[name='currentPostDuration']")[i]).val();
			var postFTE = $($("#tblFunding input[name='currentPostFTE']")[i]).val()
			
			var postDurationHidden = $('<input>').attr('type', 'hidden');
			$(postDurationHidden).attr('name', 'hidPostDuration');
			$(postDurationHidden).val(postDuration);
			$(postDurationHidden).appendTo($(currentRow));
			
			var postFTEHidden = $('<input>').attr('type', 'hidden');
			$(postFTEHidden).attr('name', 'hidPostFTE');
			$(postFTEHidden).val(postFTE);
			$(postFTEHidden).appendTo($(currentRow));
		}	
		
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='hcmPositionId']").val($('#editPostModel #tmpHCMPositionId').val());
		
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='requestProgramType']").val($('#editPostModel #programType').val());
		$("#tblPositionTo tbody tr[id='"+postID+"'] input[name='requestSubSpecialty']").val($('#editPostModel #subSpecialty').val());
		
		// Update the table
		var rank_desc ="";
		if ( $("#editPostModel #ddl_rank").val() != "") {
			rank_desc = $("#editPostModel #ddl_rank option:selected").text();
		}
			
		var durationDesc = "";
		durationDesc = $("#editPostModel #ddl_post_duration option:selected").text();
		 
		$("#tblPositionTo tr:eq(1) td:eq(1)").text(rank_desc);
		$("#tblPositionTo tr:eq(1) td:eq(2)").text(durationDesc);
		$("#tblPositionTo tr:eq(1) td:eq(3)").text($("#editPostModel #postFTEValue").val());
		
		$("#editPostModel").modal("hide");
	}

	function selectHcmPositionDetail(positionId, cluster, inst, dept, staffGroup, effectiveStartDate, name, fte, headcount, status, positionType) {
		$("#tblHCMResult tbody").remove();
		
		var field = "<tbody><tr><td name='effectiveStartDate'>"+ effectiveStartDate +"</td>" +
		"<td name='positionName'>"+ name +"</td>" +
		"<td name='hcmFte'>"+ fte +"</td>" +
		"<td name='hcmHeadCount'>"+ headcount +"</td>" +
		"<td name='hcmHiringStatus'>"+ status +"</td>" +
		"<td name='hcmPositionType'>"+ positionType +"</td>" +
		"<td name='action'><button type='button' id='btnChangeHCMPosition' " +
		"class='btn btn-primary' " +
		"onclick='changeHCMPosition()'>Change</button></td></tr></tbody>";
		
		 // Append row to search result
		$("#tblHCMResult thead").after(field);
		$("#tblHCMResult").show();
		
		// tblHCMResult.draw();
		
		showLoading();
		$.ajax({
            url: "<%=request.getContextPath() %>/hcm/getHCMPostDetail",
            type: "POST",
            data: {postId: positionId},
            success: function(data) {
				// Remove the selected row
				//$("#tblPositionDelete tbody").remove();
				$('#editPostModel #tmpHCMPositionId').val(positionId);
				$("#editPostModel #ddl_cluster").val(cluster);
				$("#editPostModel #ddl_institution").val(inst);
				$("#editPostModel #ddl_department").val(data.deptCode);
				$("#editPostModel #ddl_staff_group").val(data.staffGroupCode);
				// $("#ddl_rank").val(data.rankCode);
				
				$("#divHCMDetail").show();
			
				// Default the position title
				$("#postTitle").val(data.hcmRecord.positionTitle);
				
				// Perform revalidate
				$("#frmDialog").bootstrapValidator('revalidateField', 'department');
				$("#frmDialog").bootstrapValidator('revalidateField', 'staffGroup');
				$("#frmDialog").bootstrapValidator('revalidateField', 'postTitle');
				
				if (data.deptCode != "" && data.deptCode != null) {
					haveDefaultDepartment = data.deptCode;
				}
				else {
					haveDefaultDepartment = "";
				}
				
				if (data.staffGroupCode != "" && data.staffGroupCode != null) {
					haveDefaultStaffGroup = data.staffGroupCode;
				}
				else {
					haveDefaultStaffGroup = "";
				}
				
				if (data.rankCode != "" && data.rankCode != null) {
					haveDefaultRank = data.rankCode;
				}
				else {
					haveDefaultRank = "";
				}
				
				// Close the search Dialog
				$("#searchHCMResultModel").modal("hide");
				$("#searchPanel").hide();
				
				hideLoading();
			}
		});
	}
	
	function changeRankDropdownList(rankCode) {
		showLoading();
		
		$.ajax ({
            url: "<%=request.getContextPath() %>/common/getRank",
            type: "POST",
            data: {rankCode: rankCode, staffGroupCode: $("#tmpStaffGroup").val() },
            success: function(data) {
            	 $("#editPostModel #ddl_rank").empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $("#editPostModel #ddl_rank").append(option);
            	 
            	 for (var i=0; i<data.rankList.length; i++) {
	            	 option = "<option value='" + data.rankList[i].rankCode + "'>" + data.rankList[i].rankName + "</option>";
	            	 $("#editPostModel #ddl_rank").append(option);
            	 }
            	 
            	 hideLoading();
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	}
	
	function changeDepartment() {
		if (haveDefaultDepartment != $("#ddl_department").val() && haveDefaultDepartment != "") {
			$("#warningTitle").html("Warning");
	        $("#warningContent").html("Please note that the selected \"Department\" is not in line with the chosen HCM Position.");
	        $("#warningModal").modal("show");
		}
		
		refreshRankDropdown();
		refreshSubSpecialty();
	}
	
	function changeStaffGroup() {
		if (haveDefaultStaffGroup != $("#ddl_staff_group").val() && haveDefaultStaffGroup != "") {
			$("#warningTitle").html("Warning");
	        $("#warningContent").html("Please note that the selected \"Staff Group\" is not in line with the chosen HCM Position.");
	        $("#warningModal").modal("show");
		}
		
		if ($("#ddl_staff_group").val() == "AH" || $("#ddl_staff_group").val() == "PHARM" ) {
			$("#lblDept").text("Specialty");
		}
		else {
			$("#lblDept").text("Department");
		}
		refreshDepartmentDropdown();
	}
	
	function changerRank() {
		if (haveDefaultRank != $("#editPostModel #ddl_rank").val() && haveDefaultRank != "") {
			$("#warningTitle").html("Warning");
	        $("#warningContent").html("Please note that the selected \"Rank\" is not in line with the chosen HCM Position.");
	        $("#warningModal").modal("show");
		}
	}
	
	function refreshDepartmentDropdown() {
		var tmpDept = $("#ddl_department").val();
	
		$.ajax ({
            url: "<%=request.getContextPath()%>/common/getDeptByStaffGroup",
            type: "POST",
            data: {staffGroupCode: $("#ddl_staff_group").val()},
            success: function(data) {
            	 $("#ddl_department").empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $("#ddl_department").append(option);
            	 
            	 for (var i=0; i<data.deptList.length; i++) {
	            	 option = "<option value='" + data.deptList[i].deptCode + "'>" + data.deptList[i].deptName + "</option>";
	            	 $("#ddl_department").append(option);
            	 }
            	 
            	 $("#ddl_department").val(tmpDept);
            	 
            	 refreshRankDropdown();
            	 refreshSubSpecialty();
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });	
	}
	
	function refreshSubSpecialty() {
		var tmpSubSpec = $("#subSpecialty").val();
	
		$.ajax ({
            url: "<%=request.getContextPath()%>/common/getSubSpecialtyByStaffGroup",
            type: "POST",
            data: {staffGroupCode: $("#ddl_staff_group").val(), deptCode: $("#ddl_department").val()},
            success: function(data) {
            	 $("#subSpecialty").empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $("#subSpecialty").append(option);
            	 for (var i=0; i<data.subSpecailtyList.length; i++) {
	            	 option = "<option value='" + data.subSpecailtyList[i].subSpecialtyCode + "'>" + data.subSpecailtyList[i].subSpecialtyDesc + "</option>";
	            	 $("#subSpecialty").append(option);
            	 }
            	 
            	 $("#subSpecialty").val(tmpSubSpec);
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });	
	}
	
	function refreshRankDropdown() {
		var tmpRank = $("#editPostModel #ddl_rank").val();
		
		if ($("#tblPositionFrom tbody tr td").length > 1) {
			tmpFromRank = $("#tblPositionFrom tbody tr:eq(0) td:eq(1)").html().trim();
		}
		
		$.ajax ({
            url: "<%=request.getContextPath()%>/common/getRankByStaffGroupAndRank",
            type: "POST",
            data: {staffGroupCode: $("#ddl_staff_group").val(), deptCode: $("#ddl_department").val(), fromRank: tmpFromRank},
            success: function(data) {
            	 $("#editPostModel #ddl_rank").empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $("#editPostModel #ddl_rank").append(option);
            	 
				for (var i=0; i<data.rankList.length; i++) {
	            	 option = "<option value='" + data.rankList[i].rankCode + "'>" + data.rankList[i].rankName + "</option>";
	            	 $("#editPostModel #ddl_rank").append(option);
            	 }
            	 
            	 $("#editPostModel #ddl_rank").val(tmpRank);
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });	
	}
	
	function performHcmSearch() {
		// Check is any criteria inputted
		if ($("#hcmJob").val() == "" &&   
			$("#hcmPostTitle").val() == "" && 
			$("#hcmOrganization").val() == "" && 
			$("#hcmPostOrganization").val() == "" && 
			$("#hcmUnitTeam").val() == "" && 
			$("#hcmPositionName").val() == "") {
		
			$("#warningTitle").html("Warning");
            $("#warningContent").html("Please select at least one criteria.");
            $("#warningModal").modal("show");
            
            return;
		}
	
		//hiddenMessage();
		showLoading();
		var staffGroupDesc =  $("#tmpStaffGroup option:selected").text();

		// Ajax call to perform search
		$.ajax({
			url: "<%= request.getContextPath() %>/api/hcm/searchHcmPositionForPostCreation",
			cache: false,
			type: "POST",
			dataType: 'json',
			data: {
				hcmJob: $("#hcmJob").val(),
				hcmPostTitle: $("#hcmPostTitle").val(),
				hcmOrganization: $("#hcmOrganization").val(),
				hcmPostOrganization: $("#hcmPostOrganization").val(),
				hcmUnitTeam: $("#hcmUnitTeam").val(),
				hcmPositionName:  $("#hcmPositionName").val(),
				staffGroup: staffGroupDesc
			},
			success: function(data) {
				// Clear the result table
				$("#tblHCMSearchResult tbody").remove();
            	var html = "";
            	var len = data.hcmPositionResponse.length;
            	if (len > 0) {
            		html = "<tbody>";
            		for (var i = 0; i < len; i++) {
						html += "<tr>";
						html += "<td>" + data.hcmPositionResponse[i].effectiveStartDate + "</td>";
						html += "<td>" + data.hcmPositionResponse[i].name + "</td>";
						html += "<td>" + data.hcmPositionResponse[i].fte + "</td>";
						html += "<td>" + data.hcmPositionResponse[i].maxPerson + "</td>";
						html += "<td>" + data.hcmPositionResponse[i].availabilityStatus + "</td>";
						html += "<td>" + data.hcmPositionResponse[i].positionType + "</td>";
						html += "<td>" + "<button type='button' class='btn btn-primary' ";
						html += "onclick=\"javascript:selectHcmPositionDetail('" + data.hcmPositionResponse[i].positionId; 
						html += "', '" + data.hcmPositionResponse[i].clusterCode + "', '" + data.hcmPositionResponse[i].instCode + "', '', '', '";
						html += data.hcmPositionResponse[i].effectiveStartDate + "', '" + data.hcmPositionResponse[i].name + "', '";
						html += data.hcmPositionResponse[i].fte + "', '" + data.hcmPositionResponse[i].maxPerson + "', '" ;
						html += data.hcmPositionResponse[i].availabilityStatus +"', '" + data.hcmPositionResponse[i].positionType + "')\">Select</button></td>";
						html += "</tr>";
            		}
            		html += "</tbody>";
            		
            		// Append row to search result
					$("#tblHCMSearchResult thead").after(html);
					
            	}
				var oTable = $('#tblHCMSearchResult').dataTable();
				oTable.fnDestroy();
				$('#tblHCMSearchResult').dataTable({
			       	"columnDefs": [ {
					"targets": 'no-sort',
					"orderable": false,
					}], 
					"language": {
						"emptyTable":"No data found, for create new HCM post, please press <a href='/mprs/hcm/createHCMPost'>here</a>.</td>"
					}
				});
				
				hideLoading();

				// Open the search result modal
				$('#searchHCMResultModel').modal('show');
			},
			error: function(request, status, error) {
				//Ajax failure
				alert("Some problem occur during call the ajax: "
						+ request.responseText);
			}
		});
	}
	
	function changeHCMPosition() {
		$("#searchPanel").show();
	}
	
	function postDurationChange2() {
		// If Recurrent - disable the duration and fixed end date
		if ($("#editPostModel #ddl_post_duration").val() == "R") {
			$("#editPostModel #rd_Duartion").prop("checked", false);
			$("#editPostModel #rd_Duartion2").prop("checked", false);
			
			$("#editPostModel #rd_Duartion").attr("disabled", true);
			$("#editPostModel #rd_Duartion2").attr("disabled", true);
			$("#editPostModel #ddl_limit_duration_unit").attr("disabled", true);
			$("#editPostModel #txt_limit_duration_no").attr("disabled", true);
			$("#editPostModel #txt_post_actual_end_date").attr("disabled", true);
			
			clickDuration2();
			
			$("#editPostModel #rd_Duartion").hide();
			$("#editPostModel #rd_Duartion2").hide();
			$("#editPostModel #ddl_limit_duration_unit").hide();
			$("#editPostModel #txt_limit_duration_no").hide();
			$("#editPostModel #grpLimitDurationEndDate").hide();
			$("#editPostModel #lblDurationType").hide();
			$("#editPostModel #lblDurationType2").hide();
			
			// $("#frmDialog").data('bootstrapValidator').enableFieldValidators('limitDurationUnit', false);
			// $("#frmDialog").data('bootstrapValidator').enableFieldValidators('limitDurationNo', false);
			// $("#frmDialog").data('bootstrapValidator').enableFieldValidators('limitDurationEndDate', false);
			endDateMandatory = false;
			$("#endDateStar").hide();
			
		}
		else {
			$("#editPostModel #rd_Duartion").attr("disabled", false);
			$("#editPostModel #rd_Duartion2").attr("disabled", false);
			$("#editPostModel #ddl_limit_duration_unit").attr("disabled", false);
			$("#editPostModel #txt_limit_duration_no").attr("disabled", false);
			$("#editPostModel #txt_post_actual_end_date").attr("disabled", false);
			
			$("#editPostModel #rd_Duartion").show();
			$("#editPostModel #rd_Duartion2").show();
			$("#editPostModel #ddl_limit_duration_unit").show();
			$("#editPostModel #txt_limit_duration_no").show();
			$("#editPostModel #grpLimitDurationEndDate").show();
			$("#editPostModel #lblDurationType").show();
			$("#editPostModel #lblDurationType2").show();
			
			// $("#frmDialog").data('bootstrapValidator').enableFieldValidators('limitDurationUnit', true);
			// $("#frmDialog").data('bootstrapValidator').enableFieldValidators('limitDurationNo', true);
			// $("#frmDialog").data('bootstrapValidator').enableFieldValidators('limitDurationEndDate', true);
			endDateMandatory = true;
			clickDuration2();
			
			$("#endDateStar").show();
		}
		
		$("#editPostModel #txt_proposed_post_id").val('');
	}
	
	function changeFTE() {
		if ($("#editPostModel #postFTE").val() == "FULL_TIME") {
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('postFTEValue', false);
			$("#editPostModel #postFTEValue").val("1");
			$("#editPostModel #postFTEValue").attr("disabled", true);
			
		}
		else {
			$("#editPostModel #postFTEValue").val("");
			$("#editPostModel #postFTEValue").attr("disabled", false);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('postFTEValue', true);
		}
		
		$("#editPostModel #txt_proposed_post_id").val('');
	}
	
	function changeAnnualPlan() {
		if ($("#editPostModel #ddl_annual_plan_ind").val() != "Y") {
			$("#editPostModel #starProgramCode").hide();
			$("#editPostModel #starYear").hide();
			$("#editPostModel #starName").hide();
			
			
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_name', false);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_year', false);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_code', false);
		}
		else {
			$("#editPostModel #starProgramCode").show();
			$("#editPostModel #starYear").show();
			$("#editPostModel #starName").show();
			
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_name', true);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_year', true);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_code', true);
		}
		
		$("#editPostModel #txt_proposed_post_id").val('');
	}
	
	function changeAnnualPlan2(newProgramType) {
		if ($("#editPostModel #ddl_annual_plan_ind").val() != "Y") {
			$("#editPostModel #starProgramCode").hide();
			$("#editPostModel #starYear").hide();
			$("#editPostModel #starName").hide();
			
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_name', false);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_year', false);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_code', false);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('programType', true);
		}
		else {
			$("#editPostModel #starProgramCode").show();
			$("#editPostModel #starYear").show();
			$("#editPostModel #starName").show();
			
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_name', true);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_year', true);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_code', true);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('programType', true);
		}
		
		// CC176525 - Refresh Program Type
		$.ajax ({
            url: "<%=request.getContextPath() %>/common/getProgramType",
            type: "POST",
            data: {annualPlanInd: $("#editPostModel #ddl_annual_plan_ind").val(), postDuration: $("#editPostModel #ddl_post_duration").val(), postFTEType: $("#editPostModel #postFTE").val()},
            success: function(data) {
            	 $("#editPostModel #programType").empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $("#editPostModel #programType").append(option);
            	 
            	 for (var i=0; i<data.programTypeList.length; i++) {
	            	 option = "<option value='" + data.programTypeList[i].programTypeCode + "'>" + data.programTypeList[i].programTypeName + "</option>";
	            	 $("#editPostModel #programType").append(option);
            	 }
            	 
            	 if (typeof newProgramType != "undefined") {
            	 	$("#editPostModel #programType").val(newProgramType);
            	 }
            	 else {
            	 	$("#editPostModel #programType").val("");
            	 }
            	 
            	 if ($("#editPostModel #programType").val() == "" && data.programTypeList.length==1) {
            	 	$("#editPostModel #programType").val($($("#editPostModel #programType option")[1]).val());
            	 }
            	 
            	 $("#editPostModel #txt_proposed_post_id").val('');
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	}
	
	function showPositionDetail(requestPositionUid) {
		$.ajax({
			url : "<%= request.getContextPath() %>/request/getRequestPosition",
			type : "POST",
			data : {
				requestPositionUid: requestPositionUid 
			},
			success : function(data) {
				initNewPostModal();
				
				$("#searchPanel").hide();
	
				//retrieve row details
				// var cluster = $("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestCluster']").val();
				$("#editPostModel #ddl_cluster").val(data.clusterCode);
				$('#editPostModel #ddl_institution').val(data.instCode);
				$('#editPostModel #ddl_department').val(data.deptCode);
				$('#editPostModel #ddl_staff_group').val(data.staffGroupCode);
				$('#editPostModel #ddl_rank').val(data.rankCode);
				$('#editPostModel #txt_unit').val(data.unit);
				$('#editPostModel #postTitle').val(data.postTitle);
				$('#editPostModel #ddl_post_duration').val(data.postDuration);
				$('#editPostModel #txt_postStartDate').val(data.postStartDateStr);
				// $('#editPostModel #rd_Duartion').val(data.limitDurationType);
				
				if (data.limitDurationType == "FIXED_END_DATE") {
					$('#editPostModel #rd_Duartion').prop('checked', 'checked');										
				}
				else {
					$('#editPostModel #rd_Duartion2').prop('checked', 'checked');
				}
				
				$('#editPostModel #txt_limit_duration_no').val(data.limitDurationNo);
				$('#editPostModel #ddl_limit_duration_unit').val(data.limitDurationUnit);
				//$('#editPostModel #txt_post_actual_start_date').val(data.);
				// $('#editPostModel #rd_Duartion2').val(data.);
				// $('#editPostModel #txt_post_actual_end_date').val(data.);
				$('#editPostModel #ddl_post_remark').val(data.postRemark);
				$('#editPostModel #postFTE').val(data.postFTEType);
				$('#editPostModel #postFTEValue').val(data.postFTE);
				$('#editPostModel #ddl_position_status').val(data.postStatus);
				$('#editPostModel #dp_position_start_date').val(data.postStatusStartDateStr);
				$('#editPostModel #dp_position_end_date').val(data.postStatusEndDateStr);
				$('#editPostModel #txt_cluster_ref_no').val(data.clusterRef);
				$('#editPostModel #txt_clusterRemark').val(data.clusterRemark);
				
				$('#editPostModel #ddl_res_sup_fr_ext').val(data.resourcesSupportFrExt);
				$('#editPostModel #txt_res_sup_remark').val(data.resourcesSupportRemark);
				
				// $('#editPostModel #txt_proposed_post_id').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestProposedPostId']").val());
				// $('#editPostModel #txt_post_id_just').val($("#tblPositionTo tbody tr[id='"+postID+"'] input[name='RequestPostIdJust']").val());
		
				//Funding
				$('#editPostModel #ddl_annual_plan_ind').val(data.annualPlanInd);
				$('#editPostModel #ddl_program_year').val(data.programYear);
				$('#editPostModel #txt_program_code').val(data.programCode);
				$('#editPostModel #txt_program_name').val(data.programName);
				$('#editPostModel #ddl_1st_funding_source').val(data.fundSrc1st);
				$('#editPostModel #ddl_2nd_funding_source').val(data.fundSrc2nd);
				$('#editPostModel #ddl_3rd_funding_source').val(data.fundSrc3rd);
				$('#editPostModel #txt_fund_src_1st_start_date').val(data.funSrc1stStartDateStr);
				$('#editPostModel #txt_fund_src_2nd_start_date').val(data.funSrc2ndStartDateStr);
				$('#editPostModel #txt_fund_src_3rd_start_date').val(data.funSrc3rdStartDateStr);
				$('#editPostModel #txt_fund_src_1st_end_date').val(data.fundSrc1stEndDateStr);
				$('#editPostModel #txt_fund_src_2nd_end_date').val(data.fundSrc2ndEndDateStr);
				$('#editPostModel #txt_fund_src_3rd_end_date').val(data.fundSrc3rdEndDateStr);
				$('#editPostModel #txt_program_remark').val(data.fundSrcRemark);
				$('#editPostModel #fund_remark').val(data.fundRemark);
				$('#editPostModel #txt_fund_cost_code').val(data.costCode);
				
				$('#editPostModel #fund_src_1st_fte').val(data.fundSrc1stFte);
				$('#editPostModel #fund_src_2nd_fte').val(data.fundSrc2ndFte);
				$('#editPostModel #fund_src_3rd_fte').val(data.fundSrc3rdFte);
				$('#editPostModel #fund_src_2nd_remark').val(data.fundSrc2ndRemark);
				$('#editPostModel #fund_src_3rd_remark').val(data.fundSrc3rdRemark);
				$('#editPostModel #programType').val(data.programTypeCode);
				
				$('#editPostModel #tmpHCMPositionId').val(data.hcmPositionId);
				
				$("#editPostModel #relatedHcmEffectiveStartDate").text(data.relatedHcmEffectiveStartDate);
				$("#editPostModel #relatedHcmFTE").text(data.relatedHcmFTE);
				$("#editPostModel #relatedHcmHeadCount").text(data.relatedHcmHeadCount);
				$("#editPostModel #relatedHcmPositionName").text(data.relatedHcmPositionName);
				$("#editPostModel #relatedHcmHiringStatus").text(data.relatedHcmHiringStatus);
				$("#editPostModel #relatedHcmType").text(data.relatedHcmType);

				postDurationChange2();
				changeAnnualPlan2(data.programTypeCode);
				clickDuration2();			
				changeRankDropdown2();			
				
				$("#btnSaveNewPosition").hide(); 					
				$("#editPostModel").modal("show");
			},
			error : function(request, status, error) {
				//Ajax failure
				alert("Some problem occur during call the ajax: "
						+ request.responseText);
			}
		});
	}
	
	function checkFTEvalue(){
		var text = $("#postFTEValue").val();
		var n = text.indexOf(".");
		
		if(text.length > n+3){
			$("#postFTEValue").val(text.substring(0, n+3));
		}
		
		if(text.indexOf(".", text.indexOf(".") + 1) != -1){
			$("#postFTEValue").val(text.substring(0, text.indexOf(".", text.indexOf(".") + 1)))
		}
	}
	
	function enableEffectiveDate() {
		if (($("#tblPositionFrom tbody tr").size() > 0 && $("#tblPositionFrom tbody tr td").html() != "No record found.") || 
			($("#tblPositionTo tbody tr").size() > 0 && $("#tblPositionTo tbody tr td").html() != "No record found.")) {
			$("#effectiveDate").attr("disabled", true);
		}
		else {
			$("#effectiveDate").attr("disabled", false);
		}
	}
	
	// Added for UT29800
	function addExistingPosition() {
		if ($("#tblPositionFrom tbody tr td").html() == "No record found." || $("#tblPositionFrom tbody tr").size() == 0) {
			var errMsg = "Please select at least one from post.<br/>";
			$("#divErrorMsg").html(errMsg);
			$("#divError").show();
			
			$('html,body').animate({ scrollTop: 0 }, 'slow');
			
			return;
		}
	
		fromPost = "N";
		fromRankName = $("#tblPositionFrom tbody tr:eq(0) td:eq(1)").html().trim();
	
		$("#divSuccess").hide();
		$("#divError").hide();
	
		if ($("#effectiveDate").val() == "") {
			displayError("Please select effective date.");
			return;
		}
	
		// Hide the result section
		$("#tblSearchResult").hide();
		var oTable = $('#tblSearchResult').dataTable();
		oTable.fnDestroy();
		
		// Default the staff group default
		var staffGroupCode =  $("#tmpStaffGroup").val();
		var staffGroupDesc =  $("#tmpStaffGroup option:selected").text();
		
		// Refresh staff group drop down
		$("#searchStaffGroupId").empty();
		var option = "<option value='" + staffGroupCode + "'>" + staffGroupDesc + "</option>";
	    $("#searchStaffGroupId").append(option);
		$("#searchStaffGroupId").val(staffGroupCode);
		
		// Refresh rank drop down
		$.ajax ({
            url: "<%=request.getContextPath() %>/common/getRank",
            type: "POST",
            data: {rankCode: fromRankName, staffGroupCode: $("#tmpStaffGroup").val() },
            success: function(data) {
            	 $("#searchRankId").empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $("#searchRankId").append(option);
            	 
            	 for (var i=0; i<data.rankList.length; i++) {
	            	 option = "<option value='" + data.rankList[i].rankCode + "'>" + data.rankList[i].rankName + "</option>";
	            	 $("#searchRankId").append(option);
            	 }
            	 
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	
		$("#searchResultModel").modal("show");
	}
	
	function refreshFundingSourceLabel() {
		for (var m=0; m<$("label[name='lblFundingSource']").length; m++) { 
			$($("label[name='lblFundingSource']")[m]).text("Funding Source " + (m+1));
		}
	}
	
	function addNewFunding() {
		// Clone the last row
		var x = $("#tblFunding tr:last").clone();
		
		var postFTE = $(x).find("input[name='currentPostFTE']").val();
		var postDuration = $(x).find("input[name='currentPostDuration']").val();
		
		// Initial the value
		$(x).find("select").val("");
		$(x).find("input").val("");
		$(x).find("textarea").val("");
		
		$(x).find("input[name='currentPostFTE']").val(postFTE);
		$(x).find("input[name='currentPostDuration']").val(postDuration);
		
		// Remove the option for program type
		$(x).find("select[name$='programTypeCode']").empty();
        var option = "<option value=''> - Select - </option>";
        $(x).find("select[name$='programTypeCode']").append(option);
	
		// UT29997 Set default value for post funding start date  
		$(x).find("input[name$='fundSrcStartDate']").val($("#editPostModel input[name='postStartDate']").val());
			
		$("#tblFunding tr:last").after(x);
	
		// Refresh the name of each component
		for (var i=0; i<$("#tblFunding tr").length; i++) {
			// Annual Plan
			$($("#tblFunding select[name$='annualPlanInd']")[i]).attr("name", "requestFundingList[" + i + "].annualPlanInd");
			$($("#tblFunding select[name$='annualPlanInd']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].annualPlanInd");		
			// $("#frmDetail").bootstrapValidator('addField', $($("#tblFunding select[name$='annualPlanInd']")[i]));
			
			// Program Year
			$($("#tblFunding select[name$='programYear']")[i]).attr("name", "requestFundingList[" + i + "].programYear");
			$($("#tblFunding select[name$='programYear']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].programYear");		
			// $("#frmDetail").bootstrapValidator('addField', $($("#tblFunding select[name$='programYear']")[i]));
			
			// Program Code
			$($("#tblFunding input[name$='programCode']")[i]).attr("name", "requestFundingList[" + i + "].programCode");
			
			// Program Name
			$($("#tblFunding input[name$='programName']")[i]).attr("name", "requestFundingList[" + i + "].programName");
			$($("#tblFunding input[name$='programName']")[i]).attr("id", "requestFundingList" + i + "programName");
			
			// Program Type
			$($("#tblFunding select[name$='programTypeCode']")[i]).attr("name", "requestFundingList[" + i + "].programTypeCode");
			
			// Funding Source
			$($("#tblFunding select[name$='fundSrcId']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcId");
			$($("#tblFunding select[name$='fundSrcId']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].fundSrcId");		
			// $("#frmDetail").bootstrapValidator('addField', $($("#tblFunding select[name$='fundSrcId']")[i]));
			
			$($("#tblFunding select[name$='fundSrcSubCatId']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcSubCatId");
			$($("#tblFunding select[name$='fundSrcSubCatId']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].fundSrcSubCatId");
			
			// Start Date
			$($("#tblFunding input[name$='fundSrcStartDate']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcStartDate");
			$($("#tblFunding input[name$='fundSrcStartDate']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].fundSrcStartDate");		
			// $("#frmDetail").bootstrapValidator('addField', $($("#tblFunding input[name$='fundSrcStartDate']")[i]));
			
			// End Date
			$($("#tblFunding input[name$='fundSrcEndDate']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcEndDate");

			// FTE			
			$($("#tblFunding input[name$='fundSrcFte']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcFte");
			$($("#tblFunding input[name$='fundSrcFte']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].fundSrcFte");
			// $("#frmDetail").bootstrapValidator('addField', $($("#tblFunding input[name$='fundSrcFte']")[i]));
	
			// Remark
			$($("#tblFunding textarea[name$='fundSrcRemark']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcRemark");
			
			// Inst
			$($("#tblFunding input[name$='inst']")[i]).attr("name", "requestFundingList[" + i + "].inst");
			
			// Section
			$($("#tblFunding input[name$='section']")[i]).attr("name", "requestFundingList[" + i + "].section");
			
			// Analytical
			$($("#tblFunding input[name$='analytical']")[i]).attr("name", "requestFundingList[" + i + "].analytical");
			
		}
		
		// Activate the datapicker
		$("#frmDialog .input-group.date").datepicker({
	  		format: "dd/mm/yyyy",
	  		autoclose: true,
	  		daysOfWeekHighlighted: [0],
	  		todayHighlight: true,
	  		todayBtn: "linked"
	  	})
	  	.on('changeDate', function(e) {
  			if ($(this).next().hasClass('help-block')) {
  				$("#frmDialog").bootstrapValidator('revalidateField', $($(this).parent().find("input")[0]).attr("name"));
  			}
	  	});
		
		// Refresh the label for funding source
		refreshFundingSourceLabel();
		
	}
	
	function removeFunding(obj) {
		if ($("#tblFunding tr").length == 1) {
			$("#warningTitle").html("Error");
          	$("#warningContent").html("Each request should contain at least one funding source.");
           	$("#warningModal").modal("show");
		
			return;
		}
	
		// Current Row
		var currentRow = $(obj).parent().parent().parent().parent().parent().parent();
	
		// Remove validator
		// $("#frmDetail").bootstrapValidator('removeField', $(currentRow.find("select[name$='annualPlanInd']")[0]));
		// $("#frmDetail").bootstrapValidator('removeField', $(currentRow.find("select[name$='programYear']")[0]));
		// $("#frmDetail").bootstrapValidator('removeField', $(currentRow.find("input[name$='fundSrcFte']")[0]));
		// $("#frmDetail").bootstrapValidator('removeField', $(currentRow.find("select[name$='fundSrcId']")[0]));
		// $("#frmDetail").bootstrapValidator('removeField', $(currentRow.find("input[name$='fundSrcStartDate']")[0]));
	
		currentRow.remove();
		
		for (var i=0; i<$("#tblFunding tr").length; i++) {
			$($("#tblFunding select[name$='annualPlanInd']")[i]).attr("name", "requestFundingList[" + i + "].annualPlanInd");
			$($("#tblFunding select[name$='programYear']")[i]).attr("name", "requestFundingList[" + i + "].programYear");
			$($("#tblFunding input[name$='programCode']")[i]).attr("name", "requestFundingList[" + i + "].programCode");
			$($("#tblFunding input[name$='programName']")[i]).attr("name", "requestFundingList[" + i + "].programName");
			$($("#tblFunding input[name$='programName']")[i]).attr("id", "requestFundingList" + i + "programName");
			$($("#tblFunding select[name$='programTypeCode']")[i]).attr("name", "requestFundingList[" + i + "].programTypeCode");
			$($("#tblFunding select[name$='fundSrcId']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcId");
			$($("#tblFunding select[name$='fundSrcSubCatId']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcSubCatId");
			$($("#tblFunding input[name$='fundSrcStartDate']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcStartDate");
			$($("#tblFunding input[name$='fundSrcEndDate']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcEndDate");
			$($("#tblFunding input[name$='fundSrcFte']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcFte");
			$($("#tblFunding textarea[name$='fundSrcRemark']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcRemark");
			$($("#tblFunding input[name$='inst']")[i]).attr("name", "requestFundingList[" + i + "].inst");
			$($("#tblFunding input[name$='section']")[i]).attr("name", "requestFundingList[" + i + "].section");
			$($("#tblFunding input[name$='analytical']")[i]).attr("name", "requestFundingList[" + i + "].analytical");
		}
		
		refreshFundingSourceLabel();
	}
	
	function changeAnnualPlanByRow(obj) {
		var annualPlanInd = $(obj).val();
		var programTypeDropdown = $(obj).parent().parent().parent().parent().find("select[name$='programTypeCode']");
		var postDuration = $("#frmDialog #ddl_post_duration").val();
		var postFTEType = $("#frmDialog #postFTE").val();

		// Load up the program type
		var tmpProgramType = $(programTypeDropdown).val();
		
		$.ajax ({
            url: "<%=request.getContextPath()%>/common/getProgramType",
            type: "POST",
            data: {annualPlanInd: annualPlanInd, postDuration: postDuration, postFTEType: postFTEType},
            success: function(data) {
            	 $(programTypeDropdown).empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $(programTypeDropdown).append(option);
            	 
            	 for (var i=0; i<data.programTypeList.length; i++) {
	            	 option = "<option value='" + data.programTypeList[i].programTypeCode + "'>" + data.programTypeList[i].programTypeName + "</option>";
	            	 $(programTypeDropdown).append(option);
            	 }
            	 
            	 $(programTypeDropdown).val(tmpProgramType);
            	 if (($(programTypeDropdown).val() == "" || $(programTypeDropdown).val() == null) && data.programTypeList.length==1) {
            	 	$(programTypeDropdown).val($($(programTypeDropdown).find("option")[1]).val());
            	 }
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });		
        
        // Add the validation
        if (annualPlanInd == "Y") {
        	$($(obj).parent().parent().parent().parent().find("font[name='starProgramCode']")[0]).show();
        	$($(obj).parent().parent().parent().parent().find("font[name='starName']")[0]).show();
        	$($(obj).parent().parent().parent().parent().find("font[name='starYear']")[0]).show();
        }
        else {
        	$($(obj).parent().parent().parent().parent().find("font[name='starProgramCode']")[0]).hide();
        	$($(obj).parent().parent().parent().parent().find("font[name='starName']")[0]).hide();
        	$($(obj).parent().parent().parent().parent().find("font[name='starYear']")[0]).hide();
        }
	}
</script>


<!-- Page Content -->
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > Request > Upgrade 
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
			<div class="title pull-left"><i class="fa fa-file-text-o"></i>Upgrade</div>
		</div>
		<form id="frmDetail" method="POST" enctype="multipart/form-data">
			<form:hidden path="formBean.formAction" id="hiddenFormAction" />
			<form:hidden path="formBean.requestNo" id="hiddenRequestNo" />
			<form:hidden path="formBean.canEditDetailInfo"/>
			<form:hidden path="formBean.canEditFinancialInfo"/>
			<form:hidden path="formBean.lastUpdateDate" id="hiddenLastUpdateDate" />
			<form:select path="formBean.tmpStaffGroup" id="tmpStaffGroup"
					     name="tmpStaffGroup"
						 class="form-control" style="display:none">
				<form:option value="" label="- Select -" />
				<form:options items="${staffGroupList}" />
			</form:select>
			
			<div class="panel panel-custom-primary">
				<div class="panel-heading">
					<div class="panel-heading-title">General Information</div>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-2">
							<label for="request_id" class="field_request_label">Request ID</label>
						</div>
						<div class="col-sm-4">
							<input type="text" id="requestId" name="requestId"
								value=" ${formBean.requestId}" class="form-control" readonly />
						</div>
						<div class="col-sm-2">
							<label for="request_status" class="field_request_label">Request Status</label>
						</div>
						<div class="col-sm-4">
							<form:select path="formBean.requestStatus" name="requestStatus"
								class="form-control">
								<option value=""></option>
								<form:options items="${requestStatusList}" />
							</form:select>
						</div>
					</div>
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="request_id" class="control-label">Requester<font class="star">*</font></label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.requester" 
											 name="requester"
											 class="form-control" required="required">
									<option value="">- Select -</option>
									<form:options items="${userList}" />
								</form:select>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-2">
								<label for="request_status" class="control-label">Effective Date of Change<font class="star">*</font></label>
							</div>
							<div class="col-sm-4">
								<div class="form-group">
									<div class="input-group date" id="eff_date_change">
										<form:input path="formBean.effectiveDate" class="form-control" required="required"/>
										<span class="input-group-addon"> <span
											class="glyphicon glyphicon-calendar"></span>
										</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="panel panel-custom-primary">
				<div class="panel-heading">
					<div class="panel-heading-title">Upgraded From Post</div>
				</div>
				<div class="panel-body" style="padding-top:5px">
					<input id="hiddenSearchAction" type="hidden"/>
					<table id="tblPositionFrom" class="table-bordered mprs_table">
						<thead>
							<tr>
								<th style="width:400px">Post ID</th>
								<th style="width:150px">Rank</th>
								<th style="width:150px">Duration</th>
								<th style="width:150px">FTE</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="listValue" items="${formBean.requestPositionFromList}">
								<tr>
									<td><input type="hidden" name="requestPostNo" value="${listValue.postNo}">${listValue.postId}</td>
									<td>${listValue.rank.rankName}</td>
									<td>${listValue.postDuration == "R"?"Recurrent":listValue.postDuration == "TLC"?"Time Limited - Contract":listValue.postDuration == "TLT"?"Time Limited - Temporary":""}</td>
									<td>${listValue.postFTE}</td>
									<td style='text-align: center'>
										<button type='button' name='btnViewPost' class='btn btn-primary' onclick='showPostDetails("${listValue.postNo}")'>View</button>
										<button type='button' name='btnRemovePost' class='btn btn-primary'
										onclick='removePost($(this));$("#btnAddFromPost").prop("disabled", false);enableEffectiveDate()'><i class="fa fa-times"></i> Remove</button></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div style="text-align: right">
						<button type="button" id="btnAddFromPost" name="btnAddPost" class="btn btn-primary" style="width:110px" onclick="startSearching()"><i class="fa fa-plus"></i> Add Post</button>
					</div>
				</div>
			</div>
			<br>
			<div class="panel panel-custom-primary">
				<div class="panel-heading">
					<div class="panel-heading-title">Upgraded To Post</div>
				</div>
				<div class="panel-body" style="padding-top:5px">
					<table id="tblPositionTo" class="table-bordered mprs_table">
						<thead style="background-color:#7d39b4">
							<tr>
								<th style="width:400px">Post ID</th>
								<th style="width:150px">Rank</th>
								<th style="width:150px">Duration</th>
								<th style="width:150px">FTE</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="listValue" items="${formBean.requestPositionToList}" varStatus="pStatus1">
								<tr id='${listValue.requestPostId}'>
									<td><input type="hidden" name="requestPostNoTo"
										value="${listValue.requestPostId}">
										${listValue.proposedPostId}
										${listValue.isExistingPost == "Y"?" [Existing]":""}
									</td>
									<td>${listValue.rank.rankName} </td>
									<td>${listValue.postDuration == "R"?"Recurrent":listValue.postDuration == "TLC"?"Time Limited - Contract":listValue.postDuration == "TLT"?"Time Limited - Temporary":""} </td>
									<td>${listValue.postFTE} </td>
									<td style="text-align: center">
										<button type="button" name="btnEdit" class="btn btn-primary" onclick="editNewPosition('${listValue.requestPostId}', this, ${pStatus1.index})" style="margin-right:5px;">${listValue.isExistingPost == "Y"?"View":"Edit Funding"}</button>
										<button type="button" name="btnRemovePost" class="btn btn-primary" onclick="removePost($(this));$('#btnAddToPost').prop('disabled', false);$('#btnAddExistingPost').prop('disabled', false);enableEffectiveDate()"><i class="fa fa-times"></i> Remove</button>
										<input name="RequestCluster" value="${listValue.clusterCode}" type="hidden"/>
										<input name="RequestInst" value="${listValue.instCode}" type="hidden"/>
										<input name="RequestDept" value="${listValue.deptCode}" type="hidden"/>
										<input name="RequestStaffGroup" value="${listValue.staffGroupCode}" type="hidden"/>
										<input name="RequestRank" value="${listValue.rank.rankCode}" type="hidden"/>
										<input name="RequestUnit" value="${listValue.unit}" type="hidden"/>
										<input name="RequestPostTitle" value="${listValue.postTitle}" type="hidden"/>
										<input name="RequestPostDuration" value="${listValue.postDuration}" type="hidden"/>
										<input name="RequestPostStartDate" value="${listValue.postStartDateStr}" type="hidden"/>
										<input name="RequestRdDuration" value="${listValue.limitDurationType}" type="hidden"/>
										<input name="RequestDurationValue" value="${listValue.limitDurationNo}" type="hidden"/>
										<input name="RequestDurationUnit" value="${listValue.limitDurationUnit}" type="hidden"/>
										<input name="RequestPostActualStartDate" value="" type="hidden"/>
										<input name="RequestRdDuration2" value="" type="hidden"/>
										<input name="RequestPostActualEndDate" value="${listValue.limitDurationEndDateStr}" type="hidden"/>
										<input name="RequestPostRemark" value="${listValue.postRemark}" type="hidden"/>
										<input name="RequestPostFTE" value="${listValue.postFTEType}" type="hidden"/>
										<input name="RequestPostFTEValue" value="${listValue.postFTE}" type="hidden"/>
										<input name="RequestPositionStatus" value="${listValue.postStatus}" type="hidden"/>
										<input name="RequestPositionStartDate" value="${listValue.postStatusStartDateStr}" type="hidden"/>
										<input name="RequestPositionEndDate" value="${listValue.postStatusEndDateStr}" type="hidden"/>
										<input name="RequestClusterRefNo" value="${listValue.clusterRef}" type="hidden"/>
										<input name="RequestClusterRemark" value="${listValue.clusterRemark}" type="hidden"/>
										<input name="RequestResSupFrExt" value="${listValue.requestFundingResource.resourcesSupportFrExt}" type="hidden">
										<input name="RequestResSupRemark" value="${listValue.requestFundingResource.resourcesSupportRemark}" type="hidden">
										<input name="RequestProposedPostId" value="${listValue.proposedPostId}" type="hidden">
										<input name="RequestPostIdJust" value="" type="hidden">
										<input name="RequestHoBuyServiceInd" value="${listValue.hoBuyServiceInd}" type="hidden">
		
										<input name="hcmPositionId" value="${listValue.hcmPositionId}" type="hidden">
										<input name="requestPostId" value="${listValue.requestPostId}" type="hidden">
										<input name="requestSubSpecialty" value="${listValue.subSpecialtyCode}" type="hidden">
										<input name="requestPostExist" value="${listValue.isExistingPost}" type="hidden">
										
										<div name='hiddenTable'>
											<c:forEach var="funding"
												   items="${listValue.requestFundingList}" varStatus="pStatus2">
												<form:hidden path="formBean.requestPositionToList[${pStatus1.index}].requestFundingList[${pStatus2.index}].annualPlanInd" />
												<form:hidden path="formBean.requestPositionToList[${pStatus1.index}].requestFundingList[${pStatus2.index}].programYear" />
												<form:hidden path="formBean.requestPositionToList[${pStatus1.index}].requestFundingList[${pStatus2.index}].programCode" />
												<form:hidden path="formBean.requestPositionToList[${pStatus1.index}].requestFundingList[${pStatus2.index}].programName" />
												<form:hidden path="formBean.requestPositionToList[${pStatus1.index}].requestFundingList[${pStatus2.index}].programTypeCode" />
												<form:hidden path="formBean.requestPositionToList[${pStatus1.index}].requestFundingList[${pStatus2.index}].fundSrcId" />
												<form:hidden path="formBean.requestPositionToList[${pStatus1.index}].requestFundingList[${pStatus2.index}].fundSrcSubCatId" />
												<form:hidden path="formBean.requestPositionToList[${pStatus1.index}].requestFundingList[${pStatus2.index}].startDateStr" />
												<form:hidden path="formBean.requestPositionToList[${pStatus1.index}].requestFundingList[${pStatus2.index}].endDateStr" />
												<form:hidden path="formBean.requestPositionToList[${pStatus1.index}].requestFundingList[${pStatus2.index}].fundSrcFte" />
												<form:hidden path="formBean.requestPositionToList[${pStatus1.index}].requestFundingList[${pStatus2.index}].fundSrcRemark" />
												<form:hidden path="formBean.requestPositionToList[${pStatus1.index}].requestFundingList[${pStatus2.index}].inst" />
												<form:hidden path="formBean.requestPositionToList[${pStatus1.index}].requestFundingList[${pStatus2.index}].section" />
												<form:hidden path="formBean.requestPositionToList[${pStatus1.index}].requestFundingList[${pStatus2.index}].analytical" />
											</c:forEach>
										</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div style="text-align: right">
						<button type="button" id="btnAddToPost" name="btnAddPost" class="btn btn-primary" style="width:160px" onclick="addNewPosition()"><i class="fa fa-plus"></i> Add New Post</button>
						<button type="button" id="btnAddExistingPost" name="btnAddExistingPost" class="btn btn-primary" style="width:160px" onclick="addExistingPosition()"><i class="fa fa-plus"></i> Add Existing Post</button>
					</div>
					
					<br>
					<div class="row">
						<div class="col-sm-2">
							<label for="txt_reason" class="field_request_label"><strong>Reason of Change</strong></label>
						</div>
						<div class="col-sm-9">
							<form:input path="formBean.requestReason" class="form-control" maxlength="200"/>
						</div>
					</div>
				</div>
			</div>
			
			<br>

			<%@ include file="/WEB-INF/views/request/common_approval.jsp"%>
		</form>

		<%@ include file="/WEB-INF/views/request/common_MPRPost_search.jsp"%>
		<%@ include file="/WEB-INF/views/request/common_MPRPost_detail.jsp"%>

	</div>
	<!-- ./#container -->
</div>
<!-- ./#page-content-wrapper -->


<form id="frmDialog">
	<!-- New Post Detail Model -->
	<div id="editPostModel" class="modal fade" role="dialog">
		<div class="modal-dialog modal-dialog-custom" style="width:980px">
			<div class="modal-content">
				<div class="modal-header">
					<h4><b>Post Detail - Upgraded To Post</b>
			    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
			    	</h4>
				</div>
				<div class="modal-body">
					<input type="hidden" id="hiddenPostId"/>
					<!-- Tab start-->
					<div id="tab_details" class="">
						<ul class="nav nav-pills">
							<li class="active"><a href="#tab0_basic_1" data-toggle="tab">Basic Information</a></li>
							<li><a href="#tab1_position_1" data-toggle="tab">Post Details</a></li>
							<li><a href="#tab2_fund_1" data-toggle="tab">Funding Related Information</a></li>
							<li><a href="#tab3_resources_1" data-toggle="tab">Resources Support from External</a></li>
						</ul>
					
						<div class="tab-content clearfix"
							style="margin-bottom: 5px; padding-bottom: 0px">
							<div class="tab-pane active" id="tab0_basic_1">
								<div id="searchPanel">
									<!-- Search Existing HCM Position Record -->
									<div>
										<div class="panel panel-custom-primary">
											<div class="panel-heading">
												<div class="panel-heading-title">
													<a role="button" data-target="#searchCriteria" class="panel-title" data-toggle="collapse">Search Existing HCM Position Record</a>
												</div>
											</div>
											<div id="searchCriteria" class="panel-collapse collapse in">
												<div class="panel-body">
													<fieldset class="scheduler-border" style="margin: 0px">
														<div class="row">
															<div class="col-sm-4">
																<label for="position_title" class="field_request_label">Position Title/Generic Rank</label>
															</div>
															<div class="col-sm-8">
																<form:input path="formBean.ddHcmPostTitle"
																	class="form-control" style="width:100%;" />
																<form:hidden path="formBean.hcmPostTitle" />
															</div>
														</div>

														<div class="row">
															<div class="col-sm-4">
																<label for="position_title" class="field_request_label">Position Organization</label>
															</div>
															<div class="col-sm-8">
																<form:input path="formBean.ddHcmPostOrganization"
																	class="form-control" style="width:100%;" />
																<form:hidden path="formBean.hcmPostOrganization" />
															</div>
														</div>

														<div class="row">
															<div class="col-sm-4">
																<label for="position_title" class="field_request_label">Unit/Team</label>
															</div>
															<div class="col-sm-8">
																<form:input path="formBean.hcmUnitTeam"
																	class="form-control" style="width:100%;" />
															</div>
														</div>

														<div class="row">
															<div class="col-sm-4">
																<label for="position_title" class="field_request_label">Job</label>
															</div>
															<div class="col-sm-8">
																<form:input path="formBean.ddHcmJob"
																	class="form-control" style="width:100%;" />
																<form:hidden path="formBean.hcmJob" />
															</div>
														</div>

														<div class="row">
															<div class="col-sm-4">
																<label for="position_title" class="field_request_label">Organization</label>
															</div>
															<div class="col-sm-8">
																<form:input path="formBean.ddHcmOrganization"
																	class="form-control" style="width:100%;" />
																<form:hidden path="formBean.hcmOrganization" />
															</div>
														</div>
														
														<div class="row">
															<div class="col-sm-4">
																<label for="positiona_name" class="field_request_label">Position Name</label>
															</div>
															<div class="col-sm-8">
																<form:input path="formBean.hcmPositionName" class="form-control" style="width:100%;" />
															</div>
														</div>
														
														<div class="row">
															<div class="col-sm-12" style="text-align: right">
																<button type="button" class="btn btn-primary" style="width:110px;" onclick="performHcmSearch()"><i class="fa fa-search"></i> Search</button>
															</div>
														</div>
													</fieldset>
												</div>
											</div>
										</div>
									</div>
								</div>

								<table id="tblHCMResult" class="table table-bordered mprs_table"
									style="border: solid 1px #DDD">
									<thead>
										<tr>
											<th style="width: 150px">Effective Date</th>
											<th style="width: 300px">Position name</th>
											<th style="width: 80px">FTE</th>
											<th style="width: 80px">Headcount</th>
											<th style="width: 100px">Hiring Status</th>
											<th style="width: 100px">Type</th>
											<th class="no-sort" style="width: 150px"></th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td style="width: 150px"><form:label
													path="formBean.relatedHcmEffectiveStartDate"
													style="width:150px" id="relatedHcmEffectiveStartDate">${formBean.relatedHcmEffectiveStartDate}</form:label>
											</td>
											<td style="width: 300px"><form:label
													path="formBean.relatedHcmPositionName"
													style="width:300px" id="relatedHcmPositionName">${formBean.relatedHcmPositionName}</form:label>
											</td>
											<td style="width: 80px"><form:label
													path="formBean.relatedHcmFTE" style="width:80px"
													id="relatedHcmFTE">${formBean.relatedHcmFTE}</form:label>
											</td>
											<td style="width: 80px"><form:label
													path="formBean.relatedHcmHeadCount" style="width:80px"
													id="relatedHcmHeadCount">${formBean.relatedHcmHeadCount}</form:label>
											</td>
											<td style="width: 100px"><form:label
													path="formBean.relatedHcmHiringStatus"
													style="width:100px" id="relatedHcmHiringStatus">${formBean.relatedHcmHiringStatus}</form:label>
											</td>
											<td style="width: 100px"><form:label
													path="formBean.relatedHcmType" style="width:100px"
													id="relatedHcmType">${formBean.relatedHcmType}</form:label>
											</td>
											<td><input type="button" id="btnChangeHCMPosition"
												value="Change" class="btn btn-primary"
												onclick="changeHCMPosition()" /></td>
										</tr>
									</tbody>
								</table>

								<div class="row">
									<div class="col-sm-2">
										<label for="ddl_cluster" class="field_request_label">Cluster</label>
									</div>
									<div class="col-sm-3">
										<form:select path="formBean.cluster" id="ddl_cluster"
											name="ddl_cluster" class="form-control" style="width:100%;"
											disabled="True">
											<form:option value="" label="- Select -" />
											<form:options items="${clusterList}" />
										</form:select>

									</div>
								</div>
								<div class="row">
									<div class="col-sm-2">
										<label for="ddl_institution" class="field_request_label">Institution</label>
									</div>
									<div class="col-sm-3">
										<form:select path="formBean.institution"
											id="ddl_institution" name="ddl_institution"
											class="form-control" style="width:100%;" disabled="True">
											<form:option value="" label="- Select -" />
											<form:options items="${instList}" />
										</form:select>

									</div>
								</div>
								<div class="row">
									<div class="form-group">
									<div class="col-sm-2">
										<label for="ddl_department" class="field_request_label" id="lblDept">Department</label>
									</div>
									<div class="col-sm-3">
										<form:select path="formBean.department" id="ddl_department"
											name="ddl_department" class="form-control" required="required"
											style="width:100%;" onchange="changeDepartment()">
											<form:option value="" label="- Select -" />
											<form:options items="${deptList}" />
										</form:select>
									</div>
									</div>
								</div>
								<div class="row">
									<div class="form-group">
									<div class="col-sm-2">
										<label for="ddl_staff_group" class="field_request_label">Staff
											Group</label>
									</div>
									<div class="col-sm-3">
										<form:select path="formBean.staffGroup" id="ddl_staff_group"
											name="ddl_staff_group" class="form-control" required="required"
											style="width:100%;" onchange="changeStaffGroup()">
											<form:option value="" label="- Select -" />
											<form:options items="${staffGroupList}" />
										</form:select>
									</div>
									</div>
								</div>
								<div class="row">
									<div class="form-group">
									<div class="col-sm-2">
										<label for="ddl_rank" class="field_request_label">Rank</label>
									</div>
									<div class="col-sm-3">
										<form:select path="formBean.rank" id="ddl_rank" required="required"
											name="ddl_rank" class="form-control" style="width:100%;" onchange="changeRankDropdown2()">
											<form:option value="" label="- Select -" />
											<form:options items="${rankList}" />
										</form:select>
									</div>
									</div>
								</div>
							</div>
							<div class="tab-pane" id="tab1_position_1">
								<input type="hidden" id="tmpHCMPositionId"/>
								<input type="hidden" id="txt_proposed_post_id"/>
								
								<div class="row">
									<div class="col-sm-2">
										<label for="txt_unit" class="control-label">Unit</label>
									</div>
									<div class="col-sm-4">
										<form:input path="formBean.unit" type="text"
											class="form-control " id="txt_unit" name="txt_unit" maxlength="100"></form:input>
									</div>
								</div>
								<div class="row">
									<div class="form-group">
										<div class="col-sm-2">
											<label for="postTitle" class="control-label">Post Title<font class="star">*</font></label>
										</div>
										<div class="col-sm-4">
											<form:input path="formBean.postTitle" class="form-control" style="width:100%;" maxlength="100" required="required"/>
										</div>
									</div>
									<div class="col-sm-2">
										<label id="lblSubSpecialty" for="subSpecialty" class="control-label">Sub-specialty</label>
									</div>
									<div class="col-sm-4">
										<form:select path="formBean.subSpecialty" 
	                                                       class="form-control"
													 style="width:100%;">
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
												class="form-control" style="width:100%;" onchange="postDurationChange2();">
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
													id="rd_Duartion" name="rd_Duartion" value="DURATION_PERIOD" onclick="clickDuration2()"  required="required"></form:radiobutton>
											<span class="form-space"></span>
											<form:input path="formBean.limitDurationNo" type="text"
												class="form-control" id="txt_limit_duration_no"
												name="txt_limit_duration_no" style="width:50px;"  onchange="selectDuration()"  required="true" oninput="this.value=this.value.replace(/[^0-9]/g,'');" maxlength="4"></form:input>
											<span class="form-space"></span>
											<form:select path="formBean.limitDurationUnit" onchange="selectDuration()"
												id="ddl_limit_duration_unit" name="ddl_limit_duration_unit"
												class="form-control" style="width:60%;"  required="true">
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
										<div class="form-inline form-group">
											<form:radiobutton path="formBean.limitDurationType"
											id="rd_Duartion2" name="rd_Duartion2" value="FIXED_END_DATE" onclick="clickDuration2()" required="required"></form:radiobutton>
											<span class="form-space"></span>
											<div class="input-group date" id="grpLimitDurationEndDate">
												<form:input path="formBean.limitDurationEndDate" type="text"
													class="form-control " id="txt_post_actual_end_date" onchange="selectFixEndDate()"
													name="txt_post_actual_end_date" required="true"></form:input>
												<span class="input-group-addon"> <span
													class="glyphicon glyphicon-calendar"></span>
												</span>
											</div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-2">
										<label for="ddl_post_remark" class="control-label">Remarks</label>
									</div>
									<div class="col-sm-10">
										<form:input path="formBean.postRemark" type="text"
											class="form-control " id="ddl_post_remark"
											name="ddl_post_remark" style="width:80%" maxlength="2000"></form:input>
									</div>
								</div>
								<div class="row">
									<div class="form-inline form-group">
										<div class="col-sm-2">
											<label for="ddl_postFTE" class="control-label">FTE<font class="star">*</font></label>
										</div>
										<div class="col-sm-6">
											<form:select path="formBean.postFTE" class="form-control"
												onchange="changeFTE(); $('#editPostModel #txt_proposed_post_id').val('');" required="required">
												<form:option value="" label="- Select -" />
												<form:option value="FULL_TIME" label="Full Time" />
												<form:option value="PART_TIME" label="Part Time" />
											</form:select>
											<span class="form-space"></span>
											<form:input path="formBean.postFTEValue" type="text" required="required" 
												class="form-control"  style="width:50px;" step="0.01" oninput="this.value=this.value.replace(/[^0-9\.]/g,'');checkFTEvalue();"></form:input>
											<span class="form-space"></span>
											<label for="postFTEValue"><i>(No. of net working hours per weeks / 39)</i></label>
										</div>
										
										<div class="col-sm-2">
										</div>
										
										<!--  HO Buy Service -->
										<div class="col-sm-2">
											<form:checkbox id="cbHoBuyServiceInd" path="formBean.hoBuyServiceInd" value="Y"/> <label>HO Buy Service</label>
										</div>
										
									</div>
								</div>
								<div class="row">
									<div class="col-sm-2">
										<label for="ddl_position_status" class="control-label">Post Status</label>
									</div>
									<div class="col-sm-2">
										<form:select path="formBean.positionStatus" id="ddl_position_status" name="ddl_position_status"
	 												     class="form-control" style="width:80%;">
										<!-- <form:option value="" label="- Select -" /> -->
										<form:options items="${postStatusList}" />
										</form:select>
									</div>
									<div style="display:none">
									<div class="col-sm-2">Start Date</div>
									<div class="col-sm-2">
										<div class='input-group date' id='position_start_date'>
											<form:input path="formBean.positionStartDate" type="text"
												class="form-control " id="dp_position_start_date"
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
												class="form-control " id="dp_position_end_date"
												name="dp_position_end_date"></form:input>
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
										<label for="txt_cluster_ref_no"
											class="control-label_opt"><strong>Cluster Reference No.</strong></label>
									</div>
									<div class="col-sm-5">
										<form:input path="formBean.clusterRefNo" type="text"
											class="form-control " id="txt_cluster_ref_no"
											name="txt_cluster_ref_no" style="width:100%" maxlength="100"></form:input>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-2">
										<label for="txt_clusterRemark"
											class="control-label_opt">Additional Remarks from Cluster </label>
									</div>
									<div class="col-sm-7">
										<form:textarea path="formBean.clusterRemark" type="text" 
										               class="form-control" 
										               id="txt_clusterRemark"
										               name="txt_clusterRemark" 
										               style="width:100%;height:60px" maxlength="2000"></form:textarea>
									</div>
								</div>
								
							</div>
							<div class="tab-pane" id="tab2_fund_1">
								
								
								<div>
								
								<table id="tblFunding" style="width: 100%">
						<tr>
							<td style="padding-bottom:20px">
								<div class="row">
										<div class="col-sm-12">
														<div class="row">
															<div class="col-sm-4">
																<div style="border-bottom: 1px solid black; width: 100%">
																	<label name="lblFundingSource" style="font-weight: bold">Funding Source</label>
																</div>
															</div>
															<div class="col-sm-8"
																style="padding-bottom: 5px; text-align: right">
																<button type="button" class="btn btn-primary"
																	style="width: 130px" onclick="removeFunding(this)" name='btnRemoveFunding'>
																	<i class="fa fa-times"></i> Remove
																</button>
															</div>
														</div>
													</div>
												</div>
												<div class="row">
													<!-- Annual Plan - Start -->
													<div class="form-group">
														<div class="col-sm-2">
															<label for="ddl_annual_plan_ind" class="control-label">Annual Plan<font class="star">*</font></label>
														</div>

																<div class="col-sm-2">
																	<input type="hidden" name="currentPostDuration"/>
																	<input type="hidden" name="currentPostFTE"/>
																
																	<form:select
																		path="formBean.annualPlanInd"
																		class="form-control"
																		style="width:100%;"
																		onchange="changeAnnualPlanByRow(this);"
																		required="required">
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
															<label class="control-label">Year<font name="starYear" class="star">*</font></label>
														</div>
														<div class="col-sm-2">
															<form:select
																path="formBean.programYear"
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
															<label class="control-label">Program Code / Ref No.<font name="starProgramCode" class="star">*</font></label>
														</div>
														<div class="col-sm-2">
															<form:input
																path="formBean.programCode"
																type="text" class="form-control" name="txt_program_code"
																style="width:100%" maxlength="50"
																data-bv-excluded="true"></form:input>
														</div>
													</div>
													<!-- Program Code - End -->
												</div>

												<div class="row">
													<!-- Program Name - Start -->
													<div class="form-group">
														<div class="col-sm-2">
															<label for="txt_program_name" class="control-label">Name<font
																name="starName" class="star">*</font></label>
														</div>
														<div class="col-sm-6">
															<form:input
																path="formBean.programName"
																type="text" class="form-control" style="width:100%"
																maxlength="500"
																data-bv-excluded="true"></form:input>
														</div>
													</div>
													<!-- Program Name - End -->

													<!-- Program Type - Start -->
													<div class="form-group">
														<div class="col-sm-2">
															<label id="lblProgramType" for="txt_program_name"
																class="control-label">Program Type<font
																class="star" id="starProgramType">*</font></label>
														</div>
														<div class="col-sm-2">
															<form:select
																path="formBean.programTypeCode"
																class="form-control" style="width:100%;"
																required="required">
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
															<label for="" class="control-label"><strong>Funding Source</strong><font
																class="star">*</font></label>
														</div>
														<div class="col-sm-2">
															<form:select
																path="formBean.fundSrcId"
																class="form-control" style="width:100%;"
																required="required">
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
															<form:select
																path="formBean.fundSrcSubCatId"
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
															<label for="" class="control-label">Start Date<font
																class="star">*</font></label>
														</div>
														<div class="col-sm-2">
															<div class="input-group date"
																id="dp_fund_src_1st_start_date">
																<form:input
																	path="formBean.fundSrcStartDate"
																	type="text" class="form-control " style="width:100%"></form:input>
																<span class="input-group-addon"> <span
																	class="glyphicon glyphicon-calendar"></span>
																</span>
															</div>
														</div>
													</div>
													<!--  Start Date - End -->


													<!--  End Date - Start -->
													<div class="form-group">
														<div class="col-sm-2">
															<label for="" class="control-label">End Date<font class="star" name="lblEndDateStar">*</font></label>
														</div>
														<div class="col-sm-2">
															<div class="input-group date"
																id="dp_fund_src_1st_end_date">
																<form:input
																	path="formBean.fundSrcEndDate"
																	type="text" class="form-control " style="width:100%"></form:input>
																<span class="input-group-addon"> <span
																	class="glyphicon glyphicon-calendar"></span></span>
															</div>
														</div>
													</div>
													<!--  End Date - End -->
													
													<!--  FTE - Start -->
													<div class="form-group">
														<div class="col-sm-2">
															<label class="control-label">FTE<font class="star">*</font></label>
														</div>
														<div class="col-sm-2">
															<form:input
																path="formBean.fundSrcFte"
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
																<form:input
																	path="formBean.inst"
																	type="text" class="form-control" style="width:100%"
																	maxlength="3"
																    data-bv-excluded="true"></form:input>
															</div>

															<div class="col-sm-1">
																<label class="control-label">Section</label>
															</div>
															<div class="col-sm-2">
																<form:input
																	path="formBean.section"
																	type="text" class="form-control" style="width:100%"
																	maxlength="7"
																    data-bv-excluded="true"></form:input>
															</div>

															<div class="col-sm-1">
																<label class="control-label">Analytical</label>
															</div>
															<div class="col-sm-2">
																<form:input
																	path="formBean.analytical"
																	type="text" class="form-control" style="width:100%"
																	maxlength="5"
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
														<form:textarea path="formBean.fundSrcRemark" type="text"
															class="form-control"
															style="width:100%;height:60px" maxlength="2000"
															data-bv-excluded="true"></form:textarea>
													</div>
													<!--  Remark - End -->
												</div>
											</td>
											
										</tr>
								</table>
								</div>
								<br />
				<div style="text-align:right">
					<button type="button" class="btn btn-primary" style="width: 180px" onclick="addNewFunding()" name='btnAddFunding'>
						<i class="fa fa-plus"></i> Add Funding Source
					</button>
				</div>
								
								
								
							</div>
							<!--tab2 end-->
							<div class="tab-pane" id="tab3_resources_1">
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
											name="txt_res_sup_remark" style="width:100%" maxlength="2000"></form:input>
									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- Tab end-->

				</div>
				<div class="modal-footer">
					<button type="button" id="btnSaveNewPosition" style="width:110px"
						class="btn btn-primary" onclick="saveNewPosition()"><i class="fa fa-check"></i> OK</button>						
					<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal">Cancel</button>
				</div>
			</div>
		</div>
	</div>
	<!-- ./#editPostModel -->	
</form>

<!--  Search Existing HCM Position Result Model -->
<div id="searchHCMResultModel" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:980px">
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Search Result - Existing HCM Position Record</b>
		    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
		    	</h4>
			</div>
			<div class="modal-body">
				<table id="tblHCMSearchResult" class="table table-striped table-bordered" style="width:950px">
					<thead>
						<tr>
							<th style="width:150px">Effective Date</th>
							<th style="width:300px">Position name</th>
							<th style="width:80px">FTE</th>
							<th style="width:80px">Headcount</th>
							<th style="width:100px">Hiring Status</th>
							<th style="width:100px">Type</th>
							<th style="width:150px" class="no-sort"></th>
						</tr>
					</thead>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>