package com.zhs.mytime.filemanage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//TODO:后期单独提取到权限认证模块
@Controller
public class WxServerAPIController {

	/**
	 *  开发者服务器使用登录凭证 code 获取 session_key 和 openid。
	 *	session_key 是对用户数据进行加密签名的密钥。为了自身应用安全，session_key 不应该在网络上传输。
	 * */
    @RequestMapping(value="/onWxLogin", method = RequestMethod.GET)
    public String goUploadImg() {
        //跳转到 templates 目录下的 uploadimg.html
        return "uploadimg";
    }
}
