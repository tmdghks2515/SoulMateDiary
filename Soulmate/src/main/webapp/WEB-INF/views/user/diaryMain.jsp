<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authentication property="principal" var="principal" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>소울메이트 :: 다이어리</title>
</head>
<body>
	<jsp:include page="../template/header.jsp" />
	<div class="container">
		<jsp:include page="../template/nav.jsp" />
		<section></section>
	</div>
	<script src="../lib/jquery-3.5.1.js"></script>
	<script src="../js/diaryMain.js"></script>
</body>
</html>