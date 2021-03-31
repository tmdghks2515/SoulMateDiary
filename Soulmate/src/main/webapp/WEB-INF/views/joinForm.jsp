<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>소울메이트 :: 회원가입</title>
<link rel="stylesheet" href="css/joinForm.css" />
</head>
<body>
	<header>
		<a href="/">Soulmate 회원가입</a>
	</header>
	<form id="joinFrm">
		<input type="text"  name="username" placeholder="아이디"><br>
		<input type="password" name="password" placeholder="비밀번호"><br>
		<input type="password" name="password2" placeholder="비밀번호 확인"><br>
		<input type="text" name="email" placeholder="이메일 주소"><br>
		<input type="text" name="name" placeholder="이름"><br>
		<button type="button" id="register">회원가입</button>
		<button type="button" id="close">닫기</button>
	</form>
	
</body>
<script src="lib/jquery-3.5.1.js"></script>
<script>
$("#register").click(() => {
	let data = $("#joinFrm").serialize();
	$.ajax({
		method: "POST",
		data: data,
		url: "joinProc",
		dataType:"json",
		success: function(resp) {
			alert(resp.responseMessage);
		}
	})
})

$("#close").click(() => {
	window.close();
})

</script>
</html>