package hk.org.ha.eclaim.bs.payment.po;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

// Transferable Object to render data on jsp view
public class PaymentBatchTo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6542499715324686340L;

	private Integer batchId;

	private Map<Integer, Integer> dutyDate = new TreeMap<Integer, Integer>();

	private String earnedMonth;

	private String status;

	private String cluster;

	private String empNo;

	private String hosp;

	private String asgNo;

	private String empName;

	private String staffRank;

	private String job;

	private String workLocation;

	private String hourType;

	private Integer totalWorkedHours;

	private String haCs;

	private String coaInst;

	private String coaFund;

	private String coaSection;

	private String coaAnalytic;

	private String coaType;
	
	private String reason;

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<Integer, Integer> getDutyDate() {
		return dutyDate;
	}

	public void setDutyDate(Map<Integer, Integer> dutyDate) {
		this.dutyDate = dutyDate;
	}

	public String getEarnedMonth() {
		return earnedMonth;
	}

	public void setEarnedMonth(String earnedMonth) {
		this.earnedMonth = earnedMonth;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public String getHosp() {
		return hosp;
	}

	public void setHosp(String hosp) {
		this.hosp = hosp;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getStaffRank() {
		return staffRank;
	}

	public void setStaffRank(String staffRank) {
		this.staffRank = staffRank;
	}

	public String getWorkLocation() {
		return workLocation;
	}

	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}

	public String getHourType() {
		return hourType;
	}

	public void setHourType(String hourType) {
		this.hourType = hourType;
	}

	public Integer getTotalWorkedHours() {
		return totalWorkedHours;
	}

	public void setTotalWorkedHours(Integer totalWorkedHours) {
		this.totalWorkedHours = totalWorkedHours;
	}

	public String getHaCs() {
		return haCs;
	}

	public void setHaCs(String haCs) {
		this.haCs = haCs;
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
		return coaSection;
	}

	public void setCoaSection(String coaSection) {
		this.coaSection = coaSection;
	}

	public String getCoaAnalytic() {
		return coaAnalytic;
	}

	public void setCoaAnalytic(String coaAnalytic) {
		this.coaAnalytic = coaAnalytic;
	}

	public String getCoaType() {
		return coaType;
	}

	public void setCoaType(String coaType) {
		this.coaType = coaType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
