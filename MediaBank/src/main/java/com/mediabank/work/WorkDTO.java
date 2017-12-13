package com.mediabank.work;

import java.sql.Date;

public class WorkDTO {
	private String work;//작품명
	private int user_num;//회원 번호
	private String nickname;//닉네임
	private int work_seq;//리스트 순서
	private Date work_date;//등록일자
	private String upload_check;//업로드 승인 현황
	private String tag;//태그
	private int price;//가격
	private String contents;//내용
	private String reply;//답글내용
	private String sell;//판매 유무
	private int download_hit;//다운로드 수
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public int getUser_num() {
		return user_num;
	}
	public void setUser_num(int user_num) {
		this.user_num = user_num;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getWork_seq() {
		return work_seq;
	}
	public void setWork_seq(int work_seq) {
		this.work_seq = work_seq;
	}
	public Date getWork_date() {
		return work_date;
	}
	public void setWork_date(Date work_date) {
		this.work_date = work_date;
	}
	public String getUpload_check() {
		return upload_check;
	}
	public void setUpload_check(String upload_check) {
		this.upload_check = upload_check;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getSell() {
		return sell;
	}
	public void setSell(String sell) {
		this.sell = sell;
	}
	public int getDownload_hit() {
		return download_hit;
	}
	public void setDownload_hit(int download_hit) {
		this.download_hit = download_hit;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
}
