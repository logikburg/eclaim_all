package hk.org.ha.eclaim.bs.hcm.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="V_HCM_LOCATION")
public class HCMLocationPo {
	@Id
	@Column(name="location_id")
	private String locationId;
	
	@Column(name="location_code")
	private String locationCode;
	
	@Column(name="description")
	private String description;

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
