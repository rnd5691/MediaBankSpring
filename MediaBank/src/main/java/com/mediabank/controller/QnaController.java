package com.mediabank.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mediabank.member.MemberDTO;
import com.mediabank.qna.QnaDTO;
import com.mediabank.qna.QnaService;
import com.mediabank.util.ListData;

@Controller
@RequestMapping("/qna")
public class QnaController {
	@Autowired
	private QnaService qnaService;
	
	@RequestMapping(value="/qnaUpdate", method=RequestMethod.POST)
	public String qnaUpdate(RedirectAttributes ra,QnaDTO qnaDTO) {
		int result=0;
		try {
			result=qnaService.qnaUpdate(qnaDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result>0) {
			ra.addFlashAttribute("message","수정 완료 되었습니다.");
		}else {
			ra.addFlashAttribute("message","수정 실패 하였습니다.");
		}
		
		return "redirect:./qnaList";
	}
	@RequestMapping(value="/qnaUpdate", method=RequestMethod.GET)
	public String qnaUpdateForm(Model model,QnaDTO qnaDTO,HttpSession session,RedirectAttributes ra) {
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		String path=null;
		if(memberDTO==null || memberDTO.getKind().equals("admin")) {
			ra.addFlashAttribute("message", "잘못된 접근 방식입니다.");
			path = "redirect:../MediaBank/main";
		}else {
			model.addAttribute("qna",qnaDTO);
			path = "qna/qnaUpdate";
		}
		
		return path;
	}
	@RequestMapping("/qnaDelete")
	public String qnaDelete(RedirectAttributes ra,HttpSession session, int qna_seq) {
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		String path=null;
		if(memberDTO==null || memberDTO.getKind().equals("admin")) {
			ra.addFlashAttribute("message", "잘못된 접근 방식입니다.");
			path = "redirect:../MediaBank/main";
		}else {
			int result = 0;
			try {
				result = qnaService.qnaDelete(qna_seq);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(result!=-1) {
				if(result>0) {
					ra.addFlashAttribute("message", "해당 게시판을 삭제하였습니다.");
				}else {
					ra.addFlashAttribute("message", "게시판 삭제를 실패하였습니다.");
				}				
			}
			
			path = "redirect:./qnaList";
		}
		return path;
	}
	@RequestMapping("/qnaReplyUpdate")
	public String qnaReplyUpdate(RedirectAttributes ra,HttpSession session, QnaDTO qnaDTO) {
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		String path=null;
		if(memberDTO==null || !memberDTO.getKind().equals("admin")) {
			ra.addAttribute("message", "잘못된 접근 방식입니다.");
			path = "redirect:../MediaBanck/main";
		}else {
			int result = 0;
			try {
				result = qnaService.qnaReplyUpdate(qnaDTO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(result>0) {
				path = "redirect:./qnaList";
			}else {
				ra.addFlashAttribute("message", "답글 업로드에 실패 하였습니다.");		
				path = "redirect:./qnaView?qna_seq="+qnaDTO.getQna_seq();
			}	
		}
		return path;
	}
	
	@RequestMapping("/qnaView")
	public ModelAndView qnaView(HttpSession session,ModelAndView mv,int qna_seq) {
		try {
			mv = qnaService.qnaView(session, qna_seq);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mv;
	}
	
	@RequestMapping(value="write", method=RequestMethod.POST)
	public String write(Model model,QnaDTO qnaDTO,HttpSession session) {
		int result = 0;
		try {
			MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
			result = qnaService.qnaWrite(qnaDTO, memberDTO.getUser_num());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(result==0) {
			model.addAttribute("message", "Q&A 업로드에 실패 하셨습니다.");
		}
		
		return "redirect:./qnaList";
	}
	@RequestMapping(value="write", method=RequestMethod.GET)
	public ModelAndView writeForm(ModelAndView mv,HttpSession session) {
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		if(memberDTO == null || memberDTO.getKind().equals("admin")) {
			mv.addObject("message", "잘못된 접근 방식입니다.");
			if(memberDTO==null) {
				mv.setViewName("redirect:../MediaBank/main");				
			}else {
				mv.setViewName("redirect:./qnaList");
			}
		}else {
			mv.setViewName("qna/qnaWrite");
		}
		return mv;
	}
	@RequestMapping("qnaList")
	public ModelAndView selectList(HttpSession session,ListData listData, ModelAndView mv){
		try {
			mv= qnaService.selectList(session, listData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mv;
	}
}
