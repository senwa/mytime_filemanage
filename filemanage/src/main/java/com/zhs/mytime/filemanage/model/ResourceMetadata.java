package com.zhs.mytime.filemanage.model;

import java.io.Serializable;
import java.util.Date;

public class ResourceMetadata implements Serializable {

	private String id;
	private Date regdate;
	private String regname;
	private String regcode;
	private String filepath;
	private Double filesize;
	private String videoaudioPosterpath;
	private Byte filetype;
	private Integer videoaudioDuration;
	private Double width;
	private Double height;

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

	public Integer getVideoaudioDuration() {
		return videoaudioDuration;
	}

	public void setVideoaudioDuration(Integer videoaudioDuration) {
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

	@Override
	public String toString() {
		return "ResourceMetadata [id=" + id + ", regdate=" + regdate + ", regname=" + regname + ", regcode=" + regcode
				+ ", filepath=" + filepath + ", filesize=" + filesize + ", videoaudioPosterpath=" + videoaudioPosterpath
				+ ", filetype=" + filetype + ", videoaudioDuration=" + videoaudioDuration + ", width=" + width
				+ ", height=" + height + "]";
	}

	
}
