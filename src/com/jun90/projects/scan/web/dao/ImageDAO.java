package com.jun90.projects.scan.web.dao;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import com.jun90.projects.scan.web.DatabaseUtils;
import com.jun90.projects.scan.web.model.Image;

public class ImageDAO {
	
	public static void insert (Connection connection, Image image) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO `scan`.`image` (`Id`, `User`, `Name`, `Thumbnail`, `Raw`, `Created`) VALUES (?, ?, ?, ?, ?, ?)");
		statement.setString(1, image.getId());
		statement.setLong(2, image.getUser().getPhoneNumber());
		statement.setString(3, image.getName());
		statement.setBlob(4, new ByteArrayInputStream(image.getThumbnail()));
		statement.setBlob(5, new ByteArrayInputStream(image.getRaw()));
		statement.setString(6, DatabaseUtils.getDateFormat().format(image.getCreated()));
		statement.execute();
	}
	
	public static void delete(Connection connection, Image image) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("DELETE FROM `image` WHERE `Id` = ? AND `User` = ?");
		statement.setString(1, image.getId());
		statement.setLong(2, image.getUser().getPhoneNumber());
		statement.execute();
	}
	
	public static void update(Connection connection, Image image) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("UPDATE `image` SET `Name` = ?, `Thumbnail` = ?, `Raw` = ?, `Created` = ? WHERE `Id` = ? AND `User` = ?");
		statement.setString(1, image.getName());
		statement.setBlob(2, new ByteArrayInputStream(image.getThumbnail()));
		statement.setBlob(3, new ByteArrayInputStream(image.getRaw()));
		statement.setString(4, DatabaseUtils.getDateFormat().format(image.getCreated()));
		statement.setString(5, image.getId());
		statement.setLong(6, image.getUser().getPhoneNumber());
		statement.execute();
	}
	
	public static Image select(Connection connection, String id, long phoneNumber) throws SQLException, ParseException {
		Image image = null;
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM `image` WHERE `Id` = ? AND `User` = ?");
		statement.setString(1, id);
		statement.setLong(2, phoneNumber);
		ResultSet result = statement.executeQuery();
		if(result.next()) {
			image = new Image();
			image.setId(result.getString(1));
			image.setUser(UserDAO.select(connection, result.getLong(2)));
			image.setName(result.getString(3));
			Blob thumbnail = result.getBlob(4);
			image.setThumbnail(thumbnail.getBytes(1, (int) thumbnail.length()));
			Blob raw = result.getBlob(5);
			image.setRaw(raw.getBytes(1, (int) raw.length()));
			image.setCreated(DatabaseUtils.getDateFormat().parse(result.getString(6)));
		} else {
			throw new SQLException("Not found");
		}
		return image;
	}
	
	public static Image[] list(Connection connection) throws SQLException, ParseException {
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM `image` ORDER BY `Created` DESC");
		List<Image> imageList = new LinkedList<Image>();
		ResultSet result = statement.executeQuery();
		while(result.next()) {
			Image image = new Image();
			image.setId(result.getString(1));
			image.setUser(UserDAO.select(connection, result.getLong(2)));
			image.setName(result.getString(3));
			Blob thumbnail = result.getBlob(4);
			image.setThumbnail(thumbnail.getBytes(1, (int) thumbnail.length()));
			Blob raw = result.getBlob(5);
			image.setRaw(raw.getBytes(1, (int) raw.length()));
			image.setCreated(DatabaseUtils.getDateFormat().parse(result.getString(6)));
			imageList.add(image);
		}
		return imageList.toArray(new Image[imageList.size()]);
	}
	
	public static Image[] list(Connection connection, long phoneNumber) throws SQLException, ParseException {
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM `image` WHERE `User` = ? ORDER BY `Created` DESC");
		statement.setLong(1, phoneNumber);
		List<Image> imageList = new LinkedList<Image>();
		ResultSet result = statement.executeQuery();
		while(result.next()) {
			Image image = new Image();
			image.setId(result.getString(1));
			image.setUser(UserDAO.select(connection, result.getLong(2)));
			image.setName(result.getString(3));
			Blob thumbnail = result.getBlob(4);
			image.setThumbnail(thumbnail.getBytes(1, (int) thumbnail.length()));
			Blob raw = result.getBlob(5);
			image.setRaw(raw.getBytes(1, (int) raw.length()));
			image.setCreated(DatabaseUtils.getDateFormat().parse(result.getString(6)));
			imageList.add(image);
		}
		return imageList.toArray(new Image[imageList.size()]);
	}

}
