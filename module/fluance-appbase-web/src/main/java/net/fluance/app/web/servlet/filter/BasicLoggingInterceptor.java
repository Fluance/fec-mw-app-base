package net.fluance.app.web.servlet.filter;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.app.data.model.identity.User;
import net.fluance.commons.json.JsonDateSerializer;

public class BasicLoggingInterceptor implements HandlerInterceptor {

	private final class PreHandleLogContent {
		@JsonSerialize(using = JsonDateSerializer.class)
		private Date date;
		private String clientIp;
		private String requestUrl;
		private String user;
		private String authorization;
		private String queryString;

		/**
		 * @param date
		 * @param clientIp
		 * @param requestUrl
		 * @param user
		 * @param authorization
		 * @param queryString
		 */
		public PreHandleLogContent(Date date, String clientIp, String requestUrl, String user, String authorization,
				String queryString) {
			super();
			this.date = date;
			this.clientIp = clientIp;
			this.requestUrl = requestUrl;
			this.user = user;
			this.authorization = authorization;
			this.queryString = queryString;
		}
		public Date getDate() {
			return date;
		}
		public String getClientIp() {
			return clientIp;
		}
		public String getRequestUrl() {
			return requestUrl;
		}
		public String getUser() {
			return user;
		}
		public String getAuthorization() {
			return authorization;
		}
		public String getQueryString() {
			return queryString;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			try {
				return "[PreHandle]Request: " + new ObjectMapper().writeValueAsString(this);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return "PreHandleLogContent [date=" + date.toString() + ", clientIp=" + clientIp + ", requestUrl=" + requestUrl
					+ ", user=" + user + ", authorization=" + authorization + ", queryString=" + queryString + "]";
		}

	}

	private final class PostHandleLogContent {
		@JsonSerialize(using = JsonDateSerializer.class)
		private Date date;
		private String clientIp;
		private String requestUrl;
		private String user;
		private String authorization;
		private String queryString;

		/**
		 * @param date
		 * @param clientIp
		 * @param requestUrl
		 * @param user
		 * @param authorization
		 * @param queryString
		 */
		public PostHandleLogContent(long startTime, String clientIp, String requestUrl, String user, String authorization,
				String queryString) {
			super();
			this.date = new Date(startTime);
			this.clientIp = clientIp;
			this.requestUrl = requestUrl;
			this.user = user;
			this.authorization = authorization;
			this.queryString = queryString;
		}
		public Date getDate() {
			return date;
		}
		public String getClientIp() {
			return clientIp;
		}
		public String getRequestUrl() {
			return requestUrl;
		}
		public String getUser() {
			return user;
		}
		public String getAuthorization() {
			return authorization;
		}
		public String getQueryString() {
			return queryString;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			try {
				return "[PostHandle]Request: " + new ObjectMapper().writeValueAsString(this);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return "PostHandle [date=" + date.toString() + ", clientIp=" + clientIp + ", requestUrl=" + requestUrl
					+ ", user=" + user + ", authorization=" + authorization + ", queryString=" + queryString + "]";
		}

	}
	
	private final class AfterCompleteLogContent {
		private long time;
		private String clientIp;
		private String requestUrl;
		private String user;
		private String authorization;
		private String queryString;

		/**
		 * @param date
		 * @param clientIp
		 * @param requestUrl
		 * @param user
		 * @param authorization
		 * @param queryString
		 */
		public AfterCompleteLogContent(String clientIp, String requestUrl, String user, String authorization,
				String queryString, long time) {
			super();
			this.time = time;
			this.clientIp = clientIp;
			this.requestUrl = requestUrl;
			this.user = user;
			this.authorization = authorization;
			this.queryString = queryString;
		}
		public long getTime() {
			return time;
		}
		public String getClientIp() {
			return clientIp;
		}
		public String getRequestUrl() {
			return requestUrl;
		}
		public String getUser() {
			return user;
		}
		public String getAuthorization() {
			return authorization;
		}
		public String getQueryString() {
			return queryString;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			try {
				return "[AfterComplete]Request: " + new ObjectMapper().writeValueAsString(this);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return "AfterComplete [date=" + time + ", clientIp=" + clientIp + ", requestUrl=" + requestUrl
					+ ", user=" + user + ", authorization=" + authorization + ", queryString=" + queryString + "]";
		}

	}
	
	private static Logger LOGGER = LogManager.getLogger(BasicLoggingInterceptor.class);
	public static final String START_TIME_ATTRIBUTE = "startTime";

	@Value("${authorization.header}")
	private String authorizationHeader;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try{
			String authorization = request.getHeader("Authorization");
			String requestUrl = request.getRequestURL().toString();
			User user = (User) request.getAttribute(User.USER_KEY);
			if ( user != null){
				String userFQN = user.getDomain() + "/" + user.getUsername();
				String queryString = request.getQueryString();
				String ip = request.getRemoteAddr();
				Date startDate = new Date(System.currentTimeMillis());
				
				PreHandleLogContent logContent = new PreHandleLogContent(startDate, ip, requestUrl, userFQN, authorization,
						queryString);
				LOGGER.info(logContent.toString());
				request.setAttribute(START_TIME_ATTRIBUTE, startDate.getTime());
			} else {
				LOGGER.warn("No USER found");
			}
		} catch (Exception e){
			LOGGER.error("Exception when Logging");
			LOGGER.error("", e);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
		try {
			String authorization = request.getHeader("Authorization");
			String requestUrl = request.getRequestURL().toString();
			User user = (User) request.getAttribute(User.USER_KEY);
			if (user != null) {
				String userFQN = user.getDomain() + "/" + user.getUsername();
				String queryString = request.getQueryString();
				String ip = request.getRemoteAddr();
				long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);

				PostHandleLogContent logContent = new PostHandleLogContent(startTime, ip, requestUrl, userFQN, authorization, queryString);
				LOGGER.info(logContent.toString());
			} else {
				LOGGER.warn("No USER found");
			}
		} catch (Exception e) {
			LOGGER.error("Exception when Logging");
			LOGGER.error("", e);
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
		try{
			String authorization = request.getHeader("Authorization");
			String requestUrl = request.getRequestURL().toString();
			User user = (User) request.getAttribute(User.USER_KEY);
			if(user != null){
				String userFQN = user.getDomain() + "/" + user.getUsername();
				String queryString = request.getQueryString();
				String ip = request.getRemoteAddr();
				long startTime = (Long)request.getAttribute(START_TIME_ATTRIBUTE);
				
				AfterCompleteLogContent logContent = new AfterCompleteLogContent(ip, requestUrl, userFQN, authorization,
						queryString, System.currentTimeMillis() - startTime);
				LOGGER.info(logContent.toString());
			} else {
				LOGGER.warn("No USER found");
			}
		} catch (Exception e){
			LOGGER.error("Exception when Logging");
			LOGGER.error("", e);
		}

	}

}
