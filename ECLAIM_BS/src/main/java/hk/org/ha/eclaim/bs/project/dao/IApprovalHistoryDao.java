package hk.org.ha.eclaim.bs.project.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.project.po.ApprovalHistoryPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IApprovalHistoryDao {

	public ApprovalHistoryPo getApprovalHistoryPoByUid(int apprHistUid);
	public List<ApprovalHistoryPo> getApprovalHistoryListByProjectId(int projectId);
	public List<ApprovalHistoryPo> getApprovalHistoryListByProjectId(int projectId, int verId);
	public void deleteApprHistPo(int apprHistUid);
	public String getActionNameByCode(String actionCode);
	public int insertApprHistPo(ApprovalHistoryPo apprHist, UserPo updateUser);
	public void updateApprHistPo(ApprovalHistoryPo apprHist, UserPo updateUser);
}
