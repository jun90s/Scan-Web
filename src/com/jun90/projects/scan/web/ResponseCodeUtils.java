package com.jun90.projects.scan.web;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class ResponseCodeUtils {

	public static final Map<Integer, String> codeMap = new HashMap<Integer, String>();
	
	static {
		codeMap.put(200, "成功");
		codeMap.put(-400, "错误请求");
		codeMap.put(-401, "拒绝访问");
		codeMap.put(-500, "内部服务器错误");
		codeMap.put(-1000, "手机号不合法");
		codeMap.put(-1001, "验证码不合法");
		codeMap.put(-1002, "无效的API密钥");
		codeMap.put(-1003, "无效的Token");
		codeMap.put(-1004, "无效的项目");
		codeMap.put(-1005, "无效的任务");
		codeMap.put(-1006, "无效的图片");
		codeMap.put(-1100, "被禁止的用户");
		codeMap.put(-1200, "不支持的图片格式");
	}
	
	public static String getMessage(int code) {
		return codeMap.get(code);
	}
	
	public static JsonObject getJSON(int code) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("code", code);
		if(codeMap.containsKey(code))
			jsonObject.addProperty("msg", codeMap.get(code));
		return jsonObject;
	}
	
}
