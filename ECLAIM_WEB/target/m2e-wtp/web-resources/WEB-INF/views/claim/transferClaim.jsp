<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.1.0/chosen.min.css" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.1.0/chosen.jquery.min.js"></script>
<style>
.claim {
	
}

.claim [class*="row"] {
	margin-bottom: 0px;
	margin-top: 0px;
}

.claim [class*="col-"] {
	padding-top: 0.8rem;
	padding-bottom: 0.8rem;
}

.claim [class*="col-text"] {
	padding-top: 0.5rem;
	padding-bottom: 0.5rem;
}

.claim [class*="col-select"] {
	padding-top: 0.7rem;
	padding-bottom: 0.7rem;
}

.claim table>tbody>tr>td.bg-green {
	background-color: #4eea1c;
}

.claim .work-hours {
	
}

.claim .total-hours-worked {
	
}

.claim table {
	border: 2px solid #dddddd;
}

.claim table>thead>tr>th, .claim table>tbody>tr>td {
	border: 1px solid #ddd;
	padding: 3px;
	text-align: center;
	vertical-align: middle;
	font-size: 11px;
}

.chosen-container {
	font-size: 12px;
}

.chosen-container-multi .chosen-choices li.search-choice {
	padding: 1px 20px 1px 3px;
	border: 1px solid #dfdfdf;
	margin-top: 3px;
	margin-bottom: 2px;
	margin-left: 5px;
	background-color: #dfdfdf;
}

.chosen-container-multi .chosen-choices li.search-choice .search-choice-close
	{
	top: 2px;
	right: 3px;
}

.chosen-container-multi .chosen-choices {
	border: 1px solid #dfdfdf;
	background-image: none;
	-webkit-appearance: textfield;
}

.search-choice>span {
	font-size: 11px;
}

.chosen-container .search-choice .search-choice-close {
	top: 3px;
}

.chosen-container-multi .chosen-choices li.search-field input[type=text]
	{
	margin: 3px 0;
	height: 17px;
}

.chosen-container.chosen-container-multi {
	width: 70%;
}

.chosen-container .chosen-drop {
	border: 1px solid #dfdfdf;
}

.chosen-container-active .chosen-choices {
	box-shadow: none;
}

table.legend {
	border: 1px solid #ddd;
	margin-top: 20px;
}

table.legend>thead>tr>th, table.legend>tbody>tr>td {
	border: 1px solid #ddd;
	padding: 3px;
	text-align: center;
	vertical-align: middle;
	font-size: 11px;
}

.breadcrumb {
	padding: 15px 15px;
	margin-bottom: 15px
}

.breadcrumb .breadcrumb-item {
	width: 92px;
	color: #2f79b9;
}

.breadcrumb>li i {
	font-size: 48px;
	float: left;
	margin-right: 8px;
	margin-top: -5px;
	vertical-align: middle;
	line-height: 45px;
	display: table-cell;
}

.breadcrumb>li a:focus, a:hover {
	text-decoration: none
}

.breadcrumb>li.active a, .breadcrumb>li.active a:focus {
	text-decoration: none;
	color: #777;
}

.breadcrumb>li.active a:hover {
	text-decoration: none;
	color: #337ab7;
}

.breadcrumb>li+li .arrow {
	font-size: 20px;
	font-style: normal;
	font-weight: bold;
}

.breadcrumb>li+li .arrow:before {
	content: ">>\00a0";
	font-size: 20px;
}

.breadcrumb>li+li:before {
	content: none;
}

table#claim-table {
	width: 100%;
}

