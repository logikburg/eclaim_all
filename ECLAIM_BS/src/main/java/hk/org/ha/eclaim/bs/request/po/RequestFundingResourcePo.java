package hk.org.ha.eclaim.bs.request.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="RQ_FUNDING_RESOURCE")
public class RequestFundingResourcePo extends AbstractBasePo implements Serializable {
	private static final long serialVersionUID = -4023115231193935866L;

	@Id
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REQUEST_POST_UID")
	private RequestPostPo requestPosition;

	@Column(name="resources_support_fr_ext")
	private String resourcesSupportFrExt;
	
	@Column(name="resources_support_remark")
	private String resourcesSupportRemark;
	
	public String getResourcesSupportFrExt() {
		return resourcesSupportFrExt;
	}

	public void setResourcesSupportFrExt(String resourcesSupportFrExt) {
		this.resourcesSupportFrExt = resourcesSupportFrExt;
	}

	public String getResourcesSupportRemark() {
		return resourcesSupportRemark;
	}

	public void setResourcesSupportRemark(String resourcesSupportRemark) {
		this.resourcesSupportRemark = resourcesSupportRemark;
	}

	public RequestPostPo getRequestPosition() {
		return requestPosition;
	}

	public void setRequestPosition(RequestPostPo requestPosition) {
		this.requestPosition = requestPosition;
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

		RequestFundingResourcePo other = (RequestFundingResourcePo) obj;

		if (requestPosition != other.requestPosition) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((requestPosition == null) ? 0 : requestPosition.hashCode());
		result = prime * result + prime;
		return result;
	}
}
