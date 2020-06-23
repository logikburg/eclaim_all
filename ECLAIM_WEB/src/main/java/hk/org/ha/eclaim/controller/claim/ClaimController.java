package hk.org.ha.eclaim.controller.claim;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.openxml4j.opc.ContentTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.util.HtmlUtils;

import hk.org.ha.eclaim.bs.cs.po.HomeWebVo;
import hk.org.ha.eclaim.bs.cs.po.StaffGroupPo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.maintenance.svc.IDocumentSvc;
import hk.org.ha.eclaim.bs.maintenance.svc.INewsSvc;
import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.bs.request.svc.IRequestSvc;
import hk.org.ha.eclaim.bs.security.po.MPRSFunctionPo;
import hk.org.ha.eclaim.bs.security.po.UserLogonPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.constant.VersionConstant;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.helper.StrHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;
import hk.org.ha.eclaim.model.maintenance.RoleVo;

@Controller
public class ClaimController extends BaseController {

	@Value("${ss.app.environment}")
	private String environment;

	private String swVersion = VersionConstant.APP_VERSION_NO;

	@Value("${bsd.login.url}")
	private String bsdLoginUrl;

	@Autowired
	IRequestSvc requestSvc;

	@Autowired
	ICommonSvc commonSvc;

	@Autowired
	ISecuritySvc securitySvc;

	@Autowired
	INewsSvc newsSvc;

