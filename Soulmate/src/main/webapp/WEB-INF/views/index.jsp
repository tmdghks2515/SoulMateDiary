<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<sec:authorize access="isAuthenticated()">
	<sec:authentication property="principal" var="principal" />
</sec:authorize>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/index.css" />
<link rel="stylesheet" href="css/slick.css" />
<link rel="stylesheet" href="css/slick-theme.css" />
<title>소울메이트</title>
</head>
<body>
	<jsp:include page="template/header.jsp" />
	<div class="container">
		<a class="prev icon-angle-left"></a>
		<div class="slider">
			<div>
				<div class="text">
					<span id="title1" class="tit"></span><br>
					<textarea id="content1" class="textarea" readonly></textarea>
					<br>
					<sec:authorize access="hasAuthority('ROLE_ADMIN')">
						<input type="text" class="carouselTitle" placeholder="문구 제목">
						<br>
						<textarea class="carouselContent" placeholder="문구 내용"></textarea>
						<br>
						<a onclick="updateCarousel(1)" class="btn">수정</a>
					</sec:authorize>
				</div>
				<div class="picture">
					<img src="/carouselImg?page=1">
					<sec:authorize access="hasRole('ADMIN')">
						<form action="/admin/changeCarouselImg" method="POST" enctype="multipart/form-data">
							<input type="file" accept="image/*" name="file" onchange="changeImage(1)"> <input
								type="hidden" value="1" name="page">
						</form>
					</sec:authorize>
				</div>
			</div>
			<div>
				<div class="text">
					<span id="title2" class="tit"></span><br>
					<textarea id="content2" class="textarea" readonly></textarea>
					<br>
					<sec:authorize access="hasAuthority('ROLE_ADMIN')">
						<input type="text" class="carouselTitle" placeholder="문구 제목">
						<br>
						<textarea class="carouselContent" placeholder="문구 내용"></textarea>
						<br>
						<a onclick="updateCarousel(2)" class="btn">수정</a>
					</sec:authorize>
				</div>
				<div class="picture">
					<img src="/carouselImg?page=2">
					<sec:authorize access="hasRole('ADMIN')">
						<form action="/admin/changeCarouselImg" method="POST" enctype="multipart/form-data">
							<input type="file" accept="image/*" name="file" onchange="changeImage(2)"> <input
								type="hidden" value="2" name="page">
						</form>
					</sec:authorize>
				</div>
			</div>
			<div>
				<div class="text">
					<span id="title3" class="tit"></span><br>
					<textarea id="content3" class="textarea" readonly></textarea>
					<br>
					<sec:authorize access="hasAuthority('ROLE_ADMIN')">
						<input type="text" class="carouselTitle" placeholder="문구 제목">
						<br>
						<textarea class="carouselContent" placeholder="문구 내용"></textarea>
						<br>
						<a onclick="updateCarousel(3)" class="btn">수정</a>
					</sec:authorize>
				</div>
				<div class="picture">
					<img src="/carouselImg?page=3">
					<sec:authorize access="hasRole('ADMIN')">
						<form action="/admin/changeCarouselImg" method="POST" enctype="multipart/form-data">
							<input type="file" accept="image/*" name="file" onchange="changeImage(3)"> <input
								type="hidden" value="3" name="page">
						</form>
					</sec:authorize>
				</div>
			</div>
		</div>
		<a class="next icon-angle-right"></a>
		<div id="myPage">
			<c:choose>
				<c:when test="${empty principal}">
					<a href="/loginForm" class="btnWide">회원가입 하러가기 <span class="icon-right-open"></span>
					</a>
				</c:when>
				<c:otherwise>
					<a href="/user/diaryMain" class="btnWide">마이 페이지</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<script src="/lib/jquery-3.5.1.js"></script>
	<script src="/lib/slick.js"></script>
	<script src="/js/index.js"></script>
</body>
</html>