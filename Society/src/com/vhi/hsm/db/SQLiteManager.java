package com.vhi.hsm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.vhi.hsm.utils.Constants;

public class SQLiteManager {

	final static Logger LOG = Logger.getLogger(SQLiteManager.class);
	private static Connection connection = null;

	public static boolean setUpDB() {
		try {
			LOG.debug("Set up new connection");
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + Constants.DB_NAME);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Connection getInstance() {
		LOG.debug("getting a connction");
		return connection;
	}

	public static void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
