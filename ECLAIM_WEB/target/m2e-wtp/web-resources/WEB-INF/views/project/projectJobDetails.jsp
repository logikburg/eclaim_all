<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>

<link rel="stylesheet" href="<c:url value="/plugins/jquery/chosen/css/chosen.css" />" type="text/css" media="all" />
<script type="text/javascript" src="<%=request.getContextPath() + "/plugins/jquery/chosen/chosen.jquery.min.js"%>"></script>


<script>
	$(document).ready(function(){
		
		$('.jobDetailsRow').each(function(){
			
			var sessHour = $(this).find("input[name$='sessionHour']");
			sessHour.css('color', sessHour.val() < 4 ? 'red' : 'black');
			
			$(this).find("select[name$='jobRank']").addClass("chosen-select").chosen();
			
			var disabled = true;
			$(this).find(".int").each(function(index){
				if(!$(this).val() || isNaN(Number($(this).val()))){
					disabled = false;
				}
			});
			$(this).find("input[name$='totalHour']").prop('readonly', disabled);
			calEstFinImp($(this));
		});
		
		calRowTotal();
	});
	
	function rowHandleEvent(row){
	}
	
	function performUpdate() {
		$("#divErrorMsg").html('');
		$("#divError").hide();
		$("#divSuccessMsg").html('');
		$("#divSuccess").hide();
	}
	
