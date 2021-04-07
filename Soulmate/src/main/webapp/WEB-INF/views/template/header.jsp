<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication property="principal" var="principal"/>
</sec:authorize>
<html>
<body>
	<header>
		<a href="/">Soulmate</a> 
		<div>
			<c:choose>
				<c:when test="${empty principal }">
					<a href="/loginForm">로그인/회원가입</a><br>
				</c:when>
				<c:otherwise>
					${principal.user.name} 님 |
					<a href="/logout">로그아웃</a><br>
				</c:otherwise>
			</c:choose>
		</div>
	</header>
</body>
<link rel="stylesheet" href="/css/template/header.css"></link>
</html>