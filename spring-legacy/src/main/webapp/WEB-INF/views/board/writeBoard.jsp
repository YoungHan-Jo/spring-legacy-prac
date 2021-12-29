<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
				class="page-section-heading text-center text-uppercase text-secondary mb-0">새글 쓰기</h2>
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
					<form action="/board/write" method="POST" enctype="multipart/form-data" 
						id="contactForm" data-sb-form-api-token="API_TOKEN">
						
						<div class="form-floating mb-3">
							<input class="form-control" id="subject" type="text" name="subject"
								data-sb-validations="required" /> <label for="subject">제목</label>
						</div>
						<div class="form-floating mb-3">
							<input class="form-control" id="content" type="text" name="content"
								data-sb-validations="required" /> <label for="content">내용</label>
						</div>
						
						<div class="row">
						<div class="col s12">
							<button type="button"
								class="btn btn-primary btn-m btn-addFile" id="btnAddFile">+ 파일 추가</button>
							</div>
						</div>
						
						<div class="row" id="fileBox">
							<div class="col-md-12">
								<input class="btn-attachFile" type="file" name="files">
								<button class="file-delete btn">❌</button>
							</div>
						</div>
						
						
						<!-- Submit Button-->
						<button class="btn btn-primary btn-xl" id="submitButton"
							type="submit">새글 쓰기</button>
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
	
	
	// 초기 첨부파일 개수
	var fileCount = 1;
	
	$('#btnAddFile').on('click',function(){
		if(fileCount >= 5){ // 5개 이상일 때
			alert('첨부파일은 최대5개까지 가능합니다.');
			return;
		}
		
		// 5개 미만일때
		var str = `<div class="col-md-12">
						<input type="file" name="files">
						<button class="file-delete btn">❌</button>
					</div>`;
					
		// id가 fileBox인 div에 str을 추가
		$('#fileBox').append(str);
		
		fileCount++; // 파일 수 1개 추가
	
	})
	
	// 동적 이벤트 연결 (이벤트 위임하는 방식)
	// 나중에 생긴 X버튼에도 이벤트를 적용시키기 위해
	$('#fileBox').on('click','button.file-delete',function(){
		// this 는 class 가 file_delete인 버튼 중에 클릭한 버튼
		$(this).closest('div').remove();
		// this에 제일 가까운 조상 div를 삭제시킴
		fileCount--;
	})
	
	
		
	</script>
</body>
</html>
