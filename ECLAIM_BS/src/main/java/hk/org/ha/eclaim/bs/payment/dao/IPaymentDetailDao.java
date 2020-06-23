package hk.org.ha.eclaim.bs.payment.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.payment.po.PaymentDetailPo;

public interface IPaymentDetailDao {

	public List<PaymentDetailPo> getAllPaymentDetails();

	public List<PaymentDetailPo> getPaymentDetailByBatchId(int Id);

	public int insert(PaymentDetailPo po);

	public List<PaymentDetailPo> insertAll(List<PaymentDetailPo> lstPo);

	public void update(PaymentDetailPo pdPo);
	
	public void delete(PaymentDetailPo pdPo);
}
