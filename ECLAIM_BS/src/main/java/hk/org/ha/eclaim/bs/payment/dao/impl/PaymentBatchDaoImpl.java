package hk.org.ha.eclaim.bs.payment.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hk.org.ha.eclaim.bs.cs.po.OrganizationPo;
import hk.org.ha.eclaim.bs.payment.dao.IPaymentBatchDao;
import hk.org.ha.eclaim.bs.payment.po.PaymentBatchPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentDetailPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentEnquiryPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentJobPo;
import hk.org.ha.eclaim.bs.payment.svc.IPaymentDetailSvc;
import hk.org.ha.eclaim.bs.payment.svc.IPaymentJobSvc;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Repository
public class PaymentBatchDaoImpl implements IPaymentBatchDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	IPaymentDetailSvc paymentDetailSvc;

	@Autowired
	IPaymentJobSvc paymentJobSvc;

	public PaymentBatchPo getPaymentBatchByBatchId(int batchId) {

		Query q = entityManager.createQuery("SELECT P FROM PaymentBatchPo P where batchId = :batchId",
				PaymentBatchPo.class);
		q.setParameter("batchId", batchId);
		
		PaymentBatchPo po = null;
		List<?> list = q.getResultList();
		if (list.size() > 0) {
			po = (PaymentBatchPo) list.get(0);
		}

		return po;
	}

	public ProjectPo getProjectDetailForBatchId(int batchId) {

		ProjectPo po = null;

		String sqlProj = "SELECT PJ FROM PaymentBatchPo PB \n" + "RIGHT JOIN ProjectPo PJ\n"
				+ "ON PB.projectId = PJ.projectId\n" + "WHERE PB.batchId = :batchId";

		Query q = entityManager.createQuery(sqlProj, ProjectPo.class);
		q.setParameter("batchId", batchId);

//		List<?> list = q.getResultList();
//
//		if (list.size() > 0) {
//			po = (ProjectPo) list.get(0);
//		}

		return po;
	}

	public List<String> getJobRanksForBatchId(int batchId) {

		/*
		 * String sqlProj = "SELECT PJ FROM PaymentBatchPo PB \n" +
		 * "RIGHT JOIN PaymentJobPo PJ\n" + "ON PB.batchId = PJ.batchId\n" +
		 * "WHERE PB.batchId = :batchId";
		 */

		String sqlProj = "SELECT PJ FROM PaymentJobPo PJ \n" + " WHERE PJ.batchId = :batchId";

		Query q = entityManager.createQuery(sqlProj, PaymentJobPo.class);
		q.setParameter("batchId", batchId);

		List<String> result = new ArrayList<String>();
		List<PaymentJobPo> tempList = q.getResultList();
		for (PaymentJobPo temp : tempList) {
			result.add(temp.getRank());
		}

		return result;
	}

	public String getDepartmentNameForBatchId(int batchId) {

		String sqlProj = "SELECT O FROM PaymentBatchPo PB \n" + "RIGHT JOIN OrganizationPo O\n"
				+ "ON PB.departmentId = O.organizationId\n" + "WHERE PB.batchId = :batchId";

		Query q = entityManager.createQuery(sqlProj, OrganizationPo.class);
		q.setParameter("batchId", batchId);

		List<?> list = q.getResultList();

		if (list.size() > 0) {
			OrganizationPo po = (OrganizationPo) list.get(0);
			return po.getName();
		}

		return "";
	}

	public List<PaymentBatchPo> getAllPaymentBatch() {
		List<PaymentBatchPo> paymentBatchList = new ArrayList<PaymentBatchPo>();
		Query q = entityManager.createQuery("SELECT P FROM PaymentBatchPo P", PaymentBatchPo.class);

		Iterator<?> result = q.getResultList().iterator();

		while (result.hasNext()) {
			paymentBatchList.add((PaymentBatchPo) result.next());
		}

		return paymentBatchList;
	}

	public List<PaymentEnquiryPo> getAllPaymentBatchForEnquiry(Integer deptId, String projectNm, Integer projectId,
			Integer empNo, String payMonth, String earnedMonth, Boolean unProcess, Boolean validated,
			Boolean partiallyValidated, Boolean pendingRollback, Boolean pendingTransfer, Boolean transferred,
			Boolean partiallyTransferred) {

		List<PaymentEnquiryPo> paymentBatchList = new ArrayList<PaymentEnquiryPo>();

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT   \n" + "A.BATCH_ID,   \n" + "B.PROJECT_NAME,  \n" + "D.NAME AS DEPARTMENT_NAME,  \n"
				+ "A.PAY_MONTH,   \n" + "A.LAST_UPDATE_DATE,  \n" + "E.STATUS_DESC AS STATUS,  \n"
				+ "A.TRANSFERRED,  \n" + "A.TOTAL_TXN,  \n" + "A.NO_OF_ON_HOLD_TXN,\n" + "A.PARENT_BATCH_ID,");
		if (StringUtils.isNotEmpty(earnedMonth) || empNo != null) {
			sqlBuilder.append("SUM(F.BATCH_ID), \n");
		}
		sqlBuilder.append("(SELECT LISTAGG(RANK, ', ')  WITHIN GROUP (ORDER BY BATCH_JOB_ID)"
				+ "FROM XXEAL_PAYMENT_JOB  WHERE BATCH_ID = A.BATCH_ID) AS JOBS \n");

		sqlBuilder.append(" FROM XXEAL_PAYMENT_BATCH A   \n" + "LEFT JOIN XXEAL_PROJECT B   \n"
				+ "ON (B.PROJECT_ID = A.PROJECT_ID)  \n" + "LEFT JOIN HR_ALL_ORGANIZATION_UNITS D   \n"
				+ "ON (D.ORGANIZATION_ID = A.DEPARTMENT_ID)   \n" + "LEFT JOIN XXEAL_STATUS_M E   \n"
				+ "ON (E.STATUS_CODE = A.STATUS) \n");

		if (!StringUtils.isEmpty(earnedMonth) || empNo != null) {
			sqlBuilder.append("LEFT JOIN XXEAL_PAYMENT_DETAIL F \n" + "ON (F.BATCH_ID = A.BATCH_ID)");
		}

		sqlBuilder.append("WHERE \n" + "A.BATCH_ID IS NOT NULL \n");

		if (deptId != null) {
			sqlBuilder.append(" AND A.DEPARTMENT_ID = " + deptId);
		}

		if (StringUtils.isNotBlank(projectNm)) {
			sqlBuilder.append(" AND D.NAME LIKE '%" + projectNm + "%'");
		}
		if (projectId != null) {
			sqlBuilder.append(" AND A.PROJECT_ID = " + projectId);
		}
		if (StringUtils.isNotEmpty(payMonth)) {
			sqlBuilder.append(" AND TO_CHAR(A.PAY_MONTH, 'YYYYMM') = " + payMonth);
		}
		if (StringUtils.isNotEmpty(earnedMonth)) {
			sqlBuilder.append(" AND TO_CHAR(F.DUTY_DATE, 'YYYYMM') = " + earnedMonth);
		}
		if (transferred != null && transferred) {
			sqlBuilder.append(" AND A.STATUS in ('PT', 'T', 'PR', 'PARTP') ");
		}
		if (empNo != null) {
			sqlBuilder.append(" AND F.EMP_NO = " + empNo);
		}
		if (StringUtils.isNotEmpty(earnedMonth) || empNo != null) {
			sqlBuilder.append(" GROUP BY A.BATCH_ID,B.PROJECT_NAME,D.NAME,A.PAY_MONTH,A.LAST_UPDATE_DATE,E.STATUS_DESC,"
					+ "A.TRANSFERRED,A.TOTAL_TXN,A.NO_OF_ON_HOLD_TXN,A.NO_OF_ON_HOLD_TXN,A.PARENT_BATCH_ID");
		}

		Query q = entityManager.createNativeQuery(sqlBuilder.toString());
		@SuppressWarnings("rawtypes")
		Iterator result = q.getResultList().iterator();

		while (result.hasNext()) {
			Object[] c = (Object[]) result.next();
			PaymentEnquiryPo paymentEnquiryPo = new PaymentEnquiryPo();

			if (c[0] != null) {
				paymentEnquiryPo.setBatchId(((BigDecimal) c[0]).intValue());
			}
			if (c[1] != null) {
				paymentEnquiryPo.setProjectName((String) c[1]);
			}
			if (c[2] != null) {
				paymentEnquiryPo.setDepartmentName((String) c[2]);
			}
			if (c[3] != null) {
				paymentEnquiryPo.setPayMonth((Date) c[3]);
			}
			if (c[4] != null) {
				paymentEnquiryPo.setLastUpdateDate((Date) c[4]);
			}
			if (c[5] != null) {
				paymentEnquiryPo.setStatus((String) c[5]);
			}
			if (c[6] != null) {
				BigDecimal txn = null;
				if(c[6] instanceof String) {
					txn = new BigDecimal((String)c[6]); 
				} else {
					txn = (BigDecimal) c[6];
				}
				paymentEnquiryPo.setTransferredTxn(txn.intValue());
			}
			if (c[7] != null) {
				paymentEnquiryPo.setTotalTxn(((BigDecimal) c[7]).intValue());
			}
			if (c[8] != null) {
				paymentEnquiryPo.setOnHoldTxn(((BigDecimal) c[8]).intValue());
			}
			if (c[9] != null) {
				paymentEnquiryPo.setParentBatchID(((BigDecimal) c[9]).intValue());
			}
			if (StringUtils.isNotEmpty(earnedMonth) || empNo != null) {
				if (c[11] != null) {
					paymentEnquiryPo.setJobs((String) c[11]);
				}
			} else {
				if (c[10] != null) {
					paymentEnquiryPo.setJobs((String) c[10]);
				}
			}

			paymentBatchList.add(paymentEnquiryPo);
		}
		return paymentBatchList;

	}

	public int insert(PaymentBatchPo paymentBatch, UserPo updateUser) {

		paymentBatch.setLastUpdateDate(new Date());
		paymentBatch.setCreatedBy(updateUser.getUserId());
		paymentBatch.setCreatedRoleId(updateUser.getCurrentRole());
		paymentBatch.setCreatedDate(new Date());
		paymentBatch.setUpdatedBy(updateUser.getUserId());
		paymentBatch.setUpdatedRoleId(updateUser.getCurrentRole());
		paymentBatch.setUpdatedDate(new Date());

		entityManager.persist(paymentBatch);
		return paymentBatch.getBatchId();
	}

	public int delete(PaymentBatchPo paymentBatch) {
		Integer batchId = paymentBatch.getBatchId();

		// TODO Remove XXEAL_PAYMENT_DETIAL
		List<PaymentDetailPo> lstDetailBatch = paymentDetailSvc.getDetailByBatchDetailId(batchId);
		for (PaymentDetailPo temp : lstDetailBatch) {
			PaymentDetailPo detailPo = entityManager.merge(temp);
			entityManager.remove(detailPo);
		}
		// TODO Remove XXEAL_PAYMENT_JOB
		List<PaymentJobPo> lstJobBatch = paymentJobSvc.getBatchJobList(batchId);
		paymentJobSvc.delete(lstJobBatch);

		// TODO Remove XXEAL_PAYMENT_BATCH
		PaymentBatchPo batchPo = entityManager.merge(paymentBatch);
		entityManager.remove(batchPo);

		return 0;
	}
	
	public int deleteDetail(PaymentBatchPo paymentBatch) {
		Integer batchId = paymentBatch.getBatchId();

		// TODO Remove XXEAL_PAYMENT_DETIAL
		List<PaymentDetailPo> lstDetailBatch = paymentDetailSvc.getDetailByBatchDetailId(batchId);
		for (PaymentDetailPo temp : lstDetailBatch) {
			PaymentDetailPo detailPo = entityManager.merge(temp);
			entityManager.remove(detailPo);
		}

		return 0;
	}

	public int updateStatus(PaymentBatchPo paymentBatch, String status, UserPo updateUser) {
		paymentBatch.setStatus(status);
		paymentBatch.setLastUpdateDate(new Date());
		paymentBatch.setUpdatedBy(updateUser.getUserId());
		paymentBatch.setUpdatedRoleId(updateUser.getCurrentRole());
		paymentBatch.setUpdatedDate(new Date());
		entityManager.merge(paymentBatch);

		return 0;
	}

	public int createSubBatch(PaymentBatchPo paymentBatch, UserPo updateUser) {

		paymentBatch.setLastUpdateDate(new Date());
		paymentBatch.setCreatedBy(updateUser.getUserId());
		paymentBatch.setCreatedRoleId(updateUser.getCurrentRole());
		paymentBatch.setCreatedDate(new Date());
		paymentBatch.setUpdatedBy(updateUser.getUserId());
		paymentBatch.setUpdatedRoleId(updateUser.getCurrentRole());
		paymentBatch.setUpdatedDate(new Date());

		entityManager.persist(paymentBatch);

		List<PaymentDetailPo> listPaymentDetail = paymentDetailSvc
				.getDetailByBatchDetailId(paymentBatch.getParentBatchId());
		for (PaymentDetailPo temp : listPaymentDetail) {
			if (!temp.getStatus().equals("T")) {
				temp.setStatus("U");
				temp.setBatchId(paymentBatch.getBatchId());
				temp.setUpdatedBy(updateUser.getUserId());
				temp.setUpdatedRoleId(updateUser.getCurrentRole());
				temp.setUpdatedDate(new Date());
				paymentDetailSvc.update(temp);
			}
		}
		return paymentBatch.getBatchId();
	}
	
	public List<OrganizationPo> getProjectDepartmentList(String term) {
		String sqlProj = "SELECT OP FROM ProjectPo PJ \n" + 
				"RIGHT JOIN OrganizationPo OP\n" + 
				"ON OP.organizationId = PJ.departmentId \n" + 
				"WHERE PJ.projectId is not null "
				+ " and upper(OP.name) like upper(:name)"
				+ " and rownum < 11"
				+ " order by OP.name";
		
		List<OrganizationPo> resultList = new ArrayList<OrganizationPo>();
		Query q = entityManager.createQuery(sqlProj, OrganizationPo.class);
		
		if (!"".equals(term)) {
			q.setParameter("name", term + "%");
		}
		
		List<OrganizationPo> tempList = q.getResultList();
		
		return tempList;
	}

	public List<?> getPaymentJobHours(int batchId) {
		String sqlJob = "select DISTINCT batch_id, prj_job_group_id,ranks,appr_hour,used_hour,(appr_hour - used_hour) \n" + 
				"avail_hour,batch_hour from V_PAYMENT_JOB_HOURS \n" + 
				"where batch_id = :batchId order by batch_id, prj_job_group_id";
		
		Query q = entityManager.createNativeQuery(sqlJob);
		q.setParameter("batchId", batchId);
		
		List<?> tempList = q.getResultList();
		
		return tempList;
	}
}
