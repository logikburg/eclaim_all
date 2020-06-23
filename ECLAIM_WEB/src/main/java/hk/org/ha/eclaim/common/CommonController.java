package hk.org.ha.eclaim.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import hk.org.ha.eclaim.bs.cs.po.CoaPo;
import hk.org.ha.eclaim.bs.cs.po.DepartmentPo;
import hk.org.ha.eclaim.bs.cs.po.EmailTemplatePo;
import hk.org.ha.eclaim.bs.cs.po.InstitutionPo;
import hk.org.ha.eclaim.bs.cs.po.ProgramTypePo;
import hk.org.ha.eclaim.bs.cs.po.RankPo;
import hk.org.ha.eclaim.bs.cs.po.SubSpecialtyPo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.cs.vo.EmailTemplateVo;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectSvc;
import hk.org.ha.eclaim.bs.request.po.RankVo;
import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.bs.request.svc.IRequestSvc;
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.po.UserRolePo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.helper.StrHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;
import hk.org.ha.eclaim.model.common.EmailTemplateRequest;
import hk.org.ha.eclaim.model.common.EmailTemplateResponse;
import hk.org.ha.eclaim.model.project.EmailVo;
import hk.org.ha.eclaim.model.request.MPRSResultResponse;

@Controller
public class CommonController extends BaseController {
	@Autowired
	ICommonSvc commonSvc;
	
	@Autowired
	ISecuritySvc securitySvc;
	
	@Autowired
	IRequestSvc requestSvc;
	
	@Autowired
	IProjectSvc projSvc;
	
	@RequestMapping(value = "/api/searchEmailTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody EmailTemplateResponse searchEmailTemplateInJson(
			@RequestBody EmailTemplateRequest emailTemplateRequestBody) throws Exception {
		
		String requestId = emailTemplateRequestBody.getRequestId();
		String verId = emailTemplateRequestBody.getVerId();
		String templateId = emailTemplateRequestBody.getTemplateId();
		String param = emailTemplateRequestBody.getParam();
		String param2 = emailTemplateRequestBody.getParam2();
		String param3 = emailTemplateRequestBody.getParam3();
		String param4 = emailTemplateRequestBody.getParam4();
		String param5 = emailTemplateRequestBody.getParam5();
		String param6 = emailTemplateRequestBody.getParam6();
		String param7 = emailTemplateRequestBody.getParam7();
		String param8 = emailTemplateRequestBody.getParam8();
		//String param9 = emailTemplateRequestBody.getParam9();
		String wfGroup = emailTemplateRequestBody.getWfGroup();
		String wfUser = emailTemplateRequestBody.getWfUser();
		String isReturn = emailTemplateRequestBody.getIsReturn();

		EClaimLogger.info(emailTemplateRequestBody.toString());
		EmailTemplateResponse response = new EmailTemplateResponse();
		
		String receiver = "";
		// If Confirm case
		if (templateId.indexOf("CONFIRM") != -1) {
			receiver = "ALL";
		}
		else {
			if (!"".equals(wfUser)) {
				UserPo requester = securitySvc.findUser(wfUser);
				receiver = requester.getUserName();
			}
			else if(!"".equals(wfGroup)){
				List<RolePo> userGroupList = securitySvc.getAllRole();
				for (int i=0; i<userGroupList.size(); i++) {
					if (wfGroup.equals(userGroupList.get(i).getRoleId())) {
						receiver = userGroupList.get(i).getRoleName();
						break;
					}
				}
			}
		}

		System.out.println("Email template ID: " + templateId);
		EmailTemplatePo emailTemplatePo = commonSvc.getTemplateByTemplateId(templateId, new String[] {param, param2, param3, param4, param5, param6, param7, param8, receiver });
		
		EmailTemplateVo emailTemplate = new EmailTemplateVo();
		emailTemplate.setTemplateId(emailTemplatePo.getTemplateId());
		if ("Y".equals(isReturn)) {
			String emailTitle = "Return: " + emailTemplatePo.getTemplateTitle();
			String emailContent = emailTemplatePo.getTemplateContent();
			
			response.setTemplateTitle(emailTitle);
			response.setTemplateContent(emailContent);
		}
		else {
			response.setTemplateTitle(emailTemplatePo.getTemplateTitle());
			response.setTemplateContent(emailTemplatePo.getTemplateContent());
		}
		
		List<String> toEmail = new ArrayList<String>();
		List<String> ccEmail = new ArrayList<String>();
		
		List<EmailVo> toEmailList = new ArrayList<EmailVo>();
		List<EmailVo> ccEmailList = new ArrayList<EmailVo>();
		
		ProjectPo project = projSvc.getProjectVerByProjectId(Integer.parseInt(requestId), Integer.parseInt(verId));
		
		UserPo userTo = null;
		if("PREPARER".equals(emailTemplateRequestBody.getToRole())) {
			 userTo = securitySvc.findUser(project.getProjectPreparerId());
		}else if("PROJECT_OWNER".equals(emailTemplateRequestBody.getToRole())) {
			 userTo = securitySvc.findUser(project.getProjectOwnerId());
		}else if("FIN_HOSP_IC".equals(emailTemplateRequestBody.getToRole())) {
			 userTo = securitySvc.findUser(param6);
		}else {
			userTo = null;
			List<UserPo> userList = securitySvc.getAllUserByGroup(emailTemplateRequestBody.getToRole());
			for (UserPo user : userList) {
				if (user.getEmail() != null) {
					if (!toEmail.contains(user.getEmail())) {
						toEmail.add(user.getEmail());
						toEmailList.add(createEmailVo(userTo));
					}
				}
			}
		}
		
		if(userTo != null && userTo.getEmail() != null) {
			toEmail.add(userTo.getEmail());
			toEmailList.add(createEmailVo(userTo));
		}
		
		List<UserPo> userListCC = securitySvc.getAllUserByGroup(emailTemplateRequestBody.getCcRole());
		for (UserPo user : userListCC) {
			if (user.getEmail() != null) {
				if (!ccEmail.contains(user.getEmail())) {
					ccEmail.add(user.getEmail());
					ccEmailList.add(createEmailVo(user));
				}
			}
		}
		
		
//		response.setToEmailList(String.join(",", toEmail));
//		response.setCcEmailList(String.join(",", ccEmail));
		response.setEmailToList(toEmailList);
		response.setEmailCCList(ccEmailList);

		EClaimLogger.info("Finish the Ajax Loading");

		return response;
	}
	
