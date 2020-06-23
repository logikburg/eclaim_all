package hk.org.ha.eclaim.bs.cs.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.InstitutionPo;


public interface IInstDao {
	List<InstitutionPo> getAllInst();

	List<InstitutionPo> getAllInstByCluster(String clusterCode);

	List<InstitutionPo> getInstByUser(String userId, String roleId);
	
	List<InstitutionPo> getInstByUserForHospitalAdmin(String userId);

	List<InstitutionPo> getInstByClusterAndUser(String clusterCode, String userId, String roleId);
	
	List<InstitutionPo> getInstByClusterForHospitalAdmin(String clusterCode, String userId);
	
	InstitutionPo getInstNameByInstCode(String instCode);
}