// 	function isOTA(isOTA){
// 		$('#frmDetail').bootstrapValidator('enableFieldValidators','otaJustifications', isOTA);
// 		$('#frmDetail').bootstrapValidator('validateField', 'otaJustifications');
// 	}
	
	function calRowTotal(){
		calTotalHours();
		calTotalEstFinImp();
	}
	
	function getSum(total, num) {
		  return total + num;
	}
	
	function calTotalHours(){
// 		var totHours = 0;
// 		$('#frmDetail').find("input[name$=totalHour]:visible").each(function(){
// 			totHours = totHours + Number($(this).val());
// 		});
		var totHours = $.map($('#frmDetail').find("input[name$=totalHour]:visible"), function(totHour,index){
			return Number(totHour.value);
		});
		
		$("#totHour").html('<b>'+totHours.reduce(getSum)+'</b>');
	}
	
	function calTotalEstFinImp(){
		var totEstFinImp = 0;
		$('#frmDetail').find(".jobDetailsRow:visible input[name$=estFinImp]").each(function(){
			totEstFinImp = totEstFinImp + Number($(this).val());
		});
		$("#totEstImp").html(totEstFinImp);
	}
	
	function calEstFinImp($row){
		var maxVal = $row.find("input[name$='maxVal']").val();
		var finImp = 0;
		
		var totalHour = $row.find("input[name$='totalHour']").val();
		finImp = Math.round((maxVal*totalHour)/140);
		
		$row.find("input[name$='estFinImp']").val(finImp).change();
	}
	
	$(function() {
// 		$("#wrapper").toggleClass("active");
		
		$('.chosen-select').chosen();
		
		$("#frmDetail").on("change",".chosen-select",function(e){
			
			var $row = $(this).parents('.jobDetailsRow');
			
			var maxVal = $row.find("input[name$='maxVal']");
			if($(this).val() != null){
				$.getJSON("getMaxVal", {rank1: $(this).val()[0],
					rank2: $(this).val()[1],
					rank3: $(this).val()[2],
					rank4: $(this).val()[3],
					rank5: $(this).val()[4] }, function(result){
						maxVal.val(result);
						calEstFinImp($row);
				});
			}else{
				maxVal.val(1);
				calEstFinImp($row);
			}
		});
		
		$('[data-toggle="popover"]').popover({
				html : true,
			    content: function() {
			        return $('#popover_content_wrapper').html();
			      }
		});
		
		$(".estImpactBtn").click(function(e){
			if($('.estImpact').is(':visible')){
				$('.estImpact').addClass('hide');
				$(this).html("Show Esitmated Financial Impact");
			}else{
				$('.estImpact').removeClass('hide');
				$(this).html("Hide Esitmated Financial Impact");
			}
			
		});
		
		$("#popover_content_wrapper").load("../html/justificationNote.html");
		
		$("#frmDetail")
				.bootstrapValidator(
						{
							excluded : [ ':disabled', ':hidden' ],
							message : 'This value is not valid',
							live : "submitted",
							fields : {
							}
						})
				.on('success.form.bv', function(e) {
// 					 e.preventDefault();
				})
				// Add button click handler
				.on('click', '.addButton', function() {
					var i = $('tr.jobDetailsRow').size();
		            var $template = $('#templateRow'),
		                $clone    = $template
		                                .clone()
		                                .removeClass('hide')
		                                .addClass('jobDetailsRow')
		                                .removeAttr('id')
		                                .insertBefore($template);
		                $option   = $clone.find('[name="jobRank"]');
						$option.chosen();
						
						$clone.find(".form-control").each(function() {
						    $(this).attr({
						      'name': function(_, name) { return 'jobList['+i+'].'+ name},
						    });
						  });
		            	
		            	$('#frmDetail').bootstrapValidator('addField', 'jobList['+ i +'].coOrdinator', {
		    	            validators: {
		    	                notEmpty: {
		    	                    message: 'this value cant be empty'
		    	                }
		    	            }
		    	        });
		            	$('#frmDetail').bootstrapValidator('addField', 'jobList['+ i +'].description', {
		    	            validators: {
		    	                notEmpty: {
		    	                    message: 'this value cant be empty'
		    	                }
		    	            }
		    	        });
		            	$('#frmDetail').bootstrapValidator('addField', 'jobList['+ i +'].quota', {
		    	            validators: {
		    	                integer: {
			                        message: 'The value is not an integer'
			                    }
		    	            }
		    	        });
		            	$('#frmDetail').bootstrapValidator('addField', 'jobList['+ i +'].sessionDay', {
		    	            validators: {
		    	                integer: {
			                        message: 'The value is not an integer'
			                    }
		    	            }
		    	        });
		            	$('#frmDetail').bootstrapValidator('addField', 'jobList['+ i +'].sessionHour', {
		    	            validators: {
		    	            	notEmpty: {
		    	                    message: 'this value cant be empty'
		    	                },
		    	                integer: {
			                        message: 'The value is not an integer'
			                    }
		    	            }
		    	        });
		            	
		            	$('#frmDetail').bootstrapValidator('addField', 'jobList['+ i +'].totalHour', {
		    	            validators: {
		    	            	notEmpty: {
		    	                    message: 'this value cant be empty'
		    	                },
		    	                integer: {
			                        message: 'The value is not an integer'
			                    }
		    	            }
		    	        });
		//             // Add new field
		        })
		     	// Remove button click handler
		        .on('click', '.removeButton', function() {
		            var $row    = $(this).parents('.jobDetailsRow'),
		            	$status   = $row.find('[name$="status"]');
		            	
		            if($('#frmDetail').find(".jobDetailsRow:visible").length == 1){
		            	alert("At laset one Job Detail row.");
		            }else{
		            	// Remove element containing the option
			            $row.hide();
						
			            // Set Row Status as Delete
			            $status.val("DELETE");
			            
			            calRowTotal();
		            }
		        });
		
		$('#frmDetail').on("change","input[name$='quota'],input[name$='sessionDay'],input[name$='sessionHour'],input[name$='duration']", function() {
			var $row =  $(this).parents('.jobDetailsRow');
			var disabled = true;
			var totHour = 1;
			
			if($(this).prop('name').match("sessionHour")){
				$(this).css('color', $(this).val() < 4 ? 'red' : 'black');
			}
			
			$row.find(".int").each(function(index){
				if(!$(this).val() || isNaN(Number($(this).val()))){
					disabled = false;
				}else{
					totHour = totHour * Number($(this).val());
				}
			});
			
			var $totalHour = $row.find("input[name$='totalHour']");
			$totalHour.val(disabled ? totHour : $totalHour.val()).change().prop('readonly', disabled);
			
			calEstFinImp($row);
		});
		
		$("#frmDetail").on("change","select[name$='staffGrp']",function(e){
			var $row = $(this).parents('.jobDetailsRow');
			var jobRank = $row.find("select[name$='jobRank']");
			
			$.getJSON("getJobRankList", {staffGrp: $(this).val()}, function(result){
				console.log(jobRank.val());
				jobRank.empty();
				$.each(result, function(i, item){
					jobRank.append("<option value='"+item+"'>"+item+"</option>")
				});
				jobRank.trigger("chosen:updated");
				console.log(jobRank.val());
			});
			
			calEstFinImp($row);
		});
		
		$("#frmDetail").on("focus","input[name$='coOrdinator']",function(e){
		    $(this).autocomplete({
		       source : function(request, response) {
					$.getJSON("<c:url value='/project/getUserNameList'/>", request, function(result){
						console.log(request);
						response($.map(result, function(item,key){
							return {label: item, value: key}
						}));
					});
				},				
				focus: function(event, ui) {
					$(this).val(ui.item.label);
					$(this).parents('.jobDetailsRow').find("input[name$='coOrdinatorId']").val(ui.item.value);
					return false;
				},
				select: function(event, ui) {
					$(this).val(ui.item.label);
					$(this).parents('.jobDetailsRow').find("input[name$='coOrdinatorId']").val(ui.item.value);
					return false;
				}
		    });
		 });
		
		$("#frmDetail").on("keydown focusout","input[name$='coOrdinator']", function(event) {
			if (event.type != "focusout") {
				return;
			}	
			if ($(this).val() == "") {
				$(this).parents('.jobDetailsRow').find("input[name$='coOrdinatorId']").val("");
				return;
			}
			
			if ($(this).parents('.jobDetailsRow').find("input[name$='coOrdinatorId']").val() == "") {
				$(this).val("");
				return;
			}
		});
		
		$("#frmDetail").on("keydown","input[name$='coOrdinator']", function(event) {
			// If enter/tab is pressed
			if (event.which != 13 && event.which != 9  && event.which != 16) {
				$(this).parents('.jobDetailsRow').find("input[name$='coOrdinatorId']").val("");
			}
		});
		
		$('#frmDetail').on("change","input[name$=totalHour]",function(){
			calTotalHours();
			calEstFinImp($(this).parents('.jobDetailsRow'));
		});
		
		$('#frmDetail').on("change","input[name$=estFinImp]",function(){
			calTotalEstFinImp();
		});
		
		$('#jobModal').on('show.bs.modal', function (event) {
			  var button = $(event.relatedTarget) // Button that triggered the modal
			  var $row =  button.parents('.jobDetailsRow');
			  var jobRank = $row.find("select[name$='jobRank']");
			  var desc = $row.find("textarea[name$='description']");
			  var info = $row.find("input[name$='otherInfo']");
			  var app = $row.find("input[name$='targerApp']");
			 
			  var modal = $(this)
			  if(jobRank.val() != null){
				  var rankList = Array.prototype.slice.call(jobRank.val());			  
				  modal.find('#jobRank').text(' for '+ rankList.join(',') + ')');
			  }else{
				  modal.find('#jobRank').text(')');
			  }
			  modal.find('#rowIndex').text($row.prop('rowIndex'));
			  modal.find('#jobDesc').val(desc.val());
			  modal.find('#otherInfo').val(info.val());
			  modal.find('#targetApp').val(app.val());
			  
		});
		$('#jobModal').on('click', '.saveButton', function(){
			var modal = $('#jobModal');
			var rowIndex = modal.find("#rowIndex");
			$row = $('tr:eq('+ rowIndex.text() +')');
			$row.find("textarea[name$='description']").val(modal.find('#jobDesc').val());
			$row.find("input[name$='otherInfo']").val(modal.find('#otherInfo').val());
			$row.find("input[name$='targerApp']").val(modal.find('#targetApp').val());
			modal.modal('toggle');
		});
		
	});
