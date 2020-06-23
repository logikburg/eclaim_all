package hk.org.ha.eclaim.bs.cs.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_POST_PROGRAM_TYPE")
public class PostProgramTypePo extends AbstractBasePo {
	
	private static final long serialVersionUID = -3248343322890204207L;

	@Id
	@Column(name="POST_PROGRAM_TYPE_UID")
	private Integer postProgramTypeUid;
	
	@Column(name="POST_FTE_TYPE")
	private String postFteType;
	
	@Column(name="POST_DURATION")
	private String postDuration;
	
	@Column(name="ANNUAL_PLAN_IND")
	private String annualPlanInd;
	
	@Column(name="PROGRAM_TYPE_CODE")
	private String programTypeCode;
	
	public Integer getPostProgramTypeUid() {
		return postProgramTypeUid;
	}

	public void setPostProgramTypeUid(Integer postProgramTypeUid) {
		this.postProgramTypeUid = postProgramTypeUid;
	}

	public String getPostFteType() {
		return postFteType;
	}

	public void setPostFteType(String postFteType) {
		this.postFteType = postFteType;
	}

	public String getPostDuration() {
		return postDuration;
	}

	public void setPostDuration(String postDuration) {
		this.postDuration = postDuration;
	}

	public String getAnnualPlanInd() {
		return annualPlanInd;
	}

	public void setAnnualPlanInd(String annualPlanInd) {
		this.annualPlanInd = annualPlanInd;
	}

	public String getProgramTypeCode() {
		return programTypeCode;
	}

	public void setProgramTypeCode(String programTypeCode) {
		this.programTypeCode = programTypeCode;
	}
	
}
