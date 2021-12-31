package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import com.example.domain.MemberVO;

public interface MemberMapper {
	
	// ============ select ==============
	
	MemberVO getMemberById(String id);
	
	int getCountById(String id);
	
	List<MemberVO> getAllMembers();
	
	// ============ insert ==============
	
	void insertMember(MemberVO memberVO);
	
	// ============ update ==============
	void modifyMember(MemberVO memberVO);
	
	void modifyPasswd(
			@Param("id") String id, 
			@Param("newPasswd") String newPasswd);
	
	// ============ delete ==============
	void deleteMemberById(String id);
	
	@Delete("DELETE FROM member WHERE id = #{id} ")
	int deleteMemberAndGetCount(String id);
	
}
