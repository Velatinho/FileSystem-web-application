package it.polimi.tiw.myproject.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import it.polimi.tiw.myproject.beans.User;

public class UserDAO {
	private Connection connection;

	public UserDAO(Connection connection) {
		this.connection = connection;
	}

	public User checkCredentials(String usrn, String pwd) throws SQLException {
		String query = "SELECT * FROM user WHERE username = ? AND password =?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, usrn);
			pstatement.setString(2, pwd);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					User user = new User();
					user.setId(result.getInt("id"));
					user.setUsername(result.getString("username"));
					user.setEmail(result.getString("email"));
					return user;
				}
			}
		}
	}
	
	public Boolean checkUsernameIsOk(String usrn) throws SQLException {
		String query = "SELECT username FROM user WHERE username = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, usrn);
			try (ResultSet result = pstatement.executeQuery()) {
				return (!result.isBeforeFirst()); // return true if no results found	
			}
		}
	}
	
	public void addUser(String usrn, String pwd, String email) throws SQLException {
		String query = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, usrn);
			pstatement.setString(2, pwd);
			pstatement.setString(3, email);
			pstatement.executeUpdate();
		}
	}
}
