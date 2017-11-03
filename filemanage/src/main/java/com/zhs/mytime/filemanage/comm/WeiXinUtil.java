package com.zhs.mytime.filemanage.comm;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;


/**
 * @author zhs
 * 微信消息处理
 * */
public class WeiXinUtil {
	private static Logger logger = LoggerFactory.getLogger(WeiXinUtil.class);
	/**
	 * 请求参数:
	 * appid 	是 	小程序唯一标识
	 *	secret 	是 	小程序的 app secret
	 *	js_code 	是 	登录时获取的 code
	 *	grant_type 	是 	填写为 authorization_code
	 * 返回值:
	 *  openid 	用户唯一标识
	 *	session_key 	会话密钥
	 *	unionid 	用户在开放平台的唯一标识符。本字段在满足一定条件的情况下才返回。具体参看UnionID机制说明
	 * */
	
	private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code";
	
	
	
	public static Map<String,String> getWXLoginInfo(String appId,String appSecret,String code){
		Object[] args={
				appId,appSecret,code
		};
		MessageFormat sqlFormater = new MessageFormat(WX_LOGIN_URL);
		String fullUrl =  sqlFormater.format(args);
		Map<String,Object> param = new HashMap<String,Object>();
		try {
			String accessTokenJson = httpPost2(fullUrl,param,false);
			logger.info(accessTokenJson);
			if(StringUtils.isNotEmpty(accessTokenJson)){
				JSONObject jsonResult = JSONObject.fromObject(accessTokenJson);
				Map<String,String> resMap = new HashMap<String,String>();
				Object errcode  = jsonResult.get("errcode");
				if(errcode==null){
					resMap.put("isSuccess", "1");
					resMap.put("openid", String.valueOf(jsonResult.get("openid")));
					resMap.put("session_key", String.valueOf(jsonResult.get("session_key")));
					resMap.put("unionid", String.valueOf(jsonResult.get("unionid")));
					return resMap;
				}else{//异常
					resMap.put("isSuccess", "0");
					resMap.put("errcode", String.valueOf(jsonResult.get("errcode")));
					resMap.put("errmsg", String.valueOf(jsonResult.get("errmsg")));
				}
				return resMap;
			}
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	  public static String httpPost2(String url,Map<String,Object> param, boolean noNeedResponse) throws ClientProtocolException, IOException{
		  CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		  HttpPost httpPost = new HttpPost(url);
		    //拼接参数
		    List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		    if (null != param) {
           		for (Entry<String, Object> s:param.entrySet()) {
           			nvps.add(new BasicNameValuePair(s.getKey(), String.valueOf(s.getValue())));
           		}
            }
		    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		    CloseableHttpResponse response2 = httpclient.execute(httpPost);
	
		    try {
		        System.out.println(response2.getStatusLine());
		        HttpEntity entity2 = response2.getEntity();
		        if (response2.getStatusLine().getStatusCode() == 200) {
	                String str = "";
	                    /**读取服务器返回过来的json字符串数据**/
	                    str = EntityUtils.toString(entity2);
	                    if (noNeedResponse) {
	                        return null;
	                    }
	                    return str;
	            }
		        //消耗掉response  and ensure it is fully consumed
		        EntityUtils.consume(entity2);
		    } finally {
		        response2.close();
		    }
		    return null;
	  }
	
	/**
     * post请求
     * @param url         url地址
     * @param jsonParam     参数
     * @param noNeedResponse    不需要返回结果
     * @return
     */
    public static String httpPost(String url,Map<String,Object> param, boolean noNeedResponse){
        //post请求返回结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpRequst = new HttpPost(url);
        try {
            if (null != param) {
            	List<NameValuePair>list = new ArrayList<NameValuePair>();
           		for (Entry<String, Object> s:param.entrySet()) {
           			list.add(new BasicNameValuePair(s.getKey(), String.valueOf(s.getValue())));
           		}
           		httpRequst.setEntity(new UrlEncodedFormEntity(list));
            }
            HttpResponse result = httpClient.execute(httpRequst);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
                    if (noNeedResponse) {
                        return null;
                    }
                    return str;
                   
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url, e);
        }finally{
        	if(httpRequst!=null&&!httpRequst.isAborted()){
        		httpRequst.abort();
        	}
        }
		return "";
        
    }
    
    public static JSONObject httpPostJSONObject(String url,JSONObject jsonParam){
    
    	String jsonStr = httpPost(url,jsonParam, false);
    	
    	 /**把json字符串转换成json对象**/
    	JSONObject jsonResult = JSONObject.fromObject(jsonStr);
    	return jsonResult;
    }
}
