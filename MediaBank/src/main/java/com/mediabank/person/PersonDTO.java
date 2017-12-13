package com.mediabank.person;

import java.sql.Date;

import com.mediabank.member.MemberDTO;

public class PersonDTO extends MemberDTO{
	private String nickName;
	private String name;
	private Date birth;
	private String artist;
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
}
