package hk.org.ha.eclaim.bs.cs.po;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import hk.org.ha.eclaim.bs.request.po.RequestPo;
import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_REQUEST_STATUS")
public class RequestStatusPo extends AbstractBasePo {
	
	private static final long serialVersionUID = -3429480804887088914L;

	@Id
	@Column(name="RQ_STATUS_CODE")
	private String statusCode;
	
	@Column(name="RQ_STATUS_DESC")
	private String statusDesc;
	
	@OneToMany(mappedBy="requestStatus", cascade=CascadeType.ALL)
	private List<RequestPo> requestList = new ArrayList<RequestPo>();

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public List<RequestPo> getRequestList() {
		return requestList;
	}

	public void setRequestList(List<RequestPo> requestList) {
		this.requestList = requestList;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
}