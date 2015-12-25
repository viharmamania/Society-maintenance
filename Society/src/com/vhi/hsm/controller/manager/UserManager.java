package com.vhi.hsm.controller.manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.User;
import com.vhi.hsm.utils.Constants;

public class UserManager {

	public static User getUser(String userName) {

		User user = null;

		PreparedStatement statement = SQLiteManager.getPreparedStatement("SELECT * FROM "
				+ Constants.Table.User.TABLE_NAME + " WHERE " + Constants.Table.User.FieldName.USER_NAME + " = ? ");
		if (statement != null) {
			try {
				statement.clearParameters();
				statement.setString(1, userName);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet != null && resultSet.next()) {
					user = User.read(userName);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return user;
		
	}
	
}
