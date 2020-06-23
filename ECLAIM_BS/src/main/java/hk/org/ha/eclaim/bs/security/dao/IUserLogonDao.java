package hk.org.ha.eclaim.bs.security.dao;

import hk.org.ha.eclaim.bs.security.po.UserLogonPo;

public interface IUserLogonDao {
	int insert(UserLogonPo userLogon);
	
	UserLogonPo getLastLogonInfo(String userId);
}
