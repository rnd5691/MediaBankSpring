<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>현재 판매중인 내 작품</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link href="../css/header.css" rel="stylesheet">
<link href="../css/mypage/mypage.css" rel="stylesheet">
<link href="../css/mypage/salesRequestNow.css" rel="stylesheet">
<script type="text/javascript">

var file_kind = "${file_kind}";
var user_num = "${user_num}";
$(function(){
	if(file_kind == "image")	{
		$("#li_image").addClass("active");
		$("#li_video").removeClass("active");
		$("#image").addClass("in active");
		$("#image").load('salesRequestNowAdd.mypage?file_kind='+file_kind);
	}else if(file_kind == "video"){
		$("#li_video").addClass("active");
		$("#li_image").removeClass("active");
		$("#video").addClass("in active");
		$("#video").load('salesRequestNowAdd.mypage?file_kind='+file_kind);
	}
	
	$("#button").click(function(){
		document.frm.action="mypageSalesRequestNowUpdate.mypage?file_kind=${file_kind}";
		document.frm.method="POST";
		document.frm.submit();
	});
	
});

function fn_tabClick(tabId){
	if(tabId == 'sajin')	{
		$("#file_kind").val("image");
	}else if(tabId == 'youngsang')	{
		$("#file_kind").val("video");
	}
	document.frm.action="mypageSalesRequestNow.mypage";
	document.frm.method="POST";
	document.frm.submit();
}

$(function(){
	$("#salesNow").css('color', 'white');
	$("#salesNow").css('background-color', '#83b14e');
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
<form name="frm">
	<div class="totalbody">
		<div class="title">
			<h1>My Page</h1>&nbsp;&nbsp;<h5>현재 판매 중인 내 작품</h5>
		</div>
		<div class="imagebody">
			<input type="hidden" id="file_kind" name="file_kind">
			<div class="container">
		  		<ul class="nav nav-tabs">
	    			<li id="li_image" class="active"><a data-toggle="tab" href="#image" onclick="fn_tabClick('sajin')">이미지</a></li>
	    			<li id="li_video"><a data-toggle="tab" href="#video" onclick="fn_tabClick('youngsang')">동영상</a></li>
	    		</ul>
				<div class="tab-content">
					<div id="${file_kind}" class="tab-pane fade in active">
	   				</div>
	    			<c:if test="${makePage.totalPage>0 }">
	    				<button id="button" class="btn btn-default">저장</button>	
	    			</c:if>
	  			</div>
			</div>
		</div>
	</div>
</form>
<div class="push"></div>
</div>

<!-- contents finish -->

<!-- footer start -->
<c:import url="../temp/footer.jsp"></c:import>
<!-- footer finish -->
</body>
</html>