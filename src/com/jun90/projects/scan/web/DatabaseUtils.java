package com.jun90.projects.scan.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;

public class DatabaseUtils {
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static DateFormat getDateFormat() {
		return DATE_FORMAT;
	}

	private ServletContext context;
	private Connection connection;
	
	public DatabaseUtils(ServletContext context) {
		this.context = context;
	}
	
	private synchronized boolean isClosed() {
		if(connection != null) {
			try {
				return connection.isClosed();
			} catch (SQLException e) { }
		}
		return true;
	}
	
	public synchronized Connection getConnection(boolean autoCommit) {
		if(isClosed()) {
			try {
				Class.forName(context.getInitParameter("db_driver"));
				connection = DriverManager.getConnection(context.getInitParameter("db_url"),
						context.getInitParameter("db_username"), context.getInitParameter("db_password"));
				if(connection.getAutoCommit() != autoCommit) connection.setAutoCommit(autoCommit);
			} catch (ClassNotFoundException | SQLException e) { }
		} else {
			
		}
		return connection;
	}
	
	public synchronized void closeConnection(boolean commit) {
		if(connection != null) {
			try {
				if(!connection.getAutoCommit()) {
					if(commit)
						connection.commit();
					else
						connection.rollback();
				}
			} catch (SQLException e) { }
			try {
				connection.close();
			} catch (SQLException e) { }
			connection = null;
		}
	}

}
