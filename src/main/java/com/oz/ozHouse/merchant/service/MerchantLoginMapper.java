package com.oz.ozHouse.merchant.service;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oz.ozHouse.dto.MemberDTO;
import com.oz.ozHouse.dto.MerchantDTO;

@Service
public class MerchantLoginMapper {
	@Autowired
	private SqlSession sqlSession;
	
	public int insertMerchant(MerchantDTO dto) {
		return sqlSession.insert("insertMerchant", dto);
	}
	
	public MerchantDTO merchant_getMember(String mer_id) {
		return sqlSession.selectOne("merchant_checkMerId", mer_id);
	}
	
    public MerchantDTO merchant_checkMerId(String id) {
        return sqlSession.selectOne("merchant_checkMerId", id);
    }
	
    public MerchantDTO merchant_checkBsNum(Map<String, String> comNum) {
    	return sqlSession.selectOne("merchant_checkBsNum", comNum);
    }
    
    public MerchantDTO merchant_checkEmail(String email) {
    	return sqlSession.selectOne("merchant_checkEmail", email);
    }
    
    public String checkMerchantIdEmail(String mer_email) {
		return sqlSession.selectOne("checkMerchantIdEmail", mer_email);
	}
}
