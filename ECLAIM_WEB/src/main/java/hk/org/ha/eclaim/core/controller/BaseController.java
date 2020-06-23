package hk.org.ha.eclaim.core.controller;

import javax.servlet.http.HttpServletRequest;

import hk.org.ha.eclaim.core.constant.CommonConstant;
import hk.org.ha.eclaim.model.security.UserProfileVo;

public class BaseController {
	
	protected static final String ERROR_MSG_KEY = "errorMsg";
	
	protected void setSessionUser(HttpServletRequest request, UserProfileVo user) {  
        request.getSession().setAttribute(CommonConstant.SESSION_USER_PROFILE, user);  
    }
	
	protected UserProfileVo getSessionUser(HttpServletRequest request) { 
		return (UserProfileVo) request.getSession().getAttribute(CommonConstant.SESSION_USER_PROFILE);  
    }
	
	protected void setSessionCurrentRoleId(HttpServletRequest request, String roleId) {  
        request.getSession().setAttribute(CommonConstant.SESSION_USER_CURRENT_ROLE, roleId);  
    }
	
	protected String getSessionCurrentRoleId(HttpServletRequest request) { 
		return (String) request.getSession().getAttribute(CommonConstant.SESSION_USER_CURRENT_ROLE);  
    }
	
}
