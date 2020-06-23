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
@Table(name="CS_REQUEST_TYPE")
public class RequestTypePo extends AbstractBasePo {
	
	private static final long serialVersionUID = -3429480804887088914L;

	@Id
	@Column(name="RQ_TYPE_CODE")
	private String rqTypeCode;
	
	@Column(name="RQ_TYPE_NAME")
	private String rqTypeName;
	
	@OneToMany(mappedBy="requestType", cascade=CascadeType.ALL)
	private List<RequestPo> requestList = new ArrayList<RequestPo>();

	public String getRqTypeCode() {
		return rqTypeCode;
	}

	public void setRqTypeCode(String rqTypeCode) {
		this.rqTypeCode = rqTypeCode;
	}

	public String getRqTypeName() {
		return rqTypeName;
	}

	public void setRqTypeName(String rqTypeName) {
		this.rqTypeName = rqTypeName;
	}

	public List<RequestPo> getRequestList() {
		return requestList;
	}

	public void setRequestList(List<RequestPo> requestList) {
		this.requestList = requestList;
	}
}