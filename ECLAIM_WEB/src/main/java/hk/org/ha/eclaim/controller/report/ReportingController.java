package hk.org.ha.eclaim.controller.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.bs.cs.po.ClusterPo;
import hk.org.ha.eclaim.bs.cs.po.DepartmentPo;
import hk.org.ha.eclaim.bs.cs.po.InstitutionPo;
import hk.org.ha.eclaim.bs.cs.po.RankPo;
import hk.org.ha.eclaim.bs.cs.po.StaffGroupPo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
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
import hk.org.ha.eclaim.bs.report.svc.IPostExportSvc;
import hk.org.ha.eclaim.bs.report.svc.IReportSvc;
import hk.org.ha.eclaim.bs.security.po.DataAccessPo;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.helper.DateTimeHelper;
import hk.org.ha.eclaim.core.helper.StrHelper;
import hk.org.ha.eclaim.core.logger.EClaimLogger;
import hk.org.ha.eclaim.model.report.ReportHomeWebVo;
import hk.org.ha.eclaim.model.report.ReportingWebVo;
import hk.org.ha.eclaim.model.request.MPRSResultResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
public class ReportingController extends BaseController {
	@Autowired
	ICommonSvc commonSvc; 

	@Autowired
	ISecuritySvc securitySvc;

	@Autowired
	IReportSvc reportSvc;

	@Autowired
	IPostExportSvc exportSvc;

	@Value("${report.jasper.path}")
	private String reportTemplatePath;

	@Value("${report.resultHistDir}")
	private String reportResultHistDir;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
}
