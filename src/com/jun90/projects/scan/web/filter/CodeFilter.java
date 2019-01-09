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

public class CodeFilter implements Filter {
	
    /**
     * Default constructor. 
     */
    public CodeFilter() {
    	
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		
	}
	
	public static boolean filter(String code) {
		return code != null && code.length() == 6 && StringUtils.isNumeric(code) && Integer.parseInt(code) > 0;
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String code = httpRequest.getParameter("code");
		if(filter(code))
			chain.doFilter(request, response);
		else
			APIUtils.writeJSON(httpResponse, ResponseCodeUtils.getJSON(-1001));
	}
	
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {

	}
	
}
