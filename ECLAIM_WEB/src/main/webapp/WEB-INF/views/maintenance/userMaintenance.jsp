<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<script>
	function redirectToIDMan() {
		// Check whether is user existing in other cluster
		$.ajax({
		 	url: "<%=request.getContextPath() %>/maintenance/checkUserExisting",
            type: "POST",
            data: {userId: $("#hiddenUserId").val()},
            success: function(data) {
            	if (data == "N" || $("#hiddenUserId").val() == '') {
            		$.ajax({
					 	url: "<%=request.getContextPath() %>/maintenance/genCidsLogonToken",
			            type: "POST",
			            data: {userId: $("#hiddenUserId").val()},
			            success: function(rawData) {
			            	myWindow=$(window.open('<c:out value="${formBean.cidLoginUrl}"/>/function_call/func_check_key.asp?unique_id=' + rawData.token + '&target_app=add_user&duplicate_id=Y','IDMAN','width=960,height=500'));
						}		
					});
            	}
            	else {
            		$("#warningTitle").html("Error");
			        $("#warningContent").html("User already exists in other cluster.");
			        $("#warningModal").modal("show");
            	}
            }
        });
	}
		
	function performSearch() {
		$("#formAction").val("");
	}
	
	function performReset() {
		document.location.href = "<%= request.getContextPath() %>/maintenance/userMaintenance";
	}
	
	function changeCluster(obj, instCode) {
		if ($(obj).val() == "") {
			var instDD = $(obj).parent().parent().find("select[name='ddInstitute']");
	        instDD.empty();
	        var option = "<option value=''> - Select - </option>";
	        instDD.append(option);
		}
		else {
			$.ajax ({
	            url: "<%=request.getContextPath() %>/maintenance/getInstitution",
	            type: "POST",
	            data: {clusterCode: $(obj).val()},
	            success: function(data) {
	            	var instDD = $(obj).parent().parent().find("select[name='ddInstitute']");
	            	instDD.empty();
	            	var option = "<option value=''> - Select - </option>";
	            	instDD.append(option);
	            	 
	            	for (var i=0; i<data.instList.length; i++) {
		            	option = "<option value='" + data.instList[i].instCode + "'>" + data.instList[i].instCode + "</option>";
		            	instDD.append(option);
	            	}
	            	
	            	if (instCode != null) {
	            		instDD.val(instCode);
	            		
	            		if (instDD.val() == null) {
	            			instDD.append("<option value='" + instCode + "'>" + instCode + "</option>");
	            			instDD.val(instCode);
	            		}
	            	}
	            },
	            error: function(request, status, error) {
	                //Ajax failure
	                alert("Some problem occur during call the ajax: " + request.responseText);
	            }
	        });
		}
	}
	
	function editRole(userId, showUser) {
		showLoading();
		var previousRole = "";
		var previousClusterCode = "";
		var previousInstCode = "";
		var previousDeptCode = "";
		var previousStaffGroupCode = "";
		
		$.ajax({
            url: "<%=request.getContextPath() %>/maintenance/getUserRoleDetail",
            type: "POST",
            data: {userId: userId},
            success: function(rawData) {
            	$("#hcmResponsibilityCount").val(rawData.responsibilityCount);
            	$("input[name='updateUserId']").val(userId);
            	$("#tblRole tbody tr").remove();
            	
            	$.ajax({
		            url: "<%=request.getContextPath() %>/maintenance/getCommonDropdown",
		            type: "POST",
		            success: function(data) {
		            	var roleOption = "";
		            	var clusterOption = "";
		            	var instOption = "";
		            	var deptOption = "";
		            	var staffGroupOption = "";
		            	
		            	for (var i=0; i<data.roleList.length; i++) {
			            	roleOption += "<option value='" + data.roleList[i].roleId + "'>" + data.roleList[i].roleName + "</option>";
		            	}
		            	
		            	for (var i=0; i<data.clusterList.length; i++) {
			            	clusterOption += "<option value='" + data.clusterList[i].clusterCode + "'>" + data.clusterList[i].clusterCode + "</option>";
		            	}
			            	 
		            	for (var i=0; i<data.instList.length; i++) {
			            	instOption += "<option value='" + data.instList[i].instCode + "'>" + data.instList[i].instCode + "</option>";
		            	}
		            	 
		            	for (var i=0; i<data.staffGroupList.length; i++) {
			            	staffGroupOption += "<option value='" + data.staffGroupList[i].staffGroupCode + "'>" + data.staffGroupList[i].staffGroupName + "</option>";
		            	}
			            	 
		            	for (var i=0; i<data.deptList.length; i++) {
			            	deptOption += "<option value='" + data.deptList[i].deptCode + "'>" + data.deptList[i].deptName + "</option>";
		            	}
		            
		            	for (var i=0; i<rawData.roleCount; i++) {
							var row = "<tr>";
							row += "<td><select name='ddRoleId' class='form-control' style='width:100%;' onchange='enableButton();changeRole(this)' /> </td>";
							row += "<td><select name='ddCluster' class='form-control' style='width:100%;' onchange='changeCluster(this);enableButton()'/> </td>";
							row += "<td><select name='ddInstitute' class='form-control' style='width:100%;' onchange='enableButton()' /> </td>";
							row += "<td><select name='ddDept' class='form-control' style='width:100%;' onchange='enableButton()' /> </td>";
							row += "<td><select name='ddStaffGroup' class='form-control' style='width:100%;' onchange='enableButton()' /> </td>";
							row += "<td><a class='btn btn-danger' href='#' onclick='$(this).parent().parent().remove();enableButton()'><i class='fa fa-trash-o fa-sm'></i> Delete</a></td>";
							row += "</tr>";
							$("#tblRole tbody").append(row);
							
	 						var newRowIdx = $("#tblRole tr").length-2;
							
							var option = "<option value=''> - Select - </option>";
            				$($("select[name='ddRoleId']")[newRowIdx]).append(option);
			            	$($("select[name='ddCluster']")[newRowIdx]).append(option);
			            	$($("select[name='ddInstitute']")[newRowIdx]).append(option);
			            	$($("select[name='ddStaffGroup']")[newRowIdx]).append(option);
			            	$($("select[name='ddDept']")[newRowIdx]).append(option);
			            	
			            	$($("select[name='ddRoleId']")[newRowIdx]).append(roleOption);
			            	$($("select[name='ddCluster']")[newRowIdx]).append(clusterOption);
			            	$($("select[name='ddInstitute']")[newRowIdx]).append(instOption);
			            	$($("select[name='ddStaffGroup']")[newRowIdx]).append(staffGroupOption);
			            	$($("select[name='ddDept']")[newRowIdx]).append(deptOption);
			            	
			            	var disableRow = false;
			            	$($("select[name='ddRoleId']")[newRowIdx]).val(rawData.roleIdList[i]);
			            	if ($($("select[name='ddRoleId']")[newRowIdx]).val() == null) {
			            		$($("select[name='ddRoleId']")[newRowIdx]).append("<option value='" + rawData.roleIdList[i] + "'>" + rawData.roleIdList[i] + "</option>");
								$($("select[name='ddRoleId']")[newRowIdx]).val(rawData.roleIdList[i]);
								disableRow = true;
			            	}
			            	
			            	$($("select[name='ddCluster']")[newRowIdx]).val(rawData.clusterCodeList[i]);
			            	if ($($("select[name='ddCluster']")[newRowIdx]).val() == null) {
			            		$($("select[name='ddCluster']")[newRowIdx]).append("<option value='" + rawData.clusterCodeList[i] + "'>" + rawData.clusterCodeList[i] + "</option>");
								$($("select[name='ddCluster']")[newRowIdx]).val(rawData.clusterCodeList[i]);
								disableRow = true;
			            	}
			            	
			            	$($("select[name='ddInstitute']")[newRowIdx]).val(rawData.instCodeList[i]);
			            	if ($($("select[name='ddInstitute']")[newRowIdx]).val() == null) {
								disableRow = true;
			            	}
			            	
			            	$($("select[name='ddStaffGroup']")[newRowIdx]).val(rawData.staffGroupCodeList[i]);
			            	$($("select[name='ddDept']")[newRowIdx]).val(rawData.deptCodeList[i]);
			            	
			            	// If hospital user admin, if hospital is empty, all disable the row
			            	if (data.defaultRole == "HOSP_USER_ADM") {
			            		if ($($("select[name='ddInstitute']")[newRowIdx]).val() == "") {
			            			disableRow = true;
			            		}
			            	}
			            	
							if (disableRow) {			            	
				            	// Disable row
								$($("select[name='ddRoleId']")[newRowIdx]).parent().parent().find("select").attr("disabled", true);
								$($("select[name='ddRoleId']")[newRowIdx]).parent().parent().find("a").attr("disabled", true);
								$($("select[name='ddRoleId']")[newRowIdx]).parent().parent().find("a").css("pointer-events", "none");
							}
							
							if (rawData.roleIdList[i] == "FIN_MANAGER" || rawData.roleIdList[i] == "FIN_OFFICER" || rawData.roleIdList[i] == "HR_MANAGER" || rawData.roleIdList[i] == "HR_OFFICER") {
								$($("select[name='ddRoleId']")[newRowIdx]).parent().parent().find("select[name='ddDept']").attr("disabled", true);
								$($("select[name='ddRoleId']")[newRowIdx]).parent().parent().find("select[name='ddStaffGroup']").attr("disabled", true);
							}
							else if (rawData.roleIdList[i] == "HOSP_USER_ADM") {
								$($("select[name='ddRoleId']")[newRowIdx]).parent().parent().find("select[name='ddDept']").attr("disabled", true);
								$($("select[name='ddRoleId']")[newRowIdx]).parent().parent().find("select[name='ddStaffGroup']").attr("disabled", true);
							}
							else if (rawData.roleIdList[i] == "CLUS_USER_ADM") {
								$($("select[name='ddRoleId']")[newRowIdx]).parent().parent().find("select[name='ddInstitute']").attr("disabled", true);
								$($("select[name='ddRoleId']")[newRowIdx]).parent().parent().find("select[name='ddDept']").attr("disabled", true);
								$($("select[name='ddRoleId']")[newRowIdx]).parent().parent().find("select[name='ddStaffGroup']").attr("disabled", true);
							}
							else {
								if (!disableRow) {
									$($("select[name='ddRoleId']")[newRowIdx]).parent().parent().find("select[name='ddDept']").attr("disabled", false);
									$($("select[name='ddRoleId']")[newRowIdx]).parent().parent().find("select[name='ddStaffGroup']").attr("disabled", false);
								}
							}
			            	
			            	changeCluster($($("select[name='ddCluster']")[newRowIdx]), rawData.instCodeList[i]);
			            	
			            	if (previousRole == rawData.roleIdList[i]) {
			            		$($("select[name='ddRoleId']")[newRowIdx]).hide();
			            		
				            	if (previousClusterCode == rawData.clusterCodeList[i]) {
				            		$($("select[name='ddCluster']")[newRowIdx]).hide();
				            		
					            	if (previousInstCode == rawData.instCodeList[i]) {
					            		$($("select[name='ddInstitute']")[newRowIdx]).hide();
					            	}
				            	}
			            	}
			            		
			            	//if (previousDeptCode == rawData.deptCodeList[i])
			            	//	$($("select[name='ddDept']")[newRowIdx]).hide();
			            	
			            	previousRole = rawData.roleIdList[i];
							previousClusterCode = rawData.clusterCodeList[i];
							previousInstCode = rawData.instCodeList[i];
							previousDeptCode = rawData.deptCodeList[i];
							previousStaffGroupCode = rawData.staffGroupCodeList[i];
						}
						
		            	hideLoading();
		            	
		            	if (!showUser) {
			            	$("#divRoleInfo").show();
			            	$("#divUserInfo").hide();
			            	$("#divBtnRoleInfo").show();
			            	$("#divBtnUserInfo").hide();
			            	$("#divBtnAllInfo").hide();
	        		    	$("#roleDetailModel").modal("show");
        		    	}
        		    	else {
        		    		$("#divRoleInfo").show();
			            	$("#divUserInfo").show();
			            	$("#divBtnRoleInfo").show();
			            	$("#divBtnUserInfo").hide();
			            	$("#divBtnAllInfo").hide();
	        		    	$("#roleDetailModel").modal("show");
        		    	}
            		}
            	});
			}
		});
	}
	
	function editProfile(userId) {
		$.ajax({
            url: "<%=request.getContextPath() %>/maintenance/getUserProfileDetail",
            type: "POST",
            data: {userId: userId},
            success: function(data) {
            	$("#formSearchCriteria").data('bootstrapValidator').enableFieldValidators('editUserName', false);
            	$("#formSearchCriteria").data('bootstrapValidator').enableFieldValidators('editPhone', false);
            	$("#formSearchCriteria").data('bootstrapValidator').enableFieldValidators('editEmail', false);
            
				$("#editUserId").val(data.userId);
				$("#editUserId").attr("readonly", "readonly");
				$("#editUserName").val(data.userName);
				$("#editPhone").val(data.phone);
				$("#editEmail").val(data.email);
			
				$("#formSearchCriteria").data('bootstrapValidator').enableFieldValidators('editUserName', true);
            	$("#formSearchCriteria").data('bootstrapValidator').enableFieldValidators('editPhone', true);
            	$("#formSearchCriteria").data('bootstrapValidator').enableFieldValidators('editEmail', true);
			
		      	$("#editUserName").attr("disabled", false);
				$("#editPhone").attr("disabled", false);
				$("#editEmail").attr("disabled", false);
				
				$("#divRoleInfo").hide();
		        $("#divUserInfo").show();
		        $("#divBtnRoleInfo").hide();
		        $("#divBtnUserInfo").show();
		        $("#divBtnAllInfo").hide();            	
            	$("#roleDetailModel").modal("show");
			}
		});
	}
	
	function changeRole(obj) {
		updateClusterList(obj);
		
		if ($(obj).val() == "FIN_MANAGER" || $(obj).val() == "FIN_OFFICER" || $(obj).val() == "HR_MANAGER" || $(obj).val() == "HR_OFFICER") {
			$(obj).parent().parent().find("select[name='ddDept']").attr("disabled", true);
			$(obj).parent().parent().find("select[name='ddStaffGroup']").attr("disabled", true);
		}
		else if ($(obj).val() == "HOSP_USER_ADM") {
			$(obj).parent().parent().find("select[name='ddDept']").attr("disabled", true);
			$(obj).parent().parent().find("select[name='ddStaffGroup']").attr("disabled", true);
		}
		else if ($(obj).val() == "CLUS_USER_ADM") {
			$(obj).parent().parent().find("select[name='ddInstitute']").attr("disabled", true);
			$(obj).parent().parent().find("select[name='ddDept']").attr("disabled", true);
			$(obj).parent().parent().find("select[name='ddStaffGroup']").attr("disabled", true);
		}
		else {
			$(obj).parent().parent().find("select[name='ddDept']").attr("disabled", false);
			$(obj).parent().parent().find("select[name='ddStaffGroup']").attr("disabled", false);
		}
	}
	
	function updateClusterList(obj){
		$.ajax ({
	            url: "<%=request.getContextPath() %>/maintenance/getCluster",
	            type: "POST",
	            data: {addRoleCode: $(obj).val()},
	            success: function(data) {
	            	var clusterDD = $(obj).parent().parent().find("select[name='ddCluster']");
	            	clusterDD.empty();
	            	var option = "<option value=''> - Select - </option>";
	            	clusterDD.append(option);
	            	 
	            	for (var i=0; i<data.clusterList.length; i++) {
		            	option = "<option value='" + data.clusterList[i].clusterCode + "'>" + data.clusterList[i].clusterCode + "</option>";
		            	clusterDD.append(option);
	            	}
	            	
	            },
	            error: function(request, status, error) {
	                //Ajax failure
	                alert("Some problem occur during call the ajax: " + request.responseText);
	            }
	        });
	}
	
	function addRow(val, display, clusterCode, instCode, deptCode, staffGroupCode) {
		var previousRole = "";
		var previousClusterCode = "";
		var previousInstCode = "";
		var previousDeptCode = "";
		var previousStaffGroupCode = "";
	
		$.ajax({
            url: "<%=request.getContextPath() %>/maintenance/getCommonDropdown",
            type: "POST",
            success: function(data) {
				var row = "<tr>";
				row += "<td><select name='ddRoleId' class='form-control' style='width:100%;' onchange='enableButton();changeRole(this)'/></td>";
				row += "<td><select name='ddCluster' class='form-control' style='width:100%;' onchange='changeCluster(this);enableButton()'/> </td>";
				row += "<td><select name='ddInstitute' class='form-control' style='width:100%;' onchange='enableButton()' /> </td>";
				row += "<td><select name='ddDept' class='form-control' style='width:100%;' onchange='enableButton()' /> </td>";
				row += "<td><select name='ddStaffGroup' class='form-control' style='width:100%;' onchange='enableButton()' /> </td>";
				row += "<td><a class='btn btn-danger' href='#' onclick='$(this).parent().parent().remove();enableButton()'><i class='fa fa-trash-o fa-sm'></i> Delete</a></td>";
				row += "</tr>";
				$("#tblRole tbody").append(row);
				
				var newRowIdx = $("#tblRole tr").length-2;
				
				var option = "<option value=''> - Select - </option>";
            	$($("select[name='ddRoleId']")[newRowIdx]).append(option);
            	$($("select[name='ddCluster']")[newRowIdx]).append(option);
            	$($("select[name='ddInstitute']")[newRowIdx]).append(option);
            	$($("select[name='ddStaffGroup']")[newRowIdx]).append(option);
            	$($("select[name='ddDept']")[newRowIdx]).append(option);
            	 
            	for (var i=0; i<data.roleList.length; i++) {
	            	option = "<option value='" + data.roleList[i].roleId + "'>" + data.roleList[i].roleName + "</option>";
	            	$($("select[name='ddRoleId']")[newRowIdx]).append(option);
            	}
            	
            	for (var i=0; i<data.clusterList.length; i++) {
	            	option = "<option value='" + data.clusterList[i].clusterCode + "'>" + data.clusterList[i].clusterCode + "</option>";
	            	$($("select[name='ddCluster']")[newRowIdx]).append(option);
            	}
            	 
            	for (var i=0; i<data.instList.length; i++) {
	            	option = "<option value='" + data.instList[i].instCode + "'>" + data.instList[i].instCode + "</option>";
	            	$($("select[name='ddInstitute']")[newRowIdx]).append(option);
            	}
            	 
            	for (var i=0; i<data.staffGroupList.length; i++) {
	            	option = "<option value='" + data.staffGroupList[i].staffGroupCode + "'>" + data.staffGroupList[i].staffGroupName + "</option>";
	            	$($("select[name='ddStaffGroup']")[newRowIdx]).append(option);
            	}
            	 
            	for (var i=0; i<data.deptList.length; i++) {
	            	option = "<option value='" + data.deptList[i].deptCode + "'>" + data.deptList[i].deptName + "</option>";
	            	$($("select[name='ddDept']")[newRowIdx]).append(option);
            	}
            	
            	$($("select[name='ddCluster']")[newRowIdx]).val(clusterCode);
            	$($("select[name='ddInstitute']")[newRowIdx]).val(instCode);
            	$($("select[name='ddStaffGroup']")[newRowIdx]).val(staffGroupCode);
            	$($("select[name='ddDept']")[newRowIdx]).val(deptCode);
            	
           		changeCluster($($("select[name='ddCluster']")[newRowIdx]), instCode);
			}
		});
	}
	
	$(function() {
		$.ajaxSetup({cache: false});
		$("#editUserName").attr("disabled", true);
		$("#editPhone").attr("disabled", true);
		$("#editEmail").attr("disabled", true);
		$("#userId").blur(function() {
			$("#userId").val($.trim($("#userId").val()).toUpperCase());
		});

		$("#tblSearchResult").dataTable({
			"columnDefs": [ {
				"targets": 'no-sort',
				"orderable": false,
			}]
		});
		
		if ($("#domainUserId").val() != "") {
			if ($("#isExistingUserInOtherCluster").val() == "Y") {
				$("#warningTitle").html("Error");
			    $("#warningContent").html("User already exists in other cluster.");
			    $("#warningModal").modal("show");
			}
			else {
				// Check is existing user
				if ($("#isExistingUser").val() == "Y") {
					editRole($("#domainUserId").val(), true);
					
					$("#editUserId").val($("#domainUserId").val());
					$("#editUserId").attr("readonly", "readonly");
					$("#editUserName").attr("disabled", true);
					$("#editPhone").attr("disabled", true);
					$("#editEmail").attr("disabled", true);
				}
				else {
					// Get the ResponsibilityCount
					$.ajax({
			            url: "<%=request.getContextPath() %>/maintenance/getResponsibilityCount",
			            type: "POST",
			            data: {userId: $("#domainUserId").val()},
			            success: function(rawData) {
			            	$("#hcmResponsibilityCount").val(rawData);
	        
							$("#editUserId").val($("#domainUserId").val());
							$("#editUserId").attr("readonly", "readonly");
							
							$("#divRoleInfo").show();
						    $("#divUserInfo").show();
						    $("#divBtnRoleInfo").hide();
						    $("#divBtnUserInfo").hide();
						    $("#divBtnAllInfo").show();
						    
						    $("#editUserName").attr("disabled", false);
							$("#editPhone").attr("disabled", false);
							$("#editEmail").attr("disabled", false);
							
							$("#roleDetailModel").modal("show");
						}
					});
				}
			}
		}
		
		$("#formSearchCriteria").bootstrapValidator({
			excluded: [':disabled'],
			message: ' This value is not valid',
			live: "submitted",
			fields: {
				editEmail : {
					validators : {
						emailAddress: {
							message: 'The value is not a valid email address'
						},
					}
				},
				editPhone: {
					validators : {
						callback: {
							message: 'Inputted value is invalid',
							callback: function(value, validator, $field) {
								if (value.match(/^[\d \/]*$/g) == null) {
									return {
										valid: false,
										message: 'Inputted value is invalid'
									}
								}
								
								return true; 
							}
						}
					}
				}
			},
		}).on('success.form.bv', function(e){
			if (!performChecking()) {
				e.preventDefault();
			}
			else {
				showLoading();
				
				// Enable all field 
				$("select").attr("disabled", false);
				$("input").attr("disabled", false);
			}
		});
		
		<c:choose>
			<c:when test="${formBean.updateSuccess == 'Y'}">
				$("#divSuccessMsg").html('<c:out value="${formBean.displayMessage}"/>');
				$("#divSuccess").show();
				$("#divHCMDetail").show();
			</c:when>
			<c:when test="${formBean.updateSuccess == 'N'}">
				$("#divErrorMsg").html('<c:out value="${formBean.displayMessage}"/>');
				$($("div[class='alert-box-icon-danger']")[0]).css("height", $("#divError").height())
				$("#divError").show();
			</c:when>
		</c:choose>
	});
	
	function performChecking() {
		if ($("#divBtnUserInfo").css("display") == "none" && $("#roleDetailModel").css("display") == "block") {
			if ($("#divBtnAllInfo").css("display") == "block") {
				if ($("select[name='ddRoleId']").length == 0) {
					$("#warningTitle").html("Error");
			        $("#warningContent").html("Please input as least one role.");
			        $("#warningModal").modal("show");
					return false;
				}
			}
			
			var roleList =[];
		
			for (var i=0; i<$("select[name='ddRoleId']").length; i++) {
				var tempRow = "";
				tempRow += $($("select[name='ddRoleId']")[i]).val();
				tempRow += $($("select[name='ddCluster']")[i]).val();
				tempRow += $($("select[name='ddInstitute']")[i]).val();
				tempRow += $($("select[name='ddDept']")[i]).val();
				tempRow += $($("select[name='ddStaffGroup']")[i]).val();
				
				//if(!roleList.includes(tempRow)){
				if(roleList.indexOf(tempRow) == -1){
					roleList.push(tempRow);
				}
				else{
					$("#warningTitle").html("Error");
			        $("#warningContent").html("Duplicate existing role setting.");
			        $("#warningModal").modal("show");
					return false;
				}
				
				if ($($("select[name='ddRoleId']")[i]).val() == "" && $($("select[name='ddRoleId']")[i]).prop("disabled") == false) {
					$("#warningTitle").html("Error");
			        $("#warningContent").html("Role cannot be empty.");
			        $("#warningModal").modal("show");
					return false;
				}
			
				if ($($("select[name='ddRoleId']")[i]).val().substring(0,2) != "HO" && $($("select[name='ddCluster']")[i]).val() == "" && $($("select[name='ddCluster']")[i]).prop("disabled") == false) {
					$("#warningTitle").html("Error");
			        $("#warningContent").html("Cluster cannot be empty.");
			        $("#warningModal").modal("show");
					return false;
				}
				
				if ($($("select[name='ddInstitute']")[i]).prop("disabled") == false) {
					if ($($("select[name='ddInstitute']")[i]).val() == "" && ($($("select[name='ddRoleId']")[i]).val() == "HOSP_USER_ADM")) {
						$("#warningTitle").html("Error");
				        $("#warningContent").html("Institution cannot be empty for Hospital User Admin.");
				        $("#warningModal").modal("show");
						return false;
					}
				
					
					// If "Hospital User Admin" add any row, "Institute" should be mandatory
					if ($($("select[name='ddInstitute']")[i]).val() == "" && ($("#currentRole").val() == "HOSP_USER_ADM")) {
						$("#warningTitle").html("Error");
				        $("#warningContent").html("Institution cannot be empty.");
				        $("#warningModal").modal("show");
						return false;
					}
				}
			
				if ($($("select[name='ddRoleId']")[i]).val() == "HR_MANAGER" || $($("select[name='ddRoleId']")[i]).val() == "HR_OFFICER") {
					for (var j=0; j<$("select[name='ddRoleId']").length; j++) {
						if (i==j) {
							continue;
						}
							
						if ($($("select[name='ddRoleId']")[i]).val() == "HR_MANAGER" && $($("select[name='ddRoleId']")[j]).val() == "HR_OFFICER") {
							$("#warningTitle").html("Error");
			            	$("#warningContent").html("User cannot have both Manager and Officer role.");
			            	$("#warningModal").modal("show");
							return false;
						}
						
						if ($($("select[name='ddRoleId']")[i]).val() == "HR_OFFICER" && $($("select[name='ddRoleId']")[j]).val() == "HR_MANAGER") {
							$("#warningTitle").html("Error");
			            	$("#warningContent").html("User cannot have both Manager and Officer role.");
			            	$("#warningModal").modal("show");
							return false;
						}
					}
				}
				
				if ($($("select[name='ddRoleId']")[i]).val() == "FIN_MANAGER" || $($("select[name='ddRoleId']")[i]).val() == "FIN_OFFICER") {
					for (var j=0; j<$("select[name='ddRoleId']").length; j++) {
						if (i==j) {
							continue;
						}
							
						if ($($("select[name='ddRoleId']")[i]).val() == "FIN_MANAGER" && $($("select[name='ddRoleId']")[j]).val() == "FIN_OFFICER") {
							$("#warningTitle").html("Error");
			            	$("#warningContent").html("User cannot have both Manager and Officer role.");
			            	$("#warningModal").modal("show");
							return false;
						}
						
						if ($($("select[name='ddRoleId']")[i]).val() == "FIN_OFFICER" && $($("select[name='ddRoleId']")[j]).val() == "FIN_MANAGER") {
							$("#warningTitle").html("Error");
			            	$("#warningContent").html("User cannot have both Manager and Officer role.");
			            	$("#warningModal").modal("show");
							return false;
						}
					}
				}
				
				if ($($("select[name='ddRoleId']")[i]).val() == "HR_OFFICER" && $($("select[name='ddRoleId']")[i]).prop("disabled") == false) {
			    	if ($("#hcmResponsibilityCount").val() == 0) {
	            		$("#warningTitle").html("Error");
		            	$("#warningContent").html("User does not have HCM Position Control responsibility.");
		            	$("#warningModal").modal("show");
	            		return false;
	            	}
				}
			}
		}

		return true;
	}
	
	function performSaveRole() {
		if (performChecking()) {
			$("#formAction").val("SAVE_ROLE");
		}
	}
	
	function performSaveUser() {
		if (performChecking()) {
			$("#formAction").val("SAVE_USER");
		}
	}
	
	function performSaveAll() {
		if (performChecking()) {
			$("#formAction").val("SAVE_ALL");
		}
	}
	
	function enableButton() {
		$("button[type='submit']").attr("disabled", false);
	}
	
	function disableAllField() {
		$("#roleDetailModel input").attr("disabled", true);
	}
	
	function enableSaveButton() {
		$("button[type='submit']").attr("disabled", false);
	}
	
