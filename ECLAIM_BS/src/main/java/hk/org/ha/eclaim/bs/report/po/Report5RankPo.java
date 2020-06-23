package hk.org.ha.eclaim.bs.report.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@IdClass(PK_Report5InstRank.class)
@Entity
@Table(name="V_REPORT_05")
public class Report5RankPo {
	@Id
	@Column(name="INSTCODE")
	private String instCode;
	
	@Column(name="instNAME")
	private String instName;
	
	@Id
	@Column(name="CLUSTERCODE")
	private String clusterCode;
	
	@Column(name="CLUSTERNAME")
	private String clusterName;
	
	@Id
	@Column(name="STAFFGROUPCODE")
	private String staffGroupCode;
	
	@Column(name="STAFFGROUPNAME")
	private String staffGroupName;
	
	
	@Column(name="VACANCIESFTE")
	private Double vacanciesFTE;
	
	@Column(name="STRENGTHFTE")
	private Double strengthFTE;
	
	@Column(name="DEFICIENCY")
	private Double deficiency;
	
	@Id
	@Column(name="deptCode")
	private String deptCode;
	
	@Column(name="deptName")
	private String deptName;
	
	@Id
	@Column(name="rankCode")
	private String rankCode;
	
	@Column(name="rankName")
	private String rankName;
	
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

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getStaffGroupCode() {
		return staffGroupCode;
	}

	public void setStaffGroupCode(String staffGroupCode) {
		this.staffGroupCode = staffGroupCode;
	}

	public String getStaffGroupName() {
		return staffGroupName;
	}

	public void setStaffGroupName(String staffGroupName) {
		this.staffGroupName = staffGroupName;
	}

	public Double getVacanciesFTE() {
		return vacanciesFTE;
	}

	public void setVacanciesFTE(Double vacanciesFTE) {
		this.vacanciesFTE = vacanciesFTE;
	}

	public Double getStrengthFTE() {
		return strengthFTE;
	}

	public void setStrengthFTE(Double strengthFte) {
		this.strengthFTE = strengthFte;
	}

	public Double getDeficiency() {
		return deficiency;
	}

	public void setDeficiency(Double deficiency) {
		this.deficiency = deficiency;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getRankCode() {
		return rankCode;
	}

	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

}
