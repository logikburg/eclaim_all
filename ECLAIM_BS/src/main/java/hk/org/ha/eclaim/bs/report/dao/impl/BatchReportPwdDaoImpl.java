package hk.org.ha.eclaim.bs.report.dao.impl;

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.report.dao.IBatchReportPwdDao;

@Repository
public class BatchReportPwdDaoImpl implements IBatchReportPwdDao {

	@PersistenceContext
	private EntityManager entityManager;

//	public int create(ReportingPo reportingPo, UserPo user) {
//		Date updateDate = new Date();
//		reportingPo.setRecState(MPRSConstant.MPRS_STATE_ACTIVE);
//		reportingPo.setCreatedBy(user.getUserId());
//		reportingPo.setCreatedDate(updateDate);
//		reportingPo.setCreatedRoleId(user.getCurrentRole());
//		reportingPo.setUpdatedBy(user.getUserId());
//		reportingPo.setUpdatedDate(updateDate);
//		reportingPo.setUpdatedRoleId(user.getCurrentRole());
//		entityManager.persist(reportingPo);
//
//		return reportingPo.getReportUid();
//	}

	public String getReportPwd(String clusterCode) {
		String pwd = null;

		Query q = entityManager.createNativeQuery("select rpt_pwd from rpt_batch_rpt_pwd where cluster_code = :clusterCode and trunc(sysdate) between start_date and nvl(end_date,to_date('4712/12/31','YYYY/MM/DD'))");
		q.setParameter("clusterCode", clusterCode);

		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();
		while (result.hasNext()) {
			pwd = (String) result.next();
		}

		return pwd;
	}

}
