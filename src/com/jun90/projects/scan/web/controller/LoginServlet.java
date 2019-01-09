package com.jun90.projects.scan.web.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.jun90.projects.scan.web.APIUtils;
import com.jun90.projects.scan.web.DatabaseUtils;
import com.jun90.projects.scan.web.KeyUtils;
import com.jun90.projects.scan.web.ResponseCodeUtils;
import com.jun90.projects.scan.web.dao.APIDAO;
import com.jun90.projects.scan.web.dao.TokenDAO;
import com.jun90.projects.scan.web.dao.UserDAO;
import com.jun90.projects.scan.web.model.API;
import com.jun90.projects.scan.web.model.Token;
import com.jun90.projects.scan.web.model.User;

public class LoginServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7632315359548028392L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String apiKey = request.getParameter("apikey");
		long phoneNumber = Long.parseLong(request.getParameter("phonenumber"));
		int code = Integer.parseInt(request.getParameter("code"));
		DatabaseUtils dbUtils = new DatabaseUtils(getServletContext());
		Connection connection = dbUtils.getConnection(false);
		if(connection == null) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(500));
			return;
		}
		API api = null;
		try {
			api = APIDAO.select(connection, apiKey);
		} catch (SQLException | ParseException e) { }
		if(api == null) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-1002));
			return;
		}
		
		User user = null;
		try {
			user = UserDAO.select(connection, phoneNumber);
		} catch (SQLException | ParseException e) { }
		if(user == null) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-1000));
			return;
		}
		
		if(new Date().after(user.getLoginCodeExpires()) || code != user.getLoginCode()) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-1001));
			return;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, Integer.parseInt(getServletContext().getInitParameter("token_expires")));
		
		Token token = new Token();
		token.setKey(KeyUtils.getStringKey());
		token.setAPI(api);
		token.setUser(user);
		token.setExpires(calendar.getTime());
		try {
			user.setLoginCode(0);
			UserDAO.update(connection, user);
			TokenDAO.insert(connection, token);
			JsonObject jsonObject = ResponseCodeUtils.getJSON(200);
			jsonObject.addProperty("token", token.getKey());
			jsonObject.addProperty("phonenumber", token.getUser().getPhoneNumber());
			if(token.getUser().getType() == 2) jsonObject.addProperty("admin", true);
			dbUtils.closeConnection(true);
			APIUtils.writeJSON(response, jsonObject);
		} catch (SQLException e) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-500));
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-400));
	}

}
