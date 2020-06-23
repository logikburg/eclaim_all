package hk.org.ha.eclaim.bs.security.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

public interface IDataAccessDao {

	List<DataAccessPo> getDataAccessByUserId(String userId);

	List<DataAccessPo> getDataAccessByRoleId(int roleId);

	void deleteDataAccess(int userRoleId, UserPo updateUser);

	void insertDataAccess(int roleId, String clusterCode, String instCode, String deptCode, String staffCode, UserPo updateUser);

	boolean haveAccessRight(String clusterCode, String instCode, String userId, String roleId);

}
