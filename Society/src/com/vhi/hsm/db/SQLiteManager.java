package com.vhi.hsm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.vhi.hsm.utils.Constants;

public class SQLiteManager {

	private final static Logger LOG = Logger.getLogger(SQLiteManager.class);
	private static Connection connection = null;

	public static boolean setUpDB() {
		try {
			LOG.debug("Set up new connection");
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + Constants.DB_NAME);
			return true;
		} catch (Exception e) {
			LOG.error(e.toString());
			return false;
		}
	}

	public static Connection getInstance() {
		//LOG.debug("getting a connection");
		return connection;
	}

	public static void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOG.error(e.toString());
			}
		}
	}

	public static ResultSet executeQuery(String query) throws SQLException {

		ResultSet result = null;
		Statement statement = getInstance().createStatement();
		result = statement.executeQuery(query);
		return result;
	}

	public static int executeUpdate(String query) {
		int result = 0;
		Statement statement;
		try {
			statement = getInstance().createStatement();
			result = statement.executeUpdate(query);
		} catch (SQLException e) {
			LOG.error(e.toString());
		}
		return result;
	}

	public static boolean executePrepStatementAndGetResult(PreparedStatement statement) {
		boolean result;
		int count;
		if (statement != null) {
			try {
				count = statement.executeUpdate();
				result = count != 0;
			} catch (SQLException e) {
				result = false;
				LOG.error(e.toString());
			}
		} else {
			result = false;
		}
		return result;
	}

	public static PreparedStatement getPreparedStatement(String query) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = getInstance().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			LOG.error(e.toString());
		}
		return preparedStatement;
	}
	
	public synchronized static Savepoint createSavepoint(String name) {
		Savepoint savePoint = null;
		try  {
			savePoint = getInstance().setSavepoint(name);
		} catch (SQLException e) {
			LOG.error(e.toString());
		}
		return savePoint;
	}
	
	public synchronized static boolean commitSavePoint(Savepoint savePoint) {
		try {
			getInstance().releaseSavepoint(savePoint);
			return true;
		} catch (SQLException e) {
			LOG.error(e.toString());
			return false;
		}
	}
	
	public synchronized static boolean rollbackSavePoint(Savepoint savePoint) {
		try {
			getInstance().rollback(savePoint);
			return true;
		} catch (SQLException e) {
			LOG.error(e.toString());
			return false;
		}
	}
	
	public synchronized static boolean startTransaction() {
		boolean result = false;
		try {
			getInstance().setAutoCommit(false);
			result = true;
		} catch (SQLException e) {
			LOG.error(e.toString());
		}
		return result;
	}
	
	public synchronized static boolean endTransaction() {
		try {
			getInstance().setAutoCommit(true);
			return true;
		} catch (SQLException e) {
			LOG.error(e.toString());
			return false;
		}
	}

	public synchronized static boolean endTransaction(boolean commit, boolean rollback) {
		boolean result = false;

		if (commit && rollback) {
			return result;
		}

		try {
			if (commit) {
				getInstance().commit();
				result = true;
			} else if (rollback) {
				getInstance().rollback();
				result = true;
			}
		} catch (SQLException e) {
			LOG.error(e.toString());
		} finally {
			try {
				getInstance().setAutoCommit(true);
			} catch (SQLException e) {
				LOG.error(e.toString());
			}
		}
		return result;
	}

}
