package com.mediabank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@RequestMapping("joinAgreement")
	public String joinAgreement() {
		return "join/joinForm_agreement";
	}
	
	@RequestMapping("kind")
	public String joinKind() {
		return "join/joinForm_kind";
	}
	
	@RequestMapping("company")
	public String joinCompany(Model model) {
		model.addAttribute("kind", "company");
		return "join/joinForm_info";
	}
	@RequestMapping("person")
	public String joinPerson(Model model) {
		model.addAttribute("kind", "person");
		return "join/joinForm_info";
	}
	@RequestMapping("table")
	public String addTable(String kind) {
		return "join/add/"+kind+"Add";
	}
}
