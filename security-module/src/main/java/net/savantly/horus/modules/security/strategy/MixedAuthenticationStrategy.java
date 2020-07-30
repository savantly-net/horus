package net.savantly.horus.modules.security.strategy;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.isis.core.commons.internal.base._Bytes;
import org.apache.isis.core.commons.internal.base._Strings;
import org.apache.isis.core.security.authentication.AuthenticationRequestPassword;
import org.apache.isis.core.security.authentication.AuthenticationSession;
import org.apache.isis.viewer.restfulobjects.viewer.webmodule.auth.AuthenticationSessionStrategyDefault;
import org.springframework.web.context.support.WebApplicationContextUtils;

import lombok.val;
import net.savantly.horus.modules.security.config.HorusSecurityConfigurationProperties;

public class MixedAuthenticationStrategy extends AuthenticationSessionStrategyDefault {

	private HorusSecurityConfigurationProperties horusSecurityConfiguration;
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String BASIC_AUTH_PREFIX = "Basic ";

    private static Pattern USER_AND_PASSWORD_REGEX = Pattern.compile("^(.+):(.+)$");
    
	
	@Override
	public AuthenticationSession lookupValid(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		if (isBasicAuth(httpServletRequest)) {
			return basicAuthAuthenticate(httpServletRequest, httpServletResponse);
		} else {
			AuthenticationSession session = super.lookupValid(httpServletRequest, httpServletResponse);
			if (Objects.nonNull(session)) {
				return session;
			} else {
				return anonymousSession(httpServletRequest);
			}
		}
	}
	
	private HorusSecurityConfigurationProperties getSecurityConfiguration(ServletRequest servletRequest) {

		if(Objects.isNull(this.horusSecurityConfiguration)) {
	        val servletContext = getServletContext(servletRequest);
	        val webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	        this.horusSecurityConfiguration = webApplicationContext.getBean(HorusSecurityConfigurationProperties.class);
		}
		return this.horusSecurityConfiguration;
	}

	private AuthenticationSession anonymousSession(HttpServletRequest httpServletRequest) {
		val user = getSecurityConfiguration(httpServletRequest).getAnonymousUsername();
        val password = getSecurityConfiguration(httpServletRequest).getAnonymousPassword();

        val authenticationRequestPwd = new AuthenticationRequestPassword(user, password);
        val authenticationManager = super.getAuthenticationManager(httpServletRequest);
        val authenticationSession = authenticationManager.authenticate(authenticationRequestPwd);
        return authenticationSession;
	}

	private AuthenticationSession basicAuthAuthenticate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

        // Basic auth should never create sessions! 
        // However, telling this Shiro here, is a fragile approach.
        //TODO[2156] do this somewhere else (more coupled with shiro)
        //httpServletRequest.setAttribute(
        //        "org.apache.shiro.subject.support.DefaultSubjectContext.SESSION_CREATION_ENABLED", 
        //        Boolean.FALSE);

        
        val digest = getBasicAuthDigest(httpServletRequest);
        if (digest == null) {
            return null;
        }

        val userAndPassword = unencoded(digest);
        val matcher = USER_AND_PASSWORD_REGEX.matcher(userAndPassword);
        if (!matcher.matches()) {
            return null;
        }

        val user = matcher.group(1);
        val password = matcher.group(2);

        val authenticationRequestPwd = new AuthenticationRequestPassword(user, password);
        val authenticationManager = super.getAuthenticationManager(httpServletRequest);
        val authenticationSession = authenticationManager.authenticate(authenticationRequestPwd);
        return authenticationSession;
	}

	private boolean isBasicAuth(HttpServletRequest httpServletRequest) {
		return Objects.nonNull(getBasicAuthDigest(httpServletRequest));
	}
    
    // value should be in the form:
    // Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
    private String getBasicAuthDigest(final HttpServletRequest httpServletRequest) {
        val authStr = httpServletRequest.getHeader(HEADER_AUTHORIZATION);
        return authStr != null &&
                authStr.startsWith(BASIC_AUTH_PREFIX)
                ? authStr.substring(BASIC_AUTH_PREFIX.length())
                        : null;
    }

    protected String unencoded(final String encodedDigest) {
        return _Strings.ofBytes(
                _Bytes.decodeBase64(
                        Base64.getUrlDecoder(),
                        encodedDigest.getBytes()),
                StandardCharsets.UTF_8);
    }

}
