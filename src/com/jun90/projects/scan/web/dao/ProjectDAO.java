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
import com.jun90.projects.scan.web.model.Project;

public class ProjectDAO {
	
	public static void insert (Connection connection, Project project) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO `scan`.`project` (`Id`, `User`, `Tasks`, `Thumbnail`, `Raw`, `Created`) VALUES (?, ?, ?, ?, ?, ?)");
		statement.setString(1, project.getId());
		statement.setLong(2, project.getUser().getPhoneNumber());
		statement.setString(3, project.getTasks());
		statement.setBlob(4, new ByteArrayInputStream(project.getThumbnail()));
		statement.setBlob(5, new ByteArrayInputStream(project.getRaw()));
		statement.setString(6, DatabaseUtils.getDateFormat().format(project.getCreated()));
		statement.execute();
	}
	
	public static void delete(Connection connection, Project project) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("DELETE FROM `project` WHERE `Id` = ? AND `User` = ?");
		statement.setString(1, project.getId());
		statement.setLong(2, project.getUser().getPhoneNumber());
		statement.execute();
	}
	
	public static void update(Connection connection, Project project) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("UPDATE `project` SET `Tasks` = ?, `Thumbnail` = ?, `Raw` = ?, `Created` = ? WHERE `Id` = ? AND `User` = ?");
		statement.setString(1, project.getTasks());
		statement.setBlob(2, new ByteArrayInputStream(project.getThumbnail()));
		statement.setBlob(3, new ByteArrayInputStream(project.getRaw()));
		statement.setString(4, DatabaseUtils.getDateFormat().format(project.getCreated()));
		statement.setString(5, project.getId());
		statement.setLong(6, project.getUser().getPhoneNumber());
		statement.execute();
	}
	
	public static Project select(Connection connection, String id, long phoneNumber) throws SQLException, ParseException {
		Project project = null;
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM `project` WHERE `Id` = ? AND `User` = ?");
		statement.setString(1, id);
		statement.setLong(2, phoneNumber);
		ResultSet result = statement.executeQuery();
		if(result.next()) {
			project = new Project();
			project.setId(result.getString(1));
			project.setUser(UserDAO.select(connection, result.getLong(2)));
			project.setTasks(result.getString(3));
			Blob thumbnail = result.getBlob(4);
			project.setThumbnail(thumbnail.getBytes(1, (int) thumbnail.length()));
			Blob raw = result.getBlob(5);
			project.setRaw(raw.getBytes(1, (int) raw.length()));
			project.setCreated(DatabaseUtils.getDateFormat().parse(result.getString(6)));
		} else {
			throw new SQLException("Not found");
		}
		return project;
	}
	
	public static Project[] list(Connection connection) throws SQLException, ParseException {
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM `project`");
		List<Project> projectList = new LinkedList<Project>();
		ResultSet result = statement.executeQuery();
		while(result.next()) {
			Project project = new Project();
			project.setId(result.getString(1));
			project.setUser(UserDAO.select(connection, result.getLong(2)));
			project.setTasks(result.getString(3));
			Blob thumbnail = result.getBlob(4);
			project.setThumbnail(thumbnail.getBytes(1, (int) thumbnail.length()));
			Blob raw = result.getBlob(5);
			project.setRaw(raw.getBytes(1, (int) raw.length()));
			project.setCreated(DatabaseUtils.getDateFormat().parse(result.getString(6)));
			projectList.add(project);
		}
		return projectList.toArray(new Project[projectList.size()]);
	}

}
