package com.jun90.projects.scan.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

public class APIUtils {
	
	public static void writeJSON(HttpServletResponse response, JsonObject json) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().write(json.toString());
	}
	
}
