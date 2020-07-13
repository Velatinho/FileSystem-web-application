/**
 * Login management function
 */

(function() { // avoid variables ending up in the global scope

	document.getElementById("loginbutton").addEventListener('click', (e) => {
		var form = e.target.closest("form");
		if (form.checkValidity()) {
			makeCall("POST", 'CheckLogin', e.target.closest("form"), true,
				function(req) {
					if (req.readyState == XMLHttpRequest.DONE) {
						var message = req.responseText;
						switch (req.status) {
							case 200:
								sessionStorage.setItem('username', message);
								window.location.href = "Home.html";
								break;
							case 400: // bad request
								document.getElementById("errormessage").textContent = message;
								break;
							case 401: // unauthorized
								document.getElementById("errormessage").textContent = message;
								break;
							case 500: // server error
								document.getElementById("errormessage").textContent = message;
								break;
						}
					}
				}
			);
		} else {
			form.reportValidity();
		}
	});

	document.getElementById("signupbutton").addEventListener('click', (e) => {
		var form = e.target.closest("form");
		var pwd = document.getElementById("checkpwd").value;
		var reppwd = document.getElementById("checkreppwd").value;
		if (pwd != reppwd){
			document.getElementById("errormessage2").textContent = "Passwords must match";
			return;
		}
		if (form.checkValidity()) {
			makeCall("POST", 'CheckRegistration', e.target.closest("form"), true,
				function(req) {
					if (req.readyState == XMLHttpRequest.DONE) {
						var message = req.responseText;
						switch (req.status) {
							case 200:	// ok: proceed with login
								document.getElementById("errormessage2").textContent = message;
								break;
							case 400: // bad request
								document.getElementById("errormessage2").textContent = message;
								break;
							case 401: // unauthorized
								document.getElementById("errormessage2").textContent = message;
								break;
							case 500: // server error
								document.getElementById("errormessage2").textContent = message;
								break;
						}
					}
				}
			);
		} else {
			form.reportValidity();
		}
	});

})();