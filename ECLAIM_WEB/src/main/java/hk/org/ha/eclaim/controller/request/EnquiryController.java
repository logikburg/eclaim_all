package hk.org.ha.eclaim.controller.request;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.bs.request.po.EnquiryWebPo;
import hk.org.ha.eclaim.bs.request.po.PostMasterPo;
import hk.org.ha.eclaim.bs.request.svc.IRequestSvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.model.request.EnquiryWebVo;
import hk.org.ha.eclaim.bs.cs.po.ClusterPo;
import hk.org.ha.eclaim.bs.cs.po.DepartmentPo;
import hk.org.ha.eclaim.bs.cs.po.ExternalSupportPo;
import hk.org.ha.eclaim.bs.cs.po.FundingSourcePo;
import hk.org.ha.eclaim.bs.cs.po.FundingSourceSubCatPo;
import hk.org.ha.eclaim.bs.cs.po.InstitutionPo;
import hk.org.ha.eclaim.bs.cs.po.MPRSPostStatusPo;
import hk.org.ha.eclaim.bs.cs.po.PostDurationPo;
import hk.org.ha.eclaim.bs.cs.po.ProgramTypePo;
import hk.org.ha.eclaim.bs.cs.po.RankPo;
import hk.org.ha.eclaim.bs.cs.po.RequestStatusPo;
import hk.org.ha.eclaim.bs.cs.po.StaffGroupPo;
import hk.org.ha.eclaim.bs.cs.po.SubSpecialtyPo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.hcm.svc.IHCMSvc;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;

@Controller
public class EnquiryController extends BaseController {
	@Autowired
	ICommonSvc commonSvc; 

	@Autowired
	ISecuritySvc securitySvc;
	
	@Autowired
	IRequestSvc requestSvc;
	
	@Autowired
	IHCMSvc hcmSvc;
	
	private int maxRecordShown = 150;

