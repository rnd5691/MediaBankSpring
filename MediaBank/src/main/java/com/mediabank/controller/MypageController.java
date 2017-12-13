package com.mediabank.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mediabank.company.CompanyDTO;
import com.mediabank.member.MemberDTO;
import com.mediabank.mypage.MypageService;
import com.mediabank.person.PersonDTO;

@Controller
@RequestMapping("/mypage")
public class MypageController {
	@Autowired
	private MypageService mypageService;
	
	@RequestMapping("salesRequestList")
	public String salesRequestList(){
		return "MYPAGE/salesRequestList";
	}
	
	@RequestMapping("update")
	public String update(RedirectAttributes ra,MemberDTO memberDTO,PersonDTO personDTO,CompanyDTO companyDTO){
		int result=0;
		try{
			result = mypageService.update(memberDTO, personDTO, companyDTO);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(result>0){
			ra.addFlashAttribute("message", "회원 정보 수정을 성공 하셨습니다.");
		}else{
			ra.addFlashAttribute("message", "회원 정보 수정에 실패 하셨습니다.");
		}
		
		return "redirect:myinfo";
	}
	
	@RequestMapping("mypageAdd")
	public String mypageAdd(RedirectAttributes ra,Model model,HttpSession session){
		MemberDTO memberDTO = null;
		String path = null;
		try{
			memberDTO = mypageService.mypageAdd(model, session);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(memberDTO == null){
			ra.addFlashAttribute("message","잘못된 접근 방식입니다.");
			path = "redirect:../MediaBank/main";
		}else{
			path = "MYPAGE/add/"+memberDTO.getKind()+"Add";
		}
		
		return path;
	}
	@RequestMapping("myinfo")
	public String myinfo(RedirectAttributes ra,HttpSession session){
		MemberDTO memberDTO = null;
		String path = null;
		try {
			memberDTO = mypageService.myinfo(session);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(memberDTO == null){
			ra.addFlashAttribute("message","잘못된 접근 방식입니다.");
			path = "redirect:../MediaBank/main";
		}else{
			path = "MYPAGE/my_info";
		}
		
		return path;
	}
}
