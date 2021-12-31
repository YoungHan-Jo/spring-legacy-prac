package com.example.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.MemberVO;
import com.example.mapper.MemberMapper;

@Service
@Transactional
public class MemberService {
	
	private MemberMapper memberMapper;

	public MemberService(MemberMapper memberMapper) {
		super();
		this.memberMapper = memberMapper;
	}
	
	public MemberVO getMemberById(String id) {
		return memberMapper.getMemberById(id);
	}
	
	public void insertMember(MemberVO memberVO) {
		memberMapper.insertMember(memberVO);
	}
	
	public void modifyMember(MemberVO memberVO) {
		memberMapper.modifyMember(memberVO);
	}
	
	public void modifyPasswd(String id,String newPasswd) {
		memberMapper.modifyPasswd(id, newPasswd);
	}
	
	public void deleteMemberById(String id) {
		memberMapper.deleteMemberById(id);
	}
	
	public int getCountById(String id) {
		return memberMapper.getCountById(id);
	}
	
	public List<MemberVO> getAllMembers(){
		return memberMapper.getAllMembers();
	}
	
	public int deleteMemberAndGetCount(String id) {
		return memberMapper.deleteMemberAndGetCount(id);
	}
}
