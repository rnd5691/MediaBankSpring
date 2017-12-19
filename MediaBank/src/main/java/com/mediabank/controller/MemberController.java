package com.mediabank.controller;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mediabank.company.CompanyDTO;
import com.mediabank.member.MemberDTO;
import com.mediabank.member.MemberService;
import com.mediabank.person.PersonDTO;

@Controller
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	/*
	  -------------------------------------------------
	  [로그아웃]
	*/
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		session.invalidate();
		
		return "redirect:../MediaBank/main";
	}
	/*
	  -------------------------------------------------
	  [로그인]
	*/
	@RequestMapping("/login")
	public String login(RedirectAttributes ra,MemberDTO memberDTO,HttpSession session){
		String message = null;
		String path = null;
		try{
			message = memberService.login(memberDTO, session);
		}catch(Exception e){
			e.printStackTrace();
		}
		ra.addFlashAttribute("message", message);
		if(memberDTO.getKind().equals("admin")){
			path = "redirect:../qna/qnaList";
		}else{
			path = "redirect:../MediaBank/main";
		}
		
		return path;
	}
	
	/*
	  -------------------------------------------------
	  [회원가입]
	*/
	//동의 페이지
	@RequestMapping("joinAgreement")
	public String joinAgreement() {
		return "join/joinForm_agreement";
	}
	
	//계정선택 페이지
	@RequestMapping("kind")
	public String joinKind() {
		return "join/joinForm_kind";
	}
	
	//회원가입 정보 입력란 페이지
	@RequestMapping("info")
	public String joinCompany(Model model, String kind) {
		model.addAttribute("kind", kind);
		return "join/joinForm_info";
	}
	
	//계정에 맞게 입력정보 테이블이 보여지도록
	@RequestMapping("table")
	public String addTable(String kind) {
		return "join/add/"+kind+"Add";
	}
	
	//id값 체크하기
	@RequestMapping("idCheck")
	public String idCheck(Model model,String id){
		boolean check=false;
		try{
			check=memberService.idCheck(id);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(check){
			model.addAttribute("message", "사용 가능한 ID 입니다.");
		}else{
			model.addAttribute("message", "이미 사용 중인 ID 입니다.");
		}
		
		return "join/check/Check";
	}
	@RequestMapping("pwCheck")
	public String pwCheck(Model model,boolean check){
		if(check){
			model.addAttribute("message", "사용 가능한 PW 입니다.");
		}else{
			model.addAttribute("message", "비밀번호는 8자리 이상이어야 하며 공백 없이 영문과 숫자, 특수문자를 반드시 포함해야 합니다.");
		}
		
		return "join/check/Check";
	}
	@RequestMapping("pwchCheck")
	public String pwchCheck(Model model,boolean check){
		if(check){
			model.addAttribute("message", "비밀 번호가 일치 합니다.");
		}else{
			model.addAttribute("message","비밀 번호가 일치 하지 않습니다.");
		}
		
		return "join/check/Check";
	}
	//입력된 계정 회원가입 정보 저장 하기
	@RequestMapping("join")
	public String insert(RedirectAttributes ra,MemberDTO memberDTO, CompanyDTO companyDTO, PersonDTO personDTO){
		
		int result = 0;
		try{
			result = memberService.insert(memberDTO, companyDTO, personDTO);
		}catch(Exception e){

			e.printStackTrace();
		}

		if(result>0){
			ra.addFlashAttribute("message", "회원 가입에 성공 하셨습니다.");
		}else{
			ra.addFlashAttribute("message", "회원 가입에 실패 하셨습니다.");
		}
		
		return "redirect:../MediaBank/main";
	}
}
