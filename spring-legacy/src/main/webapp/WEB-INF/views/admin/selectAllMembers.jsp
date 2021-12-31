<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>


	<h1>전체 회원정보 가져오기</h1>
	<hr>


	<button type="button" id="btn">버튼</button>
	<br>
	<table border="1">
	</table>

	<script src="/resources/js/jquery-3.6.0.min.js"></script>
	<script>
		
		function showData(array){
			var str = `
					<tr>
						<th>아이디</th><th>이름</th><th>성별</th><th>생년월일</th>
					<tr>
					`;
					
			if(array != null && array.length > 0) {
				
				for(var member of array) {
					
					str += `
						<tr>/* jsp문서라서 \$로 해야함 */
							<td>\${member.id}</td>
							<td>\${member.name}</td>
							<td>\${member.gender}</td>
							<td>\${member.birthday}</td>
						</tr>
					`;
				} // for
				
			}else{ // array != null || array.length == 0
				str = `
					<tr><td>해당하는 데이터가 없습니다.</td></tr>
				`;
			}
			$('table').html(str);
			
		}// showData
	
		$('button#btn').on('click', function() {

			//ajax 함수 호출 - 비동기(쓰레드랑 비슷) 자바스크립트
			$.ajax({
				url : '/api/members',
				method : 'GET',
				success : function(data){
					console.log(data);
					console.log(typeof data);
					
					showData(data);
					
				}
			});
		})
	</script>
</body>
</html>