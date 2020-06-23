<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script>
	function performSearch() {
		showLoading();
		
		$("#searchEffectiveDate").val($("#effectiveDate").val());
		$("#hiddenRequestType").val(typeof requestType == 'undefined' ? "" : requestType);
	
		// Ajax call to perform search
		$.ajax({
            url: "<%=request.getContextPath() %>/api/request/searchVacantPost",
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
							html += "<td><a href=\"javascript:selectPost('" + data.postResponse[i].clusterCode + "', '" + data.postResponse[i].instCode; 
							html += "', '" + data.postResponse[i].postUid + "', '" + data.postResponse[i].postId + "', '" + data.postResponse[i].rankDesc;
							html += "', '" + data.postResponse[i].annualPlanDesc + "', '" + data.postResponse[i].postDurationDesc + "', '" + data.postResponse[i].postFte;
							html += "', '" + data.postResponse[i].deptCode + "', '" + data.postResponse[i].staffGroupCode + "')\">" + data.postResponse[i].postId + "</a></td>";
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
				$("#tblSearchResult thead").after(html);
				
				var oTable = $('#tblSearchResult').dataTable();
				oTable.fnDestroy();
				$('#tblSearchResult').dataTable({
					"language": {
						"emptyTable":"No record found."
					}
				});
				
				$("#tblSearchResult").show();
				
            },
            error: function(request, status, error) {
                //Ajax failure
                alert("Some problem occur during call the ajax: " + request.responseText);
            }
        });
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
			    	<form:hidden path="formBean.requestType" id="hiddenRequestType"/>
			    	<fieldset class="scheduler-border" style="margin-top:0px;margin-bottom:0px">
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
							<div class="col-sm-12">
								<button type="button" class="btn btn-primary" style="width:110px;" onclick="performSearch()"><i class="fa fa-search"></i> Search</button>
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
<!--  Search Result Model - End -->		