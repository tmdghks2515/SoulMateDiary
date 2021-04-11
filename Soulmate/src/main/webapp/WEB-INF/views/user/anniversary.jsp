<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cutil" uri="tld/CommonUtil.tld" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/css/user/anniversary.css">
<title>소울메이트 :: 기념일</title>
</head>
<body>
	<jsp:include page="../template/header.jsp" />
	<div class="container">
		<jsp:include page="../template/nav.jsp" />
		<section>
			<div class="top">
				<a class="btn" id="showForm">기념일 등록</a>
				<form class="hidden" method="POST" id="frmAnni">
					<input type="text" name="anniName" placeholder="기념일 이름"><br> <input type="date"
						name="date"><br> <a class="btn">등록</a>
				</form>
			</div>
			<c:if test="${anniList.size() == 0 }">
				<h3 id="noAnni">
					기념일이 없습니다
				</h3>
			</c:if>
			<c:forEach var="anni" items="${anniList }">
				<div class="anniBox" onclick="openModal('${anni.anniName}','${anni.anniDate }','${anni.story }','${anni.id }')">
					<div class="d1">
						<span class="name">${anni.anniName}</span><span class="count">${cutil:anniDateDiff(anni.anniDate)}</span>
						<br> <span class="date">${anni.anniDate }</span>
					</div>
					<div class="d2">${anni.story}</div>
				</div>
			</c:forEach>

			<!-- modal box -->
			<div id="myModal" class="modal">
				<!-- Modal content -->
				<div class="modal-content">
					<span class="close">&times;</span>
					<p>
							<span id="name"></span><br>
							<span id="date"></span>
							<textarea placeholder="어떤 일이 있었나요?" id="story"></textarea>
							<input type="hidden" id="id">
							<a class="btn">저장</a>
					</p>
				</div>
			</div>
			<!--  -->
			
		</section>
	</div>
	<script src="/js/anniversary.js"></script>
</body>
</html>