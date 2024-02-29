package com.oz.ozHouse.merchant.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oz.ozHouse.dto.Product_QA_DTO;
import com.oz.ozHouse.dto.Product_reQA_DTO;
import com.oz.ozHouse.dto.Order_QADTO;
import com.oz.ozHouse.dto.Order_reQADTO;
import com.oz.ozHouse.dto.ReviewDTO;;

@Service
public class BoardMapper {
	
	@Autowired
	private SqlSession sqlSession;
	
	//주문 문의
	public List<Order_QADTO> orderQAList(Map<String, String> map) {
		return sqlSession.selectList("orderQAList", map);
	}
	//상품 문의
	public List<Product_QA_DTO> productQAList(Map<String, String> map) {
		return sqlSession.selectList("productQAList", map);
	}
	//구매 후기
	public List<ReviewDTO> reviewList(Map<String, String> map) {
		return sqlSession.selectList("reviewList", map);
	}
	//구매 후기 상세보기
	public List<ReviewDTO> reviewContent(int review_num) {
		return sqlSession.selectList("reviewContent", review_num);
	}
	//상품 문의 상세보기
	public List<Product_QA_DTO> productContent(int product_QA_num) {
		return sqlSession.selectList("productContent", product_QA_num);
	}
	//주문 문의 상세보기
	public List<Order_QADTO> orderContent(int order_QA_num) {
		return sqlSession.selectList("orderContent", order_QA_num);
	}
	//상품 문의 답변하기
	public int productReQa(Map<String, Object> map) {
		return sqlSession.insert("productReQa", map);
	}
	//상품 문의 답변 상세보기
	public List<Product_reQA_DTO> productReContent(int product_reQA_num) {
		return sqlSession.selectList("productReContent", product_reQA_num);
	}
	//상품 문의 답변수정하기
	public int productReQaUpdate(Product_reQA_DTO dto) {
		return sqlSession.update("productReQaUpdate", dto);
	}
	//상품 문의 상태 수정하기
	public int productQAstate(int product_QA_num) {
		return sqlSession.update("productQAstate", product_QA_num);
	}
	//주문 문의 답변하기
	public int orderReQa(Map<String, Object> map) {
		return sqlSession.insert("orderReQa", map);
	}
	//주문 문의 답변 상세보기
	public List<Order_reQADTO> orderReContent(int order_reQA_num) {
		return sqlSession.selectList("orderReContent", order_reQA_num);
	}
	//주문 문의 답변수정하기
	public int orderReQaUpdate(Order_reQADTO dto) {
		return sqlSession.update("orderReQaUpdate", dto);
	}
	//주문 문의 상태 수정하기
	public int orderQAstate(int order_QA_num) {
		return sqlSession.update("orderQAstate", order_QA_num);
	}
	
	public int productqaCount(Map<String, String> map) {
	    return sqlSession.selectOne("productqaCount", map);
	}
	
	public int orderqaCount(Map<String, String> map) {
	    return sqlSession.selectOne("orderqaCount", map);
	}

	public int reviewqaCount(Map<String, String> map) {
	    return sqlSession.selectOne("reviewqaCount", map);
	}
}
