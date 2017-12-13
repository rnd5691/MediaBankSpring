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
	<tr>
		<td><span>*</span>닉네임</td>
		<td><input type="text" name="nickname" readonly="readonly" value="${person.nickname}"></td>
	</tr>
	<tr>
		<td>이름</td>
		<td><input class="change" type="text" name="name" value="${person.name}"></td>
	</tr>
	<tr>
		<td>생년월일</td>
		<td><input class="change" type="date" name="birth" value="${person.birth}"></td>
	</tr>
	<tr>
		<td><span>*</span>작가 등록</td>
		<td>
			<c:choose>
				<c:when test="${person.artist eq 'artist'}">
					<input class="change" type="radio" name="artist" value="artist" required="required" checked="checked">작가
					<input class="change" type="radio" name="artist" value="general" required="required">일반
				</c:when>
				<c:otherwise>
					<input class="change" type="radio" name="artist" value="artist" required="required">작가
					<input class="change" type="radio" name="artist" value="general" required="required" checked="checked">일반
				</c:otherwise>
			</c:choose>
			
		</td>
	</tr>
</body>
</html>