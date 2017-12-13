<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	<c:forEach items="${requestScope.video}" var="dto">
		if(${dto.sell eq 'Y'}){
			$("#" + ${dto.file_num}).prop("checked", true);
		}else	{
			$("#" + ${dto.file_num}).prop("checked", false);
		}
	</c:forEach>
</script>
</head>
<body>
<table>
	    <c:forEach items="${requestScope.video}" var="dto">
	    	<c:if test="${dto.file_kind eq 'video'}">
				<tr class="trtable1">
					<td class="tdtable1">
						<input type="checkbox" class="img" name="view" id="${dto.file_num}" value="${dto.work_seq}">
						<div class="images1">
							<div>
							<video src="${pageContext.request.contextPath}/upload/${dto.file_name} " width="200" height="200" controls="controls"></video>
							</div>
						</div>
					</td>
				</tr>
			</c:if>
	    </c:forEach>
			</table>
			
<c:if test="${makePage.totalPage>0 }">
		<div class="paging">
			<ul class="pagination">
				<c:if test="${makePage.curBlock>1}">
					<li><a href="./mypageSalesRequestNow.mypage?curPage=1&file_kind=${file_kind}">&lt;&lt;</a></li>
					<li><a href="./mypageSalesRequestNow.mypage?curPage=${makePage.startNum-1}&file_kind=${file_kind}">[이전]</a></li>
				</c:if>
				<c:forEach begin="${makePage.startNum}" end="${makePage.lastNum}" var="i">
					<li><a href="./mypageSalesRequestNow.mypage?curPage=${i}&file_kind=${file_kind}">${i}</a></li>
				</c:forEach>
				<c:if test="${makePage.curBlock<makePage.totalBlock}">
					<li><a href="./mypageSalesRequestNow.mypage?curPage=${makePage.lastNum+1}&file_kind=${file_kind}">[다음]</a></li>
					<li><a href="./mypageSalesRequestNow.mypage?curPage=${makePage.totalPage}&file_kind=${file_kind}">&gt;&gt;</a></li>
				</c:if>
			</ul>
		</div>
	</c:if>	
</body>
</html>