package hk.org.ha.eclaim.bs.cs.svc.impl;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.cs.dao.ICircumstanceDao;
import hk.org.ha.eclaim.bs.cs.dao.IClusterDao;
import hk.org.ha.eclaim.bs.cs.dao.ICoaDao;
import hk.org.ha.eclaim.bs.cs.dao.IDeptDao;
import hk.org.ha.eclaim.bs.cs.dao.IEmailTemplateDao;
import hk.org.ha.eclaim.bs.cs.dao.IExternalSupportDao;
import hk.org.ha.eclaim.bs.cs.dao.IFundingSourceDao;
import hk.org.ha.eclaim.bs.cs.dao.IFundingSourceSubCatDao;
import hk.org.ha.eclaim.bs.cs.dao.IInstDao;
import hk.org.ha.eclaim.bs.cs.dao.IOrganizationDao;
import hk.org.ha.eclaim.bs.cs.dao.IPostDurationDao;
import hk.org.ha.eclaim.bs.cs.dao.IPostStatusDao;
import hk.org.ha.eclaim.bs.cs.dao.IProgramTypeDao;
import hk.org.ha.eclaim.bs.cs.dao.IRankDao;
import hk.org.ha.eclaim.bs.cs.dao.IRequestStatusDao;
import hk.org.ha.eclaim.bs.cs.dao.IStaffGroupDao;
import hk.org.ha.eclaim.bs.cs.dao.IStatusDao;
import hk.org.ha.eclaim.bs.cs.dao.ISubSpecialtyDao;
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
import hk.org.ha.eclaim.bs.cs.po.SubSpecialtyPo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.request.dao.IRequestEmailDao;
import hk.org.ha.eclaim.bs.request.po.RequestEmailPo;
import hk.org.ha.eclaim.core.logger.EClaimLogger;;

@Service("commonSvc")
public class CommonSvcImpl implements ICommonSvc {

	@Value("${mail.host}")
	private String emailHost;
	
	@Value("${mail.port}")
	private String emailPort;
	
	@Value("${mail.sender}")
	private String emailSender;
	
	@Value("${ss.app.environment}")
	private String environmentPrefix;
	
	@Autowired
	private IClusterDao clusterDao;
	
	@Autowired
	private IInstDao instDao;
	
	@Autowired
	private IDeptDao deptDao;
	
	@Autowired
	private ISubSpecialtyDao subSpecialtyDao;
	
	@Autowired
	private IEmailTemplateDao emailTemplateDao;
	
	@Autowired
	private IRequestStatusDao requestStatusDao;

	@Autowired
	private IStaffGroupDao staffGroup;

	@Autowired
	private IRankDao rank;

	@Autowired
	private IFundingSourceDao fundingSource;
	
	@Autowired
	private IFundingSourceSubCatDao fundingSourceSubCatDao;

	@Autowired
	private IExternalSupportDao externalSupport;
	
	@Autowired
	private IPostDurationDao postDuration;
	
	@Autowired
	private IPostStatusDao postStatus;
	
	@Autowired
	private IProgramTypeDao programTypeDao;
	
	@Autowired
	private IRequestEmailDao requestEmailDao;
	
	@Autowired
	private ICircumstanceDao circumDao;
	
	@Autowired
	private IOrganizationDao organizationDao;
	
	@Autowired
	private IStatusDao statusDao;
	
	@Autowired
	private ICoaDao coaDao;
	
	public List<ClusterPo> getAllCluster() {
		return clusterDao.getAllCluster();
	}
	
	public List<ClusterPo> getAllClusterForHospitalAdmin() {
		return clusterDao.getAllClusterForHospitalAdmin();
	}
	
	public List<ClusterPo> getClusterByUser(String userId, String roleId) {
		return clusterDao.getClusterByUser(userId, roleId);
	}
	
	public List<InstitutionPo> getAllInst() {
		return instDao.getAllInst();
	}
	
	public List<InstitutionPo> getAllInstByCluster(String clusterCode) {
		return instDao.getAllInstByCluster(clusterCode);
	}

	public List<InstitutionPo> getInstByUser(String userId, String roleId) {
		return instDao.getInstByUser(userId, roleId);
	}
	
