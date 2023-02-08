            jQuery(document).ready(function () {
                jQuery("#name").keyup(function () {
                    ajaxCall();
                });

                jQuery("#role").change(function () {
                    ajaxCall();
                });

            });


            function ajaxCall() {
                var request = {};
                request["role"] = jQuery("#role").val() == "" ? null
                        : jQuery("#role option:selected").val();
                request["fullName"] = jQuery("#name").val() == "" ? null
                        : jQuery("#name").val();

                jQuery.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: ajaxUrl + "?" + csrfParameterName + "=" + csrfToken,
                    data: JSON
                            .stringify(request),
                    dataType: 'json',
                    timeout: 600000,
                    success: function (response) {
                        var tbody = "";

                        for (var i = 0; i < response.length; i++) {

                            tbody += "<tr><td>" + (i + 1) + "</td>";
                            tbody += "<td>" + translateRole(response[i].role) + "</td>";
                            tbody += "<td>" + response[i].username + "</td>";
                            tbody += "<td>" + response[i].fullName + "</td>";
                            tbody += "<td>" + response[i].extension + "</td>";
                            tbody += "<td>" + externalLocation(response[i].role, response[i].location) + "</td>";
                            tbody += "<td class='text-center'>";
                            if(role === "SUPERVISOR"){
                               tbody += "<button type='button' class='btn btn-danger btn-sm' data-toggle='modal' data-target='#remove' onclick='prepareRemoveDialog(\"" + response[i].username + "\")' data-backdrop='true'><span class='glyphicon glyphicon-trash'> </span></button>";
							}
							tbody += "</td></tr>";
                        }
                        jQuery("#userTable tbody").html(tbody);
                    },
                    error: function (e) {
                    }
                });
            }

            function translateRole(role) {
                switch (role) {
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
                switch (role) {
                    case 'EMPLOYEE':
                        return location;
                    default:
                        return externalMsg + location;
                }
            }
