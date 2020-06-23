package hk.org.ha.eclaim.bs.request.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.request.po.RequestWorkflowRoutePo;
import hk.org.ha.eclaim.bs.security.po.RolePo;

public interface IRequestWorkflowRouteDao {
	public List<RolePo> getNextGroup(String staffGroupCode, String requestType, String currentStatus);
	public List<RolePo> getNextGroup(String staffGroupCode, String requestType, String currentStatus, String mgReviewInd);
	public List<RolePo> getPreviousGroup(String staffGroupCode, String requestType, String currentStatus);
	public List<RolePo> getPreviousGroup(String staffGroupCode, String requestType, String currentStatus, String mgReviewInd);
	public RequestWorkflowRoutePo getDefaultRoute(String staffGroupCode, String requestType, String currentStatus);
	public RequestWorkflowRoutePo getDefaultRoute(String staffGroupCode, String requestType, String currentStatus, String mgReviewInd);
}
