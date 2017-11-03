package com.zhs.mytime.filemanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhs.mytime.filemanage.model.User;
import com.zhs.mytime.filemanage.security.JwtAuthenticationRequest;
import com.zhs.mytime.filemanage.security.JwtAuthenticationResponse;
import com.zhs.mytime.filemanage.service.AuthService;

@Controller
public class AuthController {
	
	 	@Value("${jwt.header}")
	    private String tokenHeader;

	    @Autowired
	    private AuthService authService;

	    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
	    public @ResponseBody ResponseEntity<?> createAuthenticationToken(
	            @RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException{
	        final String token = authService.login(authenticationRequest.getAccount(), authenticationRequest.getPwd());

	        // Return the token
	        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
	    }

	    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
	    public @ResponseBody ResponseEntity<?> refreshAndGetAuthenticationToken(
	            HttpServletRequest request) throws AuthenticationException{
	        String token = request.getHeader(tokenHeader);
	        String refreshedToken = authService.refresh(token);
	        if(refreshedToken == null) {
	            return ResponseEntity.badRequest().body(null);
	        } else {
	            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
	        }
	    }

	    @RequestMapping(value = "${jwt.route.authentication.register}", method = RequestMethod.POST)
	    public @ResponseBody User register(@RequestBody User addedUser) throws AuthenticationException{
	        return authService.register(addedUser);
	    }

}
