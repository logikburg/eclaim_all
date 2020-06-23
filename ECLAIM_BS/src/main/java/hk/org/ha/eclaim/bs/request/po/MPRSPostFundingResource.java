package hk.org.ha.eclaim.bs.request.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity 
@Table(name="POST_FUNDING_RESOURCE")
public class MPRSPostFundingResource extends AbstractBasePo implements Serializable {
	private static final long serialVersionUID = 1363772468090810551L;

	@Id
	@OneToOne
	@JoinColumn(name="POST_UID")
	private PostMasterPo post;
	
	@Column(name="RESOURCES_SUPPORT_FR_EXT")
	private String resourceSupportFromExt;
	
	@Column(name="RESOURCES_SUPPORT_REMARK")
	private String resourceSupportRemark;

	public PostMasterPo getPost() {
		return post;
	}

	public void setPost(PostMasterPo post) {
		this.post = post;
	}

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		MPRSPostFundingResource other = (MPRSPostFundingResource) obj;

		if (post != other.post) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((post == null) ? 0 : post.hashCode());
		result = prime * result + prime;
		return result;
	}
}