	public List<InstitutionPo> getInstByUserForHospitalAdmin(String userId) {
		return instDao.getInstByUserForHospitalAdmin(userId);
	}

	public List<InstitutionPo> getInstByClusterAndUser(String clusterCode, String userId, String roleId) {
		return instDao.getInstByClusterAndUser(clusterCode, userId, roleId);
	}
	
	public List<InstitutionPo> getInstByClusterForHospitalAdmin(String clusterCode, String userId) {
		return instDao.getInstByClusterForHospitalAdmin(clusterCode, userId);
	}
	
	public List<DepartmentPo> getAllDept() {
		return deptDao.getAllDept();
	}
	
	public List<DepartmentPo> getAllDeptByStaffGroup(String staffGroupCode) {
		return deptDao.getAllDeptByStaffGroup(staffGroupCode);
	}

	public EmailTemplatePo getTemplateByTemplateId(String id, String[] para) {
		return emailTemplateDao.getTemplateByTemplateId(id, para);
	}

	public List<RequestStatusPo> getAllRequestStatus() {
		return requestStatusDao.getAllRequestStatus();
	}

	public List<StaffGroupPo> getAllStaffGroup() {
		return staffGroup.getAllStaffGroup();
	}
	
	@Transactional
	public List<RankPo> getAllRank() {
		return rank.getAllRank();
	}
	
	public List<RankPo> getAllRankByStaffGroup(String staffGroupCode, String deptCode) {
		return rank.getAllRankByStaffGroup(staffGroupCode, deptCode);
	}
	
	@Transactional
	public List<RankPo> getAllRank(String staffGroup, String fromRank) {
		return rank.getAllRank(staffGroup, fromRank);
	}
	
	@Transactional
	public String getRankNameByRankCode(String rankCode) {
		return rank.getRankNameByRankCode(rankCode).getRankName();
	}
	
	public String getInstNameByInstCode(String instCode) {
		return instDao.getInstNameByInstCode(instCode).getInstName();
	}
	
	public String getDeptNameByDeptCode(String deptCode) {
		return deptDao.getDeptNameByDeptCode(deptCode).getDeptName();
	}
	
	public List<FundingSourcePo> getAllFundingSource() {
		return fundingSource.getAllFundingSource();
	}
	
	public List<FundingSourceSubCatPo> getAllFundingSourceSubCat() {
		return fundingSourceSubCatDao.getAllFundingSourceSubCat();
	}

	public List<ExternalSupportPo> getAllExternalSupport() {
		return externalSupport.getAllExternalSupport();
	}
	
	public List<PostDurationPo> getAllPostDuration() {
		return postDuration.getAllPostDuration();
	}

	public List<SubSpecialtyPo> getAllSubSpecialty() {
		return subSpecialtyDao.getAllSubSpecialty();
	}
	
	public List<MPRSPostStatusPo> getAllPostStatus() {
		return postStatus.getAllPostStatus();
	}

	public String getDuartionDescByDurationCode(String durationCode) {
		return postDuration.getDuartionDescByDurationCode(durationCode).getPostDurationDesc();
	}
	
	@Transactional
	public void performSendEmail(int requestUid) {
		// Look up all unsent email
		List<RequestEmailPo> unsendEmailList = requestEmailDao.getUnsendEmail(requestUid);
		System.out.println("unsendEmailList: " + unsendEmailList.size());
		
		for (int i=0; i<unsendEmailList.size(); i++) {
			// Perform send
			Properties connectionProperties = new Properties();
			
			connectionProperties.put("mail.smtp.host", emailHost);
			connectionProperties.put("mail.smtp.auth", "false");
			connectionProperties.put("mail.smtp.port", emailPort);
			Session session = Session.getDefaultInstance(connectionProperties);
			
			try { 
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(emailSender));
				if (unsendEmailList.get(i).getEmailTo() != null) {
					String tmpEmailToList = unsendEmailList.get(i).getEmailTo();
					tmpEmailToList = tmpEmailToList.replace(";", ",");
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(tmpEmailToList));
				}
				if (unsendEmailList.get(i).getEmailCc() != null) {
					String tmpEmailCCList = unsendEmailList.get(i).getEmailCc();
					tmpEmailCCList = tmpEmailCCList.replace(";", ",");
					message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(tmpEmailCCList));
				}
				