	@RequestMapping(value="/common/getWFUserList")
	public @ResponseBody MPRSResultResponse getWFUserList(@RequestParam("requestNo")String requestNo,
														  @RequestParam("groupId")String groupId) {
		MPRSResultResponse response = new MPRSResultResponse();
		
		RequestPo requestCaseTemp = requestSvc.getRequestByRequestNo(Integer.parseInt(requestNo));
		String clusterCode = "";
		String instCode = "";
		
		List<String> userId = new ArrayList<String>();
		List<String> userName = new ArrayList<String>();
		List<String> instList = new ArrayList<String>();
		for (int j=0; j<requestCaseTemp.getRequestPositionList().size(); j++) {
			clusterCode = requestCaseTemp.getRequestPositionList().get(j).getClusterCode();
			instCode = requestCaseTemp.getRequestPositionList().get(j).getInstCode();
			
			if (!instList.contains(instCode)) {
				List<UserPo> userList = securitySvc.getAllUserByGroupAndCluster(clusterCode, instCode, groupId);
				for (int i=0; i<userList.size(); i++) {
					if (!userId.contains(userList.get(i).getUserId())) {
						userId.add(userList.get(i).getUserId());
						userName.add(userList.get(i).getUserName());
					}
				}
				
				instList.add(instCode);
			}
		}
		
		response.setUserId(userId);
		response.setUserName(userName);
		
		EClaimLogger.info("Finish the Ajax Loading");
		return response;
	}
	
	
	@RequestMapping(value="/common/getRank")
	public @ResponseBody MPRSResultResponse getRank(@RequestParam("rankCode")String rankCode, 
													@RequestParam("staffGroupCode")String staffGroupCode,
													HttpServletRequest request) {
		
		String userId = this.getSessionUser(request).getUserId();
		EClaimLogger.info("CommonController: rankCode=" + rankCode + "/userId=" + userId);
		MPRSResultResponse responseObj = new MPRSResultResponse();
		
		List<RankPo> rankList = null;
		if ("".equals(rankCode)) {
			if ("".equals(staffGroupCode)) {
				rankList = commonSvc.getAllRank();
			}
			else {
				rankList = commonSvc.getAllRank(staffGroupCode, "");
			}
		}
		else {
			if ("".equals(staffGroupCode)) {
				rankList = commonSvc.getAllRank("", rankCode);
			}
			else {
				rankList = commonSvc.getAllRank(staffGroupCode, rankCode);
			}
		}
		List<RankVo> resultRankList = new ArrayList<RankVo>();
		for (int i=0; i<rankList.size(); i++) {
			RankVo rank = new RankVo();
			rank.setRankCode(rankList.get(i).getRankCode());
			rank.setRankName(rankList.get(i).getRankName());
			
			resultRankList.add(rank);
		}
		
		EClaimLogger.info("Finish the Ajax Loading");
		responseObj.setRankList(resultRankList);
		return responseObj;
	}
	
