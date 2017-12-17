package com.mediabank.qna;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mediabank.company.CompanyDAO;
import com.mediabank.member.MemberDAO;
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
	@Autowired
	private MemberDAO memberDAO;
	public int qnaUpdate(QnaDTO qnaDTO) throws Exception{
		return qnaDAO.update(qnaDTO);
	}
	public int qnaDelete(RedirectAttributes ra,int qna_seq) throws Exception{
		QnaDTO qnaDTO = qnaDAO.searchQna_seq(qna_seq);
		int result = 0;
		if(qnaDTO==null) {
			ra.addFlashAttribute("message", "해당 하는 번호가 존재하지 않습니다.");
			result = -1;
		}else {
			result = qnaDAO.delete(qna_seq);
		}
		return result;
	}
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
		for(QnaDTO qnaDTO : ar) {
			String member_kind = memberDAO.searchKind(qnaDTO.getUser_num());
			qnaDTO.setWriter(memberDAO.searchNickName(qnaDTO.getUser_num(), member_kind));
		}
		model.addAttribute("list", ar);
		model.addAttribute("kind", kind);
		model.addAttribute("search", search);
		model.addAttribute("makePage", pageMaker.getMakePage());
	}
}
