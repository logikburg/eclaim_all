package hk.org.ha.eclaim.bs.cs.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_CLUSTER")
public class ClusterPo extends AbstractBasePo {
	
	private static final long serialVersionUID = -4121533815431442746L;

	@Id
	@Column(name="CLUSTER_CODE")
	private String clusterCode;
	
	@Column(name="CLUSTER_NAME")
	private String clusterName;
	
	
	public String getClusterCode() {
		return clusterCode;
	}

	public void setClusterCode(String clusterCode) {
		this.clusterCode = clusterCode;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
}