package hk.org.ha.eclaim.bs.payment.dao;

import java.util.HashMap;
import java.util.List;

import hk.org.ha.eclaim.bs.payment.po.PaymentJobPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IPaymentJobDao {

	public String getBatchJobListByString(Integer batcdId);
	
	public List<PaymentJobPo> getBatchJobList(Integer batchId);
	
	public void insert(Integer batchId, HashMap<String, Integer> rankList, UserPo user);
	
	public void delete(List<PaymentJobPo> rankList);
}


