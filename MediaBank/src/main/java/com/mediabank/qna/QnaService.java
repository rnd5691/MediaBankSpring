package com.mediabank.qna;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.mediabank.company.CompanyDAO;
import com.mediabank.member.MemberDTO;
import com.mediabank.person.PersonDAO;
import com.mediabank.util.PageMaker;

@Service
public class QnaService {
	@Autowired
	private QnaDAO qnaDAO;
	@Autowired
	private CompanyDAO companyDAO;
	@Autowired
	private PersonDAO personDAO;
	
	public int qnaReplyUpdate(QnaDTO qnaDTO) throws Exception{
		return qnaDAO.replyUpdate(qnaDTO);
	}
	public void qnaView(Model model,int qna_seq) throws Exception{
		QnaDTO qnaDTO = qnaDAO.selectOne(qna_seq);
		model.addAttribute("qna", qnaDTO);
	}
	
	public int qnaWrite(QnaDTO qnaDTO,int user_num) throws Exception{
		return qnaDAO.insert(qnaDTO, user_num);
	}
	
	public void qnaWriteForm(Model model,MemberDTO memberDTO) throws Exception{
		String writer = null;
		if(memberDTO.getKind().equals("company")) {
			writer = companyDAO.selectWriter(memberDTO.getUser_num());
		}else {
			writer = personDAO.selectWriter(memberDTO.getUser_num());
		}
		model.addAttribute("writer", writer);
	}
	public void qnaList(Model model,HttpSession session,int curPage, String kind, String search) throws Exception{
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		if(kind.equals("writer")) {
			int company = companyDAO.kindCheck(search);
			int person = personDAO.kindCheck(search);
			
			if(company==1) {
				kind="company_name";
			}else if(person==1) {
				kind="nickname";
			}
		}
		
		int totalCount = qnaDAO.getTotalCount(kind, search);
		PageMaker pageMaker = new PageMaker(curPage, totalCount);
		
		if(memberDTO == null) {
			memberDTO = new MemberDTO();
			memberDTO.setKind("");
		}
		
		List<QnaDTO> ar = null;
		if(memberDTO.getKind().equals("admin")) {
			ar = qnaDAO.adminSelectList(pageMaker.getMakeRow(), kind, search);
		}else {
			ar = qnaDAO.selectList(pageMaker.getMakeRow(), kind, search);
		}
		
		model.addAttribute("list", ar);
		model.addAttribute("kind", kind);
		model.addAttribute("search", search);
		model.addAttribute("makePage", pageMaker.getMakePage());
	}
}
