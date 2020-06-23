package hk.org.ha.eclaim.bs.cs.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.RequestStatusPo;

public interface IRequestStatusDao {
	public List<RequestStatusPo> getAllRequestStatus();
}
