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

	int getCountBoardsByCri(Criteria cri);

	// join 쿼리로 게시글,첨부파일 가져오기
	BoardVO getBoardAndAttaches(int num);

	// ============ insert ==============
	void writeBoard(BoardVO boardVO);

	// ============ update ==============
	void addViewCount(int num);
	
	void updateBoard(BoardVO boardVO);

	// ============ delete ==============

	void deleteBoard(int boardNum);
	
}
