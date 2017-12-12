<!-- 회원 가입시, 계정 선택 부분 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원 가입</title>
<link href="${pageContext.request.contextPath}/resources/css/header.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/join/joinForm_kind.css" rel="stylesheet">

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>   

</head>
<body oncontextmenu="return false" ondragstart="return false" onselectstart="return false">
<%@ include file="../temp/header.jsp" %>
<!-- Contents -->
<section class="body">
	<article class="contents">
		<article class="title">
			<h1>회원 가입</h1> <h5>계정 선택</h5>
		</article>
		<article class="session">
			<a class="button" href="company">기업으로 가입하기</a>
			<a class="button" href="person">개인으로 가입하기</a>

			
		</article>
	</article>
	<div class="push"></div>
</section>
<!-- Contents 끝 -->
<%@ include file="../temp/footer.jsp" %>
</body>
</html>