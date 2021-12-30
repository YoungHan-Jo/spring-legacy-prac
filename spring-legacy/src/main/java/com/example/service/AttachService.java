package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.AttachVO;
import com.example.mapper.AttachMapper;

@Service
@Transactional
public class AttachService {
	
	private AttachMapper attachMapper;

	public AttachService(AttachMapper attachMapper) {
		super();
		this.attachMapper = attachMapper;
	}
	
	public List<AttachVO> getAttachesByBoardNum(int BoardNum){
		return attachMapper.getAttachesByBoardNum(BoardNum);
	}
	
	public void addAttaches(List<AttachVO> attachList) {
		attachMapper.addAttaches(attachList);
	}
	
	public List<AttachVO> getAttachesByUuids(List<String> uuidList){
		
		
		if(uuidList != null && uuidList.size() > 0) {
			return attachMapper.getAttachesByUuids(uuidList);
			
		}
		
		return null;
	}
	
}
