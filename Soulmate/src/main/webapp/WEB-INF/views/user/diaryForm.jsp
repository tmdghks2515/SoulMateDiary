<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="isAuthenticated()">
	<sec:authentication property="principal" var="principal" />
</sec:authorize>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/css/user/diary.css">
<title>소울메이트 :: 다이어리 작성하기</title>
</head>
<body>
	<%!public String getDate() {
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(today);
	}%>
	<jsp:include page="../template/header.jsp" />
	<div class="container">
		<jsp:include page="../template/nav.jsp" />
		<section>
			<form action="/user/diaryProc" method="POST" id="frmDiary">
				<table>
					<tr>
						<td><span>일기 제목</span></td>
						<td><input type="text" name="title"></td>
					</tr>
					<tr>
						<td><span>작성자</span></td>
						<td>${principal.user.name}</td>
					</tr>
					<tr>
						<td><span>작성일</span></td>
						<td><%=getDate()%></td>
					</tr>
					<tr>
						<td class="top"><span>일기 내용</span></td>
						<td>
							<textarea cols="117" rows="20" name="content"></textarea>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<a href="/user/diary" class="btn">목록보기</a>
							<a href="#" class="btn" onclick="$('#frmDiary').submit()">저장</a>		
						</td>
					</tr>
				</table>
			</form>
		</section>
	</div>
	<script src="/lib/jquery-3.5.1.js"></script>
	<script src="/js/diary.js"></script>
</body>
</html>