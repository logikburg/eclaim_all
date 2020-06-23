package hk.org.ha.eclaim.bs.report.svc;

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
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;

public interface IPostExportSvc {
	
	public byte[] exportPost(List<DataAccessPo> dataAccessList, Date asAtDate, String clusterCode, String instCode, String deptCode, String staffGroupName, String rankName, String password) throws Exception;
		
	// Added for UT30067
	public byte[] exportPostByPostId(List<DataAccessPo> dataAccessList, String postId, String password) throws Exception;
	
	// Export Excel for Report 1
	public byte[] exportESVbyHospRank(List<Report1Po> listOfRequest, 
            						  List<ReportHeldAgainstListPo> listOfHeldAgainst,
			                          String asAt, String clusterCode, String hospital, String rank, String staffGroupName) throws Exception;

	// Export Excel for Report 2
	public byte[] exportESVbyDeptRank(List<Report2Po> listOfRequest, 
									  List<ReportHeldAgainstListPo> listOfHeldAgainst,
									  String dateAsAt, String clusterCode, String deptName, String staffGroupName, String rankName) throws Exception;

	// Export Excel for Report 3
	public byte[] exportESVDetail(List<Report3Po> listOfRequest, 
			                      List<ReportHeldAgainstListPo> listOfHeldAgainst,
			                      String dateAsAt, String clusterCode, String staffGroupName, String rankName) throws Exception;

	// Export Excel for Report 4
	public byte[] exportReviewTimeLtdPost(List<Report4Po> listReminder, 
										  List<Report4Po> listOutstanding,
										  String dateAsAt, String clusterCode, String staffGroupName, String rankName) throws Exception;

	// Export Excel for Report 5
	public byte[] exportNoOfVacanciesAfterOffset(List<Report5Po> listOfRequest,
											     List<Report5ClusterPo> listOfRequestCluster, 
											     List<ReportHeldAgainstListPo> listOfHeldAgainst,
											     String dateAsAt, String clusterCode, String staffGroupName, String rankName) throws Exception;
	
	public byte[] exportNoOfVacanciesAfterOffsetByRank(List<Report5RankPo> listOfRequest,
													   List<Report5RankClusterPo> listOfRequestCluster, 
													   List<ReportHeldAgainstListPo> listOfHeldAgainst,
													   String dateAsAt, String clusterCode, String staffGroupName, String rankName) throws Exception;

	// Export Excel for Report 6
	public byte[] exportClosedPostOccupied(List<Report6Po> listOfRequest, String clusterCode, String dateAsAt, String staffGroupName, String rankName) throws Exception;

	// Export Excel for Report 7
	public byte[] exportDiscrepanciesOnHCMPost(List<Report7Po> listOfRequest, String clusterCode, String dateAsAt, String staffGroupName, String rankName) throws Exception;
}
