<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	var haveDefaultDepartment = "";
	var haveDefaultStaffGroup = "";
	var haveDefaultRank = "";
	var endDateMandatory = false;

	function payrollIsRunning() {
		if ($("button[name='btnConfirm']").length > 0) {
			$("#warningTitle").html("Warning");
	        $("#warningContent").html("HCM Payroll is running, please wait a moment and re-try.");
	        $("#warningModal").modal("show");
	        
	        $("button[name='btnConfirm']").attr("disabled", true);
        }
	}
	
	function checkFTE() {
		if ($("#currentRole").val() == "FIN_MANAGER") {
			if ($("#postFTE").val() == "FULL_TIME" && $("#postFTEValue").val() != 1) {
				displayError("FTE should be 1 for full time post.");
					
				hideLoading();
				return false;
			}
		}
		
		return true;
	}
	
	function createNewRequest() {
		document.location.href = "<%= request.getContextPath() %>/request/newPost";
	}

	function validateForm() {
		var errMsg = "";
		
		if (!$("#txt_postStartDate").prop("disabled")) {
			// Check whether the HCM position start date is after post start date
			var hcmStartDate = $("#hiddenHcmDateEffective").val();
			var postStartDate = $("#txt_postStartDate").val();
			var dayBetween = getDaysBetween(postStartDate, hcmStartDate);
			if (dayBetween < 0) {
				errMsg = "Post Start Date should be after Effective Date of HCM Position.<br/>";
			}
			
			if (!$("#txt_post_actual_end_date").prop("disabled")) {
				var postEndDate = $("#txt_post_actual_end_date").val();
				var dayBetween2 = getDaysBetween(postEndDate, postStartDate);
				if (dayBetween2 < 0) {
					errMsg = "Fixed End Date should be after Post Start Date.<br/>";
				} 
			}
		}
		
		// Perform Funding Source checking or not
		if ($("#canEditFinancialInfo").val() == "Y" && $("#returnCase").val() != "Y") {
			var minStartDate = 0;
			var maxEndDate = 0;
		
			var totalFTE = 0;
			for (var i=0; i<$("#tblFunding tr").length; i++) {
				// UT29797: Validation 1: Cross check again year and start date
				// Get the program year
				var year = $($("#tblFunding select[name$='programYear']")[i]).val();
				var startDate = $($("#tblFunding input[name$='fundSrcStartDate']")[i]).val();
				
				var fromYear = year.split("/")[0];
				var toYear = "20" + year.split("/")[1];
				var startDateSplit = startDate.split("/");
				
				if (fromYear != "") {
					if (!(new Date(fromYear, 3, 1).getTime() <= new Date(startDateSplit[2], Number(startDateSplit[1])-1, Number(startDateSplit[0])).getTime() 
						&& new Date(toYear, 2, 31).getTime() >= new Date(startDateSplit[2], Number(startDateSplit[1])-1, Number(startDateSplit[0])).getTime())) {
						errMsg += "Funding Source " + (i+1) + ": Start Date should fall within the selected Program Year. <br/>"; 
					}
				}
				
				totalFTE = Number(totalFTE) + Number($($("#tblFunding input[name$='fundSrcFte']")[i]).val());
				
				var startDateStr = $($("#tblFunding input[name$='fundSrcStartDate']")[i]).val();
				var endDateStr = $($("#tblFunding input[name$='fundSrcEndDate']")[i]).val();
				
				if (startDateStr != "") {
					var currentStartDate = new Date(startDateStr.split("/")[2], startDateStr.split("/")[1]-1, startDateStr.split("/")[0]);
					
					if (minStartDate == 0) {
						minStartDate = currentStartDate.getTime();
					}
					else {
						if (currentStartDate < minStartDate) {
							minStartDate = currentStartDate.getTime();
						}
					}
				}
				
				if (endDateStr != "") {
					var currentEndDate = new Date(endDateStr.split("/")[2], endDateStr.split("/")[1]-1, endDateStr.split("/")[0]);
					
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
				
			totalFTE = new Number(totalFTE).toFixed(4);
			
				// UT29925(b): Check total FTE entered
			var postFTE = Number($("#postFTEValue").val());
			if (totalFTE < postFTE) {
				errMsg += "Total FTE should not be less than the FTE value entered in \"Post Detail\".<br/>";
			}		
				
			// UT29925(c)
			var postStartDateStr = $("#txt_postStartDate").val();
			var postStartDate = new Date(postStartDateStr.split("/")[2], postStartDateStr.split("/")[1]-1, postStartDateStr.split("/")[0]);
			var duration = $("#ddl_post_duration").val();
			
			if (postStartDate.getTime() < minStartDate) {
				errMsg += "Funding Start Date should not later than the Post Start Date.<br/>";
			}
			
			if (duration != "R") {
				if (maxEndDate == 0) {
					errMsg += "Funding End Date cannot be empty.<br/>";
				}
				
				var postEndDateStr = $("#postEndDate").val();
				var postEndDate = new Date(postEndDateStr.split("/")[2], postEndDateStr.split("/")[1]-1, postEndDateStr.split("/")[0]);
				if (postEndDate.getTime() > maxEndDate) {
					errMsg += "Funding End Date should later than the Post End Date.<br/>";
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
	
	function changeFTE() {
		if ($("#postFTE").val() == "FULL_TIME") {
			$("#postFTEValue").val("1");
			
			// Revalidate
			$("#frmDetail").bootstrapValidator('revalidateField', 'postFTE');
			$("#frmDetail").bootstrapValidator('revalidateField', 'postFTEValue');			
		}
		else {
			$("#postFTEValue").val("");
			
			$("#frmDetail").bootstrapValidator('revalidateField', 'postFTE');
			$("#frmDetail").bootstrapValidator('revalidateField', 'postFTEValue');		
		} 
	}
	
	function showDiv(displayid) {

		if (document.getElementById(displayid).classList.contains('hidediv')) {
			document.getElementById(displayid).classList.remove('hidediv');
		}
		else {
			document.getElementById(displayid).classList.add('hidediv');
		}
	}

	function hiddenMessage() {
		$(".alert").hide();
		$("#divHCMDetail").hide();
	}
	
	function performHcmSearch() {
		// Check is any criteria inputted
		if ($("#hcmJob").val() == "" &&   
			$("#hcmPostTitle").val() == "" && 
			$("#hcmOrganization").val() == "" && 
			$("#hcmPostOrganization").val() == "" && 
			$("#hcmUnitTeam").val() == "") {
		
			$("#warningTitle").html("Warning");
            $("#warningContent").html("Please select at least one criteria.");
            $("#warningModal").modal("show");
            
            return;
		}
	
	
		hiddenMessage();
		showLoading();

		// Ajax call to perform search
		$.ajax({
			url: "<%= request.getContextPath() %>/api/hcm/searchHcmPositionForPostCreation",
			cache: false,
			type: "POST",
			data: {
				hcmJob: $("#hcmJob").val(),
				hcmPostTitle: $("#hcmPostTitle").val(),
				hcmOrganization: $("#hcmOrganization").val(),
				hcmPostOrganization: $("#hcmPostOrganization").val(),
				hcmUnitTeam: $("#hcmUnitTeam").val(),
				hcmPositionName: $("#hcmPositionName").val(),
				staffGroup: $("#staffGroupDisplay option:selected").text() 
			},
			success: function(data) {
				// Clear the result table
				$("#tblSearchResult tbody").remove();
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
						html += "', '" + data.hcmPositionResponse[i].clusterCode + "', '" + data.hcmPositionResponse[i].instCode + "', '', '')\">Select</button></td>";
						html += "</tr>";
            		}
            		html += "</tbody>";
            		
            		// Append row to search result
					$("#tblSearchResult thead").after(html);
					
            	}
				var oTable = $('#tblSearchResult').dataTable();
				oTable.fnDestroy();
				$('#tblSearchResult').dataTable({
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
				$('#searchResultModel').modal('show');
			},
			error: function(request, status, error) {
				//Ajax failure
				alert("Some problem occur during call the ajax: "
						+ request.responseText);
			}
		});
	}

	function performSave() {
		hiddenMessage();
		$("#frmDetail").submit();
	}

	function selectHcmPositionDetail(positionId, cluster, inst, dept, staffGroup) {
		showLoading();
		$.ajax({
            url: "<%=request.getContextPath() %>/hcm/getHCMPostDetail",
            type: "POST",
            data: {postId: positionId},
            success: function(data) {
				// Remove the selected row
				//$("#tblPositionDelete tbody").remove();
				$("#hiddenHCMPositionId").val(positionId);
				$("#ddl_cluster").val(cluster);
				changeCluster(inst);
				// $("#ddl_institution").val(inst);
				$("#ddl_department").val(data.deptCode);
				// $("#ddl_staff_group").val(data.staffGroupCode);
				$("#ddl_staff_group").val($("#staffGroupDisplay").val());
				
				refreshDepartmentDropdown();
				
				// Refresh the sub-specialty list
				refreshSubSpecialty();
				
				// If is medical lblDepartment = "Department", otherwise "Specialty"
				var labelText = $("#lblDepartment").html();
				if ($("#ddl_staff_group").val() == "MEDICAL") {
					$("#lblDepartment").html(labelText.replace("Specialty", "Department"));
				}
				else {
					$("#lblDepartment").html(labelText.replace("Department", "Specialty"));
				}
				
				$("#ddl_rank").val(data.rankCode);
				$("#divHCMDetail").show();
				
				var proposedPostId = $('#txt_proposed_post_id').val();
				var prefix = cluster + "-" + inst + "-" + data.deptCode + "-" + data.rankCode;
				if ( proposedPostId.substring(0, prefix.length) != prefix ) {
					$('#txt_proposed_post_id').val('');
				}
	
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

				// Default the position title
				$("#postTitle").val(data.hcmRecord.positionTitle);
				
				$("#hiddenHcmDateEffective").val(data.hcmRecord.dateEffectiveStr);
				$("#relatedHcmEffectiveStartDate").html(data.hcmRecord.effectiveStartDateStr);
				$("#relatedHcmFTE").html(data.hcmRecord.fte);
				$("#relatedHcmHeadCount").html(data.hcmRecord.maxPerson);
				$("#relatedHcmPositionName").html(data.hcmRecord.name);
				$("#relatedHcmHiringStatus").html(data.hcmRecord.availabilityStatus);
				$("#relatedHcmType").html(data.hcmRecord.positionType);
				
				// Close the search Dialog
				$("#searchResultModel").modal("hide");
				$("#searchPanel").hide();
				
				hideLoading();
			}
		});
	}
	
	function performMassSave() {
		$("#txtCopy").prop("disabled", false);
		$("#frmDetail").data('bootstrapValidator').enableFieldValidators('txtCopy', true);
		$("#massSave").modal('show');
	}
	
	function submitMassSave() {
		showLoading();
	
		$("#hiddenNoCopy").val($("#txtCopy").val());
		$("#hiddenFormAction").val("SAVE");
	}
	
	function closeMassSave() {
		$("#frmDetail").data('bootstrapValidator').enableFieldValidators('txtCopy', false);
		$("#txtCopy").val("");
		$("#hiddenNoCopy").val("");
		$("#txtCopy").prop("disabled", true);
		$("#massSave").modal('hide');
	}
	
	function changeRankDropdown() {
		$("#lblSubSpecialty").show();
		$("#subSpecialty").show();
		
		if (haveDefaultRank != $("#ddl_rank").val() && haveDefaultRank != "") {
			$("#warningTitle").html("Warning");
	        $("#warningContent").html("Please note that the selected \"Rank\" is not in line with the chosen HCM Position.");
	        $("#warningModal").modal("show");
		}
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
            	 
            	 if ($("#ddl_department").val() != "") {
            	 	refreshRankDropdown();
            	 	refreshSubSpecialty();
            	 }
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });	
	}
	
	function refreshRankDropdown() {
		var tmpRank = $("#ddl_rank").val();
	
		$.ajax ({
            url: "<%=request.getContextPath()%>/common/getRankByStaffGroup",
            type: "POST",
            data: {staffGroupCode: $("#ddl_staff_group").val(), deptCode: $("#ddl_department").val()},
            success: function(data) {
            	 $("#ddl_rank").empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $("#ddl_rank").append(option);
            	 
            	 for (var i=0; i<data.rankList.length; i++) {
	            	 option = "<option value='" + data.rankList[i].rankCode + "'>" + data.rankList[i].rankName + "</option>";
	            	 $("#ddl_rank").append(option);
            	 }
            	 
            	 $("#ddl_rank").val(tmpRank);
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
	
	function changeAnnualPlanByRow(obj) {
		var annualPlanInd = $(obj).val();
		var programTypeDropdown = $(obj).parent().parent().parent().parent().find("select[name$='programTypeCode']");
		var programTypeDropdownName = programTypeDropdown.attr("name");
		
		// Load up the program type
		var tmpProgramType = $(programTypeDropdown).val();
		
		$.ajax ({
            url: "<%=request.getContextPath()%>/common/getProgramType",
            type: "POST",
            data: {annualPlanInd: annualPlanInd, postDuration: $("#ddl_post_duration").val(), postFTEType: $("#postFTE").val()},
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
            	 	$("#frmDetail").bootstrapValidator('revalidateField', programTypeDropdownName);
            	 }
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });		
        
        // Add the validation
        var programCodeComp = $(obj).parent().parent().parent().parent().find("input[name$='programCode']");
		var programCodeCompName = programCodeComp.attr("name");
		
		var programNameComp = $(obj).parent().parent().parent().parent().find("input[name$='programName']");
		var programNameCompName = programNameComp.attr("name");
		
		var programYearComp = $(obj).parent().parent().parent().parent().find("select[name$='programYear']");
		var programYearCompName = programYearComp.attr("name");
        
        if (annualPlanInd == "Y") {
        	$($(obj).parent().parent().parent().parent().find("font[name='starProgramCode']")[0]).show();
        	$($(obj).parent().parent().parent().parent().find("font[name='starName']")[0]).show();
        	$($(obj).parent().parent().parent().parent().find("font[name='starYear']")[0]).show();
        	
        	$(obj).parent().parent().parent().parent().find("select[name$='programTypeCode']");
        	
        	$("#frmDetail").data('bootstrapValidator').enableFieldValidators(programCodeCompName, true);
        	$("#frmDetail").data('bootstrapValidator').enableFieldValidators(programNameCompName, true);
        	$("#frmDetail").data('bootstrapValidator').enableFieldValidators(programYearCompName, true);
        	
        }
        else {
        	$($(obj).parent().parent().parent().parent().find("font[name='starProgramCode']")[0]).hide();
        	$($(obj).parent().parent().parent().parent().find("font[name='starName']")[0]).hide();
        	$($(obj).parent().parent().parent().parent().find("font[name='starYear']")[0]).hide();
        	
        	$("#frmDetail").data('bootstrapValidator').enableFieldValidators(programCodeCompName, false);
        	$("#frmDetail").data('bootstrapValidator').enableFieldValidators(programNameCompName, false);
        	$("#frmDetail").data('bootstrapValidator').enableFieldValidators(programYearCompName, false);
        }
	}
	
	function resetValidatorForFundingSource() {
		var objCount = $("#tblFunding tr").length;
		// Remove all validator
		for (var i=0; i<objCount; i++) {
			var annualPlanInd = $($("#tblFunding tr")[i]).find("select[name$='annualPlanInd']").val()

			// Add the validation
	        if (annualPlanInd == "Y") {
	        	// alert("add ind");
	        	// $("#frmDetail").bootstrapValidator('addField', $($("#tblFunding input[name$='programCode']")[i]));
	        }
	        else {
	        	//$("#frmDetail").bootstrapValidator('removeField', $($("#tblFunding input[name$='programCode']")[i]));
	        	//$("#frmDetail").bootstrapValidator('revalidateField', $($("#tblFunding input[name$='programCode']")[i]));
	        }
			// $("#frmDetail").data('bootstrapValidator').enableFieldValidators("requestFundingList[" + i + "].programName", false);
		}
	
		// For each row, if annual plan indicator is "Y"
		for (var i=0; i<objCount; i++) {
			if ($($("#tblFunding select[name$='annualPlanInd']")[i]).val() == "Y") {
				// $("#frmDetail").data('bootstrapValidator').enableFieldValidators("requestFundingList[" + i + "].programName", true);
			}
		}
	}
	
	function changeAnnualPlanInit() {
		//For each row of funding source, reload the dropdown for program type code
		var objCount = $("#tblFunding select[name$='annualPlanInd']").length;
		for (var i=0; i<objCount; i++) {
			changeAnnualPlanByRow($($("#tblFunding select[name$='annualPlanInd']")[i]));
		}
	}
	
	function changeAnnualPlan(firstLoad) {
		var fromFirstLoad = false;
		if (typeof firstLoad != "undefined") {
			fromFirstLoad = firstLoad;
		}
		
		var disabledButton = $("#btnSave").prop("disabled");
	
		/*if ($("#ddl_annual_plan_ind").val() != "Y") {
			$("#starProgramCode").hide();
			$("#starYear").hide();
			$("#starName").hide();
			
			if (!fromFirstLoad) {
				$("#programType").val("");
			}
			
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('program_name', false);
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('program_year', false);
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('program_code', false);
			
			if ($("#ddl_annual_plan_ind").val() != "") {
				$("#frmDetail").data('bootstrapValidator').enableFieldValidators('programType', true);
				
				if (disabledButton) {
					$("#frmDetail").bootstrapValidator('revalidateField', 'programType');
				};
			}
		}
		else {
			$("#starProgramCode").show();
			$("#starYear").show();
			$("#starName").show();
			
			if (!fromFirstLoad) {
				$("#programType").val("");
			}
			
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('program_name', true);
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('program_year', true);
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('program_code', true);
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('programType', true);
			
			if (disabledButton) {
				$("#frmDetail").bootstrapValidator('revalidateField', 'program_name');
				$("#frmDetail").bootstrapValidator('revalidateField', 'program_year');
				$("#frmDetail").bootstrapValidator('revalidateField', 'program_code');
			};
		}*/
		
		// CC176525 - Refresh Program Type
		var tmpProgramType = $("#programType").val();
		$.ajax ({
            url: "<%=request.getContextPath() %>/common/getProgramType",
            type: "POST",
            data: {annualPlanInd: $("#ddl_annual_plan_ind").val(), postDuration: $("#ddl_post_duration").val(), postFTEType: $("#postFTE").val()},
            success: function(data) {
            	 $("#programType").empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $("#programType").append(option);
            	 
            	 for (var i=0; i<data.programTypeList.length; i++) {
	            	 option = "<option value='" + data.programTypeList[i].programTypeCode + "'>" + data.programTypeList[i].programTypeName + "</option>";
	            	 $("#programType").append(option);
            	 }
            	 
            	 $("#programType").val(tmpProgramType);
            	 
            	 if ($("#programType").val() == "" && data.programTypeList.length==1) {
            	 	$("#programType").val($($("#programType option")[1]).val());
            	 }
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	}
	
	function postDurationChange() {
		// If Recurrent - disable the duration and fixed end date
		if ($("#ddl_post_duration").val() == "R") {
			$("#rd_Duartion").prop("checked", false);
			$("#rd_Duartion2").prop("checked", false);
			
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('limitDurationUnit', false);
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('limitDurationNo', false);
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('limitDurationEndDate', false);
			endDateMandatory = false;
			
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
			$("#endDateStar").hide();
			
			clickDuration();
		}
		else {
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('limitDurationUnit', true);
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('limitDurationNo', true);
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('limitDurationEndDate', true);
			$("#rd_Duartion").attr("disabled", false);
			$("#rd_Duartion2").attr("disabled", false);
			$("#ddl_limit_duration_unit").attr("disabled", false);
			$("#txt_limit_duration_no").attr("disabled", false);
			$("#txt_post_actual_end_date").attr("disabled", false);
			endDateMandatory = true;
			
			$("#rd_Duartion").show();
			$("#rd_Duartion2").show();
			$("#ddl_limit_duration_unit").show();
			$("#txt_limit_duration_no").show();
			$("#grpLimitDurationEndDate").show();
			$("#lblDurationType").show();
			$("#lblDurationType2").show();
			$("#endDateStar").show();
			
			clickDuration();
		}
	}
	
	function changeSearchCluster() {
		$.ajax ({
            url: "<%=request.getContextPath() %>/common/getInstitution",
            type: "POST",
            data: {clusterCode: $("#searchClusterId").val()},
            success: function(data) {
            	 $("#searchInstId").empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $("#searchInstId").append(option);
            	 
            	 for (var i=0; i<data.instList.length; i++) {
	            	 option = "<option value='" + data.instList[i].instCode + "'>" + data.instList[i].instCode + "</option>";
	            	 $("#searchInstId").append(option);
            	 }
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	}
	
	function changeCluster() {
		$.ajax ({
            url: "<%=request.getContextPath() %>/common/getInstitution",
            type: "POST",
            data: {clusterCode: $("#ddl_cluster").val()},
            success: function(data) {
            	 $("#ddl_institution").empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $("#ddl_institution").append(option);
            	 
            	 for (var i=0; i<data.instList.length; i++) {
	            	 option = "<option value='" + data.instList[i].instCode + "'>" + data.instList[i].instCode + "</option>";
	            	 $("#ddl_institution").append(option);
            	 }
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
        
        $('#txt_proposed_post_id').val('');
	}
	
	function changeCluster(inst) {
		$.ajax ({
            url: "<%=request.getContextPath() %>/common/getInstitution",
            type: "POST",
            data: {clusterCode: $("#ddl_cluster").val()},
            success: function(data) {
            	 $("#ddl_institution").empty();
            	 var option = "<option value=''> - Select - </option>";
            	 $("#ddl_institution").append(option);
            	 
            	 for (var i=0; i<data.instList.length; i++) {
	            	 option = "<option value='" + data.instList[i].instCode + "'>" + data.instList[i].instCode + "</option>";
	            	 $("#ddl_institution").append(option);
            	 }
            	 
            	 $("#ddl_institution").val(inst);
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	}
	
	function cloneFromExisting() {
		// Hide the result section
		$("#tblMPRSSearchResult").hide();
		var oTable = $('#tblMPRSSearchResult').dataTable();
		oTable.fnDestroy();
		
		$("#searchMPRSModel").modal("show");
	}
	
	function performPostSearch() {
		showLoading();
	
		// Ajax call to perform search
		$.ajax({
            url: "<%=request.getContextPath() %>/api/post/searchPost",
            cache: false,
            type: "POST",
            data: $("#searchPosition").serialize(),
            success: function(data) {
            	hideLoading();
            
            	// Clear the result table
            	$("#tblMPRSSearchResult tbody").remove();
            	
            	var html = "";
            	html = "<tbody>";
            	if (data.postResponse != null) {
            		var len = data.postResponse.length;
            		if (len > 0) {
	            		for (var i = 0; i < len; i++) {
							html += "<tr>";
							html += "<td><a href=\"javascript:selectPost('" + data.postResponse[i].postUid + "')\">" + data.postResponse[i].postId + "</a></td>";
							html += "<td>" + data.postResponse[i].rankDesc + "</td>";
							html += "<td>" + data.postResponse[i].annualPlanDesc + "</td>";
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
				$("#tblMPRSSearchResult thead").after(html);
				
				var oTable = $('#tblMPRSSearchResult').dataTable();
				oTable.fnDestroy();
				
				$('#tblMPRSSearchResult').dataTable({
					"language": {
						"emptyTable":"No record found."
					}
				});
				$("#tblMPRSSearchResult").show();
			
				// Open the search result modal
				// $('#searchResultModel').modal('show');
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	}
	
	function selectPost(postUid) {
		showLoading();
		$.ajax({
            url: "<%=request.getContextPath() %>/api/request/getPostDetails",
            cache: false,
            type: "POST",
            data: {searchPostNo: postUid},
            success: function(data) {

            	//Position info
            	$('#txt_unit').val(data.unit);	
            	$("#postTitle").val(data.postTitle);
				$('#ddl_post_duration').val(data.postDuration);
				$('#txt_postStartDate').val(data.postStartDate);
				if (data.limitDurationType!=null && data.limitDurationType=="FIXED_END_DATE") {
					$("#rd_Duartion2").prop('checked', 'checked');
				}
					
				if (data.limitDurationType!=null && data.limitDurationType=="DURATION_PERIOD" ) {
					$("#rd_Duartion").prop('checked', 'checked');	
				}
				$('#txt_limit_duration_no').val(data.limitDurationNo);
				$('#ddl_limit_duration_unit').val(data.limitDurationUnit);
				$('#txt_post_actual_end_date').val(data.limitDurationEndDate);
				
				$('#ddl_post_remark').val(data.postRemark);
				$('#postFTE').val(data.postFTE);
				$('#postFTEValue').val(data.postFTEValue);
				$('#ddl_position_status').val(data.positionStatus);
				$('#dp_position_start_date').val(data.positionStartDate);
				$('#dp_position_end_date').val(data.positionEndDate);
				$('#txt_cluster_ref_no').val(data.clusterRefNo);
				$('#clusterRemark').val(data.clusterRemark);
		
				// Remove all row(s)
				var tableLength = $("#tblFunding tr").length;
				for (var i=1; i<tableLength; i++) {
					$($("#tblFunding tr")[0]).remove();
				}
				
				// Get Numberd of Funding Source
				var numberOfFundingSource = data.fundingList.length;
				
				// Add row
				for (var i=1; i<numberOfFundingSource; i++) {
					addNewFunding();
				}

				for (var i=0; i<numberOfFundingSource; i++) {
					$($("#tblFunding select[name$='annualPlanInd']")[i]).val(data.fundingList[i].annualPlanInd);
					$($("#tblFunding select[name$='programYear']")[i]).val(data.fundingList[i].programYear);
					$($("#tblFunding input[name$='programCode']")[i]).val(data.fundingList[i].programCode);
					$($("#tblFunding input[name$='programName']")[i]).val(data.fundingList[i].programName);
					$($("#tblFunding textarea[name$='fundSrcRemark']")[i]).val(data.fundingList[i].fundSrcRemark);
					$($("#tblFunding select[name$='programTypeCode']")[i]).val(data.fundingList[i].programTypeCode);
					$($("#tblFunding select[name$='fundSrcId']")[i]).val(data.fundingList[i].fundSrcId);
					$($("#tblFunding select[name$='fundSrcSubCatId']")[i]).val(data.fundingList[i].fundSrcSubCatId);
					$($("#tblFunding input[name$='fundSrcEndDate']")[i]).val(data.fundingList[i].fundSrcEndDate);
					$($("#tblFunding input[name$='fundSrcFte']")[i]).val(data.fundingList[i].fundSrcFte);
					$($("#tblFunding input[name$='inst']")[i]).val(data.fundingList[i].inst);
					$($("#tblFunding input[name$='section']")[i]).val(data.fundingList[i].section);
					$($("#tblFunding input[name$='analytical']")[i]).val(data.fundingList[i].analytical);
				}			
		
            	$('#ddl_res_sup_fr_ext').val(data.res_sup_fr_ext);
				$('#txt_res_sup_remark').val(data.res_sup_remark);
				
				$('#txt_proposed_post_id').val(data.proposed_post_id);
				
				$.ajax({
		            url: "<%=request.getContextPath() %>/hcm/getHCMPostDetail",
		            type: "POST",
		            data: {postId: data.hcmPositionId},
		            success: function(data) {
		            	$("#hiddenHcmDateEffective").val(data.hcmRecord.dateEffectiveStr);
						$("#relatedHcmEffectiveStartDate").html(data.hcmRecord.effectiveStartDateStr);
						$("#relatedHcmFTE").html(data.hcmRecord.fte);
						$("#relatedHcmHeadCount").html(data.hcmRecord.maxPerson);
						$("#relatedHcmPositionName").html(data.hcmRecord.name);
						$("#relatedHcmHiringStatus").html(data.hcmRecord.availabilityStatus);
						$("#relatedHcmType").html(data.hcmRecord.positionType);
					}
				});
				
				$("#ddl_cluster").val(data.clusterCode);
				$("#ddl_institution").val(data.instCode);
				$("#ddl_department").val(data.deptCode);
				$("#ddl_staff_group").val(data.staffGroupCode);
				$("#staffGroupDisplay").val(data.staffGroupCode);
				
				refreshDepartmentDropdown();
				
				// If is medical lblDepartment = "Department", otherwise "Specialty"
				var labelText = $("#lblDepartment").html();
				if ($("#ddl_staff_group").val() == "MEDICAL") {
					$("#lblDepartment").html(labelText.replace("Specialty", "Department"));
				}
				else {
					$("#lblDepartment").html(labelText.replace("Department","Specialty"));
				}
				
				// Refresh the sub-specialty list
				refreshSubSpecialty();
				
				$("#ddl_rank").val(data.rankCode);
				$("#hiddenHCMPositionId").val(data.hcmPositionId);
				
				// Update the program type
				$("#programType").val(data.programType);
				
				
				changeRankDropdown();
				// changeAnnualPlan();
				postDurationChange();
				
				changeCluster(data.instCode);
				
				// Close the search Dialog
				$("#searchMPRSModel").modal("hide");
				$("#searchPanel").hide();
				
				hideLoading();
				$("#divHCMDetail").show();
			}
		});
	}
	
	function clickDuration() {
		if ($("#rd_Duartion").prop("checked") == false) {
			$("#ddl_limit_duration_unit").val("");
			$("#txt_limit_duration_no").val("");
			
			$("#ddl_limit_duration_unit").attr("disabled", true);
			$("#txt_limit_duration_no").attr("disabled", true);
			
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('limitDurationUnit', false);
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('limitDurationNo', false);
			$("#frmDetail").bootstrapValidator('revalidateField', 'limitDurationUnit');
			$("#frmDetail").bootstrapValidator('revalidateField', 'limitDurationNo');
		}
		else {
			$("#ddl_limit_duration_unit").attr("disabled", false);
			$("#txt_limit_duration_no").attr("disabled", false);
			
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('limitDurationUnit', true);
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('limitDurationNo', true);
		}
		
		if ($("#rd_Duartion2").prop("checked") == false) {
			$("#txt_post_actual_end_date").val("");
			$("#txt_post_actual_end_date").attr("disabled", true);
			
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('limitDurationEndDate', false);
			$("#frmDetail").bootstrapValidator('revalidateField', 'limitDurationEndDate');
		}
		else {
			$("#txt_post_actual_end_date").attr("disabled", false);
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('limitDurationEndDate', true);
		}
		
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

	function changeHCMPosition() {
		$("#searchPanel").show();
	}
	
	var currentTarget = null;
	
	$(document).ready(function() {
		$.ajaxSetup({cache: false});
			
		// Init the data table
		$('#tblSearchResult').dataTable({
	       	"columnDefs": [ {
	          "targets": 'no-sort',
	          "orderable": false,
	    	}]
		});
		
		// Disable the field
		$("#requestId").prop("disabled", true);
		$("#requestStatus").prop("disabled", true);
		$("#txt_post_actual_start_date").attr("disabled", true);
		// $("#txt_proposed_post_id").attr("readonly", true);
		
		$("#postID").attr("disabled", true);
		$("#txt_proposed_post_id").attr("disabled", true);
		
		// For autocomplete
		$("#ddHcmPostTitle").autocomplete({
			source: function(request, response) {
				$.getJSON("<%= request.getContextPath() %>/hcm/getHCMPostTitleListByStaffGroup?staffGroup=" + $("#staffGroupDisplay option:selected").text(), request, function(result){
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
		
		$("#frmDetail").bootstrapValidator({
			excluded: [':disabled'],
			message: ' This value is not valid',
			live: "submitted",
			fields: {
				postFTEValue : {
					validators : {
						numeric: {
							message: 'Inputted value is invalid'
						},
						callback: {
							message: 'Inputted value is invalid',
							callback: function(value, validator, $field) {
								if ($("#postFTE").val() == "FULL_TIME") {
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
								else {
									if (value < 0.01 || value >= 1) {
										return {
											valid: false,
											message: 'Value should between 0.01 and 0.99'
										}
									}
									else {
										return true; 
									}
								}
							}
						}
					}
				},/* 
				fund_src_1st_fte : {
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
								if ($("#txt_fund_src_1st_start_date").val() == "") {
									return {
										valid: false,
										message: 'Please enter a value'
									}
								}
								
								if ($("#txt_fund_src_1st_start_date").val() == "" || $("#txt_fund_src_1st_end_date").val() == "") {
									return true;
								}
								
								var diff = getDaysBetween($("#txt_fund_src_1st_start_date").val(), $("#txt_fund_src_1st_end_date").val());
								
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
								if ($("#txt_fund_src_1st_end_date").val() == "") {
									if (endDateMandatory) {
										return {
											valid: false,
											message: 'Please enter a value'
										}
									}
								}
								
								if ($("#txt_fund_src_1st_start_date").val() == "" || $("#txt_fund_src_1st_end_date").val() == "") {
									return true;
								}			
								
								var diff = getDaysBetween($("#txt_fund_src_1st_start_date").val(), $("#txt_fund_src_1st_end_date").val());
								
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
								if ($("#txt_fund_src_2nd_start_date").val() == "" || $("#txt_fund_src_2nd_end_date").val() == "") {
									return true;
								}
							
								var diff = getDaysBetween($("#txt_fund_src_2nd_start_date").val(), $("#txt_fund_src_2nd_end_date").val());
								
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
								if ($("#txt_fund_src_2nd_start_date").val() == "" || $("#txt_fund_src_2nd_end_date").val() == "") {
									return true;
								}
							
								var diff = getDaysBetween($("#txt_fund_src_2nd_start_date").val(), $("#txt_fund_src_2nd_end_date").val());
								
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
								if ($("#txt_fund_src_3rd_start_date").val() == "" || $("#txt_fund_src_3rd_end_date").val() == "") {
									return true;
								}
							
								var diff = getDaysBetween($("#txt_fund_src_3rd_start_date").val(), $("#txt_fund_src_3rd_end_date").val());
								
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
								if ($("#txt_fund_src_3rd_start_date").val() == "" || $("#txt_fund_src_3rd_end_date").val() == "") {
									return true;
								}
							
								var diff = getDaysBetween($("#txt_fund_src_3rd_start_date").val(), $("#txt_fund_src_3rd_end_date").val());
								
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
				txtCopy: {
					validators : {
						integer: {
							message: 'Inputted value is invalid'
						},
						callback: {
							message: 'Inputted value is invalid',
							callback: function(value, validator, $field) {
								if (value < 1 || value > 20) {
									return {
										valid: false,
										message: 'Value should between 1 and 20'
									}
								}

								return true; 
							}
						}
					}
				}
			},
		})
		.on('error.field.bv', function(e, data) {
			hideLoading();
		})
		.on('success.form.bv', function(e){
			currentTarget = e.target;
			e.preventDefault();
			 
			if (!validateForm()) {
				hideLoading();
			}
			else {
				// Added for MPR0002 - Validation between Post Details and Funding Related Info
				if (newRequestFTEChecker()) {
				
					$("#frmDetail input[type='checkbox']").attr("disabled", false);
					$("#frmDetail input[type='text']").attr("disabled", false);
					$("#frmDetail input[type='radio']").attr("disabled", false);
					$("#frmDetail textarea").attr("disabled", false);
					$("#frmDetail select").attr("disabled", false);
					$("#frmDetail input[type='hidden']").attr("disabled", false);
					currentTarget.submit();
				}
				else {
					hideLoading();
					e.preventDefault();
				}
			}
		});
		
		var status = $("#requestStatus").val();
		if (status == "CONFIRMED" && status != "WITHDRAW") {
			$("select").prop("disabled", true);
			$("#model_nextWFStep select").prop("disabled", false);
			$("input[type='text']").prop("disabled", true);
			$("#model_editEmail input[type='text']").prop("disabled", false);
			$("#btnChangeHCMPosition").prop("disabled", true);
			$("#btnRequestNewPostId").prop("disabled", true);
		}
		
		if (status == "CONFIRMED") {
			$("#divGenInfoPostId").show();
			$("#divGenInfoProposedPostId").hide();
		} else {
			$("#divGenInfoPostId").hide();
			$("#divGenInfoProposedPostId").show();
		}
			
		changeRankDropdown();
		// changeAnnualPlan(true);
		changeAnnualPlanInit();
		postDurationChange();
		clickDuration();
		
		refreshDepartmentDropdown();
		
		$("#frmDetail").on("submit", function(){
			
		});
		
		changeCluster($("#ddl_institution").val());
		
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
				$("select").prop("disabled", true);
				$("#model_nextWFStep select").prop("disabled", false);
				$("input[type='text']").prop("disabled", true);
				$("#model_editEmail input[type='text']").prop("disabled", false);
				$("#btnAddPost").prop("disabled", true);
				$("button[name='btnRemove']").prop("disabled", true);
			</c:when>
		</c:choose>
		
		// Set Request ID to readonly instead of disable
		$("#requestId").prop("disabled", false);
		$("#requestId").attr("readonly", true); 
		$("#postID").prop("disabled", false);
		$("#postID").attr("readonly", true);
		$("#txt_proposed_post_id").prop("disabled", false);
		$("#txt_proposed_post_id").attr("readonly", true);
		
		// Disable the cluster and staff group drop down
		$("#ddl_cluster").prop("disabled", true);
		$("#ddl_staff_group").prop("disabled", true);
		
		// Refresh funding source header
		refreshFundingSourceLabel();
		
		$("#ddHcmPostTitle").attr("disabled", true);
		$("#ddHcmPostOrganization").attr("disabled", true);
		$("#hcmUnitTeam").attr("disabled", true);
		$("#ddHcmJob").attr("disabled", true);
		$("#ddHcmOrganization").attr("disabled", true);
		$("#hcmPositionName").attr("disabled", true);
		$("#btnSearchHCM").attr("disabled", true);
		
		<c:choose>
			<c:when test="${ formBean.postDuration != 'R' }">
				$("font[name='lblEndDateStar']").show();
			</c:when>
		</c:choose>
		
		if ($("#hidShortFallPostId").val() != "" && $("#shortFallPostId").val() == "") {
			var tmpShortFall = $("#hidShortFallPostId").val();
			// Add this to the select list
			$($("#shortFallPostId option")[1]).before("<option value='" + tmpShortFall + "'>" + tmpShortFall + "</option>");
			
			$("#shortFallPostId").val(tmpShortFall);
		}
	});
	
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
		
		// Fixed for ST08706
		sum = sum.toFixed(4);
		
		var totalFTE = parseFloat($("#postFTEValue").val()).toFixed(4);
		
		if (sum != totalFTE || showWarning) {
			$("#fteWarningTitle").html("Warning");
	        $("#fteWarningContent").html("Post FTE value is not equals to the sum of FTE under the Funding related information.");
	        $("#fteWarningModal").modal("show");
	        
	        return false;
		}
		
		return true;
	}
	
	function resumeSubmit() {
		showLoading();
		$("#frmDetail input[type='text']").attr("disabled", false);
		$("#frmDetail input[type='radio']").attr("disabled", false);
		$("#frmDetail textarea").attr("disabled", false);
		$("#frmDetail select").attr("disabled", false);
		$("#frmDetail input[type='hidden']").attr("disabled", false);
		currentTarget.submit();
	}
	
	function addNewFunding() {
		// Clone the last row
		var x = $("#tblFunding tr:last").clone();
		
		// Initial the value
		$(x).find("select").val("");
		$(x).find("input").val("");
		$(x).find("textarea").val("");
		
		// Remove the option for program type
		$(x).find("select[name$='programTypeCode']").empty();
        var option = "<option value=''> - Select - </option>";
        $(x).find("select[name$='programTypeCode']").append(option);
	
		// UT29997 Set default value for post funding start date  
		$(x).find("input[name$='fundSrcStartDate']").val($("#txt_postStartDate").val());
			
		$("#tblFunding tr:last").after(x);
	
		// Refresh the name of each component
		for (var i=0; i<$("#tblFunding tr").length; i++) {
			// Annual Plan
			$($("#tblFunding select[name$='annualPlanInd']")[i]).attr("name", "requestFundingList[" + i + "].annualPlanInd");
			$($("#tblFunding select[name$='annualPlanInd']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].annualPlanInd");		
			$("#frmDetail").bootstrapValidator('addField', $($("#tblFunding select[name$='annualPlanInd']")[i]));
			
			// Program Year
			$($("#tblFunding select[name$='programYear']")[i]).attr("name", "requestFundingList[" + i + "].programYear");
			$($("#tblFunding select[name$='programYear']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].programYear");		
			$("#frmDetail").bootstrapValidator('addField', $($("#tblFunding select[name$='programYear']")[i]));
			
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
			$("#frmDetail").bootstrapValidator('addField', $($("#tblFunding select[name$='fundSrcId']")[i]));
			
			// Start Date
			$($("#tblFunding input[name$='fundSrcStartDate']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcStartDate");
			$($("#tblFunding input[name$='fundSrcStartDate']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].fundSrcStartDate");		
			$("#frmDetail").bootstrapValidator('addField', $($("#tblFunding input[name$='fundSrcStartDate']")[i]));
			
			// End Date
			$($("#tblFunding input[name$='fundSrcEndDate']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcEndDate");

			// FTE			
			$($("#tblFunding input[name$='fundSrcFte']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcFte");
			$($("#tblFunding input[name$='fundSrcFte']")[i]).attr("data-bv-field", "requestFundingList[" + i + "].fundSrcFte");
			$("#frmDetail").bootstrapValidator('addField', $($("#tblFunding input[name$='fundSrcFte']")[i]));

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
		$("#frmDetail .input-group.date").datepicker({
	  		format: "dd/mm/yyyy",
	  		autoclose: true,
	  		daysOfWeekHighlighted: [0],
	  		todayHighlight: true,
	  		todayBtn: "linked"
	  	})
	  	.on('changeDate', function(e) {
  			if ($(this).next().hasClass('help-block')) {
  				$("#frmDetail").bootstrapValidator('revalidateField', $($(this).parent().find("input")[0]).attr("name"));
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
		$("#frmDetail").bootstrapValidator('removeField', $(currentRow.find("select[name$='annualPlanInd']")[0]));
		$("#frmDetail").bootstrapValidator('removeField', $(currentRow.find("select[name$='programYear']")[0]));
		$("#frmDetail").bootstrapValidator('removeField', $(currentRow.find("input[name$='fundSrcFte']")[0]));
		$("#frmDetail").bootstrapValidator('removeField', $(currentRow.find("select[name$='fundSrcId']")[0]));
		$("#frmDetail").bootstrapValidator('removeField', $(currentRow.find("input[name$='fundSrcStartDate']")[0]));
	
		currentRow.remove();
		
		for (var i=0; i<$("#tblFunding tr").length; i++) {
			$($("#tblFunding select[name$='annualPlanInd']")[i]).attr("name", "requestFundingList[" + i + "].annualPlanInd");
			$($("#tblFunding select[name$='programYear']")[i]).attr("name", "requestFundingList[" + i + "].programYear");
			$($("#tblFunding input[name$='programCode']")[i]).attr("name", "requestFundingList[" + i + "].programCode");
			$($("#tblFunding input[name$='programName']")[i]).attr("name", "requestFundingList[" + i + "].programName");
			$($("#tblFunding input[name$='programName']")[i]).attr("id", "requestFundingList" + i + "programName");
			$($("#tblFunding select[name$='programTypeCode']")[i]).attr("name", "requestFundingList[" + i + "].programTypeCode");
			$($("#tblFunding select[name$='fundSrcId']")[i]).attr("name", "requestFundingList[" + i + "].fundSrcId");
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
	
	function refreshFundingSourceLabel() {
		for (var m=0; m<$("label[name='lblFundingSource']").length; m++) { 
			$($("label[name='lblFundingSource']")[m]).text("Funding Source " + (m+1));
		}
	}
	
	function changeStaffGroupHCM() {
		if ($("#staffGroupDisplay").val() == "") {
			$("#ddHcmPostTitle").attr("disabled", true);
			$("#ddHcmPostOrganization").attr("disabled", true);
			$("#hcmUnitTeam").attr("disabled", true);
			$("#ddHcmJob").attr("disabled", true);
			$("#ddHcmOrganization").attr("disabled", true);
			$("#hcmPositionName").attr("disabled", true);
			$("#btnSearchHCM").attr("disabled", true);
			
			$("#ddHcmPostTitle").val("");
			$("#ddHcmPostOrganization").val("");
			$("#hcmUnitTeam").val("");
			$("#ddHcmJob").val("");
			$("#ddHcmOrganization").val("");
			$("#hcmPositionName").val("");
			$("#btnSearchHCM").val("");
		}
		else {
			$("#ddHcmPostTitle").attr("disabled", false);
			$("#ddHcmPostOrganization").attr("disabled", false);
			$("#hcmUnitTeam").attr("disabled", false);
			$("#ddHcmJob").attr("disabled", false);
			$("#ddHcmOrganization").attr("disabled", false);
			$("#hcmPositionName").attr("disabled", false);
			$("#btnSearchHCM").attr("disabled", false);
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
			
	function changeEndDate() {
		$("#frmDetail").bootstrapValidator('revalidateField', 'requestFundingList[0].fundSrcStartDate');
	}
	
</script>
<style>
.hidediv {
	display: none;
}

.clickable {
	cursor: pointer;
}
</style>

<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href='<c:url value="/home/home"/>'><i class="fa fa-home"></i>Home</a>
			> Request > New Post
		</div>
		<div id="divSuccess" style="display: none" class="alert-box-success">
			<div class="alert-box-icon-success">
				<i class="fa fa-check"></i>
			</div>
			<div id="divSuccessMsg" class="alert-box-content-success"></div>
		</div>
		<div id="divError" style="display: none" class="alert-box-danger">
			<div class="alert-box-icon-danger">
				<i class="fa fa-warning"></i>
			</div>
			<div id="divErrorMsg" class="alert-box-content-danger"></div>
		</div>

		<div class="section-title">
			<div class="title pull-left">
				<i class="fa fa-file-text-o"></i>New Post
			</div>
		</div>
		<form id="frmDetail" method="POST" enctype="multipart/form-data">
			<div class="panel panel-custom-primary">
				<div class="panel-heading">
					<div class="panel-heading-title">General Information</div>
				</div>
				<div class="panel-body">
					<form:hidden path="formBean.formAction" id="hiddenFormAction" />
					<form:hidden path="formBean.noOfCopySave" id="hiddenNoCopy" />
					<form:hidden path="formBean.currentRequestNo" id="hiddenRequestNo" />
					<form:hidden path="formBean.hcmPositionId" id="hiddenHCMPositionId" />
					<form:hidden path="formBean.relatedHcmDateEffective"
						id="hiddenHcmDateEffective" />
					<form:hidden path="formBean.canEditDetailInfo" />
					<form:hidden path="formBean.canEditFinancialInfo" />
					<form:hidden path="formBean.lastUpdateDate"
						id="hiddenLastUpdateDate" />
					<form:hidden path="formBean.currentRole" />
					<form:hidden path="formBean.postEndDate" />
					<div class="row">
						<div class="col-sm-2">
							<label for="requestId" class="control-label">Request ID</label>
						</div>
						<div class="col-sm-4">
							<form:input path="formBean.requestId" type="text"
								class="form-control " id="requestId" name="requestId"
								style="width:100%;"></form:input>
						</div>
						<div class="col-sm-2">
							<label for="request_status" class="control-label">Request Status</label>
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
								<label for="requester" class="control-label">Requester<font
									class="star">*</font></label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.requester" name="requester"
									class="form-control" required="required">
									<option value="">- Select -</option>
									<form:options items="${userList}" />
								</form:select>
							</div>
						</div>

						<div id="divGenInfoPostId" class="form-group"
							style="display: none">
							<div class="col-sm-2">
								<label for="post_ID" class="control-label">Post ID</label>
							</div>
							<div class="col-sm-4">
								<form:input path="formBean.postID" type="text"
									class="form-control" style="width:100%;"></form:input>
							</div>
						</div>

						<div id="divGenInfoProposedPostId" class="form-group"
							style="display: none">
							<div class="col-sm-2">
								<label for="post_ID" class="control-label">Proposed Post ID</label>
							</div>
							<div class="col-sm-4">
								<form:input path="formBean.proposed_post_id"
									id="txt_proposed_post_id" type="text" class="form-control"
									style="width:100%;"></form:input>
							</div>
						</div>
					</div>
					
					<div class="row" id="divShortFall">
						<div class="col-sm-6"></div>
					
						<div class="form-group">
							<div class="col-sm-2">
								<label for="requester" class="control-label">Shortfall Post</label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.shortFallPostId" name="shortFallPostId"
									class="form-control" >
									<option value="">- Select -</option>
									<form:options items="${shortFallIdList}" />
								</form:select>
								
								<input type="hidden" id="hidShortFallPostId" value="${formBean.shortFallPostId}"/>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div id="searchPanel"
				<c:if test="${formBean.updateSuccess == 'Y' || formBean.showDetail == 'Y'}"> style="display: none"</c:if>>
				<!-- Search Existing HCM Position Record -->
				<div class="panel panel-custom-primary">
					<div class="panel-heading">
						<div class="panel-heading-title">
							<a role="button" data-target="#searchCriteria" class="panel-title" data-toggle="collapse">Search Existing HCM Position Record</a>
						</div>
					</div>
					<div id="searchCriteria" class="panel-collapse collapse in">
						<div class="panel-body">
							<div class="row">
								<div class="form-group">
									<div class="col-sm-4">
										<label for="ddl_staff_group" class="control-label">Staff Group<font class="star">*</font>
										</label>
									</div>
									<div class="col-sm-8">
										<form:select path="formBean.staffGroupDisplay" id="staffGroupDisplay"
											name="staffGroupDisplay"
											class="form-control" style="width:100%;"
											onchange="changeStaffGroupHCM()">
											<form:option value="" label="- Select -" />
											<form:options items="${staffGroupList}" />
										</form:select>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-sm-4">
									<label for="position_title" class="field_request_label">Position Title/Generic Rank</label>
								</div>
								<div class="col-sm-8">
									<form:input path="formBean.ddHcmPostTitle" class="form-control"
										style="width:100%;" />
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
									<form:input path="formBean.hcmUnitTeam" class="form-control"
										style="width:100%;" />
								</div>
							</div>

							<div class="row">
								<div class="col-sm-4">
									<label for="position_title" class="field_request_label">Job</label>
								</div>
								<div class="col-sm-8">
									<form:input path="formBean.ddHcmJob" class="form-control"
										style="width:100%;" />
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
									<form:input path="formBean.hcmPositionName"
										class="form-control" style="width:100%;" />
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12" style="text-align: right">
									<button type="button" class="btn btn-primary" id="btnSearchHCM"
										style="width: 170px;" onclick="performHcmSearch()">
										<i class="fa fa-search"></i> Search
									</button>
									<button type="button" name="btnClone" class="btn btn-default"
										style="width: 170px;" onclick="cloneFromExisting()">Clone from existing Post</button>
								</div>
							</div>
						</div>
						<!-- ./panel-body  -->

					</div>
					<!-- #searchCriteria -->

				</div>
			</div>
			<!-- ./#searchPanel -->

			<div id="divHCMDetail"
				<c:if test="${formBean.updateSuccess != 'Y' && formBean.showDetail != 'Y'}"> style="display: none"</c:if>>
				<div id="HCMInfoDiv" class="panel panel-custom-primary">
					<div class="panel-heading">
						<div class="panel-heading-title">HCM Position Information</div>
					</div>
					<div class="panel-body" style="padding-top: 5px">
						<table class="table table-bordered mprs_table"
							style="border: solid 1px #DDD; margin-top: 5px">
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
											style="width:150px" id="relatedHcmEffectiveStartDate">
											<c:out value="${formBean.relatedHcmEffectiveStartDate}" />
										</form:label></td>
									<td style="width: 300px"><form:label
											path="formBean.relatedHcmPositionName" style="width:300px"
											id="relatedHcmPositionName">
											<c:out value="${formBean.relatedHcmPositionName}" />
										</form:label></td>
									<td style="width: 80px"><form:label
											path="formBean.relatedHcmFTE" style="width:80px"
											id="relatedHcmFTE">
											<c:out value="${formBean.relatedHcmFTE}" />
										</form:label></td>
									<td style="width: 80px"><form:label
											path="formBean.relatedHcmHeadCount" style="width:80px"
											id="relatedHcmHeadCount">
											<c:out value="${formBean.relatedHcmHeadCount}" />
										</form:label></td>
									<td style="width: 100px"><form:label
											path="formBean.relatedHcmHiringStatus" style="width:100px"
											id="relatedHcmHiringStatus">
											<c:out value="${formBean.relatedHcmHiringStatus}" />
										</form:label></td>
									<td style="width: 100px"><form:label
											path="formBean.relatedHcmType" style="width:100px"
											id="relatedHcmType">
											<c:out value="${formBean.relatedHcmType}" />
										</form:label></td>
									<td><button type="button" id="btnChangeHCMPosition"
											class="btn btn-primary" onclick="changeHCMPosition()">Change</button></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<!-- ./#HCMInfoDiv -->

				<div id="postInfoDiv" class="panel panel-custom-primary">
					<div class="panel-heading">
						<div class="panel-heading-title">Post Information</div>
					</div>
					<div class="panel-body" id="divPostInfo">
						<div class="row">
							<div class="form-group">
								<div class="col-sm-1">
									<label for="ddl_cluster" class="control-label">Cluster</label>
								</div>
								<div class="col-sm-3">
									<form:select path="formBean.cluster" id="ddl_cluster"
										required="required" name="ddl_cluster" class="form-control"
										style="width:100%;" onchange="changeCluster()">
										<form:option value="" label="- Select -" />
										<form:options items="${clusterList}" />
									</form:select>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-1">
									<label for="ddl_institution" class="control-label">Institution<font
										class="star">*</font></label>
								</div>
								<div class="col-sm-3">
									<form:select path="formBean.institution" id="ddl_institution"
										required="required" name="ddl_institution"
										class="form-control"
										onchange=" $('#txt_proposed_post_id').val('');"
										style="width:100%;">
										<form:option value="" label="- Select -" />
										<form:options items="${instList}" />
									</form:select>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="form-group">
								<div class="col-sm-1">
									<label class="control-label">Staff Group</label>
								</div>
								<div class="col-sm-3">
									<form:select path="formBean.staffGroup" id="ddl_staff_group"
										name="ddl_staff_group" class="form-control"
										style="width:100%;"
										onchange="changeStaffGroupHCM()">
										<form:option value="" label="- Select -" />
										<form:options items="${staffGroupList}" />
									</form:select>
								</div>
							</div>
						
							<div class="form-group">
								<div class="col-sm-1">
									<label for="ddl_department" class="control-label" id="lblDepartment">Department<font
										class="star">*</font></label>
								</div>
								<div class="col-sm-3">
									<form:select path="formBean.department" id="ddl_department"
										required="required" name="ddl_department" class="form-control"
										style="width:100%;"
										onchange=" $('#txt_proposed_post_id').val('');changeDepartment()">
										<form:option value="" label="- Select -" />
										<form:options items="${deptList}" />
									</form:select>
								</div>
							</div>
						
							<div class="form-group">
								<div class="col-sm-1">
									<label id="lblRank" for="ddl_rank" class="control-label">Rank<font
										class="star">*</font></label>
								</div>
								<div class="col-sm-3">
									<form:select path="formBean.rank" id="ddl_rank" name="ddl_rank"
										required="required" class="form-control" style="width:100%;"
										onchange="changeRankDropdown(); $('#txt_proposed_post_id').val('');">
										<form:option value="" label="- Select -" />
										<form:options items="${rankList}" />
									</form:select>
								</div>
							</div>
						</div>
					</div>
					<!-- ./#postInfoDiv -->

					<!-- Tab start-->
					<div id="tab_details" class="">
						<ul class="nav nav-pills">
							<li class="active"><a href="#tab1_position"
								data-toggle="tab">Post Details</a></li>
							<li><a href="#tab2_fund" data-toggle="tab">Funding Related Information</a></li>
							<li><a href="#tab3_resources" data-toggle="tab">Resources Support from External</a></li>
						</ul>

						<div class="tab-content clearfix">
							<div class="tab-pane active" id="tab1_position">
								<div class="row">
									<div class="col-sm-2">
										<label for="txt_unit" class="control-label">Unit</label>
									</div>
									<div class="col-sm-4">
										<form:input path="formBean.unit" type="text"
											class="form-control " id="txt_unit" name="txt_unit"
											maxlength="100"></form:input>
									</div>
								</div>
								<div class="row">
									<div class="form-group">
										<div class="col-sm-2">
											<label for="postTitle" class="control-label">Post Title<font class="star">*</font></label>
										</div>
										<div class="col-sm-4">
											<form:input path="formBean.postTitle" class="form-control"
												style="width:100%;" maxlength="100" required="required" />
										</div>
									</div>
									<div class="col-sm-2">
										<label id="lblSubSpecialty" for="subSpecialty"
											class="control-label">Sub-specialty</label>
									</div>
									<div class="col-sm-4">
										<form:select path="formBean.subSpecialty" class="form-control"
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
											<label for="ddl_duration" class="control-label">Duration<font
												class="star">*</font></label>
										</div>
										<div class="col-sm-2">
											<form:select path="formBean.postDuration" required="required"
												id="ddl_post_duration" name="ddl_post_duration"
												class="form-control" style="width:100%;"
												onchange="postDurationChange(); $('#txt_proposed_post_id').val('');">
												<form:option value="" label="- Select -" />
												<form:options items="${PostDurationList}" />
											</form:select>
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-2 nopadding">
											<label class="control-label">Start Date<font class="star">*</font></label>
										</div>
										<div class="col-sm-2">
											<div class="input-group date">
												<form:input path="formBean.postStartDate"
													id="txt_postStartDate" class="form-control"
													required="required"></form:input>
												<span class="input-group-addon"> <span
													class="glyphicon glyphicon-calendar"></span></span>
											</div>
										</div>
									</div>
									<div class="col-sm-2" id="lblDurationType">
										<label class="control-label">Duration</label>
									</div>
									<div class="col-sm-2 nopadding">
										<div class="form-inline form-group">
											<form:radiobutton path="formBean.limitDurationType"
												id="rd_Duartion" name="rd_Duartion" value="DURATION_PERIOD"
												onclick="clickDuration()" required="required"></form:radiobutton>
											<span class="form-space"></span>
											<form:input path="formBean.limitDurationNo" type="text"
												class="form-control" id="txt_limit_duration_no"
												name="txt_limit_duration_no" style="width:50px;"
												required="required"
												oninput="this.value=this.value.replace(/[^0-9]/g,'');"
												maxlength="3"></form:input>
											<span class="form-space"></span>
											<form:select path="formBean.limitDurationUnit"
												id="ddl_limit_duration_unit" name="ddl_limit_duration_unit"
												class="form-control" style="width:60%;" required="required">
												<form:option value="" label="- Select -" />
												<form:option value="Y" label="Year" />
												<form:option value="M" label="Month" />
											</form:select>
										</div>
									</div>
								</div>

								<div class="row clearfix">
									<div class="col-sm-4"></div>
									<div class="col-sm-2 nopadding">
										<label class="control-label">Actual Start Date</label>
									</div>
									<div class="col-sm-2">
										<form:input path="formBean.postActualStartDate" type="text"
											class="form-control " id="txt_post_actual_start_date"
											name="txt_post_actual_start_date" readonly="readonly"></form:input>
									</div>
									<div class="col-sm-2" id="lblDurationType2">
										<label class="control-label">Fixed End Date</label>
									</div>
									<div class="col-sm-2 nopadding">
										<div class="form-inline">
											<form:radiobutton path="formBean.limitDurationType"
												id="rd_Duartion2" name="rd_Duartion" value="FIXED_END_DATE"
												onclick="clickDuration()" required="required"></form:radiobutton>
											<span class="form-space"></span>
											<div class="form-group">
												<div class="input-group date" id="grpLimitDurationEndDate">
													<form:input path="formBean.limitDurationEndDate"
														type="text" class="form-control "
														id="txt_post_actual_end_date"
														name="txt_post_actual_end_date" required="required"></form:input>
													<span class="input-group-addon"> <span
														class="glyphicon glyphicon-calendar"></span></span>
												</div>
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
											name="ddl_post_remark" style="width:100%" maxlength="2000"></form:input>
									</div>
								</div>
								<div class="row">
									<div class="form-inline">
										<div class="col-sm-2">
											<label for="ddl_postFTE" class="control-label">FTE<font
												class="star">*</font></label>
										</div>
										<div class="col-sm-1">
											<div class="form-group">
												<form:select path="formBean.postFTE" class="form-control"
													onchange="changeFTE(); $('#txt_proposed_post_id').val('');"
													required="required">
													<form:option value="" label="- Select -" />
													<form:option value="FULL_TIME" label="Full Time" />
													<form:option value="PART_TIME" label="Part Time" />
												</form:select>
											</div>
										</div>
										<div class="col-sm-4">
											<div class="form-group">
												<form:input path="formBean.postFTEValue" type="text"
													required="required" class="form-control"
													style="width:50px;" step="0.01"
													oninput="this.value=this.value.replace(/[^0-9\.]/g,'');checkFTEvalue();"></form:input>
											</div>

											<label for="txt_post_FTE_value"><i>(No. of net
													working hours per weeks / 39)</i></label>
										</div>
										<div class="col-sm-3">
											
										</div>
										<!--  HO Buy Service -->
										<div class="col-sm-2">
											<form:checkbox path="formBean.hoBuyServiceInd" value="Y"/> <label>HO Buy Service</label>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-2">
										<label for="ddl_position_status" class="control-label">Post
											Status</label>
									</div>
									<div class="col-sm-2">
										<form:select path="formBean.positionStatus"
											id="ddl_position_status" name="ddl_position_status"
											class="form-control" style="width:80%;">
											<!-- <form:option value="" label="- Select -" /> -->
											<form:options items="${postStatusList}" />
										</form:select>
									</div>
									<div style="display: none">
										<div class="col-sm-2">Start Date</div>
										<div class="col-sm-2">
											<div class='input-group date' id='position_start_date'>
												<form:input path="formBean.positionStartDate" type="text"
													class="form-control " id="dp_position_start_date"
													name="dp_position_start_date"></form:input>
												<span class="input-group-addon"> <span
													class="glyphicon glyphicon-calendar"></span></span>
											</div>
										</div>
										<div class="col-sm-2">End Date</div>
										<div class="col-sm-2">
											<div class='input-group date' id='position_end_date'>
												<form:input path="formBean.positionEndDate" type="text"
													class="form-control " id="dp_position_end_date"
													name="dp_position_end_date"></form:input>
												<span class="input-group-addon"> <span
													class="glyphicon glyphicon-calendar"></span></span>
											</div>
										</div>
									</div>
								</div>
								<div class="delimiter"></div>
								<div class="row">
									<div class="col-sm-2">
										<label for="txt_cluster_ref_no" class="control-label_opt"><strong>Cluster
												Reference No.</strong></label>
									</div>
									<div class="col-sm-5">
										<form:input path="formBean.clusterRefNo" type="text"
											class="form-control " id="txt_cluster_ref_no"
											name="txt_cluster_ref_no" style="width:100%" maxlength="100"></form:input>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-2">
										<label for="txt_clusterRemark" class="control-label_opt">Additional
											Remarks from Cluster</label>
									</div>
									<div class="col-sm-7">
										<form:textarea path="formBean.clusterRemark" type="text"
											class="form-control" id="clusterRemark" name="clusterRemark"
											style="width:100%;height:60px" maxlength="2000"></form:textarea>
									</div>
								</div>
							</div>
							<!-- ./#tab1_position -->
							<div class="tab-pane" id="tab2_fund">
								<!-- New Funding Source - Start -->

								<table id="tblFunding" style="width: 100%">
									<c:forEach var="listValue"
										items="${formBean.requestFundingList}" varStatus="pStatus">
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

														<c:choose>
															<c:when test="${formBean.canEditFinancialInfo != 'Y'}">
																<div class="col-sm-2">
																	<form:select
																		path="formBean.requestFundingList[${pStatus.index}].annualPlanInd"
																		class="form-control" style="width:100%;"
																		required="required">
																		<form:option value="" label="- Select -" />
																		<form:option value="Y" label="Yes" />
																		<form:option value="N" label="No" />
																	</form:select>
																</div>
															</c:when>
														</c:choose>

														<c:choose>
															<c:when test="${formBean.canEditFinancialInfo == 'Y'}">
																<div class="col-sm-2">
																	<form:select
																		path="formBean.requestFundingList[${pStatus.index}].annualPlanInd"
																		class="form-control"
																		style="width:100%;"
																		onchange="changeAnnualPlanByRow(this);$('#txt_proposed_post_id').val('');"
																		required="required">
																		<form:option value="" label="- Select -" />
																		<form:option value="Y" label="Yes" />
																		<form:option value="N" label="No" />
																	</form:select>
																</div>
															</c:when>
														</c:choose>
													</div>
													<!-- Annual Plan - End -->

													<!-- Year - Start -->
													<div class="form-group">
														<div class="col-sm-2">
															<label class="control-label">Year
															<font name="starYear" class="star">*</font>
															</label>
														</div>
														<div class="col-sm-2">
															<form:select
																path="formBean.requestFundingList[${pStatus.index}].programYear"
																class="form-control" style="width:100%;" 
																required="required">
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
																path="formBean.requestFundingList[${pStatus.index}].programCode"
																type="text" class="form-control" name="txt_program_code"
																style="width:100%" maxlength="50"
																required="required"></form:input>
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
																path="formBean.requestFundingList[${pStatus.index}].programName"
																type="text" class="form-control" style="width:100%"
																maxlength="500"
																required="required"></form:input>
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
																path="formBean.requestFundingList[${pStatus.index}].programTypeCode"
																class="form-control" style="width:100%;"
																required="required"
																onchange="$('#txt_proposed_post_id').val('');" >
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
																path="formBean.requestFundingList[${pStatus.index}].fundSrcId"
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
																path="formBean.requestFundingList[${pStatus.index}].fundSrcSubCatId"
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
																	path="formBean.requestFundingList[${pStatus.index}].fundSrcStartDate"
																	type="text" class="form-control " style="width:100%"
																	required="required"></form:input>
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
															<label for="" class="controlnameel">End Date
															<font name="lblEndDateStar"
															   	  class="star" 
															   	  style='display:none'>*</font>
															</label>
														</div>
														<div class="col-sm-2">
															<div class="input-group date"
																id="dp_fund_src_1st_end_date">
																<form:input
																	path="formBean.requestFundingList[${pStatus.index}].fundSrcEndDate"
																	type="text" class="form-control " style="width:100%"
																	onchange="changeEndDate()"></form:input>
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
																path="formBean.requestFundingList[${pStatus.index}].fundSrcFte"
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
																	path="formBean.requestFundingList[${pStatus.index}].inst"
																	type="text" class="form-control" style="width:100%"
																	maxlength="3"
																    data-bv-excluded="true"></form:input>
															</div>

															<div class="col-sm-1">
																<label class="control-label">Section</label>
															</div>
															<div class="col-sm-2">
																<form:input
																	path="formBean.requestFundingList[${pStatus.index}].section"
																	type="text" class="form-control" style="width:100%"
																	maxlength="7"
																    data-bv-excluded="true"></form:input>
															</div>

															<div class="col-sm-1">
																<label class="control-label">Analytical</label>
															</div>
															<div class="col-sm-2">
																<form:input
																	path="formBean.requestFundingList[${pStatus.index}].analytical"
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
														<form:textarea path="formBean.requestFundingList[${pStatus.index}].fundSrcRemark" 
															type="text" class="form-control" 
															style="width:80%;height:60px" maxlength="2000"
															data-bv-excluded="true"></form:textarea>
													</div>
													<!--  Remark - End -->
												</div>
											</td>
											
										</tr>
									</c:forEach>
								</table>
								<br />
								<div style="text-align:right">
									<button type="button" class="btn btn-primary" style="width: 180px" onclick="addNewFunding()" name='btnAddFunding'>
										<i class="fa fa-plus"></i> Add Funding Source
									</button>
								</div>

							</div>
							<!-- ./#tab2_fund-->
							<div class="tab-pane" id="tab3_resources">
								<div class="row">
									<div class="col-sm-3">
										<label for="ddl_res_sup_fr_ext" class="control-label">Resources support from external</label>
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
										<label for="txt_res_sup_remark" class="control-label">Remark</label>
									</div>
									<div class="col-sm-6">
										<form:input path="formBean.res_sup_remark" type="text"
											class="form-control " id="txt_res_sup_remark"
											name="txt_res_sup_remark" style="width:100%" maxlength="2000"></form:input>
									</div>
								</div>
							</div>
							<!-- ./#tab3_resources-->
						</div>
					</div>
					<!-- Tab end-->

				</div>
				<!-- ./#postInfoDiv -->


				<%@ include file="/WEB-INF/views/request/common_approval.jsp"%>

				<!-- Modal massSave-->
				<div id="massSave" class="modal fade" role="dialog">
					<div class="modal-dialog modal-dialog-custom modal-dialog-postId"
						style="width: 900px">
						<!-- Modal content-->
						<div class="modal-content">
							<div class="modal-header">
								<h4>
									<b>Multiple Creation</b>
									<button type="button" class="close" data-dismiss="modal"
										aria-hidden="true" aria-label="Close">&times;</button>
								</h4>
							</div>
							<div class="modal-body">
								<div class="row" style="padding: 20px;">
									<div class="form-group">
										<div class="col-sm-12">
											<label for="" class="control-label">No. of copies to
												be created (Max.: 20 copies)</label>
										</div>
										<div class="col-sm-12">
											<input type="text" class="form-control" id="txtCopy"
												name="txtCopy" disabled />
										</div>
									</div>
								</div>
							</div>
							<div class="modal-footer">
								<button type="submit" class="btn btn-primary"
									style="width: 130px" onclick="submitMassSave()">
									<i class="fa fa-check"></i> Create
								</button>
								<button type="button" class="btn btn-default"
									style="width: 130px" onclick="closeMassSave()">
									<i class="fa fa-times"></i> Cancel
								</button>
							</div>
						</div>
					</div>
				</div>
				<!-- ./Modal #massSave -->
			</div>
			<!-- ./#divHCMDetail -->
		</form>
	</div>
	<!-- ./#container -->
</div>
<!-- ./#page-content-wrapper -->

<!--  Search Result Model -->
<div id="searchResultModel" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom modal-dialog-postId"
		style="width: 980px">
		<div class="modal-content">
			<div class="modal-header">
				<h4>
					<b>Search Result - Existing HCM Position Record</b>
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" aria-label="Close">&times;</button>
				</h4>
			</div>
			<div class="modal-body">
				<table id="tblSearchResult"
					class="table table-striped table-bordered" style="width: 950px">
					<thead>
						<tr>
							<th style="width: 120px">Effective Date</th>
							<th style="width: 400px">Position name</th>
							<th style="width: 80px">FTE</th>
							<th style="width: 80px">Headcount</th>
							<th style="width: 100px">Hiring Status</th>
							<th style="width: 100px">Type</th>
							<th style="width: 100px" class="no-sort"></th>
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
<!-- ./#searchResultModel -->

<!--  Search Result Model - Start -->
<div id="searchMPRSModel" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width: 980px">
		<div class="modal-content">
			<div class="modal-header">
				<h4>
					<b>Search Post</b>
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" aria-label="Close">&times;</button>
				</h4>
			</div>
			<div class="modal-body">
				<form id="searchPosition">
					<fieldset class="scheduler-border"
						style="margin-top: 0px; margin-bottom: 0px">
						<div class="row">
							<div class="col-sm-2">
								<label class="field_request_label">Cluster</label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.searchClusterId"
									name="searchClusterId" class="form-control"
									onchange="changeSearchCluster()">
									<option value="">- Select -</option>
									<form:options items="${clusterList}" />
								</form:select>
							</div>
							<div class="col-sm-2">
								<label class="field_request_label">Institution</label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.searchInstId" name="searchInstId"
									class="form-control">
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
								<form:select path="formBean.searchDeptId" name="searchDeptId"
									class="form-control" onchange="changeSearchDept()">
									<option value="">- Select -</option>
									<form:options items="${deptList}" />
								</form:select>
							</div>
							<div class="col-sm-2">
								<label class="field_request_label">Staff Group</label>
							</div>
							<div class="col-sm-4">
								<form:select path="formBean.searchStaffGroupId"
									name="searchStaffGroupId" class="form-control" onchange="changeSearchStaffGroup()">
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
								<form:select path="formBean.searchRankId" name="searchRankId"
									class="form-control">
									<option value="">- Select -</option>
									<form:options items="${rankList}" />
								</form:select>
							</div>
							<div class="col-sm-2">
								<label class="field_request_label">Post ID</label>
							</div>
							<div class="col-sm-4">
								<form:input path="formBean.searchPostId" name="searchPostId"
									class="form-control" />
							</div>
						</div>

						<div class="row" style="text-align: right">
							<div class="col-sm-12">
								<button type="button" class="btn btn-primary"
									style="width: 130px;" onclick="performPostSearch()">
									<i class="fa fa-search"></i> Search
								</button>
							</div>
						</div>
					</fieldset>
				</form>
				<br />
				<table id="tblMPRSSearchResult"
					class="table table-striped table-bordered">
					<thead>
						<tr>
							<th>Post ID</th>
							<th>Rank</th>
							<th>Annual Plan</th>
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
</div>
<!-- ./#searchMPRSModel -->

<!-- Warning Model -->
<div id="fteWarningModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-custom" style="width: 600px">
		<div class="modal-content">
			<div class="modal-header" style="background-color: #f39c12">
				<h4>
					<b><span id="fteWarningTitle"></span></b>
				</h4>
			</div>
			<div class="modal-body">
				<div id="fteWarningContent"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" style="width: 100px"
					data-dismiss="modal" onclick="resumeSubmit()">OK</button>
			</div>
		</div>
	</div>
</div>
<!-- ./#warningModal -->

<c:choose>
	<c:when test="${ formBean.canEditDetailInfo != 'Y'}">
		<script>
			$(function(){
				$("#tab1_position input").attr("disabled", true);
				$("#tab1_position textarea").attr("readonly", true);
				$("#tab1_position select").attr("disabled", true);
				
				$("#tab3_resources input").attr("disabled", true);
				$("#tab3_resources select").attr("disabled", true);
				
				$("#divApproval input").attr("disabled", true);
				$("#model_editEmail input[type='text']").prop("disabled", false);
				$("#divApproval input[type='file']").attr("disabled", true);
				$("#divApproval button[name='btnUploadFile']").attr("disabled", true);
				$("#divApproval button[name='btnAddUploadFile']").attr("disabled", true);
				$("#divApproval button[name='btnRemoveFile']").attr("disabled", true);
				$("#requester").attr("disabled", true);
				$("#divPostInfo select").attr("disabled", true);
				$("#btnChangeHCMPosition").attr("disabled", true);
			});
		</script>
	</c:when>
</c:choose>
<c:choose>
	<c:when test="${ formBean.showEditShortFall == 'Y' }">
		<script>
			$(function(){
				$("#divShortFall").show();
			});
		</script>
	</c:when>
</c:choose>

<c:choose>
	<c:when test="${ formBean.showEditShortFall != 'Y'}">
		<script>
			$(function(){
				$("#divShortFall").hide();
			});
		</script>
	</c:when>
</c:choose>

<c:choose>
	<c:when test="${ formBean.canEditDetailInfo == 'Y'}">
		<script>
			$(function(){
				if ($("#hiddenHCMPositionId").val() != "") {
					$.ajax({
						url: "<%=request.getContextPath()%>/hcm/getHCMPostDetail",
				        type: "POST",
				        data: {postId: $("#hiddenHCMPositionId").val()},
				        success: function(data) {
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
						}
					});
				}
			});
		</script>
	</c:when>
</c:choose>
<c:choose>
	<c:when test="${ formBean.canEditFinancialInfo != 'Y'}">
		<script>
			$(function(){
				// Disable the bootstrap validator for tab 2
				// $("#frmDetail").data('bootstrapValidator').enableFieldValidators('ddl_annual_plan_ind', false);
				
				/*$("#frmDetail").data('bootstrapValidator').enableFieldValidators('fund_src_1st', false);
				$("#frmDetail").data('bootstrapValidator').enableFieldValidators('fund_src_1st_start_date', false);
				$("#frmDetail").data('bootstrapValidator').enableFieldValidators('fund_src_1st_end_date', false);			
				$("#frmDetail").data('bootstrapValidator').enableFieldValidators('fund_src_1st_end_date', false);
				
				$("#frmDetail").data('bootstrapValidator').enableFieldValidators('program_name', false);
				$("#frmDetail").data('bootstrapValidator').enableFieldValidators('program_year', false);
				$("#frmDetail").data('bootstrapValidator').enableFieldValidators('program_code', false);*/
	
				$("#tab2_fund input").attr("disabled", true);
				$("#tab2_fund select").attr("disabled", true);
				$("#tab2_fund textarea").attr("disabled", true);
				$("#tab2_fund button").attr("disabled", true);
				$("#tab2_fund button[name='btnRemoveFunding']").attr("disabled", true);
				$("#tab2_fund button[name='btnAddFunding']").attr("disabled", true);
			});
		</script>
	</c:when>
</c:choose>
<c:choose>
	<c:when test="${ formBean.userHaveSaveRight != 'Y' }">
		<script>
			$(function(){
				$("#divApproval input").attr("disabled", true);
				$("#model_editEmail input[type='text']").prop("disabled", false);
				$("#divApproval input[type='file']").attr("disabled", true);
				$("#divApproval input[name='btnUploadFile']").attr("disabled", true);
				$("#divApproval input[name='btnAddUploadFile']").attr("disabled", true);
				$("#requester").attr("disabled", true);
				$("#postInfoDiv select").attr("disabled", true);
				$("#divHCMDetail input[type='button']").attr("disabled", true);
				
				$("input[type='file']").prop("disabled", true);
				$("button[name='btnUploadFile']").attr("disabled", true);
				$("button[name='btnRemoveFile']").attr("disabled", true);
				
				$("#shortFallPostId").attr("disabled", true);
			});
		</script>
	</c:when>
</c:choose>

<%@ include
	file="/WEB-INF/views/request/common_MPRPost_detail_newPost.jsp"%>

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>
