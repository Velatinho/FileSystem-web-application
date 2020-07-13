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

import it.polimi.tiw.myproject.beans.Subfolder;
import it.polimi.tiw.myproject.beans.User;
import it.polimi.tiw.myproject.dao.SubfolderDAO;
import it.polimi.tiw.myproject.utils.ConnectionHandler;

@WebServlet("/GetSubfolders")
public class GetSubfolders extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public GetSubfolders() {
		super();
	}

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SubfolderDAO subfolderDAO = new SubfolderDAO(connection);
		List<Subfolder> subfolders = new ArrayList<Subfolder>();
		
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("user");
		
		Integer folID = null;
		try {
			folID = Integer.parseInt(request.getParameter("parentid"));
		} catch (NumberFormatException | NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Incorrect param values");
			return;
		}	
		
		try {
			subfolders = subfolderDAO.findAllSubfoldersByParentID(folID);
			if (subfolders == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println("Resources not found");
				return;
			}
			if (subfolders.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
				return;
			}
			if(subfolders.get(0).getOwnerID() != user.getId()) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println("User not allowed");
				return;
			}
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not possible to recover folders");
			return;
		}

		String json = new Gson().toJson(subfolders);
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
