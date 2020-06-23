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
		document.location.href = "<%= request.getContextPath() %>/request/changeHCMPost";
	}

	var canEditFinancialInfo = "${formBean.canEditFinancialInfo}";
	var canEditDetailInfo = "${formBean.canEditDetailInfo}";

	function validateForm() {
		var errMsg = "";
	
		if ($("#tblChangeHCMPost tbody tr td").html() == "No record found." || $("#tblChangeHCMPost tbody tr").size() == 0) {
			errMsg += "Please select at least one post.<br/>";
		}
		
		if (canEditFinancialInfo == "Y") {
			for (var i=0; i< $("#tblChangeHCMPost tbody tr").size(); i++) {
				if ($($("input[name='requestFundSrc1st']")[i]).val() == "") {
					errMsg += "Please input the detail funding information.<br/>";	
				}
			}
		}
	
		if (canEditDetailInfo == "Y") {
			for (var i=0; i< $("#tblChangeHCMPost tbody tr").size(); i++) {
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
	
	function editPost(mprspostNo, postDurationDesc, hcmPositionId) {			
		$('#ddl_annual_plan_ind').val("");
		$('#ddl_program_year').val("");
		$('#txt_program_code').val("");
		$('#txt_program_name').val("");
		$('#programType').val("");
		$('#txt_program_remark').val("");
		$('#fund_remark').val("");
		$('#txt_fund_cost_code').val("");
		
		$('#hiddenMPRSPostNo').val(mprspostNo);

		// Set the default staff group
		$("#MPRSPostFundingResultModel #tmpStaffGroup").val($("#tblChangeHCMPost tbody tr[id='postNO_"+mprspostNo+"'] input[name='staffGroup']").val());
		
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
	
	function resetValidation() {
		// Reset all date field checking (to perform revalidate)
	}
	
	function savePostFunding() {
		// Reset all date field checking (to perform revalidate)
		resetValidation();
	
		var validator = $("#frmDialog").data('bootstrapValidator');
		validator.validate();
		if (!validator.isValid()) {
			return;
		}
		
		var editMPRSPostNo = $('#hiddenMPRSPostNo').val();
		if (editMPRSPostNo!= null && editMPRSPostNo!="") {
			var duration = $("#tblChangeHCMPost tbody tr[id='postNO_"+editMPRSPostNo+"'] td:eq(2)").text().trim();
			var fte = $("#tblChangeHCMPost tbody tr[id='postNO_"+editMPRSPostNo+"'] td:eq(3)").text().trim();
			var hcmPositionId = $("#tmpHCMPositionId").val();
			var hcmPositionName = $("#tmpHCMPositionName").val();
			var staffGroup = $("#tmpStaffGroup").val();
			$("#tblChangeHCMPost tbody tr[id='postNO_"+editMPRSPostNo+"'] input[name='staffGroup']").val(staffGroup);
			$("#tblChangeHCMPost tbody tr[id='postNO_"+editMPRSPostNo+"'] input[name='requestHcmPositionId']").val(hcmPositionId);
			$("#tblChangeHCMPost tbody tr[id='postNO_"+editMPRSPostNo+"'] td:eq(5)").text(hcmPositionName);
			
			$('#btn_editPost_'+editMPRSPostNo).attr("onclick", "editPost('" + editMPRSPostNo + "', '" + duration + "', '" + hcmPositionId + "')");					
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
            url: "<%=request.getContextPath()%>/api/request/changeHCMPost/searchPost",
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
							html += "'" + data.postResponse[i].postFte + "', '" + data.postResponse[i].hcmPositionName + "')\">" + data.postResponse[i].postId + "</a></td>"
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
						"emptyTable":"No data found, for create new HCM post, please press <a href='/mprs/hcm/createHCMPost'>here</a>.</td>"
					}
				});
				
				
				$("#tblSearchResult").show();
			},
			error : function(request, status, error) {
				//Ajax failure
				alert("Some problem occur during call the ajax: "
						+ request.responseText);
			}
		});
	}

	function selectPost(postUid, postId, rankDesc, postDurationDesc, fte, hcmPositionName) {		
		// Check is Post ID exist 
		for (var i=0; i<$("#tblChangeHCMPost tbody tr").length; i++) {
			if ($($($("#tblChangeHCMPost tbody tr")[i]).find("td")[0]).text().trim() == postId) {
			
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
							if ($("#tblChangeHCMPost tbody tr td").html() == "No record found.") {
								$("#tblChangeHCMPost tbody").remove();
							}
							
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
									+ postUid + "', '" + postDurationDesc + "', '"
									+ "')\" style='margin-right:5px;'>Edit</button><button type='button' class='btn btn-primary' onclick='$(this).parent().parent().remove();'><i class='fa fa-times'></i> Remove</button></td>";
							
							row += "<input type='hidden' name='staffGroup' value='" + nvl(dataPostDetail.staffGroupCode) + "'/>";
							row += "<input name='requestHcmPositionId' value='" + "' type='hidden'/>";
							
							
							row += "</td></tr></tbody>";
							if ($("#tblChangeHCMPost tbody").length == 0) {
								$("#tblChangeHCMPost thead").after(row);
							}
							else {
								$("#tblChangeHCMPost tbody:last").after(row);
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

		if ($("#tblChangeHCMPost tbody tr").length == 0) {
			$("#tblChangeHCMPost thead").after("<tbody><tr><td colspan='7'>No record found.</td></tr></tbody>");
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
				$("button[name='btnEdit']").text("View");	// Added for ST08750
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
		}
		else {
			$("#starProgramCode").show();
			$("#starYear").show();
			$("#starName").show();
		}
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
				hcmPositionName: "",
				allowSingleIncumbent: "N",
				requestType: "CHG_HCM"
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
		// Check is the post is frozen
		if (status == "Frozen") {
			$("#warningTitle").html("Error");
           	$("#warningContent").html("Selected HCM Position is in Frozen status.");
           	$("#warningModal").modal("show");
			
			return;
		}

		// Check is the post is FTE and Head count
		if (parseInt(fte) < 0 || parseInt(headcount) < 0) {
			$("#warningTitle").html("Error");
           	$("#warningContent").html("Invalid Position FTE/Headcount, please update HCM Position.");
           	$("#warningModal").modal("show");

			return;
		}
	
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
		if ($("#tblChangeHCMPost tbody tr").size() > 0 && $("#tblChangeHCMPost tbody tr td").html() != "No record found.") {
			$("#effectiveDate").attr("disabled", true);
		}
		else {
			$("#effectiveDate").attr("disabled", false);
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
			<a href='<c:url value="/home/home"/>'><i class="fa fa-home"></i>Home</a> > Request > Change HCM Position of Post 
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
			<div class="title pull-left"><i class="fa fa-file-text-o"></i>Change HCM Position of Post</div>
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
					<div class="panel-heading-title">Post to be updated</div>
				</div>
				<div class="panel-body" style="padding-top:5px">
					<table id="tblChangeHCMPost" class="table-bordered mprs_table">
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
								items="${formBean.requestPositionList}">
								<tr id="postNO_${listValue.postNo}">
									<td><input type="hidden" name="requestPostNo" value="${listValue.postNo}">${listValue.postId}</td>
									<td>${listValue.rank.rankName}</td>
									<td>${listValue.postDuration == "R"?"Recurrent":listValue.postDuration == "TLC"?"Time Limited - Contract":listValue.postDuration == "TLT"?"Time Limited - Temporary":""}</td>
									<td>${listValue.postFTE}</td>
									<td>${listValue.oriHcmPositionName}</td>
									<td>${listValue.hcmPositionName}</td>
									<td style='text-align: center'>
										<button type="button" id="btn_editPost_${listValue.postNo}" name="btnEdit" class="btn btn-primary" style="margin-right:5px;" onclick="editPost('${listValue.postNo}', '${listValue.postDuration}', '${listValue.hcmPositionId}')">Edit</button>
										<button type="button" name="btnChildRemove" class="btn btn-primary" onclick='$(this).parent().parent().remove();enableEffectiveDate();'><i class="fa fa-times"></i> Remove</button>
										<input name="staffGroup" value="${listValue.staffGroupCode}" type="hidden"/>
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
				<div id="tab_details" class="">
					<ul class="nav nav-pills">
						<li class="active"><a href="#tab0_basic_1" data-toggle="tab">Basic Information</a></li>
					</ul>
					<div class="tab-content clearfix"
						style="margin-bottom: 5px; padding-bottom: 0px">
						<div class="tab-pane active" id="tab0_basic_1">
							<input type="hidden" id="tmpHCMPositionId"/>
							<input type="hidden" id="tmpHCMPositionName"/>
							
							<form:select path="formBean.tmpStaffGroup" id="tmpStaffGroup"
								    	 name="tmpStaffGroup"
										 class="form-control" style="display:none">
								<form:option value="" label="- Select -" />
								<form:options items="${staffGroupList}" />
							</form:select>
							
							<div id="searchPanel">
								<!-- Search Existing HCM Position Record -->
								<strong>Search Existing HCM Position Record</strong>
								<div>
									<div class="panel panel-info">
										<div class="panel-heading">
											<a role="button" data-target="#searchCriteria" class="panel-title" data-toggle="collapse">Searching Criteria</a>
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