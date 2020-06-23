package hk.org.ha.eclaim.bs.request.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.request.po.MPRSSearchCriteria;
import hk.org.ha.eclaim.bs.request.po.PostMasterPo;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.report.helper.ReportSubqueryHelper;
import hk.org.ha.eclaim.bs.request.dao.IMPRSPostDao;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Repository
public class MPRSPostDaoImpl implements IMPRSPostDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Returns Post from Post Master table
	 * For New Post (Clone from existing post), Change of Funding, HCM Approver
	 */
	public List<PostMasterPo> getMPRSPost(List<DataAccessPo> dataAccessList, MPRSSearchCriteria mprSearchRequest,
			String userId, String roleId) {
		List<PostMasterPo> resultList = new ArrayList<PostMasterPo>();

		String sql = "SELECT C.* FROM POST_MASTER C ";
		
		// For Upgrade to Existing Post, limit search result to Upgrade To Rank
		if (MPRSConstant.REQUEST_TYPE_UPGRADE.equals(mprSearchRequest.getRequestType())) {
			sql += " inner join CS_UPGRADE_RANK_MAP M ON ( M.RANK_TO = C.RANK_CODE ) "
					+ " inner join CS_RANK R ON ( R.RANK_NAME = :rankName and R.RANK_CODE = M.RANK_FROM ) ";
		}
		
		// if employee id is not search criteria, no need to join V_HCM_STRENGTH
		if (!"".equals(mprSearchRequest.getSearchEmployeeId()) && mprSearchRequest.getSearchEmployeeId() != null) {
			sql += " left join V_HCM_STRENGTH s on ( C.POST_ID = s.POST_ID "
					+ "and s.EMPLOYEE_NUMBER = :employeeId "
					+ "and trunc(sysdate) between s.EFFECTIVE_START_DATE and s.EFFECTIVE_END_DATE ) ";
		}
		
		sql += " where C.post_status = 'ACTIVE' ";
		
		// cluster
		if (!"".equals(mprSearchRequest.getSearchClusterId())) {
			sql += "and C.cluster_code = :clusterCode ";
		}
		
		// inst
		if (!"".equals(mprSearchRequest.getSearchInstId())) {
			sql += "and C.inst_code = :instCode ";
		}
		
		// dept
		if (!"".equals(mprSearchRequest.getSearchDeptId())) {
			sql += "and C.DEPT_CODE = :deptCode ";
		}
		
		// staff group
		if (!"".equals(mprSearchRequest.getSearchStaffGroupId())) {
			sql += "and C.STAFF_GROUP_CODE = :staffGroupCode ";
		}
		
		// post rank
		if (!"".equals(mprSearchRequest.getSearchRankId())) {
			sql += "and C.RANK_CODE = :rankCode ";
		}
		
		// post id
		if (!"".equals(mprSearchRequest.getSearchPostId())) {
			sql += "and upper(C.POST_ID) like '%' || upper(:postId) || '%' ";
		}
		
		// employee id
		if (!"".equals(mprSearchRequest.getSearchEmployeeId()) && mprSearchRequest.getSearchEmployeeId() != null) {
			sql += " and s.EMPLOYEE_NUMBER = :employeeId ";
		}

		// Append the Data Access
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "C");
		if (!sqlDataAccess.isEmpty()) {
			sql += " and ( ";
			sql += sqlDataAccess;
			sql += " ) ";
		}

		sql += " order by C.POST_ID ";

		Query q = entityManager.createNativeQuery(sql, PostMasterPo.class);
		if (!"".equals(mprSearchRequest.getSearchClusterId())) {
			q.setParameter("clusterCode", mprSearchRequest.getSearchClusterId());
		}
		if (!"".equals(mprSearchRequest.getSearchInstId())) {
			q.setParameter("instCode", mprSearchRequest.getSearchInstId());
		}
		if (!"".equals(mprSearchRequest.getSearchDeptId())) {
			q.setParameter("deptCode", mprSearchRequest.getSearchDeptId());
		}
		if (!"".equals(mprSearchRequest.getSearchStaffGroupId())) {
			q.setParameter("staffGroupCode", mprSearchRequest.getSearchStaffGroupId());
		}
		if (!"".equals(mprSearchRequest.getSearchRankId())) {
			q.setParameter("rankCode", mprSearchRequest.getSearchRankId());
		}
		if (!"".equals(mprSearchRequest.getSearchPostId())) {
			q.setParameter("postId", mprSearchRequest.getSearchPostId().trim());
		}
		if (!"".equals(mprSearchRequest.getSearchEmployeeId()) && mprSearchRequest.getSearchEmployeeId() != null) {
			q.setParameter("employeeId", mprSearchRequest.getSearchEmployeeId());
		}
		if (!"".equals(mprSearchRequest.getFromRankName()) && mprSearchRequest.getFromRankName() != null) {
			q.setParameter("rankName", mprSearchRequest.getFromRankName());
		}

		for (int i = 0; i < dataAccessList.size(); i++) {
			DataAccessPo d = dataAccessList.get(i);
			if (d.getClusterCode() != null) {
				q.setParameter("cluster_code" + i, d.getClusterCode());
			}
			if (d.getInstCode() != null) {
				q.setParameter("inst_code" + i, d.getInstCode());
			}
			if (d.getDeptCode() != null) {
				q.setParameter("dept_code" + i, d.getDeptCode());
			}
			if (d.getStaffGroupCode() != null) {
				q.setParameter("staff_group_code" + i, d.getStaffGroupCode());
			}
		}

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			PostMasterPo c = (PostMasterPo) result.next();
			resultList.add(c);
		}

		return resultList;
	}
	
	/**
	 * Return Vacancy Post from Post Master table, and V_HCM_STRENGTH
	 * For Change of Staff Mix, Deletion, Frozen, Upgrade
	 */
	public List<PostMasterPo> getVacancyMPRSPost(List<DataAccessPo> dataAccessList, MPRSSearchCriteria mprSearchRequest,
			String userId, String roleId) {
		List<PostMasterPo> resultList = new ArrayList<PostMasterPo>();
		
		String sql = "SELECT C.* FROM POST_MASTER C ";
		sql += " left join V_HCM_STRENGTH s on C.POST_ID = s.POST_ID and to_date(:effectiveDate, 'dd/mm/yyyy') between s.EFFECTIVE_START_DATE and s.EFFECTIVE_END_DATE ";
		sql += " where C.post_status in ('ACTIVE'" + ( "FROZEN".equals(mprSearchRequest.getRequestType()) ? ", 'FROZEN')" : ")" );
		sql += " and s.EMPLOYEE_NUMBER is null ";
		sql += " and (C.CLUSTER_CODE, C.INST_CODE) in (select i.CLUSTER_CODE, i.inst_code from SS_USER_ROLE ur, SS_USER_ROLE_DATA_ACCESS d ";
		sql += "LEFT JOIN CS_INST i on (d.CLUSTER_CODE = i.CLUSTER_CODE and (d.INST_CODE = i.INST_CODE or d.INST_CODE is null)) or (d.CLUSTER_CODE is null and d.INST_CODE is null)  ";
		sql += "where ur.USER_ROLE_UID = d.USER_ROLE_UID and ur.user_id = :userId and ur.role_id = :roleId) and TRUNC(C.POST_START_DATE) <= TRUNC(to_date(:effectiveDate, 'dd/mm/yyyy'))  ";
		
		// cluster
		if (!"".equals(mprSearchRequest.getSearchClusterId())) {
			sql += " and C.cluster_code = :clusterCode ";
		}
		
		// inst
		if (!"".equals(mprSearchRequest.getSearchInstId())) {
			sql += " and C.inst_code = :instCode ";
		}
		
		// dept
		if (!"".equals(mprSearchRequest.getSearchDeptId())) {
			sql += " and c.DEPT_CODE = :deptCode ";
		}
		
		// staff group
		if (!"".equals(mprSearchRequest.getSearchStaffGroupId())) {
			sql += " and C.STAFF_GROUP_CODE = :staffGroupCode ";
		}
		
		// post rank
		if (!"".equals(mprSearchRequest.getSearchRankId())) {
			sql += " and C.RANK_CODE = :rankCode ";
		}
		
		// post id
		if (!"".equals(mprSearchRequest.getSearchPostId())) {
			sql += " and upper(C.POST_ID) like '%' || upper(:postId) || '%' ";
		}

		// Append the Data Access
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "C");
		if (!sqlDataAccess.isEmpty()) {
			sql += " and ( ";
			sql += sqlDataAccess;
			sql += " ) ";
		}

		sql += " order by C.POST_ID ";

		System.out.println("sql: " + sql);
		Query q = entityManager.createNativeQuery(sql, PostMasterPo.class);
		q.setParameter("userId", userId);
		q.setParameter("roleId", roleId);
		q.setParameter("effectiveDate", mprSearchRequest.getSearchEffectiveDate());
		if (!"".equals(mprSearchRequest.getSearchClusterId())) {
			q.setParameter("clusterCode", mprSearchRequest.getSearchClusterId());
		}
		if (!"".equals(mprSearchRequest.getSearchInstId())) {
			q.setParameter("instCode", mprSearchRequest.getSearchInstId());
		}
		if (!"".equals(mprSearchRequest.getSearchDeptId())) {
			q.setParameter("deptCode", mprSearchRequest.getSearchDeptId());
		}
		if (!"".equals(mprSearchRequest.getSearchStaffGroupId())) {
			q.setParameter("staffGroupCode", mprSearchRequest.getSearchStaffGroupId());
		}
		if (!"".equals(mprSearchRequest.getSearchRankId())) {
			q.setParameter("rankCode", mprSearchRequest.getSearchRankId());
		}
		if (!"".equals(mprSearchRequest.getSearchPostId())) {
			q.setParameter("postId", mprSearchRequest.getSearchPostId().trim());
		}

		for (int i = 0; i < dataAccessList.size(); i++) {
			DataAccessPo d = dataAccessList.get(i);
			if (d.getClusterCode() != null) {
				q.setParameter("cluster_code" + i, d.getClusterCode());
			}
			if (d.getInstCode() != null) {
				q.setParameter("inst_code" + i, d.getInstCode());
			}
			if (d.getDeptCode() != null) {
				q.setParameter("dept_code" + i, d.getDeptCode());
			}
			if (d.getStaffGroupCode() != null) {
				q.setParameter("staff_group_code" + i, d.getStaffGroupCode());
			}
		}

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			PostMasterPo c = (PostMasterPo) result.next();
			resultList.add(c);
		}

		return resultList;
	}
	
	/**
	 * Return Resident Post from Post Master table
	 * For Supplementary Promotion
	 */
	public List<PostMasterPo> getMPRSResidentPost(List<DataAccessPo> dataAccessList, MPRSSearchCriteria mprSearchRequest,
			String userId, String roleId) {
		List<PostMasterPo> resultList = new ArrayList<PostMasterPo>();
		
		String sql = "SELECT C.* FROM POST_MASTER C ";
		sql += " inner join V_HCM_STRENGTH s on ( C.POST_ID = s.POST_ID and to_date(:effectiveDate, 'dd/mm/yyyy') between s.EFFECTIVE_START_DATE and s.EFFECTIVE_END_DATE ) ";
		sql += " where C.post_status = 'ACTIVE' and C.RANK_CODE = 'R' and s.EMPLOYEE_NUMBER is not null ";
		sql += " and NVL(C.SUPP_PROMO_IND, 'N') = 'N' ";
		sql += " and (C.CLUSTER_CODE, C.INST_CODE) in (select i.CLUSTER_CODE, i.inst_code from SS_USER_ROLE ur, SS_USER_ROLE_DATA_ACCESS d ";
		sql += "LEFT JOIN CS_INST i on (d.CLUSTER_CODE = i.CLUSTER_CODE and (d.INST_CODE = i.INST_CODE or d.INST_CODE is null)) or (d.CLUSTER_CODE is null and d.INST_CODE is null)  ";
		sql += "where ur.USER_ROLE_UID = d.USER_ROLE_UID and ur.user_id = :userId and ur.role_id = :roleId) ";

		if (!"".equals(mprSearchRequest.getSearchClusterId())) {
			sql += " and C.cluster_code = :clusterCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchInstId())) {
			sql += " and C.inst_code = :instCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchDeptId())) {
			sql += " and c.DEPT_CODE = :deptCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchStaffGroupId())) {
			sql += " and C.STAFF_GROUP_CODE = :staffGroupCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchRankId())) {
			sql += " and C.RANK_CODE = :rankCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchPostId())) {
			sql += " and upper(C.POST_ID) like '%' || upper(:postId) || '%' ";
		}

		// Append the Data Access
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "C");
		if (!sqlDataAccess.isEmpty()) {
			sql += " and ( ";
			sql += sqlDataAccess;
			sql += " ) ";
		}

		sql += " order by C.POST_ID ";

		System.out.println("sql: " + sql);
		Query q = entityManager.createNativeQuery(sql, PostMasterPo.class);
		q.setParameter("userId", userId);
		q.setParameter("roleId", roleId);
		q.setParameter("effectiveDate", mprSearchRequest.getSearchEffectiveDate());
		if (!"".equals(mprSearchRequest.getSearchClusterId())) {
			q.setParameter("clusterCode", mprSearchRequest.getSearchClusterId());
		}
		if (!"".equals(mprSearchRequest.getSearchInstId())) {
			q.setParameter("instCode", mprSearchRequest.getSearchInstId());
		}
		if (!"".equals(mprSearchRequest.getSearchDeptId())) {
			q.setParameter("deptCode", mprSearchRequest.getSearchDeptId());
		}
		if (!"".equals(mprSearchRequest.getSearchStaffGroupId())) {
			q.setParameter("staffGroupCode", mprSearchRequest.getSearchStaffGroupId());
		}
		if (!"".equals(mprSearchRequest.getSearchRankId())) {
			q.setParameter("rankCode", mprSearchRequest.getSearchRankId());
		}
		if (!"".equals(mprSearchRequest.getSearchPostId())) {
			q.setParameter("postId", mprSearchRequest.getSearchPostId().trim());
		}

		for (int i = 0; i < dataAccessList.size(); i++) {
			DataAccessPo d = dataAccessList.get(i);
			if (d.getClusterCode() != null) {
				q.setParameter("cluster_code" + i, d.getClusterCode());
			}
			if (d.getInstCode() != null) {
				q.setParameter("inst_code" + i, d.getInstCode());
			}
			if (d.getDeptCode() != null) {
				q.setParameter("dept_code" + i, d.getDeptCode());
			}
			if (d.getStaffGroupCode() != null) {
				q.setParameter("staff_group_code" + i, d.getStaffGroupCode());
			}
		}

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			PostMasterPo c = (PostMasterPo) result.next();
			resultList.add(c);
		}

		return resultList;
	}
	
	/**
	 * Return Time Limited Post from Post Master table
	 * For Extension
	 */
	public List<PostMasterPo> getMPRSPostTimeLimit(List<DataAccessPo> dataAccessList,
			MPRSSearchCriteria mprSearchRequest) {
		List<PostMasterPo> resultList = new ArrayList<PostMasterPo>();
		
		String sql = "SELECT C.* FROM POST_MASTER C ";
		sql += " where C.post_Status = 'ACTIVE' and C.POST_DURATION <> 'R' ";

		if (!"".equals(mprSearchRequest.getSearchClusterId())) {
			sql += " and C.cluster_code = :clusterCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchInstId())) {
			sql += " and C.INST_CODE = :instCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchDeptId())) {
			sql += " and C.DEPT_CODE = :deptCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchStaffGroupId())) {
			sql += " and C.STAFF_GROUP_CODE = :staffGroupCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchRankId())) {
			sql += " and C.RANK_CODE = :rankCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchPostId())) {
			sql += " and upper(C.POST_ID) like '%' || upper(:postId) || '%' ";
		}

		// Append the Data Access
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "C");
		if (!sqlDataAccess.isEmpty()) {
			sql += " and ( ";
			sql += sqlDataAccess;
			sql += " ) ";
		}

		Query q = entityManager.createNativeQuery(sql, PostMasterPo.class);
		if (!"".equals(mprSearchRequest.getSearchClusterId())) {
			q.setParameter("clusterCode", mprSearchRequest.getSearchClusterId());
		}
		if (!"".equals(mprSearchRequest.getSearchInstId())) {
			q.setParameter("instCode", mprSearchRequest.getSearchInstId());
		}
		if (!"".equals(mprSearchRequest.getSearchDeptId())) {
			q.setParameter("deptCode", mprSearchRequest.getSearchDeptId());
		}
		if (!"".equals(mprSearchRequest.getSearchStaffGroupId())) {
			q.setParameter("staffGroupCode", mprSearchRequest.getSearchStaffGroupId());
		}
		if (!"".equals(mprSearchRequest.getSearchRankId())) {
			q.setParameter("rankCode", mprSearchRequest.getSearchRankId());
		}
		if (!"".equals(mprSearchRequest.getSearchPostId())) {
			q.setParameter("postId", mprSearchRequest.getSearchPostId().trim());
		}

		for (int i = 0; i < dataAccessList.size(); i++) {
			DataAccessPo d = dataAccessList.get(i);
			if (d.getClusterCode() != null) {
				q.setParameter("cluster_code" + i, d.getClusterCode());
			}
			if (d.getInstCode() != null) {
				q.setParameter("inst_code" + i, d.getInstCode());
			}
			if (d.getDeptCode() != null) {
				q.setParameter("dept_code" + i, d.getDeptCode());
			}
			if (d.getStaffGroupCode() != null) {
				q.setParameter("staff_group_code" + i, d.getStaffGroupCode());
			}
		}

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			PostMasterPo c = (PostMasterPo) result.next();
			resultList.add(c);
		}

		return resultList;
	}
	
	public List<PostMasterPo> getMPRSPostTimeLimitAndFTELessThanOne(List<DataAccessPo> dataAccessList, MPRSSearchCriteria mprSearchRequest) {
		List<PostMasterPo> resultList = new ArrayList<PostMasterPo>();
		
		String sql = "SELECT C.* FROM POST_MASTER C ";
		sql += " where C.post_Status = 'ACTIVE' and C.POST_DURATION <> 'R' and C.POST_FTE < 1 ";

		if (!"".equals(mprSearchRequest.getSearchClusterId())) {
			sql += " and C.cluster_code = :clusterCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchInstId())) {
			sql += " and C.INST_CODE = :instCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchDeptId())) {
			sql += " and C.DEPT_CODE = :deptCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchStaffGroupId())) {
			sql += " and C.STAFF_GROUP_CODE = :staffGroupCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchRankId())) {
			sql += " and C.RANK_CODE = :rankCode ";
		}

		if (!"".equals(mprSearchRequest.getSearchPostId())) {
			sql += " and upper(C.POST_ID) like '%' || upper(:postId) || '%' ";
		}

		// Append the Data Access
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "C");
		if (!sqlDataAccess.isEmpty()) {
			sql += " and ( ";
			sql += sqlDataAccess;
			sql += " ) ";
		}

		Query q = entityManager.createNativeQuery(sql, PostMasterPo.class);
		if (!"".equals(mprSearchRequest.getSearchClusterId())) {
			q.setParameter("clusterCode", mprSearchRequest.getSearchClusterId());
		}
		if (!"".equals(mprSearchRequest.getSearchInstId())) {
			q.setParameter("instCode", mprSearchRequest.getSearchInstId());
		}
		if (!"".equals(mprSearchRequest.getSearchDeptId())) {
			q.setParameter("deptCode", mprSearchRequest.getSearchDeptId());
		}
		if (!"".equals(mprSearchRequest.getSearchStaffGroupId())) {
			q.setParameter("staffGroupCode", mprSearchRequest.getSearchStaffGroupId());
		}
		if (!"".equals(mprSearchRequest.getSearchRankId())) {
			q.setParameter("rankCode", mprSearchRequest.getSearchRankId());
		}
		if (!"".equals(mprSearchRequest.getSearchPostId())) {
			q.setParameter("postId", mprSearchRequest.getSearchPostId().trim());
		}

		for (int i = 0; i < dataAccessList.size(); i++) {
			DataAccessPo d = dataAccessList.get(i);
			if (d.getClusterCode() != null) {
				q.setParameter("cluster_code" + i, d.getClusterCode());
			}
			if (d.getInstCode() != null) {
				q.setParameter("inst_code" + i, d.getInstCode());
			}
			if (d.getDeptCode() != null) {
				q.setParameter("dept_code" + i, d.getDeptCode());
			}
			if (d.getStaffGroupCode() != null) {
				q.setParameter("staff_group_code" + i, d.getStaffGroupCode());
			}
		}

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			PostMasterPo c = (PostMasterPo) result.next();
			resultList.add(c);
		}

		return resultList;
	}
	
	public PostMasterPo getPostByPostUid(int postUid) {
		Query q = entityManager.createQuery("SELECT C FROM PostMasterPo C where C.postUid = :postUid", PostMasterPo.class);
		q.setParameter("postUid", postUid);

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		if (result.hasNext()) {
			return (PostMasterPo) result.next();
		}

		return null;
	}
	
	/**
	 * For HCM functions
	 */
	public PostMasterPo getMPRSDefaultByHCMPositionId(int hcmPositionId) {
		String deptCode = null;
		String staffGroupCode = null;
		String rankCode = null;

		Query q = entityManager.createQuery("SELECT C FROM PostMasterPo C where C.hcmPositionId = :hcmPositionId",
				PostMasterPo.class);
		q.setParameter("hcmPositionId", String.valueOf(hcmPositionId));

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			PostMasterPo tmp = (PostMasterPo) result.next();
			if (deptCode == null) {
				deptCode = tmp.getDeptCode();
			} else {
				if (!deptCode.equals(tmp.getDeptCode())) {
					deptCode = "";
				}
			}

			if (staffGroupCode == null) {
				staffGroupCode = tmp.getStaffGroupCode();
			} else {
				if (!staffGroupCode.equals(tmp.getStaffGroupCode())) {
					staffGroupCode = "";
				}
			}

			if (rankCode == null) {
				rankCode = tmp.getRankCode();
			} else {
				if (!rankCode.equals(tmp.getRankCode())) {
					rankCode = "";
				}
			}
		}

		PostMasterPo tmpResult = new PostMasterPo();
		tmpResult.setDeptCode(deptCode);
		tmpResult.setStaffGroupCode(staffGroupCode);
		tmpResult.setRankCode(rankCode);

		return tmpResult;
	}
	
