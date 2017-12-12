package com.mediabank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@RequestMapping("joinAgreement")
	public String joinAgreement() {
		return "join/joinForm_agreement";
	}
}