</script>

<style type="text/css">
    .popover{
        max-width:100%;
    }
    .descButton{
    	width:"100px";
    }
    
    textarea.form-control{
        margin-bottom: 5px;
    	padding-top: 6px;
    }
    
</style>

<!-- Page Content -->
<div id="page-content-wrapper">
	<div id="currStep" style="display: none">2</div>
	<div class="container-fluid">
		<div id="divSuccess" style="display: none" class="alert-box-success">
			<div class="alert-box-icon-success">
				<i class="fa fa-check"></i>
			</div>
			<div id="divSuccessMsg" class="alert-box-content-success"></div>
		</div>
		<div id="divError" style="display: none" class="alert-box-danger">
			<div class="alert-box-icon-danger">
				<i class="fa fa-warning"></i>
			</div>
			<div id="divErrorMsg" class="alert-box-content-danger"></div>
		</div>

		<%@ include file="/WEB-INF/views/project/projectHeading.jsp"%>

		<form id="frmDetail" method="POST">
			<div class="panel panel-custom-primary">

				<div class="panel-body">
					<div class="form-group">
					
					<div class="col-sm">
						<label for="ch">Job Details</label>
<!-- 							<div class="table-responsive" style="padding: 3px"> -->
							<table id="myRequestTable"
								class="table table-striped table-bordered">
								<thead>
									<tr>
										<th style="width: 9%;"></th>
										<th style="width: 10%;">Staff Group<font class="star">*</font></th>
										<th style="width: 10%;">Job Required<font class="star">*</font></th>
										<th style="width: 20%;">Project <br>Co-ordinator<font class="star">*</font></th>
										<th style="width: 15%">Job Descriptions <br>/ Competency Requirements<font class="star">*</font></th>
										<th style="width: 5%;">Quota</th>
										<th style="width: 5%;">No. of Days</th>
										<th style="width: 5%;">Session<br>/Day</th>
										<th style="width: 5%;">Hours<br>/Session<font class="star">*</font></th>
										<th style="width: 5%;">Total<br> Hours<font class="star">*</font></th>
										<th class="hide estImpact" style="width: 5%;">Estimated<br> Financial<br> Impact</th>
									</tr>
								</thead>
								<tbody>
								<c:forEach items="${formBean.jobList}" var="job" varStatus="status">
									<tr class="jobDetailsRow">
										<td>
											<div class="btn-group">
												<button name="newRow" type="button" class="btn btn-default addButton"><i class="fa fa-plus-circle fa-2x"></i></button>
												<button name="deleteRow" type="button" class="btn btn-default removeButton" ><i class="fa fa-times-circle fa-2x"></i></button>
											</div>
										</td>
										<td class="form-group">
											<form:select path="formBean.jobList[${status.index}].staffGrp" class="form-control" data-bv-notempty="true">
