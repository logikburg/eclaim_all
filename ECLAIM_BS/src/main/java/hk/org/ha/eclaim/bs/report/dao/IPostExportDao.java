package hk.org.ha.eclaim.bs.report.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import hk.org.ha.eclaim.bs.request.po.MPRSPostFundingSnapPo;
import hk.org.ha.eclaim.bs.report.po.PostExportPo;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;

public interface IPostExportDao {

	public abstract List<PostExportPo> getDataExportContent(List<DataAccessPo> dataAccessList, Date asAtDate, Map<String, String> condCriteria);
	
	// Added for UT30067
	public abstract List<PostExportPo> getDataExportContentByPostId(List<DataAccessPo> dataAccessList, String postId);

	public abstract int getMaxNumberFundingSrc(List<DataAccessPo> dataAccessList, Date asAtDate, Map<String, String> condCriteria);
	
	public abstract int getMaxNumberFundingSrcByPostId(List<DataAccessPo> dataAccessList, String postId);

	public abstract List<MPRSPostFundingSnapPo> getPostFundingSnap(List<DataAccessPo> dataAccessList, Date asAtDate, Map<String, String> condCriteria, int i);

	public abstract List<MPRSPostFundingSnapPo> getPostFundingSnapByPostId(List<DataAccessPo> dataAccessList, String postId, int i);
	
}
