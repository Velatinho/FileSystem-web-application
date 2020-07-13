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
import it.polimi.tiw.myproject.beans.Folder;
import it.polimi.tiw.myproject.beans.Subfolder;
import it.polimi.tiw.myproject.dao.DocumentDAO;
import it.polimi.tiw.myproject.dao.FolderDAO;
import it.polimi.tiw.myproject.dao.SubfolderDAO;
import it.polimi.tiw.myproject.utils.ConnectionHandler;


@WebServlet("/SelectDocument")
public class SelectDocument extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	
	public SelectDocument() {
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
			
			SubfolderDAO subfolderDAO = new SubfolderDAO(connection);
			List<Subfolder> subfolders = new ArrayList<Subfolder>();
			Subfolder starting_sub = new Subfolder();
			
			FolderDAO folderDAO = new FolderDAO(connection);
			List<Folder> folders = new ArrayList<Folder>();

			
			try {
				folders = folderDAO.findAllFolders();
				subfolders = subfolderDAO.findAllSubfolders();
				document = documentDAO.findDocumentByID(docID);
				starting_sub = subfolderDAO.findSubfolderByID(document.getParentID());
				// Redirect to the Home page  
				String path = "/WEB-INF/Home.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("folders", folders);
				ctx.setVariable("subfolders", subfolders);
				ctx.setVariable("document", document);
				ctx.setVariable("starting_sub", starting_sub);
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
