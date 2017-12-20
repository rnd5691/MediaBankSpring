package com.mediabank.qna;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.mediabank.company.CompanyDAO;
import com.mediabank.member.MemberDAO;
import com.mediabank.member.MemberDTO;
import com.mediabank.person.PersonDAO;
import com.mediabank.util.ListData;
import com.mediabank.util.Pager;
import com.mediabank.util.RowNum;

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
	public int qnaDelete(int qna_seq) throws Exception{
		QnaDTO qnaDTO = qnaDAO.searchQna_seq(qna_seq);
		ModelAndView mv = new ModelAndView();
		int result = 0;
		if(qnaDTO==null) {
			mv.addObject("message", "해당 하는 번호가 존재하지 않습니다.");
			result = -1;
		}else {
			result = qnaDAO.delete(qna_seq);
		}
		return result;
	}
	public int qnaReplyUpdate(QnaDTO qnaDTO) throws Exception{
		return qnaDAO.replyUpdate(qnaDTO);
	}
	public ModelAndView qnaView(HttpSession session, int qna_seq) throws Exception{
		QnaDTO qnaDTO = qnaDAO.selectOne(qna_seq);
		
		String member_kind = memberDAO.searchKind(qnaDTO.getUser_num());
		qnaDTO.setWriter(memberDAO.searchNickName(qnaDTO.getUser_num(), member_kind));;
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("qna", qnaDTO);
		mv.setViewName("qna/qnaView");
		
		return mv;
	}
	
	public int qnaWrite(QnaDTO qnaDTO,int user_num) throws Exception{
		qnaDTO.setUser_num(user_num);
		return qnaDAO.insert(qnaDTO);
	}
	
	public ModelAndView selectList(HttpSession session,ListData listData) throws Exception{
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		ModelAndView mv = new ModelAndView();
		RowNum rowNum = listData.makeRow(10);
		int totalCount = qnaDAO.getTotalCount(rowNum);
		Pager pager = listData.makePage(totalCount);
		if(rowNum.getKind().equals("writer")){
			int company = companyDAO.kindCheck(rowNum.getSearch());
			int person = personDAO.kindCheck(rowNum.getSearch());
			
			if(company==1){
				pager.setKind("company_name");
				rowNum.setKind("company_name");
			}else if(person==1){
				pager.setKind("nickname");
				rowNum.setKind("nickname");
			}
		}
		
		
		if(memberDTO == null) {
			memberDTO = new MemberDTO();
			memberDTO.setKind("");
		}
		
		List<QnaDTO> ar = null;
		if(memberDTO.getKind().equals("admin")) {
			ar = qnaDAO.adminSelectList(rowNum);
		}else {
			ar = qnaDAO.selectList(rowNum);
		}
		for(QnaDTO qnaDTO : ar){
			String member_kind = memberDAO.searchKind(qnaDTO.getUser_num());
			qnaDTO.setWriter(memberDAO.searchNickName(qnaDTO.getUser_num(), member_kind));
		}
		mv.addObject("list", ar);
		mv.addObject("pager", pager);
		mv.setViewName("qna/qnaList");
		
		return mv;
	}
}
