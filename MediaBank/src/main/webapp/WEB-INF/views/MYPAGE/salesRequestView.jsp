<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>내 작품 판매 승인 현황</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link href="${pageContext.request.contextPath}/resources/css/header.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/mypage/mypage.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/mypage/salesRequestView.css" rel="stylesheet">
<script type="text/javascript">
	$(function(){
		$("#salesRequestList").css('color', 'white');
		$("#salesRequestList").css('background-color', '#83b14e');
	});
</script>
</head>
<body oncontextmenu="return false" ondragstart="return false" onselectstart="return false">
<!-- header start -->
<c:import url="../temp/header.jsp"></c:import>
<!-- header finish -->

<!-- contents start -->
<!-- menu는 mypage나 구매목록이 나오는 탭 부분 -->
<c:if test="${!empty work}">
<div class="body">

<c:import url="./menu.jsp"></c:import>
<div class="totalbody">
	<div class="title">
		<h1>My Page</h1>&nbsp;&nbsp;<h5>내 작품 판매승인 요청</h5>
	</div>
	<form action="salesRequestUpdate">
		<input type="hidden" name="work_seq" value="${work.work_seq}">
		<input type="hidden" name="file_kind" value="${file.file_kind}">
		<table class="table">
			<tr>
         		<c:if test="${file.file_kind eq 'image'}">
            		<td rowspan="9" colspan="2">
						<img src="${pageContext.request.contextPath}/resources/upload/${file.file_name}">
					</td>
				</c:if>
				<c:if test="${file.file_kind eq 'video'}">
					<td rowspan="8" colspan="2">
						<video src="${pageContext.request.contextPath}/resources/upload/${file.file_name} " width="310" height="310" controls="controls"></video>
					</td>
				</c:if>
         	</tr>
			<tr>
				<td>작품명</td>
				<td><input id="work" name="work" type="text" readonly="readonly" value="${work.work}"></td>
			</tr>
			<tr>
				<td>승인현황</td>
				<td><input name="upload_check" type="text" readonly="readonly" value="${work.upload_check}"></td>
			</tr>
			<tr>
				<td>작가명</td>
				<td><input name="nickname" type="text" readonly="readonly" value="${work.nickname}"></td>
			</tr>
			<tr>
				<td>등록 일자</td>
				<td><input name="work_date" type="date" readonly="readonly" value="${work.work_date }"></td>
			</tr>
			<tr>
				<td>가격</td>
				<td><input name="price" type="text" readonly="readonly" value="${work.price}"></td>
			</tr>
			<c:if test="${requestScope.file.file_kind eq 'image'}">
		         <tr>
		            <td>파일 사이즈</td>
		            <td><input class="size" name="width" type="text" readonly="readonly" value="${file.width }"> X <input class="size" name="height" type="text"readonly="readonly" value="${file.height}"></td>
		         </tr>
	     	</c:if>
			<tr>
				<td>상세 내용</td>
				<td><textarea name="contents" readonly="readonly">${work.contents}</textarea></td>
			</tr>
			<tr>
				<td>태그</td>
				<td><textarea name="tag" readonly="readonly">${work.tag}</textarea></td>
			</tr>
		</table>
		<c:if test="${work.reply ne null}">
			<div class="reply">
				<textarea name="reply" readonly="readonly">${work.reply }</textarea>
			</div>		
		</c:if>
		<c:if test="${work.upload_check eq '대기중' || work.upload_check eq '거부'}">
	      <a href="./viewDelete?work_seq=${work.work_seq}" class="bloat btn btn-default">DELETE</a>
	      <input type="submit" class="bloat btn btn-default" value="UPDATE">
      	</c:if>
	</form>
</div>
<div class="push"></div>
</div>
</c:if>
<c:if test="${empty requestScope.work}">
	<script type="text/javascript">
		alert('해당하는 번호가 없습니다.');
		location.href="salesRequestList";
	</script>
</c:if>
<!-- contents finish -->

<!-- footer start -->
<c:import url="../temp/footer.jsp"></c:import>
<!-- footer finish -->
</body>
</html>