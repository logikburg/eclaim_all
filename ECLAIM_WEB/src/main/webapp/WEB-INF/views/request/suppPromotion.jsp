<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>
<script>
	var endDateMandatory = false;
	
	function payrollIsRunning() {
		if ($("button[name='btnConfirm']").length > 0) {
			$("#warningTitle").html("Warning");
	        $("#warningContent").html("HCM Payroll is running, please wait a moment and re-try.");
	        $("#warningModal").modal("show");
	        
	        $("button[name='btnConfirm']").attr("disabled", true);
        }
	}
	
	function showDetail() {
		showPostDetails($('#hiddenMPRSPostNo').val());
		$("#tabFunding").text("Funding Related Information (Original)");
	}

	function displayError(errMsg) {
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
	
	function createNewRequest() {
		document.location.href = "<%= request.getContextPath() %>/request/suppPromotion";
	}

	var canEditFinancialInfo = "${formBean.canEditFinancialInfo}";
	var canEditDetailInfo = "${formBean.canEditDetailInfo}";

	function validateForm() {
		var errMsg = "";
	
		if ($("#tblSuppPromotion tbody tr td").html() == "No record found." || $("#tblSuppPromotion tbody tr").size() == 0) {
			errMsg += "Please select at least one post.<br/>";
		}
		
		if (canEditFinancialInfo == "Y") {
			for (var i=0; i< $("#tblSuppPromotion tbody tr").size(); i++) {
				if ($($("input[name='requestFundSrc1st']")[i]).val() == "") {
					errMsg += "Please input the detail funding information.<br/>";	
				}
			}
		}
	
		if (canEditDetailInfo == "Y") {
			for (var i=0; i< $("#tblSuppPromotion tbody tr").size(); i++) {
				if ($($("input[name='requestHcmPositionId']")[i]).val() == "") {
					errMsg += "Please select HCM Position.<br/>";	
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
			
			if (currentDuration != "Recurrent" && currentDuration != "R") {
				if (fundSrcEndDateStr == "") {
					errMsg += "Funding Source " + (i+1) + ": End date cannot be empty.<br/>";
				}
			}
		}		
		
		// Check sum of FTE should >= total FTE
		sum = sum.toFixed(4);
		
		// Get the FTE for current row 
		var totalFTE = $($($("#tblSuppPromotion tbody tr")[currentIdx]).find("td")[3]).text();
		
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
	
	function startSearching() {
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
		
		$("#searchResultModel").modal("show");
	}
	
	var currentIdx = 0;
	var currentDuration = "";
		
	function editPost(mprspostNo, postDurationDesc, hcmPositionId, postStartDate, obj, idx) {
		currentIdx = idx;
		currentDuration = postDurationDesc;
		
		$('#hiddenMPRSPostNo').val(mprspostNo);
		$('#hiddenPostStartDate').val(postStartDate);
		
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
			
			// Update program type
			changeAnnualPlanByRow($($("#tblFunding select[name$='annualPlanInd']")[i]));
		}
		
		// Refresh the label for funding source
		refreshFundingSourceLabel();
		
		if (postDurationDesc == "Recurrent" || postDurationDesc == "R") {
			$("font[name='lblEndDateStar']").hide();
		}
		else {
			$("font[name='lblEndDateStar']").show();
		}
		
		$('#ddl_annual_plan_ind').attr("disabled", true);
		$('#programType').attr("disabled", true);
		
		if (hcmPositionId != "") {
			$("#tmpHCMPositionId").val(hcmPositionId);
			$.ajax({
				url: "<%= request.getContextPath() %>/request/getRelatedHCMInfo",
				type: "POST",
				data: {
					hcmPositionId: hcmPositionId
				},
				success: function(data) {
					$("#tmpHCMPositionName").val(data.relatedHcmPositionName);
					$("#MPRSPostFundingResultModel #relatedHcmEffectiveStartDate").text(data.relatedHcmEffectiveStartDate);
					$("#MPRSPostFundingResultModel #relatedHcmFTE").text(data.relatedHcmFTE);
					$("#MPRSPostFundingResultModel #relatedHcmHeadCount").text(data.relatedHcmHeadCount);
					$("#MPRSPostFundingResultModel #relatedHcmPositionName").text(data.relatedHcmPositionName);
					$("#MPRSPostFundingResultModel #relatedHcmHiringStatus").text(data.relatedHcmHiringStatus);
					$("#MPRSPostFundingResultModel #relatedHcmType").text(data.relatedHcmType);
					$("#searchPanel").hide();
					$("#tblHCMResult").show();
					resetValidation(); 					
					$("#MPRSPostFundingResultModel").modal("show");
				}
			});
		}
		else {
			$("#MPRSPostFundingResultModel #relatedHcmEffectiveStartDate").text("");
			$("#MPRSPostFundingResultModel #relatedHcmFTE").text("");
			$("#MPRSPostFundingResultModel #relatedHcmHeadCount").text("");
			$("#MPRSPostFundingResultModel #relatedHcmPositionName").text("");
			$("#MPRSPostFundingResultModel #relatedHcmHiringStatus").text("");
			$("#MPRSPostFundingResultModel #relatedHcmType").text("");
			$("#searchPanel").show();
			$("#tblHCMResult").hide();
			resetValidation();		 					
			$("#MPRSPostFundingResultModel").modal("show");
		}
	}
	
	function refreshFundingSourceLabel() {
		for (var m=0; m<$("label[name='lblFundingSource']").length; m++) { 
			$($("label[name='lblFundingSource']")[m]).text("Funding Source " + (m+1));
		}
	}
	
	function resetValidation() {
	/*	// Reset all date field checking (to perform revalidate)
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_1st_start_date', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_1st_end_date', 'NOT_VALIDATED');
		// $("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_2nd_start_date', 'NOT_VALIDATED');
		// $("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_2nd_end_date', 'NOT_VALIDATED');
		// $("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_3rd_start_date', 'NOT_VALIDATED');
		// $("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_3rd_end_date', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_1st', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'annual_plan_ind', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'programType', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'program_year', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'program_code', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'program_name', 'NOT_VALIDATED');*/
	}
	
	function savePostFunding() {
		if (!performDialogChecking()) {
			return;
		}
	
		// Reset all date field checking (to perform revalidate)
		resetValidation();
	
		var validator = $("#frmDialog").data('bootstrapValidator');
		validator.validate();
		if (!validator.isValid()) {
			return;
		}
		
		var editMPRSPostNo = $('#hiddenMPRSPostNo').val();
		var editPostStartDate = $('#hiddenPostStartDate').val();
		if (editMPRSPostNo!= null && editMPRSPostNo!="") {
			var duration = $("#tblSuppPromotion tbody tr[id='postNO_"+editMPRSPostNo+"'] td:eq(2)").text().trim();
			var fte = $("#tblSuppPromotion tbody tr[id='postNO_"+editMPRSPostNo+"'] td:eq(3)").text().trim();
			var fundRemark = $('#fund_remark').val();
			var hcmPositionId = $("#tmpHCMPositionId").val();
			var hcmPositionName = $("#tmpHCMPositionName").val();
			
			$("#tblSuppPromotion tbody tr[id='postNO_"+editMPRSPostNo+"'] input[name='requestHcmPositionId']").val(hcmPositionId);
			$("#tblSuppPromotion tbody tr[id='postNO_"+editMPRSPostNo+"'] td:eq(5)").text(hcmPositionName);
			
			var rowId = currentIdx;
			var numberOfFundingSource = $("#tblFunding tr").length;
			
			// Remove All child
			var currentRow = $("div[name='hiddenTable']")[rowId];
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
				$(annualPlanIndHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].annualPlanInd');
				$(annualPlanIndHidden).val(annualPlanInd);
				$(annualPlanIndHidden).appendTo($(currentRow));
				
				var programYearHidden = $('<input>').attr('type', 'hidden');
				$(programYearHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].programYear');
				$(programYearHidden).val(programYear);
				$(programYearHidden).appendTo($(currentRow));
				
				var programCodeHidden = $('<input>').attr('type', 'hidden');
				$(programCodeHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].programCode');
				$(programCodeHidden).val(programCode);
				$(programCodeHidden).appendTo($(currentRow));
				
				var programNameHidden = $('<input>').attr('type', 'hidden');
				$(programNameHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].programName');
				$(programNameHidden).val(programName);
				$(programNameHidden).appendTo($(currentRow));
				
				var fundSrcRemarkHidden = $('<input>').attr('type', 'hidden');
				$(fundSrcRemarkHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].fundSrcRemark');
				$(fundSrcRemarkHidden).val(fundSrcRemark);
				$(fundSrcRemarkHidden).appendTo($(currentRow));
				
				var programTypeCodeHidden = $('<input>').attr('type', 'hidden');
				$(programTypeCodeHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].programTypeCode');
				$(programTypeCodeHidden).val(programTypeCode);
				$(programTypeCodeHidden).appendTo($(currentRow));
				
				var fundSrcIdHidden = $('<input>').attr('type', 'hidden');
				$(fundSrcIdHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].fundSrcId');
				$(fundSrcIdHidden).val(fundSrcId);
				$(fundSrcIdHidden).appendTo($(currentRow));
				
				var fundSrcSubCatIdHidden = $('<input>').attr('type', 'hidden');
				$(fundSrcSubCatIdHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].fundSrcSubCatId');
				$(fundSrcSubCatIdHidden).val(fundSrcSubCatId);
				$(fundSrcSubCatIdHidden).appendTo($(currentRow));
				
				var fundSrcStartDateStrHidden = $('<input>').attr('type', 'hidden');
				$(fundSrcStartDateStrHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].startDateStr');
				$(fundSrcStartDateStrHidden).val(fundSrcStartDateStr);
				$(fundSrcStartDateStrHidden).appendTo($(currentRow));
				
				var fundSrcEndDateStrHidden = $('<input>').attr('type', 'hidden');
				$(fundSrcEndDateStrHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].endDateStr');
				$(fundSrcEndDateStrHidden).val(fundSrcEndDateStr);
				$(fundSrcEndDateStrHidden).appendTo($(currentRow));
				
				var fundSrcFteHidden = $('<input>').attr('type', 'hidden');
				$(fundSrcFteHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].fundSrcFte');
				$(fundSrcFteHidden).val(fundSrcFte);
				$(fundSrcFteHidden).appendTo($(currentRow));
				
				var instHidden = $('<input>').attr('type', 'hidden');
				$(instHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].inst');
				$(instHidden).val(inst);
				$(instHidden).appendTo($(currentRow));
				
				var sectionHidden = $('<input>').attr('type', 'hidden');
				$(sectionHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].section');
				$(sectionHidden).val(section);
				$(sectionHidden).appendTo($(currentRow));
				
				var analyticalHidden = $('<input>').attr('type', 'hidden');
				$(analyticalHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].analytical');
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
			
			$('#btn_editPost_'+editMPRSPostNo).attr("onclick", "editPost('" + editMPRSPostNo + "', '" + duration + "', '" + hcmPositionId + "', '" + editPostStartDate + "', this)");					
		}
		
		// Enable the submit button
		$("#btnSave").attr("disabled", false);
		
	 	$("#MPRSPostFundingResultModel").modal("hide");
	};
	
	function performSearch() {
		showLoading();
	
		$("#searchEffectiveDate").val($("#effectiveDate").val());
		
		// Ajax call to perform search
		$.ajax({
            url: "<%=request.getContextPath()%>/api/request/suppPromotion/searchPost",
			cache: false,
			type: "POST",
			data: $("#searchPosition").serialize(),
			success: function(data) {

				hideLoading();
				// Clear the result table
				$("#tblSearchResult tbody").remove();
            	var html = "";
            	html = "<tbody>";
            	if (data.postResponse != null) {
            		var len = data.postResponse.length;
            		if (len > 0) {
	            		for (var i = 0; i < len; i++) {
							html += "<tr>";
							html += "<td><a href='#' onclick=\"javascript:selectPost('" + data.postResponse[i].postUid + "', '" + data.postResponse[i].postId + "', ";
							html += "'" + data.postResponse[i].rankDesc + "', "
							html += "'" + data.postResponse[i].postDurationDesc + "', "
							html += "'" + data.postResponse[i].postFte + "', '" + data.postResponse[i].hcmPositionName + "', '" + data.postResponse[i].postStartDate + "')\">" + data.postResponse[i].postId + "</a></td>"
							html += "<td>" + data.postResponse[i].rankDesc + "</td>";
							html += "<td>" + data.postResponse[i].postDurationDesc + "</td>";
							html += "<td>" + data.postResponse[i].postFte + "</td>";
							html += "<td><button type='button' class='btn btn-primary' ";
							html += "onclick=\"showPostDetails('" + data.postResponse[i].postUid + "')\">Detail</button></td>";
							html += "</tr>";
	            		}
	            	}
            	}
            	
            	html += "</tbody>";
				// Append row to search result
				$("#tblSearchResult thead").after(html);
				
				var oTable = $('#tblSearchResult').dataTable();
				oTable.fnDestroy();
				$('#tblSearchResult').dataTable();
				$("#tblSearchResult").show();
			},
			error : function(request, status, error) {
				//Ajax failure
				alert("Some problem occur during call the ajax: "
						+ request.responseText);
			}
		});
	}

	function selectPost(postUid, postId, rankDesc, postDurationDesc, fte, hcmPositionName, postStartDate) {		
		// Check is Post ID exist 
		for (var i=0; i<$("#tblSuppPromotion tbody tr").length; i++) {
			if ($($($("#tblSuppPromotion tbody tr")[i]).find("td")[0]).text().trim() == postId) {
			
				$("#warningTitle").html("Error");
            	$("#warningContent").html("Post ID already exist.");
            	$("#warningModal").modal("show");
			
				return;
			}
		}	
			
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
					showLoading();
            		// Look up from database
            		$.ajax({
			            url: "<%=request.getContextPath() %>/api/request/getPostDetails",
			            cache: false,
			            type: "POST",
			            data: {searchPostNo: postUid},
			            success: function(dataPostDetail) {
				
							// Remove the selected row
							if ($("#tblSuppPromotion tbody tr td").html() == "No record found.") {
								$("#tblSuppPromotion tbody").remove();
							}
							
							var rowNum = $("#tblExtension tbody tr").length;
							
							var row = "<tbody><tr id='postNO_" + postUid + "'>";
							row += "<td><input type='hidden' name='requestPostNo' value='" + postUid + "'/>"
									+ postId + "</td>";
							row += "<td>" + rankDesc + "</td>";
							row += "<td>" + postDurationDesc + "</td>";
							row += "<td>" + fte + "</td>";
							row += "<td>" + hcmPositionName + "</td>";
							row += "<td></td>";
							row += "<td style='text-align:center'><button id='btn_editPost_"
									+ postUid
									+ "' type='button' name='btnEdit' class='btn btn-primary' onclick=\"editPost('"
									+ postUid + "', '" + postDurationDesc + "', '', '" + postStartDate
									+ "', this, " + rowNum + ")\" style='margin-right:5px;'>Edit</button><button type='button' class='btn btn-primary' onclick='$(this).parent().parent().parent().remove();'><i class='fa fa-times'></i> Remove</button>";
							
							
							row += "<div name='hiddenTable'>";
							
							for (var i=0; i<dataPostDetail.fundingList.length; i++) {
								row += "<input type='hidden' name='hidPostDuration' value='" + dataPostDetail.postDuration + "'/>";
								row += "<input type='hidden' name='hidPostFTE' value='" + dataPostDetail.postFTE + "'/>";
								
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].annualPlanInd' value='" + dataPostDetail.fundingList[i].annualPlanInd  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].programYear' value='" + dataPostDetail.fundingList[i].programYear  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].programCode' value='" + dataPostDetail.fundingList[i].programCode  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].programName' value='" + dataPostDetail.fundingList[i].programName  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].programTypeCode' value='" + dataPostDetail.fundingList[i].programTypeCode  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].fundSrcId' value='" + dataPostDetail.fundingList[i].fundSrcId  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].fundSrcSubCatId' value='" + dataPostDetail.fundingList[i].fundSrcSubCatId  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].fundSrcStartDateStr' value='" + dataPostDetail.fundingList[i].fundSrcStartDate  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].fundSrcEndDateStr' value='" + dataPostDetail.fundingList[i].fundSrcEndDate  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].fundSrcFte' value='" + dataPostDetail.fundingList[i].fundSrcFte  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].fundSrcRemark' value='" + dataPostDetail.fundingList[i].fundSrcRemark  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].inst' value='" + dataPostDetail.fundingList[i].inst  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].section' value='" + dataPostDetail.fundingList[i].section  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].analytical' value='" + dataPostDetail.fundingList[i].analytical  + "'/>";
							
							}
							
							row += "</div>";
							row += "<input name='requestHcmPositionId' value='" + "' type='hidden'/>";
							
							row += "</td></tr></tbody>";
							if ($("#tblSuppPromotion tbody").length == 0) {
								$("#tblSuppPromotion thead").after(row);
							}
							else {
								$("#tblSuppPromotion tbody:last").after(row);
							}
							
							enableEffectiveDate();
							
							hideLoading();
					
							// Close the search Dialog
							$("#searchResultModel").modal("hide");
							
							// Enable the submit button
							$("#btnSave").attr("disabled", false);
						}
					});
				}
			}
		});
	}

	$(function() {

		$("#requestStatus").prop("disabled", true);

		var status = $("#requestStatus").val();

		// If Withdraw / Approved, disable All field
		if (status == "A" || status == "W") {
			$("select").prop("disabled", true);
			$("input[type='text']").prop("disabled", true);
			$("#btnAddPost").prop("disabled", true);
		}

		if ($("#tblSuppPromotion tbody tr").length == 0) {
			$("#tblSuppPromotion thead").after("<tbody><tr><td colspan='7'>No record found.</td></tr></tbody>");
		}
		
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
			if (!validateForm()) {
				e.preventDefault();
				hideLoading();
			}
			else {
				$("#frmDetail input[type='text']").attr("disabled", false);
				$("#frmDetail textarea").attr("disabled", false);
				$("#frmDetail select").attr("disabled", false);
				$("#frmDetail input[type='hidden']").attr("disabled", false);
			}
		});
		
		$("#frmDialog").bootstrapValidator({
			message: ' This value is not valid',
		    excluded: [':disabled'],
			live: "submitted",
			fields: {
			},
		}).on('success.form.bv', function(e){
		});
		
		// For autocomplete - Start
		$("#ddHcmPostTitle").autocomplete({
			source: function(request, response) {
				$.getJSON("<%= request.getContextPath() %>/hcm/getHCMPostTitleListByStaffGroup?staffGroup=Medical", request, function(result){
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
			<c:when test="${ formBean.canEditFinancialInfo != 'Y'}">
				$("#MPRSPostFundingResultModel input").attr("disabled", true);
				$("#MPRSPostFundingResultModel select").attr("disabled", true);
				$("#MPRSPostFundingResultModel textarea").attr("disabled", true);
				$("#MPRSPostFundingResultModel button[name='btnRemoveFunding']").attr("disabled", true);
				$("#MPRSPostFundingResultModel button[name='btnAddFunding']").attr("disabled", true);
				$("#btnSavePostFunding").attr("disabled", true);
			</c:when>
			<c:when test="${ formBean.canEditFinancialInfo == 'Y'}">
				$("#MPRSPostFundingResultModel input").attr("disabled", false);
				$("#MPRSPostFundingResultModel select").attr("disabled", false);
				$("#MPRSPostFundingResultModel textarea").attr("disabled", false);
				
				$("input[type='file']").prop("disabled", true);
				$("button[name='btnUploadFile']").attr("disabled", true);
				$("button[name='btnRemoveFile']").attr("disabled", true);
			</c:when>
		</c:choose>
		
		<c:choose>
			<c:when test="${ formBean.canEditDetailInfo != 'Y'}">
				$("#divApproval input").attr("disabled", true);
				$("#divApproval input[type='file']").attr("disabled", true);
				$("#divApproval input[name='btnUploadFile']").attr("disabled", true);
				$("#divApproval input[name='btnAddUploadFile']").attr("disabled", true);
				$("#requester").attr("disabled", true);
				$("#requestStartDate").attr("disabled", true);
				$("#requestEndDate").attr("disabled", true);
				$("#requestReason").attr("disabled", true);
				$("#effectiveDate").attr("disabled", true);
				$("#btnAddPost").attr("disabled", true);
				$("button[name='btnChildRemove']").attr("disabled", true);
				$("button[id='btnChangeHCMPosition']").attr("disabled", true);
			</c:when>
			<c:when test="${ formBean.canEditDetailInfo == 'Y'}">
				$("#tab0_basic_1 input").attr("disabled", false);
				$("#btnSavePostFunding").attr("disabled", false);
				$("button[id='btnChangeHCMPosition']").attr("disabled", false);
			</c:when>
		</c:choose>
	
		<c:choose>
			<c:when test="${ formBean.userHaveSaveRight != 'Y' }">
				$("#divApproval input").attr("disabled", true);
				$("#divApproval input[type='file']").attr("disabled", true);
				$("#divApproval input[name='btnUploadFile']").attr("disabled", true);
				$("#divApproval input[name='btnAddUploadFile']").attr("disabled", true);
				$("#requester").attr("disabled", true);
				$("#requestStartDate").attr("disabled", true);
				$("#requestEndDate").attr("disabled", true);
				$("#requestReason").attr("disabled", true);
				
				$("#btnAddPost").attr("disabled", true);
				$("button[name='btnChildRemove']").attr("disabled", true);
				$("#effectiveDate").attr("disabled", true);
				
				$("button[name='btnEdit']").val("View Funding");
				$("#btnSavePostFunding").hide();
				
				$("input[type='file']").prop("disabled", true);
				$("button[name='btnUploadFile']").attr("disabled", true);
				$("button[name='btnRemoveFile']").attr("disabled", true);
			</c:when>
		</c:choose>
	
		enableEffectiveDate();
		
		// Set Request ID to readonly instead of disable
		$("#requestId").prop("disabled", false);
		$("#requestId").attr("readonly", true); 
	});
	
	function changeAnnualPlan2() {
		if ($("#ddl_annual_plan_ind").val() != "Y") {
			$("#starProgramCode").hide();
			$("#starYear").hide();
			$("#starName").hide();
			
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_name', false);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_year', false);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_code', false);
		}
		else {
			$("#starProgramCode").show();
			$("#starYear").show();
			$("#starName").show();
			
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_name', true);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_year', true);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_code', true);
		}
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
				staffGroup: "Medical",
				allowSingleIncumbent: "Y"
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
		
		$("#tmpHCMPositionId").val(positionId);
		$("#tmpHCMPositionName").val(name);
		
		$("#searchHCMResultModel").modal("hide");
		$("#searchPanel").hide();
	}
	
	function enableEffectiveDate() {
		if ($("#tblSuppPromotion tbody tr").size() > 0 && $("#tblSuppPromotion tbody tr td").html() != "No record found.") {
			$("#effectiveDate").attr("disabled", true);
		}
		else {
			$("#effectiveDate").attr("disabled", false);
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
		$(x).find("input[name$='fundSrcStartDate']").val($("#hiddenPostStartDate").val());
			
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
		var postDuration = $(obj).parent().find("input[name='currentPostDuration']").val();
		var postFTEType = $(obj).parent().find("input[name='currentPostFTE']").val();

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
	
	function changeSearchStaffGroup() {
		if ($("#searchStaffGroupId").val() == "AH" || $("#searchStaffGroupId").val() == "PHARM" ) {
			$("#lblSearchDeptId").text("Specialty");
		}
		else {
			$("#lblSearchDeptId").text("Department");
		}
		
		refreshSearchDepartmentDropdown();
	}
	
	function changeSearchDept() {
		refreshSearchRankDropdown();
	}
	
	function refreshSearchDepartmentDropdown() {
		var tmpDept = $("#searchDeptId").val();
	
		$.ajax ({
            url: "<%=request.getContextPath()%>/common/getDeptByStaffGroup",
            type: "POST",
            data: {staffGroupCode: $("#searchStaffGroupId").val()},
            success: function(data) {
            	 $("#searchDeptId").empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $("#searchDeptId").append(option);
            	 
            	 for (var i=0; i<data.deptList.length; i++) {
	            	 option = "<option value='" + data.deptList[i].deptCode + "'>" + data.deptList[i].deptName + "</option>";
	            	 $("#searchDeptId").append(option);
            	 }
            	 
            	 $("#searchDeptId").val(tmpDept);
            	 if ($("#searchDeptId").val() == null) {
            	 	$("#searchDeptId").val("");
            	 }
            	 
            	 refreshSearchRankDropdown();
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });	
	}
	
	function refreshSearchRankDropdown() {
		var tmpRank = $("#searchRankId").val();
	
		$.ajax ({
            url: "<%=request.getContextPath()%>/common/getRankByStaffGroup",
            type: "POST",
            data: {staffGroupCode: $("#searchStaffGroupId").val(), deptCode: $("#searchDeptId").val()},
            success: function(data) {
            	 $("#searchRankId").empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $("#searchRankId").append(option);
            	 
            	 for (var i=0; i<data.rankList.length; i++) {
	            	 option = "<option value='" + data.rankList[i].rankCode + "'>" + data.rankList[i].rankName + "</option>";
	            	 $("#searchRankId").append(option);
            	 }
            	 
            	 $("#searchRankId").val(tmpRank);
            	 if ($("#searchRankId").val() == null) {
            	 	$("#searchRankId").val("");
            	 }
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });	
	}
</script>

<!-- Page Content -->
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href='<c:url value="/home/home"/>'><i class="fa fa-home"></i>Home</a> > Request > Supplementary Promotion 
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
			<div class="title pull-left"><i class="fa fa-file-text-o"></i>Supplementary Promotion</div>
		</div>
		<form id="frmDetail" method="POST" enctype="multipart/form-data">
			<form:hidden path="formBean.formAction" id="hiddenFormAction" />
			<form:hidden path="formBean.requestNo" id="hiddenRequestNo" />
			<form:hidden path="formBean.lastUpdateDate" id="hiddenLastUpdateDate" />
			<div class="panel panel-custom-primary">
				<div class="panel-heading">
					<div class="panel-heading-title">General Information</div>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-2">
							<label for="requestId" class="field_request_label">Request ID</label>
						</div>
						<div class="col-sm-4">
							<form:input path="formBean.requestId" type="text"
								class="form-control " id="requestId" name="requestId"
								style="width:100%;" disabled="true"></form:input>
						</div>
						<div class="col-sm-3">
							<label for="request_status" class="field_request_label">Request Status</label>
						</div>
						<div class="col-sm-3">
							<form:select path="formBean.requestStatus" name="requestStatus" class="form-control">
								<option value=""></option>
								<form:options items="${requestStatusList}" />
							</form:select>
						</div>
					</div>
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="requester" class="control-label">Requester<font class='star'>*</font></label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.requester" name="requester"
									class="form-control" style="width:100%;" required="required">
									<option value="">- Select -</option>
									<form:options items="${userList}" />
								</form:select>
							</div>
						</div>								
						<div class="form-group">
							<div class="col-sm-3">
							  	<label for="effectiveDate" class="control-label">Effective Date of Change<font class='star'>*</font></label>
							</div>
							<div class="col-sm-3">
									<div class="input-group date" id="eff_date_change">
						  			<form:input path="formBean.effectiveDate" class="form-control" required="required"/>
									<span class="input-group-addon">
										<span class="glyphicon glyphicon-calendar"></span>
									</span>	
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="panel panel-custom-primary">
				<div class="panel-heading">
					<div class="panel-heading-title">Post to be promoted</div>
				</div>
				<div class="panel-body" style="padding-top:5px">
					<table id="tblSuppPromotion" class="table-bordered mprs_table">
						<thead>
							<tr>
								<th>Post ID</th>
								<th>Rank</th>
								<th>Duration</th>
								<th>FTE</th>
								<th>Original Position</th>
								<th>New Position</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="listValue"
								items="${formBean.requestPositionList}" varStatus="pStatus1">
								<tr id="postNO_${listValue.postNo}">
									<td><input type="hidden" name="requestPostNo" value="${listValue.postNo}">${listValue.postId}</td>
									<td>${listValue.rank.rankName}</td>
									<td>${listValue.postDuration == "R"?"Recurrent":listValue.postDuration == "TLC"?"Time Limited - Contract":listValue.postDuration == "TLT"?"Time Limited - Temporary":""}</td>
									<td>${listValue.postFTE}</td>
									<td>${listValue.oriHcmPositionName}</td>
									<td>${listValue.hcmPositionName}</td>
									<td style='text-align: center'>
										<button type="button" id="btn_editPost_${listValue.postNo}" name="btnEdit" class="btn btn-primary" style="margin-right:5px;" onclick="editPost('${listValue.postNo}', '${listValue.postDuration}', '${listValue.hcmPositionId}', '<fmt:formatDate value="${listValue.postStartDate}" pattern="dd/MM/yyyy"/>', this, ${pStatus1.index})">Edit</button>
										<button type="button" name="btnChildRemove" class="btn btn-primary" onclick='$(this).parent().parent().parent().remove();enableEffectiveDate();'><i class="fa fa-times"></i> Remove</button>
										<div name='hiddenTable'>
											<c:forEach var="funding"
												   items="${listValue.requestFundingList}" varStatus="pStatus2">
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].annualPlanInd" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].programYear" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].programCode" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].programName" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].programTypeCode" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].fundSrcId" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].fundSrcSubCatId" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].startDateStr" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].endDateStr" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].fundSrcFte" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].fundSrcRemark" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].inst" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].section" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].analytical" />
												<input type='hidden' name='hidPostDuration' value='${ listValue.postDuration }'/>
												<input type='hidden' name='hidPostFTE' value='${ listValue.postFTEType }'/>
											</c:forEach>
										</div>
										<input name="requestHcmPositionId" value="${listValue.hcmPositionId}" type="hidden">
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div style="text-align: right">
						<button type="button" id="btnAddPost" class="btn btn-primary" style="width:110px" onclick="startSearching()"><i class="fa fa-plus"></i> Add Post</button>
					</div>
				</div>
				<div class="panel-body" style="padding-top:5px">
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="txt_reason" class="field_request_label"><strong>Reason of Change</strong></label>
							</div>
							<div class="col-sm-9">
								<form:input path="formBean.requestReason" class="form-control" style="width:100%" maxlength="200"/>
							</div>
						</div>
					</div>
				</div>
			</div>
			<br>

			<%@ include file="/WEB-INF/views/request/common_approval.jsp"%>
		</form>
		
	</div>
	<!-- ./#container -->
