<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MY PAGE_내 정보</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link href="${pageContext.request.contextPath}/resources/css/header.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/mypage/mypage.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/mypage/myinfo.css" rel="stylesheet">
<script type="text/javascript">
	var ch = true;
	$(function(){
		var message = '${message}';
		if(message != ""){
			alert(message);
		}
		$("#add").load('mypageAdd');
		$("#myinfo").css('color', 'white');
		$("#myinfo").css('background-color', '#83b14e');


		$('#pw').change(function(){
			ch = false;
			
			var pw = $("#pw").val();
			var number = 0;
			var char = 0;
			var special = 0;
			var tm = 0;
			
			var trim = /\s/;
			var specialChar = /[\[\]{}()<>?|`~!@#$%^&*-_+=,.;:\"\\\'\\']/g;
			if(pw.length>=8){
				for(var i=0; i<pw.length; i++){
					/* 숫자 일 때 */
					if('0'<=pw.charAt(i) && pw.charAt(i)<='9'){
						number++;
					}else if('a'<=pw.charAt(i) && pw.charAt(i)<='z'){
						/* 소문자일때 */
						char++;
					}else if(specialChar.test(pw.charAt(i))){
						/* 특수문자가 있을 때 */
						special++;
					}else if(trim.test(pw.charAt(i))){
						/* 공백이 존재할때 */
						tm++;
					}else if('A'<=pw.charAt(i) && pw.charAt(i)<='Z'){
						/* 대문자가 있을 때 */
						char++;
					}
				}
			}
			
			if(number>0 && char>0 && special>0 && tm==0){
				ch = true;
			}
			
			$.get("mypagePwCheck.mypage?check="+ch, function(result){
				$("#pw_check").html(result);
				if($("#pw_check").text().trim()=='사용 가능한 PW 입니다.'){
					$("#pw_check").css('color', 'blue');
					$(".contents").css('height', '600px');
				}else{
					$("#pw_check").css('color', 'red');
					$(".contents").css('height', '650px');
				}
			});
			
			if($("#pw").val()==$("#pwch").val()){
				ch = true;
			}else{
				ch = false;
			}
			

			if($("#pw").val()==$("#pwch").val()){
				ch = true;
			}else{
				ch = false;
			}
			
			$.get("mypagePwchCheck.mypage?check="+ch, function(result){
				$("#pwch_check").html(result);
				if($("#pwch_check").text().trim()=='비밀 번호가 일치 합니다.'){
					$("#pwch_check").css('color', 'blue');
				}else{
					$("#pwch_check").css('color', 'red');
				}
			});
		});
		
		$("#pwch").change(function(){
			ch = false;
			
			if($("#pw").val()==$("#pwch").val()){
				ch = true;
			}
			
			$.get("mypagePwchCheck.mypage?check="+ch, function(result){
				$("#pwch_check").html(result);
				if($("#pwch_check").text().trim()=='비밀 번호가 일치 합니다.'){
					$("#pwch_check").css('color', 'blue');
				}else{
					$("#pwch_check").css('color', 'red');
				}
			});
		});
		
		$("#btn").click(function(){
			if(ch==true){
				$("#frm").prop('action', 'update');
			}else{
				alert("비밀번호를 확인 하십시오.");
			}
		});
		
	});
</script>
</head>
<body oncontextmenu="return false" ondragstart="return false" onselectstart="return false">
<!-- header start -->
<c:import url="../temp/header.jsp"></c:import>
<!-- header finish -->

<!-- contents start -->
<!-- menu는 mypage나 구매목록이 나오는 탭 부분 -->

<section class="body">
	<c:import url="./menu.jsp"></c:import>
	<div class="totalbody">
		<div class="title">
			<h1>MY PAGE</h1> <h5>내 정보</h5>
		</div>
		<article class="join_info">
			<form id="frm" name="frm" method="post">
				<input type="hidden" name="kind" value="">
				<table class="table table-striped">
					<tr>
						<td>회원 번호</td>
						<td>
							<input type="text" name="user_num" readonly="readonly" value="${member.user_num}">
						</td>
					</tr>
					<tr>
						<td><span>*</span>ID</td>
						<td>
							<input id="id"type="text" name="id" readonly="readonly" value="${member.id}">
						</td>
					</tr>
					<c:if test="${empty member.token}">
						<tr>
							<td><span>*</span>PW</td>
							<td>
								<input id="pw" type="password" name="pw" required="required">
								<div id="pw_check"></div>
							</td>
						</tr>
						<tr>
							<td><span>*</span>PW 확인</td>
							<td>
								<input id="pwch" type="password" required="required">
								<div id="pwch_check"></div>
							</td>
						</tr>
					</c:if>
					<tr>
						<td>연락처</td>
						<td><input class="change" type="text" name="phone" value="${member.phone}"></td>
					</tr>
					<tr>
						<td><span>*</span>이메일</td>
						<td><input class="change" type="email" name="email" required="required" value="${member.email}"></td>
					</tr>
					<tbody id="add"></tbody>
				</table>
				<div id="btn">
					<button  class="btn btn-default">저장</button>
				</div>
			</form>
		</article>
	</div>
	<div class="push"></div>
</section>

<!-- contents finish -->

<!-- footer start -->
<c:import url="../temp/footer.jsp"></c:import>
<!-- footer finish -->
</body>
</html>