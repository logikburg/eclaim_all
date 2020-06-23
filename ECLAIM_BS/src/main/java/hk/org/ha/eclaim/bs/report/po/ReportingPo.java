package hk.org.ha.eclaim.bs.report.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;

@Entity
@Table(name="RPT_REPORT_HIST")
public class ReportingPo extends AbstractBasePo {
	
	private static final long serialVersionUID = 8082912198288498182L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RPT_REPORT_HIST_UID_SEQ")
	@SequenceGenerator(name="RPT_REPORT_HIST_UID_SEQ", sequenceName="RPT_REPORT_HIST_UID_SEQ", allocationSize=1)
	@Column(name="REPORT_HIST_UID")
	private Integer reportUid;
	
	@Column(name="REPORT_TYPE")
	private String reportType;
	
	@Column(name="GENERATED_BY")
	private String generatedBy;
	
	@Column(name="REPORT_CRITERIA")
	private String criteria;
	
	@Column(name="REMARK")
	private String remark;
	
	@Column(name="OUTPUT_FORMAT")
	private String outputFormat;

	public Integer getReportUid() {
		return reportUid;
	}

	public void setReportUid(Integer reportUid) {
		this.reportUid = reportUid;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(String generatedBy) {
		this.generatedBy = generatedBy;
	}

	public String getCreatedDateStr() {
		return DateTimeHelper.formatDateTimeToString(this.getCreatedDate());
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}
}
