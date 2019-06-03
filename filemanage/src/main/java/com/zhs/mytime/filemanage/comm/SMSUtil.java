package com.zhs.mytime.filemanage.comm;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMSUtil {
	private static Logger logger = LoggerFactory.getLogger(SMSUtil.class);
	public static Map<String,String> resDic;
	static{
		resDic = new HashMap<String,String>();
		resDic.put("-1","没有该用户账户");
		resDic.put("-2","接口密钥不正确，不是账户登陆密码");
		resDic.put("-21","MD5接口密钥加密不正确");
		resDic.put("-3","短信数量不足");
		resDic.put("-11","该用户被禁用");
		resDic.put("-14","短信内容出现非法字符");
		resDic.put("-4","手机号格式不正确");
		resDic.put("-41","手机号码为空");
		resDic.put("-42","短信内容为空");
		resDic.put("-51","短信签名格式不正确，接口签名格式为：【签名内容】");
		resDic.put("-52","短信签名太长建议签名10个字符以内");
		resDic.put("-6","IP限制");
		//大于0 短信发送数量
	}
	
	//#################中国网建SMS短信
	
	/**
	 * @param phone 手机号，可以逗号隔开
	 * @param validateNum 验证码
	 * @param msg 验证码前面的附加信息
	 * @param uid SMS网建的账户名
	 * @param key SMS网建的密钥
	 * @return  成功发送的短信个数
	 * */
	public static int sendMsg(String phone,String validateNum,String msg,String uid,String key)throws Exception
	{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://utf8.api.smschinese.cn");
		
		List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
        valuePairs.add(new BasicNameValuePair("Uid", uid));
        valuePairs.add(new BasicNameValuePair("Key", key));
        valuePairs.add(new BasicNameValuePair("smsMob",phone));
        valuePairs.add(new BasicNameValuePair("smsText",msg+validateNum));
        
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);
        entity.setContentType("application/x-www-form-urlencoded;charset=utf-8");
        post.setEntity(entity);
        
        try {
        	 // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				@Override
				public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					StatusLine statusLine = response.getStatusLine();
					logger.debug("{},短信发送结果:{}",valuePairs,statusLine);
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
				}

            };
        	
        	 String resStr = client.execute(post,responseHandler);
             
             // 打印响应信息，查看是否登陆是否成功
            logger.debug("短信发送结果{}",resStr);
             //判定返回结果
             if(StringUtils.isNotEmpty(resDic.get(resStr))) {
            	 throw new Exception(resDic.get(resStr));
             }else {
            	 int count = 0;
            	 try {
            		count = Integer.parseInt(resStr);//成功发送短信的个数
            	 }catch(NumberFormatException ne) {
            		 logger.error("短信发送失败");
            		 throw new Exception(resStr);
            	 }
            			
            	 return count;
             }

        }catch(IOException e) {
        	logger.error(e.getMessage());
        }catch(Exception e) {
        	logger.error(e.getMessage());
        	throw e;
        }
        finally {
        	client.close();
        }
        return 0;
	}
	
	public static void main(String[] args) {
		try {
			SMSUtil.sendMsg("15201055473,130513472603", "1234567","验证码:","XX","XXXXXXXXXXX");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
