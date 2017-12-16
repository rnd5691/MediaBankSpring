<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Q&A View</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link href="${pageContext.request.contextPath}/resources/css/header.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/qna/qnaWrite.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/qna/qnaView.css" rel="stylesheet">
<script type="text/javascript">
	$(function(){
		if($(".reply_check").val()=='답변 미완료'){
		$(".reply_check").css('color', 'red');
		}else{
		$(".reply_check").css('color', 'blue');
		}
		
		$("#reply_ok").click(function(){
			document.frm.setAttribute("action", "./qnaReplyUpdate");
			document.frm.submit();
		});
	});
</script>
</head>
<!-- header start -->
<c:if test="${sessionScope.member.kind ne 'admin'}">
	<c:import url="../temp/header.jsp"></c:import>
</c:if>
<c:if test="${sessionScope.member.kind eq 'admin' }">
	<c:import url="../admin/header.jsp"></c:import>
</c:if>
<!-- header finish -->

<!-- contents start -->
<c:if test="${!empty qna }">
<body oncontextmenu="return false" ondragstart="return false" onselectstart="return false">
<div class="body">
	<div class="qnaTitle">
		<h1><a href="qnaList.qna">Q&A</a></h1> <h4>View</h4>
	</div>
	<form name="frm" action="./qnaUpdate.qna">
		<article>
			<input type="hidden" name="qna_seq" value="${qna.qna_seq}">
			<div id="view_title">
				<input class="view_title" type="text" name="title" readonly="readonly" value="${qna.title}">
			</div>
			<div id="second_title">
				작성자 : <input class="writer" type="text" name="writer" readonly="readonly" value="${qna.writer}">
				<input class="reply_check" type="text" name="reply_check" readonly="readonly" value="${qna.reply_check}">
				<input id="date" type="text" name="write_date" readonly="readonly" value="${qna.reg_date}">
			</div>
			<div id="contents">
				<textarea name="contents" readonly="readonly">${qna.contents}</textarea>
			</div>
			<c:if test="${sessionScope.member.kind eq 'admin' && empty qna.reply}">
				<div class="reply">
					<textarea name="reply"></textarea>
				</div>
				<div class="reply_btn">
					<button id="reply_ok" class="btn btn-default">확인</button>
				</div>
			</c:if>
			<c:if test="${!empty qna.reply }">
				<div class="reply">
					<textarea class="before" readonly="readonly">${qna.reply}</textarea>
				</div>
			</c:if>
			<c:if test="${sessionScope.writer eq qna.writer }">
				<div class="button">
					<button class="btn btn-default">수정</button>
					<a href="./qnaDelete.qna?qna_seq=${qna.qna_seq}"class="btn btn-default">삭제</a>
				</div>	
			</c:if>
				
		</article>
	</form>
	<div class="push"></div>
</div>
</c:if>
<c:if test="${empty qna}">
	<script type="text/javascript">
		alert('해당 하는 번호가 없습니다.');
		location.href="qnaList";
	</script>
</c:if>
<!-- contents finish -->

<!-- footer start -->
<c:import url="../temp/footer.jsp"></c:import>
<!-- footer finish -->
</body>
</html>