<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<nav
	class="navbar navbar-expand-lg bg-secondary text-uppercase fixed-top"
	id="mainNav">
	<div class="container">
		<a class="navbar-brand" href="/index">Start Bootstrap</a>
		<button
			class="navbar-toggler text-uppercase font-weight-bold bg-primary text-white rounded"
			type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarResponsive" aria-controls="navbarResponsive"
			aria-expanded="false" aria-label="Toggle navigation">
			Menu <i class="fas fa-bars"></i>
		</button>
		<div class="collapse navbar-collapse" id="navbarResponsive">
			<ul class="navbar-nav ms-auto">
				<li class="nav-item mx-0 mx-lg-1"><a
					class="nav-link py-3 px-0 px-lg-3 rounded" href="/board/list">게시판</a></li>
				<c:choose>
					<c:when test="${ not empty sessionScope.id }">
						<li class="nav-item mx-0 mx-lg-1"><a
							class="nav-link py-3 px-0 px-lg-3 rounded" href="/member/myInfo">내정보</a></li>
						<li class="nav-item mx-0 mx-lg-1"><a
							class="nav-link py-3 px-0 px-lg-3 rounded" href="/member/logout">로그아웃</a></li>
					</c:when>
					<c:otherwise>
						<li class="nav-item mx-0 mx-lg-1"><a
							class="nav-link py-3 px-0 px-lg-3 rounded" href="/member/login">로그인</a></li>
						<li class="nav-item mx-0 mx-lg-1"><a
							class="nav-link py-3 px-0 px-lg-3 rounded" href="/member/join">회원가입</a></li>
					</c:otherwise>
				</c:choose>
				<c:if test="${ id eq 'admin' }">
					<li class="nav-item mx-0 mx-lg-1"><a
							class="nav-link py-3 px-0 px-lg-3 rounded" href="/admin/home">관리자</a></li>
				</c:if>

			</ul>
		</div>
	</div>
</nav>