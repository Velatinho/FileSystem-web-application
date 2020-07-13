package it.polimi.tiw.myproject.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import it.polimi.tiw.myproject.beans.Folder;
import it.polimi.tiw.myproject.beans.User;
import it.polimi.tiw.myproject.dao.FolderDAO;
import it.polimi.tiw.myproject.utils.ConnectionHandler;

@WebServlet("/GetFolders")
public class GetFolders extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public GetFolders() {
		super();
	}

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		FolderDAO folderDAO = new FolderDAO(connection);
		List<Folder> folders = new ArrayList<Folder>();

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		try {
			folders = folderDAO.findAllFolders(user.getId());		
			if (folders == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println("Resources not found");
				return;
			}
			if (folders.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println("Your file system is empty!");
				return;
			}
			if(folders.get(0).getOwnerID() != user.getId()) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println("User not allowed");
				return;
			}
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not possible to recover folders");
			return;
		}

		String json = new Gson().toJson(folders);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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
