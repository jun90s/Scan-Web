package com.jun90.projects.scan.web.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jun90.projects.scan.web.APIUtils;
import com.jun90.projects.scan.web.DatabaseUtils;
import com.jun90.projects.scan.web.ResponseCodeUtils;
import com.jun90.projects.scan.web.dao.ImageDAO;
import com.jun90.projects.scan.web.filter.AuthFilter;
import com.jun90.projects.scan.web.model.Image;
import com.jun90.projects.scan.web.model.Token;

public class GalleryServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1045511905392388572L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public GalleryServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String apiKey = request.getParameter("apikey");
		String tokenKey = request.getParameter("token");
		String imageId = request.getParameter("image");
		String action = request.getParameter("action");
		
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
		Image image = null;
		if(imageId != null) {
			try {
				image = ImageDAO.select(connection, imageId, token.getUser().getPhoneNumber());
			} catch (SQLException | ParseException e) { }
		}
		if(request.getMethod().equalsIgnoreCase("post") && action == null) {
			Image[] images = null;
			try {
				images = ImageDAO.list(connection, token.getUser().getPhoneNumber());
			} catch (SQLException | ParseException e) {
				images = new Image[0];
			}
			dbUtils.closeConnection(false);
			JsonObject jsonObject = ResponseCodeUtils.getJSON(200);
			JsonArray imageArray = new JsonArray();
			for(Image i : images) {
				JsonObject imageJsonObject = new JsonObject();
				imageJsonObject.addProperty("id", i.getId());
				imageJsonObject.addProperty("name", i.getName());
				imageJsonObject.addProperty("created", DatabaseUtils.getDateFormat().format(i.getCreated()));
				imageJsonObject.addProperty("image", "data:image/jpeg;base64," + Base64.encodeBase64String(i.getThumbnail()));
				imageArray.add(imageJsonObject);
			}
			jsonObject.add("images", imageArray);
			APIUtils.writeJSON(response, jsonObject);
		} else if(action.equalsIgnoreCase("get") && image != null) {
			dbUtils.closeConnection(false);
			response.setContentType("image/jpeg");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(image.getName() + ".jpg", "utf-8"));
			byte[] data = image.getRaw();
			response.getOutputStream().write(data);
		} else if(request.getMethod().equalsIgnoreCase("post") && action.equalsIgnoreCase("delete") && image != null) {
			try {
				ImageDAO.delete(connection, image);
				dbUtils.closeConnection(true);
				APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(200));
			} catch (SQLException e) {
				dbUtils.closeConnection(false);
				APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-500));
			}
		} else {
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-401));
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}