span.prog-status {
	display: none;
	font-style: italic;
	font-size: 11px;
}
</style>
<script>
	var claimTable = '#claim-table';
	$(function() {
		$('.chosen-select').chosen();
		$('.chosen-container.chosen-container-multi').width('70%');

		TableEvents = {
			'check.table' : 'onCheck',
			'uncheck.table' : 'onUncheck',
			'check-all.table' : 'onCheckAll',
			'uncheck-all.table' : 'onUncheckAll'
		}

		that = this;
		this.$elTable = $(claimTable);
		this.$selectAll = this.$elTable.find('[name="chkSelectAll"]');
		this.$selectItem = this.$elTable.find('[name="chkSelectItem"]');
		this.$selectAll.off('click').on('click', function() {
			var checked = $(this).prop('checked');
			that.$selectItem.each(function() {
				$(this).closest('tr').find('input').prop('checked', checked);
			});
			that.$elTable.trigger(checked ? TableEvents['check-all.table'] : TableEvents['uncheck-all.table']);
		});

		this.$selectItem.off('click').on('click', function() {
			var checked = $(this).prop('checked');
			that.$elTable.trigger(checked ? TableEvents['check.table'] : TableEvents['uncheck.table']);
			updateSelected();
		});

		updateSelected = function() {
			var checkAll = that.$selectItem.filter(':enabled').length
					&& that.$selectItem.filter(':enabled').length === that.$selectItem.filter(':enabled').filter(
							':checked').length;
			that.$selectAll.prop('checked', checkAll);
		};
	});

	$(document).ready(function(e) {
		that = this;

		this.$selectItem.closest('tr').each(function() {
			var totalWH_ = 0;
			$(this).find('td.work-hours').each(function() {
				totalWH_ += Number(this.innerText);
			});
			$(this).find('td.total-hours-worked').text(totalWH_);
		});
		
		$("[class$='hours']").hide();
		$("[class$='coa']").hide();

		updateLegend = (function() {
			var total = that.$selectItem.filter(':enabled').length;
			var selected = that.$selectItem.filter(':enabled').filter(':checked').length;
			var onhold = total - selected;
			$('.legend .total').text(total);
			$('.legend .selected').text(selected);
			$('.legend .onhold').text(onhold);
		});

		updateLegend();

		$(claimTable).on('onCheck onCheckAll', function(e) {
			updateLegend();
			console.log('onCheck');
		});

		$(claimTable).on('onUncheck onUncheckAll', function(e) {
			updateLegend();
			console.log('onUncheck');
		});
				
		<%
			String requestURI = (String) request.getAttribute("javax.servlet.forward.request_uri");
			String[] el = requestURI.split("/");
			String currPage = el.length > 0 ? el[el.length - 1] : "";
			currPage = currPage.replace(".jsp", "").replaceAll("(?i)claim", "");%>
		var filteredDivs = $(".breadcrumb-item").filter(function() {
		var reg = new RegExp("<%=currPage%>", "i");
				return reg.test($(this).text());
			});
			filteredDivs.removeClass('active');
			
		<%if ("prepare".equals(currPage)) {%> 
		$("[name='btn13'").text('Submit')
		$("[name='btn12'").text('Save')
		$("[name='btn11'").text('Quit')
		<%} else if ("approve".equals(currPage)) {%> 
		$("[name='btn13'").text('Submit')
		$("[name='btn12'").text('Return for correction')
		$("[name='btn11'").text('Quit')
		<%} else if ("review".equals(currPage)) {%> 
		$("[name='btn13'").text('Submit')
		$("[name='btn12'").text('Save')
		$("[name='btn11'").text('Quit')
		<%} else if ("transfer".equals(currPage)) {%> 
		$("[name='btn13'").text('Quit')
		$("[name='btn12'").text('Validate & Transfer')
		$("[name='btn11'").text('').hide();
		<%}%> 
		
		$("[name='fileUp']").on('click', function(e) {
			$('[name="fn"]').click();
			console.log('selectedFile');
		});
		
		$("[name='fn']").change(function (e){
			$('progress').show();
			i = 0;
			var clI = setInterval(function(){
					i = i+1;
					if(i >= 100){ 
						clearInterval(clI);
						$(".prog-status").show().text('failed');
						$(".prog-status" ).wrap( "<a href='#'></a>" );
					}		
					$('progress').attr({
						value: i,
		            });
			}, 100);
  		});
	});

	function performCheckQuota() {

	}

	var showingHrsTable = false;
	function showDetailHours(event) {
		showingHrsTable == true ? (showingHrsTable = false, $("[class$='hours']").hide()) : (showingHrsTable = true, $("[class$='hours']").show()); 	
	}
	
	var showingCoaTable = false;
	function showDetailCoa(event) {
		showingCoaTable == true ? (showingCoaTable = false, $("[class$='coa']").hide()) : (showingCoaTable = true, $("[class$='coa']").show()); 	
	}
	
	function dwnldTempl(ev){
		var downloadXlsx = document.createElement("form");
        downloadXlsx.action = "/claim/download-xlsx";
        downloadXlsx.method = "GET";
        downloadXlsx.submit();
	}
