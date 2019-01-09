package com.jun90.projects.scan.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import com.jun90.projects.scan.web.DatabaseUtils;
import com.jun90.projects.scan.web.model.User;

public class UserDAO {
	
	public static void insert (Connection connection, User user) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO `user` (`PhoneNumber`, `Type`, `LoginCode`, `LoginCodeExpires`) VALUES (?, ?, ?, ?)");
		statement.setLong(1, user.getPhoneNumber());
		statement.setInt(2, user.getType());
		statement.setInt(3, user.getLoginCode());
		statement.setString(4, DatabaseUtils.getDateFormat().format(user.getLoginCodeExpires()));
		statement.execute();
	}
	
	public static void delete(Connection connection, User user) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("DELETE FROM `user` WHERE `PhoneNumber` = ?");
		statement.setLong(1, user.getPhoneNumber());
		statement.execute();
	}
	
	public static void update(Connection connection, User user) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("UPDATE `user` SET `Type` = ?, `LoginCode` = ?, `LoginCodeExpires` = ? WHERE `PhoneNumber` = ?");
		statement.setInt(1, user.getType());
		statement.setInt(2, user.getLoginCode());
		statement.setString(3, DatabaseUtils.getDateFormat().format(user.getLoginCodeExpires()));
		statement.setLong(4, user.getPhoneNumber());
		statement.execute();
	}
	
	public static User select(Connection connection, long phoneNumber) throws SQLException, ParseException {
		User user = null;
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM `user` WHERE `PhoneNumber` = ?");
		statement.setLong(1, phoneNumber);
		ResultSet result = statement.executeQuery();
		if(result.next()) {
			user = new User();
			user.setPhoneNumber(result.getLong(1));
			user.setType(result.getInt(2));
			user.setLoginCode(result.getInt(3));
			user.setLoginCodeExpires(DatabaseUtils.getDateFormat().parse(result.getString(4)));
		} else {
			throw new SQLException("Not found");
		}
		return user;
	}
	
	public static User[] list(Connection connection) throws SQLException, ParseException {
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM `user`");
		List<User> userList = new LinkedList<User>();
		ResultSet result = statement.executeQuery();
		while(result.next()) {
			User user = new User();
			user.setPhoneNumber(result.getLong(1));
			user.setType(result.getInt(2));
			user.setLoginCode(result.getInt(3));
			user.setLoginCodeExpires(DatabaseUtils.getDateFormat().parse(result.getString(4)));
			userList.add(user);
		}
		return userList.toArray(new User[userList.size()]);
	}

}
