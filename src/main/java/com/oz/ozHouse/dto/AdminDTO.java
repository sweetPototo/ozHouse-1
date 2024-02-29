package com.oz.ozHouse.dto;

public class AdminDTO {

	private int admin_num;
	private String admin_id;
	private String admin_passwd;
	private String admin_email;
	private String admin_hp1;
	private String admin_hp2;
	private String admin_hp3;
	private String admin_joindate;
	
	public int getAdmin_num() {
		return admin_num;
	}
	public void setAdmin_num(int admin_num) {
		this.admin_num = admin_num;
	}
	public String getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}
	public String getAdmin_passwd() {
		return admin_passwd;
	}
	public void setAdmin_passwd(String admin_passwd) {
		this.admin_passwd = admin_passwd;
	}
	public String getAdmin_email() {
		return admin_email;
	}
	public void setAdmin_email(String admin_email) {
		this.admin_email = admin_email;
	}
	public String getAdmin_hp1() {
		return admin_hp1;
	}
	public void setAdmin_hp1(String admin_hp1) {
		this.admin_hp1 = admin_hp1;
	}
	public String getAdmin_hp2() {
		return admin_hp2;
	}
	public void setAdmin_hp2(String admin_hp2) {
		this.admin_hp2 = admin_hp2;
	}
	public String getAdmin_hp3() {
		return admin_hp3;
	}
	public void setAdmin_hp3(String admin_hp3) {
		this.admin_hp3 = admin_hp3;
	}
	public String getAdmin_joindate() {
		return admin_joindate;
	}
	public void setAdmin_joindate(String admin_joindate) {
		this.admin_joindate = admin_joindate;
	}
	public String getAllAdmin_hp() {
		if (admin_hp1 == null) return "다시 확인해주세요.";
		return admin_hp1+"-"+admin_hp2+"-"+admin_hp3;
	}
}
