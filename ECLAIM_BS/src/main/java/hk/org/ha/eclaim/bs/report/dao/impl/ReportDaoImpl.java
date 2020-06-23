package hk.org.ha.eclaim.bs.report.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.report.dao.IReportDao;
import hk.org.ha.eclaim.core.po.MPRSConstant;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.report.helper.ReportSubqueryHelper;
import hk.org.ha.eclaim.bs.report.po.Report1Po;
import hk.org.ha.eclaim.bs.report.po.Report2Po;
import hk.org.ha.eclaim.bs.report.po.Report3Po;
import hk.org.ha.eclaim.bs.report.po.Report4Po;
import hk.org.ha.eclaim.bs.report.po.Report5ClusterPo;
import hk.org.ha.eclaim.bs.report.po.Report5Po;
import hk.org.ha.eclaim.bs.report.po.Report5RankClusterPo;
import hk.org.ha.eclaim.bs.report.po.Report5RankPo;
import hk.org.ha.eclaim.bs.report.po.Report6Po;
import hk.org.ha.eclaim.bs.report.po.Report7Po;
import hk.org.ha.eclaim.bs.report.po.ReportHeldAgainstListPo;
import hk.org.ha.eclaim.bs.report.po.ReportingPo;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Repository
public class ReportDaoImpl implements IReportDao {
	@Autowired
	ICommonSvc commonSvc; 

	@PersistenceContext
	private EntityManager entityManager;

	public int create(ReportingPo reportingPo, UserPo user) {
		Date updateDate = new Date();
		reportingPo.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
		reportingPo.setCreatedBy(user.getUserId());
		reportingPo.setCreatedDate(updateDate);
		reportingPo.setCreatedRoleId(user.getCurrentRole());
		reportingPo.setUpdatedBy(user.getUserId());
		reportingPo.setUpdatedDate(updateDate);
		reportingPo.setUpdatedRoleId(user.getCurrentRole());
		entityManager.persist(reportingPo);

		return reportingPo.getReportUid();
	}

	public List<ReportingPo> getReportListByType(String type, String userId) {
		List<ReportingPo> resultList = new ArrayList<ReportingPo>();

		Query q = entityManager.createQuery("SELECT C FROM ReportingPo C where C.reportType = :reportType and C.generatedBy = :generatedBy order by C.createdDate desc ", ReportingPo.class);

		q.setParameter("reportType", type);
		q.setParameter("generatedBy", userId);

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			ReportingPo c = (ReportingPo)result.next();
			resultList.add(c);
		}

