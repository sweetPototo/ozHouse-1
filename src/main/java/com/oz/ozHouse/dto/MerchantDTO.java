package com.oz.ozHouse.dto;

public class MerchantDTO {
	
	private int mer_num;
	private String mer_id;
	private String mer_pw;
	private String mer_isbrand;
	private String mer_company;
	private String mer_comnum1;
	private String mer_comnum2;
	private String mer_comnum3;
	private String mer_hp1;
	private String mer_hp2;
	private String mer_hp3;
	private String mer_comintro;
	private String mer_homepage;
	private String mer_manname;
	private String mer_manhp1;
	private String mer_manhp2;
	private String mer_manhp3;
	private String mer_manemail;
	private String mer_category;
	private String mer_prodintro;
	private String mer_othershop;
	private String mer_file;
	private String mer_joindate;
	private String mer_inbranddate;
	private String mer_delete;
	private String mer_business_adress;
	private String mer_business_registration;
	private String mer_name;
	private String mer_email;
	private String mer_business_post;
	
	public int getMer_num() {
		return mer_num;
	}
	public void setMer_num(int mer_num) {
		this.mer_num = mer_num;
	}
	public String getMer_id() {
		return mer_id;
	}
	public void setMer_id(String mer_id) {
		this.mer_id = mer_id;
	}
	public String getMer_pw() {
		return mer_pw;
	}
	public void setMer_pw(String mer_pw) {
		this.mer_pw = mer_pw;
	}
	public String getMer_isbrand() {
		return mer_isbrand;
	}
	public void setMer_isbrand(String mer_isbrand) {
		this.mer_isbrand = mer_isbrand;
	}
	public String getMer_company() {
		return mer_company;
	}
	public void setMer_company(String mer_company) {
		this.mer_company = mer_company;
	}
	public String getMer_comnum1() {
		return mer_comnum1;
	}
	public void setMer_comnum1(String mer_comnum1) {
		this.mer_comnum1 = mer_comnum1;
	}
	public String getMer_comnum2() {
		return mer_comnum2;
	}
	public void setMer_comnum2(String mer_comnum2) {
		this.mer_comnum2 = mer_comnum2;
	}
	public String getMer_comnum3() {
		return mer_comnum3;
	}
	public void setMer_comnum3(String mer_comnum3) {
		this.mer_comnum3 = mer_comnum3;
	}
	public String getMer_hp1() {
		return mer_hp1;
	}
	public void setMer_hp1(String mer_hp1) {
		this.mer_hp1 = mer_hp1;
	}
	public String getMer_hp2() {
		return mer_hp2;
	}
	public void setMer_hp2(String mer_hp2) {
		this.mer_hp2 = mer_hp2;
	}
	public String getMer_hp3() {
		return mer_hp3;
	}
	public void setMer_hp3(String mer_hp3) {
		this.mer_hp3 = mer_hp3;
	}
	public String getMer_comintro() {
		return mer_comintro;
	}
	public void setMer_comintro(String mer_comintro) {
		this.mer_comintro = mer_comintro;
	}
	public String getMer_homepage() {
		return mer_homepage;
	}
	public void setMer_homepage(String mer_homepage) {
		this.mer_homepage = mer_homepage;
	}
	public String getMer_manname() {
		return mer_manname;
	}
	public void setMer_manname(String mer_manname) {
		this.mer_manname = mer_manname;
	}
	public String getMer_manhp1() {
		return mer_manhp1;
	}
	public void setMer_manhp1(String mer_manhp1) {
		this.mer_manhp1 = mer_manhp1;
	}
	public String getMer_manhp2() {
		return mer_manhp2;
	}
	public void setMer_manhp2(String mer_manhp2) {
		this.mer_manhp2 = mer_manhp2;
	}
	public String getMer_manhp3() {
		return mer_manhp3;
	}
	public void setMer_manhp3(String mer_manhp3) {
		this.mer_manhp3 = mer_manhp3;
	}
	public String getMer_manemail() {
		return mer_manemail;
	}
	public void setMer_manemail(String mer_manemail) {
		this.mer_manemail = mer_manemail;
	}
	public String getMer_category() {
		return mer_category;
	}
	public void setMer_category(String mer_category) {
		this.mer_category = mer_category;
	}
	public String getMer_prodintro() {
		return mer_prodintro;
	}
	public void setMer_prodintro(String mer_prodintro) {
		this.mer_prodintro = mer_prodintro;
	}
	public String getMer_othershop() {
		return mer_othershop;
	}
	public void setMer_othershop(String mer_othershop) {
		this.mer_othershop = mer_othershop;
	}
	public String getMer_file() {
		return mer_file;
	}
	public void setMer_file(String mer_file) {
		this.mer_file = mer_file;
	}
	public String getMer_joindate() {
		return mer_joindate;
	}
	public void setMer_joindate(String mer_joindate) {
		this.mer_joindate = mer_joindate;
	}
	public String getMer_inbranddate() {
		return mer_inbranddate;
	}
	public void setMer_inbranddate(String mer_inbranddate) {
		this.mer_inbranddate = mer_inbranddate;
	}
	public String getAllComnum() {
		if (mer_comnum1 == null) return "다시 확인해주세요";
		return mer_comnum1+"-"+mer_comnum2+"-"+mer_comnum3;
	}
	public String getAllMer_hp() {
		if (mer_hp1 == null) return "다시 확인해주세요";
		return mer_hp1+"-"+mer_hp2+"-"+mer_hp3;
	}
	public String getAllMer_manhp() {
		if (mer_manhp1 == null) return "다시 확인해주세요";
		return mer_manhp1+"-"+mer_manhp2+"-"+mer_manhp3;
	}
	public String getMer_delete() {
		return mer_delete;
	}
	public void setMer_delete(String mer_delete) {
		this.mer_delete = mer_delete;
	}
	public String getMer_business_adress() {
		return mer_business_adress;
	}
	public void setMer_business_adress(String mer_business_adress) {
		this.mer_business_adress = mer_business_adress;
	}
	public String getMer_business_registration() {
		return mer_business_registration;
	}
	public void setMer_business_registration(String mer_business_registration) {
		this.mer_business_registration = mer_business_registration;
	}
	public String getMer_name() {
		return mer_name;
	}
	public void setMer_name(String mer_name) {
		this.mer_name = mer_name;
	}
	public String getMer_email() {
		return mer_email;
	}
	public void setMer_email(String mer_email) {
		this.mer_email = mer_email;
	}
	public String getMer_business_post() {
		return mer_business_post;
	}
	public void setMer_business_post(String mer_business_post) {
		this.mer_business_post = mer_business_post;
	}
}
