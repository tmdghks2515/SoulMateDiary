<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cutil"  uri="tld/CommonUtil.tld"%>
<sec:authorize access="isAuthenticated()">
	<sec:authentication property="principal" var="principal" />
</sec:authorize>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/fontello-95e63002/css/fontello.css">
<link rel="stylesheet" href="/css/user/diary.css">
<link rel="stylesheet" href="/css/user/diaryDetail.css">
<title>소울메이트 :: 다리어리 상세보기</title>
</head>
<body>
	<jsp:include page="../template/header.jsp" />
	<div class="container">
		<jsp:include page="../template/nav.jsp" />
		<section>
			<form action="/user/diaryProc" method="POST" id="frmDiary">
				<table>
					<tr>
						<td><span>일기 제목</span></td>
						<td>${diary.title }</td>
					</tr>
					<tr>
						<td><span>작성자</span></td>
						<td>${diary.writer.name}</td>
					</tr>
					<tr>
						<td><span>작성일</span></td>
						<td>${fn: substring(diary.writeDate,0,10) }</td>
					</tr>
					<tr>
						<td class="top"><span>일기 내용</span></td>
						<td>
							<textarea cols="117" rows="20" name="content" readonly>${diary.content}</textarea>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<a href="/user/diary" class="btn">목록보기</a>
						</td>
					</tr>
				</table>
			</form>
			<div id="comment">
				<span class="sub">댓글 <span class="yellow">${cmtListPage.totalElements}</span></span><br>
				<form action="/user/writeComment" id="writeComment" method="POST">
					<span class="sub-small">${principal.user.name}</span> <input type="text" name="content"> <a class="a" onclick="writeComment()">등록</a>
					<input type="hidden" value="${diary.id}" name="id">
				</form>
				<c:forEach var="cmt" items="${cmtList}">
					<div class="cmt">
						<span class="sub-small">${cmt.writer.name}</span>
						${cmt.content}<br>
						<small>${cutil:dateDiff(cmt.writeDate)}</small>
					</div>
				</c:forEach>
			</div>
			<div id="paging">
				<c:if test="${!cmtListPage.first }">
					<a href="/user/diaryDetail?id=${diary.id }&page=${cmtListPage.pageable.pageNumber-1}" class="icon-angle-left dis mg"></a>
				</c:if>
 				<c:forEach var="i"  begin="1" end="${cmtListPage.totalPages}">
 					<c:choose>
 					<c:when test="${i==cmtListPage.pageable.pageNumber+1}">
						<a class="page_i current">${i}</a>
 					</c:when>
 					<c:otherwise>
						<a href="/user/diaryDetail?id=${diary.id }&page=${i-1}" class="page_i">${i}</a>
 					</c:otherwise>
 					</c:choose>
				</c:forEach> 
				<c:if test="${!cmtListPage.last }">
					<a href="/user/diaryDetail?id=${diary.id }&page=${cmtListPage.pageable.pageNumber+1}" class="icon-angle-right dis mg"></a>
				</c:if>
			</div>
		</section>
	</div>
	<script src="/js/diary.js"></script>
</body>
</html>