	@RequestMapping(value="/request/enquiry", method=RequestMethod.GET)
	public ModelAndView init(HttpServletRequest request) throws Exception {
		EnquiryWebVo vo = new EnquiryWebVo();
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));
		vo.setUserName(user.getUserName());
		vo.setEffectiveDate(DateTimeHelper.formatDateToString(new Date()));

		return new ModelAndView("request/enquiry", "formBean", vo);
	}

	@RequestMapping(value="/request/enquiry", method=RequestMethod.POST)
	public ModelAndView performSearch(@ModelAttribute("formBean")EnquiryWebVo vo, 
									  HttpServletRequest request) throws Exception {
		String userId = this.getSessionUser(request).getUserId();
        String currentRoleId = (String)request.getSession().getAttribute("currentRole");
		
		// Convert EnquiryWebVo to EnquiryWebPo
		EnquiryWebPo po = new EnquiryWebPo();
		po.setClusterCode(vo.getClusterCode());
		po.setInstCode(vo.getInstCode());
		po.setDeptCode(vo.getDeptCode());
		po.setStaffGroupCode(vo.getStaffGroupCode());
		po.setRankCode(vo.getRankCode());
		po.setPostId(vo.getPostId().trim());
		po.setEffectiveDate(vo.getEffectiveDate());
		po.setEmployeeId(vo.getEmployeeId().trim());
		// Added for UT30064 
		po.setApprovalRef(vo.getApprovalReference().trim());
		
		System.out.println("vo.getClusterId:" + vo.getClusterCode());
		System.out.println("vo.getEffectiveDate:" + vo.getEffectiveDate());
		
		int userRoleId = (int) request.getSession().getAttribute("currentUserRoleId");
		List<DataAccessPo> dataAccessList = securitySvc.getDataAccessByRoleId(userRoleId);
		System.out.println("userRoleId: " + userRoleId);
		List<PostMasterPo> list = requestSvc.getMPRSPost(dataAccessList, po, userId, currentRoleId);
		
		List<PostMasterPo> resultList = new ArrayList<PostMasterPo>();
		for (int i=0; i<list.size(); i++) {
			if (i >= maxRecordShown) {
				break;
			}
			
			resultList.add(list.get(i));
		}
		
		vo.setSearchResultList(resultList);
		vo.setHaveResult("Y");
		vo.setTotalRecordCount(String.valueOf(list.size()));
		if (list.size() > maxRecordShown) {
			vo.setShowRecordTrimmedMsg("Y");
		}
		else {
			vo.setShowRecordTrimmedMsg("N");
		}
		
		// Added for CC177246
        if ("HR_OFFICER".equals(currentRoleId) || "HR_MANAGER".equals(currentRoleId)) {
        	vo.setIsHR("Y");
        }
		
		return new ModelAndView("request/enquiry", "formBean", vo);
	}

	/**** Drop down ****/
	@ModelAttribute("clusterList")
	public Map<String, String> getClusterList(HttpServletRequest request) {
		String userId = this.getSessionUser(request).getUserId();
		String currentRole = (String)request.getSession().getAttribute("currentRole");
		List<ClusterPo> clusterList = commonSvc.getClusterByUser(userId, currentRole);

		Map<String, String> displayClusterList = new LinkedHashMap<String, String>();
		for (int i=0; i<clusterList.size(); i++) {
			displayClusterList.put(clusterList.get(i).getClusterCode(), clusterList.get(i).getClusterCode());
		}
		return displayClusterList;
	}

	@ModelAttribute("instList")
	public Map<String, String> getInstList(HttpServletRequest request) {
		String userId = this.getSessionUser(request).getUserId();
		String currentRole = (String)request.getSession().getAttribute("currentRole");
		List<InstitutionPo> InstList = commonSvc.getInstByUser(userId, currentRole);

		Map<String, String> displayInstList = new LinkedHashMap<String, String>();
		for (int i=0; i<InstList.size(); i++) {
			displayInstList.put(InstList.get(i).getInstCode(), InstList.get(i).getInstCode());
		}
		return displayInstList;
	}
	
	@ModelAttribute("deptList")
	public Map<String, String> getDeptList() {
		List<DepartmentPo> DeptList = commonSvc.getAllDept();

		Map<String, String> displayDeptList = new LinkedHashMap<String, String>();
		for (int i=0; i<DeptList.size(); i++) {
			displayDeptList.put(DeptList.get(i).getDeptCode(), DeptList.get(i).getDeptName());
		}
		return displayDeptList;

	}

	@ModelAttribute("staffGroupList")
	public Map<String, String> getStaffGroupList() {
		List<StaffGroupPo> StaffGroupList = commonSvc.getAllStaffGroup();

		Map<String, String> displayStaffGroupList = new LinkedHashMap<String, String>();
		for (int i=0; i<StaffGroupList.size(); i++) {
			displayStaffGroupList.put(StaffGroupList.get(i).getStaffGroupCode(), StaffGroupList.get(i).getStaffGroupName());
		}
		return displayStaffGroupList;

	}

	@ModelAttribute("rankList")
	public Map<String, String> getRankList() {
		List<RankPo> RankList = commonSvc.getAllRank();

		Map<String, String> displayRankList = new LinkedHashMap<String, String>();
		for (int i=0; i<RankList.size(); i++) {
			displayRankList.put(RankList.get(i).getRankCode(), RankList.get(i).getRankName());
		}
		return displayRankList;

	}
	
	@ModelAttribute("FundingSourceList")
	public Map<String, String> getFundingSourceList() {
		List<FundingSourcePo> FundingSourceList = commonSvc.getAllFundingSource();

		Map<String, String> displayFundingSourceList = new LinkedHashMap<String, String>();
		for (int i=0; i<FundingSourceList.size(); i++) {
			displayFundingSourceList.put(FundingSourceList.get(i).getSourceId(), FundingSourceList.get(i).getSourceDesc());
		}
		return displayFundingSourceList;
	}
	
	@ModelAttribute("FundingSourceSubCatList")
	public Map<String, String> getFundingSourceSubCatList() {
		List<FundingSourceSubCatPo> fundingSourceSubCatList = commonSvc.getAllFundingSourceSubCat();

		Map<String, String> displayFundingSourceSubCatList = new LinkedHashMap<String, String>();
		for (int i=0; i<fundingSourceSubCatList.size(); i++) {
			displayFundingSourceSubCatList.put(fundingSourceSubCatList.get(i).getSourceSubCatId(), fundingSourceSubCatList.get(i).getSourceSubCatDesc());
		}
		return displayFundingSourceSubCatList;
	}

	@ModelAttribute("PostDurationList")
	public Map<String, String> getPostDurationList() {
		List<PostDurationPo> PostDurationList = commonSvc.getAllPostDuration();

		Map<String, String> displayPostDurationList = new LinkedHashMap<String, String>();
		for (int i=0; i<PostDurationList.size(); i++) {
			displayPostDurationList.put(PostDurationList.get(i).getPostDurationCode(), PostDurationList.get(i).getPostDurationDesc());
		}
		return displayPostDurationList;
	}

	@ModelAttribute("ExternalSupportList")
	public Map<String, String> getExternalSupportList() {
		List<ExternalSupportPo> ExternalSupportList = commonSvc.getAllExternalSupport();

		Map<String, String> displayExternalSupportList = new LinkedHashMap<String, String>();
		for (int i=0; i<ExternalSupportList.size(); i++) {
			displayExternalSupportList.put(ExternalSupportList.get(i).getSupportId(), ExternalSupportList.get(i).getSupportDesc());
		}
		return displayExternalSupportList;
	}
	
	@ModelAttribute("requestStatusList")
	public Map<String, String> getRequestStatusList() {
		List<RequestStatusPo> requestStatusList = commonSvc.getAllRequestStatus();

		Map<String, String> displayInstList = new LinkedHashMap<String, String>();
		for (int i=0; i<requestStatusList.size(); i++) {
			displayInstList.put(requestStatusList.get(i).getStatusCode(), requestStatusList.get(i).getStatusDesc());
		}
		return displayInstList;
	}
	
	@ModelAttribute("postStatusList")
	public Map<String, String> getpostStatusList() {
		List<MPRSPostStatusPo> postStatusList = commonSvc.getAllPostStatus();

		Map<String, String> displayPostStatusList = new LinkedHashMap<String, String>();
		for (int i=0; i<postStatusList.size(); i++) {
			displayPostStatusList.put(postStatusList.get(i).getPostStatusCode(), postStatusList.get(i).getPostStatusDesc());
		}
		return displayPostStatusList;
	}

	@ModelAttribute("programTypeList")
	public Map<String, String> getProgramTypeList() {
		Map<String, String> displayProgramTypeList = new LinkedHashMap<String, String>();
		List<ProgramTypePo> programTypeList = commonSvc.getAllProgramType();
		for (int i=0; i<programTypeList.size(); i++) {
			displayProgramTypeList.put(programTypeList.get(i).getProgramTypeCode(), programTypeList.get(i).getProgramTypeName());
		}
		
		return displayProgramTypeList;
	}
	
	@ModelAttribute("subSpecialtyList")
	public Map<String, String> getSubSpecialtyList() {
		List<SubSpecialtyPo> subSpecialtyList = commonSvc.getAllSubSpecialty();

		Map<String, String> displayInstList = new LinkedHashMap<String, String>();
		for (int i=0; i<subSpecialtyList.size(); i++) {
			displayInstList.put(subSpecialtyList.get(i).getSubSpecialtyCode(), subSpecialtyList.get(i).getSubSpecialtyDesc());
		}
		return displayInstList;
	}
	
	@ModelAttribute("financialYearList")
	public Map<String, String> getFinancialYearList() {
		int startYear = 2012;
		
		// Get the current year
		Calendar cal = Calendar.getInstance();
		int currentYear = cal.get(Calendar.YEAR);
		int maxYear = (currentYear+2);
		
		Map<String, String> fyList = new LinkedHashMap<String, String>();
		while (maxYear >= startYear) {
			String display = maxYear + "/" + String.valueOf(maxYear + 1).substring(2); 
			fyList.put(display, display);
			maxYear --;
		}
		return fyList;
	}
}