<%-- 											<select name='jobList[${status.index}].staffGrp' value="${job.staffGrp}" class='form-control'> --%>
												<option value=''> - Select - </option>
												<form:options items="${staffGrpList}" /> 
<%-- 												<c:forEach items="${staffGrpList}" var="grp"> --%>
<%-- 													<option value='${grp.key}'> ${grp.value} </option> --%>
<%-- 												</c:forEach> --%>
											</form:select>
										</td>
										<td class="form-group">
												<form:select path="formBean.jobList[${status.index}].jobRank" class="form-control"  multiple = "true"  data-placeholder="Choose at least one">
<%-- 												<select name="jobList[${status.index}].jobRank" value="${job.jobRank}" class="form-control chosen-select" multiple  data-placeholder="Choose at least one"> --%>
<%-- 														<c:forEach items="${job.jobRankList}" var="jr"> --%>
<%-- 														<option value='${jr.key}'> ${jr.value} </option> --%>
<%-- 														</c:forEach> --%>
														<form:options items="${job.jobRankList}" /> 
												</form:select>
										</td>
										<td class="form-group">
											<input id="jobList[${status.index}].coOrdinator" name="jobList[${status.index}].coOrdinator" value="${job.coOrdinator}" class='form-control' type="text" data-bv-notempty="true" />
											<form:hidden path="formBean.jobList[${status.index}].coOrdinatorId" />
										</td>
										<td class="form-group"><textarea name="jobList[${status.index}].description" value="${job.description}" class='form-control' row="4" data-bv-notempty="true">${job.description}</textarea>
										<button name="descDetails" type="button" class="btn btn-primary descButton" data-toggle="modal" data-target="#jobModal">More</button></td>
										<td class="form-group"><input name="jobList[${status.index}].quota" value="${job.quota}" class='form-control int' type="text" data-bv-integer="true"/></td>
										<td class="form-group"><input name="jobList[${status.index}].duration" value="${job.duration}" class='form-control int' type="text"data-bv-integer="true" /></td>
										<td class="form-group"><input name="jobList[${status.index}].sessionDay" value="${job.sessionDay}" class='form-control int' type="text" data-bv-integer="true"/></td>
										<td class="form-group"><input name="jobList[${status.index}].sessionHour" value="${job.sessionHour}" class='form-control int' type="text" data-bv-notempty="true"  data-bv-integer="true"/></td>
										<td class="form-group"><input name="jobList[${status.index}].totalHour" value="${job.totalHour}" class='form-control' type="text" data-bv-notempty="true" data-bv-integer="true"/></td>
										<td class="hide estImpact"><input name="jobList[${status.index}].estFinImp" class="form-control" type="text" readonly></td>
										<td class="hide"><input name="jobList[${status.index}].status" value="${job.status}" class='form-control' type="text"/></td>
										<td class="hide"><input name="jobList[${status.index}].otherInfo" value="${job.otherInfo}" class='form-control' type="text"/></td>
										<td class="hide"><input name="jobList[${status.index}].targerApp" value="${job.targerApp}" class='form-control' type="text"/></td>
										<td class="hide"><input name="jobList[${status.index}].projectId" value="${job.projectId}" class='form-control' type="text"/></td>
										<td class="hide"><input name="jobList[${status.index}].projectVerId" value="${job.projectVerId}" class='form-control' type="text"/></td>
										<td class="hide"><input name="jobList[${status.index}].jobGroupId" value="${job.jobGroupId}" class='form-control' type="text"/></td>
										<td class="hide"><input name="jobList[${status.index}].maxVal" value="${job.maxGradeVal}" class='form-control' type="text"/></td>
									</tr>
								</c:forEach>
								
									<tr class="hide" id="templateRow">
										<td>
											<div class="btn-group">
												<button name="newRow" type="button" class="btn btn-default addButton"><i class="fa fa-plus-circle fa-2x"></i></button>
												<button name="deleteRow" type="button" class="btn btn-default removeButton" ><i class="fa fa-times-circle fa-2x"></i></button>
											</div>
										</td>
										
										<td class="form-group">
												<select name='staffGrp' class='form-control'/>
												<option value=''> - Select - </option>
												<c:forEach items="${staffGrpList}" var="grp">
													<option value='${grp.key}'> ${grp.value} </option>
												</c:forEach>
												</select>
										</td>
										
										<td class="form-group">
												<select name="jobRank" class="form-control" multiple data-placeholder="Choose at least one">
												</select>
					                    </td>
										
										<td class="form-group"><input name='coOrdinator' class='form-control' type="text"/><input name="coOrdinatorId" class='form-control' type="hidden"/></td>
										<td class="form-group"><textarea name='description' class='form-control' row="4"> </textarea>
										<button name="descDetails" type="button" class="btn btn-primary descButton" data-toggle="modal" data-target="#jobModal">More</button></td>
										<td class="form-group"><input name="quota" class='form-control int ' type="text"/></td>
										<td class="form-group"><input name="duration" class='form-control int' type="text"/></td>
										<td class="form-group"><input name="sessionDay" class='form-control int' type="text"/></td>
										<td class="form-group"><input name="sessionHour" class='form-control int' type="text"/></td>
										<td class="form-group"><input name="totalHour" class='form-control' type="text"/></td>
										<td class="hide estImpact"><input name="estFinImp" class="form-control" type="text" readonly></td>
										<td class="hide"><input name="status" value="NEW" class='form-control' type="text"/></td>
										<td class="hide"><input name="otherInfo" class='hide form-control' type="text"/></td>
										<td class="hide"><input name="targerApp" class='hide form-control' type="text"/></td>
										<td class="hide"><input name="maxVal" value="1" class='hide form-control' type="text"/></td>
									</tr>
									<tr id="footerRow">
										<td colspan="8"><span>Note: Extra service sessions should be arranged preferably on a stretch of 4 hours basis each, shorter sessions (i.e. 2 - <4 hrs) might be arranged under exceptional circumstances (e.g. grades facing persistent manpower shortage), subject to be reviewed by HOHR.
											</span></td>
										<td><span>Total:</span></td>
										<td><span id="totHour" style="font-size:16px;"></span></td>
										<td class="hide estImpact"><span id="totEstImp" style="font-size:16px;"></span></td>
									</tr>
								</tbody>
							</table>
