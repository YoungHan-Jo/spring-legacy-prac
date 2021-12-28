package com.example.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

// HandleInterceptor 인터페이스 = Controller를 Intercept 하기

// 톰캣의 필터보다 dispatcherServlet과 더 잘 어우러지며 할 수 있는게 더 많음


public class RememberMeInterceptor implements HandlerInterceptor {

	// alt + shift + s -> override
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 컨트롤러 메소드가 실행되기 전 호출됨.
		
		HttpSession session = request.getSession();
		
		String id = (String)session.getAttribute("id");
		
		if(id == null) {
			Cookie[] cookies = request.getCookies();
			
			if(cookies != null) {
				for(Cookie cookie : cookies) {
					if(cookie.getName().equals("userId")) {
						id = cookie.getValue();
						
						session.setAttribute("id", id);
					}
				} // for
			}
		}// if
		
		// 리턴값이 true면 호출이 예정되어있는 해당 컨트롤러 메소드를 호출함.
		// 리턴값이 false면 호출이 예정되어있는 해당 컨트롤러 메소드를 호출 안함.
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// 컨트롤러 메소드 실행 직후 view페이지 렌더링 되기 전 호출됨
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// view렌더링 후 호출됨
	}

}
