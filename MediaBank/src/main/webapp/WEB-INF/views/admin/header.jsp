<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- header start -->
	<header>
		<div class="header_wrap">
			<a href="${pageContext.request.contextPath}/qna/qnaList"><img class="logo" align="middle" alt="logo" src="${pageContext.request.contextPath}/resources/images/logo.png"></a>
			<ul class="header_join">
				<li><a href="${pageContext.request.contextPath}/qna/qnaList">Q&A</a></li>
				<li><a href="${pageContext.request.contextPath}/mypage/mypageSalesRequestList.mypage">작품 승인 관리</a></li>
				<li><a href="${pageContext.request.contextPath}/member/logout">로그아웃</a></li>
			</ul>
		</div>
		
	</header>