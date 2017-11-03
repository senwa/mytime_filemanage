package com.zhs.mytime.filemanage.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhs.mytime.filemanage.comm.WeiXinUtil;
import com.zhs.mytime.filemanage.model.User;

//TODO:后期单独提取到权限认证模块
@Controller
public class WxServerAPIController {

	@Value("${wx.appId}")
	private  String APPID;
	@Value("${wx.appSecret}")
	private  String APPSECRECT;
	/**
	 *  开发者服务器使用登录凭证 code 获取 session_key 和 openid。
	 *	session_key 是对用户数据进行加密签名的密钥。为了自身应用安全，session_key 不应该在网络上传输。
	 * */
    @RequestMapping(value = "/onWxLogin", method = RequestMethod.GET)
    public @ResponseBody Map<String,String> onWxLogin(@RequestParam String code) throws AuthenticationException{
    	Map<String,String> res = WeiXinUtil.getWXLoginInfo(APPID, APPSECRECT, code);
    	
    	return res;
    }
}
