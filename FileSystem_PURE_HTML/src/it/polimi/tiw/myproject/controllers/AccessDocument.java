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
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.myproject.beans.Document;
import it.polimi.tiw.myproject.dao.DocumentDAO;
import it.polimi.tiw.myproject.utils.ConnectionHandler;

@WebServlet("/AccessDocument")
public class AccessDocument extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
       
 
    public AccessDocument() {
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
		
		String id = request.getParameter("documentid");
		if (id != null) {
			int docID = 0;
			try {
				docID = Integer.parseInt(id);
			} catch (NumberFormatException e) {
				response.sendError(505, "Bad number format");
			}
			DocumentDAO documentDAO = new DocumentDAO(connection);
			Document document = new Document();
			try {
				document = documentDAO.findDocumentByID(docID);
				
				// Redirect to the Document page 
				String path = "/WEB-INF/Document.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("document", document);
				templateEngine.process(path, ctx, response.getWriter());
			} catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database access failed");
			}
		} else {
			response.sendError(505, "Bad document ID");
		}
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
