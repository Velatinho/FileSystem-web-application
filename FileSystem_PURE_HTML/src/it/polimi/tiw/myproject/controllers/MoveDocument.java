package it.polimi.tiw.myproject.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.myproject.dao.DocumentDAO;
import it.polimi.tiw.myproject.utils.ConnectionHandler;

@WebServlet("/MoveDocument")
public class MoveDocument extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	
	public MoveDocument() {
		super();
	}
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setSuffix(".html");
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		connection = ConnectionHandler.getConnection(getServletContext());
	}
	
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		
		String idD = request.getParameter("documentid");
		String idS = request.getParameter("newsubfolderid");
		
		if (idD != null && idS != null) {
			int subID = 0;
			int docID = 0;
			try {
				subID = Integer.parseInt(idS);
				docID = Integer.parseInt(idD);
			} catch (NumberFormatException e) {
				response.sendError(505, "Bad number format");
			}
			//need to check the paramenters .......
			DocumentDAO documentDAO = new DocumentDAO(connection);
			try {
				// Update DB
				documentDAO.moveDocument(docID, subID);		
				// Redirect to the Document list page
				String path = getServletContext().getContextPath() + "/DocumentList?subfolderid=" + subID;
				response.sendRedirect(path);
			} catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database access failed!");
			}
		} else {
			response.sendError(505, "Bad document ID");
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