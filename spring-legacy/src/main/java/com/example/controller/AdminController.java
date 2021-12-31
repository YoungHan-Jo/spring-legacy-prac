package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/*")
public class AdminController {

	@GetMapping("/home")
	public String adminPage() {
		
		return "admin/adminHome";
	}
	
	@GetMapping("/getOneMember")
	public String getOneMember() {
		return "admin/selectOneMember";
	}
	
	@GetMapping("/getAllMembers")
	public String getAllMembers(){
		return "admin/selectAllMembers";
	}
	
	@GetMapping("/insertMember")
	public String insertMember(){
		return "admin/insertMember";
	}
	
	@GetMapping("/deleteMember")
	public String deleteMember(){
		return "admin/deleteMember";
	}
	
	@GetMapping("/updateMember")
	public String updateMember(){
		return "admin/updateMember";
	}
	
	
	
}
