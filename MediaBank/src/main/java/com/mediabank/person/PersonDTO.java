package com.mediabank.person;

import java.sql.Date;

import com.mediabank.member.MemberDTO;

public class PersonDTO extends MemberDTO{
	private String nickname;
	private String name;
	private Date birth;
	private String artist;
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
