package com.oz.ozHouse.merchant.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oz.ozHouse.dto.NoticeDTO;

@Service
public class StoreMainMapper {
	
	@Autowired
	private SqlSession sqlSession;
	
	//관리자 홈 공지사항
	public List<NoticeDTO> noticeTitleList() {
		return sqlSession.selectList("noticeTitleList");
	}
	
	//전체 count
	public int allCount(Map map) {
		return sqlSession.selectOne("allCount",map);
	}
	
	//승인대기 count
	public int waitCount(Map map) {
		return sqlSession.selectOne("waitCount", map);
	}
	
	//승인보류 count
	public int requestCount(Map map) {
		return sqlSession.selectOne("requestCount", map);
	}
	
	//수정반려 count
	public int cancleCount(Map map) {
		return sqlSession.selectOne("cancleCount", map);
	}

	//판매중 count
	public int saleOk(Map map) {
		return sqlSession.selectOne("saleOk", map);
	}
	
	//수정취소 count
	public int requestCancle(Map map) {
		return sqlSession.selectOne("requestCancle", map);
	}
	
	//상품문의 count
	public int productCount(Map map) {
		return sqlSession.selectOne("productCount", map);
	}
	
	//주문문의 count
	public int orderCount(Map map) {
		return sqlSession.selectOne("orderCount", map);
	}
	
	//교환 count
	public int exchangeCount(Map map) {
		return sqlSession.selectOne("exchangeCount", map);
	}
	
	//환불 count
	public int returnCount(Map map) {
		return sqlSession.selectOne("returnCount", map);
	}
	
	//발송준비 count
	public int readyCount(Map map) {
		return sqlSession.selectOne("readyCount", map);
	}
	
	//배송중 count
	public int deliveryCount(Map map) {
		return sqlSession.selectOne("deliveryCount", map);
	}
	
	//배송완료 count
	public int completeCount(Map map) {
		return sqlSession.selectOne("completeCount", map);
	}
}
