package hk.org.ha.eclaim.bs.cs.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name="CS_POST_DURATION")
public class PostDurationPo extends AbstractBasePo {
	
	private static final long serialVersionUID = 4517310937362532154L;

	@Id
	@Column(name="POST_DURATION_CODE")
	private String postDurationCode;
	
	@Column(name="post_duration_desc")
	private String postDurationDesc;
	
	public String getPostDurationDesc() {
		return postDurationDesc;
	}

	public void setPostDurationDesc(String postDurationDesc) {
		this.postDurationDesc = postDurationDesc;
	}

	public String getPostDurationCode() {
		return postDurationCode;
	}

	public void setPostDurationCode(String postDurationCode) {
		this.postDurationCode = postDurationCode;
	}
}