package com.oz.ozHouse.client.service;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oz.ozHouse.dto.Mer_CouponDTO;
import com.oz.ozHouse.dto.OrderDTO;
import com.oz.ozHouse.dto.ProductDTO;
import com.oz.ozHouse.dto.ReviewDTO;
import com.oz.ozHouse.dto.ScrapDTO;
import com.oz.ozHouse.dto.SearchDTO;
import com.oz.ozHouse.dto.User_CouponDTO;

@Service
public class MypageMapper {
	@Autowired
	private SqlSession sqlSession;
	
	public List<ProductDTO> getOrderProduct(int member_num){
		return sqlSession.selectList("getOrderProduct", member_num);
	}
	
	public List<ReviewDTO> getMyReview(int member_num){
		return sqlSession.selectList("getMyReview", member_num);
	}
	
	public int insertReview(ReviewDTO dto) {
		return sqlSession.insert("insertReview", dto);
	}
	
	public ReviewDTO getReview(int review_num){
		return sqlSession.selectOne("getReview", review_num);
	}
	
	public List<Mer_CouponDTO> getUserCoupon(int member_num){
		return sqlSession.selectList("getUserCoupon", member_num);
	}
	
	public List<Mer_CouponDTO> getMerCouponList(int member_num) {
		return sqlSession.selectList("getMerCouponList", member_num);
	}
	
	public int insertUserCoupon(User_CouponDTO dto) {
		return sqlSession.insert("insertUserCoupon", dto);
	}
	
	public List<ProductDTO> getAllProduct() {
		return sqlSession.selectList("getAllProduct");
	}
	
	public int insertScrap(HashMap<String, Integer> hm) {
		return sqlSession.insert("insertScrap", hm);
	}
	
	public ScrapDTO checkScrap(HashMap<String, Integer> hm) {
		return sqlSession.selectOne("checkScrap", hm);
	}
	
	public List<ProductDTO> getMyScrap(int member_num) {
		return sqlSession.selectList("getMyScrap", member_num);
	}
	
	public int deleteScrap(HashMap<String, Integer> hm) {
		return sqlSession.delete("deleteScrap", hm);
	}
	
	public int couponCount(int member_num) {
		return sqlSession.selectOne("couponCount", member_num);
	}
	
	public int getProductPoint(int product_num) {
		return sqlSession.selectOne("getProductPoint", product_num);
	}
	
	public List<Long> getOrderCodeSearch(SearchDTO search){
		return sqlSession.selectList("getOrderCodeSearch", search);
	}
	
	public List<OrderDTO> getOrderListSearch(List<Long> order_code_list){
		return sqlSession.selectList("getOrderListSearch", order_code_list);
	}
}