	@RequestMapping(value="/common/getRankByStaffGroupAndRank")
	public @ResponseBody MPRSResultResponse getRankByStaffGroupAndRank(@RequestParam("staffGroupCode")String staffGroupCode,
																@RequestParam("deptCode")String deptCode,
																@RequestParam("fromRank")String fromRank, 
																HttpServletRequest request) {
		
		String userId = this.getSessionUser(request).getUserId();
		EClaimLogger.info("CommonController: staffGroupCode=" + staffGroupCode + "/userId=" + userId);
		MPRSResultResponse responseObj = new MPRSResultResponse();
		
		List<RankPo> rankList = null;
		if (fromRank != null && !"".equals(fromRank)) {
		}
		else {
			rankList = commonSvc.getAllRankByStaffGroup(staffGroupCode, deptCode);
		}

		List<RankVo> resultRankList = new ArrayList<RankVo>();
		for (int i=0; i<rankList.size(); i++) {
			RankVo rank = new RankVo();
			rank.setRankCode(rankList.get(i).getRankCode());
			rank.setRankName(rankList.get(i).getRankName());
			
			resultRankList.add(rank);
		}
		
		EClaimLogger.info("Finish the Ajax Loading");
		responseObj.setRankList(resultRankList);
		return responseObj;
	}
	
	@RequestMapping(value="/common/getSubSpecialtyByStaffGroup")
	public @ResponseBody MPRSResultResponse getSubSpecialtyByStaffGroup(@RequestParam("staffGroupCode")String staffGroupCode,
																@RequestParam("deptCode")String deptCode,
																HttpServletRequest request) {
		
		String userId = this.getSessionUser(request).getUserId();
		EClaimLogger.info("CommonController: staffGroupCode=" + staffGroupCode + "/userId=" + userId);
		MPRSResultResponse responseObj = new MPRSResultResponse();
		
		List<SubSpecialtyPo> subSpecList = null;
//		subSpecList = commonSvc.getAllSubSpecialtyByStaffGroup(staffGroupCode, deptCode);

		EClaimLogger.info("Finish the Ajax Loading");
		responseObj.setSubSpecailtyList(subSpecList);
		return responseObj;
	}
	
	
	
	
	@RequestMapping(value="/common/isRoleExist")
	public @ResponseBody String isRoleExist(@RequestParam("role")String role, 
										    HttpServletRequest request,
											HttpServletResponse response) {
		EClaimLogger.info("CommonController.isRoleExist: " + role);
		if (StrHelper.isNotEmpty(role)) {
			String userId = this.getSessionUser(request).getUserId();
			UserPo user;
			try {
				user = securitySvc.findUser(userId);
				return "Y";
			} catch (Exception e) {
				e.printStackTrace();
				return "N";
			}
//			List<UserRolePo> userRoleListPo = securitySvc.findUserRoleByUserId(userId);

//			for (int i=0; i<userRoleListPo.size(); i++) {
//				EClaimLogger.debug("Role: " + userRoleListPo.get(i).getRole().getRoleId());
//				if (userRoleListPo.get(i).getRole().getRoleId().equals(role)) {
//					return "Y";
//				}
//			}
		}
		
		return "N";
		
	}
	
