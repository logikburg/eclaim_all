package hk.org.ha.eclaim.bs.cs.po;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import hk.org.ha.eclaim.bs.request.po.PostMasterPo;
import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_POST_STATUS")
public class MPRSPostStatusPo extends AbstractBasePo {
	
	private static final long serialVersionUID = 7487463929843314640L;

	@Id
	@Column(name="POST_STATUS_CODE")
	private String postStatusCode;
	
	@Column(name="POST_STATUS_DESC")
	private String postStatusDesc;
	
	@OneToMany(mappedBy="postStatus", cascade=CascadeType.ALL)
	private List<PostMasterPo> postList = new ArrayList<PostMasterPo>();

	public String getPostStatusCode() {
		return postStatusCode;
	}

	public void setPostStatusCode(String postStatusCode) {
		this.postStatusCode = postStatusCode;
	}

	public String getPostStatusDesc() {
		return postStatusDesc;
	}

	public void setPostStatusDesc(String postStatusDesc) {
		this.postStatusDesc = postStatusDesc;
	}

	public List<PostMasterPo> getPostList() {
		return postList;
	}

	public void setPostList(List<PostMasterPo> postList) {
		this.postList = postList;
	}
}
