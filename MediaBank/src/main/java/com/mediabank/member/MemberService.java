package com.mediabank.member;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediabank.company.CompanyDAO;
import com.mediabank.company.CompanyDTO;
import com.mediabank.person.PersonDAO;
import com.mediabank.person.PersonDTO;

@Service
public class MemberService {
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private CompanyDAO companyDAO;
	@Autowired
	private PersonDAO personDAO;

	/*
	  -------------------------------------------------
	  [로그인]
	*/
	public String login(MemberDTO memberDTO,HttpSession session) throws Exception{
		memberDTO = memberDAO.selectOne(memberDTO);
		String message = "로그인 실패 하셨습니다.";
		if(memberDTO != null){
			session.setAttribute("member", memberDTO);
			message = memberDTO.getId()+"님 어서오세요.";
			if(!memberDTO.getKind().equals("admin")){
				String writer = null;
				if(memberDTO.getKind().equals("company")) {
					writer = companyDAO.selectWriter(memberDTO.getUser_num());
				}else {
					writer = personDAO.selectWriter(memberDTO.getUser_num());
				}
				session.setAttribute("writer", writer);	
			}
		}
		return message;
	}
	/*
	  -------------------------------------------------
	  [회원가입]
	*/
	public boolean idCheck(String id) throws Exception{
		return memberDAO.checkId(id);
	}
	
	@Transactional
	public int insert(MemberDTO memberDTO, CompanyDTO companyDTO, PersonDTO personDTO) throws Exception{
		//company계정인지 person계정인지 비교
		int result = 0;
		int user_num = memberDAO.searchUserNum();
		memberDTO.setUser_num(user_num);
		result = memberDAO.insert(memberDTO);
		if(memberDTO.getKind().equals("company")){
			companyDTO.setUser_num(user_num);
			System.out.println("2");
			result = companyDAO.insert(companyDTO);
		}else{
			personDTO.setUser_num(user_num);
			result = personDAO.insert(personDTO);
		}
		return result;
	}
	
}
