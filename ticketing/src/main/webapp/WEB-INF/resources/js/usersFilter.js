jQuery(document).ready(function () {
    jQuery("#name").keyup(function () {
        ajaxCall();
    });

    jQuery("#role").change(function () {
        ajaxCall();
    });

});


function ajaxCall() {
    var data = {};
    data["role"] = jQuery("#role").val() == "" ? null
            : jQuery("#role option:selected").val();
    data["fullName"] = jQuery("#name").val() == "" ? null
            : jQuery("#name").val();

    jQuery.ajax({
        type: "POST",
        contentType: "application/json",
        url:  ajaxUrl + "?" + csrfParameterName + "=" + csrfToken,
        data: JSON
                .stringify(data),
        dataType: 'json',
        timeout: 600000,
        success: function (data) {
            var tbody = "";

            for (var i = 0; i < data.length; i++) {

                tbody += "<tr><td>" + (i + 1) + "</td>";
                tbody += "<td>" + translateRole(data[i].role) + "</td>";
                tbody += "<td>" + data[i].username + "</td>";
                tbody += "<td>" + data[i].fullName + "</td>";
                tbody += "<td>" + data[i].extension + "</td>";
                tbody += "<td>" + externalLocation(data[i].role, data[i].location) + "</td>";
                tbody += "<td>&nbsp;</td></tr>"; //TODO Poner los botones

            }
            jQuery("#userTable tbody").html(tbody);
        },
        error: function (e) {
        }
    });
}

function translateRole(role) {
	switch(role){
		case 'EMPLOYEE':
			return employeeMsg;
		case 'TECHNICIAN':
			return technicianMsg;
		case 'SUPERVISOR':
			return supervisorMsg;
		default:
			return '-';
	}
}

function externalLocation(role, location) {
	switch(role){
		case 'EMPLOYEE':
			return location;
		default:
			return externalMsg + location;
	}
}
