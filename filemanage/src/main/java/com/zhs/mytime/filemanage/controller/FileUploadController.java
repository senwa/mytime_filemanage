package com.zhs.mytime.filemanage.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zhs.mytime.filemanage.comm.FastDFSClientWrapper;
import com.zhs.mytime.filemanage.comm.FileUtil;
import com.zhs.mytime.filemanage.comm.RedisUtil;
import com.zhs.mytime.filemanage.comm.ResultMessage;
import com.zhs.mytime.filemanage.comm.StringUtils;
import com.zhs.mytime.filemanage.comm.UniqueIdUtil;
import com.zhs.mytime.filemanage.model.ResourceMetadata;
import com.zhs.mytime.filemanage.service.ResourceMetadataMapperService;

@Controller
@PreAuthorize("hasRole('USER')")
public class FileUploadController {
	@Resource
	private ResourceMetadataMapperService resMetadataService;
	@Autowired
	private FastDFSClientWrapper fastDFSClientWrapper;
	
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());  
	@Value("${fileuploadFolder}")
	private String fileuploadFolder;
	
    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;
    
    @Value("${fdfs.thumbImage.width}")
    private String  fdfs_thumbImage_width;
    
    @Value("${fdfs.thumbImage.height}")
    private String fdfs_thumbImage_height;
	
	//跳转到上传文件的页面
    @RequestMapping(value="/uploadPage", method = RequestMethod.GET)
    public String goUploadImg() {
        //跳转到 templates 目录下的 uploadimg.html
        return "uploadimg";
    }
    
    private Map<String,String> getAcoountAndFullName(HttpServletRequest request){
    	String account = null;
    	String fullName = null;
    	//通过header中的token,避免再次解析token花费时间,直接到redis中拿到过滤器中存放的账号信息
    	String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
             final String authToken = authHeader.substring(tokenHead.length());
             account = RedisUtil.getMapValue(authToken, "account");
             fullName = RedisUtil.getMapValue(authToken, "fullName");
        }
        
        Map<String,String> res = new HashMap<String,String>();
        res.put("account", account);
        res.put("fullName", fullName);
        return res;
    }
    
  //处理文件上传
    @RequestMapping(value="/upload", method = RequestMethod.POST)
    public @ResponseBody ResultMessage uploadImg(@RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
    	
    	String account = null,fullName=null;
    	Map<String,String> userInfo = getAcoountAndFullName(request);
    	account = userInfo.get("account");
    	fullName = userInfo.get("fullName");
    	
    	ResultMessage res = new ResultMessage(ResultMessage.SUCCESS, "上传成功");
    	String filePath = null;
    	String fsdfsFullPath = null;
    	String fileName = "";
    	String wrongFileDir = fileuploadFolder+File.separator+"wrong"+File.separator;
    	try{
    		
    		fileName = file.getOriginalFilename();
    		if(StringUtils.isEmpty(account)){
    			account = request.getParameter("account");//账号名
    		}
    		
    		logger.info("account",account);
    		
    		String contentType = file.getContentType();
    		logger.info("contentType",contentType);
    		//Calendar cal = Calendar.getInstance();
    		//路径定义规则(便于存储备份用这种格式):../人的微信号unionIds/年份s/月份s/
	       /* filePath = fileuploadFolder+account+File.separator+cal.get(Calendar.YEAR)+File.separator+(cal.get(Calendar.MONTH)+1)+File.separator;
	        fileName = System.currentTimeMillis()+fileName;*/
	        
	        //FileUtil.uploadFile(file.getBytes(), filePath, fileName);
	        //上传到fastdfs服务器 并生成缩略图
    		fsdfsFullPath = fastDFSClientWrapper.uploadFileWithGenThumb(file);//返回带group的fastdfs上的文件存储路径
	        
	        //写数据库
	        ResourceMetadata record = new ResourceMetadata();
	        record.setId(UniqueIdUtil.getGuidRan());
	        record.setFilename(fileName);
	        record.setClientinfo(request.getParameter("clientinfo"));//客户端信息(网页端待测试)
	        record.setFilepath(fsdfsFullPath);
	        record.setFilesize(Double.valueOf(file.getSize()));
	        record.setFiletype(ResourceMetadata.getFileType(fileName));
	        String locationMsg = request.getParameter("locationMsg");
        	record.setLocationMsg(locationMsg);
        	record.setRegcode(account);
        	record.setRegname(fullName);
        	Calendar cal = Calendar.getInstance();
        	record.setRegdate(cal.getTime());
        	record.setYearStr(StringUtils.padLeft(String.valueOf(cal.get(Calendar.YEAR)),2,'0'));
        	record.setMonthStr(StringUtils.padLeft(String.valueOf(cal.get(Calendar.MONTH)+1),2,'0'));
        	record.setDayStr(StringUtils.padLeft(String.valueOf(cal.get(Calendar.DATE)), 2, '0'));
        	record.setWeekdayStr(String.valueOf(cal.get(Calendar.DAY_OF_WEEK)));
        	record.setSlavePostfix(fdfs_thumbImage_width+"x"+fdfs_thumbImage_height);
	        
        	String latStr = request.getParameter("lat");
        	String lngStr = request.getParameter("lng");
        	String duration = request.getParameter("duration");
        	String heightStr = request.getParameter("height");
        	String widthStr = request.getParameter("width");
        	
	        if(record.getFiletype().byteValue()==ResourceMetadata.PIC){
	        	//如果传入的参数中存在信息,优先使用传进来的参数信息
	        	Map<String,String> imageInfo = null;
	        	String fileExt = FileUtil.getFileExt(fileName);
	        	if("JPG".equalsIgnoreCase(fileExt)||"JPEG".equalsIgnoreCase(fileExt)){
	        		imageInfo = FileUtil.getImageInfo(file.getInputStream());
	        	}
	        	
	        	
	        	if(StringUtils.isNotEmpty(heightStr)&&StringUtils.isNumberic(heightStr)){
	        		record.setHeight(Double.valueOf(heightStr));
	        	}else{
	        		if(imageInfo!=null){
	        			record.setHeight(Double.valueOf(imageInfo.get("height")));
	        		}else{
	        			logger.warn(record.getId()+"未设置高度");
	        		}
	        	}
	        	
	        	if(StringUtils.isNotEmpty(widthStr)&&StringUtils.isNumberic(widthStr)){
	        		record.setWidth(Double.valueOf(widthStr));
	        	}else{
	        		if(imageInfo!=null){
	        			record.setWidth(Double.valueOf(imageInfo.get("width")));
	        		}else{
	        			logger.warn(record.getId()+"未设置宽度");
	        		}
	        	}
	        	
	        	if(StringUtils.isNotEmpty(latStr)&&StringUtils.isNumberic(latStr)){
	        		record.setLatitude(latStr);
	        	}else{
	        		if(imageInfo!=null){
	        			record.setLatitude(imageInfo.get("lat"));
	        		}else{
	        			logger.warn(record.getId()+"未设置经纬度");
	        		}
	        	}
	        	
	        	if(StringUtils.isNotEmpty(lngStr)&&StringUtils.isNumberic(lngStr)){
	        		record.setLongitude(lngStr);
	        	}else{
	        		if(imageInfo!=null){
	        			record.setLongitude(imageInfo.get("lng"));
	        		}else{
	        			logger.warn(record.getId()+"未设置经纬度");
	        		}
	        	}
	        	
	        }else if(record.getFiletype().byteValue()==ResourceMetadata.AUDIO){
	        	
	        	record.setLatitude(latStr);
	        	record.setLongitude(lngStr);
	        	if(StringUtils.isNotEmpty(duration)&&StringUtils.isNumberic(duration)){
	        		try{
	        			record.setVideoaudioDuration(Double.valueOf(duration)*1000);
	        		}catch(NumberFormatException fe){
	        			logger.error(duration+"转为Double型失败!");
	        		}
	        	}
	        	
	        }else if(record.getFiletype().byteValue()==ResourceMetadata.VIDEO){
	        	record.setLatitude(latStr);
	        	record.setLongitude(lngStr);
	        	if(StringUtils.isNotEmpty(duration)&&StringUtils.isNumberic(duration)){
	        		try{
	        			record.setVideoaudioDuration(Double.valueOf(duration)*1000);
	        		}catch(NumberFormatException fe){
	        			fe.printStackTrace();
	        			logger.error(duration+"转为Double型失败!");
	        		}
	        	}
	        	
	        	if(StringUtils.isNotEmpty(heightStr)&&StringUtils.isNumberic(heightStr)){
	        		record.setHeight(Double.valueOf(heightStr));
	        	}

	        	if(StringUtils.isNotEmpty(widthStr)&&StringUtils.isNumberic(widthStr)){
	        		record.setWidth(Double.valueOf(widthStr));
	        	}
	        }
	        
	        resMetadataService.insert(record);
	        
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error(fsdfsFullPath+" "+fileName+e.getMessage());
    		res.setResult(ResultMessage.FAIL);
    		res.setCause(e.getMessage());
    		res.setMessage("上传失败");
    	}
        
        //返回json
        return res;
    }
    
    //获取文件列表
    @RequestMapping(value="/getFileNames_bak", method = RequestMethod.GET)
    public @ResponseBody ResultMessage getFileNames_bak(HttpServletRequest request) {
    	ResultMessage res = new ResultMessage(ResultMessage.SUCCESS);
	    try{
	    	ArrayList<File> files = FileUtil.getFileOnly(new File(fileuploadFolder));
	    	List<HashMap<String, String>> filenames = new ArrayList<HashMap<String,String>>();
	    	if(files!=null&&!files.isEmpty()){
	    		File temp = null;
	    		HashMap<String,String> dic = null;
		    		try {
		    		for(int i=0; i<files.size(); i++){
		    			temp = files.get(i);
		    			if(temp!=null){
		    				dic = new HashMap<String,String>();
		    				dic.put("fileName", temp.getName());
							dic.put("fileSize", FileUtil.getFileSize(temp));
							dic.put("fileType", FileUtil.getFileExt(temp));
		    				filenames.add(dic);
		    			}
		    		}
	    		} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
	    	res.setExtData(filenames);
	    }catch(Exception e){
	    	e.printStackTrace();
	    	res.setResult(ResultMessage.FAIL);
	    	res.setCause(e.getMessage());
	    	res.setMessage("获取失败");
	    }
    	
        //返回json
        return res;
    }
    
    //获取文件列表
    @RequestMapping(value="/getFileNames", method = RequestMethod.GET)
    public @ResponseBody ResultMessage getFileNames(HttpServletRequest request) {
    	ResultMessage res = new ResultMessage(ResultMessage.SUCCESS);
	    try{
	    /*	List<HashMap<String, String>> filenames = new ArrayList<HashMap<String,String>>();
	    	HashMap<String,String>  dic = new HashMap<String,String>();
			dic.put("fileName", temp.getName());
			dic.put("fileSize", FileUtil.getFileSize(temp));
			dic.put("fileType", FileUtil.getFileExt(temp));
			filenames.add(dic);*/
			//解析出用户信息
			String account = null,fullName=null;
	    	Map<String,String> userInfo = getAcoountAndFullName(request);
	    	account = userInfo.get("account");
	    	fullName = userInfo.get("fullName");
	    	
	    	Map<String,Object> queryParam = new HashMap<String,Object>();
	    	
	    	String pageStr = request.getParameter("page");
	    	String pageSizeStr= request.getParameter("pageSize");
	    	int page = 0;
	    	int pageSize = 20;
	    	if(StringUtils.isInteger(pageStr)){
	    		page = Integer.valueOf(page);
	    	}
	    	if(StringUtils.isInteger(pageSizeStr)){
	    		pageSize = Integer.valueOf(pageSizeStr);
	    	}	    	
	    	
	    	String yearStr = request.getParameter("yearStr");
	    	String monthStr = request.getParameter("monthStr");
	    	String dayStr = request.getParameter("dayStr");
	    	String weekdayStr = request.getParameter("weekdayStr");
	    	if(StringUtils.isNotEmpty(yearStr)){
	    		queryParam.put("yearStr", yearStr);
	    	}
	    	if(StringUtils.isNotEmpty(monthStr)){
	    		queryParam.put("monthStr", monthStr);
	    	}
	    	if(StringUtils.isNotEmpty(dayStr)){
	    		queryParam.put("dayStr", dayStr);
	    	}
	    	if(StringUtils.isNotEmpty(weekdayStr)){
	    		queryParam.put("weekdayStr", weekdayStr);
	    	}
	    	
	    	queryParam.put("regcode", account);
	    	queryParam.put("page", page*pageSize);
	    	queryParam.put("pageSize", pageSize);
	    	
			List<ResourceMetadata> resList = resMetadataService.getListByParam(queryParam);
	    	res.setExtData(resList);
	    }catch(Exception e){
	    	e.printStackTrace();
	    	res.setResult(ResultMessage.FAIL);
	    	res.setCause(e.getMessage());
	    	res.setMessage("获取失败");
	    }
    	
        //返回json
        return res;
    }
    
    //处理文件下载
    @RequestMapping(value="/download")
    public void downloadFile(HttpServletRequest request,HttpServletResponse response) {
       
    	String fileName = request.getParameter("fileName");
    	if(fileName!=null&&!fileName.contains("..")){
    		
    		try {
				FileUtil.downLoadFileByPath(request, response, fileuploadFolder+fileName, fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    
	
}
