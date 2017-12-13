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
<link href="${pageContext.request.contextPath}/resources/css/mypage/List.css" rel="stylesheet">
<script type="text/javascript">
	$(function(){
		var message = '${message}';
		if(message != ""){
			alert(message);
		}
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

<div class="body">
	<c:import url="./menu.jsp"></c:import>
	<div class="totalbody">
		<div class="title">
			<h1>My Page</h1>&nbsp;&nbsp;<h5>내 작품 판매승인 요청 List</h5>
		</div>
		<div>
				<table class="table table-hover">
					<tr class="table-title">
						<td>번호</td>
						<td class="title_contents">작품명</td>
						<td>작가명</td>
						<td>승인현황</td>
						<td>등록일자</td>
					</tr>
					<c:forEach items="${list}" var="dto">
						<tr>
							<td>${dto.work_seq}</td>
							<td><a href="salesRequestView?work_seq=${dto.work_seq}">${dto.work}</a></td>
							<td>${dto.nickname}</td>
							<td>${dto.upload_check}</td>
							<td>${dto.work_date}</td>
						</tr>
					</c:forEach>
				</table>
				
				<a  id="btn" href="mypageSalesRequestWrite.mypage" class="btn btn-default">WRITE</a>
				
				<c:if test="${makePage.totalPage > 0}">
					<div class="paging">
						<ul class="pagination">
							<c:if test="${makePage.curBlock>1}">
								<li><a href="./salesRequestList?curPage=1">&lt;&lt;</a></li>
								<li><a href="./salesRequestList?curPage=${makePage.startNum-1}">[이전]</a></li>
							</c:if>
							<c:forEach begin="${makePage.startNum}" end="${makePage.lastNum}" var="i">
								<li><a href="./salesRequestList?curPage=${i}">${i}</a></li>
							</c:forEach>
							<c:if test="${makePage.curBlock < makePage.totalBlock}">
								<li><a href="./salesRequestList?curPage=${makePage.getLastNum()+1}">[다음]</a></li>
								<li><a href="./salesRequestList?curPage=${makePage.totalPage}">&gt;&gt;</a></li>
							</c:if>
						</ul>
					</div>
				</c:if> 
					
			</div>
	</div>
	<div class="push"></div>
</div>

<!-- contents finish -->

<!-- footer start -->
<c:import url="../temp/footer.jsp"></c:import>
<!-- footer finish -->
</body>
</html>