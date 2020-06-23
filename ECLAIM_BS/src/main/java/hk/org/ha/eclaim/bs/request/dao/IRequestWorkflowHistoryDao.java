package hk.org.ha.eclaim.bs.request.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.request.po.RequestWorkflowHistoryPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IRequestWorkflowHistoryDao {

	List<RequestWorkflowHistoryPo> getRequestHistory(int requestUid);
	
	void insert(RequestWorkflowHistoryPo history, UserPo updateUser);
	
}
