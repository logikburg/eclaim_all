package hk.org.ha.eclaim.bs.payment.po;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

// We require to add a new field to add remark for the records i.e ERROR/
@Entity
@Table(name = "XXEAL_PAYMENT_DETAIL")
public class PaymentDetailPo extends AbstractBasePo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6542499715324686340L;

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XXEAL_BATCH_DETAIL_ID_SEQ")
	@SequenceGenerator(name = "XXEAL_BATCH_DETAIL_ID_SEQ", sequenceName = "XXEAL_BATCH_DETAIL_ID_SEQ", allocationSize = 1)
	@Id
	@Column(name = "BATCH_DETAIL_ID")
	private Integer batchDetailId;

	@Column(name = "BATCH_ID")
	private Integer batchId;

	@Column(name = "ATTENDNECE_ID")
	private Integer attendneceId;

	@Column(name = "ATTENDNECE_VER_ID")
	private Integer attendneceVerId;

	@Column(name = "SESSION_APPLICATION_ID")
	private Integer sessionApplicationId;

	@Column(name = "ELEMENT_ENTRY_ID")
	private Integer elementEntryId;

	@Column(name = "PAYROLL")
	private String payroll;

	@Column(name = "DUTY_DATE")
	private Date dutyDate;

	@Column(name = "JOB")
	private String job;

	@Column(name = "LOCATION_ID")
	private Integer locationId;

	@Column(name = "EMP_NO")
	private String empNo;

	@Column(name = "ASG_NO")
	private String asgNo;

	@Column(name = "COA_INST")
	private String coaInst;

	@Column(name = "COA_FUND")
	private String coaFund;

	@Column(name = "COA_SECTION")
	private String cosSection;

	@Column(name = "COA_ANALYSTIC")
	private String coaAnalystic;

	@Column(name = "COA_TYPE")
	private String coaType;

	@Column(name = "HOUR_TYPE")
	private String hourType;

	@Column(name = "WORK_HOUR")
	private Integer totalHour;

	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "REASON")
	private String reason;

	transient private Calendar cal = null;

	public Integer getBatchDetailId() {
		return batchDetailId;
	}

	public void setBatchDetailId(Integer batchDetailId) {
		this.batchDetailId = batchDetailId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
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

	public String getEarnedMonthYear() {
		cal = cal == null ? Calendar.getInstance() : cal;
		cal.setTime(dutyDate);
		return (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
	}

	public int getDay() {
		cal = cal == null ? Calendar.getInstance() : cal;
		cal.setTime(dutyDate);
		return cal.get(Calendar.DATE);
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

	public String getCoaSection() {
		return cosSection;
	}

	public void setCoaSection(String cosSection) {
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return "PaymentDetailPo [batchDetailId=" + batchDetailId + ", batchId=" + batchId + ", attendneceId="
				+ attendneceId + ", attendneceVerId=" + attendneceVerId + ", sessionApplicationId="
				+ sessionApplicationId + ", elementEntryId=" + elementEntryId + ", payroll=" + payroll + ", dutyDate="
				+ dutyDate + ", job=" + job + ", locationId=" + locationId + ", empNo=" + empNo + ", asgNo=" + asgNo
				+ ", coaInst=" + coaInst + ", coaFund=" + coaFund + ", cosSection=" + cosSection + ", coaAnalystic="
				+ coaAnalystic + ", coaType=" + coaType + ", hourType=" + hourType + ", totalHour=" + totalHour
				+ ", status=" + status + ", reason=" + reason + "]";
	}


}
