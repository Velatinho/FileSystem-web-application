package it.polimi.tiw.myproject.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import it.polimi.tiw.myproject.beans.Subfolder;


public class SubfolderDAO {
	private Connection connection;
	
	public SubfolderDAO(Connection connection) {
		this.connection = connection;
	}
	
	//delete a subfolder given its id (and all its child document CASCADE)
	public void deleteSubfolder(int subID) throws SQLException{
		String query = "DELETE FROM subfolder WHERE id = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setInt(1, subID);
			pstatement.executeUpdate();
		}
	}
		
	//find subfolders of a given folder
	public ArrayList<Subfolder> findAllSubfoldersByParentID(int folID) throws SQLException{
		ArrayList<Subfolder> subfolders = new ArrayList<Subfolder>();
		
		String query = "SELECT * from subfolder WHERE parentid = ? ORDER BY name";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, folID);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Subfolder subfolder = new Subfolder();
					subfolder.setID(result.getInt("id"));
					subfolder.setName(result.getString("name"));
					subfolder.setDate(result.getDate("date"));
					subfolder.setParentID(result.getInt("parentid"));
					subfolder.setOwnerID(result.getInt("ownerid"));
					subfolders.add(subfolder);
				}
			}
		}
		return subfolders;
		
	}
	
	//find a specific subfolder given its unique ID
	public Subfolder findSubfolderByID(int subID) throws SQLException{
		Subfolder subfolder = null;
		
		String query = "SELECT * FROM subfolder WHERE id = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, subID);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					subfolder = new Subfolder();
					subfolder.setID(result.getInt("id"));
					subfolder.setName(result.getString("name"));
					subfolder.setDate(result.getDate("date"));
					subfolder.setParentID(result.getInt("parentid"));
					subfolder.setOwnerID(result.getInt("ownerid"));
				}
			}
		}
		return subfolder;
	}

}
