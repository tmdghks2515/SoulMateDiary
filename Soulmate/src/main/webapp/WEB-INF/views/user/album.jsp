<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/css/user/album.css">
<link rel="stylesheet" href="/fontello-05cba82f/css/fontello.css">
<title>소율메이트 :: 앨범</title>
</head>
<body>
	<%@include file="../template/header.jsp"%>
	<div class="container">
		<%@include file="../template/nav.jsp"%>
		<section>
			<div id="addCard">
				<a href="#" id="btnAddCard" class="btn">사진 추가</a> <a href="#" id="btnShowSearch" class="btn">앨범
					검색</a>

				<form action="/user/imgUpload" method="POST" class="hidden" enctype="multipart/form-data">
					<div>
						<p>
							<input type="file" name="file" accept="image/x-png,image/gif,image/jpeg">
							<button type="button" id="plus">+</button>
							<button type="button" id="minus">-</button>
						</p>
					</div>
					<div>
						<table>
							<tr>
								<td class="sub2">앨범 제목:</td>
								<td><input type="text" name="title"></td>
							</tr>
							<tr>
								<td class="sub2 top">한줄일기:</td>
								<td><textarea rows="5" cols="22" name="content"></textarea></td>
							</tr>
							<tr>
								<td colspan="2" class="right"><a href="#" class="btn">추가하기</a></td>
							</tr>
						</table>
					</div>
				</form>
				<form id="frmSearch" class="hidden icon-search" onsubmit="doSearch();" target="iframe1">
					<input type="text" name="title" placeholder="앨범이름">
				</form>
				<iframe id="iframe1" name="iframe1" style="display: none"></iframe>
			</div>
			<hr>
			<div id="cards">
				<c:forEach var="album" items="${requestScope.albumPage.content}">
					<div class="card" onclick="showModal('${album.title}','${album.soulmate.id}')">
						<div>
							<img src="/user/showImg?soulmateId=${album.soulmate.id}&title=${album.title}">
						</div>
						<div class="noselect">
							${album.title} <br> ${fn: substring(album.createDate, 0, 10) }
						</div>
					</div>
				</c:forEach>
				<c:if test="${!requestScope.albumPage.last}">
					<div id="viewMore">
						<a href="/user/album?size=${requestScope.albumPage.size+10}">더보기</a>
					</div>
				</c:if>
			</div>
			<!--  -->
			<!-- The Modal -->
			<div id="myModal" class="modal">

				<!-- Modal content -->
				<div class="modal-content">
					<div class="modal-header">
						<span class="close">&times;</span>
						<h2>Modal Header</h2>
					</div>
					<div class="modal-body"></div>
				</div>

			</div>
			<!--  -->
		</section>
	</div>
	<script src="/lib/jquery-3.5.1.js"></script>
	<script src="/js/album.js"></script>
</body>
</html>