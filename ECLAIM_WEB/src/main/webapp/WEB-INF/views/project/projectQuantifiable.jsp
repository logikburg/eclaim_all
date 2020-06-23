<%@ include file="/WEB-INF/views/core/commonHeader.jsp"%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.js"></script>
<script>
	
	function performUpdate() {
		$("#divErrorMsg").html('');
		$("#divError").hide();
		$("#divSuccessMsg").html('');
		$("#divSuccess").hide();
	}
	
	$(function() {
		
		$('[data-toggle="popover"]').popover({
			html : true,
		    content: function() {
		        return $('#popover_content_wrapper').html();
		      }
		});
		
		$("#popover_content_wrapper").load("../html/quantifiableNote.html");
		
		$('.form_datetime').datetimepicker({
			format: "hh:ii",
			startView: 1,  
			minView: 0,
			maxView: 1,
			autoclose: true,
			minuteStep: 15
		});
		
		$('#check7x24').change(function(e){
			console.log('1');
			$("tr.scheduleRow button, tr.scheduleRow input, tr.scheduleRow select").prop("disabled", this.checked);
// 			$("tr.scheduleRow input, tr.scheduleRow select").prop("readonly", this.checked);
		});
		
		$("#frmDetail").on("change","input[type='radio']",function(e){
			$('#frmDetail').bootstrapValidator('enableFieldValidators','other', $(this).val() == "OTH" ? true : false);
			$('#frmDetail').bootstrapValidator('validateField', 'other');
		});
		
// 		$("#wrapper").toggleClass("active");
		
		$("#frmDetail")
				.bootstrapValidator(
						{
							excluded : [ ':disabled', ':hidden' ],
							message : 'This value is not valid',
							live : "submitted",
							fields : {
								startDate: {
					                validators: {
					                	notEmpty : {
											message : 'The Start Date is required and cannot be empty'
										},
					                    date: {
					                        format: 'DD/MM/YYYY',
					                        message: 'The start date is not a valid date'
					                    },
					                    callback: {
					                        message: 'The start date cant after than end date',
					                        callback: function(value, validator) {
					                            var endDate = $('#endDate');
					                            if(endDate.val() === ''){
					                            	return true;
					                            }
					                            
					                            var m = new moment(value, 'DD/MM/YYYY', true);
					                            var e = new moment(endDate.val(), 'DD/MM/YYYY', true);
					                            return m.isBefore(e);
					                        }
					                    }
					                }
					            },
								other : {
									enabled : false,
									message : 'The other is not valid',
									validators : {
										notEmpty : {
											message : 'The other is required and cannot be empty'
										},
										stringLength : {
											max : 4000,
											message : 'The other must less than 4000 characters long'
										}
									}
								}
							}
						})
				.on('success.form.bv', function(e) {
// 					 e.preventDefault();
				})
				// Add button click handler
				.on('click', '.addButton', function() {
					var i = $('tr.scheduleRow').size();
		            var $template = $('#templateRow'),
		                $clone    = $template
		                                .clone()
		                                .removeClass('hide')
		                                .addClass('scheduleRow')
		                                .removeAttr('id')
		                                .insertBefore($template);
						
						$clone.find(".form-control").each(function() {
						    $(this).attr({
						      'name': function(_, name) { return 'scheduleList['+i+'].'+ name},
						    });
						  });
						
						$clone.find(".form_datetime").each(function() {
						    $(this).datetimepicker({
								format: "hh:ii",
								startView: 1,  
								minView: 0,
								maxView: 1,
								autoclose: true,
								minuteStep: 15
							});
						});
						
		            	$('#frmDetail').bootstrapValidator('addField', 'scheduleList['+ i +'].patternCode', {
		    	            validators: {
		    	                notEmpty: {
		    	                    message: 'this value cant be empty'
		    	                }
		    	            }
		    	        });
		            	$('#frmDetail').bootstrapValidator('addField', 'scheduleList['+ i +'].startTime', {
		    	            validators: {
		    	                notEmpty: {
		    	                    message: 'this value cant be empty'
		    	                }
		    	            }
		    	        });
		            	$('#frmDetail').bootstrapValidator('addField', 'scheduleList['+ i +'].endTime', {
		    	            validators: {
		    	                notEmpty: {
		    	                    message: 'this value cant be empty'
		    	                }
		    	            }
		    	        });
		        })
		     	// Remove button click handler
		        .on('click', '.removeButton', function() {
		            var $row    = $(this).parents('.scheduleRow'),
		            	$status   = $clone.find('[name$="status"]');
		            	
		            // Remove element containing the option
		            $row.hide();

		            // Set Row Status as Delete
		            $status.val("DELETE");
		        });
	});
