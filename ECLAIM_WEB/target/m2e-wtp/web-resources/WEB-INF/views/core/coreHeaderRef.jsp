<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  	<meta http-equiv="X-UA-Compatible" content="IE=edge">
  	<title>eAllowance</title>
  	<!-- Tell the browser to be responsive to screen width -->
  	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  	<!-- CSS - Start -->
  	<!-- Bootstrap 3.3.7 -->
  	<link rel="stylesheet" href="<c:url value="/css/bootstrap.min.css" />" type="text/css" media="all" />
  	<!-- Font Awesome -->
  	<link rel="stylesheet" href="<c:url value="/css/font-awesome.min.css" />" type="text/css" media="all" />
  	<!-- Data table -->
  	<link rel="stylesheet" href="<c:url value="/plugins/jquery/datatable/css/dataTables.bootstrap.css" />" type="text/css" media="all" />
  	<link rel="stylesheet" href="<c:url value="/plugins/bootstrap/datepicker/css/bootstrap-datepicker3.css" />" type="text/css" media="all" />
  	<link rel="stylesheet" href="<c:url value="/plugins/bootstrap/datetimepicker/css/bootstrap-datetimepicker.css" />" type="text/css" media="all" />
  	
  	<link rel="stylesheet" href="<c:url value="/plugins/bootstrap/validator/css/bootstrapValidator.css" />" type="text/css" media="all" />
	<link rel="stylesheet" href="<c:url value="/css/style.css" />" type="text/css" media="all" />
	<!-- CSS - End -->
  	
    <!-- Javascript - Start -->
  	<!-- jQuery 1.12.3 -->
   	<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/jquery/core/jquery-1.12.3.min.js"%>"></script>
   	<!-- jQuery UI 1.11.4 -->
   	<script type="text/javascript" src="<%=request.getContextPath() + "/js/jquery-ui.min.js"%>"></script>
	<!-- Bootstrap 3.3.7 -->
	<script type="text/javascript" src="<%=request.getContextPath() + "/js/bootstrap.min.js"%>"></script>
	<!-- Datatable 1.10.16 -->
	<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/jquery/datatable/js/jquery.dataTables.min.js"%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/jquery/datatable/js/dataTables.bootstrap.min.js"%>"></script>
	<!-- Bootstrap Datepicker 1.6.4 -->
	<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/bootstrap/datepicker/bootstrap-datepicker.js" %>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/bootstrap/datetimepicker/bootstrap-datetimepicker.js"%>"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/bootstrap/validator/bootstrapValidator.js"%>"></script>
	<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/jquery/autocomplete/jquery.select-to-autocomplete.js"%>"></script>
	<!-- jquery cookie 1.4 -->
	<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/jquery/cookie/jquery.cookie.js"%>"></script>
	
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
	<script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
	<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
	<![endif]-->
	
	<!-- Javascript - End -->
  