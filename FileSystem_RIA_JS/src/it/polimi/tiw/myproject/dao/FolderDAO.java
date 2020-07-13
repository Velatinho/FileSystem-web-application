package it.polimi.tiw.myproject.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import it.polimi.tiw.myproject.beans.Folder;

public class FolderDAO {
	private Connection connection;
	
	public FolderDAO(Connection connection) {
		this.connection = connection;
	}
	
	//find all the existing folders
	public ArrayList<Folder> findAllFolders(int userid) throws SQLException{
		ArrayList<Folder> folders = new ArrayList<Folder>();
		
		String query = "SELECT * from folder where ownerid = ? ORDER BY name";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, userid);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Folder folder = new Folder();
					folder.setID(result.getInt("id"));
					folder.setName(result.getString("name"));
					folder.setDate(result.getDate("date"));
					folder.setOwnerID(result.getInt("ownerid"));
					folders.add(folder);
				}
			}
		}
		return folders;
		
	}

}
