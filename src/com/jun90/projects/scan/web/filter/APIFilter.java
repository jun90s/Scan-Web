package com.jun90.projects.scan.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.jun90.projects.scan.web.APIUtils;
import com.jun90.projects.scan.web.ResponseCodeUtils;

public class APIFilter implements Filter {
	
    /**
     * Default constructor. 
     */
    public APIFilter() {
    	
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		
	}
	
	public static boolean filter(String apiKey) {
		return apiKey != null && apiKey.length() == 32 && StringUtils.isAlphanumeric(apiKey);
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String apiKey = httpRequest.getParameter("apikey");
		if(filter(apiKey))
			chain.doFilter(request, response);
		else
			APIUtils.writeJSON(httpResponse, ResponseCodeUtils.getJSON(-1002));
	}
	
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {

	}
	
}
