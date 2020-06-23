package hk.org.ha.eclaim.controller.payment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import hk.org.ha.eclaim.bs.cs.po.OrganizationPo;
import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.payment.po.PaymentBatchPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentBatchTo;
import hk.org.ha.eclaim.bs.payment.po.PaymentDetailPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentDetailVo;
import hk.org.ha.eclaim.bs.payment.po.PaymentEnquiryPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentJobHoursVo;
import hk.org.ha.eclaim.bs.payment.svc.IPaymentBatchSvc;
import hk.org.ha.eclaim.bs.payment.svc.IPaymentDetailSvc;
import hk.org.ha.eclaim.bs.payment.svc.IPaymentJobSvc;
import hk.org.ha.eclaim.bs.project.po.ProjectJobPo;
import hk.org.ha.eclaim.bs.project.po.ProjectJobRankPo;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectJobSvc;
import hk.org.ha.eclaim.bs.project.svc.IProjectSvc;
import hk.org.ha.eclaim.bs.security.po.UserPo;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.constant.VersionConstant;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.core.logger.EClaimLogger;
import hk.org.ha.eclaim.model.payment.PaymentBatchVo;
import hk.org.ha.eclaim.model.payment.PaymentEnquirySearchVo;
import hk.org.ha.eclaim.model.payment.ValidPaymentEnquiryVo;

@Controller
public class PaymentController extends BaseController {

	@Value("${ss.app.environment}")
	private String environment;

	private String swVersion = VersionConstant.APP_VERSION_NO;

	@Value("${bsd.login.url}")
	private String bsdLoginUrl;

	@Autowired
	ICommonSvc commonSvc;

	@Autowired
	ISecuritySvc securitySvc;

	@Autowired
	IPaymentBatchSvc paymentBatchSvc;

	@Autowired
	IPaymentDetailSvc paymentDetailSvc;
	
	@Autowired
	IPaymentJobSvc paymentJobSvc;

	@Autowired
	IProjectJobSvc jobSvc;

	@Autowired
	IProjectSvc projectSvc;

