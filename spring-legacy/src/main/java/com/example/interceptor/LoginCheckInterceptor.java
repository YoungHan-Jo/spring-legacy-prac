package com.example.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.example.domain.MemberVO;
import com.example.service.MemberService;

public class LoginCheckInterceptor implements HandlerInterceptor {

	private MemberService memberService;

	public LoginCheckInterceptor(MemberService memberService) {
		super();
		this.memberService = memberService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");

		if (id == null) {

			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");

			response.getWriter()
					.print("<script>alert('로그인이 필요한 서비스입니다'); location.href = '/member/login';</script>");

			return false; // 예정이었던 컨트롤러는 호출 하지 말아야함. false
		}

		MemberVO memberVO = memberService.getMemberById(id);
		System.out.println(memberVO.toString());

		return true;
	}

}
