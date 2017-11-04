package com.zhs.mytime.filemanage.comm;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisUtil {
	private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    //Redis服务器IP
    private final static String ADDR = "127.0.0.1";
    
    //Redis的端口号
    private final static int PORT = 6379;
    
    //访问密码
    private final static String AUTH = "";
    
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private final static int MAX_ACTIVE = 512;
    
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private final static int MAX_IDLE = 200;
    
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private final static int MAX_WAIT = 10000;
    
    private final static int TIMEOUT = 10000;
    
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private final static boolean TEST_ON_BORROW = true;
    
    private static JedisPool jedisPool = null;
    
    
    /**
     * redis过期时间,以秒为单位
     */
    public final static int EXRP_HOUR = 60*60;          //一小时
    public final static int EXRP_DAY = 60*60*24;        //一天
    public final static int EXRP_MONTH = 60*60*24*30;   //一个月
    
    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxWaitMillis(MAX_WAIT);
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("First create JedisPool error : "+e);
        }
    }
    /**
     * 同步获取Jedis实例
     * @return Jedis
     */
    public synchronized static Jedis getJedis() {  
        Jedis jedis = null;
        try {  
            if (jedisPool != null) {  
                jedis = jedisPool.getResource(); 
            }
        } catch (Exception e) {  
            logger.error("Get jedis error : "+e);
        }
        return jedis;
    }  
     
     
    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null && jedisPool !=null) {
        	jedis.close();
        }
    }
     
     
    /**
     * 设置 String
     * @param key
     * @param value
     */
    public static void setString(String key ,String value){
    	Jedis jedis = null;
    	try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis =  getJedis();
            jedis.set(key,value);
            logger.debug("设置值:"+key+":"+value);
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("Set key error : "+e);
        }finally{
        	returnResource(jedis);
        }
    }
    
     
    /**
     * 设置 过期时间
     * @param key
     * @param seconds 以秒为单位
     * @param value
     */
    public static boolean setString(String key ,int seconds,String value){
    	Jedis jedis = null;
    	boolean res = true;
    	try {
            value = StringUtils.isEmpty(value) ? "" : value;
            jedis =  getJedis();
            jedis.setex(key, seconds, value);
            logger.debug("设置值:"+key+":"+value+" 超时时间:"+seconds);
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("Set keyex error : "+e);
            res = false;
        }finally{
        	returnResource(jedis);
        }
    	return res;
    }
     
    /**
     * 获取String值
     * @param key
     * @return value
     */
    public static String getString(String key){
    	Jedis jedis = null;
    	try{
    		jedis = getJedis();
    		if( jedis!= null && jedis.exists(key)){
    			 return jedis.get(key);
            }
           
    	}catch(Exception e){
    	   logger.error(e.getMessage());
    	   e.printStackTrace();
    	}finally{
    		returnResource(jedis);
        }
		return null;
    }
    
    /**
     * 删除String值
     * @param key
     * @return value
     */
    public static Long delString(String key){
    	Jedis jedis = null;
    	try{
    		jedis = getJedis();
    		if( jedis!= null && jedis.exists(key)){
    		     logger.debug("删除值:"+key);
    			 return jedis.del(key);
            }
           
    	}catch(Exception e){
    	   logger.error(e.getMessage());
    	   e.printStackTrace();
    	}finally{
    		returnResource(jedis);
        }
		return 0L;
    }
    
    //Map操作
    /**
     * 设置 Map
     * @param key
     * @param value
     */
    public static void addMap(String key ,Map<String,String> map){
    	Jedis jedis = null;
    	try {
    		if(map!=null){
    			 jedis =  getJedis();
    			 jedis.hmset(key, map);
    		}
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("Set key error : "+e);
        }finally{
        	returnResource(jedis);
        }
    }
    
    /**
     * 设置 Map
     * @param key
     * @param value
     */
    public static void addMap(String key ,int seconds,Map<String,String> map){
    	Jedis jedis = null;
    	try {
    		if(map!=null){
    			 jedis =  getJedis();
    			 jedis.hmset(key, map);
    			 jedis.expire(key, seconds);
    		}
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("Set key error : "+e);
        }finally{
        	returnResource(jedis);
        }
    }
    
    /**
     * 设置 Map
     * @param key
     * @param value
     */
    public static String getMapValue(String key,String field){
    	Jedis jedis = null;
    	try {
    		jedis =  getJedis();
    		if(jedis!=null){
    			if(StringUtils.isNotEmpty(key)&&StringUtils.isNotEmpty(field)){
    				if(jedis.hexists(key, field)){
    					return jedis.hget(key, field);
    				}
    			}
    		}
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("Set key error : "+e);
        }finally{
        	returnResource(jedis);
        }
    	return null;
    }
    
    /**
     * 设置 Map
     * @param key
     * @param value
     */
    public static void setMapValue(String key,String field,String value){
    	Jedis jedis = null;
    	try {
    		jedis =  getJedis();
    		if(jedis!=null){
    			if(StringUtils.isNotEmpty(key)&&StringUtils.isNotEmpty(field)){
    				if(jedis.hexists(key, field)){
    					 jedis.hset(key, field, value);
    				}
    			}
    		}
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("Set key error : "+e);
        }finally{
        	returnResource(jedis);
        }
    }
}