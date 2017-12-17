<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<script type="text/javascript">
	$(function(){
		$("#yes").click(function(){
			if(${member.token eq null}){
				location.href = "DropOut";
			}else{
				location.href = "naverDropOut.mypage";
			}
		});
	});
</script>
<div class="totalbutton">
	<div class="totalbutton">
		<div class="btn-group-vertical">
			<div class="mypage">MY PAGE</div>
			<a id="myinfo" href="myinfo" class="btn btn-default">내 정보</a>
			<a id="buyList" class="btn btn-default" href="mypageBuyList.mypage">구매 목록</a>
			<c:if test="${sessionScope.artist eq 'artist' }">
				<a id="salesRequestList" class="btn btn-default" href="salesRequestList">내 작품 판매승인 요청 현황</a>
				<a id="salesNow" class="btn btn-default" href="salesRequestNow">현재 판매 중인 내 작품</a>
				<a id="salesRequestMoney" class="btn btn-default" href="salesRequestMoney">작품 별 수익 현황</a>
			</c:if>
			<!-- Trigger the modal with a button -->
			<button class="btn btn-default"data-toggle="modal" data-target="#session">탈퇴하기</button>
		</div>
	</div>
</div>

<!-- Modal -->
					  <div class="modal fade" id="session" role="dialog">
					    <div class="modal-dialog modal-sm">
					      <div class="modal-content">
					      	
						    <div class="modal-header">
						        <h4 class="modal-title">탈퇴하기</h4>
						    	<button type="button" class="close" data-dismiss="modal">&times;</button>
						    </div>
						    
						    	<div class="modal-body">
						    		탈퇴 하시겠습니까?
						        </div>
						        <div class="modal-footer">
						        	<button type="button" class="btn btn-default" data-dismiss="modal">아니오</button>
						        	<button class="btn btn-default" id="yes">네</button>
						        </div>						   
						   
					      </div>
					    </div>
					  </div>
</html>