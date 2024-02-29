package com.oz.ozHouse.dto;

public class MsgDTO {
	private int product_num;
	private String reason;
	private String product_approval_status;
	
	public int getProduct_num() {
		return product_num;
	}
	public void setProduct_num(int product_num) {
		this.product_num = product_num;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getProduct_approval_status() {
		return product_approval_status;
	}
	public void setProduct_approval_status(String product_approval_status) {
		this.product_approval_status = product_approval_status;
	}
}
