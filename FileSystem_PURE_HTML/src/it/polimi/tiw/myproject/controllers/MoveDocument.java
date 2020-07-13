package it.polimi.tiw.myproject.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.myproject.beans.Document;
import it.polimi.tiw.myproject.beans.Subfolder;
import it.polimi.tiw.myproject.dao.DocumentDAO;
import it.polimi.tiw.myproject.dao.SubfolderDAO;
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
		
		/*
		HttpSession session = request.getSession(false);
		if (session == null) {
			String path = getServletContext().getContextPath();
			response.sendRedirect(path);
		}*/

		
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
			DocumentDAO documentDAO = new DocumentDAO(connection);
			List<Document> documents = new ArrayList<Document>();
			SubfolderDAO subfolderDAO = new SubfolderDAO(connection);
			Subfolder subfolder = new Subfolder();
			try {
				// Update DB
				documentDAO.moveDocument(docID, subID);		
				documents = documentDAO.findAllDocumentsByParentID(subID);
				subfolder = subfolderDAO.findSubfolderByID(subID);
				// Redirect to the Document list page
				String path = "/WEB-INF/DocumentList.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("documents", documents);
				ctx.setVariable("subfolder", subfolder);
				templateEngine.process(path, ctx, response.getWriter());
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