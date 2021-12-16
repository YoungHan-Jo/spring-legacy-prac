package com.example.mapper;

import java.util.List;

import com.example.domain.BoardVO;
import com.example.domain.Criteria;

public interface BoardMapper {
	
	// ============ select ==============
	int getNextNum();
	
	BoardVO getBoardByNum(int num);
	
	List<BoardVO> getAllBoards();
	
	List<BoardVO> getBoardsByCri(Criteria cri);
	
	
	// ============ insert ==============
	void writeBoard(BoardVO boardVO);
	

	// ============ update ==============
	void addViewCount(int num);
	
	
	
	// ============ delete ==============
	
	
}
