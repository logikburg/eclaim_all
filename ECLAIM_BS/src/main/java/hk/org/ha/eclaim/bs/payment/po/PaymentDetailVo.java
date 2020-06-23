package hk.org.ha.eclaim.bs.payment.po;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PaymentDetailVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer batchId;

	private List<PaymentDetailPo> lstBatchRecords;

	private Map<String, PaymentBatchTo> mapPaymentBatch = new TreeMap<String, PaymentBatchTo>();

	private List<String> errorMsg;

	private List<String> warningMsg;

	private List<String> rankList;
	
	private Integer totalCnt;
	
	private Integer validateCnt;
	
	private Integer transferCnt;
	
	private Integer outstandingCnt;
	
	private List<PaymentJobHoursVo> paymentJobHoursVo;

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public List<PaymentDetailPo> getLstBatchRecords() {
		return lstBatchRecords;
	}

	public void setLstBatchRecords(List<PaymentDetailPo> lstBatchRecords) {
		this.lstBatchRecords = lstBatchRecords;
	}

	public List<String> getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(List<String> errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List<String> getWarningMsg() {
		return warningMsg;
	}

	public void setWarningMsg(List<String> warningMsg) {
		this.warningMsg = warningMsg;
	}

	public List<String> getRankList() {
		return rankList;
	}

	public void setRankList(List<String> rankList) {
		this.rankList = rankList;
	}

	public Map<String, PaymentBatchTo> getMapPaymentBatch() {
		return mapPaymentBatch;
	}

	public void setMapPaymentBatch(Map<String, PaymentBatchTo> mapPaymentBatch) {
		this.mapPaymentBatch = mapPaymentBatch;
	}

	public Integer getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(Integer totalCnt) {
		this.totalCnt = totalCnt;
	}

	public Integer getValidateCnt() {
		return validateCnt;
	}

	public void setValidateCnt(Integer validateCnt) {
		this.validateCnt = validateCnt;
	}

	public Integer getTransferCnt() {
		return transferCnt;
	}

	public void setTransferCnt(Integer transferCnt) {
		this.transferCnt = transferCnt;
	}

	public Integer getOutstandingCnt() {
		return outstandingCnt;
	}

	public void setOutstandingCnt(Integer outstandingCnt) {
		this.outstandingCnt = outstandingCnt;
	}

	public List<PaymentJobHoursVo> getPaymentJobHoursVo() {
		return paymentJobHoursVo;
	}

	public void setPaymentJobHoursVo(List<PaymentJobHoursVo> paymentJobHoursVo) {
		this.paymentJobHoursVo = paymentJobHoursVo;
	}

	
}
