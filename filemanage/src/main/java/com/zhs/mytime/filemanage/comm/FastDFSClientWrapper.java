package com.zhs.mytime.filemanage.comm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.domain.FileInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

@Component
public class FastDFSClientWrapper {
	 private final Logger logger = LoggerFactory.getLogger(FastDFSClientWrapper.class);

	    @Autowired
	    private FastFileStorageClient storageClient;
	    /**
	     * 上传文件
	     * @param file 文件对象
	     * @return 文件访问地址
	     * @throws IOException
	     */
	    public String uploadFile(MultipartFile file) throws IOException {
	        StorePath storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()),null);
	        return storePath.getFullPath();//getResAccessUrl(storePath);
	    }
	    /**
	     * 上传文件
	     * @param file 文件对象
	     * @return 文件访问地址
	     * @throws IOException
	     */
	    public String uploadFileWithGenThumb(MultipartFile file) throws IOException {
	        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(),file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()),null);
	        return storePath.getFullPath();//getResAccessUrl(storePath);
	    }

	    /**
	     * 将一段字符串生成一个文件上传
	     * @param content 文件内容
	     * @param fileExtension
	     * @return
	     */
	    public String uploadFile(String content, String fileExtension) {
	        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
	        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
	        StorePath storePath = storageClient.uploadFile(stream,buff.length, fileExtension,null);
	        return storePath.getFullPath();//getResAccessUrl(storePath);
	    }

	    // 封装图片完整URL地址
	  /*  private String getResAccessUrl(StorePath storePath) {
	        String fileUrl = AppConstants.HTTP_PRODOCOL + appConfig.getResHost()
	                + ":" + appConfig.getFdfsStoragePort() + "/" + storePath.getFullPath();
	        return fileUrl;
	    }*/
	    
	    
	    /**
	     * 获取文件信息
	     * @param pathWithGroup 带group的路径
	     * @param slaveExt 缩略图后缀,比如 XXXX/XXX/22123_100x100.jpg其中_100x100就是slaveExt
	     * */
	    public boolean isSlaveFileExist(String pathWithGroup,String slaveExt){
	    	//group1/M00/00/00/Co0lgVotLPyATKtpAAUupGAdg1I737.jpg
	    	if(StringUtils.isNotEmpty(pathWithGroup)&&StringUtils.isNotEmpty(slaveExt)){
	    		StringBuilder buff = new StringBuilder(pathWithGroup);
	            int index = buff.lastIndexOf(".");
	            buff.insert(index, slaveExt);
	            this.isFileExist(buff.toString());
	    	}
	    	return false;
	    }
	    
	    
	    /**
	     * 获取文件信息
	     * */
	    public boolean isFileExist(String pathWithGroup){
	    	//group1/M00/00/00/Co0lgVotLPyATKtpAAUupGAdg1I737.jpg
	    	if(StringUtils.isNotEmpty(pathWithGroup)){
	    		  String pattern = "(group\\d+)(.*)";
	    	      // 创建 Pattern 对象
	    	      Pattern r = Pattern.compile(pattern);
	    	      // 现在创建 matcher 对象
	    	      Matcher m = r.matcher(pathWithGroup);
	    	      if (m.find( )) {
	    	         return this.isFileExist(m.group(1), m.group(2));
	    	      }
	    	}
	    	return false;
	    }
	    
	    public boolean isFileExist(String groupName,String path){
	    	try{
	    		FileInfo fileInfo = storageClient.queryFileInfo(groupName, path);
		    	return fileInfo!=null;
	    	}catch(Exception e){
	    		//e.printStackTrace();
	    		logger.warn("文件不存在:"+groupName+" "+path);
	    		return false;
	    	}
	    }
	    

	    /**
	     * 删除文件
	     * @param fileUrl 文件访问地址
	     * @return
	     */
	    public void deleteFile(String fileUrl) {
	        if (StringUtils.isEmpty(fileUrl)) {
	            return;
	        }
	        try {
	            StorePath storePath = StorePath.praseFromUrl(fileUrl);
	            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
	        } catch (FdfsUnsupportStorePathException e) {
	            logger.warn(e.getMessage());
	        }
	    }
	}
