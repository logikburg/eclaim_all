<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/core/coreHeaderRef.jsp"%>

<script>
	// Register on window close and open event
	var resetFlag = false;
	$(window).on('load', function() {
		if (typeof $.cookie('openedApp') != "undefined") {
			if ($.cookie('openedApp') == 'Y') {
				// window.location.href = "<%=request.getContextPath() %>/login/loginPage";
			} 
			else {
				$.cookie('openedApp', 'Y', { expires: 7, path: '/'  });
		    	resetFlag = true;
			}
		}
		else {
	    	$.cookie('openedApp', 'Y', { expires: 7, path: '/'  });
	    	resetFlag = true;
	    }
	});
	
	$(window).unload( function () {
		if (resetFlag) {
			$.removeCookie('openedApp', { path: '/' });
		}
	});

	var hcmPayrollIsRunning = false;
	var firstTimeoutPeriod = 1500000;
	var secondTimeoutPeriod = 1800000;

	function parseStrToDate(str) {
		var mdy = str.split("/");
		return new Date(mdy[2], mdy[1]-1, mdy[0]);
	}
	
	function getDaysBetween(date1, date2) {
		return Math.round((parseStrToDate(date1)-parseStrToDate(date2))/ (1000*60*60*24));
	}
	
	function formatDateForSort(inDate) {
		if (inDate == "") {
			return "";
		}
		
		var tmp = inDate.split("/");
		
		if (tmp.length != 3) {
			return inDate;
		}
		
		return tmp[2] + tmp[1] + tmp[0];
	}
	
	function disableF5(e) {
		if ((e.which || e.keyCode) == 116) {
			e.preventDefault();
		}
	}
	
	function nvl(inStr) {
		if (inStr == null) {
			return "";
		}
		else {
			return inStr;
		}
	}

	$.fn.modal.prototype.constructor.Constructor.DEFAULTS.backdrop = 'static';
	
	$(document).ready(function () {
	  	
		/*hamburger-toggle*/
		$("#hamburger-toggle").click(function(e) {
			e.preventDefault();
			$("#wrapper").toggleClass("active");
		});
	  	
	  	$("#frmDetail #approvalDateGroup").datepicker({
	  		format: "dd/mm/yyyy",
	  		endDate: '+0d',
	  		autoclose: true,
	  		daysOfWeekHighlighted: [0],
	  		todayHighlight: true,
	  		todayBtn: "linked"
	  	})
	  	.on('changeDate', function(e) {
	  		$("#txtApprovalDate").change();
	  	});
		
	  	$("#txtApprovalDate").change(function(e) {
	  		if ($(this).attr("required") === undefined) {
	  		}
	  		else {
	  			if ($(this).val() != "") {
	  				var today = new Date();
	  				var dd = today.getDate();
	  				var mm = today.getMonth()+1;
	  				var yyyy = today.getFullYear();
	  				
	  				if (dd < 10) {
	  					dd = "0" + dd;
	  				}
	  				
	  				if (mm < 10) {
	  					mm = "0" + mm;
	  				}
	  				
	  				var compDate = dd + "/" + mm + "/" + yyyy;
	  				
	  				if (getDaysBetween($(this).val(), compDate) > 0) {
	  					$(this).val("");
	  				}
	  			}
	  		
	  			if ($(this).css("display") != "none") {
	  				$("#frmDetail").bootstrapValidator('revalidateField', $(this).attr("name"));
	  			}
	  		}
	  	});
	  	
	  	$("#txtApprovalDate").blur(function(e) {
	  		if ($(this).attr("required") === undefined) {
	  		}
	  		else {
	  			if ($(this).css("display") != "none") {
	  				$("#frmDetail").bootstrapValidator('revalidateField', $(this).attr("name"));
	  			}
	  		}
	  	});
	  	
	  	// Initial the date picker
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
	  	
	  	// Look up hcm is running
// 	  	$.ajax({
<%--             url: "<%=request.getContextPath() %>/hcm/isHcmRunning", --%>
//             type: "POST",
//             success: function(data) {
//             	if (data == "Y") {
//             		$("#hcm a").addClass("disabled");
//             		hcmPayrollIsRunning = true;
            		
//             		if (typeof payrollIsRunning == 'function') {
//             			payrollIsRunning();
//             		}
//             	}
//             	else {
//             		hcmPayrollIsRunning = false;
//             	}
//             }
//         });
	});
	
	$(document).on("keydown", disableF5);

	function showLoading() {
		$("#loader").css("top", $(document).scrollTop()+300);
		$("#loader").show();
		$("#loaderBackground").height($(document).height());
		$("#loaderBackground").show();
	}

	function hideLoading() {
		$("#loader").hide();
		$("#loaderBackground").hide();
	}
	
	function switchRoleFromHeader(targetRole) {
		showLoading();
		// Perform checking of role
		$.ajax({
            url: "<%=request.getContextPath() %>/common/isRoleExist",
            data: {role: targetRole},
            type: "POST",
            success: function(data) {
            	if (data == "Y") {
					$("#targetRole").val(targetRole);
					$("form")[0].submit();
            	}
            	else {
            		hideLoading();
            		$("#warningTitle").html("Error");
            		$("#warningContent").html("User role invalid, please contact system admin.");
            		$("#warningModal").modal("show");
            	}
            }
        });
	}
	
	function returnToHome() {
		document.location.href = '<c:url value="/home/home"/>';
	}
	
	function removePost(row) {
		var table = row.parent().parent().parent().parent();
		var count = table.children('tbody').length;
		if (count == 1) {
			row.parent().parent().html('<td colspan="6">No record found.</td>');
		} else {
			row.parent().parent().parent().remove();
		}
	}
	function openInNewTab(url) {
		var win = window.open(url, '_blank');
		win.focus();
	}
	
	function MPRSEncodeURI(inStr) {
		if (inStr == null) {
			return inStr;
		}
		
		return inStr.replace(/,/g, "%2C");
	}
	
	function MPRSDecodeURI(inStr) {
		if (inStr == null) {
			return inStr;
		}
		
		return inStr.replace(/%2C/g, ",");
	}
