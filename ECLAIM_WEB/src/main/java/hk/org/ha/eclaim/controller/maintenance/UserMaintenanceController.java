package hk.org.ha.eclaim.controller.maintenance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.bs.request.svc.IRequestSvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.model.maintenance.RoleVo;
import hk.org.ha.eclaim.model.maintenance.UserMaintenanceWebVo;
import hk.org.ha.eclaim.model.maintenance.UserRoleListWebVo;
import hk.org.ha.eclaim.model.maintenance.UserVo;
import hk.org.ha.eclaim.model.request.MPRSResultResponse;
import hk.org.ha.eclaim.bs.cs.po.ClusterPo;
import hk.org.ha.eclaim.bs.cs.po.DepartmentPo;
import hk.org.ha.eclaim.bs.cs.po.InstitutionPo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.hcm.po.HCMResponsibilityPo;
import hk.org.ha.eclaim.bs.hcm.svc.IHCMSvc;
import hk.org.ha.eclaim.bs.security.constant.UserAccountStatusConstant;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.helper.StrHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Controller
public class UserMaintenanceController extends BaseController {
	@Autowired
	ICommonSvc commonSvc; 

	@Autowired
	ISecuritySvc securitySvc;
	
	@Autowired
	IHCMSvc hcmSvc;
	
	@Autowired
	IRequestSvc requestSvc;
	
	@Value("${ss.cidlogin.admin}")
	private String cidLogin;
	
	@Value("${ss.login.domain}")
	private String domain;

	@RequestMapping(value="/maintenance/userMaintenance", method=RequestMethod.GET)
	public ModelAndView init(HttpServletRequest request) throws Exception {
		UserMaintenanceWebVo vo = new UserMaintenanceWebVo();
		System.out.println("UserMaintenanceController - init: cidLogin=" + cidLogin);
		String idmanDomainUser = "";
		String idmanReturnCode = "";
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
		String currentRole = (String)request.getSession().getAttribute("currentRole");
        user.setCurrentRole(currentRole);
        vo.setCidLoginUrl(cidLogin);
        vo.setCurrentRole(currentRole);
       
        if (StrHelper.isNotEmpty(request.getParameter("return_code"))) {
        	idmanReturnCode = (String)request.getParameter("return_code");
        	String targetApp = (String)request.getParameter("target_app");
        	if (!"9".equals(idmanReturnCode) && !"".equals(targetApp)) {
	        	if (StrHelper.isNotEmpty(request.getParameter("domain_user"))) {
	        		idmanDomainUser = (String)request.getParameter("domain_user");
	        	}
	        	EClaimLogger.info("return_code=" + idmanReturnCode==null?"return_code is null":idmanReturnCode);
	        	EClaimLogger.info("domain_user=" + idmanDomainUser==null?"domain_user is null":idmanDomainUser);
	        	idmanDomainUser = idmanDomainUser.split("\\\\")[1];
	        	EClaimLogger.info("After split idmanDomainUser=" + idmanDomainUser);
	        	vo.setDomainUserId(idmanDomainUser);
	        	
	        	// Look up all
	        	List<UserPo> userList1 = securitySvc.searchUser(idmanDomainUser, "", true);
	        	List<UserPo> userList2 = securitySvc.searchUser(idmanDomainUser, "", userId, currentRole, true);
	        	List<UserPo> userListExisting = securitySvc.searchUser(idmanDomainUser, "");
	        	
	        	boolean isExistingUser = false;
	        	boolean isExistingUserInOtherCluster = false;
	        	
	        	if (userList1.size() != userList2.size()) {
	        		isExistingUserInOtherCluster = true;
	        	}
	        	
	        	if (userListExisting.size() > 0) {
	        		UserPo newUser = securitySvc.findUser(idmanDomainUser);
	        		isExistingUser = true;
	        		
	        		vo.setEditUserName(newUser.getUserName());
		        	vo.setEditPhone(newUser.getPhoneNo());
		        	vo.setEditEmail(newUser.getEmail());
	        	}
	        	else {
		        	// Look up from CIDS
		        	UserPo tmpUser = securitySvc.getCidsUserInformation((String)request.getParameter("domain_user"));
		        	if (tmpUser != null) {
			        	vo.setEditUserName(tmpUser.getUserName());
			        	vo.setEditPhone(tmpUser.getPhoneNo());
			        	vo.setEditEmail(tmpUser.getEmail());
		        	}
		        	
		        	isExistingUser = false;
	        	}
	        	
	        	vo.setIsExistingUser(isExistingUser?"Y":"N");
	        	vo.setIsExistingUserInOtherCluster(isExistingUserInOtherCluster?"Y":"N");
	        	vo.setReturnCode((String)request.getParameter("return_code"));
        	}
        	else {
        		vo.setUpdateSuccess("N");
        		vo.setDisplayMessage("User is not a ISA.  Only ISA can update user information.  Please contact administrator.");
        	}
        }
        
		return new ModelAndView("maintenance/userMaintenance", "formBean", vo);
	}

