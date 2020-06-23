package hk.org.ha.eclaim.bs.report.dao;

import java.util.Date;
import java.util.List;

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

public interface IReportDao {
	int create(ReportingPo reportingPo, UserPo user);

	List<ReportingPo> getReportListByType(String type, String userId);

	ReportingPo getReportByUid(String uid);

	List<Report1Po> getReport1Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String instCode, String staffGroupCode, String rankCode);

	List<Report2Po> getReport2Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String deptCode, String staffGroupCode, String rankCode);
	
	List<Report3Po> getReport3Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode);

	List<Report5Po> getReport5Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode);

	List<Report5RankPo> getReport5RankContent(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode);

	List<Report5ClusterPo> getReport5SubContent(List<Report5Po> listOfRequest);

	List<Report5RankClusterPo> getReport5RankSubContent(List<Report5RankPo> listOfRequest);
	
	List<Report4Po> getReport4ContentReminder(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode);
	
	List<Report4Po> getReport4ContentOutstanding(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode);
	
	List<Report6Po> getReport6Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode);

	List<Report7Po> getReport7Content(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String staffGroupCode, String rankCode);
	
	List<ReportHeldAgainstListPo> getReportHeldAgainstList(List<DataAccessPo> dataAccessList, Date dateIn, String clusterCode, String instCode, String deptCode, String staffGroupCode, String rankCode);
}
