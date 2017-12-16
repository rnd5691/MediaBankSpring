<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Q&A Write</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link href="${pageContext.request.contextPath}/resources/css/header.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/qna/qnaWrite.css" rel="stylesheet">
</head>
<!-- header start -->
<c:import url="../temp/header.jsp"></c:import>
<!-- header finish -->

<!-- contents start -->
<body oncontextmenu="return false" ondragstart="return false" onselectstart="return false">
<div class="body">
	<div class="qnaTitle">
		<h1><a href="qnaList">Q&A</a></h1> <h4>Write</h4>
	</div>
	<form action="write" method="post">
		<table class="table table-bordered">
			<tr>
				<td class="table-title">제목</td>
				<td><input class="title" type="text" name="title" required="required"></td>
				<td class="table-title">작성자</td>
				<td><input class="writer" type="text" name="writer" readonly="readonly" value="${writer}"></td>
			</tr>
			<tr>
				<td class="table-title">내용</td>
				<td colspan="3"><textarea name="contents"rows="" cols=""></textarea></td>
			</tr>
		</table>
		<div id="btn">
			<button class="btn btn-default">확인</button>
		</div>
	</form>
	<div class="push"></div>
</div>
<!-- contents finish -->

<!-- footer start -->
<c:import url="../temp/footer.jsp"></c:import>
<!-- footer finish -->
</body>
</html>