	@RequestMapping(value="/maintenance/userMaintenance", method=RequestMethod.POST)
	public ModelAndView performSearch(@ModelAttribute("formBean")UserMaintenanceWebVo vo, HttpServletRequest request) throws Exception {
		System.out.println("UserMaintenanceController - performSearch: " + "formAction=" + vo.getFormAction());
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
		String currentRole = (String)request.getSession().getAttribute("currentRole"); 
        user.setCurrentRole(currentRole);
		
		// Check form action
		if ("SAVE_ROLE".equals(vo.getFormAction())) {
			// Get the user id
			EClaimLogger.info("user Id: " + userId);
			
			// Update user role
			securitySvc.updateUserRole(vo.getUpdateUserId(), 
									   vo.getDdRoleId(), 
									   vo.getDdCluster(), 
									   vo.getDdInstitute(), 
									   vo.getDdDept(), 
									   vo.getDdStaffGroup(), 
									   user);
			
			boolean inactiveUser = false;
			if (vo.getDdRoleId() != null) {
				if (vo.getDdRoleId().size() == 0) {
					inactiveUser = true;
				}
			}
			else {
				inactiveUser = true;
			}
			
			UserPo updatedUser = securitySvc.findUser(vo.getUpdateUserId());
			if (inactiveUser) {
				if (MPRSConstant.MPRS_STATE_ACTIVE.equals(updatedUser.getAccountStatus())) {
					updatedUser.setAccountStatus(MPRSConstant.MPRS_STATE_INACTIVE);
					securitySvc.updateUser(updatedUser);
				}
			}
			else {
				if (MPRSConstant.MPRS_STATE_INACTIVE.equals(updatedUser.getAccountStatus())) {
					updatedUser.setAccountStatus(MPRSConstant.MPRS_STATE_ACTIVE);
					securitySvc.updateUser(updatedUser);
				}
			}
			
			vo.setDisplayMessage("Record update success.");
			vo.setUpdateSuccess("Y");
		}
		else if ("SAVE_USER".equals(vo.getFormAction())) {
			UserPo updatedUser = securitySvc.findUser(vo.getEditUserId());
			
			if (updatedUser != null) {
				updatedUser.setUserName(vo.getEditUserName());
				updatedUser.setPhoneNo(vo.getEditPhone());
				updatedUser.setEmail(vo.getEditEmail());
				updatedUser.setUpdatedBy(user.getUserId());
				updatedUser.setUpdatedRoleId(user.getCurrentRole());
				updatedUser.setUpdatedDate(new Date());
				
				securitySvc.updateUser(updatedUser);
			}
			else {
				updatedUser = new UserPo();
				updatedUser.setUserId(vo.getEditUserId());
				updatedUser.setUserName(vo.getEditUserName());
				updatedUser.setPhoneNo(vo.getEditPhone());
				updatedUser.setEmail(vo.getEditEmail());
				updatedUser.setCreatedBy(user.getUserId());
				updatedUser.setCreatedRoleId(user.getCurrentRole());
				updatedUser.setCreatedDate(new Date());
				updatedUser.setUpdatedBy(user.getUserId());
				updatedUser.setUpdatedRoleId(user.getCurrentRole());
				updatedUser.setUpdatedDate(new Date());
				updatedUser.setAccountStatus(UserAccountStatusConstant.ACTIVE);
				updatedUser.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
				
				securitySvc.insertUser(updatedUser);
			}
			vo.setDisplayMessage("Record update success.");
			vo.setUpdateSuccess("Y");
		}
		else if ("SAVE_ALL".equals(vo.getFormAction())) {
			UserPo updatedUser = new UserPo();
			updatedUser.setUserId(vo.getEditUserId());
			updatedUser.setUserName(vo.getEditUserName());
			updatedUser.setPhoneNo(vo.getEditPhone());
			updatedUser.setEmail(vo.getEditEmail());
			updatedUser.setCreatedBy(user.getUserId());
			updatedUser.setCreatedRoleId(user.getCurrentRole());
			updatedUser.setCreatedDate(new Date());
			updatedUser.setUpdatedBy(user.getUserId());
			updatedUser.setUpdatedRoleId(user.getCurrentRole());
			updatedUser.setUpdatedDate(new Date());
			updatedUser.setAccountStatus(UserAccountStatusConstant.ACTIVE);
			updatedUser.setRecState(MPRSConstant.MPRS_STATE_ACTIVE); 
			
			securitySvc.insertUser(updatedUser);
			
			securitySvc.updateUserRole(vo.getEditUserId(), 
					   vo.getDdRoleId(), 
					   vo.getDdCluster(), 
					   vo.getDdInstitute(), 
					   vo.getDdDept(), 
					   vo.getDdStaffGroup(), 
					   user);
			
			vo.setCidLoginUrl("");
	        vo.setDomainUserId("");
	        vo.setReturnCode("");
			vo.setDisplayMessage("Record update success.");
			vo.setUpdateSuccess("Y");
		}
		else {
			vo.setDisplayMessage("");
			vo.setUpdateSuccess("");
		}
		
		// Perform Search
		List<UserPo> userList = securitySvc.searchUser(vo.getSearchUserId(), vo.getSearchUserName(), userId, currentRole);
		List<UserVo> list = new ArrayList<UserVo>();
		for (int i=0; i<userList.size(); i++) {
			UserVo tmp = new UserVo();
			tmp.setUserId(userList.get(i).getUserId());
			tmp.setUserName(userList.get(i).getUserName());
			tmp.setEmail(userList.get(i).getEmail());
			tmp.setPhone(userList.get(i).getPhoneNo());
			tmp.setRecState(userList.get(i).getRecState());
			tmp.setStateDesc(MPRSConstant.MPRS_STATE_ACTIVE.equals(userList.get(i).getAccountStatus())?"ACTIVE":"INACTIVE");
			
			list.add(tmp);
		}
		
		System.out.println("list.size(): " + list.size() + ", cidLogin=" + cidLogin);
		
		vo.setUserId(vo.getSearchUserId().replaceAll("\\\\", "\\\\\\\\"));
		vo.setSearchResultList(list);
		vo.setHaveResult("Y");
        vo.setCidLoginUrl(cidLogin);
        vo.setDomainUserId("");
        vo.setReturnCode("");
        vo.setCurrentRole(currentRole);
        
		return new ModelAndView("maintenance/userMaintenance", "formBean", vo);
	}

