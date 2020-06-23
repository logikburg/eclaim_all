package hk.org.ha.eclaim.bs.report.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.report.dao.IPostExportDao;
import hk.org.ha.eclaim.bs.request.po.MPRSPostFundingSnapPo;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.report.helper.ReportSubqueryHelper;
import hk.org.ha.eclaim.bs.report.po.PostExportPo;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;

@Repository
public class PostExportDaoImpl implements IPostExportDao {

	@PersistenceContext
	private EntityManager entityManager;

	public List<PostExportPo> getDataExportContent(List<DataAccessPo> dataAccessList, Date asAtDate, Map<String, String> condCriteria) {
		System.out.println("Perform PostExportDaoImpl.query");
		
		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");
		
		// 1. query vacant posts + primary assignment of non vacant posts
		StringBuilder sqlVacantAndNonVacantPriPost = new StringBuilder("select ")
					.append("vpe.post_id")
					.append(", hcm_position_name")
					.append(", staff_group_name")
					.append(", cluster_code")
					.append(", inst_code")
				    .append(", dept_code")
				    .append(", sub_specialty_code")
				    .append(", sub_specialty_desc")
				    .append(", unit")
				    .append(", rank_name")
				    .append(", post_title")
				    .append(", approval_date")
				    .append(", approval_ref")
				    .append(", approval_remark")
				    .append(", post_duration_desc")
				    .append(", post_start_date")
				    .append(", limit_duration_no")
				    .append(", limit_duration_unit")
				    .append(", actual_start_date")
				    .append(", actual_end_date")
				    .append(", fixed_end_date")
				    .append(", limit_duration_end_date")
				    .append(", decode(a.fte,0,null,s.employee_number) employee_number")
				    .append(", decode(a.fte,0,null,a.full_name) employee_name")
				    .append(", decode(a.fte,0,null,a.rank) employee_rank")
				    .append(", a.employee_category")
				    .append(", post_fte")
				    .append(", nvl(a.fte,0) strength_fte")
				    .append(", (vpe.post_fte - nvl(a.fte,0)) vacancy_fte")
				    .append(", cluster_ref")
				    .append(", cluster_remark")
				    .append(", post_remark")
				    .append(", post_status_desc")
				    .append(", post_status_start_date")
				    .append(", post_status_end_date")
				    .append(", supp_promo_ind")
				    .append(", junior_post_id")
				    .append(", resources_support_fr_ext")
				    .append(", resources_support_remark ")
				    .append(", a.payzone, vpe.POST_SNAP_UID ")
				.append("from v_post_export vpe ")
				.append("left join v_hcm_strength s on ( vpe.post_id = s.post_id and trunc(:asAtDate) between s.effective_start_date and s.effective_end_date ) ")
				.append("left join v_hcm_assignment a on ( s.person_id = a.person_id and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date and a.primary_flag = 'Y' and assignment_status = 'Active Assignment' ) ")
				.append("where 1=1 ")
				.append("and trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ");
		
		// 2. query secondary assignment of non vacant posts
		StringBuilder sqlNonVacantSecPost = new StringBuilder("select ")
					    .append("'(' || vpe.post_id || ')' post_id")
						.append(", null hcm_position_name")
						.append(", null staff_group_name")
						.append(", null cluster_code")
						.append(", null inst_code")
					    .append(", null dept_code")
					    .append(", null sub_specialty_code")
					    .append(", null sub_specialty_desc")
					    .append(", null unit")
					    .append(", null rank_name")
					    .append(", null post_title")
					    .append(", null approval_date")
					    .append(", null approval_ref")
					    .append(", null approval_remark")
					    .append(", null post_duration_desc")
					    .append(", null post_start_date")
					    .append(", null limit_duration_no")
					    .append(", null limit_duration_unit")
					    .append(", null actual_start_date")
					    .append(", null actual_end_date")
					    .append(", null fixed_end_date")
					    .append(", null limit_duration_end_date")
					    .append(", s.employee_number")
					    .append(", a.full_name employee_name")
					    .append(", a.rank employee_rank")
					    .append(", a.employee_category")
					    .append(", 0 post_fte")
					    .append(", nvl(a.fte,0) strength_fte")
					    .append(", (0 - nvl(a.fte,0)) vacancy_fte")
					    .append(", null cluster_ref")
					    .append(", null cluster_remark")
					    .append(", null post_remark")
					    .append(", null post_status_desc")
					    .append(", null post_status_start_date")
					    .append(", null post_status_end_date")
					    .append(", null supp_promo_ind")
					    .append(", null junior_post_id")
					    .append(", null resources_support_fr_ext")
					    .append(", null resources_support_remark ")
					    .append(", a.payzone, vpe.POST_SNAP_UID ")
				    .append("from v_post_export vpe ")
				    	.append(", ( select post_id, person_id, employee_number from v_hcm_strength ")
						.append("where trunc(:asAtDate) between effective_start_date and effective_end_date ")
						.append(") s ")
						.append(", ( select * from v_hcm_assignment ")
						.append("where trunc(:asAtDate) between effective_start_date and effective_end_date ")
						.append("and primary_flag = 'N' ")
						.append("and assignment_status = 'Active Assignment' ")
						.append(") a ")
				    .append("where 1=1 ")
				    .append("and vpe.post_id = s.post_id ")
				    .append("and s.person_id = a.person_id ")
				    .append("and trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ")
				    ;
		
		// 4. append data access scope
		if(!sqlDataAccess.isEmpty()) {
			sqlVacantAndNonVacantPriPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
			sqlNonVacantSecPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}
		
		// 5. append report criteria input by user
		@SuppressWarnings("rawtypes")
		Iterator criteria = condCriteria.keySet().iterator();
		while (criteria.hasNext()) {
			String criteriaKey = (String) criteria.next();
			sqlVacantAndNonVacantPriPost.append(" and vpe.").append(criteriaKey).append(" = :").append(criteriaKey);
			sqlNonVacantSecPost.append(" and vpe.").append(criteriaKey).append(" = :").append(criteriaKey);
		}
		
		// 6. union all vacant and non vacant posts
		StringBuilder sql = new StringBuilder("select ")
					.append("post_id")
					.append(", hcm_position_name")
					.append(", staff_group_name")
					.append(", cluster_code")
					.append(", inst_code")
				    .append(", dept_code")
				    .append(", sub_specialty_code")
				    .append(", sub_specialty_desc")
				    .append(", unit")
				    .append(", rank_name")
				    .append(", post_title")
				    .append(", approval_date")
				    .append(", approval_ref")
				    .append(", approval_remark")
				    .append(", post_duration_desc")
				    .append(", post_start_date")
				    .append(", limit_duration_no")
				    .append(", limit_duration_unit")
				    .append(", actual_start_date")
				    .append(", actual_end_date")
				    .append(", fixed_end_date")
				    .append(", limit_duration_end_date")
				    .append(", employee_number")
				    .append(", employee_name")
				    .append(", employee_rank")
				    .append(", employee_category")
				    .append(", post_fte")
				    .append(", strength_fte")
				    .append(", vacancy_fte")
				    .append(", cluster_ref")
				    .append(", cluster_remark")
				    .append(", post_remark")
				    .append(", post_status_desc")
				    .append(", post_status_start_date")
				    .append(", post_status_end_date")
				    .append(", supp_promo_ind")
				    .append(", junior_post_id")
				    .append(", resources_support_fr_ext")
				    .append(", resources_support_remark ")
				    .append(", payzone, POST_SNAP_UID ")
			    .append("from ( ")
			    	.append(sqlVacantAndNonVacantPriPost)
			    	.append(" union all ")
			    	.append(sqlNonVacantSecPost)
			    .append(") rpt ")
			    .append("order by cluster_code, inst_code, dept_code, rank_name");
		
		// Set parameters to query
		System.out.println("sqlN: " + sql.toString());
		Query q = entityManager.createNativeQuery(sql.toString());
		q.setParameter("asAtDate", asAtDate);
		for (String condKey : condCriteria.keySet()) {
			q.setParameter(condKey, condCriteria.get(condKey));
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
		
		List<PostExportPo> resultList = new ArrayList<PostExportPo>();
		while (result.hasNext()) {
			Object[] obj = (Object[]) result.next();
			
			PostExportPo post = new PostExportPo();
			post.setPostId((String) obj[0]);
			post.setHcmPositionName((String) obj[1]);
			post.setStaffGroupName((String) obj[2]);
			post.setClusterCode((String) obj[3]);
			post.setInstCode((String) obj[4]);
			post.setDeptCode((String) obj[5]);
			post.setSubSpecialtyCode((String) obj[6]);
			post.setSubSpecialtyDesc((String) obj[7]);
			post.setUnit((String) obj[8]);
		    post.setRankName((String) obj[9]);
		    post.setPostTitle((String) obj[10]);
		    post.setApprovalDate((Date) obj[11]);
		    post.setApprovalRef((String) obj[12]);
		    post.setApprovalRemark((String) obj[13]);
		    post.setPostDuration((String) obj[14]);
		    post.setPostStartDate((Date) obj[15]);
		    post.setLimitDurationNo(obj[16] == null ? null : ((BigDecimal) obj[16]).intValue());
		    post.setLimitDurationUnit((String) obj[17]);
		    post.setActualStartDate((Date) obj[18]);
		    post.setActualEndDate((Date) obj[19]);
		    post.setFixedEndDate((Date) obj[20]);
		    post.setLimitDurationEndDate((Date) obj[21]);
		    post.setEmployeeNumber((String) obj[22]);
		    post.setEmployeeName((String) obj[23]);
		    post.setEmployeeRank((String) obj[24]);
		    post.setEmployeeCategory((String) obj[25]);
		    post.setPostFTE(obj[26] == null ? null : ((BigDecimal) obj[26]).doubleValue());
		    post.setStrengthFTE(obj[27] == null ? null : ((BigDecimal) obj[27]).doubleValue());
		    post.setVacancyFTE(obj[28] == null ? null : ((BigDecimal) obj[28]).doubleValue());
		    post.setClusterRef((String) obj[29]);
		    post.setClusterRemark((String) obj[30]);
		    post.setPostRemark((String) obj[31]);
		    post.setPostStatusDesc((String) obj[32]);
		    post.setPostStatusStartDate((Date) obj[33]);
		    post.setPostStatusEndDate((Date) obj[34]);
		    post.setSuppPromoInd((String) obj[35]);
		    post.setJuniorPostId((String) obj[36]);
		    post.setResourcesSupportFrExt((String) obj[37]);
		    post.setResourcesSupportRemark((String) obj[38]);
			post.setPayzone((String)obj[39]);
			post.setPostSnapUid(obj[40] == null ? null : ((BigDecimal) obj[40]).intValue());
			resultList.add(post);
		}

		return resultList;
	}

	// Added for UT30067
	public List<PostExportPo> getDataExportContentByPostId(List<DataAccessPo> dataAccessList, String postId) {
		System.out.println("Perform PostExportDaoImpl.getDataExportContentByPostId");
		
		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");
		
		// 1. query vacant posts
		StringBuilder sqlVacantPost = new StringBuilder("select ")
					.append("vpe.post_id")
					.append(", hcm_position_name")
					.append(", staff_group_name")
					.append(", cluster_code")
					.append(", inst_code")
				    .append(", dept_code")
				    .append(", sub_specialty_code")
				    .append(", sub_specialty_desc")
				    .append(", unit")
				    .append(", rank_name")
				    .append(", post_title")
				    .append(", approval_date")
				    .append(", approval_ref")
				    .append(", approval_remark")
				    .append(", post_duration_desc")
				    .append(", post_start_date")
				    .append(", limit_duration_no")
				    .append(", limit_duration_unit")
				    .append(", actual_start_date")
				    .append(", actual_end_date")
				    .append(", fixed_end_date")
				    .append(", limit_duration_end_date")
				    .append(", null employee_number")
				    .append(", null employee_name")
				    .append(", null employee_rank")
				    .append(", null employee_category")
				    .append(", post_fte")
				    .append(", 0 strength_fte")
				    .append(", cast(vpe.post_fte as number) vacancy_fte")
				    .append(", cluster_ref")
				    .append(", cluster_remark")
				    .append(", post_remark")
				    .append(", post_status_desc")
				    .append(", post_status_start_date")
				    .append(", post_status_end_date")
				    .append(", supp_promo_ind")
				    .append(", junior_post_id")
				    .append(", resources_support_fr_ext")
				    .append(", resources_support_remark, request_type, vpe.effective_start_date ")
				    .append(", null payzone, vpe.POST_SNAP_UID ")
				.append("from v_trans_export vpe")
					.append(", ( select post_id, person_id, employee_number from v_hcm_strength ")
					.append(") s ")
				.append("where 1=1 ")
				.append("and vpe.post_id = s.post_id (+) ")
				.append("and s.person_id is null ");
		
		// 2. query non vacant primary posts
		StringBuilder sqlNonVacantPriPost = new StringBuilder("select ")
					    .append("vpe.post_id")
						.append(", hcm_position_name")
						.append(", staff_group_name")
						.append(", cluster_code")
						.append(", inst_code")
					    .append(", dept_code")
					    .append(", sub_specialty_code")
					    .append(", sub_specialty_desc")
					    .append(", unit")
					    .append(", rank_name")
					    .append(", post_title")
					    .append(", approval_date")
					    .append(", approval_ref")
					    .append(", approval_remark")
					    .append(", post_duration_desc")
					    .append(", post_start_date")
					    .append(", limit_duration_no")
					    .append(", limit_duration_unit")
					    .append(", actual_start_date")
					    .append(", actual_end_date")
					    .append(", fixed_end_date")
					    .append(", limit_duration_end_date")
					    .append(", decode(a.fte,0,null,s.employee_number) employee_number")
					    .append(", decode(a.fte,0,null,a.full_name) employee_name")
					    .append(", decode(a.fte,0,null,a.rank) employee_rank")
					    .append(", a.employee_category")
					    .append(", post_fte")
					    .append(", a.fte strength_fte")
					    .append(", cast(decode(a.fte,null,null,vpe.post_fte - a.fte) as number) vacancy_fte")
					    .append(", cluster_ref")
					    .append(", cluster_remark")
					    .append(", post_remark")
					    .append(", post_status_desc")
					    .append(", post_status_start_date")
					    .append(", post_status_end_date")
					    .append(", supp_promo_ind")
					    .append(", junior_post_id")
					    .append(", resources_support_fr_ext")
					    .append(", resources_support_remark, request_type, vpe.effective_start_date ")
					    .append(", a.payzone, vpe.POST_SNAP_UID ")
				    .append(" from v_trans_export vpe ")
				    .append(" inner join ( select post_id, person_id, employee_number from v_hcm_strength ")
						.append(") s ")
						.append(" on vpe.post_id = s.post_id ")	
				    .append(" left join ( select * from v_hcm_assignment ")
						.append("where primary_flag = 'Y' ")
						.append("and assignment_status = 'Active Assignment' ")
						.append(") a ")
						.append("on s.person_id = a.person_id  ")
						.append("and trunc(vpe.effective_start_date) between trunc(a.EFFECTIVE_START_DATE) and trunc(a.EFFECTIVE_END_DATE) ")
					.append("where 1=1 ")
				    ;
		
		// 3. query non vacant secondary posts
		StringBuilder sqlNonVacantSecPost = new StringBuilder("select ")
					    .append("'(' || vpe.post_id || ')' post_id")
						.append(", null hcm_position_name")
						.append(", null staff_group_name")
						.append(", null cluster_code")
						.append(", null inst_code")
					    .append(", null dept_code")
					    .append(", null sub_specialty_code")
					    .append(", null sub_specialty_desc")
					    .append(", null unit")
					    .append(", null rank_name")
					    .append(", null post_title")
					    .append(", null approval_date")
					    .append(", null approval_ref")
					    .append(", null approval_remark")
					    .append(", null post_duration_desc")
					    .append(", null post_start_date")
					    .append(", null limit_duration_no")
					    .append(", null limit_duration_unit")
					    .append(", null actual_start_date")
					    .append(", null actual_end_date")
					    .append(", null fixed_end_date")
					    .append(", null limit_duration_end_date")
					    .append(", s.employee_number")
					    .append(", a.full_name employee_name")
					    .append(", a.rank employee_rank")
					    .append(", a.employee_category")
					    .append(", 0 post_fte")
					    .append(", a.fte strength_fte")
					    .append(", cast(decode(a.fte,null,null, 0 - a.fte) as number) vacancy_fte")
					    .append(", null cluster_ref")
					    .append(", null cluster_remark")
					    .append(", null post_remark")
					    .append(", null post_status_desc")
					    .append(", null post_status_start_date")
					    .append(", null post_status_end_date")
					    .append(", null supp_promo_ind")
					    .append(", null junior_post_id")
					    .append(", null resources_support_fr_ext")
					    .append(", null resources_support_remark, null request_type, vpe.effective_start_date ")
					    .append(", a.payzone, vpe.POST_SNAP_UID ")
				    .append("from v_trans_export vpe")
				    	.append(", ( select post_id, person_id, employee_number from v_hcm_strength ")
						.append(") s ")
						.append(", ( select * from v_hcm_assignment ")
						.append("where primary_flag = 'N' ")
						.append("and assignment_status = 'Active Assignment' ")
						.append(") a ")
				    .append("where 1=1 ")
				    .append("and vpe.post_id = s.post_id ")
				    .append("and s.person_id = a.person_id ")
				    .append("and trunc(vpe.effective_start_date) between trunc(a.EFFECTIVE_START_DATE) and trunc(a.EFFECTIVE_END_DATE) ");
				    ;
		
		// 4. append data access scope
		if(!sqlDataAccess.isEmpty()) {
			sqlVacantPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
			sqlNonVacantPriPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
			sqlNonVacantSecPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}
		
		// 5. append report criteria input by user
		sqlVacantPost.append(" and vpe.post_id ").append(" = :postId ");
		sqlNonVacantPriPost.append(" and vpe.post_id ").append(" = :postId ");
		sqlNonVacantSecPost.append(" and vpe.post_id ").append(" = :postId ");
		
		// 6. union all vacant and non vacant posts
		StringBuilder sql = new StringBuilder("select ")
					.append("post_id")
					.append(", hcm_position_name")
					.append(", staff_group_name")
					.append(", cluster_code")
					.append(", inst_code")
				    .append(", dept_code")
				    .append(", sub_specialty_code")
				    .append(", sub_specialty_desc")
				    .append(", unit")
				    .append(", rank_name")
				    .append(", post_title")
				    .append(", approval_date")
				    .append(", approval_ref")
				    .append(", approval_remark")
				    .append(", post_duration_desc")
				    .append(", post_start_date")
				    .append(", limit_duration_no")
				    .append(", limit_duration_unit")
				    .append(", actual_start_date")
				    .append(", actual_end_date")
				    .append(", fixed_end_date")
				    .append(", limit_duration_end_date")
				    .append(", employee_number")
				    .append(", employee_name")
				    .append(", employee_rank")
				    .append(", employee_category")
				    .append(", post_fte")
				    .append(", strength_fte")
				    .append(", vacancy_fte")
				    .append(", cluster_ref")
				    .append(", cluster_remark")
				    .append(", post_remark")
				    .append(", post_status_desc")
				    .append(", post_status_start_date")
				    .append(", post_status_end_date")
				    .append(", supp_promo_ind")
				    .append(", junior_post_id")
				    .append(", resources_support_fr_ext")
				    .append(", resources_support_remark ")
				    .append(", request_type, effective_start_date ")
				    .append(", payzone, POST_SNAP_UID ")
			    .append("from ( ")
			    	.append(sqlVacantPost)
			    	.append(" union all ")
			    	.append(sqlNonVacantPriPost)
			    	.append(" union all ")
			    	.append(sqlNonVacantSecPost)
			    .append(") rpt ")
			    .append("order by effective_start_date, cluster_code, inst_code, dept_code, rank_name");
		
		// Set parameters to query
		System.out.println("sqlN: " + sql.toString());
		Query q = entityManager.createNativeQuery(sql.toString());
//		q.setParameter("asAtDate", asAtDate);
//		for (String condKey : condCriteria.keySet()) {
//			q.setParameter(condKey, condCriteria.get(condKey));
//		}
		
		q.setParameter("postId", postId);
		
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
		
		List<PostExportPo> resultList = new ArrayList<PostExportPo>();
		while (result.hasNext()) {
			Object[] obj = (Object[]) result.next();
			
			PostExportPo post = new PostExportPo();
			post.setPostId((String) obj[0]);
			post.setHcmPositionName((String) obj[1]);
			post.setStaffGroupName((String) obj[2]);
			post.setClusterCode((String) obj[3]);
			post.setInstCode((String) obj[4]);
			post.setDeptCode((String) obj[5]);
			post.setSubSpecialtyCode((String) obj[6]);
			post.setSubSpecialtyDesc((String) obj[7]);
			post.setUnit((String) obj[8]);
		    post.setRankName((String) obj[9]);
		    post.setPostTitle((String) obj[10]);
		    post.setApprovalDate((Date) obj[11]);
		    post.setApprovalRef((String) obj[12]);
		    post.setApprovalRemark((String) obj[13]);
		    post.setPostDuration((String) obj[14]);
		    post.setPostStartDate((Date) obj[15]);
		    post.setLimitDurationNo(obj[16] == null ? null : ((BigDecimal) obj[16]).intValue());
		    post.setLimitDurationUnit((String) obj[17]);
		    post.setActualStartDate((Date) obj[18]);
		    post.setActualEndDate((Date) obj[19]);
		    post.setFixedEndDate((Date) obj[20]);
		    post.setLimitDurationEndDate((Date) obj[21]);
		    post.setEmployeeNumber((String) obj[22]);
		    post.setEmployeeName((String) obj[23]);
		    post.setEmployeeRank((String) obj[24]);
		    post.setEmployeeCategory((String) obj[25]);
		    post.setPostFTE(obj[26] == null ? null : ((BigDecimal) obj[26]).doubleValue());
		    post.setStrengthFTE(obj[27] == null ? null : ((BigDecimal) obj[27]).doubleValue());
		    post.setVacancyFTE(obj[28] == null ? null : ((BigDecimal) obj[28]).doubleValue());
		    post.setClusterRef((String) obj[29]);
		    post.setClusterRemark((String) obj[30]);
		    post.setPostRemark((String) obj[31]);
		    post.setPostStatusDesc((String) obj[32]);
		    post.setPostStatusStartDate((Date) obj[33]);
		    post.setPostStatusEndDate((Date) obj[34]);
		    post.setSuppPromoInd((String) obj[35]);
		    post.setJuniorPostId((String) obj[36]);
		    post.setResourcesSupportFrExt((String) obj[37]);
		    post.setResourcesSupportRemark((String) obj[38]);
		    post.setRequestType((String) obj[39]);
		    post.setEffectiveStartDate((Date) obj[40]);
		    post.setPayzone((String)obj[41]);
		    post.setPostSnapUid(obj[42] == null ? null : ((BigDecimal) obj[42]).intValue());
			resultList.add(post);
		}

		return resultList;
	}

	public int getMaxNumberFundingSrc(List<DataAccessPo> dataAccessList, Date asAtDate, Map<String, String> condCriteria) {
		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");

		// 1. query vacant posts + primary assignment of non vacant posts
		StringBuilder sqlVacantAndNonVacantPriPost = new StringBuilder("select ")
				.append("vpe.post_id")
				.append(", hcm_position_name")
				.append(", staff_group_name")
				.append(", cluster_code")
				.append(", inst_code")
				.append(", dept_code")
				.append(", sub_specialty_code")
				.append(", sub_specialty_desc")
				.append(", unit")
				.append(", rank_name")
				.append(", post_title")
				.append(", approval_date")
				.append(", approval_ref")
				.append(", approval_remark")
				.append(", post_duration_desc")
				.append(", post_start_date")
				.append(", limit_duration_no")
				.append(", limit_duration_unit")
				.append(", actual_start_date")
				.append(", actual_end_date")
				.append(", fixed_end_date")
				.append(", limit_duration_end_date")
				.append(", decode(a.fte,0,null,s.employee_number) employee_number")
				.append(", decode(a.fte,0,null,a.full_name) employee_name")
				.append(", decode(a.fte,0,null,a.rank) employee_rank")
				.append(", a.employee_category")
				.append(", post_fte")
				.append(", nvl(a.fte,0) strength_fte")
				.append(", (vpe.post_fte - nvl(a.fte,0)) vacancy_fte")
				.append(", cluster_ref")
				.append(", cluster_remark")
				.append(", post_remark")
				.append(", post_status_desc")
				.append(", post_status_start_date")
				.append(", post_status_end_date")
				.append(", supp_promo_ind")
				.append(", junior_post_id")
				.append(", resources_support_fr_ext")
				.append(", resources_support_remark ")
				.append(", a.payzone, vpe.POST_SNAP_UID ")
				.append("from v_post_export vpe ")
				.append("left join v_hcm_strength s on ( vpe.post_id = s.post_id and trunc(:asAtDate) between s.effective_start_date and s.effective_end_date ) ")
				.append("left join v_hcm_assignment a on ( s.person_id = a.person_id and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date and a.primary_flag = 'Y' and assignment_status = 'Active Assignment' ) ")
				.append("where 1=1 ")
				.append("and trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ");

		// 2. query secondary assignment of non vacant posts
		StringBuilder sqlNonVacantSecPost = new StringBuilder("select ")
				.append("'(' || vpe.post_id || ')' post_id")
				.append(", null hcm_position_name")
				.append(", null staff_group_name")
				.append(", null cluster_code")
				.append(", null inst_code")
				.append(", null dept_code")
				.append(", null sub_specialty_code")
				.append(", null sub_specialty_desc")
				.append(", null unit")
				.append(", null rank_name")
				.append(", null post_title")
				.append(", null approval_date")
				.append(", null approval_ref")
				.append(", null approval_remark")
				.append(", null post_duration_desc")
				.append(", null post_start_date")
				.append(", null limit_duration_no")
				.append(", null limit_duration_unit")
				.append(", null actual_start_date")
				.append(", null actual_end_date")
				.append(", null fixed_end_date")
				.append(", null limit_duration_end_date")
				.append(", s.employee_number")
				.append(", a.full_name employee_name")
				.append(", a.rank employee_rank")
				.append(", a.employee_category")
				.append(", 0 post_fte")
				.append(", nvl(a.fte,0) strength_fte")
				.append(", (0 - nvl(a.fte,0)) vacancy_fte")
				.append(", null cluster_ref")
				.append(", null cluster_remark")
				.append(", null post_remark")
				.append(", null post_status_desc")
				.append(", null post_status_start_date")
				.append(", null post_status_end_date")
				.append(", null supp_promo_ind")
				.append(", null junior_post_id")
				.append(", null resources_support_fr_ext")
				.append(", null resources_support_remark ")
				.append(", a.payzone, vpe.POST_SNAP_UID ")
				.append("from v_post_export vpe ")
				.append(", ( select post_id, person_id, employee_number from v_hcm_strength ")
				.append("where trunc(:asAtDate) between effective_start_date and effective_end_date ")
				.append(") s ")
				.append(", ( select * from v_hcm_assignment ")
				.append("where trunc(:asAtDate) between effective_start_date and effective_end_date ")
				.append("and primary_flag = 'N' ")
				.append("and assignment_status = 'Active Assignment' ")
				.append(") a ")
				.append("where 1=1 ")
				.append("and vpe.post_id = s.post_id ")
				.append("and s.person_id = a.person_id ")
				.append("and trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ")
				;

		// 4. append data access scope
		if(!sqlDataAccess.isEmpty()) {
			sqlVacantAndNonVacantPriPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
			sqlNonVacantSecPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}

		// 5. append report criteria input by user
		@SuppressWarnings("rawtypes")
		Iterator criteria = condCriteria.keySet().iterator();
		while (criteria.hasNext()) {
			String criteriaKey = (String) criteria.next();
			sqlVacantAndNonVacantPriPost.append(" and vpe.").append(criteriaKey).append(" = :").append(criteriaKey);
			sqlNonVacantSecPost.append(" and vpe.").append(criteriaKey).append(" = :").append(criteriaKey);
		}

		// 6. union all vacant and non vacant posts
		StringBuilder sql = new StringBuilder("select ")
				.append(" POST_SNAP_UID ")
				.append("from ( ")
				.append(sqlVacantAndNonVacantPriPost)
				.append(" union all ")
				.append(sqlNonVacantSecPost)
				.append(") rpt ");

		String sqlFinal = " select max(FUNDING_SEQ_NO) from POST_FUNDING_SNAP where POST_SNAP_UID in (";
		sqlFinal += sql.toString();
		sqlFinal += ")";
		
		Query q = entityManager.createNativeQuery(sqlFinal);
		q.setParameter("asAtDate", asAtDate);
		for (String condKey : condCriteria.keySet()) {
			q.setParameter(condKey, condCriteria.get(condKey));
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
		
		if (result.hasNext()) {
			return ((BigDecimal)result.next()).intValue();
		}
		
		return 0;
		
	}

	public List<MPRSPostFundingSnapPo> getPostFundingSnap(List<DataAccessPo> dataAccessList, Date asAtDate, Map<String, String> condCriteria, int seqNo) {
		List<MPRSPostFundingSnapPo> resultList = new ArrayList<MPRSPostFundingSnapPo>();

		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");

		// 1. query vacant posts + primary assignment of non vacant posts
		StringBuilder sqlVacantAndNonVacantPriPost = new StringBuilder("select ")
				.append("vpe.post_id")
				.append(", hcm_position_name")
				.append(", staff_group_name")
				.append(", cluster_code")
				.append(", inst_code")
				.append(", dept_code")
				.append(", sub_specialty_code")
				.append(", sub_specialty_desc")
				.append(", unit")
				.append(", rank_name")
				.append(", post_title")
				.append(", approval_date")
				.append(", approval_ref")
				.append(", approval_remark")
				.append(", post_duration_desc")
				.append(", post_start_date")
				.append(", limit_duration_no")
				.append(", limit_duration_unit")
				.append(", actual_start_date")
				.append(", actual_end_date")
				.append(", fixed_end_date")
				.append(", limit_duration_end_date")
				.append(", decode(a.fte,0,null,s.employee_number) employee_number")
				.append(", decode(a.fte,0,null,a.full_name) employee_name")
				.append(", decode(a.fte,0,null,a.rank) employee_rank")
				.append(", a.employee_category")
				.append(", post_fte")
				.append(", nvl(a.fte,0) strength_fte")
				.append(", (vpe.post_fte - nvl(a.fte,0)) vacancy_fte")
				.append(", cluster_ref")
				.append(", cluster_remark")
				.append(", post_remark")
				.append(", post_status_desc")
				.append(", post_status_start_date")
				.append(", post_status_end_date")
				.append(", supp_promo_ind")
				.append(", junior_post_id")
				.append(", resources_support_fr_ext")
				.append(", resources_support_remark ")
				.append(", a.payzone, vpe.POST_SNAP_UID ")
				.append("from v_post_export vpe ")
				.append("left join v_hcm_strength s on ( vpe.post_id = s.post_id and trunc(:asAtDate) between s.effective_start_date and s.effective_end_date ) ")
				.append("left join v_hcm_assignment a on ( s.person_id = a.person_id and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date and a.primary_flag = 'Y' and assignment_status = 'Active Assignment' ) ")
				.append("where 1=1 ")
				.append("and trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ");

		// 2. query secondary assignment of non vacant posts
		StringBuilder sqlNonVacantSecPost = new StringBuilder("select ")
				.append("'(' || vpe.post_id || ')' post_id")
				.append(", null hcm_position_name")
				.append(", null staff_group_name")
				.append(", null cluster_code")
				.append(", null inst_code")
				.append(", null dept_code")
				.append(", null sub_specialty_code")
				.append(", null sub_specialty_desc")
				.append(", null unit")
				.append(", null rank_name")
				.append(", null post_title")
				.append(", null approval_date")
				.append(", null approval_ref")
				.append(", null approval_remark")
				.append(", null post_duration_desc")
				.append(", null post_start_date")
				.append(", null limit_duration_no")
				.append(", null limit_duration_unit")
				.append(", null actual_start_date")
				.append(", null actual_end_date")
				.append(", null fixed_end_date")
				.append(", null limit_duration_end_date")
				.append(", s.employee_number")
				.append(", a.full_name employee_name")
				.append(", a.rank employee_rank")
				.append(", a.employee_category")
				.append(", 0 post_fte")
				.append(", nvl(a.fte,0) strength_fte")
				.append(", (0 - nvl(a.fte,0)) vacancy_fte")
				.append(", null cluster_ref")
				.append(", null cluster_remark")
				.append(", null post_remark")
				.append(", null post_status_desc")
				.append(", null post_status_start_date")
				.append(", null post_status_end_date")
				.append(", null supp_promo_ind")
				.append(", null junior_post_id")
				.append(", null resources_support_fr_ext")
				.append(", null resources_support_remark ")
				.append(", a.payzone, vpe.POST_SNAP_UID ")
				.append("from v_post_export vpe ")
				.append(", ( select post_id, person_id, employee_number from v_hcm_strength ")
				.append("where trunc(:asAtDate) between effective_start_date and effective_end_date ")
				.append(") s ")
				.append(", ( select * from v_hcm_assignment ")
				.append("where trunc(:asAtDate) between effective_start_date and effective_end_date ")
				.append("and primary_flag = 'N' ")
				.append("and assignment_status = 'Active Assignment' ")
				.append(") a ")
				.append("where 1=1 ")
				.append("and vpe.post_id = s.post_id ")
				.append("and s.person_id = a.person_id ")
				.append("and trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ")
				;

		// 4. append data access scope
		if(!sqlDataAccess.isEmpty()) {
			sqlVacantAndNonVacantPriPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
			sqlNonVacantSecPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}

		// 5. append report criteria input by user
		@SuppressWarnings("rawtypes")
		Iterator criteria = condCriteria.keySet().iterator();
		while (criteria.hasNext()) {
			String criteriaKey = (String) criteria.next();
			sqlVacantAndNonVacantPriPost.append(" and vpe.").append(criteriaKey).append(" = :").append(criteriaKey);
			sqlNonVacantSecPost.append(" and vpe.").append(criteriaKey).append(" = :").append(criteriaKey);
		}

		// 6. union all vacant and non vacant posts
		StringBuilder sql = new StringBuilder("select ")
				.append(" POST_SNAP_UID ")
				.append("from ( ")
				.append(sqlVacantAndNonVacantPriPost)
				.append(" union all ")
				.append(sqlNonVacantSecPost)
				.append(") rpt ");

		String sqlFinal = "SELECT C.* FROM POST_FUNDING_SNAP C where C.FUNDING_SEQ_NO = :fundingSeqNo and C.REC_STATE = :recState ";
		sqlFinal += " and POST_SNAP_UID in (";
		sqlFinal += sql.toString();
		sqlFinal += ")";
		
		Query q = entityManager.createNativeQuery(sqlFinal, MPRSPostFundingSnapPo.class);
		q.setParameter("asAtDate", asAtDate);
		q.setParameter("fundingSeqNo", seqNo);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
		for (String condKey : condCriteria.keySet()) {
			q.setParameter(condKey, condCriteria.get(condKey));
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
			MPRSPostFundingSnapPo c = (MPRSPostFundingSnapPo)result.next();
			resultList.add(c);
			
		}
		
		return resultList;
	}
	
	public int getMaxNumberFundingSrcByPostId(List<DataAccessPo> dataAccessList, String postId) {
		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");

		// 1. query vacant posts
		StringBuilder sqlVacantPost = new StringBuilder("select ")
				.append("vpe.post_id")
				.append(", hcm_position_name")
				.append(", staff_group_name")
				.append(", cluster_code")
				.append(", inst_code")
				.append(", dept_code")
				.append(", sub_specialty_code")
				.append(", sub_specialty_desc")
				.append(", unit")
				.append(", rank_name")
				.append(", post_title")
				.append(", approval_date")
				.append(", approval_ref")
				.append(", approval_remark")
				.append(", post_duration_desc")
				.append(", post_start_date")
				.append(", limit_duration_no")
				.append(", limit_duration_unit")
				.append(", actual_start_date")
				.append(", actual_end_date")
				.append(", fixed_end_date")
				.append(", limit_duration_end_date")
				.append(", null employee_number")
				.append(", null employee_name")
				.append(", null employee_rank")
				.append(", null employee_category")
				.append(", post_fte")
				.append(", 0 strength_fte")
				.append(", cast(vpe.post_fte as number) vacancy_fte")
				.append(", cluster_ref")
				.append(", cluster_remark")
				.append(", post_remark")
				.append(", post_status_desc")
				.append(", post_status_start_date")
				.append(", post_status_end_date")
				.append(", supp_promo_ind")
				.append(", junior_post_id")
				.append(", resources_support_fr_ext")
				.append(", resources_support_remark, request_type, vpe.effective_start_date ")
				.append(", null payzone, vpe.POST_SNAP_UID ")
				.append("from v_trans_export vpe")
				.append(", ( select post_id, person_id, employee_number from v_hcm_strength ")
				.append(") s ")
				.append("where 1=1 ")
				.append("and vpe.post_id = s.post_id (+) ")
				.append("and s.person_id is null ");

		// 2. query non vacant primary posts
		StringBuilder sqlNonVacantPriPost = new StringBuilder("select ")
				.append("vpe.post_id")
				.append(", hcm_position_name")
				.append(", staff_group_name")
				.append(", cluster_code")
				.append(", inst_code")
				.append(", dept_code")
				.append(", sub_specialty_code")
				.append(", sub_specialty_desc")
				.append(", unit")
				.append(", rank_name")
				.append(", post_title")
				.append(", approval_date")
				.append(", approval_ref")
				.append(", approval_remark")
				.append(", post_duration_desc")
				.append(", post_start_date")
				.append(", limit_duration_no")
				.append(", limit_duration_unit")
				.append(", actual_start_date")
				.append(", actual_end_date")
				.append(", fixed_end_date")
				.append(", limit_duration_end_date")
				.append(", decode(a.fte,0,null,s.employee_number) employee_number")
				.append(", decode(a.fte,0,null,a.full_name) employee_name")
				.append(", decode(a.fte,0,null,a.rank) employee_rank")
				.append(", a.employee_category")
				.append(", post_fte")
				.append(", a.fte strength_fte")
				.append(", cast(decode(a.fte,null,null,vpe.post_fte - a.fte) as number) vacancy_fte")
				.append(", cluster_ref")
				.append(", cluster_remark")
				.append(", post_remark")
				.append(", post_status_desc")
				.append(", post_status_start_date")
				.append(", post_status_end_date")
				.append(", supp_promo_ind")
				.append(", junior_post_id")
				.append(", resources_support_fr_ext")
				.append(", resources_support_remark, request_type, vpe.effective_start_date ")
				.append(", a.payzone, vpe.POST_SNAP_UID ")
				.append(" from v_trans_export vpe ")
				.append(" inner join ( select post_id, person_id, employee_number from v_hcm_strength ")
				.append(") s ")
				.append(" on vpe.post_id = s.post_id ")	
				.append(" left join ( select * from v_hcm_assignment ")
				.append("where primary_flag = 'Y' ")
				.append("and assignment_status = 'Active Assignment' ")
				.append(") a ")
				.append("on s.person_id = a.person_id  ")
				.append("and trunc(vpe.effective_start_date) between trunc(a.EFFECTIVE_START_DATE) and trunc(a.EFFECTIVE_END_DATE) ")
				.append("where 1=1 ")
				;

		// 3. query non vacant secondary posts
		StringBuilder sqlNonVacantSecPost = new StringBuilder("select ")
				.append("'(' || vpe.post_id || ')' post_id")
				.append(", null hcm_position_name")
				.append(", null staff_group_name")
				.append(", null cluster_code")
				.append(", null inst_code")
				.append(", null dept_code")
				.append(", null sub_specialty_code")
				.append(", null sub_specialty_desc")
				.append(", null unit")
				.append(", null rank_name")
				.append(", null post_title")
				.append(", null approval_date")
				.append(", null approval_ref")
				.append(", null approval_remark")
				.append(", null post_duration_desc")
				.append(", null post_start_date")
				.append(", null limit_duration_no")
				.append(", null limit_duration_unit")
				.append(", null actual_start_date")
				.append(", null actual_end_date")
				.append(", null fixed_end_date")
				.append(", null limit_duration_end_date")
				.append(", s.employee_number")
				.append(", a.full_name employee_name")
				.append(", a.rank employee_rank")
				.append(", a.employee_category")
				.append(", 0 post_fte")
				.append(", a.fte strength_fte")
				.append(", cast(decode(a.fte,null,null, 0 - a.fte) as number) vacancy_fte")
				.append(", null cluster_ref")
				.append(", null cluster_remark")
				.append(", null post_remark")
				.append(", null post_status_desc")
				.append(", null post_status_start_date")
				.append(", null post_status_end_date")
				.append(", null supp_promo_ind")
				.append(", null junior_post_id")
				.append(", null resources_support_fr_ext")
				.append(", null resources_support_remark, null request_type, vpe.effective_start_date ")
				.append(", a.payzone, vpe.POST_SNAP_UID ")
				.append("from v_trans_export vpe")
				.append(", ( select post_id, person_id, employee_number from v_hcm_strength ")
				.append(") s ")
				.append(", ( select * from v_hcm_assignment ")
				.append("where  primary_flag = 'N' ")
				.append("and assignment_status = 'Active Assignment' ")
				.append(") a ")
				.append("where 1=1 ")
				.append("and vpe.post_id = s.post_id ")
				.append("and s.person_id = a.person_id ")
				.append("and trunc(vpe.effective_start_date) between trunc(a.EFFECTIVE_START_DATE) and trunc(a.EFFECTIVE_END_DATE) ");
		;

		// 4. append data access scope
		if(!sqlDataAccess.isEmpty()) {
			sqlVacantPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
			sqlNonVacantPriPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
			sqlNonVacantSecPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}

		// 5. append report criteria input by user
		sqlVacantPost.append(" and vpe.post_id ").append(" = :postId ");
		sqlNonVacantPriPost.append(" and vpe.post_id ").append(" = :postId ");
		sqlNonVacantSecPost.append(" and vpe.post_id ").append(" = :postId ");

		// 6. union all vacant and non vacant posts
		StringBuilder sql = new StringBuilder("select ")
				.append(" POST_SNAP_UID ")
				.append("from ( ")
				.append(sqlVacantPost)
				.append(" union all ")
				.append(sqlNonVacantPriPost)
				.append(" union all ")
				.append(sqlNonVacantSecPost)
				.append(") rpt ");

		String sqlFinal = " select max(FUNDING_SEQ_NO) from POST_FUNDING_SNAP where POST_SNAP_UID in (";
		sqlFinal += sql.toString();
		sqlFinal += ")";
		
		Query q = entityManager.createNativeQuery(sqlFinal);
		q.setParameter("postId", postId);
		
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
		
		if (result.hasNext()) {
			return ((BigDecimal)result.next()).intValue();
		}
		
		return 0;
		
	}

	public List<MPRSPostFundingSnapPo> getPostFundingSnapByPostId(List<DataAccessPo> dataAccessList, String postId, int seqNo) {
		List<MPRSPostFundingSnapPo> resultList = new ArrayList<MPRSPostFundingSnapPo>();

		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");

		// 1. query vacant posts
		StringBuilder sqlVacantPost = new StringBuilder("select ")
				.append("vpe.post_id")
				.append(", hcm_position_name")
				.append(", staff_group_name")
				.append(", cluster_code")
				.append(", inst_code")
				.append(", dept_code")
				.append(", sub_specialty_code")
				.append(", sub_specialty_desc")
				.append(", unit")
				.append(", rank_name")
				.append(", post_title")
				.append(", approval_date")
				.append(", approval_ref")
				.append(", approval_remark")
				.append(", post_duration_desc")
				.append(", post_start_date")
				.append(", limit_duration_no")
				.append(", limit_duration_unit")
				.append(", actual_start_date")
				.append(", actual_end_date")
				.append(", fixed_end_date")
				.append(", limit_duration_end_date")
				.append(", null employee_number")
				.append(", null employee_name")
				.append(", null employee_rank")
				.append(", null employee_category")
				.append(", post_fte")
				.append(", 0 strength_fte")
				.append(", cast(vpe.post_fte as number) vacancy_fte")
				.append(", cluster_ref")
				.append(", cluster_remark")
				.append(", post_remark")
				.append(", post_status_desc")
				.append(", post_status_start_date")
				.append(", post_status_end_date")
				.append(", supp_promo_ind")
				.append(", junior_post_id")
				.append(", resources_support_fr_ext")
				.append(", resources_support_remark, request_type, vpe.effective_start_date ")
				.append(", null payzone, vpe.POST_SNAP_UID ")
				.append("from v_trans_export vpe")
				.append(", ( select post_id, person_id, employee_number from v_hcm_strength ")
				.append(") s ")
				.append("where 1=1 ")
				.append("and vpe.post_id = s.post_id (+) ")
				.append("and s.person_id is null ");

		// 2. query non vacant primary posts
		StringBuilder sqlNonVacantPriPost = new StringBuilder("select ")
				.append("vpe.post_id")
				.append(", hcm_position_name")
				.append(", staff_group_name")
				.append(", cluster_code")
				.append(", inst_code")
				.append(", dept_code")
				.append(", sub_specialty_code")
				.append(", sub_specialty_desc")
				.append(", unit")
				.append(", rank_name")
				.append(", post_title")
				.append(", approval_date")
				.append(", approval_ref")
				.append(", approval_remark")
				.append(", post_duration_desc")
				.append(", post_start_date")
				.append(", limit_duration_no")
				.append(", limit_duration_unit")
				.append(", actual_start_date")
				.append(", actual_end_date")
				.append(", fixed_end_date")
				.append(", limit_duration_end_date")
				.append(", decode(a.fte,0,null,s.employee_number) employee_number")
				.append(", decode(a.fte,0,null,a.full_name) employee_name")
				.append(", decode(a.fte,0,null,a.rank) employee_rank")
				.append(", a.employee_category")
				.append(", post_fte")
				.append(", a.fte strength_fte")
				.append(", cast(decode(a.fte,null,null,vpe.post_fte - a.fte) as number) vacancy_fte")
				.append(", cluster_ref")
				.append(", cluster_remark")
				.append(", post_remark")
				.append(", post_status_desc")
				.append(", post_status_start_date")
				.append(", post_status_end_date")
				.append(", supp_promo_ind")
				.append(", junior_post_id")
				.append(", resources_support_fr_ext")
				.append(", resources_support_remark, request_type, vpe.effective_start_date ")
				.append(", a.payzone, vpe.POST_SNAP_UID ")
				.append(" from v_trans_export vpe ")
				.append(" inner join ( select post_id, person_id, employee_number from v_hcm_strength ")
				.append(") s ")
				.append(" on vpe.post_id = s.post_id ")	
				.append(" left join ( select * from v_hcm_assignment ")
				.append("where primary_flag = 'Y' ")
				.append("and assignment_status = 'Active Assignment' ")
				.append(") a ")
				.append("on s.person_id = a.person_id  ")
				.append("and trunc(vpe.effective_start_date) between trunc(a.EFFECTIVE_START_DATE) and trunc(a.EFFECTIVE_END_DATE) ")
				.append("where 1=1 ")
				;

		// 3. query non vacant secondary posts
		StringBuilder sqlNonVacantSecPost = new StringBuilder("select ")
				.append("'(' || vpe.post_id || ')' post_id")
				.append(", null hcm_position_name")
				.append(", null staff_group_name")
				.append(", null cluster_code")
				.append(", null inst_code")
				.append(", null dept_code")
				.append(", null sub_specialty_code")
				.append(", null sub_specialty_desc")
				.append(", null unit")
				.append(", null rank_name")
				.append(", null post_title")
				.append(", null approval_date")
				.append(", null approval_ref")
				.append(", null approval_remark")
				.append(", null post_duration_desc")
				.append(", null post_start_date")
				.append(", null limit_duration_no")
				.append(", null limit_duration_unit")
				.append(", null actual_start_date")
				.append(", null actual_end_date")
				.append(", null fixed_end_date")
				.append(", null limit_duration_end_date")
				.append(", s.employee_number")
				.append(", a.full_name employee_name")
				.append(", a.rank employee_rank")
				.append(", a.employee_category")
				.append(", 0 post_fte")
				.append(", a.fte strength_fte")
				.append(", cast(decode(a.fte,null,null, 0 - a.fte) as number) vacancy_fte")
				.append(", null cluster_ref")
				.append(", null cluster_remark")
				.append(", null post_remark")
				.append(", null post_status_desc")
				.append(", null post_status_start_date")
				.append(", null post_status_end_date")
				.append(", null supp_promo_ind")
				.append(", null junior_post_id")
				.append(", null resources_support_fr_ext")
				.append(", null resources_support_remark, null request_type, vpe.effective_start_date ")
				.append(", a.payzone, vpe.POST_SNAP_UID ")
				.append("from v_trans_export vpe")
				.append(", ( select post_id, person_id, employee_number from v_hcm_strength ")
				.append(") s ")
				.append(", ( select * from v_hcm_assignment ")
				.append("where  primary_flag = 'N' ")
				.append("and assignment_status = 'Active Assignment' ")
				.append(") a ")
				.append("where 1=1 ")
				.append("and vpe.post_id = s.post_id ")
				.append("and s.person_id = a.person_id ")
				.append("and trunc(vpe.effective_start_date) between trunc(a.EFFECTIVE_START_DATE) and trunc(a.EFFECTIVE_END_DATE) ");
		;

		// 4. append data access scope
		if(!sqlDataAccess.isEmpty()) {
			sqlVacantPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
			sqlNonVacantPriPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
			sqlNonVacantSecPost.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}

		// 5. append report criteria input by user
		sqlVacantPost.append(" and vpe.post_id ").append(" = :postId ");
		sqlNonVacantPriPost.append(" and vpe.post_id ").append(" = :postId ");
		sqlNonVacantSecPost.append(" and vpe.post_id ").append(" = :postId ");

		// 6. union all vacant and non vacant posts
		StringBuilder sql = new StringBuilder("select ")
				.append(" POST_SNAP_UID ")
				.append("from ( ")
				.append(sqlVacantPost)
				.append(" union all ")
				.append(sqlNonVacantPriPost)
				.append(" union all ")
				.append(sqlNonVacantSecPost)
				.append(") rpt ");

		String sqlFinal = "SELECT C.* FROM POST_FUNDING_SNAP C where C.FUNDING_SEQ_NO = :fundingSeqNo and C.REC_STATE = :recState ";
		sqlFinal += " and POST_SNAP_UID in (";
		sqlFinal += sql.toString();
		sqlFinal += ")";
		
		Query q = entityManager.createNativeQuery(sqlFinal, MPRSPostFundingSnapPo.class);
		q.setParameter("postId", postId);
		q.setParameter("fundingSeqNo", seqNo);
		q.setParameter("recState", MPRSConstant.MPRS_STATE_ACTIVE);
		
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
			MPRSPostFundingSnapPo c = (MPRSPostFundingSnapPo)result.next();
			resultList.add(c);
			
		}
		
		return resultList;
	}
}
