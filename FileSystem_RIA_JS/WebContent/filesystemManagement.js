(function() { // avoid variables ending up in the global scope

	//general vars
	var currentDocumentIDShowed;
	var currentSubfolderIDShowed;
	
	//components
	var tree;
	var documentDetails;
	var pageManager = new PageManager();



	//inizialization
	window.addEventListener("load", () => {
		if (sessionStorage.getItem("username") == null) {
			window.location.href = "index.html";
		} else {
			pageManager.start();
			pageManager.refresh();
		}
	}, false);

	// Constructor of view component "User message"
	function UserMessage(_username, messagecontainer) {
		this.username = _username;
		this.show = function() {
			messagecontainer.textContent = this.username;
		}
	};

	// Constructor of the main view component "Tree"
	function Tree(_alert, _foldercontainer) {
		this.alert = _alert;
		this.foldercontainer = _foldercontainer;

		//Retrive all folders of this user
		this.showlevel1 = function() {
			var self = this;
			makeCall("GET", "GetFolders", null, false,
				function(req) {
					if (req.readyState == XMLHttpRequest.DONE /*4*/) {
						var message = req.responseText;
						if (req.status == 200) {
							var foldersToShow = JSON.parse(req.responseText);
							self.showlevel2(foldersToShow);
						} 
						else {
							self.alert.textContent = message;
						}
					}
				}
			);
		}
		
		//Retrive all subfolders of the given folders
		this.showlevel2 = function(foldersArray) {
			var self = this;
			foldersArray.forEach(function(folder) {
				makeCall("GET", "GetSubfolders?parentid=" + folder.ID, null, false,
					function(req) {
						if (req.readyState == XMLHttpRequest.DONE) {
							var message = req.responseText;
							if (req.status == 200 || req.status == 206  /*SC_PARTIAL_CONTET*/) {
								var foldercell;
								foldercell = document.createElement("div");
								foldercell.setAttribute("id", "level1 list");
								foldercell.textContent = folder.name;
								self.foldercontainer.appendChild(foldercell);
							}
							if (req.status == 200){
								var subsToShow = JSON.parse(req.responseText);
								self.showlevel3(foldercell, subsToShow);
							}
							else {
								self.alert.textContent = message;
							}
						}
						
					}
				);
			});

		}
		
		//Retrive all the documents of the given subfolders and display them
		this.showlevel3 = function(foldercell, subfoldersArray) {
			var self = this;

			subfoldersArray.forEach(function(subfolder) {
				makeCall("GET", "GetDocuments?parentid=" + subfolder.ID, null, false,
					function(req) {
						if (req.readyState == XMLHttpRequest.DONE) {
							var message = req.responseText;
							if (req.status == 200 || req.status == 206  /*SC_PARTIAL_CONTENT*/) {
								var subfoldercell, documentlist,
									documenttext, newline, subfolderlist;

								subfolderlist = document.createElement("div");
								subfolderlist.setAttribute("class", "dropzone");
								subfolderlist.setAttribute("subfolderid", subfolder.ID);
								subfolderlist.setAttribute("draggable", "true");
								subfolderlist.setAttribute("ondragstart",
									"event.dataTransfer.setData('text/plain',null)");

								subfoldercell = document.createElement("span");
								subfoldercell.textContent = subfolder.name;
								subfoldercell.setAttribute("id", "level2");

								documentlist = document.createElement("div");
								documentlist.setAttribute("class", "content");
								documentlist.setAttribute("id", "list");


								foldercell.appendChild(subfolderlist);
								subfolderlist.appendChild(subfoldercell);

								subfolderlist.appendChild(documentlist);
								
								//Listener for collapsable items
								subfoldercell.addEventListener("click", (event) => {
									var content = event.target.nextElementSibling;
									if (content.style.maxHeight) {
										content.style.maxHeight = null;
									} else {
										content.style.maxHeight = content.scrollHeight + "px";
									}
								}, false);
							}
							if (req.status == 200) {
								var docsToShow = JSON.parse(req.responseText);
								docsToShow.forEach(function(doc) {
									anchor = document.createElement("a");
									anchor.setAttribute("id", "level3");
									anchor.setAttribute("draggable", "true");
									anchor.setAttribute("ondragstart",
										"event.dataTransfer.setData('text/plain',null)");
									anchor.setAttribute('documentid', doc.ID);

									documenttext = document.createTextNode(doc.name);

									newline = document.createElement("br");

									anchor.appendChild(documenttext);
									anchor.appendChild(newline);
									documentlist.appendChild(anchor);
									
									//Listener on document to be displayed
									anchor.addEventListener("click", (e) => {
										documentDetails.show(e.target.getAttribute("documentid"), e.target.parentNode.parentNode.getAttribute("subfolderid"));
										document.getElementById("modal").innerHTML = "";
									}, false);
									anchor.href = "#";
								});
							}
							else {
								self.alert.textContent = message;
							}
						}
					}
				);
			});

		}
	
		//add drag & drop listeners
		this.registerEvents = function() {
			var dragged;
			var self = this;

			document.addEventListener("dragstart", function(event) {	//while holding an element
				dragged = event.target;
				event.target.style.opacity = 0.5;
			}, false);
			document.addEventListener("dragend", function(event) {		//no more holding
				event.target.style.opacity = "";
			}, false);
			document.addEventListener("dragover", function(event) {		
				// prevent default to allow drop
				event.preventDefault();
			}, false);

			document.addEventListener("dragenter", function(event) {	//entering a dropzone while holding an element
				//dragging a folder: only allowed to trash it
				if (dragged.classList.contains("dropzone") && event.target.classList.contains("trash_dropzone")) {
					event.target.style.border = "inset";
					dragged.style.cursor = "copy";
				}
				//dragging a document: both trash or subfolder allowed
				if ((event.target.classList.contains("dropzone") || event.target.className == "trash_dropzone")
					&& event.target != dragged.parentNode.parentNode && !dragged.classList.contains("dropzone")) {
					event.target.style.border = "inset";
					dragged.style.cursor = "copy";
				}

			}, false);

			document.addEventListener("dragleave", function(event) {	//exiting a dropzone while holding an element
				if (event.target.className == "dropzone" || event.target.className == "trash_dropzone") {
					event.target.style.border = "";
					dragged.style.cursor = "not-allowed";
				}
				if (dragged.classList.contains("dropzone") && event.target.classList.contains("trash_dropzone")) {
					event.target.style.border = "";
					dragged.style.cursor = "not-allowed";
				}
			}, false);

			document.addEventListener("drop", function(event) {			//dropping an element
				// prevent default action (open as link for some elements)
				event.preventDefault();
				//resetting dragging properties
				event.target.style.border = "";
				dragged.style.cursor = "";

				//dropping a document inside a folder
				if (event.target.className == "dropzone" && event.target != dragged.parentNode.parentNode && !dragged.classList.contains("dropzone")) {
					//remove child
					dragged.parentNode.removeChild(dragged);
					//add child
					event.target.childNodes[1].appendChild(dragged);
					//show new child
					var content = event.target.childNodes[0].nextElementSibling;
					content.style.maxHeight = content.scrollHeight + "px";
					//actual modification of the node
					self.movenode(event.target.getAttribute("subfolderid"), dragged.getAttribute("documentid"));
				}
				//dropping document or folder inside the trash can
				else if (event.target.className == "trash_dropzone") {
					var txt;
					var docID = dragged.getAttribute("documentid");
					var subID = dragged.getAttribute("subfolderid");
					//spawning a modal window
					var response = confirm("You are deleting an element. Please confirm or cancel your choice");
					if (response == true) {
						if (docID == null) {				//deleting subfolder: -1 ignored
							self.removenode(subID, -1);
							//remove element
							dragged.parentNode.removeChild(dragged);
							if (subID == currentSubfolderIDShowed){
								documentDetails.reset();	//hide details of deleted document (if in this subfolder)
							}
						}
						else if (subID == null) {			//deleting a document: -1 to be checked in the servlet
							self.removenode(-1, docID);
							//remove element
							dragged.parentNode.removeChild(dragged);
							if (docID == currentDocumentIDShowed){
								documentDetails.reset();	//hide details of deleted document
							}
						}
						txt = "Element deleted";
					}
					else {
						txt = "";
					}
					document.getElementById("modal").innerHTML = txt;
				}
			}, false);
		}

		//function to move a node: a document inside a different subfolder
		this.movenode = function(newsubfolderid, documentid) {
			var self = this;
			var params = 'subid=';
			params = params.concat(newsubfolderid.toString(), '&docid=', documentid.toString());
			// params = ' subid=?&docid=? '
			makeCall("POST", 'MoveDocument', params, false,
				function(req) {
					if (req.readyState == XMLHttpRequest.DONE) {
						var message = req.responseText;
						if (req.status == 200) {
						} else {
							self.alert.textContent = message;
							pageManager.refresh();
						}
					}
				}
			);

		}
		
		//function to remove a node, whether a subfolder or a document
		this.removenode = function(subfolderid, documentid) {
			var self = this;
			var params = 'subid=';
			params = params.concat(subfolderid.toString(), '&docid=', documentid.toString());
			makeCall("POST", 'DeleteElement', params, false,
				function(req) {
					if (req.readyState == XMLHttpRequest.DONE) {
						var message = req.responseText;
						if (req.status == 200) {
						} else {
							self.alert.textContent = message;
							pageManager.refresh();
						}
					}
				}
			);
		}
	
		this.update = function() {
			this.foldercontainer.innerHTML = ""; // empty the table body
			this.showlevel1();
		}

	};


	function DocumentDetails(options) {
		this.detailscontainer = options['detailscontainer'];
		this.name = options['name'];
		this.date = options['date'];
		this.summary = options['summary'];
		this.type = options['type'];
		this.alert = options['alertContainer']
		this.detailscontainer.style.visibility = "hidden";

		this.show = function(docID, subID) {
			var self = this;
			currentDocumentIDShowed = docID;
			currentSubfolderIDShowed = subID;
			makeCall("GET", "AccessDocument?documentid=" + docID, null, false,
				function(req) {
					if (req.readyState == 4) {
						var message = req.responseText;
						if (req.status == 200) {
							var doc = JSON.parse(req.responseText);
							self.update(doc);
							self.detailscontainer.style.visibility = "visible";
						}
						else {
							self.alert.textContent = message;
						}
					}
				}

			);
		}

		this.update = function(doc) {
			this.name.textContent = doc.name;
			this.date.textContent = doc.date;
			this.summary.textContent = doc.summary;
			this.type.textContent = doc.type;
		}
		
		this.reset = function() {
			this.detailscontainer.style.visibility = "hidden";
		}

	};

	function PageManager() {
		var alertContainer = document.getElementById("id_alert");
		this.start = function() {
			userMessage = new UserMessage(sessionStorage.getItem('username'),
				document.getElementById("id_username"));
			userMessage.show();

			tree = new Tree(alertContainer, document.getElementById("id_foldercontainer"));
			tree.registerEvents(this);

			documentDetails = new DocumentDetails({
				detailscontainer: document.getElementById("id_detailscontainer"),
				name: document.getElementById("id_documentname"),
				date: document.getElementById("id_documentdate"),
				summary: document.getElementById("id_documentsummary"),
				type: document.getElementById("id_documenttype"),
				alertContainer
			});



			document.querySelector("a[href='Logout']").addEventListener('click', () => {
				window.sessionStorage.removeItem('username');
			});

		}


		this.refresh = function() {
			alertContainer.textContent = "";
			tree.update();
		}

	};
})();