package hk.org.ha.eclaim.bs.batch.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.batch.dao.IRequestBatchDao;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.batch.po.BatchLogDtlPo;
import hk.org.ha.eclaim.bs.batch.po.BatchLogPo;
import hk.org.ha.eclaim.core.logger.EClaimLogger;

@Repository
public class RequestBatchDaoImpl implements IRequestBatchDao {
	
	private String EXECUTE_FAIL = "FAIL"; 
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public int supplePromotion() {
		String executeResult = "";
		
		try {
			StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("BATCH_JOB_PKG.update_supp_prom");
			storedProcedure.registerStoredProcedureParameter("in_user_id", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("in_role_id", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("exec_result", String.class, ParameterMode.OUT);
			
			storedProcedure.setParameter("in_user_id", "MPRHCMSUPPORT");
			storedProcedure.setParameter("in_role_id", "BATCH_JOB");
			storedProcedure.execute();
			
			// Added for CR0342
			executeResult = (String)storedProcedure.getOutputParameterValue("exec_result");
			if (EXECUTE_FAIL.equals(executeResult)) {
				return 1;
			}
		}
		catch (Exception ex) {
			EClaimLogger.error("supplePromotion(): " + ex.getMessage(), ex);
			return 1;
		}

		return 0;
	}
	
	public int updateEndDate() {
		String executeResult = "";
		
		try {
			StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("BATCH_JOB_PKG.update_limit_duration_end_date");
			storedProcedure.registerStoredProcedureParameter("in_user_id", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("in_role_id", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("exec_result", String.class, ParameterMode.OUT);
			
			storedProcedure.setParameter("in_user_id", "MPRHCMSUPPORT");
			storedProcedure.setParameter("in_role_id", "BATCH_JOB");
			storedProcedure.execute();
			
			// Added for CR0342
			executeResult = (String)storedProcedure.getOutputParameterValue("exec_result");
			if (EXECUTE_FAIL.equals(executeResult)) {
				return 1;
			}
		}
		catch (Exception ex) {
			EClaimLogger.error("updateEndDate()" + ex.getMessage(), ex);
			return 1;
		}

		return 0;
	}
	
	public int syncPost() {
		String executeResult = "";
		
		try {
			StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("BATCH_JOB_PKG.process_daily_status_change");
			storedProcedure.registerStoredProcedureParameter("in_user_id", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("in_role_id", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("exec_result", String.class, ParameterMode.OUT);
			
			storedProcedure.setParameter("in_user_id", "MPRHCMSUPPORT");
			storedProcedure.setParameter("in_role_id", "BATCH_JOB");
			storedProcedure.execute();
			
			// Added for CR0342
			executeResult = (String)storedProcedure.getOutputParameterValue("exec_result");
			if (EXECUTE_FAIL.equals(executeResult)) {
				return 1;
			}
		}
		catch (Exception ex) {
			EClaimLogger.error("newPost()" + ex.getMessage(), ex);
			return 1;
		}

		return 0;
	}
	
	public List<String> getRptBatchUserAccess(Date lastUpdateDtFrom) {
		
		List<String> resultList = new ArrayList<String>();
		
		try {
			
			// List of active HR FIN roles
			// For checking User who was removed a HR FIN role from Cluster, but still have other active HR FIN role of same Cluster
			StringBuilder sqlActiveHrFinRoleByCluster = new StringBuilder("select * from ss_user s1, ss_user_role r1, ss_user_role_data_access da1 ")
				.append("where r1.role_id in ('HR_OFFICER','HR_MANAGER','FIN_OFFICER','FIN_MANAGER') ")
				.append("and s1.user_id = s.user_id ")
				.append("and da1.cluster_code = da.cluster_code ")
				.append("and s1.account_status = 'A' ")
				.append("and s1.rec_state = 'A' ")
				.append("and r1.rec_state = 'A' ")
				.append("and da1.rec_state = 'A' ")
				.append("and s1.user_id = r1.user_id ")
				.append("and r1.user_role_uid = da1.user_role_uid ");
			
			// Last updated HR FIN roles
			StringBuilder sqlLastUpdate = new StringBuilder("select ")
					.append("  row_number() over (partition by s.user_id, da.cluster_code order by da.updated_date desc, r.updated_date desc, s.updated_date desc) rowNumber ")
					.append(", case when account_status = 'I' or s.rec_state = 'I' then 'REMOVE' ")
					.append("       when da.rec_state = 'A' then 'ADD' ")
					.append("       when exists ( ")
					.append(sqlActiveHrFinRoleByCluster)
					.append("            ) then 'ADD' ")
					.append("       else 'REMOVE' ")
					.append("  end action ")
					.append(", da.cluster_code ")
					.append(", s.user_id ")
					.append(", da.updated_date da_updated_date ")
					.append(", r.updated_date r_updated_date ")
					.append(", s.updated_date s_updated_date ")			
				.append("from ss_user s, ss_user_role r, ss_user_role_data_access da ")
				.append("where r.role_id in ('HR_OFFICER','HR_MANAGER','FIN_OFFICER','FIN_MANAGER') ")
				.append("and ( s.updated_date >= trunc(:lastUpdatedDtFrom) ")
				.append("		or r.updated_date >= trunc(:lastUpdatedDtFrom) ")
				.append("		or da.updated_date >= trunc(:lastUpdatedDtFrom) ) ")
				.append("and s.user_id = r.user_id ")
				.append("and r.user_role_uid = da.user_role_uid ");
			
			StringBuilder sql = new StringBuilder("select ")
					.append("  action ")
					.append(", cluster_code ")
					.append(", user_id ")
					.append("from ( ")
					.append(sqlLastUpdate)
					.append("	) where rowNumber = 1 ")
					.append("order by user_id, cluster_code, da_updated_date, r_updated_date, s_updated_date ");
					
			Query q = entityManager.createNativeQuery(sql.toString());
			q.setParameter("lastUpdatedDtFrom", lastUpdateDtFrom);
	
			@SuppressWarnings("rawtypes")
			Iterator result = q.getResultList().iterator();
	
			while (result.hasNext()) {
				Object[] obj = (Object[]) result.next();
				
				String lastUpdateStr = (String) obj[0] + " " + (String) obj[1] + " " + (String) obj[2];
				resultList.add(lastUpdateStr);
			}
	
			return resultList;
			
		} catch (Exception ex) {
			EClaimLogger.error("generateRptBatchAccessConfig()" + ex.getMessage(), ex);
			return null;
		}
	}
	
	public BatchLogPo createBatchLog(String batchName) {
		Date updateDate = new Date();
		BatchLogPo batchLogPo = new BatchLogPo();
		batchLogPo.setBatchName(batchName);
		batchLogPo.setStartTime(updateDate);
		batchLogPo.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
		batchLogPo.setCreatedBy("SYSTEM");
		batchLogPo.setCreatedRoleId("SYSTEM");
		batchLogPo.setCreatedDate(updateDate);
		batchLogPo.setUpdatedBy("SYSTEM");
		batchLogPo.setUpdatedRoleId("SYSTEM");
		batchLogPo.setUpdatedDate(updateDate);
		entityManager.persist(batchLogPo);
		
		return batchLogPo;
	}
	
	public int updateBatchLog(BatchLogPo batchLogPo, String batchResult) {
		Date updateDate = new Date();
		batchLogPo.setEndTime(updateDate);
		batchLogPo.setBatchResult(batchResult);
		batchLogPo.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
		batchLogPo.setUpdatedBy("SYSTEM");
		batchLogPo.setUpdatedRoleId("SYSTEM");
		batchLogPo.setUpdatedDate(updateDate);
		entityManager.merge(batchLogPo);
		
		return batchLogPo.getBatchUid();
	}
	
	public int createBatchLogDtl(Integer batchUid, String postId, String batchDtlResult) {
		Date updateDate = new Date();
		BatchLogDtlPo batchLogDtlPo = new BatchLogDtlPo();
		batchLogDtlPo.setBatchUid(batchUid);
		batchLogDtlPo.setPostId(postId);
		batchLogDtlPo.setBatchDtlResult(batchDtlResult);
		batchLogDtlPo.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
		batchLogDtlPo.setCreatedBy("SYSTEM");
		batchLogDtlPo.setCreatedRoleId("SYSTEM");
		batchLogDtlPo.setCreatedDate(updateDate);
		batchLogDtlPo.setUpdatedBy("SYSTEM");
		batchLogDtlPo.setUpdatedRoleId("SYSTEM");
		batchLogDtlPo.setUpdatedDate(updateDate);
		entityManager.persist(batchLogDtlPo);
		
		return batchLogDtlPo.getBatchUid();
	}
}