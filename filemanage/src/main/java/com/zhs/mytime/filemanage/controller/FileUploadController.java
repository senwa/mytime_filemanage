package com.zhs.mytime.filemanage.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.zhs.mytime.filemanage.comm.FileUtil;
import com.zhs.mytime.filemanage.comm.ResultMessage;

@Controller
public class FileUploadController {
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
    	try{
    		//String contentType = file.getContentType();
    		String fileName = file.getOriginalFilename();
	        String filePath = fileuploadFolder;
	        FileUtil.uploadFile(file.getBytes(), filePath, System.currentTimeMillis()+fileName);
    	}catch(Exception e){
    		e.printStackTrace();
    		res.setResult(ResultMessage.FAIL);
    		res.setCause(e.getMessage());
    		res.setMessage("上传失败");
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
