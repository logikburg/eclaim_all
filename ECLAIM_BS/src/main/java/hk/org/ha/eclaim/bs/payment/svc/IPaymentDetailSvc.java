package hk.org.ha.eclaim.bs.payment.svc;

import java.io.OutputStream;
import java.util.List;

import hk.org.ha.eclaim.bs.payment.po.PaymentBatchPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentBatchTo;
import hk.org.ha.eclaim.bs.payment.po.PaymentDetailPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentDetailVo;

public interface IPaymentDetailSvc {

	public List<PaymentBatchPo> getAllDetail();

	public List<PaymentDetailPo> getDetailByBatchDetailId(int batchDetailId);

	public int insert();

	public int insertAll(List<PaymentDetailPo> lstPaymentDetail);

	public void update(PaymentDetailPo paymentDetailPo);

	public List<PaymentDetailPo> prepareBatchFromXlsFile(String fn);

	public PaymentDetailVo prepareBatchFromXls(PaymentBatchPo batchPo, String fn) throws Exception;

	public void prepareXlsForOutStream(OutputStream out, PaymentBatchPo batchPo) throws Exception;
	
	public void prepareXlsForOutStreamWithDetails(OutputStream out, PaymentBatchPo batchPo, List<PaymentBatchTo> tmpBatch) throws Exception;

}