</script>
</head>

<body>
<div id="loader" style="display:none;z-index:9999"></div>
<div id="loaderBackground" style="display:none;position:absolute;width:100%;height:100%;background-color:rgba(90%, 90%, 90%, 0.6);z-index:9998"></div>
<div class="main-wrapper">
	<header class="main-header">
		<!-- Navigation Bar -->
		<c:choose>
			<c:when test="${sessionScope.environment == 'PRD'}">
				<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
			</c:when>
			<c:when test="${sessionScope.environment == 'DEV'}">
				<nav class="navbar navbar-default navbar-fixed-top mprs_navbar_dev" role="navigation">
			</c:when>
			<c:when test="${sessionScope.environment == 'SIT'}">
				<nav class="navbar navbar-default navbar-fixed-top mprs_navbar_sit" role="navigation">
			</c:when>
			<c:when test="${sessionScope.environment == 'FIX_SIT'}">
				<nav class="navbar navbar-default navbar-fixed-top mprs_navbar_fixsit" role="navigation">
			</c:when>
			<c:when test="${sessionScope.environment == 'UAT' || sessionScope.environment == 'Enhancement UAT'}">
				<nav class="navbar navbar-default navbar-fixed-top mprs_navbar_uat" role="navigation">
			</c:when>
			<c:when test="${sessionScope.environment == 'FIX_UAT'}">
				<nav class="navbar navbar-default navbar-fixed-top mprs_navbar_fixuat" role="navigation">
			</c:when>
			<c:otherwise>
				<nav class="navbar navbar-default navbar-fixed-top mprs_navbar_dev" role="navigation">
			</c:otherwise>
		</c:choose>
		  
		  	<div class="container-fluid">
		  		<div class="navbar-header">
					<button type="button" id="hamburger-toggle" class="btn navbar-btn"><i class="fa fa-bars fa-lg"></i></button>
			  	</div>
		    	<div class="navbar-header" style="padding-left:10px; color: #ffffff">
			  		<div>
			  			<span class="navbar-brand" style="color: #ffffff">	<img src = '../img/left_ha_logo.jpg' /></span><span class="brand-name-sm">MPRS</span>
			  			<span class="label"><small>[<c:out value="${sessionScope.environment}"/> <c:out value="${sessionScope.swVersion}"/>]</small></span>
			  		</div>
		    	</div>
		    	
		    	<ul class="nav navbar-nav navbar-right">
					<li>
					<a href="#" class="dropdown-toggle" style="color: #ffffff" data-toggle="dropdown">
						<i class="fa fa-users"></i><span class="hidden-sm"> <c:out
									value="${sessionScope.currentRoleName}" /></span></a>
						<ul class="dropdown-menu">
							<!-- The user image in the menu -->
							<li><a href='<c:url value="/maintenance/userSetup"/>'><i
									class="fa fa-gear"></i> User Setup</a></li>
							<li><a href='<c:url value="/home/home"/>'><i
									class="fa fa-arrow-right"></i> Project Owner</a></li>
							<li><a href='<c:url value="/home/home"/>'><i
									class="fa fa-arrow-right"></i> Roster Planner</a></li>
						</ul>
					</li>
					<li><a href="#" style="color: #ffffff" onclick="returnToHome()"><i class="fa fa-home"></i><span class="hidden-sm"> Home</span></a></li>
			  		<li class="dropdown user user-menu">
						<!-- Menu Toggle Button --> 
						<a href="#" class="dropdown-toggle"
						   data-toggle="dropdown" style="color: #ffffff"> <!-- hidden-xs hides the username on small devices so only the image appears. -->
							<span class="glyphicon glyphicon-user"></span>
							<span class="hidden-sm"><c:out value="${sessionScope.userProfile.userName}"/></span><span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<!-- The user image in the menu -->
							<li class="user-header">
								<p>
									<c:out value="${sessionScope.userProfile.userName}" />
									-
									<c:out value="${sessionScope.currentRoleName}" />
									<small> <!-- HEC -->
									</small>
								</p>
							</li>
							<!-- Menu Body -->
							<li class="user-body">
								<div class="row">
									<c:forEach var="listUserRole" items="${sessionScope.userRoleList}">
										<div class="col-xs-4 text-center">
											<a href="javascript:switchRoleFromHeader('<c:out value="${listUserRole.roleId}"/>')"><c:out value="${listUserRole.roleName}"/></a>
										</div>
									</c:forEach>
									<form action='<c:url value="/home/home"/>' method="POST">
										<input type="hidden" id="targetRole" name="targetRole"/>
									</form>
								</div> <!-- /.row -->
							</li>
							<!-- Menu Footer-->
							<li class="user-footer">
								<div class="pull-right">
									<a href='<c:url value="/maintenance/editProfile"/>' class="btn btn-primary btn-flat">Edit Profile</a>
								</div>
							</li>
						</ul>
					</li>
					<li>
						<a href='<c:url value="/login/logout"/>' style="color: #ffffff"><span class="glyphicon glyphicon-log-out"></span><span class="hidden-sm"> Logout</span></a>
					</li>
		    	</ul>
		  	</div>
		</nav>
		
	</header>
	
	<div id="wrapper">
		<!-- Sidebar -->
		<nav id="sidebar-wrapper" class="navbar navbar-inverse navbar-fixed-top" role="navigation" style="font-family:calibri;font-size:8px;font-weight:bold">
			<ul class="sidebar-nav">
		        <li>
		        	<a href='<c:url value="/home/home"/>'><i class="fa fa-home"></i> Home</a>
		        </li>
		        <c:set var="pFnList" value="${sessionScope.parentFuncList}>" />
		        <c:set var="fnList" value="${sessionScope.funcList}>" />
