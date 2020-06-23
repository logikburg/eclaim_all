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

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="POST_FUNDING")
public class MPRSPostFunding extends AbstractBasePo implements Serializable {
	private static final long serialVersionUID = 5589356117755954564L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="POST_FUNDING_UID_SEQ")
	@SequenceGenerator(name="POST_FUNDING_UID_SEQ", sequenceName="POST_FUNDING_UID_SEQ", allocationSize=1)
	@Column(name="POST_FUNDING_UID")
	private Integer postFundingUid;
	
	@OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="POST_UID")
	private PostMasterPo post;
	
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
	
	@Temporal(TemporalType.DATE)
	@Column(name="FUND_SRC_START_DATE")
	private Date fundSrcStartDate;
	
	@Temporal(TemporalType.DATE)
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

	public Integer getPostFundingUid() {
		return postFundingUid;
	}

	public void setPostFundingUid(Integer postFundingUid) {
		this.postFundingUid = postFundingUid;
	}
	
	public PostMasterPo getPost() {
		return post;
	}

	public void setPost(PostMasterPo post) {
		this.post = post;
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
	}*/

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

	public void setFundSrcStartDate(Date fundSrcStartDate) {
		this.fundSrcStartDate = fundSrcStartDate;
	}

	public Date getFundSrcEndDate() {
		return fundSrcEndDate;
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

		MPRSPostFunding other = (MPRSPostFunding) obj;

		if (post != other.post) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((post == null) ? 0 : post.hashCode());
		result = prime * result + prime;
		return result;
	}
	
}
