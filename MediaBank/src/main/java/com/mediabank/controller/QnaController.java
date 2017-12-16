package com.mediabank.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mediabank.member.MemberDTO;
import com.mediabank.qna.QnaDTO;
import com.mediabank.qna.QnaService;

@Controller
@RequestMapping("/qna")
public class QnaController {
	@Autowired
	private QnaService qnaService;
	
	@RequestMapping("qnaReplyUpdate")
	public String qnaReplyUpdate(RedirectAttributes ra,HttpSession session, QnaDTO qnaDTO) {
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		String path=null;
		if(memberDTO==null || !memberDTO.getKind().equals("admin")) {
			ra.addAttribute("message", "잘못된 접근 방식입니다.");
			path = "redirect:MediaBanck/main";
		}else {
			int result = 0;
			try {
				result = qnaService.qnaReplyUpdate(qnaDTO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(result>0) {
				path = "redirect:qnaList";
			}else {
				ra.addFlashAttribute("message", "답글 업로드에 실패 하였습니다.");		
				path = "redirect:qnaView?qna_seq="+qnaDTO.getQna_seq();
			}	
		}
		return path;
	}
	
	@RequestMapping("qnaView")
	public String qnaView(Model model,int qna_seq) {
		try {
			qnaService.qnaView(model, qna_seq);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "qna/qnaView";
	}
	
	@RequestMapping(value="write", method=RequestMethod.POST)
	public String write(RedirectAttributes ra,QnaDTO qnaDTO,HttpSession session) {
		int result = 0;
		try {
			MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
			result = qnaService.qnaWrite(qnaDTO, memberDTO.getUser_num());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(result==0) {
			ra.addFlashAttribute("message", "Q&A 업로드에 실패 하셨습니다.");
		}
		
		return "redirect:qnaList";
	}
	@RequestMapping(value="write", method=RequestMethod.GET)
	public String writeForm(Model model,HttpSession session,RedirectAttributes ra) {
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		String path=null;
		if(memberDTO == null || memberDTO.getKind().equals("admin")) {
			ra.addFlashAttribute("message", "잘못된 접근 방식입니다.");
			if(memberDTO==null) {
				path = "redirect:../MediaBank/main";				
			}else {
				path = "redirect:../qna/qnaList";
			}
		}else {
			try {
				qnaService.qnaWriteForm(model, memberDTO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			path = "qna/qnaWrite";
		}
		return path;
	}
	@RequestMapping("qnaList")
	public String qnaList(HttpSession session,Model model,@RequestParam(defaultValue="1")int curPage, @RequestParam(defaultValue="title")String kind, @RequestParam(defaultValue="")String search) {
		try {
			qnaService.qnaList(model, session, curPage, kind, search);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return "qna/qnaList";
	}
}
