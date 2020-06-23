package hk.org.ha.eclaim.bs.cs.dao;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.OrganizationPo;

public interface IOrganizationDao {
	public List<OrganizationPo> getAllOrganization();
	
	public List<OrganizationPo> getAllActiveOrganization();
	
	public OrganizationPo getOrganizationByOrganizationId(int organizationId);
	
	public List<OrganizationPo> searchOrganization(String name, String loginUserId, String roleId, boolean activeOnly);

	public String getOrgNameByOrgId(int organizationId);
	
	public String getClusIdByOrgId(int organizationId);
}
