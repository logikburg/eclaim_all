package hk.org.ha.eclaim.core.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnTransformer;

import hk.org.ha.eclaim.core.dao.jpa.listener.EntityChangeListener;

@MappedSuperclass
@EntityListeners(value={EntityChangeListener.class})
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class AbstractBasePo implements Serializable{
	
	private static final long serialVersionUID = 1796958555418801009L;
	
	@Column(name="REC_State")
	private String recState;

	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_ROLE_ID")
	@ColumnTransformer(write="UPPER(?)")
	private String createdRoleId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "UPDATED_BY")
	private String updatedBy;
	
	@Column(name="UPDATED_ROLE_ID")
	@ColumnTransformer(write="UPPER(?)")
	private String updatedRoleId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

	public String getRecState() {
		return recState;
	}

	public void setRecState(String recState) {
		this.recState = recState;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedRoleId() {
		return createdRoleId;
	}

	public void setCreatedRoleId(String createdRoleId) {
		this.createdRoleId = createdRoleId;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public String getUpdatedRoleId() {
		return updatedRoleId;
	}

	public void setUpdatedRoleId(String updatedRoleId) {
		this.updatedRoleId = updatedRoleId;
	}
	
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}