		return resultList;
	}

	public ReportingPo getReportByUid(String uid) {
		Query q = entityManager.createQuery("SELECT C FROM ReportingPo C where C.reportUid = :uid", ReportingPo.class);
		q.setParameter("uid", Integer.parseInt(uid));

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		if (result.hasNext()) {
			return (ReportingPo)result.next();
		}

		return null;
	}

	public List<Report1Po> getReport1Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String instCode, String staffGroupCode, String rankCode) {
		System.out.println("instCode" + instCode);
		System.out.println("staffGroupCode" + staffGroupCode);
		System.out.println("rankCode" + rankCode);
		String criteria = "";

		//criteria
		System.out.println("report 1 sql: have criteria");
			
		if (clusterCode != null && !clusterCode.isEmpty()) {
			criteria += "and vpe.cluster_code = :clusterCode ";
		}
			
		if (instCode != null && !instCode.isEmpty()) {
			criteria += "and vpe.inst_code = :instCode ";
		}
			
		// show AC (Supplementary Promotion) as AC
		if (rankCode != null && !rankCode.isEmpty()) {
			criteria += "and CASE WHEN vpe.supp_promo_ind = 'Y' then 'AC' else vpe.rank_code END = :rankCode ";
		}
			
		System.out.println("report 1 criteria sql: "+ criteria);
		
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			criteria += "and vpe.staff_group_code = :staffGroupCode ";
		}

		List<Report1Po> resultList = new ArrayList<Report1Po>();
		
		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");

		// MPR Post query of curr yr
		StringBuilder sqlPostExportCurrYr = new StringBuilder("select vpe.post_id, ")
				.append("                         vpe.cluster_code, ")
				.append("                         vpe.inst_code, ")
				.append("                         CASE WHEN vpe.supp_promo_ind = 'Y' then 'AC (Supplementary Promotion)' ELSE vpe.rank_name END rank_name, ")
				.append("                         vpe.rank_seq_no, ")
				.append("                         post_fte ")
				.append("from v_post_export vpe ")
				.append("where trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ")
				.append("and post_status = 'ACTIVE' ")
				
				/** TODO temp fix for CC180502, to be enhanced in Phase 2 for HO posts
				 *  To fix ESV report summed HO/MG shortfall into total,
				 *  the shortfall should be excluded from the report
				 */
				.append(" and vpe.cluster_code <> 'HO' ")
				
				.append(criteria);
		
		// MPR Post query of last yr
		StringBuilder sqlPostExportLastYr = new StringBuilder("select ")
				.append("                         vpe.cluster_code, ")
				.append("                         vpe.inst_code, ")
				.append("                         CASE WHEN vpe.supp_promo_ind = 'Y' then 'AC (Supplementary Promotion)' ELSE vpe.rank_name END rank_name, ")
				.append("                         vpe.rank_seq_no, ")
				.append("                         post_fte ")
				.append("from v_post_export vpe ")
				.append("where to_date('31/03/'||(EXTRACT (YEAR FROM ADD_MONTHS(:asAtDate,-3))),'dd/mm/yyyy') between vpe.effective_start_date and vpe.effective_end_date ")
				.append("and post_status = 'ACTIVE' ")
				
				/** TODO temp fix for CC180502, to be enhanced in Phase 2 for HO posts
				 *  To fix ESV report summed HO/MG shortfall into total,
				 *  the shortfall should be excluded from the report
				 */
				.append(" and vpe.cluster_code <> 'HO' ")
				
				.append(criteria);
		
		if(!sqlDataAccess.isEmpty()) {
			sqlPostExportCurrYr.append(" and ( ").append(sqlDataAccess).append(" ) ");
			sqlPostExportLastYr.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}
		
		// HCM Strength
		StringBuilder sqlHcmStrength = new StringBuilder("select ")
				.append("s.post_id")
				.append(", s.person_id ")
				.append("from v_hcm_strength s ")
				.append("where trunc(:asAtDate) between s.effective_start_date and s.effective_end_date ");
				
		// HCM Assignment
		StringBuilder sqlHcmAssignment = new StringBuilder("select ")
				.append("a.person_id")
				.append(", sum(NVL(a.fte, 0)) str_fte ")
				.append("from v_hcm_assignment a ")
				.append("where a.assignment_status = 'Active Assignment' ")
				.append("and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date ")
				.append("group by a.person_id ");

		StringBuilder sql = new StringBuilder("select ")
				.append("         curr_yr.cluster_code, ")
				.append("         curr_yr.inst_code HOSPITAL, ")
				.append("         curr_yr.rank_name post_title, ")
				.append("         curr_yr.rank_seq_no, ")
				.append("         curr_yr.postFTE TOTAL_FTE, ")
				.append("         curr_yr.strengthFTE STRENGTH_FTE, ")
				.append("         curr_yr.postFTE - curr_yr.strengthFTE VACANCIES_FTE, ")
				.append("         NVL(last_yr.postFTE,0) last_yr_fte, ")
				.append("         curr_yr.postFTE - NVL(last_yr.postFTE,0) CURR_YR_FTE, ")
				.append("         round(curr_yr.strengthFTE/curr_yr.postFTE*100, 2) OCC_RATE ")
				.append("	from ( ")
				.append("    	select mpr.cluster_code, ")
				.append("              mpr.inst_code, ")
				.append("              mpr.rank_name, ")
				.append("              mpr.rank_seq_no, ")
				.append("              sum(post_fte) postFTE, ")
				.append("              sum(NVL(str_fte,0)) strengthFTE ")
				.append("       from ( ")
				.append(sqlPostExportCurrYr)
				.append("       ) mpr ")
				.append("	 	left join ( ")
				.append(sqlHcmStrength)
				.append("	 	) s on ( mpr.post_id = s.post_id ) ")
				.append("	 	left join ( ")
				.append(sqlHcmAssignment)
				.append("	 	) a on ( a.person_id = s.person_id ) ")
				.append("    	group by mpr.cluster_code, mpr.inst_code, mpr.rank_seq_no, mpr.rank_name ")
				.append("	) curr_yr ")
				.append("   left join ( ")
				.append("   	select cluster_code, ")
				.append("              inst_code, ")
				.append("              rank_name, ")
				.append("              rank_seq_no, ")
				.append("              sum(post_fte) postFTE ")
				.append("        from ( ")
				.append(sqlPostExportLastYr)
				.append("        ) ")
				.append("        group by cluster_code, inst_code, rank_name, rank_seq_no ")
				.append("	) last_yr on curr_yr.cluster_code = last_yr.cluster_code and curr_yr.inst_code = last_yr.inst_code and curr_yr.rank_name = last_yr.rank_name ")
				.append("   order by curr_yr.cluster_code, curr_yr.inst_code, curr_yr.rank_seq_no, curr_yr.rank_name ");

		Query q = entityManager.createNativeQuery(sql.toString(), Report1Po.class);
		q.setParameter("asAtDate", dateIn);

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

		if (clusterCode != null && !clusterCode.isEmpty()) {
			q.setParameter("clusterCode", clusterCode);
		}
		if (instCode != null && !instCode.isEmpty()) {
			q.setParameter("instCode", instCode);
		}
		if (rankCode != null && !rankCode.isEmpty()) {
			q.setParameter("rankCode", rankCode);
		}
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			q.setParameter("staffGroupCode", staffGroupCode);
		}

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			Report1Po c = (Report1Po)result.next();
			resultList.add(c);
		}

		return resultList;
	}

	public List<Report2Po> getReport2Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String deptCode, String staffGroupCode, String rankCode) {
		List<Report2Po> resultList = new ArrayList<Report2Po>();
		String criteria = "";

		//criteria
			System.out.println("report 2 sql: have criteria");
			
			if (clusterCode != null && !clusterCode.isEmpty()) {
				criteria += "and vpe.cluster_code = :clusterCode ";
			}
			
			if (deptCode != null && !deptCode.isEmpty()) {
				criteria += "and vpe.dept_code = :deptCode ";
			}
			
			// show AC (Supplementary Promotion) as AC
			if (rankCode != null && !rankCode.isEmpty()) {
				criteria += "and CASE WHEN vpe.supp_promo_ind = 'Y' then 'AC' else vpe.rank_code END = :rankCode ";
			}
			
			System.out.println("report 2 criteria sql: "+ criteria);
		
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			criteria += "and vpe.staff_group_code = :staffGroupCode ";
		}

		//MPR Post query
		StringBuilder sqlPostExport = new StringBuilder("select vpe.post_id, ")
				.append("                         vpe.cluster_code, ")
				.append("                         vpe.inst_code, ")
				.append("                         vpe.dept_code, ")
				.append("                         CASE WHEN vpe.supp_promo_ind = 'Y' then 'AC (Supplementary Promotion)' ELSE vpe.rank_name END rank_name, ")
				.append("                         vpe.RANK_SEQ_NO, ")
				.append("                         post_fte ")
				.append(" from v_post_export vpe ")
				.append(" where trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ")
				.append(" and post_status = 'ACTIVE' ")
				
				/** TODO temp fix for CC180502, to be enhanced in Phase 2 for HO posts
				 *  To fix ESV report summed HO/MG shortfall into total,
				 *  the shortfall should be excluded from the report
				 */
				.append(" and vpe.cluster_code <> 'HO' ")
				
				.append(criteria);

		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");
		if(!sqlDataAccess.isEmpty()) {
			sqlPostExport.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}
		
		// HCM Strength
		StringBuilder sqlHcmStrength = new StringBuilder("select ")
				.append("s.post_id")
				.append(", s.person_id ")
				.append("from v_hcm_strength s ")
				.append("where trunc(:asAtDate) between s.effective_start_date and s.effective_end_date ");
				
		// HCM Assignment
		StringBuilder sqlHcmAssignment = new StringBuilder("select ")
				.append("a.person_id")
				.append(", sum(NVL(a.fte,0)) str_fte ")
				.append("from v_hcm_assignment a ")
				.append("where a.assignment_status = 'Active Assignment' ")
				.append("and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date ")
				.append("group by a.person_id ");

		StringBuilder sql = new StringBuilder("select ")
				.append("         curr_yr.cluster_code clusterCode, ")
				.append("         curr_yr.inst_code hospital, ")
				.append("         curr_yr.dept_code deptName, ")
				.append("         curr_yr.rank_seq_no rank_seq_no, ")
				.append("         curr_yr.rank_name postTitle, ")
				.append("         curr_yr.postFTE totalFTE, ")
				.append("         curr_yr.strengthFTE strengthFTE, ")
				.append("         curr_yr.postFTE - curr_yr.strengthFTE vacanciesFTE, ")
				.append("         round(curr_yr.strengthFTE/curr_yr.postFTE*100, 2) occRate ")
				.append("from ( ")
				.append("    select mpr.cluster_code, ")
				.append("           mpr.inst_code, ")
				.append("           mpr.dept_code, ")
				.append("           mpr.rank_seq_no, ")
				.append("           mpr.rank_name, ")
				.append("           sum(post_fte) postFTE, ")
				.append("           sum(NVL(str_fte,0)) strengthFTE ")
				.append("    from ( ")
				.append(sqlPostExport)
				.append("    ) mpr ")
				.append("	 left join ( ")
				.append(sqlHcmStrength)
				.append("	 ) s on ( mpr.post_id = s.post_id ) ")
				.append("	 left join ( ")
				.append(sqlHcmAssignment)
				.append("	 ) a on ( a.person_id = s.person_id ) ")
				.append("    group by mpr.cluster_code, mpr.inst_code, mpr.dept_code, mpr.rank_seq_no, mpr.rank_name ")
				.append(") curr_yr ")
				.append("order by curr_yr.cluster_code, curr_yr.inst_code, curr_yr.dept_code, curr_yr.rank_seq_no, curr_yr.rank_name ");

		Query q = entityManager.createNativeQuery(sql.toString(), Report2Po.class);
		q.setParameter("asAtDate", dateIn);

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

		if (clusterCode != null && !clusterCode.isEmpty()) {
			q.setParameter("clusterCode", clusterCode);
		}
		if (deptCode != null && !deptCode.isEmpty()) {
			q.setParameter("deptCode", deptCode);
		}
		if (rankCode != null && !rankCode.isEmpty()) {
			q.setParameter("rankCode", rankCode);
		}
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			q.setParameter("staffGroupCode", staffGroupCode);
		}

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			Report2Po c = (Report2Po)result.next();
			resultList.add(c);
		}

		return resultList;
	}

	public List<Report3Po> getReport3Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode) {
		System.out.println("Perform ReportDaoImpl.getReport3Content");
		System.out.println("dateIn" + dateIn.toString());
		List<Report3Po> resultList = new ArrayList<Report3Po>();

		//MPR Post query
		StringBuilder sqlPostExport = new StringBuilder("select vpe.effective_start_date, ")
				.append("                         vpe.post_snap_uid, ")
				.append("                         vpe.effective_end_date, ")
				.append("                         vpe.post_id, ")
				.append("                         vpe.cluster_code, ")
				.append("                         vpe.inst_code, ")
				.append("                         CASE WHEN vpe.supp_promo_ind = 'Y' then 'AC (Supplementary Promotion)' ELSE vpe.rank_name END rank_name, ")
				.append("                         vpe.rank_seq_no, ")
				.append("                         vpe.post_duration_desc postDurationType, ")
				.append("                         vpe.limit_duration_end_date postEndDate, ")
				.append("                         vpe.post_fte, ")
				.append("                         vpe.post_remark, ")
				.append("                         vpe.hcm_position_id ")
				.append(" from v_post_export vpe ")
				.append(" where trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ")
				.append(" and post_status = 'ACTIVE' ")
				
				/** TODO temp fix for CC180502, to be enhanced in Phase 2 for HO posts
				 *  To fix ESV Details report summed HO/MG shortfall into total,
				 *  the shortfall should be excluded from the report
				 */
				.append(" and vpe.cluster_code <> 'HO' ");
		
		
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			sqlPostExport.append(" and vpe.staff_group_code = :staffGroupCode ");
		}
		
		if (rankCode != null && !rankCode.isEmpty()) {
			sqlPostExport.append(" and vpe.rank_code = :rankCode ");
		}
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			sqlPostExport.append(" and vpe.cluster_code = :clusterCode ");
		}

		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");
		if(!sqlDataAccess.isEmpty()) {
			sqlPostExport.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}
		
		// HCM Strength
		StringBuilder sqlHcmStrength = new StringBuilder("select ")
				.append("s.post_id")
				.append(", s.person_id")
				.append(", s.employee_number ")
				.append("from v_hcm_strength s ")
				.append("where trunc(:asAtDate) between s.effective_start_date and s.effective_end_date ");
		
		// HCM Assignment
		StringBuilder sqlHcmAssignment = new StringBuilder("select ")
				.append("a.person_id")
				.append(", a.position_id")
				.append(", NVL(a.fte, 0) fte")
				.append(", a.full_name")
				.append(", a.rank generic_rank")
				.append(", a.employment_type")
				.append(", a.employee_category ")
				.append("from v_hcm_assignment a ")
				.append("where a.assignment_status = 'Active Assignment' ")
				.append("and a.primary_flag = 'Y' ")
				.append("and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date ");

		StringBuilder sql = new StringBuilder("select ")
				.append("        m.effective_start_date effDate, ")
				.append("        m.effective_end_date, ")
				.append("        m.post_snap_uid postSnapId, ")
				.append("        m.post_id postID, ")
				.append("        m.cluster_code clusterCode, ")
				.append("        m.inst_code instCode, ")
				.append("        a.generic_rank generic_rank_name, ")
				.append("        m.rank_seq_no, ")
				.append("        m.rank_name postTitle, ")
				.append("        m.postDurationType, ")
				.append("        m.postEndDate, ")
				.append("        m.post_fte totalFTE, ")
				.append("        m.hcm_position_id, ")
				.append("        m.post_fte - NVL(a.fte, 0) vacanciesFTE, ")
				.append("        NVL(m.post_remark, '') postRemark, ")
				.append("        s.employee_number employeeId, ")
				.append("        a.full_name employeeName, ")
				.append("        a.employment_type employmentType, ")
				.append("        NVL(a.employee_category, ' ') employeeCategory, ")
				.append("        NVL(a.fte, 0) strengthFTE ")
				.append("from ( ")
				.append(sqlPostExport)
				.append(") m ")
				.append("left join ( ")
				.append(sqlHcmStrength)
				.append(") s on ( m.post_id = s.post_id ) ")
				.append("left join ( ")
				.append(sqlHcmAssignment)
				.append(") a on ( a.person_id = s.person_id ) ")
                .append("order by rank_seq_no, posttitle, clustercode, instcode, postID, employeeid ");

		System.out.println("sql: " + sql.toString());
		Query q = entityManager.createNativeQuery(sql.toString(), Report3Po.class);
		q.setParameter("asAtDate", dateIn);
		
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			q.setParameter("staffGroupCode", staffGroupCode);
		}
		
		if (rankCode != null && !rankCode.isEmpty()) {
			q.setParameter("rankCode", rankCode);
		}
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			q.setParameter("clusterCode", clusterCode);
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
			Report3Po c = (Report3Po)result.next();
			resultList.add(c);
		}

		return resultList;
	}

	public List<Report5Po> getReport5Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode) {
		List<Report5Po> resultList = new ArrayList<Report5Po>();
		
		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");

		//MPR Post query
		StringBuilder sqlPostExport = new StringBuilder("select ")
				.append("vpe.cluster_code")
				.append(", vpe.inst_code")
				.append(", vpe.staff_group_code")
				.append(", vpe.post_id")
				.append(", vpe.post_fte ")
				.append("from v_post_export vpe ")
				.append("where trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ")
				.append("and vpe.post_status = 'ACTIVE' ")
				
				/** TODO temp fix for CC180502, to be enhanced in Phase 2 for HO posts
				 *  To fix Vacancies report summed HO/MG shortfall into vacancies,
				 *  the shortfall should be excluded from the report
				 */
				.append("and vpe.cluster_code <> 'HO' ");
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			sqlPostExport.append(" and vpe.cluster_code = :clusterCode ");
		}
		if (rankCode != null && !rankCode.isEmpty()) {
			sqlPostExport.append(" and vpe.rank_code = :rankCode ");
		}
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			sqlPostExport.append(" and vpe.staff_group_code = :staffGroupCode ");
		}
		
		if(!sqlDataAccess.isEmpty()) {
			sqlPostExport.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}
		
		StringBuilder sqlPostExportA = new StringBuilder(sqlPostExport.toString())
				.append("and vpe.post_duration in ('R', 'TLC') ");
		
		StringBuilder sqlPostExportB = new StringBuilder(sqlPostExport.toString())
				.append("and ( vpe.post_duration = 'TLT' or ( vpe.post_duration = 'TLC' and vpe.post_fte_type = 'PART_TIME') ) ");
		
		// HCM Strength
		StringBuilder sqlHcmStrength = new StringBuilder("select ")
				.append("s.post_id")
				.append(", s.person_id ")
				.append("from v_hcm_strength s ")
				.append("where trunc(:asAtDate) between s.effective_start_date and s.effective_end_date ");
		
		// HCM Assignment - Part A
		StringBuilder sqlHcmAssignmentA = new StringBuilder("select ")
				.append("a.person_id")
				.append(", sum(nvl(a.fte, 0)) strFTE ")
				.append("from v_hcm_assignment a ")
				.append("where a.assignment_status = 'Active Assignment' ")
				.append("and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date ")
				.append("group by a.person_id ");
		
		// HCM Assignment - Part B (better performance)
		StringBuilder sqlHcmAssignmentB = new StringBuilder("select ")
				.append("a.person_id")
				.append(", nvl(a.fte, 0) strFTE ")
				.append("from v_hcm_assignment a ")
				.append("where a.assignment_status = 'Active Assignment' ")
				.append("and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date ");
		
		// A
		StringBuilder sqlA = new StringBuilder("select ")
				.append("p.cluster_code")
				.append(", p.inst_code")
				.append(", p.staff_group_code")
				.append(", sum( p.post_fte - nvl(a.strFTE, 0) ) A ")
				.append("from ( ")
				.append(sqlPostExportA)
				.append(") p, ( ")
				.append(sqlHcmStrength)
				.append(") s, ( ")
				.append(sqlHcmAssignmentA)
				.append(") a ")
				.append("where p.post_id = s.post_id (+) ")
				.append("and s.person_id = a.person_id (+) ")
				.append("group by p.staff_group_code, p.cluster_code, p.inst_code ");
		
		// B
		StringBuilder sqlB = new StringBuilder("select ")
				.append("p.cluster_code")
				.append(", p.inst_code")
				.append(", p.staff_group_code")
				.append(", sum(a.strFTE) B ")
				.append("from ( ")
				.append(sqlPostExportB)
				.append(") p, ( ")
				.append(sqlHcmStrength)
				.append(") s, ( ")
				.append(sqlHcmAssignmentB)
				.append(") a ")
				.append("where p.post_id = s.post_id ")
				.append("and s.person_id = a.person_id ")
				.append("group by p.staff_group_code, p.cluster_code, p.inst_code ");
		
		StringBuilder sql = new StringBuilder("select ")
				.append("partA.cluster_code clusterCode")
				.append(", c.cluster_name clusterName")
				.append(", partA.inst_code instCode")
				.append(", i.inst_name instName")
				.append(", partA.staff_group_code staffGroupCode")
				.append(", sg.staff_group_name staffGroupName")
				.append(", nvl(partA.A, 0) vacanciesfte")
				.append(", nvl(partB.B, 0) strengthfte")
				.append(", ( nvl(partA.A, 0) - nvl(partB.B, 0) ) deficiency ")
				.append("from ( ")
				.append(sqlA)
				.append(") partA ")
				.append("left join ( ")
				.append(sqlB)
				.append(") partB on ( partA.staff_group_code = partB.staff_group_code and partA.cluster_code = partB.cluster_code and partA.inst_code = partB.inst_code ) ")
				.append("inner join cs_staff_group sg on ( sg.staff_group_code = partA.staff_group_code and sg.rec_state = 'A' ) ")
				.append("inner join cs_cluster c on ( c.cluster_code = partA.cluster_code and c.rec_state = 'A' ) ")
				.append("inner join cs_inst i on ( i.inst_code = partA.inst_code and i.rec_state = 'A' ) ")
				.append("order by partA.cluster_code, partA.inst_code ")
				;

		Query q = entityManager.createNativeQuery(sql.toString(), Report5Po.class);
		q.setParameter("asAtDate", dateIn);
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			q.setParameter("clusterCode", clusterCode);
		}
		
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			q.setParameter("staffGroupCode", staffGroupCode);
		}
		
		if (rankCode != null && !rankCode.isEmpty()) {
			q.setParameter("rankCode", rankCode);
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
			Report5Po c = (Report5Po)result.next();
			resultList.add(c);
		}

		return resultList;
	}
	
	public List<Report5RankPo> getReport5RankContent(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode) {
		List<Report5RankPo> resultList = new ArrayList<Report5RankPo>();
		
		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");

		//MPR Post query
		StringBuilder sqlPostExport = new StringBuilder("select ")
				.append("vpe.cluster_code")
				.append(", vpe.inst_code, vpe.dept_code, vpe.rank_code")
				.append(", vpe.staff_group_code")
				.append(", vpe.post_id")
				.append(", vpe.post_fte, r.SEQ_NO ")
				.append("from v_post_export vpe, CS_RANK r ")
				.append("where vpe.rank_code = r.rank_code and trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ")
				.append("and vpe.post_status = 'ACTIVE' ");
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			sqlPostExport.append(" and vpe.cluster_code = :clusterCode ");
		}
		if (rankCode != null && !rankCode.isEmpty()) {
			sqlPostExport.append(" and vpe.rank_code = :rankCode ");
		}
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			sqlPostExport.append(" and vpe.staff_group_code = :staffGroupCode ");
		}
		
		if(!sqlDataAccess.isEmpty()) {
			sqlPostExport.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}
		
		StringBuilder sqlPostExportA = new StringBuilder(sqlPostExport.toString())
				.append("and vpe.post_duration in ('R', 'TLC') ");
		
		StringBuilder sqlPostExportB = new StringBuilder(sqlPostExport.toString())
				.append("and ( vpe.post_duration = 'TLT' or ( vpe.post_duration = 'TLC' and vpe.post_fte_type = 'PART_TIME') ) ");
		
		// HCM Strength
		StringBuilder sqlHcmStrength = new StringBuilder("select ")
				.append("s.post_id")
				.append(", s.person_id ")
				.append("from v_hcm_strength s ")
				.append("where trunc(:asAtDate) between s.effective_start_date and s.effective_end_date ");
		
		// HCM Assignment - Part A
		StringBuilder sqlHcmAssignmentA = new StringBuilder("select ")
				.append("a.person_id")
				.append(", sum(nvl(a.fte, 0)) strFTE ")
				.append("from v_hcm_assignment a ")
				.append("where a.assignment_status = 'Active Assignment' ")
				.append("and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date ")
				.append("group by a.person_id ");
		
		// HCM Assignment - Part B (better performance)
		StringBuilder sqlHcmAssignmentB = new StringBuilder("select ")
				.append("a.person_id")
				.append(", nvl(a.fte, 0) strFTE ")
				.append("from v_hcm_assignment a ")
				.append("where a.assignment_status = 'Active Assignment' ")
				.append("and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date ");
		
		// A
		StringBuilder sqlA = new StringBuilder("select ")
				.append("p.cluster_code")
				.append(", p.inst_code")
				.append(", p.dept_code, p.rank_code, p.seq_no, p.staff_group_code")
				.append(", sum( p.post_fte - nvl(a.strFTE, 0) ) A ")
				.append("from ( ")
				.append(sqlPostExportA)
				.append(") p, ( ")
				.append(sqlHcmStrength)
				.append(") s, ( ")
				.append(sqlHcmAssignmentA)
				.append(") a ")
				.append("where p.post_id = s.post_id (+) ")
				.append("and s.person_id = a.person_id (+) ")
				.append("group by p.staff_group_code, p.cluster_code, p.inst_code, p.dept_code, p.rank_code, p.seq_no ");
		
		// B
		StringBuilder sqlB = new StringBuilder("select ")
				.append("p.cluster_code")
				.append(", p.inst_code")
				.append(", p.dept_code, p.rank_code, p.seq_no, p.staff_group_code")
				.append(", sum(a.strFTE) B ")
				.append("from ( ")
				.append(sqlPostExportB)
				.append(") p, ( ")
				.append(sqlHcmStrength)
				.append(") s, ( ")
				.append(sqlHcmAssignmentB)
				.append(") a ")
				.append("where p.post_id = s.post_id ")
				.append("and s.person_id = a.person_id ")
				.append("group by p.staff_group_code, p.cluster_code, p.inst_code, p.dept_code, p.rank_code, p.seq_no ");
		
		StringBuilder sql = new StringBuilder("select ")
				.append("partA.cluster_code clusterCode")
				.append(", c.cluster_name clusterName")
				.append(", partA.inst_code instCode")
				.append(", i.inst_name instName")
				.append(", partA.dept_code deptCode ")
				.append(", d.dept_name deptName ")
				.append(", partA.rank_code rankCode ")
				.append(", r.rank_name rankName ")
				.append(", partA.staff_group_code staffGroupCode")
				.append(", sg.staff_group_name staffGroupName")
				.append(", nvl(partA.A, 0) vacanciesfte")
				.append(", nvl(partB.B, 0) strengthfte")
				.append(", ( nvl(partA.A, 0) - nvl(partB.B, 0) ) deficiency ")
				.append("from ( ")
				.append(sqlA)
				.append(") partA ")
				.append("left join ( ")
				.append(sqlB)
				.append(") partB on ( partA.staff_group_code = partB.staff_group_code and partA.cluster_code = partB.cluster_code and partA.inst_code = partB.inst_code and partA.dept_code = partB.dept_code and partA.rank_code = partB.rank_code) ")
				.append("inner join cs_staff_group sg on ( sg.staff_group_code = partA.staff_group_code and sg.rec_state = 'A' ) ")
				.append("inner join cs_cluster c on ( c.cluster_code = partA.cluster_code and c.rec_state = 'A' ) ")
				.append("inner join cs_inst i on ( i.inst_code = partA.inst_code and i.rec_state = 'A' ) ")
				.append("inner join xxmpr.cs_dept d on (d.dept_code = partA.dept_code and d.rec_state = 'A') ")
				.append("inner join xxmpr.cs_rank r on (r.rank_code = partA.rank_code and r.rec_state = 'A') ")
				.append(" order by partA.cluster_code, ")
				.append(" partA.inst_code, partA.dept_code, partA.SEQ_NO, partA.rank_code")
				;

		Query q = entityManager.createNativeQuery(sql.toString(), Report5RankPo.class);
		q.setParameter("asAtDate", dateIn);
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			q.setParameter("clusterCode", clusterCode);
		}
		
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			q.setParameter("staffGroupCode", staffGroupCode);
		}
		
		if (rankCode != null && !rankCode.isEmpty()) {
			q.setParameter("rankCode", rankCode);
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
			Report5RankPo c = (Report5RankPo)result.next();
			resultList.add(c);
		}

		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<Report5ClusterPo> getReport5SubContent(List<Report5Po> listOfRequest) {
		List<Report5ClusterPo> resultList = new ArrayList<Report5ClusterPo>();
		
		@SuppressWarnings("rawtypes")
		Map<String, List> rptMap = new HashMap();
		List<String> rptClusterList = new ArrayList<String>();
		for (Report5Po rptPo: listOfRequest) {
			String staffGrpCluster = rptPo.getStaffGroupCode() + "_" + rptPo.getStaffGroupName() + "_" + rptPo.getClusterCode() + "_" + rptPo.getClusterName();
			if (rptMap.containsKey(staffGrpCluster)) {
				List<Double> fte = rptMap.get(staffGrpCluster);
				rptMap.get(staffGrpCluster).set(0, fte.get(0) + rptPo.getVacanciesFTE());
				rptMap.get(staffGrpCluster).set(1, fte.get(1) + rptPo.getStrengthFTE());
				rptMap.get(staffGrpCluster).set(2, fte.get(2) + rptPo.getDeficiency());
			} else {
				rptMap.put(staffGrpCluster, Arrays.asList(rptPo.getVacanciesFTE(), rptPo.getStrengthFTE(), rptPo.getDeficiency()));
				rptClusterList.add(staffGrpCluster);
			}
		}
		
		for (String staffGrpCluster : rptClusterList) {
			
			String[] sp = staffGrpCluster.split("_");
			
			Report5ClusterPo c = new Report5ClusterPo();
			c.setStaffGroupCode(sp[0]);
			c.setStaffGroupName(sp[1]);
			c.setClusterCode(sp[2]);
			c.setClusterName(sp[3]);
			c.setTotalVacanciesFte((Double) rptMap.get(staffGrpCluster).get(0));
			c.setTotalStrengthfte((Double) rptMap.get(staffGrpCluster).get(1));
			c.setTotalDeficiency((Double) rptMap.get(staffGrpCluster).get(2));
			
			resultList.add(c);
		}

		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<Report5RankClusterPo> getReport5RankSubContent(List<Report5RankPo> listOfRequest) {
		List<Report5RankClusterPo> resultList = new ArrayList<Report5RankClusterPo>();
		
		System.out.println("listOfRequest: " + listOfRequest.size());
		@SuppressWarnings("rawtypes")
		Map<String, List> rptMap = new HashMap();
		for (Report5RankPo rptPo: listOfRequest) {
			System.out.println("A:" + rptPo.getClusterCode() + "/" + rptPo.getInstCode() + "/" + rptPo.getDeptName() + "/" + rptPo.getVacanciesFTE() + "/" + rptPo.getStrengthFTE() + "/" + rptPo.getDeficiency());
			String staffGrpCluster = rptPo.getStaffGroupCode() + "_" + rptPo.getStaffGroupName() + "_" + rptPo.getClusterCode() + "_" + rptPo.getClusterName();
			if (rptMap.containsKey(staffGrpCluster)) {
				List<Double> fte = rptMap.get(staffGrpCluster);
				rptMap.get(staffGrpCluster).set(0, fte.get(0) + rptPo.getVacanciesFTE());
				rptMap.get(staffGrpCluster).set(1, fte.get(1) + rptPo.getStrengthFTE());
				rptMap.get(staffGrpCluster).set(2, fte.get(2) + rptPo.getDeficiency());
			} else {
				rptMap.put(staffGrpCluster, Arrays.asList(rptPo.getVacanciesFTE(), rptPo.getStrengthFTE(), rptPo.getDeficiency()));
			}
		}
		
		for (String staffGrpCluster : rptMap.keySet()) {
			
			String[] sp = staffGrpCluster.split("_");
			
			Report5RankClusterPo c = new Report5RankClusterPo();
			c.setStaffGroupCode(sp[0]);
			c.setStaffGroupName(sp[1]);
			c.setClusterCode(sp[2]);
			c.setClusterName(sp[3]);
			c.setTotalVacanciesFte((Double) rptMap.get(staffGrpCluster).get(0));
			c.setTotalStrengthfte((Double) rptMap.get(staffGrpCluster).get(1));
			c.setTotalDeficiency((Double) rptMap.get(staffGrpCluster).get(2));
			
			resultList.add(c);
		}

		return resultList;
	}

	public List<Report4Po> getReport4ContentReminder(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode) {
		System.out.println("Perform ReportDaoImpl.getReport4ContentReminder");
		System.out.println("dateIn" + dateIn.toString());
		List<Report4Po> resultList = new ArrayList<Report4Po>();

		//MPR Post query
		StringBuilder sqlPostExport = new StringBuilder("select vpe.effective_start_date, ")
				.append("                         vpe.effective_end_date, ")
				.append("                         vpe.post_id, ")
				.append("                         vpe.cluster_code, ")
				.append("                         vpe.inst_code, ")
				.append("                         vpe.rank_seq_no, ")
				.append("                         vpe.rank_name, ")
				.append("                         CASE WHEN vpe.supp_promo_ind = 'Y' then ")
				.append("                            'AC (Supplementary Promotion)' ")
				.append("                           ELSE ")
				.append("                            vpe.rank_name ")
				.append("                         END postTitle, ")
				.append("                         vpe.post_duration_desc postDurationType, ")
				.append("                         vpe.LIMIT_DURATION_END_DATE postEndDate, ")
				.append("                         vpe.post_fte, ")
				.append("                         vpe.post_remark, ")
				.append("                         vpe.hcm_position_id ")
				.append("from v_post_export vpe ")
				.append("where vpe.post_duration <> 'R' ")
				.append("and vpe.post_status <> 'CLOSED' ")
				.append("and trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ")
				.append("and vpe.LIMIT_DURATION_END_DATE >= trunc(:asAtDate) ")
				.append("and vpe.LIMIT_DURATION_END_DATE <= batch_job_pkg.mprs_add_months(trunc(:asAtDate), 3) ");
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			sqlPostExport.append(" and vpe.cluster_code = :clusterCode ");
		}
		if (rankCode != null && !rankCode.isEmpty()) {
			sqlPostExport.append(" and vpe.rank_code = :rankCode ");
		}
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			sqlPostExport.append(" and vpe.staff_group_code = :staffGroupCode ");
		}

		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");
		if(!sqlDataAccess.isEmpty()) {
			sqlPostExport.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}
		
		// HCM Strength
		StringBuilder sqlHcmStrength = new StringBuilder("select ")
				.append("s.post_id")
				.append(", s.person_id")
				.append(", s.employee_number ")
				.append("from v_hcm_strength s ")
				.append("where trunc(:asAtDate) between s.effective_start_date and s.effective_end_date ");
		
		// HCM Assignment
		StringBuilder sqlHcmAssignment = new StringBuilder("select ")
				.append("a.person_id")
				.append(", a.position_id")
				.append(", NVL(a.fte, 0) fte")
				.append(", a.full_name")
				.append(", a.rank generic_rank")
				.append(", a.employment_type")
				.append(", a.employee_category ")
				.append("from v_hcm_assignment a ")
				.append("where a.assignment_status = 'Active Assignment' ")
				.append("and a.primary_flag = 'Y' ")
				.append("and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date ");
		
		// HCM Contract
		StringBuilder sqlHcmContract = new StringBuilder("select ")
				.append("c.person_id")
				.append(", c.contract_active_start_date")
				.append(", c.contract_active_end_date")
				.append(", c.contract_type")
				.append(", c.contract_original_end_date")
				.append(", c.extended_contract_end_date")
				.append(", c.fixed_term_appt_end_date")
				.append(", c.orginal_grade")
				.append(", c.appointment_orginal_end_date")
				.append(", c.extended_appointment_end_date ")
				.append("from v_hcm_contract c ")
				.append("where trunc(:asAtDate) between c.contract_active_start_date and c.contract_active_end_date ");
		
		StringBuilder sql = new StringBuilder("select ")
				.append("        m.effective_start_date, ")
				.append("        m.effective_end_date, ")
				.append("        m.post_id postID, ")
				.append("        m.cluster_code clusterCode, ")
				.append("        m.inst_code instCode, ")
				.append("        m.postTitle, ")
				.append("        m.postDurationType, ")
				.append("        m.postEndDate, ")
				.append("        NVL(m.post_fte, 0) totalFTE, ")
				.append("        to_char(m.hcm_position_id) hcm_position_id, ")
				.append("        (NVL(m.post_fte, 0) - NVL(a.fte, 0)) vacanciesFTE, ")
				.append("        NVL(m.post_remark, '') postRemark, ")
				.append("        s.employee_number employeeId, ")
				.append("        a.full_name employeeName, ")
				.append("        a.employment_type employmentCategory, ")
				.append("        (NVL(a.fte, 0)) strengthFTE, ")
				.append("        CASE WHEN c.contract_type = 'CONTRACT_W_GRAT' THEN 'Contract with Gratuity' ")
				.append("        	WHEN c.contract_type = 'OTHER_APPT' THEN 'Other Appointment' ")
				.append("        	WHEN c.contract_type = 'FIXED_TERM' THEN 'Fixed Term Appointment' ")
				.append("        END contract_type,  ")
				.append("        c.contract_active_start_date, ")
				.append("        a.generic_rank generic_rank_name, ")
				.append("        CASE WHEN c.contract_type = 'CONTRACT_W_GRAT' THEN NVL(c.extended_contract_end_date, c.contract_original_end_date)               ")
				.append("          WHEN c.contract_type = 'FIXED_TERM' THEN c.fixed_term_appt_end_date  ")
				.append("          WHEN c.contract_type = 'OTHER_APPT' THEN NVL(c.extended_appointment_end_date, c.appointment_orginal_end_date)               ")
				.append("        END contract_end_date ")
				.append("    from ( ")
				.append(sqlPostExport)
				.append("    ) m ")
				.append("	 left join ( ")
				.append(sqlHcmStrength)
				.append("	 ) s on ( m.post_id = s.post_id ) ")
				.append("	 left join ( ")
				.append(sqlHcmAssignment)
				.append("	 ) a on ( s.person_id = a.person_id ) ")
				.append("	 left join ( ")
				.append(sqlHcmContract)
				.append("    ) c on a.person_id = c.person_id ")
				.append("    order by m.cluster_code, m.inst_code, m.rank_seq_no, m.postTitle, m.postEndDate, m.hcm_position_id, s.employee_number ");

		Query q = entityManager.createNativeQuery(sql.toString());
		q.setParameter("asAtDate", dateIn);
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			q.setParameter("clusterCode", clusterCode);
		}
		
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			q.setParameter("staffGroupCode", staffGroupCode);
		}
		
		if (rankCode != null && !rankCode.isEmpty()) {
			q.setParameter("rankCode", rankCode);
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

			Object[] c = (Object[])result.next();

			Report4Po s = new Report4Po();
			if (c[0] != null){
				s.setEffDate((Date)c[0]);
			}
			if (c[1] != null){
				s.setEffEndDate((Date)c[1]);
			}			
			if (c[2] != null){
				s.setPostId((String)c[2]);
			}			
			if (c[3] != null){
				s.setClusterCode((String)c[3]);		
			}				
			if (c[4] != null){
				s.setInstCode((String)c[4]);
			}						
			if (c[5] != null){
				s.setPostTitle((String)c[5]);
			}						
			if (c[6] != null){
				s.setPostDurationType((String)c[6]);
			}						
			if (c[7] != null){
				s.setPostEndDate((Date)c[7]);
			}
			if (c[8] != null){
				s.setTotalFTE(((BigDecimal)c[8]).doubleValue());
			}						
			if (c[9] != null){
				s.setHcmPositionId((String)c[9]);
			}			
			if (c[10] != null){
				s.setVacanciesFTE(((BigDecimal)c[10]).doubleValue());
			}			
			if (c[11] != null){
				s.setPostRemark((String)c[11]);
			}			
			if (c[12] != null){
				s.setEmployeeId((String)c[12]);
			}			
			if (c[13] != null){
				s.setEmployeeName((String)c[13]);
			}			
			if (c[14] != null){
				s.setEmploymentCategory((String)c[14]);
			}			
			if (c[15] != null){
				s.setStrengthFTE(((BigDecimal)c[15]).doubleValue());
			}			
			if (c[16] != null){
				s.setContractType((String)c[16]);
			}			
			if (c[17] != null){
				s.setContractStartDate((Date)c[17]);
			}			
			if (c[18] != null){
				s.setRank((String)c[18]);
			}			
			if (c[19] != null){
				s.setContractEndDate((Date)c[19]);
			}			

			resultList.add(s);
		}

		return resultList;
	}

	public List<Report4Po> getReport4ContentOutstanding(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode) {
		System.out.println("Perform ReportDaoImpl.getReport4ContentOutstanding");
		System.out.println("dateIn" + dateIn.toString());
		List<Report4Po> resultList = new ArrayList<Report4Po>();

		//MPR Post query
		StringBuilder sqlPostExport = new StringBuilder("select vpe.effective_start_date, ")
				.append("                         vpe.effective_end_date, ")
				.append("                         vpe.post_id, ")
				.append("                         vpe.cluster_code, ")
				.append("                         vpe.inst_code, ")
				.append("                         vpe.rank_seq_no, ")
				.append("                         vpe.rank_name, ")
				.append("                         CASE WHEN vpe.supp_promo_ind = 'Y' then ")
				.append("                            'AC (Supplementary Promotion)' ")
				.append("                           ELSE ")
				.append("                            vpe.rank_name ")
				.append("                         END postTitle, ")
				.append("                         vpe.post_duration_desc postDurationType, ")
				.append("                         vpe.LIMIT_DURATION_END_DATE postEndDate, ")
				.append("                         vpe.post_fte, ")
				.append("                         vpe.post_remark, ")
				.append("                         vpe.hcm_position_id ")
				.append(" from v_post_export vpe ")
				.append("where vpe.post_duration <> 'R' ")
				.append("and vpe.post_status <> 'CLOSED' ")
				.append("and trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ")
				.append("and vpe.LIMIT_DURATION_END_DATE < trunc(:asAtDate) ");
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			sqlPostExport.append(" and vpe.cluster_code = :clusterCode ");
		}
		if (rankCode != null && !rankCode.isEmpty()) {
			sqlPostExport.append(" and vpe.rank_code = :rankCode ");
		}
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			sqlPostExport.append(" and vpe.staff_group_code = :staffGroupCode ");
		}

		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");
		if(!sqlDataAccess.isEmpty()) {
			sqlPostExport.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}
		
		// HCM Strength
		StringBuilder sqlHcmStrength = new StringBuilder("select ")
				.append("s.post_id")
				.append(", s.person_id")
				.append(", s.employee_number ")
				.append("from v_hcm_strength s ")
				.append("where trunc(:asAtDate) between s.effective_start_date and s.effective_end_date ");
		
		// HCM Assignment
		StringBuilder sqlHcmAssignment = new StringBuilder("select ")
				.append("a.person_id")
				.append(", a.position_id")
				.append(", NVL(a.fte, 0) fte")
				.append(", a.full_name")
				.append(", a.rank generic_rank")
				.append(", a.employment_type")
				.append(", a.employee_category ")
				.append("from v_hcm_assignment a ")
				.append("where a.assignment_status = 'Active Assignment' ")
				.append("and a.primary_flag = 'Y' ")
				.append("and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date ");
		
		// HCM Contract
		StringBuilder sqlHcmContract = new StringBuilder("select ")
				.append("c.person_id")
				.append(", c.contract_active_start_date")
				.append(", c.contract_active_end_date")
				.append(", c.contract_type")
				.append(", c.contract_original_end_date")
				.append(", c.extended_contract_end_date")
				.append(", c.fixed_term_appt_end_date")
				.append(", c.orginal_grade")
				.append(", c.appointment_orginal_end_date")
				.append(", c.extended_appointment_end_date ")
				.append("from v_hcm_contract c ")
				.append("where trunc(:asAtDate) between c.contract_active_start_date and c.contract_active_end_date ");
		
		StringBuilder sql = new StringBuilder("select ")
				.append("        m.effective_start_date, ")
				.append("        m.effective_end_date, ")
				.append("        m.post_id postID, ")
				.append("        m.cluster_code clusterCode, ")
				.append("        m.inst_code instCode, ")
				.append("        m.rank_seq_no, ")
				.append("        m.postTitle, ")
				.append("        m.postDurationType, ")
				.append("        m.postEndDate, ")
				.append("        NVL(m.post_fte, 0) totalFTE, ")
				.append("        to_char(m.hcm_position_id) hcm_position_id, ")
				.append("        (NVL(m.post_fte, 0) - NVL(a.fte, 0)) vacanciesFTE, ")
				.append("        NVL(m.post_remark, '') postRemark, ")
				.append("        s.employee_number employeeId, ")
				.append("        a.full_name employeeName, ")
				.append("        a.employment_type employmentCategory, ")
				.append("        NVL(a.fte, 0) strengthFTE, ")
				.append("        CASE WHEN c.contract_type = 'CONTRACT_W_GRAT' THEN 'Contract with Gratuity' ")
				.append("        	WHEN c.contract_type = 'OTHER_APPT' THEN 'Other Appointment' ")
				.append("        	WHEN c.contract_type = 'FIXED_TERM' THEN 'Fixed Term Appointment' ")
				.append("        END contract_type,  ")
				.append("        c.contract_active_start_date, ")
				.append("        a.generic_rank generic_rank_name, ")
				.append("        CASE WHEN c.contract_type = 'CONTRACT_W_GRAT' THEN NVL(c.extended_contract_end_date, c.contract_original_end_date)               ")
				.append("          WHEN c.contract_type = 'FIXED_TERM' THEN c.fixed_term_appt_end_date  ")
				.append("          WHEN c.contract_type = 'OTHER_APPT' THEN NVL(c.extended_appointment_end_date, c.appointment_orginal_end_date)               ")
				.append("        END contract_end_date ")
				.append("    from ( ")
				.append(sqlPostExport)
				.append("    ) m ")
				.append("	 left join ( ")
				.append(sqlHcmStrength)
				.append("	 ) s on ( m.post_id = s.post_id ) ")
				.append("	 left join ( ")
				.append(sqlHcmAssignment)
				.append("	 ) a on ( s.person_id = a.person_id ) ")
				.append("	 left join ( ")
				.append(sqlHcmContract)
				.append("    ) c on a.person_id = c.person_id ")
				.append("     order by m.cluster_code, m.inst_code, m.rank_seq_no, m.postTitle, m.postEndDate, m.hcm_position_id, s.employee_number ");

		Query q = entityManager.createNativeQuery(sql.toString());
		q.setParameter("asAtDate", dateIn);
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			q.setParameter("clusterCode", clusterCode);
		}
		
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			q.setParameter("staffGroupCode", staffGroupCode);
		}
		
		if (rankCode != null && !rankCode.isEmpty()) {
			q.setParameter("rankCode", rankCode);
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

			Object[] c = (Object[])result.next();

			Report4Po s = new Report4Po();
			if (c[0] != null){
				s.setEffDate((Date)c[0]);
			}
			if (c[1] != null){
				s.setEffEndDate((Date)c[1]);
			}			
			if (c[2] != null){
				s.setPostId((String)c[2]);
			}			
			if (c[3] != null){
				s.setClusterCode((String)c[3]);
			}			
			if (c[4] != null){
				s.setInstCode((String)c[4]);
			}			
			if (c[6] != null){
				s.setPostTitle((String)c[6]);
			}			
			if (c[7] != null){
				s.setPostDurationType((String)c[7]);
			}			
			if (c[8] != null){
				s.setPostEndDate((Date)c[8]);
			}
			if (c[9] != null){
				s.setTotalFTE(((BigDecimal)c[9]).doubleValue());
			}						
			if (c[10] != null){
				s.setHcmPositionId((String)c[10]);
			}			
			if (c[11] != null){
				s.setVacanciesFTE(((BigDecimal)c[11]).doubleValue());
			}			
			if (c[12] != null){
				s.setPostRemark((String)c[12]);
			}			
			if (c[13] != null){
				s.setEmployeeId((String)c[13]);
			}			
			if (c[14] != null){
				s.setEmployeeName((String)c[14]);
			}			
			if (c[15] != null){
				s.setEmploymentCategory((String)c[15]);
			}			
			if (c[16] != null){
				s.setStrengthFTE(((BigDecimal)c[16]).doubleValue());
			}			
			if (c[17] != null){
				s.setContractType((String)c[17]);
			}			
			if (c[18] != null){
				s.setContractStartDate((Date)c[18]);
			}			
			if (c[19] != null){
				s.setRank((String)c[19]);
			}			
			if (c[20] != null){
				s.setContractEndDate((Date)c[20]);
			}
			
			resultList.add(s);
		}

		return resultList;
	}
	
	public List<Report6Po> getReport6Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode) {
		System.out.println("Perform ReportDaoImpl.getReport6Content");
		System.out.println("dateIn" + dateIn.toString());
		List<Report6Po> resultList = new ArrayList<Report6Po>();

		//MPR Post query
		StringBuilder sqlPostExport = new StringBuilder("select ")
				.append("	vpe.effective_start_date, ")
				.append("	vpe.post_snap_uid, ")
				.append("	vpe.effective_end_date, ")
				.append("	vpe.post_id, ")
				.append("	vpe.post_duration_desc postDurationType, ")
				.append("	vpe.LIMIT_DURATION_END_DATE postEndDate, ")
				.append("	NVL(vpe.post_fte, 0) post_fte, ")
				.append("	vpe.post_remark, ")
				.append("	vpe.hcm_position_id, vpe.post_status_desc ")
				.append("from ")
				.append("	v_post_export vpe ")
				.append("where ")
				.append("	trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ")
				.append("	and ( ")
				.append("		post_status in ('CLOSED', 'FROZEN') ")
				.append("	) ");
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			sqlPostExport.append(" and vpe.cluster_code = :clusterCode ");
		}
		if (rankCode != null && !rankCode.isEmpty()) {
			sqlPostExport.append(" and vpe.rank_code = :rankCode ");
		}
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			sqlPostExport.append(" and vpe.staff_group_code = :staffGroupCode ");
		}
				
		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");
		if(!sqlDataAccess.isEmpty()) {
			sqlPostExport.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}
		
		// HCM Strength
		StringBuilder sqlHcmStrength = new StringBuilder("select ")
				.append("s.post_id")
				.append(", s.person_id")
				.append(", s.employee_number ")
				.append("from v_hcm_strength s ")
				.append("where trunc(:asAtDate) between s.effective_start_date and s.effective_end_date ");
		
		// HCM Assignment
		StringBuilder sqlHcmAssignment = new StringBuilder("select ")
				.append("a.person_id")
				.append(", a.position_id")
				.append(", a.full_name")
				.append(", a.rank generic_rank")
				.append(", a.employment_type ")
				.append("from v_hcm_assignment a ")
				.append("where a.assignment_status = 'Active Assignment' ")
				.append("and a.primary_flag = 'Y' ")
				.append("and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date ");

		StringBuilder sql = new StringBuilder("select ")
				.append("	m.post_snap_uid postSnapId, ")
				.append("	m.post_id postID, ")
				.append("	a.generic_rank generic_rank_name, ")
				.append("	m.postEndDate, ")
				.append("	m.hcm_position_id, ")
				.append("	s.employee_number employeeId, ")
				.append("	a.full_name employeeName, ")
				.append("	a.employment_type employmentCategory, m.post_status_desc ")
				.append("from ")
				.append("	( ")
				.append(sqlPostExport)
				.append("	) m ")  		
				.append("	inner join ( ")
				.append(sqlHcmStrength)
				.append("	) s on ( m.post_id = s.post_id ) ")
				.append("	inner join ( ")
				.append(sqlHcmAssignment)
				.append("	) a on ( s.person_id = a.person_id ) ")
				.append("	order by post_status_desc, postID, employeeid ");

		Query q = entityManager.createNativeQuery(sql.toString(), Report6Po.class);
		q.setParameter("asAtDate", dateIn);
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			q.setParameter("clusterCode", clusterCode);
		}
		if (rankCode != null && !rankCode.isEmpty()) {
			q.setParameter("rankCode", rankCode);
		}
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			q.setParameter("staffGroupCode", staffGroupCode);
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
			Report6Po c = (Report6Po)result.next();
			resultList.add(c);
		}

		return resultList;
	}
	
	public List<Report7Po> getReport7Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode) {
		System.out.println("Perform ReportDaoImpl.getReport7Content");
		System.out.println("dateIn" + dateIn.toString());
		List<Report7Po> resultList = new ArrayList<Report7Po>();

		//MPR Post query
		StringBuilder sqlPostExport = new StringBuilder("select ")
				.append("		vpe.effective_start_date, ")
				.append("		vpe.post_snap_uid, ")
				.append("		vpe.effective_end_date, ")
				.append("		vpe.post_id, ")
				.append("		vpe.cluster_code, ")
				.append("		vpe.inst_code, ")
				.append("		vpe.rank_name, ")
				.append("		vpe.hcm_position_id, ")
				.append("		vpe.hcm_position_name, ")
				.append("		CASE WHEN vpe.supp_promo_ind = 'Y' then ")
				.append("			  'AC (Supplementary Promotion)' ")                       
				.append("			ELSE ")
				.append("			  vpe.rank_name ")
				.append("		END postTitle, ")
				.append("		vpe.post_duration_desc postDurationType, ")
				.append("		vpe.LIMIT_DURATION_END_DATE postEndDate, ")
				.append("		vpe.post_fte post_fte, ")
				.append("		vpe.post_remark ")
				.append("	from ")
				.append("		v_post_export vpe ")
				.append("	where ")
				.append("		trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ")
				.append("		and ( ")
				.append("			post_status = 'ACTIVE' ")
				.append("		) ");
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			sqlPostExport.append(" and vpe.cluster_code = :clusterCode ");
		}
		if (rankCode != null && !rankCode.isEmpty()) {
			sqlPostExport.append(" and vpe.rank_code = :rankCode ");
		}
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			sqlPostExport.append(" and vpe.staff_group_code = :staffGroupCode ");
		}
				
		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");
		if(!sqlDataAccess.isEmpty()) {
			sqlPostExport.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}
		
		// HCM Strength
		StringBuilder sqlHcmStrength = new StringBuilder("select ")
				.append("s.post_id")
				.append(", s.person_id ")
				.append(", s.employee_number ")
				.append("from v_hcm_strength s ")
				.append("where trunc(:asAtDate) between s.effective_start_date and s.effective_end_date ");
		
		// HCM Assignment
		StringBuilder sqlHcmAssignment = new StringBuilder("select ")
				.append("a.person_id")
				.append(", a.position_id")
				.append(", a.position_name")
				.append(", a.full_name")
				.append(", a.rank generic_rank")
				.append(", a.employment_type ")
				.append("from v_hcm_assignment a ")
				.append("where a.assignment_status = 'Active Assignment' ")
				.append("and a.primary_flag = 'Y' ")
				.append("and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date ");

		StringBuilder sql = new StringBuilder("select ")
				.append("	m.post_snap_uid postSnapId, ")
				.append("	m.post_id postID, ")
				.append("	m.hcm_position_id mpr_hcm_position_id, ")
				.append("	a.position_id hcm_hcm_position_id,  ")
				.append("   m.hcm_position_name mpr_hcm_position_name, ")
		        .append("   a.position_name hcm_hcm_position_name, ")
				.append("	a.generic_rank generic_rank_name, ")
				.append("	s.employee_number employeeId, ")
				.append("	a.full_name employeeName, ")
				.append("	a.employment_type employmentCategory ")
				.append("from ")
				.append("	(  ")
				.append(sqlPostExport)
				.append("	) m ")  		
				.append("	inner join ( ")
				.append(sqlHcmStrength)
				.append("	) s on ( m.post_id = s.post_id ) ")
				.append("	inner join ( ")
				.append(sqlHcmAssignment)
				.append("	) a on ( s.person_id = a.person_id ) ")
				.append("where NVL(a.position_id,'') <> NVL(m.hcm_position_id,'') ")
				.append("order by m.post_id, s.employee_number ");
				
		Query q = entityManager.createNativeQuery(sql.toString(), Report7Po.class);
		q.setParameter("asAtDate", dateIn);
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			q.setParameter("clusterCode", clusterCode);
		}
		
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			q.setParameter("staffGroupCode", staffGroupCode);
		}
		
		if (rankCode != null && !rankCode.isEmpty()) {
			q.setParameter("rankCode", rankCode);
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
			Report7Po c = (Report7Po)result.next();
			resultList.add(c);
		}

		return resultList;
	}
	
	public List<ReportHeldAgainstListPo> getReportHeldAgainstList(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String instCode, String deptCode, String staffGroupCode, String rankCode) {
		System.out.println("Perform ReportDaoImpl.getHeldAgainstContent");
		System.out.println("dateIn" + dateIn.toString());
		List<ReportHeldAgainstListPo> resultList = new ArrayList<ReportHeldAgainstListPo>();

		String criteria = "";

		//criteria
		System.out.println("getHeldAgainst : have criteria");
		if (clusterCode != null && !clusterCode.isEmpty()){
			criteria += "and vpe.cluster_code = :clusterCode ";
		}
		
		if (instCode != null && !instCode.isEmpty()){
			criteria += "and vpe.inst_code = :instCode ";
		}
		
		if (deptCode != null && !deptCode.isEmpty()){
			criteria += "and vpe.dept_code = :deptCode ";
		}
		
		if (rankCode != null && !rankCode.isEmpty()){
			criteria += "and CASE WHEN vpe.supp_promo_ind = 'Y' then 'AC' else vpe.rank_code END = :rankCode ";
		}
		
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			criteria += "and vpe.staff_group_code = :staffGroupCode ";
		}

		System.out.println("getHeldAgainst criteria sql: "+ criteria);
				
		//MPR Post query
		StringBuilder sqlPostExport = new StringBuilder("select ")       
				.append("                         			vpe.post_id, ")
				.append("                                   vpe.post_snap_uid, ")
				.append("                                   vpe.cluster_code, ")
				.append("                                   vpe.inst_code, ")
				.append("                                   vpe.dept_code, ")
				.append("                                   vpe.rank_name, ")
				.append("                                   CASE WHEN vpe.supp_promo_ind = 'Y' then ")
				.append("                                    'AC (Supplementary Promotion)' ")
				.append("                                   ELSE ")
				.append("                                    vpe.rank_name ")
				.append("                                   END postTitle, ")
				.append("                                   vpe.post_duration_desc postDurationType, ")
				.append("                                   vpe.LIMIT_DURATION_END_DATE postEndDate, ")
				.append("                                   vpe.post_fte post_fte, ")
				.append("                                   vpe.post_remark, ")
				.append("                                   vpe.hcm_position_id ")
				.append("                             from v_post_export vpe ")
				.append("                               where trunc(:asAtDate) between vpe.effective_start_date and vpe.effective_end_date ")
				.append(criteria);
		

		// Data Access Scope of user
		String sqlDataAccess = ReportSubqueryHelper.getDataAccessScopeQuery(dataAccessList, "vpe");
		if(!sqlDataAccess.isEmpty()) {
			sqlPostExport.append(" and ( ").append(sqlDataAccess).append(" ) ");
		}
		
		// HCM Strength
		StringBuilder sqlHcmStrength = new StringBuilder("select ")
				.append("s.post_id")
				.append(", s.person_id")
				.append(", s.employee_number ")
				.append("from v_hcm_strength s ")
				.append("where trunc(:asAtDate) between s.effective_start_date and s.effective_end_date ");
		
		// HCM Assignment
		StringBuilder sqlHcmAssignment = new StringBuilder("select ")
				.append("a.person_id")
				.append(", a.position_id")
				.append(", NVL(a.fte, 0) fte")
				.append(", a.full_name")
				.append(", a.rank generic_rank ")
				.append(", nvl(a.employment_type, ' ') employment_type ")
				.append(", nvl(a.employee_category, ' ') employee_category ")
				.append("from v_hcm_assignment a ")
				.append("where a.assignment_status = 'Active Assignment' ")
				.append("and a.primary_flag = 'N' ")
				.append("and trunc(:asAtDate) between a.effective_start_date and a.effective_end_date ");

		StringBuilder sql = new StringBuilder("select ")
				.append("              '(' || m.post_id || ')' postID, ")
				.append("              m.post_snap_uid postSnapUID, ")
				.append("              m.cluster_code clusterCode, ")
				.append("              m.inst_code instCode, ")
				.append("              m.dept_code deptName, ")
				.append("              s.employee_number employeeId, ")
				.append("              a.full_name employeeName, ")
				.append("              a.generic_rank generic_rank_name, ")
				.append("              a.fte strengthFTE, a.employment_type, a.employee_category ")
				.append("            from  ")
				.append("            (  ")
				.append(sqlPostExport)
				.append("            ) m ")
				.append("            inner join ( ")
				.append(sqlHcmStrength)
				.append("			 ) s on ( m.post_id = s.post_id ) ")
				.append("			 inner join ( ")
				.append(sqlHcmAssignment)
				.append("			 ) a on ( s.person_id = a.person_id ) ");

		Query q = entityManager.createNativeQuery(sql.toString(), ReportHeldAgainstListPo.class);
		q.setParameter("asAtDate", dateIn);

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
		
		if (clusterCode != null && !clusterCode.isEmpty()) {
			q.setParameter("clusterCode", clusterCode);
		}
		if (instCode != null && !instCode.isEmpty()) {
			q.setParameter("instCode", instCode);
		}
		if (rankCode != null && !rankCode.isEmpty()) {
			q.setParameter("rankCode", rankCode);
		}
		if (deptCode != null && !deptCode.isEmpty()) {
			q.setParameter("deptCode", deptCode);
		}
		if (staffGroupCode != null && !staffGroupCode.isEmpty()) {
			q.setParameter("staffGroupCode", staffGroupCode);
		}
		
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			ReportHeldAgainstListPo c = (ReportHeldAgainstListPo)result.next();
			resultList.add(c);
		}

		return resultList;
	}
	
}
