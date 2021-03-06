package hk.org.ha.eclaim.bs.payment.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.OrganizationPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentBatchPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentEnquiryPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IPaymentBatchDao {

	public List<PaymentBatchPo> getAllPaymentBatch();

	public List<PaymentEnquiryPo> getAllPaymentBatchForEnquiry(Integer deptId, String projectNm, Integer projectId, Integer empNo, String payMonth, String earnedMonth,
			Boolean unProcess, Boolean validated, Boolean partiallyValidated, 
			Boolean pendingRollback, Boolean pendingTransfer, Boolean transferred, Boolean partiallyTransferred);

	public PaymentBatchPo getPaymentBatchByBatchId(int batchId);
	
	public List<String> getJobRanksForBatchId(int batchId);
	
	public String getDepartmentNameForBatchId(int batchId);
	
	public ProjectPo getProjectDetailForBatchId(int batchId);
	
	public int insert(PaymentBatchPo paymentBatch, UserPo updateUser);
	
	public int delete(PaymentBatchPo paymentBatch);
	
	public int deleteDetail(PaymentBatchPo paymentBatch);
	
	public int updateStatus(PaymentBatchPo paymentBatch, String status, UserPo updateUser);
	
	public int createSubBatch(PaymentBatchPo paymentBatch, UserPo updateUser);
	
	public List<OrganizationPo> getProjectDepartmentList(String term);
	
	public List<?> getPaymentJobHours(int batchId);
}
