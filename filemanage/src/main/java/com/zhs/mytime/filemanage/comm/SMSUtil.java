package com.zhs.mytime.filemanage.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.github.qcloudsms.*;
public class SMSUtil {
	
	public static Map<Integer,String> resDic;
	static{
		resDic = new HashMap<Integer,String>();
		resDic.put(1001,"sig校验失败");
		resDic.put(1002,"短信/语音内容中含有敏感词");
		resDic.put(1003,"请求包体没有sig字段或sig为空");
		resDic.put(1004,"请求包解析失败，通常情况下是由于没有遵守API接口说明规范导致的");
		resDic.put(1006,"请求没有权限，比如没有扩展码权限等");
		resDic.put(1007,"其他错误");
		resDic.put(1008,"请求下发短信/语音超时");
		resDic.put(1009,"请求ip不在白名单中");
		resDic.put(1011,"不存在该REST");
		resDic.put(1012,"签名格式错误或者签名未审批");
		resDic.put(1013,"下发短信/语音命中了频率限制策略");
		resDic.put(1014,"模版未审批或请求的内容与审核通过的模版内容不匹配");
		resDic.put(1015,"手机号在黑名单库中,通常是用户退订或者命中运营商黑名单导致的");
		resDic.put(1016,"手机号格式错误");
		resDic.put(1017,"请求的短信内容太长");
		resDic.put(1018,"语音验证码格式错误");
		resDic.put(1019,"sdkappid不存在");
		resDic.put(1020,"sdkappid已禁用");
		resDic.put(1021,"请求发起时间不正常，通常是由于您的服务器时间与腾讯云服务器时间差异超过10分钟导致的");
		resDic.put(1022,"业务短信日下发条数超过设定的上限");
		resDic.put(1023,"单个手机号30秒内下发短信条数超过设定的上限");
		resDic.put(1024,"单个手机号1小时内下发短信条数超过设定的上限");
		resDic.put(1025,"单个手机号日下发短信条数超过设定的上限");
		resDic.put(1026,"单个手机号下发相同内容超过设定的上限");
		resDic.put(1029,"营销短信发送时间限制");
		resDic.put(1030,"不支持该请求");
		resDic.put(1031,"套餐包余额不足");
		resDic.put(1032,"个人用户没有发营销短信的权限");
		resDic.put(1033,"欠费被停止服务");

	}
	
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
	
	/**
	 * 
	 *  SmsSingleSenderResult
		result 0
		errMsg OK
		ext 
		sid 8:JjFy93IGKpqwwCLA9sr20171106
		fee 1
		09:53:05.524 [main] WARN com.zhs.mytime.filemanage.comm.RedisUtil - SmsSingleSenderResult
		result 0
		errMsg OK
		ext 
		sid 8:JjFy93IGKpqwwCLA9sr20171106
		fee 1
		
		"result": 0, //0表示成功(计费依据)，非0表示失败
	    "errmsg": "OK", //result非0时的具体错误信息
	    "ext": "", //用户的session内容，腾讯server回包中会原样返回
	    "sid": "xxxxxxx", //标识本次发送id，标识一次短信下发记录
	    "fee": 1 //短信计费的条数
	 * @throws Exception 
	 * 
	 * */
	//templateId 是短信正文的模板Id
	public static String sendMsgByTemplate(int smsAppId, String secrectKey,String phone,String info,int templateId) throws Exception{
		
		//假设短信模板 id 为 123，模板内容为：测试短信，{1}，{2}，{3}，上学。
		 SmsSingleSender sender = null;
		 sender = new SmsSingleSender(smsAppId,secrectKey);
		 ArrayList<String> params = new ArrayList<String>();
		 params.add(info);
		 SmsSingleSenderResult   result = sender.sendWithParam("86", phone, templateId, params, "", "", "");
		 if(result!=null&&result.result!=0){
			 throw new Exception(resDic.get(result.result));
		 }
		 System.out.println(result);
		 logger.warn(result.toString());
		 return result.toString();
		
	}
	
	//#################中国网建SMS短信
	public static void sendMsg(String phone,String validateNum)throws Exception
	{
		sendMsg( phone, validateNum,"验证码：");
	}
	
	public static void sendMsg(String phone,String validateNum,String msg)throws Exception
	{
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://utf8.api.smschinese.cn");
		post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");//在头文件中设置转码
		NameValuePair[] data ={ new NameValuePair("Uid", "XXX"),new NameValuePair("Key", "XXXX"),new NameValuePair("smsMob",phone),new NameValuePair("smsText",msg+validateNum)};
		post.setRequestBody(data);
	
		client.executeMethod(post);
		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		System.out.println("statusCode:"+statusCode);
		for(Header h : headers)
		{
		System.out.println(h.toString());
		}
		String result = new String(post.getResponseBodyAsString().getBytes("utf-8"));
		System.out.println(result); //打印返回消息状态
		post.releaseConnection();
	}

}