	/* Ajax */
	@RequestMapping(value="/maintenance/getUserRoleDetail")
	public @ResponseBody UserRoleListWebVo getUserRoleDetail(@RequestParam("userId")String userId,
															 HttpServletRequest request) throws Exception {
		EClaimLogger.info("userId: " + userId);
		UserPo user = this.securitySvc.findUser(userId);

		UserRoleListWebVo result = new UserRoleListWebVo();
		List<String> roleIdList = new ArrayList<String>();
		List<String> roleNameList = new ArrayList<String>();
		List<String> clusterCodeList = new ArrayList<String>();
		List<String> instCodeList = new ArrayList<String>();
		List<String> deptCodeList = new ArrayList<String>();
		List<String> staffGroupCodeList = new ArrayList<String>();
		
		if (user.getUserRole()!=null) {
			for (int i=0; i<user.getUserRole().size(); i++) {
				// Get all data
				List<DataAccessPo> dataAccessList = securitySvc.getDataAccessByRoleId(user.getUserRole().get(i).getUserRoleId());
				
				if (dataAccessList.size() == 0) {
					roleIdList.add(user.getUserRole().get(i).getRole().getRoleId());
					roleNameList.add(user.getUserRole().get(i).getRole().getRoleName());
					clusterCodeList.add("");
					instCodeList.add("");
					deptCodeList.add("");
					staffGroupCodeList.add("");
				}
				else {
					for (int x=0; x<dataAccessList.size(); x++) {
						roleIdList.add(user.getUserRole().get(i).getRole().getRoleId());
						roleNameList.add(user.getUserRole().get(i).getRole().getRoleName());
						clusterCodeList.add(dataAccessList.get(x).getClusterCode());
						instCodeList.add(dataAccessList.get(x).getInstCode());
						deptCodeList.add(dataAccessList.get(x).getDeptCode());
						staffGroupCodeList.add(dataAccessList.get(x).getStaffGroupCode());
					}
				}
			}
		}
		
		result.setRoleIdList(roleIdList);
		result.setRoleNameList(roleNameList);
		result.setClusterCodeList(clusterCodeList);
		result.setInstCodeList(instCodeList);
		result.setDeptCodeList(deptCodeList);
		result.setStaffGroupCodeList(staffGroupCodeList);
		
		// Whether user have hcm responsibility
		String currentCluster = (String)request.getSession().getAttribute("currentCluster");
		result.setResponsibilityCount(0);
		
		// Whether user have hcm responsibility
		List<HCMResponsibilityPo> list = hcmSvc.getHCMResponsibilityByUserId(userId);
		if (list != null) {
			for (int i=0; i<list.size(); i++) {
				if (getHCMResponsibilityMapping(list.get(i).getResponsibilityName()).equals(currentCluster)) {
					result.setResponsibilityCount(list.size());
					break;
				}
			}
		}

		return result;
	} 
	
