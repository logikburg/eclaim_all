package hk.org.ha.eclaim.bs.claim.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

//@Entity
@Table(name="XXEC_CLAIM_DETAIL")
public class ClaimDetailsPo extends AbstractBasePo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6542499715324686340L;

	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="XXEC_CLAIM_DETAIL_ID_SEQ")
	@SequenceGenerator(name="XXEC_CLAIM_DETAIL_ID_SEQ", sequenceName="XXEC_CLAIM_DETAIL_ID_SEQ", allocationSize=1)
	@Id
	@Column(name="XXEC_CLAIM_DETAIL_ID")
	private Integer claimDetailId;
	
	@Column(name="CLAIM_ID")
	private Integer claimID;
	
	@Column(name="ATTENDNECE_ID")
	private Integer attendneceId;
	
	@Column(name="ATTENDNECE_VER_ID")
	private Integer attendneceVerId;
	
	@Column(name="SESSION_APPLICATION_ID")
	private Integer sessionApplicationId;
	
	@Column(name="ELEMENT_ENTRY_ID")
	private Integer elementEntryId;
	
	@Column(name="PAYROLL")
	private String payroll;
	
	@Column(name="DUTY_DATE")
	private Date dutyDate;
	
	@Column(name="JOB")
	private String job;
	
	@Column(name="LOCATION_ID")
	private Integer locationId;
	
	@Column(name="EMP_NO")
	private String empNo;
	
	@Column(name="ASG_NO")
	private String asgNo;
	
	@Column(name="COA_INST")
	private String coaInst;
	
	@Column(name="COA_FUND")
	private String coaFund;
	
	@Column(name="COA_SECTION")
	private String cosSection;
	
	@Column(name="COA_ANALYSTIC")
	private String coaAnalystic;
	
	@Column(name="COA_TYPE")
	private String coaType;
	
	@Column(name="HOUR_TYPE")
	private String hourType;
	
	@Column(name="WOKR_HOUR")
	private Integer totalHour;
	
	@Column(name="STATUS")
	private String status;

	public Integer getClaimDetailId() {
		return claimDetailId;
	}

	public void setClaimDetailId(Integer claimDetailId) {
		this.claimDetailId = claimDetailId;
	}

	public Integer getClaimID() {
		return claimID;
	}

	public void setClaimID(Integer claimID) {
		this.claimID = claimID;
	}

	public Integer getAttendneceId() {
		return attendneceId;
	}

	public void setAttendneceId(Integer attendneceId) {
		this.attendneceId = attendneceId;
	}

	public Integer getAttendneceVerId() {
		return attendneceVerId;
	}

	public void setAttendneceVerId(Integer attendneceVerId) {
		this.attendneceVerId = attendneceVerId;
	}

	public Integer getSessionApplicationId() {
		return sessionApplicationId;
	}

	public void setSessionApplicationId(Integer sessionApplicationId) {
		this.sessionApplicationId = sessionApplicationId;
	}

	public Integer getElementEntryId() {
		return elementEntryId;
	}

	public void setElementEntryId(Integer elementEntryId) {
		this.elementEntryId = elementEntryId;
	}

	public String getPayroll() {
		return payroll;
	}

	public void setPayroll(String payroll) {
		this.payroll = payroll;
	}

	public Date getDutyDate() {
		return dutyDate;
	}

	public void setDutyDate(Date dutyDate) {
		this.dutyDate = dutyDate;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getAsgNo() {
		return asgNo;
	}

	public void setAsgNo(String asgNo) {
		this.asgNo = asgNo;
	}

	public String getCoaInst() {
		return coaInst;
	}

	public void setCoaInst(String coaInst) {
		this.coaInst = coaInst;
	}

	public String getCoaFund() {
		return coaFund;
	}

	public void setCoaFund(String coaFund) {
		this.coaFund = coaFund;
	}

	public String getCosSection() {
		return cosSection;
	}

	public void setCosSection(String cosSection) {
		this.cosSection = cosSection;
	}

	public String getCoaAnalystic() {
		return coaAnalystic;
	}

	public void setCoaAnalystic(String coaAnalystic) {
		this.coaAnalystic = coaAnalystic;
	}

	public String getCoaType() {
		return coaType;
	}

	public void setCoaType(String coaType) {
		this.coaType = coaType;
	}

	public String getHourType() {
		return hourType;
	}

	public void setHourType(String hourType) {
		this.hourType = hourType;
	}

	public Integer getTotalHour() {
		return totalHour;
	}

	public void setTotalHour(Integer totalHour) {
		this.totalHour = totalHour;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
