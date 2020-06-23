package hk.org.ha.eclaim.bs.cs.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="HR_ALL_ORGANIZATION_UNITS")
public class OrganizationPo{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6375379796255818830L;

	@Id
	@Column(name="ORGANIZATION_ID")
	private Integer organizationId;
	
	@Column(name="BUSINESS_GROUP_ID")
	private Integer businessGroupId;
	
	@Column(name="COST_ALLOCATION_KEYFLEX_ID")
	private Integer costAllocationKeyflexId;
	
	@Column(name="LOCATION_ID")
	private Integer locationId;
	
	@Column(name="SOFT_CODING_KEYFLEX_ID")
	private Integer softCodingKeyflexId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATE_FROM")
	private Date dateFrom;
	
	@Column(name="name")
	private String name;
	
	@Column(name="DATE_TO")
	private Date dateTo;
	
	@Column(name="INTERNAL_EXTERNAL_FLAG")
	private String internalEXternalFlag;
	
	@Column(name="INTERNAL_ADDRESS_LINE")
	private String internalAddressLine;
	
	@Column(name="TYPE")
	private String type;
	
	@Column (name="REQUEST_ID")
	private Integer requestId;
	
	@Column(name="PROGRAM_APPLICATION_ID")
	private Integer programApplicationId;
	
	@Column(name="PROGRAM_ID")
	private Integer programId;
	
	@Column(name="PROGRAM_UPDATE_DATE")
	private Date programUpdateDate; 
	
	@Column(name="ATTRIBUTE_CATEGORY")
	private String attributeCategory;
	
	@Column(name="ATTRIBUTE1")
	private String attribute1;
	
	@Column(name="ATTRIBUTE2")
	private String clusId;
	
	@Column(name="ATTRIBUTE3")
	private String attribute3;
	
	@Column(name="ATTRIBUTE4")
	private String attribute4;
	
	@Column(name="ATTRIBUTE5")
	private String attribute5;
	
	@Column(name="ATTRIBUTE6")
	private String attribute6;
	
	@Column(name="ATTRIBUTE7")
	private String attribute7;
	
	@Column(name="ATTRIBUTE8")
	private String attribute8;
	
	@Column(name="ATTRIBUTE9")
	private String attribute9;
	
	@Column(name="ATTRIBUTE10")
	private String attribute10;
	
	@Column(name="ATTRIBUTE11")
	private String attribute11;

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getBusinessGroupId() {
		return businessGroupId;
	}

	public void setBusinessGroupId(Integer businessGroupId) {
		this.businessGroupId = businessGroupId;
	}

	public Integer getCostAllocationKeyflexId() {
		return costAllocationKeyflexId;
	}

	public void setCostAllocationKeyflexId(Integer costAllocationKeyflexId) {
		this.costAllocationKeyflexId = costAllocationKeyflexId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Integer getSoftCodingKeyflexId() {
		return softCodingKeyflexId;
	}

	public void setSoftCodingKeyflexId(Integer softCodingKeyflexId) {
		this.softCodingKeyflexId = softCodingKeyflexId;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public String getInternalEXternalFlag() {
		return internalEXternalFlag;
	}

	public void setInternalEXternalFlag(String internalEXternalFlag) {
		this.internalEXternalFlag = internalEXternalFlag;
	}

	public String getInternalAddressLine() {
		return internalAddressLine;
	}

	public void setInternalAddressLine(String internalAddressLine) {
		this.internalAddressLine = internalAddressLine;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public Integer getProgramApplicationId() {
		return programApplicationId;
	}

	public void setProgramApplicationId(Integer programApplicationId) {
		this.programApplicationId = programApplicationId;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public Date getProgramUpdateDate() {
		return programUpdateDate;
	}

	public void setProgramUpdateDate(Date programUpdateDate) {
		this.programUpdateDate = programUpdateDate;
	}

	public String getAttributeCategory() {
		return attributeCategory;
	}

	public void setAttributeCategory(String attributeCategory) {
		this.attributeCategory = attributeCategory;
	}

	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

//	public String getAttribute2() {
//		return attribute2;
//	}
//
//	public void setAttribute2(String attribute2) {
//		this.attribute2 = attribute2;
//	}

	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}

	public String getAttribute4() {
		return attribute4;
	}

	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}

	public String getAttribute5() {
		return attribute5;
	}

	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}

	public String getAttribute6() {
		return attribute6;
	}

	public void setAttribute6(String attribute6) {
		this.attribute6 = attribute6;
	}

	public String getAttribute7() {
		return attribute7;
	}

	public void setAttribute7(String attribute7) {
		this.attribute7 = attribute7;
	}

	public String getAttribute8() {
		return attribute8;
	}

	public void setAttribute8(String attribute8) {
		this.attribute8 = attribute8;
	}

	public String getAttribute9() {
		return attribute9;
	}

	public void setAttribute9(String attribute9) {
		this.attribute9 = attribute9;
	}

	public String getAttribute10() {
		return attribute10;
	}

	public void setAttribute10(String attribute10) {
		this.attribute10 = attribute10;
	}

	public String getAttribute11() {
		return attribute11;
	}

	public void setAttribute11(String attribute11) {
		this.attribute11 = attribute11;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getClusId() {
		return clusId;
	}

	public void setClusId(String clusId) {
		this.clusId = clusId;
	}
	
	
}