</script>

<style type="text/css">
    .popover{
        max-width:100%;
    }
     .date{ 
     	width:130px;  
     }
</style>

<!-- Page Content -->
<div id="page-content-wrapper">
	<div id="currStep" style="display: none">5</div>
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
				
					<div class="col-sm">
						<label>Quantifiable Deliverables <font class="star">*</font>
								<a href="#" id="justificationPop" data-toggle="popover" data-html="true" data-trigger="focus">
								<i class="fa fa-info-circle fa-2x"></i></a>
						</label>
					</div>
<!-- 					<label>Quantifiable Deliverables <font class="star">*</font></label> -->
<!-- 					<div class="panel panel-default" style="width:600px;"> -->
<!--   						<div class="panel-body"> -->
<!-- 							<div class="col-sm"> -->
<!-- 									<table id="myRequestTable" class="table table-striped table-bordered"> -->
<!-- 										<thead> -->
<!-- 											<tr> -->
<!-- 												<th style="width: 30%;">SHS Parameters</th> -->
<!-- 												<th style="width: 40%;">Quantifiable Deliverables</th> -->
<!-- 												<th style="width: 30%;">Example</th> -->
<!-- 											</tr> -->
<!-- 										</thead> -->
<!-- 										<tbody> -->
<%-- 										<c:forEach items="${formBean.detailsList}" var="vo" varStatus="status"> --%>
<!-- 											<tr class="scheduleRow"> -->
<!-- 												<td> -->
<%-- 													<div><c:out value="${vo.shsParm}"></c:out></div> --%>
<!-- 												</td> -->
<!-- 												<td class="form-group"> -->
<%-- 													<textarea name="detailsList[${status.index}].qDeliver" rows="6" type="text" --%>
<%-- 														required="required" value="${vo.qDeliver}" class="form-control"></textarea> --%>
<!-- 													<span>Maximum 4000 characters</span> -->
<!-- 												</td> -->
<%-- 												<td><div><c:out value="${vo.example}"></c:out></div></td> --%>
<%-- 												<td class="hide"><input name="detailsList[${status.index}].projectCircumUid" value="${vo.projectCircumUid}" class='form-control' type="text"/></td> --%>
<%-- 												<td class="hide"><input name="detailsList[${status.index}].circumstanceId" value="${vo.circumstanceId}" class='form-control' type="text"/></td> --%>
<!-- 											</tr> -->
<%-- 										</c:forEach> --%>
<!-- 										</tbody> -->
<!-- 									</table> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
					<br>
					<div class="col-sm">		
						<div class="form-group">
<!-- 								<label for="manpower">Manpower situation (strength and vacancies by rank) of the department unit <font class="star">**</font> (for manpower situation only)</label> -->
								<form:textarea path="formBean.qDeliver" rows="6" type="text" 
									id="manpower" class="form-control"></form:textarea>
						</div>
					</div>
					
					<br>
					<form:hidden id="projectStep" path="formBean.projectStep" />
					
					<div class="col-sm-12">
						<div class="pull-right">
							<button name="saveBtn" type="submit" class="btn-lg btn-primary"
								onclick="performUpdate()">Next</button>
							<button name="cancelBtn" type="button" class="btn-lg btn-primary">Cancel</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

<div id="popover_content_wrapper" style="display: none"></div>
<%@ include file="/WEB-INF/views/core/commonFooter.jsp"%>