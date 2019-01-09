package com.jun90.projects.scan.web.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jun90.projects.scan.web.APIUtils;
import com.jun90.projects.scan.web.DatabaseUtils;
import com.jun90.projects.scan.web.ResponseCodeUtils;
import com.jun90.projects.scan.web.dao.APIDAO;
import com.jun90.projects.scan.web.dao.UserDAO;
import com.jun90.projects.scan.web.model.API;
import com.jun90.projects.scan.web.model.User;

public class ReadyLoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5652462101725528994L;
	private static final Random random = new Random();

	/**
     * @see HttpServlet#HttpServlet()
     */
    public ReadyLoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String apiKey = request.getParameter("apikey");
		long phoneNumber = Long.parseLong(request.getParameter("phonenumber"));
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

		int loginCode = 1 + random.nextInt(999999);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, Integer.parseInt(getServletContext().getInitParameter("code_expires")));
		
		User user = null;
		try {
			user = UserDAO.select(connection, phoneNumber);
		} catch (SQLException | ParseException e) { }
		try {
			if(user == null) {
				user = new User();
				user.setPhoneNumber(phoneNumber);
				user.setType(1);
				user.setLoginCode(loginCode);
				user.setLoginCodeExpires(calendar.getTime());
				UserDAO.insert(connection, user);
			} else {
				if(user.getType() == 0) {
					dbUtils.closeConnection(false);
					APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-1100));
					return;
				}
				user.setLoginCode(loginCode);
				user.setLoginCodeExpires(calendar.getTime());
				UserDAO.update(connection, user);
			}
			dbUtils.closeConnection(true);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(200));
			System.out.println(user.getPhoneNumber() + "\t" + String.format("%06d", user.getLoginCode()));
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