<%-- 		        <c:if test='${fn:contains(pFnList, "RQ")}'> --%>
<!-- 		        <li class="dropdown"> -->
<!-- 					<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="glyphicon glyphicon-list"></i> Request</a> -->
<!-- 		           	<ul class="dropdown-menu" role="menu"> -->
<%-- 		           		<c:if test='${fn:contains(fnList, "RQ_NEW")}'> --%>
<%-- 		            		<li><a href='<c:url value="/request/newPost"/>'><i class="fa fa-file-text-o"></i> New Post</a></li> --%>
<%-- 		            	</c:if> --%>
<%-- 		            	<c:if test='${fn:contains(fnList, "RQ_UPG")}'> --%>
<%-- 		                	<li><a href='<c:url value="/request/upgrade"/>'><i class="fa fa-file-text-o"></i> Upgrade</a></li> --%>
<%-- 		            	</c:if> --%>
<%-- 		                <c:if test='${fn:contains(fnList, "RQ_EXT")}'> --%>
<%-- 		                	<li><a href='<c:url value="/request/extension"/>'><i class="fa fa-file-text-o"></i> Extension</a></li> --%>
<%-- 		            	</c:if> --%>
<%-- 		                <c:if test='${fn:contains(fnList, "RQ_DEL")}'> --%>
<%-- 		                	<li><a href='<c:url value="/request/deletion"/>'><i class="fa fa-file-text-o"></i> Deletion</a></li> --%>
<%-- 		            	</c:if> --%>
<%-- 		            	<c:if test='${fn:contains(fnList, "RQ_FRO")}'> --%>
<%-- 							<li><a href='<c:url value="/request/frozen"/>'><i class="fa fa-file-text-o"></i> Frozen Post</a></li> --%>
<%-- 		            	</c:if> --%>
<%-- 		               	<c:if test='${fn:contains(fnList, "RQ_CSM")}'> --%>
<%-- 		                	<li><a href='<c:url value="/request/changeStaffMix"/>'><i class="fa fa-file-text-o"></i> Change of Staff Mix</a></li> --%>
<%-- 		            	</c:if> --%>
<%-- 		                <c:if test='${fn:contains(fnList, "RQ_CHF")}'> --%>
<%-- 							<li><a href='<c:url value="/request/changeOfFunding"/>'><i class="fa fa-file-text-o"></i> Change of Funding</a></li> --%>
<%-- 		            	</c:if> --%>
<%-- 		                <c:if test='${fn:contains(fnList, "RQ_SUP")}'> --%>
<%-- 							<li><a href='<c:url value="/request/suppPromotion"/>'><i class="fa fa-file-text-o"></i> Supplementary Promotion</a></li> --%>
<%-- 		            	</c:if> --%>
<%-- 		            	<c:if test='${fn:contains(fnList, "RQ_CHP")}'> --%>
<%-- 							<li><a href='<c:url value="/request/changeHCMPost"/>'><i class="fa fa-file-text-o"></i> Change HCM Position of Post</a></li> --%>
<%-- 		            	</c:if> --%>
<%-- 		            	<c:if test='${fn:contains(fnList, "RQ_CHP")}'> --%>
<%-- 							<li><a href='<c:url value="/request/fteAdjustment"/>'><i class="fa fa-file-text-o"></i> FTE Adjustment</a></li> --%>
<%-- 		            	</c:if> --%>
<!-- 					</ul> -->
<!-- 				</li> -->
<%-- 		        </c:if> --%>
<%-- 		        <c:if test='${fn:contains(pFnList, "HCM_POSITION")}'> --%>
<!-- 		        <li class="dropdown"> -->
<!-- 					<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-laptop"></i> HCM Position</a> -->
<!-- 		            <ul class="dropdown-menu" role="menu" id="hcm"> -->
<%-- 		                <c:if test='${fn:contains(fnList, "HCM_CREATE")}'> --%>
<%-- 			            	<li><a href='<c:url value="/hcm/createHCMPost"/>' id='hcmLink'><i class="fa fa-envelope-open-o"></i> Create HCM Position</a></li> --%>
<%-- 		            	</c:if> --%>
<%-- 		                <c:if test='${fn:contains(fnList, "HCM_UPDATE")}'> --%>
<%-- 		    	            <li><a href='<c:url value="/hcm/updateHCMPost"/>' id='hcmLink'><i class="fa fa-edit"></i> Update HCM Position</a></li> --%>
<%-- 		            	</c:if> --%>
<%-- 		            	<c:if test='${fn:contains(fnList, "HCM_CREATE_APPROVER")}'> --%>
<%-- 		    	            <li><a href='<c:url value="/hcm/createHCMApprover"/>' id='hcmLink'><i class="fa fa-edit"></i> Create HCM Approver</a></li> --%>
<%-- 		            	</c:if> --%>
<!-- 					</ul> -->
<!-- 		        </li> -->
<%-- 		        </c:if> --%>
<%-- 		        <c:if test='${fn:contains(pFnList, "ENQUIRY")}'> --%>
<!-- 		        <li> -->
<!-- 		        	<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-question-circle"></i> Enquiry</a> -->
<!-- 		            <ul class="dropdown-menu" role="menu"> -->
<%-- 		                <c:if test='${fn:contains(fnList, "ENQUIRY_POST")}'> --%>
<%-- 			            	<li><a href='<c:url value="/request/requestEnquiry"/>'><i class="fa fa-question-circle"></i> Request Enquiry</a></li> --%>
<%-- 		            	</c:if> --%>
<%-- 		                <c:if test='${fn:contains(fnList, "ENQUIRY_RQ")}'> --%>
<%-- 		    	            <li><a href='<c:url value="/request/enquiry"/>'><i class="fa fa-question-circle"></i> Post Enquiry</a></li> --%>
<%-- 		            	</c:if> --%>
<!-- 					</ul> -->
		        	