	@RequestMapping(value="/maintenance/getUserProfileDetail")
	public @ResponseBody UserVo getUserProfileDetail(@RequestParam("userId")String userId) throws Exception {
		EClaimLogger.info("userId: " + userId);
		UserPo user = this.securitySvc.findUser(userId);

		UserVo result = new UserVo();
		result.setUserId(user.getUserId());
		result.setUserName(user.getUserName());
		result.setPhone(user.getPhoneNo());
		result.setEmail(user.getEmail());
		result.setRecState(user.getRecState());
		
		return result;
	} 
	
	@RequestMapping(value="/maintenance/getUserRole", method = RequestMethod.GET)
	public @ResponseBody List<RoleVo> getUserRole(@RequestParam String term, 
												  HttpServletRequest request) {
		String currentRole = (String)request.getSession().getAttribute("currentRole");
		List<RolePo> roleList = securitySvc.getAllRoleByRoleId(term, currentRole);
		System.out.println("RoleList.size: " + roleList.size());

		List<RoleVo> resultList = new ArrayList<RoleVo>();
		for (int i=0; i<roleList.size(); i++) {
			RoleVo role = new RoleVo();
			role.setRoleId(roleList.get(i).getRoleId());
			role.setRoleName(roleList.get(i).getRoleName());
			resultList.add(role);
		}
		
		return resultList;
	}
	
	
	@RequestMapping(value="/maintenance/getCluster")
	public @ResponseBody MPRSResultResponse getCluster(@RequestParam("addRoleCode")String addRoleCode,
														HttpServletRequest request) {
		// Get the user
//		String userId = this.getSessionUser(request).getUserId();
//		String currentRole = (String)request.getSession().getAttribute("currentRole");
		
		MPRSResultResponse responseObj = new MPRSResultResponse();
		
//		List<ClusterPo> clusterList = null;
		List<ClusterPo> tmpClusterList = new ArrayList<ClusterPo>();
//		
//		if ("HO_ADM".equals(currentRole) && "CLUS_USER_ADM".equals(addRoleCode)) {
//			clusterList = commonSvc.getAllClusterForHospitalAdmin();
//		}
//		else if ("HOSP_USER_ADM".equals(addRoleCode) 
//				|| "FIN_MANAGER".equals(addRoleCode)
//				|| "FIN_OFFICER".equals(addRoleCode)
//				|| "HR_MANAGER".equals(addRoleCode)
//				|| "HR_OFFICER".equals(addRoleCode)
//				|| "REVIEWER".equals(addRoleCode)
//				|| "CLUS_USER_ADM".equals(addRoleCode)) {
//			clusterList = commonSvc.getClusterByUser(userId, currentRole);
//		}
//		
//		if (clusterList != null) {
//			for (int x=0; x<clusterList.size(); x++) {
//				ClusterPo cluster = new ClusterPo();
//				cluster.setClusterCode(StrHelper.format(clusterList.get(x).getClusterCode()));
//				cluster.setClusterName(StrHelper.format(clusterList.get(x).getClusterName()));
//				tmpClusterList.add(cluster);
//			}
//		}
		
		responseObj.setClusterList(tmpClusterList);
		
		return responseObj;
	}
	
