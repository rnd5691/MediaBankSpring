<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Q&A</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link href="${pageContext.request.contextPath}/resources/css/header.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/qna/qnaList.css" rel="stylesheet">
<script type="text/javascript">
	$(function(){
		var message = '${message}';
		if(message != ""){
			alert(message);
		}
	});
</script>
</head>
<body oncontextmenu="return false" ondragstart="return false" onselectstart="return false">
<!-- header start -->
<c:if test="${sessionScope.member.kind ne 'admin'}">
	<c:import url="../temp/header.jsp"></c:import>
</c:if>
<c:if test="${sessionScope.member.kind eq 'admin' }">
	<c:import url="../admin/header.jsp"></c:import>
</c:if>
<!-- header finish -->
<div class="body">
	<div class="qnaTitle">
		<h1><a href="qnaList.qna">Q&A</a></h1>
	</div>
		<form name="frm" action="qnaList" method="post">
			<div class="search">
				<select name="kind" id="kind" class="kind">
					<option value="title">title</option>
					<option value="writer">writer</option>
					<option value="contents">contents</option>
				</select>
				<input type="text" placeholder="Enter Search" name="search">
				<button class="btn btn-default">Search</button>
			</div>
		</form>
		<div class="contents">
			<table class="table table-hover">
				<tr class="table-title">
					<td>번호</td>
					<td class="title_contents">제목</td>
					<td>작성자</td>
					<td>답변 상태</td>
					<td>등록날짜</td>
				</tr>
				<c:forEach items="${list}" var="dto">
					<tr>
						<td>${dto.qna_seq}</td>
						<td><a href="qnaView?qna_seq=${dto.qna_seq}">${dto.title}</a></td>
						<td>${dto.writer}</td>
						<td>${dto.reply_check}</td>
						<td>${dto.reg_date}</td>
					</tr>
				</c:forEach>	
			</table>
			<c:if test="${!empty sessionScope.member && sessionScope.member.kind ne 'admin'}">
				<a  id="btn" href="write" class="btn btn-default">WRITE</a>			
			</c:if>
			<c:if test="${makePage.totalPage > 0}">
				<div class="paging">
					<ul class="pagination">
						<c:if test="${makePage.curBlock>1}">
							<li><a href="./qnaList?curPage=1&kind=${kind}&search=${search}">&lt;&lt;</a></li>
							<li><a href="./qnaList?curPage=${makePage.startNum-1}&kind=${kind}&search=${search}">[이전]</a></li>
						</c:if>
							
						<c:forEach begin="${makePage.startNum}" end="${makePage.lastNum}" var="i">
							<li><a href="./qnaList.qna?curPage=${i}&kind=${kind}&search=${search}">${i}</a></li>
						</c:forEach>
						<c:if test="${makePage.curBlock < makePage.totalBlock}">
							<li><a href="./qnaList.qna?curPage=${makePage.getLastNum()+1}&kind=${kind}&search=${search}">[다음]</a></li>
							<li><a href="./qnaList.qna?curPage=${makePage.totalPage}&kind=${kind}&search=${search}">&gt;&gt;</a></li>
						</c:if>
					</ul>
				</div>
			</c:if>
			
		</div>		
	<div class="push"></div>
</div>
<!-- footer start -->
<c:import url="../temp/footer.jsp"></c:import>
<!-- footer finish -->
</body>
</html>