<!-- 							</div> -->
					</div>
					</div>
					<div class="col-sm">
						<div class="pull-right">
							<button name="estImpactBtn" type="button" class="btn btn-primary estImpactBtn">Show Estimated Financial Impact</button>
						</div>
					</div>
					<br>
					<br>
					
					<form:hidden path="formBean.projectVerId" />
					<form:hidden id="projectStatus" path="formBean.projectStatus" />
					<form:hidden id="recType" path="formBean.recType" />
					<form:hidden id="projectStep" path="formBean.projectStep" />
					
					<%@ include file="/WEB-INF/views/project/projectFooter.jsp"%>
				</div>
			</div>
		</form>
	</div>
</div>

<div class="modal fade" id="jobModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLabel">More Job Descriptions (To be included in SHS job adv.
					<span id="jobRank"></span>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				          <span aria-hidden="true">&times;</span>
				      </button>
				</h5>
				 
			</div>
			<div class="modal-body">
				<div class="hide" id="rowIndex"></div>
					<div class="form-group">
						<label for="jobDesc">Job Descriptions / Competency Requirements<font class="star">*</font></label> 
						<textarea id="jobDesc" class='form-control' row="8" style="height: 100px;"></textarea>
					</div>
				
					<div class="form-group">
						<label for="otherInfo"> Other Information(e.g. requirements, preferred attributes, brief job descriptions, if any)</label>
						<textarea id="otherInfo" class='form-control' rows="4"></textarea>
					</div>
				
					<div class="form-group">
						<label for="targetApp"> Target Applicant:</label>
						<textarea id="targetApp" class='form-control' rows="4"></textarea>
					</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary saveButton">Save</button>
				<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
			</div>
		</div>
	</div>
</div>

<div id="popover_content_wrapper" style="display: none"></div>
<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>