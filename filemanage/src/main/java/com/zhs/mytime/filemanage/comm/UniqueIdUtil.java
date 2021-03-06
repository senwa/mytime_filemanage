package com.zhs.mytime.filemanage.comm;



import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * 产生一个唯一ID
 */
public class UniqueIdUtil {
	/**
	 * ID校正（集群部署时的应用服务器编号）
	 */
	private static long adjust = 1;
	/**
	 * ID当前值
	 */
	private static long nextId = 0;
	/**
	 * 当前ID的上限
	 */
	private static long lastId = -1;
	private static JdbcTemplate jdbcTemplate;
	
	
	private static void init() {
		try {
			jdbcTemplate = null;//(JdbcTemplate) AppUtil.getBean("jdbcTemplateSN");
			String path = FileUtil.getClassesPath() + "conf/app.properties".replace("/", File.separator);
			String strAdjust = FileUtil.readFromProperties(path, "genId.adjust");
			if (strAdjust != null) {
				adjust = Integer.parseInt(strAdjust);
			}
		} catch (Exception ex) {
			adjust=1;
		}
	}

	/**
	 * 获取下一段ID范围，并记录该ID计算机的bound
	 */
	private static  void getNextIdBlock() {
		if(jdbcTemplate==null){
			init();
		}
		Long bound=-1L;
		Integer incremental=-1;
		String sql="SELECT bound,incremental FROM SYS_DB_ID T WHERE T.ID=?";
		String upSql="UPDATE SYS_DB_ID  SET BOUND=? WHERE ID=?";
		try{
			Map map = jdbcTemplate.queryForMap(sql, new Object[] { adjust });
			bound = Long.parseLong(map.get("bound").toString());
			incremental = Integer.parseInt(map.get("incremental").toString());
			nextId = bound;
			lastId = bound + incremental;
			jdbcTemplate.update(upSql, new Object[] { lastId , adjust });
		}
		catch(org.springframework.dao.EmptyResultDataAccessException e){
			insertNewComputer();
		}
	}
	
	/**
	 * 不存在该计算机编号的则插入一条记录
	 */
	private static void insertNewComputer(){
		try{
			lastId = 10000;
			String sql="INSERT INTO SYS_DB_ID (id,incremental,bound) VALUES("+adjust+",10000,"+lastId+")";
			jdbcTemplate.update(sql);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 产生一个唯一ID。 使用同步，防止重复，测试方法见main方法
	 * 
	 */
	public static synchronized long genId() {
		if (lastId <= nextId) {
			getNextIdBlock();
		}
		long _nextId = nextId++;
		return _nextId + adjust*10000000000000L;
	}
	


	/**
	 * 产生guid
	 * 
	 * @return
	 */
	public static final String getGuid() {
		
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	/*2014-1-10 start by zsd*/
	
	/**
	 * 产生guid带随机数
	 * 
	 * @return
	 */
	public static final String getGuidRan() {
		int[] array = {0,1,2,3,4,5,6,7,8,9}; 
		Random rand = new Random(); 
		for (int i = 10; i > 1; i--) 
		{     
			int index = rand.nextInt(i); 
			int tmp = array[index];    
			array[index] = array[i - 1];     
			array[i - 1] = tmp; 
		 }
		 String result =""; 
		 for(int i = 0; i < 5; i++){   
			 Integer ar=array[i];
			 result =result+ar.toString();
		 }
		 UUID uuid = UUID.randomUUID();
		 String uuidString=uuid.toString().replaceAll("-", "");
		 return (uuidString+"_"+result).toUpperCase();
	}
	/**
	 * 生成指定位数的随机数
	 * 
	 * @return
	 */
	public static final String getRan(Integer num) {
		int[] array = {0,1,2,3,4,5,6,7,8,9}; 
		Random rand = new Random(); 
		for (int i = 10; i > 1; i--) 
		{     
			int index = rand.nextInt(i); 
			int tmp = array[index];    
			array[index] = array[i - 1];     
			array[i - 1] = tmp; 
		 }
		 String result =""; 
		 for(int i = 0; i < num; i++){   
			 Integer ar=array[i];
			 result =result+ar.toString();
		 }
		return result;
	}
	
	/**
	 * 流水号，当前日期+三位随机数---zsd
	 * 
	 * @return
	 */
	public static final String getSerialNum() {
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
		String date= simpleDateFormat.format(new Date());
		String ran=getRan(3);
		return date+ran;
	}
	/*2014-1-10 end by zsd*/

	/**
	 * 测试类 使用两个线程进行测试
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		

		// final Set<Long> set=Collections.synchronizedSet(new HashSet<Long>());
		// //final Set<Long> set=new HashSet<Long>();
		// Thread td1=new Thread(){
		// public void run() {
		// try {
		// int i=0;
		// while(i<10000)
		// {
		// Long id=genId();
		// //Long id=new Long(i);
		// //demo.add(id);
		// set.add(id);
		// i++;
		//
		// }
		// } catch (Exception e) {
		// logger.info("td1:" + e.getMessage());
		// }
		// }
		// };
		// //
		// Thread td2=new Thread(){
		// public void run() {
		// try {
		// int i=0;
		// while(i<10000)
		// {
		// Long id=genId();
		// //Long id=new Long(i);
		// set.add(id);
		// i++;
		// }
		// } catch (Exception e) {
		// logger.info("td2:" + e.getMessage());
		// }
		// }
		// };
		// logger.info("start");
		// long start=System.currentTimeMillis();
		// td1.start();
		// td2.start();
		// td1.join();
		// td2.join();
		// long end=System.currentTimeMillis();
		//
		// logger.info((end-start) +"," + set.size() +"," + nextValue);

	}


	

}