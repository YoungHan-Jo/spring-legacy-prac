package com.example.mapper;

import com.example.domain.BoardVO;

public interface BoardMapper {
	
	// ============ select ==============
	int getNextNum();
	
	BoardVO getBoardByNum(int num);
	
	
	
	// ============ insert ==============
	void writeBoard(BoardVO boardVO);
	

	// ============ update ==============
	void addViewCount(int num);
	
	
	
	// ============ delete ==============
	
	
}
