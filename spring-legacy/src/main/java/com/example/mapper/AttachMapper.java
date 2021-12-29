package com.example.mapper;

import java.util.List;

import com.example.domain.AttachVO;

public interface AttachMapper {

	// ============ select ==============

	// ============ insert ==============
	void addAttaches(List<AttachVO> attachList);
	// ============ update ==============

	// ============ delete ==============
}