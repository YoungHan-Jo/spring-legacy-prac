package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.AttachVO;
import com.example.domain.BoardVO;
import com.example.domain.Criteria;
import com.example.mapper.AttachMapper;
import com.example.mapper.BoardMapper;

@Service
@Transactional
public class BoardService {
	
	private BoardMapper boardMapper;
	private AttachMapper attachMapper;

	public BoardService(BoardMapper boardMapper, AttachMapper attachMapper) {
		super();
		this.boardMapper = boardMapper;
		this.attachMapper = attachMapper;
	}

	public int getNextNum() {
		return boardMapper.getNextNum();
	}
	
	public void writeBoard(BoardVO boardVO) {
		boardMapper.writeBoard(boardVO);
	}
	
	public BoardVO getBoardByNum(int num) {
		return boardMapper.getBoardByNum(num);
	}
	
	public void addViewCount(int num) {
		boardMapper.addViewCount(num);
	}
	
	public List<BoardVO> getAllBoards(){
		return boardMapper.getAllBoards();
	}
	
	public List<BoardVO> getBoardsByCri(Criteria cri){
		
		int startRow = (cri.getPageNum() - 1) * cri.getAmount();
		
		cri.setStartRow(startRow);
		
		return boardMapper.getBoardsByCri(cri);
	}
	
	public int getCountBoardsByCri(Criteria cri) {
		return boardMapper.getCountBoardsByCri(cri);
	}
	
	public void addBoardAndAddAttaches(BoardVO boardVO) {
		// 게시글 DB등록
		boardMapper.writeBoard(boardVO);
		// 첨부파일 DB등록
		//List<AttachVO> attachList = boardVO.getAttachList();
		//attachMapper.addAttaches(attachList);
		attachMapper.addAttaches(boardVO.getAttachList());
		
	}
	
}
