package net.savantly.horus.modules.security.interceptor;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CookiesInterceptor extends HandlerInterceptorAdapter {
	
	final String sameSiteAttribute = "; SameSite=None";
	final String secureAttribute = "; Secure";

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		Collection<String> setCookieHeaders = response.getHeaders(HttpHeaders.SET_COOKIE);

		if (setCookieHeaders == null || setCookieHeaders.isEmpty() || !request.isSecure())
			return;

		setCookieHeaders.stream().filter(h -> !StringUtils.isEmpty(h)).map(header -> {
			if (header.toLowerCase().contains("samesite")) {
				return header;
			} else {
				return header.concat(sameSiteAttribute);
			}
		}).map(header -> {
			if (header.toLowerCase().contains("secure")) {
				return header;
			} else {
				return header.concat(secureAttribute);
			}
		}).forEach(finalHeader -> response.setHeader(HttpHeaders.SET_COOKIE, finalHeader));
	}
}