	@Autowired
	IDocumentSvc documentSvc;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/claim/prepare", method = RequestMethod.GET)
	public ModelAndView prepare(@RequestParam(required = false) String claimId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String userId = this.getSessionUser(request).getUserId();
		HomeWebVo webVo = new HomeWebVo();

		UserPo user = securitySvc.findUser(userId);
		webVo.setUserName(user.getUserName());
		webVo.setUser(user);

		System.out.println("userId : " + user.getUserId());

		String currentRoleId = (String) request.getSession().getAttribute("currentRole");
		String cluster = (String) request.getSession().getAttribute("currentCluster");
		String currentRoleName = (String) request.getSession().getAttribute("currentRoleName");
		int userRoleUid = (int) request.getSession().getAttribute("currentUserRoleId");

		System.out.println("userRoleUid: " + userRoleUid);

		List<RoleVo> userRoleListVo = (ArrayList<RoleVo>) request.getSession().getAttribute("userRoleList");
		System.out.println("UserRoleListVo is " + userRoleListVo);
		System.out.println(
				"UserRoleListVo.size: " + (ObjectUtils.notEqual(userRoleListVo, null) ? userRoleListVo.size() : "0"));

		List<String> funcIdList = (ArrayList<String>) request.getSession().getAttribute("funcList");
		List<String> parentFuncIdList = (ArrayList<String>) request.getSession().getAttribute("parentFuncList");

		System.out.println("parentFuncIdList.size: " + parentFuncIdList.size());
		System.out.println("funcList.size: " + funcIdList.size());

		// Retrieve my request
		UserLogonPo lastLogonPo = securitySvc.getLastLogonInfo(userId);
		webVo.setUserLogonPo(lastLogonPo);

		// user.setFunctionList(funcIdList);
		user.setCurrentRole((String) request.getSession().getAttribute("currentRole"));

		webVo.setCurrentCluster(cluster);
		request.getSession().setAttribute("currentUser", user);
		request.getSession().setAttribute("currentUserId", user.getUserId());
		request.getSession().setAttribute("currentUserRoleId", userRoleUid);
		request.getSession().setAttribute("currentUserName", user.getUserName());
		request.getSession().setAttribute("currentCluster", cluster);
		request.getSession().setAttribute("environment", environment);
		request.getSession().setAttribute("swVersion", swVersion);
		request.getSession().setAttribute("bsdLoginUrl", bsdLoginUrl);

		request.getSession().setAttribute("currentRole", currentRoleId);
		request.getSession().setAttribute("userRoleList", userRoleListVo);
		request.getSession().setAttribute("currentRoleName", currentRoleName);
		request.getSession().setAttribute("parentFuncList", parentFuncIdList);
		request.getSession().setAttribute("funcList", funcIdList);

		if (!"".equals(request.getParameter("url")) && request.getParameter("url") != null) {
			response.sendRedirect(request.getParameter("url"));
		}

		if (claimId != null) {
			return new ModelAndView("claim/quotaClaim", "formBean", webVo);
		}

		return new ModelAndView("claim/followUpClaim", "formBean", webVo);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/claim/approve", method = RequestMethod.GET)
	public ModelAndView approval(@RequestParam(required = false) String claimId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String userId = this.getSessionUser(request).getUserId();
		HomeWebVo webVo = new HomeWebVo();

		UserPo user = securitySvc.findUser(userId);
		webVo.setUserName(user.getUserName());
		webVo.setUser(user);

		System.out.println("userId : " + user.getUserId());

		String currentRoleId = (String) request.getSession().getAttribute("currentRole");
		String cluster = (String) request.getSession().getAttribute("currentCluster");
		String currentRoleName = (String) request.getSession().getAttribute("currentRoleName");
		int userRoleUid = (int) request.getSession().getAttribute("currentUserRoleId");

		System.out.println("userRoleUid: " + userRoleUid);

		List<RoleVo> userRoleListVo = (ArrayList<RoleVo>) request.getSession().getAttribute("userRoleList");
		System.out.println("UserRoleListVo is " + userRoleListVo);
		System.out.println(
				"UserRoleListVo.size: " + (ObjectUtils.notEqual(userRoleListVo, null) ? userRoleListVo.size() : "0"));

		List<String> funcIdList = (ArrayList<String>) request.getSession().getAttribute("funcList");
		List<String> parentFuncIdList = (ArrayList<String>) request.getSession().getAttribute("parentFuncList");

		System.out.println("parentFuncIdList.size: " + parentFuncIdList.size());
		System.out.println("funcList.size: " + funcIdList.size());

		// Retrieve my request
		UserLogonPo lastLogonPo = securitySvc.getLastLogonInfo(userId);
		webVo.setUserLogonPo(lastLogonPo);

		// user.setFunctionList(funcIdList);
		user.setCurrentRole((String) request.getSession().getAttribute("currentRole"));

		webVo.setCurrentCluster(cluster);
		request.getSession().setAttribute("currentUser", user);
		request.getSession().setAttribute("currentUserId", user.getUserId());
		request.getSession().setAttribute("currentUserRoleId", userRoleUid);
		request.getSession().setAttribute("currentUserName", user.getUserName());
		request.getSession().setAttribute("currentCluster", cluster);
		request.getSession().setAttribute("environment", environment);
		request.getSession().setAttribute("swVersion", swVersion);
		request.getSession().setAttribute("bsdLoginUrl", bsdLoginUrl);

		request.getSession().setAttribute("currentRole", currentRoleId);
		request.getSession().setAttribute("userRoleList", userRoleListVo);
		request.getSession().setAttribute("currentRoleName", currentRoleName);
		request.getSession().setAttribute("parentFuncList", parentFuncIdList);
		request.getSession().setAttribute("funcList", funcIdList);

		if (!"".equals(request.getParameter("url")) && request.getParameter("url") != null) {
			response.sendRedirect(request.getParameter("url"));
		}

		if (claimId != null) {
			return new ModelAndView("claim/quotaClaim", "formBean", webVo);
		}

		return new ModelAndView("claim/followUpClaim", "formBean", webVo);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/claim/review", method = RequestMethod.GET)
	public ModelAndView review(@RequestParam(required = false) String claimId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String userId = this.getSessionUser(request).getUserId();
		HomeWebVo webVo = new HomeWebVo();

		UserPo user = securitySvc.findUser(userId);
		webVo.setUserName(user.getUserName());
		webVo.setUser(user);

		System.out.println("userId : " + user.getUserId());

		String currentRoleId = (String) request.getSession().getAttribute("currentRole");
		String cluster = (String) request.getSession().getAttribute("currentCluster");
		String currentRoleName = (String) request.getSession().getAttribute("currentRoleName");
		int userRoleUid = (int) request.getSession().getAttribute("currentUserRoleId");

		System.out.println("userRoleUid: " + userRoleUid);

		List<RoleVo> userRoleListVo = (ArrayList<RoleVo>) request.getSession().getAttribute("userRoleList");
		System.out.println("UserRoleListVo is " + userRoleListVo);
		System.out.println(
				"UserRoleListVo.size: " + (ObjectUtils.notEqual(userRoleListVo, null) ? userRoleListVo.size() : "0"));

		List<String> funcIdList = (ArrayList<String>) request.getSession().getAttribute("funcList");
		List<String> parentFuncIdList = (ArrayList<String>) request.getSession().getAttribute("parentFuncList");

		System.out.println("parentFuncIdList.size: " + parentFuncIdList.size());
		System.out.println("funcList.size: " + funcIdList.size());

		// Retrieve my request
		UserLogonPo lastLogonPo = securitySvc.getLastLogonInfo(userId);
		webVo.setUserLogonPo(lastLogonPo);

		// user.setFunctionList(funcIdList);
		user.setCurrentRole((String) request.getSession().getAttribute("currentRole"));

		webVo.setCurrentCluster(cluster);
		request.getSession().setAttribute("currentUser", user);
		request.getSession().setAttribute("currentUserId", user.getUserId());
		request.getSession().setAttribute("currentUserRoleId", userRoleUid);
		request.getSession().setAttribute("currentUserName", user.getUserName());
		request.getSession().setAttribute("currentCluster", cluster);
		request.getSession().setAttribute("environment", environment);
		request.getSession().setAttribute("swVersion", swVersion);
		request.getSession().setAttribute("bsdLoginUrl", bsdLoginUrl);

		request.getSession().setAttribute("currentRole", currentRoleId);
		request.getSession().setAttribute("userRoleList", userRoleListVo);
		request.getSession().setAttribute("currentRoleName", currentRoleName);
		request.getSession().setAttribute("parentFuncList", parentFuncIdList);
		request.getSession().setAttribute("funcList", funcIdList);

		if (!"".equals(request.getParameter("url")) && request.getParameter("url") != null) {
			response.sendRedirect(request.getParameter("url"));
		}

		if (claimId != null) {
			return new ModelAndView("claim/quotaClaim", "formBean", webVo);
		}
		
		return new ModelAndView("claim/reviewClaim", "formBean", webVo);
		/*if (claimId != null) {
			return new ModelAndView("claim/quotaClaim", "formBean", webVo);
		}

		return new ModelAndView("claim/followUpClaim", "formBean", webVo);*/
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/claim/transfer", method = RequestMethod.GET)
	public ModelAndView transfer(@RequestParam(required = false) String claimId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String userId = this.getSessionUser(request).getUserId();
		HomeWebVo webVo = new HomeWebVo();

		UserPo user = securitySvc.findUser(userId);
		webVo.setUserName(user.getUserName());
		webVo.setUser(user);

		System.out.println("userId : " + user.getUserId());

		String currentRoleId = (String) request.getSession().getAttribute("currentRole");
		String cluster = (String) request.getSession().getAttribute("currentCluster");
		String currentRoleName = (String) request.getSession().getAttribute("currentRoleName");
		int userRoleUid = (int) request.getSession().getAttribute("currentUserRoleId");

		System.out.println("userRoleUid: " + userRoleUid);

		List<RoleVo> userRoleListVo = (ArrayList<RoleVo>) request.getSession().getAttribute("userRoleList");
		System.out.println("UserRoleListVo is " + userRoleListVo);
		System.out.println(
				"UserRoleListVo.size: " + (ObjectUtils.notEqual(userRoleListVo, null) ? userRoleListVo.size() : "0"));

		List<String> funcIdList = (ArrayList<String>) request.getSession().getAttribute("funcList");
		List<String> parentFuncIdList = (ArrayList<String>) request.getSession().getAttribute("parentFuncList");

		System.out.println("parentFuncIdList.size: " + parentFuncIdList.size());
		System.out.println("funcList.size: " + funcIdList.size());

		// Retrieve my request
		UserLogonPo lastLogonPo = securitySvc.getLastLogonInfo(userId);
		webVo.setUserLogonPo(lastLogonPo);

		// user.setFunctionList(funcIdList);
		user.setCurrentRole((String) request.getSession().getAttribute("currentRole"));

		webVo.setCurrentCluster(cluster);
		request.getSession().setAttribute("currentUser", user);
		request.getSession().setAttribute("currentUserId", user.getUserId());
		request.getSession().setAttribute("currentUserRoleId", userRoleUid);
		request.getSession().setAttribute("currentUserName", user.getUserName());
		request.getSession().setAttribute("currentCluster", cluster);
		request.getSession().setAttribute("environment", environment);
		request.getSession().setAttribute("swVersion", swVersion);
		request.getSession().setAttribute("bsdLoginUrl", bsdLoginUrl);

		request.getSession().setAttribute("currentRole", currentRoleId);
		request.getSession().setAttribute("userRoleList", userRoleListVo);
		request.getSession().setAttribute("currentRoleName", currentRoleName);
		request.getSession().setAttribute("parentFuncList", parentFuncIdList);
		request.getSession().setAttribute("funcList", funcIdList);

		if (!"".equals(request.getParameter("url")) && request.getParameter("url") != null) {
			response.sendRedirect(request.getParameter("url"));
		}

		if (claimId != null) {
			return new ModelAndView("claim/transferClaim", "formBean", webVo);
		}

		return new ModelAndView("claim/followUpClaim", "formBean", webVo);
	}

	// Added for CR0367
	public List<RequestPo> matchStaffGroup(List<RequestPo> requestList) {
		List<StaffGroupPo> staffGroupList = commonSvc.getAllStaffGroup();

		for (int i = 0; i < requestList.size(); i++) {
			String tmpStaffGroup = "";

			for (int j = 0; j < requestList.get(i).getRequestPositionList().size(); j++) {
				if (!tmpStaffGroup.equals(requestList.get(i).getRequestPositionList().get(j).getStaffGroupCode())
						&& requestList.get(i).getRequestPositionList().get(j).getStaffGroupCode() != null) {
					if ("".equals(tmpStaffGroup)) {
						tmpStaffGroup = requestList.get(i).getRequestPositionList().get(j).getStaffGroupCode();
					} else {
						tmpStaffGroup = "";
						break;
					}
				}
			}
			requestList.get(i).setStaffGroup(getStaffGroupAbbr(tmpStaffGroup, staffGroupList));
		}

		return requestList;
	}

	// Added for CR0367
	public String getStaffGroupAbbr(String staffGroupCode, List<StaffGroupPo> staffGroupList) {
		if (staffGroupCode == null) {
			return "";
		}

		// Look up the staff group abbr
		for (int x = 0; x < staffGroupList.size(); x++) {
			if (staffGroupCode.equals(staffGroupList.get(x).getStaffGroupCode())) {
				return staffGroupList.get(x).getStaffGroupAbbr();
			}
		}

		return "";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/claim/searchClaim", method = RequestMethod.GET)
	public ModelAndView searchClaim(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = this.getSessionUser(request).getUserId();
		HomeWebVo webVo = new HomeWebVo();

		UserPo user = securitySvc.findUser(userId);
		webVo.setUserName(user.getUserName());
		webVo.setUser(user);

		String currentRoleId = (String) request.getSession().getAttribute("currentRole");
		String cluster = (String) request.getSession().getAttribute("currentCluster");
		String currentRoleName = (String) request.getSession().getAttribute("currentRoleName");
		int userRoleUid = (int) request.getSession().getAttribute("currentUserRoleId");

		System.out.println("userRoleUid: " + userRoleUid);

		List<RoleVo> userRoleListVo = (ArrayList<RoleVo>) request.getSession().getAttribute("userRoleList");
		System.out.println("UserRoleListVo is " + userRoleListVo);
		System.out.println(
				"UserRoleListVo.size: " + (ObjectUtils.notEqual(userRoleListVo, null) ? userRoleListVo.size() : "0"));

		List<String> funcIdList = (ArrayList<String>) request.getSession().getAttribute("funcList");
		List<String> parentFuncIdList = (ArrayList<String>) request.getSession().getAttribute("parentFuncList");

		System.out.println("parentFuncIdList.size: " + parentFuncIdList.size());
		System.out.println("funcList.size: " + funcIdList.size());

		// Retrieve my request
		UserLogonPo lastLogonPo = securitySvc.getLastLogonInfo(userId);
		webVo.setUserLogonPo(lastLogonPo);

		// user.setFunctionList(funcIdList);
		user.setCurrentRole((String) request.getSession().getAttribute("currentRole"));

		webVo.setCurrentCluster(cluster);
		request.getSession().setAttribute("currentUser", user);
		request.getSession().setAttribute("currentUserId", user.getUserId());
		request.getSession().setAttribute("currentUserRoleId", userRoleUid);
		request.getSession().setAttribute("currentUserName", user.getUserName());
		request.getSession().setAttribute("currentCluster", cluster);
		request.getSession().setAttribute("environment", environment);
		request.getSession().setAttribute("swVersion", swVersion);
		request.getSession().setAttribute("bsdLoginUrl", bsdLoginUrl);

		request.getSession().setAttribute("currentRole", currentRoleId);
		request.getSession().setAttribute("userRoleList", userRoleListVo);
		request.getSession().setAttribute("currentRoleName", currentRoleName);
		request.getSession().setAttribute("parentFuncList", parentFuncIdList);
		request.getSession().setAttribute("funcList", funcIdList);

		if (!"".equals(request.getParameter("url")) && request.getParameter("url") != null) {
			response.sendRedirect(request.getParameter("url"));
		}

		return new ModelAndView("claim/searchClaim", "formBean", webVo);
	}

	@RequestMapping(value = "/claim/switchRole", method = RequestMethod.POST)
	public String switchRole(@ModelAttribute("targetRole") String targetRole, HttpServletRequest request)
			throws Exception {
		System.out.println("HomeController Perform Update");
		boolean isError = true;
		String userId = this.getSessionUser(request).getUserId();
		String switchToRoleName = "";
		targetRole = HtmlUtils.htmlEscape(targetRole);
		EClaimLogger.info("user Id: " + userId + ", targetRole : " + targetRole);
		UserPo user = null;
		List<String> funcIdList = new ArrayList<String>();
		if (StrHelper.isNotEmpty(targetRole)) {
			// Get the user info
			user = securitySvc.findUser(userId);

			if (user.getUserRole() != null) {
				for (int i = 0; i < user.getUserRole().size(); i++) {
					if (targetRole.equals(user.getUserRole().get(i).getRole().getRoleId())) {
						switchToRoleName = user.getUserRole().get(i).getRole().getRoleName();
						// Get function ID for current role
						List<MPRSFunctionPo> funcList = new ArrayList<MPRSFunctionPo>();
						funcList.addAll(securitySvc.getFunctionListByRole(targetRole));
						funcIdList = new ArrayList<String>();
						for (int j = 0; j < funcList.size(); j++) {
							funcIdList.add(funcList.get(j).getFuncId());
						}
						isError = false;
						break;
					}
				}
			}
		} else {
			throw new Exception("Target Role is invalid");
		}

		if (!isError) {
			user.setCurrentRole(targetRole);
			setSessionCurrentRoleId(request, targetRole);
			request.getSession().setAttribute("currentRoleName", switchToRoleName);
			request.getSession().setAttribute("currentUser", user);
			request.getSession().setAttribute("funcList", funcIdList);
		}

		return "redirect:/home/home";
	}

	@RequestMapping(value = "/try-to-upload-file", method = RequestMethod.POST, consumes = "multipart/form-data")
	public @ResponseBody String tryUpload(@RequestParam("fn") MultipartFile multipartFile, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String fileName = multipartFile.getOriginalFilename();
		EClaimLogger.info("Upload - Uploaded: filename=" + fileName + ", " + multipartFile.getSize() + " bytes");
		File f = new File("");
		EClaimLogger.info("Transfered the uploaded file to path " + f.getAbsolutePath());
		multipartFile.transferTo(f);
		return "Uploaded Successfully";
	}

	@RequestMapping(value = "/claim/download-xlsx", method = RequestMethod.GET)
	public void downloadXlsx(HttpServletResponse response) throws IOException {

		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"claim.xlsx\"");
		response.setContentType(ContentTypes.getContentTypeFromFileExtension("claim.xlsx"));

		File f = new File("claim.xlsx");
		InputStream inputStream = new FileInputStream(f); // load the file
		EClaimLogger.info("Transfered the uploaded file to path" + f.getAbsolutePath());

		IOUtils.copy(inputStream, response.getOutputStream());
		response.flushBuffer();
	}
}
