package com.jun90.projects.scan.web.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;


import com.jun90.projects.scan.web.APIUtils;
import com.jun90.projects.scan.web.ResponseCodeUtils;
import com.jun90.projects.scan.web.dao.TokenDAO;
import com.jun90.projects.scan.web.model.Token;

public class AuthFilter {
	
	public static Token doFilter(Connection connection, HttpServletResponse response, String apiKey, String tokenKey) throws IOException {
		if(connection == null) {
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(500));
			return null;
		}
		Token token = null;
		try {
			token = TokenDAO.select(connection, tokenKey, apiKey);
		} catch (SQLException | ParseException e) { }
		if(token == null) {
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-1003));
			return null;
		}
		if(new Date().after(token.getExpires())) {
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-401));
			return null;
		}
		return token;
	}
	
}
