package hk.org.ha.eclaim.controller.hcm;

import java.sql.SQLSyntaxErrorException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.model.hcm.CreateHCMApproverWebVo;
import hk.org.ha.eclaim.model.hcm.HCMRecordGradeVo;
import hk.org.ha.eclaim.model.hcm.UpdateHCMPostWebVo;
import hk.org.ha.eclaim.bs.cs.po.ClusterPo;
import hk.org.ha.eclaim.bs.cs.po.DepartmentPo;
import hk.org.ha.eclaim.bs.cs.po.InstitutionPo;
import hk.org.ha.eclaim.bs.cs.po.RankPo;
import hk.org.ha.eclaim.bs.cs.po.StaffGroupPo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.hcm.po.CreateHCMPostWebPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMGradePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMJobPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMOrganizationPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMPostOrganizationPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMPostTitlePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMProbationDurationUnitPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMRecordGradePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMRecordPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMSourceFundingPo;
import hk.org.ha.eclaim.bs.hcm.svc.IHCMSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Controller
public class CreateHCMApproverController extends BaseController {
	@Autowired
	IHCMSvc hcmSvc;

	@Autowired
	ICommonSvc commonSvc;

	@Autowired
	ISecuritySvc securitySvc;

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@RequestMapping(value="/hcm/createHCMApprover", method=RequestMethod.GET)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Location UpdateHCMPostController.init()");

		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		UserPo user = securitySvc.findUser(userId);
        user.setCurrentRole((String)request.getSession().getAttribute("currentRole"));

		List<HCMJobPo> hcmJobs = hcmSvc.getAllHCMJob();
		System.out.println("hcmJobs.size(): " + hcmJobs.size());

		List<HCMPostTitlePo> hcmPostTitle = hcmSvc.getAllHCMPostTitle();
		System.out.println("hcmPostTitle.size(): " + hcmPostTitle.size());

		List<HCMPostOrganizationPo> hcmPostOrganization = hcmSvc.getAllHCMPostOrganization();
		System.out.println("hcmPostOrganization.size(): " + hcmPostOrganization.size());

		List<HCMOrganizationPo> hcmOrganization = hcmSvc.getAllHCMOrganization();
		System.out.println("hcmOrganization.size(): " + hcmOrganization.size());

		CreateHCMApproverWebVo vo = new CreateHCMApproverWebVo();
		vo.setHcmEffectiveDate(DateTimeHelper.formatDateToString(new Date()));
		vo.setPositionId(request.getParameter("positionId"));
		vo.setUserName(user.getUserName());
		vo.setUserId(userId);
		
		if (request.getParameter("pId") != null) {
			// Retrieve the content again
			HCMRecordPo hcmRecord = hcmSvc.getHCMRecordByPositionId(Integer.parseInt((String)request.getParameter("pId")));
			vo.setPositionId((String)request.getParameter("pId"));
			vo.setEffectiveFromDate(hcmRecord.getDateEffectiveStr());
			vo.setStartDate(hcmRecord.getEffectiveStartDateStr());
			vo.setProbationDuration(String.valueOf(hcmRecord.getPorbationPeriod()));
			vo.setProbationDurationUnit(hcmRecord.getProbationPeriodUnitCd());
			vo.setPostOrganization(hcmRecord.getPositionOrganization());
			vo.setOrganization(String.valueOf(hcmRecord.getOrganizationId()));
			vo.setJob(String.valueOf(hcmRecord.getJobId()));
			vo.setPostName(hcmRecord.getName());
			vo.setFte(String.valueOf(hcmRecord.getFte()));
			vo.setHeadCount(String.valueOf(hcmRecord.getMaxPerson()));
			vo.setPostTitle(hcmRecord.getPositionTitle());
			vo.setPostOrganization(hcmRecord.getPostOrganizationId());
			vo.setUnitTeam(hcmRecord.getUnitTeam());
			vo.setVersionNo(String.valueOf(hcmRecord.getVersionNumber()));
			vo.setHiringStatus(hcmRecord.getAvailabilityStatus());
			vo.setType(hcmRecord.getPositionType());
			vo.setProposedEndDate(hcmRecord.getHiringStatusPropEndDateStr());
			vo.setDdPostOrganization(hcmRecord.getPositionOrganization());
			vo.setDdJob(String.valueOf(hcmRecord.getJobName()));
			vo.setDdOrganization(hcmRecord.getOrganizationName());
			vo.setDdPostTitle(hcmRecord.getPositionTitle());
			vo.setDdLocation(hcmRecord.getLocationDesc());
			vo.setPositionGroup(hcmRecord.getPositionGroup());
			vo.setSrcFunding(hcmRecord.getSrcFunding());
			vo.setLocation(hcmRecord.getLocationId());
			
			List<HCMRecordGradePo> poList = hcmSvc.getHCMGradeRecordByPositionId(Integer.parseInt(vo.getPositionId()));
			List<HCMRecordGradeVo> tmpVoList = new ArrayList<HCMRecordGradeVo>();
			
			List<HCMGradePo> gradeList = hcmSvc.getAllHCMGrade();
			vo.setGradeListAll(hcmSvc.getAllHCMGrade());
			for (int i=0; i<poList.size(); i++) {
				HCMRecordGradeVo tmp = new HCMRecordGradeVo();
				
				tmp.setGradeId(String.valueOf(poList.get(i).getGradeId()));
				tmp.setPositionId(String.valueOf(poList.get(i).getPositionId()));
				tmp.setDateFrom(poList.get(i).getDateFrom()!=null ? DateTimeHelper.formatDateToString(poList.get(i).getDateFrom()) : "");
				tmp.setDateTo(poList.get(i).getDateTo()!=null ? DateTimeHelper.formatDateToString(poList.get(i).getDateTo()) : "");
				for (int x=0; x<gradeList.size(); x++) {
					if (gradeList.get(x).getGradeId().equals(String.valueOf(poList.get(i).getGradeId()))) {
						tmp.setGradeDesc(gradeList.get(x).getGradeName());
						break;
					}
				}
				
				tmpVoList.add(tmp);
			}
			System.out.println("xxxxx: " + tmpVoList.size());
			
			vo.setGradeList(tmpVoList);
		}	

