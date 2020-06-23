package hk.org.ha.eclaim.bs.report.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@IdClass(PK_Report5Cluster.class)
@Entity
@Table(name="V_REPORT_05_CLUSTER")
public class Report5RankClusterPo {
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
		
		@Column(name="totalVacanciesFte")
		private Double totalVacanciesFte;
		
		@Column(name="TOTALSTRENGTHFTE")
		private Double totalStrengthfte;
		
		@Column(name="TOTALDEFICIENCY")
		private Double totalDeficiency;
		
		@Column(name="DETPCODE")
		private String deptCode;
		
		@Column(name="DETPNAME")
		private String deptName;
		
		@Column(name="RANKCODE")
		private String rankCode;
		
		@Column(name="RANKNAME")
		private String rankName;
		
		public String getClusterCode() {
			return clusterCode;
		}

		public void setClusterCode(String clusterCode) {
			this.clusterCode = clusterCode;
		}

		public String getStaffGroupCode() {
			return staffGroupCode;
		}

		public void setStaffGroupCode(String staffGroupCode) {
			this.staffGroupCode = staffGroupCode;
		}

		public Double getTotalVacanciesFte() {
			return totalVacanciesFte;
		}

		public void setTotalVacanciesFte(Double totalVacanciesFte) {
			this.totalVacanciesFte = totalVacanciesFte;
		}

		public Double getTotalStrengthfte() {
			return totalStrengthfte;
		}

		public void setTotalStrengthfte(Double totalStrengthfte) {
			this.totalStrengthfte = totalStrengthfte;
		}

		public Double getTotalDeficiency() {
			return totalDeficiency;
		}

		public void setTotalDeficiency(Double totalDeficiency) {
			this.totalDeficiency = totalDeficiency;
		}
		
		public String getClusterName() {
			return clusterName;
		}

		public void setClusterName(String clusterName) {
			this.clusterName = clusterName;
		}

		public String getStaffGroupName() {
			return staffGroupName;
		}

		public void setStaffGroupName(String staffGroupName) {
			this.staffGroupName = staffGroupName;
		}

		public String getDeptCode() {
			return deptCode;
		}

		public void setDeptCode(String deptCode) {
			this.deptCode = deptCode;
		}

		public String getDeptName() {
			return deptName;
		}

		public void setDeptName(String deptName) {
			this.deptName = deptName;
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

}
