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
				<input type="text" name="username" placeholder="아이디"><br>
				<input type="password" name="password" placeholder="비밀번호"><br>
				<button>로그인</button><br>
				<a href="#" onclick="showPopup();">회원가입</a>
				<br>
				<span>sns계정으로 간편 로그인</span><br>
				<a href="/oauth2/authorization/google" class="icon"><img src="/icons/google.png"/></a>
				<a href="/oauth2/authorization/facebook" class="icon"><img src="/icons/facebook.png"/></a>
				<a href="/oauth2/authorization/kakao" class="icon"><img src="/icons/kakao.png"/></a>
				<a href="/oauth2/authorization/naver" class="icon"><img src="/icons/naver.png"/></a>
				
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