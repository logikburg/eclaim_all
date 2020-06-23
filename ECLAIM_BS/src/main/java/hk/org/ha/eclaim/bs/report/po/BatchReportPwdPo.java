package hk.org.ha.eclaim.bs.report.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="RPT_BATCH_RPT_PWD")
public class BatchReportPwdPo extends AbstractBasePo {
	
	private static final long serialVersionUID = 5277270314098991062L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RPT_BATCH_RPT_PWD_UID_SEQ")
	@SequenceGenerator(name="RPT_BATCH_RPT_PWD_UID_SEQ", sequenceName="RPT_BATCH_RPT_PWD_UID_SEQ", allocationSize=1)
	@Column(name="BATCH_RPT_PWD_UID")
	private Integer batchRptPwdUid;
	
	@Column(name="CLUSTER_CODE")
	private String clusterCode;
	
	@Column(name="RPT_PWD")
	private String rptPwd;
	
	@Temporal(TemporalType.DATE)
	@Column(name="START_DATE")
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="END_DATE")
	private Date endDate;

	public Integer getBatchRptPwdUid() {
		return batchRptPwdUid;
	}

	public void setBatchRptPwdUid(Integer batchRptPwdUid) {
		this.batchRptPwdUid = batchRptPwdUid;
	}

	public String getClusterCode() {
		return clusterCode;
	}

	public void setClusterCode(String clusterCode) {
		this.clusterCode = clusterCode;
	}

	public String getRptPwd() {
		return rptPwd;
	}

	public void setRptPwd(String rptPwd) {
		this.rptPwd = rptPwd;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
