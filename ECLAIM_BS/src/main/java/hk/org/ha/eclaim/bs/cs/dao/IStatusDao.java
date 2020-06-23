package hk.org.ha.eclaim.bs.cs.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.RequestStatusPo;
import hk.org.ha.eclaim.bs.cs.po.StatusPo;

public interface IStatusDao {
	public List<StatusPo> getAllStatus();
	public String getStatusDesc(String statusCode);
}
