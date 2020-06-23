package hk.org.ha.eclaim.bs.security.po;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import hk.org.ha.eclaim.core.po.AbstractBasePo;

@Entity
@Table(name = "XXEAL_USER_ROLE")
public class UserRolePo extends AbstractBasePo {

	private static final long serialVersionUID = -4618369012942532825L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XXEAL_USER_ROLE_UID_SEQ")
	@SequenceGenerator(name = "XXEAL_USER_ROLE_UID_SEQ", sequenceName = "XXEAL_USER_ROLE_UID_SEQ", allocationSize = 1)
	@Column(name = "USER_ROLE_UID")
	private int userRoleId;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "ROLE_ID")
	private RolePo role;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID")
	private UserPo user;

	// @Column(name = "USER_ID")
	// private String userId;

	@Column(name = "DEFAULT_ROLE_IND")
	private String defaultRole;

	public int getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
	}

	public UserPo getUser() {
		return user;
	}

	public void setUser(UserPo user) {
		this.user = user;
	}

	public RolePo getRole() {
		return role;
	}

	public void setRole(RolePo role) {
		this.role = role;
	}

//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}

	public String getDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(String defaultRole) {
		this.defaultRole = defaultRole;
	}
}
