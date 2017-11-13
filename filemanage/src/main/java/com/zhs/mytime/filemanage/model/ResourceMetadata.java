package com.zhs.mytime.filemanage.model;

import java.util.Date;

import com.zhs.mytime.filemanage.comm.FileUtil;

public class ResourceMetadata {
    private String id;

    private Date regdate;

    private String regname;

    private String regcode;

    private String filepath;

    private Double filesize;

    private String videoaudioPosterpath;

    private Byte filetype;

    private Double videoaudioDuration;

    private Double width;

    private Double height;

    private String longitude;

    private String latitude;

    private String locationMsg;

    private String clientinfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public String getRegname() {
        return regname;
    }

    public void setRegname(String regname) {
        this.regname = regname == null ? null : regname.trim();
    }

    public String getRegcode() {
        return regcode;
    }

    public void setRegcode(String regcode) {
        this.regcode = regcode == null ? null : regcode.trim();
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath == null ? null : filepath.trim();
    }

    public Double getFilesize() {
        return filesize;
    }

    public void setFilesize(Double filesize) {
        this.filesize = filesize;
    }

    public String getVideoaudioPosterpath() {
        return videoaudioPosterpath;
    }

    public void setVideoaudioPosterpath(String videoaudioPosterpath) {
        this.videoaudioPosterpath = videoaudioPosterpath == null ? null : videoaudioPosterpath.trim();
    }

    public Byte getFiletype() {
        return filetype;
    }

    public void setFiletype(Byte filetype) {
        this.filetype = filetype;
    }

    public Double getVideoaudioDuration() {
        return videoaudioDuration;
    }

    public void setVideoaudioDuration(Double videoaudioDuration) {
        this.videoaudioDuration = videoaudioDuration;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public String getLocationMsg() {
        return locationMsg;
    }

    public void setLocationMsg(String locationMsg) {
        this.locationMsg = locationMsg == null ? null : locationMsg.trim();
    }

    public String getClientinfo() {
        return clientinfo;
    }

    public void setClientinfo(String clientinfo) {
        this.clientinfo = clientinfo == null ? null : clientinfo.trim();
    }
    
    public final static byte UNKNOW = (byte) 0;//未知
    public final static byte PIC = (byte) 1;//图片
    public final static byte AUDIO = (byte) 2;//音频
    public final static byte VIDEO = (byte) 3;//视频
    
    //根据文件名返回文件类别
    public static byte getFileType(String fileName){
    	String fileExt = FileUtil.getFileExt(fileName);
    	if("jpg".equalsIgnoreCase(fileExt)||"jpeg".equalsIgnoreCase(fileExt)||"png".equalsIgnoreCase(fileExt)||"gif".equalsIgnoreCase(fileExt)||"icon".equalsIgnoreCase(fileExt)||"bmp".equalsIgnoreCase(fileExt)){
    		return PIC;
    	}else if("silk".equalsIgnoreCase(fileExt)||"wav".equalsIgnoreCase(fileExt)||"mp3".equalsIgnoreCase(fileExt)||"aac".equalsIgnoreCase(fileExt)||"mid".equalsIgnoreCase(fileExt)||"OGG".equalsIgnoreCase(fileExt)){ 
    		return AUDIO;
    	}else if("mp4".equalsIgnoreCase(fileExt)||"avi".equalsIgnoreCase(fileExt)||"rm".equalsIgnoreCase(fileExt)||"wmv".equalsIgnoreCase(fileExt)){
    		return VIDEO;
    	}else{
    		return UNKNOW;
    	}
    }
}