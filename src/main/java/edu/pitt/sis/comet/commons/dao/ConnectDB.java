/**
 * 
 */
package edu.pitt.sis.comet.commons.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * Apr 23, 2017 6:23:53 PM
 *
 * @author Chirayu Kong Wongchokprasitti, PhD (chw20@pitt.edu)
 *
 */
public class ConnectDB {

	private Connection conn;

	private String serverName = "127.0.0.1";

	private String database = "colloquia";

	private String username = "recommender";

	private String password = "system";

	private int port = 3306;

	public ConnectDB(String serverName, String database, String username,
			String password, int port) {
		this.serverName = serverName;
		this.database = database;
		this.username = username;
		this.password = password;
		this.port = port;
		establishConnection();
	}

	public ConnectDB() {
		establishConnection();
	}

	private void establishConnection() {
		String url = "jdbc:mysql://" + serverName + "" + ":" + port + "/"
				+ database + "?user=" + username + "&" + "password=" + password;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url);
		} catch (Exception ex) {
			// handle any errors
			System.out.println("Load Driver Exception: " + ex.toString());
			ex.printStackTrace();
		}
	}

	public ResultSet getResultSet(String sql) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			if (stmt.execute(sql)) {
				rs = stmt.getResultSet();
			}
		} catch (SQLException ex) {
			// handle any errors
			System.out.println(sql);
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return rs;
	}

	public boolean executeUpdate(String sql) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			// handle any errors
			System.out.println(sql);
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		}
		return true;
	}

	public int executeInsert(String sql) {
		Statement stmt = null;
		int autoinckey = -1;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
			if (rs.next()) {
				autoinckey = rs.getInt(1);
			}
			rs.close();
			rs = null;
		} catch (SQLException ex) {
			// handle any errors
			System.out.println(sql);
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return autoinckey;
	}
}
