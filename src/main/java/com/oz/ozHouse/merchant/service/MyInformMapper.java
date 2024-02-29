package com.oz.ozHouse.merchant.service;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.oz.ozHouse.dto.MerchantDTO;

@Service
public class MyInformMapper {

	@Autowired
	private SqlSession sqlSession;
	
	public MerchantDTO myInformView(int mer_num) {
		return sqlSession.selectOne("myInformView", mer_num);
	}
	
	public String selectCateName(int mer_category) {
		String cName = sqlSession.selectOne("selectCateNameMain", mer_category);
		return cName;
	}
	
	public int updateMerchant(MerchantDTO dto) {
		return sqlSession.update("updateMerchant", dto);
	}
	
	public int updatePass(Map map) {
		return sqlSession.update("updatePass", map);
	}
	
	public int memberOut(String mer_num) {
		return sqlSession.update("memberOut", mer_num);
	}
}
