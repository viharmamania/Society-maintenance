package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class User {

	private String userName;

	private String email;

	private int societyId;
	
	private String name;
	
	private static HashMap<String,User> userMap = new HashMap<>();
	
	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;

	/*private static PreparedStatement readStatement = SQLiteManager
			.getPreparedStatement("SELECT * FROM " + Constants.Table.User.TABLE_NAME + " WHERE "
					+ Constants.Table.User.FieldName.USER_NAME + " = ?");
	
	private static PreparedStatement insertStatement =  SQLiteManager.getPreparedStatement(
			"INSERT INTO " + Constants.Table.User.TABLE_NAME + " VALUES (?, ?, ?, ?, ?)");
	
	
	private static PreparedStatement updateStatement = SQLiteManager.getPreparedStatement(
			"UPDATE " + Constants.Table.User.TABLE_NAME + " SET "
			+ Constants.Table.User.FieldName.FULL_NAME + " =? "
			+ Constants.Table.User.FieldName.EMAIL + " =? "
			+ Constants.Table.User.FieldName.PASSWORD + " =? "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?"
			+ " WHERE " + Constants.Table.User.FieldName.USER_NAME +" =? ");

	private static PreparedStatement deleteStatement = SQLiteManager.getPreparedStatement(""
			+ "UPDATE " + Constants.Table.User.TABLE_NAME + " SET "
			+ Constants.Table.User.FieldName.IS_DELETED + " =? "
			+ " WHERE " + Constants.Table.User.FieldName.USER_NAME +" =? ");*/

	private static String readString = "SELECT * FROM " + Constants.Table.User.TABLE_NAME + " WHERE "
					+ Constants.Table.User.FieldName.USER_NAME + " = ?";
	
	private static String insertString = "INSERT INTO " + Constants.Table.User.TABLE_NAME + " VALUES (?, ?, ?, ?, ?)";
	
	
	private static String updateString = "UPDATE " + Constants.Table.User.TABLE_NAME + " SET "
			+ Constants.Table.User.FieldName.FULL_NAME + " =? "
			+ Constants.Table.User.FieldName.EMAIL + " =? "
			+ Constants.Table.User.FieldName.PASSWORD + " =? "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?"
			+ " WHERE " + Constants.Table.User.FieldName.USER_NAME +" =? ";

	private static String deleteString =  "UPDATE " + Constants.Table.User.TABLE_NAME + " SET "
			+ Constants.Table.User.FieldName.IS_DELETED + " =? "
			+ " WHERE " + Constants.Table.User.FieldName.USER_NAME +" =? ";
	
	private User() {
		super();
	}

	public User(String userName, String email, int societyId) {
		super();
		this.userName = userName;
		this.email = email;
		this.societyId = societyId;
	}

	public User(String userName, String email) {
		super();
		this.userName = userName;
		this.email = email;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getSocietyId() {
		return societyId;
	}

	public void setSocietyId(int societyId) {
		this.societyId = societyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static User create(int societyId){
		User user = new User();
		user.setSocietyId(societyId);
		return user;
	}

	public static boolean delete(User user){
		
		boolean result = false;

		try {
			if(deleteStatement == null){
				deleteStatement = SQLiteManager.getPreparedStatement(deleteString);
			}
			deleteStatement.setInt(0, 1);
			deleteStatement.setString(1, user.getUserName());
			result = deleteStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean save(User user, String password, boolean insertEntry) {

		boolean result = false;
		try {
			if (insertEntry) {
				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement(insertString);
				}
				insertStatement.setString(0, user.getUserName());
				insertStatement.setInt(1, user.getSocietyId());
				insertStatement.setString(2, password);
				insertStatement.setString(3, user.getEmail());
				insertStatement.setString(4, user.getName());
				result = insertStatement.execute();
			} else {
				if (updateStatement == null) {
					updateStatement = SQLiteManager.getPreparedStatement(updateString);
				}
				updateStatement.setString(0, user.getName());
				updateStatement.setString(1, user.getEmail());
				updateStatement.setString(2, password);
				updateStatement.setInt(3, user.getSocietyId());
				updateStatement.setString(4, user.getUserName());
				result = updateStatement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static User read(String userName){
		
		User user = null;
		user = userMap.get(userName);
		
		if (user == null) {
			try {
				user = new User();
				if (readStatement == null) {
					readStatement = SQLiteManager.getPreparedStatement(readString);
				}
				readStatement.setString(0, userName);
				ResultSet resultset = readStatement.executeQuery();
				if (resultset != null && resultset.next()) {
					user.setUserName(resultset.getString(Constants.Table.User.FieldName.USER_NAME));
					user.setName(resultset.getString(Constants.Table.User.FieldName.FULL_NAME));
					user.setSocietyId(resultset.getInt(Constants.Table.Society.FieldName.SOCIETY_ID));
					user.setEmail(resultset.getString(Constants.Table.User.FieldName.EMAIL));
				}
				userMap.put(userName, user);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		return user;
	}
}