	@RequestMapping(value="/maintenance/genCidsLogonToken")
	public @ResponseBody MPRSResultResponse genCidsLogonToken(@RequestParam("userId")String userId, HttpServletRequest request) {
		System.out.println("Location userMaintenanceController.genCidsLogonToken");
		String currentAdminUserId = this.getSessionUser(request).getUserId();
		EClaimLogger.info("currentAdminUserId: " + currentAdminUserId);
		EClaimLogger.info("userId: " + userId);
		
		String token = securitySvc.generateCidsLogonToken(currentAdminUserId, domain);
		MPRSResultResponse response = new MPRSResultResponse();
		response.setToken(token);
		
		System.out.println("Finish the Ajax Loading");

		return response;
	}
	
	@RequestMapping(value="/maintenance/checkUserExisting")
	public @ResponseBody String checkUserExisting(@RequestParam("userId")String userId, HttpServletRequest request) {
		System.out.println("Location userMaintenanceController.checkUserExisting");
		List<UserPo> userList = securitySvc.searchUser(userId, "");
		if (userList.size() > 0) {
			for (int i=0; i<userList.size(); i++) {
				if ("A".equals(userList.get(i).getAccountStatus())) {
					return "Y";
				}
			}
			
			return "N";
		}
		else {
			return "N";
		}
	}
	
	@RequestMapping(value="/maintenance/getResponsibilityCount")
	public @ResponseBody int getResponsibilityCount(@RequestParam("userId")String userId, HttpServletRequest request) throws Exception {
		System.out.println("userId: " + userId);
		EClaimLogger.info("userId: " + userId);
        String currentCluster = (String)request.getSession().getAttribute("currentCluster");
		
		// Whether user have hcm responsibility
		List<HCMResponsibilityPo> list = hcmSvc.getHCMResponsibilityByUserId(userId);
		if (list != null) {
			for (int i=0; i<list.size(); i++) {
				if (getHCMResponsibilityMapping(list.get(i).getResponsibilityName()).equals(currentCluster)) {
					return 1;
				}
			}
		}

		return 0;
	} 
	
	private String getHCMResponsibilityMapping(String inHCMResponName) {
		String[] tmp = inHCMResponName.split(" ");
		String hcmCluster = "";
		if (tmp.length > 2) {
			hcmCluster = tmp[1];
		}
		
		if (hcmCluster.equals("HKWC")) {
			return "HWC";
		}
		else if (hcmCluster.equals("NTEC")) {
			return "NEC";
		} 
		else if (hcmCluster.equals("HKEC")) {
			return "HEC";
		} 
		else if (hcmCluster.equals("KCC")) {
			return "KCC";
		} 
		else if (hcmCluster.equals("HOC")) {
			return "HO";
		} 
		else if (hcmCluster.equals("HCH")) {
			return "HCH";
		} 
		else if (hcmCluster.equals("NTWC")) {
			return "NWC";
		} 
		else if (hcmCluster.equals("KWC")) {
			return "KWC";
		} 
		else if (hcmCluster.equals("KEC")) {
			return "KEC";
		} 
		return "";
	}
}