		return new ModelAndView("hcm/createHCMApprover", "formBean", vo);
	}

	@RequestMapping(value="/hcm/createHCMApprover", method=RequestMethod.POST)
	public ModelAndView performAction(@ModelAttribute("formBean")CreateHCMApproverWebVo createPositionWebVo, 
			HttpServletRequest request) throws Exception {
		// Get the user name from cookie
		String userId = this.getSessionUser(request).getUserId();
		String roleId = (String)request.getSession().getAttribute("currentRole");
		int postId = -1;
		
		try {
			System.out.println("repsonsibilityId: " + createPositionWebVo.getRepsonsibilityId());
			postId = hcmSvc.createHCMPositionApprover(convertVoToPo(createPositionWebVo), userId, roleId);
			System.out.println("post Id: " + postId);
			
			// Create new UpdatePositionWebVo
			UpdateHCMPostWebVo vo = new UpdateHCMPostWebVo();
			vo.setUserId(userId);
			vo.setUpdateSuccess("Y");
			vo.setDisplayMessage("HCM Position create success.");
			
			// Get the hcm record 
			HCMRecordPo hcmRecord = hcmSvc.getHCMRecordByPositionId(postId, createPositionWebVo.getEffectiveFromDate());
			vo.setPositionId(String.valueOf(hcmRecord.getPositionId()));
			vo.setEffectiveFromDate(hcmRecord.getEffectiveStartDateStr());
			vo.setHiddenEffectiveFromDate(hcmRecord.getEffectiveStartDateStr());
			// updatePositionWebVo.setStartDate(hcmRecord.getEffectiveStartDateStr());
			vo.setProbationDuration(String.valueOf(hcmRecord.getPorbationPeriod()));
			vo.setProbationDurationUnit(hcmRecord.getProbationPeriodUnitCd());
			vo.setPostOrganization(hcmRecord.getPositionOrganization());
			vo.setOrganization(String.valueOf(hcmRecord.getOrganizationId()));
			vo.setJob(String.valueOf(hcmRecord.getJobId()));
			vo.setPostName(hcmRecord.getName());
			vo.setHiddenPostName(hcmRecord.getName());
			vo.setFte(String.valueOf(hcmRecord.getFte()));
			vo.setHeadCount(String.valueOf(hcmRecord.getMaxPerson()));
			vo.setPostTitle(hcmRecord.getPositionTitle());
			vo.setPostOrganization(hcmRecord.getPostOrganizationId());
			vo.setUnitTeam(hcmRecord.getUnitTeam());
			vo.setVersionNo(String.valueOf(hcmRecord.getVersionNumber()));
			vo.setHiringStatus(hcmRecord.getAvailabilityStatus());
			vo.setType(hcmRecord.getPositionType());
			vo.setProposedEndDate(hcmRecord.getHiringStatusPropEndDateStr());
			vo.setDdPostOrganization(hcmRecord.getPositionOrganization());
			vo.setDdJob(String.valueOf(hcmRecord.getJobName()));
			vo.setDdOrganization(hcmRecord.getOrganizationName());
			vo.setDdPostTitle(hcmRecord.getPositionTitle());
			vo.setDdLocation(hcmRecord.getLocationId());
			vo.setPositionGroup(hcmRecord.getPositionGroup());
			vo.setSrcFunding(hcmRecord.getSrcFunding());
			vo.setDdLocation(hcmRecord.getLocationDesc());
			vo.setStartDate(hcmRecord.getPositionStartDateStr());
			vo.setEffectiveFromDisplay(hcmRecord.getEffectiveStartDateStr());
			vo.setEffectiveToDisplay(hcmRecord.getEffectiveEndDateStr());
			vo.setHiringStatusStartDate(hcmRecord.getHiringStatusStartDateStr());
			
			List<HCMRecordGradePo> poList = hcmSvc.getHCMGradeRecordByPositionId(Integer.parseInt(vo.getPositionId()));
			List<HCMRecordGradeVo> tmpVoList = new ArrayList<HCMRecordGradeVo>();
			vo.setGradeListAll(hcmSvc.getAllHCMGrade());
			
			List<HCMGradePo> gradeList = hcmSvc.getAllHCMGrade();
			
			for (int i=0; i<poList.size(); i++) {
				HCMRecordGradeVo tmp = new HCMRecordGradeVo();
				tmp.setGradeId(String.valueOf(poList.get(i).getGradeId()));
				tmp.setPositionId(String.valueOf(poList.get(i).getPositionId()));
				tmp.setDateFrom(poList.get(i).getDateFrom()!=null ? DateTimeHelper.formatDateToString(poList.get(i).getDateFrom()) : "");
				tmp.setDateTo(poList.get(i).getDateTo()!=null ? DateTimeHelper.formatDateToString(poList.get(i).getDateTo()) : "");
				tmp.setVersionNumber(String.valueOf(poList.get(i).getVersionNumber()));
				tmp.setGradeUid(String.valueOf(poList.get(i).getGradeUid()));
				for (int x=0; x<gradeList.size(); x++) {
					if (gradeList.get(x).getGradeId().equals(String.valueOf(poList.get(i).getGradeId()))) {
						tmp.setGradeDesc(gradeList.get(x).getGradeName());
						break;
					}
				}
				tmpVoList.add(tmp);
			}
			System.out.println("xxxxx: " + tmpVoList.size());
			
			vo.setGradeList(tmpVoList);
			
			// Checking whether the case have future record
			boolean lastRecord = false;
			String openEndDate = "31/12/4712";
			if (openEndDate.equals(hcmRecord.getEffectiveEndDateStr())) {
				lastRecord = true;
			}
			else {
				Date tmpDate = hcmRecord.getEffectiveEndDate();
				Calendar cal = Calendar.getInstance();
				cal.setTime(tmpDate);
				cal.add(Calendar.DATE, 1);
				System.out.println("Look for effective date: " + DateTimeHelper.formatDateToString(cal.getTime()));
				HCMRecordPo tmpRecord = hcmSvc.getHCMRecordByPositionId(hcmRecord.getPositionId(), DateTimeHelper.formatDateToString(cal.getTime()));
				if (tmpRecord == null) {
					lastRecord = true;
				}
			}
			
			vo.setLastRecord(lastRecord?"Y":"N");
			
			return new ModelAndView("hcm/updateHCMPost", "formBean", vo);
		}
		catch (Exception ex) {
			String errorMsg = doHandleException(ex.getCause());
			EClaimLogger.error("performAction:" + ex.getMessage(), ex);
			
			List<HCMRecordGradeVo> tmpVoList = new ArrayList<HCMRecordGradeVo>();
			List<HCMGradePo> gradeList = hcmSvc.getAllHCMGrade();
			
			for (int i=0; i<createPositionWebVo.getGrade().size(); i++) {
				HCMRecordGradeVo tmp = new HCMRecordGradeVo();
				tmp.setGradeId(createPositionWebVo.getGrade().size()>0?createPositionWebVo.getGrade().get(i):"");
				tmp.setDateFrom(createPositionWebVo.getGradeDateFrom().size()>0?createPositionWebVo.getGradeDateFrom().get(i):"");
				tmp.setDateTo(createPositionWebVo.getGradeDateTo().size()>0?createPositionWebVo.getGradeDateTo().get(i):"");
				for (int x=0; x<gradeList.size(); x++) {
					if (gradeList.get(x).getGradeId().equals(createPositionWebVo.getGrade().get(i))) {
						tmp.setGradeDesc(gradeList.get(x).getGradeName());
						break;
					}
				}
				tmpVoList.add(tmp);
			}
			createPositionWebVo.setGradeList(tmpVoList);
			
			createPositionWebVo.setUpdateSuccess("N");
			createPositionWebVo.setDisplayMessage("Error occur:" + errorMsg.replaceAll("\r", " ").replaceAll("\n", " "));
			createPositionWebVo.setUserId(userId);
			return new ModelAndView("hcm/createHCMApprover", "formBean", createPositionWebVo);
		}
	}
	
	private String doHandleException(Throwable t) {
		if (t == null) {
			return "Exception";
		}
		if (t.getClass().isAssignableFrom(SQLSyntaxErrorException.class)) {
			String msg = t.getMessage();
			if (msg.contains("ORA")) {
				msg = msg.substring(0, msg.indexOf("ORA", 5));
				return msg;
			}
		}
		else {
			if (t.getCause() != null) {
				return this.doHandleException(t.getCause());
			}
		}
		
		return "";
	}

	/**** Dropdown ****/
	@ModelAttribute("hiringStatusList")
	public Map<String, String> getHiringStatusList() {
		Map<String, String> displayList = new LinkedHashMap<String, String>();
		displayList.put("Active", "Active");
		displayList.put("Proposed", "Proposed");

		return displayList;
	}

	@ModelAttribute("durationUnitList")
	public Map<String, String> getDuringUnitList() {
		List<HCMProbationDurationUnitPo> list = hcmSvc.getAllHCMProbationDurationUnit();

		Map<String, String> displayList = new LinkedHashMap<String, String>();
		for (int i=0; i<list.size(); i++) {
			displayList.put(list.get(i).getCode(), list.get(i).getDesc());
		}
		return displayList;
	}
	
	/*@ModelAttribute("gradeList")
	public Map<String, String> getGradeList() {
		List<HCMGradePo> list = hcmSvc.getAllHCMGrade();

		Map<String, String> displayList = new HashMap<String, String>();
		for (int i=0; i<list.size(); i++) {
			displayList.put(list.get(i).getGradeId(), list.get(i).getGradeName());
		}
		return displayList;
	}
	
	@ModelAttribute("locationList")
	public Map<String, String> getLocationList() {
		List<HCMLocationPo> list = hcmSvc.getAllHCMLocation();

		Map<String, String> displayList = new HashMap<String, String>();
		for (int i=0; i<list.size(); i++) {
			displayList.put(list.get(i).getLocationId(), list.get(i).getDescription());
		}
		return displayList;
	}*/
	
	private CreateHCMPostWebPo convertVoToPo(CreateHCMApproverWebVo vo) {
		CreateHCMPostWebPo po = new CreateHCMPostWebPo();
		po.setHcmJob(vo.getJob());
		po.setHcmPostTitle(vo.getPostTitle());
		po.setHcmPostOrganization(vo.getPostOrganization());
		po.setHcmOrganization(vo.getOrganization());
		po.setHcmUnitTeam(vo.getUnitTeam());
		po.setPostName(vo.getHiddenPostName());
		
		po.setEffectiveFromDate(vo.getEffectiveFromDate());
		po.setStartDate(vo.getStartDate());
		po.setType(vo.getType());
		
		po.setHiringStatus(vo.getHiringStatus());
		po.setLocation(vo.getLocation());
		po.setFte(vo.getFte());
		po.setHeadCount(vo.getHeadCount());
		po.setProbationDuration(vo.getProbationDuration());
		po.setProbationDurationUnit(vo.getProbationDurationUnit());
		
		po.setUpdateSuccess(vo.getUpdateSuccess());
		po.setDisplayMessage(vo.getDisplayMessage());
		
		po.setUserName(vo.getUserName());
		po.setProposedEndDate(vo.getProposedEndDate());
		
		System.out.println("vo.getGrade(): " + vo.getGrade());
		System.out.println("vo.getGradeDateFrom(): " + vo.getGradeDateFrom());
		System.out.println("vo.getGradeDateTo(): " + vo.getGradeDateTo());
		po.setGrade(vo.getGrade());
		po.setGradeDateFrom(vo.getGradeDateFrom());
		po.setGradeDateTo(vo.getGradeDateTo());		
		
		po.setPositionGroup(vo.getPositionGroup());
		po.setSrcFunding(vo.getSrcFunding());
		
		po.setRepsonsibilityId(vo.getRepsonsibilityId());
		
		po.setSelectedFTE(vo.getSelectedFTE());
		po.setSelectedPostId(vo.getSelectedPostId());
		po.setSelectedMPRSPostId(vo.getSelectedMPRSPostId());
		return po;
	}
	
	@ModelAttribute("srcFundingList")
	public Map<String, String> getSourceFundingList() {
		List<HCMSourceFundingPo> list = hcmSvc.getAllHCMSourceFunding();

		Map<String, String> displayList = new LinkedHashMap<String, String>();
		for (int i=0; i<list.size(); i++) {
			displayList.put(list.get(i).getSourceFundingCode(), list.get(i).getSourceFundingDesc());
		}
		return displayList;
	}
	
	// For drop down
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
}