	@RequestMapping(value="/common/getProgramType")
	public @ResponseBody MPRSResultResponse getProgramType(@RequestParam("annualPlanInd")String annualPlanInd, 
															@RequestParam("postDuration")String postDuration,
															@RequestParam("postFTEType")String postFteType,
															HttpServletRequest request) {
		List<ProgramTypePo> programTypeList = commonSvc.getProgramTypeList(postDuration, postFteType, annualPlanInd);
		MPRSResultResponse responseObj = new MPRSResultResponse();
		responseObj.setProgramTypeList(programTypeList);
		return responseObj;
	}
	
	@RequestMapping(value="/common/getEmailList", method = RequestMethod.GET)
	public @ResponseBody List<EmailVo>  getUserNameList(@RequestParam String name) throws JsonProcessingException {
		List<UserPo> users = null; //securitySvc.searchUserByName(name);
		List<EmailVo> emailList = new ArrayList<EmailVo>();
		for(UserPo user : users) {
			EmailVo email = new EmailVo();
			email.setId(user.getUserId());
			email.setEmail(user.getEmail());
			email.setName(user.getUserName());
		    emailList.add(email);
		}
		
		return emailList;
	}
	
	@RequestMapping(value="/common/getCoaType", method=RequestMethod.GET)
    public @ResponseBody List<String> getTypeCOA() throws Exception {
		System.out.println("CommonController Perform COA Type");
		List<CoaPo> coaList = commonSvc.getAllTypeCOA();
		List<String> json = coaList.stream().map(coa -> coa.getCoaVal()).collect(Collectors.toList());
		return json;
	}
	
	@RequestMapping(value="/common/getCoaSection", method=RequestMethod.GET)
    public @ResponseBody List<String> getSectionCOA() throws Exception {
		System.out.println("CommonController Perform COA Section");
		List<CoaPo> coaList = commonSvc.getAllSectionCOA();
		List<String> json = coaList.stream().map(coa -> coa.getCoaVal()).collect(Collectors.toList());
		return json;
	}
	
	@RequestMapping(value="/common/getCoaFund", method=RequestMethod.GET)
    public @ResponseBody List<String> getFundCOA() throws Exception {
		System.out.println("CommonController Perform COA Fund");
		List<CoaPo> coaList = commonSvc.getAllFundCOA();
		List<String> json = coaList.stream().map(coa -> coa.getCoaVal()).collect(Collectors.toList());
		return json;
	}
	
	@RequestMapping(value="/common/getCoaInst", method=RequestMethod.GET)
    public @ResponseBody List<String> getInstCOA() throws Exception {
		System.out.println("CommonController Perform COA Inst");
		List<CoaPo> coaList = commonSvc.getAllInstCOA();
		List<String> json = coaList.stream().map(coa -> coa.getCoaVal()).collect(Collectors.toList());
		return json;
	}
	
	@RequestMapping(value="/common/getCoaAnaly", method=RequestMethod.GET)
    public @ResponseBody List<String> getAnalyCOA() throws Exception {
		System.out.println("CommonController Perform COA Analy");
		List<CoaPo> coaList = commonSvc.getAllAnalyCOA();
		List<String> json = coaList.stream().map(coa -> coa.getCoaVal()).collect(Collectors.toList());
		return json;
	}
	
	private EmailVo createEmailVo(UserPo user) {
		EmailVo email = new EmailVo();
		email.setEmail(user.getEmail());
		email.setId(user.getUserId());
		email.setName(user.getUserName());
		return email;
	}
}
