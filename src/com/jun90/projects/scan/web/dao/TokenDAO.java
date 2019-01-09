package com.jun90.projects.scan.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import com.jun90.projects.scan.web.DatabaseUtils;
import com.jun90.projects.scan.web.model.Token;

public class TokenDAO {
	
	public static void insert (Connection connection, Token token) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO `token` (`Key`, `API`, `User`, `Expires`) VALUES (?, ?, ?, ?)");
		statement.setString(1, token.getKey());
		statement.setString(2, token.getAPI().getKey());
		statement.setLong(3, token.getUser().getPhoneNumber());
		statement.setString(4, DatabaseUtils.getDateFormat().format(token.getExpires()));
		statement.execute();
	}
	
	public static void delete(Connection connection, Token token) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("DELETE FROM `token` WHERE `Key` = ? AND `API` = ?");
		statement.setString(1, token.getKey());
		statement.setString(2, token.getAPI().getKey());
		statement.execute();
	}
	
	public static void update(Connection connection, Token token) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("UPDATE `token` SET `User` = ?, `Expires` = ? WHERE `Key` = ? AND `API` = ?");
		statement.setLong(1, token.getUser().getPhoneNumber());
		statement.setString(2, DatabaseUtils.getDateFormat().format(token.getExpires()));
		statement.setString(3, token.getKey());
		statement.setString(4, token.getAPI().getKey());
		statement.execute();
	}
	
	public static Token select(Connection connection, String key, String apiKey) throws SQLException, ParseException {
		Token token = null;
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM `token` WHERE `Key` = ? AND `API` = ?");
		statement.setString(1, key);
		statement.setString(2, apiKey);
		ResultSet result = statement.executeQuery();
		if(result.next()) {
			token = new Token();
			token.setKey(result.getString(1));
			token.setAPI(APIDAO.select(connection, result.getString(2)));
			token.setUser(UserDAO.select(connection, result.getLong(3)));
			token.setExpires(DatabaseUtils.getDateFormat().parse(result.getString(4)));
		} else {
			throw new SQLException("Not found");
		}
		return token;
	}
	
	public static Token[] list(Connection connection) throws SQLException, ParseException {
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM `token`");
		List<Token> tokenList = new LinkedList<Token>();
		ResultSet result = statement.executeQuery();
		while(result.next()) {
			Token token = new Token();
			token.setKey(result.getString(1));
			token.setAPI(APIDAO.select(connection, result.getString(2)));
			token.setUser(UserDAO.select(connection, result.getLong(3)));
			token.setExpires(DatabaseUtils.getDateFormat().parse(result.getString(4)));
			tokenList.add(token);
		}
		return tokenList.toArray(new Token[tokenList.size()]);
	}

}
