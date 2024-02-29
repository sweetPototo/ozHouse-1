package com.oz.ozHouse.merchant.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oz.ozHouse.dto.Mer_CouponDTO;

@Service
public class CouponMapper {

	@Autowired
	private SqlSession sqlSession;
	
	public int mer_couponInsert(Mer_CouponDTO dto) {
		int res = sqlSession.insert("mer_couponInsert", dto);
		return res;
	}
	
	public int mer_deletemsg(int mer_couponnum) {
		return sqlSession.delete("mer_deletecoumsg", mer_couponnum);
	}
	
	public String mer_msgString(int mer_couponnum) {
		return sqlSession.selectOne("mer_msgCouView", mer_couponnum);
	}
	
	public List<Mer_CouponDTO> mer_couponList(int mer_num){
		List<Mer_CouponDTO> list = sqlSession.selectList("mer_couponList", mer_num);
		return list;
	}
	
	public List<Mer_CouponDTO> mer_searchCouponList(Map map){
		List<Mer_CouponDTO> list = sqlSession.selectList("mer_searchCouponList", map);
		return list;
	}
	
	public int mer_couponDelete(int mer_couponnum) {
		int res = sqlSession.delete("mer_couponDelete", mer_couponnum);
		return res;
	}
	
	public int mer_couponCount(int mer_num) {
		int couponCount = sqlSession.selectOne("mer_couponCount", mer_num);
		return couponCount;
	}
	
	public int mer_couponSearchCount(Map map) {
		int couponSearchCount = sqlSession.selectOne("mer_couponSearchCount", map);
		return couponSearchCount;
	}
}
