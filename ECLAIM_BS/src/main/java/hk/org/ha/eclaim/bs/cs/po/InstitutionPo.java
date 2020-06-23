package hk.org.ha.eclaim.bs.cs.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_INST")
public class InstitutionPo extends AbstractBasePo {
	
	private static final long serialVersionUID = -7288372959886776934L;

	@Id
	@Column(name="INST_CODE")
	private String instCode;
	
	@Column(name="INST_NAME")
	private String instName;

	@Column(name="CLUSTER_CODE")
	private String clusterCode;
	
	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getClusterCode() {
		return clusterCode;
	}

	public void setClusterCode(String clusterCode) {
		this.clusterCode = clusterCode;
	}
}
