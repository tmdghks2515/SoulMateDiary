<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<sec:authentication property="principal" var="principal" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/css/user/diaryMain.css" />
</head>
<body>
	<nav>
		<div>
			<c:choose>
				<c:when test="${empty requestScope.soulmate}">
					<div id="single">
						${principal.user.name } 님 !<br> 아직 소울메이트가 없습니다.<br> <a href="#">소울메이트 연결하기</a>
					</div>
				</c:when>
				<c:when test="${!empty  requestScope.soulmate}">
					<div id="couple">
						${principal.user.name } 💗 ${requestScope.user2.name}<br> D+${requestScope.Dday }
					</div>
				</c:when>
			</c:choose>
		</div>
		<ul>
			<li><a href="/user/album">앨범</a></li>
			<li><a href="#">달력</a></li>
			<li><a href="#">기념일</a></li>
			<li><a href="/user/diary">일기장</a></li>
		</ul>
	</nav>
</body>
</html>