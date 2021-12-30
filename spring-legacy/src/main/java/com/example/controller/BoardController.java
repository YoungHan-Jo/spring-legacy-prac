package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.AttachVO;
import com.example.domain.BoardVO;
import com.example.domain.Criteria;
import com.example.domain.PageDTO;
import com.example.service.AttachService;
import com.example.service.BoardService;

import net.coobird.thumbnailator.Thumbnailator;

@Controller
@RequestMapping("/board/*")
public class BoardController {

	private final String BASE_PATH = "C:/uploadFolder/upload";

	private BoardService boardService;
	private AttachService attachService;

	public BoardController(BoardService boardService, AttachService attachService) {
		super();
		this.boardService = boardService;
		this.attachService = attachService;
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

	// "년/월/일' 형식의 폴더명을 리턴하는 메소드
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		String str = sdf.format(new Date());

		return str;
	}// getFolder

	// 이미지 파일인지 체크
	private boolean checkImageType(File file) throws IOException {
		boolean isImage = false;

		String contentType = Files.probeContentType(file.toPath()); // "image/jpg" "image/png" 등으로 리턴함.
		System.out.println("contentType : " + contentType);

		isImage = contentType.startsWith("image"); // image로 시작할 때 true로 리턴

		return isImage;
	} // checkImageType

	private List<AttachVO> uploadFilesAndGetAttachList(List<MultipartFile> files, int boardNum)
			throws IllegalStateException, IOException {

		List<AttachVO> attachList = new ArrayList<AttachVO>();

		// null 체크
		if (files == null || files.size() == 0) {
			System.out.println("첨부파일 없음 ...");
			return attachList;
		}

		File uploadPath = new File(BASE_PATH, getFolder());
		// getFolder() -> "yyyy/MM/dd" 형식의 String 으로 리턴
		// uploadPath = "C:/uploadFile/upload/2021/12/29"

		// 폴더 경로가 존재 하는지 체크
		if (uploadPath.exists() == false) { // 경로가 존재하지 않을때
			uploadPath.mkdirs(); // 경로를 생성해줌
		}

		// files 에 있는 파일 하나씩 꺼내서 처리하기
		for (MultipartFile multipartFile : files) {
			if (multipartFile.isEmpty()) { // 파일이 비어있으면 다음 파일로 넘어가기
				continue;
			}

			String originalFilename = multipartFile.getOriginalFilename();

			UUID uuid = UUID.randomUUID();
			String uploadFilename = uuid.toString() + "_" + originalFilename;

			File file = new File(uploadPath, uploadFilename);
			// file = "C:/uploadFile/upload/2021/12/29" + "/" +
			// "ajskdfo2kgixldfijkwle_a.jpg"

			multipartFile.transferTo(file); // 경로에 맞게 파일 생성

			// 첨부파일이 이미지일 경우
			boolean isImage = checkImageType(file);
			if (isImage == true) { // 이미지 파일일 때 파일이름에 s_ 붙여서 썸네일 파일 추가로 만들기
				File outFile = new File(uploadPath, "s_" + uploadFilename);

				// "C:/uploadFile/upload/2021/12/29" + "s_ajskdfo2kgixldfijkwle_a.jpg"

				Thumbnailator.createThumbnail(file, outFile, 100, 100);
			}

			AttachVO attachVO = new AttachVO();
			attachVO.setUuid(uuid.toString());
			attachVO.setUploadpath(getFolder());
			attachVO.setFilename(originalFilename);
			attachVO.setFiletype(isImage ? "I" : "O");
			attachVO.setBoardNum(boardNum);

			attachList.add(attachVO);

		} // for

		return attachList;
	} // uploadFilesAndGetAttachList

	@PostMapping("/write")
	public String write(List<MultipartFile> files, BoardVO boardVO, HttpSession session, RedirectAttributes rttr)
			throws IllegalStateException, IOException {

		System.out.println("수정 전 boardVO : " + boardVO);

		// 1. 새 글 번호 추가
		int nextNum = boardService.getNextNum();
		boardVO.setNum(nextNum);

		List<AttachVO> attachList = uploadFilesAndGetAttachList(files, nextNum);

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

		boardVO.setAttachList(attachList);

		System.out.println("수정 후 boardVO : " + boardVO);

		// DB에 등록하기
		// boardService.writeBoard(boardVO);

		// DB에 게시글, 첨부파일 등록하기
		boardService.addBoardAndAddAttaches(boardVO);

		// 리다이렉트로 글 번호, 글목록 페이지 번호 전달하기
		// 리다이렉트로 보내기 위해, RedirectAttributes rttr 사용
		rttr.addAttribute("num", boardVO.getNum());
		rttr.addAttribute("pageNum", 1); // 새글 등록하면 1페이지에 등록 되기때문에

		return "redirect:/board/content"; // 리다이렉트 방식으로 GetMapping("/content")에 보내기
	}

