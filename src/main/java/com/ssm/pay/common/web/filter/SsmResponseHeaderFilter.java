/*@版权所有LIJIANG*/ 
package com.ssm.pay.common.web.filter;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/** 
* 为Response设置Expires等Header的Filter.
* 该filter的配置在web.xml中
* <filter-name>expiresHeaderFilter</filter-name>
* @author Jiang.Li
* @version 2016年1月28日 下午9:44:23
*/
public class SsmResponseHeaderFilter implements Filter{
	
	private FilterConfig fc;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		this.fc = filterConfig;
		
	}
	/**
     * 设置Filter在web.xml中定义的所有参数到Response.
     */
	@SuppressWarnings("rawtypes")
	public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse)res;
		
		for(Enumeration e = fc.getInitParameterNames();e.hasMoreElements();){
			String headerName = (String) e.nextElement();
			response.addHeader(headerName, fc.getInitParameter(headerName));
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
