<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>
<script>
	var canEditFinancialInfo = "${ formBean.canEditFinancialInfo }";
	var endDateMandatory = false;
	
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
	
	function payrollIsRunning() {
		if ($("button[name='btnConfirm']").length > 0) {
			$("#warningTitle").html("Warning");
	        $("#warningContent").html("HCM Payroll is running, please wait a moment and re-try.");
	        $("#warningModal").modal("show");
	        
	        $("button[name='btnConfirm']").attr("disabled", true);
        }
	}
	
	function parseDate(str) {
		var mdy = str.split("/");
		return new Date(mdy[2], mdy[1]-1, mdy[0]);
	}
	
	function daysBetween(date1, date2) {
		return Math.round((parseDate(date1)-parseDate(date2))/ (1000*60*60*24));
	}
	
	function createNewRequest() {
		document.location.href = "<%= request.getContextPath() %>/request/fteAdjustment";
	}
	
	function validateForm() {
		var errMsg = "";
	
		if ($("#tblFteAdjustment tbody tr td").html() == "No record found." || $("#tblFteAdjustment tbody tr").size() == 0) {
			errMsg += "Please select at least one post.<br/>";
		}
		
		for (var i=0; i< $("#tblFteAdjustment tbody tr").size(); i++) {
			if (isNaN($($("input[name='requestNewFTE']")[i]).val())) {
				errMsg += "FTE should be numeric.<br/>";
			}
			else {
				var n = parseFloat($($("input[name='requestNewFTE']")[i]).val());
				if (n >= 1) {
					errMsg += "FTE cannot equal or greater than 1.<br/>";
				}
				
				if (n <= 0) {
					errMsg += "FTE cannot equal or smaller than 0.<br/>"; 
				}
				
				var old = parseFloat($($("input[name='hidOriginalPostFTE']")[i]).val());
				if (old == n) {
					errMsg += "Post FTE has no changes. Please update Post FTE.<br/>";
				}
				
			}
		}
	
		if (canEditFinancialInfo == "Y") {
			for (var i=0; i< $("#tblFteAdjustment tbody tr").size(); i++) {
				if ($($("input[name='requestFundSrc1st']")[i]).val() == "") {
					errMsg += "Please input the detail funding information.<br/>";	
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
		var minStartDate = 0;
		var maxEndDate = 0;
		
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
			
			if (fundSrcStartDateStr != "") {
				var currentStartDate = new Date(fundSrcStartDateStr.split("/")[2], fundSrcStartDateStr.split("/")[1]-1, fundSrcStartDateStr.split("/")[0]);
					
				if (minStartDate == 0) {
					minStartDate = currentStartDate.getTime();
				}
				else {
					if (currentStartDate < minStartDate) {
						minStartDate = currentStartDate.getTime();
					}
				}
			}
				
			if (fundSrcEndDateStr != "") {
				var currentEndDate = new Date(fundSrcEndDateStr.split("/")[2], fundSrcEndDateStr.split("/")[1]-1, fundSrcEndDateStr.split("/")[0]);
				
				if (maxEndDate == 0) {
					maxEndDate = currentEndDate.getTime();
				}
				else {
					if (currentEndDate > maxEndDate) {
						maxEndDate = currentEndDate.getTime();
					}
				}
			}
		}		
		
		// Check sum of FTE should >= total FTE
		sum = sum.toFixed(4);
		
		// Get the FTE for current row 
		var totalFTE = $($($($("#tblFteAdjustment tbody tr")[currentIdx]).find("td")[3]).find("input")[0]).val();
		
		if (sum < parseFloat(totalFTE).toFixed(4)) {
			errMsg += "Total FTE should be equal or greater than Post FTE.<br/>";
		}
		
		if (currentDuration != "Recurrent" && currentDuration != "R") {
			var postEndDate = new Date(currentPostEndDate.split("/")[2], currentPostEndDate.split("/")[1]-1, currentPostEndDate.split("/")[0]);
			if (postEndDate.getTime() > maxEndDate) {
				errMsg += "Funding End Date should later than the Post End Date.<br/>";
			}		
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
		// Hide the result section
		$("#tblSearchResult").hide();
		var oTable = $('#tblSearchResult').dataTable();
		oTable.fnDestroy();
		
		$("#searchResultModel").modal("show");
	}
	
	var currentIdx = 0;
	var currentDuration = "";
	var currentPostStartDate = "";
	var currentPostEndDate = "";
		
	function editPost(mprspostNo, duration, postStartDate, postEndDate, obj, idx) {
		currentIdx = idx;
		currentDuration = duration;
		currentPostStartDate = postStartDate;
		currentPostEndDate = postEndDate;
	
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
			
			$($("#tblFunding input[name$='oriAnnualPlanInd']")[i]).val($(hiddenTable.find("input[name$='oriAnnualPlanInd']")[i]).val());
			$($("#tblFunding input[name$='oriProgramYear']")[i]).val($(hiddenTable.find("input[name$='oriProgramYear']")[i]).val());
			$($("#tblFunding input[name$='oriProgramCode']")[i]).val($(hiddenTable.find("input[name$='oriProgramCode']")[i]).val());
			$($("#tblFunding input[name$='oriProgramName']")[i]).val($(hiddenTable.find("input[name$='oriProgramName']")[i]).val());
			$($("#tblFunding input[name$='oriProgramTypeCode']")[i]).val($(hiddenTable.find("input[name$='oriProgramTypeCode']")[i]).val());
			$($("#tblFunding input[name$='oriFundSrcId']")[i]).val($(hiddenTable.find("input[name$='oriFundSrcId']")[i]).val());
			$($("#tblFunding input[name$='oriFundSrcSubCatId']")[i]).val($(hiddenTable.find("input[name$='oriFundSrcSubCatId']")[i]).val());
			$($("#tblFunding input[name$='oriFundSrcStartDate']")[i]).val($(hiddenTable.find("input[name$='oriStartDateStr']")[i]).val());
			$($("#tblFunding input[name$='oriFundSrcEndDate']")[i]).val($(hiddenTable.find("input[name$='oriEndDateStr']")[i]).val());
			$($("#tblFunding input[name$='oriFundSrcFte']")[i]).val($(hiddenTable.find("input[name$='oriFundSrcFte']")[i]).val());
			$($("#tblFunding input[name$='oriFundSrcRemark']")[i]).val($(hiddenTable.find("input[name$='oriFundSrcRemark']")[i]).val());
			$($("#tblFunding input[name$='oriInst']")[i]).val($(hiddenTable.find("input[name$='oriInst']")[i]).val());
			$($("#tblFunding input[name$='oriSection']")[i]).val($(hiddenTable.find("input[name$='oriSection']")[i]).val());
			$($("#tblFunding input[name$='oriAnalytical']")[i]).val($(hiddenTable.find("input[name$='oriAnalytical']")[i]).val());
			
			// Update program type
			changeAnnualPlanByRow($($("#tblFunding select[name$='annualPlanInd']")[i]));
		}
		
		// Refresh the label for funding source
		refreshFundingSourceLabel();
		
		$("#MPRSPostFundingResultModel").modal("show");		
	}
	
	function resetValidation() {
		// Reset all date field checking (to perform revalidate)
		/*$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_1st_start_date', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_1st_end_date', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_2nd_start_date', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_2nd_end_date', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_3rd_start_date', 'NOT_VALIDATED');
		$("#frmDialog").bootstrapValidator('updateStatus', 'fund_src_3rd_end_date', 'NOT_VALIDATED');
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
	
		var rowId = currentIdx;
		var numberOfFundingSource = $("#tblFunding tr").length;
		
		for (var i=0; i< numberOfFundingSource; i++) {
			var annualPlanInd = $($("#tblFunding select[name$='annualPlanInd']")[i]).val();
			var programYear = $($("#tblFunding select[name$='programYear']")[i]).val();
			var programCode = $($("#tblFunding input[name$='programCode']")[i]).val();
			var programName = $($("#tblFunding input[name$='programName']")[i]).val();
			var programTypeCode = $($("#tblFunding select[name$='programTypeCode']")[i]).val();
			var fundSrcId = $($("#tblFunding select[name$='fundSrcId']")[i]).val();
			var fundSrcStartDateStr = $($("#tblFunding input[name$='fundSrcStartDate']")[i]).val();
			var fundSrcEndDateStr = $($("#tblFunding input[name$='fundSrcEndDate']")[i]).val();
			var fundSrcFte = $($("#tblFunding input[name$='fundSrcFte']")[i]).val();
			var fundSrcRemark = $($("#tblFunding textarea[name$='fundSrcRemark']")[i]).val();
			var inst = $($("#tblFunding input[name$='inst']")[i]).val();
			var section = $($("#tblFunding input[name$='section']")[i]).val();
			var analytical = $($("#tblFunding input[name$='analytical']")[i]).val();
		}
		
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
			
			var oriannualPlanInd = $($("#tblFunding input[name$='oriAnnualPlanInd']")[i]).val();
			var oriprogramYear = $($("#tblFunding input[name$='oriProgramYear']")[i]).val();
			var oriprogramCode = $($("#tblFunding input[name$='oriProgramCode']")[i]).val();
			var oriprogramName = $($("#tblFunding input[name$='oriProgramName']")[i]).val();
			var orifundSrcSubCatId = $($("#tblFunding select[name$='oriFundSrcSubCatId']")[i]).val()
			var oriprogramTypeCode = $($("#tblFunding input[name$='oriProgramTypeCode']")[i]).val();
			var orifundSrcId = $($("#tblFunding input[name$='oriFundSrcId']")[i]).val();
			var orifundSrcStartDateStr = $($("#tblFunding input[name$='oriFundSrcStartDate']")[i]).val();
			var orifundSrcEndDateStr = $($("#tblFunding input[name$='oriFundSrcEndDate']")[i]).val();
			var orifundSrcFte = $($("#tblFunding input[name$='oriFundSrcFte']")[i]).val();
			var orifundSrcRemark = $($("#tblFunding input[name$='oriFundSrcRemark']")[i]).val();
			var oriinst = $($("#tblFunding input[name$='oriInst']")[i]).val();
			var orisection = $($("#tblFunding input[name$='oriSection']")[i]).val();
			var orianalytical = $($("#tblFunding input[name$='oriAnalytical']")[i]).val();
			
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
			
			var oriannualPlanIndHidden = $('<input>').attr('type', 'hidden');
			$(oriannualPlanIndHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].oriAnnualPlanInd');
			$(oriannualPlanIndHidden).val(oriannualPlanInd);
			$(oriannualPlanIndHidden).appendTo($(currentRow));
			
			var oriprogramYearHidden = $('<input>').attr('type', 'hidden');
			$(oriprogramYearHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].oriProgramYear');
			$(oriprogramYearHidden).val(oriprogramYear);
			$(oriprogramYearHidden).appendTo($(currentRow));
			
			var oriprogramCodeHidden = $('<input>').attr('type', 'hidden');
			$(oriprogramCodeHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].oriProgramCode');
			$(oriprogramCodeHidden).val(oriprogramCode);
			$(oriprogramCodeHidden).appendTo($(currentRow));
			
			var oriprogramNameHidden = $('<input>').attr('type', 'hidden');
			$(oriprogramNameHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].oriProgramName');
			$(oriprogramNameHidden).val(oriprogramName);
			$(oriprogramNameHidden).appendTo($(currentRow));
			
			var oriprogramTypeCodeHidden = $('<input>').attr('type', 'hidden');
			$(oriprogramTypeCodeHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].oriProgramTypeCode');
			$(oriprogramTypeCodeHidden).val(oriprogramTypeCode);
			$(oriprogramTypeCodeHidden).appendTo($(currentRow));
			
			var orifundSrcIdHidden = $('<input>').attr('type', 'hidden');
			$(orifundSrcIdHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].oriFundSrcId');
			$(orifundSrcIdHidden).val(orifundSrcId);
			$(orifundSrcIdHidden).appendTo($(currentRow));
			
			var orifundSrcSubCatIdHidden = $('<input>').attr('type', 'hidden');
			$(orifundSrcSubCatIdHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].oriFundSrcSubCatId');
			$(orifundSrcSubCatIdHidden).val(orifundSrcSubCatId);
			$(orifundSrcSubCatIdHidden).appendTo($(currentRow));
			
			var orifundSrcStartDateStrHidden = $('<input>').attr('type', 'hidden');
			$(orifundSrcStartDateStrHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].oriStartDateStr');
			$(orifundSrcStartDateStrHidden).val(orifundSrcStartDateStr);
			$(orifundSrcStartDateStrHidden).appendTo($(currentRow));
			
			var orifundSrcEndDateStrHidden = $('<input>').attr('type', 'hidden');
			$(orifundSrcEndDateStrHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].oriEndDateStr');
			$(orifundSrcEndDateStrHidden).val(orifundSrcEndDateStr);
			$(orifundSrcEndDateStrHidden).appendTo($(currentRow));
			
			var orifundSrcFteHidden = $('<input>').attr('type', 'hidden');
			$(orifundSrcFteHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].oriFundSrcFte');
			$(orifundSrcFteHidden).val(orifundSrcFte);
			$(orifundSrcFteHidden).appendTo($(currentRow));
			
			var orifundSrcRemarkHidden = $('<input>').attr('type', 'hidden');
			$(orifundSrcRemarkHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].oriFundSrcRemark');
			$(orifundSrcRemarkHidden).val(orifundSrcRemark);
			$(orifundSrcRemarkHidden).appendTo($(currentRow));
			
			var oriinstHidden = $('<input>').attr('type', 'hidden');
			$(oriinstHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].oriInst');
			$(oriinstHidden).val(oriinst);
			$(oriinstHidden).appendTo($(currentRow));
			
			var orisectionHidden = $('<input>').attr('type', 'hidden');
			$(orisectionHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].oriSection');
			$(orisectionHidden).val(orisection);
			$(orisectionHidden).appendTo($(currentRow));
			
			var orianalyticalHidden = $('<input>').attr('type', 'hidden');
			$(orianalyticalHidden).attr('name', 'requestPositionList[' + rowId +'].requestFundingList[' + i + '].oriAnalytical');
			$(orianalyticalHidden).val(orianalytical);
			$(orianalyticalHidden).appendTo($(currentRow));
			
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
	
		var editMPRSPostNo = $('#hiddenMPRSPostNo').val();
		var editPostStartDate = $('#hiddenPostStartDate').val();
		if (editMPRSPostNo!= null && editMPRSPostNo!="") {
			$('#btn_editPost_'+editMPRSPostNo).attr("onclick", "editPost('" + editMPRSPostNo + "', '" + currentDuration + "', '" + editPostStartDate + "', '" + currentPostEndDate + "', this, " + currentIdx + ")");					
		}
	 	$("#MPRSPostFundingResultModel").modal("hide");
	};
	
	function performSearch() {
		showLoading();
		
		// Ajax call to perform search
		$.ajax({
            url: "<%=request.getContextPath()%>/api/request/fteAdjustment/searchPost",
			cache: false,
			type: "POST",
			data: $("#searchPosition").serialize(),
			success: function(data) {
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
							html += "'" + data.postResponse[i].postFte + "', '" + data.postResponse[i].postStartDateStr;
							html += "', '" + data.postResponse[i].limitDurationEndDateStr + "', '" + data.postResponse[i].postEndDateStr + "')\">" + data.postResponse[i].postId + "</a></td>"
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
				$('#tblSearchResult').dataTable({
					"language": {
						"emptyTable":"No record found."
					}
				});
				
				hideLoading();
				$("#tblSearchResult").show();
			},
			error : function(request, status, error) {
				//Ajax failure
				alert("Some problem occur during call the ajax: "
						+ request.responseText);
			}
		});
	}

	function selectPost(postUid, postId, rankDesc, postDurationDesc, fte, startDate, endDate, endDateDisplay) {				
		// Check is Post ID exist 
		for (var i=0; i<$("#tblFteAdjustment tbody tr").length; i++) {
			if ($($($("#tblFteAdjustment tbody tr")[i]).find("td")[0]).text().trim() == postId) {
			
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
            		var responseStr = "<table class='table-bordered mprs_table'><thead><th>Request ID</th><th>Request Type</th><th>Status</th></thead><tbody>";
            		for (i=0; i<data.jsonResultResponse.length; i++) {
	            		responseStr += "<tr>";
						responseStr += "<td>" + data.jsonResultResponse[i].requestId + "</td>";
						responseStr += "<td>" + data.jsonResultResponse[i].requestType + "</td>";
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
							if ($("#tblFteAdjustment tbody tr td").html() == "No record found.") {
								$("#tblFteAdjustment tbody").remove();
							}
							
							var rowNum = $("#tblFteAdjustment tbody tr").length;
					
							var row = "<tbody><tr id='postNO_" + postUid + "'>";
							row += "<td><input type='hidden' name='requestPostNo' value='" + postUid + "'/>"
									+ postId + "</td>";
							row += "<td name='requestRank'>" + rankDesc + "</td>";
							row += "<td name='requestDuration'>" + postDurationDesc + "</td>";
							row += "<td style='text-align:center;width:60px'><input type='text' name='requestNewFTE' value='" + fte + "' class='form-control' style='width:60px' step='0.01' oninput=\"this.value=this.value.replace(/[^0-9\.]/g,'');\" /></td>";
							row += "<td name='oriStartDate'>" + startDate + "</td>";
							row += "<td name='oriEndDate'>" + endDateDisplay + "<input name='postEndDate' value='" + endDate + "' type='hidden'/></td>";
							row += "<td style='text-align:center'><button id='btn_editPost_"
									+ postUid
									+ "' type='button' name='btnEdit' class='btn btn-primary' onclick=\"editPost('"
									+ postUid + "', '" + postDurationDesc + "', '" + startDate
									+ "', '" + dataPostDetail.limitDurationEndDate + "', this, " +  rowNum + ")\" style='margin-right:5px;'>View Funding</button><button type='button' class='btn btn-primary' onclick='$(this).parent().parent().parent().remove();'><i class='fa fa-times'></i> Remove</button>";
							row += "<input type='hidden' name='hidOriginalPostFTE' value='" + fte + "'/>";							
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
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].startDateStr' value='" + dataPostDetail.fundingList[i].fundSrcStartDate  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].endDateStr' value='" + dataPostDetail.fundingList[i].fundSrcEndDate  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].fundSrcFte' value='" + dataPostDetail.fundingList[i].fundSrcFte  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].fundSrcRemark' value='" + dataPostDetail.fundingList[i].fundSrcRemark  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].inst' value='" + dataPostDetail.fundingList[i].inst  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].section' value='" + dataPostDetail.fundingList[i].section  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].analytical' value='" + dataPostDetail.fundingList[i].analytical  + "'/>";
								
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].oriAnnualPlanInd' value='" + dataPostDetail.fundingList[i].annualPlanInd  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].oriProgramYear' value='" + dataPostDetail.fundingList[i].programYear  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].oriProgramCode' value='" + dataPostDetail.fundingList[i].programCode  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].oriProgramName' value='" + dataPostDetail.fundingList[i].programName  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].oriProgramTypeCode' value='" + dataPostDetail.fundingList[i].programTypeCode  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].oriFundSrcId' value='" + dataPostDetail.fundingList[i].fundSrcId  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].oriFundSrcSubCatId' value='" + dataPostDetail.fundingList[i].fundSrcSubCatId  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].oriStartDateStr' value='" + dataPostDetail.fundingList[i].fundSrcStartDate  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].oriEndDateStr' value='" + dataPostDetail.fundingList[i].fundSrcEndDate  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].oriFundSrcFte' value='" + dataPostDetail.fundingList[i].fundSrcFte  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].oriFundSrcRemark' value='" + dataPostDetail.fundingList[i].fundSrcRemark  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].oriInst' value='" + dataPostDetail.fundingList[i].inst  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].oriSection' value='" + dataPostDetail.fundingList[i].section  + "'/>";
								row += "<input type='hidden' name='requestPositionList[" + rowNum +"].requestFundingList[" + i + "].oriAnalytical' value='" + dataPostDetail.fundingList[i].analytical  + "'/>";							
							}
							
							row += "</div>";
							row += "</td>";
							
							row += "</tr></tbody>";
							if ($("#tblFteAdjustment tbody").length == 0) {
								$("#tblFteAdjustment thead").after(row);
							}
							else {
								$("#tblFteAdjustment tbody:last").after(row);
							}
							
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
	
	var currentAction = null;
	
	function resumeSubmit() {
		showLoading();
		var postUid = [];
		for (var i=0; i<$("input[name='requestPostNo']").length; i++) {
			postUid.push($($("input[name='requestPostNo']")[i]).val());
		}
			
		$("#frmDetail input[type='text']").attr("disabled", false);
		$("#frmDetail input[type='radio']").attr("disabled", false);
		$("#frmDetail textarea").attr("disabled", false);
		$("#frmDetail select").attr("disabled", false);
		$("#frmDetail input[type='hidden']").attr("disabled", false);
		currentAction.submit();
	}

	$(function() {
		$("#requestId").prop("disabled", true);
		$("#requestStatus").prop("disabled", true);

		var status = $("#requestStatus").val();

		// If Withdraw / Approved, disable All field
		if (status == "CONFIRMED" || status == "WITHDRAW") {
			$("select").prop("disabled", true);
			$("#model_nextWFStep select").prop("disabled", false);
			$("input[type='text']").prop("disabled", true);
			$("#model_editEmail input[type='text']").prop("disabled", false);
			$("#btnAddPost").prop("disabled", true);
			$("button[name='btnChildRemove']").prop("disabled", true);
		}

		if ($("#tblFteAdjustment tbody tr").length == 0) {
			$("#tblFteAdjustment thead").after("<tbody><tr><td colspan='7'>No record found.</td></tr></tbody>");
		}
	
		$("#frmDetail").bootstrapValidator({
			message: ' This value is not valid',
			live: "submitted",
			
		})
		.on('error.field.bv', function(e, data) {
			hideLoading();
		})
		.on('success.form.bv', function(e){
			var currentTarget = e.target;
			e.preventDefault();
			
			if (!validateForm()) {
				e.preventDefault();
				hideLoading();
			}
			else {
				currentAction = e.target;
				if (canEditFinancialInfo == "Y") {
					// Compare the funding different
					var formEdited = false;
					
					var noOfFunding = $("div[name='hiddenTable'] input[name$='annualPlanInd']").size();
					for (var i=0; i< noOfFunding; i++) {
						var annualPlanInd = $($("div[name='hiddenTable'] input[name$='annualPlanInd']")[i]).val();
						var programYear = $($("div[name='hiddenTable'] input[name$='programYear']")[i]).val();
						var programCode = $($("div[name='hiddenTable'] input[name$='programCode']")[i]).val();
						var programName = $($("div[name='hiddenTable'] input[name$='programName']")[i]).val();
						var programTypeCode = $($("div[name='hiddenTable'] input[name$='programTypeCode']")[i]).val();
						var fundSrcId = $($("div[name='hiddenTable'] input[name$='fundSrcId']")[i]).val();
						var fundSrcStartDateStr = $($("div[name='hiddenTable'] input[name$='fundSrcStartDate']")[i]).val();
						var fundSrcEndDateStr = $($("div[name='hiddenTable'] input[name$='fundSrcEndDate']")[i]).val();
						var fundSrcFte = $($("div[name='hiddenTable'] input[name$='fundSrcFte']")[i]).val();
						var fundSrcRemark = $($("div[name='hiddenTable'] input[name$='fundSrcRemark']")[i]).val();
						var inst = $($("div[name='hiddenTable'] input[name$='inst']")[i]).val();
						var section = $($("div[name='hiddenTable'] input[name$='section']")[i]).val();
						var analytical = $($("div[name='hiddenTable'] input[name$='analytical']")[i]).val();
						
						var oriAnnualPlanInd = $($("div[name='hiddenTable'] input[name$='oriAnnualPlanInd']")[i]).val();
						var oriProgramYear = $($("div[name='hiddenTable'] input[name$='oriProgramYear']")[i]).val();
						var oriProgramCode = $($("div[name='hiddenTable'] input[name$='oriProgramCode']")[i]).val();
						var oriProgramName = $($("div[name='hiddenTable'] input[name$='oriProgramName']")[i]).val();
						var oriProgramTypeCode = $($("div[name='hiddenTable'] input[name$='oriProgramTypeCode']")[i]).val();
						var oriFundSrcId = $($("div[name='hiddenTable'] input[name$='oriFundSrcId']")[i]).val();
						var oriFundSrcStartDateStr = $($("div[name='hiddenTable'] input[name$='oriFundSrcStartDate']")[i]).val();
						var oriFundSrcEndDateStr = $($("div[name='hiddenTable'] input[name$='oriFundSrcEndDate']")[i]).val();
						var oriFundSrcFte = $($("div[name='hiddenTable'] input[name$='oriFundSrcFte']")[i]).val();
						var oriFundSrcRemark = $($("div[name='hiddenTable'] input[name$='oriFundSrcRemark']")[i]).val();
						var oriInst = $($("div[name='hiddenTable'] input[name$='oriInst']")[i]).val();
						var oriSection = $($("div[name='hiddenTable'] input[name$='oriSection']")[i]).val();
						var oriAnalytical = $($("div[name='hiddenTable'] input[name$='oriAnalytical']")[i]).val();
						
						if (annualPlanInd != oriAnnualPlanInd) {
							formEdited = true;
							break;
						}	
					
						if (programYear != oriProgramYear) {
							formEdited = true;
							break;
						}	
					
						if (programCode != oriProgramCode) {
							formEdited = true;
							break;
						}	
					
						if (programName != oriProgramName) {
							formEdited = true;
							break;
						}	
					
						if (programTypeCode != oriProgramTypeCode) {
							formEdited = true;
							break;
						}	
					
						if (fundSrcId != oriFundSrcId) {
							formEdited = true;
							break;
						}	
					
						if (fundSrcStartDateStr != oriFundSrcStartDateStr) {
							formEdited = true;
							break;
						}	
					
						if (fundSrcEndDateStr != oriFundSrcEndDateStr) {
							formEdited = true;
							break;
						}	
					
						if (fundSrcFte != oriFundSrcFte) {
							formEdited = true;
							break;
						}	
					
						if (fundSrcRemark != oriFundSrcRemark) {
							formEdited = true;
							break;
						}	
					
						if (inst != oriInst) {
							formEdited = true;
							break;
						}
					
						if (section != oriSection) {
							formEdited = true;
							break;
						}
					
						if (analytical != oriAnalytical) {
							formEdited = true;
							break;
						}
					}
					
					if (!formEdited) {
						$("#fundingSrcNoChangeDialog").modal("show");	
						hideLoading();
						e.preventDefault();
						return;
					}
				}
				resumeSubmit();
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
			<c:when test="${formBean.canEditDetailInfo != 'Y'}">
				$("#divApproval input").attr("disabled", true);
				$("#divApproval input[type='file']").attr("disabled", true);
				$("#divApproval button[name='btnUploadFile']").attr("disabled", true);
				$("#divApproval button[name='btnAddUploadFile']").attr("disabled", true);
				$("#divApproval button[name='btnRemoveFile']").attr("disabled", true);
				// $("#requester").attr("disabled", true);
				$("#requestReason").attr("disabled", true);
				$("#requestDuartion").attr("disabled", true);
				$("#requestDuartion2").attr("disabled", true);
				
				$("#btnAddPost").attr("disabled", true);
				
				// Disable the FTE field 
				$("input[name='requestNewFTE']").attr("disabled", true);
				
				
				$("button[name='btnChildRemove']").attr("disabled", true);
			</c:when>
		</c:choose>
		
		<c:choose>	
			<c:when test="${formBean.canEditFinancialInfo != 'Y'}">
				$("#MPRSPostFundingResultModel input").attr("disabled", true);
				$("#MPRSPostFundingResultModel select").attr("disabled", true);
				$("#MPRSPostFundingResultModel textarea").attr("disabled", true);
				$("#MPRSPostFundingResultModel button[name='btnRemoveFunding']").attr("disabled", true);
				$("#MPRSPostFundingResultModel button[name='btnAddFunding']").attr("disabled", true);
				$("#btnSavePostFunding").attr("disabled", true);
				
			</c:when>
		</c:choose>

		<c:choose>
			<c:when test="${formBean.userHaveSaveRight != 'Y'}">
				$("#divApproval input").attr("disabled", true);
				$("#divApproval input[type='file']").attr("disabled", true);
				$("#divApproval button[name='btnUploadFile']").attr("disabled", true);
				$("#divApproval button[name='btnAddUploadFile']").attr("disabled", true);
				$("#divApproval button[name='btnRemoveFile']").attr("disabled", true);
				// $("#requester").attr("disabled", true);
				$("#requestReason").attr("disabled", true);
				$("#btnAddPost").attr("disabled", true);
				$("button[name='btnChildRemove']").attr("disabled", true);
				$("button[name='btnEdit']").html('<i class="fa fa-money"></i> View Funding');
				$("#btnSavePostFunding").hide();
				
				$("input[type='file']").prop("disabled", true);
				$("button[name='btnUploadFile']").attr("disabled", true);
				$("button[name='btnRemoveFile']").attr("disabled", true);
			</c:when>
		</c:choose>
		
		
	});
	
	function changeAnnualPlan2() {
		if ($("#ddl_annual_plan_ind").val() != "Y") {
			// $("#programType").show(); 
			// $("#lblProgramType").show(); 
			
			// $("#starProgramCode").hide();
			$("#starYear").hide();
			$("#starName").hide();
			
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_name', false);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_year', false);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_code', false);
		}
		else {
			// $("#programType").hide();
			// $("#lblProgramType").hide();
			
			// $("#starProgramCode").show();
			$("#starYear").show();
			$("#starName").show();
			
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_name', true);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_year', true);
			$("#frmDialog").data('bootstrapValidator').enableFieldValidators('program_code', true);
		}
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
			
			$($("#tblFunding input[name$='oriAnnualPlanInd']")[i]).attr("name", "requestFundingList[" + i + "].oriAnnualPlanInd");
			$($("#tblFunding input[name$='oriProgramYear']")[i]).attr("name", "requestFundingList[" + i + "].oriProgramYear");
			$($("#tblFunding input[name$='oriProgramCode']")[i]).attr("name", "requestFundingList[" + i + "].oriProgramCode");
			$($("#tblFunding input[name$='oriProgramName']")[i]).attr("name", "requestFundingList[" + i + "].oriProgramName");
			$($("#tblFunding input[name$='oriProgramTypeCode']")[i]).attr("name", "requestFundingList[" + i + "].oriProgramTypeCode");
			$($("#tblFunding input[name$='oriFundSrcId']")[i]).attr("name", "requestFundingList[" + i + "].oriFundSrcId");
			$($("#tblFunding input[name$='oriFundSrcStartDate']")[i]).attr("name", "requestFundingList[" + i + "].oriFundSrcStartDate");
			$($("#tblFunding input[name$='oriFundSrcEndDate']")[i]).attr("name", "requestFundingList[" + i + "].oriFundSrcEndDate");
			$($("#tblFunding input[name$='oriFundSrcFte']")[i]).attr("name", "requestFundingList[" + i + "].oriFundSrcFte");
			$($("#tblFunding input[name$='oriFundSrcRemark']")[i]).attr("name", "requestFundingList[" + i + "].oriFundSrcRemark");
			$($("#tblFunding input[name$='oriInst']")[i]).attr("name", "requestFundingList[" + i + "].oriInst");
			$($("#tblFunding input[name$='oriSection']")[i]).attr("name", "requestFundingList[" + i + "].oriSection");
			$($("#tblFunding input[name$='oriAnalytical']")[i]).attr("name", "requestFundingList[" + i + "].oriAnalytical");
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
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > Request > FTE Adjustment 
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
			<div class="title pull-left"><i class="fa fa-file-text-o"></i>FTE Adjustment</div>
		</div>
		<form id="frmDetail" method="POST" enctype="multipart/form-data">
			<form:hidden path="formBean.formAction" id="hiddenFormAction" />
			<form:hidden path="formBean.requestNo" id="hiddenRequestNo" />
			<form:hidden path="formBean.lastUpdateDate" id="hiddenLastUpdateDate" />
			<form:hidden path="formBean.lastUpdatedByLoginUser" id="hiddenLastUpdatedByLoginUser" />
			
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
								style="width:100%;"></form:input>
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
				</div>
				<div class="row" style="display:none">
						<div class="form-group">
							<div class="col-sm-3">
							  	<label for="effectiveDate" class="control-label">Effective Date of Change<font class="star">*</font></label>
							</div>
							<div class="col-sm-3">
							  	<div class="input-group date">
							  		<form:input path="formBean.effectiveDate" class="form-control"/>
									<span class="input-group-addon">
										<span class="glyphicon glyphicon-calendar"></span>
									</span>	
								</div>
							</div>
						</div>
					</div>
			</div>
			<div class="panel panel-custom-primary">
				<div class="panel-heading">
					<div class="panel-heading-title" style="width:500px">Adjust FTE of following Posts</div>
				</div>
				<div class="panel-body" style="padding-top:5px">
					<table id="tblFteAdjustment" class="table-bordered mprs_table">
						<thead>
							<tr>
								<th>Post ID</th>
								<th>Rank</th>
								<th>Duration</th>
								<th>FTE</th> <!-- Add the FTE field -->
								<th>Original Start Date</th>
								<th>Original End Date</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="listValue" items="${formBean.requestPositionList}" varStatus="pStatus1">
								<tr id="postNO_${listValue.postNo}">
									<td><input type="hidden" name="requestPostNo" value="${listValue.postNo}">${listValue.postId}</td>
									<td>${listValue.rank.rankName}</td>
									<td>${listValue.postDuration == "R"?"Recurrent":listValue.postDuration == "TLC"?"Time Limited - Contract":listValue.postDuration == "TLT"?"Time Limited - Temporary":""}</td>
									<td style='text-align:center;width:60px'><input type="text" name="requestNewFTE" value="${listValue.postFTE}" class="form-control" style="width:60px" step="0.01" oninput="this.value=this.value.replace(/[^0-9\.]/g,'');" /></td>
									<td><fmt:formatDate value="${listValue.postStartDate}" pattern="dd/MM/yyyy"/></td>
									<td>
										<c:if test="${ listValue.limitDurationUnit == null}">
											<fmt:formatDate value="${listValue.limitDurationEndDate}" pattern="dd/MM/yyyy"/> 
										</c:if>
										<c:if test="${listValue.limitDurationUnit != null}">
											${listValue.limitDurationNo} 
											<c:if test="${listValue.limitDurationUnit == 'M'}">
												Month(s)
											</c:if>
											<c:if test="${listValue.limitDurationUnit == 'Y'}">
												Year(s)
											</c:if>
										</c:if>
										<input name="postEndDate" value="<fmt:formatDate value='${listValue.limitDurationEndDate}' pattern='dd/MM/yyyy'/> " type="hidden"/>
									</td>
									
									<td style="text-align: center">
										<button id="btn_editPost_${listValue.postNo}" name="btnEdit" type="button" class="btn btn-primary" style="margin-right:5px;"
											onclick="editPost('${listValue.postNo}', '${ listValue.postDuration }', '<fmt:formatDate value="${listValue.postStartDate}" pattern="dd/MM/yyyy"/>', '<fmt:formatDate value="${listValue.limitDurationEndDate}" pattern="dd/MM/yyyy"/>', this, ${pStatus1.index})">Edit Funding</button>
										<button type="button" name="btnChildRemove" class="btn btn-primary"
											onclick="$(this).parent().parent().parent().remove();"><i class="fa fa-times"></i> Remove</button>
											<input type='hidden' name='hidOriginalPostFTE' value='${listValue.originalPostFTE}'/>
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
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].oriAnnualPlanInd" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].oriProgramYear" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].oriProgramCode" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].oriProgramName" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].oriProgramTypeCode" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].oriFundSrcId" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].oriFundSrcSubCatId" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].oriStartDateStr" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].oriEndDateStr" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].oriFundSrcFte" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].oriFundSrcRemark" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].oriInst" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].oriSection" />
												<form:hidden path="formBean.requestPositionList[${pStatus1.index}].requestFundingList[${pStatus2.index}].oriAnalytical" />
												<input type='hidden' name='hidPostDuration' value='${listValue.postDuration}'/>
												<input type='hidden' name='hidPostFTE' value='${listValue.postFTEType}'/>
											</c:forEach>
										</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div style="text-align:right">
						<button type="button" id="btnAddPost" class="btn btn-primary" style="width:110px" onclick="startSearching()"><i class="fa fa-plus"></i> Add Post</button>
					</div>
					<br>
					<div class="row">
						<div class="col-sm-2">
							<label for="txt_reason" class="field_request_label"><strong>Reason of FTE Adjustment</strong></label>
						</div>
						<div class="col-sm-9">
							<form:input path="formBean.requestReason" class="form-control" style="width:100%" maxlength="200"/>
						</div>
					</div>
				</div>
			</div>
			<br>
			
			<%@ include file="/WEB-INF/views/request/common_approval_FTE_adjustment.jsp"%>
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
					<input type="hidden" id="timeLimitedPostOnly" value="Y" />
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
									<option value="">- Select -</option>
									<form:options items="${staffGroupList}" />
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
						
						<div class="row" style="text-align:right">
							<div class="col-sm-12" style="text-align:right">
								<button type="button" class="btn btn-primary" style="width:130px;" onclick="performSearch()"><i class="fa fa-search"></i> Search</button>
							</div>
						</div>
					</fieldset>
				</form>
				<br/>
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
											<label for="txt_program_name" class="control-label">Name
												<font name="starName" class="star">*</font>
											</label>
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
												   class="control-label">Program Type
												<font class="star" id="starProgramType">*</font>
													</label>
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
											<label for="" class="control-label"><strong>Funding Source</strong><font class="star">*</font></label>
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
											<label for="" class="control-label"><strong>Sub-category of <br/>Funding Source</strong></label>
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
											<label for="" class="control-label">Start Date<font class="star">*</font></label>
										</div>
										<div class="col-sm-2">
											<div class="input-group date" id="dp_fund_src_1st_start_date">
												<form:input
														path="formBean.fundSrcStartDate"
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
											<label for="" class="control-label">End Date<font class="star" name="lblEndDateStar">*</font></label>
										</div>
										<div class="col-sm-2">
											<div class="input-group date" id="dp_fund_src_1st_end_date">
												<form:input
														path="formBean.fundSrcEndDate"
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
												
										<input type="hidden" name="oriAnnualPlanInd"/>
										<input type="hidden" name="oriProgramYear"/>
										<input type="hidden" name="oriProgramCode"/>
										<input type="hidden" name="oriProgramName"/>
										<input type="hidden" name="oriProgramTypeCode"/>
										<input type="hidden" name="oriFundSrcId"/>
										<input type="hidden" name="oriFundSrcStartDate"/>
										<input type="hidden" name="oriFundSrcEndDate"/>
										<input type="hidden" name="oriFundSrcFte"/>
										<input type="hidden" name="oriFundSrcRemark"/>
										<input type="hidden" name="oriInst"/>
										<input type="hidden" name="oriSection"/>
										<input type="hidden" name="oriAnalytical"/>
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
			<div class="modal-footer">
				<button type="button" class="btn btn-default" style="width:110px" onclick='showDetail()'>View Detail</button>
				<button type="button" id="btnSavePostFunding" style="width:110px"
					class="btn btn-primary" onclick="savePostFunding()"><i class="fa fa-check"></i> OK</button>
				<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal">Cancel</button>
			</div>
		</div>
	</div>
</div>
</form>
<!-- ./#frmDialog -->

<!-- Warning Model -->
<div id="fundingSrcNoChangeDialog" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width: 600px">
		<div class="modal-content">
			<div class="modal-header" style="background-color: #f39c12">
				<h4>
					<b><span>Warning</span></b>
				</h4>
			</div>
			<div class="modal-body">
				<div id="fundingSrcWarningContent">Funding information have no changes. Please review and update the detail funding information.</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" style="width: 100px"
					data-dismiss="modal" onclick="resumeSubmit()">OK</button>
			</div>
		</div>
	</div>
</div>
<!-- ./#warningModal -->


<%@ include file="/WEB-INF/views/request/common_MPRPost_detail.jsp"%>
<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>