<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>


	<h1>관리자 페이지</h1>
	<hr>
	<button onclick="location.href='/admin/getOneMember'">회원 한명만 검색</button>
	<button onclick="location.href='/admin/getAllMembers'">모든 회원 검색</button>
	<button onclick="location.href='/admin/insertMember'">회원 추가</button>
	<button onclick="location.href='/admin/deleteMember'">회원 삭제</button>
	<button onclick="location.href='/admin/updateMember'">회원 수정</button>

	<script src="/resources/js/jquery-3.6.0.js"></script>
	<script>
	
	</script>
</body>
</html>