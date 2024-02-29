package com.oz.ozHouse.dto;

public class MypagePointDTO {
	long order_code;
	String order_date;
	String statement;
	int point;
	
	public long getOrder_code() {
		return order_code;
	}
	public void setOrder_code(long order_code) {
		this.order_code = order_code;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public String getStatement() {
		return statement;
	}
	public void setStatement(String statement) {
		this.statement = statement;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
}
