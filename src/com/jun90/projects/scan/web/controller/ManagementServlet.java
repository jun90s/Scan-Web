package com.jun90.projects.scan.web.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jun90.projects.scan.web.APIUtils;
import com.jun90.projects.scan.web.DatabaseUtils;
import com.jun90.projects.scan.web.ResponseCodeUtils;
import com.jun90.projects.scan.web.dao.UserDAO;
import com.jun90.projects.scan.web.filter.AuthFilter;
import com.jun90.projects.scan.web.model.Token;
import com.jun90.projects.scan.web.model.User;

public class ManagementServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2253095455437105027L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public ManagementServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tokenKey = request.getParameter("token");
		String apiKey = request.getParameter("apikey");
		String action = request.getParameter("action");
		long phoneNumber = Long.parseLong(request.getParameter("phonenumber"));
		DatabaseUtils dbUtils = new DatabaseUtils(getServletContext());
		Connection connection = dbUtils.getConnection(false);
		if(connection == null) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(500));
			return;
		}
		Token token = AuthFilter.doFilter(connection, response, apiKey, tokenKey);
		if(token == null) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-401));
			return;
		}
		if(token.getUser().getType() != 2) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-401));
			return;
		}
		User user = null;
		try {
			user = UserDAO.select(connection, phoneNumber);
		} catch (SQLException | ParseException e) { }
		if(user == null) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-1003));
			return;
		}
		if(action == null) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-400));
		} else if(action.equalsIgnoreCase("ban")) {
			user.setType(0);
			try {
				UserDAO.update(connection, user);
				dbUtils.closeConnection(true);
				APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(200));
			} catch (SQLException e) {
				dbUtils.closeConnection(false);
				APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-500));
			}
		} else if(action.equalsIgnoreCase("unban")) {
			user.setType(1);
			try {
				UserDAO.update(connection, user);
				dbUtils.closeConnection(true);
				APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(200));
			} catch (SQLException e) {
				dbUtils.closeConnection(false);
				APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-500));
			}
		} else {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-400));
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-400));
	}
	
}