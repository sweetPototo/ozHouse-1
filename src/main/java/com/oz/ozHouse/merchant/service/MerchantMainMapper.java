package com.oz.ozHouse.merchant.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oz.ozHouse.dto.MerchantDTO;
import com.oz.ozHouse.dto.NoticeDTO;

@Service
public class MerchantMainMapper {
	
	@Autowired
	private SqlSession sqlSession;
	
	public List<NoticeDTO> noticeMainList() {
		return sqlSession.selectList("noticeMainList");
	}
	
	public List<NoticeDTO> noticeDetail(int notice_num) {
		return sqlSession.selectList("noticeDetail", notice_num);
	}
	
	public List<NoticeDTO> noticeList() {
		return sqlSession.selectList("noticeList");
	}
	
	public List<NoticeDTO> findNotice(String notice_title) {
		return sqlSession.selectList("findNotice", notice_title);
	}
}
