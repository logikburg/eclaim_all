package hk.org.ha.eclaim.bs.request.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.request.po.EnquiryWebPo;
import hk.org.ha.eclaim.bs.request.po.MPRSPostFundingResourceSnapPo;
import hk.org.ha.eclaim.bs.request.po.MPRSPostFundingSnapPo;
import hk.org.ha.eclaim.bs.request.po.MPRSPostSnapPo;
import hk.org.ha.eclaim.bs.request.po.PostMasterPo;
import hk.org.ha.eclaim.bs.report.helper.ReportSubqueryHelper;
import hk.org.ha.eclaim.bs.request.dao.IMPRSPostSnapDao;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;

@Repository
public class MPRSPostSnapDaoImpl implements IMPRSPostSnapDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public MPRSPostSnapPo getMPRSPostSnapDetail(int postSnapUid) {
		Query q = entityManager.createQuery("SELECT C FROM MPRSPostSnapPo C where C.postSnapUid = :postSnapUid", MPRSPostSnapPo.class);
		q.setParameter("postSnapUid", postSnapUid);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			return (MPRSPostSnapPo)result.next();
		}
		
		return null;
	}

	public List<MPRSPostFundingSnapPo> getMPRSPostFundingSnapDetail(int postSnapUid) {
		Query q = entityManager.createQuery("SELECT C FROM MPRSPostFundingSnapPo C where C.postSnapUid = :postSnapUid", MPRSPostFundingSnapPo.class);
		q.setParameter("postSnapUid", postSnapUid);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		List<MPRSPostFundingSnapPo> rslt = new ArrayList<MPRSPostFundingSnapPo>();
		
		while (result.hasNext()) {
			rslt.add((MPRSPostFundingSnapPo)result.next());
		}
		
		return rslt;
	}

	public MPRSPostFundingResourceSnapPo getMPRSPostFundingResourceSnapDetail(int postSnapUid) {
		Query q = entityManager.createQuery("SELECT C FROM MPRSPostFundingResourceSnapPo C where C.postSnapUid = :postSnapUid", MPRSPostFundingResourceSnapPo.class);
		q.setParameter("postSnapUid", postSnapUid);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		if (result.hasNext()) {
			return (MPRSPostFundingResourceSnapPo)result.next();
		}
		
		return null;
	}
	
	public List<PostMasterPo> getMPRSPostSnap(List<DataAccessPo> dataAccessList, EnquiryWebPo vo, String userId, String roleId) {
		List<PostMasterPo> resultList = new ArrayList<PostMasterPo>();
		
		String sqlN = "select ";
		sqlN += " p.POST_UID, p.POST_ID, p.UNIT, '' ANNUAL_PLAN_IND, p.POST_DURATION,  p.POST_FTE, p.post_start_date, "; 
		sqlN += " p.limit_duration_end_date, p.post_status POST_STATUS_DESC, s.EMPLOYEE_NUMBER, p.POST_SNAP_UID,  ";
		sqlN += " p.effective_start_date, p.effective_end_date, p.SUPP_PROMO_IND, LIMIT_DURATION_NO , LIMIT_DURATION_UNIT ";
		sqlN += " from V_POST_EXPORT p ";
		sqlN += " left join V_HCM_STRENGTH s on ( p.POST_ID = s.POST_ID and to_date(:effectiveDate, 'dd/mm/yyyy') between s.effective_start_date and s.effective_end_date ) ";
		sqlN += " where 1=1 ";
		
		if (!"".equals(vo.getClusterCode())) {
			sqlN += " and p.cluster_code = :clusterCode ";
		}
		
		if (!"".equals(vo.getInstCode())) {
			sqlN += " and p.INST_CODE = :instCode ";
		}
		
		if (!"".equals(vo.getDeptCode())) {
			sqlN += " and p.DEPT_CODE = :deptCode ";
		}
		
		System.out.println("vo.getDeptId()):" + vo.getDeptCode() + "==");
		
		if (!"".equals(vo.getStaffGroupCode())) {
			sqlN += " and p.STAFF_GROUP_CODE = :staffGroupCode ";
		}
		
		if (!"".equals(vo.getRankCode())) {
			sqlN += " and p.RANK_CODE = :rankCode ";
		}
		
		if (!"".equals(vo.getPostId())) {
			sqlN += " and upper(p.POST_ID) like '%' || upper(:postId) || '%' ";
		}
		
		if (!"".equals(vo.getEmployeeId())) {
			sqlN += " and s.EMPLOYEE_NUMBER = :employeeId ";
		}
		
		if (!"".equals(vo.getEffectiveDate())) {
			sqlN += " and to_date(:effectiveDate, 'dd/mm/yyyy') between p.effective_start_date and p.effective_end_date ";
		}
		
		// UT30064: Added Approval Reference
		if (!"".equals(vo.getApprovalRef().trim())) {
			sqlN += " and upper(p.APPROVAL_REF) like '%' || upper(:approvalRef) || '%' ";
		}
		
		// Append the Data Access
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "p");
		if(!sqlDataAccess.isEmpty()) {
			sqlN += " and ( ";
			sqlN += sqlDataAccess;
			sqlN += " ) ";
		}
		
		sqlN += " order by p.POST_ID ";
		System.out.println("sqlN: " + sqlN);
		Query nativeQuery = entityManager.createNativeQuery(sqlN);
		if (!"".equals(vo.getEffectiveDate())) {
			nativeQuery.setParameter("effectiveDate", vo.getEffectiveDate());
		} else {
			nativeQuery.setParameter("effectiveDate", DateTimeHelper.formatDateToString(new Date()));
		}
		if (!"".equals(vo.getClusterCode())) {
			nativeQuery.setParameter("clusterCode", vo.getClusterCode());
		}
		if (!"".equals(vo.getInstCode())) {
			nativeQuery.setParameter("instCode", vo.getInstCode());
		}
		if (!"".equals(vo.getDeptCode())) {
			nativeQuery.setParameter("deptCode", vo.getDeptCode());
		}
		if (!"".equals(vo.getStaffGroupCode())) {
			nativeQuery.setParameter("staffGroupCode", vo.getStaffGroupCode());
		}
		if (!"".equals(vo.getRankCode())) {
			nativeQuery.setParameter("rankCode", vo.getRankCode());
		}
		if (!"".equals(vo.getPostId())) {
			nativeQuery.setParameter("postId", vo.getPostId());
		}
		if (!"".equals(vo.getEmployeeId())) {
			nativeQuery.setParameter("employeeId", vo.getEmployeeId());
		}
		
		// UT30064: Added Approval Reference
		if (!"".equals(vo.getApprovalRef().trim())) {
			nativeQuery.setParameter("approvalRef", vo.getApprovalRef());
		}
		
		for (int i = 0; i < dataAccessList.size(); i++) {
			DataAccessPo d = dataAccessList.get(i);
			if (d.getClusterCode() != null) {
				nativeQuery.setParameter("cluster_code" + i, d.getClusterCode());
			}
			if (d.getInstCode() != null) {
				nativeQuery.setParameter("inst_code" + i, d.getInstCode());
			}
			if (d.getDeptCode() != null) {
				nativeQuery.setParameter("dept_code" + i, d.getDeptCode());
			}
			if (d.getStaffGroupCode() != null) {
				nativeQuery.setParameter("staff_group_code" + i, d.getStaffGroupCode());
			}
		}
		
		@SuppressWarnings("unchecked")
		List<Object[]> resultNative = nativeQuery.getResultList(); 
		
		for (int i=0; i<resultNative.size(); i++) {
			Object[] temp = (Object[])resultNative.get(i);
			
			PostMasterPo c = new PostMasterPo();
			c.setPostUid(((BigDecimal)temp[0]).intValue());
			c.setPostId((String)temp[1]);
			c.setUnit((String)temp[2]);
			c.setAnnualPlan((String)temp[3]);
			c.setPostDuration((String)temp[4]);
			c.setPostFTE(((BigDecimal)temp[5]).doubleValue());
			c.setPostStartDate((Date)temp[6]);
			c.setLimitDurationEndDate((Date)temp[7]);
			
			if (temp[14] != null) {
				c.setLimitDurationNo(((BigDecimal)temp[14]).intValue());
			}
			c.setLimitDurationUnit((String)temp[15]);
			c.setPostStatusDesc((String)temp[8]);
			c.setEmployeeID((String)temp[9]);
			c.setPostSnapUid(String.valueOf(((BigDecimal)temp[10]).intValue()));
			
			String suppPromoInd = (String)temp[13];

			if ("Y".equals(suppPromoInd)) {
				c.setExtraInfo("S");
			}
			else {
				c.setExtraInfo("");
			}
			
			resultList.add(c);
		}
		
		return resultList;
	}
	
	public int getActivePostCountByHcmPositionId(String hcmPositionId, String effectiveStartDate, String effectiveEndDate) {
		Query q = entityManager.createNativeQuery("select count(*) from V_POST_EXPORT "
				+ "where HCM_POSITION_ID = :hcmPositionId "
				+ "and ( trunc(to_date(:effectiveStartDate, 'dd/mm/yyyy')) between EFFECTIVE_START_DATE and EFFECTIVE_END_DATE "
				+ "      or trunc(to_date(:effectiveEndDate, 'dd/mm/yyyy')) between EFFECTIVE_START_DATE and EFFECTIVE_END_DATE ) "
				+ "and POST_STATUS = 'ACTIVE' ");
		q.setParameter("hcmPositionId", hcmPositionId);
		q.setParameter("effectiveStartDate", effectiveStartDate);
		q.setParameter("effectiveEndDate", effectiveEndDate);

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		if (result.hasNext()) {
			return ((BigDecimal)result.next()).intValue();
		}

		return 0;
	}
	
	// Added for CC177246
	public void updateClusterRemark(String searchPostSnapUid, String clusterRef, String clusterRemark, UserPo user) {
		MPRSPostSnapPo po = getMPRSPostSnapDetail(Integer.parseInt(searchPostSnapUid));
		
		po.setClusterRef(clusterRef);
		po.setClusterRemark(clusterRemark);
		po.setUpdatedBy(user.getUserId());
		po.setUpdatedDate(new Date());
		po.setUpdatedRoleId(user.getCurrentRole());
		
		entityManager.merge(po);
	}

	// Added for CC177246
	public List<MPRSPostSnapPo> getMPRSPostSnapDetailListByPostUid(int postUid) {
		List<MPRSPostSnapPo> list = new ArrayList<MPRSPostSnapPo>();
		Query q = entityManager.createQuery("SELECT C FROM MPRSPostSnapPo C where C.postUid = :postUid", MPRSPostSnapPo.class);
		q.setParameter("postUid", postUid);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		
		while (result.hasNext()) {
			list.add((MPRSPostSnapPo)result.next());
		}
		
		return list;
	}
	
	// Added for ST08733
	public boolean isExistingPost(String postId){
		System.out.println("isExistingPost: " + postId);
		Query q = entityManager.createNativeQuery(" select 1 from rq_master m, rq_post p "
				+ " where post_id = :postId "
				+ " and m.request_type = 'NEW' "
				+ " and m.request_status = 'CONFIRMED' "
				+ " and p.request_uid = m.request_uid "
				+ " and m.rec_state = 'A' "
				+ " and p.rec_state = 'A' ");
		q.setParameter("postId", postId);

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		if (result.hasNext()) {
			return true;
		}

		return false;
	}
}
