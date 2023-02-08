function changeTicketId(ticketId) {
	ticketsIdInput = document.getElementById("ticketId");
	ticketsIdInput.value = ticketId;
}

function prepareAssignTicketDialog(ticketId) {
	changeTicketId(ticketId);
}

function prepareInterventionTicketDialog(ticketId) {
	changeTicketId(ticketId);
}

function prepareCloseTicketDialog(openingDate, performer, category, description, ticketStatus) {
	document.getElementById("closeDialogMessage").innerHTML = closeDialogMsg.replace('{0}', openingDate)
		.replace('{1}', performer).replace('{2}', category).replace('{3}', description);
	document.getElementById("closeDialogHref").href = closeDialogHref;
}