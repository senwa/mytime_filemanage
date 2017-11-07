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
	    public @ResponseBody ResultMessage createAuthenticationToken(
	            @RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException{
	    	ResultMessage res = new ResultMessage(ResultMessage.SUCCESS, "登录成功");
	    	String token=null;
	    	try{
	    		  token = authService.login(authenticationRequest.getAccount(), authenticationRequest.getPwd());
	    		  res.setExtData(token);
	    		  res.setMessage("登录成功!");
	    	}catch(Exception e){//授权失败,账号密码错误等信息
	    		e.printStackTrace();
	    		logger.error(e.getMessage());
	    		res.setResult(ResultMessage.FAIL);
	    		res.setMessage("登录失败!");
	    		res.setCause(e.getMessage());
	    	}
	        return res;//返回带上token
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
	    public @ResponseBody ResultMessage register(@RequestBody User addedUser) throws AuthenticationException{
	    	ResultMessage res = new ResultMessage(ResultMessage.SUCCESS, "注册成功");
	    	User user = authService.register(addedUser);
	    	String token = authService.login(addedUser.getAccount(),addedUser.getPwd());
	    	if(user!=null){
	    		 res.setMessage(token);//直接返回token存到客户端
	    		 res.setExtData(user);
	    		 return res;
	    	}else{
	    		res.setResult(ResultMessage.FAIL);
	    		res.setMessage("用户名已存在,请换个用户名或直接使用手机号");
	    		return res;
	    	}
	    }
	    
	    @RequestMapping(value = "${jwt.route.authentication.sms}")
	    public @ResponseBody ResultMessage sms(@RequestParam String phone) throws AuthenticationException{
	    	ResultMessage res = new ResultMessage(ResultMessage.SUCCESS, "发送成功");
	    	try{
	    		if(StringUtils.isMobile(phone)){
		    		String random = String.valueOf((new Random().nextInt(8999) + 1000));
		    		//res.setMessage(random);//直接发到客户端去验证
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
	    		res.setMessage("发送失败:"+e.getMessage());
	    	}
			return res;
	    }

}
