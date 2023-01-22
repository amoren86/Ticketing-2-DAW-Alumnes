function changeAllTicketId(ticketId) {
	const ticketsId = document.getElementsByName("ticketId");
	for (let i = 0; i < ticketsId.length; i++) {
		ticketsId[i].value = ticketId;
	}
}

function changeConfirmClose(confirmCloseMessage, closeHref) {
	document.getElementById("confirmCloseMessage").innerHTML = confirmCloseMessage;
	document.getElementById("closeHref").href = closeHref;
}