				if (!"".equals(environmentPrefix)) {
					message.setSubject("[" + environmentPrefix + "]" + unsendEmailList.get(i).getTitle());
				}
				else {
					message.setSubject(unsendEmailList.get(i).getTitle());
				}
				
				// message.setText(unsendEmailList.get(i).getContent());
				Multipart multipart = new MimeMultipart("alternative");
				MimeBodyPart htmlPart = new MimeBodyPart();
				String content = unsendEmailList.get(i).getContent().concat(unsendEmailList.get(i).getSuppInfo());
				htmlPart.setContent(content, "text/html; charset=utf-8");
				multipart.addBodyPart(htmlPart);
				message.setContent(multipart);
				// Transport.send(message);
				
				requestEmailDao.performSend(unsendEmailList.get(i));
			}
			catch (Exception ex) {
				EClaimLogger.error("performSendEmail:" + ex.getMessage(), ex);
			}
		}
	}

	public List<ProgramTypePo> getAllProgramType() {
		return programTypeDao.getAllProgramType(); 
	}

	// Added for CC176525
	public List<ProgramTypePo> getProgramTypeList(String postDuration, String postFteType, String annaulPlanInd) {
		return programTypeDao.getProgramTypeListByPostInfo(postDuration, postFteType, annaulPlanInd);
	}

	
	public List<CircumstancePo> getAllCircum() {
		return circumDao.getAllCircum();
	}

	
	public List<CircumstancePo> getAllActiveCircum() {
		return circumDao.getAllActiveCircum();
	}
	public CircumstancePo getCircumPoByCircumId(Integer circumId) {
		return circumDao.getCircumPoByCircumId(circumId);
	}
	
	public CircumstancePo getCircumPoByCircumCode(String circumCode) {
		return circumDao.getCircumPoByCircumCode(circumCode);
	}

	public List<OrganizationPo> getAllOrganization() {
		return organizationDao.getAllOrganization();
	}
	
	public List<OrganizationPo> getAllActiveOrganization() {
		return organizationDao.getAllActiveOrganization();
	}
	
	public List<OrganizationPo> searchOrganizationByName(String name) {
		return this.searchOrganizationByName(name,"","",false);
	}
	
	public List<OrganizationPo> searchOrganizationByName(String name, String loginUserId, String roleId) {
		return this.searchOrganizationByName(name,loginUserId,roleId,false);
	}
	
	public List<OrganizationPo> searchOrganizationByName(String name, String loginUserId, String roleId, boolean activeOnly) {
		return organizationDao.searchOrganization(name,loginUserId,roleId,activeOnly);
	}
	public OrganizationPo getOrgPoByOrgCode(int organizationId) {
		return organizationDao.getOrganizationByOrganizationId(organizationId);
	}
	public String getOrgNameByOrgCode(int organizationId) {
		return organizationDao.getOrgNameByOrgId(organizationId);
	}
	public String getClusIdByOrgId(int organizationId) {
		return organizationDao.getClusIdByOrgId(organizationId);
	}
	public List<StatusPo> getAllStatusList(){
		return statusDao.getAllStatus();
	}
	public String getStatusDesc(String statusCode) {
		return statusDao.getStatusDesc(statusCode);
	}

	public List<CoaPo> getAllCOA(String coaName) {
		return coaDao.getAllCOA(coaName);
	}

	public List<CoaPo> getAllInstCOA() {
		return getAllCOA("XXGL_INSTITUTION");
	}

	public List<CoaPo> getAllTypeCOA() {
		return getAllCOA("XXGL_TYPE");
	}

	public List<CoaPo> getAllSectionCOA() {
		return getAllCOA("XXGL_SECTION");
	}

	public List<CoaPo> getAllFundCOA() {
		return getAllCOA("XXGL_FUND");
	}

	public List<CoaPo> getAllAnalyCOA() {
		return getAllCOA("XXGL_ANALYTICAL");
	}
}
