package com.example.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.MemberVO;
import com.example.service.MemberService;

/*
REST API 방식 컨트롤러의 HTTP method 매핑 규칙
POST	- Create ( SQL Insert문 )
GET		- Read   ( SQL Select문 )
PUT		- Update ( SQL Update문 )
DELETE	- Delete ( SQL Delete문 )
*/

//GET 요청 - http://localhost:8090/api/members/user1 -> XML으로 응답
//GET 요청 - http://localhost:8090/api/members/user1.json -> JSON으로 응답
//--------jackson-databind 라이브러리(json 변환기)---------maven pom.xml추가하기
//---------jackson-dataformat-xml 라이브러리(xml 변환기)-------- maven pom.xml추가하기

//일반 Controller 애노테이션 클래스에서 사용한다면
//특정 Rest방식 메소드에 ResponseBody 애노테이션을 붇이면 됨.
@RestController // 클래스의 모든 메소드의 리턴값이 JSON 또는 XML를 바로 응답으로 보내도록 동작함, 경로가 아닌 데이터만 보냄.
@RequestMapping("/api/*")
public class MemberRestController {

	private MemberService memberService;

	public MemberRestController(MemberService memberService) {
		super();
		this.memberService = memberService;
	}

	// json
//	{
//		'name' : '망고',
//		'age' : 5,
//		'gender' : 'male'
//		
//	}

	// xml
//	<name>망고</name>
//	<age>5</age>
//	<gender>male</gender>

	// 중괄호로 하면 스프링이 알아서 해줌
	// gson 도움 없이 스프링이 json 미디어 타입, xml 미디어 타입으로 산출하도록
	// produces 는 기본값이므로 생략가능
	@GetMapping(value = "/members/{id}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Map<String, Object>> getOneMember(@PathVariable("id") String id) {
		// Pathvariable애노테이션. /members/{id} 경로로 받은 경로변수(id) 값 연결 시키기
		
		System.out.println("id : " + id);

		MemberVO memberVO = memberService.getMemberById(id);
		int count = memberService.getCountById(id);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("member", memberVO);
		map.put("count", count);
		
		System.out.println("map : " + map);

		// 상태코드도 함게 넘기기
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	} // getOneMember

	@GetMapping(value = "/members", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<MemberVO>> getAll() {

		List<MemberVO> memberList = memberService.getAllMembers();

		return new ResponseEntity<List<MemberVO>>(memberList, HttpStatus.OK);
	} // getAll

	// consumes속성 : 받을 자료형 json형태로만 받는다.
	// produces : 리턴하는 자료형 json, xml 형태로 리턴함
	@PostMapping(value = "/members", consumes = "application/json", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Map<String, Object>> create(@RequestBody MemberVO memberVO) {
		// json으로 읽을 것이다 라는 선언 requestbody애노테이션
		// RequestBody 애노테이션 : spring이 알아서 json을 분해 해서 memberVO 객체에 넣어줌
		// body는 실제 데이터를 의미함. ( 요청받은 실제 데이터 )
		// ResponseBody 애노테이션 : json,xml형으로 변형해서 응답을 하도록
		// body는 실제 데이터를 의미함. ( 응답으로 내보낼 실제 데이터 )

		// 회원가입 날짜 설정
		memberVO.setRegDate(new Date());

		// 비밀번호를 jbcrypt 라이브러리 사용해서 암호화하여 저장하기
		String passwd = memberVO.getPasswd();
		String pwHash = BCrypt.hashpw(passwd, BCrypt.gensalt());
		memberVO.setPasswd(pwHash); // 암호화된 비밀번호 문자열로 수정하기

		// 생년월일 '-' 제거
		String birthday = memberVO.getBirthday();
		birthday = birthday.replace("-", "");
		memberVO.setBirthday(birthday);

		System.out.println(memberVO);

		memberService.insertMember(memberVO);

		// insert 된 VO객체를 OK상태코드와 함게 응답으로 줌

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", "success");
		map.put("member", memberVO);

		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);

	} // create

	@DeleteMapping(value = "/members/{id}", produces = MediaType.TEXT_PLAIN_VALUE) // 텍스트파일 리턴
	public ResponseEntity<String> delete(@PathVariable("id") String id) {

		int count = memberService.deleteMemberAndGetCount(id);

		// 정상적인 처리 200번대 코드
		// 에러 시 400번대 코드

		// INTERNAL_SERVER_ERROR (500) : 서버 로직 에러.
		// BAD_GATEWAY (502) : 외부에서 전달받은 값이 잘못되어 오류가 발생한 경우
		
//		if(count > 0) {
//			new ResponseEntity<String>("success", HttpStatus.OK)
//		}else {
//			new ResponseEntity<String>("fail", HttpStatus.BAD_GATEWAY)
//		}
		
		return count > 0 ? new ResponseEntity<String>("success", HttpStatus.OK) 
				: new ResponseEntity<String>("fail", HttpStatus.BAD_GATEWAY);

	}// delete

	// 일반 문자열을 리턴하면 produces속성을 text plain value로
	@PutMapping(value = "/members/{id}", consumes = "application/json", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> modify(@RequestBody MemberVO memberVO, @PathVariable String id) {
		// json으로 읽을 것이다 라는 선언 requestbody애노테이션
		// RequestBody 애노테이션 : spring이 알아서 json을 분해 해서 memberVO 객체에 넣어줌
		// body는 실제 데이터를 의미함. ( 요청받은 실제 데이터 )
		// ResponseBody 애노테이션 : json,xml형으로 변형해서 응답을 하도록
		// body는 실제 데이터를 의미함. ( 응답으로 내보낼 실제 데이터 )

		// 회원가입 날짜 설정
		memberVO.setRegDate(new Date());

		// 생년월일 '-' 제거
		String birthday = memberVO.getBirthday();
		birthday = birthday.replace("-", "");
		memberVO.setBirthday(birthday);

		System.out.println(memberVO);

		memberService.modifyMember(memberVO);

		return new ResponseEntity<String>("success", HttpStatus.OK);

	} // modify

}
