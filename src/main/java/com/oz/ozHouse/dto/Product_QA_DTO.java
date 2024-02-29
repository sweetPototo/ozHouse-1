package com.oz.ozHouse.dto;

public class Product_QA_DTO {
	
	private int product_QA_num;
	private int product_num;
	private String member_id;
	private String product_QA_subject;
	private String product_QA_content;
	private String product_QA_date;
	private int product_re_level;
	private int product_re_step;
	private String product_inquiry_type;
	private String product_QA_state;
	
	public String getProduct_inquiry_type() {
		return product_inquiry_type;
	}
	public void setProduct_inquiry_type(String product_inquiry_type) {
		this.product_inquiry_type = product_inquiry_type;
	}
	public String getProduct_QA_state() {
		return product_QA_state;
	}
	public void setProduct_QA_state(String product_QA_state) {
		this.product_QA_state = product_QA_state;
	}
	public int getProduct_QA_num() {
		return product_QA_num;
	}
	public void setProduct_QA_num(int product_QA_num) {
		this.product_QA_num = product_QA_num;
	}
	public int getProduct_num() {
		return product_num;
	}
	public void setProduct_num(int product_num) {
		this.product_num = product_num;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getProduct_QA_subject() {
		return product_QA_subject;
	}
	public void setProduct_QA_subject(String product_QA_subject) {
		this.product_QA_subject = product_QA_subject;
	}
	public String getProduct_QA_content() {
		return product_QA_content;
	}
	public void setProduct_QA_content(String product_QA_content) {
		this.product_QA_content = product_QA_content;
	}
	public String getProduct_QA_date() {
		return product_QA_date;
	}
	public void setProduct_QA_date(String product_QA_date) {
		this.product_QA_date = product_QA_date;
	}
	public int getProduct_re_level() {
		return product_re_level;
	}
	public void setProduct_re_level(int product_re_level) {
		this.product_re_level = product_re_level;
	}
	public int getProduct_re_step() {
		return product_re_step;
	}
	public void setProduct_re_step(int product_re_step) {
		this.product_re_step = product_re_step;
	}
	
	
	
}
