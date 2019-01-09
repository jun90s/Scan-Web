package com.jun90.projects.scan.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import com.jun90.projects.scan.web.model.API;

public class APIDAO {
	
	public static void insert (Connection connection, API api) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO `api` (`Key`, `Description`) VALUES (?, ?)");
		statement.setString(1, api.getKey());
		statement.setString(2, api.getDescription());
		statement.execute();
	}
	
	public static void delete(Connection connection, API api) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("DELETE FROM `api` WHERE `Key` = ?");
		statement.setString(1, api.getKey());
		statement.execute();
	}
	
	public static void update(Connection connection, API api) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("UPDATE `api` SET `Description` = ? WHERE `Key` = ?");
		statement.setString(1, api.getDescription());
		statement.setString(2, api.getKey());
		statement.execute();
	}
	
	public static API select(Connection connection, String key) throws SQLException, ParseException {
		API api = null;
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM `api` WHERE `Key` = ?");
		statement.setString(1, key);
		ResultSet result = statement.executeQuery();
		if(result.next()) {
			api = new API();
			api.setKey(result.getString(1));
			api.setDescription(result.getString(2));
		} else {
			throw new SQLException("Not found");
		}
		return api;
	}
	
	public static API[] list(Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM `api`");
		List<API> apiList = new LinkedList<API>();
		ResultSet result = statement.executeQuery();
		while(result.next()) {
			API api = new API();
			api.setKey(result.getString(1));
			api.setDescription(result.getString(2));
			apiList.add(api);
		}
		return apiList.toArray(new API[apiList.size()]);
	}
	
}
