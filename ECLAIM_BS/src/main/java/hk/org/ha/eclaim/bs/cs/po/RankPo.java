package hk.org.ha.eclaim.bs.cs.po;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import hk.org.ha.eclaim.bs.request.po.RequestPostPo;
import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_RANK")
public class RankPo extends AbstractBasePo {
	
	private static final long serialVersionUID = -5527322699467105769L;

	@Id
	@Column(name="RANK_CODE")
	private String rankCode;

	@Column(name="RANK_NAME")
	private String rankName;
	
	@Column(name="STAFF_GROUP_CODE")
	private String staffGroupCode;
	
	@Column(name="DEPT_CODE")
	private String deptCode;
	
	@Column(name="SEQ_NO")
	private Integer seqNo;
	
	@OneToMany(mappedBy="rank", cascade=CascadeType.ALL)
	private List<RequestPostPo> requestPositionList = new ArrayList<RequestPostPo>();

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public String getRankCode() {
		return rankCode;
	}

	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}
	
	public String getStaffGroupCode() {
		return staffGroupCode;
	}

	public void setStaffGroupCode(String staffGroupCode) {
		this.staffGroupCode = staffGroupCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public List<RequestPostPo> getRequestPositionList() {
		return requestPositionList;
	}

	public void setRequestPositionList(List<RequestPostPo> requestPositionList) {
		this.requestPositionList = requestPositionList;
	}
	
}