</div>
<!-- ./#page-content-wrapper -->

<!--  Search Result Model - Start -->
<div id="searchResultModel" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:900px">
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Search Post</b>
		    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
		    	</h4>
			</div>
			<div class="modal-body">
				<form id="searchPosition">
			    	<form:hidden path="formBean.searchEffectiveDate"/>
					<fieldset class="scheduler-border"
						style="margin-top: 0px; margin-bottom: 0px">
						<div class="row">
							<div class="col-sm-2">
								<label class="field_request_label">Cluster</label>
							</div>
							<div class="col-sm-4">
							  	<form:select path="formBean.searchClusterId" name="searchClusterId" class="form-control">
									<option value="">- Select -</option>
									<form:options items="${clusterList}" />
								</form:select>
							</div>
							<div class="col-sm-2">
								<label class="field_request_label">Institution</label>
							</div>
							<div class="col-sm-4">
							  	<form:select path="formBean.searchInstId" name="searchInstId" class="form-control">
									<option value="">- Select -</option>
									<form:options items="${instList}" />
								</form:select>
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-2">
								<label class="field_request_label" id="lblSearchDeptId">Department</label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.searchDeptId" name="searchDeptId" class="form-control" onchange="changeSearchDept()">
									<option value="">- Select -</option>
									<form:options items="${deptList}" />
								</form:select>
							</div>
							<div class="col-sm-2">
								<label class="field_request_label">Staff Group</label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.searchStaffGroupId" name="searchStaffGroupId" class="form-control" onchange="changeSearchStaffGroup()">
									<option value="MEDICAL">Medical</option>
								</form:select>
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-2">
								<label class="field_request_label">Rank</label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.searchRankId" name="searchRankId" class="form-control">
									<option value="">- Select -</option>
									<form:options items="${rankList}" />
								</form:select>
							</div>
							<div class="col-sm-2">
								<label class="field_request_label">Post ID</label>
							</div>
							<div class="col-sm-4">
								<form:input path="formBean.searchPostId" name="searchPostId" class="form-control"/>
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-12" style="text-align:right">
								<button type="button" class="btn btn-primary" style="width:110px;" onclick="performSearch()"><i class="fa fa-search"></i> Search</button>
							</div>
						</div>
					</fieldset>
				</form>

				<table id="tblSearchResult" class="table table-striped table-bordered">
					<thead>
						<tr>
							<th>Post ID</th>
							<th>Rank</th>
							<th>Duration</th>
							<th>FTE</th>
							<th>Action</th>
						</tr>
					</thead>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div><!-- ./#searchResultModel -->

