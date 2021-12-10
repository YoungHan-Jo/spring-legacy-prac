package com.example.controller;

import java.util.Date;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.MemberVO;
import com.example.service.MemberService;
import com.example.util.JScript;

@Controller
@RequestMapping("/member/*")
public class MemberController {

	private MemberService memberService;

	public MemberController(MemberService memberService) {
		super();
		this.memberService = memberService;
	}

	@GetMapping("/login")
	public String loginForm() {
		System.out.println("loginForm() 호출됨...");
		return "member/login";
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(String id, String passwd) {
		// 1. 아이디 존재 여부 체크
		MemberVO dbMemberVO = memberService.getMemberById(id);

		if (dbMemberVO == null) { // 아이디가 존재하지 않을 경우
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");
			String str = JScript.back("존재하지 않는 아이디 입니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}
		// 2. 비밀번호 체크
		String realHashPasswd = dbMemberVO.getPasswd();
		
		Boolean isRightPasswd = BCrypt.checkpw(passwd, realHashPasswd);

		// 3. 세션 등록

		// 4. 쿠키 등록

		// 5. 메인화면을 보내기

		return null;
	}

	@GetMapping("/join")
	public String joinForm() {
		System.out.println("joinForm() 호출됨...");
		return "member/join";
	}

	@PostMapping("/join")
	public ResponseEntity<String> join(MemberVO memberVO, String passwdConfirm) {
		// 1.아이디 중복 체크
		String id = memberVO.getId();

		MemberVO dbMemberVO = memberService.getMemberById(id);

		if (dbMemberVO != null) { // 중복된 아이디가 있음
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");
			String str = JScript.back("중복된 아이디가 있습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}

		// 2.비밀번호 확인 맞는지 체크
		String passwd = memberVO.getPasswd();

		if (passwd.equals(passwdConfirm) == false) { // 비밀번호와 비밀번호 확인이 서로 일치하지 않음
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");
			String str = JScript.back("비밀번호가 서로 일치하지 않습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}
		System.out.println("memberVO : " + memberVO);

		// 회원 가입 날짜
		memberVO.setRegDate(new Date());

		// 비밀번호 암호화
		String hashPasswd = BCrypt.hashpw(passwd, BCrypt.gensalt());

		memberVO.setPasswd(hashPasswd);

		System.out.println("수정 후 memberVO : " + memberVO);

		// 3. DB에 회원 정보 등록하기(가입하기)
		memberService.insertMember(memberVO);

		// 4. 회원가입 완료 메세지 + 로그인 페이지로 보내기
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");
		String str = JScript.href("회원가입 완료", "/member/login");

		return new ResponseEntity<String>(str, headers, HttpStatus.OK);
	}

}
