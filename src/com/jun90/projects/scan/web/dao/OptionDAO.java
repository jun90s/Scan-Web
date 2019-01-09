package com.jun90.projects.scan.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import com.jun90.projects.scan.web.model.Option;

public class OptionDAO {
	
	public static void insert (Connection connection, Option option) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO `option` (`Key`, `Value`) VALUES (?, ?)");
		statement.setString(1, option.getKey());
		statement.setString(2, option.getValue());
		statement.execute();
	}
	
	public static void delete(Connection connection, Option option) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("DELETE FROM `option` WHERE `Key` = ?");
		statement.setString(1, option.getKey());
		statement.execute();
	}
	
	public static void update(Connection connection, Option option) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("UPDATE `option` SET `Value` = ? WHERE `Key` = ?");
		statement.setString(1, option.getValue());
		statement.setString(2, option.getKey());
		statement.execute();
	}
	
	public static Option select(Connection connection, String key) throws SQLException, ParseException {
		Option option = null;
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM `option` WHERE `Key` = ?");
		statement.setString(1, key);
		ResultSet result = statement.executeQuery();
		if(result.next()) {
			option = new Option();
			option.setKey(result.getString(1));
			option.setValue(result.getString(2));
		} else {
			throw new SQLException("Not found");
		}
		return option;
	}
	
	public static Option[] list(Connection connection) throws SQLException, ParseException {
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM `option`");
		List<Option> optionList = new LinkedList<Option>();
		ResultSet result = statement.executeQuery();
		while(result.next()) {
			Option option = new Option();
			option.setKey(result.getString(1));
			option.setValue(result.getString(2));
			optionList.add(option);
		}
		return optionList.toArray(new Option[optionList.size()]);
	}

}
