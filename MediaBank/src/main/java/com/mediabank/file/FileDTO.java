package com.mediabank.file;

import com.mediabank.work.WorkDTO;

public class FileDTO extends WorkDTO{
private int file_num;
	
	private String file_route;
	private String width;
	private String height;
	private String file_kind;
	
	public String getFile_kind() {
		return file_kind;
	}
	public void setFile_kind(String file_kind) {
		this.file_kind = file_kind;
	}
	
	private String file_name;
	
	public int getFile_num() {
		return file_num;
	}
	public void setFile_num(int file_num) {
		this.file_num = file_num;
	}
	public String getFile_route() {
		return file_route;
	}
	public void setFile_route(String file_route) {
		this.file_route = file_route;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
}
