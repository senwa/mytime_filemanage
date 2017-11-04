package com.zhs.mytime.filemanage.comm;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.qcloudsms.*;
public class SMSUtil {
	private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
	public static void sendMsg(int smsAppId, String secrectKey,String phone,String info){
		
		try {
	        SmsSingleSender sender = new  SmsSingleSender(smsAppId,secrectKey);
			SmsSingleSenderResult result = sender.send(0, "86", phone, info, "", "");
			System.out.print(result);
		 } catch (Exception e) {
			 logger.error(e.getMessage());
			e.printStackTrace();
		 }
	}
	
	public static String sendMsgByTemplate(int smsAppId, String secrectKey,String phone,String info,int templateId){
		
		//假设短信模板 id 为 123，模板内容为：测试短信，{1}，{2}，{3}，上学。
		 SmsSingleSender sender = null;
		try {
			 sender = new SmsSingleSender(smsAppId,secrectKey);
			 ArrayList<String> params = new ArrayList<String>();
			 params.add(info);
			 SmsSingleSenderResult   result = sender.sendWithParam("86", phone, templateId, params, "", "", "");
			 System.out.println(result);
			 logger.warn(result.toString());
			 return result.errMsg;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
		
	}
}
