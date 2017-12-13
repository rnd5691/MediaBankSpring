<!-- 회원 가입시, 계정 선택 부분 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원 가입</title>
<link href="${pageContext.request.contextPath}/resources/css/header.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/join/joinForm_info.css" rel="stylesheet">

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>   
<script type="text/javascript">
	var ch = false;
	var id_ch = true;
	var pw_ch = true;
	$(function(){
		$("#add").load('table?kind='+'${kind}');
		
		$("#id").change(function(){
			id_ch = false;
		});
		
		$("#id_btn").click(function(){
			id_ch = true;
			var id = $("#id").val();
			
			$.post('idCheck?id='+id, function(result){
				$("#id_check").html(result);
				if($("#id_check").text().trim()=='이미 사용 중인 ID 입니다.'){
					id_ch = false;
					$("#id_check").css('color', 'red');
				}else{
					$("#id_check").css('color', 'blue');
				}
			});
		});
		
		$('#pw').change(function(){
			pw_ch = false;
			
			var pw = $("#pw").val();
			var number = 0;
			var charic = 0;
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
						charic++;
					}else if(specialChar.test(pw.charAt(i))){
						/* 특수문자가 있을 때 */
						special++;
					}else if(trim.test(pw.charAt(i))){
						/* 공백이 존재할때 */
						tm++;
					}else if('A'<=pw.charAt(i) && pw.charAt(i)<='Z'){
						/* 대문자가 있을 때 */
						charic++;
					}
				}
			}
			
			if(number>0 && charic>0 && special>0 && tm==0){
				pw_ch = true;
			}
			
			$.post("pwCheck?check="+pw_ch, function(result){
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
				pw_ch = true;
			}else{
				pw_ch = false;
			}
			

			if($("#pw").val()==$("#pwch").val()){
				pw_ch = true;
			}else{
				pw_ch = false;
			}
			
			$.get("pwchCheck?check="+pw_ch, function(result){
				$("#pwch_check").html(result);
				if($("#pwch_check").text().trim()=='비밀 번호가 일치 합니다.'){
					$("#pwch_check").css('color', 'blue');
				}else{
					$("#pwch_check").css('color', 'red');
				}
			});
		});
		
		$("#pwch").change(function(){
			pw_ch = false;
			
			if($("#pw").val()==$("#pwch").val()){
				pw_ch = true;
			}
			
			$.get("pwchCheck?check="+pw_ch, function(result){
				$("#pwch_check").html(result);
				if($("#pwch_check").text().trim()=='비밀 번호가 일치 합니다.'){
					$("#pwch_check").css('color', 'blue');
				}else{
					$("#pwch_check").css('color', 'red');
				}
			});
		});
		
		$("#btn").click(function(){
			if(id_ch==true && pw_ch==true){
				ch=true;
			}
			if(id_ch==false){
				alert("ID 중복 확인을 하시기 바랍니다.");
			}else{
				if(ch==false){
					alert("ID 나 비밀번호를 확인 하십시오.");
				}else{
					document.frm.setAttribute('action', 'join');
				}
			}
		});
	});
</script>
<body oncontextmenu="return false" ondragstart="return false" onselectstart="return false">
<c:import url="../temp/header.jsp"></c:import>
<!-- Contents -->
<section class="body">
	<article class="contents">
		<article class="title">
			<h1>회원 가입</h1>  <h5><c:if test="${kind == 'company'}">기업</c:if><c:if test="${kind == 'person'}">개인</c:if>회원 가입</h5>
		</article>
		<article class="join_info">
			<form name="frm" method="post">
				<input type="hidden" name="kind" value="${kind}">
				<table class="table table-striped">
					<tr>
						<td><span>*</span>ID</td>
						<td>
							<input id="id"type="text" name="id" required="required">
							<input id="id_btn" type="button" class="btn btn-default" value="중복확인">
							<div id="id_check"></div>
						</td>
					</tr>
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
					<tr>
						<td>연락처</td>
						<td><input type="text" name="phone"></td>
					</tr>
					<tr>
						<td><span>*</span>이메일</td>
						<td><input type="email" name="email" required="required"></td>
					</tr>
					<tbody id="add"></tbody>
				</table>
				<div>
					<a href="kind" class="btn btn-default">취소</a>
					<button id="btn" class="btn btn-default">확인</button>
				</div>
			</form>
		</article>
	</article>
	<div class="push"></div>
</section>
<!-- Contents 끝 -->
<c:import url="../temp/footer.jsp"></c:import>
</body>
</html>