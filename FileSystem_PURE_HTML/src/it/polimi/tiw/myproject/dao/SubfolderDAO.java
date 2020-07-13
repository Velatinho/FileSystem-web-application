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
	
	//find all the subfolders of every folder (?)
	public ArrayList<Subfolder> findAllSubfolders() throws SQLException{
		ArrayList<Subfolder> subfolders = new ArrayList<Subfolder>();
		
		String query = "SELECT * from subfolder ORDER BY name";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Subfolder subfolder = new Subfolder();
					subfolder.setID(result.getInt("id"));
					subfolder.setName(result.getString("name"));
					subfolder.setDate(result.getDate("date"));
					subfolder.setParentID(result.getInt("parentid"));
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
				}
			}
		}
		return subfolder;
	}

}
