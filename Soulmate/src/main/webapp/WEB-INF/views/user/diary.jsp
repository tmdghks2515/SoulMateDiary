<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/fontello-05cba82f/css/fontello.css">
<link rel="stylesheet" href="/fontello-95e63002/css/fontello.css">
<link rel="stylesheet" href="/css/user/diary.css">
<title>소울메이트 :: 일기장</title>
</head>
<body>
	<%@include file="../template/header.jsp"%>
	<div class="container">
		<%@include file="../template/nav.jsp"%>
		<section>
			<div class="top">
				<a href="/user/diaryForm" class="btn">작성하기</a>
			</div>
			<hr>
			<div>
			
				<div class="th">
					<span>작성일</span>
					<span>작성자</span>
					<span>제목</span>
				</div>
				
				<c:forEach var="diary" items="${diaries}">
					<div class="row">
						<span>${fn:substring(diary.writeDate,0,10) }</span>
						<span>${diary.writer.name }</span>
						<span class="title" >${diary.title }</span>
						<div class="content">
							<textarea readonly>${diary.content }</textarea>
						</div>
					</div>
				</c:forEach>
				
				<div id="paging">
					<c:choose>
						<c:when test="${!diaryPage.first}">
							<a href="/user/diary?page=0" class="icon-angle-double-left"></a>
							<a href="/user/diary?page=${diaryPage.pageable.pageNumber-1 }" class="icon-angle-left"></a>
						</c:when>
						<c:otherwise>
							<a class="icon-angle-double-left dis"></a>
							<a class="icon-angle-left dis"></a>
						</c:otherwise>
					</c:choose>
					<span class="des">Page ${diaryPage.pageable.pageNumber+1 } of ${diaryPage.totalPages }</span>
					<c:choose>
					<c:when test="${!diaryPage.last}">
						<a href="/user/diary?page=${diaryPage.pageable.pageNumber+1 }" class="icon-angle-right"></a>
						<a href="/user/diary?page=${diaryPage.totalPages-1}" class="icon-angle-double-right"></a>
					</c:when>
					<c:otherwise>
						<a class="icon-angle-right dis"></a>
						<a class="icon-angle-double-right dis"></a>
					</c:otherwise>
					</c:choose>
				</div>
			</div>
		</section>
	</div>
	<script src="/lib/jquery-3.5.1.js"></script>
	<script src="/js/diary.js"></script>
</body>
</html>