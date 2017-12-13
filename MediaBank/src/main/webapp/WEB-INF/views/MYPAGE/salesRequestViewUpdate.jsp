<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link href="${pageContext.request.contextPath}/resources/css/header.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/mypage/mypage.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/mypage/salesRequestView.css" rel="stylesheet">
<script oncontextmenu="return false" ondragstart="return false" onselectstart="return false">
$(function(){

	$("#salesRequestList").css('color', 'white');
	$("#salesRequestList").css('background-color', '#83b14e');
	var check = false;
	$("#work").change(function(){
		check = true;
	});
	
	$("#price").change(function(){
		check = true;
	});
	$("#contents").change(function(){
		check = true;
	});
	$("#tag").change(function(){
		check = true;
	});
	//파일 사이즈 체크
	$("#file").change(function(){
		check=true;
		if($(this).val() != ""){
			var file = this.files[0]; // files 를 사용하면 파일의 정보를 알 수 있음
			// file 은 배열 형태이므로 file[0] 처럼 접근해야함
			var _URL = window.URL || window.webkitURL;
			var img = new Image();
				
			var reader = new FileReader();
			reader.onload = function(rst){
				$("#imagebox").prop("src", rst.target.result);
			}
			reader.readAsDataURL(file);
			
			img.src = _URL.createObjectURL(file);
			img.onload = function(){
			 	$("#fileWidth").val(img.naturalWidth);
				$("#fileHeight").val(img.naturalHeight);
			}
		}
	});
	
	$("#update").click(function(){
		if(check==false){
			alert('변경 사항이 없습니다.');
		}else{
			$("#frm").prop("action", "mypageSalesRequestViewUpdate.mypage");
			$("#frm").submit();
		}
	});
	
});
</script>
<style type="text/css">
	.border {
		border: 1px solid rgb(169, 169, 169);
		border-radius: 5px;
	}
</style>
</head>
<body>
<!-- header start -->
<c:import url="../temp/header.jsp"></c:import>
<!-- header finish -->

<!-- contents start -->
<!-- menu는 mypage나 구매목록이 나오는 탭 부분 -->

<div class="body">

<c:import url="./menu.jsp"></c:import>
<div class="totalbody">
	<div class="title">
		<h1>My Page</h1>&nbsp;&nbsp;<h5>내 작품 판매승인 요청</h5>
	</div>
	<form id="frm" method="post" enctype="multipart/form-data">
		<input type="hidden" name="work_seq" value="${work.work_seq}">
		<input type="hidden" name="file_kind" value="${file.file_kind}">
		<table class="table">
			 <tr>
         		<c:if test="${file.file_kind eq 'image'}">
            		<td rowspan="10" colspan="2">
						<img src="${pageContext.request.contextPath}/upload/${file.file_name}">
					</td>
				</c:if>
				<c:if test="${file.file_kind eq 'video'}">
					<td rowspan="9" colspan="2">
						<video src="${pageContext.request.contextPath}/upload/${file.file_name} " width="310" height="310" controls="controls"></video>
					</td>
				</c:if>
         	</tr>
			<tr>
				<td>작품명</td>
				<td><input id="work"class="border" name="work" type="text" value="${work.work}"></td>
			</tr>
			<tr>
				<td>승인현황</td>
				<td><input name="upload_check" type="text" readonly="readonly" value="${work.upload_check}"></td>
			</tr>
			<tr>
				<td>작가명</td>
				<td><input name="nickname" type="text" readonly="readonly" value="${requestScope.work.nickname}"></td>
			</tr>
			<tr>
				<td>등록 일자</td>
				<td><input name="work_date" type="date" readonly="readonly" value="${requestScope.work.work_date }"></td>
			</tr>
			<tr>
				<td>가격</td>
				<td><input id="price" class="border" required="required" name="price" type="text" value="${requestScope.work.price}"></td>
			</tr>
			<tr>
				<td>파일 업로드</td>
				<td><input type="file" required="required" id="file" name="file"></td>
			</tr>
			<c:if test="${requestScope.file.file_kind eq 'image'}">
		         <tr>
		            <td>파일 사이즈</td>
		            <td><input class="size" name="width" type="text" readonly="readonly" value="${requestScope.file.width }"> X <input class="size" name="height" type="text"readonly="readonly" value="${requestScope.file.height}"></td>
		         </tr>
	     	</c:if>
			<tr>
				<td>상세 내용</td>
				<td><textarea id="contents" required="required" class="border" name="contents">${requestScope.work.contents}</textarea></td>
			</tr>
			<tr>
				<td>태그</td>
				<td><textarea id="tag" required="required" class="border" name="tag">${requestScope.work.tag}</textarea></td>
			</tr>
		</table>
		<c:if test="${!empty requestScope.work.reply}">
			<div class="reply">
				<textarea name="reply" readonly="readonly">${requestScope.work.reply }</textarea>
			</div>		
		</c:if>
		<button id="update" class="bloat btn btn-default">UPDATE</button>
		<!-- <input type="button" id="update" class="bloat btn btn-default" value="UPDATE"> -->
		
	</form>
</div>
<div class="push"></div>
</div>
<!-- contents finish -->
<!-- footer start -->
<c:import url="../temp/footer.jsp"></c:import>
<!-- footer finish -->
</body>
</html>