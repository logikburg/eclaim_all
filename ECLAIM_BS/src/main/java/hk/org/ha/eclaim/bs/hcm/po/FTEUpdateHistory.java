package hk.org.ha.eclaim.bs.hcm.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="HCM_FTE_UPDATE_HIST")
public class FTEUpdateHistory extends AbstractBasePo {
	
	private static final long serialVersionUID = 1309014195127519323L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HCM_FTE_UPDATE_HIST_UID_SEQ")
	@SequenceGenerator(name="HCM_FTE_UPDATE_HIST_UID_SEQ", sequenceName="HCM_FTE_UPDATE_HIST_UID_SEQ", allocationSize=1)
	@Column(name="FTE_UPDATE_HIST_UID")
	private Integer updateUid;
	 
	@Column(name="hcm_position_id")
	private Integer hcmPositionId;
	
	@Column(name="new_fte")
	private Double newFte;
	
	@Column(name="update_reason")
	private String updateReason;
	
	public void setUpdateUid(Integer updateUid) {
		this.updateUid = updateUid;
	}

	public Integer getHcmPositionId() {
		return hcmPositionId;
	}

	public void setHcmPositionId(Integer hcmPositionId) {
		this.hcmPositionId = hcmPositionId;
	}

	public Double getNewFte() {
		return newFte;
	}

	public void setNewFte(Double newFte) {
		this.newFte = newFte;
	}

	public String getUpdateReason() {
		return updateReason;
	}

	public void setUpdateReason(String updateReason) {
		this.updateReason = updateReason;
	}
}
