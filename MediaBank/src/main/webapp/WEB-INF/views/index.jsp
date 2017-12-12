<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="plugin/slick/slick.css"/>
<link rel="stylesheet" type="text/css" href="plugin/slick/slick-theme.css"/>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<link href="../resources/css/header.css" rel="stylesheet">

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css">
<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<!-- Popper JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.6/umd/popper.min.js"></script>
<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js"></script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<title>Media Bank</title>
<link href="../resources/css/index.css" rel="stylesheet">
<script>
$(function() {
	$("#btn").click(function() {
		if ($("#select").val() == "video") {
			$("#kind").prop("value", false);
		} else {
			$("#kind").prop("value", true);
		}
		document.frm.submit();
	});
	
	$("#search").keydown(function(key){
		if(key.keyCode==13){
			if ($("#select").val() == "video") {
				$("#kind").prop("value", false);
			} else {
				$("#kind").prop("value", true);
			}
			document.frm.submit();
		}
	});
	
	 $('.searh_main').slick({
		  slidesToShow: 1,
		  slidesToScroll: 4,
		  arrows: false,
		  fade: true,
		  asNavFor: '.multiple-items'
		});
		$('.multiple-items').slick({
		  slidesToShow: 3,
		  slidesToScroll: 4,
		  asNavFor: '.searh_main',
		  dots: true,
		  centerMode: true,
		  focusOnSelect: true
		});
		
	var API_KEY = '5591688-41cb145c7a8c9f609261640ef';
	var count = 1;
	var URL = "https://pixabay.com/api/?key=" + API_KEY +"&q="
			+ encodeURIComponent(' ');
	
	$.getJSON(URL, function(data) {
	if (parseInt(data.totalHits) > 0) {	
		$.each(data.hits, function(i, hit) {
			$("#slide"+count).prop("src", hit.webformatURL);
			$("#slider"+count).prop("src", hit.webformatURL);
			count++;
			});
		} else {
			console.log('No hits');
		}
	});
});
</script>
</head>
<body oncontextmenu="return false" ondragstart="return false" onselectstart="return false">
<!-- header start -->
<c:import url="./temp/header.jsp"></c:import>
<!-- header finish -->

<!-- contents -->
<div class="body">

<!-- 메인슬라이더 -->
<div style="width : 100%; height: 500px;">
<div class="searh_main">
	<c:forEach begin="1" end="20" var="i">
		<div class="pic"><img id="slider${i}"></div>
	</c:forEach>
</div>
<!-- 메인슬라이더 끝 -->
<!-- 검색창 -->
	<h3 class="search-text">Beautiful Free Image!</h3>
	<div id="search_warp">
	<form action="search/searchList.search" name="frm" id="f_position">
		<input type="text" name="search" class="form-control" placeholder="Search images, videos">
		<input type="hidden" id="kind" name="kind">
		<input type="hidden" name="perPage" value="1">
		<input type="hidden" name="check" value=true>
		<select id="select" name="select" class="form-control">
			<option value="image">사진</option>
			<option value="video">비디오</option>
		</select>
		<input type="button" class="btn btn-default" id="btn">
	</form>
	</div>
</div>
<!-- 검색창 끝  -->
<!-- 무명작가 슬라이더 -->
<div class="void"></div>
<div class="author_warp">
	<h3 class="slide-font" style="margin-top: 0;">Artist Art Collection <span>더 보기</span></h3>
	<div class="multiple-items">
		<c:forEach begin="1" end="20" var="i">
		<div class="pic"><img id="slide${i}"></div>
		</c:forEach>
	</div>
</div>
<div class="info_sarp">
	
</div>
<!-- 무명작가 슬라이더 끝 -->
<!-- contents finish -->
<div class="push"></div>
</div>
<!-- footer -->
<c:import url="./temp/footer.jsp"></c:import>
<!-- footer finish -->
<!-- slick plugin -->
	<script type="text/javascript" src="//code.jquery.com/jquery-1.11.0.min.js"></script>
	<script type="text/javascript" src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
	<script type="text/javascript" src="plugin/slick/slick.min.js"></script>
<!-- slick plugin finish -->
</body>
</html>