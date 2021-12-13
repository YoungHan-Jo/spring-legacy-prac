package com.example.mapper;

import org.apache.ibatis.annotations.Param;

import com.example.domain.MemberVO;

public interface MemberMapper {
	
	// ============ select ==============
	
	MemberVO getMemberById(String id);
	
	// ============ insert ==============
	
	void insertMember(MemberVO memberVO);
	
	// ============ update ==============
	void modifyMember(MemberVO memberVO);
	
	void modifyPasswd(
			@Param("id") String id, 
			@Param("newPasswd") String newPasswd);
	
	// ============ delete ==============
	void deleteMemberById(String id);
	
}
