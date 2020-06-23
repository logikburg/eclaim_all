package hk.org.ha.eclaim.controller.payment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import hk.org.ha.eclaim.bs.cs.svc.ICommonSvc;
import hk.org.ha.eclaim.bs.payment.po.PaymentBatchPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentBatchTo;
import hk.org.ha.eclaim.bs.payment.po.PaymentDetailPo;
import hk.org.ha.eclaim.bs.payment.po.PaymentEnquiryPo;
import hk.org.ha.eclaim.bs.payment.svc.IPaymentBatchSvc;
import hk.org.ha.eclaim.bs.payment.svc.IPaymentDetailSvc;
import hk.org.ha.eclaim.bs.project.po.ProjectPo;
import hk.org.ha.eclaim.bs.project.svc.IProjectJobSvc;
import hk.org.ha.eclaim.bs.project.svc.IProjectSvc;
import hk.org.ha.eclaim.bs.security.svc.ISecuritySvc;
import hk.org.ha.eclaim.core.constant.VersionConstant;
import hk.org.ha.eclaim.core.controller.BaseController;
import hk.org.ha.eclaim.model.payment.PaymentBatchVo;
import hk.org.ha.eclaim.model.payment.ValidPaymentEnquiryVo;

@Controller
public class PaymentTransferController extends BaseController {

	@Value("${ss.app.environment}")
	private String environment;

	@SuppressWarnings("unused")
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
	IProjectJobSvc jobSvc;

	@Autowired
	IProjectSvc projectSvc;

	@RequestMapping(value = "/payment/transfer", method = RequestMethod.GET)
	public ModelAndView transferEnquiry(@RequestParam(required = false) String paymentId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ValidPaymentEnquiryVo webVo = new ValidPaymentEnquiryVo();

		if (paymentId == null) {
			// Enquiry Mode
			List<PaymentEnquiryPo> paymentBatchList = paymentBatchSvc.getAllPaymentBatchForEnquiry(null, null, null,
					null, null, null, null, null, null, true, true, true, true);
			System.out.println("PaymentBatchList.size():  " + paymentBatchList.size());
			List<PaymentBatchVo> paymentBatchVoList = new ArrayList<PaymentBatchVo>();

			for (PaymentEnquiryPo temp : paymentBatchList) {
				PaymentBatchVo vo = new PaymentBatchVo(temp);
				paymentBatchVoList.add(vo);
			}
			webVo.setPaymentBatchList(paymentBatchVoList);

			// to highlight the menu
			request.getSession().setAttribute("currentView", "transfer");

			return new ModelAndView("payment/followUp", "formBean", webVo);
		} else {
			// Detail Mode
			int batchId = Integer.parseInt(paymentId);
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

			String departmentName = paymentBatchSvc.getDepartmentName(batchId);
			batchVo.setDepartmentName(departmentName);

			List<String> jobRanks = (List<String>) paymentBatchSvc.getJobRanks(batchId);
			batchVo.setJobs(String.join(", ", jobRanks));

			PaymentBatchPo paymentBatchPo = paymentBatchSvc.getPaymentBatchByBatchId(batchId);
			ProjectPo projectPo = paymentBatchSvc.getProjectDetail(batchId);
			if (paymentBatchPo != null) {
				batchVo.setPaymentType(paymentBatchPo.getPaymentType());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy");
				batchVo.setStatus(paymentBatchPo.getStatus());
				batchVo.setPayMonth(simpleDateFormat.format(paymentBatchPo.getPayMonth()));
				batchVo.setTotalSum(paymentBatchPo.getTotalTxn());
				batchVo.setTransferredSum(paymentBatchPo.getTransferred());
				batchVo.setOutstandingSum(outstandingCnt);
				batchVo.setValidatedSum(paymentBatchPo.getTotalTxn() - outstandingCnt);
			}

			if (projectPo != null) {
				batchVo.setProjectName(projectPo.getProjectName());
				batchVo.setProjectType(projectPo.getProjectType());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
				String fromDate = simpleDateFormat.format(new Date());
				String toDate = simpleDateFormat.format(new Date());
				/*
				 * String fromDate = simpleDateFormat.format(projectPo.getFromDate()); String
				 * toDate = simpleDateFormat.format(projectPo.getToDate());
				 */
				batchVo.setProjectDuration(fromDate, toDate);
				batchVo.setProjectType(projectPo.getProjectType());
			}

			return new ModelAndView("payment/quota", "formBean", batchVo);
		}

	}

	@RequestMapping(value = "/payment/transfer", method = RequestMethod.POST)
	public ModelAndView transfer(@RequestParam String detailGroupId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ValidPaymentEnquiryVo webVo = new ValidPaymentEnquiryVo();

		// Enquiry Mode
		List<PaymentEnquiryPo> paymentBatchList = paymentBatchSvc.getAllPaymentBatchForEnquiry(null, null, null, null,
				null, null, null, null, null, true, true, true, true);
		System.out.println("PaymentBatchList.size():  " + paymentBatchList.size());
		List<PaymentBatchVo> paymentBatchVoList = new ArrayList<PaymentBatchVo>();

		for (PaymentEnquiryPo temp : paymentBatchList) {
			PaymentBatchVo vo = new PaymentBatchVo(temp);
			paymentBatchVoList.add(vo);
		}
		webVo.setPaymentBatchList(paymentBatchVoList);

		// to highlight the menu
		request.getSession().setAttribute("currentView", "transfer");

		return new ModelAndView("payment/followUp", "formBean", webVo);

	}

}
