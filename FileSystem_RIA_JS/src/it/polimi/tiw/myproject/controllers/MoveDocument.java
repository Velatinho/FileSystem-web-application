package it.polimi.tiw.myproject.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.myproject.beans.Document;
import it.polimi.tiw.myproject.beans.Subfolder;
import it.polimi.tiw.myproject.beans.User;
import it.polimi.tiw.myproject.dao.DocumentDAO;
import it.polimi.tiw.myproject.dao.SubfolderDAO;
import it.polimi.tiw.myproject.utils.ConnectionHandler;

@WebServlet("/MoveDocument")
@MultipartConfig

public class MoveDocument extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	
	public MoveDocument() {
		super();
	}
	
	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}
	
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("user");

		Document document = new Document();
		Subfolder subfolder = new Subfolder();
		int subID = 0;
		int docID = 0;
		try {
			subID = Integer.parseInt(request.getParameter("subid"));
			docID = Integer.parseInt(request.getParameter("docid"));
		} catch (NumberFormatException | NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Incorrect param values");
			return;
		}

		DocumentDAO documentDAO = new DocumentDAO(connection);
		SubfolderDAO subfolderDAO = new SubfolderDAO(connection);
		try {
			// Check if user is allowed
			document = documentDAO.findDocumentByID(docID);
			subfolder = subfolderDAO.findSubfolderByID(subID);
			if (document == null || subfolder == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println("Resources not found");
				return;
			}
			if(document.getOwnerID() != user.getId() || subfolder.getOwnerID() != user.getId()) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println("User not allowed");
				return;
			}
			// Update DB
			documentDAO.moveDocument(docID, subID);		
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Database access failed!");
			return;
		}

	}
	
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}