</script>
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12" id="divMyRequest"
				style="padding-left: 5px; padding-right: 5px;">
				<div class="panel panel-custom-primary">
					<div class="panel-heading">
						<h3 class="panel-title">
							<i class="glyphicon glyphicon-list-alt"></i> Claim
						</h3>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-md-12">
								<ol class="breadcrumb">
									<!-- <li class="breadcrumb-item active" style="width: 115px;"><a href="#"> <i class="arrow"></i> <i class="glyphicon glyphicon-file"> </i> Prepare
											Payment
									</a></li>
									<li class="breadcrumb-item active" style="width: 155px;"><a href="#"><i class="arrow"></i> <i class="glyphicon glyphicon-new-window"> </i>
											Submit to Fin </a></li> -->
									<li class="breadcrumb-item active" style="width: 155px;"><a
										href="#"><i class="arrow"></i> <i
											class="glyphicon glyphicon-list-alt"> </i> Validate Payment</a></li>
									<li class="breadcrumb-item active" style="width: 170px;"><a
										href="#"><i class="arrow"></i> <i
											class="glyphicon glyphicon-new-window"> </i> Transfer to HCM
									</a></li>
								</ol>
							</div>
						</div>
						<!-- Check Quota Project -->
						<form id="frmCheckQuota" method="POST">
							<div class="claim panel-body">
								<div class="row">
									<div class="col-sm-2 col-darkgrey">Type:</div>
									<div class="col-sm-4">SHS</div>
									<div class="col-sm-2 ">Pay month:</div>
									<div class="col-sm-4">09/2018</div>
								</div>
								<div class="row">
									<div class="col-sm-2 col-darkgrey">Hospital /Department:</div>
									<div class="col-sm-4">PYN - Medicine & Geriatrics</div>
									<div class="col-sm-2">Project Name/ No.:</div>
									<div class="col-sm-4">SHS00001 - To enhance medical
										manpower support</div>
								</div>
								<div class="row">
									<div class="col-sm-2 col-darkgrey">Job(s):</div>
									<div class="col-sm-4">Resident, AC</div>
									<div class="col-sm-2 ">Work Location:</div>
									<div class="col-sm-4"></div>
								</div>
								<div class="row">
									<div class="col-sm-2 col-darkgrey">Project Duration:</div>
									<div class="col-sm-4 col-darkgrey">Propagated from
										Project Initiation</div>
									<div class="col-sm-2 ">Available Work Hours:</div>
									<div class="col-sm-4 ">1234</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6 text-left">
									<!-- <button type="button" class="btn btn-primary">Upload</button> -->
									<!-- <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#notifyModel">Send Notification</button>
										<button type="button" class="btn btn-success" onclick="performCheckQuota()">New Check Quota</button> -->
								</div>
								<div class="col-sm-6 text-right">
									<button type="button" class="btn btn-primary" name="btn14"
										onclick="showDetailCoa()">COA</button>
									<button type="button" class="btn btn-primary" name="btn15"
										onclick="showDetailHours()">Date</button>
								</div>
							</div>
						</form>
						<div class="claim table-responsive">
							<div class="fixed-table-container">
								<div class="fixed-table-body">
									<table id="claim-table">
										<thead>
											<tr>
												<th style="text-align: center; vertical-align: middle; width: 36px;" rowspan="2"><input name="chkSelectAll" type="checkbox"></th> 
												<th rowspan="2">Status</th>
												<th rowspan="2">Cluster</th>
												<th rowspan="2">Hosp.</th>
												<th rowspan="2">Emp. no.</th>
												<th rowspan="2">Assign No.</th>
												<th rowspan="2">Name</th>
												<th rowspan="2">Staff Rank</th>
												<th rowspan="2">Performed Job</th>
												<th rowspan="2">Work Location</th>
												<th rowspan="2">HA/CS</th>
												<th rowspan="2">Hour Type</th>
												<th rowspan="2">Earned Month</th>
												<th colspan="5" class="th-header-coa">COA</th>
												<th colspan="31" class="th-header-hours">Date / No. of
													hours worked</th>
												<th style="width: 72px;" rowspan="2">Total no. of hours
													worked</th>
											</tr>
											<tr>
												<th class="th-coa">Inst</th>
												<th class="th-coa">Fund</th>
												<th class="th-coa">Section</th>
												<th class="th-coa">Analytic</th>
												<th class="th-coa">Type</th>
												<th class="th-hours">1</th>
												<th class="th-hours">2</th>
												<th class="th-hours">3</th>
												<th class="th-hours">4</th>
												<th class="th-hours">5</th>
												<th class="th-hours">6</th>
												<th class="th-hours">7</th>
												<th class="th-hours">8</th>
												<th class="th-hours">9</th>
												<th class="th-hours">10</th>
												<th class="th-hours">11</th>
												<th class="th-hours">12</th>
												<th class="th-hours">13</th>
												<th class="th-hours">14</th>
												<th class="th-hours">15</th>
												<th class="th-hours">16</th>
												<th class="th-hours">17</th>
												<th class="th-hours">18</th>
												<th class="th-hours">19</th>
												<th class="th-hours">20</th>
												<th class="th-hours">21</th>
												<th class="th-hours">22</th>
												<th class="th-hours">23</th>
												<th class="th-hours">24</th>
												<th class="th-hours">25</th>
												<th class="th-hours">26</th>
												<th class="th-hours">27</th>
												<th class="th-hours">28</th>
												<th class="th-hours">29</th>
												<th class="th-hours">30</th>
												<th class="th-hours">31</th>
											</tr>
										</thead>
										<tbody>
											<tr data-index="0">
												<td class="td-checkbox"><input data-index="0" name="chkSelectItem" type="checkbox" value="0"></td> 
												<td>A</td>
												<td>HEC</td>
												<td>PYN</td>
												<td>EMP00001</td>
												<td>ASG00001</td>
												<td>Chan Tai Man</td>
												<td>WM</td>
												<td>AC</td>
												<td></td>
												<td>HA</td>
												<td></td>
												<td>09/2018</td>
												<td class="th-coa">101</td>
												<td class="th-coa">99</td>
												<td class="th-coa">0010001</td>
												<td class="th-coa">01010</td>
												<td class="th-coa">11</td>
												<td class="work-hours">2</td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours">2</td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours">2</td>
												<td class="work-hours"></td>
												<td class="work-hours">1</td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="total-hours-worked"></td>
											</tr>
											<tr data-index="0">
												<td class="td-checkbox"><input data-index="0" name="chkSelectItem" type="checkbox" value="0"></td> 
												<td>A</td>
												<td>HEC</td>
												<td>PYN</td>
												<td>EMP00001</td>
												<td>ASG00001</td>
												<td>Chan Tai Man</td>
												<td>WM</td>
												<td>AC</td>
												<td></td>
												<td>HA</td>
												<td></td>
												<td>09/2018</td>
												<td class="th-coa">101</td>
												<td class="th-coa">99</td>
												<td class="th-coa">0010001</td>
												<td class="th-coa">01010</td>
												<td class="th-coa">11</td>
												<td class="work-hours">2</td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours">1</td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours">5</td>
												<td class="work-hours"></td>
												<td class="work-hours">1</td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="work-hours"></td>
												<td class="total-hours-worked"></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12">
								<table class="legend pull-right">
									<thead>
										<tr>
											<th style="width: 40px; background-color: #dfdfdf;"></th>
											<th style="width: 170px;" colspan="2">For Validate
												&amp;Transfer HCM</th>
										</tr>
										<tr>
											<th style="font-weight: normal;">Total</th>
											<th style="font-weight: normal; width: 65px;">Transfered</th>
											<th style="font-weight: normal; width: 65px;">On-hold</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><div class="total">0</div></td>
											<td><div class="selected">0</div></td>
											<td><div class="onhold">0</div></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12">
								<div class="pull-right" style="padding-top: 15px;">
									<!-- <button type="button" class="btn btn-primary" name="btn16" onclick="dwnldTempl()">Download Attendant template</button>
									<input type="file" id="fn" name="fn" style="display: none;">
									<progress value=0 max=100 style="display: none; width: 66px;"></progress>
									<span class="prog-status">failed</span> -->
									<a class="btn btn-primary"
										href="<c:url value="/claim/transfer"/>" role="button">RollBack</a>
									<a class="btn btn-primary"
										href="<c:url value="/claim/transfer"/>" role="button">Transfer</a>
									<!-- <button type="button" class="btn btn-primary" >Submit</button> -->
									<!-- <button type="button" class="btn btn-primary" name="btn11">Return for Correction</button>
									<button type="button" class="btn btn-primary" name="btn12">Return for Correction</button>
									<button type="button" class="btn btn-primary" name="btn13">Approve</button> -->
									<!-- <button type="button" class="btn btn-primary" name="btn12">Return for Correction</button>
									<button type="button" class="btn btn-primary" name="btn13">Approve</button> -->
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- ./panel-body -->
			</div>
			<!-- ./panel -->
		</div>
	</div>
	<!-- ./col-md-4 -->
</div>
<!-- ./row -->
<%@ include file="/WEB-INF/views/core/commonPopups.jsp"%>
<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>