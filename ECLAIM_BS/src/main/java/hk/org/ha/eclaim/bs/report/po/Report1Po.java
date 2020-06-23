package hk.org.ha.eclaim.bs.report.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@IdClass(PK_Report1.class)
@Entity
@Table(name="V_REPORT_01")
public class Report1Po {
	
	@Id
	@Column(name="CLUSTER_CODE")
	private String clusterCode;
	
	@Id
	@Column(name="HOSPITAL")
	private String hospital;
	
	@Id
	@Column(name="POST_TITLE")
	private String postTitle;
	
	@Column(name="LAST_YR_FTE")
	private Double lastYrFTE;
	
	@Column(name="CURR_YR_FTE")
	private Double currYrFTE;
	
	@Column(name="TOTAL_FTE")
	private Double totalFTE;
	
	@Column(name="STRENGTH_FTE")
	private Double strengthFTE;
	
	@Column(name="VACANCIES_FTE")
	private Double vacanciesFTE;
	
	@Column(name="OCC_RATE")
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

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public Double getLastYrFTE() {
		return lastYrFTE;
	}

	public void setLastYrFTE(Double lastYrFTE) {
		this.lastYrFTE = lastYrFTE;
	}

	public Double getCurrYrFTE() {
		return currYrFTE;
	}

	public void setCurrYrFTE(Double currYrFTE) {
		this.currYrFTE = currYrFTE;
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
