package hk.org.ha.eclaim.bs.request.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.request.po.MPRSSearchCriteria;
import hk.org.ha.eclaim.bs.request.po.PostMasterPo;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IMPRSPostDao {
	public List<PostMasterPo> getMPRSPost(List<DataAccessPo> dataAccessList, MPRSSearchCriteria mprSearchRequest,
			String userId, String roleId);

	public List<PostMasterPo> getVacancyMPRSPost(List<DataAccessPo> dataAccessList, MPRSSearchCriteria mprSearchRequest,
			String userId, String roleId);

	public List<PostMasterPo> getMPRSResidentPost(List<DataAccessPo> dataAccessList,
			MPRSSearchCriteria mprSearchRequest, String userId, String roleId);

	public PostMasterPo getPostByPostUid(int postNo);

	public List<PostMasterPo> getMPRSPostTimeLimit(List<DataAccessPo> dataAccessList,
			MPRSSearchCriteria mprSearchRequest);
	
	public List<PostMasterPo> getMPRSPostTimeLimitAndFTELessThanOne(List<DataAccessPo> dataAccessList, MPRSSearchCriteria mprSearchRequest);

	public PostMasterPo getMPRSDefaultByHCMPositionId(int hcmPositionId);
	
	// public int getMPRSByHCMPositionId(String hcmPositionId, String effectiveDate);

	// Added for CC177246
	public void updateClusterRemark(int postUid, String clusterRef, String clusterRemark, UserPo user);

	// Added for CR0355
	public List<PostMasterPo> getMPRSPostWithoutSupplementPromotions(List<DataAccessPo> dataAccessList, MPRSSearchCriteria mprSearchRequest, String userId, String roleId);

	public List<String> getShortFallList();

}
