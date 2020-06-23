<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="breadcrumbs">
			<a href="<c:url value="/home/home"/>"><i class="fa fa-home"></i>Home</a> > <a href="<c:url value="/reporting/reportHome"/>">Reporting</a>
		</div>
		<div class="section-title">
			<div class="title pull-left"><i class="fa fa-bar-chart"></i>Reporting</div>
		</div>
  		<div class="panel panel-custom-primary">
	  		<div class="panel-heading">
				<div class="panel-heading-title">Report Title</div>
			</div>
			<div class="panel-body">
				<div class="row">
					<div class="form-group">
						<div class="col-sm-12">
							<table class="table table-condensed">
								<tbody>
									<c:choose>
										<c:when test="${formBean.reportESVByHospRank == 'Y'}">
											<tr>
												<td style="text-align:left"><i class="fa fa-file-pdf-o"></i> ESV By Hospitals and Ranks</td>
												<td><button type="button" class="btn btn-primary" style="width:110px" onclick="document.location.href='reportESVbyHospRank'"><i class="fa fa-chevron-right"></i> Go</button></td>
									  		</tr>
										</c:when>
									</c:choose>
									<c:choose>
										<c:when test="${formBean.reportESVByDeptRank == 'Y'}">
									  		<tr>
												<td style="text-align:left"><i class="fa fa-file-pdf-o"></i> ESV By Departments and Ranks</td>
												<td><button type="button" class="btn btn-primary" style="width:110px" onclick="document.location.href='reportESVByDeptRank'"><i class="fa fa-chevron-right"></i> Go</button></td>
								  			</tr>
										</c:when>
									</c:choose>
						  			<c:choose>
										<c:when test="${formBean.reportESVDetail == 'Y'}">
										 	<tr>
												<td style="text-align:left"><i class="fa fa-file-pdf-o"></i> ESV Details</td>
												<td><button type="button" class="btn btn-primary" style="width:110px" onclick="document.location.href='reportESVDetail'"><i class="fa fa-chevron-right"></i> Go</button></td>
										  	</tr>
										</c:when>
									</c:choose>
								  	<c:choose>
										<c:when test="${formBean.reportReviewTimeLtdPost == 'Y'}">
										   	<tr>
												<td style="text-align:left"><i class="fa fa-file-pdf-o"></i> Review of Time-Limited Posts that are close to effective date</td>
												<td><button type="button" class="btn btn-primary" style="width:110px" onclick="document.location.href='reportReviewTimeLtdPost'"><i class="fa fa-chevron-right"></i> Go</button></td>
										  	</tr>
										</c:when>
									</c:choose>
								  	<c:choose>
										<c:when test="${formBean.reportNoOfVacanciesAfterOffset == 'Y'}">
										  	<tr>
												<td style="text-align:left"><i class="fa fa-file-pdf-o"></i> No. of Vacancies &amp; No. of Vacancies after offsetting the Contract Part-time &amp; Temporary Staff</td>
												<td><button type="button" class="btn btn-primary" style="width:110px" onclick="document.location.href='reportNoOfVacanciesAfterOffset'"><i class="fa fa-chevron-right"></i> Go</button></td>
										  	</tr>
										</c:when>
									</c:choose>
									
									<c:choose>
										<c:when test="${formBean.closedPostOccupied == 'Y'}">
											<tr>
												<td style="text-align:left"><i class="fa fa-file-pdf-o"></i> Closed and Frozen Post being occupied</td>
												<td><button type="button" class="btn btn-primary" style="width:110px" onclick="document.location.href='closedPostOccupied'"><i class="fa fa-chevron-right"></i> Go</button></td>
											</tr>
										</c:when>
									</c:choose>
									
									<c:choose>
										<c:when test="${formBean.discrepanciesOnHCMPost == 'Y'}">
											<tr>
												<td style="text-align:left"><i class="fa fa-file-pdf-o"></i> Discrepancies on HCM Position of Post ID and Employee</td>
												<td><button type="button" class="btn btn-primary" style="width:110px" onclick="document.location.href='discrepanciesOnHCMPost'"><i class="fa fa-chevron-right"></i> Go</button></td>
											</tr>
										</c:when>
									</c:choose>
									
									
									<c:choose>
										<c:when test="${formBean.dataExport == 'Y'}">
											<tr>
												<td style="text-align:left"><i class="fa fa-file-excel-o"></i> Data Export</td>
												<td><button type="button" class="btn btn-primary" style="width:110px" onclick="document.location.href='dataExport'"><i class="fa fa-chevron-right"></i> Go</button></td>
											</tr>
										</c:when>
									</c:choose>
									
									<c:choose>
										<c:when test="${formBean.transExport == 'Y'}">
											<tr>
												<td style="text-align:left"><i class="fa fa-file-excel-o"></i> Data Export by Post</td>
												<td><button type="button" class="btn btn-primary" style="width:110px" onclick="document.location.href='dataExportByPost'"><i class="fa fa-chevron-right"></i> Go</button></td>
											</tr>
										</c:when>
									</c:choose>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- ./#container -->
</div>
<!-- ./#page-content-wrapper -->

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>