</script>

<!-- Page Content -->
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > Maintenance > User / Role Maintenance
		</div>
		<div id="divSuccess" style="display:none" class="alert-box-success">
			<div class="alert-box-icon-success"><i class="fa fa-check"></i></div>
			<div id="divSuccessMsg" class="alert-box-content-success"></div>
		</div>
		<div id="divError" style="display:none" class="alert-box-danger">
			<div class="alert-box-icon-danger"><i class="fa fa-warning"></i></div>
			<div id="divErrorMsg" class="alert-box-content-danger"></div>
		</div>
		<!-- Function Name -->
		<div class="section-title">
			<div class="title pull-left"><i class="fa fa-users"></i>User / Role Maintenance</div>
		</div>
				  
		<form id="formSearchCriteria" method="POST">
			<form:hidden path="formBean.domainUserId" />
			<form:hidden path="formBean.returnCode" />
			<form:hidden path="formBean.isExistingUser" />
			<form:hidden path="formBean.isExistingUserInOtherCluster" />
			<form:hidden path="formBean.currentRole" />
			<div class="panel panel-custom-warning">
				<div class="panel-heading">
					<div class="panel-heading-title">
						<a role="button" data-target="#searchCriteria" class="panel-title" data-toggle="collapse">Searching Criteria</a>
					</div>
				</div>
				<div id="searchCriteria" class="panel-collapse collapse in">
					<div class="panel-body">
							<div class="row">
								<div class="col-sm-2">
									<label for="" class="field_request_label">User ID</label>
								</div>
								<div class="col-sm-4">
									<form:input path="formBean.searchUserId" id="userId" name="userId" class="form-control" />
								</div>
							</div>
							<div class="row">
								<div class="col-sm-2">
									<label for="" class="field_request_label">User Name</label>
								</div>
								<div class="col-sm-4">
									<form:input path="formBean.searchUserName" name="userName" class="form-control" />
								</div>
							</div>
							
							<div class="row">
								<div class="col-sm-12" style="text-align:right">
									<button type="submit" class="btn btn-primary" name="btnSearch" style="width:110px" 
											onclick="performSearch()"><i class="fa fa-search"></i> Search</button>
									<button type="button" class="btn btn-default" name="btnReset" style="width:110px"
									       	onclick="performReset()"><i class="fa fa-eraser"></i> Clear</button>
								</div>
							</div>
					</div>
				</div>
			</div>
	
		<c:if test="${formBean.haveResult == 'Y'}">
			<div id="divSearchResult">
				<H4>Search Result</H4>
			
				<table id="tblSearchResult" class="display cell-border mprs_table">
					<thead>
						<tr>
							<th>User ID</th>
							<th>User Name</th>
							<th>Phone No.</th>
							<th>Email</th>
							<th>Status</th>
							<th class="no-sort" style="width:200px">Action</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${formBean.searchResultListSize > 0}">
							<c:forEach var="searchResult" items="${formBean.searchResultList}">
							<tr>
								<td><c:out value="${searchResult.userId}"/></td>
								<td><c:out value="${searchResult.userName}"/></td>
								<td><c:out value="${searchResult.phone}"/></td>
								<td><c:out value="${searchResult.email}"/></td>
								<td><c:out value="${searchResult.stateDesc}"/></td>
								<td>
									<a class="btn btn-primary" href="#" onclick="editProfile('<c:out value="${searchResult.userIdStr}"/>')"><i class="fa fa-pencil fa-sm"></i> Edit Profile</a>
									<a class="btn btn-primary" href="#" onclick="editRole('<c:out value="${searchResult.userIdStr}"/>', false)"><i class="fa fa-pencil fa-sm"></i> Edit Role</a>
								</td>
							</tr>
							</c:forEach>
							</c:when>
							
							<c:when test="${formBean.searchResultListSize == 0}" >
								<tr>
									<td colspan="6">No record found, press <a href="#" onclick="javascript:redirectToIDMan()">here</a> to create a new user.</td>
								</tr>
							</c:when>
						</c:choose>
					</tbody>
				</table>
			</div>
		</c:if>
			
			<form:hidden path="formBean.formAction"/>
			<form:hidden path="formBean.userId" id="hiddenUserId"/>
			<div id="roleDetailModel" class="modal fade" role="dialog">
				<div class="modal-dialog modal-dialog-custom" style="width:800px">
					<form:hidden path="formBean.updateUserId"/>
					<form:hidden path="formBean.hcmResponsibilityCount"/>
					<div class="modal-content">
						<div class="modal-header">
							<h4><b>User Role Detail</b>
					    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" aria-label="Close" onclick='enableSaveButton()'>&times;</button>
					    	</h4>
						</div>
					    <div class="modal-body">
						    <div id="divUserInfo">
						    	<div class="row">
						    		<div class="form-group">
										<div class="col-sm-3">
											<label for="" class="control-label">User ID</label>
										</div>
										<div class="col-sm-9">
											<form:input path="formBean.editUserId" name="editUserId" class="form-control" readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="row">
									<div class="form-group">
										<div class="col-sm-3">
											<label for="" class="control-label">User Name<font class="star">*</font></label>
										</div>
										<div class="col-sm-9">
											<form:input path="formBean.editUserName" name="editUserName" class="form-control" required="required"/>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="form-group">
										<div class="col-sm-3">
											<label for="" class="control-label">Phone</label>
										</div>
										<div class="col-sm-9">
											<form:input path="formBean.editPhone" name="editPhone" class="form-control" />
										</div>
									</div>
								</div>
								<div class="row">
									<div class="form-group">
										<div class="col-sm-3">
											<label for="" class="control-label">Email<font class="star">*</font></label>
										</div>
										<div class="col-sm-9">
											<form:input path="formBean.editEmail" name="editEmail" class="form-control" required="required"/>
										</div>
									</div>
								</div>
						    </div>
						    <div id="divRoleInfo">
						    	<table id="tblRole" class="display table-bordered mprs_table">
						    		<thead>
						    			<tr>
						    				<th>Role</th>
						    				<th>Cluster</th>
						    				<th>Institution</th>
						    				<th>Department</th>
						    				<th>Staff Group</th>
						    				<th></th>
						    			</tr>
						    		</thead>
						    		<tbody>
						    			
						    		</tbody>
						    	</table>
						    	<div style="width:100%;text-align:right">
									<button type="button" class="btn btn-primary" onclick="addRow('','','','','','')"><i class="fa fa-plus fa-sm"></i> Add Role</button>
								</div>
							</div>
					    </div>
					    <div class="modal-footer">
					    	<div id="divBtnRoleInfo">
						    	<button type="submit" class="btn btn-primary" style="width:110px" onclick="performSaveRole()"><i class="fa fa-save"></i> Save</button>
						    	<button type="button" class="btn btn-default" data-dismiss="modal" onclick="disableAllField();enableSaveButton()">Close</button>
						    </div>
						    <div id="divBtnUserInfo">
						    	<button type="submit" class="btn btn-primary" style="width:110px" onclick="performSaveUser()"><i class="fa fa-save"></i> Save</button>
					    		<button type="button" class="btn btn-default" data-dismiss="modal" onclick="disableAllField();enableSaveButton()">Close</button>
						    </div>
						    <div id="divBtnAllInfo">
						    	<button type="submit" class="btn btn-primary" style="width:110px" onclick="performSaveAll()"><i class="fa fa-save"></i> Save</button>
					    		<button type="button" class="btn btn-default" data-dismiss="modal" onclick="disableAllField();enableSaveButton()">Close</button>
						    </div>
						</div>
					</div>
				</div>
			</div>
			
		</form>
	</div>
</div>

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>