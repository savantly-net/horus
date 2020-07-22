package net.savantly.horus.modules.security.strategy;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.isis.core.security.authentication.AuthenticationSession;
import org.apache.isis.core.security.authentication.manager.AuthenticationManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.support.WebApplicationContextUtils;

import lombok.val;

public class RestSessionAuthenticationStrategy {

    public static final String HTTP_SESSION_AUTHENTICATION_SESSION_KEY = RestSessionAuthenticationStrategy.class.getPackage().getName() + ".authenticationSession";
    private AuthenticationManager authenticationManager;
    

    public AuthenticationSession lookupValid(
            final HttpServletRequest httpServletRequest, 
            final HttpServletResponse httpServletResponse) {

        val authenticationManager = getAuthenticationManager(httpServletRequest);
        val httpSession = getHttpSession(httpServletRequest);

        // use previously authenticated session if available
        val authSession = (AuthenticationSession) 
                httpSession.getAttribute(HTTP_SESSION_AUTHENTICATION_SESSION_KEY);
        if (authSession != null) {
            val sessionValid = authenticationManager.isSessionValid(authSession);
            if (sessionValid) {
                return authSession;
            }
        }

        return null;
    }
    
    protected AuthenticationManager getAuthenticationManager(ServletRequest servletRequest) {
        if(authenticationManager==null) {
            val servletContext = getServletContext(servletRequest);
            val webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            authenticationManager = webApplicationContext.getBean(AuthenticationManager.class);
        }
        return authenticationManager;
    }

    protected HttpSession getHttpSession(ServletRequest servletRequest) {
        val httpServletRequest = (HttpServletRequest) servletRequest;
        return httpServletRequest.getSession();
    }

    protected ServletContext getServletContext(ServletRequest servletRequest) {
        return servletRequest.getServletContext();
    }

    public final void invalidate(
            final HttpServletRequest httpServletRequest, 
            final HttpServletResponse httpServletResponse) {
        
        bind(httpServletRequest, httpServletResponse, null);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
    
    public void bind(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse,
            final AuthenticationSession authSession) {
        
        val httpSession = getHttpSession(httpServletRequest);
        if(authSession != null) {
            httpSession.setAttribute(
                    HTTP_SESSION_AUTHENTICATION_SESSION_KEY, authSession);
        } else {
            httpSession.removeAttribute(HTTP_SESSION_AUTHENTICATION_SESSION_KEY);
        }
    }
}
