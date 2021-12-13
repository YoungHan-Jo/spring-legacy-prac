package com.example.controller;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	} // loginForm

	@PostMapping("/login")
	public ResponseEntity<String> login(String id, String passwd, boolean rememberMe, HttpServletResponse response,
			HttpSession session) {
		System.out.println("id : " + id);
		System.out.println("passwd : " + passwd);
		System.out.println("rememberMe : " + rememberMe);

		// 1. id 존재여부
		MemberVO dbMemberVO = memberService.getMemberById(id);
		if (dbMemberVO == null) { // 존재하지 않는 아이디
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");

			String str = JScript.back("아이디가 존재하지 않습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}

		// 2. 비밀번호 체크
		Boolean isPasswdRight = BCrypt.checkpw(passwd, dbMemberVO.getPasswd());

		if (isPasswdRight == false) { // 비밀번호가 틀림
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");

			String str = JScript.back("비밀번호가 틀렸습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}

		// 3. 로그인 유지 체크했는지
		if (rememberMe == true) { // 로그인 유지에 체크 했을 때
			// 쿠키등록하기
			Cookie cookie = new Cookie("userId", id);
			cookie.setMaxAge(60 * 60 * 24 * 7); // 쿠키 수명 설정 초단위
			cookie.setPath("/"); // 모든경로에 적용

			response.addCookie(cookie);

		}
		// 4. 세션 등록
		session.setAttribute("id", id);

		// 5. 로그인 성공 메세지 띄우고, 메인화면으로 이동
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");

		String str = JScript.href("로그인 성공", "/");

		return new ResponseEntity<String>(str, headers, HttpStatus.OK);
	}

	@GetMapping("/join")
	public String joinForm() {
		System.out.println("joinForm() 호출됨...");
		return "member/join";
	} // joinForm

	@PostMapping("/join")
	public ResponseEntity<String> join(MemberVO memberVO, String passwdConfirm) {

		// 1. 아이디 중복체크(DB에 있는지 확인)
		String id = memberVO.getId();

		MemberVO dbMemberVO = memberService.getMemberById(id);
		System.out.println("dbMemberVO : " + dbMemberVO);

		if (dbMemberVO != null) { // 이미존재하는 아이디
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");

			String str = JScript.back("이미 존재하는 아이디입니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}
		// 2. 비밀번호, 비밀번호 확인 서로 같은지 체크
		String passwd = memberVO.getPasswd();

		// 비밀번호가 서로 다를때
		if (passwd.equals(passwdConfirm) == false) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");

			String str = JScript.back("비밀번호가 서로 다릅니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}

		// 아이디체크. 비밀번호체크 모두 통과했을때
		System.out.println("수정 전 memberVO : " + memberVO);
		// 회원가입 날짜 설정하기
		memberVO.setRegDate(new Date());

		// 비밀번호 암호화하기
		String hashPasswd = BCrypt.hashpw(passwd, BCrypt.gensalt());
		memberVO.setPasswd(hashPasswd);

		System.out.println("수정 후 memberVO : " + memberVO);

		// 3. DB에 등록
		memberService.insertMember(memberVO);

		// 4. 회원가입완료 메세지를 띄우고, 로그인화면으로 이동
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");

		String str = JScript.href("회원가입 완료", "/member/login");

		return new ResponseEntity<String>(str, headers, HttpStatus.OK);
	} // join

	@GetMapping("/logout")
	public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		// 세션 비우기
		session.invalidate();

		// 쿠키 수명 0으로 만들어서 보내기
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("userId")) {
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}
		}

		return "index";
	}

	@GetMapping("/myInfo")
	public String myInfo(HttpSession session, Model model) {

		String id = (String) session.getAttribute("id");

		MemberVO memberVO = memberService.getMemberById(id);

		model.addAttribute("member", memberVO);

		return "member/myInfo";
	}

	@GetMapping("/modify")
	public String modifyForm(HttpSession session, Model model) {

		String id = (String) session.getAttribute("id");

		MemberVO memberVO = memberService.getMemberById(id);

		model.addAttribute("member", memberVO);

		return "member/memberModify";
	}

	@PostMapping("/modify")
	public ResponseEntity<String> modify(MemberVO memberVO, HttpSession session) {

		String id = (String) session.getAttribute("id");

		System.out.println("memberVO : " + memberVO);
		memberVO.setId(id);
		System.out.println("수정 후 memberVO : " + memberVO);

		// 1. 비밀번호 맞는지 체크
		MemberVO dbMemberVO = memberService.getMemberById(id);

		boolean isPasswdRight = BCrypt.checkpw(memberVO.getPasswd(), dbMemberVO.getPasswd());

		if (isPasswdRight == false) { // 비밀번호 틀림
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");

			String str = JScript.back("비밀번호가 틀렸습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}

		// 2. DB 정보 수정하기
		memberService.modifyMember(memberVO);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");

		String str = JScript.href("회원정보 수정 완료", "/member/myInfo");

		return new ResponseEntity<String>(str, headers, HttpStatus.OK);
	}

	@GetMapping("/passwd")
	public String passwdForm() {

		return "member/passwd";
	}

	@PostMapping("/passwd")
	public ResponseEntity<String> passwd(String passwd, String newPasswd, String newPasswdConfirm,
			HttpSession session) {

		// 1. 현재 비밀번호 맞는지 체크
		String id = (String) session.getAttribute("id");
		MemberVO dbMemberVO = memberService.getMemberById(id);

		boolean isPasswdRight = BCrypt.checkpw(passwd, dbMemberVO.getPasswd());

		if (isPasswdRight == false) { // 현재 비밀번호 일치하지 않음
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");

			String str = JScript.back("현재 비밀번호가 틀렸습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}

		// 2. 새 비밀번호, 새 비밀번호 확인 맞는지 체크
		if (newPasswd.equals(newPasswdConfirm) == false) { // 새비밀번호, 새비밀번호 확인이 서로 다름
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");

			String str = JScript.back("새 비밀번호와 새 비밀번호 확인이 서로 일치하지 않습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}

		// 3. DB 비밀번호 변경
		// 3-1. 비밀번호 암호화
		String hashPasswd = BCrypt.hashpw(newPasswd, BCrypt.gensalt());
		
		memberService.modifyPasswd(id, hashPasswd);

		// 4. 비밀번호 변경 완료 메세지 띄우고 로그아웃처리
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");

		String str = JScript.href("비밀번호 변경 완료", "/member/logout");

		return new ResponseEntity<String>(str, headers, HttpStatus.OK);
	}
	
	@GetMapping("/remove")
	public String removeForm() {
		
		return "member/memberRemove";
	}
	
	@PostMapping("/remove")
	public ResponseEntity<String> remove(String passwd,
			HttpSession session){
		
		// 1. 비밀번호 체크
		String id = (String) session.getAttribute("id");
		MemberVO dbMemberVO = memberService.getMemberById(id);

		boolean isPasswdRight = BCrypt.checkpw(passwd, dbMemberVO.getPasswd());

		if (isPasswdRight == false) { // 현재 비밀번호 일치하지 않음
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");

			String str = JScript.back("현재 비밀번호가 틀렸습니다.");

			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}
		// 2. DB에서 해당 아이디 정보 삭제
		memberService.deleteMemberById(id);
		
		// 3. 회원탈퇴 메세지 띄우고 로그아웃 처리(세션,쿠키 삭제)
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");

		String str = JScript.href("회원탈퇴완료", "/member/logout");

		return new ResponseEntity<String>(str, headers, HttpStatus.OK);
	}

}