<!--  Show Funding Information - Start -->
<form id="frmDialog">
<div id="MPRSPostFundingResultModel" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:980px">
		<div class="modal-content">
			<div id="MPRSPostFundingResultModel_header" class="modal-header">
				<h4><b>Change Post Funding Information</b>
		    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
		    	</h4>
			</div>
			<div id="MPRSPostFundingResultModel_body" class="modal-body">
				<input id="hiddenMPRSPostNo" name="hiddenMPRSPostNo" type="hidden">
				<input id="hiddenPostStartDate" name="hiddenPostStartDate" type="hidden">
				<div id="tab_details" class="">
					<ul class="nav nav-pills">
						<li class="active"><a href="#tab0_basic_1" data-toggle="tab">Basic Information</a></li>
						<li><a href="#tab2_fund_1" data-toggle="tab">Funding Related Information</a></li>
					</ul>
					<div class="tab-content clearfix"
						style="margin-bottom: 5px; padding-bottom: 0px">
						<div class="tab-pane active" id="tab0_basic_1">
							<input type="hidden" id="tmpHCMPositionId"/>
							<input type="hidden" id="tmpHCMPositionName"/>
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
												<!-- <form id="frmSearch" method="POST">   -->
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
															<button type="button" class="btn btn-primary" style="width:130px;" onclick="performHcmSearch()"><i class="fa fa-search"></i> Search</button>
														</div>
													</div>
												</fieldset>
												<!-- </form>  -->
											</div>
										</div>
									</div>
								</div>
							</div> <!-- ./searchPanel  -->
								
							<table id="tblHCMResult" class="table table-bordered mprs_table" style="border: solid 1px #DDD">
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
										<td><button type="button" id="btnChangeHCMPosition" class="btn btn-primary"
											onclick="changeHCMPosition()">Change</button></td>
									</tr>
								</tbody>
							</table>
						</div><!-- ./tab0_basic_1 -->
						
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
													<form:textarea
														path="formBean.fundSrcRemark"
														class="form-control" style="width:100%"
														maxlength="2000"
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
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" style="width:110px" onclick='showDetail()'>View Detail</button>
				<button type="button" id="btnSavePostFunding" style="width:110px"
					class="btn btn-primary" onclick="savePostFunding()"><i class="fa fa-check"></i> OK</button>
				<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal">Cancel</button>
			</div>
		</div>
	</div>
</div>
</form><!-- ./#frmDialog -->

<!--  Search Existing HCM Position Result Model -->
<div id="searchHCMResultModel" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width:980px">
		<div class="modal-content">
			<div class="modal-header">
				<h4><b>Search Result - Existing HCM Position Record123</b>
		    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
		    	</h4>
			</div>
			<div class="modal-body">
				<table id="tblHCMSearchResult" class="table table-striped table-bordered" style="width:950px">
					<thead>
						<tr>
							<th style="width:150px">Effective Date</th>
							<th style="width:300px">Position name</th>
							<th style="width:50px">FTE</th>
							<th style="width:50px">Headcount</th>
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
</div><!-- ./#searchHCMResultModel -->

<%@ include file="/WEB-INF/views/request/common_MPRPost_detail.jsp"%>
<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>