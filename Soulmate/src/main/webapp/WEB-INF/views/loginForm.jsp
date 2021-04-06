<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>소울메이트 :: 로그인</title>
</head>
<body>
	<div class="container">
		<div id="login">
			<form action="/login" method="POST" >
				<a href="/">Soulmate</a><br>
				<small>다이어리</small><br>
				<input type="text" name="username"><br>
				<input type="password" name="password"><br>
				<button>로그인</button>	<a href="#" onclick="showPopup();">회원가입</a>
				<br>
				간편로그인:<br>
				<a href="/oauth2/authorization/google">구글 로그인</a><br>
				<a href="/oauth2/authorization/facebook">페이스북 로그인</a><br>
				<a href="/oauth2/authorization/kakao">카카오톡 로그인</a><br>
				<a href="/oauth2/authorization/naver">네이버 로그인</a><br>
				
			</form>
		</div>
	</div>
</body>
<link rel="stylesheet" href="css/loginForm.css" />
<script>
	const showPopup = () => {
		window.open("/joinForm","joinForm","width=600, height=800, left=400, top=50");
	}
</script>
</html>