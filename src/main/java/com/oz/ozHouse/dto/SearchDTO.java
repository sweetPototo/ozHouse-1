package com.oz.ozHouse.dto;

public class SearchDTO {
	String member_id;
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public int getDetail_int() {
		return detail_int;
	}
	public void setDetail_int(int detail_int) {
		this.detail_int = detail_int;
	}
	public String getDetail_string() {
		return detail_string;
	}
	public void setDetail_string(String detail_string) {
		this.detail_string = detail_string;
	}
	String mode;
	int detail_int;
	String detail_string;
	
}