	@RequestMapping(value = "/payment/review", method = RequestMethod.GET)
	public ModelAndView review(@RequestParam(required = false) String claimId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String userId = this.getSessionUser(request).getUserId();
		ValidPaymentEnquiryVo webVo = new ValidPaymentEnquiryVo();

		UserPo user = securitySvc.findUser(userId);
		webVo.setUserName(user.getUserName());
		webVo.setUser(user);

		System.out.println("userId : " + user.getUserId());

		int userRoleUid = (int) request.getSession().getAttribute("currentUserRoleId");

		System.out.println("userRoleUid: " + userRoleUid);

		List<PaymentEnquiryPo> paymentBatchList = paymentBatchSvc.getAllPaymentBatchForEnquiry(null, null, null, null,
				null, null, null, null, null, null, null, null, null);
		System.out.println("PaymentBatchList.size():  " + paymentBatchList.size());
		List<PaymentBatchVo> paymentBatchVoList = new ArrayList<PaymentBatchVo>();

		for (PaymentEnquiryPo temp : paymentBatchList) {
			PaymentBatchVo vo = new PaymentBatchVo(temp);
			paymentBatchVoList.add(vo);
		}
		webVo.setPaymentBatchList(paymentBatchVoList);

		// to highlight the menu
		request.getSession().setAttribute("currentView", "validate");

		if (claimId != null) {
			// Detail Mode
			int batchId = Integer.parseInt(claimId);
			PaymentBatchVo batchVo = new PaymentBatchVo();
			List<PaymentDetailPo> listPaymentDetail = paymentDetailSvc.getDetailByBatchDetailId(batchId);
			Map<String, PaymentBatchTo> mapPaymentBatch = new TreeMap<String, PaymentBatchTo>();
			PaymentBatchTo payTo = null;

			Integer outstandingCnt = 0;

			for (PaymentDetailPo temp : listPaymentDetail) {
				if ("V".equals(temp.getStatus())) {
					outstandingCnt = outstandingCnt + 1;
				}
				String ky = temp.getAsgNo() + temp.getJob();
				if (mapPaymentBatch.containsKey(ky)) {
					payTo = mapPaymentBatch.get(ky);
					payTo.getDutyDate().put(temp.getDay(), temp.getTotalHour());
					payTo.setTotalWorkedHours(payTo.getTotalWorkedHours() + temp.getTotalHour());
				} else {
					payTo = new PaymentBatchTo();
					payTo.setAsgNo(temp.getAsgNo());
					payTo.setBatchId(temp.getBatchId());
					payTo.setReason(temp.getReason());
					payTo.setCluster("HEC");
					payTo.setHosp("PYN");
					payTo.setEmpNo("EMP00001");
					payTo.setStaffRank("WM");
					payTo.setJob(temp.getJob());
					payTo.setHaCs("HA");
					payTo.setStatus("A");
					payTo.setEarnedMonth(temp.getEarnedMonthYear());
					mapPaymentBatch.put(ky, payTo);
					payTo.getDutyDate().put(temp.getDay(), temp.getTotalHour());
					payTo.setTotalWorkedHours(temp.getTotalHour());
				}
			}
			List<PaymentBatchTo> tmpBatch = new ArrayList<PaymentBatchTo>(mapPaymentBatch.values());
			batchVo.setListDetailBatch(tmpBatch);

			PaymentBatchPo paymentBatchPo = paymentBatchSvc.getPaymentBatchByBatchId(batchId);
			String departmentName = paymentBatchSvc.getDepartmentName(batchId);
			batchVo.setDepartmentName(departmentName);
			paymentBatchPo.setDepartmentName(departmentName);

			List<String> jobRanks = (List<String>) paymentBatchSvc.getJobRanks(batchId);
			batchVo.setJobs(String.join(", ", jobRanks));

			batchVo.setStatus(commonSvc.getStatusDesc(paymentBatchPo.getStatus()));
			
			if (paymentBatchPo != null) {
				batchVo.setPaymentType(paymentBatchPo.getPaymentType());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy");
				batchVo.setPayMonth(simpleDateFormat.format(paymentBatchPo.getPayMonth()));
				batchVo.setTotalSum(paymentBatchPo.getTotalTxn());
				batchVo.setTransferredSum(paymentBatchPo.getTransferred());
				batchVo.setOutstandingSum(outstandingCnt);
				batchVo.setValidatedSum(paymentBatchPo.getTotalTxn() - outstandingCnt);
			}
			
			request.getSession().setAttribute("batchPo", paymentBatchPo);
			request.getSession().setAttribute("batchTo", tmpBatch);

			return new ModelAndView("payment/detailReview", "formBean", batchVo);
		}

		return new ModelAndView("payment/review", "formBean", webVo);
	}

