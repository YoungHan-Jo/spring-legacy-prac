package com.example.mapper;

import java.util.List;

import com.example.domain.AttachVO;

public interface AttachMapper {

	// ============ select ==============
	List<AttachVO> getAttachesByBoardNum(int boardNum);
	
	List<AttachVO> getAttachesByUuids(List<String> uuidList);
	// ============ insert ==============
	void addAttaches(List<AttachVO> attachList);
	// ============ update ==============

	// ============ delete ==============
	
	void deleteAttachesByBoardNum(int boardNum);
	
	int deleteAttachesByUuids(List<String> uuidList);
}