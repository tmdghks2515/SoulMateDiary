<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>소울메이트 :: 달력</title>
<link rel="stylesheet" href="/css/user/calendar.css">
</head>
<body>
	<jsp:include page="../template/header.jsp"></jsp:include>
	<div class="container">
		<jsp:include page="../template/nav.jsp"></jsp:include>
		<section>
			<div id="calendarForm"></div>
			
			<div id="myModal" class="modal">
				<!-- Modal content -->
				<div class="modal-content">
					<span class="close">&times;</span>
					<p>Some text in the Modal..</p>
					<form action="/user/saveSchedule" method="POST">
						<input type="text" placeholder="스케줄 제목" name="name"><br>
						<input type="time" name="time">
						<input type="hidden" name="date">
						<a href="#" class="btn" onclick="$('form').submit()">저장</a>
					</form>
				</div>
			</div>
			
		</section>
	</div>
	<script src="/js/calendar.js"></script>
</body>
</html>