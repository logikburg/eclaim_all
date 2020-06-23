<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
 	var orgProposedEndDate;
 	
	function returnToHome() {
		document.location.href = "<%= request.getContextPath() %>/home/home";
	}
	
	function confirmChangeMode() {
		showLoading();
		$("#hiddenFormAction").val("CORRECTION");
		
		if (currentTarget != null) {
           	currentTarget.submit();
        }
        else {
        	$("#btnSubmitSave").click();
        }
	}		
	
	function hiddenMessage() {
		$(".alert").hide();
		$("#divHCMDetail").hide();
	}

	function performSearch() {
		// Check is any criteria inputted
		if ($("#hcmJob").val() == "" &&   
			$("#hcmPostTitle").val() == "" && 
			$("#hcmOrganization").val() == "" && 
			$("#hcmPostOrganization").val() == "" && 
			$("#hcmUnitTeam").val() == "" &&
			$("#hcmPositionName").val() == "") {
		
			$("#warningModal #warningTitle").html("Warning");
            $("#warningModal #warningContent").html("Please select at least one criteria.");
            $("#warningModal").modal("show");
            
            return;
		}
	
		hiddenMessage();
		showLoading();
		// Ajax call to perform search
		$.ajax({
            url: "<%= request.getContextPath() %>/api/hcm/searchHcmPositionForHcmFunction",
            cache: false,
			type: "POST",
			dataType: 'json',
            data: $("#frmSearch").serialize(),
            success: function(data) {
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
						html += "onclick=\"javascript:selectHcmPositionDetail('" + data.hcmPositionResponse[i].positionId + "')\">Select</button></td>";
						html += "</tr>";
            		}
            		html += "</tbody>";
            		
            		// Append row to search result
					$("#tblSearchResult thead").after(html);
					
            	}
				
				var oTable = $('#tblSearchResult').dataTable();
				oTable.fnDestroy();
				$('#tblSearchResult').dataTable({
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
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	}
	
	function showUpdateFTE() {
		$('#updateFTEModel').modal('show');
	}
	
	function performUpdateFTE() {
		if ($("#updatedFTEReason").val() == "") {
			$('#reasonMissingModal').modal('show');
		}
		else {
			// Update the FTE
			$("#txtFTE").val($("#updatedFTE").val());
			$('#updateFTEModel').modal('hide');
		}
	}
	
	function selectHcmPositionDetail(postId) {
		// Hide the search result model and collapse the search panel
		$('#searchCriteria').collapse('hide');
		$('#searchResultModel').modal('hide');
		showLoading();
		
		// Ajax call to perform search
		$.ajax({
            url: "<%= request.getContextPath() %>/api/hcm/getHcmPositionDetailWithEffectiveDate",
            cache: false,
            type: "POST",
            data: {postId:postId, hcmEffectiveDate: $("#hcmEffectiveDate").val()},
            success: function(data) {
            	$("#hiddenVersionNo").val(data.hcmRecord.versionNumber);
            	$("#hiddenPositionId").val(data.hcmRecord.positionId);
            	
            	$("#txtEffectiveFromDate").val(data.hcmRecord.effectiveStartDateStr);
            	$("#hiddenEffectiveFromDate").val(data.hcmRecord.effectiveStartDateStr);
            	$("#txtStartDate").val(data.hcmRecord.positionStartDateStr);
            	$("#txtProposedEndDate").val(data.hcmRecord.hiringStatusPropEndDateStr);
            	orgProposedEndDate = $("#txtProposedEndDate").val(); 
            	
            	$("#lblPostName").html(data.hcmRecord.name);
            	$("#hiddenPostName").val(data.hcmRecord.name);
            	$("#postTitle").val(data.hcmRecord.positionTitle);
            	$("#postOrganization").val(data.hcmRecord.postOrganizationId);
            	$("#unitTeam").val(data.hcmRecord.unitTeam); 
            	$("#organization").val(data.hcmRecord.organizationId);
            	$("#job").val(data.hcmRecord.jobId);
            	
            	$("#ddPostTitle").val(data.hcmRecord.positionTitle);
            	$("#ddPostOrganization").val(data.hcmRecord.positionOrganization);
            	$("#ddOrganization").val(data.hcmRecord.organizationName);
            	$("#ddJob").val(data.hcmRecord.jobName);
            	/*if (data.hcmRecord.availabilityStatusId == 1) {
            		$("#hiringStatus").val("Active");
            	}
            	else {
            		$("#hiringStatus").val("Proposed");
            	}*/
            	$("#hiringStatus").val(data.hcmRecord.availabilityStatus);
            	if (data.hcmRecord.positionType == "SHARED") {
            		$("#type1").prop("checked", true);
            	}
            	else if (data.hcmRecord.positionType == "NONE") {
            		$("#type3").prop("checked", true);
            	}
            	else {
            		$("#type2").prop("checked", true);
            	}
            	$("#txtEndDate").val(data.hcmRecord.effectiveEndDateStr);
            	
            	$("#txtFTE").val(data.hcmRecord.fte);
            	$("#txtHeadCount").val(data.hcmRecord.maxPerson);
            	$("#txtProbationDuration").val(data.hcmRecord.porbationPeriod);
            	$("#txtProbationDurationUnit").val(data.hcmRecord.probationPeriodUnitCd);
            	
            	// Clear the valid grade table first
            	var validGradeCnt = $("#tblValidGrade tbody tr").length;
            	for (var m=0; m<validGradeCnt; m++) {
            		$($("#tblValidGrade tbody tr")[validGradeCnt - m]).remove();
            	}
            	
            	if (data.hcmGradeListSize > 0) {
	            	for (var i=0; i<data.hcmGradeListSize; i++) {
	            		if (i!=0) {
	            			var x = $("#tblValidGrade tbody tr:last").clone();
							$("#tblValidGrade tbody tr:last").after(x);
	            		} 	
	            		$($("input[name='grade']")[i]).val(data.hcmGradeList[i].gradeId);
	            		$($("input[name='ddGrade']")[i]).attr("readonly", true);
	            		$($("input[name='ddGrade']")[i]).val(data.hcmGradeList[i].gradeDesc);
	            		$($("input[name='gradeDateFrom']")[i]).val(data.hcmGradeList[i].dateFromStr);
	            		$($("input[name='gradeDateTo']")[i]).val(data.hcmGradeList[i].dateToStr);
	            		$($("input[name='gradeUid']")[i]).val(data.hcmGradeList[i].gradeUid);
	            		$($("input[name='versionNumber']")[i]).val(data.hcmGradeList[i].versionNumber);
	            	}
	            }
	            
	            $("#location").val(data.hcmRecord.locationId);
	            $("#positionGroup").val(data.hcmRecord.positionGroup);
	            $("#srcFunding").val(data.hcmRecord.srcFunding);
	            $("#ddLocation").val(data.hcmRecord.locationDesc);
	            
	            $("#effectiveFromDisplay").val(data.hcmRecord.effectiveStartDateStr);
	            $("#effectiveToDisplay").val(data.hcmRecord.effectiveEndDateStr);
	            
	            $("#txtHiringStatusStartDate").val(data.hcmRecord.hiringStatusStartDateStr);

				$("#lastRecord").val(data.lastRecord);
				
				$("#updatedFTE").val("");
				
				hiringStatusChange();

            	hideLoading();
            
		        $("#divHCMDetail").show();
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
	}
	
	function parseDate(str) {
		var mdy = str.split("/");
		return new Date(mdy[2], mdy[1]-1, mdy[0]);
	}
	
	function daysBetween(date1, date2) {
		return Math.round((parseDate(date1)-parseDate(date2))/ (1000*60*60*24));
	}

	function performValidation() {
		var errMsg = "";
		
		// Perform checking on valid grade date field
		for (var i=0; i<$("input[name='gradeDateFrom']").length; i++) {
			if ($($("input[name='ddGrade']")[i]).val() != "") {
				if ($($("input[name='gradeDateFrom']")[i]).val() == "") {
					errMsg = "From date for Valid Grade cannot be empty." 
					break;
				}
		
				if (daysBetween($($("input[name='gradeDateFrom']")[i]).val(), $("#txtStartDate").val()) < 0) {
					errMsg = "From/To date for Valid Grade should on or after position start date." 
					break;
				}	
			
				if ($($("input[name='gradeDateTo']")[i]).val() != "") {
					if (daysBetween($($("input[name='gradeDateTo']")[i]).val(), $("#txtStartDate").val()) < 0) {
						errMsg = "From/To date for Valid Grade should on or after position start date." 
						break;
					}
				}
			}
		}
		
		if (errMsg == "") {
			return true;
		}
		else {
			hideLoading();
			$("#divErrorMsg").html(errMsg);
			$($("div[class='alert-box-icon-danger']")[0]).css("height", $("#divError").height())
			$("#divError").show();
			
			$('html,body').animate({ scrollTop: 0 }, 'slow');
			
			return false;
		}
	}
	
	function performSave() {
		// Hide the success/error message
		$("#divError").hide();
		$("#divSuccess").hide();
	
		var validator = $("#frmDetail").data('bootstrapValidator');
		validator.validate();
		if (!validator.isValid() || !performValidation()) {
			return;
		}
		
		// Compare the effective date with the effective end date
		if (daysBetween($("#txtEffectiveFromDate").val(), $("#effectiveToDisplay").val()) > 0 ||
		daysBetween($("#txtEffectiveFromDate").val(), $("#effectiveFromDisplay").val()) < 0) {
			$("#invalidEffectiveDate").modal("show");
			return;
		}	
		
		if ($("#hiddenEffectiveFromDate").val() == $("#txtEffectiveFromDate").val()) {
			$("#divWithoutEffectiveDateChange").show();
			$("#divWithEffectiveDateChange").hide();
		}
		else {
			$("#divWithoutEffectiveDateChange").hide();
			$("#divWithEffectiveDateChange").show();
		}
		
		// If Headcount = 0, check is there any post related to this HCM record.
		if ($("#hiringStatus").val() == 'Frozen' && $("#txtHeadCount").val() == 0) {
			var jqResponse = $.ajax({
					            url: "<%=request.getContextPath() %>/hcm/getZeroHeadcountInd",
					            type: "POST",
					            async: false,
					            data: {positionId: $("#hiddenPositionId").val(), effectiveFromDate: $("#txtEffectiveFromDate").val(), effectiveToDate: $("#effectiveToDisplay").val()},
					            success: function(data) {
								}
							});
			
			if (jqResponse.responseText == "N") {
				$("#modelMPRSPostFound").modal("show");
				return;
			}
		}
		
		$("#updateModeModel").modal("show");
	}
	
	function checkUpdate() {
		if ($("#lastRecord").val() == "Y") {
			performUpdate(); 
		}
		else {
			$("#updateModeModel2").modal("show");	
		}
	}
	
	function performUpdate() {
		$("#hiddenFormAction").val("UPDATE");
		
		showLoading();
		
		// Check the responsibility
		$.ajax({
            url: "<%=request.getContextPath() %>/hcm/searchResponsibility",
            type: "POST",
            data: {userId: '<c:out value="${formBean.userId}"/>'},
            success: function(data) {
            	hideLoading();
            
            	if (data.length == 0) {
            		$("#divErrorMsg").html("You do not have responsibility to create HCM record.");
					$("#divError").show();
			
					$('html,body').animate({ scrollTop: 0 }, 'slow');
            	} 
            	else if (data.length == 1) {
            		$("#repsonsibilityId").val(data[0].responsibilityName);
            		if ($("#hiddenEffectiveFromDate").val() == $("#txtEffectiveFromDate").val()) {
						// performCorrection(); 
						showCorrectionMessage();
					} 
					else {
            			if (currentTarget != null) {
	           				currentTarget.submit();
	           			}
	           			else {
	           				$("#btnSubmitSave").click();
	           			}
           				showLoading();
            		}
            	}
            	else {
            		$("#tblResponsibility tbody").empty()
            		var html = "";
            		for (var i=0; i<data.length; i++) {
            			html += "<tr>";
            			html += "<td>";
            			html += "<div class='row'><button type='button' style='width:200px' class='btn btn-primary' onclick='confirmSave(\"" + data[i].responsibilityName + "\")'>" + data[i].responsibilityName + "</button></div>";
            			html += "</td>";
            			html += "</tr>";
            		
            		}
            		$("#tblResponsibility tbody").append(html);
            		$("#responsibilityModal").modal("show");
            	}
			}
		});
	}
	
	function performCorrection() {
		$("#hiddenFormAction").val("CORRECTION");
		
		showLoading();

		// Check the responsibility
		$.ajax({
            url: "<%=request.getContextPath() %>/hcm/searchResponsibility",
            type: "POST",
            data: {userId: '<c:out value="${formBean.userId}"/>'  },
            success: function(data) {
            	hideLoading();
            	if (data.length == 0) {
            		$("#divErrorMsg").html("You do not have responsibility to create HCM record.");
					$("#divError").show();
			
					$('html,body').animate({ scrollTop: 0 }, 'slow');
            	} 
            	else if (data.length == 1) {
            		$("#repsonsibilityId").val(data[0].responsibilityName);
            		if (currentTarget != null) {
           				currentTarget.submit();
           			}
           			else {
           				$("#btnSubmitSave").click();
           			}
            		showLoading();
            	}
            	else {
            		$("#tblResponsibility tbody").empty()
            		var html = "";
            		for (var i=0; i<data.length; i++) {
            			html += "<tr>";
            			html += "<td>";
            			html += "<div class='row'><button type='button' style='width:200px' class='btn btn-primary' onclick='confirmSave(\"" + data[i].responsibilityName + "\")'>" + data[i].responsibilityName + "</button></div>";
            			html += "</td>";
            			html += "</tr>";
            		
            		}
            		$("#tblResponsibility tbody").append(html);
            		$("#responsibilityModal").modal("show");
            	}
			}
		});
	}
	
	function performInsert() {
		$("#hiddenFormAction").val("UPDATE_CHANGE_INSERT");
		
		showLoading();
		
		// Check the responsibility
		$.ajax({
            url: "<%=request.getContextPath() %>/hcm/searchResponsibility",
            type: "POST",
            data: {userId: '<c:out value="${formBean.userId}"/>'  },
            success: function(data) {
            	hideLoading();
            
            	if (data.length == 0) {
            		$("#divErrorMsg").html("You do not have responsibility to create HCM record.");
					$("#divError").show();
			
					$('html,body').animate({ scrollTop: 0 }, 'slow');
            	} 
            	else if (data.length == 1) {
            		$("#repsonsibilityId").val(data[0].responsibilityName);
            		
            		if ($("#hiddenEffectiveFromDate").val() == $("#txtEffectiveFromDate").val()) {
						// performCorrection(); 
						showCorrectionMessage();
					} 
					else {
            			if (currentTarget != null) {
	           				currentTarget.submit();
	           			}
	           			else {
	           				$("#btnSubmitSave").click();
	           			}
           				showLoading();
            		}
            	}
            	else {
            		$("#tblResponsibility tbody").empty()
            		var html = "";
            		for (var i=0; i<data.length; i++) {
            			html += "<tr>";
            			html += "<td>";
            			html += "<div class='row'><button type='button' style='width:200px' class='btn btn-primary' onclick='confirmSave(\"" + data[i].responsibilityName + "\")'>" + data[i].responsibilityName + "</button></div>";
            			html += "</td>";
            			html += "</tr>";
            		
            		}
            		$("#tblResponsibility tbody").append(html);
            		$("#responsibilityModal").modal("show");
            	}
			}
		});
	}
	
	function performReplace() {
		$("#hiddenFormAction").val("UPDATE_OVERRIDE");
		
		showLoading();
		
		// Check the responsibility
		$.ajax({
            url: "<%=request.getContextPath() %>/hcm/searchResponsibility",
            type: "POST",
            data: {userId: '<c:out value="${formBean.userId}"/>'  },
            success: function(data) {
            	hideLoading();
            
            	if (data.length == 0) {
            		$("#divErrorMsg").html("You do not have responsibility to create HCM record.");
					$("#divError").show();
			
					$('html,body').animate({ scrollTop: 0 }, 'slow');
            	} 
            	else if (data.length == 1) {
            		$("#repsonsibilityId").val(data[0].responsibilityName);
            		
            		if ($("#hiddenEffectiveFromDate").val() == $("#txtEffectiveFromDate").val()) {
						// performCorrection(); 
						showCorrectionMessage();
					} 
					else {
            			if (currentTarget != null) {
	           				currentTarget.submit();
	           			}
	           			else {
	           				$("#btnSubmitSave").click();
	           			}
            			showLoading();
            		}
            	}
            	else {
            		$("#tblResponsibility tbody").empty()
            		var html = "";
            		for (var i=0; i<data.length; i++) {
            			html += "<tr>";
            			html += "<td>";
            			html += "<div class='row'><button type='button' style='width:200px' class='btn btn-primary' onclick='confirmSave(\"" + data[i].responsibilityName + "\")'>" + data[i].responsibilityName + "</button></div>";
            			html += "</td>";
            			html += "</tr>";
            		
            		}
            		$("#tblResponsibility tbody").append(html);
            		$("#responsibilityModal").modal("show");
            	}
			}
		});
	}
	
	function confirmSave(responsibilityId) {
		$("#repsonsibilityId").val(responsibilityId);
	
		if ($("#hiddenEffectiveFromDate").val() == $("#txtEffectiveFromDate").val() && $("#hiddenFormAction").val() == "UPDATE") {
			// performCorrection(); 
			showCorrectionMessage();
		} 
		else {
        	showLoading();
			if (currentTarget != null) {
           		currentTarget.submit();
           	}
           	else {
           		$("#btnSubmitSave").click();
           	}
        }
	}
	
	function showCorrectionMessage() {
		$("#changeModeModal").modal("show");
	}
	
	function performDelete() {
		$("#model_confirmDelete").modal("show");
	}
	
	function submitDelete() {
		if ($("#lastRecord").val() == "Y") {
			$("#divOtherDeleteOption").hide();
		}
		else {
			$("#divOtherDeleteOption").show();
		}
	
		$("#deleteModeModel").modal("show");
	}
	
	function addGradeRow() {
		var x = $("#tblValidGrade tr:last").clone();
		$("#tblValidGrade tr:last").after(x);
		
		var newRowIdx = $("#tblValidGrade tr").length-2;
		$("#tblValidGrade tr:last input:eq(0)").attr("readonly", false);
		$("#tblValidGrade tr:last input").val("");
		
		$("#tblValidGrade tr:last input:eq(0)").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMGradeList", request, function(result){
					response($.map(result, function(item){
						return {label: item.gradeName, value: item.gradeId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$(this).parent().find("input[name='grade']").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$(this).parent().find("input[name='grade']").val(ui.item.value);
				return false;
			}
		});
		
		$(".input-group.date").datepicker({
	  		format: "dd/mm/yyyy",
	  		autoclose: true,
	  		daysOfWeekHighlighted: [0],
	  		todayHighlight: true,
	  		todayBtn: "linked"
	  	})
	  	.on('changeDate', function(e) {
	  		if ($($(this).parent().find("input")[0]).attr("required") === undefined) {
	  		}
	  		else {
	  			if ($(this).next().css("display") != "none")
	  				$("#frmDetail").bootstrapValidator('revalidateField', $($(this).parent().find("input")[0]).attr("name"));
	  		}
	  	});
	}

	function updatePostName() {
		var str = "";
		str += $("#postTitle").val();
		if ($("#ddPostOrganization").val() != "")	{
			str += "." + $("#ddPostOrganization").val() + ".";
			if ($("#unitTeam").val() != "") {
				str += $("#unitTeam").val();
			}
		}
	
		$("#lblPostName").html(str);
		$("#hiddenPostName").val(str);
	}	
	
	function changeDefaultLocation() {
		if ($("#postOrganization").val() != "") {
			showLoading();
			
			$.ajax({
	            url: "<%=request.getContextPath() %>/hcm/getDefaultLocation",
	            type: "POST",
	            data: {organizationId: $("#postOrganization").val()},
	            success: function(data) {
	            	hideLoading();
	            	
	            	if (data != null) {
	            		$("#ddLocation").val(data.locationCode + " [" + data.description + "]");
	            		$("#location").val(data.locationId);
	            	}
				}
			});
		}
	}
	
	var currentTarget = null;
	var haveError = "N";
	
	$(function(){
		$.ajaxSetup({cache: false});
	
		orgProposedEndDate = $("#txtProposedEndDate").val();
		$("#txtStartDate").prop("readonly", true);
		$("#txtFTE").prop("readonly", true);
		$("#txtHiringStatusStartDate").prop("readonly", true);
		
		$("#effectiveFromDisplay").prop("readonly", true);
		$("#effectiveToDisplay").prop("readonly", true);
		
		$('#tblSearchResult').DataTable();
		 
		$("#frmDetail").bootstrapValidator({
			message: 'This field cannot be empty.',
			live: "submitted",
			fields: {
			},
		})
		.on('error.field.bv', function(e, data) {
			hideLoading();
			$("#updateModeModel").modal("hide");
		})
		.on('success.form.bv', function(e){
			currentTarget = e.target;
			if (!performValidation()) {
				e.preventDefault();
			}
		});
		
		
		$("#ddHcmPostTitle").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMPostTitleList", request, function(result){
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
				$.getJSON("getHCMPostOrganizationList", request, function(result){
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
				$.getJSON("getHCMOrganizationList", request, function(result){
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
				$.getJSON("getHCMJobList", request, function(result){
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
		
		$("#ddPostTitle").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMPostTitleList", request, function(result){
					response($.map(result, function(item){
						return {label: item.titleId, value: item.titleId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#postTitle").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#postTitle").val(ui.item.value);
				return false;
			}
		});
		
		var loadFlag = false;
		$("#ddPostOrganization").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMPostOrganizationList", request, function(result){
					response($.map(result, function(item){
						return {label: item.organizatonName, value: item.organizatonId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#postOrganization").val(ui.item.value);
				
				$("#ddOrganization").val(ui.item.label);
				$("#organization").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#postOrganization").val(ui.item.value);
				
				$("#ddOrganization").val(ui.item.label);
				$("#organization").val(ui.item.value);
				
				changeDefaultLocation();
				loadFlag = true;
				return false;
			},
			change: function(event, ui) {
				if (!loadFlag) {
					changeDefaultLocation();
				}
				else {
					loadFlag = false;
				}
				
				return false;
			}
		});
		
		$("#ddOrganization").attr("readonly", true);
		
		$("#ddJob").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMJobList", request, function(result){
					response($.map(result, function(item){
						return {label: item.jobName, value: item.jobId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#job").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#job").val(ui.item.value);
				return false;
			}
		});
		
		$("#ddLocation").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMLocationList", request, function(result){
					response($.map(result, function(item){
						return {label: item.locationCode + " [" + item.description + "]", value: item.locationId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$("#location").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$("#location").val(ui.item.value);
				return false;
			}
		});
		
		// Update the name field for valid grade
		for (var i=1; i<$("#tblValidGrade tr").length; i++) {
			$("#tblValidGrade tr:eq(" + i + ") input:eq(0)").attr("name", "ddGrade");
			$("#tblValidGrade tr:eq(" + i + ") input:eq(1)").attr("name", "grade");
			$("#tblValidGrade tr:eq(" + i + ") input:eq(2)").attr("name", "gradeUid");
			$("#tblValidGrade tr:eq(" + i + ") input:eq(3)").attr("name", "versionNumber");
			$("#tblValidGrade tr:eq(" + i + ") input:eq(4)").attr("name", "gradeDateFrom");
			$("#tblValidGrade tr:eq(" + i + ") input:eq(5)").attr("name", "gradeDateTo");
		}
		
		$("input[name='ddGrade']").autocomplete({
			source: function(request, response) {
				$.getJSON("getHCMGradeList", request, function(result){
					response($.map(result, function(item){
						return {label: item.gradeName, value: item.gradeId}
					}));
				});
			},
			focus: function(event, ui) {
				$(this).val(ui.item.label);
				$(this).parent().find("input[name='grade']").val(ui.item.value);
				return false;
			},
			select: function(event, ui) {
				$(this).val(ui.item.label);
				$(this).parent().find("input[name='grade']").val(ui.item.value);
				return false;
			}
		});

		if ($("#hiddenPositionId").val() != "") {
			$('#searchCriteria').collapse('hide');
			$("#divHCMDetail").show();
		}
		
		$("#ddGrade").on("keydown focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($(this).parent().find("input[name='grade']").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$(this).parent().find("input[name='grade']").val("");
				return;
			}
		});
		
		$("#ddPostTitle").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#postTitle").val("");
			}
		});
		
		$("#ddPostTitle").on("focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#postTitle").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#postTitle").val("");
				return;
			}
		});
		
		$("#ddPostOrganization").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#postOrganization").val("");
				
				$("#ddOrganization").val("");
				$("#organization").val("");
				
				$("#ddLocation").val("");
				$("#location").val("");
			}
		});
		
		$("#ddPostOrganization").on("focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#postOrganization").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#postOrganization").val("");
				return;
			}
		});
		
		$("#ddOrganization").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#organization").val("");
			}
		});
		
		$("#ddOrganization").on("focusout", function(event) {
			if (event.type != "focusout") {
				return;
			}
				
			if ($("#organization").val() == "") {
				$(this).val("");
				return;
			}
			
			if ($(this).val() == "") {
				$("#organization").val("");
				return;
			}
		});
		
		$("#ddHcmPostTitle").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#hcmPostTitle").val("");
			}
		});
		
		$("#ddHcmPostTitle").on("focusout", function(event) {
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
		
		$("#ddHcmJob").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#hcmJob").val("");
			}
		});
		
		$("#ddHcmJob").on("focusout", function(event) {
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
		
		$("#ddHcmPostOrganization").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#hcmPostOrganization").val("");
			}
		});
		
		$("#ddHcmPostOrganization").on("focusout", function(event) {
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
		
		$("#ddHcmOrganization").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#hcmOrganization").val("");
			}
		});
		
		$("#ddHcmOrganization").on("focusout", function(event) {
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
		
		$("#ddLocation").on("keydown", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$("#location").val("");
			}
		});
		
		$("#ddLocation").on("focusout", function(event) {
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
		
		$("#frmSearch .input-group.date").datepicker({
	  		format: "dd/mm/yyyy",
	  		autoclose: true,
	  		daysOfWeekHighlighted: [0],
	  		todayHighlight: true,
	  		todayBtn: "linked"
	  	})
	  	.on('changeDate', function(e) {
	  		if ($($(this).parent().find("input")[0]).attr("required") === undefined) {
	  		}
	  		else {
	  			if ($(this).next().css("display") != "none") {
	  				$("#frmSearch").bootstrapValidator('revalidateField', $($(this).parent().find("input")[0]).attr("name"));
	  			}
	  		}
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
		
		hiringStatusChange();
		
		<c:choose>
			<c:when test="${formBean.positionId == ''}">
				$("#divHCMDetail").show();
			</c:when>
		</c:choose>
		
	});
	
	function performDeleteNext() {
		$("#hiddenFormAction").val("DEL_NEXT");
		
		showLoading();
		
		// Check the responsibility
		$.ajax({
            url: "<%=request.getContextPath() %>/hcm/searchResponsibility",
            type: "POST",
            data: {userId: '<c:out value="${formBean.userId}"/>'  },
            success: function(data) {
            	hideLoading();
            
            	if (data.length == 0) {
            		$("#divErrorMsg").html("You do not have responsibility to create HCM record.");
					$("#divError").show();
			
					$('html,body').animate({ scrollTop: 0 }, 'slow');
            	} 
            	else if (data.length == 1) {
            		$("#repsonsibilityId").val(data[0].responsibilityName);
           			if (currentTarget != null) {
           				currentTarget.submit();
           			}
           			else {
           				$("#btnSubmitSave").click();
           			}
           			showLoading();
            	}
            	else {
            		$("#tblResponsibility tbody").empty()
            		var html = "";
            		for (var i=0; i<data.length; i++) {
            			html += "<tr>";
            			html += "<td>";
            			html += "<div class='row'><button type='button' style='width:200px' class='btn btn-primary' onclick='confirmSave(\"" + data[i].responsibilityName + "\")'>" + data[i].responsibilityName + "</button></div>";
            			html += "</td>";
            			html += "</tr>";
            		
            		}
            		$("#tblResponsibility tbody").append(html);
            		$("#responsibilityModal").modal("show");
            	}
			}
		});
	}
	
	function performDeletePurge() {
		$("#hiddenFormAction").val("DEL_PURGE");
		
		showLoading();
		
		// Check the responsibility
		$.ajax({
            url: "<%=request.getContextPath() %>/hcm/searchResponsibility",
            type: "POST",
            data: {userId: '<c:out value="${formBean.userId}"/>'  },
            success: function(data) {
            	hideLoading();
            
            	if (data.length == 0) {
            		$("#divErrorMsg").html("You do not have responsibility to create HCM record.");
					$("#divError").show();
			
					$('html,body').animate({ scrollTop: 0 }, 'slow');
            	} 
            	else if (data.length == 1) {
            		$("#repsonsibilityId").val(data[0].responsibilityName);
            		if (currentTarget != null) {
           				currentTarget.submit();
           			}
           			else {
           				$("#btnSubmitSave").click();
           			}
            		showLoading();
            	}
            	else {
            		$("#tblResponsibility tbody").empty()
            		var html = "";
            		for (var i=0; i<data.length; i++) {
            			html += "<tr>";
            			html += "<td>";
            			html += "<div class='row'><button type='button' style='width:200px' class='btn btn-primary' onclick='confirmSave(\"" + data[i].responsibilityName + "\")'>" + data[i].responsibilityName + "</button></div>";
            			html += "</td>";
            			html += "</tr>";
            		
            		}
            		$("#tblResponsibility tbody").append(html);
            		$("#responsibilityModal").modal("show");
            	}
			}
		});
	}
	
	function performDeleteAll() {
		$("#hiddenFormAction").val("DEL_ALL");
		
		showLoading();
		
		// Check the responsibility
		$.ajax({
            url: "<%=request.getContextPath() %>/hcm/searchResponsibility",
            type: "POST",
            data: {userId: '<c:out value="${formBean.userId}"/>'  },
            success: function(data) {
            	hideLoading();
            
            	if (data.length == 0) {
            		$("#divErrorMsg").html("You do not have responsibility to create HCM record.");
					$("#divError").show();
			
					$('html,body').animate({ scrollTop: 0 }, 'slow');
            	} 
            	else if (data.length == 1) {
            		$("#repsonsibilityId").val(data[0].responsibilityName);
            		if (currentTarget != null) {
           				currentTarget.submit();
           			}
           			else {
           				$("#btnSubmitSave").click();
           			}
            		showLoading();
            	}
            	else {
            		$("#tblResponsibility tbody").empty()
            		var html = "";
            		for (var i=0; i<data.length; i++) {
            			html += "<tr>";
            			html += "<td>";
            			html += "<div class='row'><button type='button' style='width:200px' class='btn btn-primary' onclick='confirmSave(\"" + data[i].responsibilityName + "\")'>" + data[i].responsibilityName + "</button></div>";
            			html += "</td>";
            			html += "</tr>";
            		
            		}
            		$("#tblResponsibility tbody").append(html);
            		$("#responsibilityModal").modal("show");
            	}
			}
		});
		
		
	}
	
	var currentValidGrade = null;
	var delGradeUid = null;
	var delVersionNo = null;
	var delResponsibilityId = null;
	function removeGrade(obj) {
		currentValidGrade = obj;
	
		$("#model_confirmDeleteValidGrade").modal("show");
	}
	
	function confirmDeleteValidGrade() {
		delGradeUid = $(currentValidGrade).parent().parent().find("input[name='gradeUid']").val();
		delVersionNo = $(currentValidGrade).parent().parent().find("input[name='versionNumber']").val();

		if (delGradeUid != "") {
			showLoading();
			
			// Check the responsibility
			$.ajax({
	            url: "<%=request.getContextPath() %>/hcm/searchResponsibility",
	            type: "POST",
	            data: {userId: '<c:out value="${formBean.userId}"/>'  },
	            success: function(data) {
	            	hideLoading();
	            
	            	if (data.length == 0) {
	            		$("#divErrorMsg").html("You do not have responsibility to create HCM record.");
						$("#divError").show();
				
						$('html,body').animate({ scrollTop: 0 }, 'slow');
	            	} 
	            	else if (data.length == 1) {
	            		proceedDeleteValidGrade(data[0].responsibilityName);
	            	}
	            	else {
	            		$("#tblResponsibility tbody").empty()
	            		var html = "";
	            		for (var i=0; i<data.length; i++) {
	            			html += "<tr>";
	            			html += "<td>";
	            			html += "<div class='row'><button type='button' style='width:200px' class='btn btn-primary' onclick='proceedDeleteValidGrade(\"" + data[i].responsibilityName + "\")'>" + data[i].responsibilityName + "</button></div>";
	            			html += "</td>";
	            			html += "</tr>";
	            		}
	            		$("#tblResponsibility tbody").append(html);
	            		$("#responsibilityModal").modal("show");
	            	}
				}
			});
		}	
		else {
			if ($("#tblValidGrade tbody tr").length == 1) {
				$("#tblValidGrade tr:last input").val("");
				$("#tblValidGrade tr:last input[name='ddGrade']").attr("readonly", false)
			}
			else {
				$(currentValidGrade).parent().parent().remove();
			}
		}
	}
	
	function proceedDeleteValidGrade(responsibilityId) {
		delResponsibilityId = responsibilityId;
		showLoading();
	            		
	    $.ajax({
			url: "<%= request.getContextPath() %>/hcm/deleteValidGrade",
			cache: false,
			type: "POST",
			data: {gradeUid: delGradeUid, versionNumber: delVersionNo, repsonsibilityId: delResponsibilityId},
				success: function(data) {
				hideLoading();
								
				if ($("#tblValidGrade tbody tr").length == 1) {
					$("#tblValidGrade tr:last input").val("");
					$("#tblValidGrade tr:last input[name='ddGrade']").attr("readonly", false)
				}
				else {
					$(currentValidGrade).parent().parent().remove();
				}
        	}
       	});
       	
       	
        $("#responsibilityModal").modal("hide");
	}
	
	function hiringStatusChange() {
		if ($("#hiringStatus").val() == 'Frozen'){
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('proposedEndDate', true);
			$(".star").show();
		}
		else{
			$("#frmDetail").data('bootstrapValidator').enableFieldValidators('proposedEndDate', false);
			$("#txtProposedEndDate").val(orgProposedEndDate);
			$(".star").hide();
		}
	}
</script>	
			
<!-- Page Content -->
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > HCM Position > Update HCM Position
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
			<div class="title pull-left"><i class="fa fa-edit"></i>Update HCM Position</div>
		</div>
		<c:choose>
			<c:when test="${formBean.fromCreateHCMPage != 'Y'}">

				<div class="panel panel-custom-primary">
					<div class="panel-heading">
						<div class="panel-heading-title">
							<a role="button" data-target="#searchCriteria" class="panel-title" data-toggle="collapse">Searching Criteria</a>
						</div>
					</div>
					<div id="searchCriteria" class="panel-collapse collapse in">
						<div class="panel-body">
							<form id="frmSearch" method="POST">
								<div class="row">
									<div class="col-sm-4">
										<label for="position_title" class="field_request_label">Effective Date<font class="star">*</font></label>
									</div>
									<div class="col-sm-8">
										<div class='input-group date'>
											<form:input path="formBean.hcmEffectiveDate" class="form-control" />
											<span class="input-group-addon">
												<span class="glyphicon glyphicon-calendar"></span>
											</span>
										</div>
									</div>
								</div>
							
								<div class="row">
									<div class="col-sm-4">
										<label for="position_title" class="field_request_label">Position Title/Generic Rank</label>
									</div>
									<div class="col-sm-8">
										<form:input path="formBean.ddHcmPostTitle" class="form-control" style="width:100%;"/>
										<form:hidden path="formBean.hcmPostTitle" />
									</div>
								</div>
								
								<div class="row">
									<div class="col-sm-4">
										<label for="position_title" class="field_request_label">Position Organization</label>
									</div>
									<div class="col-sm-8">
										<form:input path="formBean.ddHcmPostOrganization" class="form-control" style="width:100%;" />
										<form:hidden path="formBean.hcmPostOrganization" />
									</div>
								</div>
								
								<div class="row">
									<div class="col-sm-4">
										<label for="position_title" class="field_request_label">Unit/Team</label>
									</div>
									<div class="col-sm-8">
										<form:input path="formBean.hcmUnitTeam" class="form-control" style="width:100%;"/>
									</div>
								</div>
								
								<div class="row">
									<div class="col-sm-4">
										<label for="position_title" class="field_request_label">Job</label>
									</div>
									<div class="col-sm-8">
										<form:input path="formBean.ddHcmJob"  class="form-control" style="width:100%;"/>
										<form:hidden path="formBean.hcmJob" />
									</div>
								</div>
								
								<div class="row">
									<div class="col-sm-4">
										<label for="position_title" class="field_request_label">Organization</label>
									</div>
									<div class="col-sm-8">
										<form:input path="formBean.ddHcmOrganization" class="form-control" style="width:100%;"/>
										<form:hidden path="formBean.hcmOrganization" />
									</div>
								</div>
								
								<!--  Added for UT29984 - Start -->
								<div class="row">
									<div class="col-sm-4">
										<label for="position_title" class="field_request_label">Position Name</label>
									</div>
									<div class="col-sm-8">
										<form:input path="formBean.hcmPositionName" class="form-control" style="width:100%;"/>
									</div>
								</div>
								<!--  Added for UT29984 - End -->
								
								<div class="row">
									<div class="col-sm-12" style="text-align:right">
										<button type="button" class="btn btn-primary" style="width:130px;" onclick="performSearch()"><i class="fa fa-search"></i> Search</button>
									</div>
								</div>
							</form>
						</div>
					</div>
					<!-- ./searchCriteria -->
				</div>
			</c:when>
		</c:choose>
		
		<div id="divHCMDetail" style="display:none" class="panel panel-custom-primary">
			<div class="panel-heading">
				<div class="panel-heading-title">HCM Position Detail</div>
			</div>
			<div class="panel-body">
				<form id="frmDetail" method="POST" action="updateHCMPost">
					<form:hidden path="formBean.formAction" id="hiddenFormAction"/>
					<form:hidden path="formBean.positionId" id="hiddenPositionId"/>
					<form:hidden path="formBean.versionNo" id="hiddenVersionNo"/>
					<form:hidden path="formBean.lastRecord" />
					<form:hidden path="formBean.repsonsibilityId"/>
					<div class="form-group">
						<div class="row">
							<div class="col-sm-2">
							  <label for="txt_unit" class="field_request_label">Effective Date<font class="star">*</font></label>
							</div>
							<div class="col-sm-2">
								<div class='input-group date' id='datetimepicker1'>
									<form:input path="formBean.effectiveFromDate" 
									            id="txtEffectiveFromDate" 
									            name="txtEffectiveFromDate" 
									            class="form-control"
									            required="required" />
									<span class="input-group-addon">
										<span class="glyphicon glyphicon-calendar"></span>
									</span>	 
								</div>
								
								<form:hidden path="formBean.hiddenEffectiveFromDate" id="hiddenEffectiveFromDate" />
							</div>
						</div>
					</div>
					<div class="delimiter"></div>
					<div class="row">
						<div class="col-sm-2">
							<label for="txt_post_title" class="field_request_label">Start Date</label>
						</div>
						<div class="col-sm-2">
							<form:input path="formBean.startDate" id="txtStartDate" name="txtStartDate" class="form-control"/>
						</div>
					</div>
					<fieldset class="scheduler-border" style="margin:0px">
						
						<div class="row">
							<div class="col-sm-2">
								<label for="txt_post_name" class="field_request_label">Position Name</label>
							</div>
							<div class="col-sm-7">
								<label for="txt_post_name" id="lblPostName" class="txt_post_name"><c:out value="${formBean.postName}"/></label>
								<form:hidden path="formBean.hiddenPostName" />
							</div>
						</div>
							 
						<div class="form-group">
							<div class="row">
								<div class="col-sm-1"></div>
								<div class="col-sm-3">
									<label for="hcmPostTitle" class="control-label">Position Title/Generic Rank<font class="star">*</font></label>
								</div>
								<div class="col-sm-6">
									<form:input path="formBean.ddPostTitle"  class="form-control" style="width:100%;" onchange="updatePostName()" onblur="updatePostName()" required="required"/>
									<form:hidden path="formBean.postTitle" />
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-1"></div>
								<div class="col-sm-3">
									<label for="hcmPostOrganization" class="control-label">Position Organization<font class="star">*</font></label>
								</div>
								<div class="col-sm-6">
									<form:input path="formBean.ddPostOrganization"  class="form-control" style="width:100%;" onchange="updatePostName()" onblur="updatePostName();" required="required"/>
									<form:hidden path="formBean.postOrganization" />
								</div>
							</div>
						</div>
					
						<div class="form-group">
							<div class="row">
								<div class="col-sm-1"></div>
								<div class="col-sm-3">
									<label for="hcmUnitTeam" class="control-label">Unit/Team</label>
								</div>
								<div class="col-sm-6">
									<form:input path="formBean.unitTeam"  class="form-control" style="width:100%;" onchange="updatePostName()" required/>
								</div>
							</div>
						</div>
					
					</fieldset>
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="txt_post_name" class="control-label">Effective From</label>
							</div>
							<div class="col-sm-2">
								<form:input path="formBean.effectiveFromDisplay"  class="form-control" style="width:100%;"/>
							</div>
							<div class="col-sm-1">
							 	to
   						    </div>
							<div class="col-sm-2">
								<form:input path="formBean.effectiveToDisplay" class="form-control" style="width:100%;"/>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="txt_post_name" class="control-label">Type</label>
							</div>
							<div class="col-sm-4">
								<form:radiobutton path="formBean.type" value="SHARED" id="type1"/><label class="radio-inline">Shared</label>
								&nbsp;&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="formBean.type" value="SINGLE" id="type2"/><label class="radio-inline">Single Incumbent</label>
								&nbsp;&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="formBean.type" value="NONE" id="type3"/><label class="radio-inline">None</label>
							</div>
						</div>
					</div>
									
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="hcmOrganization" class="control-label">Organization<font class="star">*</font></label>
							</div>
							<div class="col-sm-8">
								<form:input path="formBean.ddOrganization" class="form-control" style="width:100%;" />
								<form:hidden path="formBean.organization" />
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="hcmJob" class="control-label">Job<font class="star">*</font></label>
							</div>
							<div class="col-sm-8">
								<form:input path="formBean.ddJob" class="form-control" style="width:100%;" required="required"/>
								<form:hidden path="formBean.job" />
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="hiringStatus" class="control-label">Hiring Status<font class="star">*</font></label>
							</div>
							<div class="col-sm-2">
								<form:select path="formBean.hiringStatus" class="form-control" style="width:100%;" required="required" onchange="hiringStatusChange();">
									<option value="">- Select -</option>
									<form:options items="${hiringStatusList}" />
								</form:select>  
							</div>
						</div>
						
						<div class="col-sm-2">
							<label for="proposedEndDate" class="control-label">Start Date</label>
						</div>
						<div class="col-sm-2">
							<form:input path="formBean.hiringStatusStartDate" id="txtHiringStatusStartDate" name="txtHiringStatusStartDate" class="form-control"/>
						</div>
						
						<div class="form-group">
							<div class="col-sm-2">
								<label for="proposedEndDate" class="control-label">Proposed End Date<font class="star">*</font></label>
							</div>
							<div class="col-sm-2">
								<div class='input-group date'>
									<form:input path="formBean.proposedEndDate" 
									            id="txtProposedEndDate" name="txtProposedEndDate" 
									            class="form-control" required="required"/>
									<span class="input-group-addon">
										<span class="glyphicon glyphicon-calendar"></span>
									</span>
								</div>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-2">
							<label for="location" class="field_request_label">Location</label>
						</div>
						<div class="col-sm-8">
							<form:input path="formBean.ddLocation" class="form-control" style="width:100%;"/>
							<form:hidden path="formBean.location" />
						</div>
					</div>
					
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="fte" class="control-label">FTE</label>
							</div>
							<div class="col-sm-2">
								<form:input path="formBean.fte" id="txtFTE" name="txtFTE" class="form-control" onclick="showUpdateFTE()" />
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-2">
								<label for="headCount" class="control-label">Headcount<font class="star">*</font></label>
							</div>
							<div class="col-sm-2">
								<form:input path="formBean.headCount" id="txtHeadCount" name="txtHeadCount" class="form-control" required="required"/>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="form-group">
							<div class="col-sm-2">
								<label for="probationDuration" class="control-label">Probation Duration<font class="star">*</font></label>
							</div>
							<div class="col-sm-2">
								<form:input path="formBean.probationDuration" id="txtProbationDuration" name="txtProbationDuration" class="form-control" required="required"/>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-2">
								<label for="probationDurationUnit" class="control-label">Duration Unit<font class="star">*</font></label></div>
							<div class="col-sm-2">
								<form:select path="formBean.probationDurationUnit" id="txtProbationDurationUnit" name="txtProbationDurationUnit" class="form-control" style="width:100%;" required="required">
									<option value="">- Select -</option>
									<form:options items="${durationUnitList}" />
								</form:select>
							</div>
						</div>
						
						<div class="col-sm-2" style="text-align:right">
							<button type="button" class="btn btn-default" style="width:130px;" onclick="$('#updateAddition').modal('show')"><i class="fa fa-plus-circle fa-sm"></i> Additional Detail</button>
						</div>
					</div>
					
					<fieldset class="scheduler-border">
						<table style="margin:0px;padding:0px">
							<tr>
								<td>
									<table id="tblValidGrade" class="table-bordered mprs_table" style="width:700px;margin-top:3px;margin-bottom:0px">
										<thead>
											<tr>
												<th style="width:400px">Valid Grades</th>
												<th style="width:150px">Date From</th>
												<th style="width:150px">Date To</th>
												<th style="width:100px">&nbsp;</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${formBean.gradeListSize == 0}">
													<tr>
														<td>
															<input type="text" name="ddGrade" class="form-control" style="width:100%;" value="" />
															<input type="hidden" name="grade" value=""/>
															<input type="hidden" name="gradeUid" value=""/>
															<input type="hidden" name="versionNumber" value=""/>
														</td>
														<td>
															<div class='input-group date'>
																<input type="text" name="gradeDateFrom" 
																		class="form-control"/>
																<span class="input-group-addon">
																	<span class="glyphicon glyphicon-calendar"></span>
																</span>
															</div>
														</td>
														<td>
															<div class='input-group date'>
																<input type="text" name="gradeDateTo" 
																		class="form-control"/>
																<span class="input-group-addon">
																	<span class="glyphicon glyphicon-calendar"></span>
																</span>
															</div>
														</td>
														<td style="text-align:center"><button type="button" name="btnRemoveValidGrade" class="btn btn-danger" onclick="removeGrade($(this))"><i class="fa fa-times"></i> Delete</button></td>
													</tr>
												</c:when>
											</c:choose>
											
											<c:forEach var="listValue" items="${formBean.gradeList}" varStatus="pStatus">
												<tr>
													<td>
														<form:input path="formBean.gradeList[${pStatus.index}].gradeDesc" name="ddGrade" class="form-control" style="width:100%;" multiple="false"/>
														<form:hidden path="formBean.gradeList[${pStatus.index}].gradeId" name="grade" />	
														<form:hidden path="formBean.gradeList[${pStatus.index}].gradeUid" name="gradeUid" />	
														<form:hidden path="formBean.gradeList[${pStatus.index}].versionNumber" name="versionNumber" />													        
													</td>
													<td>
														<div class='input-group date'>
															<form:input path="formBean.gradeList[${pStatus.index}].dateFrom" 
															            name="gradeDateFrom" 
															            class="form-control"/>
															<span class="input-group-addon">
																<span class="glyphicon glyphicon-calendar"></span>
															</span>
														</div>
													</td>
													<td>
														<div class='input-group date'>
															<form:input path="formBean.gradeList[${pStatus.index}].dateTo" 
																	    name="gradeDateTo" 
																	    class="form-control"/>														            
															<span class="input-group-addon">
																<span class="glyphicon glyphicon-calendar"></span>
															</span>
														</div>
													</td>
													<td style="text-align:center"><button type="button" name="btnRemoveValidGrade" class="btn btn-danger" onclick="removeGrade($(this))"><i class="fa fa-times"></i> Delete</button></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</td>
								<td style="vertical-align:bottom">&nbsp;&nbsp;
									<button type="button" id="btnAddGradeRow" class="btn btn-primary" style="width:130px;" onclick="addGradeRow()"><i class="fa fa-plus"></i> Add Valid Grade</button>
								</td>
							</tr>
						</table>
					</fieldset> 
	
					<div class="row" style="text-align:right">
						<div class="col-sm-12">
							<button type="button" class="btn btn-primary" name="btnSave" style="width:110px" onclick="performSave()"><i class="fa fa-floppy-o"></i> Save</button>
							<button type="button" class="btn btn-danger" name="btnDelete" style="width:110px" onclick="performDelete()"><i class="fa fa-trash-o"></i> Delete</button>
						</div>
					</div>
					
					<!-- Model for select Update / Correction -->
					<div id="updateAddition" class="modal fade" role="dialog">
						<div class="modal-dialog modal-dialog-custom" style="width:750px">
					    	<div class="modal-content">
					    		<div class="modal-header">
									<h4><b>Additional Details</b></h4>
								</div>
					      		<div class="modal-body">
						      		<div class="row">
										<div class="col-sm-3">
											<label for="positionGroup" class="control-label">Position Group</label>
										</div>
										<div class="col-sm-7">
											<form:input path="formBean.positionGroup" class="form-control"/>
										</div>
									</div>
									<div class="row">
										<div class="col-sm-3">
											<label for="srcFunding" class="control-label">Source Funding</label></div>
										<div class="col-sm-7">
											<form:select path="formBean.srcFunding" class="form-control" style="width:100%" >
												<option value="">- Select -</option>
												<form:options items="${srcFundingList}" />
											</form:select>
										</div>
					      			</div>
						      	</div>
					      		<div class="modal-footer">
					        		<button type="button" class="btn btn-default" style="width:100px" data-dismiss="modal">OK</button>
					      		</div>
					    	</div>
					  	</div>
					</div><!-- ./updateAddition -->
					
					<!-- Model for select Update / Correction -->
					<div id="updateModeModel" class="modal fade" role="dialog">
						<div class="modal-dialog modal-dialog-custom" style="width:500px">
					    	<div class="modal-content">
					      		<div class="modal-header">
					        		<h4><b>Choose an option:</b>
							    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
							    	</h4>
					      		</div>
						    	<div class="modal-body">
						    		<div id="divWithEffectiveDateChange">
						    			<div class="row">
								        	<div class="col-sm-12">
								        		<button type="button" style="width:130px" class="btn btn-primary" onclick="checkUpdate()">Update</button>
							      				Keep history of existing information
							      			</div>
							      		</div>
						      			<div class="row">
						      				<div class="col-sm-12">
								        		<button type="button" name="btnCorrection" style="width:130px" class="btn btn-primary" onclick="performCorrection()">Correction</button>
												Correct existing information
											</div>
										</div>
							      	</div>
						      		
						      		<div id="divWithoutEffectiveDateChange">
							      		<div class="row">
						      				<div class="col-sm-12">
								        		<button type="button" name="btnCorrection" style="width:130px" class="btn btn-primary" onclick="performCorrection()">Correction</button>
												Correct existing information
											</div>
										</div>
						      		</div>
						      		<button type="submit" id="btnSubmitSave" style="display:none"></button>
						      	</div>
					      		<div class="modal-footer">
					        		<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-reply"></i> Cancel</button>
					      		</div>
					    	</div>
					  	</div>
					</div>
					
					<!-- Model for select Update / Correction -->
					<div id="updateModeModel2" class="modal fade" role="dialog">
						<div class="modal-dialog modal-dialog-custom" style="width:500px">
					    	<div class="modal-content">
					      		<div class="modal-header">
					        		<h4><b>Choose an option:</b>
							    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
							    	</h4>
					      		</div>
						    	<div class="modal-body">
						      		<div id="withFutureRecDiv">
							      		<div class="row">
								        	<div class="col-sm-12">
								        		<button type="button" style="width:130px" class="btn btn-primary" onclick="performReplace()">Replace</button>
							      				Replace all scheduled changes
							      			</div>
							      		</div>
						      		
							      		<div class="row">
								        	<div class="col-sm-12">
								        		<button type="button" style="width:130px" class="btn btn-primary" onclick="performInsert()">Insert</button>
							      				Insert this change before next scheduled changed
							      			</div>
							      		</div>
						      		</div>
						      	</div>
					      		<div class="modal-footer">
					        		<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-reply"></i> Cancel</button>
					      		</div>
					    	</div>
					  	</div>
					</div>
					
					<!-- Model for select Update / Correction -->
					<div id="deleteModeModel" class="modal fade" role="dialog">
						<div class="modal-dialog modal-dialog-custom" style="width:500px">
					    	<div class="modal-content">
					      		<div class="modal-header">
					        		<h4><b>Choose an option:</b>
							    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
							    	</h4>
					      		</div>
						    	<div class="modal-body">
						    		<div id="divOtherDeleteOption">
						      			<div class="row">
						      				<div class="col-sm-12">
								        		<button type="button" type="button" name="btnDeleteNext" style="width:130px" class="btn btn-primary" onclick="performDeleteNext()">Next</button>
												Remove next change
											</div>
										</div>
								        <div class="row">
								        	<div class="col-sm-12">
								        		<button type="button" type="button" style="width:130px" class="btn btn-primary" onclick="performDeleteAll()">All</button>
							      				Remove all scheduled changes
							      			</div>
							      		</div>
						      		</div>
						      		<div class="row">
							        	<div class="col-sm-12">
							        		<button type="button" type="button" style="width:130px" class="btn btn-primary" onclick="performDeletePurge()">Purge</button>
						      				Complete remove from the database
						      			</div>
						      		</div>
						      	</div>
					      		<div class="modal-footer">
					        		<button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-reply"></i> Cancel</button>
					      		</div>
					    	</div>
					  	</div>
					</div>
					
					<!-- Model for select Update / Correction -->
					<div id="updateFTEModel" class="modal fade" role="dialog">
						<div class="modal-dialog modal-dialog-custom" style="width:500px">
					    	<div class="modal-content">
					    		<div class="modal-header">
					        		<h4><b>Update FTE</b></h4>
					      		</div>
						    	<div class="modal-body">
					      			<div class="row">
					      				<div class="col-sm-4">
							        		New FTE<font class="star">*</font>
										</div>
										<div class="col-sm-8">
											<form:input path="formBean.updatedFTE" class="form-control" />
										</div>
									</div>
							        <div class="row">
					      				<div class="col-sm-4">
							        		Update reason<font class="star">*</font>
										</div>
										<div class="col-sm-8">
											<form:input path="formBean.updatedFTEReason" class="form-control" />
										</div>
									</div>
						      	</div>
					      		<div class="modal-footer">
					      			<button type="button" class="btn btn-primary" name="btnSaveFTE" style="width:110px" onclick="performUpdateFTE()"><i class="fa fa-floppy-o"></i> Save</button>
					        		<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal"><i class="fa fa-times"></i> Cancel</button>
					      		</div>
					    	</div>
					  	</div>
					</div>
				</form>
			</div>
		</div> <!-- ./divHCMDetail -->

		<!-- Model -->
		<!--  Search Result Model -->
		<div id="searchResultModel" class="modal fade" role="dialog">
			<div class="modal-dialog modal-dialog-custom" style="width:900px">
		    	<div class="modal-content">
		      		<div class="modal-header">
				    	<h4><b>Search Post</b>
				    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
				    	</h4>
				    </div>
		      		<div class="modal-body">
		        		<table id="tblSearchResult" class="table table-striped table-bordered">
							<thead>
								<tr>
									<th style="width:150px">Effective Date</th>
									<th style="width:300px">Position name</th>
									<th style="width:80px">FTE</th>
									<th style="width:80px">Headcount</th>
									<th style="width:100px">Hiring Status</th>
									<th style="width:100px">Type</th>
									<th style="width:150px"></th>
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
		
		<!-- Model to confirm Delete - Start -->
		<div id="model_confirmDelete" class="modal fade" role="dialog">
			<div class="modal-dialog modal-dialog-postId">
			    <!-- Modal content-->
			    <div class="modal-content">
			    	<div class="modal-body">
			        	<div class="row" style="padding:20px;">
							<div class="col-sm-12">
							  	<label for="" class="field_request_label">Do you really want to delete this record?</label>
							</div>
						</div>		
			      	</div>
			      	<div class="modal-footer">
						<button type="button" class="btn btn-primary" style="width:110px" data-dismiss="modal" onclick="submitDelete();"><i class="fa fa-check"></i> Yes</button>
			        	<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal"><i class="fa fa-times"></i> Cancel</button>
				    </div>
			    </div>
		  	</div>
		</div>
		<!-- Model to confirm Withdraw - End -->
		
		<div id="responsibilityModal" class="modal fade" role="dialog">
			<div class="modal-dialog modal-dialog-custom" style="width:300px">
		    	<div class="modal-content">
		    		<div class="modal-header">
						<h4><b>Select Responsibility</b>
			    			<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close">&times;</button>
			    		</h4>
					</div>
		      		<div class="modal-body">
				        <table id="tblResponsibility" style="width:260px;text-align:center">
				        	<tbody>
				        	
				        	</tbody>
				        </table>
			      	</div>
		      		<div class="modal-footer">
		        		<button type="button" class="btn btn-default" style="width:110px" data-dismiss="modal">Close</button>
		      		</div>
		    	</div>
		  	</div>
		</div>
	
		<!--  Warning Model - Start -->
		<div id="changeModeModal" class="modal fade" role="dialog">
			<div class="modal-dialog modal-dialog-custom" style="width:500px">
				<div class="modal-content">
					<div class="modal-header">
				    	<h5 class="modal-title">Warning</h5>
				    </div>
				    <div class="modal-body">
				    	<div id="warningContent">Since effective date no change, update mode will change to "CORRECTION".</div>
				    </div>
				    <div class="modal-footer">
				    	<button type="button" class="btn btn-primary" style="width:110px" data-dismiss="modal" onclick="confirmChangeMode()">OK</button>
					</div>
				</div>
			</div>
		</div>
		
		<div id="invalidEffectiveDate" class="modal fade" role="dialog">
			<div class="modal-dialog modal-dialog-custom" style="width:500px">
				<div class="modal-content">
					<div class="modal-header">
				    	<h5 class="modal-title">Error</h5>
				    </div>
				    <div class="modal-body">
				    	<div id="warningContent">Invalid effective date, another record with inputted effective date exist.</div>
				    </div>
				    <div class="modal-footer">
				    	<button type="button" class="btn btn-default" data-dismiss="modal">OK</button>
					</div>
				</div>
			</div>
		</div>
		
		<div id="reasonMissingModal" class="modal fade" role="dialog">
			<div class="modal-dialog modal-dialog-custom" style="width:500px">
				<div class="modal-content">
					<div class="modal-header">
				    	<h5 class="modal-title">Error</h5>
				    </div>
				    <div class="modal-body">
				    	<div id="warningContent">Reason cannot be empty.</div>
				    </div>
				    <div class="modal-footer">
				    	<button type="button" class="btn btn-default" data-dismiss="modal">OK</button>
					</div>
				</div>
			</div>
		</div>
		
		<div id="modelMPRSPostFound" class="modal fade" role="dialog">
			<div class="modal-dialog modal-dialog-custom" style="width:500px">
				<div class="modal-content">
					<div class="modal-header">
				    	<h5 class="modal-title">Error</h5>
				    </div>
				    <div class="modal-body">
				    	<div id="warningContent">MPRS Post found and cannot set the headcount to zero.</div>
				    </div>
				    <div class="modal-footer">
				    	<button type="button" class="btn btn-default" data-dismiss="modal">OK</button>
					</div>
				</div>
			</div>
		</div>
		<!--  Warning Model - End -->
		
		<!-- Model to confirm delete valid grade- Start -->
		<div id="model_confirmDeleteValidGrade" class="modal fade" role="dialog">
			<div class="modal-dialog modal-dialog-postId">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-body">
						<div class="row" style="padding:20px">
							<div class="col-sm-12">
								<label for="" class="field_request_label">Are you sure to delete this valid grade information?</label>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" style="width:110px" 
							data-dismiss="modal" onclick="confirmDeleteValidGrade();"><i class="fa fa-check"></i> Yes</button>
						<button type="button" class="btn btn-default" style="width:110px"
							data-dismiss="modal"><i class="fa fa-times"></i> Cancel</button>
					</div>
				</div>
			</div>
		</div>
		<!-- ./#model_confirmDeleteValidGrade -->

	</div> <!-- ./contain -->
</div> <!-- ./page-content-wrapper -->

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>

