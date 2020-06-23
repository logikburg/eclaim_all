<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>eAllowance</title>
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<!-- Bootstrap 3.3.7 -->
<link rel='stylesheet' href='<c:url value="/css/bootstrap.min.css" />' type='text/css' media='all' />
<!-- Font Awesome -->
<link rel='stylesheet' href='<c:url value="/css/font-awesome.min.css" />' type='text/css' media='all' />
<link rel="stylesheet" href='<c:url value="/plugins/bootstrap/validator/css/bootstrapValidator.css" />' type='text/css'
	media='all' />

<!-- jQuery 1.12.3 -->
<script src="<%=request.getContextPath() + "/plugins/jquery/core/jquery-1.12.3.min.js"%>"></script>
<!-- jQuery UI 1.11.4 -->
<script src="<%=request.getContextPath() + "/js/jquery-ui.min.js"%>"></script>
<!-- Bootstrap 3.3.7 -->
<script src="<%=request.getContextPath() + "/js/bootstrap.min.js"%>"></script>
<script src="<%=request.getContextPath() + "/plugins/bootstrap/validator/bootstrapValidator.js"%>"></script>
<!-- jquery cookie 1.4 -->
<script src="<%=request.getContextPath() + "/plugins/jquery/cookie/jquery.cookie.js"%>"></script>
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
	<script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
	<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
	<![endif]-->

<style>
.login-container {
	background: url('../img/eclaim_background.jpg') no-repeat center center
		fixed;
	-webkit-background-size: cover;
	-moz-background-size: cover;
	background-size: cover;
	-o-background-size: cover;
}

div.login-section {
	margin: 0 auto;
	padding: 50px 0 0;
}

div.login-trans-box {
	position: relative;
	padding: auto;
	margin: auto;
	width: auto;
	height: 100%;
	background-color: #ffffff;
	border: 1px solid #ccc;
	opacity: 0.8;
	valign: center;
}

div.login-box {
	margin: auto;
	width: 360px;
	height: 100%;
	padding-top: 0px;
	padding-bottom: 50px;
}

div.login-box .input-group-addon {
	padding: 10px 18px;
}

/*    --------------------------------------------------
	:: Inputs & Buttons
	-------------------------------------------------- */
.form-control {
	color: #212121;
}

.btn-custom {
	color: #fff;
	background-color: #1fa67b;
}

.btn-custom:hover, .btn-custom:focus {
	color: #fff;
}

/*    --------------------------------------------------
    :: Footer
	-------------------------------------------------- */
#footer {
	font-size: 12px;
	text-align: left;
}

#footer p {
	margin-bottom: 0;
}

.showUserRole {
	padding-left: 10px;
}

.page-header {
	padding-bottom: 9px;
	margin: 0px 0 20px;
	border-bottom: 1px solid #eee;
	padding-top: 10px;
	text-align: center;
	color: #018175;
	background-color: #4a82bd;
}
</style>

<script>
	/* jQuery(window).resize(function() {
		resizeWindow();
	}) 
	
	function resizeWindow() {
		var width = jQuery(window).width();
		var height = jQuery(window).height();
		//$("#resolution").html("Resolution: " + width + " x " + height);
		//console.log("Window: " + width + " x " + height);
	}
	 */

	function setCookie(c_name, c_value) {
		var d = new Date();
		d.setTime(d.getTime() + (30 * 24 * 60 * 60 * 1000)); // plus 30 days
		var expires = "expires=" + d.toUTCString();
		document.cookie = c_name + "=" + c_value + ";" + expires + ";path=/";
	}

	function saveCookie() {
		$("#userId").val($.trim($("#userId").val()).toUpperCase());
		$("#domain").val($.trim($("#domain").val()));
		//setCookie("userId", $("#userId").val());
	}

	function getCookie(c_name) {
		var c_value = document.cookie;
		var c_start = c_value.indexOf(" " + c_name + "=");
		if (c_start == -1) {
			c_start = c_value.indexOf(c_name + "=");
		}
		if (c_start == -1) {
			c_value = null;
		} else {
			c_start = c_value.indexOf("=", c_start) + 1;
			var c_end = c_value.indexOf(";", c_start);
			if (c_end == -1) {
				c_end = c_value.length;
			}
			c_value = unescape(c_value.substring(c_start, c_end));
		}
		return c_value;
	}

	function loadCookie() {
		var userId = getCookie("userId");
		$("#userId").val(userId);
	}

	function loginButtonDisable() {
		$('#loginButton').attr('disabled', true);
	}

	$(document).ready(function() {
		//loadCookie();
		//resizeWindow();
		$("#userId").focus();
		$("#userId").blur(function() {
			$("#userId").val($.trim($("#userId").val()).toUpperCase());
		});

		$("#loginButton").click(function() {
			if (performValidation()) {
				return true;
			} else {
				return false;
			}
		});

		$(".showUserRole").length > 0 ? $(".login-box").width(500) : $(".login-box").width();

		// Check is any browser is opened
		/*if (typeof $.cookie('openedApp') != "undefined") {
			if ($.cookie('openedApp') == "Y") {
				$("#userId").attr("disabled" , true);
				$("#password").attr("disabled" , true);
				$('#loginButton').attr('disabled', true);
			}
			else {
				$("#userId").attr("disabled" , false);
				$("#password").attr("disabled" , false);
				$('#loginButton').attr('disabled', false);
			}
		}
		else {
			$("#userId").attr("disabled" , false);
			$("#password").attr("disabled" , false);
			$('#loginButton').attr('disabled', false);
		}*/
	});

	function performValidation() {
		var errFlag = "true";
		var errMsg = "";

		$("#divErrorMessage").hide();
		$("#errMsg").html('');
		$("#divBackendErrorMessage").html('');

		if ($("#userId").val() == "") {
			errMsg += errMsg + "<b>User ID</b> cannot be empty.";
		}
		if ($("#password").val() == "") {
			if (errMsg != "") {
				errMsg += "<br/>";
			}
			errMsg += "<b>Password</b> cannot be empty.";
		}

		if (errMsg == "") {
			return true;
		} else {
			$("#errMsg").html(errMsg);
			$("#divErrorMessage").show();
			return false;
		}

	}

	function openInNewTab(url) {
		var win = window.open(url, '_blank');
		win.focus();
	}
