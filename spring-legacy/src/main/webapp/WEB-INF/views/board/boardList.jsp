<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
<jsp:include page="/WEB-INF/views/include/head.jsp" />
<style>
#contact {
	padding-top: 300px;
}
tr{
	cursor: pointer;
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
				목록</h2>
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
					onclick="location.href='/board/write'">새글 쓰기</button>
			</div>
			<!-- Contact Section Form-->
			<div class="row justify-content-center">
				<table class="table table-hover">
					<thead>
						<tr>
							<th scope="col">게시글 번호</th>
							<th scope="col">글 제목</th>
							<th scope="col">글쓴이</th>
							<th scope="col">등록날짜</th>
							<th scope="col">조회수</th>
						</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${ fn:length(boardList) gt 0 }">
							<c:forEach var="board" items="${ boardList }">
							<tr onclick="location.href='/board/content?num=${board.num}'">
								<th>${ board.num }</th>
								<th>${ board.subject }</th>
								<th>${ board.memberId }</th>
								<th><fmt:formatDate value="${ board.regDate }" pattern="yyyy.MM.dd HH:mm"/></th>
								<th>${ board.viewCount }</th>
							</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<td colspan="5">게시글이 존재하지 않습니다.</td>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>
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
