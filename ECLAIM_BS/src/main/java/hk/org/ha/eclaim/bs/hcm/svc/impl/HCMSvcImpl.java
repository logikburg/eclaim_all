package hk.org.ha.eclaim.bs.hcm.svc.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.hcm.dao.IHCMCommonDao;
import hk.org.ha.eclaim.bs.hcm.po.CreateHCMPostWebPo;
import hk.org.ha.eclaim.bs.hcm.po.FTEUpdateHistory;
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
import hk.org.ha.eclaim.bs.hcm.svc.IHCMSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Service("hcmSvc")
public class HCMSvcImpl implements IHCMSvc {

	@Autowired
	IHCMCommonDao hcmCommonDao;
	
	public List<HCMJobPo> getAllHCMJob() {
		return hcmCommonDao.getAllHCMJob();
	}

	public List<HCMOrganizationPo> getAllHCMOrganization() {
		return hcmCommonDao.getAllHCMOrganization();
	}

	public List<HCMPostOrganizationPo> getAllHCMPostOrganization() {
		return hcmCommonDao.getAllHCMPostOrganization();
	}

	public List<HCMPostTitlePo> getAllHCMPostTitle() {
		return hcmCommonDao.getAllHCMPostTitle();
	}

	@Transactional
	public int createHCMPosition(CreateHCMPostWebPo createPositionWebVo, String userId) throws Exception {
		return hcmCommonDao.createHCMPosition(createPositionWebVo, userId);
	}
	
	@Transactional
	public int createHCMPositionApprover(CreateHCMPostWebPo createPositionWebVo, String userId, String roleId) throws Exception {
		return hcmCommonDao.createHCMPositionApprover(createPositionWebVo, userId, roleId);
	}

	@Transactional
	public String updateHCMPosition(UpdateHCMPostWebPo updatePositionWebVo, String userId) throws Exception {
		return hcmCommonDao.updateHCMPosition(updatePositionWebVo, userId);
	}

	@Transactional
	public void deleteHCMPosition(String effectiveDate, int positionId, int versionNo, String type, String repsonsibilityId, String userId) throws ParseException {
		hcmCommonDao.deleteHCMPosition(effectiveDate, positionId, versionNo, type, repsonsibilityId, userId);		
	}
	
	@Transactional
	public void deleteValidGrade(int gradeUid, int versionNumber, String repsonsibilityId, String userId) {
		hcmCommonDao.deleteValidGrade(gradeUid, versionNumber, repsonsibilityId, userId);
	}

	public List<HCMRecordPo> searchHCMRecord(String effectiveDate, 
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
								             String roleId) {
		return hcmCommonDao.searchHCMRecord(effectiveDate, positionTitle, positionOrganization, unitTeam, job, organization, positionName, allowSingleIncumbent, requestType, staffGroup, userId, roleId);
	}

	public HCMRecordPo getHCMRecordByPositionId(int positionId) {
		return hcmCommonDao.getHCMRecordByPositionId(positionId);
	}

	public HCMRecordPo getHCMRecordByPositionId(int positionId, String effectiveDate) {
		return hcmCommonDao.getHCMRecordByPositionId(positionId, effectiveDate);
	}
	
	public List<HCMRecordGradePo> getHCMGradeRecordByPositionId(int positionId) {
		return hcmCommonDao.getHCMGradeRecordByPositionId(positionId);
	}

	public List<HCMProbationDurationUnitPo> getAllHCMProbationDurationUnit() {
		return hcmCommonDao.getAllHCMProbationDurationUnit();
	}

	public List<HCMGradePo> getAllHCMGrade() {
		return hcmCommonDao.getAllHCMGrade();
	}
	
	public List<HCMLocationPo> getAllHCMLocation() {
		return hcmCommonDao.getAllHCMLocation();
	}
	
	public List<HCMAssignmentPo> getAllHCMAssignment(String postId) {
		return hcmCommonDao.getAllHCMAssignment(postId);
	}

	public List<HCMJobPo> getAllHCMJob(String term) {
		return hcmCommonDao.getAllHCMJob(term);
	}

	public List<HCMOrganizationPo> getAllHCMOrganization(String term, String userId, String roleId) {
		return hcmCommonDao.getAllHCMOrganization(term, userId, roleId);
	}

	public List<HCMPostOrganizationPo> getAllHCMPostOrganization(String term, String userId, String roleId) {
		return hcmCommonDao.getAllHCMPostOrganization(term, userId, roleId);
	}

	public List<HCMPostTitlePo> getAllHCMPostTitle(String term) {
		return hcmCommonDao.getAllHCMPostTitle(term);
	}
	
	public List<HCMPostTitlePo> getAllHCMPostTitleByStaffGroup(String term, String staffGroup) {
		return hcmCommonDao.getAllHCMPostTitleByStaffGroup(term, staffGroup);
	}

	public List<HCMLocationPo> getAllHCMLocation(String term) {
		return hcmCommonDao.getAllHCMLocation(term);
	}

	public List<HCMGradePo> getAllHCMGrade(String term) {
		return hcmCommonDao.getAllHCMGrade(term);
	}

	public List<HCMSourceFundingPo> getAllHCMSourceFunding() {
		return hcmCommonDao.getAllHCMSourceFunding();
	}
	
	@Transactional
	public void updateFTE(int positionId, Double fte, int headCount, String reason, UserPo user) throws ParseException {
		// hcmCommonDao.updateFTE(positionId, fte, headCount);
		
		FTEUpdateHistory history = new FTEUpdateHistory();
		history.setHcmPositionId(positionId);
		history.setNewFte(fte);
		history.setUpdateReason(reason);
		history.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
		history.setCreatedBy(user.getUserId());
		history.setCreatedRoleId(user.getCurrentRole());
		history.setCreatedDate(new Date());
		history.setUpdatedBy(user.getUserId());
		history.setUpdatedRoleId(user.getCurrentRole());
		history.setUpdatedDate(new Date());
		
		hcmCommonDao.saveFTEUpdateHistory(history);
	}

	public List<HCMStrengthPo> getStrength(String mprsPostId) {
		return hcmCommonDao.getStrength(mprsPostId);
	}

	public List<HCMEmployeePo> getEmployee() {
		return hcmCommonDao.getEmployee();
	}
	
	public HCMEmployeePo getEmployeeByEmployeeId(int employeeId) {
		return hcmCommonDao.getEmployeeByEmployeeId(employeeId);
	}
	
	public List<HCMResponsibilityPo> getHCMResponsibilityByUserId(String userId) {
		return hcmCommonDao.getHCMResponsibilityByUserId(userId);
	}

	public HCMAssignmentPo getHCMAssignment(String assignmentNumber, String effectiveStartDate) {
		return hcmCommonDao.getHCMAssignment(assignmentNumber, effectiveStartDate);
	}

	public boolean isHCMPayrollRunning() {
		return hcmCommonDao.isHCMPayrollRunning();
	}
	
	public HCMLocationPo getDefaultLocation(int organizationId) {
		return hcmCommonDao.getDefaultLocation(organizationId);
	}

	public HCMAssignmentPo getHCMAssignmentByPostIdEffectiveDate(String postId, String effectiveDate) {
		return hcmCommonDao.getHCMAssignmentByPostIdEffectiveDate(postId, effectiveDate);
	}
}
