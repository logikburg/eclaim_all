package hk.org.ha.eclaim.controller.request;

import java.sql.SQLDataException;
import java.sql.SQLSyntaxErrorException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import hk.org.ha.eclaim.bs.request.svc.IRequestSvc;
import hk.org.ha.eclaim.core.controller.BaseController;
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
import hk.org.ha.eclaim.bs.security.po.RolePo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;

public class CommonRequestController extends BaseController {
	protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Autowired
	ICommonSvc commonSvc;
	
	@Autowired
	ISecuritySvc securitySvc;
	
	@Autowired
	IRequestSvc requestSvc;

	@ModelAttribute("clusterList")
	public Map<String, String> getClusterList(HttpServletRequest request,
			  								  HttpServletResponse response) {
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
	public Map<String, String> getInstList(HttpServletRequest request,
			                               HttpServletResponse response) {
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

	@ModelAttribute("userList")
	public Map<String, String> getUserList(HttpServletRequest request,
			  							   HttpServletResponse response) {
		String userId = this.getSessionUser(request).getUserId();
		String currentRole = (String)request.getSession().getAttribute("currentRole");
		
		List<UserPo> userList = securitySvc.getAllHROfficer(userId, currentRole);

		Map<String, String> displayUserList = new LinkedHashMap<String, String>();
		for (int i=0; i<userList.size(); i++) {
			displayUserList.put(userList.get(i).getUserId(), userList.get(i).getUserName());
		}
		return displayUserList;
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
	
	@ModelAttribute("groupList")
	public Map<String, String> getGroupList() {
		List<RolePo> groupList = securitySvc.getAllRole();

		Map<String, String> displayGroupList = new LinkedHashMap<String, String>();
		for (int i=0; i<groupList.size(); i++) {
			displayGroupList.put(groupList.get(i).getRoleId(), groupList.get(i).getRoleName());
		}
		return displayGroupList;
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
	
	@ModelAttribute("programTypeList")
	public Map<String, String> getProgramTypeList() {
		Map<String, String> displayProgramTypeList = new LinkedHashMap<String, String>();
		List<ProgramTypePo> programTypeList = commonSvc.getAllProgramType();
		for (int i=0; i<programTypeList.size(); i++) {
			displayProgramTypeList.put(programTypeList.get(i).getProgramTypeCode(), programTypeList.get(i).getProgramTypeName());
		}
		
		return displayProgramTypeList;
	}
	
	@ModelAttribute("postStatusList")
	public Map<String, String> getPostStatusList() {
		List<MPRSPostStatusPo> postStatusList = commonSvc.getAllPostStatus();

		Map<String, String> displayPostStatusList = new LinkedHashMap<String, String>();
		for (int i=0; i<postStatusList.size(); i++) {
			if ("CLOSED".equals(postStatusList.get(i).getPostStatusCode())) {
				continue;
			}
			
			displayPostStatusList.put(postStatusList.get(i).getPostStatusCode(), postStatusList.get(i).getPostStatusDesc());
		}
		return displayPostStatusList;
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
	
	protected String doHandleException(Throwable t, String errMsg) {
		if (t == null) {
			return errMsg;
		}
		if (t.getClass().isAssignableFrom(SQLSyntaxErrorException.class)
				|| t.getClass().isAssignableFrom(SQLDataException.class)) {
			String msg = t.getMessage();
			if (msg.contains("ORA")) {
				msg = msg.substring(0, msg.indexOf("ORA", 5));
				return msg.replaceAll("\r", "").replaceAll("\n", "");
			}
		}
		else {
			if (t.getCause() != null) {
				return this.doHandleException(t.getCause(), errMsg);
			}
		}
		
		return "";
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
