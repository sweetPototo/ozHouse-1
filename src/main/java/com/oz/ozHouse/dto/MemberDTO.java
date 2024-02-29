package com.oz.ozHouse.dto;

public class MemberDTO {

	private int member_num;
	private String member_name;
	private String member_id;
	private String member_passwd;
	private String member_nickname;
	private String member_email;
	private String member_image;
	private String member_address1;
	private String member_address2;
	private String member_address3;
	private String member_postcode1;
	private String member_postcode2;
	private String member_postcode3;
	private String member_hp1;
	private String member_hp2;
	private String member_hp3;
	private int member_point;
	private String member_level;
	private String member_joindate;
	private String member_deletedate;
	
	public String getMember_deletedate() {
		return member_deletedate;
	}
	public void setMember_deletedate(String member_deletedate) {
		this.member_deletedate = member_deletedate;
	}
	public int getMember_num() {
		return member_num;
	}
	public void setMember_num(int member_num) {
		this.member_num = member_num;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMember_passwd() {
		return member_passwd;
	}
	public void setMember_passwd(String member_passwd) {
		this.member_passwd = member_passwd;
	}
	public String getMember_nickname() {
		return member_nickname;
	}
	public void setMember_nickname(String member_nickname) {
		this.member_nickname = member_nickname;
	}
	public String getMember_email() {
		return member_email;
	}
	public void setMember_email(String member_email) {
		this.member_email = member_email;
	}
	public String getMember_image() {
		return member_image;
	}
	public void setMember_image(String member_image) {
		this.member_image = member_image;
	}
	public String getMember_address1() {
		return member_address1;
	}
	public void setMember_address1(String member_address1) {
		this.member_address1 = member_address1;
	}
	public String getMember_address2() {
		return member_address2;
	}
	public void setMember_address2(String member_address2) {
		this.member_address2 = member_address2;
	}
	public String getMember_address3() {
		return member_address3;
	}
	public void setMember_address3(String member_address3) {
		this.member_address3 = member_address3;
	}
	public String getMember_postcode1() {
		return member_postcode1;
	}
	public void setMember_postcode1(String member_postcode1) {
		this.member_postcode1 = member_postcode1;
	}
	public String getMember_postcode2() {
		return member_postcode2;
	}
	public void setMember_postcode2(String member_postcode2) {
		this.member_postcode2 = member_postcode2;
	}
	public String getMember_postcode3() {
		return member_postcode3;
	}
	public void setMember_postcode3(String member_postcode3) {
		this.member_postcode3 = member_postcode3;
	}
	public String getMember_hp1() {
		return member_hp1;
	}
	public void setMember_hp1(String member_hp1) {
		this.member_hp1 = member_hp1;
	}
	public String getMember_hp2() {
		return member_hp2;
	}
	public void setMember_hp2(String member_hp2) {
		this.member_hp2 = member_hp2;
	}
	public String getMember_hp3() {
		return member_hp3;
	}
	public void setMember_hp3(String member_hp3) {
		this.member_hp3 = member_hp3;
	}
	public int getMember_point() {
		return member_point;
	}
	public void setMember_point(int member_point) {
		this.member_point = member_point;
	}
	public String getMember_level() {
		return member_level;
	}
	public void setMember_level(String member_level) {
		this.member_level = member_level;
	}
	public String getMember_joindate() {
		return member_joindate;
	}
	public void setMember_joindate(String member_joindate) {
		this.member_joindate = member_joindate;
	}
	public String getAllMember_hp() {
		if (member_hp1 == null) return "�떎�떆 �솗�씤�빐二쇱꽭�슂.";
		return member_hp1+"-"+member_hp2+"-"+member_hp3;
	}
}
