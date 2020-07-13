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

import org.apache.commons.lang.StringEscapeUtils;

import it.polimi.tiw.myproject.dao.UserDAO;
import it.polimi.tiw.myproject.utils.ConnectionHandler;

@WebServlet("/CheckRegistration")
@MultipartConfig
public class CheckRegistration extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public CheckRegistration() {
		super();
	}

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// obtain and escape params
		String usrn = null;
		String email = null;
		String pwd = null;
		String reppwd = null;
		usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
		email = StringEscapeUtils.escapeJava(request.getParameter("email"));
		pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
		reppwd = StringEscapeUtils.escapeJava(request.getParameter("reppwd"));
		if (usrn == null || email == null || pwd == null || reppwd == null ||
				usrn.isBlank() || email.isBlank() || pwd.isBlank() || reppwd.isBlank() ) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Credentials must be not null");
			return;
		}
		else if (!pwd.equals(reppwd)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("There is a mismatch between the two passwords");
			return;
		}
		else if (!isValid(email)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Invalid email address");
			return;
		}
		// query db to register the new user
		UserDAO userDAO = new UserDAO(connection);
		try {
			if (userDAO.checkUsernameIsOk(usrn)) {
				userDAO.addUser(usrn, pwd, email);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println("Registration successful. Please log in");
			}
			else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Username already taken ");
			}
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Internal server error, retry later");
			return;
		}
	}
	
	protected boolean isValid(String email) {
    String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    return email.matches(regex);
 }

	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}