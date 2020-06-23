package hk.org.ha.eclaim.controller.login;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hk.org.ha.cid_login_ws.cidloginfunctions.CheckLogin;
import hk.org.ha.cid_login_ws.cidloginfunctions.CheckLoginResponse;
import hk.org.ha.cid_login_ws.cidloginfunctions.LoginInfo;
import hk.org.ha.eclaim.core.constant.CommonConstant;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.model.login.LoginPageWebVo;
import hk.org.ha.eclaim.model.security.UserProfileVo;
import hk.org.ha.eclaim.bs.integration.cidlogin.svc.IWsCidLoginSvc;
import hk.org.ha.eclaim.bs.security.constant.LogonStatusConstant;
import hk.org.ha.eclaim.bs.security.constant.UserAccountStatusConstant;
import hk.org.ha.eclaim.bs.security.po.UserLogonPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.constant.VersionConstant;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.helper.StrHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;
	
@Controller
public class LoginController extends BaseController {
	
	@Value("${ss.login.domain}")
	private String loginDomain;
	
	@Value("${ss.app.environment}")
	private String environment;
	
	private String swVersion = VersionConstant.APP_VERSION_NO;
	
	@Value("${bsd.login.url}")
	private String bsdLoginUrl;
	
	@Autowired
	ISecuritySvc securitySvc;
	@Autowired
	IWsCidLoginSvc wsCidLoginSvc;
	
	@RequestMapping(value="/login/doLogin", method=RequestMethod.POST)
	public String executeLogin(HttpServletRequest request, HttpServletResponse response,  
			@ModelAttribute("loginBean")LoginPageWebVo loginWebVo, RedirectAttributes redirectAttributes) {
		String userId = loginWebVo.getUserId();
		String pwd = loginWebVo.getPassword();
		String domain = loginWebVo.getDomain();
		String message = "";
		String hostName = null;
		String browser = null;
		boolean cidLoginflag = false;
		boolean logonPassed = false;
		
		loginWebVo.setPassword("");
		EClaimLogger.info("#### executeLogin: userId=" + userId + ", domain=" + domain);
		try {
			String url = "?url=";
			if (!"".equals(loginWebVo.getUrl()) && loginWebVo.getUrl() != null) {
				url += loginWebVo.getUrl();
			}
			
			if (request != null) {
				hostName = request.getHeader("x-forwarded-for");
				if (StrHelper.isEmpty(hostName)){
					//InetAddress inetAddress = InetAddress.getByName(request.getRemoteAddr());
					hostName = request.getRemoteAddr(); 
					//hostName = inetAddress.getHostName();
				}
			}
			
			//browser = readBrowserInfo(request);
			
			if (userId != null && !userId.equals("") && pwd != null && !pwd.equals("")) {
				
//				CheckLoginResponse idManResponse = checkCidMan(userId, pwd, domain);
//				pwd = "";
//				
//				if (idManResponse == null) {
//					message = "Login failed. Cannot invoke IDMan logon service. Please call Enquiry / Support Hotline for assistance";
//				} else {
//					int result = idManResponse.getCheckLoginResult();
//					if (result == 0) {
//						cidLoginflag = true;
//					} else if (result == 1) {
//						message = "Login failed. User ID is invalid.";
//					} else if (result == 2) {
//						message = "Login failed. User ID is revoked.";
//					} else if (result == 3) {
//						message = "Login failed. The supplied password for the CMS / HBFS / HRPS / SRS is incorrect.";
//					} else if (result == 4) {
//						message = "Login failed. User ID / Password is / are incorrect.";
//					} else if (result == 5) {
//						message = "Login failed. You are not authorized to use MPRS.";
//					} else if (result == 6) {
//						message = "Login failed. MPRS is NOT available at present.";
//					} else if (result == 7) {
//						message = "Login failed. Your User ID is registered twice in FMMS ID.";
//					} else if (result == 8) {
//						message = "Login failed. Your User ID is not registered in FMMS ID Management Module.";
//					} else if (result == 9) {
//						message = "External Access to MPRS is not allowed.";
//					} else if (result == 10) {
//						message = "The user account has expired. You are forced to change the password.";
//					} else if (result == 11) {
//						message = "The user account has expired. Please call Enquiry / Support Hotline for assistance.";
//					} else {
//						message = "Logon failed. Unknow error got from login service. (CID Code:"+result+")";
//					}
//				}
				
				cidLoginflag = true;
				
				if (cidLoginflag) {
					UserPo user = securitySvc.findUser(userId);
					
					if (user != null) {
						
						if (UserAccountStatusConstant.ACTIVE.equals(user.getAccountStatus())) {
							UserProfileVo userProfileVo = new UserProfileVo();
							userProfileVo.setUserId(userId);
							userProfileVo.setUserName(user.getUserName());
							userProfileVo.setLastLogonDate(DateTimeHelper.formatDateTimeToString(new Date()));
							userProfileVo.setLastLogonResult("Success");
							redirectAttributes.addFlashAttribute("userProfile", userProfileVo);
							// Reset user role
							request.getSession().setAttribute("currentRole", "");
							setSessionUser(request, userProfileVo);
							logonPassed = true;
							
						} else {
							message = "Logon failed. No Active role is being assigned.";
						}
						
					} else {
						message = "Login failed. User ID / Password is / are incorrect.";
					}
					
				}
				
			} else {
				message = "Please input user ID and password.";
			}
			
			addLogonTrail(userId, domain, logonPassed, message, hostName, browser);
			redirectAttributes.addFlashAttribute("error", message);
			
			if (logonPassed) {
				return "redirect:/home/home" + url;
			}
			
			return "redirect:/login/loginPage" + url;
			
		} catch (Exception e) {
			EClaimLogger.error("executeLogin Exception", e);
			message += "System error. Please contact MPRS IT Support.";
			addLogonTrail(userId, domain, false, message, hostName, browser);
			redirectAttributes.addFlashAttribute("error", message);
			return "redirect:/login/loginPage";
		}
		
	}
		
