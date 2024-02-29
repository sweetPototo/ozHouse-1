package com.oz.ozHouse.dto;

public class User_CouponDTO {

	private int user_coupon_num;
	private int mer_coupon_num;
	private int member_num;
	private int mer_num;
	private String user_coupon_active;
	private String mer_couponenddate;
	private long order_code;
	
	public long getOrder_code() {
		return order_code;
	}
	public void setOrder_code(long order_code) {
		this.order_code = order_code;
	}
	public int getUser_coupon_num() {
		return user_coupon_num;
	}
	public void setUser_coupon_num(int user_coupon_num) {
		this.user_coupon_num = user_coupon_num;
	}
	public int getMer_coupon_num() {
		return mer_coupon_num;
	}
	public void setMer_coupon_num(int mer_coupon_num) {
		this.mer_coupon_num = mer_coupon_num;
	}
	public int getMember_num() {
		return member_num;
	}
	public void setMember_num(int member_num) {
		this.member_num = member_num;
	}
	public int getMer_num() {
		return mer_num;
	}
	public void setMer_num(int mer_num) {
		this.mer_num = mer_num;
	}
	public String getUser_coupon_active() {
		return user_coupon_active;
	}
	public void setUser_coupon_active(String user_coupon_active) {
		this.user_coupon_active = user_coupon_active;
	}
	public String getMer_couponenddate() {
		return mer_couponenddate;
	}
	public void setMer_couponenddate(String mer_couponenddate) {
		this.mer_couponenddate = mer_couponenddate;
	}

	
	
	
	
}