//	public int getMPRSByHCMPositionId(String hcmPositionId, String effectiveDate) {
//		Query q = entityManager.createNativeQuery("select count(*) from POST_MASTER_SNAP where HCM_POSITION_ID = :hcmPositionId and trunc(EFFECTIVE_DATE) >= trunc(to_date(:effectiveDate, 'dd/mm/yyyy'))");
//		q.setParameter("hcmPositionId", hcmPositionId);
//		q.setParameter("effectiveDate", effectiveDate);
//
//		@SuppressWarnings("rawtypes")
//		Iterator result = q.getResultList().iterator();
//
//		if (result.hasNext()) {
//			return ((BigDecimal)result.next()).intValue();
//		}
//
//		return 0;
//	}

	// Added for CC177246
	public void updateClusterRemark(int postUid, String clusterRef, String clusterRemark, UserPo user) {
		PostMasterPo po = getPostByPostUid(postUid);
		
		po.setClusterRef(clusterRef);
		po.setClusterRemark(clusterRemark);
		po.setUpdatedBy(user.getUserId());
		po.setUpdatedDate(new Date());
		po.setUpdatedRoleId(user.getCurrentRole());
		
		entityManager.merge(po);
	}
	
	// Added for CR0355
	public List<PostMasterPo> getMPRSPostWithoutSupplementPromotions(List<DataAccessPo> dataAccessList, MPRSSearchCriteria mprSearchRequest,
			String userId, String roleId) {
		List<PostMasterPo> resultList = new ArrayList<PostMasterPo>();

		String sql = "SELECT C.* FROM POST_MASTER C ";
		
		// if employee id is not search criteria, no need to join V_HCM_STRENGTH
		if (!"".equals(mprSearchRequest.getSearchEmployeeId()) && mprSearchRequest.getSearchEmployeeId() != null) {
			sql += " left join V_HCM_STRENGTH s on ( C.POST_ID = s.POST_ID "
					+ "and s.EMPLOYEE_NUMBER = :employeeId "
					+ "and trunc(sysdate) between s.EFFECTIVE_START_DATE and s.EFFECTIVE_END_DATE ) ";
		}
		
		sql += " where C.post_status = 'ACTIVE' ";
		sql += " and nvl(SUPP_PROMO_IND, 'N') <> 'Y' ";
		
		// cluster
		if (!"".equals(mprSearchRequest.getSearchClusterId())) {
			sql += "and C.cluster_code = :clusterCode ";
		}
		
		// inst
		if (!"".equals(mprSearchRequest.getSearchInstId())) {
			sql += "and C.inst_code = :instCode ";
		}
		
		// dept
		if (!"".equals(mprSearchRequest.getSearchDeptId())) {
			sql += "and C.DEPT_CODE = :deptCode ";
		}
		
		// staff group
		if (!"".equals(mprSearchRequest.getSearchStaffGroupId())) {
			sql += "and C.STAFF_GROUP_CODE = :staffGroupCode ";
		}
		
		// post rank
		if (!"".equals(mprSearchRequest.getSearchRankId())) {
			sql += "and C.RANK_CODE = :rankCode ";
		}
		
		// post id
		if (!"".equals(mprSearchRequest.getSearchPostId())) {
			sql += "and upper(C.POST_ID) like '%' || upper(:postId) || '%' ";
		}
		
		// employee id
		if (!"".equals(mprSearchRequest.getSearchEmployeeId()) && mprSearchRequest.getSearchEmployeeId() != null) {
			sql += " and s.EMPLOYEE_NUMBER = :employeeId ";
		}

		// Append the Data Access
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "C");
		if (!sqlDataAccess.isEmpty()) {
			sql += " and ( ";
			sql += sqlDataAccess;
			sql += " ) ";
		}

		sql += " order by C.POST_ID ";

		Query q = entityManager.createNativeQuery(sql, PostMasterPo.class);
		if (!"".equals(mprSearchRequest.getSearchClusterId())) {
			q.setParameter("clusterCode", mprSearchRequest.getSearchClusterId());
		}
		if (!"".equals(mprSearchRequest.getSearchInstId())) {
			q.setParameter("instCode", mprSearchRequest.getSearchInstId());
		}
		if (!"".equals(mprSearchRequest.getSearchDeptId())) {
			q.setParameter("deptCode", mprSearchRequest.getSearchDeptId());
		}
		if (!"".equals(mprSearchRequest.getSearchStaffGroupId())) {
			q.setParameter("staffGroupCode", mprSearchRequest.getSearchStaffGroupId());
		}
		if (!"".equals(mprSearchRequest.getSearchRankId())) {
			q.setParameter("rankCode", mprSearchRequest.getSearchRankId());
		}
		if (!"".equals(mprSearchRequest.getSearchPostId())) {
			q.setParameter("postId", mprSearchRequest.getSearchPostId().trim());
		}
		if (!"".equals(mprSearchRequest.getSearchEmployeeId()) && mprSearchRequest.getSearchEmployeeId() != null) {
			q.setParameter("employeeId", mprSearchRequest.getSearchEmployeeId());
		}

		for (int i = 0; i < dataAccessList.size(); i++) {
			DataAccessPo d = dataAccessList.get(i);
			if (d.getClusterCode() != null) {
				q.setParameter("cluster_code" + i, d.getClusterCode());
			}
			if (d.getInstCode() != null) {
				q.setParameter("inst_code" + i, d.getInstCode());
			}
			if (d.getDeptCode() != null) {
				q.setParameter("dept_code" + i, d.getDeptCode());
			}
			if (d.getStaffGroupCode() != null) {
				q.setParameter("staff_group_code" + i, d.getStaffGroupCode());
			}
		}
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			PostMasterPo c = (PostMasterPo) result.next();
			resultList.add(c);
		}

		return resultList;
	}
	
	public List<String> getShortFallList() {
		List<String> resultList = new ArrayList<String>();
		String sql = "select POST_ID from POST_MASTER where SHORTFALL_IND = 'Y' and POST_STATUS = 'ACTIVE' and REC_STATE = 'A' ";
		sql += " and POST_ID not in (select SHORTFALL_POST_ID from RQ_MASTER m, RQ_POST p where m.REQUEST_UID = p.REQUEST_UID and m.REQUEST_STATUS not in ('WITHDRAW') and SHORTFALL_POST_ID is not null) ";
		sql += " order by POST_ID";

		Query q = entityManager.createNativeQuery(sql);
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			resultList.add((String) result.next());
		}

		return resultList;
	}
}
