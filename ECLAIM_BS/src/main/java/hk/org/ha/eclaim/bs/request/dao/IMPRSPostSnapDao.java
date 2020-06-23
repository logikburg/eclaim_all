package hk.org.ha.eclaim.bs.request.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.request.po.EnquiryWebPo;
import hk.org.ha.eclaim.bs.request.po.MPRSPostFundingResourceSnapPo;
import hk.org.ha.eclaim.bs.request.po.MPRSPostFundingSnapPo;
import hk.org.ha.eclaim.bs.request.po.MPRSPostSnapPo;
import hk.org.ha.eclaim.bs.request.po.PostMasterPo;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IMPRSPostSnapDao {
	
	public MPRSPostSnapPo getMPRSPostSnapDetail(int postSnapUid);
	
	// public MPRSPostFundingSnapPo getMPRSPostFundingSnapDetail(int postSnapUid);
	public List<MPRSPostFundingSnapPo> getMPRSPostFundingSnapDetail(int postSnapUid);

	public MPRSPostFundingResourceSnapPo getMPRSPostFundingResourceSnapDetail(int postSnapUid);
	
	public List<PostMasterPo> getMPRSPostSnap(List<DataAccessPo> dataAccessList, EnquiryWebPo vo, String userId, String roleId);
	
	public int getActivePostCountByHcmPositionId(String hcmPositionId, String effectiveStartDate, String effectiveEndDate);
	// Added for CC177246
	public void updateClusterRemark(String searchPostSnapUid, String clusterRef, String clusterRemark, UserPo user);

	// Added for CC177246
	public List<MPRSPostSnapPo> getMPRSPostSnapDetailListByPostUid(int postUid);

	// Added for ST08733
	public boolean isExistingPost(String postId);
}
