package com.oz.ozHouse.client.service;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oz.ozHouse.dto.*;

@Service
public class ShoppingMapper {
	@Autowired
	private SqlSession sqlSession;

	public List<ProductDTO> main_listProduct() {
		return sqlSession.selectList("main_listProduct");
	}

	public List<CategoryDTO> main_listCate() {
		return sqlSession.selectList("main_listCate");
	}

	public List<BlogDTO> main_listBlog() {
		return sqlSession.selectList("main_listBlog");
	}
	
	public List<ProductDTO> main_listTodays() {
		return sqlSession.selectList("main_listTodays");
	}

	public int main_insertScrap(HashMap<String, Integer> hm) {
		return sqlSession.insert("main_insertScrap", hm);
	}

	public ScrapDTO main_checkScrap(HashMap<String, Integer> hm) {
		return sqlSession.selectOne("main_checkScrap", hm);
	}

	public int main_deleteScrap(HashMap<String, Integer> hm) {
		return sqlSession.delete("main_deleteScrap", hm);
	}

	public int main_scrap_count(int product_num) {
		return sqlSession.selectOne("main_scrap_count", product_num);
	}
	
	public int main_review(ReviewDTO dto) {
		return sqlSession.insert("main_review", dto);
	}
	
	public int review_count(int product_num) {
		return sqlSession.selectOne("review_count", product_num);
	}
	
	public List<ReviewDTO> main_listReview(int product_num) {
		return sqlSession.selectList("main_listReview", product_num);
	}
	
	public MemberDTO shop_getMember(String member_id) {
		return sqlSession.selectOne("shop_getMember", member_id);
	}

	public List<ProductDTO> Todaysdeal(String product_spec) {
		return sqlSession.selectList("Todaysdeal", product_spec);
	}

	public List<ProductDTO> Best(String product_spec) {
		return sqlSession.selectList("Best", product_spec);
	}

	public List<ProductDTO> Category(String product_spec) {
		return sqlSession.selectList("Category", product_spec);
	}

	public ProductDTO getProduct(int product_num) {
		ProductDTO dto = sqlSession.selectOne("getProductClient", product_num);
		return dto;
	}

	public List<ProductDTO> selectBySpec(String product_spec) {
		return sqlSession.selectList("selectBySpec", product_spec);
	}

	public int insertOrder(OrderDTO dto) {
		return sqlSession.insert("insertOrder", dto);
	}

	public List<OrderDTO> getOrder(long order_code) {
		return sqlSession.selectList("getOrder", order_code);
	}

	public List<Long> getOrderCode(String member_id) {
		return sqlSession.selectList("getOrderCode", member_id);
	}

	public List<OrderDTO> getUserOrderList(String member_id) {
		return sqlSession.selectList("getUserOrderList", member_id);
	}

	public ProductDTO getOrderProduct(int product_num) {
		return sqlSession.selectOne("getOrderProductShopping", product_num);
	}

	public String getOrderStatement(long order_code) {
		return sqlSession.selectOne("getOrderStatement", order_code);
	}

	public int updateOrderProduct(ProductDTO pdto) {
		return sqlSession.update("updateOrderProduct", pdto);
	}

	public int updateMemberPoint(MemberDTO dto) {
		return sqlSession.update("updateMemberPoint", dto);
	}

	public int selectedCoupons(User_CouponDTO dto) {
		return sqlSession.update("selectedCoupons", dto);
	}

	public List<Mer_CouponDTO> selectOrderCoupon(long order_code) {
		return sqlSession.selectList("selectOrderCoupon", order_code);
	}

	public int orderCancel(long order_code) {
		return sqlSession.update("orderCancel", order_code);
	}

	public int setProductQuantity(ProductDTO dto) {
		return sqlSession.update("setProductQuantity", dto);
	}

	public List<ProductDTO> selectBestProduct() {
		return sqlSession.selectList("selectBestProduct");
	}

	public List<ProductDTO> selectTodayProduct() {
		return sqlSession.selectList("selectTodayProduct");
	}

}