	@RequestMapping(value = "/payment/createBatch", method = RequestMethod.POST)
	public ModelAndView createBatch(@ModelAttribute PaymentBatchVo batchVo, HttpServletRequest request)
			throws Exception {

		PaymentBatchPo batchPo = new PaymentBatchPo();

		// SMog - post request instead of get to handle if batch is selected from
		// inquiry page.
		if (batchVo.getBatchId() != null) {
			int batchId = Integer.parseInt(batchVo.getBatchId());

			List<PaymentDetailPo> listPaymentDetail = paymentDetailSvc.getDetailByBatchDetailId(batchId);
			Map<String, PaymentBatchTo> mapPaymentBatch = new TreeMap<String, PaymentBatchTo>();
			PaymentBatchTo payTo = null;

			for (PaymentDetailPo temp : listPaymentDetail) {
				String ky = temp.getAsgNo() + temp.getJob();
				if (mapPaymentBatch.containsKey(ky)) {
					payTo = mapPaymentBatch.get(ky);
					payTo.getDutyDate().put(temp.getDay(), temp.getTotalHour());
				} else {
					payTo = new PaymentBatchTo();
					payTo.setAsgNo(temp.getAsgNo());
					payTo.setBatchId(temp.getBatchId());
					payTo.setCluster("HEC");
					payTo.setHosp("PYN");
					payTo.setEmpNo("EMP00001");
					payTo.setStaffRank("WM");
					payTo.setJob(temp.getJob());
					payTo.setHaCs("HA");
					payTo.setStatus("A");
					payTo.setReason(temp.getReason());
					payTo.setEarnedMonth(temp.getEarnedMonthYear());
					mapPaymentBatch.put(ky, payTo);
					payTo.getDutyDate().put(temp.getDay(), temp.getTotalHour());
				}
			}
			List<PaymentBatchTo> tmpBatch = new ArrayList<PaymentBatchTo>(mapPaymentBatch.values());
			batchVo.setListDetailBatch(tmpBatch);

			String departmentName = paymentBatchSvc.getDepartmentName(batchId);
			batchVo.setDepartmentName(departmentName);

			List<?> jobRanks = paymentBatchSvc.getJobRanks(batchId);
			batchVo.setJobs(jobRanks.toString());

			ProjectPo projectPo = paymentBatchSvc.getProjectDetail(batchId);
			
			// TODO - batchId should be real
			@SuppressWarnings("unchecked")
			List<PaymentJobHoursVo> paymentJobHoursVo = (List<PaymentJobHoursVo>) paymentBatchSvc.getPaymentJobHours(111);
			batchVo.setPaymentJobHours(paymentJobHoursVo);

			if (projectPo != null) {
				batchVo.setProjectName(projectPo.getProjectName());
				batchVo.setProjectDuration(projectPo.getFromDate().toString(), projectPo.getToDate().toString());
				batchVo.setProjectType(projectPo.getProjectType());
			}

			System.out.println("projectName is " + batchVo.getProjectName());

		} else {
			int departId = batchVo.getDepartmentId().length() < 1 ? 0 : Integer.parseInt(batchVo.getDepartmentId());
			batchPo.setDepartmentId(departId);
			batchPo.setLocationId(0);
			int projId = batchVo.getProjectId().length() < 1 ? 0 : Integer.parseInt(batchVo.getProjectId());
			batchPo.setProjectId(projId);
			batchPo.setPaymentType(batchVo.getPaymentType());

			batchPo.setNoOfApprovedTxn(0);
			batchPo.setNoOfHaCsPayrollTxn(0);
			batchPo.setNoOfOnHoldTxn(0);
			batchPo.setNoOfPendingTransferTxn(0);
			batchPo.setNoOfReviewedTxn(0);
			batchPo.setTotalTxn(0);
			batchPo.setTransferred(0);
			batchPo.setStatus("U");
			
			String month = batchVo.getPayMonth().substring(0, 2);
			String year = batchVo.getPayMonth().substring(3, 7);
			Calendar c = Calendar.getInstance();
			c.set(Integer.parseInt(year), Integer.parseInt(month), 1);
			batchPo.setPayMonth(c.getTime());
			
			// Get the user name from cookie
			String userId = this.getSessionUser(request).getUserId();
			UserPo user = securitySvc.findUser(userId);
			Integer newBatchId = paymentBatchSvc.insert(batchPo, user);
			
			// update Job Group Table
			List<ProjectJobPo> jobs = jobSvc.getProjectJobListByProjectId(projId);
			Map<String, Integer> jobMap = new HashMap<String, Integer>();
			for (ProjectJobPo job : jobs) {
				List<ProjectJobRankPo> jobRanks = jobSvc.getJobRankPoList(job.getJobGroupId());
				for (ProjectJobRankPo temp : jobRanks) {
					jobMap.put(temp.getRank(), temp.getJobGroupId());
				}
			}
			
			HashMap<String, Integer> mapJobList = new HashMap<String, Integer>();
			for (Entry<String, Integer> key : jobMap.entrySet()) {
				for (String tempJob : batchVo.getJobGroupId()) {
					if (tempJob.equals(key.getKey())) {
						mapJobList.put(tempJob, key.getValue());
					}
				}
			};
			
			paymentJobSvc.insert(newBatchId, mapJobList, user);
			
			ProjectPo projectPo = paymentBatchSvc.getProjectDetail(newBatchId);
			if(projectPo != null) {
				batchPo.setProjectName(projectPo.getProjectName());
				batchVo.setProjectName(projectPo.getProjectName());
				
				String departmentName = paymentBatchSvc.getDepartmentName(newBatchId);
				batchVo.setDepartmentName(departmentName);
				batchPo.setDepartmentName(departmentName);
				
				List<String> jobRanks = (List<String>) paymentBatchSvc.getJobRanks(newBatchId);
				batchVo.setJobs(String.join(", ", jobRanks));
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy");
				String fromDate = simpleDateFormat.format(projectPo.getFromDate());
				String toDate = simpleDateFormat.format(projectPo.getToDate());
				
				batchVo.setProjectDuration(fromDate, toDate);
				batchVo.setProjectType(projectPo.getProgramType());
			}
			
			batchVo.setStatus(commonSvc.getStatusDesc(batchPo.getStatus()));
			
			request.getSession().setAttribute("batchPo", batchPo);
		}
		
		return new ModelAndView("payment/detailReview", "formBean", batchVo);
	}


