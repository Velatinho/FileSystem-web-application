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
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.context.WebContext;

import it.polimi.tiw.myproject.beans.Document;
import it.polimi.tiw.myproject.beans.Subfolder;
import it.polimi.tiw.myproject.dao.DocumentDAO;
import it.polimi.tiw.myproject.dao.SubfolderDAO;
import it.polimi.tiw.myproject.utils.ConnectionHandler;

@WebServlet("/DocumentList")
public class GoToDocumentList extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	
	public GoToDocumentList() {
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
	
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Integer subID = null;
		try {
			subID = Integer.parseInt(request.getParameter("subfolderid"));
		} catch (NumberFormatException | NullPointerException e) {
			response.sendError(505, "Bad number format");
			return;
		}
		DocumentDAO documentDAO = new DocumentDAO(connection);
		List<Document> documents = new ArrayList<Document>();
		SubfolderDAO subfolderDAO = new SubfolderDAO(connection);
		Subfolder subfolder = new Subfolder();
		try {
			documents = documentDAO.findAllDocumentsByParentID(subID);
			subfolder = subfolderDAO.findSubfolderByID(subID);
			if (documents == null || subfolder == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
				return;
			}
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database access failed");
			return;
		}
		// Redirect to the DocumentList page 
		String path = "/WEB-INF/DocumentList.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("documents", documents);
		ctx.setVariable("subfolder", subfolder);
		templateEngine.process(path, ctx, response.getWriter());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
