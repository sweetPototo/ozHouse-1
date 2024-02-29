package com.oz.ozHouse.merchant.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oz.ozHouse.dto.CategoryDTO;
import com.oz.ozHouse.dto.InbrandDTO;
import com.oz.ozHouse.dto.NoticeDTO;
import com.oz.ozHouse.dto.ProductDTO;
import com.oz.ozHouse.dto.RequestProductDTO;

@Service
public class ProductManagementMapper {
	
	@Autowired
	private SqlSession sqlSession;

	
	//category 
	public List<CategoryDTO> merchant_listCate() {
		return sqlSession.selectList("merchant_listCate");
	}
	
	public InbrandDTO merchant_getInbrandByMerNum(String mer_num) {
	    return sqlSession.selectOne("merchant_getInbrandByMerNum", mer_num);
	}
	
	public CategoryDTO merchant_getCategoryByNum(int category_num) {
	    return sqlSession.selectOne("merchant_getCategoryByNum", category_num);
	}
	
	public void processInbrandCategories(String mer_num) {
	    InbrandDTO inbrand = merchant_getInbrandByMerNum(mer_num);
	    if (inbrand != null) {
	        String[] categoryNums = inbrand.getInbrand_category().split(",");
	        for (String numStr : categoryNums) {
	            try {
	                int num = Integer.parseInt(numStr.trim());
	                CategoryDTO category = merchant_getCategoryByNum(num);
	                if (category != null) {
	                    System.out.println("Category Num: " + category.getCategory_num() + ", Name: " + category.getCategory_name());
	                }
	            } catch (NumberFormatException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	//�긽�뭹�벑濡�
	public int merchant_insertProduct(ProductDTO dto) {
		return sqlSession.insert("merchant_insertProduct", dto);
	}
	
	public int merchant_listCount(Map<String, String> map) {
	    return sqlSession.selectOne("merchant_listCount", map);
	}

	public int merchant_requestListCount(Map<String, String> map) {
	    return sqlSession.selectOne("merchant_requestListCount", map);
	}
	
	//�긽�뭹議고쉶
	public List<ProductDTO> merchant_listProduct(Map<String, String> map) {
		return sqlSession.selectList("merchant_listProduct", map);
	}
	
	public ProductDTO merchant_getProduct(String product_num) {
		java.util.Map<String, String> map = new java.util.Hashtable<>();
		map.put("key", "product_num");
		map.put("value", String.valueOf(product_num));
		return sqlSession.selectOne("merchant_getProduct", map);
	}
	
	public RequestProductDTO merchant_getreProduct(String product_num) {
		java.util.Map<String, String> map = new java.util.Hashtable<>();
		map.put("key", "product_num");
		map.put("value", String.valueOf(product_num));
		return sqlSession.selectOne("merchant_getreProduct", map);
	}

	public int merchant_updateImage(Map<String, String> map) {
		return sqlSession.update("merchant_updateImage", map);
	}

	public int merchant_updateproduct(RequestProductDTO dto) {
		return sqlSession.insert("merchant_updateproduct", dto);
	}
	
	public int merchant_updateProductInfo(RequestProductDTO dto) {
		return sqlSession.insert("merchant_updateProductInfo",dto);
	}
	
	//�닔�젙
	public int merchant_updateRe(String product_num) {
		return sqlSession.update("merchant_updateRe", product_num);
	}
	//�긽�뭹�닔�젙 �뤌
	public int merchant_insertReProduct(RequestProductDTO dto) {
		return sqlSession.insert("merchant_insertReProduct", dto);
	}
	//�궘�젣 �슂泥�
	public int merchant_deleteRe(int productNum) {
		return sqlSession.update("merchant_deleteRe", productNum);
	}
	
	public int merchant_requestRe(String productNum) {
		return sqlSession.update("merchant_requestRe", productNum);
	}

	//�닔�젙�슂泥� 由ъ뒪�듃
	public List<ProductDTO> merchant_requestListProduct(Map<String, String> map) {
		return sqlSession.selectList("merchant_requestListProduct", map);
	}
	//�벑濡� 痍⑥냼
	public int merchant_fcancle(Map<String, String> params) {
		return sqlSession.delete("merchant_fcancle", params);
	}
	//�궘�젣 痍⑥냼
	public int merchant_dcancle(Map<String, String> params) {
		return sqlSession.update("merchant_dcancle", params);
	}
	//�닔�젙 痍⑥냼
	public int merchant_ucancle(String product_num) {
		return sqlSession.update("merchant_ucancle", product_num);
	}

	public int merchant_insertimage(RequestProductDTO dto) {
		return sqlSession.insert("merchant_insertimage", dto);
	}
	
	public String merchant_msgString(int product_num) {
		return sqlSession.selectOne("merchant_msgView", product_num);
	}
	
	public int merchant_deleteProduct(int product_num) {
		return sqlSession.delete("merchant_deleteProduct", product_num);
	}
	
	public int merchant_deletereProduct(String productNum) {
		return sqlSession.delete("merchant_deletereProduct", productNum);
	}
	
	public int merchant_deletemsg(int product_num) {
		return sqlSession.delete("merchant_deletemsg", product_num);
	}
	
	//�옱怨좊━�뒪�듃
	public List<ProductDTO> merchant_stockListProduct(Map<String, Object> map) {
		return sqlSession.selectList("merchant_stockListProduct", map);
	}
	
	//�옱怨좊━�뒪�듃 count
	public int merchant_stockListCount(Map<String, Object> map) {
	    return sqlSession.selectOne("merchant_stockListCount", map);
	}
	
	//�옱怨� �닔�젙
	public int merchant_updateStock(Map<String, String> params) {
		return sqlSession.update("merchant_updateStock", params);
	}
}
