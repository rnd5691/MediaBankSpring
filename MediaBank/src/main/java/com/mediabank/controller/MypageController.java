package com.mediabank.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mediabank.company.CompanyDTO;
import com.mediabank.file.FileDTO;
import com.mediabank.member.MemberDTO;
import com.mediabank.mypage.MypageService;
import com.mediabank.person.PersonDTO;
import com.mediabank.work.WorkDTO;

@Controller
@RequestMapping("/mypage")
public class MypageController {
	@Autowired
	private MypageService mypageService;
	//--------------<내 작품 승인 현황>--------------
	@RequestMapping(value="write", method=RequestMethod.POST)
	public String write(RedirectAttributes ra,MultipartHttpServletRequest request,HttpSession session,FileDTO fileDTO, WorkDTO workDTO){
		int result = 0;
		try {
			result = mypageService.write(ra, request, session, fileDTO, workDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(result != -1){
			if(result>0){
				ra.addFlashAttribute("message", "업로드에 성공하였습니다.");
			}else{
				ra.addFlashAttribute("message", "업로드에 실패하였습니다.");
			}			
		}
		
		return "redirect:salesRequestList";
	}
	@RequestMapping(value="write", method=RequestMethod.GET)
	public String writeForm(Model model,RedirectAttributes ra,HttpSession session){
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		String path = null;
		if(memberDTO==null){
			ra.addFlashAttribute("message", "잘못된 접근 방식입니다.");
			path = "redirect:../MediaBank/main";
		}else{
			try {
				mypageService.writeForm(model, memberDTO.getUser_num());
				path = "MYPAGE/salesRequestWrite";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return path;
	}
	@RequestMapping(value="salesRequestUpdate", method=RequestMethod.POST)
	public String salesRequestUpdate(RedirectAttributes ra,MultipartHttpServletRequest request,HttpSession session,FileDTO fileDTO, WorkDTO workDTO) {
		int result=0;
		try{
			result = mypageService.viewUpdate(ra, request, session, fileDTO, workDTO);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(result != -1){
			if(result>0){
				ra.addFlashAttribute("message", "업로드에 성공하였습니다.");
			}else{
				ra.addFlashAttribute("message", "업로드에 실패하였습니다.");
			}			
		}
		
		return "redirect:salesRequestList";
	}
	@RequestMapping(value="salesRequestUpdate", method=RequestMethod.GET)
	public String salesRequestUpdateForm(Model model, int work_seq) {
		try {
			mypageService.salesRequestViewForm(model, work_seq);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "MYPAGE/salesRequestViewUpdate";
	}
	@RequestMapping("salesRequestView")
	public String salesRequestView(RedirectAttributes ra,Model model,int work_seq,HttpSession session) {
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		String path = null;
		if(memberDTO==null) {
			ra.addFlashAttribute("message", "잘못된 접근 방식 입니다.");
			path = "redirect:../MediaBank/main";
		}else {
			boolean check = false;
			try {
				check=mypageService.salesRequestView(model, work_seq);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(memberDTO.getKind().equals("admin")) {
				
			}else {
				if(!check) {
					ra.addFlashAttribute("message","이미 관리가 완료 된 작품입니다.");
					path = "redirect:salesRequestList";
				}else {
					path = "MYPAGE/salesRequestView";					
				}
			}
		}
		return path;
	}
	@RequestMapping("salesRequestList")
	public String salesRequestList(RedirectAttributes ra,Model model,@RequestParam(defaultValue="1")int curPage, HttpSession session){
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		String path = null;
		if(memberDTO==null) {
			ra.addFlashAttribute("message", "잘못된 접근 방식 입니다.");
			path = "redirect:../MediaBank/main";
		}else {
			List<WorkDTO> ar = null;
			try {
				ar = mypageService.salesRequestList(model, curPage, memberDTO);
				
				model.addAttribute("list", ar);
			}catch(Exception e) {
				e.printStackTrace();
			}
			path = "MYPAGE/salesRequestList";
		}
		return path;
	}
	//------------<내 정보>---------------------
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
