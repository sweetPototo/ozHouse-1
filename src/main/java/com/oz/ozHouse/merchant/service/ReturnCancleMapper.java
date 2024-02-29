package com.oz.ozHouse.merchant.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oz.ozHouse.dto.ListDTO;

@Service
public class ReturnCancleMapper {

	@Autowired
	private SqlSession sqlSession;
	
	public List<ListDTO> returnCancleList(Map map){
		List<ListDTO> list = sqlSession.selectList("orderList", map);
		return list;
	}
	
	public List<ListDTO> searchReturnCancleList(Map map){
		List<ListDTO> list = sqlSession.selectList("searchOrderList", map);
		return list;
	}
	
	public int returnCancelCheck(Map map) {
		return sqlSession.update("returnCancelCheck", map);
	}
	
	public int countReturn(Map map) {
		return sqlSession.selectOne("countReturn", map);
	}
	
	public int countSearchReturn(Map map) {
		return sqlSession.selectOne("countSearchReturn", map);
	}
}