	@RequestMapping(value = "/payment/search_ajax", method = RequestMethod.GET)
	public @ResponseBody String searchClaimAjax(@ModelAttribute PaymentEnquirySearchVo vo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		List<PaymentEnquiryPo> paymentBatchList = paymentBatchSvc.getAllPaymentBatchForEnquiry(vo.getDeptId(),
				vo.getProjectName(), vo.getProjectId(), vo.getEmpNo(), vo.getPayMonth(), vo.getEarnedMonth(),
				vo.getUnProcess(), vo.getValidated(), vo.getPartiallyValidated(), vo.getPendingRollback(),
				vo.getPendingTransfer(), vo.getTransferred(), vo.getPartiallyTransferred());
		System.out.println("PaymentBatchList.size():  " + paymentBatchList.size());

		ObjectMapper mapper = new ObjectMapper();
		String jsonRecords = mapper.writeValueAsString(paymentBatchList);

		EClaimLogger.info("prepared records to transfer to view has content: \n" + jsonRecords);
		return jsonRecords;
	}
	
	@RequestMapping(value = "/payment/search", method = RequestMethod.GET)
	public ModelAndView searchClaim(@ModelAttribute PaymentEnquirySearchVo vo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String userId = this.getSessionUser(request).getUserId();
		ValidPaymentEnquiryVo webVo = new ValidPaymentEnquiryVo();

		UserPo user = securitySvc.findUser(userId);
		webVo.setUserName(user.getUserName());
		webVo.setUser(user);

		System.out.println("userId : " + user.getUserId());

		int userRoleUid = (int) request.getSession().getAttribute("currentUserRoleId");

		System.out.println("userRoleUid: " + userRoleUid);

		List<PaymentEnquiryPo> paymentBatchList = paymentBatchSvc.getAllPaymentBatchForEnquiry(vo.getDeptId(),
				vo.getProjectName(), vo.getProjectId(), vo.getEmpNo(), vo.getPayMonth(), vo.getEarnedMonth(),
				vo.getUnProcess(), vo.getValidated(), vo.getPartiallyValidated(), vo.getPendingRollback(),
				vo.getPendingTransfer(), vo.getTransferred(), vo.getPartiallyTransferred());
		System.out.println("PaymentBatchList.size():  " + paymentBatchList.size());
		List<PaymentBatchVo> paymentBatchVoList = new ArrayList<PaymentBatchVo>();

		for (PaymentEnquiryPo temp : paymentBatchList) {
			PaymentBatchVo vo1 = new PaymentBatchVo(temp);
			paymentBatchVoList.add(vo1);
		}
		webVo.setPaymentBatchList(paymentBatchVoList);

		// to highlight the menu
		request.getSession().setAttribute("currentView", "validate");
		
		//save the search criteria in session to recall users.
		request.getSession().setAttribute("searchVo", vo);

		return new ModelAndView("payment/review", "formBean", webVo);
	}

