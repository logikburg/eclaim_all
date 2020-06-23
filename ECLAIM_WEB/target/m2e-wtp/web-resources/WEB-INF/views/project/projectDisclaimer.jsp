<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<style>
.has-error .help-block{
	color: #ff605d;
}

</style>
<script>

	$(function(){
// 		$("#wrapper").toggleClass("active");
		
		$("#frmDetail").bootstrapValidator({
			excluded: [':disabled'],
			message: ' This value is not valid',
			live: "submitted",
			fields: {
				cb: {
	                message: 'This value is not valid',
	                validators: {
	                	choice : {
							min : 1,
							message : 'You must agree before submitting.'
						}
	                }
	            }
			},
		}).on('success.form.bv', function(e){

		});
	});
</script>

<!-- Page Content -->
<div id="page-content-wrapper">
	<div id="currStep" style="display: none">1</div>
	<div class="container-fluid">
		<div id="divSuccess" style="display:none" class="alert-box-success">
			<div class="alert-box-icon-success"><i class="fa fa-check"></i></div>
			<div id="divSuccessMsg" class="alert-box-content-success"></div>
		</div>
		<div id="divError" style="display:none" class="alert-box-danger">
			<div class="alert-box-icon-danger"><i class="fa fa-warning"></i></div>
			<div id="divErrorMsg" class="alert-box-content-danger"></div>
		</div>
		
		<%@ include file="/WEB-INF/views/project/projectHeading.jsp"%>
		<div class="panel panel-custom-primary" id="disclaimer">
		<div id="currStep"  style="display: none">1</div>
		<form id="frmDetail" action="<c:url value="/project/newProject"/>" method="GET">
			<div class="panel-body"
				style="background-color: #337ab7; border-color: #2e6da4">
				<div class="row justify-content-start">
					<div class="col-sm-12">
					<textarea class="form-control rounded-0" style="overflow-y: scroll"
						id="exampleFormControlTextarea1" rows="25" readonly>1.	The approving authority for SHS rests with CCEs. Projects not meeting the set of parameter should be brought up for HAHO's review on a case by case basis before CCE's approval. For projects related to doctors' critical manpower shortage, support from CCE / HCE / DHCE should be obtained before the case is brought up for HAHO's review. For extension of SHS projects which run beyond 12 months, CE's approval is required.

2.	When vetting and processing the SHS project application, Human Resources Section should ensure full justifications and sufficient information have been provided to support the application.

3.	When recruiting participants into the SHS project, Human Resources Section and Department/ Unit should take note of the followings:

(a)	Positions which require training supervision should not be included in the SHS project. For example, Resident Pharmacist is NOT a suitable rank for SHS projects as it is a training rank which requires supervision.
(b)	Jobs required for the project are defined based on the job content and competency requirements. Jobs, which required the qualifications, skills and competencies of an entry rank employee, e.g. Resident, RN, EN, Clerk III, PCAIIIB/OpAIIIB/EAIIIB etc are entry level jobs. Jobs, which required the qualifications, skills and competencies of a higher rank employee, e.g. AC, Cons, NO, APN, Clerk II, PCAIIIA/OpAIIIA/EAIIIA etc are higher level jobs. 
(c)	Jobs available should be open for recruitment, and details of the SHS should be promulgated to avoid possible allegations of favoritism.
(d)	Interested employees are required to apply for enrolment in writing by completing the application form template.
(e)	All applications should route through the employees' supervisors who should indicate whether the application is supported or not, to ensure that the service level and standards of the parent unit will not be affected by the employee's participation in the extra work.
(f)	Management will consider the applicant's qualifications, competency and experience upon receipt of an application to join the special project covered under the SHS.
(g)	Cluster has the flexibility to decide whether a formal recruitment board or a paper board should be held to select suitable employees to participate in the project. 
(h)	To avoid conflict of interest, officers responsible for selecting suitable candidates for the project should not be the ones who are receiving the Special Honorarium under the project.
(i)	Cross-cluster SHS should only be used when a hiring cluster is facing recruitment difficulty. Open recruitment should be conducted in all clusters to maintain fairness and transparency with responsibilities held by the hiring cluster.

4.	When arranging the SHS duty schedule for the participants, Head of Department/ Unit should take note of the followings:

(a)	Extra service sessions should be arranged by management on need basis, preferably on a stretch of four hours basis each. 
(b)	The hours worked should not be counted as the staff's normal weekly work hours.
(c)	Employees may, if they agree, choose to work voluntarily during any extra service sessions which may overlap with their scheduled rest days and annual leave days. The compensation for his/her voluntary work on his/her rest days will be the Special Honorarium paid by hospital. There will not be any further rest day compensation made to the employee for his/her voluntary work on his/her scheduled rest day.
(d)	Extra service sessions worked by the employees concerned would not infringe upon the legal requirements related to Statutory Holidays, and HA's policy on Public Holidays.
(e)	Priority should be given to on-call duties and doctors should not be put to work in SHS if being on-call, no matter on-site or off-site calls. 
(f)	The extra hours worked by the participants are not excessive in view of the need for adequate rest without compromising on patient safety.

5.	All relevant documentation should be kept for audit purpose.

						</textarea>
					</div>
				</div>
				
				<div class="row justify-content-start">
						<div class="form-group">
							<div class="col-sm-5" style="text-align: left">
								<input type="Checkbox" name="cb"
									class="custom-control-input"><font style="color: #fff">
									I've read and understood the statements above.</font>
							</div>
						</div>
				</div>
				
				<input type="hidden" id="projectStep" value="1" />
				<input type="hidden" name="projectId" value="">
				
				<div class="row">
					<div class="col-sm-10"></div>
					<div class="col-sm-2"  style="text-align:right">
					<button type="submit" class="btn btn-default btn-flat" >OK</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	</div>
</div>

<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>