package hk.org.ha.eclaim.interceptor;

import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import hk.org.ha.eclaim.core.constant.CommonConstant;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	private static final String LOGIN_PAGE_URL = "/eclaim/login/loginPage";
	private static final String LOGIN_URL = "/eclaim/login/doLogin";
	private static final String LOGOUT_URL = "/eclaim/login/doLogout";
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getRequestURI();
		EClaimLogger.info("url=" + url);
		Enumeration<String> para = request.getParameterNames();
		boolean fromMail = false;
		int i = 0;
		while (para.hasMoreElements()) {
			String paraName = para.nextElement();
			if ("fromMail".equals(paraName)) {
				fromMail = true;
			}
			else {
				if (i==0) {
					url += "?";
				}
				else {
					url += "&";
				}
				
				url += paraName + "=" + request.getParameter(paraName);
				i++;
			}
		}
		
		boolean alreadyLoggedIn = request.getSession().getAttribute(CommonConstant.SESSION_USER_PROFILE) != null;
		
		// Avoid a redirect loop for some urls
		if (!request.getRequestURI().equals(LOGIN_PAGE_URL) && !request.getRequestURI().equals(LOGIN_URL)
				&& !request.getRequestURI().equals(LOGOUT_URL)) {
			if (!alreadyLoggedIn) {
				if (url.contains("\n") == false) {
					response.sendRedirect(LOGIN_PAGE_URL + (fromMail?"?url=" + URLEncoder.encode(url, "UTF-8"):""));
				} else {
					response.sendRedirect(LOGIN_PAGE_URL);
				}
				
				return false;
			}
		}
		return true;

	}

}
