<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>소울메이트 :: 달력</title>
<link rel="stylesheet"  href="/css/user/calendar.css">
</head>
<body>
	<jsp:include page="../template/header.jsp"></jsp:include>
	<div class="container">
		<jsp:include page="../template/nav.jsp"></jsp:include>
		<section>
			<div id="calendarForm"></div>
		</section>
	</div>
	<script src="/js/calendar.js"></script>
</body>
</html>