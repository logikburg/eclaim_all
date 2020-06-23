package hk.org.ha.eclaim.bs.batch.dao;

import java.util.Date;
import java.util.List;

import hk.org.ha.eclaim.bs.batch.po.BatchLogPo;

public interface IRequestBatchDao {
	
	public int updateEndDate();

	public int syncPost();
	
	public int supplePromotion();
	
	public List<String> getRptBatchUserAccess(Date lastUpdateDtFrom);
	
	public BatchLogPo createBatchLog(String batchName);
	
	public int updateBatchLog(BatchLogPo batchLogPo, String batchResult);
	
	public int createBatchLogDtl(Integer batchUid, String postId, String batchDtlResult);

}
