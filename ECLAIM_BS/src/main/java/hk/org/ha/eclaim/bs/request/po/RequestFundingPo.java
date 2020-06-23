package hk.org.ha.eclaim.bs.request.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import hk.org.ha.eclaim.core.po.AbstractBasePo;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;

@Entity
@Table(name="RQ_FUNDING")
public class RequestFundingPo extends AbstractBasePo implements Serializable {
	private static final long serialVersionUID = -5914663253064433255L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RQ_POST_FUNDING_UID_SEQ")
	@SequenceGenerator(name="RQ_POST_FUNDING_UID_SEQ", sequenceName="RQ_POST_FUNDING_UID_SEQ", allocationSize=1)
	@Column(name="REQUEST_POST_FUNDING_UID")
	private Integer requestPostFundingUid;
	
	@OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="REQUEST_POST_UID")
	private RequestPostPo requestPosition;
	
	@Column(name="FUNDING_SEQ_NO")
	private Integer fundingSeqNo;
	
	@Column(name="ANNUAL_PLAN_IND")
	private String annualPlanInd;

	@Column(name="PROGRAM_YEAR")
	private String programYear;
	
	@Column(name="PROGRAM_CODE")
	private String programCode;
	
	@Column(name="PROGRAM_NAME")
	private String programName;
	
	@Column(name="PROGRAM_TYPE_CODE")
	private String programTypeCode;
	
	@Column(name="FUND_SRC_ID")
	private String fundSrcId;
	
	@Column(name="FUND_SRC_SUB_CAT_ID")
	private String fundSrcSubCatId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FUND_SRC_START_DATE")
	private Date fundSrcStartDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FUND_SRC_END_DATE")
	private Date fundSrcEndDate;
	
	@Column(name="FUND_SRC_FTE")
	private Double fundSrcFte;
	
	@Column(name="FUND_SRC_REMARK")
	private String fundSrcRemark;
	
	@Column(name="INST")
	private String inst;

	@Column(name="SECTION")
	private String section;
	
	@Column(name="ANALYTICAL")
	private String analytical;
	
	@Transient
	private String oriAnnualPlanInd;

	@Transient
	private String oriProgramYear;
	
	@Transient
	private String oriProgramCode;
	
	@Transient
	private String oriProgramName;
	
	@Transient
	private String oriProgramTypeCode;
	
	@Transient
	private String oriFundSrcId;
	
	@Transient
	private String oriFundSrcSubCatId;
	
	@Transient
	private String oriStartDateStr;
	
	@Transient
	private String oriEndDateStr;
	
	@Transient
	private Double oriFundSrcFte;
	
	@Transient
	private String oriFundSrcRemark;
	
	@Transient
	private String oriInst;

	@Transient
	private String oriSection;
	
	@Transient
	private String oriAnalytical;
	
	@Transient
	private String startDateStr;
	
	@Transient
	private String endDateStr;
	
	public Integer getRequestPostFundingUid() {
		return requestPostFundingUid;
	}

	public void setRequestPostFundingUid(Integer requestPostFundingUid) {
		this.requestPostFundingUid = requestPostFundingUid;
	}
	
	public RequestPostPo getRequestPosition() {
		return requestPosition;
	}

	public void setRequestPosition(RequestPostPo requestPosition) {
		this.requestPosition = requestPosition;
	}
	
	public Integer getFundingSeqNo() {
		return fundingSeqNo;
	}

	public void setFundingSeqNo(Integer fundingSeqNo) {
		this.fundingSeqNo = fundingSeqNo;
	}
	
	public String getAnnualPlanInd() {
		return annualPlanInd;
	}

	public void setAnnualPlanInd(String annualPlanInd) {
		this.annualPlanInd = annualPlanInd;
	}

	public String getProgramYear() {
		return programYear;
	}

	public void setProgramYear(String programYear) {
		this.programYear = programYear;
	}

	public String getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getProgramTypeCode() {
		return programTypeCode;
	}

	public void setProgramTypeCode(String programTypeCode) {
		this.programTypeCode = programTypeCode;
	}
	
	/*public String getProgramRemark() {
		return programRemark;
	}

	public void setProgramRemark(String programRemark) {
		this.programRemark = programRemark;
	}
*/
	public String getFundSrcId() {
		return fundSrcId;
	}

	public void setFundSrcId(String fundSrcId) {
		this.fundSrcId = fundSrcId;
	}
	
	public String getFundSrcSubCatId() {
		return fundSrcSubCatId;
	}

	public void setFundSrcSubCatId(String fundSrcSubCatId) {
		this.fundSrcSubCatId = fundSrcSubCatId;
	}
	
	public Date getFundSrcStartDate() {
		return fundSrcStartDate;
	}
	
	public String getFundSrcStartDateStr() {
		if (fundSrcStartDate == null) {
			return "";
		}
		return DateTimeHelper.formatDateToString(fundSrcStartDate);
	}

	public void setFundSrcStartDate(Date funSrcStartDate) {
		this.fundSrcStartDate = funSrcStartDate;
	}

	public Date getFundSrcEndDate() {
		return fundSrcEndDate;
	}
	
	public String getFundSrcEndDateStr() {
		if (fundSrcEndDate == null) {
			return "";
		}
		return DateTimeHelper.formatDateToString(fundSrcEndDate);
	}

	public void setFundSrcEndDate(Date fundSrcEndDate) {
		this.fundSrcEndDate = fundSrcEndDate;
	}
	
	public Double getFundSrcFte() {
		return fundSrcFte;
	}

	public void setFundSrcFte(Double fundSrcFte) {
		this.fundSrcFte = fundSrcFte;
	}
	
	public String getFundSrcRemark() {
		return fundSrcRemark;
	}

	public void setFundSrcRemark(String fundSrcRemark) {
		this.fundSrcRemark = fundSrcRemark;
	}
	
	public String getInst() {
		return inst;
	}

	public void setInst(String inst) {
		this.inst = inst;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getAnalytical() {
		return analytical;
	}

	public void setAnalytical(String analytical) {
		this.analytical = analytical;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		RequestFundingPo other = (RequestFundingPo) obj;

		if (requestPosition != other.requestPosition) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((requestPosition == null) ? 0 : requestPosition.hashCode());
		result = prime * result + prime;
		return result;
	}

	public String getStartDateStr() {
		if (this.startDateStr != null && !"".equals(this.startDateStr)) {
			return startDateStr;
		}
		else {
			if (fundSrcStartDate == null) {
				return "";
			}
			return DateTimeHelper.formatDateToString(fundSrcStartDate);
		}
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getEndDateStr() {
		if (this.endDateStr != null && !"".equals(this.endDateStr)) {
			return endDateStr;
		}
		else {
			if (fundSrcEndDate == null) {
				return "";
			}
			return DateTimeHelper.formatDateToString(fundSrcEndDate);
		}
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getOriAnnualPlanInd() {
		return oriAnnualPlanInd;
	}

	public void setOriAnnualPlanInd(String oriAnnualPlanInd) {
		this.oriAnnualPlanInd = oriAnnualPlanInd;
	}

	public String getOriProgramYear() {
		return oriProgramYear;
	}

	public void setOriProgramYear(String oriProgramYear) {
		this.oriProgramYear = oriProgramYear;
	}

	public String getOriProgramCode() {
		return oriProgramCode;
	}

	public void setOriProgramCode(String oriProgramCode) {
		this.oriProgramCode = oriProgramCode;
	}

	public String getOriProgramName() {
		return oriProgramName;
	}

	public void setOriProgramName(String oriProgramName) {
		this.oriProgramName = oriProgramName;
	}

	public String getOriProgramTypeCode() {
		return oriProgramTypeCode;
	}

	public void setOriProgramTypeCode(String oriProgramTypeCode) {
		this.oriProgramTypeCode = oriProgramTypeCode;
	}

	public String getOriFundSrcId() {
		return oriFundSrcId;
	}

	public void setOriFundSrcId(String oriFundSrcId) {
		this.oriFundSrcId = oriFundSrcId;
	}

	public String getOriFundSrcSubCatId() {
		return oriFundSrcSubCatId;
	}

	public void setOriFundSrcSubCatId(String oriFundSrcSubCatId) {
		this.oriFundSrcSubCatId = oriFundSrcSubCatId;
	}

	public Double getOriFundSrcFte() {
		return oriFundSrcFte;
	}

	public void setOriFundSrcFte(Double oriFundSrcFte) {
		this.oriFundSrcFte = oriFundSrcFte;
	}

	public String getOriFundSrcRemark() {
		return oriFundSrcRemark;
	}

	public void setOriFundSrcRemark(String oriFundSrcRemark) {
		this.oriFundSrcRemark = oriFundSrcRemark;
	}

	public String getOriInst() {
		return oriInst;
	}

	public void setOriInst(String oriInst) {
		this.oriInst = oriInst;
	}

	public String getOriSection() {
		return oriSection;
	}

	public void setOriSection(String oriSection) {
		this.oriSection = oriSection;
	}

	public String getOriAnalytical() {
		return oriAnalytical;
	}

	public void setOriAnalytical(String oriAnalytical) {
		this.oriAnalytical = oriAnalytical;
	}

	public String getOriStartDateStr() {
		return oriStartDateStr;
	}

	public void setOriStartDateStr(String oriStartDateStr) {
		this.oriStartDateStr = oriStartDateStr;
	}

	public String getOriEndDateStr() {
		return oriEndDateStr;
	}

	public void setOriEndDateStr(String oriEndDateStr) {
		this.oriEndDateStr = oriEndDateStr;
	}

}
