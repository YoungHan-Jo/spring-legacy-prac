package com.example.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.BoardVO;
import com.example.domain.Criteria;
import com.example.domain.PageDTO;
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
	public String boardList(Criteria cri, Model model) {
		
		System.out.println("cri : " + cri);
		
		List<BoardVO> boardList = boardService.getBoardsByCri(cri);
		
		int totalCount = boardService.getCountBoardsByCri(cri);
		
		System.out.println("totalCount : " + totalCount);
		
		PageDTO pageDTO = new PageDTO(cri, totalCount);
		
		model.addAttribute("boardList", boardList);
		model.addAttribute("pageMaker", pageDTO);
		
		return "board/boardList";
	}

	@GetMapping("/write")
	public String writeForm() {

		return "board/writeBoard";
	}

	@PostMapping("/write")
	public String write(BoardVO boardVO, HttpSession session, RedirectAttributes rttr) {

		System.out.println("수정 전 boardVO : " + boardVO);

		// 1. 새 글 번호 추가
		int nextNum = boardService.getNextNum();
		boardVO.setNum(nextNum);

		// 2. 글쓴이 추가
		String id = (String) session.getAttribute("id");
		boardVO.setMemberId(id);

		// 3. 조회수 0으로 세팅
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

		// 리다이렉트로 글 번호, 글목록 페이지 번호 전달하기
		// 리다이렉트로 보내기 위해, RedirectAttributes rttr 사용
		rttr.addAttribute("num", boardVO.getNum());
		rttr.addAttribute("pageNum", 1); // 새글 등록하면 1페이지에 등록 되기때문에

		return "redirect:/board/content"; // 리다이렉트 방식으로 GetMapping("/content")에 보내기
	}

	@GetMapping("/content")
	public String boardContent(Criteria cri,int num, @RequestParam(required = false, defaultValue = "1") String pageNum,
			Model model) {
		
		System.out.println("cri : " + cri);
		
		// num, pageNum 값 잘 가져왔는지 체크
		System.out.println("num : " + num);
		System.out.println("pageNum: " + pageNum);

		// num에 해당하는 글 DB로 조회수 +1 증가시켜서 수정하기
		boardService.addViewCount(num);

		// num에 해당하는 글 정보 DB에서 가져오기
		BoardVO boardVO = boardService.getBoardByNum(num);

		System.out.println("boardVO : " + boardVO);
		
		// 프론트로 던질 값 싣기
		model.addAttribute("board", boardVO);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("cri", cri);

		// board폴더 밑에 boardContent.jsp파일(화면)로 보내기
		return "board/boardContent";
	}

}
