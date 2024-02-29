package com.oz.ozHouse.client.service;

import org.springframework.stereotype.Service;

import com.oz.ozHouse.dto.MemberDTO;

import java.io.IOException;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class MemberMapper {
	@Autowired
	private SqlSession sqlSession;
	
	public int insertMember(MemberDTO dto) {
		return sqlSession.insert("insertMember", dto);
	}
	
	public MemberDTO getMember(String member_id) {
		return sqlSession.selectOne("getMember", member_id);
	}
	
	public int updateMember(MemberDTO dto) {
		return sqlSession.update("updateMember", dto);
	}
	
    public MemberDTO checkId(String id) {
        return sqlSession.selectOne("checkId", id);
    }
    
    public MemberDTO checkEmail(String email) {
    	return sqlSession.selectOne("checkEmail", email);
    }
    
	public void kakaoinsert(HashMap<String, String> userInfo) {
		sqlSession.insert("kakaoInsert", userInfo);
	}

	public MemberDTO findkakao(HashMap<String, String> userInfo) {
		return sqlSession.selectOne("findKakao", userInfo);
	}
	
	public int updatePasswd(MemberDTO dto) {
		return sqlSession.update("updatePasswd", dto);
	}

	public MemberDTO checkNaver(String naverEmail) {
		return sqlSession.selectOne("checkNaver", naverEmail);
	}
	
	public String checkMemberIdEmail(String member_email) {
		return sqlSession.selectOne("checkMemberIdEmail", member_email);
	}
	
	public int deleteMember(MemberDTO member) {
		return sqlSession.update("deleteMember", member);
	}
	
	public String confirmDelete(String member_id) {
		return sqlSession.selectOne("confirmDelete", member_id);
	}
	
}
