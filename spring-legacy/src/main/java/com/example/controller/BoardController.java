package com.example.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.BoardVO;
import com.example.service.BoardService;

@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	private BoardService boardService;

	public BoardController(BoardService boardService) {
		super();
		this.boardService = boardService;
	}

	@GetMapping("/list")
	public String boardList() {
		return "board/boardList";
	}
	
	@GetMapping("/write")
	public String writeForm() {
		
		return "board/writeBoard";
	}
	
	@PostMapping("/write")
	public String write(BoardVO boardVO, HttpSession session) {
		
		System.out.println("수정 전 boardVO : " + boardVO);
		
		// 1. 새 글 번호 추가
		int nextNum = boardService.getNextNum();
		boardVO.setNum(nextNum);
		
		// 2. 글쓴이 추가
		String id = (String) session.getAttribute("id");
		boardVO.setMemberId(id);
		
		// 3. 조회수 0
		boardVO.setViewCount(0);
		
		// 4. 글 쓴 날짜 추가
		boardVO.setRegDate(new Date());
		
		// reRef, reLev, reSeq 설정
		boardVO.setReRef(nextNum);
		boardVO.setReLev(0);
		boardVO.setReSeq(0);
		
		System.out.println("수정 후 boardVO : " + boardVO);
		
		// DB에 등록하기
		boardService.writeBoard(boardVO);
		
		
		return "";
	}
	
}