	@RequestMapping(value = "/payment/delete", method = RequestMethod.POST)
	public ModelAndView deleteClaim(@RequestParam String batchId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			int bid = Integer.parseInt(batchId);
			PaymentBatchPo paymentBatch = (PaymentBatchPo) request.getSession().getAttribute("batchPo");
			paymentBatchSvc.delete(paymentBatch);
		} catch (NumberFormatException ne) {
			throw ne;
		}

		return new ModelAndView("redirect:/payment/review");
	}

	@RequestMapping(value = "/payment/upload-payment-detail", method = RequestMethod.POST, consumes = "multipart/form-data")
	public @ResponseBody String uploadPaymentDetail(@RequestParam("fn") MultipartFile multipartFile,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = multipartFile.getOriginalFilename();
		EClaimLogger.info("Upload - Uploaded: filename=" + fileName + ", " + multipartFile.getSize() + " bytes");
		File f = new File(fileName);
		EClaimLogger.info("Transfered the uploaded file to path " + f.getAbsolutePath());
		multipartFile.transferTo(f);

		// Get Batch Po
		PaymentBatchPo batchPo = new PaymentBatchPo();
		batchPo = (PaymentBatchPo) request.getSession().getAttribute("batchPo");
		
		// process and validation
		PaymentDetailVo paymentDetailVo = paymentDetailSvc.prepareBatchFromXls(batchPo, f.getAbsolutePath());
		Map<String, PaymentBatchTo> mapPaymentBatch = new TreeMap<String, PaymentBatchTo>();
		PaymentBatchTo payTo = null;

		for (PaymentDetailPo temp : paymentDetailVo.getLstBatchRecords()) {
			String ky = temp.getAsgNo() + temp.getJob();
			if (mapPaymentBatch.containsKey(ky)) {
				payTo = mapPaymentBatch.get(ky);
				payTo.getDutyDate().put(temp.getDay(), temp.getTotalHour());
				payTo.setTotalWorkedHours(payTo.getTotalWorkedHours() + temp.getTotalHour());
			} else {
				payTo = new PaymentBatchTo();
				payTo.setAsgNo(temp.getAsgNo());
				payTo.setBatchId(temp.getBatchId());
				payTo.setCluster("HEC");
				payTo.setHosp("PYN");
				payTo.setEmpNo("EMP00001");
				payTo.setStaffRank("WM");
				payTo.setJob(temp.getJob());
				payTo.setHaCs("HA");
				payTo.setStatus("U");
				payTo.setReason(temp.getReason());
				payTo.setEarnedMonth(temp.getEarnedMonthYear());
				mapPaymentBatch.put(ky, payTo);
				payTo.getDutyDate().put(temp.getDay(), temp.getTotalHour());
				payTo.setTotalWorkedHours(temp.getTotalHour());
			}
		}

		paymentDetailVo.setMapPaymentBatch(mapPaymentBatch);

		ObjectMapper mapper = new ObjectMapper();
		String jsonRecords = mapper.writeValueAsString(paymentDetailVo);

		EClaimLogger.info("prepared records to transfer to view has content: \n" + jsonRecords);
		return jsonRecords;
	}

	@RequestMapping(value = "/payment/generate-payment-template", method = RequestMethod.GET)
	public void downloadXlsTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PaymentBatchPo batchPo = new PaymentBatchPo();
		batchPo = (PaymentBatchPo) request.getSession().getAttribute("batchPo");
		List<PaymentBatchTo> tmpBatch = (List<PaymentBatchTo>) request.getSession().getAttribute("batchTo");

		Calendar cald = Calendar.getInstance();
		cald.setTime(batchPo.getPayMonth());
		String payMonth = (cald.get(Calendar.MONTH) + 1) + "" + cald.get(Calendar.YEAR);
		String fname = String.format("%s %s.xlsx", batchPo.getProjectName(), payMonth);

		// process to download
		try {
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fname + "\"");
			response.setContentType("application/vnd.ms-excel");
			if (tmpBatch != null && tmpBatch.size() > 0) {
				paymentDetailSvc.prepareXlsForOutStreamWithDetails(response.getOutputStream(), batchPo, tmpBatch);
			} else {
				paymentDetailSvc.prepareXlsForOutStream(response.getOutputStream(), batchPo);
			}
			response.flushBuffer();
		} catch (Exception e) {
			EClaimLogger.error(e.getMessage());
		}
	}

	@RequestMapping(value = "/payment/getJobListByProject", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getProjectNameList(@RequestParam String projectId) {
		List<ProjectJobPo> jobs = jobSvc.getProjectJobListByProjectId(Integer.parseInt(projectId));
		Map<String, String> jobMap = new HashMap<String, String>();
		
		Map<String, Object> project = new HashMap<String, Object>();
		for (ProjectJobPo job : jobs) {
			List<ProjectJobRankPo> jobRanks = jobSvc.getJobRankPoList(job.getJobGroupId());
			for (ProjectJobRankPo temp : jobRanks) {
				jobMap.put(temp.getRank(), temp.getRank());
			}
		}
		ProjectPo po = paymentBatchSvc.getProjectDetail(Integer.parseInt(projectId));
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy");
		String fromDate = simpleDateFormat.format(po.getFromDate());
		String toDate = simpleDateFormat.format(po.getToDate());
		String pt = po.getProgramType();
		
		project.put("ranks", jobMap);
		project.put("prjDuration", fromDate + " - " + toDate);
		project.put("projectType", pt);
		
		return project;
	}

	@RequestMapping(value = "/payment/getProjectDepartmentList", method = RequestMethod.GET)
	public @ResponseBody Map<Integer, String> getProjectDepartmentList(@RequestParam String term) {

		List<OrganizationPo> organList = paymentBatchSvc.getProjectDepartmentList(term);

		return organList.stream().collect(Collectors.toMap(OrganizationPo::getOrganizationId, OrganizationPo::getName));
	}

	@RequestMapping(value = "/payment/getProjectNameList", method = RequestMethod.GET)
	public @ResponseBody Map<Integer, String> getProjectNameList(@RequestParam String name, String deptId,
			String[] status) {
		List<ProjectPo> projects = projectSvc.searchProjectByName(name, deptId, status);
		return projects.stream().collect(Collectors.toMap(ProjectPo::getProjectId, ProjectPo::getProjectName));
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/payment/submitBatch", method = RequestMethod.GET)
	public ModelAndView submitBatch(@RequestParam(required = false) String claimId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String userId = this.getSessionUser(request).getUserId();
		ValidPaymentEnquiryVo webVo = new ValidPaymentEnquiryVo();

		UserPo user = securitySvc.findUser(userId);
		webVo.setUserName(user.getUserName());
		webVo.setUser(user);
		
		PaymentBatchPo batchPo = new PaymentBatchPo();
		batchPo = (PaymentBatchPo) request.getSession().getAttribute("batchPo");
		paymentBatchSvc.updateStatus(batchPo, "PT", user);
		
		List<PaymentEnquiryPo> paymentBatchList = paymentBatchSvc.getAllPaymentBatchForEnquiry(null, null, null, null,
				null, null, null, null, null, null, null, null, null);
		System.out.println("PaymentBatchList.size():  " + paymentBatchList.size());
		List<PaymentBatchVo> paymentBatchVoList = new ArrayList<PaymentBatchVo>();

		for (PaymentEnquiryPo temp : paymentBatchList) {
			PaymentBatchVo vo = new PaymentBatchVo(temp);
			paymentBatchVoList.add(vo);
		}
		webVo.setPaymentBatchList(paymentBatchVoList);

		// to highlight the menu
		request.getSession().setAttribute("currentView", "validate");
		
		return new ModelAndView("payment/review", "formBean", webVo);
	}
}
