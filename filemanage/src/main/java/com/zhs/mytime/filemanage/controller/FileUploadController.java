package com.zhs.mytime.filemanage.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.zhs.mytime.filemanage.comm.FileUtil;
import com.zhs.mytime.filemanage.comm.ResultMessage;
import com.zhs.mytime.filemanage.comm.StringUtils;
import com.zhs.mytime.filemanage.comm.UniqueIdUtil;
import com.zhs.mytime.filemanage.model.ResourceMetadata;
import com.zhs.mytime.filemanage.service.ResourceMetadataMapperService;

@Controller
public class FileUploadController {
	@Resource
	private ResourceMetadataMapperService resMetadataService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());  
	@Value("${fileuploadFolder}")
	private String fileuploadFolder;
	//跳转到上传文件的页面
    @RequestMapping(value="/uploadPage", method = RequestMethod.GET)
    public String goUploadImg() {
        //跳转到 templates 目录下的 uploadimg.html
        return "uploadimg";
    }
    
  //处理文件上传
    @RequestMapping(value="/upload", method = RequestMethod.POST)
    public @ResponseBody ResultMessage uploadImg(@RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
    	ResultMessage res = new ResultMessage(ResultMessage.SUCCESS, "上传成功");
    	String filePath = null;
    	String fileName = "";
    	String wrongFileDir = fileuploadFolder+File.separator+"wrong"+File.separator;
    	try{
    		
    		fileName = file.getOriginalFilename();
    		String unionId = request.getParameter("unionId");
    		String wxaccount = request.getParameter("wxaccount");//微信账号名
    		if(StringUtils.isEmpty(unionId)){
    			logger.warn("unionId为空上传资源:"+fileName);
    			unionId = "unknown";
    		}
    		
    		logger.info("unionId",unionId);
    		
    		String contentType = file.getContentType();
    		logger.info("contentType",contentType);
    		Calendar cal = Calendar.getInstance();
    		//路径定义规则(便于存储备份用这种格式):../人的微信号unionIds/年份s/月份s/
	        filePath = fileuploadFolder+unionId+File.separator+cal.get(Calendar.YEAR)+File.separator+(cal.get(Calendar.MONTH)+1)+File.separator;
	        fileName = System.currentTimeMillis()+fileName;
	        
	        FileUtil.uploadFile(file.getBytes(), filePath, fileName);
	        
	        //写数据库
	        ResourceMetadata record = new ResourceMetadata();
	        record.setId(UniqueIdUtil.getGuidRan());
	        record.setClientinfo(request.getParameter("clientinfo"));//客户端信息(网页端待测试)
	        record.setFilepath(filePath);
	        record.setFilesize(Double.valueOf(file.getSize()));
	        record.setFiletype(ResourceMetadata.getFileType(fileName));
	        String locationMsg = request.getParameter("locationMsg");
        	record.setLocationMsg(locationMsg);
        	record.setRegcode(unionId);
        	record.setRegname(wxaccount);
        	record.setRegdate(new Date());
	        
        	String latStr = request.getParameter("lat");
        	String lngStr = request.getParameter("lng");
        	String duration = request.getParameter("duration");
        	String heightStr = request.getParameter("height");
        	String widthStr = request.getParameter("width");
        	
	        if(record.getFiletype().byteValue()==ResourceMetadata.PIC){
	        	//如果传入的参数中存在信息,优先使用传进来的参数信息
	        	Map<String,String> imageInfo = FileUtil.getImageInfo(file.getInputStream());
	        	
	        	
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
	        			record.setVideoaudioDuration(Integer.valueOf(duration));
	        		}catch(NumberFormatException fe){
	        			logger.error(duration+"转为整型失败!");
	        		}
	        	}
	        	
	        }else if(record.getFiletype().byteValue()==ResourceMetadata.VIDEO){
	        	record.setLatitude(latStr);
	        	record.setLongitude(lngStr);
	        	if(StringUtils.isNotEmpty(duration)&&StringUtils.isNumberic(duration)){
	        		try{
	        			record.setVideoaudioDuration(Integer.valueOf(duration));
	        		}catch(NumberFormatException fe){
	        			logger.error(duration+"转为整型失败!");
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
	        
    	}catch(FileNotFoundException e){
    		e.printStackTrace();
    		logger.error(e.getMessage());
    		res.setResult(ResultMessage.FAIL);
    		res.setCause(e.getMessage());
    		res.setMessage("上传失败,创建文件失败");
    	}catch(IOException e){
    		e.printStackTrace();
    		logger.error(e.getMessage());
    		res.setResult(ResultMessage.FAIL);
    		res.setCause(e.getMessage());
    		res.setMessage("上传失败,写入文件失败");
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error(fileName+e.getMessage());
    		res.setResult(ResultMessage.FAIL);
    		res.setCause(e.getMessage());
    		res.setMessage("上传失败");
    		
    		if(StringUtils.isNotEmpty(filePath+fileName)){
    			//移动文件
        		FileUtil.moveFile(filePath+fileName, wrongFileDir+fileName);
    		}
    	}
        
        //返回json
        return res;
    }
    
    //获取文件列表
    @RequestMapping(value="/getFileNames", method = RequestMethod.GET)
    public @ResponseBody ResultMessage getFileNames(HttpServletRequest request) {
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