	@RequestMapping(value="/login/loginPage", method=RequestMethod.GET)
	public ModelAndView loginPage(LoginPageWebVo loginWebVo, RedirectAttributes redirectAttributes) {
		LoginPageWebVo loginFormWebVo = new LoginPageWebVo();
		loginFormWebVo.setDomain(loginDomain);
		loginFormWebVo.setEnvironment(environment);
		loginFormWebVo.setSwVersion(swVersion);
		loginFormWebVo.setBsdLoginUrl(bsdLoginUrl);
		String url = loginWebVo.getUrl();
		loginFormWebVo.setUrl(url);
		return new ModelAndView("common/login", "loginBean", loginFormWebVo);
	}
	
	@RequestMapping("/login/logout")
	public String logoutPage (HttpSession session){
		session.removeAttribute(CommonConstant.SESSION_USER_PROFILE);
		return "redirect:/login/loginPage";
	}
	
	@RequestMapping("/error")
	public ModelAndView errorPage(HttpServletRequest request, HttpServletResponse response) {
		String message = "ERROR";
		return new ModelAndView("common/errorPage", "message", message);
	}
	
	private CheckLoginResponse checkCidMan(String userId, String password, String domain) throws Exception{
		CheckLogin checkLogin = new CheckLogin();
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setProject("MPRS");
		loginInfo.setLoginType("DOMAIN");
		loginInfo.setUserId(userId);
		loginInfo.setDomain(domain);
		loginInfo.setPassword(password);
		loginInfo.setIpAddress("");
		loginInfo.setComputerName("");
		loginInfo.setMacAddress("");
		loginInfo.setIeVersion("");
		loginInfo.setOsVersion("");
		loginInfo.setServerName("");
		loginInfo.setExtCPUType("");
		loginInfo.setExtMemSize("");
		checkLogin.setLoginInfo(loginInfo);
		
		return wsCidLoginSvc.checkLogin(checkLogin);
		
	}
	
	private void addLogonTrail(String userId, String domain, boolean loginPassed, String failReason, String hostName, String browser) {
		UserLogonPo po = new UserLogonPo();
		po.setUserId(userId);
		po.setDomain(domain);
		po.setLoginStatus(loginPassed?LogonStatusConstant.SUCCESS:LogonStatusConstant.FAILURE);
		po.setFailReason(failReason);
		po.setHostName(hostName);
		po.setBrowser(browser);
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession();
		po.setSessionToken(session.getId());
		securitySvc.insertUserLogonInfo(po);
	}
	
	/*private String readBrowserInfo(HttpServletRequest request) {
		String browser = null;
		try{
		
			String browserDetails = request.getHeader("User-Agent");
			String userAgent = browserDetails;
			String user = userAgent.toLowerCase();
			String os;
	
			if (userAgent.toLowerCase().indexOf("windows") >= 0 ){
				os = "Windows";
			}else if(userAgent.toLowerCase().indexOf("mac") >= 0){
				os = "Mac";
		    }else if(userAgent.toLowerCase().indexOf("x11") >= 0){
		    	os = "Unix";
		    }else if(userAgent.toLowerCase().indexOf("android") >= 0){
		    	os = "Android";
		    }else if(userAgent.toLowerCase().indexOf("iphone") >= 0){
		    	os = "IPhone";
		    }else{
		    	 os = "UnKnown, More-Info: "+userAgent;
		    }
		     //===============Browser===========================
			if (user.contains("msie")) {
				String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
				browser = substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
			} else if (user.contains("safari") && user.contains("version")) {
				browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
			} else if ( user.contains("opr") || user.contains("opera")) {
				if(user.contains("opera")) {
					browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
				}else if(user.contains("opr")) {
					browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
				}    
			} else if (user.contains("chrome")) {
				browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
			} else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) ){
				browser = "Netscape-?";
			} else if (user.contains("firefox")) {
				browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
			} else {
				browser = "UnKnown, More-Info: "+userAgent;
			}
			return browser;
			
		}catch(Exception e){
			return null;
		}
	        
	}*/
}
