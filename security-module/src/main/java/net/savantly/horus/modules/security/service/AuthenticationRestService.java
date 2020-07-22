package net.savantly.horus.modules.security.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.isis.core.security.authentication.AuthenticationRequestPassword;
import org.apache.isis.core.security.authentication.AuthenticationSession;
import org.apache.isis.core.security.authentication.manager.AuthenticationManager;
import org.apache.isis.viewer.restfulobjects.viewer.webmodule.auth.AuthenticationSessionStrategyDefault;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@RestController
@RequestMapping("/security")
public class AuthenticationRestService extends AuthenticationSessionStrategyDefault {

	@PostMapping
	@RequestMapping("/signin")
	public ResponseEntity<Map<String, Object>> signIn(@RequestBody final SignInRequest request, 
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		Subject currentUser = SecurityUtils.getSubject();
		
		if ( !currentUser.isAuthenticated() ) {
		    UsernamePasswordToken token = new UsernamePasswordToken(request.getUsername(), request.getPassword());
		    token.setRememberMe(true);
		    try {
		    	AuthenticationManager authManager = getAuthenticationManager(httpServletRequest);
		    	// 
		    	AuthenticationSession authSession = authManager.authenticate(new AuthenticationRequestPassword(request.getUsername(), request.getPassword()));
				bind(httpServletRequest, httpServletResponse, authSession);
				
		    	//currentUser.login(token);
		    	response.put("message", "signed in");
		    	return ResponseEntity.ok(response);
		    } catch (Exception ex) {
		    	response.put("error", ex.getLocalizedMessage());
		    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		    }
		} else {
			response.put("message", "already signed in");
	    	return ResponseEntity.ok(response);
		}
	}


	static class SignInRequest {
		@Getter @Setter @NonNull String username;
		@Getter @Setter @NonNull String password;
	}
}
