package com.oz.ozHouse.merchant.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oz.ozHouse.dto.ListDTO;

@Service
public class DeliveryMapper {
	
	@Autowired
	private SqlSession sqlSession;
	
	public List<ListDTO> deliveryList(Map map){
		List<ListDTO> list = sqlSession.selectList("deliveryAllList", map);
		return list;
	}
	
	public List<ListDTO> deliveryLikeList(Map map){
		List<ListDTO> list = sqlSession.selectList("deliveryLikeList", map);
		return list;
	}
	
	public List<ListDTO> searchDeliveryList(Map map){
		List<ListDTO> list = sqlSession.selectList("searchDeliveryList", map);
		return list;
	}
	
	public int countDelivery(Map map) {
		return sqlSession.selectOne("countDelivery", map);
	}
	
	public int countDeliveryLike(Map map) {
		return sqlSession.selectOne("countDeliveryLike", map);
	}
	
	public int countSearchDelivery(Map map) {
		return sqlSession.selectOne("countSearchDelivery", map);
	}
}