<!-- 		        </li> -->
<%-- 		        </c:if> --%>
<%-- 		        <c:if test='${fn:contains(pFnList, "RPT")}'> --%>
<!-- 		        <li> -->
<%-- 		        	<a href='<c:url value="/reporting/reportHome"/>'><i class="fa fa-bar-chart"></i> Reporting</a> --%>
<!-- 		        </li> -->
<%-- 		        </c:if> --%>
<%--  		        <c:if test='${fn:contains(pFnList, "ADM")}'>  --%>
<!-- 		        <li class="dropdown"> -->
<!-- 					<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-cubes"></i> Maintenance</a> -->
<!-- 		            <ul class="dropdown-menu" role="menu"> -->
<%-- 						<c:if test='${fn:contains(fnList, "ADM_NEWS")}'> --%>
<%-- 		            	<li><a href='<c:url value="/maintenance/newsMaintenance"/>'><i class="fa fa-bullhorn"></i> News and Announcement </a></li> --%>
<%-- 		            	</c:if> --%>
<%-- 		                <c:if test='${fn:contains(fnList, "ADM_DOC")}'> --%>
<%-- 		                <li><a href='<c:url value="/maintenance/documentMaintenance"/>'><i class="fa fa-file-text-o"></i> Document</a></li> --%>
<%-- 		                </c:if> --%>
<%-- 		                <c:if test='${fn:contains(fnList, "ADM_USER")}'> --%>
<%-- 						<li><a href='<c:url value="/maintenance/userMaintenance"/>'><i class="fa fa-users"></i> User Role</a></li> --%>
<%-- 		            	</c:if> --%>
<!-- 		            </ul> -->
<!-- 				</li> -->
<%-- 				</c:if> --%>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">Project</a>
		            <ul class="dropdown-menu" role="menu">
		            	<li><a href='<c:url value="/home/home"/>'>Search Project<div class="pull-right"><i class="fa fa-search"></i></div></a></li>
		                <li><a href='<c:url value="/project/newProject"/>'>New Project<div class="pull-right"><i class="fa fa-edit"></i></div></a></li>
					</ul>
				</li>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">Payment</a>
		            <ul class="dropdown-menu" role="menu">
		            	<%-- <li><a href='<c:url value="/claim/searchClaim"/>'>Search Claim<div class="pull-right"><i class="fa fa-search"></i></div></a></li>
		            	<li><a href='<c:url value="/claim/prepare"/>'>Prepare Payment<div class="pull-right"><i class="fa fa-edit"></i></div></a></li>
		                <li><a href='<c:url value="/claim/approve"/>'>Submit to Fin<div class="pull-right"><i class="fa fa-edit"></i></div></a></li> --%>
						<li><a href='<c:url value="/payment/review"/>'>Validate Payment<div class="pull-right"><i class="fa fa-edit"></i></div></a></li>
						<li><a href='<c:url value="/payment/transfer"/>'>Transfer to HCM<div class="pull-right"><i class="fa fa-edit"></i></div></a></li>
		            </ul>
				</li>
				<%-- <li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">Claim</a>
		            <ul class="dropdown-menu" role="menu">
		            	<li><a href='<c:url value="/claim/searchClaim"/>'>Search Claim<div class="pull-right"><i class="fa fa-search"></i></div></a></li>
		            	<li><a href='<c:url value="/claim/prepare"/>'>Prepare Claim<div class="pull-right"><i class="fa fa-edit"></i></div></a></li>
		                <li><a href='<c:url value="/claim/approve"/>'>Approval Claim<div class="pull-right"><i class="fa fa-edit"></i></div></a></li>
						<li><a href='<c:url value="/claim/review"/>'>Review Claim<div class="pull-right"><i class="fa fa-edit"></i></div></a></li>
						<li><a href='<c:url value="/claim/transfer"/>'>Transfer To BEE<div class="pull-right"><i class="fa fa-edit"></i></div></a></li>
		            </ul>
				</li> --%>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">User</a>
		            <ul class="dropdown-menu" role="menu">
		            	<li><a href='<c:url value="/home/home"/>'>User Administration<div class="pull-right"><i class="fa fa-edit"></i></div></a></li>
					</ul>
				</li>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">Schedule</a>
		            <ul class="dropdown-menu" role="menu">
		            	<li><a href='<c:url value="/home/home"/>'>Duty Schedule<div class="pull-right"><i class="fa fa-edit"></i></div></a></li>
					</ul>
				</li>
<!-- 				<li class="dropdown"> -->
<!-- 					<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-user-o"></i> Profile</a> -->
<!-- 		            <ul class="dropdown-menu" role="menu"> -->
<%-- 		            	<li><a href='<c:url value="/maintenance/editProfile"/>'><i class="glyphicon glyphicon-user"></i> Edit Profile</a></li> --%>
<%-- 		                <li><a href='<c:url value="/maintenance/switchRole"/>'><i class="fa fa-exchange"></i> Switch Role</a></li> --%>
<!-- 					</ul> -->
<!-- 				</li> -->
				
				<li>
		        	<a href='<c:url value="/login/logout"/>'><i class="glyphicon glyphicon-log-out"></i> Logout</a>
		        </li>
			</ul>
		</nav>
		<!-- /#sidebar-wrapper -->
	
	
