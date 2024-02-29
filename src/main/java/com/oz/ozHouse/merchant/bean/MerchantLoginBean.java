package com.oz.ozHouse.merchant.bean;

import com.oz.ozHouse.dto.MerchantDTO;
import com.oz.ozHouse.merchant.service.MerchantLoginMapper;

public class MerchantLoginBean {
	private int mer_num;
	private String mer_id;
	private String mer_pw;
	private String mer_isbrand;
	private String mer_company;
	
	public static final int OK = 0;
	public static final int NOT_ID = 1;
	public static final int NOT_PW = 2;
	public static final int ERROR = -1;
	public static final int DELETE_ID = -2;
	
	public void setMer_id(String mer_id) {
		this.mer_id = mer_id;
	}
	public void setMer_passwd(String mer_pw) {
		this.mer_pw = mer_pw;
	}
	public String getMer_id() {
		return mer_id;
	}
	public int getMer_num() {
		return mer_num;
	}
	public void setMer_num(int mer_num) {
		this.mer_num = mer_num;
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
	public String getMer_pw() {
		return mer_pw;
	}
	public void setMer_pw(String mer_pw) {
		this.mer_pw = mer_pw;
	}
	public int loginOk(MerchantLoginMapper memberMapper) {
		try {
			MerchantDTO dto = memberMapper.merchant_getMember(mer_id);
			if (dto != null) {
				if(dto.getMer_delete() == null && dto.getMer_pw().trim().equals(mer_pw)) {
					mer_num = dto.getMer_num();
					mer_isbrand = dto.getMer_isbrand();
					mer_company = dto.getMer_company();
					return OK;
				}else if (dto.getMer_delete() != null) { 
					return DELETE_ID;
				}else {
					return NOT_PW;
				}
			}else {
				return NOT_ID;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
}
