package hk.org.ha.eclaim.bs.request.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.request.po.RequestEmailPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IRequestEmailDao {
	List<RequestEmailPo> getUnsendEmail(int requestUid);
	void performSend(RequestEmailPo email);
	void insertEmail(RequestEmailPo email, UserPo updateUser);
}
