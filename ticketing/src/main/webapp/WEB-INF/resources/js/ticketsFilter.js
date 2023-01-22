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

    var data = {};

    data["performer"] = jQuery("#performer").val() == "" ? null : jQuery("#performer option:selected").val();
    data["category"] = jQuery("#category").val() == "" ? null : jQuery("#category option:selected").val();
    data["from"] = jQuery("#from").val() == "" ? null :  new Date(jQuery("#from").val()).toUTCString();
    data["to"] = jQuery("#to").val() == "" ? null :  new Date(jQuery("#to").val()).toUTCString();
	data["status"] = filterStatus;

    jQuery.ajax({
        type: "POST",
        contentType: "application/json",
        url: ajaxUrl + "?" + csrfParameterName + "=" + csrfToken,
        data: JSON
                .stringify(data),
        dataType: 'json',
        timeout: 600000,
        success: function (data) {
            var tbody = "";

            for (var i = 0; i < data.length; i++) {
                tbody += "<tr><td>" + (i + 1) + "</td>";
				var resDate = manageDate(data[i].openingDate);
                tbody += "<td>" + resDate + "</td>";
                tbody += "<td>" + data[i].performer + "</td>";
                tbody += "<td>" + translateCategory(data[i].category) + "</td>";
                tbody += "<td>" + data[i].description + "</td>";
                tbody += "<td>&nbsp;</td><td>&nbsp;</td></tr>"; //TODO Poner los botones

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

