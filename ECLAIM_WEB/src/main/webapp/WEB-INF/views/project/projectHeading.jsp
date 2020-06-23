<script>
$(document).ready(function(){
	
	//Disabled All step
	$('ul.pagination li').addClass("disabled").find("a").addClass('disabled');
	//active all pass step
	switch($('#projectStep').val()){
		case "4":
			$('ul.pagination li').eq(3).removeClass("disabled").find("a").removeClass('disabled');
		case "3":
			$('ul.pagination li').eq(2).removeClass("disabled").find("a").removeClass('disabled');
		case "2":
			$('ul.pagination li').eq(1).removeClass("disabled").find("a").removeClass('disabled');
		case "1":
			$('ul.pagination li').eq(0).removeClass("disabled").find("a").removeClass('disabled');
	}
	//Find & active current step
	$('ul.pagination li').eq($('#currStep').text()-1).addClass('active').find("a").addClass('disabled');
	
	$('a.disabled').click(function(e){
	    e.preventDefault();
	})
	
	
	var status = $("#projectStatus").val();
	var recType = $("#recType").val();
	console.log(status);
	if(status == "DRAFT" || status == "OPEN"){
		if(recType == "PROJ_UPDATE"){
			$("#frmDetail").find(".department, input[name='startDate'], .removeButton").each(function(index){
				 $(this).prop('disabled', true);
			});
		}else if (recType == "PROJ_EXTEND"){
			$("#frmDetail").find(".department, .removeButton").each(function(index){
				 $(this).prop('disabled', true);
			});
		}
	}else{
		//Disabled All function, read-only
		if($('#currStep').text() == 4){
			if(status != "PENDING_FIN_VET"){
				$("#finImplTab ").find(".form-control").each(function(index){
					 $(this).prop('disabled', true);
				});
			}
		}else{
			$("#frmDetail").find(".form-control, .addButton, .removeButton, .saveButton, .custom-control-input, .chosen-container").each(function(index){
				 $(this).prop('disabled', true);
			});
			$("#frmDetail").find(".chosen-container").prop('disabled', true).trigger("chosen:updated");
			$("#frmDetail").find(".is-timeEntry").timeEntry('disable');
			
			$(".modal-dialog").find(".form-control, .saveButton").each(function(index){
				 $(this).prop('disabled', true);
			});
		}
	}
});

</script>

<style>
.header_title {
	display: inline-block;
	font-size: 24px;
	padding-left: 10px;
	padding-right: 10px;
}
.pagination>li>a{
    padding: 6px 11px;
}
.btn-lg.disabled, .btn-lg:disabled {
    cursor: not-allowed;
    box-shadow: none;
    opacity: .65;
}

</style>

<ul class="pagination" style="min-width: 984px">
				<li><a href="<c:url value="/project/newProject"> <c:param name="projectId" value="${formBean.projectId}"/> <c:param name="verId" value="${formBean.projectVerId}"/> </c:url>"><i class="fa fa-info-circle fa-2x"></i><div class="header_title">1. General</div><i class="fa fa-angle-double-right fa-2x"></i></a></li>
				<li><a href="<c:url value="/project/jobDetails"> <c:param name="projectId" value="${formBean.projectId}"/> <c:param name="verId" value="${formBean.projectVerId}"/> </c:url>"><i class="fa fa-address-card fa-2x"></i><div class="header_title">2. Job Details</div><i class="fa fa-angle-double-right fa-2x"></i></a></li>
				<li><a href="<c:url value="/project/projectCircum"> <c:param name="projectId" value="${formBean.projectId}"/> <c:param name="verId" value="${formBean.projectVerId}"/> </c:url>"><i class="fa fa-file fa-2x"></i><div class="header_title">3. Justifications</div><i class="fa fa-angle-double-right fa-2x"></i></a></li>
				<li><a href="<c:url value="/project/review"> <c:param name="projectId" value="${formBean.projectId}"/> <c:param name="verId" value="${formBean.projectVerId}"/> </c:url>"><i class="fa fa-check-circle fa-2x"></i><div class="header_title">Review and Confirm</div></a></li>
</ul>