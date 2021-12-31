<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>


	<h1>회원정보 한개 가져오기</h1>
	<hr>

	<input type="text" placeholder="아이디 입력" id="id">
	<button type="button" id="btn">버튼</button>
	<br>
	<table border="1">

	</table>

	<script src="/resources/js/jquery-3.6.0.min.js"></script>
	<script>
		
		function showData(obj){
			var str = '';
			var id = $('#id').val();
			
			if(obj.count > 0) {
				var member = obj.member;
				
				str = `
					<tr>
						<th>아이디</th><td>\${member.id}</td>/* jsp문서라서 \$로 해야함 */
					</tr>
					<tr>
						<th>이름</th><td>\${member.name}</td>
					</tr>
					<tr>
						<th>성별</th><td>\${member.gender}</td>
					</tr>
					<tr>
						<th>생년월일</th><td>\${member.birthday}</td>
					</tr>
					
				`;
			}else{ // obj.count == 0
				str = `
					<tr><td>\${id}해당하는 데이터가 없습니다.</td></tr>
				`;
			}
			$('table').html(str);
		}// showData
	
	
		$('button#btn').on('click', function() {

			var id = $('#id').val();
			console.log('id : ' + id);

			//ajax 함수 호출 - 비동기(쓰레드랑 비슷) 자바스크립트
			$.ajax({
				url : '/api/members/' + id,
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