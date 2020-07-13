package it.polimi.tiw.myproject.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import it.polimi.tiw.myproject.beans.Document;

public class DocumentDAO {
	private Connection connection;
	
	public DocumentDAO(Connection connection) {
		this.connection = connection;
	}
	
	//move a document from a subfolder to a different one, selected by the user
	public void moveDocument(int documentID, int newsubfolderID) throws SQLException{
		String query = "UPDATE document SET parentid = ? WHERE id = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setInt(1, newsubfolderID);
			pstatement.setInt(2, documentID);
			System.out.println(pstatement);
			pstatement.executeUpdate();
		}
	}
	
	//delete a document given its id
	public void deleteDocumentByID(int documentID) throws SQLException{
		String query = "DELETE FROM document WHERE id = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setInt(1, documentID);
			pstatement.executeUpdate();
		}
	}
	
	
	//find all the documents given a certain subfolder
	public ArrayList<Document> findAllDocumentsByParentID(int parentID) throws SQLException{
		ArrayList<Document> documents = new ArrayList<Document>();
		
		String query = "SELECT * from document WHERE parentid = ? ORDER BY name";
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setInt(1, parentID);
			try (ResultSet result = pstatement.executeQuery()) {
				while (result.next()) {
					Document document = new Document();
					document.setID(result.getInt("id"));
					document.setName(result.getString("name"));
					document.setDate(result.getDate("date"));
					document.setSummary(result.getString("summary"));
					document.setType(result.getString("type"));
					document.setParentID(result.getInt("parentid"));
					document.setOwnerID(result.getInt("ownerid"));
					documents.add(document);
				}
			}
		}
		return documents;
		
	}
	
	//find a specific document given its unique ID
	public Document findDocumentByID(int docID) throws SQLException{
		Document document = null;
		
		String query = "SELECT * FROM document WHERE id = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setInt(1, docID);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					document = new Document();
					document.setID(result.getInt("id"));
					document.setName(result.getString("name"));
					document.setDate(result.getDate("date"));
					document.setSummary(result.getString("summary"));
					document.setType(result.getString("type"));
					document.setParentID(result.getInt("parentid"));
					document.setOwnerID(result.getInt("ownerid"));
				}
			}
		}
		return document;
	}

}
