package hk.org.ha.eclaim.bs.cs.svc;

import java.util.List;

import hk.org.ha.eclaim.bs.cs.po.CircumstancePo;
import hk.org.ha.eclaim.bs.cs.po.ClusterPo;
import hk.org.ha.eclaim.bs.cs.po.CoaPo;
import hk.org.ha.eclaim.bs.cs.po.DepartmentPo;
import hk.org.ha.eclaim.bs.cs.po.EmailTemplatePo;
import hk.org.ha.eclaim.bs.cs.po.ExternalSupportPo;
import hk.org.ha.eclaim.bs.cs.po.FundingSourcePo;
import hk.org.ha.eclaim.bs.cs.po.FundingSourceSubCatPo;
import hk.org.ha.eclaim.bs.cs.po.InstitutionPo;
import hk.org.ha.eclaim.bs.cs.po.MPRSPostStatusPo;
import hk.org.ha.eclaim.bs.cs.po.OrganizationPo;
import hk.org.ha.eclaim.bs.cs.po.PostDurationPo;
import hk.org.ha.eclaim.bs.cs.po.ProgramTypePo;
import hk.org.ha.eclaim.bs.cs.po.RankPo;
import hk.org.ha.eclaim.bs.cs.po.RequestStatusPo;
import hk.org.ha.eclaim.bs.cs.po.StaffGroupPo;
import hk.org.ha.eclaim.bs.cs.po.StatusPo;
import hk.org.ha.eclaim.bs.cs.po.SubSpecialtyPo;;

public interface ICommonSvc {
	List<ClusterPo> getAllCluster();
	List<ClusterPo> getAllClusterForHospitalAdmin();
	List<ClusterPo> getClusterByUser(String userId, String roleId);
	
	List<InstitutionPo> getAllInst();
	List<InstitutionPo> getAllInstByCluster(String clusterCode);
	List<InstitutionPo> getInstByUser(String userId, String roleId);
	List<InstitutionPo> getInstByUserForHospitalAdmin(String userId);
	List<InstitutionPo> getInstByClusterAndUser(String clusterCode, String userId, String roleId);
	List<InstitutionPo> getInstByClusterForHospitalAdmin(String clusterCode, String userId);
	
	List<DepartmentPo> getAllDept();
	List<DepartmentPo> getAllDeptByStaffGroup(String staffGroupCode);
	List<RequestStatusPo> getAllRequestStatus();
	
	public EmailTemplatePo getTemplateByTemplateId(String id, String[] para);

	List<StaffGroupPo> getAllStaffGroup();
	public String getRankNameByRankCode(String rankCode);
	public String getInstNameByInstCode(String instCode);
	public String getDeptNameByDeptCode(String deptCode);
	List<RankPo> getAllRank();
	List<RankPo> getAllRank(String staffGroup, String fromRank);
	List<RankPo> getAllRankByStaffGroup(String staffGroupCode, String deptCode);
	List<FundingSourcePo> getAllFundingSource();
	List<ExternalSupportPo> getAllExternalSupport();
	List<PostDurationPo> getAllPostDuration();
	public String getDuartionDescByDurationCode(String durationCode);
	List<SubSpecialtyPo> getAllSubSpecialty();

	List<MPRSPostStatusPo> getAllPostStatus();
	void performSendEmail(int requestUid);
	List<ProgramTypePo> getAllProgramType();
	
	// Added for CC176525
	public List<ProgramTypePo> getProgramTypeList(String postDuration, String postFteType, String annaulPlanInd);
	
	List<FundingSourceSubCatPo> getAllFundingSourceSubCat();
	
	public List<CircumstancePo> getAllCircum();
	
	public List<CircumstancePo> getAllActiveCircum();
	
	public CircumstancePo getCircumPoByCircumId(Integer circumId);
	
	public CircumstancePo getCircumPoByCircumCode(String circumCode);
	
	public List<OrganizationPo> getAllOrganization();
	
	public List<OrganizationPo> getAllActiveOrganization();
	
	public List<OrganizationPo> searchOrganizationByName(String name);
	public List<OrganizationPo> searchOrganizationByName(String name, String loginUserId, String roleId);
	public List<OrganizationPo> searchOrganizationByName(String name, String loginUserId, String roleId, boolean activeOnly);
		
	public OrganizationPo getOrgPoByOrgCode(int organizationId);
	
	public String getOrgNameByOrgCode(int organizationId);
	
	public String getClusIdByOrgId(int organizationId);
	
	public List<StatusPo> getAllStatusList();
	
	public String getStatusDesc(String statusCode);
	
	public List<CoaPo> getAllCOA(String coaName);
	public List<CoaPo> getAllInstCOA();
	public List<CoaPo> getAllTypeCOA();
	public List<CoaPo> getAllSectionCOA();
	public List<CoaPo> getAllFundCOA();
	public List<CoaPo> getAllAnalyCOA();
	
}
