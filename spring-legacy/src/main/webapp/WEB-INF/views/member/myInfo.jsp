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

			<!-- Contact Section Form-->
			<div class="row justify-content-center">
				<!-- side-menu -->
				<jsp:include page="/WEB-INF/views/include/side-menu.jsp" />
				<div class="col-lg-6">
					<!-- Contact Section Heading-->
					<h2
						class="page-section-heading text-center text-uppercase text-secondary mb-0">내
						정보 보기</h2>
					<!-- Icon Divider-->
					<div class="divider-custom">
						<div class="divider-custom-line"></div>
						<div class="divider-custom-icon">
							<i class="fas fa-star"></i>
						</div>
						<div class="divider-custom-line"></div>
					</div>
					<form action="/member/join" method="POST" id="contactForm"
						data-sb-form-api-token="API_TOKEN">
						<!-- id input-->
						<div class="form-floating mb-3">
							<input class="form-control" id="id" type="text" name="id"
								data-sb-validations="required" value="${ member.id }" disabled /> <label for="id">아이디</label>
						</div>
						<!-- name input-->
						<div class="form-floating mb-3">
							<input class="form-control" id="name" type="text" name="name"
								data-sb-validations="required" value="${ member.name }" disabled /> <label for="name">이름</label>
						</div>
						<!-- birthday input-->
						<div class="form-floating mb-3">
							<input name="birthday" class="form-control" id="birthday"
								type="date" data-sb-validations="required" value="${ member.birthday }" disabled
								data-sb-can-submit="no"> <label for="birthday">생년월일</label>
							<div class="invalid-feedback" data-sb-feedback="name:required">
								birthday is required.</div>
						</div>
						<!-- gender input-->
						<div class="form-floating mb-3">
							<select name="gender" class="form-control" id="gender" disabled
								data-sb-validations="required" data-sb-can-submit="no">
								<option value="" disabled>성별을 선택하세요.</option>
								<option value="M" ${ member.gender eq 'M' ? 'selected' : '' }>남자</option>
								<option value="F" ${ member.gender eq 'F' ? 'selected' : '' }>여자</option>
								<option value="N" ${ member.gender eq 'N' ? 'selected' : '' }>선택 안함</option>
							</select> <label for="gender">성별</label>
						</div>

						<!-- email input-->
						<div class="form-floating mb-3">
							<input class="form-control" id="email" type="email" name="email"
								data-sb-validations="required" value="${ member.email }" disabled /> <label for="email">이메일</label>
						</div>
						<!-- recvEmail input-->
						<div class="form-floating mb-3">
							<select name="recvEmail" class="form-control" id="recvEmail"
								disabled data-sb-validations="required" data-sb-can-submit="no">
								<option value="" disabled>메일 수신 여부를 선택하세요</option>
								<option value="Y" ${ member.recvEmail eq 'Y' ? 'selected' : '' }>예</option>
								<option value="N" ${ member.recvEmail eq 'N' ? 'selected' : '' }>아니오</option>
							</select> <label for="recvEmail">메일 수신 여부</label>
							<div class="invalid-feedback" data-sb-feedback="name:required">
								choosing is required.</div>
						</div>
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
	<script>
		
	</script>
</body>
</html>
