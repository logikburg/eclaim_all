package hk.org.ha.eclaim.bs.report.svc.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hk.org.ha.eclaim.bs.report.dao.IBatchReportPwdDao;
import hk.org.ha.eclaim.bs.report.dao.IReportDao;
import hk.org.ha.eclaim.bs.report.svc.IReportSvc;
import hk.org.ha.eclaim.bs.report.po.Report1Po;
import hk.org.ha.eclaim.bs.report.po.Report2Po;
import hk.org.ha.eclaim.bs.report.po.Report3Po;
import hk.org.ha.eclaim.bs.report.po.Report4Po;
import hk.org.ha.eclaim.bs.report.po.Report5ClusterPo;
import hk.org.ha.eclaim.bs.report.po.Report5Po;
import hk.org.ha.eclaim.bs.report.po.Report5RankClusterPo;
import hk.org.ha.eclaim.bs.report.po.Report5RankPo;
import hk.org.ha.eclaim.bs.report.po.Report6Po;
import hk.org.ha.eclaim.bs.report.po.Report7Po;
import hk.org.ha.eclaim.bs.report.po.ReportHeldAgainstListPo;
import hk.org.ha.eclaim.bs.report.po.ReportingPo;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;

@Service("reportSvc")
public class ReportSvcImpl implements IReportSvc {

	@Autowired
	private IReportDao reportingDao;
	
	@Autowired
	private IBatchReportPwdDao batchReportPwdDao;
	
	@Transactional
	public int create(ReportingPo reportingPo, UserPo user) {
		return reportingDao.create(reportingPo, user);
	}

	public List<ReportingPo> getReportListByType(String type, String userId) {
		return reportingDao.getReportListByType(type, userId);
	}

	public ReportingPo getReportByUid(String uid) {
		return reportingDao.getReportByUid(uid);
	}

	public List<Report1Po> getReport1Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String instCode, String staffGroupCode, String rankCode) {
		return reportingDao.getReport1Content(dataAccessList, dateIn, clusterCode, instCode, staffGroupCode, rankCode);
	}

	public List<Report2Po> getReport2Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String deptCode, String staffGroupCode, String rankCode) {
		return reportingDao.getReport2Content(dataAccessList, dateIn, clusterCode, deptCode, staffGroupCode, rankCode);
	}

	public List<Report3Po> getReport3Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode) {
		return reportingDao.getReport3Content(dataAccessList, dateIn, clusterCode, staffGroupCode, rankCode);
	}
	
	public List<Report4Po> getReport4ContentReminder(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode) {
		return reportingDao.getReport4ContentReminder(dataAccessList, dateIn, clusterCode, staffGroupCode, rankCode);
	}
	
	public List<Report4Po> getReport4ContentOutstanding(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode) {
		return reportingDao.getReport4ContentOutstanding(dataAccessList, dateIn, clusterCode, staffGroupCode, rankCode);
	}

	public List<Report5Po> getReport5Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode) {
		return reportingDao.getReport5Content(dataAccessList, dateIn, clusterCode, staffGroupCode, rankCode);
	}
	
	public List<Report5RankPo> getReport5RankContent(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode) {
		return reportingDao.getReport5RankContent(dataAccessList, dateIn, clusterCode, staffGroupCode, rankCode);
	}

	public List<Report5ClusterPo> getReport5SubContent(List<Report5Po> listOfRequest) {
		return reportingDao.getReport5SubContent(listOfRequest);
	}

	public List<Report5RankClusterPo> getReport5RankSubContent(List<Report5RankPo> listOfRequest) {
		return reportingDao.getReport5RankSubContent(listOfRequest);
	}
	
	public List<Report6Po> getReport6Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode) {
		return reportingDao.getReport6Content(dataAccessList, dateIn, clusterCode, staffGroupCode, rankCode);
	}
	
	public List<Report7Po> getReport7Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode) {
		return reportingDao.getReport7Content(dataAccessList, dateIn, clusterCode, staffGroupCode, rankCode);
	}
	
	public List<ReportHeldAgainstListPo> getReportHeldAgainstList(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String instCode, String deptCode, String staffGroupCode, String rankCode) {
		return reportingDao.getReportHeldAgainstList(dataAccessList, dateIn, clusterCode, instCode, deptCode, staffGroupCode, rankCode);
	}
	
	public String getReportPwd(String clusterCode) {
		return batchReportPwdDao.getReportPwd(clusterCode);
	}
}
