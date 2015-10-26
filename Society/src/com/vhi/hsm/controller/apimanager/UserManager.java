package com.vhi.hsm.controller.apimanager;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.vhi.hsm.controller.api.UserApi;
import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.User;
import com.vhi.hsm.utils.Constants;

public class UserManager implements UserApi {

	@Override
	public User createUser() {
		User newUser = new User();
		return newUser;
	}

	@Override
	public boolean saveUser(User user) {
		return false;
	}

	@Override
	public User readUser(String userName) {
		return null;
	}

	@Override
	public boolean deleteUser(String userName) {
		return false;
	}

	/***
	 * Validate User from user name and password
	 * @param userName
	 * User name
	 * @param password
	 * Password
	 * @return
	 * If user found in data base than return valid user object else null
	 */
	public static User getUser(String userName, String password) {
		User user = null;
		ResultSet resultSet = SQLiteManager.executeQuery("SELECT * FROM " + Constants.Table.User.TABLE_NAME 
				+ " WHERE " + Constants.Table.User.FieldName.USER_NAME + " = '" + userName + "'"
				+ " AND " + Constants.Table.User.FieldName.PASSWORD + " = '" + password + "'");
		if (resultSet != null) {
			try {
				if (resultSet.next()) {
					user = new User();
					user.setUserName(resultSet.getString(Constants.Table.User.FieldName.USER_NAME));
					user.setName(resultSet.getString(Constants.Table.User.FieldName.FULL_NAME));
					user.setEmail(resultSet.getString(Constants.Table.User.FieldName.EMAIL));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}
	
}
