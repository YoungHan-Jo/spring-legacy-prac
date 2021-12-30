<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
<jsp:include page="/WEB-INF/views/include/head.jsp" />
<style>
#contact {
	padding-top: 300px;
}

#content-box{
	margin-top: 30px;
}

.rows{
	border-bottom: black solid 1px;
}

#row1 {
	font-size: 40px;
}

#row2 {
	font-size: 30px;
}

#row3 {
	padding: 20px;
	font-size: 25px;
	height: 600px;
}

#regDate{
	font-size: 20px;
}


</style>
</head>
<body id="page-top">
	<!-- Navigation-->
	<jsp:include page="/WEB-INF/views/include/nav-bar.jsp" />
	<!-- Contact Section-->
	<section class="page-section" id="contact">
		<div class="container">
			<!-- Contact Section Heading-->
			<h2
				class="page-section-heading text-center text-uppercase text-secondary mb-0">게시글
				상세보기</h2>
			<!-- Icon Divider-->
			<div class="divider-custom">
				<div class="divider-custom-line"></div>
				<div class="divider-custom-icon">
					<i class="fas fa-star"></i>
				</div>
				<div class="divider-custom-line"></div>
			</div>
			
			<div>
				<button type="button" class="btn btn-primary"
					onclick="location.href='/board/list?pageNum=${ pageNum }&type=${ cri.type }&keyword=${ cri.keyword }'">게시글 목록</button>
					
				<c:if test="${ id eq board.memberId || id eq 'admin' }">
					<form action="/board/remove" method="POST">
						<input type="hidden" name="boardNum" value="${ board.num }">
						<input type="hidden" name="pageNum" value="${ pageNum }">
						<button type="submit" class="btn btn-primary">게시글 삭제</button>
					</form>
					<button onclick="location.href='/board/modify?boardNum=${ board.num }&pageNum=${ pageNum }'">수정</button>			
				</c:if>
			</div>
			<!-- Contact Section Form-->
			<div class="row justify-content-center" id="content-box">
				<div class="row rows" id="row1">
					<div id="boardNum" class="col-lg-9">
						<span>${ board.num }</span>&nbsp;&nbsp;&nbsp;
						<span>${ board.subject }</span>
					</div>
					<div id="regDate" class="col-lg-3">
						<span>등록날짜 <fmt:formatDate value="${ board.regDate }" pattern="yyyy.MM.dd HH:mm"/></span>
					</div>
				</div>
				<div class="row rows" id="row2">
					<div id="memberId" class="col-lg-10">
						<span>작성자 ${ board.memberId }</span>
					</div>
					<div id="viewCount" class="col-lg-2">
						<span>조회수 ${ board.viewCount }</span>
					</div>
				</div >
				<div class="row rows" id="row3">
				${ board.content }
				</div>
				<div class="row rows">
					<h3>첨부파일</h3>
					<c:choose>
						<c:when test="${ fn:length(board.attachList) gt 0 }" >
							<c:forEach var="attach" items="${ board.attachList }"> 

								<c:if test="${ attach.filetype eq 'O' }">
									<!-- 일반파일 -->
									<!-- 다운로드할 일반파일 경로 변수 만들기 pageScope로 저장 -->
									<c:set var="fileCallPath"
										value="${ attach.uploadpath }/${ attach.uuid }_${ attach.filename }" />
									<li>
										<a href="/download?fileName=${ fileCallPath }">
											💾 ${ attach.filename }
										</a>
									</li>
								</c:if>
								<c:if test="${ attach.filetype eq 'I' }">
									<!-- 이미지파일 -->
									<c:set var="fileCallPath"
										value="${ attach.uploadpath }/s_${ attach.uuid }_${ attach.filename }" />
									<c:set var="fileCallPathOrigin"
										value="${ attach.uploadpath }/${ attach.uuid }_${ attach.filename }" />
									<li>
										<a href="/download?fileName=${ fileCallPathOrigin }"> 
											💾 ${ attach.filename }<img src="/display?fileName=${ fileCallPath }" style="width: 200px">
										</a>
									</li>
								</c:if>
							</c:forEach>
						</c:when>
						<c:otherwise>
							첨부파일 없음
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</section>
	<!-- Footer-->
	<jsp:include page="/WEB-INF/views/include/footer.jsp" />
	<!-- Bootstrap core JS-->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
	<!-- Core theme JS-->
	<script src="/resources/js/scripts.js"></script>
	<script src="https://cdn.startbootstrap.com/sb-forms-latest.js"></script>
	<script>
		
	</script>
</body>
</html>
