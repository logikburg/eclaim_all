package hk.org.ha.eclaim.bs.security.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.security.po.MPRSFunctionPo;

public interface IFuncAccessDao {
	List<MPRSFunctionPo> getFunctionListByRole(String roleId);
}
