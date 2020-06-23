package hk.org.ha.eclaim.bs.hcm.dao.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.request.po.PostMasterPo;
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
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Repository
public class HCMCommonDaoImpl implements IHCMCommonDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public List<HCMJobPo> getAllHCMJob() {
		List<HCMJobPo> resultList = new ArrayList<HCMJobPo>();

		return resultList;
	}

	public List<HCMOrganizationPo> getAllHCMOrganization() {
		List<HCMOrganizationPo> resultList = new ArrayList<HCMOrganizationPo>();

		return resultList;
	}

	public List<HCMPostOrganizationPo> getAllHCMPostOrganization() {
		List<HCMPostOrganizationPo> resultList = new ArrayList<HCMPostOrganizationPo>();

		return resultList;
	}

	public List<HCMPostTitlePo> getAllHCMPostTitle() {
		List<HCMPostTitlePo> resultList = new ArrayList<HCMPostTitlePo>();

		return resultList;
	}

	public int createHCMPosition(CreateHCMPostWebPo vo, String userId) throws Exception {
		return 0;
	}
	
	
	public int createHCMPositionApprover(CreateHCMPostWebPo vo, String userId, String roleId) throws Exception {
		return 1;
	}

	public String updateHCMPosition(UpdateHCMPostWebPo updatePositionWebVo, String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteHCMPosition(String effectiveDate, int positionId, int versionNo, String type,
			String repsonsibilityId, String userId) throws ParseException {
		// TODO Auto-generated method stub
		
	}

	public void deleteValidGrade(int gradeUid, int versionNumber, String repsonsibilityId, String userId) {
		// TODO Auto-generated method stub
		
	}

	public List<HCMRecordPo> searchHCMRecord(String effectiveDate, String positionTitle, String positionOrganization,
			String unitTeam, String job, String organization, String positionName, String allowSingleIncumbent,
			String requestType, String staffGroup, String userId, String roleId) {
		// TODO Auto-generated method stub
		return null;
	}

	public HCMRecordPo getHCMRecordByPositionId(int positionId) {
		// TODO Auto-generated method stub
		return null;
	}

	public HCMRecordPo getHCMRecordByPositionId(int positionId, String effectiveDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMProbationDurationUnitPo> getAllHCMProbationDurationUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMRecordGradePo> getHCMGradeRecordByPositionId(int positionId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMGradePo> getAllHCMGrade() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMLocationPo> getAllHCMLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMAssignmentPo> getAllHCMAssignment(String postId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMJobPo> getAllHCMJob(String term) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMOrganizationPo> getAllHCMOrganization(String term, String userId, String roleId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMPostOrganizationPo> getAllHCMPostOrganization(String term, String userId, String roleId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMPostTitlePo> getAllHCMPostTitle(String term) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMPostTitlePo> getAllHCMPostTitleByStaffGroup(String term, String staffGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMLocationPo> getAllHCMLocation(String term) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMGradePo> getAllHCMGrade(String term) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMSourceFundingPo> getAllHCMSourceFunding() {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveFTEUpdateHistory(FTEUpdateHistory history) {
		// TODO Auto-generated method stub
		
	}

	public List<HCMStrengthPo> getStrength(String mprsPostId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMEmployeePo> getEmployee() {
		// TODO Auto-generated method stub
		return null;
	}

	public HCMEmployeePo getEmployeeByEmployeeId(int employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HCMResponsibilityPo> getHCMResponsibilityByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public HCMAssignmentPo getHCMAssignment(String assignmentNumber, String effectiveStartDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isHCMPayrollRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	public HCMLocationPo getDefaultLocation(int organizationId) {
		// TODO Auto-generated method stub
		return null;
	}

	public HCMAssignmentPo getHCMAssignmentByPostIdEffectiveDate(String postId, String effectiveDate) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
