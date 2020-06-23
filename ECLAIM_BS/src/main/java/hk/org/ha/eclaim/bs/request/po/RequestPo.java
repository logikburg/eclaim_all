package hk.org.ha.eclaim.bs.request.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import hk.org.ha.eclaim.core.po.AbstractBasePo;
import hk.org.ha.eclaim.bs.cs.po.RequestStatusPo;
import hk.org.ha.eclaim.bs.cs.po.RequestTypePo;

@Entity
@Table(name="RQ_MASTER")
public class RequestPo extends AbstractBasePo {
	
	private static final long serialVersionUID = -1822258452733986439L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RQ_MASTER_UID_SEQ")
	@SequenceGenerator(name="RQ_MASTER_UID_SEQ", sequenceName="RQ_MASTER_UID_SEQ", allocationSize=1)
	@Column(name="request_uid")
	private int requestNo;
	
	@Column(name="request_id")
	private String requestId;
	
	@ManyToOne
	@JoinColumn(name="request_status")
	private RequestStatusPo requestStatus;
	
	@Temporal(TemporalType.DATE)
	@Column(name="request_date")
	private Date requestDate;
	
	@ManyToOne
	@JoinColumn(name="request_type")
	private RequestTypePo requestType;
	
	@Column(name="CURRENT_USER")
	private String currentWFUser;
	
	@Column(name="CURRENT_WF_GROUP")
	private String currentWFGroup;
	
	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_START_DATE")
	private Date effectFrom;
	
	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_END_DATE")
	private Date effectTo;
	
	@Column(name="REASON")
	private String requestReason;
	
	@Column(name="REQUESTER")
	private String requester;
	
	@Temporal(TemporalType.DATE)
	@Column(name="APPROVAL_DATE")
	private Date approvalDate;
	
	@Column(name="APPROVAL_REMARK")
	private String approvalRemark;
	
	@Column(name="APPROVAL_REF")
	private String approvalReference;
	
	@Column(name="DURATION_TYPE")
	private String durationType;

	@Column(name="DURATION_NO")
	private Integer durationNo;

	@Column(name="DURATION_UNIT")
	private String durationUnit;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="request", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RequestPostPo> requestPositionList = new ArrayList<RequestPostPo>();
	
	@Transient
	private List<RequestAttachmentPo> attachment = new ArrayList<RequestAttachmentPo>();
	
	@Transient
	private Integer deleteAttachmentId;
	
	// Added for CR0367
	@Transient
	private String staffGroup;

	public int getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(int requestNo) {
		this.requestNo = requestNo;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public RequestTypePo getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestTypePo requestType) {
		this.requestType = requestType;
	}

	public List<RequestPostPo> getRequestPositionList() {
		return requestPositionList;
	}

	public void setRequestPositionList(List<RequestPostPo> requestPositionList) {
		this.requestPositionList = requestPositionList;
	}

	public String getCurrentWFUser() {
		return currentWFUser;
	}

	public void setCurrentWFUser(String currentWFUser) {
		this.currentWFUser = currentWFUser;
	}

	public String getCurrentWFGroup() {
		return currentWFGroup;
	}

	public void setCurrentWFGroup(String currentWFGroup) {
		this.currentWFGroup = currentWFGroup;
	}

	public Date getEffectFrom() {
		return effectFrom;
	}

	public void setEffectFrom(Date effectFrom) {
		this.effectFrom = effectFrom;
	}

	public String getRequestReason() {
		return requestReason;
	}

	public void setRequestReason(String requestReason) {
		this.requestReason = requestReason;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}
	
	public void addRequestPosition(RequestPostPo post) {
        this.requestPositionList.add(post);
        post.setRequest(this);
    }
    
    public void removeRequestPosition(RequestPostPo emp) {
        this.getRequestPositionList().remove(emp);
        emp.setRequest(null);
    }
    
    public RequestStatusPo getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(RequestStatusPo requestStatus) {
		this.requestStatus = requestStatus;
	}

	public Date getEffectTo() {
		return effectTo;
	}

	public void setEffectTo(Date effectTo) {
		this.effectTo = effectTo;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getApprovalRemark() {
		return approvalRemark;
	}

	public void setApprovalRemark(String approvalRemark) {
		this.approvalRemark = approvalRemark;
	}

	public String getApprovalReference() {
		return approvalReference;
	}

	public void setApprovalReference(String approvalReference) {
		this.approvalReference = approvalReference;
	}

	public Integer getDeleteAttachmentId() {
		return deleteAttachmentId;
	}

	public void setDeleteAttachmentId(Integer deleteAttachmentId) {
		this.deleteAttachmentId = deleteAttachmentId;
	}
	
	public List<RequestAttachmentPo> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<RequestAttachmentPo> attachment) {
		this.attachment = attachment;
	}

	public String getDurationType() {
		return durationType;
	}

	public void setDurationType(String durationType) {
		this.durationType = durationType;
	}

	public Integer getDurationNo() {
		return durationNo;
	}

	public void setDurationNo(Integer durationNo) {
		this.durationNo = durationNo;
	}

	public String getDurationUnit() {
		return durationUnit;
	}

	public void setDurationUnit(String durationUnit) {
		this.durationUnit = durationUnit;
	}

	public String getStaffGroup() {
		return staffGroup;
	}

	public void setStaffGroup(String staffGroup) {
		this.staffGroup = staffGroup;
	}
}