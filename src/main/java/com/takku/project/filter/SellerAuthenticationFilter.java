package com.takku.project.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = "/seller/*")
public class SellerAuthenticationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

		String cpath = req.getContextPath();
		String uri = req.getRequestURI();

		// 로그인 안 되어있으면 로그인 페이지로
		/*
		 * if (session == null || session.getAttribute("loginUser") == null) {
		 * res.sendRedirect(cpath + "/auth/login?msg=needLogin"); return; }
		 */

		// 상점 선택 안 되어있으면 /seller/home으로 (단, 이미 /seller/home이면 무한 루프 방지)
		/*
		 * if (!uri.endsWith("/seller/home") && session.getAttribute("store") == null) {
		 * res.sendRedirect(cpath + "/seller/home?msg=needStore"); return; }
		 */

		// 조건 통과 시 다음 필터 또는 컨트롤러 실행
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
