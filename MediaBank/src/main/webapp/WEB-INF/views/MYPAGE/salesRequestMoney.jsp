<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>작품 별 수익 현황</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link href="${pageContext.request.contextPath}/resources/css/header.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/mypage/mypage.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/mypage/salesMoney.css" rel="stylesheet">
<script type="text/javascript">
$(function(){
	$("#salesRequestMoney").css('color', 'white');
	$("#salesRequestMoney").css('background-color', '#83b14e');
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
		<h1>My Page</h1>&nbsp;&nbsp;<h5>작품 별 수익 현황</h5>
	</div>
	<div class="moneytable">
			<table id="table1" class="table table-hover">
				<tr>
					<td class="tdtable1" colspan="2">총 내 작품</td>
					<td>
					<input type="text" id="totalfile" name="totalfile" value="${workTotal}" readonly="readonly">개
					</td>
					<td class="tdtable1" colspan="2">총 판매 금액</td>
					<td>
					
					<input type="text" id="totalMoney" name="totalMoney" value="${totalMoney}" readonly="readonly">원
					</td>
				</tr>
			</table>
			<table class="table table-hober">
				<tr>
					<td class="tdtable1">번호</td>
					<td class="tdtable1">작품명</td>
					<td class="tdtable1">작가명</td>
					<td class="tdtable1">등록일자</td>
					<td class="tdtable1">판매금액</td>
					<td class="tdtable1">수익금액</td>
				</tr>
				
				<c:forEach items="${list}" var="dto">
				<tr>
					<td class="tdtable2">${dto.work_seq}</td>
					<td class="tdtable2">${dto.work}</td>
					<td class="tdtable2">${dto.nickname}</td>
					<td class="tdtable2">${dto.work_date}</td>
					<td class="tdtable2">${dto.price}</td>
					<td class="tdtable2">${dto.download_hit * dto.price}</td>
				</tr>
				</c:forEach>
				
			</table>
			<c:if test="${makePage.totalPage > 0}">
				<div class="paging">
					<ul class="pagination">
					<c:if test="${makePage.curBlock>1}">
							<li><a href="./salesRequestMoney?curPage=${1}">&lt;&lt;</a></li>
							<li><a href="./salesRequestMoney?curPage=${makePage.startNum-1}">[이전]</a></li>
						</c:if>
						<c:forEach begin="${makePage.startNum}" end="${makePage.lastNum}" var="i">
							<li><a href="./salesRequestMoney?curPage=${i}">${i}</a></li>
						</c:forEach>
						<c:if test="${makePage.curBlock < makePage.totalBlock}">
							<li><a href="./salesRequestMoney?curPage=${makePage.lastNum+1}">[다음]</a></li>
							<li><a href="./salesRequestMoney?curPage=${makePage.totalPage}">&gt;&gt;</a></li>
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