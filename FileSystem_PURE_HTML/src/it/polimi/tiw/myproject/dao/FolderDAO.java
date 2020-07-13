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
	public ArrayList<Folder> findAllFolders() throws SQLException{
		ArrayList<Folder> folders = new ArrayList<Folder>();
		
		String query = "SELECT * from folder ORDER BY id";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Folder folder = new Folder();
					folder.setID(result.getInt("id"));
					folder.setName(result.getString("name"));
					folder.setDate(result.getDate("date"));
					folders.add(folder);
				}
			}
		}
		return folders;
		
	}

}