</script>
</head>
<body class="login-container">
	<div class="page-header">
		<img src='../img/eallowance_logo.jpg' style="width: 230px;" />
	</div>
	<div class="login-section">
		<!-- 		<div class="login-trans-box"> -->
		<div class="login-box">
			<!-- 				<div class="page-header" style="text-align:center;color:#018175"> -->
			<!-- 					<h2><b>eClaim System</b></h2> -->
			<!-- 				</div> -->

			<div class="panel panel-success">
				<!-- 					<div class="panel-heading"> -->
				<!-- 						<h3 class="panel-title">Login to eClaim</h3> -->
				<!-- 					</div> -->
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-12">
							<table style="width: 100%; border: 0">
								<tr valign='middle'>
									<td style="width: 60%;"><c:url var="loginUrl" value="/login/doLogin" />
										<form id="loginForm" action='<c:out value="${loginUrl}"/>' method="post" role="form"
											onSubmit="loginButtonDisable()">
											<form:hidden path="loginBean.url" />
											<div id="divErrorMessage" style="display: none">
												<div id="errMsg" class="alert alert-danger"></div>
											</div>
											<div id="divBackendErrorMessage" class="form-group has-error">
												<c:if test="${error != null}">
													<label class="control-label" for="inputError"><c:out value="${error}" /></label>
												</c:if>
											</div>
											<div class="form-group has-feedback">
												<div class="input-group">
													<form:input path="loginBean.userId" name="userId" id="userId" type="text" class="form-control"
														placeholder="User ID" maxlength="30" />
													<div class="input-group-addon">
														<span class="glyphicon glyphicon-user form-control-feedback"></span>
													</div>
												</div>
											</div>
											<div class="form-group has-feedback">
												<div class="input-group">
													<form:password path="loginBean.password" name="password" id="password" class="form-control"
														placeholder="Password" maxlength="30" />
													<div class="input-group-addon">
														<span class="glyphicon glyphicon-lock form-control-feedback"></span>
													</div>
												</div>
											</div>
											<form:hidden path="loginBean.domain" name="domain" id="domain" />
											<div class="row">
												<div class="col-xs-12">
													<button type="submit" id="loginButton" class="btn btn-custom btn-block btn-flat">
														<i class="glyphicon glyphicon-log-in"></i> Sign In
													</button>
												</div>
												<!-- /.col -->
											</div>

										</form></td>
<%-- 									<c:if test="${error != null}"> --%>
<!-- 										<td class="showUserRole"> -->
<!-- 											<div style="border-left: 1px solid #ccc; height: 160px; padding-left: 10px;"> -->
<!-- 												<table style="border: 1px #777777 solid"> -->
<!-- 													<div class="form-group"> -->
<!-- 														<div class="input-group"> -->
<!-- 															<b>Please select role</b> -->
<!-- 														</div> -->
<!-- 													</div> -->
<%-- 													<c:forEach var="listUserRole" items="${userRoleList}"> --%>
<!-- 														<div class="form-group"> -->
<%-- 															<a href="javascript:switchRoleFromHeader('<c:out value="${listUserRole.role.roleId}"/>')"> <c:out --%>
<%-- 																	value="${listUserRole.role.roleName}" /> --%>
<!-- 															</a> -->
<!-- 														</div> -->
<%-- 													</c:forEach> --%>
<!-- 													<div class="form-group"> -->
<!-- 														<input type="Checkbox" style="margin-right: 7px;" /><i>Mark as default role </i> -->
<!-- 													</div> -->
<!-- 												</table> -->
<!-- 											</div> -->
<!-- 										</td> -->
<%-- 									</c:if> --%>
								</tr>
							</table>
						</div>
						<div style="display: none"></div>
					</div>
					<!-- ./row -->
					<footer id="footer" style="text-align: center">
						<br />
						<div class="row">
							<div class="col-xs-12">
								<p>
									If you encounter any problems, Please use <br /> <a href="#"
										onclick="openInNewTab('<c:out value="${loginBean.bsdLoginUrl}"/>')">Business Support Desk</a> for assistance.
								</p>
							</div>
						</div>
					</footer>
				</div>
			</div>
		</div>

		<!-- 		</div>./login-section -->
	</div>
	<!-- ./login-container -->
</body>
</html>
