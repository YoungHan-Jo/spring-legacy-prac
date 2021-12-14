package com.example.mapper;

import com.example.domain.BoardVO;

public interface BoardMapper {
	
	// ============ select ==============
	
	int getNextNum();
	
	
	
	// ============ insert ==============
	void writeBoard(BoardVO boardVO);
	

	// ============ update ==============
	
	
	// ============ delete ==============
	
	
}
