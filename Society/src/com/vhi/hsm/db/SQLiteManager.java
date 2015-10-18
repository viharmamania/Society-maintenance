package com.vhi.hsm.db;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.log4j.Logger;

import com.vhi.hsm.utils.Constants;

public class SQLiteManager {

	final static Logger LOG = Logger.getLogger(SQLiteManager.class);
	private static Connection connection = null;

	public SQLiteManager() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + Constants.DB_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getInstance() {
		LOG.debug("getting a new instance");
		if (connection == null) {
			new SQLiteManager();	
		}
		return connection;
	}
	
}
