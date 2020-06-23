package hk.org.ha.eclaim.bs.cs.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.ClusterPo;

public interface IClusterDao {
	List<ClusterPo> getAllCluster();
	List<ClusterPo> getAllClusterForHospitalAdmin();
	List<ClusterPo> getClusterByUser(String userId, String roleId);
}
