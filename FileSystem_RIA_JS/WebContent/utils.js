/**
 * AJAX call management
 */

function makeCall(method, url, formElement, isForm, cback, reset = true) {
	var req = new XMLHttpRequest(); // visible by closure
	req.onreadystatechange = function() {
		cback(req)
	}; // closure
	req.open(method, url, true);
	if (formElement == null) {
		req.send();
	} else {
		if (isForm){
			req.send(new FormData(formElement));
		}
		else {
			req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			req.send(formElement);	//parameters
		}
	}
	if (formElement !== null && reset === true && isForm) {
		formElement.reset();
	}
}
