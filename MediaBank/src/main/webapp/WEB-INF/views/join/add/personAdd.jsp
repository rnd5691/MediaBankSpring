<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:if test="${empty sessionScope.naver}">
		<tr>
			<td><span>*</span>닉네임</td>
			<td><input type="text" name="nickname" required="required"></td>
		</tr>
		<tr>
			<td>이름</td>
			<td><input type="text" name="name"></td>
		</tr>
		<tr>
			<td><span>*</span>생년월일</td>
			<td><input type="date" name="birth" required="required"></td>
		</tr>
		<tr>
			<td><span>*</span>작가 등록</td>
			<td>
				<input type="radio" name="artist" value="artist" required="required">작가
				<input type="radio" name="artist" value="general" required="required">일반
			</td>
		</tr>
	</c:if>
	<c:if test="${!empty sessionScope.naver}">
		<tr>
			<td><span>*</span>닉네임</td>
			<td><input type="text" name="nickname" readonly="readonly" value="${sessionScope.naver.nickName }"></td>
		</tr>
		<tr>
			<td>이름</td>
			<td><input type="text" name="name"></td>
		</tr>
		<tr>
			<td><span>*</span>생년월일</td>
			<td><input type="date" name="birth" required="required"></td>
		</tr>
		<tr>
			<td><span>*</span>작가 등록</td>
			<td>
				<input type="radio" name="artist" value="artist" required="required">작가
				<input type="radio" name="artist" value="general" required="required">일반
			</td>
		</tr>
	</c:if>
</body>
</html>