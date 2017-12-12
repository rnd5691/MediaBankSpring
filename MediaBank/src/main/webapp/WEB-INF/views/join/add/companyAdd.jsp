<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<tr>
		<td><span>*</span>사업자등록번호</td>
		<td>
			<input id="company_num" type="text" name="company_num" required="required">
			<div id="company_num_check"></div>
		</td>
	</tr>
	<tr>
		<td><span>*</span>상호명</td>
		<td><input type="text" name="company_name" required="required"></td>
	</tr>
	<tr>
		<td><span>*</span>회사 연락처</td>
		<td>
			<input type="text" name="company_phone" required="required">
		</td>
	</tr>
</body>
</html>