<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="joinProc" method="POST" >
		<input type="text"  name="username" placeholder="아이디"><br>
		<input type="password" name="password" placeholder="비밀번호"><br>
		<input type="password" name="password2" placeholder="비밀번호 확인"><br>
		<input type="text" name="email" placeholder="이메일 주소"><br>
		<input type="text" name="name" placeholder="이름"><br>
		<button>회원가입</button>
	</form>
</body>
</html>