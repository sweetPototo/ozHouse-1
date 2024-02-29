package com.oz.ozHouse.client.service;


import java.util.List;

import org.apache.ibatis.session.SqlSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oz.ozHouse.dto.BlogDTO;

@Service
public class BlogMapper {
	@Autowired
	private SqlSession sqlSession;
	
	public int insert_blog(BlogDTO dto) {        
		return sqlSession.insert("insertBlog", dto);
	}
	
	public List<BlogDTO> blog_list() {
		return sqlSession.selectList("blog_list");
	}
	
	public BlogDTO getBlog(int num) {
		return sqlSession.selectOne("blog_get", num);
	}
	
	public int plusReadcount(int num) {
		return sqlSession.update("plus_readcount", num);
	}

}
