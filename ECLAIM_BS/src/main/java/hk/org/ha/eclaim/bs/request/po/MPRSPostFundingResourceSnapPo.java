package hk.org.ha.eclaim.bs.request.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity 
@Table(name="POST_FUNDING_RESOURCE_SNAP")
public class MPRSPostFundingResourceSnapPo extends AbstractBasePo implements Serializable {
	private static final long serialVersionUID = 1363772468090810551L;

	@Id
	@Column(name="POST_SNAP_UID")
	private Integer postSnapUid;
	
	@Column(name="RESOURCES_SUPPORT_FR_EXT")
	private String resourceSupportFromExt;
	
	@Column(name="RESOURCES_SUPPORT_REMARK")
	private String resourceSupportRemark;

	public String getResourceSupportFromExt() {
		return resourceSupportFromExt;
	}

	public void setResourceSupportFromExt(String resourceSupportFromExt) {
		this.resourceSupportFromExt = resourceSupportFromExt;
	}

	public String getResourceSupportRemark() {
		return resourceSupportRemark;
	}

	public void setResourceSupportRemark(String resourceSupportRemark) {
		this.resourceSupportRemark = resourceSupportRemark;
	}

	public Integer getPostSnapUid() {
		return postSnapUid;
	}

	public void setPostSnapUid(Integer postSnapUid) {
		this.postSnapUid = postSnapUid;
	}
}
