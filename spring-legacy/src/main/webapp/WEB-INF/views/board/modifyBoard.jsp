<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
<jsp:include page="/WEB-INF/views/include/head.jsp" />
<style>
#contact {
	padding-top: 300px;
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
				class="page-section-heading text-center text-uppercase text-secondary mb-0">게시글 수정</h2>
			<!-- Icon Divider-->
			<div class="divider-custom">
				<div class="divider-custom-line"></div>
				<div class="divider-custom-icon">
					<i class="fas fa-star"></i>
				</div>
				<div class="divider-custom-line"></div>
			</div>
			<!-- Contact Section Form-->
			<div class="row justify-content-center">
				<div class="col-lg-8 col-xl-7">
					<form action="/board/modify" method="POST" enctype="multipart/form-data" 
						id="contactForm" data-sb-form-api-token="API_TOKEN">
						
						
						<input type="hidden" name="num" value="${ board.num }">
						<input type="hidden" name="pageNum" value="${ pageNum }">
						
						<div class="form-floating mb-3">
							<input class="form-control" id="subject" type="text" name="subject"
								value="${ board.subject }"
								data-sb-validations="required" /> <label for="subject">제목</label>
						</div>
						<div class="form-floating mb-3">
							<input class="form-control" id="content" type="text" name="content"
								value="${ board.content }"
								data-sb-validations="required" /> <label for="content">내용</label>
						</div>
						
						<div class="row">
						<div class="col s12">
							<button type="button"
								class="btn btn-primary btn-m btn-addFile" id="btnAddFile">+ 파일 추가</button>
							</div>
						</div>
						
						<!-- 기존 첨부 파일 목록 -->
						<div class="row" id="oldFileBox">
							<%-- .delete-oldfile 버튼 클릭 시 hidden input의 name속성값이 oldfile->delfile로 변환 --%>
							<%-- 서버에서는 oldfile은 찾지않고 delfile만 찾아서 파일 삭제처리 --%>
							<c:forEach var="attach" items="${ board.attachList }">
								<input type="hidden" name="oldfile" value="${ attach.uuid }">
								<div class="col s12">
									<span class="filename">${ attach.filename }</span>
									<button class="btn delete-oldfile">❌</button>
								</div>
								</br>
							</c:forEach>
						</div>
						
						<!-- 신규 첨파 파일 목록 -->
						<div class="row" id="newFileBox"></div>
						
						<!-- Submit Button-->
						<button class="btn btn-primary btn-xl" id="submitButton"
							type="submit">글 수정하기</button>
					</form>
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
	<script src="/resources/js/jquery-3.6.0.min.js"></script>
	<script>
	
	
	var currentFileCount = ${ fn:length(board.attachList) }; // 현재 첨부된 파일 개수
	const MAX_FILE_COUNT = 5; // 최대 첨부파일 개수
		
	$('#btnAddFile').on('click',function(){
		if(currentFileCount >= MAX_FILE_COUNT){
			alert(`첨부파일은 최대 \${MAX_FILE_COUNT}개까지 가능합니다.`);
			/* js에서 백틱으로 표현할때 \${ }을 jsp로 사용하면 EL언어로 인식하기 때문에 앞에 \를 넣어야함 */
			return;
		}
		
		var str = `<div class="col s12">
						<input type="file" name="files"> 
						<button class="btn delete-addfile">
						❌</button>
					</div>`;
					
		$('#newFileBox').append(str);
		
		currentFileCount++;
	
	})
	
	// 동적 이벤트 연결 (이벤트 등록을 이미 존재하는 요소에게 위임하는 방식)
	$('#newFileBox').on('click','button.delete-addfile',function(){
		$(this).closest('div').remove();
		currentFileCount--;
	})
	
	// 기존 첨부파일의 삭제버튼 눌렀을때
	$('button.delete-oldfile').on('click',function(){
	
		// name속성의 값을 oldfile -> delfile(서버에서 찾을 파라미터값, 파일삭제용도)
		$(this).parent().prev().prop('name','delfile'); 
		
		//현재 클릭한 요소의 직계부모(parent)요소를 삭제하기
		$(this).parent().remove();
		
		currentFileCount--;
	})
	
	
		
	</script>
</body>
</html>
