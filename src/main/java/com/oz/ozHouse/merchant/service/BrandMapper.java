package com.oz.ozHouse.merchant.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oz.ozHouse.dto.ApplicationDTO;
import com.oz.ozHouse.dto.CategoryDTO;
import com.oz.ozHouse.dto.InbrandDTO;
import com.oz.ozHouse.dto.MerchantDTO;

@Service
public class BrandMapper {

	@Autowired
	private SqlSession sqlSession;
	
	public MerchantDTO searchComNum(String mer_num) {
		MerchantDTO comNum = sqlSession.selectOne("selectComNum", mer_num);
		return comNum;
	}
	
	public List<CategoryDTO> selectCate(){
		List<CategoryDTO> list = sqlSession.selectList("selectCate");
		return list;
	}
	
	public int application(InbrandDTO dto) {
		int res = sqlSession.insert("application", dto);
		return res;
	}
	
	public ApplicationDTO applicationList(int mer_num) {
		ApplicationDTO dto = sqlSession.selectOne("applicationList", mer_num);
		return dto;
	}
	
	public InbrandDTO selectMer(int mer_num) {
		InbrandDTO dto = sqlSession.selectOne("selectMer", mer_num);
		return dto;
	}

	public int brandCancelUpdate(int inbrand_num) {
		int res = sqlSession.update("brandCancelUpdate", inbrand_num);
		return res;
	}
	
	public int deleteInbrand(int inbrand_num) {
		int res = sqlSession.delete("deleteInbrand", inbrand_num);
		return res;
	}
	
	public String selectCateName(int inbrand_category) {
		String cName = sqlSession.selectOne("selectCateName", inbrand_category);
		return cName;
	}
}
