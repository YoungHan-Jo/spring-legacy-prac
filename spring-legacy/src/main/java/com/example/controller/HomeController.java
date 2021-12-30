package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HomeController {
	
	@GetMapping(value = {"/","/index"})
	public String home() {
		System.out.println("home() 호출됨...");
		return "index";
	}
	
	// /display?fileName=xxxxxxxxxxxxx
	@GetMapping("/display")
	@ResponseBody // 컨트롤러 메소드가 리턴하는 데이터 자체를 바로 응답으로 주고자 할 경우 사용
	public ResponseEntity<byte[]> getImageFile(String fileName) throws IOException {
		// byte배열 객체로 보내기
		System.out.println("fileName : " + fileName);

		File file = new File("C:/uploadFolder/upload", fileName);
		// "C:/uploadFolder/upload" + "yy/MM/dd/dog.jpg"
		System.out.println("실제 이미지 파일 경로 : " + file.getPath());

		HttpHeaders headers = new HttpHeaders();

		String contentType = Files.probeContentType(file.toPath());

		headers.add("Content-Type", contentType);

		// 이미지 파일을 byte배열로 변환
		byte[] imageData = FileCopyUtils.copyToByteArray(file);

		// responseEntity(보낼 body데이터, headers, 상태)
		return new ResponseEntity<byte[]>(imageData, headers, HttpStatus.OK);
	} // getImageFile

	// /download?fileName=xxxxxxxxx
	// 속성 여러개 넣으려면. 경로값에 value로 하기(원래 생략했던것)
	// produces : contentType가 없을 때 지정할 application/octet-stream으로 설정하기
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> download(String fileName) throws UnsupportedEncodingException {
		// 스트링이랑 연동이 잘되는 Resource 타입으로 보내기
		System.out.println("fileName : " + fileName);

		File file = new File("C:/uploadFolder/upload", fileName);
		// "C:/uploadFolder/upload" + "yy/MM/dd/dog.jpg"
		
		// 파일정보 resource
		Resource resource = new FileSystemResource(file);
		System.out.println("resource : " + resource);

		if (resource.exists() == false) { // 다운로드할 파일이 존재하지 않을때
			// 404코드, 자원없음 응답코드 보내고 종료.
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}

		// 다운로드 할 파일(resource)이 존재하면

		String resourceName = resource.getFilename();

		System.out.println("resourceName : " + resourceName);
		// resourceName : 1c228983-d76e-46dd-a427-897bfafc6e7a_dog.jpg
		// 앞에 uuid 지저분하기 때문에 잘라내기
		int beginIndex = resourceName.indexOf("_") + 1;
		String originFilename = resourceName.substring(beginIndex);
		System.out.println("originFilename : " + originFilename); // dog.jpg

		// 한글 파일명이면 한글 깨지는거 해결
		// 다운로드 파일명의 문자셋을 utf-8에서 iso-8859-1로 변경
		// 기존문자열을 바이트배열로 바꿔서 iso-8859-1의 문자열로 바꿔줌
		String downloadName = new String(originFilename.getBytes("utf-8"), "iso-8859-1");
		System.out.println("downloadName : " + downloadName);

		HttpHeaders headers = new HttpHeaders();
		//content-type은 애노테이션의 produces속성으로 처리함
		//headers.add("Content-Type", "application/octet-stream");
		headers.add("Content-Disposition", "attachment; filename=" + downloadName);

		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	} // download
	
	
}
