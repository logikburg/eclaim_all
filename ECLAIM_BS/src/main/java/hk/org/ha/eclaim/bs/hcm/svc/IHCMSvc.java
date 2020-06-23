package hk.org.ha.eclaim.bs.hcm.svc;

import java.text.ParseException;
import java.util.List;

import hk.org.ha.eclaim.bs.hcm.po.CreateHCMPostWebPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMAssignmentPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMEmployeePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMGradePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMJobPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMLocationPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMOrganizationPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMPostOrganizationPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMPostTitlePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMProbationDurationUnitPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMRecordGradePo;
import hk.org.ha.eclaim.bs.hcm.po.HCMRecordPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMResponsibilityPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMSourceFundingPo;
import hk.org.ha.eclaim.bs.hcm.po.HCMStrengthPo;
import hk.org.ha.eclaim.bs.hcm.po.UpdateHCMPostWebPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IHCMSvc {
	List<HCMJobPo> getAllHCMJob();
	
	List<HCMOrganizationPo> getAllHCMOrganization();
	
	List<HCMPostOrganizationPo> getAllHCMPostOrganization();
	
	List<HCMPostTitlePo> getAllHCMPostTitle();
	
	int createHCMPosition(CreateHCMPostWebPo createPositionWebVo, String userId) throws Exception;
	
	int createHCMPositionApprover(CreateHCMPostWebPo createPositionWebVo, String userId, String roleId) throws Exception;

	String updateHCMPosition(UpdateHCMPostWebPo updatePositionWebVo, String userId) throws Exception;
	
	void deleteHCMPosition(String effectiveDate, int positionId, int versionNo, String type, String repsonsibilityId, String userId) throws ParseException;
	
	void deleteValidGrade(int gradeUid, int versionNumber, String repsonsibilityId, String userId);
	
	List<HCMRecordPo> searchHCMRecord(String effectiveDate, 
									  String positionTitle, 
						              String positionOrganization, 
						              String unitTeam,
						              String job,
						              String organization,
						              String positionName,
						              String allowSingleIncumbent,
						              String requestType,
						              String staffGroup,
						              String userId,
						              String roleId);
	
	HCMRecordPo getHCMRecordByPositionId(int positionId);
	
	HCMRecordPo getHCMRecordByPositionId(int positionId, String effectiveDate);

	List<HCMProbationDurationUnitPo> getAllHCMProbationDurationUnit();
	
	List<HCMRecordGradePo> getHCMGradeRecordByPositionId(int positionId); 

	List<HCMGradePo> getAllHCMGrade();
	
	List<HCMLocationPo> getAllHCMLocation();
	
	List<HCMAssignmentPo> getAllHCMAssignment(String postId);

	List<HCMJobPo> getAllHCMJob(String term);
	
	List<HCMOrganizationPo> getAllHCMOrganization(String term, String userId, String roleId);
	
	List<HCMPostOrganizationPo> getAllHCMPostOrganization(String term, String userId, String roleId);
	
	List<HCMPostTitlePo> getAllHCMPostTitle(String term);
	
	List<HCMPostTitlePo> getAllHCMPostTitleByStaffGroup(String term, String staffGroup);
	
	List<HCMLocationPo> getAllHCMLocation(String term);

	List<HCMGradePo> getAllHCMGrade(String term);
	
	List<HCMSourceFundingPo> getAllHCMSourceFunding();
	
	void updateFTE(int positionId, Double fte, int headCount, String reason, UserPo user) throws ParseException;
	
	List<HCMStrengthPo> getStrength(String mprsPostId);
	
	List<HCMEmployeePo> getEmployee();
	
	HCMEmployeePo getEmployeeByEmployeeId(int employeeId);
	
	List<HCMResponsibilityPo> getHCMResponsibilityByUserId(String userId);
	
	HCMAssignmentPo getHCMAssignment(String assignmentNumber, String effectiveStartDate);

	boolean isHCMPayrollRunning();
	
	HCMLocationPo getDefaultLocation(int organizationId);
	
	HCMAssignmentPo getHCMAssignmentByPostIdEffectiveDate(String postId, String effectiveDate);
}
