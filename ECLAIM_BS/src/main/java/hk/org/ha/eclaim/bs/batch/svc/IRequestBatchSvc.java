package hk.org.ha.eclaim.bs.batch.svc;

import hk.org.ha.eclaim.bs.batch.po.BatchLogPo;

public interface IRequestBatchSvc {
	
	public int updateEndDate();
	
	public int syncPost();
	
	public int supplePromotion();
	
	public int getRptBatchUserAccess(String reportBatchDir);
	
	public BatchLogPo createBatchLog(String batchName);
	
	public int updateBatchLog(BatchLogPo batchLogPo, String batchResult);
	
	public int createBatchLogDtl(Integer batchUid, String postId, String batchDtlResult);
	
}
