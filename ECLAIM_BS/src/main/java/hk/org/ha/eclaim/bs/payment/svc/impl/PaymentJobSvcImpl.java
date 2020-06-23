package hk.org.ha.eclaim.bs.payment.svc.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.payment.dao.IPaymentJobDao;
import hk.org.ha.eclaim.bs.payment.po.PaymentJobPo;
import hk.org.ha.eclaim.bs.payment.svc.IPaymentJobSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Service("paymentJobSvc")
public class PaymentJobSvcImpl implements IPaymentJobSvc {

	@Autowired
	IPaymentJobDao paymentJobDao;
	
	public String getBatchJobListByString(Integer batcdId) {
		return paymentJobDao.getBatchJobListByString(batcdId);
	}

	public List<PaymentJobPo> getBatchJobList(Integer batchId) {
		return paymentJobDao.getBatchJobList(batchId);
	}
	
	@Transactional
	public void insert(Integer batchId, HashMap<String, Integer> rankList, UserPo user) {
		paymentJobDao.insert(batchId, rankList, user);
	}

	@Transactional
	public void delete(List<PaymentJobPo> jobList) {
		paymentJobDao.delete(jobList);
	}

}