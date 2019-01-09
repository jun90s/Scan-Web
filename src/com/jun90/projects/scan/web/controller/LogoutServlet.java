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
import com.jun90.projects.scan.web.dao.TokenDAO;
import com.jun90.projects.scan.web.model.Token;

public class LogoutServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3085894595173552380L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tokenKey = request.getParameter("token");
		String apiKey = request.getParameter("apikey");
		DatabaseUtils dbUtils = new DatabaseUtils(getServletContext());
		Connection connection = dbUtils.getConnection(false);
		if(connection == null) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-500));
			return;
		}
		try {
			Token token = TokenDAO.select(connection, tokenKey, apiKey);
			TokenDAO.delete(connection, token);
			dbUtils.closeConnection(true);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(200));
		} catch (SQLException | ParseException e) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-401));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}
