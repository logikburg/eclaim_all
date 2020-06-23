package hk.org.ha.eclaim.bs.payment.svc.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.cs.po.OrganizationPo;
import hk.org.ha.eclaim.bs.payment.dao.IPaymentBatchDao;
import hk.org.ha.eclaim.bs.payment.po.PaymentBatchPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentEnquiryPo;
import hk.org.ha.eclaim.bs.payment.svc.IPaymentBatchSvc;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Service("paymentBatchSvc")
public class PaymentBatchSvcImpl implements IPaymentBatchSvc {

	@Autowired
	IPaymentBatchDao paymentBatchDao;

	public List<PaymentBatchPo> getAllPaymentBatch() {
		return paymentBatchDao.getAllPaymentBatch();
	}

	public List<PaymentEnquiryPo> getAllPaymentBatchForEnquiry(Integer deptId, String projectNm, Integer projectId,
			Integer empNo, String payMonth, String earnedMonth, Boolean unProcess, Boolean validated,
			Boolean partiallyValidated, Boolean pendingRollback, Boolean pendingTransfer, Boolean transferred,
			Boolean partiallyTransferred) {
		return paymentBatchDao.getAllPaymentBatchForEnquiry(deptId, projectNm, projectId, empNo, payMonth, earnedMonth,
				unProcess, validated, partiallyValidated, pendingRollback, pendingTransfer, transferred,
				partiallyTransferred);
	}

	public PaymentBatchPo getPaymentBatchByBatchId(int batchId) {
		return paymentBatchDao.getPaymentBatchByBatchId(batchId);
	}

	public ProjectPo getProjectDetail(int batchId) {
		return paymentBatchDao.getProjectDetailForBatchId(batchId);
	}

	public String getDepartmentName(int batchId) {
		return paymentBatchDao.getDepartmentNameForBatchId(batchId);
	}

	public List<String> getJobRanks(int batchId) {
		return paymentBatchDao.getJobRanksForBatchId(batchId);
	}

	@Transactional
	public int insert(PaymentBatchPo paymentBatch, UserPo updateUser) {
		return paymentBatchDao.insert(paymentBatch, updateUser);
	}

	@Transactional
	public int delete(PaymentBatchPo paymentBatch) {
		return paymentBatchDao.delete(paymentBatch);
	}

	@Transactional
	public int deleteDetail(PaymentBatchPo paymentBatch) {
		return paymentBatchDao.deleteDetail(paymentBatch);
	}

	@Transactional
	public int updateStatus(PaymentBatchPo paymentBatch, String status, UserPo updateUser) {
		return paymentBatchDao.updateStatus(paymentBatch, status, updateUser);
	}

	@Transactional
	public int createSubBatch(PaymentBatchPo paymentBatch, UserPo updateUser) {
		return paymentBatchDao.createSubBatch(paymentBatch, updateUser);
	}

	public List<OrganizationPo> getProjectDepartmentList(String term) {
		return paymentBatchDao.getProjectDepartmentList(term);
	}

	public List<?> getPaymentJobHours(int batchId) {
		return paymentBatchDao.getPaymentJobHours(batchId);
	}

}