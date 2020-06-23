package hk.org.ha.eclaim.bs.report.po;

import javax.persistence.Column;

import hk.org.ha.eclaim.bs.report.po.PK_Report2;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@IdClass(PK_Report2.class)
@Entity
@Table(name="V_REPORT_02")
public class Report2Po {
	@Id
	@Column(name="CLUSTERCODE")
	private String clusterCode;
	
	@Id
	@Column(name="HOSPITAL")
	private String hospital;
	
	@Id
	@Column(name="DEPTNAME")
	private String deptName;
	
	@Id
	@Column(name="POSTTITLE")
	private String postTitle;
	
	@Column(name="TOTALFTE")
	private Double totalFTE;
	
	@Column(name="STRENGTHFTE")
	private Double strengthFTE;
	
	@Column(name="VACANCIESFTE")
	private Double vacanciesFTE;
	
	@Column(name="OCCRATE")
	private Double occRate;
	
	public String getClusterCode() {
		return clusterCode;
	}

	public void setClusterCode(String clusterCode) {
		this.clusterCode = clusterCode;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public Double getTotalFTE() {
		return totalFTE;
	}

	public void setTotalFTE(Double totalFTE) {
		this.totalFTE = totalFTE;
	}

	public Double getStrengthFTE() {
		return strengthFTE;
	}

	public void setStrengthFTE(Double strengthFTE) {
		this.strengthFTE = strengthFTE;
	}

	public Double getVacanciesFTE() {
		return vacanciesFTE;
	}

	public void setVacanciesFTE(Double vacanciesFTE) {
		this.vacanciesFTE = vacanciesFTE;
	}

	public Double getOccRate() {
		return occRate;
	}

	public void setOccRate(Double occRate) {
		this.occRate = occRate;
	}
	
}
