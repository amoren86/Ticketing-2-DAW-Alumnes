jQuery(document).ready(function () {
    jQuery("#performer").change(function () {
        ajaxCall();
    });
    jQuery("#category").change(function () {
        ajaxCall();
    });
    jQuery("#from").change(function () {
        ajaxCall();
    });
    jQuery("#to").change(function () {
        ajaxCall();
    });
});
function ajaxCall() {

    var request = {};

    request["performer"] = jQuery("#performer").val() == "" ? null : jQuery("#performer option:selected").val();
    request["category"] = jQuery("#category").val() == "" ? null : jQuery("#category option:selected").val();
    request["from"] = jQuery("#from").val() == "" ? null :  new Date(jQuery("#from").val()).toUTCString();
    request["to"] = jQuery("#to").val() == "" ? null :  new Date(jQuery("#to").val()).toUTCString();
	request["status"] = filterStatus;

    jQuery.ajax({
        type: "POST",
        contentType: "application/json",
        url: ajaxUrl + "?" + csrfParameterName + "=" + csrfToken,
        data: JSON
                .stringify(request),
        dataType: 'json',
        //responseType: 'json',
        timeout: 600000,
        success: function (response) {
            var tbody = "";

            for (var i = 0; i < response.length; i++) {
            	console.log[response[i]];
                tbody += "<tr><td>" + (i + 1) + "</td>";
				var resDate = manageDate(response[i].openingDate);
                tbody += "<td>" + resDate + "</td>";
                tbody += "<td>" + response[i].performer + "</td>";
                tbody += "<td>" + translateCategory(response[i].category) + "</td>";
                tbody += "<td>" + response[i].description + "</td>";
                                
                switch(role){
				   case "SUPERVISOR":
					   switch(ticketStatus){
						   case "PENDING":
							   	tbody += `<td class='text-center'>
        							         <button type='button' class='btn btn-info btn-sm' data-toggle='modal' data-target='#assign' onclick='prepareAssignTicketDialog(${response[i].id})' data-backdrop='true'>
            						            <span class='glyphicon glyphicon-hand-right'>&nbsp;</span>
        							         </button>
    							          </td>`;
							   break;
						   case "IN_PROCESS":
							   tbody +=  `<td class='text-center'>
        							         <button type='button' class='btn btn-warning btn-sm' data-toggle='modal' data-target='#intervention' onclick='prepareInterventionTicketDialog(${response[i].id})' data-backdrop='true'>
                                                <span class='glyphicon glyphicon-wrench'>&nbsp;</span>
                                             </button>
                                          </td>`;
							   break;
						   default:
							   tbody += "<td>&nbsp;</td>";
							   break;
					   }
					   break;
				   case "TECHNICIAN":
					   if (ticketStatus === "IN_PROCESS"){
						  tbody +=  `<td class='text-center'>
        					            <button type='button' class='btn btn-warning btn-sm' data-toggle='modal' data-target='#intervention' onclick='prepareInterventionTicketDialog(${response[i].id})' data-backdrop='true'>
                                           <span class='glyphicon glyphicon-wrench'>&nbsp;</span>
                                        </button>
                                     </td>`;
					   }else{
						  tbody += "<td>&nbsp;</td>"; 
					   }
					   break;
				   default:
					   tbody += "<td>&nbsp;</td>";
					   break;
				}
				if(role === "SUPERVISOR"){
					var parsedDesc = response[i].description.replace("'","&prime;");
					tbody += `<td class='text-center'>
        				         <button type='button' class='btn btn-success btn-sm' data-toggle='modal' data-target='#confirm' onclick='prepareCloseTicketDialog("${response[i].openingDate}", "${response[i].performer}", "${response[i].category}", "${parsedDesc}", "${ticketStatus}")' data-backdrop='true'>
            					    <span class='glyphicon glyphicon-ok'>&nbsp;</span>
        						 </button>
    						  </td>`;
				}else{
					tbody += "<td>&nbsp;</td>";
				}
				
				                         
                tbody += "</tr>";

            }
            jQuery(".table tbody").html(tbody);
        },
        error: function (e) {
        }
    });
}

function manageDate(date){
   var dataAux = date.replace(" ","").substring(0,10);
   var yearAux = dataAux.substring(6,10);
   var monthAux = dataAux.substring(3,5);
   var dayAux = dataAux.substring(0,2);
                
   var hourAux = date.replace(" ","").substring(10,19);
   
   var dataObject = new Date(yearAux + "-" + monthAux + "-" + dayAux + "T" + hourAux + "Z");

   var year = dataObject.getFullYear();
   var month = dataObject.getMonth();
   var monthFormat = addZero(parseInt(month)+ 1);
   var day = addZero(dataObject.getDate());
   
   var h = addZero(dataObject.getHours());
   var m = addZero(dataObject.getMinutes());
   var s = addZero(dataObject.getSeconds());
   var time = h + ":" + m + ":" + s;
   
   var res = day + "/" + monthFormat + "/" + year + " " + time;
   return res;
}

function addZero(i) {
  if (i < 10) {i = "0" + i}
  return "" + i;
}

function translateCategory(category) {
	switch(category){
	case 'HARDWARE':
		return hardwareMsg;
    case 'SOFTWARE':
    	return softwareMsg;
    case 'PRINTER':
    	return printerMsg;
    case 'NETWORK':
    	return networkMsg;
    case 'SUPPORT':
    	return supportMsg;
    case 'OTHERS':
    	return othersMsg;
	default:
		return '-';
	}
}

