package it.polimi.tiw.myproject.beans;

import java.sql.Date;

public class Folder {
	
	private int ID;
	private String name;
	private Date date;
	
	public int getID() {
		return ID;
	}
	public void setID(int ID) {
		this.ID = ID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

}