	@GetMapping("/content")
	public String boardContent(Criteria cri, int num,
			@RequestParam(required = false, defaultValue = "1") String pageNum, Model model) {

		System.out.println("cri : " + cri);

		// num, pageNum 값 잘 가져왔는지 체크
		System.out.println("num : " + num);
		System.out.println("pageNum: " + pageNum);

		// num에 해당하는 글 DB로 조회수 +1 증가시켜서 수정하기
		boardService.addViewCount(num);

		// num에 해당하는 글 정보 DB에서 가져오기
		// BoardVO boardVO = boardService.getBoardByNum(num);

		// num에 해당하는 글+첨부파일 DB에서 가져오기
		BoardVO boardVO = boardService.getBoardAndAttaches(num);

		System.out.println("boardVO : " + boardVO);

		// 프론트로 던질 값 싣기
		model.addAttribute("board", boardVO);
		// attachList 분리시켜서 던져도 됨
		// model.addAttribute("attachList", boardVO.getAttachList());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("cri", cri);

		// board폴더 밑에 boardContent.jsp파일(화면)로 보내기
		return "board/boardContent";
	}

	// 첨부파일 삭제하는 메소드
	private void deleteAttachFiles(List<AttachVO> attachList) {
		// 삭제할 파일정보가 없으면 메소드 종료
		if (attachList == null || attachList.size() == 0) {
			System.out.println("삭제할 첨부파일 정보가 없습니다...");
			return;
		}

		for (AttachVO attachVO : attachList) {
			String uploadpath = BASE_PATH + "/" + attachVO.getUploadpath();
			String filename = attachVO.getUuid() + "_" + attachVO.getFilename();

			File file = new File(uploadpath, filename);
			// C:uploadFolder/upload/2021/12/29/asdljkfja;sof_dog.jpg"
			if (file.exists()) { // 해당경로에 첨부파일이 존재하면
				file.delete();
			}
			// 첨부파일이 이미지일 경우 썸네일 이미지도 삭제
			if (attachVO.getFiletype().equals("I")) {
				File thumbnailFile = new File(uploadpath, "s_" + filename);
				if (thumbnailFile.exists()) {
					thumbnailFile.delete();
				}
			}
		} // for
	} // deleteAttachFiles

	@PostMapping("/remove")
	public String removeBoardAndAttaches(int boardNum, String pageNum, RedirectAttributes rttr) {

		System.out.println("==========================remove=============================");
		
		System.out.println("boardNum : " + boardNum);

		List<AttachVO> attachList = attachService.getAttachesByBoardNum(boardNum);

		System.out.println("attachList : " + attachList);

		// 실제 첨부파일 삭제
		deleteAttachFiles(attachList);

		// 게시글, 첨부파일 DB에서 삭제
		boardService.deleteBoardAndAttaches(boardNum);
		
		
		rttr.addAttribute("pageNum", pageNum);

		return "redirect:/board/list";
	}

	@GetMapping("/modify")
	public String modifyForm(int boardNum, @ModelAttribute("pageNum") String pageNum, Model model) {

		System.out.println("boardNum : " + boardNum);

		// 게시글 번호에 해당하는 게시글+첨부파일 가져오기
		BoardVO boardVO = boardService.getBoardAndAttaches(boardNum);

		model.addAttribute("board", boardVO);

		// model.addAttribute("pageNum", pageNum);
		// @ModelAttribute("pageNum") String pageNum로 생략 가능

		return "board/modifyBoard";
	}

	@PostMapping("/modify")
	public String modify(List<MultipartFile> files, BoardVO boardVO,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(name = "delfile", required = false) List<String> delUuidList, HttpServletRequest request,
			RedirectAttributes rttr) throws IllegalStateException, IOException {

		System.out.println("==============/modify====================");

		System.out.println("boardVO : " + boardVO);
		System.out.println("pageNum : " + pageNum);
		System.out.println("delUuidList : " + delUuidList);

		// 실제 첨부파일 삭제
		List<AttachVO> delAttachList = attachService.getAttachesByUuids(delUuidList);
		System.out.println("delAttachList : " + delAttachList);
		deleteAttachFiles(delAttachList);

		// 새로운 첨부 파일 업로드 후 attachList 가져오기
		List<AttachVO> newAttachList = uploadFilesAndGetAttachList(files, boardVO.getNum());

		// DB에서 게시글 수정 / 새 파일 추가 / 삭제할 파일 삭제
		boardService.updateBoardAndInsertAttachesAndDeleteAttaches(boardVO, newAttachList, delUuidList);

		// 리다이렉트 쿼리스트링 정보 설정
		rttr.addAttribute("num", boardVO.getNum());
		rttr.addAttribute("pageNum", pageNum);

		// 상세보기 화면으로 리다이렉트 이동
		return "redirect:/board/content";
	}

}
