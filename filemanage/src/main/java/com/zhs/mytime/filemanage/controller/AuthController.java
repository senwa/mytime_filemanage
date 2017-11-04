package com.zhs.mytime.filemanage.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhs.mytime.filemanage.comm.RedisUtil;
import com.zhs.mytime.filemanage.comm.ResultMessage;
import com.zhs.mytime.filemanage.comm.SMSUtil;
import com.zhs.mytime.filemanage.comm.StringUtils;
import com.zhs.mytime.filemanage.model.User;
import com.zhs.mytime.filemanage.security.JwtAuthenticationRequest;
import com.zhs.mytime.filemanage.security.JwtAuthenticationResponse;
import com.zhs.mytime.filemanage.service.AuthService;

@Controller
public class AuthController {
		private static Logger logger = LoggerFactory.getLogger(AuthController.class);
	 	@Value("${jwt.header}")
	    private String tokenHeader;
	 	
	 	@Value("${sms.smsAppId}")
	    private int smsAppId;
	 	
	 	@Value("${sms.secrectKey}")
	    private String secrectKey;
	 	
	 	@Value("${sms.templateId}")
	    private int templateId;
	 	

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
	    
	    @RequestMapping(value = "${jwt.route.authentication.sms}")
	    public @ResponseBody ResultMessage sms(@RequestParam String phone) throws AuthenticationException{
	    	ResultMessage res = new ResultMessage(ResultMessage.SUCCESS, "发送成功");
	    	try{
	    		if(StringUtils.isMobile(phone)){
		    		String random = String.valueOf((new Random().nextInt(8999) + 1000));
		    		res.setMessage(random);//直接发到客户端去验证
		    		SMSUtil.sendMsgByTemplate(smsAppId, secrectKey, phone, random, templateId);
		    		boolean redisRes = RedisUtil.setString(phone, 120, random);//保留2分钟
		    		if(!redisRes){
		    			res.setResult(ResultMessage.FAIL);
			    		res.setMessage("服务器缓存失败");
			    		logger.error("redis缓存失败");
		    		}
		    	}
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		res.setResult(ResultMessage.FAIL);
	    		res.setMessage("发送失败");
	    	}
			return res;
	    }

}
