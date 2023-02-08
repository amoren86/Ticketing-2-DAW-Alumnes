function prepareRemoveDialog(username) {
	document.getElementById("removeDialogMessage").innerHTML = removeDialogMsg.replace('{0}',username);
	document.getElementById("removeDialogHref").href = removeDialogHref + username;
}