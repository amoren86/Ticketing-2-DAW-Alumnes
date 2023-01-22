function roleFields() {
	var role = document.getElementById("role");
	var employeeFields = document.getElementById("employeeFields");
	var technicianFields = document.getElementById("technicianFields");

	var roleValue = role.options[role.selectedIndex].value;

	if (roleValue == 'EMPLOYEE') {
		employeeFields.style.display = 'block';
		technicianFields.style.display = 'none';
	} else {
		employeeFields.style.display = 'none';
		technicianFields.style.display = 'block';
	}
}